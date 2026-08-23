/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.net.ConnectivityManager
 *  android.net.ConnectivityManager$NetworkCallback
 *  android.net.Network
 *  android.net.NetworkInfo
 *  android.os.Build$VERSION
 *  android.util.Log
 */
package com.bumptech.glide.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.util.GlideSuppliers;
import com.bumptech.glide.util.Util;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

final class SingletonConnectivityReceiver {
    private static final String TAG = "ConnectivityMonitor";
    private static volatile SingletonConnectivityReceiver instance;
    private final FrameworkConnectivityMonitor frameworkConnectivityMonitor;
    private boolean isRegistered;
    final Set<ConnectivityMonitor.ConnectivityListener> listeners = new HashSet<ConnectivityMonitor.ConnectivityListener>();

    private SingletonConnectivityReceiver(Context object) {
        GlideSuppliers.GlideSupplier<ConnectivityManager> glideSupplier = GlideSuppliers.memorize(new GlideSuppliers.GlideSupplier<ConnectivityManager>(this, object){
            final SingletonConnectivityReceiver this$0;
            final Context val$context;
            {
                this.this$0 = singletonConnectivityReceiver;
                this.val$context = context;
            }

            @Override
            public ConnectivityManager get() {
                return (ConnectivityManager)this.val$context.getSystemService("connectivity");
            }
        });
        ConnectivityMonitor.ConnectivityListener connectivityListener = new ConnectivityMonitor.ConnectivityListener(this){
            final SingletonConnectivityReceiver this$0;
            {
                this.this$0 = singletonConnectivityReceiver;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void onConnectivityChanged(boolean bl) {
                Object object = this.this$0;
                synchronized (object) {
                    ArrayList<ConnectivityMonitor.ConnectivityListener> arrayList = new ArrayList<ConnectivityMonitor.ConnectivityListener>(this.this$0.listeners);
                    // MONITOREXIT @DISABLED, blocks:[0, 2] lbl5 : MonitorExitStatement: MONITOREXIT : var2_2
                    object = arrayList.iterator();
                    {
                        catch (Throwable throwable) {}
                        {
                            throw throwable;
                        }
                    }
                }
                while (object.hasNext()) {
                    ((ConnectivityMonitor.ConnectivityListener)object.next()).onConnectivityChanged(bl);
                }
            }
        };
        object = Build.VERSION.SDK_INT >= 24 ? new FrameworkConnectivityMonitorPostApi24(glideSupplier, connectivityListener) : new FrameworkConnectivityMonitorPreApi24((Context)object, glideSupplier, connectivityListener);
        this.frameworkConnectivityMonitor = object;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static SingletonConnectivityReceiver get(Context context) {
        if (instance != null) return instance;
        synchronized (SingletonConnectivityReceiver.class) {
            SingletonConnectivityReceiver singletonConnectivityReceiver;
            if (instance != null) return instance;
            instance = singletonConnectivityReceiver = new SingletonConnectivityReceiver(context.getApplicationContext());
            return instance;
        }
    }

    private void maybeRegisterReceiver() {
        if (!this.isRegistered && !this.listeners.isEmpty()) {
            this.isRegistered = this.frameworkConnectivityMonitor.register();
            return;
        }
    }

    private void maybeUnregisterReceiver() {
        if (this.isRegistered && this.listeners.isEmpty()) {
            this.frameworkConnectivityMonitor.unregister();
            this.isRegistered = false;
            return;
        }
    }

    static void reset() {
        instance = null;
    }

    void register(ConnectivityMonitor.ConnectivityListener connectivityListener) {
        synchronized (this) {
            this.listeners.add(connectivityListener);
            this.maybeRegisterReceiver();
            return;
        }
    }

    void unregister(ConnectivityMonitor.ConnectivityListener connectivityListener) {
        synchronized (this) {
            this.listeners.remove(connectivityListener);
            this.maybeUnregisterReceiver();
            return;
        }
    }

    private static interface FrameworkConnectivityMonitor {
        public boolean register();

        public void unregister();
    }

    private static final class FrameworkConnectivityMonitorPostApi24
    implements FrameworkConnectivityMonitor {
        private final GlideSuppliers.GlideSupplier<ConnectivityManager> connectivityManager;
        boolean isConnected;
        final ConnectivityMonitor.ConnectivityListener listener;
        private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback(this){
            final FrameworkConnectivityMonitorPostApi24 this$0;
            {
                this.this$0 = frameworkConnectivityMonitorPostApi24;
            }

            private void postOnConnectivityChange(boolean bl) {
                Util.postOnUiThread(new Runnable(this, bl){
                    final 1 this$1;
                    final boolean val$newState;
                    {
                        this.this$1 = var1_1;
                        this.val$newState = bl;
                    }

                    @Override
                    public void run() {
                        this.this$1.onConnectivityChange(this.val$newState);
                    }
                });
            }

            public void onAvailable(Network network) {
                this.postOnConnectivityChange(true);
            }

            void onConnectivityChange(boolean bl) {
                Util.assertMainThread();
                boolean bl2 = this.this$0.isConnected;
                this.this$0.isConnected = bl;
                if (bl2 != bl) {
                    this.this$0.listener.onConnectivityChanged(bl);
                }
            }

            public void onLost(Network network) {
                this.postOnConnectivityChange(false);
            }
        };

        FrameworkConnectivityMonitorPostApi24(GlideSuppliers.GlideSupplier<ConnectivityManager> glideSupplier, ConnectivityMonitor.ConnectivityListener connectivityListener) {
            this.connectivityManager = glideSupplier;
            this.listener = connectivityListener;
        }

        @Override
        public boolean register() {
            boolean bl = this.connectivityManager.get().getActiveNetwork() != null;
            this.isConnected = bl;
            try {
                this.connectivityManager.get().registerDefaultNetworkCallback(this.networkCallback);
                return true;
            }
            catch (RuntimeException runtimeException) {
                if (Log.isLoggable((String)SingletonConnectivityReceiver.TAG, (int)5)) {
                    Log.w((String)SingletonConnectivityReceiver.TAG, (String)"Failed to register callback", (Throwable)runtimeException);
                }
                return false;
            }
        }

        @Override
        public void unregister() {
            this.connectivityManager.get().unregisterNetworkCallback(this.networkCallback);
        }
    }

    private static final class FrameworkConnectivityMonitorPreApi24
    implements FrameworkConnectivityMonitor {
        private final GlideSuppliers.GlideSupplier<ConnectivityManager> connectivityManager;
        private final BroadcastReceiver connectivityReceiver = new BroadcastReceiver(this){
            final FrameworkConnectivityMonitorPreApi24 this$0;
            {
                this.this$0 = frameworkConnectivityMonitorPreApi24;
            }

            public void onReceive(Context object, Intent intent) {
                boolean bl = this.this$0.isConnected;
                object = this.this$0;
                ((FrameworkConnectivityMonitorPreApi24)object).isConnected = ((FrameworkConnectivityMonitorPreApi24)object).isConnected();
                if (bl != this.this$0.isConnected) {
                    if (Log.isLoggable((String)SingletonConnectivityReceiver.TAG, (int)3)) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("connectivity changed, isConnected: ");
                        ((StringBuilder)object).append(this.this$0.isConnected);
                        Log.d((String)SingletonConnectivityReceiver.TAG, (String)((StringBuilder)object).toString());
                    }
                    this.this$0.listener.onConnectivityChanged(this.this$0.isConnected);
                }
            }
        };
        private final Context context;
        boolean isConnected;
        final ConnectivityMonitor.ConnectivityListener listener;

        FrameworkConnectivityMonitorPreApi24(Context context, GlideSuppliers.GlideSupplier<ConnectivityManager> glideSupplier, ConnectivityMonitor.ConnectivityListener connectivityListener) {
            this.context = context.getApplicationContext();
            this.connectivityManager = glideSupplier;
            this.listener = connectivityListener;
        }

        boolean isConnected() {
            NetworkInfo networkInfo;
            boolean bl = true;
            try {
                networkInfo = this.connectivityManager.get().getActiveNetworkInfo();
            }
            catch (RuntimeException runtimeException) {
                if (Log.isLoggable((String)SingletonConnectivityReceiver.TAG, (int)5)) {
                    Log.w((String)SingletonConnectivityReceiver.TAG, (String)"Failed to determine connectivity status when connectivity changed", (Throwable)runtimeException);
                }
                return true;
            }
            if (networkInfo == null || !networkInfo.isConnected()) {
                bl = false;
            }
            return bl;
        }

        @Override
        public boolean register() {
            this.isConnected = this.isConnected();
            try {
                Context context = this.context;
                BroadcastReceiver broadcastReceiver = this.connectivityReceiver;
                IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
                context.registerReceiver(broadcastReceiver, intentFilter);
                return true;
            }
            catch (SecurityException securityException) {
                if (Log.isLoggable((String)SingletonConnectivityReceiver.TAG, (int)5)) {
                    Log.w((String)SingletonConnectivityReceiver.TAG, (String)"Failed to register", (Throwable)securityException);
                }
                return false;
            }
        }

        @Override
        public void unregister() {
            this.context.unregisterReceiver(this.connectivityReceiver);
        }
    }
}

