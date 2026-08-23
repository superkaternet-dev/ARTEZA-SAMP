/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Looper
 */
package com.google.android.gms.tasks;

import android.os.Looper;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.tasks.zza;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.gms.tasks.zzaa;
import com.google.android.gms.tasks.zzab;
import com.google.android.gms.tasks.zzad;
import com.google.android.gms.tasks.zzae;
import com.google.android.gms.tasks.zzaf;
import com.google.android.gms.tasks.zzb;
import com.google.android.gms.tasks.zzw;
import com.google.android.gms.tasks.zzx;
import com.google.android.gms.tasks.zzy;
import com.google.android.gms.tasks.zzz;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class Tasks {
    private Tasks() {
    }

    public static <TResult> TResult await(Task<TResult> task) throws ExecutionException, InterruptedException {
        Preconditions.checkNotMainThread();
        Preconditions.checkNotNull(task, "Task must not be null");
        if (task.isComplete()) {
            return Tasks.zza(task);
        }
        zzad zzad2 = new zzad(null);
        Tasks.zzb(task, zzad2);
        zzad2.zza();
        return Tasks.zza(task);
    }

    public static <TResult> TResult await(Task<TResult> task, long l, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        Preconditions.checkNotMainThread();
        Preconditions.checkNotNull(task, "Task must not be null");
        Preconditions.checkNotNull(timeUnit, "TimeUnit must not be null");
        if (task.isComplete()) {
            return Tasks.zza(task);
        }
        zzad zzad2 = new zzad(null);
        Tasks.zzb(task, zzad2);
        if (zzad2.zzb(l, timeUnit)) {
            return Tasks.zza(task);
        }
        throw new TimeoutException("Timed out waiting for Task");
    }

    @Deprecated
    public static <TResult> Task<TResult> call(Callable<TResult> callable) {
        return Tasks.call(TaskExecutors.MAIN_THREAD, callable);
    }

    @Deprecated
    public static <TResult> Task<TResult> call(Executor executor, Callable<TResult> callable) {
        Preconditions.checkNotNull(executor, "Executor must not be null");
        Preconditions.checkNotNull(callable, "Callback must not be null");
        zzw zzw2 = new zzw();
        executor.execute(new zzz(zzw2, callable));
        return zzw2;
    }

    public static <TResult> Task<TResult> forCanceled() {
        zzw zzw2 = new zzw();
        zzw2.zzc();
        return zzw2;
    }

    public static <TResult> Task<TResult> forException(Exception exception) {
        zzw zzw2 = new zzw();
        zzw2.zza(exception);
        return zzw2;
    }

    public static <TResult> Task<TResult> forResult(TResult TResult) {
        zzw<TResult> zzw2 = new zzw<TResult>();
        zzw2.zzb(TResult);
        return zzw2;
    }

    public static Task<Void> whenAll(Collection<? extends Task<?>> object) {
        if (object != null && !object.isEmpty()) {
            Object object2 = object.iterator();
            while (object2.hasNext()) {
                if (object2.next() != null) continue;
                throw new NullPointerException("null tasks are not accepted");
            }
            object2 = new zzw();
            zzaf zzaf2 = new zzaf(object.size(), (zzw<Void>)object2);
            object = object.iterator();
            while (object.hasNext()) {
                Tasks.zzb((Task)object.next(), zzaf2);
            }
            return object2;
        }
        return Tasks.forResult(null);
    }

    public static Task<Void> whenAll(Task<?> ... taskArray) {
        if (taskArray != null && taskArray.length != 0) {
            return Tasks.whenAll(Arrays.asList(taskArray));
        }
        return Tasks.forResult(null);
    }

    public static Task<List<Task<?>>> whenAllComplete(Collection<? extends Task<?>> object) {
        if (object != null && !object.isEmpty()) {
            Task<Void> task = Tasks.whenAll(object);
            object = new zzab((Collection)object);
            return task.continueWithTask(TaskExecutors.MAIN_THREAD, object);
        }
        return Tasks.forResult(Collections.emptyList());
    }

    public static Task<List<Task<?>>> whenAllComplete(Task<?> ... taskArray) {
        if (taskArray != null && taskArray.length != 0) {
            return Tasks.whenAllComplete(Arrays.asList(taskArray));
        }
        return Tasks.forResult(Collections.emptyList());
    }

    public static <TResult> Task<List<TResult>> whenAllSuccess(Collection<? extends Task> object) {
        if (object != null && !object.isEmpty()) {
            Task<Void> task = Tasks.whenAll(object);
            object = new zzaa((Collection)object);
            return task.continueWith(TaskExecutors.MAIN_THREAD, object);
        }
        return Tasks.forResult(Collections.emptyList());
    }

    public static <TResult> Task<List<TResult>> whenAllSuccess(Task ... taskArray) {
        if (taskArray != null && taskArray.length != 0) {
            return Tasks.whenAllSuccess(Arrays.asList(taskArray));
        }
        return Tasks.forResult(Collections.emptyList());
    }

    public static <T> Task<T> withTimeout(Task<T> task, long l, TimeUnit timeUnit) {
        Preconditions.checkNotNull(task, "Task must not be null");
        boolean bl = l > 0L;
        Preconditions.checkArgument(bl, "Timeout must be positive");
        Preconditions.checkNotNull(timeUnit, "TimeUnit must not be null");
        zzb zzb2 = new zzb();
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource(zzb2);
        zza zza2 = new zza(Looper.getMainLooper());
        zza2.postDelayed(new zzy(taskCompletionSource), timeUnit.toMillis(l));
        task.addOnCompleteListener(new zzx(zza2, taskCompletionSource, zzb2));
        return taskCompletionSource.getTask();
    }

    private static <TResult> TResult zza(Task<TResult> task) throws ExecutionException {
        if (task.isSuccessful()) {
            return task.getResult();
        }
        if (task.isCanceled()) {
            throw new CancellationException("Task is already canceled");
        }
        throw new ExecutionException(task.getException());
    }

    private static <T> void zzb(Task<T> task, zzae<? super T> zzae2) {
        task.addOnSuccessListener(TaskExecutors.zza, zzae2);
        task.addOnFailureListener(TaskExecutors.zza, zzae2);
        task.addOnCanceledListener(TaskExecutors.zza, zzae2);
    }
}

