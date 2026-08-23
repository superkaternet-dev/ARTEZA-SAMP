/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Process
 */
package com.downloader.core;

import android.os.Process;
import java.util.concurrent.ThreadFactory;

public class PriorityThreadFactory
implements ThreadFactory {
    private final int mThreadPriority;

    PriorityThreadFactory(int n) {
        this.mThreadPriority = n;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(new Runnable(this, runnable){
            final PriorityThreadFactory this$0;
            final Runnable val$runnable;
            {
                this.this$0 = priorityThreadFactory;
                this.val$runnable = runnable;
            }

            @Override
            public void run() {
                try {
                    Process.setThreadPriority((int)this.this$0.mThreadPriority);
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                this.val$runnable.run();
            }
        });
    }
}

