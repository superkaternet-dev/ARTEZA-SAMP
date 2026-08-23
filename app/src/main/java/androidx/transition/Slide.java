/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.TimeInterpolator
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.DecelerateInterpolator
 *  org.xmlpull.v1.XmlPullParser
 */
package androidx.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import androidx.transition.SidePropagation;
import androidx.transition.Styleable;
import androidx.transition.TransitionValues;
import androidx.transition.TranslationAnimationCreator;
import androidx.transition.Visibility;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.xmlpull.v1.XmlPullParser;

public class Slide
extends Visibility {
    private static final String PROPNAME_SCREEN_POSITION = "android:slide:screenPosition";
    private static final TimeInterpolator sAccelerate;
    private static final CalculateSlide sCalculateBottom;
    private static final CalculateSlide sCalculateEnd;
    private static final CalculateSlide sCalculateLeft;
    private static final CalculateSlide sCalculateRight;
    private static final CalculateSlide sCalculateStart;
    private static final CalculateSlide sCalculateTop;
    private static final TimeInterpolator sDecelerate;
    private CalculateSlide mSlideCalculator = sCalculateBottom;
    private int mSlideEdge = 80;

    static {
        sDecelerate = new DecelerateInterpolator();
        sAccelerate = new AccelerateInterpolator();
        sCalculateLeft = new CalculateSlideHorizontal(){

            @Override
            public float getGoneX(ViewGroup viewGroup, View view) {
                return view.getTranslationX() - (float)viewGroup.getWidth();
            }
        };
        sCalculateStart = new CalculateSlideHorizontal(){

            @Override
            public float getGoneX(ViewGroup viewGroup, View view) {
                int n = ViewCompat.getLayoutDirection((View)viewGroup);
                boolean bl = true;
                if (n != 1) {
                    bl = false;
                }
                float f = bl ? view.getTranslationX() + (float)viewGroup.getWidth() : view.getTranslationX() - (float)viewGroup.getWidth();
                return f;
            }
        };
        sCalculateTop = new CalculateSlideVertical(){

            @Override
            public float getGoneY(ViewGroup viewGroup, View view) {
                return view.getTranslationY() - (float)viewGroup.getHeight();
            }
        };
        sCalculateRight = new CalculateSlideHorizontal(){

            @Override
            public float getGoneX(ViewGroup viewGroup, View view) {
                return view.getTranslationX() + (float)viewGroup.getWidth();
            }
        };
        sCalculateEnd = new CalculateSlideHorizontal(){

            @Override
            public float getGoneX(ViewGroup viewGroup, View view) {
                int n = ViewCompat.getLayoutDirection((View)viewGroup);
                boolean bl = true;
                if (n != 1) {
                    bl = false;
                }
                float f = bl ? view.getTranslationX() - (float)viewGroup.getWidth() : view.getTranslationX() + (float)viewGroup.getWidth();
                return f;
            }
        };
        sCalculateBottom = new CalculateSlideVertical(){

            @Override
            public float getGoneY(ViewGroup viewGroup, View view) {
                return view.getTranslationY() + (float)viewGroup.getHeight();
            }
        };
    }

    public Slide() {
        this.setSlideEdge(80);
    }

    public Slide(int n) {
        this.setSlideEdge(n);
    }

    public Slide(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        context = context.obtainStyledAttributes(attributeSet, Styleable.SLIDE);
        int n = TypedArrayUtils.getNamedInt((TypedArray)context, (XmlPullParser)attributeSet, "slideEdge", 0, 80);
        context.recycle();
        this.setSlideEdge(n);
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        int[] nArray = new int[2];
        view.getLocationOnScreen(nArray);
        transitionValues.values.put(PROPNAME_SCREEN_POSITION, nArray);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        this.captureValues(transitionValues);
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        this.captureValues(transitionValues);
    }

    public int getSlideEdge() {
        return this.mSlideEdge;
    }

    @Override
    public Animator onAppear(ViewGroup viewGroup, View view, TransitionValues object, TransitionValues transitionValues) {
        if (transitionValues == null) {
            return null;
        }
        object = (int[])transitionValues.values.get(PROPNAME_SCREEN_POSITION);
        float f = view.getTranslationX();
        float f2 = view.getTranslationY();
        float f3 = this.mSlideCalculator.getGoneX(viewGroup, view);
        float f4 = this.mSlideCalculator.getGoneY(viewGroup, view);
        return TranslationAnimationCreator.createAnimation(view, transitionValues, (int)object[0], (int)object[1], f3, f4, f, f2, sDecelerate);
    }

    @Override
    public Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues object) {
        if (transitionValues == null) {
            return null;
        }
        object = (int[])transitionValues.values.get(PROPNAME_SCREEN_POSITION);
        float f = view.getTranslationX();
        float f2 = view.getTranslationY();
        float f3 = this.mSlideCalculator.getGoneX(viewGroup, view);
        float f4 = this.mSlideCalculator.getGoneY(viewGroup, view);
        return TranslationAnimationCreator.createAnimation(view, transitionValues, (int)object[0], (int)object[1], f, f2, f3, f4, sAccelerate);
    }

    public void setSlideEdge(int n) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("Invalid slide direction");
            }
            case 0x800005: {
                this.mSlideCalculator = sCalculateEnd;
                break;
            }
            case 0x800003: {
                this.mSlideCalculator = sCalculateStart;
                break;
            }
            case 80: {
                this.mSlideCalculator = sCalculateBottom;
                break;
            }
            case 48: {
                this.mSlideCalculator = sCalculateTop;
                break;
            }
            case 5: {
                this.mSlideCalculator = sCalculateRight;
                break;
            }
            case 3: {
                this.mSlideCalculator = sCalculateLeft;
            }
        }
        this.mSlideEdge = n;
        SidePropagation sidePropagation = new SidePropagation();
        sidePropagation.setSide(n);
        this.setPropagation(sidePropagation);
    }

    private static interface CalculateSlide {
        public float getGoneX(ViewGroup var1, View var2);

        public float getGoneY(ViewGroup var1, View var2);
    }

    private static abstract class CalculateSlideHorizontal
    implements CalculateSlide {
        private CalculateSlideHorizontal() {
        }

        @Override
        public float getGoneY(ViewGroup viewGroup, View view) {
            return view.getTranslationY();
        }
    }

    private static abstract class CalculateSlideVertical
    implements CalculateSlide {
        private CalculateSlideVertical() {
        }

        @Override
        public float getGoneX(ViewGroup viewGroup, View view) {
            return view.getTranslationX();
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface GravityFlag {
    }
}

