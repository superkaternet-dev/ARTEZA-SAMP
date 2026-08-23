/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.Matrix
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewParent
 *  android.view.ViewTreeObserver$OnPreDrawListener
 *  android.widget.FrameLayout
 */
package androidx.transition;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import androidx.transition.GhostViewImpl;
import androidx.transition.R;

class GhostViewApi14
extends View
implements GhostViewImpl {
    Matrix mCurrentMatrix;
    private int mDeltaX;
    private int mDeltaY;
    private final Matrix mMatrix = new Matrix();
    private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener(this){
        final GhostViewApi14 this$0;
        {
            this.this$0 = ghostViewApi14;
        }

        public boolean onPreDraw() {
            GhostViewApi14 ghostViewApi14 = this.this$0;
            ghostViewApi14.mCurrentMatrix = ghostViewApi14.mView.getMatrix();
            ViewCompat.postInvalidateOnAnimation(this.this$0);
            if (this.this$0.mStartParent != null && this.this$0.mStartView != null) {
                this.this$0.mStartParent.endViewTransition(this.this$0.mStartView);
                ViewCompat.postInvalidateOnAnimation((View)this.this$0.mStartParent);
                this.this$0.mStartParent = null;
                this.this$0.mStartView = null;
            }
            return true;
        }
    };
    int mReferences;
    ViewGroup mStartParent;
    View mStartView;
    final View mView;

    GhostViewApi14(View view) {
        super(view.getContext());
        this.mView = view;
        this.setLayerType(2, null);
    }

    static GhostViewImpl addGhost(View view, ViewGroup viewGroup) {
        GhostViewApi14 ghostViewApi14;
        GhostViewApi14 ghostViewApi142 = ghostViewApi14 = GhostViewApi14.getGhostView(view);
        if (ghostViewApi14 == null) {
            if ((viewGroup = GhostViewApi14.findFrameLayout(viewGroup)) == null) {
                return null;
            }
            ghostViewApi142 = new GhostViewApi14(view);
            viewGroup.addView((View)ghostViewApi142);
        }
        ++ghostViewApi142.mReferences;
        return ghostViewApi142;
    }

    private static FrameLayout findFrameLayout(ViewGroup viewGroup) {
        while (!(viewGroup instanceof FrameLayout)) {
            if ((viewGroup = viewGroup.getParent()) instanceof ViewGroup) continue;
            return null;
        }
        return (FrameLayout)viewGroup;
    }

    static GhostViewApi14 getGhostView(View view) {
        return (GhostViewApi14)view.getTag(R.id.ghost_view);
    }

    static void removeGhost(View view) {
        if ((view = GhostViewApi14.getGhostView(view)) != null) {
            ViewParent viewParent;
            int n;
            view.mReferences = n = view.mReferences - 1;
            if (n <= 0 && (viewParent = view.getParent()) instanceof ViewGroup) {
                viewParent = (ViewGroup)viewParent;
                viewParent.endViewTransition(view);
                viewParent.removeView(view);
            }
        }
    }

    private static void setGhostView(View view, GhostViewApi14 ghostViewApi14) {
        view.setTag(R.id.ghost_view, (Object)ghostViewApi14);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        GhostViewApi14.setGhostView(this.mView, this);
        int[] nArray = new int[2];
        int[] nArray2 = new int[2];
        this.getLocationOnScreen(nArray);
        this.mView.getLocationOnScreen(nArray2);
        nArray2[0] = (int)((float)nArray2[0] - this.mView.getTranslationX());
        nArray2[1] = (int)((float)nArray2[1] - this.mView.getTranslationY());
        this.mDeltaX = nArray2[0] - nArray[0];
        this.mDeltaY = nArray2[1] - nArray[1];
        this.mView.getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
        this.mView.setVisibility(4);
    }

    protected void onDetachedFromWindow() {
        this.mView.getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
        this.mView.setVisibility(0);
        GhostViewApi14.setGhostView(this.mView, null);
        super.onDetachedFromWindow();
    }

    protected void onDraw(Canvas canvas) {
        this.mMatrix.set(this.mCurrentMatrix);
        this.mMatrix.postTranslate((float)this.mDeltaX, (float)this.mDeltaY);
        canvas.setMatrix(this.mMatrix);
        this.mView.draw(canvas);
    }

    @Override
    public void reserveEndViewTransition(ViewGroup viewGroup, View view) {
        this.mStartParent = viewGroup;
        this.mStartView = view;
    }

    @Override
    public void setVisibility(int n) {
        super.setVisibility(n);
        View view = this.mView;
        n = n == 0 ? 4 : 0;
        view.setVisibility(n);
    }
}

