/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.widget.ProgressBar
 */
package androidx.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class ContentLoadingProgressBar
extends ProgressBar {
    private static final int MIN_DELAY = 500;
    private static final int MIN_SHOW_TIME = 500;
    private final Runnable mDelayedHide = new Runnable(this){
        final ContentLoadingProgressBar this$0;
        {
            this.this$0 = contentLoadingProgressBar;
        }

        @Override
        public void run() {
            this.this$0.mPostedHide = false;
            this.this$0.mStartTime = -1L;
            this.this$0.setVisibility(8);
        }
    };
    private final Runnable mDelayedShow = new Runnable(this){
        final ContentLoadingProgressBar this$0;
        {
            this.this$0 = contentLoadingProgressBar;
        }

        @Override
        public void run() {
            this.this$0.mPostedShow = false;
            if (!this.this$0.mDismissed) {
                this.this$0.mStartTime = System.currentTimeMillis();
                this.this$0.setVisibility(0);
            }
        }
    };
    boolean mDismissed = false;
    boolean mPostedHide = false;
    boolean mPostedShow = false;
    long mStartTime = -1L;

    public ContentLoadingProgressBar(Context context) {
        this(context, null);
    }

    public ContentLoadingProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
    }

    private void removeCallbacks() {
        this.removeCallbacks(this.mDelayedHide);
        this.removeCallbacks(this.mDelayedShow);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void hide() {
        synchronized (this) {
            this.mDismissed = true;
            this.removeCallbacks(this.mDelayedShow);
            this.mPostedShow = false;
            long l = System.currentTimeMillis();
            long l2 = this.mStartTime;
            if ((l -= l2) < 500L && l2 != -1L) {
                if (!this.mPostedHide) {
                    this.postDelayed(this.mDelayedHide, 500L - l);
                    this.mPostedHide = true;
                }
            } else {
                this.setVisibility(8);
            }
            return;
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.removeCallbacks();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.removeCallbacks();
    }

    public void show() {
        synchronized (this) {
            this.mStartTime = -1L;
            this.mDismissed = false;
            this.removeCallbacks(this.mDelayedHide);
            this.mPostedHide = false;
            if (!this.mPostedShow) {
                this.postDelayed(this.mDelayedShow, 500L);
                this.mPostedShow = true;
            }
            return;
        }
    }
}

