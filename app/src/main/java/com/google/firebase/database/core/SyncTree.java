/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.InternalHelpers;
import com.google.firebase.database.Query;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.database.connection.CompoundHash;
import com.google.firebase.database.connection.ListenHashProvider;
import com.google.firebase.database.core.CompoundWrite;
import com.google.firebase.database.core.Context;
import com.google.firebase.database.core.EventRegistration;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.ServerValues;
import com.google.firebase.database.core.SyncPoint;
import com.google.firebase.database.core.SyncTree$$ExternalSyntheticLambda0;
import com.google.firebase.database.core.Tag;
import com.google.firebase.database.core.UserWriteRecord;
import com.google.firebase.database.core.WriteTree;
import com.google.firebase.database.core.WriteTreeRef;
import com.google.firebase.database.core.operation.AckUserWrite;
import com.google.firebase.database.core.operation.ListenComplete;
import com.google.firebase.database.core.operation.Merge;
import com.google.firebase.database.core.operation.Operation;
import com.google.firebase.database.core.operation.OperationSource;
import com.google.firebase.database.core.operation.Overwrite;
import com.google.firebase.database.core.persistence.PersistenceManager;
import com.google.firebase.database.core.utilities.Clock;
import com.google.firebase.database.core.utilities.ImmutableTree;
import com.google.firebase.database.core.utilities.NodeSizeEstimator;
import com.google.firebase.database.core.utilities.Pair;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.CacheNode;
import com.google.firebase.database.core.view.Change;
import com.google.firebase.database.core.view.DataEvent;
import com.google.firebase.database.core.view.Event;
import com.google.firebase.database.core.view.QuerySpec;
import com.google.firebase.database.core.view.View;
import com.google.firebase.database.logging.LogWrapper;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.RangeMerge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class SyncTree {
    private static final long SIZE_THRESHOLD_FOR_COMPOUND_HASH = 1024L;
    private final Set<QuerySpec> keepSyncedQueries;
    private final ListenProvider listenProvider;
    private final LogWrapper logger;
    private long nextQueryTag = 1L;
    private final WriteTree pendingWriteTree;
    private final PersistenceManager persistenceManager;
    private final Map<QuerySpec, Tag> queryToTagMap;
    private ImmutableTree<SyncPoint> syncPointTree = ImmutableTree.emptyInstance();
    private final Map<Tag, QuerySpec> tagToQueryMap;

    public SyncTree(Context context, PersistenceManager persistenceManager, ListenProvider listenProvider) {
        this.pendingWriteTree = new WriteTree();
        this.tagToQueryMap = new HashMap<Tag, QuerySpec>();
        this.queryToTagMap = new HashMap<QuerySpec, Tag>();
        this.keepSyncedQueries = new HashSet<QuerySpec>();
        this.listenProvider = listenProvider;
        this.persistenceManager = persistenceManager;
        this.logger = context.getLogger("SyncTree");
    }

    static /* synthetic */ ImmutableTree access$702(SyncTree syncTree, ImmutableTree immutableTree) {
        syncTree.syncPointTree = immutableTree;
        return immutableTree;
    }

    private List<Event> applyOperationDescendantsHelper(Operation operation, ImmutableTree<SyncPoint> immutableTree, Node node, WriteTreeRef writeTreeRef) {
        SyncPoint syncPoint = immutableTree.getValue();
        if (node == null && syncPoint != null) {
            node = syncPoint.getCompleteServerCache(Path.getEmptyPath());
        }
        ArrayList<Event> arrayList = new ArrayList<Event>();
        immutableTree.getChildren().inOrderTraversal(new LLRBNode.NodeVisitor<ChildKey, ImmutableTree<SyncPoint>>(this, node, writeTreeRef, operation, arrayList){
            final SyncTree this$0;
            final List val$events;
            final Operation val$operation;
            final Node val$resolvedServerCache;
            final WriteTreeRef val$writesCache;
            {
                this.this$0 = syncTree;
                this.val$resolvedServerCache = node;
                this.val$writesCache = writeTreeRef;
                this.val$operation = operation;
                this.val$events = list;
            }

            @Override
            public void visitEntry(ChildKey object, ImmutableTree<SyncPoint> immutableTree) {
                Node node = null;
                Object object2 = this.val$resolvedServerCache;
                if (object2 != null) {
                    node = object2.getImmediateChild((ChildKey)object);
                }
                object2 = this.val$writesCache.child((ChildKey)object);
                if ((object = this.val$operation.operationForChild((ChildKey)object)) != null) {
                    this.val$events.addAll(this.this$0.applyOperationDescendantsHelper((Operation)object, immutableTree, node, (WriteTreeRef)object2));
                }
            }
        });
        if (syncPoint != null) {
            arrayList.addAll(syncPoint.applyOperation(operation, writeTreeRef, node));
        }
        return arrayList;
    }

    private List<Event> applyOperationHelper(Operation operation, ImmutableTree<SyncPoint> iterable, Node iterable2, WriteTreeRef writeTreeRef) {
        if (operation.getPath().isEmpty()) {
            return this.applyOperationDescendantsHelper(operation, (ImmutableTree<SyncPoint>)iterable, (Node)iterable2, writeTreeRef);
        }
        SyncPoint syncPoint = iterable.getValue();
        Node node = iterable2;
        if (iterable2 == null) {
            node = iterable2;
            if (syncPoint != null) {
                node = syncPoint.getCompleteServerCache(Path.getEmptyPath());
            }
        }
        iterable2 = new ArrayList();
        Object object = operation.getPath().getFront();
        Operation operation2 = operation.operationForChild((ChildKey)object);
        ImmutableTree<SyncPoint> immutableTree = iterable.getChildren().get((ChildKey)object);
        if (immutableTree != null && operation2 != null) {
            iterable = node != null ? node.getImmediateChild((ChildKey)object) : null;
            object = writeTreeRef.child((ChildKey)object);
            iterable2.addAll(this.applyOperationHelper(operation2, immutableTree, (Node)iterable, (WriteTreeRef)object));
        }
        if (syncPoint != null) {
            iterable2.addAll(syncPoint.applyOperation(operation, writeTreeRef, node));
        }
        return iterable2;
    }

    private List<Event> applyOperationToSyncPoints(Operation operation) {
        return this.applyOperationHelper(operation, this.syncPointTree, null, this.pendingWriteTree.childWrites(Path.getEmptyPath()));
    }

    private List<? extends Event> applyTaggedOperation(QuerySpec object, Operation operation) {
        SyncPoint syncPoint = this.syncPointTree.get((Path)(object = ((QuerySpec)object).getPath()));
        boolean bl = syncPoint != null;
        Utilities.hardAssert(bl, "Missing sync point for query tag that we're tracking");
        return syncPoint.applyOperation(operation, this.pendingWriteTree.childWrites((Path)object), null);
    }

    private List<View> collectDistinctViewsForSubTree(ImmutableTree<SyncPoint> immutableTree) {
        ArrayList<View> arrayList = new ArrayList<View>();
        this.collectDistinctViewsForSubTree(immutableTree, arrayList);
        return arrayList;
    }

    private void collectDistinctViewsForSubTree(ImmutableTree<SyncPoint> object, List<View> list) {
        SyncPoint syncPoint = ((ImmutableTree)object).getValue();
        if (syncPoint != null && syncPoint.hasCompleteView()) {
            list.add(syncPoint.getCompleteView());
        } else {
            if (syncPoint != null) {
                list.addAll(syncPoint.getQueryViews());
            }
            object = ((ImmutableTree)object).getChildren().iterator();
            while (object.hasNext()) {
                this.collectDistinctViewsForSubTree((ImmutableTree)((Map.Entry)object.next()).getValue(), list);
            }
        }
    }

    private Tag getNextQueryTag() {
        long l = this.nextQueryTag;
        this.nextQueryTag = 1L + l;
        return new Tag(l);
    }

    private QuerySpec queryForListening(QuerySpec querySpec) {
        if (querySpec.loadsAllData() && !querySpec.isDefault()) {
            return QuerySpec.defaultQueryAtPath(querySpec.getPath());
        }
        return querySpec;
    }

    private QuerySpec queryForTag(Tag tag) {
        return this.tagToQueryMap.get(tag);
    }

    private List<Event> removeEventRegistration(QuerySpec querySpec, EventRegistration eventRegistration, DatabaseError databaseError) {
        return this.persistenceManager.runInTransaction(new Callable<List<Event>>(this, querySpec, eventRegistration, databaseError){
            final SyncTree this$0;
            final DatabaseError val$cancelError;
            final EventRegistration val$eventRegistration;
            final QuerySpec val$query;
            {
                this.this$0 = syncTree;
                this.val$query = querySpec;
                this.val$eventRegistration = eventRegistration;
                this.val$cancelError = databaseError;
            }

            @Override
            public List<Event> call() {
                Object object5;
                block10: {
                    boolean bl;
                    Iterator<Object> iterator2;
                    boolean bl2;
                    List list;
                    Object object2;
                    Object object3;
                    Object object42;
                    block9: {
                        boolean bl3;
                        object42 = this.val$query.getPath();
                        object3 = (SyncPoint)this.this$0.syncPointTree.get((Path)object42);
                        object5 = new ArrayList<Event>();
                        if (object3 == null || !this.val$query.isDefault() && !((SyncPoint)object3).viewExistsForQuery(this.val$query)) break block10;
                        object5 = ((SyncPoint)object3).removeEventRegistration(this.val$query, this.val$eventRegistration, this.val$cancelError);
                        if (((SyncPoint)object3).isEmpty()) {
                            object2 = this.this$0;
                            SyncTree.access$702((SyncTree)object2, ((SyncTree)object2).syncPointTree.remove((Path)object42));
                        }
                        list = (List)((Pair)object5).getFirst();
                        object2 = (List)((Pair)object5).getSecond();
                        bl2 = false;
                        for (Object object5 : list) {
                            this.this$0.persistenceManager.setQueryInactive(this.val$query);
                            bl3 = bl2 || ((QuerySpec)object5).loadsAllData();
                            bl2 = bl3;
                        }
                        object5 = this.this$0.syncPointTree;
                        bl3 = ((ImmutableTree)object5).getValue() != null && ((SyncPoint)((ImmutableTree)object5).getValue()).hasCompleteView();
                        iterator2 = ((Path)object42).iterator();
                        do {
                            bl = bl3;
                            if (!iterator2.hasNext()) break block9;
                            object5 = ((ImmutableTree)object5).getChild((ChildKey)iterator2.next());
                            bl3 = bl3 || ((ImmutableTree)object5).getValue() != null && ((SyncPoint)((ImmutableTree)object5).getValue()).hasCompleteView();
                            bl = bl3;
                            if (bl3) break block9;
                        } while (!((ImmutableTree)object5).isEmpty());
                        bl = bl3;
                    }
                    if (bl2 && !bl && !((ImmutableTree)(object5 = this.this$0.syncPointTree.subtree((Path)object42))).isEmpty()) {
                        iterator2 = this.this$0.collectDistinctViewsForSubTree((ImmutableTree)object5).iterator();
                        object5 = object3;
                        while (iterator2.hasNext()) {
                            Object object6 = (View)iterator2.next();
                            object3 = new ListenContainer(this.this$0, (View)object6);
                            object6 = ((View)object6).getQuery();
                            this.this$0.listenProvider.startListening(this.this$0.queryForListening((QuerySpec)object6), ((ListenContainer)object3).tag, (ListenHashProvider)object3, (CompletionListener)object3);
                        }
                    }
                    if (!bl && !list.isEmpty() && this.val$cancelError == null) {
                        if (bl2) {
                            this.this$0.listenProvider.stopListening(this.this$0.queryForListening(this.val$query), null);
                        } else {
                            for (Object object42 : list) {
                                object5 = this.this$0.tagForQuery((QuerySpec)object42);
                                boolean bl4 = object5 != null;
                                Utilities.hardAssert(bl4);
                                this.this$0.listenProvider.stopListening(this.this$0.queryForListening((QuerySpec)object42), (Tag)object5);
                            }
                        }
                    }
                    this.this$0.removeTags(list);
                    object5 = object2;
                }
                return object5;
            }
        });
    }

    private void removeTags(List<QuerySpec> object) {
        Iterator<QuerySpec> iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            QuerySpec querySpec = iterator2.next();
            if (querySpec.loadsAllData()) continue;
            object = this.tagForQuery(querySpec);
            boolean bl = object != null;
            Utilities.hardAssert(bl);
            this.queryToTagMap.remove(querySpec);
            this.tagToQueryMap.remove(object);
        }
    }

    private void setupListener(QuerySpec object, View object2) {
        Path path = ((QuerySpec)object).getPath();
        Tag tag = this.tagForQuery((QuerySpec)object);
        object2 = new ListenContainer(this, (View)object2);
        this.listenProvider.startListening(this.queryForListening((QuerySpec)object), tag, (ListenHashProvider)object2, (CompletionListener)object2);
        object = this.syncPointTree.subtree(path);
        if (tag != null) {
            Utilities.hardAssert(((SyncPoint)((ImmutableTree)object).getValue()).hasCompleteView() ^ true, "If we're adding a query, it shouldn't be shadowed");
        } else {
            ((ImmutableTree)object).foreach(new ImmutableTree.TreeVisitor<SyncPoint, Void>(this){
                final SyncTree this$0;
                {
                    this.this$0 = syncTree;
                }

                @Override
                public Void onNodeValue(Path object, SyncPoint object2, Void void_) {
                    if (!((Path)object).isEmpty() && ((SyncPoint)object2).hasCompleteView()) {
                        object = ((SyncPoint)object2).getCompleteView().getQuery();
                        this.this$0.listenProvider.stopListening(this.this$0.queryForListening((QuerySpec)object), this.this$0.tagForQuery((QuerySpec)object));
                    } else {
                        object = ((SyncPoint)object2).getQueryViews().iterator();
                        while (object.hasNext()) {
                            object2 = ((View)object.next()).getQuery();
                            this.this$0.listenProvider.stopListening(this.this$0.queryForListening((QuerySpec)object2), this.this$0.tagForQuery((QuerySpec)object2));
                        }
                    }
                    return null;
                }
            });
        }
    }

    private Tag tagForQuery(QuerySpec querySpec) {
        return this.queryToTagMap.get(querySpec);
    }

    public List<? extends Event> ackUserWrite(long l, boolean bl, boolean bl2, Clock clock) {
        return this.persistenceManager.runInTransaction(new Callable<List<? extends Event>>(this, bl2, l, bl, clock){
            final SyncTree this$0;
            final boolean val$persist;
            final boolean val$revert;
            final Clock val$serverClock;
            final long val$writeId;
            {
                this.this$0 = syncTree;
                this.val$persist = bl;
                this.val$writeId = l;
                this.val$revert = bl2;
                this.val$serverClock = clock;
            }

            @Override
            public List<? extends Event> call() {
                Object object;
                ImmutableTree<Boolean> immutableTree;
                if (this.val$persist) {
                    this.this$0.persistenceManager.removeUserWrite(this.val$writeId);
                }
                UserWriteRecord userWriteRecord = this.this$0.pendingWriteTree.getWrite(this.val$writeId);
                boolean bl = this.this$0.pendingWriteTree.removeWrite(this.val$writeId);
                if (userWriteRecord.isVisible() && !this.val$revert) {
                    immutableTree = ServerValues.generateServerValues(this.val$serverClock);
                    if (userWriteRecord.isOverwrite()) {
                        immutableTree = ServerValues.resolveDeferredValueSnapshot(userWriteRecord.getOverwrite(), this.this$0, userWriteRecord.getPath(), (Map<String, Object>)((Object)immutableTree));
                        this.this$0.persistenceManager.applyUserWriteToServerCache(userWriteRecord.getPath(), (Node)((Object)immutableTree));
                    } else {
                        immutableTree = ServerValues.resolveDeferredValueMerge(userWriteRecord.getMerge(), this.this$0, userWriteRecord.getPath(), (Map<String, Object>)((Object)immutableTree));
                        this.this$0.persistenceManager.applyUserWriteToServerCache(userWriteRecord.getPath(), (CompoundWrite)((Object)immutableTree));
                    }
                }
                if (!bl) {
                    return Collections.emptyList();
                }
                immutableTree = ImmutableTree.emptyInstance();
                if (userWriteRecord.isOverwrite()) {
                    object = immutableTree.set(Path.getEmptyPath(), true);
                } else {
                    Iterator<Map.Entry<Path, Node>> iterator2 = userWriteRecord.getMerge().iterator();
                    while (true) {
                        object = immutableTree;
                        if (!iterator2.hasNext()) break;
                        immutableTree = immutableTree.set(iterator2.next().getKey(), true);
                    }
                }
                return this.this$0.applyOperationToSyncPoints(new AckUserWrite(userWriteRecord.getPath(), (ImmutableTree<Boolean>)object, this.val$revert));
            }
        });
    }

    public List<? extends Event> addEventRegistration(EventRegistration eventRegistration) {
        return this.persistenceManager.runInTransaction(new Callable<List<? extends Event>>(this, eventRegistration){
            final SyncTree this$0;
            final EventRegistration val$eventRegistration;
            {
                this.this$0 = syncTree;
                this.val$eventRegistration = eventRegistration;
            }

            @Override
            public List<? extends Event> call() {
                Object object;
                Object object2;
                boolean bl;
                QuerySpec querySpec = this.val$eventRegistration.getQuerySpec();
                Path path = querySpec.getPath();
                Object object3 = null;
                boolean bl2 = false;
                Object object4 = this.this$0.syncPointTree;
                Object object5 = path;
                while (true) {
                    bl = ((ImmutableTree)object4).isEmpty();
                    boolean bl3 = true;
                    if (bl) break;
                    object2 = (SyncPoint)((ImmutableTree)object4).getValue();
                    object = object3;
                    boolean bl4 = bl2;
                    if (object2 != null) {
                        if (object3 == null) {
                            object3 = ((SyncPoint)object2).getCompleteServerCache((Path)object5);
                        }
                        bl2 = !bl2 && !((SyncPoint)object2).hasCompleteView() ? false : bl3;
                        bl4 = bl2;
                        object = object3;
                    }
                    object3 = ((Path)object5).isEmpty() ? ChildKey.fromString("") : ((Path)object5).getFront();
                    object4 = ((ImmutableTree)object4).getChild((ChildKey)object3);
                    object5 = ((Path)object5).popFront();
                    object3 = object;
                    bl2 = bl4;
                }
                object5 = (SyncPoint)this.this$0.syncPointTree.get(path);
                if (object5 == null) {
                    object5 = new SyncPoint(this.this$0.persistenceManager);
                    object4 = this.this$0;
                    SyncTree.access$702(object4, ((SyncTree)object4).syncPointTree.set(path, object5));
                } else {
                    bl2 = bl2 || ((SyncPoint)object5).hasCompleteView();
                    if (object3 == null) {
                        object3 = ((SyncPoint)object5).getCompleteServerCache(Path.getEmptyPath());
                    }
                }
                this.this$0.persistenceManager.setQueryActive(querySpec);
                if (object3 != null) {
                    object3 = new CacheNode(IndexedNode.from((Node)object3, querySpec.getIndex()), true, false);
                } else {
                    object = this.this$0.persistenceManager.serverCache(querySpec);
                    if (((CacheNode)object).isFullyInitialized()) {
                        object3 = object;
                    } else {
                        object3 = EmptyNode.Empty();
                        for (Map.Entry entry : this.this$0.syncPointTree.subtree(path).getChildren()) {
                            Object object6 = (SyncPoint)((ImmutableTree)entry.getValue()).getValue();
                            object4 = object3;
                            if (object6 != null) {
                                object6 = ((SyncPoint)object6).getCompleteServerCache(Path.getEmptyPath());
                                object4 = object3;
                                if (object6 != null) {
                                    object4 = object3.updateImmediateChild((ChildKey)entry.getKey(), (Node)object6);
                                }
                            }
                            object3 = object4;
                        }
                        object = ((CacheNode)object).getNode().iterator();
                        object4 = object3;
                        while (object.hasNext()) {
                            object2 = (NamedNode)object.next();
                            object3 = object4;
                            if (!object4.hasChild(((NamedNode)object2).getName())) {
                                object3 = object4.updateImmediateChild(((NamedNode)object2).getName(), ((NamedNode)object2).getNode());
                            }
                            object4 = object3;
                        }
                        object3 = new CacheNode(IndexedNode.from(object4, querySpec.getIndex()), false, false);
                    }
                }
                bl = ((SyncPoint)object5).viewExistsForQuery(querySpec);
                if (!bl && !querySpec.loadsAllData()) {
                    Utilities.hardAssert(true ^ this.this$0.queryToTagMap.containsKey(querySpec), "View does not exist but we have a tag");
                    object4 = this.this$0.getNextQueryTag();
                    this.this$0.queryToTagMap.put(querySpec, object4);
                    this.this$0.tagToQueryMap.put(object4, querySpec);
                }
                object4 = this.this$0.pendingWriteTree.childWrites(path);
                object3 = ((SyncPoint)object5).addEventRegistration(this.val$eventRegistration, (WriteTreeRef)object4, (CacheNode)object3);
                if (!bl && !bl2) {
                    object5 = ((SyncPoint)object5).viewForQuery(querySpec);
                    this.this$0.setupListener(querySpec, (View)object5);
                }
                return object3;
            }
        });
    }

    public List<? extends Event> applyListenComplete(Path path) {
        return this.persistenceManager.runInTransaction(new Callable<List<? extends Event>>(this, path){
            final SyncTree this$0;
            final Path val$path;
            {
                this.this$0 = syncTree;
                this.val$path = path;
            }

            @Override
            public List<? extends Event> call() {
                this.this$0.persistenceManager.setQueryComplete(QuerySpec.defaultQueryAtPath(this.val$path));
                return this.this$0.applyOperationToSyncPoints(new ListenComplete(OperationSource.SERVER, this.val$path));
            }
        });
    }

    public List<? extends Event> applyServerMerge(Path path, Map<Path, Node> map) {
        return this.persistenceManager.runInTransaction(new Callable<List<? extends Event>>(this, map, path){
            final SyncTree this$0;
            final Map val$changedChildren;
            final Path val$path;
            {
                this.this$0 = syncTree;
                this.val$changedChildren = map;
                this.val$path = path;
            }

            @Override
            public List<? extends Event> call() {
                CompoundWrite compoundWrite = CompoundWrite.fromPathMerge(this.val$changedChildren);
                this.this$0.persistenceManager.updateServerCache(this.val$path, compoundWrite);
                return this.this$0.applyOperationToSyncPoints(new Merge(OperationSource.SERVER, this.val$path, compoundWrite));
            }
        });
    }

    public List<? extends Event> applyServerOverwrite(Path path, Node node) {
        return this.persistenceManager.runInTransaction(new Callable<List<? extends Event>>(this, path, node){
            final SyncTree this$0;
            final Node val$newData;
            final Path val$path;
            {
                this.this$0 = syncTree;
                this.val$path = path;
                this.val$newData = node;
            }

            @Override
            public List<? extends Event> call() {
                this.this$0.persistenceManager.updateServerCache(QuerySpec.defaultQueryAtPath(this.val$path), this.val$newData);
                return this.this$0.applyOperationToSyncPoints(new Overwrite(OperationSource.SERVER, this.val$path, this.val$newData));
            }
        });
    }

    public List<? extends Event> applyServerRangeMerges(Path path, List<RangeMerge> object) {
        Object object2 = this.syncPointTree.get(path);
        if (object2 == null) {
            return Collections.emptyList();
        }
        if ((object2 = ((SyncPoint)object2).getCompleteView()) != null) {
            object2 = ((View)object2).getServerCache();
            Iterator<RangeMerge> iterator2 = object.iterator();
            object = object2;
            while (iterator2.hasNext()) {
                object = iterator2.next().applyTo((Node)object);
            }
            return this.applyServerOverwrite(path, (Node)object);
        }
        return Collections.emptyList();
    }

    public List<? extends Event> applyTaggedListenComplete(Tag tag) {
        return this.persistenceManager.runInTransaction(new Callable<List<? extends Event>>(this, tag){
            final SyncTree this$0;
            final Tag val$tag;
            {
                this.this$0 = syncTree;
                this.val$tag = tag;
            }

            @Override
            public List<? extends Event> call() {
                QuerySpec querySpec = this.this$0.queryForTag(this.val$tag);
                if (querySpec != null) {
                    this.this$0.persistenceManager.setQueryComplete(querySpec);
                    ListenComplete listenComplete = new ListenComplete(OperationSource.forServerTaggedQuery(querySpec.getParams()), Path.getEmptyPath());
                    return this.this$0.applyTaggedOperation(querySpec, listenComplete);
                }
                return Collections.emptyList();
            }
        });
    }

    public List<? extends Event> applyTaggedQueryMerge(Path path, Map<Path, Node> map, Tag tag) {
        return this.persistenceManager.runInTransaction(new Callable<List<? extends Event>>(this, tag, path, map){
            final SyncTree this$0;
            final Map val$changedChildren;
            final Path val$path;
            final Tag val$tag;
            {
                this.this$0 = syncTree;
                this.val$tag = tag;
                this.val$path = path;
                this.val$changedChildren = map;
            }

            @Override
            public List<? extends Event> call() {
                QuerySpec querySpec = this.this$0.queryForTag(this.val$tag);
                if (querySpec != null) {
                    Object object = Path.getRelative(querySpec.getPath(), this.val$path);
                    CompoundWrite compoundWrite = CompoundWrite.fromPathMerge(this.val$changedChildren);
                    this.this$0.persistenceManager.updateServerCache(this.val$path, compoundWrite);
                    object = new Merge(OperationSource.forServerTaggedQuery(querySpec.getParams()), (Path)object, compoundWrite);
                    return this.this$0.applyTaggedOperation(querySpec, (Operation)object);
                }
                return Collections.emptyList();
            }
        });
    }

    public List<? extends Event> applyTaggedQueryOverwrite(Path path, Node node, Tag tag) {
        return this.persistenceManager.runInTransaction(new Callable<List<? extends Event>>(this, tag, path, node){
            final SyncTree this$0;
            final Path val$path;
            final Node val$snap;
            final Tag val$tag;
            {
                this.this$0 = syncTree;
                this.val$tag = tag;
                this.val$path = path;
                this.val$snap = node;
            }

            @Override
            public List<? extends Event> call() {
                QuerySpec querySpec = this.this$0.queryForTag(this.val$tag);
                if (querySpec != null) {
                    Path path = Path.getRelative(querySpec.getPath(), this.val$path);
                    Object object = path.isEmpty() ? querySpec : QuerySpec.defaultQueryAtPath(this.val$path);
                    this.this$0.persistenceManager.updateServerCache((QuerySpec)object, this.val$snap);
                    object = new Overwrite(OperationSource.forServerTaggedQuery(querySpec.getParams()), path, this.val$snap);
                    return this.this$0.applyTaggedOperation(querySpec, (Operation)object);
                }
                return Collections.emptyList();
            }
        });
    }

    public List<? extends Event> applyTaggedRangeMerges(Path path, List<RangeMerge> object, Tag tag) {
        Object object2 = this.queryForTag(tag);
        if (object2 != null) {
            Utilities.hardAssert(path.equals(((QuerySpec)object2).getPath()));
            Object object3 = this.syncPointTree.get(((QuerySpec)object2).getPath());
            boolean bl = true;
            boolean bl2 = object3 != null;
            Utilities.hardAssert(bl2, "Missing sync point for query tag that we're tracking");
            object3 = ((SyncPoint)object3).viewForQuery((QuerySpec)object2);
            bl2 = object3 != null ? bl : false;
            Utilities.hardAssert(bl2, "Missing view for query tag that we're tracking");
            object3 = ((View)object3).getServerCache();
            object2 = object.iterator();
            object = object3;
            while (object2.hasNext()) {
                object = ((RangeMerge)object2.next()).applyTo((Node)object);
            }
            return this.applyTaggedQueryOverwrite(path, (Node)object, tag);
        }
        return Collections.emptyList();
    }

    public List<? extends Event> applyUserMerge(Path path, CompoundWrite compoundWrite, CompoundWrite compoundWrite2, long l, boolean bl) {
        return this.persistenceManager.runInTransaction(new Callable<List<? extends Event>>(this, bl, path, compoundWrite, l, compoundWrite2){
            final SyncTree this$0;
            final CompoundWrite val$children;
            final Path val$path;
            final boolean val$persist;
            final CompoundWrite val$unresolvedChildren;
            final long val$writeId;
            {
                this.this$0 = syncTree;
                this.val$persist = bl;
                this.val$path = path;
                this.val$unresolvedChildren = compoundWrite;
                this.val$writeId = l;
                this.val$children = compoundWrite2;
            }

            @Override
            public List<? extends Event> call() throws Exception {
                if (this.val$persist) {
                    this.this$0.persistenceManager.saveUserMerge(this.val$path, this.val$unresolvedChildren, this.val$writeId);
                }
                this.this$0.pendingWriteTree.addMerge(this.val$path, this.val$children, this.val$writeId);
                return this.this$0.applyOperationToSyncPoints(new Merge(OperationSource.USER, this.val$path, this.val$children));
            }
        });
    }

    public List<? extends Event> applyUserOverwrite(Path path, Node node, Node node2, long l, boolean bl, boolean bl2) {
        boolean bl3 = bl || !bl2;
        Utilities.hardAssert(bl3, "We shouldn't be persisting non-visible writes.");
        return this.persistenceManager.runInTransaction(new Callable<List<? extends Event>>(this, bl2, path, node, l, node2, bl){
            final SyncTree this$0;
            final Node val$newData;
            final Node val$newDataUnresolved;
            final Path val$path;
            final boolean val$persist;
            final boolean val$visible;
            final long val$writeId;
            {
                this.this$0 = syncTree;
                this.val$persist = bl;
                this.val$path = path;
                this.val$newDataUnresolved = node;
                this.val$writeId = l;
                this.val$newData = node2;
                this.val$visible = bl2;
            }

            @Override
            public List<? extends Event> call() {
                if (this.val$persist) {
                    this.this$0.persistenceManager.saveUserOverwrite(this.val$path, this.val$newDataUnresolved, this.val$writeId);
                }
                this.this$0.pendingWriteTree.addOverwrite(this.val$path, this.val$newData, this.val$writeId, this.val$visible);
                if (!this.val$visible) {
                    return Collections.emptyList();
                }
                return this.this$0.applyOperationToSyncPoints(new Overwrite(OperationSource.USER, this.val$path, this.val$newData));
            }
        });
    }

    public Node calcCompleteEventCache(Path path, List<Long> list) {
        Comparable<ChildKey> comparable;
        ImmutableTree immutableTree = this.syncPointTree;
        Iterable iterable = immutableTree.getValue();
        Comparable<ChildKey> comparable2 = null;
        Path path2 = path;
        iterable = Path.getEmptyPath();
        do {
            comparable = path2.getFront();
            path2 = path2.popFront();
            Path path3 = ((Path)iterable).child((ChildKey)comparable);
            Path path4 = Path.getRelative(path3, path);
            iterable = comparable != null ? immutableTree.getChild((ChildKey)comparable) : ImmutableTree.emptyInstance();
            immutableTree = (SyncPoint)iterable.getValue();
            comparable = comparable2;
            if (immutableTree != null) {
                comparable = ((SyncPoint)((Object)immutableTree)).getCompleteServerCache(path4);
            }
            if (path2.isEmpty()) break;
            immutableTree = iterable;
            comparable2 = comparable;
            iterable = path3;
        } while (comparable == null);
        return this.pendingWriteTree.calcCompleteEventCache(path, (Node)comparable, list, true);
    }

    public Node calcCompleteEventCacheFromRoot(Path path, List<Long> list) {
        SyncPoint syncPoint = this.syncPointTree.getValue();
        Node node = null;
        if (syncPoint != null) {
            node = syncPoint.getCompleteServerCache(Path.getEmptyPath());
        }
        if (node != null) {
            return this.pendingWriteTree.calcCompleteEventCache(path, node, list, true);
        }
        return this.calcCompleteEventCache(path, list);
    }

    public Node getServerValue(QuerySpec querySpec) {
        return (Node)this.persistenceManager.runInTransaction(new SyncTree$$ExternalSyntheticLambda0(this, querySpec));
    }

    ImmutableTree<SyncPoint> getSyncPointTree() {
        return this.syncPointTree;
    }

    public boolean isEmpty() {
        return this.syncPointTree.isEmpty();
    }

    public void keepSynced(QuerySpec querySpec, boolean bl) {
        if (bl && !this.keepSyncedQueries.contains(querySpec)) {
            this.addEventRegistration(new KeepSyncedEventRegistration(querySpec));
            this.keepSyncedQueries.add(querySpec);
        } else if (!bl && this.keepSyncedQueries.contains(querySpec)) {
            this.removeEventRegistration(new KeepSyncedEventRegistration(querySpec));
            this.keepSyncedQueries.remove(querySpec);
        }
    }

    public /* synthetic */ Node lambda$getServerValue$0$com-google-firebase-database-core-SyncTree(QuerySpec querySpec) throws Exception {
        boolean bl;
        Path path = querySpec.getPath();
        Object object = null;
        boolean bl2 = false;
        Object object2 = this.syncPointTree;
        Object object3 = path;
        while (true) {
            boolean bl3 = ((ImmutableTree)object2).isEmpty();
            bl = true;
            boolean bl4 = true;
            if (bl3) break;
            SyncPoint syncPoint = ((ImmutableTree)object2).getValue();
            Node node = object;
            boolean bl5 = bl2;
            if (syncPoint != null) {
                if (object == null) {
                    object = syncPoint.getCompleteServerCache((Path)object3);
                }
                bl5 = !bl2 && !syncPoint.hasCompleteView() ? false : bl4;
                node = object;
            }
            object = ((Path)object3).isEmpty() ? ChildKey.fromString("") : ((Path)object3).getFront();
            object2 = ((ImmutableTree)object2).getChild((ChildKey)object);
            object3 = ((Path)object3).popFront();
            object = node;
            bl2 = bl5;
        }
        object3 = this.syncPointTree.get(path);
        if (object3 == null) {
            object3 = new SyncPoint(this.persistenceManager);
            this.syncPointTree = this.syncPointTree.set(path, (SyncPoint)object3);
        } else if (object == null) {
            object = ((SyncPoint)object3).getCompleteServerCache(Path.getEmptyPath());
        }
        object2 = object != null ? object : EmptyNode.Empty();
        object2 = IndexedNode.from(object2, querySpec.getIndex());
        if (object == null) {
            bl = false;
        }
        object = new CacheNode((IndexedNode)object2, bl, false);
        return ((SyncPoint)object3).getView(querySpec, this.pendingWriteTree.childWrites(path), (CacheNode)object).getCompleteNode();
    }

    public DataSnapshot persistenceServerCache(Query query) {
        return InternalHelpers.createDataSnapshot(query.getRef(), this.persistenceManager.serverCache(query.getSpec()).getIndexedNode());
    }

    public List<Event> removeAllEventRegistrations(QuerySpec querySpec, DatabaseError databaseError) {
        return this.removeEventRegistration(querySpec, null, databaseError);
    }

    public List<? extends Event> removeAllWrites() {
        return this.persistenceManager.runInTransaction(new Callable<List<? extends Event>>(this){
            final SyncTree this$0;
            {
                this.this$0 = syncTree;
            }

            @Override
            public List<? extends Event> call() throws Exception {
                this.this$0.persistenceManager.removeAllUserWrites();
                if (this.this$0.pendingWriteTree.purgeAllWrites().isEmpty()) {
                    return Collections.emptyList();
                }
                ImmutableTree<Boolean> immutableTree = new ImmutableTree<Boolean>(true);
                return this.this$0.applyOperationToSyncPoints(new AckUserWrite(Path.getEmptyPath(), immutableTree, true));
            }
        });
    }

    public List<Event> removeEventRegistration(EventRegistration eventRegistration) {
        return this.removeEventRegistration(eventRegistration.getQuerySpec(), eventRegistration, null);
    }

    public void setQueryActive(QuerySpec querySpec) {
        this.persistenceManager.runInTransaction(new Callable<Void>(this, querySpec){
            final SyncTree this$0;
            final QuerySpec val$query;
            {
                this.this$0 = syncTree;
                this.val$query = querySpec;
            }

            @Override
            public Void call() {
                this.this$0.persistenceManager.setQueryActive(this.val$query);
                return null;
            }
        });
    }

    public void setQueryInactive(QuerySpec querySpec) {
        this.persistenceManager.runInTransaction(new Callable<Void>(this, querySpec){
            final SyncTree this$0;
            final QuerySpec val$query;
            {
                this.this$0 = syncTree;
                this.val$query = querySpec;
            }

            @Override
            public Void call() {
                this.this$0.persistenceManager.setQueryInactive(this.val$query);
                return null;
            }
        });
    }

    public static interface CompletionListener {
        public List<? extends Event> onListenComplete(DatabaseError var1);
    }

    private static class KeepSyncedEventRegistration
    extends EventRegistration {
        private QuerySpec spec;

        public KeepSyncedEventRegistration(QuerySpec querySpec) {
            this.spec = querySpec;
        }

        @Override
        public EventRegistration clone(QuerySpec querySpec) {
            return new KeepSyncedEventRegistration(querySpec);
        }

        @Override
        public DataEvent createEvent(Change change, QuerySpec querySpec) {
            return null;
        }

        public boolean equals(Object object) {
            boolean bl = object instanceof KeepSyncedEventRegistration && ((KeepSyncedEventRegistration)object).spec.equals(this.spec);
            return bl;
        }

        @Override
        public void fireCancelEvent(DatabaseError databaseError) {
        }

        @Override
        public void fireEvent(DataEvent dataEvent) {
        }

        @Override
        public QuerySpec getQuerySpec() {
            return this.spec;
        }

        public int hashCode() {
            return this.spec.hashCode();
        }

        @Override
        public boolean isSameListener(EventRegistration eventRegistration) {
            return eventRegistration instanceof KeepSyncedEventRegistration;
        }

        @Override
        public boolean respondsTo(Event.EventType eventType) {
            return false;
        }
    }

    private class ListenContainer
    implements ListenHashProvider,
    CompletionListener {
        private final Tag tag;
        final SyncTree this$0;
        private final View view;

        public ListenContainer(SyncTree syncTree, View view) {
            this.this$0 = syncTree;
            this.view = view;
            this.tag = syncTree.tagForQuery(view.getQuery());
        }

        @Override
        public CompoundHash getCompoundHash() {
            com.google.firebase.database.snapshot.CompoundHash compoundHash = com.google.firebase.database.snapshot.CompoundHash.fromNode(this.view.getServerCache());
            Object object = compoundHash.getPosts();
            ArrayList<List<String>> arrayList = new ArrayList<List<String>>(object.size());
            object = object.iterator();
            while (object.hasNext()) {
                arrayList.add(((Path)object.next()).asList());
            }
            return new CompoundHash(arrayList, compoundHash.getHashes());
        }

        @Override
        public String getSimpleHash() {
            return this.view.getServerCache().getHash();
        }

        @Override
        public List<? extends Event> onListenComplete(DatabaseError object) {
            if (object == null) {
                QuerySpec querySpec = this.view.getQuery();
                object = this.tag;
                if (object != null) {
                    return this.this$0.applyTaggedListenComplete((Tag)object);
                }
                return this.this$0.applyListenComplete(querySpec.getPath());
            }
            LogWrapper logWrapper = this.this$0.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Listen at ");
            stringBuilder.append(this.view.getQuery().getPath());
            stringBuilder.append(" failed: ");
            stringBuilder.append(((DatabaseError)object).toString());
            logWrapper.warn(stringBuilder.toString());
            return this.this$0.removeAllEventRegistrations(this.view.getQuery(), (DatabaseError)object);
        }

        @Override
        public boolean shouldIncludeCompoundHash() {
            boolean bl = NodeSizeEstimator.estimateSerializedNodeSize(this.view.getServerCache()) > 1024L;
            return bl;
        }
    }

    public static interface ListenProvider {
        public void startListening(QuerySpec var1, Tag var2, ListenHashProvider var3, CompletionListener var4);

        public void stopListening(QuerySpec var1, Tag var2);
    }
}

