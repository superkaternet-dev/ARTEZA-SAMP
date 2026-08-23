/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.utilities.NodeSizeEstimator;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.ChildrenNode;
import com.google.firebase.database.snapshot.LeafNode;
import com.google.firebase.database.snapshot.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class CompoundHash {
    private final List<String> hashes;
    private final List<Path> posts;

    private CompoundHash(List<Path> list, List<String> list2) {
        if (list.size() == list2.size() - 1) {
            this.posts = list;
            this.hashes = list2;
            return;
        }
        throw new IllegalArgumentException("Number of posts need to be n-1 for n hashes in CompoundHash");
    }

    public static CompoundHash fromNode(Node node) {
        return CompoundHash.fromNode(node, new SimpleSizeSplitStrategy(node));
    }

    public static CompoundHash fromNode(Node node, SplitStrategy object) {
        if (node.isEmpty()) {
            return new CompoundHash(Collections.<Path>emptyList(), Collections.singletonList(""));
        }
        object = new CompoundHashBuilder((SplitStrategy)object);
        CompoundHash.processNode(node, (CompoundHashBuilder)object);
        ((CompoundHashBuilder)object).finishHashing();
        return new CompoundHash(((CompoundHashBuilder)object).currentPaths, ((CompoundHashBuilder)object).currentHashes);
    }

    private static void processNode(Node node, CompoundHashBuilder object) {
        block4: {
            block5: {
                block3: {
                    block2: {
                        if (!node.isLeafNode()) break block2;
                        ((CompoundHashBuilder)object).processLeaf((LeafNode)node);
                        break block3;
                    }
                    if (node.isEmpty()) break block4;
                    if (!(node instanceof ChildrenNode)) break block5;
                    ((ChildrenNode)node).forEachChild(new ChildrenNode.ChildVisitor((CompoundHashBuilder)object){
                        final CompoundHashBuilder val$state;
                        {
                            this.val$state = compoundHashBuilder;
                        }

                        @Override
                        public void visitChild(ChildKey childKey, Node node) {
                            this.val$state.startChild(childKey);
                            CompoundHash.processNode(node, this.val$state);
                            this.val$state.endChild();
                        }
                    }, true);
                }
                return;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Expected children node, but got: ");
            ((StringBuilder)object).append(node);
            throw new IllegalStateException(((StringBuilder)object).toString());
        }
        throw new IllegalArgumentException("Can't calculate hash on empty node!");
    }

    public List<String> getHashes() {
        return Collections.unmodifiableList(this.hashes);
    }

    public List<Path> getPosts() {
        return Collections.unmodifiableList(this.posts);
    }

    static class CompoundHashBuilder {
        private final List<String> currentHashes;
        private Stack<ChildKey> currentPath = new Stack();
        private int currentPathDepth;
        private final List<Path> currentPaths = new ArrayList<Path>();
        private int lastLeafDepth = -1;
        private boolean needsComma = true;
        private StringBuilder optHashValueBuilder = null;
        private final SplitStrategy splitStrategy;

        public CompoundHashBuilder(SplitStrategy splitStrategy) {
            this.currentHashes = new ArrayList<String>();
            this.splitStrategy = splitStrategy;
        }

        private void appendKey(StringBuilder stringBuilder, ChildKey childKey) {
            stringBuilder.append(Utilities.stringHashV2Representation(childKey.asString()));
        }

        private Path currentPath(int n) {
            ChildKey[] childKeyArray = new ChildKey[n];
            for (int i = 0; i < n; ++i) {
                childKeyArray[i] = (ChildKey)this.currentPath.get(i);
            }
            return new Path(childKeyArray);
        }

        private void endChild() {
            --this.currentPathDepth;
            if (this.buildingRange()) {
                this.optHashValueBuilder.append(")");
            }
            this.needsComma = true;
        }

        private void endRange() {
            Utilities.hardAssert(this.buildingRange(), "Can't end range without starting a range!");
            for (int i = 0; i < this.currentPathDepth; ++i) {
                this.optHashValueBuilder.append(")");
            }
            this.optHashValueBuilder.append(")");
            Path path = this.currentPath(this.lastLeafDepth);
            String string2 = Utilities.sha1HexDigest(this.optHashValueBuilder.toString());
            this.currentHashes.add(string2);
            this.currentPaths.add(path);
            this.optHashValueBuilder = null;
        }

        private void ensureRange() {
            if (!this.buildingRange()) {
                Comparable<StringBuilder> comparable2;
                this.optHashValueBuilder = comparable2 = new StringBuilder();
                comparable2.append("(");
                for (Comparable<StringBuilder> comparable2 : this.currentPath(this.currentPathDepth)) {
                    this.appendKey(this.optHashValueBuilder, (ChildKey)comparable2);
                    this.optHashValueBuilder.append(":(");
                }
                this.needsComma = false;
            }
        }

        private void finishHashing() {
            boolean bl = this.currentPathDepth == 0;
            Utilities.hardAssert(bl, "Can't finish hashing in the middle processing a child");
            if (this.buildingRange()) {
                this.endRange();
            }
            this.currentHashes.add("");
        }

        private void processLeaf(LeafNode<?> leafNode) {
            this.ensureRange();
            this.lastLeafDepth = this.currentPathDepth;
            this.optHashValueBuilder.append(leafNode.getHashRepresentation(Node.HashVersion.V2));
            this.needsComma = true;
            if (this.splitStrategy.shouldSplit(this)) {
                this.endRange();
            }
        }

        private void startChild(ChildKey childKey) {
            this.ensureRange();
            if (this.needsComma) {
                this.optHashValueBuilder.append(",");
            }
            this.appendKey(this.optHashValueBuilder, childKey);
            this.optHashValueBuilder.append(":(");
            if (this.currentPathDepth == this.currentPath.size()) {
                this.currentPath.add(childKey);
            } else {
                this.currentPath.set(this.currentPathDepth, childKey);
            }
            ++this.currentPathDepth;
            this.needsComma = false;
        }

        public boolean buildingRange() {
            boolean bl = this.optHashValueBuilder != null;
            return bl;
        }

        public int currentHashLength() {
            return this.optHashValueBuilder.length();
        }

        public Path currentPath() {
            return this.currentPath(this.currentPathDepth);
        }
    }

    private static class SimpleSizeSplitStrategy
    implements SplitStrategy {
        private final long splitThreshold;

        public SimpleSizeSplitStrategy(Node node) {
            this.splitThreshold = Math.max(512L, (long)Math.sqrt(100L * NodeSizeEstimator.estimateSerializedNodeSize(node)));
        }

        @Override
        public boolean shouldSplit(CompoundHashBuilder compoundHashBuilder) {
            boolean bl = (long)compoundHashBuilder.currentHashLength() > this.splitThreshold && (compoundHashBuilder.currentPath().isEmpty() || !compoundHashBuilder.currentPath().getBack().equals(ChildKey.getPriorityKey()));
            return bl;
        }
    }

    public static interface SplitStrategy {
        public boolean shouldSplit(CompoundHashBuilder var1);
    }
}

