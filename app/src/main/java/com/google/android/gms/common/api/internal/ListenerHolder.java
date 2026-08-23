/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Looper
 */
package com.google.android.gms.common.api.internal;

import android.os.Looper;
import com.google.android.gms.common.api.internal.zacb;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.concurrent.HandlerExecutor;
import java.util.concurrent.Executor;

public final class ListenerHolder<L> {
    private final Executor zaa;
    private volatile L zab;
    private volatile ListenerKey<L> zac;

    ListenerHolder(Looper looper, L l, String string2) {
        this.zaa = new HandlerExecutor(looper);
        this.zab = Preconditions.checkNotNull(l, "Listener must not be null");
        this.zac = new ListenerKey<L>(l, Preconditions.checkNotEmpty(string2));
    }

    ListenerHolder(Executor executor, L l, String string2) {
        this.zaa = Preconditions.checkNotNull(executor, "Executor must not be null");
        this.zab = Preconditions.checkNotNull(l, "Listener must not be null");
        this.zac = new ListenerKey<L>(l, Preconditions.checkNotEmpty(string2));
    }

    public void clear() {
        this.zab = null;
        this.zac = null;
    }

    public ListenerKey<L> getListenerKey() {
        return this.zac;
    }

    public boolean hasListener() {
        return this.zab != null;
    }

    public void notifyListener(Notifier<? super L> notifier) {
        Preconditions.checkNotNull(notifier, "Notifier must not be null");
        this.zaa.execute(new zacb(this, notifier));
    }

    final void zaa(Notifier<? super L> notifier) {
        L l = this.zab;
        if (l == null) {
            notifier.onNotifyListenerFailed();
            return;
        }
        try {
            notifier.notifyListener(l);
            return;
        }
        catch (RuntimeException runtimeException) {
            notifier.onNotifyListenerFailed();
            throw runtimeException;
        }
    }

    public static final class ListenerKey<L> {
        private final L zaa;
        private final String zab;

        ListenerKey(L l, String string2) {
            this.zaa = l;
            this.zab = string2;
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof ListenerKey)) {
                return false;
            }
            object = (ListenerKey)object;
            return this.zaa == ((ListenerKey)object).zaa && this.zab.equals(((ListenerKey)object).zab);
        }

        public int hashCode() {
            return System.identityHashCode(this.zaa) * 31 + this.zab.hashCode();
        }

        public String toIdString() {
            String string2 = this.zab;
            int n = System.identityHashCode(this.zaa);
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(string2).length() + 12);
            stringBuilder.append(string2);
            stringBuilder.append("@");
            stringBuilder.append(n);
            return stringBuilder.toString();
        }
    }

    public static interface Notifier<L> {
        public void notifyListener(L var1);

        public void onNotifyListenerFailed();
    }
}

