/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.ObjectAnimator
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.Canvas
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.Region$Op
 *  android.graphics.Typeface
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.os.Build$VERSION
 *  android.text.Layout
 *  android.text.Layout$Alignment
 *  android.text.StaticLayout
 *  android.text.TextPaint
 *  android.text.TextUtils
 *  android.text.method.TransformationMethod
 *  android.util.AttributeSet
 *  android.util.Property
 *  android.view.ActionMode$Callback
 *  android.view.MotionEvent
 *  android.view.VelocityTracker
 *  android.view.View
 *  android.view.ViewConfiguration
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.widget.CompoundButton
 *  android.widget.TextView
 */
package androidx.appcompat.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Property;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.text.AllCapsTransformationMethod;
import androidx.appcompat.widget.AppCompatTextHelper;
import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.ThemeUtils;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;

public class SwitchCompat
extends CompoundButton {
    private static final String ACCESSIBILITY_EVENT_CLASS_NAME = "android.widget.Switch";
    private static final int[] CHECKED_STATE_SET;
    private static final int MONOSPACE = 3;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int THUMB_ANIMATION_DURATION = 250;
    private static final Property<SwitchCompat, Float> THUMB_POS;
    private static final int TOUCH_MODE_DOWN = 1;
    private static final int TOUCH_MODE_DRAGGING = 2;
    private static final int TOUCH_MODE_IDLE = 0;
    private boolean mHasThumbTint = false;
    private boolean mHasThumbTintMode = false;
    private boolean mHasTrackTint = false;
    private boolean mHasTrackTintMode = false;
    private int mMinFlingVelocity;
    private Layout mOffLayout;
    private Layout mOnLayout;
    ObjectAnimator mPositionAnimator;
    private boolean mShowText;
    private boolean mSplitTrack;
    private int mSwitchBottom;
    private int mSwitchHeight;
    private int mSwitchLeft;
    private int mSwitchMinWidth;
    private int mSwitchPadding;
    private int mSwitchRight;
    private int mSwitchTop;
    private TransformationMethod mSwitchTransformationMethod;
    private int mSwitchWidth;
    private final Rect mTempRect;
    private ColorStateList mTextColors;
    private final AppCompatTextHelper mTextHelper;
    private CharSequence mTextOff;
    private CharSequence mTextOn;
    private final TextPaint mTextPaint;
    private Drawable mThumbDrawable;
    float mThumbPosition;
    private int mThumbTextPadding;
    private ColorStateList mThumbTintList = null;
    private PorterDuff.Mode mThumbTintMode = null;
    private int mThumbWidth;
    private int mTouchMode;
    private int mTouchSlop;
    private float mTouchX;
    private float mTouchY;
    private Drawable mTrackDrawable;
    private ColorStateList mTrackTintList = null;
    private PorterDuff.Mode mTrackTintMode = null;
    private VelocityTracker mVelocityTracker = VelocityTracker.obtain();

    static {
        THUMB_POS = new Property<SwitchCompat, Float>(Float.class, "thumbPos"){

            public Float get(SwitchCompat switchCompat) {
                return Float.valueOf(switchCompat.mThumbPosition);
            }

            public void set(SwitchCompat switchCompat, Float f) {
                switchCompat.setThumbPosition(f.floatValue());
            }
        };
        CHECKED_STATE_SET = new int[]{0x10100A0};
    }

    public SwitchCompat(Context context) {
        this(context, null);
    }

    public SwitchCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.switchStyle);
    }

    public SwitchCompat(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        int n2;
        this.mTempRect = new Rect();
        ThemeUtils.checkAppCompatTheme((View)this, this.getContext());
        Object object = new TextPaint(1);
        this.mTextPaint = object;
        ((TextPaint)object).density = this.getResources().getDisplayMetrics().density;
        object = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.SwitchCompat, n, 0);
        ViewCompat.saveAttributeDataForStyleable((View)this, context, R.styleable.SwitchCompat, attributeSet, ((TintTypedArray)object).getWrappedTypeArray(), n, 0);
        Object object2 = ((TintTypedArray)object).getDrawable(R.styleable.SwitchCompat_android_thumb);
        this.mThumbDrawable = object2;
        if (object2 != null) {
            object2.setCallback((Drawable.Callback)this);
        }
        object2 = ((TintTypedArray)object).getDrawable(R.styleable.SwitchCompat_track);
        this.mTrackDrawable = object2;
        if (object2 != null) {
            object2.setCallback((Drawable.Callback)this);
        }
        this.mTextOn = ((TintTypedArray)object).getText(R.styleable.SwitchCompat_android_textOn);
        this.mTextOff = ((TintTypedArray)object).getText(R.styleable.SwitchCompat_android_textOff);
        this.mShowText = ((TintTypedArray)object).getBoolean(R.styleable.SwitchCompat_showText, true);
        this.mThumbTextPadding = ((TintTypedArray)object).getDimensionPixelSize(R.styleable.SwitchCompat_thumbTextPadding, 0);
        this.mSwitchMinWidth = ((TintTypedArray)object).getDimensionPixelSize(R.styleable.SwitchCompat_switchMinWidth, 0);
        this.mSwitchPadding = ((TintTypedArray)object).getDimensionPixelSize(R.styleable.SwitchCompat_switchPadding, 0);
        this.mSplitTrack = ((TintTypedArray)object).getBoolean(R.styleable.SwitchCompat_splitTrack, false);
        object2 = ((TintTypedArray)object).getColorStateList(R.styleable.SwitchCompat_thumbTint);
        if (object2 != null) {
            this.mThumbTintList = object2;
            this.mHasThumbTint = true;
        }
        if (this.mThumbTintMode != (object2 = DrawableUtils.parseTintMode(((TintTypedArray)object).getInt(R.styleable.SwitchCompat_thumbTintMode, -1), null))) {
            this.mThumbTintMode = object2;
            this.mHasThumbTintMode = true;
        }
        if (this.mHasThumbTint || this.mHasThumbTintMode) {
            this.applyThumbTint();
        }
        if ((object2 = ((TintTypedArray)object).getColorStateList(R.styleable.SwitchCompat_trackTint)) != null) {
            this.mTrackTintList = object2;
            this.mHasTrackTint = true;
        }
        if (this.mTrackTintMode != (object2 = DrawableUtils.parseTintMode(((TintTypedArray)object).getInt(R.styleable.SwitchCompat_trackTintMode, -1), null))) {
            this.mTrackTintMode = object2;
            this.mHasTrackTintMode = true;
        }
        if (this.mHasTrackTint || this.mHasTrackTintMode) {
            this.applyTrackTint();
        }
        if ((n2 = ((TintTypedArray)object).getResourceId(R.styleable.SwitchCompat_switchTextAppearance, 0)) != 0) {
            this.setSwitchTextAppearance(context, n2);
        }
        object2 = new AppCompatTextHelper((TextView)this);
        this.mTextHelper = object2;
        ((AppCompatTextHelper)object2).loadFromAttributes(attributeSet, n);
        ((TintTypedArray)object).recycle();
        context = ViewConfiguration.get((Context)context);
        this.mTouchSlop = context.getScaledTouchSlop();
        this.mMinFlingVelocity = context.getScaledMinimumFlingVelocity();
        this.refreshDrawableState();
        this.setChecked(this.isChecked());
    }

    private void animateThumbToCheckedState(boolean bl) {
        ObjectAnimator objectAnimator;
        float f = bl ? 1.0f : 0.0f;
        this.mPositionAnimator = objectAnimator = ObjectAnimator.ofFloat((Object)((Object)this), THUMB_POS, (float[])new float[]{f});
        objectAnimator.setDuration(250L);
        if (Build.VERSION.SDK_INT >= 18) {
            this.mPositionAnimator.setAutoCancel(true);
        }
        this.mPositionAnimator.start();
    }

    private void applyThumbTint() {
        Drawable drawable2 = this.mThumbDrawable;
        if (drawable2 != null && (this.mHasThumbTint || this.mHasThumbTintMode)) {
            this.mThumbDrawable = drawable2 = DrawableCompat.wrap(drawable2).mutate();
            if (this.mHasThumbTint) {
                DrawableCompat.setTintList(drawable2, this.mThumbTintList);
            }
            if (this.mHasThumbTintMode) {
                DrawableCompat.setTintMode(this.mThumbDrawable, this.mThumbTintMode);
            }
            if (this.mThumbDrawable.isStateful()) {
                this.mThumbDrawable.setState(this.getDrawableState());
            }
        }
    }

    private void applyTrackTint() {
        Drawable drawable2 = this.mTrackDrawable;
        if (drawable2 != null && (this.mHasTrackTint || this.mHasTrackTintMode)) {
            this.mTrackDrawable = drawable2 = DrawableCompat.wrap(drawable2).mutate();
            if (this.mHasTrackTint) {
                DrawableCompat.setTintList(drawable2, this.mTrackTintList);
            }
            if (this.mHasTrackTintMode) {
                DrawableCompat.setTintMode(this.mTrackDrawable, this.mTrackTintMode);
            }
            if (this.mTrackDrawable.isStateful()) {
                this.mTrackDrawable.setState(this.getDrawableState());
            }
        }
    }

    private void cancelPositionAnimator() {
        ObjectAnimator objectAnimator = this.mPositionAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    private void cancelSuperTouch(MotionEvent motionEvent) {
        motionEvent = MotionEvent.obtain((MotionEvent)motionEvent);
        motionEvent.setAction(3);
        super.onTouchEvent(motionEvent);
        motionEvent.recycle();
    }

    private static float constrain(float f, float f2, float f3) {
        block1: {
            block0: {
                if (!(f < f2)) break block0;
                f = f2;
                break block1;
            }
            if (!(f > f3)) break block1;
            f = f3;
        }
        return f;
    }

    private boolean getTargetCheckedState() {
        boolean bl = this.mThumbPosition > 0.5f;
        return bl;
    }

    private int getThumbOffset() {
        float f = ViewUtils.isLayoutRtl((View)this) ? 1.0f - this.mThumbPosition : this.mThumbPosition;
        return (int)((float)this.getThumbScrollRange() * f + 0.5f);
    }

    private int getThumbScrollRange() {
        Drawable drawable2 = this.mTrackDrawable;
        if (drawable2 != null) {
            Rect rect = this.mTempRect;
            drawable2.getPadding(rect);
            drawable2 = this.mThumbDrawable;
            drawable2 = drawable2 != null ? DrawableUtils.getOpticalBounds(drawable2) : DrawableUtils.INSETS_NONE;
            return this.mSwitchWidth - this.mThumbWidth - rect.left - rect.right - drawable2.left - drawable2.right;
        }
        return 0;
    }

    private boolean hitThumb(float f, float f2) {
        Drawable drawable2 = this.mThumbDrawable;
        boolean bl = false;
        if (drawable2 == null) {
            return false;
        }
        int n = this.getThumbOffset();
        this.mThumbDrawable.getPadding(this.mTempRect);
        int n2 = this.mSwitchTop;
        int n3 = this.mTouchSlop;
        int n4 = this.mSwitchLeft + n - n3;
        int n5 = this.mThumbWidth;
        int n6 = this.mTempRect.left;
        n = this.mTempRect.right;
        int n7 = this.mTouchSlop;
        int n8 = this.mSwitchBottom;
        boolean bl2 = bl;
        if (f > (float)n4) {
            bl2 = bl;
            if (f < (float)(n5 + n4 + n6 + n + n7)) {
                bl2 = bl;
                if (f2 > (float)(n2 - n3)) {
                    bl2 = bl;
                    if (f2 < (float)(n8 + n7)) {
                        bl2 = true;
                    }
                }
            }
        }
        return bl2;
    }

    private Layout makeLayout(CharSequence charSequence) {
        TransformationMethod transformationMethod = this.mSwitchTransformationMethod;
        if (transformationMethod != null) {
            charSequence = transformationMethod.getTransformation(charSequence, (View)this);
        }
        transformationMethod = this.mTextPaint;
        int n = charSequence != null ? (int)Math.ceil(Layout.getDesiredWidth((CharSequence)charSequence, (TextPaint)transformationMethod)) : 0;
        return new StaticLayout(charSequence, (TextPaint)transformationMethod, n, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
    }

    private void setSwitchTypefaceByIndex(int n, int n2) {
        Typeface typeface = null;
        switch (n) {
            default: {
                break;
            }
            case 3: {
                typeface = Typeface.MONOSPACE;
                break;
            }
            case 2: {
                typeface = Typeface.SERIF;
                break;
            }
            case 1: {
                typeface = Typeface.SANS_SERIF;
            }
        }
        this.setSwitchTypeface(typeface, n2);
    }

    private void stopDrag(MotionEvent motionEvent) {
        this.mTouchMode = 0;
        int n = motionEvent.getAction();
        boolean bl = true;
        n = n == 1 && this.isEnabled() ? 1 : 0;
        boolean bl2 = this.isChecked();
        if (n != 0) {
            this.mVelocityTracker.computeCurrentVelocity(1000);
            float f = this.mVelocityTracker.getXVelocity();
            if (Math.abs(f) > (float)this.mMinFlingVelocity) {
                if (!(ViewUtils.isLayoutRtl((View)this) ? f < 0.0f : f > 0.0f)) {
                    bl = false;
                }
            } else {
                bl = this.getTargetCheckedState();
            }
        } else {
            bl = bl2;
        }
        if (bl != bl2) {
            this.playSoundEffect(0);
        }
        this.setChecked(bl);
        this.cancelSuperTouch(motionEvent);
    }

    public void draw(Canvas canvas) {
        Rect rect = this.mTempRect;
        int n = this.mSwitchLeft;
        int n2 = this.mSwitchTop;
        int n3 = this.mSwitchRight;
        int n4 = this.mSwitchBottom;
        int n5 = this.getThumbOffset() + n;
        Drawable drawable2 = this.mThumbDrawable;
        drawable2 = drawable2 != null ? DrawableUtils.getOpticalBounds(drawable2) : DrawableUtils.INSETS_NONE;
        Drawable drawable3 = this.mTrackDrawable;
        int n6 = n5;
        if (drawable3 != null) {
            drawable3.getPadding(rect);
            int n7 = n5 + rect.left;
            n5 = n2;
            int n8 = n4;
            int n9 = n;
            int n10 = n5;
            int n11 = n3;
            int n12 = n8;
            if (drawable2 != null) {
                n6 = n;
                if (drawable2.left > rect.left) {
                    n6 = n + (drawable2.left - rect.left);
                }
                n = n5;
                if (drawable2.top > rect.top) {
                    n = n5 + (drawable2.top - rect.top);
                }
                n5 = n3;
                if (drawable2.right > rect.right) {
                    n5 = n3 - (drawable2.right - rect.right);
                }
                n9 = n6;
                n10 = n;
                n11 = n5;
                n12 = n8;
                if (drawable2.bottom > rect.bottom) {
                    n12 = n8 - (drawable2.bottom - rect.bottom);
                    n11 = n5;
                    n10 = n;
                    n9 = n6;
                }
            }
            this.mTrackDrawable.setBounds(n9, n10, n11, n12);
            n6 = n7;
        }
        if ((drawable2 = this.mThumbDrawable) != null) {
            drawable2.getPadding(rect);
            n3 = n6 - rect.left;
            n6 = this.mThumbWidth + n6 + rect.right;
            this.mThumbDrawable.setBounds(n3, n2, n6, n4);
            drawable2 = this.getBackground();
            if (drawable2 != null) {
                DrawableCompat.setHotspotBounds(drawable2, n3, n2, n6, n4);
            }
        }
        super.draw(canvas);
    }

    public void drawableHotspotChanged(float f, float f2) {
        Drawable drawable2;
        if (Build.VERSION.SDK_INT >= 21) {
            super.drawableHotspotChanged(f, f2);
        }
        if ((drawable2 = this.mThumbDrawable) != null) {
            DrawableCompat.setHotspot(drawable2, f, f2);
        }
        if ((drawable2 = this.mTrackDrawable) != null) {
            DrawableCompat.setHotspot(drawable2, f, f2);
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] nArray = this.getDrawableState();
        boolean bl = false;
        Drawable drawable2 = this.mThumbDrawable;
        boolean bl2 = bl;
        if (drawable2 != null) {
            bl2 = bl;
            if (drawable2.isStateful()) {
                bl2 = false | drawable2.setState(nArray);
            }
        }
        drawable2 = this.mTrackDrawable;
        bl = bl2;
        if (drawable2 != null) {
            bl = bl2;
            if (drawable2.isStateful()) {
                bl = bl2 | drawable2.setState(nArray);
            }
        }
        if (bl) {
            this.invalidate();
        }
    }

    public int getCompoundPaddingLeft() {
        int n;
        if (!ViewUtils.isLayoutRtl((View)this)) {
            return super.getCompoundPaddingLeft();
        }
        int n2 = n = super.getCompoundPaddingLeft() + this.mSwitchWidth;
        if (!TextUtils.isEmpty((CharSequence)this.getText())) {
            n2 = n + this.mSwitchPadding;
        }
        return n2;
    }

    public int getCompoundPaddingRight() {
        int n;
        if (ViewUtils.isLayoutRtl((View)this)) {
            return super.getCompoundPaddingRight();
        }
        int n2 = n = super.getCompoundPaddingRight() + this.mSwitchWidth;
        if (!TextUtils.isEmpty((CharSequence)this.getText())) {
            n2 = n + this.mSwitchPadding;
        }
        return n2;
    }

    public boolean getShowText() {
        return this.mShowText;
    }

    public boolean getSplitTrack() {
        return this.mSplitTrack;
    }

    public int getSwitchMinWidth() {
        return this.mSwitchMinWidth;
    }

    public int getSwitchPadding() {
        return this.mSwitchPadding;
    }

    public CharSequence getTextOff() {
        return this.mTextOff;
    }

    public CharSequence getTextOn() {
        return this.mTextOn;
    }

    public Drawable getThumbDrawable() {
        return this.mThumbDrawable;
    }

    public int getThumbTextPadding() {
        return this.mThumbTextPadding;
    }

    public ColorStateList getThumbTintList() {
        return this.mThumbTintList;
    }

    public PorterDuff.Mode getThumbTintMode() {
        return this.mThumbTintMode;
    }

    public Drawable getTrackDrawable() {
        return this.mTrackDrawable;
    }

    public ColorStateList getTrackTintList() {
        return this.mTrackTintList;
    }

    public PorterDuff.Mode getTrackTintMode() {
        return this.mTrackTintMode;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable2 = this.mThumbDrawable;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
        }
        if ((drawable2 = this.mTrackDrawable) != null) {
            drawable2.jumpToCurrentState();
        }
        if ((drawable2 = this.mPositionAnimator) != null && drawable2.isStarted()) {
            this.mPositionAnimator.end();
            this.mPositionAnimator = null;
        }
    }

    protected int[] onCreateDrawableState(int n) {
        int[] nArray = super.onCreateDrawableState(n + 1);
        if (this.isChecked()) {
            SwitchCompat.mergeDrawableStates((int[])nArray, (int[])CHECKED_STATE_SET);
        }
        return nArray;
    }

    protected void onDraw(Canvas canvas) {
        int n;
        Rect rect;
        super.onDraw(canvas);
        Rect rect2 = this.mTempRect;
        Object object = this.mTrackDrawable;
        if (object != null) {
            object.getPadding(rect2);
        } else {
            rect2.setEmpty();
        }
        int n2 = this.mSwitchTop;
        int n3 = this.mSwitchBottom;
        int n4 = rect2.top;
        int n5 = rect2.bottom;
        Drawable drawable2 = this.mThumbDrawable;
        if (object != null) {
            if (this.mSplitTrack && drawable2 != null) {
                rect = DrawableUtils.getOpticalBounds(drawable2);
                drawable2.copyBounds(rect2);
                rect2.left += rect.left;
                rect2.right -= rect.right;
                n = canvas.save();
                canvas.clipRect(rect2, Region.Op.DIFFERENCE);
                object.draw(canvas);
                canvas.restoreToCount(n);
            } else {
                object.draw(canvas);
            }
        }
        int n6 = canvas.save();
        if (drawable2 != null) {
            drawable2.draw(canvas);
        }
        if ((rect2 = this.getTargetCheckedState() ? this.mOnLayout : this.mOffLayout) != null) {
            object = this.getDrawableState();
            rect = this.mTextColors;
            if (rect != null) {
                this.mTextPaint.setColor(rect.getColorForState((int[])object, 0));
            }
            this.mTextPaint.drawableState = (int[])object;
            if (drawable2 != null) {
                drawable2 = drawable2.getBounds();
                n = drawable2.left + drawable2.right;
            } else {
                n = this.getWidth();
            }
            int n7 = rect2.getWidth() / 2;
            n5 = (n4 + n2 + (n3 - n5)) / 2;
            n4 = rect2.getHeight() / 2;
            canvas.translate((float)((n /= 2) - n7), (float)(n5 - n4));
            rect2.draw(canvas);
        }
        canvas.restoreToCount(n6);
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName((CharSequence)ACCESSIBILITY_EVENT_CLASS_NAME);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName((CharSequence)ACCESSIBILITY_EVENT_CLASS_NAME);
        CharSequence charSequence = this.isChecked() ? this.mTextOn : this.mTextOff;
        if (!TextUtils.isEmpty((CharSequence)charSequence)) {
            CharSequence charSequence2 = accessibilityNodeInfo.getText();
            if (TextUtils.isEmpty((CharSequence)charSequence2)) {
                accessibilityNodeInfo.setText(charSequence);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(charSequence2);
                stringBuilder.append(' ');
                stringBuilder.append(charSequence);
                accessibilityNodeInfo.setText((CharSequence)stringBuilder);
            }
        }
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        n = 0;
        n2 = 0;
        if (this.mThumbDrawable != null) {
            Rect rect = this.mTempRect;
            Drawable drawable2 = this.mTrackDrawable;
            if (drawable2 != null) {
                drawable2.getPadding(rect);
            } else {
                rect.setEmpty();
            }
            drawable2 = DrawableUtils.getOpticalBounds(this.mThumbDrawable);
            n = Math.max(0, drawable2.left - rect.left);
            n2 = Math.max(0, drawable2.right - rect.right);
        }
        if (ViewUtils.isLayoutRtl((View)this)) {
            n4 = this.getPaddingLeft() + n;
            n3 = this.mSwitchWidth + n4 - n - n2;
        } else {
            n3 = this.getWidth() - this.getPaddingRight() - n2;
            n4 = n3 - this.mSwitchWidth + n + n2;
        }
        switch (this.getGravity() & 0x70) {
            default: {
                n2 = this.getPaddingTop();
                n = this.mSwitchHeight + n2;
                break;
            }
            case 80: {
                n = this.getHeight() - this.getPaddingBottom();
                n2 = n - this.mSwitchHeight;
                break;
            }
            case 16: {
                n = (this.getPaddingTop() + this.getHeight() - this.getPaddingBottom()) / 2;
                int n5 = this.mSwitchHeight;
                n2 = n -= n5 / 2;
                n = n5 + n;
            }
        }
        this.mSwitchLeft = n4;
        this.mSwitchTop = n2;
        this.mSwitchBottom = n;
        this.mSwitchRight = n3;
    }

    public void onMeasure(int n, int n2) {
        int n3;
        int n4;
        if (this.mShowText) {
            if (this.mOnLayout == null) {
                this.mOnLayout = this.makeLayout(this.mTextOn);
            }
            if (this.mOffLayout == null) {
                this.mOffLayout = this.makeLayout(this.mTextOff);
            }
        }
        Rect rect = this.mTempRect;
        Drawable drawable2 = this.mThumbDrawable;
        if (drawable2 != null) {
            drawable2.getPadding(rect);
            n4 = this.mThumbDrawable.getIntrinsicWidth() - rect.left - rect.right;
            n3 = this.mThumbDrawable.getIntrinsicHeight();
        } else {
            n4 = 0;
            n3 = 0;
        }
        int n5 = this.mShowText ? Math.max(this.mOnLayout.getWidth(), this.mOffLayout.getWidth()) + this.mThumbTextPadding * 2 : 0;
        this.mThumbWidth = Math.max(n5, n4);
        drawable2 = this.mTrackDrawable;
        if (drawable2 != null) {
            drawable2.getPadding(rect);
            n4 = this.mTrackDrawable.getIntrinsicHeight();
        } else {
            rect.setEmpty();
            n4 = 0;
        }
        int n6 = rect.left;
        int n7 = rect.right;
        rect = this.mThumbDrawable;
        int n8 = n6;
        n5 = n7;
        if (rect != null) {
            rect = DrawableUtils.getOpticalBounds((Drawable)rect);
            n8 = Math.max(n6, rect.left);
            n5 = Math.max(n7, rect.right);
        }
        n5 = Math.max(this.mSwitchMinWidth, this.mThumbWidth * 2 + n8 + n5);
        n3 = Math.max(n4, n3);
        this.mSwitchWidth = n5;
        this.mSwitchHeight = n3;
        super.onMeasure(n, n2);
        if (this.getMeasuredHeight() < n3) {
            this.setMeasuredDimension(this.getMeasuredWidthAndState(), n3);
        }
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        CharSequence charSequence = this.isChecked() ? this.mTextOn : this.mTextOff;
        if (charSequence != null) {
            accessibilityEvent.getText().add(charSequence);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mVelocityTracker.addMovement(motionEvent);
        switch (motionEvent.getActionMasked()) {
            default: {
                break;
            }
            case 2: {
                switch (this.mTouchMode) {
                    default: {
                        break;
                    }
                    case 2: {
                        float f = motionEvent.getX();
                        int n = this.getThumbScrollRange();
                        float f2 = f - this.mTouchX;
                        f2 = n != 0 ? (f2 /= (float)n) : (f2 > 0.0f ? 1.0f : -1.0f);
                        float f3 = f2;
                        if (ViewUtils.isLayoutRtl((View)this)) {
                            f3 = -f2;
                        }
                        if ((f2 = SwitchCompat.constrain(this.mThumbPosition + f3, 0.0f, 1.0f)) != this.mThumbPosition) {
                            this.mTouchX = f;
                            this.setThumbPosition(f2);
                        }
                        return true;
                    }
                    case 1: {
                        float f = motionEvent.getX();
                        float f4 = motionEvent.getY();
                        if (!(Math.abs(f - this.mTouchX) > (float)this.mTouchSlop) && !(Math.abs(f4 - this.mTouchY) > (float)this.mTouchSlop)) break;
                        this.mTouchMode = 2;
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                        this.mTouchX = f;
                        this.mTouchY = f4;
                        return true;
                    }
                    case 0: 
                }
                break;
            }
            case 1: 
            case 3: {
                if (this.mTouchMode == 2) {
                    this.stopDrag(motionEvent);
                    super.onTouchEvent(motionEvent);
                    return true;
                }
                this.mTouchMode = 0;
                this.mVelocityTracker.clear();
                break;
            }
            case 0: {
                float f = motionEvent.getX();
                float f5 = motionEvent.getY();
                if (!this.isEnabled() || !this.hitThumb(f, f5)) break;
                this.mTouchMode = 1;
                this.mTouchX = f;
                this.mTouchY = f5;
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setChecked(boolean bl) {
        super.setChecked(bl);
        bl = this.isChecked();
        if (this.getWindowToken() != null && ViewCompat.isLaidOut((View)this)) {
            this.animateThumbToCheckedState(bl);
        } else {
            this.cancelPositionAnimator();
            float f = bl ? 1.0f : 0.0f;
            this.setThumbPosition(f);
        }
    }

    public void setCustomSelectionActionModeCallback(ActionMode.Callback callback) {
        super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback((TextView)this, callback));
    }

    public void setShowText(boolean bl) {
        if (this.mShowText != bl) {
            this.mShowText = bl;
            this.requestLayout();
        }
    }

    public void setSplitTrack(boolean bl) {
        this.mSplitTrack = bl;
        this.invalidate();
    }

    public void setSwitchMinWidth(int n) {
        this.mSwitchMinWidth = n;
        this.requestLayout();
    }

    public void setSwitchPadding(int n) {
        this.mSwitchPadding = n;
        this.requestLayout();
    }

    public void setSwitchTextAppearance(Context object, int n) {
        ColorStateList colorStateList = ((TintTypedArray)(object = TintTypedArray.obtainStyledAttributes((Context)object, n, R.styleable.TextAppearance))).getColorStateList(R.styleable.TextAppearance_android_textColor);
        this.mTextColors = colorStateList != null ? colorStateList : this.getTextColors();
        n = ((TintTypedArray)object).getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
        if (n != 0 && (float)n != this.mTextPaint.getTextSize()) {
            this.mTextPaint.setTextSize((float)n);
            this.requestLayout();
        }
        this.setSwitchTypefaceByIndex(((TintTypedArray)object).getInt(R.styleable.TextAppearance_android_typeface, -1), ((TintTypedArray)object).getInt(R.styleable.TextAppearance_android_textStyle, -1));
        this.mSwitchTransformationMethod = ((TintTypedArray)object).getBoolean(R.styleable.TextAppearance_textAllCaps, false) ? new AllCapsTransformationMethod(this.getContext()) : null;
        ((TintTypedArray)object).recycle();
    }

    public void setSwitchTypeface(Typeface typeface) {
        if (this.mTextPaint.getTypeface() != null && !this.mTextPaint.getTypeface().equals((Object)typeface) || this.mTextPaint.getTypeface() == null && typeface != null) {
            this.mTextPaint.setTypeface(typeface);
            this.requestLayout();
            this.invalidate();
        }
    }

    public void setSwitchTypeface(Typeface typeface, int n) {
        float f = 0.0f;
        boolean bl = false;
        if (n > 0) {
            typeface = typeface == null ? Typeface.defaultFromStyle((int)n) : Typeface.create((Typeface)typeface, (int)n);
            this.setSwitchTypeface(typeface);
            int n2 = typeface != null ? typeface.getStyle() : 0;
            n = ~n2 & n;
            typeface = this.mTextPaint;
            if ((n & 1) != 0) {
                bl = true;
            }
            typeface.setFakeBoldText(bl);
            typeface = this.mTextPaint;
            if ((n & 2) != 0) {
                f = -0.25f;
            }
            typeface.setTextSkewX(f);
        } else {
            this.mTextPaint.setFakeBoldText(false);
            this.mTextPaint.setTextSkewX(0.0f);
            this.setSwitchTypeface(typeface);
        }
    }

    public void setTextOff(CharSequence charSequence) {
        this.mTextOff = charSequence;
        this.requestLayout();
    }

    public void setTextOn(CharSequence charSequence) {
        this.mTextOn = charSequence;
        this.requestLayout();
    }

    public void setThumbDrawable(Drawable drawable2) {
        Drawable drawable3 = this.mThumbDrawable;
        if (drawable3 != null) {
            drawable3.setCallback(null);
        }
        this.mThumbDrawable = drawable2;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback)this);
        }
        this.requestLayout();
    }

    void setThumbPosition(float f) {
        this.mThumbPosition = f;
        this.invalidate();
    }

    public void setThumbResource(int n) {
        this.setThumbDrawable(AppCompatResources.getDrawable(this.getContext(), n));
    }

    public void setThumbTextPadding(int n) {
        this.mThumbTextPadding = n;
        this.requestLayout();
    }

    public void setThumbTintList(ColorStateList colorStateList) {
        this.mThumbTintList = colorStateList;
        this.mHasThumbTint = true;
        this.applyThumbTint();
    }

    public void setThumbTintMode(PorterDuff.Mode mode) {
        this.mThumbTintMode = mode;
        this.mHasThumbTintMode = true;
        this.applyThumbTint();
    }

    public void setTrackDrawable(Drawable drawable2) {
        Drawable drawable3 = this.mTrackDrawable;
        if (drawable3 != null) {
            drawable3.setCallback(null);
        }
        this.mTrackDrawable = drawable2;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback)this);
        }
        this.requestLayout();
    }

    public void setTrackResource(int n) {
        this.setTrackDrawable(AppCompatResources.getDrawable(this.getContext(), n));
    }

    public void setTrackTintList(ColorStateList colorStateList) {
        this.mTrackTintList = colorStateList;
        this.mHasTrackTint = true;
        this.applyTrackTint();
    }

    public void setTrackTintMode(PorterDuff.Mode mode) {
        this.mTrackTintMode = mode;
        this.mHasTrackTintMode = true;
        this.applyTrackTint();
    }

    public void toggle() {
        this.setChecked(this.isChecked() ^ true);
    }

    protected boolean verifyDrawable(Drawable drawable2) {
        boolean bl = super.verifyDrawable(drawable2) || drawable2 == this.mThumbDrawable || drawable2 == this.mTrackDrawable;
        return bl;
    }
}

