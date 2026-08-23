/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.core.CompoundWrite;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.UserWriteRecord;
import com.google.firebase.database.core.WriteTreeRef;
import com.google.firebase.database.core.utilities.Predicate;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.CacheNode;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WriteTree {
    private static final Predicate<UserWriteRecord> DEFAULT_FILTER = new Predicate<UserWriteRecord>(){

        @Override
        public boolean evaluate(UserWriteRecord userWriteRecord) {
            return userWriteRecord.isVisible();
        }
    };
    private List<UserWriteRecord> allWrites;
    private Long lastWriteId = -1L;
    private CompoundWrite visibleWrites = CompoundWrite.emptyWrite();

    public WriteTree() {
        this.allWrites = new ArrayList<UserWriteRecord>();
    }

    private static CompoundWrite layerTree(List<UserWriteRecord> iterable, Predicate<UserWriteRecord> predicate, Path path) {
        Iterable<Map.Entry<Path, Node>> iterable2 = CompoundWrite.emptyWrite();
        for (UserWriteRecord userWriteRecord : iterable) {
            iterable = iterable2;
            if (predicate.evaluate(userWriteRecord)) {
                Path path2 = userWriteRecord.getPath();
                if (userWriteRecord.isOverwrite()) {
                    if (path.contains(path2)) {
                        iterable = ((CompoundWrite)iterable2).addWrite(Path.getRelative(path, path2), userWriteRecord.getOverwrite());
                    } else {
                        iterable = iterable2;
                        if (path2.contains(path)) {
                            iterable = ((CompoundWrite)iterable2).addWrite(Path.getEmptyPath(), userWriteRecord.getOverwrite().getChild(Path.getRelative(path2, path)));
                        }
                    }
                } else if (path.contains(path2)) {
                    iterable = ((CompoundWrite)iterable2).addWrites(Path.getRelative(path, path2), userWriteRecord.getMerge());
                } else {
                    iterable = iterable2;
                    if (path2.contains(path)) {
                        iterable = Path.getRelative(path2, path);
                        if (((Path)iterable).isEmpty()) {
                            iterable = ((CompoundWrite)iterable2).addWrites(Path.getEmptyPath(), userWriteRecord.getMerge());
                        } else {
                            Node node = userWriteRecord.getMerge().getCompleteNode((Path)iterable);
                            iterable = iterable2;
                            if (node != null) {
                                iterable = ((CompoundWrite)iterable2).addWrite(Path.getEmptyPath(), node);
                            }
                        }
                    }
                }
            }
            iterable2 = iterable;
        }
        return iterable2;
    }

    private boolean recordContainsPath(UserWriteRecord userWriteRecord, Path path) {
        if (userWriteRecord.isOverwrite()) {
            return userWriteRecord.getPath().contains(path);
        }
        for (Map.Entry<Path, Node> entry : userWriteRecord.getMerge()) {
            if (!userWriteRecord.getPath().child(entry.getKey()).contains(path)) continue;
            return true;
        }
        return false;
    }

    private void resetTree() {
        this.visibleWrites = WriteTree.layerTree(this.allWrites, DEFAULT_FILTER, Path.getEmptyPath());
        if (this.allWrites.size() > 0) {
            List<UserWriteRecord> list = this.allWrites;
            this.lastWriteId = list.get(list.size() - 1).getWriteId();
        } else {
            this.lastWriteId = -1L;
        }
    }

    public void addMerge(Path path, CompoundWrite compoundWrite, Long l) {
        boolean bl = l > this.lastWriteId;
        Utilities.hardAssert(bl);
        this.allWrites.add(new UserWriteRecord(l, path, compoundWrite));
        this.visibleWrites = this.visibleWrites.addWrites(path, compoundWrite);
        this.lastWriteId = l;
    }

    public void addOverwrite(Path path, Node node, Long l, boolean bl) {
        boolean bl2 = l > this.lastWriteId;
        Utilities.hardAssert(bl2);
        this.allWrites.add(new UserWriteRecord(l, path, node, bl));
        if (bl) {
            this.visibleWrites = this.visibleWrites.addWrite(path, node);
        }
        this.lastWriteId = l;
    }

    public Node calcCompleteChild(Path path, ChildKey childKey, CacheNode cacheNode) {
        Node node = this.visibleWrites.getCompleteNode(path = path.child(childKey));
        if (node != null) {
            return node;
        }
        if (cacheNode.isCompleteForChild(childKey)) {
            return this.visibleWrites.childCompoundWrite(path).apply(cacheNode.getNode().getImmediateChild(childKey));
        }
        return null;
    }

    public Node calcCompleteEventCache(Path path, Node node) {
        return this.calcCompleteEventCache(path, node, new ArrayList<Long>());
    }

    public Node calcCompleteEventCache(Path path, Node node, List<Long> list) {
        return this.calcCompleteEventCache(path, node, list, false);
    }

    public Node calcCompleteEventCache(Path iterable, Node node, List<Long> object, boolean bl) {
        if (object.isEmpty() && !bl) {
            object = this.visibleWrites.getCompleteNode((Path)iterable);
            if (object != null) {
                return object;
            }
            if (((CompoundWrite)(iterable = this.visibleWrites.childCompoundWrite((Path)iterable))).isEmpty()) {
                return node;
            }
            if (node == null && !((CompoundWrite)iterable).hasCompleteWrite(Path.getEmptyPath())) {
                return null;
            }
            if (node == null) {
                node = EmptyNode.Empty();
            }
            return ((CompoundWrite)iterable).apply(node);
        }
        CompoundWrite compoundWrite = this.visibleWrites.childCompoundWrite((Path)iterable);
        if (!bl && compoundWrite.isEmpty()) {
            return node;
        }
        if (!bl && node == null && !compoundWrite.hasCompleteWrite(Path.getEmptyPath())) {
            return null;
        }
        object = new Predicate<UserWriteRecord>(this, bl, object, iterable){
            final WriteTree this$0;
            final boolean val$includeHiddenWrites;
            final Path val$treePath;
            final List val$writeIdsToExclude;
            {
                this.this$0 = writeTree;
                this.val$includeHiddenWrites = bl;
                this.val$writeIdsToExclude = list;
                this.val$treePath = path;
            }

            @Override
            public boolean evaluate(UserWriteRecord userWriteRecord) {
                boolean bl = !(!userWriteRecord.isVisible() && !this.val$includeHiddenWrites || this.val$writeIdsToExclude.contains(userWriteRecord.getWriteId()) || !userWriteRecord.getPath().contains(this.val$treePath) && !this.val$treePath.contains(userWriteRecord.getPath()));
                return bl;
            }
        };
        iterable = WriteTree.layerTree(this.allWrites, (Predicate<UserWriteRecord>)object, iterable);
        if (node == null) {
            node = EmptyNode.Empty();
        }
        return ((CompoundWrite)iterable).apply(node);
    }

    public Node calcCompleteEventChildren(Path object, Node object22) {
        Node node = EmptyNode.Empty();
        Object object2 = this.visibleWrites.getCompleteNode((Path)object);
        if (object2 != null) {
            object = node;
            if (!object2.isLeafNode()) {
                Iterator iterator2 = object2.iterator();
                while (true) {
                    object = node;
                    if (!iterator2.hasNext()) break;
                    object = (NamedNode)iterator2.next();
                    node = node.updateImmediateChild(((NamedNode)object).getName(), ((NamedNode)object).getNode());
                }
            }
            return object;
        }
        object = this.visibleWrites.childCompoundWrite((Path)object);
        Iterator iterator3 = object22.iterator();
        while (iterator3.hasNext()) {
            object2 = (NamedNode)iterator3.next();
            Node node2 = ((CompoundWrite)object).childCompoundWrite(new Path(((NamedNode)object2).getName())).apply(((NamedNode)object2).getNode());
            node = node.updateImmediateChild(((NamedNode)object2).getName(), node2);
        }
        for (NamedNode namedNode : ((CompoundWrite)object).getCompleteChildren()) {
            node = node.updateImmediateChild(namedNode.getName(), namedNode.getNode());
        }
        return node;
    }

    public Node calcEventCacheAfterServerOverwrite(Path iterable, Path path, Node node, Node node2) {
        boolean bl = node != null || node2 != null;
        Utilities.hardAssert(bl, "Either existingEventSnap or existingServerSnap must exist");
        iterable = ((Path)iterable).child(path);
        if (this.visibleWrites.hasCompleteWrite((Path)iterable)) {
            return null;
        }
        if (((CompoundWrite)(iterable = this.visibleWrites.childCompoundWrite((Path)iterable))).isEmpty()) {
            return node2.getChild(path);
        }
        return ((CompoundWrite)iterable).apply(node2.getChild(path));
    }

    public NamedNode calcNextNodeAfterPost(Path comparable, Node object, NamedNode namedNode, boolean bl, Index index) {
        block4: {
            Object object2;
            block3: {
                object2 = this.visibleWrites.childCompoundWrite((Path)comparable);
                if ((comparable = ((CompoundWrite)object2).getCompleteNode(Path.getEmptyPath())) != null) break block3;
                if (object == null) break block4;
                comparable = ((CompoundWrite)object2).apply((Node)object);
            }
            object = null;
            Iterator iterator2 = comparable.iterator();
            comparable = object;
            while (iterator2.hasNext()) {
                block5: {
                    block6: {
                        object2 = (NamedNode)iterator2.next();
                        object = comparable;
                        if (index.compare((NamedNode)object2, namedNode, bl) <= 0) break block5;
                        if (comparable == null) break block6;
                        object = comparable;
                        if (index.compare((NamedNode)object2, (NamedNode)((Object)comparable), bl) >= 0) break block5;
                    }
                    object = object2;
                }
                comparable = object;
            }
            return comparable;
        }
        return null;
    }

    public WriteTreeRef childWrites(Path path) {
        return new WriteTreeRef(path, this);
    }

    public Node getCompleteWriteData(Path path) {
        return this.visibleWrites.getCompleteNode(path);
    }

    public UserWriteRecord getWrite(long l) {
        for (UserWriteRecord userWriteRecord : this.allWrites) {
            if (userWriteRecord.getWriteId() != l) continue;
            return userWriteRecord;
        }
        return null;
    }

    public List<UserWriteRecord> purgeAllWrites() {
        ArrayList<UserWriteRecord> arrayList = new ArrayList<UserWriteRecord>(this.allWrites);
        this.visibleWrites = CompoundWrite.emptyWrite();
        this.allWrites = new ArrayList<UserWriteRecord>();
        return arrayList;
    }

    public boolean removeWrite(long l) {
        UserWriteRecord userWriteRecord;
        Object object = null;
        int n = 0;
        Iterator<Object> iterator2 = this.allWrites.iterator();
        while (true) {
            userWriteRecord = object;
            if (!iterator2.hasNext() || (userWriteRecord = iterator2.next()).getWriteId() == l) break;
            ++n;
        }
        boolean bl = userWriteRecord != null;
        Utilities.hardAssert(bl, "removeWrite called with nonexistent writeId");
        this.allWrites.remove(userWriteRecord);
        boolean bl2 = userWriteRecord.isVisible();
        boolean bl3 = false;
        for (int i = this.allWrites.size() - 1; bl2 && i >= 0; --i) {
            object = this.allWrites.get(i);
            bl = bl2;
            boolean bl4 = bl3;
            if (((UserWriteRecord)object).isVisible()) {
                if (i >= n && this.recordContainsPath((UserWriteRecord)object, userWriteRecord.getPath())) {
                    bl = false;
                    bl4 = bl3;
                } else {
                    bl = bl2;
                    bl4 = bl3;
                    if (userWriteRecord.getPath().contains(((UserWriteRecord)object).getPath())) {
                        bl4 = true;
                        bl = bl2;
                    }
                }
            }
            bl2 = bl;
            bl3 = bl4;
        }
        if (!bl2) {
            return false;
        }
        if (bl3) {
            this.resetTree();
            return true;
        }
        if (userWriteRecord.isOverwrite()) {
            this.visibleWrites = this.visibleWrites.removeWrite(userWriteRecord.getPath());
        } else {
            iterator2 = userWriteRecord.getMerge().iterator();
            while (iterator2.hasNext()) {
                object = (Path)((Map.Entry)iterator2.next()).getKey();
                this.visibleWrites = this.visibleWrites.removeWrite(userWriteRecord.getPath().child((Path)object));
            }
        }
        return true;
    }

    public Node shadowingWrite(Path path) {
        return this.visibleWrites.getCompleteNode(path);
    }
}

