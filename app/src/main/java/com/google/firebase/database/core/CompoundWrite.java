/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.utilities.ImmutableTree;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.NodeUtilities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class CompoundWrite
implements Iterable<Map.Entry<Path, Node>> {
    private static final CompoundWrite EMPTY = new CompoundWrite(new ImmutableTree<Object>(null));
    private final ImmutableTree<Node> writeTree;

    private CompoundWrite(ImmutableTree<Node> immutableTree) {
        this.writeTree = immutableTree;
    }

    private Node applySubtreeWrite(Path path, ImmutableTree<Node> iterable, Node node) {
        if (iterable.getValue() != null) {
            return node.updateChild(path, iterable.getValue());
        }
        Iterable<NamedNode> iterable2 = null;
        Iterator<Map.Entry<ChildKey, ImmutableTree<Node>>> iterator2 = iterable.getChildren().iterator();
        iterable = iterable2;
        while (iterator2.hasNext()) {
            Object object = iterator2.next();
            iterable2 = object.getValue();
            if (((ChildKey)(object = object.getKey())).isPriorityChildName()) {
                boolean bl = ((ImmutableTree)iterable2).getValue() != null;
                Utilities.hardAssert(bl, "Priority writes must always be leaf nodes");
                iterable = (Node)((ImmutableTree)iterable2).getValue();
                continue;
            }
            node = this.applySubtreeWrite(path.child((ChildKey)object), (ImmutableTree<Node>)iterable2, node);
        }
        iterable2 = node;
        if (!node.getChild(path).isEmpty()) {
            iterable2 = node;
            if (iterable != null) {
                iterable2 = node.updateChild(path.child(ChildKey.getPriorityKey()), (Node)iterable);
            }
        }
        return iterable2;
    }

    public static CompoundWrite emptyWrite() {
        return EMPTY;
    }

    public static CompoundWrite fromChildMerge(Map<ChildKey, Node> immutableTree) {
        ImmutableTree<Object> immutableTree2 = ImmutableTree.emptyInstance();
        Iterator<Map.Entry<ChildKey, Node>> iterator2 = immutableTree.entrySet().iterator();
        immutableTree = immutableTree2;
        while (iterator2.hasNext()) {
            Map.Entry<ChildKey, Node> entry = iterator2.next();
            immutableTree2 = new ImmutableTree<Node>(entry.getValue());
            immutableTree = immutableTree.setTree(new Path(entry.getKey()), immutableTree2);
        }
        return new CompoundWrite(immutableTree);
    }

    public static CompoundWrite fromPathMerge(Map<Path, Node> immutableTree) {
        Object object = ImmutableTree.emptyInstance();
        Iterator<Map.Entry<Path, Node>> iterator2 = immutableTree.entrySet().iterator();
        immutableTree = object;
        while (iterator2.hasNext()) {
            object = iterator2.next();
            ImmutableTree<Node> immutableTree2 = new ImmutableTree<Node>((Node)object.getValue());
            immutableTree = immutableTree.setTree((Path)object.getKey(), immutableTree2);
        }
        return new CompoundWrite(immutableTree);
    }

    public static CompoundWrite fromValue(Map<String, Object> immutableTree) {
        ImmutableTree<Object> immutableTree2 = ImmutableTree.emptyInstance();
        Iterator<Map.Entry<String, Object>> iterator2 = immutableTree.entrySet().iterator();
        immutableTree = immutableTree2;
        while (iterator2.hasNext()) {
            Map.Entry<String, Object> entry = iterator2.next();
            immutableTree2 = new ImmutableTree<Node>(NodeUtilities.NodeFromJSON(entry.getValue()));
            immutableTree = immutableTree.setTree(new Path(entry.getKey()), immutableTree2);
        }
        return new CompoundWrite(immutableTree);
    }

    public CompoundWrite addWrite(Path comparable, Node iterable) {
        if (comparable.isEmpty()) {
            return new CompoundWrite(new ImmutableTree<Node>((Node)iterable));
        }
        Path path = this.writeTree.findRootMostPathWithValue((Path)comparable);
        if (path != null) {
            comparable = Path.getRelative(path, comparable);
            Node node = this.writeTree.get(path);
            ChildKey childKey = comparable.getBack();
            if (childKey != null && childKey.isPriorityChildName() && node.getChild(comparable.getParent()).isEmpty()) {
                return this;
            }
            comparable = node.updateChild((Path)comparable, (Node)iterable);
            return new CompoundWrite(this.writeTree.set(path, (Node)comparable));
        }
        iterable = new ImmutableTree<Node>((Node)iterable);
        return new CompoundWrite(this.writeTree.setTree((Path)comparable, (ImmutableTree<Node>)iterable));
    }

    public CompoundWrite addWrite(ChildKey childKey, Node node) {
        return this.addWrite(new Path(childKey), node);
    }

    public CompoundWrite addWrites(Path path, CompoundWrite compoundWrite) {
        return compoundWrite.writeTree.fold(this, new ImmutableTree.TreeVisitor<Node, CompoundWrite>(this, path){
            final CompoundWrite this$0;
            final Path val$path;
            {
                this.this$0 = compoundWrite;
                this.val$path = path;
            }

            @Override
            public CompoundWrite onNodeValue(Path path, Node node, CompoundWrite compoundWrite) {
                return compoundWrite.addWrite(this.val$path.child(path), node);
            }
        });
    }

    public Node apply(Node node) {
        return this.applySubtreeWrite(Path.getEmptyPath(), this.writeTree, node);
    }

    public CompoundWrite childCompoundWrite(Path path) {
        if (path.isEmpty()) {
            return this;
        }
        Node node = this.getCompleteNode(path);
        if (node != null) {
            return new CompoundWrite(new ImmutableTree<Node>(node));
        }
        return new CompoundWrite(this.writeTree.subtree(path));
    }

    public Map<ChildKey, CompoundWrite> childCompoundWrites() {
        HashMap<ChildKey, CompoundWrite> hashMap = new HashMap<ChildKey, CompoundWrite>();
        for (Map.Entry<ChildKey, ImmutableTree<Node>> entry : this.writeTree.getChildren()) {
            hashMap.put(entry.getKey(), new CompoundWrite(entry.getValue()));
        }
        return hashMap;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object != null && object.getClass() == this.getClass()) {
            return ((CompoundWrite)object).getValue(true).equals(this.getValue(true));
        }
        return false;
    }

    public List<NamedNode> getCompleteChildren() {
        ArrayList<NamedNode> arrayList = new ArrayList<NamedNode>();
        if (this.writeTree.getValue() != null) {
            for (NamedNode namedNode : this.writeTree.getValue()) {
                arrayList.add(new NamedNode(namedNode.getName(), namedNode.getNode()));
            }
        } else {
            for (Map.Entry<ChildKey, ImmutableTree<Node>> entry : this.writeTree.getChildren()) {
                ImmutableTree<Node> immutableTree = entry.getValue();
                if (immutableTree.getValue() == null) continue;
                arrayList.add(new NamedNode(entry.getKey(), immutableTree.getValue()));
            }
        }
        return arrayList;
    }

    public Node getCompleteNode(Path path) {
        Path path2 = this.writeTree.findRootMostPathWithValue(path);
        if (path2 != null) {
            return this.writeTree.get(path2).getChild(Path.getRelative(path2, path));
        }
        return null;
    }

    public Map<String, Object> getValue(boolean bl) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        this.writeTree.foreach(new ImmutableTree.TreeVisitor<Node, Void>(this, hashMap, bl){
            final CompoundWrite this$0;
            final boolean val$exportFormat;
            final Map val$writes;
            {
                this.this$0 = compoundWrite;
                this.val$writes = map;
                this.val$exportFormat = bl;
            }

            @Override
            public Void onNodeValue(Path path, Node node, Void void_) {
                this.val$writes.put(path.wireFormat(), node.getValue(this.val$exportFormat));
                return null;
            }
        });
        return hashMap;
    }

    public boolean hasCompleteWrite(Path path) {
        boolean bl = this.getCompleteNode(path) != null;
        return bl;
    }

    public int hashCode() {
        return this.getValue(true).hashCode();
    }

    public boolean isEmpty() {
        return this.writeTree.isEmpty();
    }

    @Override
    public Iterator<Map.Entry<Path, Node>> iterator() {
        return this.writeTree.iterator();
    }

    public CompoundWrite removeWrite(Path path) {
        if (path.isEmpty()) {
            return EMPTY;
        }
        return new CompoundWrite(this.writeTree.setTree(path, ImmutableTree.emptyInstance()));
    }

    public Node rootWrite() {
        return this.writeTree.getValue();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CompoundWrite{");
        stringBuilder.append(this.getValue(true).toString());
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}

