/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.view;

import com.google.firebase.database.core.CompoundWrite;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.WriteTreeRef;
import com.google.firebase.database.core.operation.AckUserWrite;
import com.google.firebase.database.core.operation.Merge;
import com.google.firebase.database.core.operation.Operation;
import com.google.firebase.database.core.operation.Overwrite;
import com.google.firebase.database.core.utilities.ImmutableTree;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.CacheNode;
import com.google.firebase.database.core.view.Change;
import com.google.firebase.database.core.view.ViewCache;
import com.google.firebase.database.core.view.filter.ChildChangeAccumulator;
import com.google.firebase.database.core.view.filter.NodeFilter;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.ChildrenNode;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.KeyIndex;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ViewProcessor {
    private static NodeFilter.CompleteChildSource NO_COMPLETE_SOURCE = new NodeFilter.CompleteChildSource(){

        @Override
        public NamedNode getChildAfterChild(Index index, NamedNode namedNode, boolean bl) {
            return null;
        }

        @Override
        public Node getCompleteChild(ChildKey childKey) {
            return null;
        }
    };
    private final NodeFilter filter;

    public ViewProcessor(NodeFilter nodeFilter) {
        this.filter = nodeFilter;
    }

    private ViewCache ackUserWrite(ViewCache viewCache, Path path, ImmutableTree<Boolean> iterable, WriteTreeRef writeTreeRef, Node node, ChildChangeAccumulator childChangeAccumulator) {
        if (writeTreeRef.shadowingWrite(path) != null) {
            return viewCache;
        }
        boolean bl = viewCache.getServerCache().isFiltered();
        Object object2 = viewCache.getServerCache();
        if (((ImmutableTree)iterable).getValue() != null) {
            if (path.isEmpty() && ((CacheNode)object2).isFullyInitialized() || ((CacheNode)object2).isCompleteForPath(path)) {
                return this.applyServerOverwrite(viewCache, path, ((CacheNode)object2).getNode().getChild(path), writeTreeRef, node, bl, childChangeAccumulator);
            }
            if (path.isEmpty()) {
                iterable = CompoundWrite.emptyWrite();
                for (Object object2 : ((CacheNode)object2).getNode()) {
                    iterable = ((CompoundWrite)iterable).addWrite(((NamedNode)object2).getName(), ((NamedNode)object2).getNode());
                }
                return this.applyServerMerge(viewCache, path, (CompoundWrite)iterable, writeTreeRef, node, bl, childChangeAccumulator);
            }
            return viewCache;
        }
        Iterable<Map.Entry<Path, Node>> iterable2 = CompoundWrite.emptyWrite();
        Iterator<Map.Entry<Path, Boolean>> iterator2 = ((ImmutableTree)iterable).iterator();
        while (iterator2.hasNext()) {
            Path path2 = iterator2.next().getKey();
            Path path3 = path.child(path2);
            iterable = iterable2;
            if (((CacheNode)object2).isCompleteForPath(path3)) {
                iterable = iterable2.addWrite(path2, ((CacheNode)object2).getNode().getChild(path3));
            }
            iterable2 = iterable;
        }
        return this.applyServerMerge(viewCache, path, (CompoundWrite)iterable2, writeTreeRef, node, bl, childChangeAccumulator);
    }

    private ViewCache applyServerMerge(ViewCache viewCache, Path iterable, CompoundWrite iterable2, WriteTreeRef writeTreeRef, Node node, boolean bl, ChildChangeAccumulator childChangeAccumulator) {
        Iterable<NamedNode> iterable3;
        if (viewCache.getServerCache().getNode().isEmpty() && !viewCache.getServerCache().isFullyInitialized()) {
            return viewCache;
        }
        Object object = viewCache;
        boolean bl2 = ((CompoundWrite)iterable2).rootWrite() == null;
        Utilities.hardAssert(bl2, "Can't have a merge that is an overwrite");
        iterable = ((Path)iterable).isEmpty() ? iterable2 : CompoundWrite.emptyWrite().addWrites((Path)iterable, (CompoundWrite)iterable2);
        iterable2 = viewCache.getServerCache().getNode();
        Map<ChildKey, CompoundWrite> object22 = ((CompoundWrite)iterable).childCompoundWrites();
        Object object2 = object22.entrySet().iterator();
        iterable = object;
        while (object2.hasNext()) {
            Map.Entry<ChildKey, CompoundWrite> entry = object2.next();
            object = entry.getKey();
            if (!iterable2.hasChild((ChildKey)object)) continue;
            iterable3 = iterable2.getImmediateChild((ChildKey)object);
            iterable3 = entry.getValue().apply((Node)iterable3);
            iterable = this.applyServerOverwrite((ViewCache)((Object)iterable), new Path(new ChildKey[]{object}), (Node)iterable3, writeTreeRef, node, bl, childChangeAccumulator);
        }
        for (Map.Entry entry : object22.entrySet()) {
            object2 = (ChildKey)entry.getKey();
            iterable3 = (CompoundWrite)entry.getValue();
            boolean bl3 = !viewCache.getServerCache().isCompleteForChild((ChildKey)object2) && ((CompoundWrite)iterable3).rootWrite() == null;
            if (iterable2.hasChild((ChildKey)object2) || bl3) continue;
            iterable3 = iterable2.getImmediateChild((ChildKey)object2);
            Node node2 = ((CompoundWrite)entry.getValue()).apply((Node)iterable3);
            iterable = this.applyServerOverwrite((ViewCache)((Object)iterable), new Path(new ChildKey[]{object2}), node2, writeTreeRef, node, bl, childChangeAccumulator);
        }
        return iterable;
    }

    private ViewCache applyServerOverwrite(ViewCache viewCache, Path path, Node iterable, WriteTreeRef writeTreeRef, Node node, boolean bl, ChildChangeAccumulator childChangeAccumulator) {
        CacheNode cacheNode = viewCache.getServerCache();
        NodeFilter nodeFilter = this.filter;
        if (!bl) {
            nodeFilter = nodeFilter.getIndexedFilter();
        }
        bl = path.isEmpty();
        boolean bl2 = true;
        if (bl) {
            iterable = nodeFilter.updateFullNode(cacheNode.getIndexedNode(), IndexedNode.from(iterable, nodeFilter.getIndex()), null);
        } else if (nodeFilter.filtersNodes() && !cacheNode.isFiltered()) {
            Utilities.hardAssert(path.isEmpty() ^ true, "An empty path should have been caught in the other branch");
            ChildKey childKey = path.getFront();
            Path path2 = path.popFront();
            iterable = cacheNode.getNode().getImmediateChild(childKey).updateChild(path2, (Node)iterable);
            iterable = cacheNode.getIndexedNode().updateChild(childKey, (Node)iterable);
            iterable = nodeFilter.updateFullNode(cacheNode.getIndexedNode(), (IndexedNode)iterable, null);
        } else {
            ChildKey childKey = path.getFront();
            if (!cacheNode.isCompleteForPath(path) && path.size() > 1) {
                return viewCache;
            }
            Path path3 = path.popFront();
            iterable = cacheNode.getNode().getImmediateChild(childKey).updateChild(path3, (Node)iterable);
            iterable = childKey.isPriorityChildName() ? nodeFilter.updatePriority(cacheNode.getIndexedNode(), (Node)iterable) : nodeFilter.updateChild(cacheNode.getIndexedNode(), childKey, (Node)iterable, path3, NO_COMPLETE_SOURCE, null);
        }
        bl = bl2;
        if (!cacheNode.isFullyInitialized()) {
            bl = path.isEmpty() ? bl2 : false;
        }
        viewCache = viewCache.updateServerSnap((IndexedNode)iterable, bl, nodeFilter.filtersNodes());
        return this.generateEventCacheAfterServerEvent(viewCache, path, writeTreeRef, new WriteTreeCompleteChildSource(writeTreeRef, viewCache, node), childChangeAccumulator);
    }

    private ViewCache applyUserMerge(ViewCache viewCache, Path path, CompoundWrite object, WriteTreeRef writeTreeRef, Node node, ChildChangeAccumulator childChangeAccumulator) {
        Object object2;
        Object object3;
        boolean bl = ((CompoundWrite)object).rootWrite() == null;
        Utilities.hardAssert(bl, "Can't have a merge that is an overwrite");
        Object object4 = viewCache;
        Object object5 = ((CompoundWrite)object).iterator();
        while (object5.hasNext()) {
            Map.Entry<Path, Node> entry = object5.next();
            object3 = path.child(entry.getKey());
            object2 = object4;
            if (ViewProcessor.cacheHasChild(viewCache, ((Path)object3).getFront())) {
                object2 = this.applyUserOverwrite((ViewCache)object4, (Path)object3, entry.getValue(), writeTreeRef, node, childChangeAccumulator);
            }
            object4 = object2;
        }
        object2 = ((CompoundWrite)object).iterator();
        while (object2.hasNext()) {
            object3 = object2.next();
            object5 = path.child((Path)object3.getKey());
            object = object4;
            if (!ViewProcessor.cacheHasChild(viewCache, ((Path)object5).getFront())) {
                object = this.applyUserOverwrite((ViewCache)object4, (Path)object5, (Node)object3.getValue(), writeTreeRef, node, childChangeAccumulator);
            }
            object4 = object;
        }
        return object4;
    }

    private ViewCache applyUserOverwrite(ViewCache viewCache, Path iterable, Node node, WriteTreeRef object, Node object2, ChildChangeAccumulator childChangeAccumulator) {
        block6: {
            CacheNode cacheNode;
            block7: {
                block5: {
                    cacheNode = viewCache.getEventCache();
                    object2 = new WriteTreeCompleteChildSource((WriteTreeRef)object, viewCache, (Node)object2);
                    if (!((Path)iterable).isEmpty()) break block5;
                    iterable = IndexedNode.from(node, this.filter.getIndex());
                    viewCache = viewCache.updateEventSnap(this.filter.updateFullNode(viewCache.getEventCache().getIndexedNode(), (IndexedNode)iterable, childChangeAccumulator), true, this.filter.filtersNodes());
                    break block6;
                }
                object = ((Path)iterable).getFront();
                if (!((ChildKey)object).isPriorityChildName()) break block7;
                iterable = this.filter.updatePriority(viewCache.getEventCache().getIndexedNode(), node);
                viewCache = viewCache.updateEventSnap((IndexedNode)iterable, cacheNode.isFullyInitialized(), cacheNode.isFiltered());
                break block6;
            }
            Path path = ((Path)iterable).popFront();
            Node node2 = cacheNode.getNode().getImmediateChild((ChildKey)object);
            if (path.isEmpty()) {
                iterable = node;
            } else {
                iterable = object2.getCompleteChild((ChildKey)object);
                if (iterable != null) {
                    if (!path.getBack().isPriorityChildName() || !iterable.getChild(path.getParent()).isEmpty()) {
                        iterable = iterable.updateChild(path, node);
                    }
                } else {
                    iterable = EmptyNode.Empty();
                }
            }
            if (node2.equals(iterable)) break block6;
            iterable = this.filter.updateChild(cacheNode.getIndexedNode(), (ChildKey)object, (Node)iterable, path, (NodeFilter.CompleteChildSource)object2, childChangeAccumulator);
            viewCache = viewCache.updateEventSnap((IndexedNode)iterable, cacheNode.isFullyInitialized(), this.filter.filtersNodes());
        }
        return viewCache;
    }

    private static boolean cacheHasChild(ViewCache viewCache, ChildKey childKey) {
        return viewCache.getEventCache().isCompleteForChild(childKey);
    }

    private ViewCache generateEventCacheAfterServerEvent(ViewCache viewCache, Path path, WriteTreeRef object, NodeFilter.CompleteChildSource object2, ChildChangeAccumulator object3) {
        boolean bl;
        block14: {
            block13: {
                CacheNode cacheNode = viewCache.getEventCache();
                if (((WriteTreeRef)object).shadowingWrite(path) != null) {
                    return viewCache;
                }
                bl = path.isEmpty();
                boolean bl2 = false;
                if (bl) {
                    Utilities.hardAssert(viewCache.getServerCache().isFullyInitialized(), "If change path is empty, we must have complete server data");
                    if (viewCache.getServerCache().isFiltered()) {
                        object2 = viewCache.getCompleteServerSnap();
                        if (!(object2 instanceof ChildrenNode)) {
                            object2 = EmptyNode.Empty();
                        }
                        object = ((WriteTreeRef)object).calcCompleteEventChildren((Node)object2);
                    } else {
                        object = ((WriteTreeRef)object).calcCompleteEventCache(viewCache.getCompleteServerSnap());
                    }
                    object = IndexedNode.from((Node)object, this.filter.getIndex());
                    object = this.filter.updateFullNode(viewCache.getEventCache().getIndexedNode(), (IndexedNode)object, (ChildChangeAccumulator)object3);
                } else {
                    ChildKey childKey = path.getFront();
                    if (childKey.isPriorityChildName()) {
                        bl = path.size() == 1;
                        Utilities.hardAssert(bl, "Can't have a priority with additional path components");
                        object3 = cacheNode.getNode();
                        object2 = viewCache.getServerCache().getNode();
                        object = ((WriteTreeRef)object).calcEventCacheAfterServerOverwrite(path, (Node)object3, (Node)object2);
                        object = object != null ? this.filter.updatePriority(cacheNode.getIndexedNode(), (Node)object) : cacheNode.getIndexedNode();
                    } else {
                        Path path2 = path.popFront();
                        if (cacheNode.isCompleteForChild(childKey)) {
                            Node node = viewCache.getServerCache().getNode();
                            object = (object = ((WriteTreeRef)object).calcEventCacheAfterServerOverwrite(path, cacheNode.getNode(), node)) != null ? cacheNode.getNode().getImmediateChild(childKey).updateChild(path2, (Node)object) : cacheNode.getNode().getImmediateChild(childKey);
                        } else {
                            object = ((WriteTreeRef)object).calcCompleteChild(childKey, viewCache.getServerCache());
                        }
                        object = object != null ? this.filter.updateChild(cacheNode.getIndexedNode(), childKey, (Node)object, path2, (NodeFilter.CompleteChildSource)object2, (ChildChangeAccumulator)object3) : cacheNode.getIndexedNode();
                    }
                }
                if (cacheNode.isFullyInitialized()) break block13;
                bl = bl2;
                if (!path.isEmpty()) break block14;
            }
            bl = true;
        }
        return viewCache.updateEventSnap((IndexedNode)object, bl, this.filter.filtersNodes());
    }

    private ViewCache listenComplete(ViewCache viewCache, Path path, WriteTreeRef writeTreeRef, Node iterable, ChildChangeAccumulator childChangeAccumulator) {
        CacheNode cacheNode = viewCache.getServerCache();
        iterable = cacheNode.getIndexedNode();
        boolean bl = cacheNode.isFullyInitialized() || path.isEmpty();
        return this.generateEventCacheAfterServerEvent(viewCache.updateServerSnap((IndexedNode)iterable, bl, cacheNode.isFiltered()), path, writeTreeRef, NO_COMPLETE_SOURCE, childChangeAccumulator);
    }

    private void maybeAddValueEvent(ViewCache viewCache, ViewCache object, List<Change> list) {
        if (((CacheNode)(object = ((ViewCache)object).getEventCache())).isFullyInitialized()) {
            boolean bl = ((CacheNode)object).getNode().isLeafNode() || ((CacheNode)object).getNode().isEmpty();
            if (!list.isEmpty() || !viewCache.getEventCache().isFullyInitialized() || bl && !((CacheNode)object).getNode().equals(viewCache.getCompleteEventSnap()) || !((CacheNode)object).getNode().getPriority().equals(viewCache.getCompleteEventSnap().getPriority())) {
                list.add(Change.valueChange(((CacheNode)object).getIndexedNode()));
            }
        }
    }

    public ProcessorResult applyOperation(ViewCache object, Operation object2, WriteTreeRef object3, Node node) {
        ChildChangeAccumulator childChangeAccumulator = new ChildChangeAccumulator();
        switch (2.$SwitchMap$com$google$firebase$database$core$operation$Operation$OperationType[((Operation)object2).getType().ordinal()]) {
            default: {
                object = new StringBuilder();
                ((StringBuilder)object).append("Unknown operation: ");
                ((StringBuilder)object).append((Object)((Operation)object2).getType());
                throw new AssertionError((Object)((StringBuilder)object).toString());
            }
            case 4: {
                object2 = this.listenComplete((ViewCache)object, ((Operation)object2).getPath(), (WriteTreeRef)object3, node, childChangeAccumulator);
                break;
            }
            case 3: {
                object2 = (AckUserWrite)object2;
                if (!((AckUserWrite)object2).isRevert()) {
                    object2 = this.ackUserWrite((ViewCache)object, ((Operation)object2).getPath(), ((AckUserWrite)object2).getAffectedTree(), (WriteTreeRef)object3, node, childChangeAccumulator);
                    break;
                }
                object2 = this.revertUserWrite((ViewCache)object, ((Operation)object2).getPath(), (WriteTreeRef)object3, node, childChangeAccumulator);
                break;
            }
            case 2: {
                object2 = (Merge)object2;
                if (((Operation)object2).getSource().isFromUser()) {
                    object2 = this.applyUserMerge((ViewCache)object, ((Operation)object2).getPath(), ((Merge)object2).getChildren(), (WriteTreeRef)object3, node, childChangeAccumulator);
                    break;
                }
                Utilities.hardAssert(((Operation)object2).getSource().isFromServer());
                boolean bl = ((Operation)object2).getSource().isTagged() || ((ViewCache)object).getServerCache().isFiltered();
                object2 = this.applyServerMerge((ViewCache)object, ((Operation)object2).getPath(), ((Merge)object2).getChildren(), (WriteTreeRef)object3, node, bl, childChangeAccumulator);
                break;
            }
            case 1: {
                object2 = (Overwrite)object2;
                if (((Operation)object2).getSource().isFromUser()) {
                    object2 = this.applyUserOverwrite((ViewCache)object, ((Operation)object2).getPath(), ((Overwrite)object2).getSnapshot(), (WriteTreeRef)object3, node, childChangeAccumulator);
                    break;
                }
                Utilities.hardAssert(((Operation)object2).getSource().isFromServer());
                boolean bl = ((Operation)object2).getSource().isTagged() || ((ViewCache)object).getServerCache().isFiltered() && !((Operation)object2).getPath().isEmpty();
                object2 = this.applyServerOverwrite((ViewCache)object, ((Operation)object2).getPath(), ((Overwrite)object2).getSnapshot(), (WriteTreeRef)object3, node, bl, childChangeAccumulator);
            }
        }
        object3 = new ArrayList<Change>(childChangeAccumulator.getChanges());
        this.maybeAddValueEvent((ViewCache)object, (ViewCache)object2, (List<Change>)object3);
        return new ProcessorResult((ViewCache)object2, (List<Change>)object3);
    }

    public ViewCache revertUserWrite(ViewCache viewCache, Path iterable, WriteTreeRef writeTreeRef, Node iterable2, ChildChangeAccumulator childChangeAccumulator) {
        if (writeTreeRef.shadowingWrite((Path)iterable) != null) {
            return viewCache;
        }
        WriteTreeCompleteChildSource writeTreeCompleteChildSource = new WriteTreeCompleteChildSource(writeTreeRef, viewCache, (Node)iterable2);
        Iterable<NamedNode> iterable3 = viewCache.getEventCache().getIndexedNode();
        if (!iterable.isEmpty() && !iterable.getFront().isPriorityChildName()) {
            ChildKey childKey = iterable.getFront();
            iterable2 = writeTreeRef.calcCompleteChild(childKey, viewCache.getServerCache());
            if (iterable2 == null && viewCache.getServerCache().isCompleteForChild(childKey)) {
                iterable2 = ((IndexedNode)iterable3).getNode().getImmediateChild(childKey);
            }
            iterable = iterable2 != null ? this.filter.updateChild((IndexedNode)iterable3, childKey, (Node)iterable2, iterable.popFront(), writeTreeCompleteChildSource, childChangeAccumulator) : (iterable2 == null && viewCache.getEventCache().getNode().hasChild(childKey) ? this.filter.updateChild((IndexedNode)iterable3, childKey, EmptyNode.Empty(), iterable.popFront(), writeTreeCompleteChildSource, childChangeAccumulator) : iterable3);
            iterable2 = iterable;
            if (((IndexedNode)iterable).getNode().isEmpty()) {
                iterable2 = iterable;
                if (viewCache.getServerCache().isFullyInitialized()) {
                    iterable3 = writeTreeRef.calcCompleteEventCache(viewCache.getCompleteServerSnap());
                    iterable2 = iterable;
                    if (iterable3.isLeafNode()) {
                        iterable2 = IndexedNode.from(iterable3, this.filter.getIndex());
                        iterable2 = this.filter.updateFullNode((IndexedNode)iterable, (IndexedNode)iterable2, childChangeAccumulator);
                    }
                }
            }
        } else {
            iterable = viewCache.getServerCache().isFullyInitialized() ? writeTreeRef.calcCompleteEventCache(viewCache.getCompleteServerSnap()) : writeTreeRef.calcCompleteEventChildren(viewCache.getServerCache().getNode());
            iterable = IndexedNode.from((Node)iterable, this.filter.getIndex());
            iterable2 = this.filter.updateFullNode((IndexedNode)iterable3, (IndexedNode)iterable, childChangeAccumulator);
        }
        boolean bl = viewCache.getServerCache().isFullyInitialized() || writeTreeRef.shadowingWrite(Path.getEmptyPath()) != null;
        return viewCache.updateEventSnap((IndexedNode)iterable2, bl, this.filter.filtersNodes());
    }

    public static class ProcessorResult {
        public final List<Change> changes;
        public final ViewCache viewCache;

        public ProcessorResult(ViewCache viewCache, List<Change> list) {
            this.viewCache = viewCache;
            this.changes = list;
        }
    }

    private static class WriteTreeCompleteChildSource
    implements NodeFilter.CompleteChildSource {
        private final Node optCompleteServerCache;
        private final ViewCache viewCache;
        private final WriteTreeRef writes;

        public WriteTreeCompleteChildSource(WriteTreeRef writeTreeRef, ViewCache viewCache, Node node) {
            this.writes = writeTreeRef;
            this.viewCache = viewCache;
            this.optCompleteServerCache = node;
        }

        @Override
        public NamedNode getChildAfterChild(Index index, NamedNode namedNode, boolean bl) {
            Node node = this.optCompleteServerCache;
            if (node == null) {
                node = this.viewCache.getCompleteServerSnap();
            }
            return this.writes.calcNextNodeAfterPost(node, namedNode, bl, index);
        }

        @Override
        public Node getCompleteChild(ChildKey childKey) {
            Object object = this.viewCache.getEventCache();
            if (((CacheNode)object).isCompleteForChild(childKey)) {
                return ((CacheNode)object).getNode().getImmediateChild(childKey);
            }
            object = this.optCompleteServerCache;
            object = object != null ? new CacheNode(IndexedNode.from((Node)object, KeyIndex.getInstance()), true, false) : this.viewCache.getServerCache();
            return this.writes.calcCompleteChild(childKey, (CacheNode)object);
        }
    }
}

