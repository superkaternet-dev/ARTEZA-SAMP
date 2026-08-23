/*
 * Decompiled with CFR 0.152.
 */
package androidx.lifecycle;

import androidx.lifecycle.GeneratedAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MethodCallsLogger;

class CompositeGeneratedAdaptersObserver
implements LifecycleEventObserver {
    private final GeneratedAdapter[] mGeneratedAdapters;

    CompositeGeneratedAdaptersObserver(GeneratedAdapter[] generatedAdapterArray) {
        this.mGeneratedAdapters = generatedAdapterArray;
    }

    @Override
    public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        int n;
        MethodCallsLogger methodCallsLogger = new MethodCallsLogger();
        GeneratedAdapter[] generatedAdapterArray = this.mGeneratedAdapters;
        int n2 = generatedAdapterArray.length;
        int n3 = 0;
        for (n = 0; n < n2; ++n) {
            generatedAdapterArray[n].callMethods(lifecycleOwner, event, false, methodCallsLogger);
        }
        generatedAdapterArray = this.mGeneratedAdapters;
        n2 = generatedAdapterArray.length;
        for (n = n3; n < n2; ++n) {
            generatedAdapterArray[n].callMethods(lifecycleOwner, event, true, methodCallsLogger);
        }
    }
}

