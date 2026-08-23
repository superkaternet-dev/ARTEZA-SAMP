/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.DisplayMetrics
 *  android.view.View
 *  android.view.animation.DecelerateInterpolator
 *  android.view.animation.Interpolator
 *  android.widget.Scroller
 */
package androidx.recyclerview.widget;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SnapHelper
extends RecyclerView.OnFlingListener {
    static final float MILLISECONDS_PER_INCH = 100.0f;
    private Scroller mGravityScroller;
    RecyclerView mRecyclerView;
    private final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener(this){
        boolean mScrolled;
        final SnapHelper this$0;
        {
            this.this$0 = snapHelper;
            this.mScrolled = false;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int n) {
            super.onScrollStateChanged(recyclerView, n);
            if (n == 0 && this.mScrolled) {
                this.mScrolled = false;
                this.this$0.snapToTargetExistingView();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int n, int n2) {
            if (n != 0 || n2 != 0) {
                this.mScrolled = true;
            }
        }
    };

    private void destroyCallbacks() {
        this.mRecyclerView.removeOnScrollListener(this.mScrollListener);
        this.mRecyclerView.setOnFlingListener(null);
    }

    private void setupCallbacks() throws IllegalStateException {
        if (this.mRecyclerView.getOnFlingListener() == null) {
            this.mRecyclerView.addOnScrollListener(this.mScrollListener);
            this.mRecyclerView.setOnFlingListener(this);
            return;
        }
        throw new IllegalStateException("An instance of OnFlingListener already set.");
    }

    private boolean snapFromFling(RecyclerView.LayoutManager layoutManager, int n, int n2) {
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return false;
        }
        RecyclerView.SmoothScroller smoothScroller = this.createScroller(layoutManager);
        if (smoothScroller == null) {
            return false;
        }
        if ((n = this.findTargetSnapPosition(layoutManager, n, n2)) == -1) {
            return false;
        }
        smoothScroller.setTargetPosition(n);
        layoutManager.startSmoothScroll(smoothScroller);
        return true;
    }

    public void attachToRecyclerView(RecyclerView recyclerView) throws IllegalStateException {
        RecyclerView recyclerView2 = this.mRecyclerView;
        if (recyclerView2 == recyclerView) {
            return;
        }
        if (recyclerView2 != null) {
            this.destroyCallbacks();
        }
        this.mRecyclerView = recyclerView;
        if (recyclerView != null) {
            this.setupCallbacks();
            this.mGravityScroller = new Scroller(this.mRecyclerView.getContext(), (Interpolator)new DecelerateInterpolator());
            this.snapToTargetExistingView();
        }
    }

    public abstract int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager var1, View var2);

    public int[] calculateScrollDistance(int n, int n2) {
        this.mGravityScroller.fling(0, 0, n, n2, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new int[]{this.mGravityScroller.getFinalX(), this.mGravityScroller.getFinalY()};
    }

    protected RecyclerView.SmoothScroller createScroller(RecyclerView.LayoutManager layoutManager) {
        return this.createSnapScroller(layoutManager);
    }

    @Deprecated
    protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager layoutManager) {
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return null;
        }
        return new LinearSmoothScroller(this, this.mRecyclerView.getContext()){
            final SnapHelper this$0;
            {
                this.this$0 = snapHelper;
                super(context);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 100.0f / (float)displayMetrics.densityDpi;
            }

            @Override
            protected void onTargetFound(View object, RecyclerView.State object2, RecyclerView.SmoothScroller.Action action) {
                if (this.this$0.mRecyclerView == null) {
                    return;
                }
                object2 = this.this$0;
                object = ((SnapHelper)object2).calculateDistanceToFinalSnap(((SnapHelper)object2).mRecyclerView.getLayoutManager(), (View)object);
                View view = object[0];
                View view2 = object[1];
                int n = this.calculateTimeForDeceleration(Math.max(Math.abs((int)view), Math.abs((int)view2)));
                if (n > 0) {
                    action.update((int)view, (int)view2, n, (Interpolator)this.mDecelerateInterpolator);
                }
            }
        };
    }

    public abstract View findSnapView(RecyclerView.LayoutManager var1);

    public abstract int findTargetSnapPosition(RecyclerView.LayoutManager var1, int var2, int var3);

    @Override
    public boolean onFling(int n, int n2) {
        boolean bl;
        block2: {
            RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
            bl = false;
            if (layoutManager == null) {
                return false;
            }
            if (this.mRecyclerView.getAdapter() == null) {
                return false;
            }
            int n3 = this.mRecyclerView.getMinFlingVelocity();
            if (Math.abs(n2) <= n3 && Math.abs(n) <= n3 || !this.snapFromFling(layoutManager, n, n2)) break block2;
            bl = true;
        }
        return bl;
    }

    void snapToTargetExistingView() {
        Object object = this.mRecyclerView;
        if (object == null) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = ((RecyclerView)object).getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        object = this.findSnapView(layoutManager);
        if (object == null) {
            return;
        }
        if ((object = (Object)this.calculateDistanceToFinalSnap(layoutManager, (View)object))[0] != false || object[1] != false) {
            this.mRecyclerView.smoothScrollBy((int)object[0], (int)object[1]);
        }
    }
}

