/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.os.CancellationSignal
 */
package androidx.core.os;

import android.os.Build;
import androidx.core.os.OperationCanceledException;

public final class CancellationSignal {
    private boolean mCancelInProgress;
    private Object mCancellationSignalObj;
    private boolean mIsCanceled;
    private OnCancelListener mOnCancelListener;

    private void waitForCancelFinishedLocked() {
        while (this.mCancelInProgress) {
            try {
                this.wait();
            }
            catch (InterruptedException interruptedException) {}
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void cancel() {
        block14: {
            Throwable throwable2;
            block13: {
                // MONITORENTER : this
                if (this.mIsCanceled) {
                    // MONITOREXIT : this
                    return;
                }
                this.mIsCanceled = true;
                this.mCancelInProgress = true;
                OnCancelListener onCancelListener = this.mOnCancelListener;
                Object object = this.mCancellationSignalObj;
                // MONITOREXIT : this
                if (onCancelListener != null) {
                    try {
                        onCancelListener.onCancel();
                    }
                    catch (Throwable throwable2) {
                        break block13;
                    }
                }
                if (object != null && Build.VERSION.SDK_INT >= 16) {
                    ((android.os.CancellationSignal)object).cancel();
                }
                break block14;
            }
            // MONITORENTER : this
            this.mCancelInProgress = false;
            this.notifyAll();
            // MONITOREXIT : this
            throw throwable2;
        }
        // MONITORENTER : this
        this.mCancelInProgress = false;
        this.notifyAll();
        // MONITOREXIT : this
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Object getCancellationSignalObject() {
        if (Build.VERSION.SDK_INT < 16) {
            return null;
        }
        synchronized (this) {
            if (this.mCancellationSignalObj != null) return this.mCancellationSignalObj;
            Object object = new android.os.CancellationSignal();
            this.mCancellationSignalObj = object;
            if (!this.mIsCanceled) return this.mCancellationSignalObj;
            object.cancel();
            return this.mCancellationSignalObj;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean isCanceled() {
        synchronized (this) {
            return this.mIsCanceled;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setOnCancelListener(OnCancelListener onCancelListener) {
        synchronized (this) {
            this.waitForCancelFinishedLocked();
            if (this.mOnCancelListener == onCancelListener) {
                return;
            }
            this.mOnCancelListener = onCancelListener;
            if (this.mIsCanceled && onCancelListener != null) {
                // MONITOREXIT @DISABLED, blocks:[2, 3] lbl8 : MonitorExitStatement: MONITOREXIT : this
                onCancelListener.onCancel();
                return;
            }
            return;
        }
    }

    public void throwIfCanceled() {
        if (!this.isCanceled()) {
            return;
        }
        throw new OperationCanceledException();
    }

    public static interface OnCancelListener {
        public void onCancel();
    }
}

