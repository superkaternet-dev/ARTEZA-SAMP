/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.AnimatorSet
 *  android.animation.ArgbEvaluator
 *  android.animation.ObjectAnimator
 *  android.animation.TypeEvaluator
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.XmlResourceParser
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.drawable.Animatable
 *  android.graphics.drawable.AnimatedVectorDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.Xml
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package androidx.vectordrawable.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import androidx.collection.ArrayMap;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.vectordrawable.graphics.drawable.AndroidResources;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatorInflaterCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCommon;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedVectorDrawableCompat
extends VectorDrawableCommon
implements Animatable2Compat {
    private static final String ANIMATED_VECTOR = "animated-vector";
    private static final boolean DBG_ANIMATION_VECTOR_DRAWABLE = false;
    private static final String LOGTAG = "AnimatedVDCompat";
    private static final String TARGET = "target";
    private AnimatedVectorDrawableCompatState mAnimatedVectorState;
    ArrayList<Animatable2Compat.AnimationCallback> mAnimationCallbacks = null;
    private Animator.AnimatorListener mAnimatorListener = null;
    private ArgbEvaluator mArgbEvaluator = null;
    AnimatedVectorDrawableDelegateState mCachedConstantStateDelegate;
    final Drawable.Callback mCallback;
    private Context mContext;

    AnimatedVectorDrawableCompat() {
        this(null, null, null);
    }

    private AnimatedVectorDrawableCompat(Context context) {
        this(context, null, null);
    }

    private AnimatedVectorDrawableCompat(Context context, AnimatedVectorDrawableCompatState animatedVectorDrawableCompatState, Resources resources) {
        Drawable.Callback callback;
        this.mCallback = callback = new Drawable.Callback(this){
            final AnimatedVectorDrawableCompat this$0;
            {
                this.this$0 = animatedVectorDrawableCompat;
            }

            public void invalidateDrawable(Drawable drawable2) {
                this.this$0.invalidateSelf();
            }

            public void scheduleDrawable(Drawable drawable2, Runnable runnable, long l) {
                this.this$0.scheduleSelf(runnable, l);
            }

            public void unscheduleDrawable(Drawable drawable2, Runnable runnable) {
                this.this$0.unscheduleSelf(runnable);
            }
        };
        this.mContext = context;
        this.mAnimatedVectorState = animatedVectorDrawableCompatState != null ? animatedVectorDrawableCompatState : new AnimatedVectorDrawableCompatState(context, animatedVectorDrawableCompatState, callback, resources);
    }

    public static void clearAnimationCallbacks(Drawable drawable2) {
        if (!(drawable2 instanceof Animatable)) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 24) {
            ((AnimatedVectorDrawable)drawable2).clearAnimationCallbacks();
        } else {
            ((AnimatedVectorDrawableCompat)drawable2).clearAnimationCallbacks();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static AnimatedVectorDrawableCompat create(Context object, int n) {
        int n2;
        if (Build.VERSION.SDK_INT >= 24) {
            AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat((Context)object);
            animatedVectorDrawableCompat.mDelegateDrawable = ResourcesCompat.getDrawable(object.getResources(), n2, object.getTheme());
            animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
            animatedVectorDrawableCompat.mCachedConstantStateDelegate = new AnimatedVectorDrawableDelegateState(animatedVectorDrawableCompat.mDelegateDrawable.getConstantState());
            return animatedVectorDrawableCompat;
        }
        Resources resources = object.getResources();
        try {
            XmlResourceParser xmlResourceParser = resources.getXml(n2);
            resources = Xml.asAttributeSet((XmlPullParser)xmlResourceParser);
            while ((n2 = xmlResourceParser.next()) != 2 && n2 != 1) {
            }
            if (n2 == 2) {
                return AnimatedVectorDrawableCompat.createFromXmlInner(object, object.getResources(), (XmlPullParser)xmlResourceParser, (AttributeSet)resources, object.getTheme());
            }
            XmlPullParserException xmlPullParserException = new XmlPullParserException("No start tag found");
            throw xmlPullParserException;
        }
        catch (IOException iOException) {
            Log.e((String)LOGTAG, (String)"parser error", (Throwable)iOException);
            return null;
        }
        catch (XmlPullParserException xmlPullParserException) {
            Log.e((String)LOGTAG, (String)"parser error", (Throwable)xmlPullParserException);
        }
        return null;
    }

    public static AnimatedVectorDrawableCompat createFromXmlInner(Context object, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        object = new AnimatedVectorDrawableCompat((Context)object);
        ((AnimatedVectorDrawableCompat)object).inflate(resources, xmlPullParser, attributeSet, theme);
        return object;
    }

    public static void registerAnimationCallback(Drawable drawable2, Animatable2Compat.AnimationCallback animationCallback) {
        if (drawable2 != null && animationCallback != null) {
            if (!(drawable2 instanceof Animatable)) {
                return;
            }
            if (Build.VERSION.SDK_INT >= 24) {
                AnimatedVectorDrawableCompat.registerPlatformCallback((AnimatedVectorDrawable)drawable2, animationCallback);
            } else {
                ((AnimatedVectorDrawableCompat)drawable2).registerAnimationCallback(animationCallback);
            }
            return;
        }
    }

    private static void registerPlatformCallback(AnimatedVectorDrawable animatedVectorDrawable, Animatable2Compat.AnimationCallback animationCallback) {
        animatedVectorDrawable.registerAnimationCallback(animationCallback.getPlatformCallback());
    }

    private void removeAnimatorSetListener() {
        if (this.mAnimatorListener != null) {
            this.mAnimatedVectorState.mAnimatorSet.removeListener(this.mAnimatorListener);
            this.mAnimatorListener = null;
        }
    }

    private void setupAnimatorsForTarget(String string2, Animator animator2) {
        animator2.setTarget(this.mAnimatedVectorState.mVectorDrawable.getTargetByName(string2));
        if (Build.VERSION.SDK_INT < 21) {
            this.setupColorAnimator(animator2);
        }
        if (this.mAnimatedVectorState.mAnimators == null) {
            this.mAnimatedVectorState.mAnimators = new ArrayList();
            this.mAnimatedVectorState.mTargetNameMap = new ArrayMap();
        }
        this.mAnimatedVectorState.mAnimators.add(animator2);
        this.mAnimatedVectorState.mTargetNameMap.put(animator2, string2);
    }

    private void setupColorAnimator(Animator object) {
        ArrayList arrayList;
        if (object instanceof AnimatorSet && (arrayList = ((AnimatorSet)object).getChildAnimations()) != null) {
            for (int i = 0; i < arrayList.size(); ++i) {
                this.setupColorAnimator((Animator)arrayList.get(i));
            }
        }
        if (object instanceof ObjectAnimator && ("fillColor".equals(object = (arrayList = (ObjectAnimator)object).getPropertyName()) || "strokeColor".equals(object))) {
            if (this.mArgbEvaluator == null) {
                this.mArgbEvaluator = new ArgbEvaluator();
            }
            arrayList.setEvaluator((TypeEvaluator)this.mArgbEvaluator);
        }
    }

    public static boolean unregisterAnimationCallback(Drawable drawable2, Animatable2Compat.AnimationCallback animationCallback) {
        if (drawable2 != null && animationCallback != null) {
            if (!(drawable2 instanceof Animatable)) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= 24) {
                return AnimatedVectorDrawableCompat.unregisterPlatformCallback((AnimatedVectorDrawable)drawable2, animationCallback);
            }
            return ((AnimatedVectorDrawableCompat)drawable2).unregisterAnimationCallback(animationCallback);
        }
        return false;
    }

    private static boolean unregisterPlatformCallback(AnimatedVectorDrawable animatedVectorDrawable, Animatable2Compat.AnimationCallback animationCallback) {
        return animatedVectorDrawable.unregisterAnimationCallback(animationCallback.getPlatformCallback());
    }

    @Override
    public void applyTheme(Resources.Theme theme) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.applyTheme(this.mDelegateDrawable, theme);
            return;
        }
    }

    public boolean canApplyTheme() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.canApplyTheme(this.mDelegateDrawable);
        }
        return false;
    }

    @Override
    public void clearAnimationCallbacks() {
        if (this.mDelegateDrawable != null) {
            ((AnimatedVectorDrawable)this.mDelegateDrawable).clearAnimationCallbacks();
            return;
        }
        this.removeAnimatorSetListener();
        ArrayList<Animatable2Compat.AnimationCallback> arrayList = this.mAnimationCallbacks;
        if (arrayList == null) {
            return;
        }
        arrayList.clear();
    }

    public void draw(Canvas canvas) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.draw(canvas);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.draw(canvas);
        if (this.mAnimatedVectorState.mAnimatorSet.isStarted()) {
            this.invalidateSelf();
        }
    }

    public int getAlpha() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.getAlpha(this.mDelegateDrawable);
        }
        return this.mAnimatedVectorState.mVectorDrawable.getAlpha();
    }

    public int getChangingConfigurations() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getChangingConfigurations();
        }
        return super.getChangingConfigurations() | this.mAnimatedVectorState.mChangingConfigurations;
    }

    public ColorFilter getColorFilter() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.getColorFilter(this.mDelegateDrawable);
        }
        return this.mAnimatedVectorState.mVectorDrawable.getColorFilter();
    }

    public Drawable.ConstantState getConstantState() {
        if (this.mDelegateDrawable != null && Build.VERSION.SDK_INT >= 24) {
            return new AnimatedVectorDrawableDelegateState(this.mDelegateDrawable.getConstantState());
        }
        return null;
    }

    public int getIntrinsicHeight() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getIntrinsicHeight();
        }
        return this.mAnimatedVectorState.mVectorDrawable.getIntrinsicHeight();
    }

    public int getIntrinsicWidth() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getIntrinsicWidth();
        }
        return this.mAnimatedVectorState.mVectorDrawable.getIntrinsicWidth();
    }

    public int getOpacity() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getOpacity();
        }
        return this.mAnimatedVectorState.mVectorDrawable.getOpacity();
    }

    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet) throws XmlPullParserException, IOException {
        this.inflate(resources, xmlPullParser, attributeSet, null);
    }

    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.inflate(this.mDelegateDrawable, resources, xmlPullParser, attributeSet, theme);
            return;
        }
        int n = xmlPullParser.getEventType();
        int n2 = xmlPullParser.getDepth();
        while (n != 1 && (xmlPullParser.getDepth() >= n2 + 1 || n != 3)) {
            if (n == 2) {
                Object object;
                String string2 = xmlPullParser.getName();
                if (ANIMATED_VECTOR.equals(string2)) {
                    string2 = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_ANIMATED_VECTOR_DRAWABLE);
                    n = string2.getResourceId(0, 0);
                    if (n != 0) {
                        object = VectorDrawableCompat.create(resources, n, theme);
                        ((VectorDrawableCompat)object).setAllowCaching(false);
                        object.setCallback(this.mCallback);
                        if (this.mAnimatedVectorState.mVectorDrawable != null) {
                            this.mAnimatedVectorState.mVectorDrawable.setCallback(null);
                        }
                        this.mAnimatedVectorState.mVectorDrawable = object;
                    }
                    string2.recycle();
                } else if (TARGET.equals(string2)) {
                    object = resources.obtainAttributes(attributeSet, AndroidResources.STYLEABLE_ANIMATED_VECTOR_DRAWABLE_TARGET);
                    string2 = object.getString(0);
                    n = object.getResourceId(1, 0);
                    if (n != 0) {
                        Context context = this.mContext;
                        if (context != null) {
                            this.setupAnimatorsForTarget(string2, AnimatorInflaterCompat.loadAnimator(context, n));
                        } else {
                            object.recycle();
                            throw new IllegalStateException("Context can't be null when inflating animators");
                        }
                    }
                    object.recycle();
                }
            }
            n = xmlPullParser.next();
        }
        this.mAnimatedVectorState.setupAnimatorSet();
    }

    public boolean isAutoMirrored() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.isAutoMirrored(this.mDelegateDrawable);
        }
        return this.mAnimatedVectorState.mVectorDrawable.isAutoMirrored();
    }

    public boolean isRunning() {
        if (this.mDelegateDrawable != null) {
            return ((AnimatedVectorDrawable)this.mDelegateDrawable).isRunning();
        }
        return this.mAnimatedVectorState.mAnimatorSet.isRunning();
    }

    public boolean isStateful() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.isStateful();
        }
        return this.mAnimatedVectorState.mVectorDrawable.isStateful();
    }

    public Drawable mutate() {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.mutate();
        }
        return this;
    }

    @Override
    protected void onBoundsChange(Rect rect) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setBounds(rect);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setBounds(rect);
    }

    @Override
    protected boolean onLevelChange(int n) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setLevel(n);
        }
        return this.mAnimatedVectorState.mVectorDrawable.setLevel(n);
    }

    protected boolean onStateChange(int[] nArray) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setState(nArray);
        }
        return this.mAnimatedVectorState.mVectorDrawable.setState(nArray);
    }

    @Override
    public void registerAnimationCallback(Animatable2Compat.AnimationCallback animationCallback) {
        if (this.mDelegateDrawable != null) {
            AnimatedVectorDrawableCompat.registerPlatformCallback((AnimatedVectorDrawable)this.mDelegateDrawable, animationCallback);
            return;
        }
        if (animationCallback == null) {
            return;
        }
        if (this.mAnimationCallbacks == null) {
            this.mAnimationCallbacks = new ArrayList();
        }
        if (this.mAnimationCallbacks.contains(animationCallback)) {
            return;
        }
        this.mAnimationCallbacks.add(animationCallback);
        if (this.mAnimatorListener == null) {
            this.mAnimatorListener = new AnimatorListenerAdapter(this){
                final AnimatedVectorDrawableCompat this$0;
                {
                    this.this$0 = animatedVectorDrawableCompat;
                }

                public void onAnimationEnd(Animator object) {
                    object = new ArrayList<Animatable2Compat.AnimationCallback>(this.this$0.mAnimationCallbacks);
                    int n = ((ArrayList)object).size();
                    for (int i = 0; i < n; ++i) {
                        ((Animatable2Compat.AnimationCallback)((ArrayList)object).get(i)).onAnimationEnd(this.this$0);
                    }
                }

                public void onAnimationStart(Animator object) {
                    object = new ArrayList<Animatable2Compat.AnimationCallback>(this.this$0.mAnimationCallbacks);
                    int n = ((ArrayList)object).size();
                    for (int i = 0; i < n; ++i) {
                        ((Animatable2Compat.AnimationCallback)((ArrayList)object).get(i)).onAnimationStart(this.this$0);
                    }
                }
            };
        }
        this.mAnimatedVectorState.mAnimatorSet.addListener(this.mAnimatorListener);
    }

    public void setAlpha(int n) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setAlpha(n);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setAlpha(n);
    }

    public void setAutoMirrored(boolean bl) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setAutoMirrored(this.mDelegateDrawable, bl);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setAutoMirrored(bl);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setColorFilter(colorFilter);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setColorFilter(colorFilter);
    }

    @Override
    public void setTint(int n) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTint(this.mDelegateDrawable, n);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setTint(n);
    }

    @Override
    public void setTintList(ColorStateList colorStateList) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTintList(this.mDelegateDrawable, colorStateList);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setTintList(colorStateList);
    }

    @Override
    public void setTintMode(PorterDuff.Mode mode) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTintMode(this.mDelegateDrawable, mode);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setTintMode(mode);
    }

    public boolean setVisible(boolean bl, boolean bl2) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setVisible(bl, bl2);
        }
        this.mAnimatedVectorState.mVectorDrawable.setVisible(bl, bl2);
        return super.setVisible(bl, bl2);
    }

    public void start() {
        if (this.mDelegateDrawable != null) {
            ((AnimatedVectorDrawable)this.mDelegateDrawable).start();
            return;
        }
        if (this.mAnimatedVectorState.mAnimatorSet.isStarted()) {
            return;
        }
        this.mAnimatedVectorState.mAnimatorSet.start();
        this.invalidateSelf();
    }

    public void stop() {
        if (this.mDelegateDrawable != null) {
            ((AnimatedVectorDrawable)this.mDelegateDrawable).stop();
            return;
        }
        this.mAnimatedVectorState.mAnimatorSet.end();
    }

    @Override
    public boolean unregisterAnimationCallback(Animatable2Compat.AnimationCallback animationCallback) {
        ArrayList<Animatable2Compat.AnimationCallback> arrayList;
        if (this.mDelegateDrawable != null) {
            AnimatedVectorDrawableCompat.unregisterPlatformCallback((AnimatedVectorDrawable)this.mDelegateDrawable, animationCallback);
        }
        if ((arrayList = this.mAnimationCallbacks) != null && animationCallback != null) {
            boolean bl = arrayList.remove(animationCallback);
            if (this.mAnimationCallbacks.size() == 0) {
                this.removeAnimatorSetListener();
            }
            return bl;
        }
        return false;
    }

    private static class AnimatedVectorDrawableCompatState
    extends Drawable.ConstantState {
        AnimatorSet mAnimatorSet;
        ArrayList<Animator> mAnimators;
        int mChangingConfigurations;
        ArrayMap<Animator, String> mTargetNameMap;
        VectorDrawableCompat mVectorDrawable;

        public AnimatedVectorDrawableCompatState(Context object, AnimatedVectorDrawableCompatState animatedVectorDrawableCompatState, Drawable.Callback object2, Resources resources) {
            if (animatedVectorDrawableCompatState != null) {
                this.mChangingConfigurations = animatedVectorDrawableCompatState.mChangingConfigurations;
                object = animatedVectorDrawableCompatState.mVectorDrawable;
                if (object != null) {
                    object = ((VectorDrawableCompat)object).getConstantState();
                    this.mVectorDrawable = resources != null ? (VectorDrawableCompat)object.newDrawable(resources) : (VectorDrawableCompat)object.newDrawable();
                    this.mVectorDrawable = object = (VectorDrawableCompat)this.mVectorDrawable.mutate();
                    object.setCallback(object2);
                    this.mVectorDrawable.setBounds(animatedVectorDrawableCompatState.mVectorDrawable.getBounds());
                    this.mVectorDrawable.setAllowCaching(false);
                }
                if ((object = animatedVectorDrawableCompatState.mAnimators) != null) {
                    int n = ((ArrayList)object).size();
                    this.mAnimators = new ArrayList(n);
                    this.mTargetNameMap = new ArrayMap(n);
                    for (int i = 0; i < n; ++i) {
                        object2 = animatedVectorDrawableCompatState.mAnimators.get(i);
                        object = object2.clone();
                        object2 = (String)animatedVectorDrawableCompatState.mTargetNameMap.get(object2);
                        object.setTarget(this.mVectorDrawable.getTargetByName((String)object2));
                        this.mAnimators.add((Animator)object);
                        this.mTargetNameMap.put((Animator)object, (String)object2);
                    }
                    this.setupAnimatorSet();
                }
            }
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations;
        }

        public Drawable newDrawable() {
            throw new IllegalStateException("No constant state support for SDK < 24.");
        }

        public Drawable newDrawable(Resources resources) {
            throw new IllegalStateException("No constant state support for SDK < 24.");
        }

        public void setupAnimatorSet() {
            if (this.mAnimatorSet == null) {
                this.mAnimatorSet = new AnimatorSet();
            }
            this.mAnimatorSet.playTogether(this.mAnimators);
        }
    }

    private static class AnimatedVectorDrawableDelegateState
    extends Drawable.ConstantState {
        private final Drawable.ConstantState mDelegateState;

        public AnimatedVectorDrawableDelegateState(Drawable.ConstantState constantState) {
            this.mDelegateState = constantState;
        }

        public boolean canApplyTheme() {
            return this.mDelegateState.canApplyTheme();
        }

        public int getChangingConfigurations() {
            return this.mDelegateState.getChangingConfigurations();
        }

        public Drawable newDrawable() {
            AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
            animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable();
            animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
            return animatedVectorDrawableCompat;
        }

        public Drawable newDrawable(Resources resources) {
            AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
            animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable(resources);
            animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
            return animatedVectorDrawableCompat;
        }

        public Drawable newDrawable(Resources resources, Resources.Theme theme) {
            AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
            animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable(resources, theme);
            animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
            return animatedVectorDrawableCompat;
        }
    }
}

