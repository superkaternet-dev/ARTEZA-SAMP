/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class NativeOnCompleteListener
implements OnCompleteListener<Object> {
    private final long zza;

    public NativeOnCompleteListener(long l) {
        this.zza = l;
    }

    public static void createAndAddCallback(Task<Object> task, long l) {
        task.addOnCompleteListener(new NativeOnCompleteListener(l));
    }

    public native void nativeOnComplete(long var1, Object var3, boolean var4, boolean var5, String var6);

    @Override
    public void onComplete(Task<Object> task) {
        String string2;
        Object object;
        if (task.isSuccessful()) {
            object = task.getResult();
            string2 = null;
        } else if (!task.isCanceled() && (object = task.getException()) != null) {
            string2 = ((Throwable)object).getMessage();
            object = null;
        } else {
            object = null;
            string2 = null;
        }
        this.nativeOnComplete(this.zza, object, task.isSuccessful(), task.isCanceled(), string2);
    }
}

