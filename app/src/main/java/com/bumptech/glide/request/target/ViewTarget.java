/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Point
 *  android.graphics.drawable.Drawable
 *  android.util.Log
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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import com.bumptech.glide.R;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.util.Preconditions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Deprecated
public abstract class ViewTarget<T extends View, Z>
extends BaseTarget<Z> {
    private static final String TAG = "ViewTarget";
    private static boolean isTagUsedAtLeastOnce;
    private static int tagId;
    private View.OnAttachStateChangeListener attachStateListener;
    private boolean isAttachStateListenerAdded;
    private boolean isClearedByUs;
    private final SizeDeterminer sizeDeterminer;
    protected final T view;

    static {
        tagId = R.id.glide_custom_view_target_tag;
    }

    public ViewTarget(T t) {
        this.view = (View)Preconditions.checkNotNull(t);
        this.sizeDeterminer = new SizeDeterminer((View)t);
    }

    @Deprecated
    public ViewTarget(T t, boolean bl) {
        this(t);
        if (bl) {
            this.waitForLayout();
        }
    }

    private Object getTag() {
        return this.view.getTag(tagId);
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
        isTagUsedAtLeastOnce = true;
        this.view.setTag(tagId, object);
    }

    @Deprecated
    public static void setTagId(int n) {
        if (!isTagUsedAtLeastOnce) {
            tagId = n;
            return;
        }
        throw new IllegalArgumentException("You cannot set the tag id more than once or change the tag id after the first request has been made");
    }

    public final ViewTarget<T, Z> clearOnDetach() {
        if (this.attachStateListener != null) {
            return this;
        }
        this.attachStateListener = new View.OnAttachStateChangeListener(this){
            final ViewTarget this$0;
            {
                this.this$0 = viewTarget;
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
    public Request getRequest() {
        Object object = this.getTag();
        Request request = null;
        if (object != null) {
            if (object instanceof Request) {
                request = (Request)object;
            } else {
                throw new IllegalArgumentException("You must not call setTag() on a view Glide is targeting");
            }
        }
        return request;
    }

    @Override
    public void getSize(SizeReadyCallback sizeReadyCallback) {
        this.sizeDeterminer.getSize(sizeReadyCallback);
    }

    public T getView() {
        return this.view;
    }

    @Override
    public void onLoadCleared(Drawable drawable2) {
        super.onLoadCleared(drawable2);
        this.sizeDeterminer.clearCallbacksAndListener();
        if (!this.isClearedByUs) {
            this.maybeRemoveAttachStateListener();
        }
    }

    @Override
    public void onLoadStarted(Drawable drawable2) {
        super.onLoadStarted(drawable2);
        this.maybeAddAttachStateListener();
    }

    void pauseMyRequest() {
        Request request = this.getRequest();
        if (request != null) {
            this.isClearedByUs = true;
            request.clear();
            this.isClearedByUs = false;
        }
    }

    @Override
    public void removeCallback(SizeReadyCallback sizeReadyCallback) {
        this.sizeDeterminer.removeCallback(sizeReadyCallback);
    }

    void resumeMyRequest() {
        Request request = this.getRequest();
        if (request != null && request.isCleared()) {
            request.begin();
        }
    }

    @Override
    public void setRequest(Request request) {
        this.setTag(request);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Target for: ");
        stringBuilder.append(this.view);
        return stringBuilder.toString();
    }

    public final ViewTarget<T, Z> waitForLayout() {
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
                context = Preconditions.checkNotNull((WindowManager)context.getSystemService("window")).getDefaultDisplay();
                Point point = new Point();
                context.getSize(point);
                maxDisplayLength = Math.max(point.x, point.y);
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
                if (Log.isLoggable((String)ViewTarget.TAG, (int)4)) {
                    Log.i((String)ViewTarget.TAG, (String)"Glide treats LayoutParams.WRAP_CONTENT as a request for an image the size of this device's screen dimensions. If you want to load the original image and are ok with the corresponding memory cost and OOMs (depending on the input size), use override(Target.SIZE_ORIGINAL). Otherwise, use LayoutParams.MATCH_PARENT, set layout_width and layout_height to fixed dimension, or use .override() with fixed dimensions.");
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

        void getSize(SizeReadyCallback object) {
            int n;
            int n2 = this.getTargetWidth();
            if (this.isViewStateAndSizeValid(n2, n = this.getTargetHeight())) {
                object.onSizeReady(n2, n);
                return;
            }
            if (!this.cbs.contains(object)) {
                this.cbs.add((SizeReadyCallback)object);
            }
            if (this.layoutListener == null) {
                ViewTreeObserver viewTreeObserver = this.view.getViewTreeObserver();
                this.layoutListener = object = new SizeDeterminerLayoutListener(this);
                viewTreeObserver.addOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)object);
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
                if (Log.isLoggable((String)ViewTarget.TAG, (int)2)) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("OnGlobalLayoutListener called attachStateListener=");
                    ((StringBuilder)object).append(this);
                    Log.v((String)ViewTarget.TAG, (String)((StringBuilder)object).toString());
                }
                if ((object = (SizeDeterminer)this.sizeDeterminerRef.get()) != null) {
                    ((SizeDeterminer)object).checkCurrentDimens();
                }
                return true;
            }
        }
    }
}

