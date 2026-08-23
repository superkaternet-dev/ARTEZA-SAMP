/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Point
 *  android.graphics.drawable.Drawable
 *  android.util.Log
 *  android.view.Display
 *  android.view.View
 *  android.view.View$OnAttachStateChangeListener
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnPreDrawListener
 *  android.view.WindowManager
 */
package com.bumptech.glide.request.target;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import com.bumptech.glide.R;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Preconditions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class CustomViewTarget<T extends View, Z>
implements Target<Z> {
    private static final String TAG = "CustomViewTarget";
    private static final int VIEW_TAG_ID = R.id.glide_custom_view_target_tag;
    private View.OnAttachStateChangeListener attachStateListener;
    private boolean isAttachStateListenerAdded;
    private boolean isClearedByUs;
    private final SizeDeterminer sizeDeterminer;
    protected final T view;

    public CustomViewTarget(T t) {
        this.view = (View)Preconditions.checkNotNull(t);
        this.sizeDeterminer = new SizeDeterminer((View)t);
    }

    private Object getTag() {
        return this.view.getTag(VIEW_TAG_ID);
    }

    private void maybeAddAttachStateListener() {
        View.OnAttachStateChangeListener onAttachStateChangeListener = this.attachStateListener;
        if (onAttachStateChangeListener != null && !this.isAttachStateListenerAdded) {
            this.view.addOnAttachStateChangeListener(onAttachStateChangeListener);
            this.isAttachStateListenerAdded = true;
            return;
        }
    }

    private void maybeRemoveAttachStateListener() {
        View.OnAttachStateChangeListener onAttachStateChangeListener = this.attachStateListener;
        if (onAttachStateChangeListener != null && this.isAttachStateListenerAdded) {
            this.view.removeOnAttachStateChangeListener(onAttachStateChangeListener);
            this.isAttachStateListenerAdded = false;
            return;
        }
    }

    private void setTag(Object object) {
        this.view.setTag(VIEW_TAG_ID, object);
    }

    public final CustomViewTarget<T, Z> clearOnDetach() {
        if (this.attachStateListener != null) {
            return this;
        }
        this.attachStateListener = new View.OnAttachStateChangeListener(this){
            final CustomViewTarget this$0;
            {
                this.this$0 = customViewTarget;
            }

            public void onViewAttachedToWindow(View view) {
                this.this$0.resumeMyRequest();
            }

            public void onViewDetachedFromWindow(View view) {
                this.this$0.pauseMyRequest();
            }
        };
        this.maybeAddAttachStateListener();
        return this;
    }

    @Override
    public final Request getRequest() {
        Object object = this.getTag();
        if (object != null) {
            if (object instanceof Request) {
                return (Request)object;
            }
            throw new IllegalArgumentException("You must not pass non-R.id ids to setTag(id)");
        }
        return null;
    }

    @Override
    public final void getSize(SizeReadyCallback sizeReadyCallback) {
        this.sizeDeterminer.getSize(sizeReadyCallback);
    }

    public final T getView() {
        return this.view;
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public final void onLoadCleared(Drawable drawable2) {
        this.sizeDeterminer.clearCallbacksAndListener();
        this.onResourceCleared(drawable2);
        if (!this.isClearedByUs) {
            this.maybeRemoveAttachStateListener();
        }
    }

    @Override
    public final void onLoadStarted(Drawable drawable2) {
        this.maybeAddAttachStateListener();
        this.onResourceLoading(drawable2);
    }

    protected abstract void onResourceCleared(Drawable var1);

    protected void onResourceLoading(Drawable drawable2) {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    final void pauseMyRequest() {
        Request request = this.getRequest();
        if (request != null) {
            this.isClearedByUs = true;
            request.clear();
            this.isClearedByUs = false;
        }
    }

    @Override
    public final void removeCallback(SizeReadyCallback sizeReadyCallback) {
        this.sizeDeterminer.removeCallback(sizeReadyCallback);
    }

    final void resumeMyRequest() {
        Request request = this.getRequest();
        if (request != null && request.isCleared()) {
            request.begin();
        }
    }

    @Override
    public final void setRequest(Request request) {
        this.setTag(request);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Target for: ");
        stringBuilder.append(this.view);
        return stringBuilder.toString();
    }

    @Deprecated
    public final CustomViewTarget<T, Z> useTagId(int n) {
        return this;
    }

    public final CustomViewTarget<T, Z> waitForLayout() {
        this.sizeDeterminer.waitForLayout = true;
        return this;
    }

    static final class SizeDeterminer {
        private static final int PENDING_SIZE = 0;
        static Integer maxDisplayLength;
        private final List<SizeReadyCallback> cbs = new ArrayList<SizeReadyCallback>();
        private SizeDeterminerLayoutListener layoutListener;
        private final View view;
        boolean waitForLayout;

        SizeDeterminer(View view) {
            this.view = view;
        }

        private static int getMaxDisplayLength(Context context) {
            if (maxDisplayLength == null) {
                Display display = Preconditions.checkNotNull((WindowManager)context.getSystemService("window")).getDefaultDisplay();
                context = new Point();
                display.getSize((Point)context);
                maxDisplayLength = Math.max(context.x, context.y);
            }
            return maxDisplayLength;
        }

        private int getTargetDimen(int n, int n2, int n3) {
            int n4 = n2 - n3;
            if (n4 > 0) {
                return n4;
            }
            if (this.waitForLayout && this.view.isLayoutRequested()) {
                return 0;
            }
            if ((n -= n3) > 0) {
                return n;
            }
            if (!this.view.isLayoutRequested() && n2 == -2) {
                if (Log.isLoggable((String)CustomViewTarget.TAG, (int)4)) {
                    Log.i((String)CustomViewTarget.TAG, (String)"Glide treats LayoutParams.WRAP_CONTENT as a request for an image the size of this device's screen dimensions. If you want to load the original image and are ok with the corresponding memory cost and OOMs (depending on the input size), use .override(Target.SIZE_ORIGINAL). Otherwise, use LayoutParams.MATCH_PARENT, set layout_width and layout_height to fixed dimension, or use .override() with fixed dimensions.");
                }
                return SizeDeterminer.getMaxDisplayLength(this.view.getContext());
            }
            return 0;
        }

        private int getTargetHeight() {
            int n = this.view.getPaddingTop();
            int n2 = this.view.getPaddingBottom();
            ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
            int n3 = layoutParams != null ? layoutParams.height : 0;
            return this.getTargetDimen(this.view.getHeight(), n3, n + n2);
        }

        private int getTargetWidth() {
            int n = this.view.getPaddingLeft();
            int n2 = this.view.getPaddingRight();
            ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
            int n3 = layoutParams != null ? layoutParams.width : 0;
            return this.getTargetDimen(this.view.getWidth(), n3, n + n2);
        }

        private boolean isDimensionValid(int n) {
            boolean bl = n > 0 || n == Integer.MIN_VALUE;
            return bl;
        }

        private boolean isViewStateAndSizeValid(int n, int n2) {
            boolean bl = this.isDimensionValid(n) && this.isDimensionValid(n2);
            return bl;
        }

        private void notifyCbs(int n, int n2) {
            Iterator<SizeReadyCallback> iterator2 = new ArrayList<SizeReadyCallback>(this.cbs).iterator();
            while (iterator2.hasNext()) {
                iterator2.next().onSizeReady(n, n2);
            }
        }

        void checkCurrentDimens() {
            int n;
            if (this.cbs.isEmpty()) {
                return;
            }
            int n2 = this.getTargetWidth();
            if (!this.isViewStateAndSizeValid(n2, n = this.getTargetHeight())) {
                return;
            }
            this.notifyCbs(n2, n);
            this.clearCallbacksAndListener();
        }

        void clearCallbacksAndListener() {
            ViewTreeObserver viewTreeObserver = this.view.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)this.layoutListener);
            }
            this.layoutListener = null;
            this.cbs.clear();
        }

        void getSize(SizeReadyCallback sizeReadyCallback) {
            int n;
            int n2 = this.getTargetWidth();
            if (this.isViewStateAndSizeValid(n2, n = this.getTargetHeight())) {
                sizeReadyCallback.onSizeReady(n2, n);
                return;
            }
            if (!this.cbs.contains(sizeReadyCallback)) {
                this.cbs.add(sizeReadyCallback);
            }
            if (this.layoutListener == null) {
                SizeDeterminerLayoutListener sizeDeterminerLayoutListener;
                sizeReadyCallback = this.view.getViewTreeObserver();
                this.layoutListener = sizeDeterminerLayoutListener = new SizeDeterminerLayoutListener(this);
                sizeReadyCallback.addOnPreDrawListener(sizeDeterminerLayoutListener);
            }
        }

        void removeCallback(SizeReadyCallback sizeReadyCallback) {
            this.cbs.remove(sizeReadyCallback);
        }

        private static final class SizeDeterminerLayoutListener
        implements ViewTreeObserver.OnPreDrawListener {
            private final WeakReference<SizeDeterminer> sizeDeterminerRef;

            SizeDeterminerLayoutListener(SizeDeterminer sizeDeterminer) {
                this.sizeDeterminerRef = new WeakReference<SizeDeterminer>(sizeDeterminer);
            }

            public boolean onPreDraw() {
                Object object;
                if (Log.isLoggable((String)CustomViewTarget.TAG, (int)2)) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("OnGlobalLayoutListener called attachStateListener=");
                    ((StringBuilder)object).append(this);
                    Log.v((String)CustomViewTarget.TAG, (String)((StringBuilder)object).toString());
                }
                if ((object = (SizeDeterminer)this.sizeDeterminerRef.get()) != null) {
                    ((SizeDeterminer)object).checkCurrentDimens();
                }
                return true;
            }
        }
    }
}

