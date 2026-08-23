/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.content.res.XmlResourceParser
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 *  org.xmlpull.v1.XmlPullParser
 */
package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.transition.AnimatorUtils;
import androidx.transition.Styleable;
import androidx.transition.Transition;
import androidx.transition.TransitionUtils;
import androidx.transition.TransitionValues;
import androidx.transition.ViewGroupOverlayImpl;
import androidx.transition.ViewGroupUtils;
import androidx.transition.ViewUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.xmlpull.v1.XmlPullParser;

public abstract class Visibility
extends Transition {
    public static final int MODE_IN = 1;
    public static final int MODE_OUT = 2;
    private static final String PROPNAME_PARENT = "android:visibility:parent";
    private static final String PROPNAME_SCREEN_LOCATION = "android:visibility:screenLocation";
    static final String PROPNAME_VISIBILITY = "android:visibility:visibility";
    private static final String[] sTransitionProperties = new String[]{"android:visibility:visibility", "android:visibility:parent"};
    private int mMode = 3;

    public Visibility() {
    }

    public Visibility(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        context = context.obtainStyledAttributes(attributeSet, Styleable.VISIBILITY_TRANSITION);
        int n = TypedArrayUtils.getNamedInt((TypedArray)context, (XmlPullParser)((XmlResourceParser)attributeSet), "transitionVisibilityMode", 0, 0);
        context.recycle();
        if (n != 0) {
            this.setMode(n);
        }
    }

    private void captureValues(TransitionValues transitionValues) {
        int n = transitionValues.view.getVisibility();
        transitionValues.values.put(PROPNAME_VISIBILITY, n);
        transitionValues.values.put(PROPNAME_PARENT, transitionValues.view.getParent());
        int[] nArray = new int[2];
        transitionValues.view.getLocationOnScreen(nArray);
        transitionValues.values.put(PROPNAME_SCREEN_LOCATION, nArray);
    }

    private VisibilityInfo getVisibilityChangeInfo(TransitionValues transitionValues, TransitionValues transitionValues2) {
        VisibilityInfo visibilityInfo = new VisibilityInfo();
        visibilityInfo.mVisibilityChange = false;
        visibilityInfo.mFadeIn = false;
        if (transitionValues != null && transitionValues.values.containsKey(PROPNAME_VISIBILITY)) {
            visibilityInfo.mStartVisibility = (Integer)transitionValues.values.get(PROPNAME_VISIBILITY);
            visibilityInfo.mStartParent = (ViewGroup)transitionValues.values.get(PROPNAME_PARENT);
        } else {
            visibilityInfo.mStartVisibility = -1;
            visibilityInfo.mStartParent = null;
        }
        if (transitionValues2 != null && transitionValues2.values.containsKey(PROPNAME_VISIBILITY)) {
            visibilityInfo.mEndVisibility = (Integer)transitionValues2.values.get(PROPNAME_VISIBILITY);
            visibilityInfo.mEndParent = (ViewGroup)transitionValues2.values.get(PROPNAME_PARENT);
        } else {
            visibilityInfo.mEndVisibility = -1;
            visibilityInfo.mEndParent = null;
        }
        if (transitionValues != null && transitionValues2 != null) {
            if (visibilityInfo.mStartVisibility == visibilityInfo.mEndVisibility && visibilityInfo.mStartParent == visibilityInfo.mEndParent) {
                return visibilityInfo;
            }
            if (visibilityInfo.mStartVisibility != visibilityInfo.mEndVisibility) {
                if (visibilityInfo.mStartVisibility == 0) {
                    visibilityInfo.mFadeIn = false;
                    visibilityInfo.mVisibilityChange = true;
                } else if (visibilityInfo.mEndVisibility == 0) {
                    visibilityInfo.mFadeIn = true;
                    visibilityInfo.mVisibilityChange = true;
                }
            } else if (visibilityInfo.mEndParent == null) {
                visibilityInfo.mFadeIn = false;
                visibilityInfo.mVisibilityChange = true;
            } else if (visibilityInfo.mStartParent == null) {
                visibilityInfo.mFadeIn = true;
                visibilityInfo.mVisibilityChange = true;
            }
        } else if (transitionValues == null && visibilityInfo.mEndVisibility == 0) {
            visibilityInfo.mFadeIn = true;
            visibilityInfo.mVisibilityChange = true;
        } else if (transitionValues2 == null && visibilityInfo.mStartVisibility == 0) {
            visibilityInfo.mFadeIn = false;
            visibilityInfo.mVisibilityChange = true;
        }
        return visibilityInfo;
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
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        VisibilityInfo visibilityInfo = this.getVisibilityChangeInfo(transitionValues, transitionValues2);
        if (visibilityInfo.mVisibilityChange && (visibilityInfo.mStartParent != null || visibilityInfo.mEndParent != null)) {
            if (visibilityInfo.mFadeIn) {
                return this.onAppear(viewGroup, transitionValues, visibilityInfo.mStartVisibility, transitionValues2, visibilityInfo.mEndVisibility);
            }
            return this.onDisappear(viewGroup, transitionValues, visibilityInfo.mStartVisibility, transitionValues2, visibilityInfo.mEndVisibility);
        }
        return null;
    }

    public int getMode() {
        return this.mMode;
    }

    @Override
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    @Override
    public boolean isTransitionRequired(TransitionValues object, TransitionValues transitionValues) {
        boolean bl;
        block5: {
            block6: {
                boolean bl2 = false;
                if (object == null && transitionValues == null) {
                    return false;
                }
                if (object != null && transitionValues != null && transitionValues.values.containsKey(PROPNAME_VISIBILITY) != ((TransitionValues)object).values.containsKey(PROPNAME_VISIBILITY)) {
                    return false;
                }
                object = this.getVisibilityChangeInfo((TransitionValues)object, transitionValues);
                bl = bl2;
                if (!((VisibilityInfo)object).mVisibilityChange) break block5;
                if (((VisibilityInfo)object).mStartVisibility == 0) break block6;
                bl = bl2;
                if (((VisibilityInfo)object).mEndVisibility != 0) break block5;
            }
            bl = true;
        }
        return bl;
    }

    public boolean isVisible(TransitionValues transitionValues) {
        boolean bl = false;
        if (transitionValues == null) {
            return false;
        }
        int n = (Integer)transitionValues.values.get(PROPNAME_VISIBILITY);
        transitionValues = (View)transitionValues.values.get(PROPNAME_PARENT);
        boolean bl2 = bl;
        if (n == 0) {
            bl2 = bl;
            if (transitionValues != null) {
                bl2 = true;
            }
        }
        return bl2;
    }

    public Animator onAppear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    public Animator onAppear(ViewGroup viewGroup, TransitionValues transitionValues, int n, TransitionValues transitionValues2, int n2) {
        if ((this.mMode & 1) == 1 && transitionValues2 != null) {
            if (transitionValues == null) {
                Object object = (View)transitionValues2.view.getParent();
                TransitionValues transitionValues3 = this.getMatchedTransitionValues((View)object, false);
                object = this.getTransitionValues((View)object, false);
                if (this.getVisibilityChangeInfo((TransitionValues)transitionValues3, (TransitionValues)object).mVisibilityChange) {
                    return null;
                }
            }
            return this.onAppear(viewGroup, transitionValues2.view, transitionValues, transitionValues2);
        }
        return null;
    }

    public Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    public Animator onDisappear(ViewGroup viewGroup, TransitionValues object, int object2, TransitionValues transitionValues, int object3) {
        Object object4;
        if ((this.mMode & 2) != 2) {
            return null;
        }
        Object object5 = object != null ? ((TransitionValues)object).view : null;
        Object object6 = transitionValues != null ? transitionValues.view : null;
        Object var10_8 = null;
        View view = null;
        if (object6 != null && object6.getParent() != null) {
            if (object3 == 4) {
                object4 = object6;
                object6 = var10_8;
            } else if (object5 == object6) {
                object4 = object6;
                object6 = var10_8;
            } else if (this.mCanRemoveViews) {
                object6 = object5;
                object4 = view;
            } else {
                object6 = TransitionUtils.copyViewImage(viewGroup, (View)object5, (View)object5.getParent());
                object4 = view;
            }
        } else if (object6 != null) {
            object4 = view;
        } else {
            object6 = var10_8;
            object4 = view;
            if (object5 != null) {
                if (object5.getParent() == null) {
                    object6 = object5;
                    object4 = view;
                } else {
                    object6 = var10_8;
                    object4 = view;
                    if (object5.getParent() instanceof View) {
                        View view2 = (View)object5.getParent();
                        object6 = this.getTransitionValues(view2, true);
                        object4 = this.getMatchedTransitionValues(view2, true);
                        if (!this.getVisibilityChangeInfo((TransitionValues)object6, (TransitionValues)object4).mVisibilityChange) {
                            object6 = TransitionUtils.copyViewImage(viewGroup, (View)object5, view2);
                            object4 = view;
                        } else {
                            object6 = var10_8;
                            object4 = view;
                            if (view2.getParent() == null) {
                                object2 = view2.getId();
                                object6 = var10_8;
                                object4 = view;
                                if (object2 != -1) {
                                    object6 = var10_8;
                                    object4 = view;
                                    if (viewGroup.findViewById(object2) != null) {
                                        object6 = var10_8;
                                        object4 = view;
                                        if (this.mCanRemoveViews) {
                                            object6 = object5;
                                            object4 = view;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (object6 != null && object != null) {
            object5 = (int[])((TransitionValues)object).values.get(PROPNAME_SCREEN_LOCATION);
            object2 = object5[0];
            object3 = object5[1];
            object5 = new int[2];
            viewGroup.getLocationOnScreen((int[])object5);
            object6.offsetLeftAndRight(object2 - object5[0] - object6.getLeft());
            object6.offsetTopAndBottom(object3 - object5[1] - object6.getTop());
            object5 = ViewGroupUtils.getOverlay(viewGroup);
            object5.add((View)object6);
            viewGroup = this.onDisappear(viewGroup, (View)object6, (TransitionValues)object, transitionValues);
            if (viewGroup == null) {
                object5.remove((View)object6);
            } else {
                viewGroup.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, (ViewGroupOverlayImpl)object5, (View)object6){
                    final Visibility this$0;
                    final View val$finalOverlayView;
                    final ViewGroupOverlayImpl val$overlay;
                    {
                        this.this$0 = visibility;
                        this.val$overlay = viewGroupOverlayImpl;
                        this.val$finalOverlayView = view;
                    }

                    public void onAnimationEnd(Animator animator2) {
                        this.val$overlay.remove(this.val$finalOverlayView);
                    }
                });
            }
            return viewGroup;
        }
        if (object4 != null) {
            object2 = object4.getVisibility();
            ViewUtils.setTransitionVisibility(object4, 0);
            viewGroup = this.onDisappear(viewGroup, (View)object4, (TransitionValues)object, transitionValues);
            if (viewGroup != null) {
                object = new DisappearListener((View)object4, (int)object3, true);
                viewGroup.addListener((Animator.AnimatorListener)object);
                AnimatorUtils.addPauseListener((Animator)viewGroup, (AnimatorListenerAdapter)object);
                this.addListener((Transition.TransitionListener)object);
            } else {
                ViewUtils.setTransitionVisibility(object4, object2);
            }
            return viewGroup;
        }
        return null;
    }

    public void setMode(int n) {
        if ((n & 0xFFFFFFFC) == 0) {
            this.mMode = n;
            return;
        }
        throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
    }

    private static class DisappearListener
    extends AnimatorListenerAdapter
    implements Transition.TransitionListener,
    AnimatorUtils.AnimatorPauseListenerCompat {
        boolean mCanceled = false;
        private final int mFinalVisibility;
        private boolean mLayoutSuppressed;
        private final ViewGroup mParent;
        private final boolean mSuppressLayout;
        private final View mView;

        DisappearListener(View view, int n, boolean bl) {
            this.mView = view;
            this.mFinalVisibility = n;
            this.mParent = (ViewGroup)view.getParent();
            this.mSuppressLayout = bl;
            this.suppressLayout(true);
        }

        private void hideViewWhenNotCanceled() {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
                ViewGroup viewGroup = this.mParent;
                if (viewGroup != null) {
                    viewGroup.invalidate();
                }
            }
            this.suppressLayout(false);
        }

        private void suppressLayout(boolean bl) {
            ViewGroup viewGroup;
            if (this.mSuppressLayout && this.mLayoutSuppressed != bl && (viewGroup = this.mParent) != null) {
                this.mLayoutSuppressed = bl;
                ViewGroupUtils.suppressLayout(viewGroup, bl);
            }
        }

        public void onAnimationCancel(Animator animator2) {
            this.mCanceled = true;
        }

        public void onAnimationEnd(Animator animator2) {
            this.hideViewWhenNotCanceled();
        }

        @Override
        public void onAnimationPause(Animator animator2) {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
            }
        }

        public void onAnimationRepeat(Animator animator2) {
        }

        @Override
        public void onAnimationResume(Animator animator2) {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, 0);
            }
        }

        public void onAnimationStart(Animator animator2) {
        }

        @Override
        public void onTransitionCancel(Transition transition) {
        }

        @Override
        public void onTransitionEnd(Transition transition) {
            this.hideViewWhenNotCanceled();
            transition.removeListener(this);
        }

        @Override
        public void onTransitionPause(Transition transition) {
            this.suppressLayout(false);
        }

        @Override
        public void onTransitionResume(Transition transition) {
            this.suppressLayout(true);
        }

        @Override
        public void onTransitionStart(Transition transition) {
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Mode {
    }

    private static class VisibilityInfo {
        ViewGroup mEndParent;
        int mEndVisibility;
        boolean mFadeIn;
        ViewGroup mStartParent;
        int mStartVisibility;
        boolean mVisibilityChange;

        VisibilityInfo() {
        }
    }
}

