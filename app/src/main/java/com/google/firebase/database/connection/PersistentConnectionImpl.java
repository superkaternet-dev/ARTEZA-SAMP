/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.connection;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.connection.CompoundHash;
import com.google.firebase.database.connection.Connection;
import com.google.firebase.database.connection.ConnectionContext;
import com.google.firebase.database.connection.ConnectionTokenProvider;
import com.google.firebase.database.connection.ConnectionUtils;
import com.google.firebase.database.connection.HostInfo;
import com.google.firebase.database.connection.ListenHashProvider;
import com.google.firebase.database.connection.PersistentConnection;
import com.google.firebase.database.connection.PersistentConnectionImpl$$ExternalSyntheticLambda0;
import com.google.firebase.database.connection.PersistentConnectionImpl$$ExternalSyntheticLambda1;
import com.google.firebase.database.connection.PersistentConnectionImpl$$ExternalSyntheticLambda2;
import com.google.firebase.database.connection.PersistentConnectionImpl$$ExternalSyntheticLambda3;
import com.google.firebase.database.connection.PersistentConnectionImpl$$ExternalSyntheticLambda4;
import com.google.firebase.database.connection.RangeMerge;
import com.google.firebase.database.connection.RequestResultCallback;
import com.google.firebase.database.connection.util.RetryHelper;
import com.google.firebase.database.logging.LogWrapper;
import com.google.firebase.database.logging.Logger;
import com.google.firebase.database.util.GAuthToken;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class PersistentConnectionImpl
implements Connection.Delegate,
PersistentConnection {
    private static final String IDLE_INTERRUPT_REASON = "connection_idle";
    private static final long IDLE_TIMEOUT = 60000L;
    private static final String INVALID_APP_CHECK_TOKEN = "Invalid appcheck token";
    private static final long INVALID_TOKEN_THRESHOLD = 3L;
    private static final String REQUEST_ACTION = "a";
    private static final String REQUEST_ACTION_APPCHECK = "appcheck";
    private static final String REQUEST_ACTION_AUTH = "auth";
    private static final String REQUEST_ACTION_GAUTH = "gauth";
    private static final String REQUEST_ACTION_GET = "g";
    private static final String REQUEST_ACTION_MERGE = "m";
    private static final String REQUEST_ACTION_ONDISCONNECT_CANCEL = "oc";
    private static final String REQUEST_ACTION_ONDISCONNECT_MERGE = "om";
    private static final String REQUEST_ACTION_ONDISCONNECT_PUT = "o";
    private static final String REQUEST_ACTION_PUT = "p";
    private static final String REQUEST_ACTION_QUERY = "q";
    private static final String REQUEST_ACTION_QUERY_UNLISTEN = "n";
    private static final String REQUEST_ACTION_STATS = "s";
    private static final String REQUEST_ACTION_UNAPPCHECK = "unappcheck";
    private static final String REQUEST_ACTION_UNAUTH = "unauth";
    private static final String REQUEST_APPCHECK_TOKEN = "token";
    private static final String REQUEST_AUTHVAR = "authvar";
    private static final String REQUEST_COMPOUND_HASH = "ch";
    private static final String REQUEST_COMPOUND_HASH_HASHES = "hs";
    private static final String REQUEST_COMPOUND_HASH_PATHS = "ps";
    private static final String REQUEST_COUNTERS = "c";
    private static final String REQUEST_CREDENTIAL = "cred";
    private static final String REQUEST_DATA_HASH = "h";
    private static final String REQUEST_DATA_PAYLOAD = "d";
    private static final String REQUEST_ERROR = "error";
    private static final String REQUEST_NUMBER = "r";
    private static final String REQUEST_PATH = "p";
    private static final String REQUEST_PAYLOAD = "b";
    private static final String REQUEST_QUERIES = "q";
    private static final String REQUEST_STATUS = "s";
    private static final String REQUEST_TAG = "t";
    private static final String RESPONSE_FOR_REQUEST = "b";
    private static final String SERVER_ASYNC_ACTION = "a";
    private static final String SERVER_ASYNC_APP_CHECK_REVOKED = "apc";
    private static final String SERVER_ASYNC_AUTH_REVOKED = "ac";
    private static final String SERVER_ASYNC_DATA_MERGE = "m";
    private static final String SERVER_ASYNC_DATA_RANGE_MERGE = "rm";
    private static final String SERVER_ASYNC_DATA_UPDATE = "d";
    private static final String SERVER_ASYNC_LISTEN_CANCELLED = "c";
    private static final String SERVER_ASYNC_PAYLOAD = "b";
    private static final String SERVER_ASYNC_SECURITY_DEBUG = "sd";
    private static final String SERVER_DATA_END_PATH = "e";
    private static final String SERVER_DATA_RANGE_MERGE = "m";
    private static final String SERVER_DATA_START_PATH = "s";
    private static final String SERVER_DATA_TAG = "t";
    private static final String SERVER_DATA_UPDATE_BODY = "d";
    private static final String SERVER_DATA_UPDATE_PATH = "p";
    private static final String SERVER_DATA_WARNINGS = "w";
    private static final String SERVER_KILL_INTERRUPT_REASON = "server_kill";
    private static final String SERVER_RESPONSE_DATA = "d";
    private static final long SUCCESSFUL_CONNECTION_ESTABLISHED_DELAY = 30000L;
    private static final String TOKEN_REFRESH_INTERRUPT_REASON = "token_refresh";
    private static long connectionIds = 0L;
    private String appCheckToken;
    private final ConnectionTokenProvider appCheckTokenProvider;
    private String authToken;
    private final ConnectionTokenProvider authTokenProvider;
    private String cachedHost;
    private ConnectionState connectionState;
    private final ConnectionContext context;
    private long currentGetTokenAttempt = 0L;
    private final PersistentConnection.Delegate delegate;
    private final ScheduledExecutorService executorService;
    private boolean firstConnection = true;
    private boolean forceAppCheckTokenRefresh;
    private boolean forceAuthTokenRefresh;
    private boolean hasOnDisconnects;
    private final HostInfo hostInfo;
    private ScheduledFuture<?> inactivityTimer = null;
    private HashSet<String> interruptReasons = new HashSet();
    private int invalidAppCheckTokenCount = 0;
    private int invalidAuthTokenCount = 0;
    private long lastConnectionEstablishedTime;
    private String lastSessionId;
    private long lastWriteTimestamp;
    private Map<QuerySpec, OutstandingListen> listens;
    private final LogWrapper logger;
    private List<OutstandingDisconnect> onDisconnectRequestQueue;
    private Map<Long, OutstandingGet> outstandingGets;
    private Map<Long, OutstandingPut> outstandingPuts;
    private long readCounter = 0L;
    private Connection realtime;
    private Map<Long, ConnectionRequestCallback> requestCBHash;
    private long requestCounter = 0L;
    private final RetryHelper retryHelper;
    private long writeCounter = 0L;

    public PersistentConnectionImpl(ConnectionContext object, HostInfo object2, PersistentConnection.Delegate object3) {
        this.connectionState = ConnectionState.Disconnected;
        this.delegate = object3;
        this.context = object;
        this.executorService = object3 = ((ConnectionContext)object).getExecutorService();
        this.authTokenProvider = ((ConnectionContext)object).getAuthTokenProvider();
        this.appCheckTokenProvider = ((ConnectionContext)object).getAppCheckTokenProvider();
        this.hostInfo = object2;
        this.listens = new HashMap<QuerySpec, OutstandingListen>();
        this.requestCBHash = new HashMap<Long, ConnectionRequestCallback>();
        this.outstandingPuts = new HashMap<Long, OutstandingPut>();
        this.outstandingGets = new ConcurrentHashMap<Long, OutstandingGet>();
        this.onDisconnectRequestQueue = new ArrayList<OutstandingDisconnect>();
        this.retryHelper = new RetryHelper.Builder((ScheduledExecutorService)object3, ((ConnectionContext)object).getLogger(), "ConnectionRetryHelper").withMinDelayAfterFailure(1000L).withRetryExponent(1.3).withMaxDelay(30000L).withJitterFactor(0.7).build();
        long l = connectionIds;
        connectionIds = 1L + l;
        object2 = ((ConnectionContext)object).getLogger();
        object = new StringBuilder();
        ((StringBuilder)object).append("pc_");
        ((StringBuilder)object).append(l);
        this.logger = new LogWrapper((Logger)object2, "PersistentConnection", ((StringBuilder)object).toString());
        this.lastSessionId = null;
        this.doIdleCheck();
    }

    static /* synthetic */ int access$1002(PersistentConnectionImpl persistentConnectionImpl, int n) {
        persistentConnectionImpl.invalidAuthTokenCount = n;
        return n;
    }

    static /* synthetic */ int access$1008(PersistentConnectionImpl persistentConnectionImpl) {
        int n = persistentConnectionImpl.invalidAuthTokenCount;
        persistentConnectionImpl.invalidAuthTokenCount = n + 1;
        return n;
    }

    static /* synthetic */ String access$1202(PersistentConnectionImpl persistentConnectionImpl, String string2) {
        persistentConnectionImpl.authToken = string2;
        return string2;
    }

    static /* synthetic */ boolean access$1302(PersistentConnectionImpl persistentConnectionImpl, boolean bl) {
        persistentConnectionImpl.forceAuthTokenRefresh = bl;
        return bl;
    }

    static /* synthetic */ ScheduledFuture access$2802(PersistentConnectionImpl persistentConnectionImpl, ScheduledFuture scheduledFuture) {
        persistentConnectionImpl.inactivityTimer = scheduledFuture;
        return scheduledFuture;
    }

    static /* synthetic */ ConnectionState access$902(PersistentConnectionImpl persistentConnectionImpl, ConnectionState connectionState) {
        persistentConnectionImpl.connectionState = connectionState;
        return connectionState;
    }

    private boolean canSendReads() {
        boolean bl = this.connectionState == ConnectionState.Connected;
        return bl;
    }

    private boolean canSendWrites() {
        boolean bl = this.connectionState == ConnectionState.Connected;
        return bl;
    }

    private void cancelSentTransactions() {
        ArrayList<OutstandingPut> arrayList = new ArrayList<OutstandingPut>();
        Iterator<Map.Entry<Long, OutstandingPut>> iterator2 = this.outstandingPuts.entrySet().iterator();
        while (iterator2.hasNext()) {
            OutstandingPut outstandingPut = iterator2.next().getValue();
            if (!outstandingPut.getRequest().containsKey(REQUEST_DATA_HASH) || !outstandingPut.wasSent()) continue;
            arrayList.add(outstandingPut);
            iterator2.remove();
        }
        iterator2 = arrayList.iterator();
        while (iterator2.hasNext()) {
            ((OutstandingPut)((Object)iterator2.next())).getOnComplete().onRequestResult("disconnected", null);
        }
    }

    private boolean connected() {
        boolean bl = this.connectionState == ConnectionState.Authenticating || this.connectionState == ConnectionState.Connected;
        return bl;
    }

    private void doIdleCheck() {
        if (this.isIdle()) {
            ScheduledFuture<?> scheduledFuture = this.inactivityTimer;
            if (scheduledFuture != null) {
                scheduledFuture.cancel(false);
            }
            this.inactivityTimer = this.executorService.schedule(new Runnable(this){
                final PersistentConnectionImpl this$0;
                {
                    this.this$0 = persistentConnectionImpl;
                }

                @Override
                public void run() {
                    PersistentConnectionImpl.access$2802(this.this$0, null);
                    if (this.this$0.idleHasTimedOut()) {
                        this.this$0.interrupt(PersistentConnectionImpl.IDLE_INTERRUPT_REASON);
                    } else {
                        this.this$0.doIdleCheck();
                    }
                }
            }, 60000L, TimeUnit.MILLISECONDS);
        } else if (this.isInterrupted(IDLE_INTERRUPT_REASON)) {
            ConnectionUtils.hardAssert(this.isIdle() ^ true);
            this.resume(IDLE_INTERRUPT_REASON);
        }
    }

    private Task<String> fetchAppCheckToken(boolean bl) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.logger.debug("Trying to fetch app check token", new Object[0]);
        this.appCheckTokenProvider.getToken(bl, new ConnectionTokenProvider.GetTokenCallback(this, taskCompletionSource){
            final PersistentConnectionImpl this$0;
            final TaskCompletionSource val$taskCompletionSource;
            {
                this.this$0 = persistentConnectionImpl;
                this.val$taskCompletionSource = taskCompletionSource;
            }

            @Override
            public void onError(String string2) {
                this.val$taskCompletionSource.setException(new Exception(string2));
            }

            @Override
            public void onSuccess(String string2) {
                this.val$taskCompletionSource.setResult(string2);
            }
        });
        return taskCompletionSource.getTask();
    }

    private Task<String> fetchAuthToken(boolean bl) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.logger.debug("Trying to fetch auth token", new Object[0]);
        this.authTokenProvider.getToken(bl, new ConnectionTokenProvider.GetTokenCallback(this, taskCompletionSource){
            final PersistentConnectionImpl this$0;
            final TaskCompletionSource val$taskCompletionSource;
            {
                this.this$0 = persistentConnectionImpl;
                this.val$taskCompletionSource = taskCompletionSource;
            }

            @Override
            public void onError(String string2) {
                this.val$taskCompletionSource.setException(new Exception(string2));
            }

            @Override
            public void onSuccess(String string2) {
                this.val$taskCompletionSource.setResult(string2);
            }
        });
        return taskCompletionSource.getTask();
    }

    private Map<String, Object> getPutObject(List<String> list, Object object, String string2) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("p", ConnectionUtils.pathToString(list));
        hashMap.put("d", object);
        if (string2 != null) {
            hashMap.put(REQUEST_DATA_HASH, string2);
        }
        return hashMap;
    }

    private void handleTimestamp(long l) {
        if (this.logger.logsDebug()) {
            this.logger.debug("handling timestamp", new Object[0]);
        }
        long l2 = System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("serverTimeOffset", l - l2);
        this.delegate.onServerInfoUpdate(hashMap);
    }

    private boolean idleHasTimedOut() {
        long l = System.currentTimeMillis();
        boolean bl = this.isIdle() && l > this.lastWriteTimestamp + 60000L;
        return bl;
    }

    private boolean isIdle() {
        boolean bl = this.listens.isEmpty() && this.outstandingGets.isEmpty() && this.requestCBHash.isEmpty() && !this.hasOnDisconnects && this.outstandingPuts.isEmpty();
        return bl;
    }

    private long nextRequestNumber() {
        long l = this.requestCounter;
        this.requestCounter = 1L + l;
        return l;
    }

    private void onAppCheckRevoked(String string2, String string3) {
        LogWrapper logWrapper = this.logger;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("App check token revoked: ");
        stringBuilder.append(string2);
        stringBuilder.append(" (");
        stringBuilder.append(string3);
        stringBuilder.append(")");
        logWrapper.debug(stringBuilder.toString(), new Object[0]);
        this.appCheckToken = null;
        this.forceAppCheckTokenRefresh = true;
    }

    private void onAuthRevoked(String string2, String string3) {
        LogWrapper logWrapper = this.logger;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Auth token revoked: ");
        stringBuilder.append(string2);
        stringBuilder.append(" (");
        stringBuilder.append(string3);
        stringBuilder.append(")");
        logWrapper.debug(stringBuilder.toString(), new Object[0]);
        this.authToken = null;
        this.forceAuthTokenRefresh = true;
        this.delegate.onConnectionStatus(false);
        this.realtime.close();
    }

    private void onDataPush(String object, Map<String, Object> object2) {
        List<String> list;
        Object object3;
        if (this.logger.logsDebug()) {
            object3 = this.logger;
            list = new StringBuilder();
            ((StringBuilder)((Object)list)).append("handleServerMessage: ");
            ((StringBuilder)((Object)list)).append((String)object);
            ((StringBuilder)((Object)list)).append(" ");
            ((StringBuilder)((Object)list)).append(object2);
            ((LogWrapper)object3).debug(((StringBuilder)((Object)list)).toString(), new Object[0]);
        }
        if (!((String)object).equals("d") && !((String)object).equals("m")) {
            if (((String)object).equals(SERVER_ASYNC_DATA_RANGE_MERGE)) {
                String string2 = (String)object2.get("p");
                List<String> list2 = ConnectionUtils.stringToPath(string2);
                list = object2.get("d");
                Long l = ConnectionUtils.longFromObject(object2.get("t"));
                object = list;
                ArrayList<RangeMerge> arrayList = new ArrayList<RangeMerge>();
                object3 = object.iterator();
                object2 = list;
                while (object3.hasNext()) {
                    Map map = (Map)object3.next();
                    list = (String)map.get("s");
                    String string3 = (String)map.get(SERVER_DATA_END_PATH);
                    List<String> list3 = null;
                    list = list != null ? ConnectionUtils.stringToPath((String)((Object)list)) : null;
                    if (string3 != null) {
                        list3 = ConnectionUtils.stringToPath(string3);
                    }
                    arrayList.add(new RangeMerge(list, list3, map.get("m")));
                }
                if (arrayList.isEmpty()) {
                    if (this.logger.logsDebug()) {
                        object = this.logger;
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append("Ignoring empty range merge for path ");
                        ((StringBuilder)object2).append(string2);
                        ((LogWrapper)object).debug(((StringBuilder)object2).toString(), new Object[0]);
                    }
                } else {
                    this.delegate.onRangeMergeUpdate(list2, arrayList, l);
                }
            } else if (((String)object).equals("c")) {
                this.onListenRevoked(ConnectionUtils.stringToPath((String)object2.get("p")));
            } else if (((String)object).equals(SERVER_ASYNC_AUTH_REVOKED)) {
                this.onAuthRevoked((String)object2.get("s"), (String)object2.get("d"));
            } else if (((String)object).equals(SERVER_ASYNC_APP_CHECK_REVOKED)) {
                this.onAppCheckRevoked((String)object2.get("s"), (String)object2.get("d"));
            } else if (((String)object).equals(SERVER_ASYNC_SECURITY_DEBUG)) {
                this.onSecurityDebugPacket((Map<String, Object>)object2);
            } else if (this.logger.logsDebug()) {
                object2 = this.logger;
                object3 = new StringBuilder();
                ((StringBuilder)object3).append("Unrecognized action from server: ");
                ((StringBuilder)object3).append((String)object);
                ((LogWrapper)object2).debug(((StringBuilder)object3).toString(), new Object[0]);
            }
        } else {
            boolean bl = ((String)object).equals("m");
            object = (String)object2.get("p");
            object3 = object2.get("d");
            object2 = ConnectionUtils.longFromObject(object2.get("t"));
            if (bl && object3 instanceof Map && ((Map)object3).size() == 0) {
                if (this.logger.logsDebug()) {
                    object3 = this.logger;
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append("ignoring empty merge for path ");
                    ((StringBuilder)object2).append((String)object);
                    ((LogWrapper)object3).debug(((StringBuilder)object2).toString(), new Object[0]);
                }
            } else {
                object = ConnectionUtils.stringToPath((String)object);
                this.delegate.onDataUpdate((List<String>)object, object3, bl, (Long)object2);
            }
        }
    }

    private void onListenRevoked(List<String> object) {
        if ((object = this.removeListens((List<String>)object)) != null) {
            object = object.iterator();
            while (object.hasNext()) {
                ((OutstandingListen)object.next()).resultCallback.onRequestResult("permission_denied", null);
            }
        }
    }

    private void onSecurityDebugPacket(Map<String, Object> map) {
        this.logger.info((String)map.get("msg"));
    }

    private void putInternal(String string2, List<String> object, Object object2, String string3, RequestResultCallback requestResultCallback) {
        object = this.getPutObject((List<String>)object, object2, string3);
        long l = this.writeCounter;
        this.writeCounter = 1L + l;
        this.outstandingPuts.put(l, new OutstandingPut(string2, (Map)object, requestResultCallback));
        if (this.canSendWrites()) {
            this.sendPut(l);
        }
        this.lastWriteTimestamp = System.currentTimeMillis();
        this.doIdleCheck();
    }

    private OutstandingListen removeListen(QuerySpec querySpec) {
        Object object;
        Object object2;
        if (this.logger.logsDebug()) {
            object2 = this.logger;
            object = new StringBuilder();
            ((StringBuilder)object).append("removing query ");
            ((StringBuilder)object).append(querySpec);
            ((LogWrapper)object2).debug(((StringBuilder)object).toString(), new Object[0]);
        }
        if (!this.listens.containsKey(querySpec)) {
            if (this.logger.logsDebug()) {
                object = this.logger;
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("Trying to remove listener for QuerySpec ");
                ((StringBuilder)object2).append(querySpec);
                ((StringBuilder)object2).append(" but no listener exists.");
                ((LogWrapper)object).debug(((StringBuilder)object2).toString(), new Object[0]);
            }
            return null;
        }
        object = this.listens.get(querySpec);
        this.listens.remove(querySpec);
        this.doIdleCheck();
        return object;
    }

    private Collection<OutstandingListen> removeListens(List<String> object) {
        Serializable serializable;
        Iterator iterator2;
        if (this.logger.logsDebug()) {
            iterator2 = this.logger;
            serializable = new StringBuilder();
            ((StringBuilder)serializable).append("removing all listens at path ");
            ((StringBuilder)serializable).append(object);
            ((LogWrapper)((Object)iterator2)).debug(((StringBuilder)serializable).toString(), new Object[0]);
        }
        serializable = new ArrayList();
        for (Map.Entry entry : this.listens.entrySet()) {
            QuerySpec querySpec = (QuerySpec)entry.getKey();
            OutstandingListen object2 = (OutstandingListen)entry.getValue();
            if (!querySpec.path.equals(object)) continue;
            serializable.add(object2);
        }
        iterator2 = serializable.iterator();
        while (iterator2.hasNext()) {
            object = (OutstandingListen)iterator2.next();
            this.listens.remove(((OutstandingListen)object).getQuery());
        }
        this.doIdleCheck();
        return serializable;
    }

    private void restoreState() {
        boolean bl = this.connectionState == ConnectionState.Connected;
        ConnectionUtils.hardAssert(bl, "Should be connected if we're restoring state, but we are: %s", new Object[]{this.connectionState});
        if (this.logger.logsDebug()) {
            this.logger.debug("Restoring outstanding listens", new Object[0]);
        }
        for (OutstandingListen outstandingListen : this.listens.values()) {
            if (this.logger.logsDebug()) {
                LogWrapper logWrapper = this.logger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Restoring listen ");
                stringBuilder.append(outstandingListen.getQuery());
                logWrapper.debug(stringBuilder.toString(), new Object[0]);
            }
            this.sendListen(outstandingListen);
        }
        if (this.logger.logsDebug()) {
            this.logger.debug("Restoring writes.", new Object[0]);
        }
        ArrayList<Long> arrayList = new ArrayList<Long>(this.outstandingPuts.keySet());
        Collections.sort(arrayList);
        Iterator<Long> iterator2 = arrayList.iterator();
        while (iterator2.hasNext()) {
            this.sendPut(iterator2.next());
        }
        for (OutstandingDisconnect outstandingDisconnect : this.onDisconnectRequestQueue) {
            this.sendOnDisconnect(outstandingDisconnect.getAction(), outstandingDisconnect.getPath(), outstandingDisconnect.getData(), outstandingDisconnect.getOnComplete());
        }
        this.onDisconnectRequestQueue.clear();
        if (this.logger.logsDebug()) {
            this.logger.debug("Restoring reads.", new Object[0]);
        }
        ArrayList<Long> arrayList2 = new ArrayList<Long>(this.outstandingGets.keySet());
        Collections.sort(arrayList2);
        Iterator<Long> iterator3 = arrayList2.iterator();
        while (iterator3.hasNext()) {
            this.sendGet(iterator3.next());
        }
    }

    private void restoreTokens() {
        if (this.logger.logsDebug()) {
            this.logger.debug("calling restore tokens", new Object[0]);
        }
        boolean bl = this.connectionState == ConnectionState.Connecting;
        ConnectionUtils.hardAssert(bl, "Wanted to restore tokens, but was in wrong state: %s", new Object[]{this.connectionState});
        if (this.authToken != null) {
            if (this.logger.logsDebug()) {
                this.logger.debug("Restoring auth.", new Object[0]);
            }
            this.connectionState = ConnectionState.Authenticating;
            this.sendAuthAndRestoreState();
        } else {
            if (this.logger.logsDebug()) {
                this.logger.debug("Not restoring auth because auth token is null.", new Object[0]);
            }
            this.connectionState = ConnectionState.Connected;
            this.sendAppCheckTokenHelper(true);
        }
    }

    private void sendAction(String string2, Map<String, Object> map, ConnectionRequestCallback connectionRequestCallback) {
        this.sendSensitive(string2, false, map, connectionRequestCallback);
    }

    private void sendAppCheckTokenHelper(boolean bl) {
        if (this.appCheckToken == null) {
            this.restoreState();
            return;
        }
        ConnectionUtils.hardAssert(this.connected(), "Must be connected to send auth, but was: %s", new Object[]{this.connectionState});
        if (this.logger.logsDebug()) {
            this.logger.debug("Sending app check.", new Object[0]);
        }
        PersistentConnectionImpl$$ExternalSyntheticLambda3 persistentConnectionImpl$$ExternalSyntheticLambda3 = new PersistentConnectionImpl$$ExternalSyntheticLambda3(this, bl);
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        bl = this.appCheckToken != null;
        ConnectionUtils.hardAssert(bl, "App check token must be set!", new Object[0]);
        hashMap.put(REQUEST_APPCHECK_TOKEN, this.appCheckToken);
        this.sendSensitive(REQUEST_ACTION_APPCHECK, true, hashMap, persistentConnectionImpl$$ExternalSyntheticLambda3);
    }

    private void sendAuthAndRestoreState() {
        this.sendAuthHelper(true);
    }

    private void sendAuthHelper(boolean bl) {
        ConnectionUtils.hardAssert(this.connected(), "Must be connected to send auth, but was: %s", new Object[]{this.connectionState});
        if (this.logger.logsDebug()) {
            this.logger.debug("Sending auth.", new Object[0]);
        }
        ConnectionRequestCallback connectionRequestCallback = new ConnectionRequestCallback(this, bl){
            final PersistentConnectionImpl this$0;
            final boolean val$restoreStateAfterComplete;
            {
                this.this$0 = persistentConnectionImpl;
                this.val$restoreStateAfterComplete = bl;
            }

            @Override
            public void onResponse(Map<String, Object> object) {
                String string2 = (String)object.get("s");
                if (string2.equals("ok")) {
                    PersistentConnectionImpl.access$902(this.this$0, ConnectionState.Connected);
                    PersistentConnectionImpl.access$1002(this.this$0, 0);
                    this.this$0.sendAppCheckTokenHelper(this.val$restoreStateAfterComplete);
                } else {
                    PersistentConnectionImpl.access$1202(this.this$0, null);
                    PersistentConnectionImpl.access$1302(this.this$0, true);
                    this.this$0.delegate.onConnectionStatus(false);
                    String string3 = (String)object.get("d");
                    LogWrapper logWrapper = this.this$0.logger;
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Authentication failed: ");
                    ((StringBuilder)object).append(string2);
                    ((StringBuilder)object).append(" (");
                    ((StringBuilder)object).append(string3);
                    ((StringBuilder)object).append(")");
                    logWrapper.debug(((StringBuilder)object).toString(), new Object[0]);
                    this.this$0.realtime.close();
                    if (string2.equals("invalid_token")) {
                        PersistentConnectionImpl.access$1008(this.this$0);
                        if ((long)this.this$0.invalidAuthTokenCount >= 3L) {
                            this.this$0.retryHelper.setMaxDelay();
                            this.this$0.logger.warn("Provided authentication credentials are invalid. This usually indicates your FirebaseApp instance was not initialized correctly. Make sure your google-services.json file has the correct firebase_url and api_key. You can re-download google-services.json from https://console.firebase.google.com/.");
                        }
                    }
                }
            }
        };
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        GAuthToken gAuthToken = GAuthToken.tryParseFromString(this.authToken);
        if (gAuthToken != null) {
            hashMap.put(REQUEST_CREDENTIAL, gAuthToken.getToken());
            if (gAuthToken.getAuth() != null) {
                hashMap.put(REQUEST_AUTHVAR, gAuthToken.getAuth());
            }
            this.sendSensitive(REQUEST_ACTION_GAUTH, true, hashMap, connectionRequestCallback);
        } else {
            hashMap.put(REQUEST_CREDENTIAL, this.authToken);
            this.sendSensitive(REQUEST_ACTION_AUTH, true, hashMap, connectionRequestCallback);
        }
    }

    private void sendConnectStats() {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        boolean bl = this.context.isPersistenceEnabled();
        Integer n = 1;
        if (bl) {
            hashMap.put("persistence.android.enabled", n);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("sdk.android.");
        stringBuilder.append(this.context.getClientSdkVersion().replace('.', '-'));
        hashMap.put(stringBuilder.toString(), n);
        if (this.logger.logsDebug()) {
            this.logger.debug("Sending first connection stats", new Object[0]);
        }
        this.sendStats(hashMap);
    }

    private void sendGet(Long l) {
        ConnectionUtils.hardAssert(this.canSendReads(), "sendGet called when we can't send gets", new Object[0]);
        Object object = this.outstandingGets.get(l);
        if (!((OutstandingGet)object).markSent() && this.logger.logsDebug()) {
            object = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("get");
            stringBuilder.append(l);
            stringBuilder.append(" cancelled, ignoring.");
            ((LogWrapper)object).debug(stringBuilder.toString(), new Object[0]);
            return;
        }
        this.sendAction(REQUEST_ACTION_GET, ((OutstandingGet)object).getRequest(), new ConnectionRequestCallback(this, l, (OutstandingGet)object){
            final PersistentConnectionImpl this$0;
            final OutstandingGet val$get;
            final Long val$readId;
            {
                this.this$0 = persistentConnectionImpl;
                this.val$readId = l;
                this.val$get = outstandingGet;
            }

            @Override
            public void onResponse(Map<String, Object> object) {
                if ((OutstandingGet)this.this$0.outstandingGets.get(this.val$readId) == this.val$get) {
                    this.this$0.outstandingGets.remove(this.val$readId);
                    this.val$get.getOnComplete().onResponse((Map<String, Object>)object);
                } else if (this.this$0.logger.logsDebug()) {
                    object = this.this$0.logger;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Ignoring on complete for get ");
                    stringBuilder.append(this.val$readId);
                    stringBuilder.append(" because it was removed already.");
                    ((LogWrapper)object).debug(stringBuilder.toString(), new Object[0]);
                }
            }
        });
    }

    private void sendListen(OutstandingListen outstandingListen) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("p", ConnectionUtils.pathToString(outstandingListen.getQuery().path));
        ArrayList<String> arrayList = outstandingListen.getTag();
        if (arrayList != null) {
            hashMap.put("q", outstandingListen.query.queryParams);
            hashMap.put("t", arrayList);
        }
        arrayList = outstandingListen.getHashFunction();
        hashMap.put(REQUEST_DATA_HASH, arrayList.getSimpleHash());
        if (arrayList.shouldIncludeCompoundHash()) {
            CompoundHash compoundHash = arrayList.getCompoundHash();
            arrayList = new ArrayList<String>();
            Object object = compoundHash.getPosts().iterator();
            while (object.hasNext()) {
                arrayList.add(ConnectionUtils.pathToString(object.next()));
            }
            object = new HashMap();
            object.put(REQUEST_COMPOUND_HASH_HASHES, compoundHash.getHashes());
            object.put(REQUEST_COMPOUND_HASH_PATHS, arrayList);
            hashMap.put(REQUEST_COMPOUND_HASH, object);
        }
        this.sendAction("q", hashMap, new ConnectionRequestCallback(this, outstandingListen){
            final PersistentConnectionImpl this$0;
            final OutstandingListen val$listen;
            {
                this.this$0 = persistentConnectionImpl;
                this.val$listen = outstandingListen;
            }

            @Override
            public void onResponse(Map<String, Object> object) {
                Object object2;
                String string2 = (String)object.get("s");
                if (string2.equals("ok") && (object2 = (Map)object.get("d")).containsKey(PersistentConnectionImpl.SERVER_DATA_WARNINGS)) {
                    object2 = (List)object2.get(PersistentConnectionImpl.SERVER_DATA_WARNINGS);
                    this.this$0.warnOnListenerWarnings((List)object2, this.val$listen.query);
                }
                if ((OutstandingListen)this.this$0.listens.get(this.val$listen.getQuery()) == this.val$listen) {
                    if (!string2.equals("ok")) {
                        this.this$0.removeListen(this.val$listen.getQuery());
                        object = (String)object.get("d");
                        this.val$listen.resultCallback.onRequestResult(string2, (String)object);
                    } else {
                        this.val$listen.resultCallback.onRequestResult(null, null);
                    }
                }
            }
        });
    }

    private void sendOnDisconnect(String string2, List<String> list, Object object, RequestResultCallback requestResultCallback) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("p", ConnectionUtils.pathToString(list));
        hashMap.put("d", object);
        this.sendAction(string2, hashMap, new ConnectionRequestCallback(this, requestResultCallback){
            final PersistentConnectionImpl this$0;
            final RequestResultCallback val$onComplete;
            {
                this.this$0 = persistentConnectionImpl;
                this.val$onComplete = requestResultCallback;
            }

            @Override
            public void onResponse(Map<String, Object> object) {
                String string2 = (String)object.get("s");
                String string3 = null;
                String string4 = null;
                if (!string2.equals("ok")) {
                    string4 = string2;
                    string3 = (String)object.get("d");
                }
                if ((object = this.val$onComplete) != null) {
                    object.onRequestResult(string4, string3);
                }
            }
        });
    }

    private void sendPut(long l) {
        ConnectionUtils.hardAssert(this.canSendWrites(), "sendPut called when we can't send writes (we're disconnected or writes are paused).", new Object[0]);
        OutstandingPut outstandingPut = this.outstandingPuts.get(l);
        RequestResultCallback requestResultCallback = outstandingPut.getOnComplete();
        String string2 = outstandingPut.getAction();
        outstandingPut.markSent();
        this.sendAction(string2, outstandingPut.getRequest(), new ConnectionRequestCallback(this, string2, l, outstandingPut, requestResultCallback){
            final PersistentConnectionImpl this$0;
            final String val$action;
            final RequestResultCallback val$onComplete;
            final OutstandingPut val$put;
            final long val$putId;
            {
                this.this$0 = persistentConnectionImpl;
                this.val$action = string2;
                this.val$putId = l;
                this.val$put = outstandingPut;
                this.val$onComplete = requestResultCallback;
            }

            @Override
            public void onResponse(Map<String, Object> object) {
                Object object2;
                if (this.this$0.logger.logsDebug()) {
                    LogWrapper logWrapper = this.this$0.logger;
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append(this.val$action);
                    ((StringBuilder)object2).append(" response: ");
                    ((StringBuilder)object2).append(object);
                    logWrapper.debug(((StringBuilder)object2).toString(), new Object[0]);
                }
                if ((OutstandingPut)this.this$0.outstandingPuts.get(this.val$putId) == this.val$put) {
                    this.this$0.outstandingPuts.remove(this.val$putId);
                    if (this.val$onComplete != null) {
                        object2 = (String)object.get("s");
                        if (((String)object2).equals("ok")) {
                            this.val$onComplete.onRequestResult(null, null);
                        } else {
                            object = (String)object.get("d");
                            this.val$onComplete.onRequestResult((String)object2, (String)object);
                        }
                    }
                } else if (this.this$0.logger.logsDebug()) {
                    object2 = this.this$0.logger;
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Ignoring on complete for put ");
                    ((StringBuilder)object).append(this.val$putId);
                    ((StringBuilder)object).append(" because it was removed already.");
                    ((LogWrapper)object2).debug(((StringBuilder)object).toString(), new Object[0]);
                }
                this.this$0.doIdleCheck();
            }
        });
    }

    private void sendSensitive(String string2, boolean bl, Map<String, Object> map, ConnectionRequestCallback connectionRequestCallback) {
        long l = this.nextRequestNumber();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put(REQUEST_NUMBER, l);
        hashMap.put("a", string2);
        hashMap.put("b", map);
        this.realtime.sendRequest(hashMap, bl);
        this.requestCBHash.put(l, connectionRequestCallback);
    }

    private void sendStats(Map<String, Integer> map) {
        if (!map.isEmpty()) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("c", map);
            this.sendAction("s", hashMap, new ConnectionRequestCallback(this){
                final PersistentConnectionImpl this$0;
                {
                    this.this$0 = persistentConnectionImpl;
                }

                @Override
                public void onResponse(Map<String, Object> object) {
                    String string2 = (String)object.get("s");
                    if (!string2.equals("ok")) {
                        String string3 = (String)object.get("d");
                        if (this.this$0.logger.logsDebug()) {
                            object = this.this$0.logger;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Failed to send stats: ");
                            stringBuilder.append(string2);
                            stringBuilder.append(" (message: ");
                            stringBuilder.append(string3);
                            stringBuilder.append(")");
                            ((LogWrapper)object).debug(stringBuilder.toString(), new Object[0]);
                        }
                    }
                }
            });
        } else if (this.logger.logsDebug()) {
            this.logger.debug("Not sending stats because stats are empty", new Object[0]);
        }
    }

    private void sendUnAppCheck() {
        ConnectionUtils.hardAssert(this.connected(), "Must be connected to send unauth.", new Object[0]);
        boolean bl = this.appCheckToken == null;
        ConnectionUtils.hardAssert(bl, "App check token must not be set.", new Object[0]);
        this.sendAction(REQUEST_ACTION_UNAPPCHECK, Collections.<String, Object>emptyMap(), null);
    }

    private void sendUnauth() {
        ConnectionUtils.hardAssert(this.connected(), "Must be connected to send unauth.", new Object[0]);
        boolean bl = this.authToken == null;
        ConnectionUtils.hardAssert(bl, "Auth token must not be set.", new Object[0]);
        this.sendAction(REQUEST_ACTION_UNAUTH, Collections.<String, Object>emptyMap(), null);
    }

    private void sendUnlisten(OutstandingListen outstandingListen) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("p", ConnectionUtils.pathToString(outstandingListen.query.path));
        Long l = outstandingListen.getTag();
        if (l != null) {
            hashMap.put("q", outstandingListen.getQuery().queryParams);
            hashMap.put("t", l);
        }
        this.sendAction(REQUEST_ACTION_QUERY_UNLISTEN, hashMap, null);
    }

    private void tryScheduleReconnect() {
        if (this.shouldReconnect()) {
            boolean bl = this.connectionState == ConnectionState.Disconnected;
            ConnectionUtils.hardAssert(bl, "Not in disconnected state: %s", new Object[]{this.connectionState});
            bl = this.forceAuthTokenRefresh;
            boolean bl2 = this.forceAppCheckTokenRefresh;
            this.logger.debug("Scheduling connection attempt", new Object[0]);
            this.forceAuthTokenRefresh = false;
            this.forceAppCheckTokenRefresh = false;
            this.retryHelper.retry(new PersistentConnectionImpl$$ExternalSyntheticLambda4(this, bl, bl2));
        }
    }

    private void upgradeAppCheck() {
        this.sendAppCheckTokenHelper(false);
    }

    private void upgradeAuth() {
        this.sendAuthHelper(false);
    }

    private void warnOnListenerWarnings(List<String> object, QuerySpec querySpec) {
        if (object.contains("no_index")) {
            object = new StringBuilder();
            ((StringBuilder)object).append("\".indexOn\": \"");
            ((StringBuilder)object).append(querySpec.queryParams.get("i"));
            ((StringBuilder)object).append('\"');
            String string2 = ((StringBuilder)object).toString();
            object = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Using an unspecified index. Your data will be downloaded and filtered on the client. Consider adding '");
            stringBuilder.append(string2);
            stringBuilder.append("' at ");
            stringBuilder.append(ConnectionUtils.pathToString(querySpec.path));
            stringBuilder.append(" to your security and Firebase Database rules for better performance");
            ((LogWrapper)object).warn(stringBuilder.toString());
        }
    }

    @Override
    public void compareAndPut(List<String> list, Object object, String string2, RequestResultCallback requestResultCallback) {
        this.putInternal("p", list, object, string2, requestResultCallback);
    }

    @Override
    public Task<Object> get(List<String> object, Map<String, Object> object2) {
        QuerySpec querySpec = new QuerySpec((List<String>)object, (Map<String, Object>)object2);
        object = new TaskCompletionSource();
        long l = this.readCounter;
        this.readCounter = 1L + l;
        object2 = new HashMap<String, Object>();
        object2.put((String)"p", (Object)ConnectionUtils.pathToString(querySpec.path));
        object2.put((String)"q", (Object)querySpec.queryParams);
        object2 = new OutstandingGet(REQUEST_ACTION_GET, (Map)object2, new PersistentConnectionImpl$$ExternalSyntheticLambda2(this, querySpec, (TaskCompletionSource)object));
        this.outstandingGets.put(l, (OutstandingGet)object2);
        if (this.canSendReads()) {
            this.sendGet(l);
        }
        this.doIdleCheck();
        return ((TaskCompletionSource)object).getTask();
    }

    @Override
    public void initialize() {
        this.tryScheduleReconnect();
    }

    public void injectConnectionFailure() {
        Connection connection = this.realtime;
        if (connection != null) {
            connection.injectConnectionFailure();
        }
    }

    @Override
    public void interrupt(String object) {
        if (this.logger.logsDebug()) {
            LogWrapper logWrapper = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Connection interrupted for: ");
            stringBuilder.append((String)object);
            logWrapper.debug(stringBuilder.toString(), new Object[0]);
        }
        this.interruptReasons.add((String)object);
        object = this.realtime;
        if (object != null) {
            ((Connection)object).close();
            this.realtime = null;
        } else {
            this.retryHelper.cancel();
            this.connectionState = ConnectionState.Disconnected;
        }
        this.retryHelper.signalSuccess();
    }

    @Override
    public boolean isInterrupted(String string2) {
        return this.interruptReasons.contains(string2);
    }

    public /* synthetic */ void lambda$get$0$com-google-firebase-database-connection-PersistentConnectionImpl(QuerySpec querySpec, TaskCompletionSource taskCompletionSource, Map map) {
        if (((String)map.get("s")).equals("ok")) {
            map = map.get("d");
            this.delegate.onDataUpdate(querySpec.path, map, false, null);
            taskCompletionSource.setResult(map);
        } else {
            taskCompletionSource.setException(new Exception((String)map.get("d")));
        }
    }

    public /* synthetic */ void lambda$sendAppCheckTokenHelper$4$com-google-firebase-database-connection-PersistentConnectionImpl(boolean bl, Map object) {
        String string2 = (String)object.get("s");
        if (string2.equals("ok")) {
            this.invalidAppCheckTokenCount = 0;
        } else {
            this.appCheckToken = null;
            this.forceAppCheckTokenRefresh = true;
            object = (String)object.get("d");
            LogWrapper logWrapper = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("App check failed: ");
            stringBuilder.append(string2);
            stringBuilder.append(" (");
            stringBuilder.append((String)object);
            stringBuilder.append(")");
            logWrapper.debug(stringBuilder.toString(), new Object[0]);
        }
        if (bl) {
            this.restoreState();
        }
    }

    public /* synthetic */ void lambda$tryScheduleReconnect$1$com-google-firebase-database-connection-PersistentConnectionImpl(long l, Task task, Task task2, Void void_) {
        if (l == this.currentGetTokenAttempt) {
            if (this.connectionState == ConnectionState.GettingToken) {
                this.logger.debug("Successfully fetched token, opening connection", new Object[0]);
                this.openNetworkConnection((String)task.getResult(), (String)task2.getResult());
            } else if (this.connectionState == ConnectionState.Disconnected) {
                this.logger.debug("Not opening connection after token refresh, because connection was set to disconnected", new Object[0]);
            }
        } else {
            this.logger.debug("Ignoring getToken result, because this was not the latest attempt.", new Object[0]);
        }
    }

    public /* synthetic */ void lambda$tryScheduleReconnect$2$com-google-firebase-database-connection-PersistentConnectionImpl(long l, Exception exception) {
        if (l == this.currentGetTokenAttempt) {
            this.connectionState = ConnectionState.Disconnected;
            LogWrapper logWrapper = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error fetching token: ");
            stringBuilder.append(exception);
            logWrapper.debug(stringBuilder.toString(), new Object[0]);
            this.tryScheduleReconnect();
        } else {
            this.logger.debug("Ignoring getToken error, because this was not the latest attempt.", new Object[0]);
        }
    }

    public /* synthetic */ void lambda$tryScheduleReconnect$3$com-google-firebase-database-connection-PersistentConnectionImpl(boolean bl, boolean bl2) {
        boolean bl3 = this.connectionState == ConnectionState.Disconnected;
        ConnectionUtils.hardAssert(bl3, "Not in disconnected state: %s", new Object[]{this.connectionState});
        this.connectionState = ConnectionState.GettingToken;
        ++this.currentGetTokenAttempt;
        long l = this.currentGetTokenAttempt;
        Task<String> task = this.fetchAuthToken(bl);
        Task<String> task2 = this.fetchAppCheckToken(bl2);
        Tasks.whenAll(task, task2).addOnSuccessListener(this.executorService, (OnSuccessListener<Void>)new PersistentConnectionImpl$$ExternalSyntheticLambda1(this, l, task, task2)).addOnFailureListener(this.executorService, (OnFailureListener)new PersistentConnectionImpl$$ExternalSyntheticLambda0(this, l));
    }

    @Override
    public void listen(List<String> object, Map<String, Object> object2, ListenHashProvider listenHashProvider, Long l, RequestResultCallback requestResultCallback) {
        LogWrapper logWrapper;
        object = new QuerySpec((List<String>)object, (Map<String, Object>)object2);
        if (this.logger.logsDebug()) {
            logWrapper = this.logger;
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("Listening on ");
            ((StringBuilder)object2).append(object);
            logWrapper.debug(((StringBuilder)object2).toString(), new Object[0]);
        }
        ConnectionUtils.hardAssert(this.listens.containsKey(object) ^ true, "listen() called twice for same QuerySpec.", new Object[0]);
        if (this.logger.logsDebug()) {
            logWrapper = this.logger;
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("Adding listen query: ");
            ((StringBuilder)object2).append(object);
            logWrapper.debug(((StringBuilder)object2).toString(), new Object[0]);
        }
        object2 = new OutstandingListen(requestResultCallback, (QuerySpec)object, l, listenHashProvider);
        this.listens.put((QuerySpec)object, (OutstandingListen)object2);
        if (this.connected()) {
            this.sendListen((OutstandingListen)object2);
        }
        this.doIdleCheck();
    }

    @Override
    public void merge(List<String> list, Map<String, Object> map, RequestResultCallback requestResultCallback) {
        this.putInternal("m", list, map, null, requestResultCallback);
    }

    @Override
    public void onCacheHost(String string2) {
        this.cachedHost = string2;
    }

    @Override
    public void onDataMessage(Map<String, Object> map) {
        if (map.containsKey(REQUEST_NUMBER)) {
            long l = ((Integer)map.get(REQUEST_NUMBER)).intValue();
            ConnectionRequestCallback connectionRequestCallback = this.requestCBHash.remove(l);
            if (connectionRequestCallback != null) {
                connectionRequestCallback.onResponse((Map)map.get("b"));
            }
        } else if (!map.containsKey(REQUEST_ERROR)) {
            if (map.containsKey("a")) {
                this.onDataPush((String)map.get("a"), (Map)map.get("b"));
            } else if (this.logger.logsDebug()) {
                LogWrapper logWrapper = this.logger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Ignoring unknown message: ");
                stringBuilder.append(map);
                logWrapper.debug(stringBuilder.toString(), new Object[0]);
            }
        }
    }

    @Override
    public void onDisconnect(Connection.DisconnectReason disconnectReason) {
        boolean bl = this.logger.logsDebug();
        boolean bl2 = false;
        if (bl) {
            LogWrapper logWrapper = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Got on disconnect due to ");
            stringBuilder.append(disconnectReason.name());
            logWrapper.debug(stringBuilder.toString(), new Object[0]);
        }
        this.connectionState = ConnectionState.Disconnected;
        this.realtime = null;
        this.hasOnDisconnects = false;
        this.requestCBHash.clear();
        this.cancelSentTransactions();
        if (this.shouldReconnect()) {
            long l = System.currentTimeMillis();
            long l2 = this.lastConnectionEstablishedTime;
            if (l2 > 0L) {
                if (l - l2 > 30000L) {
                    bl2 = true;
                }
            } else {
                bl2 = false;
            }
            if (disconnectReason == Connection.DisconnectReason.SERVER_RESET || bl2) {
                this.retryHelper.signalSuccess();
            }
            this.tryScheduleReconnect();
        }
        this.lastConnectionEstablishedTime = 0L;
        this.delegate.onDisconnect();
    }

    @Override
    public void onDisconnectCancel(List<String> list, RequestResultCallback requestResultCallback) {
        if (this.canSendWrites()) {
            this.sendOnDisconnect(REQUEST_ACTION_ONDISCONNECT_CANCEL, list, null, requestResultCallback);
        } else {
            this.onDisconnectRequestQueue.add(new OutstandingDisconnect(REQUEST_ACTION_ONDISCONNECT_CANCEL, list, null, requestResultCallback));
        }
        this.doIdleCheck();
    }

    @Override
    public void onDisconnectMerge(List<String> list, Map<String, Object> map, RequestResultCallback requestResultCallback) {
        this.hasOnDisconnects = true;
        if (this.canSendWrites()) {
            this.sendOnDisconnect(REQUEST_ACTION_ONDISCONNECT_MERGE, list, map, requestResultCallback);
        } else {
            this.onDisconnectRequestQueue.add(new OutstandingDisconnect(REQUEST_ACTION_ONDISCONNECT_MERGE, list, map, requestResultCallback));
        }
        this.doIdleCheck();
    }

    @Override
    public void onDisconnectPut(List<String> list, Object object, RequestResultCallback requestResultCallback) {
        this.hasOnDisconnects = true;
        if (this.canSendWrites()) {
            this.sendOnDisconnect(REQUEST_ACTION_ONDISCONNECT_PUT, list, object, requestResultCallback);
        } else {
            this.onDisconnectRequestQueue.add(new OutstandingDisconnect(REQUEST_ACTION_ONDISCONNECT_PUT, list, object, requestResultCallback));
        }
        this.doIdleCheck();
    }

    @Override
    public void onKill(String charSequence) {
        int n;
        if (((String)charSequence).equals(INVALID_APP_CHECK_TOKEN) && (long)(n = this.invalidAppCheckTokenCount) < 3L) {
            this.invalidAppCheckTokenCount = n + 1;
            LogWrapper logWrapper = this.logger;
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("Detected invalid AppCheck token. Reconnecting (");
            ((StringBuilder)charSequence).append(3L - (long)this.invalidAppCheckTokenCount);
            ((StringBuilder)charSequence).append(" attempts remaining)");
            logWrapper.warn(((StringBuilder)charSequence).toString());
        } else {
            LogWrapper logWrapper = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Firebase Database connection was forcefully killed by the server. Will not attempt reconnect. Reason: ");
            stringBuilder.append((String)charSequence);
            logWrapper.warn(stringBuilder.toString());
            this.interrupt(SERVER_KILL_INTERRUPT_REASON);
        }
    }

    @Override
    public void onReady(long l, String string2) {
        if (this.logger.logsDebug()) {
            this.logger.debug("onReady", new Object[0]);
        }
        this.lastConnectionEstablishedTime = System.currentTimeMillis();
        this.handleTimestamp(l);
        if (this.firstConnection) {
            this.sendConnectStats();
        }
        this.restoreTokens();
        this.firstConnection = false;
        this.lastSessionId = string2;
        this.delegate.onConnect();
    }

    public void openNetworkConnection(String object, String string2) {
        boolean bl = this.connectionState == ConnectionState.GettingToken;
        ConnectionUtils.hardAssert(bl, "Trying to open network connection while in the wrong state: %s", new Object[]{this.connectionState});
        if (object == null) {
            this.delegate.onConnectionStatus(false);
        }
        this.authToken = object;
        this.appCheckToken = string2;
        this.connectionState = ConnectionState.Connecting;
        this.realtime = object = new Connection(this.context, this.hostInfo, this.cachedHost, this, this.lastSessionId, string2);
        ((Connection)object).open();
    }

    @Override
    public void purgeOutstandingWrites() {
        for (OutstandingPut object : this.outstandingPuts.values()) {
            if (object.onComplete == null) continue;
            object.onComplete.onRequestResult("write_canceled", null);
        }
        for (OutstandingDisconnect outstandingDisconnect : this.onDisconnectRequestQueue) {
            if (outstandingDisconnect.onComplete == null) continue;
            outstandingDisconnect.onComplete.onRequestResult("write_canceled", null);
        }
        this.outstandingPuts.clear();
        this.onDisconnectRequestQueue.clear();
        if (!this.connected()) {
            this.hasOnDisconnects = false;
        }
        this.doIdleCheck();
    }

    @Override
    public void put(List<String> list, Object object, RequestResultCallback requestResultCallback) {
        this.putInternal("p", list, object, null, requestResultCallback);
    }

    @Override
    public void refreshAppCheckToken() {
        this.logger.debug("App check token refresh requested", new Object[0]);
        this.interrupt(TOKEN_REFRESH_INTERRUPT_REASON);
        this.resume(TOKEN_REFRESH_INTERRUPT_REASON);
    }

    @Override
    public void refreshAppCheckToken(String string2) {
        this.logger.debug("App check token refreshed.", new Object[0]);
        this.appCheckToken = string2;
        if (this.connected()) {
            if (string2 != null) {
                this.upgradeAppCheck();
            } else {
                this.sendUnAppCheck();
            }
        }
    }

    @Override
    public void refreshAuthToken() {
        this.logger.debug("Auth token refresh requested", new Object[0]);
        this.interrupt(TOKEN_REFRESH_INTERRUPT_REASON);
        this.resume(TOKEN_REFRESH_INTERRUPT_REASON);
    }

    @Override
    public void refreshAuthToken(String string2) {
        this.logger.debug("Auth token refreshed.", new Object[0]);
        this.authToken = string2;
        if (this.connected()) {
            if (string2 != null) {
                this.upgradeAuth();
            } else {
                this.sendUnauth();
            }
        }
    }

    @Override
    public void resume(String string2) {
        if (this.logger.logsDebug()) {
            LogWrapper logWrapper = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Connection no longer interrupted for: ");
            stringBuilder.append(string2);
            logWrapper.debug(stringBuilder.toString(), new Object[0]);
        }
        this.interruptReasons.remove(string2);
        if (this.shouldReconnect() && this.connectionState == ConnectionState.Disconnected) {
            this.tryScheduleReconnect();
        }
    }

    boolean shouldReconnect() {
        boolean bl = this.interruptReasons.size() == 0;
        return bl;
    }

    @Override
    public void shutdown() {
        this.interrupt("shutdown");
    }

    @Override
    public void unlisten(List<String> object, Map<String, Object> object2) {
        object = new QuerySpec((List<String>)object, (Map<String, Object>)object2);
        if (this.logger.logsDebug()) {
            object2 = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unlistening on ");
            stringBuilder.append(object);
            ((LogWrapper)object2).debug(stringBuilder.toString(), new Object[0]);
        }
        if ((object = this.removeListen((QuerySpec)object)) != null && this.connected()) {
            this.sendUnlisten((OutstandingListen)object);
        }
        this.doIdleCheck();
    }

    private static interface ConnectionRequestCallback {
        public void onResponse(Map<String, Object> var1);
    }

    private static final class ConnectionState
    extends Enum<ConnectionState> {
        private static final ConnectionState[] $VALUES;
        public static final /* enum */ ConnectionState Authenticating;
        public static final /* enum */ ConnectionState Connected;
        public static final /* enum */ ConnectionState Connecting;
        public static final /* enum */ ConnectionState Disconnected;
        public static final /* enum */ ConnectionState GettingToken;

        static {
            ConnectionState connectionState;
            ConnectionState connectionState2;
            ConnectionState connectionState3;
            ConnectionState connectionState4;
            ConnectionState connectionState5;
            Disconnected = connectionState5 = new ConnectionState();
            GettingToken = connectionState4 = new ConnectionState();
            Connecting = connectionState3 = new ConnectionState();
            Authenticating = connectionState2 = new ConnectionState();
            Connected = connectionState = new ConnectionState();
            $VALUES = new ConnectionState[]{connectionState5, connectionState4, connectionState3, connectionState2, connectionState};
        }

        public static ConnectionState valueOf(String string2) {
            return Enum.valueOf(ConnectionState.class, string2);
        }

        public static ConnectionState[] values() {
            return (ConnectionState[])$VALUES.clone();
        }
    }

    private static class OutstandingDisconnect {
        private final String action;
        private final Object data;
        private final RequestResultCallback onComplete;
        private final List<String> path;

        private OutstandingDisconnect(String string2, List<String> list, Object object, RequestResultCallback requestResultCallback) {
            this.action = string2;
            this.path = list;
            this.data = object;
            this.onComplete = requestResultCallback;
        }

        public String getAction() {
            return this.action;
        }

        public Object getData() {
            return this.data;
        }

        public RequestResultCallback getOnComplete() {
            return this.onComplete;
        }

        public List<String> getPath() {
            return this.path;
        }
    }

    private static class OutstandingGet {
        private final ConnectionRequestCallback onComplete;
        private final Map<String, Object> request;
        private boolean sent;

        private OutstandingGet(String string2, Map<String, Object> map, ConnectionRequestCallback connectionRequestCallback) {
            this.request = map;
            this.onComplete = connectionRequestCallback;
            this.sent = false;
        }

        private ConnectionRequestCallback getOnComplete() {
            return this.onComplete;
        }

        private Map<String, Object> getRequest() {
            return this.request;
        }

        private boolean markSent() {
            if (this.sent) {
                return false;
            }
            this.sent = true;
            return true;
        }
    }

    private static class OutstandingListen {
        private final ListenHashProvider hashFunction;
        private final QuerySpec query;
        private final RequestResultCallback resultCallback;
        private final Long tag;

        private OutstandingListen(RequestResultCallback requestResultCallback, QuerySpec querySpec, Long l, ListenHashProvider listenHashProvider) {
            this.resultCallback = requestResultCallback;
            this.query = querySpec;
            this.hashFunction = listenHashProvider;
            this.tag = l;
        }

        public ListenHashProvider getHashFunction() {
            return this.hashFunction;
        }

        public QuerySpec getQuery() {
            return this.query;
        }

        public Long getTag() {
            return this.tag;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.query.toString());
            stringBuilder.append(" (Tag: ");
            stringBuilder.append(this.tag);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private static class OutstandingPut {
        private String action;
        private RequestResultCallback onComplete;
        private Map<String, Object> request;
        private boolean sent;

        private OutstandingPut(String string2, Map<String, Object> map, RequestResultCallback requestResultCallback) {
            this.action = string2;
            this.request = map;
            this.onComplete = requestResultCallback;
        }

        public String getAction() {
            return this.action;
        }

        public RequestResultCallback getOnComplete() {
            return this.onComplete;
        }

        public Map<String, Object> getRequest() {
            return this.request;
        }

        public void markSent() {
            this.sent = true;
        }

        public boolean wasSent() {
            return this.sent;
        }
    }

    private static class QuerySpec {
        private final List<String> path;
        private final Map<String, Object> queryParams;

        public QuerySpec(List<String> list, Map<String, Object> map) {
            this.path = list;
            this.queryParams = map;
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof QuerySpec)) {
                return false;
            }
            object = (QuerySpec)object;
            if (!this.path.equals(((QuerySpec)object).path)) {
                return false;
            }
            return this.queryParams.equals(((QuerySpec)object).queryParams);
        }

        public int hashCode() {
            return this.path.hashCode() * 31 + this.queryParams.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(ConnectionUtils.pathToString(this.path));
            stringBuilder.append(" (params: ");
            stringBuilder.append(this.queryParams);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }
}

