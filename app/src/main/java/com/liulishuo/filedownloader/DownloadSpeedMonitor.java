/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.SystemClock
 */
package com.liulishuo.filedownloader;

import android.os.SystemClock;
import com.liulishuo.filedownloader.IDownloadSpeed;

public class DownloadSpeedMonitor
implements IDownloadSpeed.Monitor,
IDownloadSpeed.Lookup {
    private long mLastRefreshSofarBytes;
    private long mLastRefreshTime;
    private int mMinIntervalUpdateSpeed = 1000;
    private int mSpeed;
    private long mStartSofarBytes;
    private long mStartTime;
    private long mTotalBytes;

    @Override
    public void end(long l) {
        if (this.mStartTime <= 0L) {
            return;
        }
        long l2 = l - this.mStartSofarBytes;
        this.mLastRefreshTime = 0L;
        l = SystemClock.uptimeMillis() - this.mStartTime;
        this.mSpeed = l <= 0L ? (int)l2 : (int)(l2 / l);
    }

    @Override
    public int getSpeed() {
        return this.mSpeed;
    }

    @Override
    public void reset() {
        this.mSpeed = 0;
        this.mLastRefreshTime = 0L;
    }

    @Override
    public void setMinIntervalUpdateSpeed(int n) {
        this.mMinIntervalUpdateSpeed = n;
    }

    @Override
    public void start(long l) {
        this.mStartTime = SystemClock.uptimeMillis();
        this.mStartSofarBytes = l;
    }

    @Override
    public void update(long l) {
        int n;
        block6: {
            long l2;
            block7: {
                int n2;
                block5: {
                    if (this.mMinIntervalUpdateSpeed <= 0) {
                        return;
                    }
                    n2 = 0;
                    if (this.mLastRefreshTime != 0L) break block5;
                    n = 1;
                    break block6;
                }
                l2 = SystemClock.uptimeMillis() - this.mLastRefreshTime;
                if (l2 >= (long)this.mMinIntervalUpdateSpeed) break block7;
                n = n2;
                if (this.mSpeed != 0) break block6;
                n = n2;
                if (l2 <= 0L) break block6;
            }
            this.mSpeed = n = (int)((l - this.mLastRefreshSofarBytes) / l2);
            this.mSpeed = Math.max(0, n);
            n = 1;
        }
        if (n != 0) {
            this.mLastRefreshSofarBytes = l;
            this.mLastRefreshTime = SystemClock.uptimeMillis();
        }
    }
}

