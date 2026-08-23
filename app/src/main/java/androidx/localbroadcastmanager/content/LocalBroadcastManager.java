/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.os.Handler
 *  android.os.Message
 *  android.util.Log
 */
package androidx.localbroadcastmanager.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;

public final class LocalBroadcastManager {
    private static final boolean DEBUG = false;
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final String TAG = "LocalBroadcastManager";
    private static LocalBroadcastManager mInstance;
    private static final Object mLock;
    private final HashMap<String, ArrayList<ReceiverRecord>> mActions;
    private final Context mAppContext;
    private final Handler mHandler;
    private final ArrayList<BroadcastRecord> mPendingBroadcasts;
    private final HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> mReceivers = new HashMap();

    static {
        mLock = new Object();
    }

    private LocalBroadcastManager(Context context) {
        this.mActions = new HashMap();
        this.mPendingBroadcasts = new ArrayList();
        this.mAppContext = context;
        this.mHandler = new Handler(this, context.getMainLooper()){
            final LocalBroadcastManager this$0;
            {
                this.this$0 = localBroadcastManager;
                super(looper);
            }

            public void handleMessage(Message message) {
                switch (message.what) {
                    default: {
                        super.handleMessage(message);
                        break;
                    }
                    case 1: {
                        this.this$0.executePendingBroadcasts();
                    }
                }
            }
        };
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static LocalBroadcastManager getInstance(Context object) {
        Object object2 = mLock;
        synchronized (object2) {
            LocalBroadcastManager localBroadcastManager;
            if (mInstance != null) return mInstance;
            mInstance = localBroadcastManager = new LocalBroadcastManager(object.getApplicationContext());
            return mInstance;
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void executePendingBroadcasts() {
        Throwable throwable2;
        block5: while (true) {
            BroadcastRecord[] broadcastRecordArray;
            int n;
            Object object = this.mReceivers;
            synchronized (object) {
                n = this.mPendingBroadcasts.size();
                if (n <= 0) {
                    return;
                }
                broadcastRecordArray = new BroadcastRecord[n];
                try {
                    this.mPendingBroadcasts.toArray(broadcastRecordArray);
                    this.mPendingBroadcasts.clear();
                    // MONITOREXIT @DISABLED, blocks:[1, 3, 5] lbl13 : MonitorExitStatement: MONITOREXIT : var5_6
                    n = 0;
                }
                catch (Throwable throwable2) {
                    break;
                }
            }
            while (true) {
                if (n >= broadcastRecordArray.length) continue block5;
                BroadcastRecord broadcastRecord = broadcastRecordArray[n];
                int n2 = broadcastRecord.receivers.size();
                for (int i = 0; i < n2; ++i) {
                    object = broadcastRecord.receivers.get(i);
                    if (((ReceiverRecord)object).dead) continue;
                    ((ReceiverRecord)object).receiver.onReceive(this.mAppContext, broadcastRecord.intent);
                }
                ++n;
            }
            break;
        }
        {
            throw throwable2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void registerReceiver(BroadcastReceiver object, IntentFilter intentFilter) {
        HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> hashMap = this.mReceivers;
        synchronized (hashMap) {
            ReceiverRecord receiverRecord = new ReceiverRecord(intentFilter, (BroadcastReceiver)object);
            Object object2 = this.mReceivers.get(object);
            ArrayList<ReceiverRecord> arrayList = object2;
            if (object2 == null) {
                arrayList = new ArrayList<ReceiverRecord>(1);
                this.mReceivers.put((BroadcastReceiver)object, arrayList);
            }
            arrayList.add(receiverRecord);
            for (int i = 0; i < intentFilter.countActions(); ++i) {
                object2 = intentFilter.getAction(i);
                arrayList = this.mActions.get(object2);
                object = arrayList;
                if (arrayList == null) {
                    object = new ArrayList(1);
                    this.mActions.put((String)object2, (ArrayList<ReceiverRecord>)object);
                }
                object.add(receiverRecord);
            }
            return;
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean sendBroadcast(Intent var1_1) {
        var8_3 = this.mReceivers;
        synchronized (var8_3) {
            block24: {
                block22: {
                    block21: {
                        block23: {
                            var9_4 = var1_1.getAction();
                            var7_5 = var1_1.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
                            var12_6 = var1_1.getData();
                            var11_7 = var1_1.getScheme();
                            var10_8 = var1_1.getCategories();
                            var2_9 = (var1_1.getFlags() & 8) != 0 ? 1 : 0;
                            if (var2_9 != 0) {
                                var5_10 = new IntentFilter();
                                var5_10.append("Resolving type ");
                                var5_10.append((String)var7_5);
                                var5_10.append(" scheme ");
                                var5_10.append(var11_7);
                                var5_10.append(" of intent ");
                                var5_10.append(var1_1);
                                Log.v((String)"LocalBroadcastManager", (String)var5_10.toString());
                            }
                            if ((var13_11 = this.mActions.get(var1_1.getAction())) == null) return false;
                            if (var2_9 != 0) {
                                var5_10 = new IntentFilter();
                                var5_10.append("Action list: ");
                                var5_10.append(var13_11);
                                Log.v((String)"LocalBroadcastManager", (String)var5_10.toString());
                            }
                            var6_12 /* !! */  = null;
                            var3_13 = 0;
lbl37:
                            // 2 sources

                            while (var3_13 < var13_11.size()) {
                                var14_15 = var13_11.get(var3_13);
                                if (var2_9 != 0) {
                                    var5_10 = new IntentFilter();
                                    var5_10.append("Matching against filter ");
                                    var5_10.append(var14_15.filter);
                                    Log.v((String)"LocalBroadcastManager", (String)var5_10.toString());
                                }
                                if (var14_15.broadcasting) {
                                    if (var2_9 != 0) {
                                        Log.v((String)"LocalBroadcastManager", (String)"  Filter's target already added");
                                    }
                                    break block21;
                                }
                                var5_10 = var14_15.filter;
                                var4_14 = var5_10.match(var9_4, (String)var7_5, var11_7, var12_6, var10_8, "LocalBroadcastManager");
                                if (var4_14 >= 0) {
                                    if (var2_9 != 0) {
                                        var5_10 = new IntentFilter();
                                        var5_10.append("  Filter matched!  match=0x");
                                        var5_10.append(Integer.toHexString(var4_14));
                                        Log.v((String)"LocalBroadcastManager", (String)var5_10.toString());
                                    }
                                    var5_10 = var6_12 /* !! */  == null ? new IntentFilter() : var6_12 /* !! */ ;
                                    var5_10.add(var14_15);
                                    var14_15.broadcasting = true;
                                    break block22;
                                }
                                if (var2_9 == 0) break block21;
                                break block23;
                            }
                            break block24;
                        }
                        switch (var4_14) {
                            default: {
                                var5_10 = "unknown reason";
                                break;
                            }
                            case -1: {
                                var5_10 = "type";
                                break;
                            }
                            case -2: {
                                var5_10 = "data";
                                break;
                            }
                            case -3: {
                                var5_10 = "action";
                                break;
                            }
                            case -4: {
                                var5_10 = "category";
                            }
                        }
                        {
                            var14_15 = new StringBuilder();
                            var14_15.append("  Filter did not match: ");
                            var14_15.append((String)var5_10);
                            Log.v((String)"LocalBroadcastManager", (String)var14_15.toString());
                        }
                    }
                    var5_10 = var6_12 /* !! */ ;
                }
                ++var3_13;
                var6_12 /* !! */  = var5_10;
                ** GOTO lbl37
            }
            if (var6_12 /* !! */  == null) return false;
            {
                for (var2_9 = 0; var2_9 < var6_12 /* !! */ .size(); ++var2_9) {
                    ((ReceiverRecord)var6_12 /* !! */ .get((int)var2_9)).broadcasting = false;
                }
            }
            var5_10 = this.mPendingBroadcasts;
            var7_5 = new BroadcastRecord(var1_1, var6_12 /* !! */ );
            var5_10.add(var7_5);
            if (this.mHandler.hasMessages(1) != false) return true;
            this.mHandler.sendEmptyMessage(1);
            return true;
        }
    }

    public void sendBroadcastSync(Intent intent) {
        if (this.sendBroadcast(intent)) {
            this.executePendingBroadcasts();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {
        HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> hashMap = this.mReceivers;
        synchronized (hashMap) {
            ArrayList<ReceiverRecord> arrayList = this.mReceivers.remove(broadcastReceiver);
            if (arrayList == null) {
                return;
            }
            int n = arrayList.size() - 1;
            while (true) {
                ReceiverRecord receiverRecord;
                if (n >= 0) {
                    receiverRecord = arrayList.get(n);
                    receiverRecord.dead = true;
                } else {
                    return;
                }
                for (int i = 0; i < receiverRecord.filter.countActions(); ++i) {
                    String string2 = receiverRecord.filter.getAction(i);
                    ArrayList<ReceiverRecord> arrayList2 = this.mActions.get(string2);
                    if (arrayList2 == null) continue;
                    for (int j = arrayList2.size() - 1; j >= 0; --j) {
                        ReceiverRecord receiverRecord2 = arrayList2.get(j);
                        if (receiverRecord2.receiver != broadcastReceiver) continue;
                        receiverRecord2.dead = true;
                        arrayList2.remove(j);
                    }
                    if (arrayList2.size() > 0) continue;
                    this.mActions.remove(string2);
                }
                --n;
            }
        }
    }

    private static final class BroadcastRecord {
        final Intent intent;
        final ArrayList<ReceiverRecord> receivers;

        BroadcastRecord(Intent intent, ArrayList<ReceiverRecord> arrayList) {
            this.intent = intent;
            this.receivers = arrayList;
        }
    }

    private static final class ReceiverRecord {
        boolean broadcasting;
        boolean dead;
        final IntentFilter filter;
        final BroadcastReceiver receiver;

        ReceiverRecord(IntentFilter intentFilter, BroadcastReceiver broadcastReceiver) {
            this.filter = intentFilter;
            this.receiver = broadcastReceiver;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(128);
            stringBuilder.append("Receiver{");
            stringBuilder.append(this.receiver);
            stringBuilder.append(" filter=");
            stringBuilder.append(this.filter);
            if (this.dead) {
                stringBuilder.append(" DEAD");
            }
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }
}

