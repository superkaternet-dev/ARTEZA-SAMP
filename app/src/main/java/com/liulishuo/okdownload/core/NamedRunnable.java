/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core;

public abstract class NamedRunnable
implements Runnable {
    protected final String name;

    public NamedRunnable(String string2) {
        this.name = string2;
    }

    protected abstract void execute() throws InterruptedException;

    protected abstract void finished();

    protected abstract void interrupted(InterruptedException var1);

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void run() {
        var1_1 = Thread.currentThread().getName();
        Thread.currentThread().setName(this.name);
        this.execute();
        Thread.currentThread().setName(var1_1);
        this.finished();
        return;
        {
            catch (InterruptedException var2_3) {}
            {
                this.interrupted(var2_3);
            }
        }
        ** finally { 
lbl11:
        // 1 sources

        Thread.currentThread().setName(var1_1);
        this.finished();
        throw var2_2;
    }
}

