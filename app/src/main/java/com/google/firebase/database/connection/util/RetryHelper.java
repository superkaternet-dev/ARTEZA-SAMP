/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.connection.util;

import com.google.firebase.database.logging.LogWrapper;
import com.google.firebase.database.logging.Logger;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RetryHelper {
    private long currentRetryDelay;
    private final ScheduledExecutorService executorService;
    private final double jitterFactor;
    private boolean lastWasSuccess = true;
    private final LogWrapper logger;
    private final long maxRetryDelay;
    private final long minRetryDelayAfterFailure;
    private final Random random = new Random();
    private final double retryExponent;
    private ScheduledFuture<?> scheduledRetry;

    private RetryHelper(ScheduledExecutorService scheduledExecutorService, LogWrapper logWrapper, long l, long l2, double d, double d2) {
        this.executorService = scheduledExecutorService;
        this.logger = logWrapper;
        this.minRetryDelayAfterFailure = l;
        this.maxRetryDelay = l2;
        this.retryExponent = d;
        this.jitterFactor = d2;
    }

    static /* synthetic */ ScheduledFuture access$002(RetryHelper retryHelper, ScheduledFuture scheduledFuture) {
        retryHelper.scheduledRetry = scheduledFuture;
        return scheduledFuture;
    }

    public void cancel() {
        if (this.scheduledRetry != null) {
            this.logger.debug("Cancelling existing retry attempt", new Object[0]);
            this.scheduledRetry.cancel(false);
            this.scheduledRetry = null;
        } else {
            this.logger.debug("No existing retry attempt to cancel", new Object[0]);
        }
        this.currentRetryDelay = 0L;
    }

    public void retry(Runnable runnable) {
        long l;
        runnable = new Runnable(this, runnable){
            final RetryHelper this$0;
            final Runnable val$runnable;
            {
                this.this$0 = retryHelper;
                this.val$runnable = runnable;
            }

            @Override
            public void run() {
                RetryHelper.access$002(this.this$0, null);
                this.val$runnable.run();
            }
        };
        if (this.scheduledRetry != null) {
            this.logger.debug("Cancelling previous scheduled retry", new Object[0]);
            this.scheduledRetry.cancel(false);
            this.scheduledRetry = null;
        }
        if (this.lastWasSuccess) {
            l = 0L;
        } else {
            double d;
            double d2;
            l = this.currentRetryDelay;
            if (l == 0L) {
                this.currentRetryDelay = this.minRetryDelayAfterFailure;
            } else {
                d2 = l;
                d = this.retryExponent;
                Double.isNaN(d2);
                this.currentRetryDelay = Math.min((long)(d2 * d), this.maxRetryDelay);
            }
            double d3 = this.jitterFactor;
            l = this.currentRetryDelay;
            d2 = l;
            Double.isNaN(d2);
            d = l;
            Double.isNaN(d);
            l = (long)((1.0 - d3) * d2 + d3 * d * this.random.nextDouble());
        }
        this.lastWasSuccess = false;
        this.logger.debug("Scheduling retry in %dms", l);
        this.scheduledRetry = this.executorService.schedule(runnable, l, TimeUnit.MILLISECONDS);
    }

    public void setMaxDelay() {
        this.currentRetryDelay = this.maxRetryDelay;
    }

    public void signalSuccess() {
        this.lastWasSuccess = true;
        this.currentRetryDelay = 0L;
    }

    public static class Builder {
        private double jitterFactor = 0.5;
        private final LogWrapper logger;
        private long minRetryDelayAfterFailure = 1000L;
        private double retryExponent = 1.3;
        private long retryMaxDelay = 30000L;
        private final ScheduledExecutorService service;

        public Builder(ScheduledExecutorService scheduledExecutorService, Logger logger, String string2) {
            this.service = scheduledExecutorService;
            this.logger = new LogWrapper(logger, string2);
        }

        public RetryHelper build() {
            return new RetryHelper(this.service, this.logger, this.minRetryDelayAfterFailure, this.retryMaxDelay, this.retryExponent, this.jitterFactor);
        }

        public Builder withJitterFactor(double d) {
            if (!(d < 0.0) && !(d > 1.0)) {
                this.jitterFactor = d;
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Argument out of range: ");
            stringBuilder.append(d);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder withMaxDelay(long l) {
            this.retryMaxDelay = l;
            return this;
        }

        public Builder withMinDelayAfterFailure(long l) {
            this.minRetryDelayAfterFailure = l;
            return this;
        }

        public Builder withRetryExponent(double d) {
            this.retryExponent = d;
            return this;
        }
    }
}

