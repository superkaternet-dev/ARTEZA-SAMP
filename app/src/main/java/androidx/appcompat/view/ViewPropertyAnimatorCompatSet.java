/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.animation.Interpolator
 */
package androidx.appcompat.view;

import android.view.View;
import android.view.animation.Interpolator;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import java.util.ArrayList;
import java.util.Iterator;

public class ViewPropertyAnimatorCompatSet {
    final ArrayList<ViewPropertyAnimatorCompat> mAnimators;
    private long mDuration = -1L;
    private Interpolator mInterpolator;
    private boolean mIsStarted;
    ViewPropertyAnimatorListener mListener;
    private final ViewPropertyAnimatorListenerAdapter mProxyListener = new ViewPropertyAnimatorListenerAdapter(this){
        private int mProxyEndCount;
        private boolean mProxyStarted;
        final ViewPropertyAnimatorCompatSet this$0;
        {
            this.this$0 = viewPropertyAnimatorCompatSet;
            this.mProxyStarted = false;
            this.mProxyEndCount = 0;
        }

        @Override
        public void onAnimationEnd(View view) {
            int n;
            this.mProxyEndCount = n = this.mProxyEndCount + 1;
            if (n == this.this$0.mAnimators.size()) {
                if (this.this$0.mListener != null) {
                    this.this$0.mListener.onAnimationEnd(null);
                }
                this.onEnd();
            }
        }

        @Override
        public void onAnimationStart(View view) {
            if (this.mProxyStarted) {
                return;
            }
            this.mProxyStarted = true;
            if (this.this$0.mListener != null) {
                this.this$0.mListener.onAnimationStart(null);
            }
        }

        void onEnd() {
            this.mProxyEndCount = 0;
            this.mProxyStarted = false;
            this.this$0.onAnimationsEnded();
        }
    };

    public ViewPropertyAnimatorCompatSet() {
        this.mAnimators = new ArrayList();
    }

    public void cancel() {
        if (!this.mIsStarted) {
            return;
        }
        Iterator<ViewPropertyAnimatorCompat> iterator2 = this.mAnimators.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().cancel();
        }
        this.mIsStarted = false;
    }

    void onAnimationsEnded() {
        this.mIsStarted = false;
    }

    public ViewPropertyAnimatorCompatSet play(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat) {
        if (!this.mIsStarted) {
            this.mAnimators.add(viewPropertyAnimatorCompat);
        }
        return this;
    }

    public ViewPropertyAnimatorCompatSet playSequentially(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, ViewPropertyAnimatorCompat viewPropertyAnimatorCompat2) {
        this.mAnimators.add(viewPropertyAnimatorCompat);
        viewPropertyAnimatorCompat2.setStartDelay(viewPropertyAnimatorCompat.getDuration());
        this.mAnimators.add(viewPropertyAnimatorCompat2);
        return this;
    }

    public ViewPropertyAnimatorCompatSet setDuration(long l) {
        if (!this.mIsStarted) {
            this.mDuration = l;
        }
        return this;
    }

    public ViewPropertyAnimatorCompatSet setInterpolator(Interpolator interpolator2) {
        if (!this.mIsStarted) {
            this.mInterpolator = interpolator2;
        }
        return this;
    }

    public ViewPropertyAnimatorCompatSet setListener(ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        if (!this.mIsStarted) {
            this.mListener = viewPropertyAnimatorListener;
        }
        return this;
    }

    public void start() {
        if (this.mIsStarted) {
            return;
        }
        for (ViewPropertyAnimatorCompat viewPropertyAnimatorCompat : this.mAnimators) {
            Interpolator interpolator2;
            long l = this.mDuration;
            if (l >= 0L) {
                viewPropertyAnimatorCompat.setDuration(l);
            }
            if ((interpolator2 = this.mInterpolator) != null) {
                viewPropertyAnimatorCompat.setInterpolator(interpolator2);
            }
            if (this.mListener != null) {
                viewPropertyAnimatorCompat.setListener(this.mProxyListener);
            }
            viewPropertyAnimatorCompat.start();
        }
        this.mIsStarted = true;
    }
}

