/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.InternalHelpers;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.connection.HostInfo;
import com.google.firebase.database.connection.ListenHashProvider;
import com.google.firebase.database.connection.PersistentConnection;
import com.google.firebase.database.connection.RequestResultCallback;
import com.google.firebase.database.core.CompoundWrite;
import com.google.firebase.database.core.Constants;
import com.google.firebase.database.core.Context;
import com.google.firebase.database.core.EventRegistration;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.Repo$9$$ExternalSyntheticLambda0;
import com.google.firebase.database.core.Repo$9$$ExternalSyntheticLambda1;
import com.google.firebase.database.core.RepoInfo;
import com.google.firebase.database.core.ServerValues;
import com.google.firebase.database.core.SnapshotHolder;
import com.google.firebase.database.core.SparseSnapshotTree;
import com.google.firebase.database.core.SyncTree;
import com.google.firebase.database.core.Tag;
import com.google.firebase.database.core.TokenProvider;
import com.google.firebase.database.core.UserWriteRecord;
import com.google.firebase.database.core.ValueEventRegistration;
import com.google.firebase.database.core.persistence.NoopPersistenceManager;
import com.google.firebase.database.core.persistence.PersistenceManager;
import com.google.firebase.database.core.utilities.DefaultClock;
import com.google.firebase.database.core.utilities.DefaultRunLoop;
import com.google.firebase.database.core.utilities.OffsetClock;
import com.google.firebase.database.core.utilities.Tree;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.Event;
import com.google.firebase.database.core.view.EventRaiser;
import com.google.firebase.database.core.view.QuerySpec;
import com.google.firebase.database.logging.LogWrapper;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.NodeUtilities;
import com.google.firebase.database.snapshot.RangeMerge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Repo
implements PersistentConnection.Delegate {
    private static final int GET_TIMEOUT_MS = 3000;
    private static final String INTERRUPT_REASON = "repo_interrupt";
    private static final int TRANSACTION_MAX_RETRIES = 25;
    private static final String TRANSACTION_OVERRIDE_BY_SET = "overriddenBySet";
    private static final String TRANSACTION_TOO_MANY_RETRIES = "maxretries";
    private PersistentConnection connection;
    private final Context ctx;
    private final LogWrapper dataLogger;
    public long dataUpdateCount = 0L;
    private FirebaseDatabase database;
    private final EventRaiser eventRaiser;
    private boolean hijackHash = false;
    private SnapshotHolder infoData;
    private SyncTree infoSyncTree;
    private boolean loggedTransactionPersistenceWarning = false;
    private long nextWriteId = 1L;
    private SparseSnapshotTree onDisconnect;
    private final LogWrapper operationLogger;
    private final RepoInfo repoInfo;
    private final OffsetClock serverClock = new OffsetClock(new DefaultClock(), 0L);
    private SyncTree serverSyncTree;
    private final LogWrapper transactionLogger;
    private long transactionOrder = 0L;
    private Tree<List<TransactionData>> transactionQueueTree;

    Repo(RepoInfo repoInfo, Context context, FirebaseDatabase firebaseDatabase) {
        this.repoInfo = repoInfo;
        this.ctx = context;
        this.database = firebaseDatabase;
        this.operationLogger = context.getLogger("RepoOperation");
        this.transactionLogger = context.getLogger("Transaction");
        this.dataLogger = context.getLogger("DataOperation");
        this.eventRaiser = new EventRaiser(context);
        this.scheduleNow(new Runnable(this){
            final Repo this$0;
            {
                this.this$0 = repo;
            }

            @Override
            public void run() {
                this.this$0.deferredInitialization();
            }
        });
    }

    private Path abortTransactions(Path object, int n) {
        Path path = this.getAncestorTransactionNode((Path)object).getPath();
        if (this.transactionLogger.logsDebug()) {
            LogWrapper logWrapper = this.operationLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Aborting transactions for path: ");
            stringBuilder.append(object);
            stringBuilder.append(". Affected: ");
            stringBuilder.append(path);
            logWrapper.debug(stringBuilder.toString(), new Object[0]);
        }
        object = this.transactionQueueTree.subTree((Path)object);
        ((Tree)object).forEachAncestor(new Tree.TreeFilter<List<TransactionData>>(this, n){
            final Repo this$0;
            final int val$reason;
            {
                this.this$0 = repo;
                this.val$reason = n;
            }

            @Override
            public boolean filterTreeNode(Tree<List<TransactionData>> tree) {
                this.this$0.abortTransactionsAtNode(tree, this.val$reason);
                return false;
            }
        });
        this.abortTransactionsAtNode((Tree<List<TransactionData>>)object, n);
        ((Tree)object).forEachDescendant(new Tree.TreeVisitor<List<TransactionData>>(this, n){
            final Repo this$0;
            final int val$reason;
            {
                this.this$0 = repo;
                this.val$reason = n;
            }

            @Override
            public void visitTree(Tree<List<TransactionData>> tree) {
                this.this$0.abortTransactionsAtNode(tree, this.val$reason);
            }
        });
        return path;
    }

    private void abortTransactionsAtNode(Tree<List<TransactionData>> object, int n) {
        List<TransactionData> list = ((Tree)object).getValue();
        ArrayList<? extends Event> arrayList = new ArrayList<Event>();
        if (list != null) {
            boolean bl;
            Object object2;
            ArrayList<25> arrayList2 = new ArrayList<25>();
            if (n == -9) {
                object2 = DatabaseError.fromStatus(TRANSACTION_OVERRIDE_BY_SET);
            } else {
                bl = n == -25;
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("Unknown transaction abort reason: ");
                ((StringBuilder)object2).append(n);
                Utilities.hardAssert(bl, ((StringBuilder)object2).toString());
                object2 = DatabaseError.fromCode(-25);
            }
            int n2 = -1;
            for (int i = 0; i < list.size(); ++i) {
                TransactionData transactionData = list.get(i);
                if (transactionData.status == TransactionStatus.SENT_NEEDS_ABORT) continue;
                if (transactionData.status == TransactionStatus.SENT) {
                    bl = n2 == i - 1;
                    Utilities.hardAssert(bl);
                    TransactionData.access$1802(transactionData, TransactionStatus.SENT_NEEDS_ABORT);
                    TransactionData.access$2902(transactionData, (DatabaseError)object2);
                    n2 = i;
                    continue;
                }
                bl = transactionData.status == TransactionStatus.RUN;
                Utilities.hardAssert(bl);
                this.removeEventCallback(new ValueEventRegistration(this, transactionData.outstandingListener, QuerySpec.defaultQueryAtPath(transactionData.path)));
                if (n == -9) {
                    arrayList.addAll(this.serverSyncTree.ackUserWrite(transactionData.currentWriteId, true, false, this.serverClock));
                } else {
                    bl = n == -25;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown transaction abort reason: ");
                    stringBuilder.append(n);
                    Utilities.hardAssert(bl, stringBuilder.toString());
                }
                arrayList2.add(new Runnable(this, transactionData, (DatabaseError)object2){
                    final Repo this$0;
                    final DatabaseError val$abortError;
                    final TransactionData val$transaction;
                    {
                        this.this$0 = repo;
                        this.val$transaction = transactionData;
                        this.val$abortError = databaseError;
                    }

                    @Override
                    public void run() {
                        this.val$transaction.handler.onComplete(this.val$abortError, false, null);
                    }
                });
            }
            if (n2 == -1) {
                ((Tree)object).setValue(null);
            } else {
                ((Tree)object).setValue(list.subList(0, n2 + 1));
            }
            this.postEvents(arrayList);
            object = arrayList2.iterator();
            while (object.hasNext()) {
                this.postEvent((Runnable)object.next());
            }
        }
    }

    private void ackWriteAndRerunTransactions(long l, Path path, DatabaseError list) {
        if (list == null || ((DatabaseError)((Object)list)).getCode() != -25) {
            boolean bl = list == null;
            list = this.serverSyncTree;
            boolean bl2 = !bl;
            if ((list = ((SyncTree)((Object)list)).ackUserWrite(l, bl2, true, this.serverClock)).size() > 0) {
                this.rerunTransactions(path);
            }
            this.postEvents(list);
        }
    }

    private void aggregateTransactionQueues(List<TransactionData> list, Tree<List<TransactionData>> tree) {
        List<TransactionData> list2 = tree.getValue();
        if (list2 != null) {
            list.addAll(list2);
        }
        tree.forEachChild(new Tree.TreeVisitor<List<TransactionData>>(this, list){
            final Repo this$0;
            final List val$queue;
            {
                this.this$0 = repo;
                this.val$queue = list;
            }

            @Override
            public void visitTree(Tree<List<TransactionData>> tree) {
                this.this$0.aggregateTransactionQueues(this.val$queue, tree);
            }
        });
    }

    private List<TransactionData> buildTransactionQueue(Tree<List<TransactionData>> tree) {
        ArrayList<TransactionData> arrayList = new ArrayList<TransactionData>();
        this.aggregateTransactionQueues(arrayList, tree);
        Collections.sort(arrayList);
        return arrayList;
    }

    private void deferredInitialization() {
        Object object = new HostInfo(this.repoInfo.host, this.repoInfo.namespace, this.repoInfo.secure);
        this.connection = this.ctx.newPersistentConnection((HostInfo)object, this);
        this.ctx.getAuthTokenProvider().addTokenChangeListener(((DefaultRunLoop)this.ctx.getRunLoop()).getExecutorService(), new TokenProvider.TokenChangeListener(this){
            final Repo this$0;
            {
                this.this$0 = repo;
            }

            @Override
            public void onTokenChange() {
                this.this$0.operationLogger.debug("Auth token changed, triggering auth token refresh", new Object[0]);
                this.this$0.connection.refreshAuthToken();
            }

            @Override
            public void onTokenChange(String string2) {
                this.this$0.operationLogger.debug("Auth token changed, triggering auth token refresh", new Object[0]);
                this.this$0.connection.refreshAuthToken(string2);
            }
        });
        this.ctx.getAppCheckTokenProvider().addTokenChangeListener(((DefaultRunLoop)this.ctx.getRunLoop()).getExecutorService(), new TokenProvider.TokenChangeListener(this){
            final Repo this$0;
            {
                this.this$0 = repo;
            }

            @Override
            public void onTokenChange() {
                this.this$0.operationLogger.debug("App check token changed, triggering app check token refresh", new Object[0]);
                this.this$0.connection.refreshAppCheckToken();
            }

            @Override
            public void onTokenChange(String string2) {
                this.this$0.operationLogger.debug("App check token changed, triggering app check token refresh", new Object[0]);
                this.this$0.connection.refreshAppCheckToken(string2);
            }
        });
        this.connection.initialize();
        object = this.ctx.getPersistenceManager(this.repoInfo.host);
        this.infoData = new SnapshotHolder();
        this.onDisconnect = new SparseSnapshotTree();
        this.transactionQueueTree = new Tree();
        this.infoSyncTree = new SyncTree(this.ctx, new NoopPersistenceManager(), new SyncTree.ListenProvider(this){
            final Repo this$0;
            {
                this.this$0 = repo;
            }

            @Override
            public void startListening(QuerySpec querySpec, Tag tag, ListenHashProvider listenHashProvider, SyncTree.CompletionListener completionListener) {
                this.this$0.scheduleNow(new Runnable(this, querySpec, completionListener){
                    final 4 this$1;
                    final SyncTree.CompletionListener val$onComplete;
                    final QuerySpec val$query;
                    {
                        this.this$1 = var1_1;
                        this.val$query = querySpec;
                        this.val$onComplete = completionListener;
                    }

                    @Override
                    public void run() {
                        Iterable<NamedNode> iterable = this.this$1.this$0.infoData.getNode(this.val$query.getPath());
                        if (!iterable.isEmpty()) {
                            iterable = this.this$1.this$0.infoSyncTree.applyServerOverwrite(this.val$query.getPath(), (Node)iterable);
                            this.this$1.this$0.postEvents((List)iterable);
                            this.val$onComplete.onListenComplete(null);
                        }
                    }
                });
            }

            @Override
            public void stopListening(QuerySpec querySpec, Tag tag) {
            }
        });
        this.serverSyncTree = new SyncTree(this.ctx, (PersistenceManager)object, new SyncTree.ListenProvider(this){
            final Repo this$0;
            {
                this.this$0 = repo;
            }

            @Override
            public void startListening(QuerySpec object, Tag tag, ListenHashProvider listenHashProvider, SyncTree.CompletionListener completionListener) {
                PersistentConnection persistentConnection = this.this$0.connection;
                List<String> list = ((QuerySpec)object).getPath().asList();
                Map<String, Object> map = ((QuerySpec)object).getParams().getWireProtocolParams();
                object = tag != null ? Long.valueOf(tag.getTagNumber()) : null;
                persistentConnection.listen(list, map, listenHashProvider, (Long)object, new RequestResultCallback(this, completionListener){
                    final 5 this$1;
                    final SyncTree.CompletionListener val$onListenComplete;
                    {
                        this.this$1 = var1_1;
                        this.val$onListenComplete = completionListener;
                    }

                    @Override
                    public void onRequestResult(String list, String string2) {
                        list = Repo.fromErrorCode((String)((Object)list), string2);
                        list = this.val$onListenComplete.onListenComplete((DatabaseError)((Object)list));
                        this.this$1.this$0.postEvents(list);
                    }
                });
            }

            @Override
            public void stopListening(QuerySpec querySpec, Tag tag) {
                this.this$0.connection.unlisten(querySpec.getPath().asList(), querySpec.getParams().getWireProtocolParams());
            }
        });
        this.restoreWrites((PersistenceManager)object);
        object = Constants.DOT_INFO_AUTHENTICATED;
        Boolean bl = false;
        this.updateInfo((ChildKey)object, bl);
        this.updateInfo(Constants.DOT_INFO_CONNECTED, bl);
    }

    private static DatabaseError fromErrorCode(String string2, String string3) {
        if (string2 != null) {
            return DatabaseError.fromStatus(string2, string3);
        }
        return null;
    }

    private Tree<List<TransactionData>> getAncestorTransactionNode(Path path) {
        Tree<List<TransactionData>> tree = this.transactionQueueTree;
        while (!path.isEmpty() && tree.getValue() == null) {
            tree = tree.subTree(new Path(path.getFront()));
            path = path.popFront();
        }
        return tree;
    }

    private Node getLatestState(Path path) {
        return this.getLatestState(path, new ArrayList<Long>());
    }

    private Node getLatestState(Path iterable, List<Long> iterable2) {
        iterable2 = this.serverSyncTree.calcCompleteEventCache((Path)iterable, (List<Long>)iterable2);
        iterable = iterable2;
        if (iterable2 == null) {
            iterable = EmptyNode.Empty();
        }
        return iterable;
    }

    private long getNextWriteId() {
        long l = this.nextWriteId;
        this.nextWriteId = 1L + l;
        return l;
    }

    private long nextTransactionOrder() {
        long l = this.transactionOrder;
        this.transactionOrder = 1L + l;
        return l;
    }

    private void postEvents(List<? extends Event> list) {
        if (!list.isEmpty()) {
            this.eventRaiser.raiseEvents(list);
        }
    }

    private void pruneCompletedTransactions(Tree<List<TransactionData>> tree) {
        List<TransactionData> list = tree.getValue();
        if (list != null) {
            int n = 0;
            while (n < list.size()) {
                if (list.get(n).status == TransactionStatus.COMPLETED) {
                    list.remove(n);
                    continue;
                }
                ++n;
            }
            if (list.size() > 0) {
                tree.setValue(list);
            } else {
                tree.setValue(null);
            }
        }
        tree.forEachChild(new Tree.TreeVisitor<List<TransactionData>>(this){
            final Repo this$0;
            {
                this.this$0 = repo;
            }

            @Override
            public void visitTree(Tree<List<TransactionData>> tree) {
                this.this$0.pruneCompletedTransactions(tree);
            }
        });
    }

    private void rerunTransactionQueue(List<TransactionData> object, Path path) {
        int n;
        if (object.isEmpty()) {
            return;
        }
        ArrayList<21> arrayList = new ArrayList<21>();
        ArrayList<Long> arrayList2 = new ArrayList<Long>();
        Iterator<TransactionData> iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            arrayList2.add(iterator2.next().currentWriteId);
        }
        iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            Object object2;
            TransactionData transactionData = iterator2.next();
            object = Path.getRelative(path, transactionData.path);
            n = 0;
            Node node = null;
            ArrayList<? extends Event> arrayList3 = new ArrayList<Event>();
            boolean bl = object != null;
            Utilities.hardAssert(bl);
            if (transactionData.status == TransactionStatus.NEEDS_ABORT) {
                n = 1;
                object = transactionData.abortReason;
                if (((DatabaseError)object).getCode() != -25) {
                    arrayList3.addAll(this.serverSyncTree.ackUserWrite(transactionData.currentWriteId, true, false, this.serverClock));
                }
            } else if (transactionData.status == TransactionStatus.RUN) {
                if (transactionData.retryCount >= 25) {
                    n = 1;
                    object = DatabaseError.fromStatus(TRANSACTION_TOO_MANY_RETRIES);
                    arrayList3.addAll(this.serverSyncTree.ackUserWrite(transactionData.currentWriteId, true, false, this.serverClock));
                } else {
                    Node node2 = this.getLatestState(transactionData.path, arrayList2);
                    TransactionData.access$1502(transactionData, node2);
                    object2 = InternalHelpers.createMutableData(node2);
                    object = null;
                    try {
                        object2 = transactionData.handler.doTransaction((MutableData)object2);
                    }
                    catch (Throwable throwable) {
                        this.operationLogger.error("Caught Throwable.", throwable);
                        object = DatabaseError.fromException(throwable);
                        object2 = Transaction.abort();
                    }
                    if (((Transaction.Result)object2).isSuccess()) {
                        object = transactionData.currentWriteId;
                        Map<String, Object> map = ServerValues.generateServerValues(this.serverClock);
                        object2 = ((Transaction.Result)object2).getNode();
                        node2 = ServerValues.resolveDeferredValueSnapshot((Node)object2, node2, map);
                        TransactionData.access$1602(transactionData, (Node)object2);
                        TransactionData.access$1702(transactionData, node2);
                        TransactionData.access$1902(transactionData, this.getNextWriteId());
                        arrayList2.remove(object);
                        arrayList3.addAll(this.serverSyncTree.applyUserOverwrite(transactionData.path, (Node)object2, node2, transactionData.currentWriteId, transactionData.applyLocally, false));
                        arrayList3.addAll(this.serverSyncTree.ackUserWrite((Long)object, true, false, this.serverClock));
                        object = node;
                    } else {
                        n = 1;
                        arrayList3.addAll(this.serverSyncTree.ackUserWrite(transactionData.currentWriteId, true, false, this.serverClock));
                    }
                }
            } else {
                object = node;
            }
            this.postEvents(arrayList3);
            if (n == 0) continue;
            TransactionData.access$1802(transactionData, TransactionStatus.COMPLETED);
            object2 = InternalHelpers.createReference(this, transactionData.path);
            node = transactionData.currentInputSnapshot;
            object2 = InternalHelpers.createDataSnapshot((DatabaseReference)object2, IndexedNode.from(node));
            this.scheduleNow(new Runnable(this, transactionData){
                final Repo this$0;
                final TransactionData val$transaction;
                {
                    this.this$0 = repo;
                    this.val$transaction = transactionData;
                }

                @Override
                public void run() {
                    Repo repo = this.this$0;
                    repo.removeEventCallback(new ValueEventRegistration(repo, this.val$transaction.outstandingListener, QuerySpec.defaultQueryAtPath(this.val$transaction.path)));
                }
            });
            arrayList.add(new Runnable(this, transactionData, (DatabaseError)object, (DataSnapshot)object2){
                final Repo this$0;
                final DatabaseError val$callbackError;
                final DataSnapshot val$snapshot;
                final TransactionData val$transaction;
                {
                    this.this$0 = repo;
                    this.val$transaction = transactionData;
                    this.val$callbackError = databaseError;
                    this.val$snapshot = dataSnapshot;
                }

                @Override
                public void run() {
                    this.val$transaction.handler.onComplete(this.val$callbackError, false, this.val$snapshot);
                }
            });
        }
        this.pruneCompletedTransactions(this.transactionQueueTree);
        for (n = 0; n < arrayList.size(); ++n) {
            this.postEvent((Runnable)arrayList.get(n));
        }
        this.sendAllReadyTransactions();
    }

    private Path rerunTransactions(Path object) {
        object = this.getAncestorTransactionNode((Path)object);
        Path path = ((Tree)object).getPath();
        this.rerunTransactionQueue(this.buildTransactionQueue((Tree<List<TransactionData>>)object), path);
        return path;
    }

    private void restoreWrites(PersistenceManager object) {
        Object object2 = object.loadUserWrites();
        object = ServerValues.generateServerValues(this.serverClock);
        long l = Long.MIN_VALUE;
        Iterator<UserWriteRecord> iterator2 = object2.iterator();
        while (iterator2.hasNext()) {
            object2 = iterator2.next();
            Object object3 = new RequestResultCallback(this, (UserWriteRecord)object2){
                final Repo this$0;
                final UserWriteRecord val$write;
                {
                    this.this$0 = repo;
                    this.val$write = userWriteRecord;
                }

                @Override
                public void onRequestResult(String object, String string2) {
                    object = Repo.fromErrorCode((String)object, string2);
                    this.this$0.warnIfWriteFailed("Persisted write", this.val$write.getPath(), (DatabaseError)object);
                    this.this$0.ackWriteAndRerunTransactions(this.val$write.getWriteId(), this.val$write.getPath(), (DatabaseError)object);
                }
            };
            if (l < ((UserWriteRecord)object2).getWriteId()) {
                Object object4;
                Object object5;
                l = ((UserWriteRecord)object2).getWriteId();
                this.nextWriteId = ((UserWriteRecord)object2).getWriteId() + 1L;
                if (((UserWriteRecord)object2).isOverwrite()) {
                    if (this.operationLogger.logsDebug()) {
                        object5 = this.operationLogger;
                        object4 = new StringBuilder();
                        ((StringBuilder)object4).append("Restoring overwrite with id ");
                        ((StringBuilder)object4).append(((UserWriteRecord)object2).getWriteId());
                        ((LogWrapper)object5).debug(((StringBuilder)object4).toString(), new Object[0]);
                    }
                    this.connection.put(((UserWriteRecord)object2).getPath().asList(), ((UserWriteRecord)object2).getOverwrite().getValue(true), (RequestResultCallback)object3);
                    object3 = ServerValues.resolveDeferredValueSnapshot(((UserWriteRecord)object2).getOverwrite(), this.serverSyncTree, ((UserWriteRecord)object2).getPath(), (Map<String, Object>)object);
                    this.serverSyncTree.applyUserOverwrite(((UserWriteRecord)object2).getPath(), ((UserWriteRecord)object2).getOverwrite(), (Node)object3, ((UserWriteRecord)object2).getWriteId(), true, false);
                    continue;
                }
                if (this.operationLogger.logsDebug()) {
                    object4 = this.operationLogger;
                    object5 = new StringBuilder();
                    ((StringBuilder)object5).append("Restoring merge with id ");
                    ((StringBuilder)object5).append(((UserWriteRecord)object2).getWriteId());
                    ((LogWrapper)object4).debug(((StringBuilder)object5).toString(), new Object[0]);
                }
                this.connection.merge(((UserWriteRecord)object2).getPath().asList(), ((UserWriteRecord)object2).getMerge().getValue(true), (RequestResultCallback)object3);
                object3 = ServerValues.resolveDeferredValueMerge(((UserWriteRecord)object2).getMerge(), this.serverSyncTree, ((UserWriteRecord)object2).getPath(), (Map<String, Object>)object);
                this.serverSyncTree.applyUserMerge(((UserWriteRecord)object2).getPath(), ((UserWriteRecord)object2).getMerge(), (CompoundWrite)object3, ((UserWriteRecord)object2).getWriteId(), false);
                continue;
            }
            throw new IllegalStateException("Write ids were not in order.");
        }
    }

    private void runOnDisconnectEvents() {
        Map<String, Object> map = ServerValues.generateServerValues(this.serverClock);
        ArrayList arrayList = new ArrayList();
        this.onDisconnect.forEachTree(Path.getEmptyPath(), new SparseSnapshotTree.SparseSnapshotTreeVisitor(this, map, arrayList){
            final Repo this$0;
            final List val$events;
            final Map val$serverValues;
            {
                this.this$0 = repo;
                this.val$serverValues = map;
                this.val$events = list;
            }

            @Override
            public void visitTree(Path path, Node node) {
                node = ServerValues.resolveDeferredValueSnapshot(node, this.this$0.serverSyncTree.calcCompleteEventCache(path, new ArrayList<Long>()), (Map<String, Object>)this.val$serverValues);
                this.val$events.addAll(this.this$0.serverSyncTree.applyServerOverwrite(path, node));
                path = this.this$0.abortTransactions(path, -9);
                this.this$0.rerunTransactions(path);
            }
        });
        this.onDisconnect = new SparseSnapshotTree();
        this.postEvents(arrayList);
    }

    private void sendAllReadyTransactions() {
        Tree<List<TransactionData>> tree = this.transactionQueueTree;
        this.pruneCompletedTransactions(tree);
        this.sendReadyTransactions(tree);
    }

    private void sendReadyTransactions(Tree<List<TransactionData>> tree) {
        if (tree.getValue() != null) {
            Boolean bl;
            List<TransactionData> list;
            block5: {
                list = this.buildTransactionQueue(tree);
                boolean bl2 = list.size() > 0;
                Utilities.hardAssert(bl2);
                Boolean bl3 = true;
                Iterator<TransactionData> iterator2 = list.iterator();
                do {
                    bl = bl3;
                    if (!iterator2.hasNext()) break block5;
                } while (iterator2.next().status == TransactionStatus.RUN);
                bl = false;
            }
            if (bl.booleanValue()) {
                this.sendTransactionQueue(list, tree.getPath());
            }
        } else if (tree.hasChildren()) {
            tree.forEachChild(new Tree.TreeVisitor<List<TransactionData>>(this){
                final Repo this$0;
                {
                    this.this$0 = repo;
                }

                @Override
                public void visitTree(Tree<List<TransactionData>> tree) {
                    this.this$0.sendReadyTransactions(tree);
                }
            });
        }
    }

    private void sendTransactionQueue(List<TransactionData> list, Path path) {
        Object object;
        Object object2 = new ArrayList<Long>();
        Object object3 = list.iterator();
        while (object3.hasNext()) {
            object2.add(object3.next().currentWriteId);
        }
        object3 = object = this.getLatestState(path, (List<Long>)object2);
        object2 = "badhash";
        if (!this.hijackHash) {
            object2 = object.getHash();
        }
        object = list.iterator();
        while (true) {
            boolean bl = object.hasNext();
            boolean bl2 = true;
            if (!bl) break;
            TransactionData transactionData = (TransactionData)object.next();
            if (transactionData.status != TransactionStatus.RUN) {
                bl2 = false;
            }
            Utilities.hardAssert(bl2);
            TransactionData.access$1802(transactionData, TransactionStatus.SENT);
            TransactionData.access$2108(transactionData);
            object3 = object3.updateChild(Path.getRelative(path, transactionData.path), transactionData.currentOutputSnapshotRaw);
        }
        object3 = object3.getValue(true);
        this.connection.compareAndPut(path.asList(), object3, (String)object2, new RequestResultCallback(this, path, list, this){
            final Repo this$0;
            final Path val$path;
            final List val$queue;
            final Repo val$repo;
            {
                this.this$0 = repo;
                this.val$path = path;
                this.val$queue = list;
                this.val$repo = repo2;
            }

            @Override
            public void onRequestResult(String object5, String object2) {
                object2 = Repo.fromErrorCode((String)object5, (String)object2);
                this.this$0.warnIfWriteFailed("Transaction", this.val$path, (DatabaseError)object2);
                object5 = new ArrayList();
                if (object2 == null) {
                    Object object32;
                    object2 = new ArrayList();
                    for (Object object32 : this.val$queue) {
                        TransactionData.access$1802((TransactionData)object32, TransactionStatus.COMPLETED);
                        object5.addAll(this.this$0.serverSyncTree.ackUserWrite(((TransactionData)object32).currentWriteId, false, false, this.this$0.serverClock));
                        Object object4 = ((TransactionData)object32).currentOutputSnapshotResolved;
                        object2.add(new Runnable(this, (TransactionData)object32, InternalHelpers.createDataSnapshot(InternalHelpers.createReference(this.val$repo, ((TransactionData)object32).path), IndexedNode.from((Node)object4))){
                            final 18 this$1;
                            final DataSnapshot val$snap;
                            final TransactionData val$txn;
                            {
                                this.this$1 = var1_1;
                                this.val$txn = transactionData;
                                this.val$snap = dataSnapshot;
                            }

                            @Override
                            public void run() {
                                this.val$txn.handler.onComplete(null, true, this.val$snap);
                            }
                        });
                        object4 = this.this$0;
                        ((Repo)object4).removeEventCallback(new ValueEventRegistration((Repo)object4, ((TransactionData)object32).outstandingListener, QuerySpec.defaultQueryAtPath(((TransactionData)object32).path)));
                    }
                    object32 = this.this$0;
                    ((Repo)object32).pruneCompletedTransactions(((Repo)object32).transactionQueueTree.subTree(this.val$path));
                    this.this$0.sendAllReadyTransactions();
                    this.val$repo.postEvents((List)object5);
                    for (int i = 0; i < object2.size(); ++i) {
                        this.this$0.postEvent((Runnable)object2.get(i));
                    }
                } else {
                    if (((DatabaseError)object2).getCode() == -1) {
                        for (Object object5 : this.val$queue) {
                            if (((TransactionData)object5).status == TransactionStatus.SENT_NEEDS_ABORT) {
                                TransactionData.access$1802((TransactionData)object5, TransactionStatus.NEEDS_ABORT);
                                continue;
                            }
                            TransactionData.access$1802((TransactionData)object5, TransactionStatus.RUN);
                        }
                    } else {
                        for (Object object5 : this.val$queue) {
                            TransactionData.access$1802((TransactionData)object5, TransactionStatus.NEEDS_ABORT);
                            TransactionData.access$2902((TransactionData)object5, (DatabaseError)object2);
                        }
                    }
                    this.this$0.rerunTransactions(this.val$path);
                }
            }
        });
    }

    private void updateInfo(ChildKey comparable, Object object) {
        if (comparable.equals(Constants.DOT_INFO_SERVERTIME_OFFSET)) {
            this.serverClock.setOffset((Long)object);
        }
        comparable = new Path(new ChildKey[]{Constants.DOT_INFO, comparable});
        try {
            object = NodeUtilities.NodeFromJSON(object);
            this.infoData.update((Path)comparable, (Node)object);
            this.postEvents(this.infoSyncTree.applyServerOverwrite((Path)comparable, (Node)object));
        }
        catch (DatabaseException databaseException) {
            this.operationLogger.error("Failed to parse info update", databaseException);
        }
    }

    private void warnIfWriteFailed(String string2, Path path, DatabaseError databaseError) {
        if (databaseError != null && databaseError.getCode() != -1 && databaseError.getCode() != -25) {
            LogWrapper logWrapper = this.operationLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(" at ");
            stringBuilder.append(path.toString());
            stringBuilder.append(" failed: ");
            stringBuilder.append(databaseError.toString());
            logWrapper.warn(stringBuilder.toString());
        }
    }

    public void addEventCallback(EventRegistration list) {
        ChildKey childKey = ((EventRegistration)((Object)list)).getQuerySpec().getPath().getFront();
        list = childKey != null && childKey.equals(Constants.DOT_INFO) ? this.infoSyncTree.addEventRegistration((EventRegistration)((Object)list)) : this.serverSyncTree.addEventRegistration((EventRegistration)((Object)list));
        this.postEvents(list);
    }

    void callOnComplete(DatabaseReference.CompletionListener completionListener, DatabaseError databaseError, Path object) {
        if (completionListener != null) {
            ChildKey childKey = ((Path)object).getBack();
            object = childKey != null && childKey.isPriorityChildName() ? InternalHelpers.createReference(this, ((Path)object).getParent()) : InternalHelpers.createReference(this, (Path)object);
            this.postEvent(new Runnable(this, completionListener, databaseError, (DatabaseReference)object){
                final Repo this$0;
                final DatabaseError val$error;
                final DatabaseReference.CompletionListener val$onComplete;
                final DatabaseReference val$ref;
                {
                    this.this$0 = repo;
                    this.val$onComplete = completionListener;
                    this.val$error = databaseError;
                    this.val$ref = databaseReference;
                }

                @Override
                public void run() {
                    this.val$onComplete.onComplete(this.val$error, this.val$ref);
                }
            });
        }
    }

    PersistentConnection getConnection() {
        return this.connection;
    }

    public FirebaseDatabase getDatabase() {
        return this.database;
    }

    SyncTree getInfoSyncTree() {
        return this.infoSyncTree;
    }

    public RepoInfo getRepoInfo() {
        return this.repoInfo;
    }

    SyncTree getServerSyncTree() {
        return this.serverSyncTree;
    }

    public long getServerTime() {
        return this.serverClock.millis();
    }

    public Task<DataSnapshot> getValue(Query query) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.scheduleNow(new Runnable(this, query, taskCompletionSource){
            final Repo this$0;
            final Query val$query;
            final TaskCompletionSource val$source;
            {
                this.this$0 = repo;
                this.val$query = query;
                this.val$source = taskCompletionSource;
            }

            static /* synthetic */ void lambda$run$0(TaskCompletionSource taskCompletionSource, DataSnapshot dataSnapshot) {
                taskCompletionSource.trySetResult(dataSnapshot);
            }

            public /* synthetic */ void lambda$run$1$com-google-firebase-database-core-Repo$9(TaskCompletionSource taskCompletionSource, DataSnapshot object, Query query, Task object2) {
                if (taskCompletionSource.getTask().isComplete()) {
                    return;
                }
                if (!((Task)object2).isSuccessful()) {
                    if (((DataSnapshot)object).exists()) {
                        taskCompletionSource.setResult(object);
                    } else {
                        object = ((Task)object2).getException();
                        object.getClass();
                        taskCompletionSource.setException((Exception)object);
                    }
                } else {
                    object2 = NodeUtilities.NodeFromJSON(((Task)object2).getResult());
                    object = this.this$0;
                    ((Repo)object).postEvents(((Repo)object).serverSyncTree.applyServerOverwrite(query.getPath(), (Node)object2));
                    taskCompletionSource.setResult(InternalHelpers.createDataSnapshot(query.getRef(), IndexedNode.from((Node)object2, query.getSpec().getIndex())));
                }
                this.this$0.serverSyncTree.setQueryInactive(query.getSpec());
            }

            @Override
            public void run() {
                Object object = this.this$0.serverSyncTree.getServerValue(this.val$query.getSpec());
                if (object != null) {
                    this.val$source.setResult(InternalHelpers.createDataSnapshot(this.val$query.getRef(), IndexedNode.from((Node)object)));
                    return;
                }
                this.this$0.serverSyncTree.setQueryActive(this.val$query.getSpec());
                object = this.this$0.serverSyncTree.persistenceServerCache(this.val$query);
                if (((DataSnapshot)object).exists()) {
                    this.this$0.scheduleDelayed(new Repo$9$$ExternalSyntheticLambda1(this.val$source, (DataSnapshot)object), 3000L);
                }
                this.this$0.connection.get(this.val$query.getPath().asList(), this.val$query.getSpec().getParams().getWireProtocolParams()).addOnCompleteListener(((DefaultRunLoop)this.this$0.ctx.getRunLoop()).getExecutorService(), (OnCompleteListener<Object>)new Repo$9$$ExternalSyntheticLambda0(this, this.val$source, (DataSnapshot)object, this.val$query));
            }
        });
        return taskCompletionSource.getTask();
    }

    boolean hasListeners() {
        boolean bl = !this.infoSyncTree.isEmpty() || !this.serverSyncTree.isEmpty();
        return bl;
    }

    void interrupt() {
        this.connection.interrupt(INTERRUPT_REASON);
    }

    public void keepSynced(QuerySpec querySpec, boolean bl) {
        boolean bl2 = querySpec.getPath().isEmpty() || !querySpec.getPath().getFront().equals(Constants.DOT_INFO);
        Utilities.hardAssert(bl2);
        this.serverSyncTree.keepSynced(querySpec, bl);
    }

    @Override
    public void onConnect() {
        this.onServerInfoUpdate(Constants.DOT_INFO_CONNECTED, true);
    }

    @Override
    public void onConnectionStatus(boolean bl) {
        this.onServerInfoUpdate(Constants.DOT_INFO_AUTHENTICATED, bl);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void onDataUpdate(List<String> list, Object object, boolean bl, Long hashMap) {
        DatabaseException databaseException2;
        block13: {
            Path path;
            block12: {
                Object object22;
                path = new Path(list);
                if (this.operationLogger.logsDebug()) {
                    list = this.operationLogger;
                    object22 = new StringBuilder();
                    ((StringBuilder)object22).append("onDataUpdate: ");
                    ((StringBuilder)object22).append(path);
                    ((LogWrapper)((Object)list)).debug(((StringBuilder)object22).toString(), new Object[0]);
                }
                if (this.dataLogger.logsDebug()) {
                    list = this.operationLogger;
                    object22 = new StringBuilder();
                    ((StringBuilder)object22).append("onDataUpdate: ");
                    ((StringBuilder)object22).append(path);
                    ((StringBuilder)object22).append(" ");
                    ((StringBuilder)object22).append(object);
                    ((LogWrapper)((Object)list)).debug(((StringBuilder)object22).toString(), new Object[0]);
                }
                ++this.dataUpdateCount;
                if (hashMap != null) {
                    try {
                        list = new List<Event>(((Long)((Object)hashMap)).longValue());
                        if (bl) {
                            hashMap = new HashMap<Path, Node>();
                            for (Object object22 : ((Map)object).entrySet()) {
                                Node node = NodeUtilities.NodeFromJSON(object22.getValue());
                                Path path2 = new Path((String)object22.getKey());
                                hashMap.put(path2, node);
                            }
                            list = this.serverSyncTree.applyTaggedQueryMerge(path, (Map<Path, Node>)hashMap, (Tag)((Object)list));
                            break block12;
                        } else {
                            object = NodeUtilities.NodeFromJSON(object);
                            list = this.serverSyncTree.applyTaggedQueryOverwrite(path, (Node)object, (Tag)((Object)list));
                        }
                        break block12;
                    }
                    catch (DatabaseException databaseException2) {
                        break block13;
                    }
                }
                if (bl) {
                    list = new List<Event>();
                    for (Map.Entry entry : ((Map)object).entrySet()) {
                        object22 = NodeUtilities.NodeFromJSON(entry.getValue());
                        object = new Path((String)entry.getKey());
                        list.put(object, object22);
                    }
                    list = this.serverSyncTree.applyServerMerge(path, (Map<Path, Node>)((Object)list));
                } else {
                    list = NodeUtilities.NodeFromJSON(object);
                    list = this.serverSyncTree.applyServerOverwrite(path, (Node)((Object)list));
                }
            }
            if (list.size() > 0) {
                this.rerunTransactions(path);
            }
            this.postEvents(list);
            return;
        }
        this.operationLogger.error("FIREBASE INTERNAL ERROR", databaseException2);
    }

    @Override
    public void onDisconnect() {
        this.onServerInfoUpdate(Constants.DOT_INFO_CONNECTED, false);
        this.runOnDisconnectEvents();
    }

    public void onDisconnectCancel(Path path, DatabaseReference.CompletionListener completionListener) {
        this.connection.onDisconnectCancel(path.asList(), new RequestResultCallback(this, path, completionListener){
            final Repo this$0;
            final DatabaseReference.CompletionListener val$onComplete;
            final Path val$path;
            {
                this.this$0 = repo;
                this.val$path = path;
                this.val$onComplete = completionListener;
            }

            @Override
            public void onRequestResult(String object, String string2) {
                if ((object = Repo.fromErrorCode((String)object, string2)) == null) {
                    this.this$0.onDisconnect.forget(this.val$path);
                }
                this.this$0.callOnComplete(this.val$onComplete, (DatabaseError)object, this.val$path);
            }
        });
    }

    public void onDisconnectSetValue(Path path, Node node, DatabaseReference.CompletionListener completionListener) {
        this.connection.onDisconnectPut(path.asList(), node.getValue(true), new RequestResultCallback(this, path, node, completionListener){
            final Repo this$0;
            final Node val$newValue;
            final DatabaseReference.CompletionListener val$onComplete;
            final Path val$path;
            {
                this.this$0 = repo;
                this.val$path = path;
                this.val$newValue = node;
                this.val$onComplete = completionListener;
            }

            @Override
            public void onRequestResult(String object, String string2) {
                object = Repo.fromErrorCode((String)object, string2);
                this.this$0.warnIfWriteFailed("onDisconnect().setValue", this.val$path, (DatabaseError)object);
                if (object == null) {
                    this.this$0.onDisconnect.remember(this.val$path, this.val$newValue);
                }
                this.this$0.callOnComplete(this.val$onComplete, (DatabaseError)object, this.val$path);
            }
        });
    }

    public void onDisconnectUpdate(Path path, Map<Path, Node> map, DatabaseReference.CompletionListener completionListener, Map<String, Object> map2) {
        this.connection.onDisconnectMerge(path.asList(), map2, new RequestResultCallback(this, path, map, completionListener){
            final Repo this$0;
            final DatabaseReference.CompletionListener val$listener;
            final Map val$newChildren;
            final Path val$path;
            {
                this.this$0 = repo;
                this.val$path = path;
                this.val$newChildren = map;
                this.val$listener = completionListener;
            }

            @Override
            public void onRequestResult(String object3, String object2) {
                DatabaseError databaseError;
                databaseError = Repo.fromErrorCode((String)object3, (String)((Object)databaseError));
                this.this$0.warnIfWriteFailed("onDisconnect().updateChildren", this.val$path, databaseError);
                if (databaseError == null) {
                    for (Map.Entry entry : this.val$newChildren.entrySet()) {
                        this.this$0.onDisconnect.remember(this.val$path.child((Path)entry.getKey()), (Node)entry.getValue());
                    }
                }
                this.this$0.callOnComplete(this.val$listener, databaseError, this.val$path);
            }
        });
    }

    @Override
    public void onRangeMergeUpdate(List<String> list, List<com.google.firebase.database.connection.RangeMerge> object, Long l) {
        Object object2;
        Path path = new Path(list);
        if (this.operationLogger.logsDebug()) {
            list = this.operationLogger;
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("onRangeMergeUpdate: ");
            ((StringBuilder)object2).append(path);
            ((LogWrapper)((Object)list)).debug(((StringBuilder)object2).toString(), new Object[0]);
        }
        if (this.dataLogger.logsDebug()) {
            object2 = this.operationLogger;
            list = new StringBuilder();
            ((StringBuilder)((Object)list)).append("onRangeMergeUpdate: ");
            ((StringBuilder)((Object)list)).append(path);
            ((StringBuilder)((Object)list)).append(" ");
            ((StringBuilder)((Object)list)).append(object);
            ((LogWrapper)object2).debug(((StringBuilder)((Object)list)).toString(), new Object[0]);
        }
        ++this.dataUpdateCount;
        list = new ArrayList<RangeMerge>(object.size());
        object = object.iterator();
        while (object.hasNext()) {
            list.add(new RangeMerge((com.google.firebase.database.connection.RangeMerge)object.next()));
        }
        list = l != null ? this.serverSyncTree.applyTaggedRangeMerges(path, list, new Tag(l)) : this.serverSyncTree.applyServerRangeMerges(path, list);
        if (list.size() > 0) {
            this.rerunTransactions(path);
        }
        this.postEvents(list);
    }

    public void onServerInfoUpdate(ChildKey childKey, Object object) {
        this.updateInfo(childKey, object);
    }

    @Override
    public void onServerInfoUpdate(Map<String, Object> object) {
        for (Map.Entry entry : object.entrySet()) {
            this.updateInfo(ChildKey.fromString((String)entry.getKey()), entry.getValue());
        }
    }

    public void postEvent(Runnable runnable) {
        this.ctx.requireStarted();
        this.ctx.getEventTarget().postEvent(runnable);
    }

    public void purgeOutstandingWrites() {
        if (this.operationLogger.logsDebug()) {
            this.operationLogger.debug("Purging writes", new Object[0]);
        }
        this.postEvents(this.serverSyncTree.removeAllWrites());
        this.abortTransactions(Path.getEmptyPath(), -25);
        this.connection.purgeOutstandingWrites();
    }

    public void removeEventCallback(EventRegistration list) {
        list = Constants.DOT_INFO.equals(((EventRegistration)((Object)list)).getQuerySpec().getPath().getFront()) ? this.infoSyncTree.removeEventRegistration((EventRegistration)((Object)list)) : this.serverSyncTree.removeEventRegistration((EventRegistration)((Object)list));
        this.postEvents(list);
    }

    void resume() {
        this.connection.resume(INTERRUPT_REASON);
    }

    public void scheduleDelayed(Runnable runnable, long l) {
        this.ctx.requireStarted();
        this.ctx.getRunLoop().schedule(runnable, l);
    }

    public void scheduleNow(Runnable runnable) {
        this.ctx.requireStarted();
        this.ctx.getRunLoop().scheduleNow(runnable);
    }

    public void setHijackHash(boolean bl) {
        this.hijackHash = bl;
    }

    public void setValue(Path path, Node node, DatabaseReference.CompletionListener completionListener) {
        Comparable<StringBuilder> comparable;
        Object object;
        if (this.operationLogger.logsDebug()) {
            object = this.operationLogger;
            comparable = new StringBuilder();
            ((StringBuilder)comparable).append("set: ");
            ((StringBuilder)comparable).append(path);
            ((LogWrapper)object).debug(((StringBuilder)comparable).toString(), new Object[0]);
        }
        if (this.dataLogger.logsDebug()) {
            object = this.dataLogger;
            comparable = new StringBuilder();
            ((StringBuilder)comparable).append("set: ");
            ((StringBuilder)comparable).append(path);
            ((StringBuilder)comparable).append(" ");
            ((StringBuilder)comparable).append(node);
            ((LogWrapper)object).debug(((StringBuilder)comparable).toString(), new Object[0]);
        }
        object = ServerValues.generateServerValues(this.serverClock);
        comparable = this.serverSyncTree.calcCompleteEventCache(path, new ArrayList<Long>());
        comparable = ServerValues.resolveDeferredValueSnapshot(node, comparable, (Map<String, Object>)object);
        long l = this.getNextWriteId();
        this.postEvents(this.serverSyncTree.applyUserOverwrite(path, node, (Node)comparable, l, true, true));
        this.connection.put(path.asList(), node.getValue(true), new RequestResultCallback(this, path, l, completionListener){
            final Repo this$0;
            final DatabaseReference.CompletionListener val$onComplete;
            final Path val$path;
            final long val$writeId;
            {
                this.this$0 = repo;
                this.val$path = path;
                this.val$writeId = l;
                this.val$onComplete = completionListener;
            }

            @Override
            public void onRequestResult(String object, String string2) {
                object = Repo.fromErrorCode((String)object, string2);
                this.this$0.warnIfWriteFailed("setValue", this.val$path, (DatabaseError)object);
                this.this$0.ackWriteAndRerunTransactions(this.val$writeId, this.val$path, (DatabaseError)object);
                this.this$0.callOnComplete(this.val$onComplete, (DatabaseError)object, this.val$path);
            }
        });
        this.rerunTransactions(this.abortTransactions(path, -9));
    }

    public void startTransaction(Path path, Transaction.Handler object, boolean bl) {
        Object object2;
        TransactionData transactionData;
        DatabaseReference databaseReference;
        Tree<List<TransactionData>> tree;
        Object object3;
        block9: {
            if (this.operationLogger.logsDebug()) {
                object3 = this.operationLogger;
                tree = new StringBuilder();
                ((StringBuilder)((Object)tree)).append("transaction: ");
                ((StringBuilder)((Object)tree)).append(path);
                ((LogWrapper)object3).debug(((StringBuilder)((Object)tree)).toString(), new Object[0]);
            }
            if (this.dataLogger.logsDebug()) {
                object3 = this.operationLogger;
                tree = new StringBuilder();
                ((StringBuilder)((Object)tree)).append("transaction: ");
                ((StringBuilder)((Object)tree)).append(path);
                ((LogWrapper)object3).debug(((StringBuilder)((Object)tree)).toString(), new Object[0]);
            }
            if (this.ctx.isPersistenceEnabled() && !this.loggedTransactionPersistenceWarning) {
                this.loggedTransactionPersistenceWarning = true;
                this.transactionLogger.info("runTransaction() usage detected while persistence is enabled. Please be aware that transactions *will not* be persisted across database restarts.  See https://www.firebase.com/docs/android/guide/offline-capabilities.html#section-handling-transactions-offline for more details.");
            }
            databaseReference = InternalHelpers.createReference(this, path);
            object3 = new ValueEventListener(this){
                final Repo this$0;
                {
                    this.this$0 = repo;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                }
            };
            this.addEventCallback(new ValueEventRegistration(this, (ValueEventListener)object3, databaseReference.getSpec()));
            transactionData = new TransactionData(path, (Transaction.Handler)object, (ValueEventListener)object3, TransactionStatus.INITIALIZING, bl, this.nextTransactionOrder());
            object3 = this.getLatestState(path);
            TransactionData.access$1502(transactionData, (Node)object3);
            object3 = InternalHelpers.createMutableData((Node)object3);
            object3 = object.doTransaction((MutableData)object3);
            if (object3 == null) break block9;
            object2 = null;
        }
        try {
            object3 = new NullPointerException("Transaction returned null as result");
            throw object3;
        }
        catch (Throwable throwable) {
            this.operationLogger.error("Caught Throwable.", throwable);
            object3 = DatabaseError.fromException(throwable);
            tree = Transaction.abort();
            object2 = object3;
            object3 = tree;
        }
        if (!((Transaction.Result)object3).isSuccess()) {
            TransactionData.access$1602(transactionData, null);
            TransactionData.access$1702(transactionData, null);
            this.postEvent(new Runnable(this, (Transaction.Handler)object, (DatabaseError)object2, InternalHelpers.createDataSnapshot(databaseReference, IndexedNode.from(transactionData.currentInputSnapshot))){
                final Repo this$0;
                final Transaction.Handler val$handler;
                final DatabaseError val$innerClassError;
                final DataSnapshot val$snap;
                {
                    this.this$0 = repo;
                    this.val$handler = handler;
                    this.val$innerClassError = databaseError;
                    this.val$snap = dataSnapshot;
                }

                @Override
                public void run() {
                    this.val$handler.onComplete(this.val$innerClassError, false, this.val$snap);
                }
            });
        } else {
            TransactionData.access$1802(transactionData, TransactionStatus.RUN);
            tree = this.transactionQueueTree.subTree(path);
            object = tree.getValue();
            if (object == null) {
                object = new ArrayList<TransactionData>();
            }
            object.add(transactionData);
            tree.setValue((List<TransactionData>)object);
            object = ServerValues.generateServerValues(this.serverClock);
            object3 = ((Transaction.Result)object3).getNode();
            object = ServerValues.resolveDeferredValueSnapshot((Node)object3, transactionData.currentInputSnapshot, (Map<String, Object>)object);
            TransactionData.access$1602(transactionData, (Node)object3);
            TransactionData.access$1702(transactionData, (Node)object);
            TransactionData.access$1902(transactionData, this.getNextWriteId());
            this.postEvents(this.serverSyncTree.applyUserOverwrite(path, (Node)object3, (Node)object, transactionData.currentWriteId, bl, false));
            this.sendAllReadyTransactions();
        }
    }

    public String toString() {
        return this.repoInfo.toString();
    }

    public void updateChildren(Path path, CompoundWrite object, DatabaseReference.CompletionListener completionListener, Map<String, Object> map) {
        StringBuilder stringBuilder;
        Object object2;
        if (this.operationLogger.logsDebug()) {
            object2 = this.operationLogger;
            stringBuilder = new StringBuilder();
            stringBuilder.append("update: ");
            stringBuilder.append(path);
            ((LogWrapper)object2).debug(stringBuilder.toString(), new Object[0]);
        }
        if (this.dataLogger.logsDebug()) {
            object2 = this.dataLogger;
            stringBuilder = new StringBuilder();
            stringBuilder.append("update: ");
            stringBuilder.append(path);
            stringBuilder.append(" ");
            stringBuilder.append(map);
            ((LogWrapper)object2).debug(stringBuilder.toString(), new Object[0]);
        }
        if (((CompoundWrite)object).isEmpty()) {
            if (this.operationLogger.logsDebug()) {
                this.operationLogger.debug("update called with no changes. No-op", new Object[0]);
            }
            this.callOnComplete(completionListener, null, path);
            return;
        }
        object2 = ServerValues.generateServerValues(this.serverClock);
        object2 = ServerValues.resolveDeferredValueMerge((CompoundWrite)object, this.serverSyncTree, path, (Map<String, Object>)object2);
        long l = this.getNextWriteId();
        this.postEvents(this.serverSyncTree.applyUserMerge(path, (CompoundWrite)object, (CompoundWrite)object2, l, true));
        this.connection.merge(path.asList(), map, new RequestResultCallback(this, path, l, completionListener){
            final Repo this$0;
            final DatabaseReference.CompletionListener val$onComplete;
            final Path val$path;
            final long val$writeId;
            {
                this.this$0 = repo;
                this.val$path = path;
                this.val$writeId = l;
                this.val$onComplete = completionListener;
            }

            @Override
            public void onRequestResult(String object, String string2) {
                object = Repo.fromErrorCode((String)object, string2);
                this.this$0.warnIfWriteFailed("updateChildren", this.val$path, (DatabaseError)object);
                this.this$0.ackWriteAndRerunTransactions(this.val$writeId, this.val$path, (DatabaseError)object);
                this.this$0.callOnComplete(this.val$onComplete, (DatabaseError)object, this.val$path);
            }
        });
        object = ((CompoundWrite)object).iterator();
        while (object.hasNext()) {
            this.rerunTransactions(this.abortTransactions(path.child((Path)((Map.Entry)object.next()).getKey()), -9));
        }
    }

    private static class TransactionData
    implements Comparable<TransactionData> {
        private DatabaseError abortReason;
        private boolean applyLocally;
        private Node currentInputSnapshot;
        private Node currentOutputSnapshotRaw;
        private Node currentOutputSnapshotResolved;
        private long currentWriteId;
        private Transaction.Handler handler;
        private long order;
        private ValueEventListener outstandingListener;
        private Path path;
        private int retryCount;
        private TransactionStatus status;

        private TransactionData(Path path, Transaction.Handler handler, ValueEventListener valueEventListener, TransactionStatus transactionStatus, boolean bl, long l) {
            this.path = path;
            this.handler = handler;
            this.outstandingListener = valueEventListener;
            this.status = transactionStatus;
            this.retryCount = 0;
            this.applyLocally = bl;
            this.order = l;
            this.abortReason = null;
            this.currentInputSnapshot = null;
            this.currentOutputSnapshotRaw = null;
            this.currentOutputSnapshotResolved = null;
        }

        static /* synthetic */ Node access$1502(TransactionData transactionData, Node node) {
            transactionData.currentInputSnapshot = node;
            return node;
        }

        static /* synthetic */ Node access$1602(TransactionData transactionData, Node node) {
            transactionData.currentOutputSnapshotRaw = node;
            return node;
        }

        static /* synthetic */ Node access$1702(TransactionData transactionData, Node node) {
            transactionData.currentOutputSnapshotResolved = node;
            return node;
        }

        static /* synthetic */ TransactionStatus access$1802(TransactionData transactionData, TransactionStatus transactionStatus) {
            transactionData.status = transactionStatus;
            return transactionStatus;
        }

        static /* synthetic */ long access$1902(TransactionData transactionData, long l) {
            transactionData.currentWriteId = l;
            return l;
        }

        static /* synthetic */ int access$2108(TransactionData transactionData) {
            int n = transactionData.retryCount;
            transactionData.retryCount = n + 1;
            return n;
        }

        static /* synthetic */ DatabaseError access$2902(TransactionData transactionData, DatabaseError databaseError) {
            transactionData.abortReason = databaseError;
            return databaseError;
        }

        @Override
        public int compareTo(TransactionData transactionData) {
            long l = this.order;
            long l2 = transactionData.order;
            if (l < l2) {
                return -1;
            }
            if (l == l2) {
                return 0;
            }
            return 1;
        }
    }

    private static final class TransactionStatus
    extends Enum<TransactionStatus> {
        private static final TransactionStatus[] $VALUES;
        public static final /* enum */ TransactionStatus COMPLETED;
        public static final /* enum */ TransactionStatus INITIALIZING;
        public static final /* enum */ TransactionStatus NEEDS_ABORT;
        public static final /* enum */ TransactionStatus RUN;
        public static final /* enum */ TransactionStatus SENT;
        public static final /* enum */ TransactionStatus SENT_NEEDS_ABORT;

        static {
            TransactionStatus transactionStatus;
            TransactionStatus transactionStatus2;
            TransactionStatus transactionStatus3;
            TransactionStatus transactionStatus4;
            TransactionStatus transactionStatus5;
            TransactionStatus transactionStatus6;
            INITIALIZING = transactionStatus6 = new TransactionStatus();
            RUN = transactionStatus5 = new TransactionStatus();
            SENT = transactionStatus4 = new TransactionStatus();
            COMPLETED = transactionStatus3 = new TransactionStatus();
            SENT_NEEDS_ABORT = transactionStatus2 = new TransactionStatus();
            NEEDS_ABORT = transactionStatus = new TransactionStatus();
            $VALUES = new TransactionStatus[]{transactionStatus6, transactionStatus5, transactionStatus4, transactionStatus3, transactionStatus2, transactionStatus};
        }

        public static TransactionStatus valueOf(String string2) {
            return Enum.valueOf(TransactionStatus.class, string2);
        }

        public static TransactionStatus[] values() {
            return (TransactionStatus[])$VALUES.clone();
        }
    }
}

