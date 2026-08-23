/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.AnimatorSet
 *  android.animation.ObjectAnimator
 *  android.animation.PropertyValuesHolder
 *  android.animation.TypeEvaluator
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.content.res.XmlResourceParser
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Canvas
 *  android.graphics.Path
 *  android.graphics.PointF
 *  android.graphics.Rect
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.util.Property
 *  android.view.View
 *  android.view.ViewGroup
 *  org.xmlpull.v1.XmlPullParser
 */
package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import androidx.transition.ObjectAnimatorUtils;
import androidx.transition.PathMotion;
import androidx.transition.PropertyValuesHolderUtils;
import androidx.transition.RectEvaluator;
import androidx.transition.Styleable;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import androidx.transition.TransitionUtils;
import androidx.transition.TransitionValues;
import androidx.transition.ViewGroupUtils;
import androidx.transition.ViewUtils;
import org.xmlpull.v1.XmlPullParser;

public class ChangeBounds
extends Transition {
    private static final Property<View, PointF> BOTTOM_RIGHT_ONLY_PROPERTY;
    private static final Property<ViewBounds, PointF> BOTTOM_RIGHT_PROPERTY;
    private static final Property<Drawable, PointF> DRAWABLE_ORIGIN_PROPERTY;
    private static final Property<View, PointF> POSITION_PROPERTY;
    private static final String PROPNAME_BOUNDS = "android:changeBounds:bounds";
    private static final String PROPNAME_CLIP = "android:changeBounds:clip";
    private static final String PROPNAME_PARENT = "android:changeBounds:parent";
    private static final String PROPNAME_WINDOW_X = "android:changeBounds:windowX";
    private static final String PROPNAME_WINDOW_Y = "android:changeBounds:windowY";
    private static final Property<View, PointF> TOP_LEFT_ONLY_PROPERTY;
    private static final Property<ViewBounds, PointF> TOP_LEFT_PROPERTY;
    private static RectEvaluator sRectEvaluator;
    private static final String[] sTransitionProperties;
    private boolean mReparent = false;
    private boolean mResizeClip = false;
    private int[] mTempLocation = new int[2];

    static {
        sTransitionProperties = new String[]{PROPNAME_BOUNDS, PROPNAME_CLIP, PROPNAME_PARENT, PROPNAME_WINDOW_X, PROPNAME_WINDOW_Y};
        DRAWABLE_ORIGIN_PROPERTY = new Property<Drawable, PointF>(PointF.class, "boundsOrigin"){
            private Rect mBounds = new Rect();

            public PointF get(Drawable drawable2) {
                drawable2.copyBounds(this.mBounds);
                return new PointF((float)this.mBounds.left, (float)this.mBounds.top);
            }

            public void set(Drawable drawable2, PointF pointF) {
                drawable2.copyBounds(this.mBounds);
                this.mBounds.offsetTo(Math.round(pointF.x), Math.round(pointF.y));
                drawable2.setBounds(this.mBounds);
            }
        };
        TOP_LEFT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class, "topLeft"){

            public PointF get(ViewBounds viewBounds) {
                return null;
            }

            public void set(ViewBounds viewBounds, PointF pointF) {
                viewBounds.setTopLeft(pointF);
            }
        };
        BOTTOM_RIGHT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class, "bottomRight"){

            public PointF get(ViewBounds viewBounds) {
                return null;
            }

            public void set(ViewBounds viewBounds, PointF pointF) {
                viewBounds.setBottomRight(pointF);
            }
        };
        BOTTOM_RIGHT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class, "bottomRight"){

            public PointF get(View view) {
                return null;
            }

            public void set(View view, PointF pointF) {
                ViewUtils.setLeftTopRightBottom(view, view.getLeft(), view.getTop(), Math.round(pointF.x), Math.round(pointF.y));
            }
        };
        TOP_LEFT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class, "topLeft"){

            public PointF get(View view) {
                return null;
            }

            public void set(View view, PointF pointF) {
                ViewUtils.setLeftTopRightBottom(view, Math.round(pointF.x), Math.round(pointF.y), view.getRight(), view.getBottom());
            }
        };
        POSITION_PROPERTY = new Property<View, PointF>(PointF.class, "position"){

            public PointF get(View view) {
                return null;
            }

            public void set(View view, PointF pointF) {
                int n = Math.round(pointF.x);
                int n2 = Math.round(pointF.y);
                ViewUtils.setLeftTopRightBottom(view, n, n2, view.getWidth() + n, view.getHeight() + n2);
            }
        };
        sRectEvaluator = new RectEvaluator();
    }

    public ChangeBounds() {
    }

    public ChangeBounds(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        context = context.obtainStyledAttributes(attributeSet, Styleable.CHANGE_BOUNDS);
        boolean bl = TypedArrayUtils.getNamedBoolean((TypedArray)context, (XmlPullParser)((XmlResourceParser)attributeSet), "resizeClip", 0, false);
        context.recycle();
        this.setResizeClip(bl);
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        if (ViewCompat.isLaidOut(view) || view.getWidth() != 0 || view.getHeight() != 0) {
            transitionValues.values.put(PROPNAME_BOUNDS, new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
            transitionValues.values.put(PROPNAME_PARENT, transitionValues.view.getParent());
            if (this.mReparent) {
                transitionValues.view.getLocationInWindow(this.mTempLocation);
                transitionValues.values.put(PROPNAME_WINDOW_X, this.mTempLocation[0]);
                transitionValues.values.put(PROPNAME_WINDOW_Y, this.mTempLocation[1]);
            }
            if (this.mResizeClip) {
                transitionValues.values.put(PROPNAME_CLIP, ViewCompat.getClipBounds(view));
            }
        }
    }

    private boolean parentMatches(View view, View view2) {
        boolean bl = true;
        if (this.mReparent) {
            boolean bl2 = true;
            bl = true;
            TransitionValues transitionValues = this.getMatchedTransitionValues(view, true);
            if (transitionValues == null) {
                if (view != view2) {
                    bl = false;
                }
            } else {
                bl = view2 == transitionValues.view ? bl2 : false;
            }
        }
        return bl;
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }

    @Override
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues object, TransitionValues object2) {
        block28: {
            block29: {
                int n;
                int n2;
                int n3;
                int n4;
                View view;
                Object object3;
                block37: {
                    block36: {
                        block30: {
                            int n5;
                            int n6;
                            int n7;
                            int n8;
                            int n9;
                            int n10;
                            int n11;
                            int n12;
                            int n13;
                            int n14;
                            int n15;
                            int n16;
                            int n17;
                            int n18;
                            ViewGroup viewGroup2;
                            block35: {
                                block34: {
                                    block32: {
                                        block33: {
                                            block31: {
                                                if (object == null || object2 == null) break block28;
                                                object3 = ((TransitionValues)object).values;
                                                viewGroup2 = ((TransitionValues)object2).values;
                                                object3 = (ViewGroup)object3.get(PROPNAME_PARENT);
                                                viewGroup2 = (ViewGroup)viewGroup2.get(PROPNAME_PARENT);
                                                if (object3 == null || viewGroup2 == null) break block29;
                                                view = ((TransitionValues)object2).view;
                                                if (!this.parentMatches((View)object3, (View)viewGroup2)) break block30;
                                                viewGroup = (Rect)((TransitionValues)object).values.get(PROPNAME_BOUNDS);
                                                object3 = (Rect)((TransitionValues)object2).values.get(PROPNAME_BOUNDS);
                                                n18 = viewGroup.left;
                                                n17 = object3.left;
                                                n16 = viewGroup.top;
                                                n15 = object3.top;
                                                n14 = viewGroup.right;
                                                n13 = object3.right;
                                                n12 = viewGroup.bottom;
                                                n11 = object3.bottom;
                                                n10 = n14 - n18;
                                                n9 = n12 - n16;
                                                n8 = n13 - n17;
                                                n7 = n11 - n15;
                                                object = (Rect)((TransitionValues)object).values.get(PROPNAME_CLIP);
                                                object3 = (Rect)((TransitionValues)object2).values.get(PROPNAME_CLIP);
                                                int n19 = 0;
                                                n6 = 0;
                                                if (n10 != 0 && n9 != 0) break block31;
                                                n5 = n19;
                                                if (n8 == 0) break block32;
                                                n5 = n19;
                                                if (n7 == 0) break block32;
                                            }
                                            if (n18 != n17 || n16 != n15) {
                                                n6 = 0 + 1;
                                            }
                                            if (n14 != n13) break block33;
                                            n5 = n6;
                                            if (n12 == n11) break block32;
                                        }
                                        n5 = n6 + 1;
                                    }
                                    if (object != null && !object.equals(object3)) break block34;
                                    n6 = n5;
                                    if (object != null) break block35;
                                    n6 = n5;
                                    if (object3 == null) break block35;
                                }
                                n6 = n5 + 1;
                            }
                            if (n6 > 0) {
                                if (!this.mResizeClip) {
                                    ViewUtils.setLeftTopRightBottom(view, n18, n16, n14, n12);
                                    if (n6 == 2) {
                                        if (n10 == n8 && n9 == n7) {
                                            viewGroup = this.getPathMotion().getPath(n18, n16, n17, n15);
                                            viewGroup = ObjectAnimatorUtils.ofPointF(view, POSITION_PROPERTY, (Path)viewGroup);
                                        } else {
                                            object = new ViewBounds(view);
                                            viewGroup = this.getPathMotion().getPath(n18, n16, n17, n15);
                                            object2 = ObjectAnimatorUtils.ofPointF(object, TOP_LEFT_PROPERTY, (Path)viewGroup);
                                            viewGroup = this.getPathMotion().getPath(n14, n12, n13, n11);
                                            object3 = ObjectAnimatorUtils.ofPointF(object, BOTTOM_RIGHT_PROPERTY, (Path)viewGroup);
                                            viewGroup = new AnimatorSet();
                                            viewGroup.playTogether(new Animator[]{object2, object3});
                                            viewGroup.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, (ViewBounds)object){
                                                private ViewBounds mViewBounds;
                                                final ChangeBounds this$0;
                                                final ViewBounds val$viewBounds;
                                                {
                                                    this.this$0 = changeBounds;
                                                    this.val$viewBounds = viewBounds;
                                                    this.mViewBounds = viewBounds;
                                                }
                                            });
                                        }
                                    } else if (n18 == n17 && n16 == n15) {
                                        viewGroup = this.getPathMotion().getPath(n14, n12, n13, n11);
                                        viewGroup = ObjectAnimatorUtils.ofPointF(view, BOTTOM_RIGHT_ONLY_PROPERTY, (Path)viewGroup);
                                    } else {
                                        viewGroup = this.getPathMotion().getPath(n18, n16, n17, n15);
                                        viewGroup = ObjectAnimatorUtils.ofPointF(view, TOP_LEFT_ONLY_PROPERTY, (Path)viewGroup);
                                    }
                                } else {
                                    n5 = Math.max(n10, n8);
                                    ViewUtils.setLeftTopRightBottom(view, n18, n16, n18 + n5, n16 + Math.max(n9, n7));
                                    if (n18 == n17 && n16 == n15) {
                                        viewGroup = null;
                                    } else {
                                        viewGroup = this.getPathMotion().getPath(n18, n16, n17, n15);
                                        viewGroup = ObjectAnimatorUtils.ofPointF(view, POSITION_PROPERTY, (Path)viewGroup);
                                    }
                                    if (object == null) {
                                        object = new Rect(0, 0, n10, n9);
                                    }
                                    object2 = object3 == null ? new Rect(0, 0, n8, n7) : object3;
                                    viewGroup2 = null;
                                    if (!object.equals(object2)) {
                                        ViewCompat.setClipBounds(view, (Rect)object);
                                        object = ObjectAnimator.ofObject((Object)view, (String)"clipBounds", (TypeEvaluator)sRectEvaluator, (Object[])new Object[]{object, object2});
                                        object.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, view, (Rect)object3, n17, n15, n13, n11){
                                            private boolean mIsCanceled;
                                            final ChangeBounds this$0;
                                            final int val$endBottom;
                                            final int val$endLeft;
                                            final int val$endRight;
                                            final int val$endTop;
                                            final Rect val$finalClip;
                                            final View val$view;
                                            {
                                                this.this$0 = changeBounds;
                                                this.val$view = view;
                                                this.val$finalClip = rect;
                                                this.val$endLeft = n;
                                                this.val$endTop = n2;
                                                this.val$endRight = n3;
                                                this.val$endBottom = n4;
                                            }

                                            public void onAnimationCancel(Animator animator2) {
                                                this.mIsCanceled = true;
                                            }

                                            public void onAnimationEnd(Animator animator2) {
                                                if (!this.mIsCanceled) {
                                                    ViewCompat.setClipBounds(this.val$view, this.val$finalClip);
                                                    ViewUtils.setLeftTopRightBottom(this.val$view, this.val$endLeft, this.val$endTop, this.val$endRight, this.val$endBottom);
                                                }
                                            }
                                        });
                                    } else {
                                        object = viewGroup2;
                                    }
                                    viewGroup = TransitionUtils.mergeAnimators((Animator)viewGroup, (Animator)object);
                                }
                                if (view.getParent() instanceof ViewGroup) {
                                    object = (ViewGroup)view.getParent();
                                    ViewGroupUtils.suppressLayout((ViewGroup)object, true);
                                    this.addListener(new TransitionListenerAdapter(this, (ViewGroup)object){
                                        boolean mCanceled;
                                        final ChangeBounds this$0;
                                        final ViewGroup val$parent;
                                        {
                                            this.this$0 = changeBounds;
                                            this.val$parent = viewGroup;
                                            this.mCanceled = false;
                                        }

                                        @Override
                                        public void onTransitionCancel(Transition transition) {
                                            ViewGroupUtils.suppressLayout(this.val$parent, false);
                                            this.mCanceled = true;
                                        }

                                        @Override
                                        public void onTransitionEnd(Transition transition) {
                                            if (!this.mCanceled) {
                                                ViewGroupUtils.suppressLayout(this.val$parent, false);
                                            }
                                            transition.removeListener(this);
                                        }

                                        @Override
                                        public void onTransitionPause(Transition transition) {
                                            ViewGroupUtils.suppressLayout(this.val$parent, false);
                                        }

                                        @Override
                                        public void onTransitionResume(Transition transition) {
                                            ViewGroupUtils.suppressLayout(this.val$parent, true);
                                        }
                                    });
                                }
                                return viewGroup;
                            }
                            break block36;
                        }
                        n4 = (Integer)((TransitionValues)object).values.get(PROPNAME_WINDOW_X);
                        n3 = (Integer)((TransitionValues)object).values.get(PROPNAME_WINDOW_Y);
                        n2 = (Integer)((TransitionValues)object2).values.get(PROPNAME_WINDOW_X);
                        n = (Integer)((TransitionValues)object2).values.get(PROPNAME_WINDOW_Y);
                        if (n4 != n2 || n3 != n) break block37;
                    }
                    return null;
                }
                viewGroup.getLocationInWindow(this.mTempLocation);
                object = Bitmap.createBitmap((int)view.getWidth(), (int)view.getHeight(), (Bitmap.Config)Bitmap.Config.ARGB_8888);
                view.draw(new Canvas((Bitmap)object));
                object = new BitmapDrawable((Bitmap)object);
                float f = ViewUtils.getTransitionAlpha(view);
                ViewUtils.setTransitionAlpha(view, 0.0f);
                ViewUtils.getOverlay((View)viewGroup).add((Drawable)object);
                object2 = this.getPathMotion();
                object3 = this.mTempLocation;
                object2 = ((PathMotion)object2).getPath(n4 - object3[0], n3 - object3[1], n2 - object3[0], n - object3[1]);
                object2 = ObjectAnimator.ofPropertyValuesHolder((Object)object, (PropertyValuesHolder[])new PropertyValuesHolder[]{PropertyValuesHolderUtils.ofPointF(DRAWABLE_ORIGIN_PROPERTY, (Path)object2)});
                object2.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, viewGroup, (BitmapDrawable)object, view, f){
                    final ChangeBounds this$0;
                    final BitmapDrawable val$drawable;
                    final ViewGroup val$sceneRoot;
                    final float val$transitionAlpha;
                    final View val$view;
                    {
                        this.this$0 = changeBounds;
                        this.val$sceneRoot = viewGroup;
                        this.val$drawable = bitmapDrawable;
                        this.val$view = view;
                        this.val$transitionAlpha = f;
                    }

                    public void onAnimationEnd(Animator animator2) {
                        ViewUtils.getOverlay((View)this.val$sceneRoot).remove((Drawable)this.val$drawable);
                        ViewUtils.setTransitionAlpha(this.val$view, this.val$transitionAlpha);
                    }
                });
                return object2;
            }
            return null;
        }
        return null;
    }

    public boolean getResizeClip() {
        return this.mResizeClip;
    }

    @Override
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    public void setResizeClip(boolean bl) {
        this.mResizeClip = bl;
    }

    private static class ViewBounds {
        private int mBottom;
        private int mBottomRightCalls;
        private int mLeft;
        private int mRight;
        private int mTop;
        private int mTopLeftCalls;
        private View mView;

        ViewBounds(View view) {
            this.mView = view;
        }

        private void setLeftTopRightBottom() {
            ViewUtils.setLeftTopRightBottom(this.mView, this.mLeft, this.mTop, this.mRight, this.mBottom);
            this.mTopLeftCalls = 0;
            this.mBottomRightCalls = 0;
        }

        void setBottomRight(PointF pointF) {
            int n;
            this.mRight = Math.round(pointF.x);
            this.mBottom = Math.round(pointF.y);
            this.mBottomRightCalls = n = this.mBottomRightCalls + 1;
            if (this.mTopLeftCalls == n) {
                this.setLeftTopRightBottom();
            }
        }

        void setTopLeft(PointF pointF) {
            int n;
            this.mLeft = Math.round(pointF.x);
            this.mTop = Math.round(pointF.y);
            this.mTopLeftCalls = n = this.mTopLeftCalls + 1;
            if (n == this.mBottomRightCalls) {
                this.setLeftTopRightBottom();
            }
        }
    }
}

