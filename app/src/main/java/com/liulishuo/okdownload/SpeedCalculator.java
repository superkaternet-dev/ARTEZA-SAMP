/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.SystemClock
 */
package com.liulishuo.okdownload;

import android.os.SystemClock;
import com.liulishuo.okdownload.core.Util;

public class SpeedCalculator {
    long allIncreaseBytes;
    long beginTimestamp;
    long bytesPerSecond;
    long endTimestamp;
    long increaseBytes;
    long timestamp;

    private static String humanReadableSpeed(long l, boolean bl) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Util.humanReadableBytes(l, bl));
        stringBuilder.append("/s");
        return stringBuilder.toString();
    }

    public String averageSpeed() {
        return this.speedFromBegin();
    }

    public void downloading(long l) {
        synchronized (this) {
            if (this.timestamp == 0L) {
                long l2;
                this.timestamp = l2 = this.nowMillis();
                this.beginTimestamp = l2;
            }
            this.increaseBytes += l;
            this.allIncreaseBytes += l;
            return;
        }
    }

    public void endTask() {
        synchronized (this) {
            this.endTimestamp = this.nowMillis();
            return;
        }
    }

    public void flush() {
        synchronized (this) {
            long l = this.nowMillis();
            long l2 = this.increaseBytes;
            long l3 = Math.max(1L, l - this.timestamp);
            this.increaseBytes = 0L;
            this.timestamp = l;
            this.bytesPerSecond = (long)((float)l2 / (float)l3 * 1000.0f);
            return;
        }
    }

    public long getBytesPerSecondAndFlush() {
        synchronized (this) {
            long l;
            block7: {
                long l2;
                block6: {
                    l = this.nowMillis() - this.timestamp;
                    if (l >= 1000L) break block6;
                    l2 = this.bytesPerSecond;
                    if (l2 == 0L) break block6;
                    return l2;
                }
                l2 = this.bytesPerSecond;
                if (l2 != 0L || l >= 500L) break block7;
                return 0L;
            }
            l = this.getInstantBytesPerSecondAndFlush();
            return l;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public long getBytesPerSecondFromBegin() {
        synchronized (this) {
            long l;
            long l2 = l = this.endTimestamp;
            if (l == 0L) {
                l2 = this.nowMillis();
            }
            l = this.allIncreaseBytes;
            l2 = Math.max(1L, l2 - this.beginTimestamp);
            return (long)((float)l / (float)l2 * 1000.0f);
        }
    }

    public long getInstantBytesPerSecondAndFlush() {
        this.flush();
        return this.bytesPerSecond;
    }

    public long getInstantSpeedDurationMillis() {
        synchronized (this) {
            long l = this.nowMillis();
            long l2 = this.timestamp;
            return l - l2;
        }
    }

    public String getSpeedWithBinaryAndFlush() {
        return SpeedCalculator.humanReadableSpeed(this.getInstantBytesPerSecondAndFlush(), false);
    }

    public String getSpeedWithSIAndFlush() {
        return SpeedCalculator.humanReadableSpeed(this.getInstantBytesPerSecondAndFlush(), true);
    }

    public String instantSpeed() {
        return this.getSpeedWithSIAndFlush();
    }

    public String lastSpeed() {
        return SpeedCalculator.humanReadableSpeed(this.bytesPerSecond, true);
    }

    long nowMillis() {
        return SystemClock.uptimeMillis();
    }

    public void reset() {
        synchronized (this) {
            this.timestamp = 0L;
            this.increaseBytes = 0L;
            this.bytesPerSecond = 0L;
            this.beginTimestamp = 0L;
            this.endTimestamp = 0L;
            this.allIncreaseBytes = 0L;
            return;
        }
    }

    public String speed() {
        return SpeedCalculator.humanReadableSpeed(this.getBytesPerSecondAndFlush(), true);
    }

    public String speedFromBegin() {
        return SpeedCalculator.humanReadableSpeed(this.getBytesPerSecondFromBegin(), true);
    }
}

