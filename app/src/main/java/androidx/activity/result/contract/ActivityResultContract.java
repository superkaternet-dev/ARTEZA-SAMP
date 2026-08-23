/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 */
package androidx.activity.result.contract;

import android.content.Context;
import android.content.Intent;

public abstract class ActivityResultContract<I, O> {
    public abstract Intent createIntent(Context var1, I var2);

    public SynchronousResult<O> getSynchronousResult(Context context, I i) {
        return null;
    }

    public abstract O parseResult(int var1, Intent var2);

    public static final class SynchronousResult<T> {
        private final T mValue;

        public SynchronousResult(T t) {
            this.mValue = t;
        }

        public T getValue() {
            return this.mValue;
        }
    }
}

