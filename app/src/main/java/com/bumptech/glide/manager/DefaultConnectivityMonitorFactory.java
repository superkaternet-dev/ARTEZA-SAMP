/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.Log
 */
package com.bumptech.glide.manager;

import android.content.Context;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.DefaultConnectivityMonitor;
import com.bumptech.glide.manager.NullConnectivityMonitor;

public class DefaultConnectivityMonitorFactory
implements ConnectivityMonitorFactory {
    private static final String NETWORK_PERMISSION = "android.permission.ACCESS_NETWORK_STATE";
    private static final String TAG = "ConnectivityMonitor";

    @Override
    public ConnectivityMonitor build(Context object, ConnectivityMonitor.ConnectivityListener connectivityListener) {
        boolean bl = ContextCompat.checkSelfPermission(object, NETWORK_PERMISSION) == 0;
        if (Log.isLoggable((String)TAG, (int)3)) {
            String string2 = bl ? "ACCESS_NETWORK_STATE permission granted, registering connectivity monitor" : "ACCESS_NETWORK_STATE permission missing, cannot register connectivity monitor";
            Log.d((String)TAG, (String)string2);
        }
        object = bl ? new DefaultConnectivityMonitor((Context)object, connectivityListener) : new NullConnectivityMonitor();
        return object;
    }
}

