/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewParent
 */
package androidx.transition;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.core.view.ViewCompat;
import androidx.transition.ViewGroupOverlayApi14;
import androidx.transition.ViewOverlayImpl;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

class ViewOverlayApi14
implements ViewOverlayImpl {
    protected OverlayViewGroup mOverlayViewGroup;

    private ViewOverlayApi14() {
    }

    ViewOverlayApi14(Context context, ViewGroup viewGroup, View view) {
        this.mOverlayViewGroup = new OverlayViewGroup(context, viewGroup, view, this);
    }

    static ViewOverlayApi14 createFrom(View view) {
        ViewGroup viewGroup = ViewOverlayApi14.getContentView(view);
        if (viewGroup != null) {
            int n = viewGroup.getChildCount();
            for (int i = 0; i < n; ++i) {
                View view2 = viewGroup.getChildAt(i);
                if (!(view2 instanceof OverlayViewGroup)) continue;
                return ((OverlayViewGroup)view2).mViewOverlay;
            }
            return new ViewGroupOverlayApi14(viewGroup.getContext(), viewGroup, view);
        }
        return null;
    }

    static ViewGroup getContentView(View view) {
        while (view != null) {
            if (view.getId() == 0x1020002 && view instanceof ViewGroup) {
                return (ViewGroup)view;
            }
            if (!(view.getParent() instanceof ViewGroup)) continue;
            view = (ViewGroup)view.getParent();
        }
        return null;
    }

    @Override
    public void add(Drawable drawable2) {
        this.mOverlayViewGroup.add(drawable2);
    }

    @Override
    public void clear() {
        this.mOverlayViewGroup.clear();
    }

    ViewGroup getOverlayView() {
        return this.mOverlayViewGroup;
    }

    boolean isEmpty() {
        return this.mOverlayViewGroup.isEmpty();
    }

    @Override
    public void remove(Drawable drawable2) {
        this.mOverlayViewGroup.remove(drawable2);
    }

    static class OverlayViewGroup
    extends ViewGroup {
        static Method sInvalidateChildInParentFastMethod;
        ArrayList<Drawable> mDrawables = null;
        ViewGroup mHostView;
        View mRequestingView;
        ViewOverlayApi14 mViewOverlay;

        static {
            try {
                sInvalidateChildInParentFastMethod = ViewGroup.class.getDeclaredMethod("invalidateChildInParentFast", Integer.TYPE, Integer.TYPE, Rect.class);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
        }

        OverlayViewGroup(Context context, ViewGroup viewGroup, View view, ViewOverlayApi14 viewOverlayApi14) {
            super(context);
            this.mHostView = viewGroup;
            this.mRequestingView = view;
            this.setRight(viewGroup.getWidth());
            this.setBottom(viewGroup.getHeight());
            viewGroup.addView((View)this);
            this.mViewOverlay = viewOverlayApi14;
        }

        private void getOffset(int[] nArray) {
            int[] nArray2 = new int[2];
            int[] nArray3 = new int[2];
            this.mHostView.getLocationOnScreen(nArray2);
            this.mRequestingView.getLocationOnScreen(nArray3);
            nArray[0] = nArray3[0] - nArray2[0];
            nArray[1] = nArray3[1] - nArray2[1];
        }

        public void add(Drawable drawable2) {
            if (this.mDrawables == null) {
                this.mDrawables = new ArrayList();
            }
            if (!this.mDrawables.contains(drawable2)) {
                this.mDrawables.add(drawable2);
                this.invalidate(drawable2.getBounds());
                drawable2.setCallback((Drawable.Callback)this);
            }
        }

        public void add(View view) {
            if (view.getParent() instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup)view.getParent();
                if (viewGroup != this.mHostView && viewGroup.getParent() != null && ViewCompat.isAttachedToWindow((View)viewGroup)) {
                    int[] nArray = new int[2];
                    int[] nArray2 = new int[2];
                    viewGroup.getLocationOnScreen(nArray);
                    this.mHostView.getLocationOnScreen(nArray2);
                    ViewCompat.offsetLeftAndRight(view, nArray[0] - nArray2[0]);
                    ViewCompat.offsetTopAndBottom(view, nArray[1] - nArray2[1]);
                }
                viewGroup.removeView(view);
                if (view.getParent() != null) {
                    viewGroup.removeView(view);
                }
            }
            super.addView(view, this.getChildCount() - 1);
        }

        public void clear() {
            this.removeAllViews();
            ArrayList<Drawable> arrayList = this.mDrawables;
            if (arrayList != null) {
                arrayList.clear();
            }
        }

        protected void dispatchDraw(Canvas canvas) {
            int[] nArray = new int[2];
            Object object = new int[2];
            this.mHostView.getLocationOnScreen(nArray);
            this.mRequestingView.getLocationOnScreen((int[])object);
            int n = 0;
            canvas.translate((float)(object[0] - nArray[0]), (float)(object[1] - nArray[1]));
            canvas.clipRect(new Rect(0, 0, this.mRequestingView.getWidth(), this.mRequestingView.getHeight()));
            super.dispatchDraw(canvas);
            object = this.mDrawables;
            if (object != null) {
                n = ((ArrayList)object).size();
            }
            for (int i = 0; i < n; ++i) {
                this.mDrawables.get(i).draw(canvas);
            }
        }

        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        public void invalidateChildFast(View object, Rect rect) {
            if (this.mHostView != null) {
                int n = object.getLeft();
                int n2 = object.getTop();
                object = new int[2];
                this.getOffset((int[])object);
                rect.offset((int)(object[0] + n), (int)(object[1] + n2));
                this.mHostView.invalidate(rect);
            }
        }

        public ViewParent invalidateChildInParent(int[] nArray, Rect rect) {
            if (this.mHostView != null) {
                rect.offset(nArray[0], nArray[1]);
                if (this.mHostView instanceof ViewGroup) {
                    nArray[0] = 0;
                    nArray[1] = 0;
                    int[] nArray2 = new int[2];
                    this.getOffset(nArray2);
                    rect.offset(nArray2[0], nArray2[1]);
                    return super.invalidateChildInParent(nArray, rect);
                }
                this.invalidate(rect);
            }
            return null;
        }

        protected ViewParent invalidateChildInParentFast(int n, int n2, Rect rect) {
            if (this.mHostView instanceof ViewGroup && sInvalidateChildInParentFastMethod != null) {
                try {
                    this.getOffset(new int[2]);
                    sInvalidateChildInParentFastMethod.invoke((Object)this.mHostView, n, n2, rect);
                }
                catch (InvocationTargetException invocationTargetException) {
                    invocationTargetException.printStackTrace();
                }
                catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                }
            }
            return null;
        }

        public void invalidateDrawable(Drawable drawable2) {
            this.invalidate(drawable2.getBounds());
        }

        boolean isEmpty() {
            ArrayList<Drawable> arrayList;
            boolean bl = this.getChildCount() == 0 && ((arrayList = this.mDrawables) == null || arrayList.size() == 0);
            return bl;
        }

        protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        }

        public void remove(Drawable drawable2) {
            ArrayList<Drawable> arrayList = this.mDrawables;
            if (arrayList != null) {
                arrayList.remove(drawable2);
                this.invalidate(drawable2.getBounds());
                drawable2.setCallback(null);
            }
        }

        public void remove(View view) {
            super.removeView(view);
            if (this.isEmpty()) {
                this.mHostView.removeView((View)this);
            }
        }

        protected boolean verifyDrawable(Drawable drawable2) {
            ArrayList<Drawable> arrayList;
            boolean bl = super.verifyDrawable(drawable2) || (arrayList = this.mDrawables) != null && arrayList.contains(drawable2);
            return bl;
        }

        static class TouchInterceptor
        extends View {
            TouchInterceptor(Context context) {
                super(context);
            }
        }
    }
}

