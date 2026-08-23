/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.Typeface
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.DrawableContainer
 *  android.graphics.drawable.GradientDrawable
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.text.Editable
 *  android.text.TextUtils
 *  android.text.TextWatcher
 *  android.text.method.PasswordTransformationMethod
 *  android.text.method.TransformationMethod
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.SparseArray
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewStructure
 *  android.view.accessibility.AccessibilityEvent
 *  android.widget.EditText
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.TextView
 */
package com.google.android.material.textfield;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.view.accessibility.AccessibilityEvent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.internal.CollapsingTextHelper;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.textfield.CutoutDrawable;
import com.google.android.material.textfield.IndicatorViewController;
import com.google.android.material.textfield.TextInputEditText;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TextInputLayout
extends LinearLayout {
    public static final int BOX_BACKGROUND_FILLED = 1;
    public static final int BOX_BACKGROUND_NONE = 0;
    public static final int BOX_BACKGROUND_OUTLINE = 2;
    private static final int INVALID_MAX_LENGTH = -1;
    private static final int LABEL_SCALE_ANIMATION_DURATION = 167;
    private static final String LOG_TAG = "TextInputLayout";
    private ValueAnimator animator;
    private GradientDrawable boxBackground;
    private int boxBackgroundColor;
    private int boxBackgroundMode;
    private final int boxBottomOffsetPx;
    private final int boxCollapsedPaddingTopPx;
    private float boxCornerRadiusBottomEnd;
    private float boxCornerRadiusBottomStart;
    private float boxCornerRadiusTopEnd;
    private float boxCornerRadiusTopStart;
    private final int boxLabelCutoutPaddingPx;
    private int boxStrokeColor;
    private final int boxStrokeWidthDefaultPx;
    private final int boxStrokeWidthFocusedPx;
    private int boxStrokeWidthPx;
    final CollapsingTextHelper collapsingTextHelper;
    boolean counterEnabled;
    private int counterMaxLength;
    private final int counterOverflowTextAppearance;
    private boolean counterOverflowed;
    private final int counterTextAppearance;
    private TextView counterView;
    private ColorStateList defaultHintTextColor;
    private final int defaultStrokeColor;
    private final int disabledColor;
    EditText editText;
    private Drawable editTextOriginalDrawable;
    private int focusedStrokeColor;
    private ColorStateList focusedTextColor;
    private boolean hasPasswordToggleTintList;
    private boolean hasPasswordToggleTintMode;
    private boolean hasReconstructedEditTextBackground;
    private CharSequence hint;
    private boolean hintAnimationEnabled;
    private boolean hintEnabled;
    private boolean hintExpanded;
    private final int hoveredStrokeColor;
    private boolean inDrawableStateChanged;
    private final IndicatorViewController indicatorViewController = new IndicatorViewController(this);
    private final FrameLayout inputFrame;
    private boolean isProvidingHint;
    private Drawable originalEditTextEndDrawable;
    private CharSequence originalHint;
    private CharSequence passwordToggleContentDesc;
    private Drawable passwordToggleDrawable;
    private Drawable passwordToggleDummyDrawable;
    private boolean passwordToggleEnabled;
    private ColorStateList passwordToggleTintList;
    private PorterDuff.Mode passwordToggleTintMode;
    private CheckableImageButton passwordToggleView;
    private boolean passwordToggledVisible;
    private boolean restoringSavedState;
    private final Rect tmpRect = new Rect();
    private final RectF tmpRectF = new RectF();
    private Typeface typeface;

    public TextInputLayout(Context context) {
        this(context, null);
    }

    public TextInputLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.textInputStyle);
    }

    public TextInputLayout(Context object, AttributeSet object2, int n) {
        super(object, (AttributeSet)object2, n);
        FrameLayout frameLayout;
        CollapsingTextHelper collapsingTextHelper;
        this.collapsingTextHelper = collapsingTextHelper = new CollapsingTextHelper((View)this);
        this.setOrientation(1);
        this.setWillNotDraw(false);
        this.setAddStatesFromChildren(true);
        this.inputFrame = frameLayout = new FrameLayout(object);
        frameLayout.setAddStatesFromChildren(true);
        this.addView((View)frameLayout);
        collapsingTextHelper.setTextSizeInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        collapsingTextHelper.setPositionInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        collapsingTextHelper.setCollapsedTextGravity(0x800033);
        object2 = ThemeEnforcement.obtainTintedStyledAttributes(object, (AttributeSet)object2, R.styleable.TextInputLayout, n, R.style.Widget_Design_TextInputLayout, new int[0]);
        this.hintEnabled = ((TintTypedArray)object2).getBoolean(R.styleable.TextInputLayout_hintEnabled, true);
        this.setHint(((TintTypedArray)object2).getText(R.styleable.TextInputLayout_android_hint));
        this.hintAnimationEnabled = ((TintTypedArray)object2).getBoolean(R.styleable.TextInputLayout_hintAnimationEnabled, true);
        this.boxBottomOffsetPx = object.getResources().getDimensionPixelOffset(R.dimen.mtrl_textinput_box_bottom_offset);
        this.boxLabelCutoutPaddingPx = object.getResources().getDimensionPixelOffset(R.dimen.mtrl_textinput_box_label_cutout_padding);
        this.boxCollapsedPaddingTopPx = ((TintTypedArray)object2).getDimensionPixelOffset(R.styleable.TextInputLayout_boxCollapsedPaddingTop, 0);
        this.boxCornerRadiusTopStart = ((TintTypedArray)object2).getDimension(R.styleable.TextInputLayout_boxCornerRadiusTopStart, 0.0f);
        this.boxCornerRadiusTopEnd = ((TintTypedArray)object2).getDimension(R.styleable.TextInputLayout_boxCornerRadiusTopEnd, 0.0f);
        this.boxCornerRadiusBottomEnd = ((TintTypedArray)object2).getDimension(R.styleable.TextInputLayout_boxCornerRadiusBottomEnd, 0.0f);
        this.boxCornerRadiusBottomStart = ((TintTypedArray)object2).getDimension(R.styleable.TextInputLayout_boxCornerRadiusBottomStart, 0.0f);
        this.boxBackgroundColor = ((TintTypedArray)object2).getColor(R.styleable.TextInputLayout_boxBackgroundColor, 0);
        this.focusedStrokeColor = ((TintTypedArray)object2).getColor(R.styleable.TextInputLayout_boxStrokeColor, 0);
        this.boxStrokeWidthDefaultPx = n = object.getResources().getDimensionPixelSize(R.dimen.mtrl_textinput_box_stroke_width_default);
        this.boxStrokeWidthFocusedPx = object.getResources().getDimensionPixelSize(R.dimen.mtrl_textinput_box_stroke_width_focused);
        this.boxStrokeWidthPx = n;
        this.setBoxBackgroundMode(((TintTypedArray)object2).getInt(R.styleable.TextInputLayout_boxBackgroundMode, 0));
        if (((TintTypedArray)object2).hasValue(R.styleable.TextInputLayout_android_textColorHint)) {
            frameLayout = ((TintTypedArray)object2).getColorStateList(R.styleable.TextInputLayout_android_textColorHint);
            this.focusedTextColor = frameLayout;
            this.defaultHintTextColor = frameLayout;
        }
        this.defaultStrokeColor = ContextCompat.getColor(object, R.color.mtrl_textinput_default_box_stroke_color);
        this.disabledColor = ContextCompat.getColor(object, R.color.mtrl_textinput_disabled_color);
        this.hoveredStrokeColor = ContextCompat.getColor(object, R.color.mtrl_textinput_hovered_box_stroke_color);
        if (((TintTypedArray)object2).getResourceId(R.styleable.TextInputLayout_hintTextAppearance, -1) != -1) {
            this.setHintTextAppearance(((TintTypedArray)object2).getResourceId(R.styleable.TextInputLayout_hintTextAppearance, 0));
        }
        int n2 = ((TintTypedArray)object2).getResourceId(R.styleable.TextInputLayout_errorTextAppearance, 0);
        boolean bl = ((TintTypedArray)object2).getBoolean(R.styleable.TextInputLayout_errorEnabled, false);
        n = ((TintTypedArray)object2).getResourceId(R.styleable.TextInputLayout_helperTextTextAppearance, 0);
        boolean bl2 = ((TintTypedArray)object2).getBoolean(R.styleable.TextInputLayout_helperTextEnabled, false);
        object = ((TintTypedArray)object2).getText(R.styleable.TextInputLayout_helperText);
        boolean bl3 = ((TintTypedArray)object2).getBoolean(R.styleable.TextInputLayout_counterEnabled, false);
        this.setCounterMaxLength(((TintTypedArray)object2).getInt(R.styleable.TextInputLayout_counterMaxLength, -1));
        this.counterTextAppearance = ((TintTypedArray)object2).getResourceId(R.styleable.TextInputLayout_counterTextAppearance, 0);
        this.counterOverflowTextAppearance = ((TintTypedArray)object2).getResourceId(R.styleable.TextInputLayout_counterOverflowTextAppearance, 0);
        this.passwordToggleEnabled = ((TintTypedArray)object2).getBoolean(R.styleable.TextInputLayout_passwordToggleEnabled, false);
        this.passwordToggleDrawable = ((TintTypedArray)object2).getDrawable(R.styleable.TextInputLayout_passwordToggleDrawable);
        this.passwordToggleContentDesc = ((TintTypedArray)object2).getText(R.styleable.TextInputLayout_passwordToggleContentDescription);
        if (((TintTypedArray)object2).hasValue(R.styleable.TextInputLayout_passwordToggleTint)) {
            this.hasPasswordToggleTintList = true;
            this.passwordToggleTintList = ((TintTypedArray)object2).getColorStateList(R.styleable.TextInputLayout_passwordToggleTint);
        }
        if (((TintTypedArray)object2).hasValue(R.styleable.TextInputLayout_passwordToggleTintMode)) {
            this.hasPasswordToggleTintMode = true;
            this.passwordToggleTintMode = ViewUtils.parseTintMode(((TintTypedArray)object2).getInt(R.styleable.TextInputLayout_passwordToggleTintMode, -1), null);
        }
        ((TintTypedArray)object2).recycle();
        this.setHelperTextEnabled(bl2);
        this.setHelperText((CharSequence)object);
        this.setHelperTextTextAppearance(n);
        this.setErrorEnabled(bl);
        this.setErrorTextAppearance(n2);
        this.setCounterEnabled(bl3);
        this.applyPasswordToggleTint();
        ViewCompat.setImportantForAccessibility((View)this, 2);
    }

    private void applyBoxAttributes() {
        int n;
        int n2;
        EditText editText;
        if (this.boxBackground == null) {
            return;
        }
        this.setBoxAttributes();
        EditText editText2 = this.editText;
        if (editText2 != null && this.boxBackgroundMode == 2) {
            if (editText2.getBackground() != null) {
                this.editTextOriginalDrawable = this.editText.getBackground();
            }
            ViewCompat.setBackground((View)this.editText, null);
        }
        if ((editText = this.editText) != null && this.boxBackgroundMode == 1 && (editText2 = this.editTextOriginalDrawable) != null) {
            ViewCompat.setBackground((View)editText, (Drawable)editText2);
        }
        if ((n2 = this.boxStrokeWidthPx) > -1 && (n = this.boxStrokeColor) != 0) {
            this.boxBackground.setStroke(n2, n);
        }
        this.boxBackground.setCornerRadii(this.getCornerRadiiAsArray());
        this.boxBackground.setColor(this.boxBackgroundColor);
        this.invalidate();
    }

    private void applyCutoutPadding(RectF rectF) {
        rectF.left -= (float)this.boxLabelCutoutPaddingPx;
        rectF.top -= (float)this.boxLabelCutoutPaddingPx;
        rectF.right += (float)this.boxLabelCutoutPaddingPx;
        rectF.bottom += (float)this.boxLabelCutoutPaddingPx;
    }

    private void applyPasswordToggleTint() {
        Object object = this.passwordToggleDrawable;
        if (object != null && (this.hasPasswordToggleTintList || this.hasPasswordToggleTintMode)) {
            Drawable drawable2;
            this.passwordToggleDrawable = object = DrawableCompat.wrap(object).mutate();
            if (this.hasPasswordToggleTintList) {
                DrawableCompat.setTintList(object, this.passwordToggleTintList);
            }
            if (this.hasPasswordToggleTintMode) {
                DrawableCompat.setTintMode(this.passwordToggleDrawable, this.passwordToggleTintMode);
            }
            if ((object = this.passwordToggleView) != null && (object = object.getDrawable()) != (drawable2 = this.passwordToggleDrawable)) {
                this.passwordToggleView.setImageDrawable(drawable2);
            }
        }
    }

    private void assignBoxBackgroundByMode() {
        int n = this.boxBackgroundMode;
        if (n == 0) {
            this.boxBackground = null;
        } else if (n == 2 && this.hintEnabled && !(this.boxBackground instanceof CutoutDrawable)) {
            this.boxBackground = new CutoutDrawable();
        } else if (!(this.boxBackground instanceof GradientDrawable)) {
            this.boxBackground = new GradientDrawable();
        }
    }

    private int calculateBoxBackgroundTop() {
        EditText editText = this.editText;
        if (editText == null) {
            return 0;
        }
        switch (this.boxBackgroundMode) {
            default: {
                return 0;
            }
            case 2: {
                return editText.getTop() + this.calculateLabelMarginTop();
            }
            case 1: 
        }
        return editText.getTop();
    }

    private int calculateCollapsedTextTopBounds() {
        switch (this.boxBackgroundMode) {
            default: {
                return this.getPaddingTop();
            }
            case 2: {
                return this.getBoxBackground().getBounds().top - this.calculateLabelMarginTop();
            }
            case 1: 
        }
        return this.getBoxBackground().getBounds().top + this.boxCollapsedPaddingTopPx;
    }

    private int calculateLabelMarginTop() {
        if (!this.hintEnabled) {
            return 0;
        }
        switch (this.boxBackgroundMode) {
            default: {
                return 0;
            }
            case 2: {
                return (int)(this.collapsingTextHelper.getCollapsedTextHeight() / 2.0f);
            }
            case 0: 
            case 1: 
        }
        return (int)this.collapsingTextHelper.getCollapsedTextHeight();
    }

    private void closeCutout() {
        if (this.cutoutEnabled()) {
            ((CutoutDrawable)this.boxBackground).removeCutout();
        }
    }

    private void collapseHint(boolean bl) {
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.animator.cancel();
        }
        if (bl && this.hintAnimationEnabled) {
            this.animateToExpansionFraction(1.0f);
        } else {
            this.collapsingTextHelper.setExpansionFraction(1.0f);
        }
        this.hintExpanded = false;
        if (this.cutoutEnabled()) {
            this.openCutout();
        }
    }

    private boolean cutoutEnabled() {
        boolean bl = this.hintEnabled && !TextUtils.isEmpty((CharSequence)this.hint) && this.boxBackground instanceof CutoutDrawable;
        return bl;
    }

    private void ensureBackgroundDrawableStateWorkaround() {
        int n = Build.VERSION.SDK_INT;
        if (n != 21 && n != 22) {
            return;
        }
        Drawable drawable2 = this.editText.getBackground();
        if (drawable2 == null) {
            return;
        }
        if (!this.hasReconstructedEditTextBackground) {
            Drawable drawable3 = drawable2.getConstantState().newDrawable();
            if (drawable2 instanceof DrawableContainer) {
                this.hasReconstructedEditTextBackground = com.google.android.material.internal.DrawableUtils.setContainerConstantState((DrawableContainer)drawable2, drawable3.getConstantState());
            }
            if (!this.hasReconstructedEditTextBackground) {
                ViewCompat.setBackground((View)this.editText, drawable3);
                this.hasReconstructedEditTextBackground = true;
                this.onApplyBoxBackgroundMode();
            }
        }
    }

    private void expandHint(boolean bl) {
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.animator.cancel();
        }
        if (bl && this.hintAnimationEnabled) {
            this.animateToExpansionFraction(0.0f);
        } else {
            this.collapsingTextHelper.setExpansionFraction(0.0f);
        }
        if (this.cutoutEnabled() && ((CutoutDrawable)this.boxBackground).hasCutout()) {
            this.closeCutout();
        }
        this.hintExpanded = true;
    }

    private Drawable getBoxBackground() {
        int n = this.boxBackgroundMode;
        if (n != 1 && n != 2) {
            throw new IllegalStateException();
        }
        return this.boxBackground;
    }

    private float[] getCornerRadiiAsArray() {
        if (!ViewUtils.isLayoutRtl((View)this)) {
            float f = this.boxCornerRadiusTopStart;
            float f2 = this.boxCornerRadiusTopEnd;
            float f3 = this.boxCornerRadiusBottomEnd;
            float f4 = this.boxCornerRadiusBottomStart;
            return new float[]{f, f, f2, f2, f3, f3, f4, f4};
        }
        float f = this.boxCornerRadiusTopEnd;
        float f5 = this.boxCornerRadiusTopStart;
        float f6 = this.boxCornerRadiusBottomStart;
        float f7 = this.boxCornerRadiusBottomEnd;
        return new float[]{f, f, f5, f5, f6, f6, f7, f7};
    }

    private boolean hasPasswordTransformation() {
        EditText editText = this.editText;
        boolean bl = editText != null && editText.getTransformationMethod() instanceof PasswordTransformationMethod;
        return bl;
    }

    private void onApplyBoxBackgroundMode() {
        this.assignBoxBackgroundByMode();
        if (this.boxBackgroundMode != 0) {
            this.updateInputLayoutMargins();
        }
        this.updateTextInputBoxBounds();
    }

    private void openCutout() {
        if (!this.cutoutEnabled()) {
            return;
        }
        RectF rectF = this.tmpRectF;
        this.collapsingTextHelper.getCollapsedTextActualBounds(rectF);
        this.applyCutoutPadding(rectF);
        ((CutoutDrawable)this.boxBackground).setCutout(rectF);
    }

    private static void recursiveSetEnabled(ViewGroup viewGroup, boolean bl) {
        int n = viewGroup.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(bl);
            if (!(view instanceof ViewGroup)) continue;
            TextInputLayout.recursiveSetEnabled((ViewGroup)view, bl);
        }
    }

    private void setBoxAttributes() {
        switch (this.boxBackgroundMode) {
            default: {
                break;
            }
            case 2: {
                if (this.focusedStrokeColor != 0) break;
                this.focusedStrokeColor = this.focusedTextColor.getColorForState(this.getDrawableState(), this.focusedTextColor.getDefaultColor());
                break;
            }
            case 1: {
                this.boxStrokeWidthPx = 0;
            }
        }
    }

    private void setEditText(EditText object) {
        if (this.editText == null) {
            if (!(object instanceof TextInputEditText)) {
                Log.i((String)LOG_TAG, (String)"EditText added is not a TextInputEditText. Please switch to using that class instead.");
            }
            this.editText = object;
            this.onApplyBoxBackgroundMode();
            this.setTextInputAccessibilityDelegate(new AccessibilityDelegate(this));
            if (!this.hasPasswordTransformation()) {
                this.collapsingTextHelper.setTypefaces(this.editText.getTypeface());
            }
            this.collapsingTextHelper.setExpandedTextSize(this.editText.getTextSize());
            int n = this.editText.getGravity();
            this.collapsingTextHelper.setCollapsedTextGravity(n & 0xFFFFFF8F | 0x30);
            this.collapsingTextHelper.setExpandedTextGravity(n);
            this.editText.addTextChangedListener(new TextWatcher(this){
                final TextInputLayout this$0;
                {
                    this.this$0 = textInputLayout;
                }

                public void afterTextChanged(Editable editable) {
                    TextInputLayout textInputLayout = this.this$0;
                    textInputLayout.updateLabelState(textInputLayout.restoringSavedState ^ true);
                    if (this.this$0.counterEnabled) {
                        this.this$0.updateCounter(editable.length());
                    }
                }

                public void beforeTextChanged(CharSequence charSequence, int n, int n2, int n3) {
                }

                public void onTextChanged(CharSequence charSequence, int n, int n2, int n3) {
                }
            });
            if (this.defaultHintTextColor == null) {
                this.defaultHintTextColor = this.editText.getHintTextColors();
            }
            if (this.hintEnabled) {
                if (TextUtils.isEmpty((CharSequence)this.hint)) {
                    object = this.editText.getHint();
                    this.originalHint = object;
                    this.setHint((CharSequence)object);
                    this.editText.setHint(null);
                }
                this.isProvidingHint = true;
            }
            if (this.counterView != null) {
                this.updateCounter(this.editText.getText().length());
            }
            this.indicatorViewController.adjustIndicatorPadding();
            this.updatePasswordToggleView();
            this.updateLabelState(false, true);
            return;
        }
        throw new IllegalArgumentException("We already have an EditText, can only have one");
    }

    private void setHintInternal(CharSequence charSequence) {
        if (!TextUtils.equals((CharSequence)charSequence, (CharSequence)this.hint)) {
            this.hint = charSequence;
            this.collapsingTextHelper.setText(charSequence);
            if (!this.hintExpanded) {
                this.openCutout();
            }
        }
    }

    private boolean shouldShowPasswordIcon() {
        boolean bl = this.passwordToggleEnabled && (this.hasPasswordTransformation() || this.passwordToggledVisible);
        return bl;
    }

    private void updateEditTextBackgroundBounds() {
        EditText editText = this.editText;
        if (editText == null) {
            return;
        }
        Drawable drawable2 = editText.getBackground();
        if (drawable2 == null) {
            return;
        }
        editText = drawable2;
        if (DrawableUtils.canSafelyMutateDrawable(drawable2)) {
            editText = drawable2.mutate();
        }
        drawable2 = new Rect();
        DescendantOffsetUtils.getDescendantRect((ViewGroup)this, (View)this.editText, (Rect)drawable2);
        drawable2 = editText.getBounds();
        if (drawable2.left != drawable2.right) {
            Rect rect = new Rect();
            editText.getPadding(rect);
            int n = drawable2.left;
            int n2 = rect.left;
            int n3 = drawable2.right;
            int n4 = rect.right;
            editText.setBounds(n - n2, drawable2.top, n3 + n4 * 2, this.editText.getBottom());
        }
    }

    private void updateInputLayoutMargins() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)this.inputFrame.getLayoutParams();
        int n = this.calculateLabelMarginTop();
        if (n != layoutParams.topMargin) {
            layoutParams.topMargin = n;
            this.inputFrame.requestLayout();
        }
    }

    private void updateLabelState(boolean bl, boolean bl2) {
        boolean bl3 = this.isEnabled();
        EditText editText = this.editText;
        boolean bl4 = true;
        boolean bl5 = editText != null && !TextUtils.isEmpty((CharSequence)editText.getText());
        editText = this.editText;
        if (editText == null || !editText.hasFocus()) {
            bl4 = false;
        }
        boolean bl6 = this.indicatorViewController.errorShouldBeShown();
        editText = this.defaultHintTextColor;
        if (editText != null) {
            this.collapsingTextHelper.setCollapsedTextColor((ColorStateList)editText);
            this.collapsingTextHelper.setExpandedTextColor(this.defaultHintTextColor);
        }
        if (!bl3) {
            this.collapsingTextHelper.setCollapsedTextColor(ColorStateList.valueOf((int)this.disabledColor));
            this.collapsingTextHelper.setExpandedTextColor(ColorStateList.valueOf((int)this.disabledColor));
        } else if (bl6) {
            this.collapsingTextHelper.setCollapsedTextColor(this.indicatorViewController.getErrorViewTextColors());
        } else if (this.counterOverflowed && (editText = this.counterView) != null) {
            this.collapsingTextHelper.setCollapsedTextColor(editText.getTextColors());
        } else if (bl4 && (editText = this.focusedTextColor) != null) {
            this.collapsingTextHelper.setCollapsedTextColor((ColorStateList)editText);
        }
        if (!(bl5 || this.isEnabled() && (bl4 || bl6))) {
            if (bl2 || !this.hintExpanded) {
                this.expandHint(bl);
            }
        } else if (bl2 || this.hintExpanded) {
            this.collapseHint(bl);
        }
    }

    private void updatePasswordToggleView() {
        if (this.editText == null) {
            return;
        }
        if (this.shouldShowPasswordIcon()) {
            Object object;
            if (this.passwordToggleView == null) {
                object = (CheckableImageButton)LayoutInflater.from((Context)this.getContext()).inflate(R.layout.design_text_input_password_icon, (ViewGroup)this.inputFrame, false);
                this.passwordToggleView = object;
                ((AppCompatImageButton)object).setImageDrawable(this.passwordToggleDrawable);
                this.passwordToggleView.setContentDescription(this.passwordToggleContentDesc);
                this.inputFrame.addView((View)this.passwordToggleView);
                this.passwordToggleView.setOnClickListener(new View.OnClickListener(this){
                    final TextInputLayout this$0;
                    {
                        this.this$0 = textInputLayout;
                    }

                    public void onClick(View view) {
                        this.this$0.passwordVisibilityToggleRequested(false);
                    }
                });
            }
            if ((object = this.editText) != null && ViewCompat.getMinimumHeight((View)object) <= 0) {
                this.editText.setMinimumHeight(ViewCompat.getMinimumHeight((View)this.passwordToggleView));
            }
            this.passwordToggleView.setVisibility(0);
            this.passwordToggleView.setChecked(this.passwordToggledVisible);
            if (this.passwordToggleDummyDrawable == null) {
                this.passwordToggleDummyDrawable = new ColorDrawable();
            }
            this.passwordToggleDummyDrawable.setBounds(0, 0, this.passwordToggleView.getMeasuredWidth(), 1);
            Drawable[] drawableArray = TextViewCompat.getCompoundDrawablesRelative((TextView)this.editText);
            Drawable drawable2 = drawableArray[2];
            object = this.passwordToggleDummyDrawable;
            if (drawable2 != object) {
                this.originalEditTextEndDrawable = drawableArray[2];
            }
            TextViewCompat.setCompoundDrawablesRelative((TextView)this.editText, drawableArray[0], drawableArray[1], (Drawable)object, drawableArray[3]);
            this.passwordToggleView.setPadding(this.editText.getPaddingLeft(), this.editText.getPaddingTop(), this.editText.getPaddingRight(), this.editText.getPaddingBottom());
        } else {
            Drawable[] drawableArray = this.passwordToggleView;
            if (drawableArray != null && drawableArray.getVisibility() == 0) {
                this.passwordToggleView.setVisibility(8);
            }
            if (this.passwordToggleDummyDrawable != null && (drawableArray = TextViewCompat.getCompoundDrawablesRelative((TextView)this.editText))[2] == this.passwordToggleDummyDrawable) {
                TextViewCompat.setCompoundDrawablesRelative((TextView)this.editText, drawableArray[0], drawableArray[1], this.originalEditTextEndDrawable, drawableArray[3]);
                this.passwordToggleDummyDrawable = null;
            }
        }
    }

    private void updateTextInputBoxBounds() {
        if (this.boxBackgroundMode != 0 && this.boxBackground != null && this.editText != null && this.getRight() != 0) {
            int n = this.editText.getLeft();
            int n2 = this.calculateBoxBackgroundTop();
            int n3 = this.editText.getRight();
            int n4 = this.editText.getBottom() + this.boxBottomOffsetPx;
            int n5 = n;
            int n6 = n2;
            int n7 = n3;
            int n8 = n4;
            if (this.boxBackgroundMode == 2) {
                n8 = this.boxStrokeWidthFocusedPx;
                n5 = n + n8 / 2;
                n6 = n2 - n8 / 2;
                n7 = n3 - n8 / 2;
                n8 = n4 + n8 / 2;
            }
            this.boxBackground.setBounds(n5, n6, n7, n8);
            this.applyBoxAttributes();
            this.updateEditTextBackgroundBounds();
            return;
        }
    }

    public void addView(View view, int n, ViewGroup.LayoutParams layoutParams) {
        if (view instanceof EditText) {
            FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(layoutParams);
            layoutParams2.gravity = layoutParams2.gravity & 0xFFFFFF8F | 0x10;
            this.inputFrame.addView(view, (ViewGroup.LayoutParams)layoutParams2);
            this.inputFrame.setLayoutParams(layoutParams);
            this.updateInputLayoutMargins();
            this.setEditText((EditText)view);
        } else {
            super.addView(view, n, layoutParams);
        }
    }

    void animateToExpansionFraction(float f) {
        if (this.collapsingTextHelper.getExpansionFraction() == f) {
            return;
        }
        if (this.animator == null) {
            ValueAnimator valueAnimator;
            this.animator = valueAnimator = new ValueAnimator();
            valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            this.animator.setDuration(167L);
            this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this){
                final TextInputLayout this$0;
                {
                    this.this$0 = textInputLayout;
                }

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    this.this$0.collapsingTextHelper.setExpansionFraction(((Float)valueAnimator.getAnimatedValue()).floatValue());
                }
            });
        }
        this.animator.setFloatValues(new float[]{this.collapsingTextHelper.getExpansionFraction(), f});
        this.animator.start();
    }

    boolean cutoutIsOpen() {
        boolean bl = this.cutoutEnabled() && ((CutoutDrawable)this.boxBackground).hasCutout();
        return bl;
    }

    public void dispatchProvideAutofillStructure(ViewStructure viewStructure, int n) {
        Object object;
        if (this.originalHint != null && (object = this.editText) != null) {
            boolean bl = this.isProvidingHint;
            this.isProvidingHint = false;
            object = object.getHint();
            this.editText.setHint(this.originalHint);
            try {
                super.dispatchProvideAutofillStructure(viewStructure, n);
                return;
            }
            finally {
                this.editText.setHint((CharSequence)object);
                this.isProvidingHint = bl;
            }
        }
        super.dispatchProvideAutofillStructure(viewStructure, n);
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        this.restoringSavedState = true;
        super.dispatchRestoreInstanceState(sparseArray);
        this.restoringSavedState = false;
    }

    public void draw(Canvas canvas) {
        GradientDrawable gradientDrawable = this.boxBackground;
        if (gradientDrawable != null) {
            gradientDrawable.draw(canvas);
        }
        super.draw(canvas);
        if (this.hintEnabled) {
            this.collapsingTextHelper.draw(canvas);
        }
    }

    protected void drawableStateChanged() {
        if (this.inDrawableStateChanged) {
            return;
        }
        boolean bl = true;
        this.inDrawableStateChanged = true;
        super.drawableStateChanged();
        int[] nArray = this.getDrawableState();
        boolean bl2 = false;
        if (!ViewCompat.isLaidOut((View)this) || !this.isEnabled()) {
            bl = false;
        }
        this.updateLabelState(bl);
        this.updateEditTextBackground();
        this.updateTextInputBoxBounds();
        this.updateTextInputBoxState();
        CollapsingTextHelper collapsingTextHelper = this.collapsingTextHelper;
        if (collapsingTextHelper != null) {
            bl2 = false | collapsingTextHelper.setState(nArray);
        }
        if (bl2) {
            this.invalidate();
        }
        this.inDrawableStateChanged = false;
    }

    public int getBoxBackgroundColor() {
        return this.boxBackgroundColor;
    }

    public float getBoxCornerRadiusBottomEnd() {
        return this.boxCornerRadiusBottomEnd;
    }

    public float getBoxCornerRadiusBottomStart() {
        return this.boxCornerRadiusBottomStart;
    }

    public float getBoxCornerRadiusTopEnd() {
        return this.boxCornerRadiusTopEnd;
    }

    public float getBoxCornerRadiusTopStart() {
        return this.boxCornerRadiusTopStart;
    }

    public int getBoxStrokeColor() {
        return this.focusedStrokeColor;
    }

    public int getCounterMaxLength() {
        return this.counterMaxLength;
    }

    CharSequence getCounterOverflowDescription() {
        TextView textView;
        if (this.counterEnabled && this.counterOverflowed && (textView = this.counterView) != null) {
            return textView.getContentDescription();
        }
        return null;
    }

    public ColorStateList getDefaultHintTextColor() {
        return this.defaultHintTextColor;
    }

    public EditText getEditText() {
        return this.editText;
    }

    public CharSequence getError() {
        CharSequence charSequence = this.indicatorViewController.isErrorEnabled() ? this.indicatorViewController.getErrorText() : null;
        return charSequence;
    }

    public int getErrorCurrentTextColors() {
        return this.indicatorViewController.getErrorViewCurrentTextColor();
    }

    final int getErrorTextCurrentColor() {
        return this.indicatorViewController.getErrorViewCurrentTextColor();
    }

    public CharSequence getHelperText() {
        CharSequence charSequence = this.indicatorViewController.isHelperTextEnabled() ? this.indicatorViewController.getHelperText() : null;
        return charSequence;
    }

    public int getHelperTextCurrentTextColor() {
        return this.indicatorViewController.getHelperTextViewCurrentTextColor();
    }

    public CharSequence getHint() {
        CharSequence charSequence = this.hintEnabled ? this.hint : null;
        return charSequence;
    }

    final float getHintCollapsedTextHeight() {
        return this.collapsingTextHelper.getCollapsedTextHeight();
    }

    final int getHintCurrentCollapsedTextColor() {
        return this.collapsingTextHelper.getCurrentCollapsedTextColor();
    }

    public CharSequence getPasswordVisibilityToggleContentDescription() {
        return this.passwordToggleContentDesc;
    }

    public Drawable getPasswordVisibilityToggleDrawable() {
        return this.passwordToggleDrawable;
    }

    public Typeface getTypeface() {
        return this.typeface;
    }

    public boolean isCounterEnabled() {
        return this.counterEnabled;
    }

    public boolean isErrorEnabled() {
        return this.indicatorViewController.isErrorEnabled();
    }

    final boolean isHelperTextDisplayed() {
        return this.indicatorViewController.helperTextIsDisplayed();
    }

    public boolean isHelperTextEnabled() {
        return this.indicatorViewController.isHelperTextEnabled();
    }

    public boolean isHintAnimationEnabled() {
        return this.hintAnimationEnabled;
    }

    public boolean isHintEnabled() {
        return this.hintEnabled;
    }

    final boolean isHintExpanded() {
        return this.hintExpanded;
    }

    public boolean isPasswordVisibilityToggleEnabled() {
        return this.passwordToggleEnabled;
    }

    boolean isProvidingHint() {
        return this.isProvidingHint;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        EditText editText;
        super.onLayout(bl, n, n2, n3, n4);
        if (this.boxBackground != null) {
            this.updateTextInputBoxBounds();
        }
        if (this.hintEnabled && (editText = this.editText) != null) {
            Rect rect = this.tmpRect;
            DescendantOffsetUtils.getDescendantRect((ViewGroup)this, (View)editText, rect);
            n = rect.left + this.editText.getCompoundPaddingLeft();
            int n5 = rect.right - this.editText.getCompoundPaddingRight();
            n3 = this.calculateCollapsedTextTopBounds();
            this.collapsingTextHelper.setExpandedBounds(n, rect.top + this.editText.getCompoundPaddingTop(), n5, rect.bottom - this.editText.getCompoundPaddingBottom());
            this.collapsingTextHelper.setCollapsedBounds(n, n3, n5, n4 - n2 - this.getPaddingBottom());
            this.collapsingTextHelper.recalculate();
            if (this.cutoutEnabled() && !this.hintExpanded) {
                this.openCutout();
            }
        }
    }

    protected void onMeasure(int n, int n2) {
        this.updatePasswordToggleView();
        super.onMeasure(n, n2);
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        this.setError(parcelable.error);
        if (parcelable.isPasswordToggledVisible) {
            this.passwordVisibilityToggleRequested(true);
        }
        this.requestLayout();
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        if (this.indicatorViewController.errorShouldBeShown()) {
            savedState.error = this.getError();
        }
        savedState.isPasswordToggledVisible = this.passwordToggledVisible;
        return savedState;
    }

    public void passwordVisibilityToggleRequested(boolean bl) {
        if (this.passwordToggleEnabled) {
            int n = this.editText.getSelectionEnd();
            if (this.hasPasswordTransformation()) {
                this.editText.setTransformationMethod(null);
                this.passwordToggledVisible = true;
            } else {
                this.editText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
                this.passwordToggledVisible = false;
            }
            this.passwordToggleView.setChecked(this.passwordToggledVisible);
            if (bl) {
                this.passwordToggleView.jumpDrawablesToCurrentState();
            }
            this.editText.setSelection(n);
        }
    }

    public void setBoxBackgroundColor(int n) {
        if (this.boxBackgroundColor != n) {
            this.boxBackgroundColor = n;
            this.applyBoxAttributes();
        }
    }

    public void setBoxBackgroundColorResource(int n) {
        this.setBoxBackgroundColor(ContextCompat.getColor(this.getContext(), n));
    }

    public void setBoxBackgroundMode(int n) {
        if (n == this.boxBackgroundMode) {
            return;
        }
        this.boxBackgroundMode = n;
        this.onApplyBoxBackgroundMode();
    }

    public void setBoxCornerRadii(float f, float f2, float f3, float f4) {
        if (this.boxCornerRadiusTopStart != f || this.boxCornerRadiusTopEnd != f2 || this.boxCornerRadiusBottomEnd != f4 || this.boxCornerRadiusBottomStart != f3) {
            this.boxCornerRadiusTopStart = f;
            this.boxCornerRadiusTopEnd = f2;
            this.boxCornerRadiusBottomEnd = f4;
            this.boxCornerRadiusBottomStart = f3;
            this.applyBoxAttributes();
        }
    }

    public void setBoxCornerRadiiResources(int n, int n2, int n3, int n4) {
        this.setBoxCornerRadii(this.getContext().getResources().getDimension(n), this.getContext().getResources().getDimension(n2), this.getContext().getResources().getDimension(n3), this.getContext().getResources().getDimension(n4));
    }

    public void setBoxStrokeColor(int n) {
        if (this.focusedStrokeColor != n) {
            this.focusedStrokeColor = n;
            this.updateTextInputBoxState();
        }
    }

    public void setCounterEnabled(boolean bl) {
        if (this.counterEnabled != bl) {
            if (bl) {
                AppCompatTextView appCompatTextView = new AppCompatTextView(this.getContext());
                this.counterView = appCompatTextView;
                appCompatTextView.setId(R.id.textinput_counter);
                appCompatTextView = this.typeface;
                if (appCompatTextView != null) {
                    this.counterView.setTypeface((Typeface)appCompatTextView);
                }
                this.counterView.setMaxLines(1);
                this.setTextAppearanceCompatWithErrorFallback(this.counterView, this.counterTextAppearance);
                this.indicatorViewController.addIndicator(this.counterView, 2);
                appCompatTextView = this.editText;
                if (appCompatTextView == null) {
                    this.updateCounter(0);
                } else {
                    this.updateCounter(appCompatTextView.getText().length());
                }
            } else {
                this.indicatorViewController.removeIndicator(this.counterView, 2);
                this.counterView = null;
            }
            this.counterEnabled = bl;
        }
    }

    public void setCounterMaxLength(int n) {
        if (this.counterMaxLength != n) {
            this.counterMaxLength = n > 0 ? n : -1;
            if (this.counterEnabled) {
                EditText editText = this.editText;
                n = editText == null ? 0 : editText.getText().length();
                this.updateCounter(n);
            }
        }
    }

    public void setDefaultHintTextColor(ColorStateList colorStateList) {
        this.defaultHintTextColor = colorStateList;
        this.focusedTextColor = colorStateList;
        if (this.editText != null) {
            this.updateLabelState(false);
        }
    }

    public void setEnabled(boolean bl) {
        TextInputLayout.recursiveSetEnabled((ViewGroup)this, bl);
        super.setEnabled(bl);
    }

    public void setError(CharSequence charSequence) {
        if (!this.indicatorViewController.isErrorEnabled()) {
            if (TextUtils.isEmpty((CharSequence)charSequence)) {
                return;
            }
            this.setErrorEnabled(true);
        }
        if (!TextUtils.isEmpty((CharSequence)charSequence)) {
            this.indicatorViewController.showError(charSequence);
        } else {
            this.indicatorViewController.hideError();
        }
    }

    public void setErrorEnabled(boolean bl) {
        this.indicatorViewController.setErrorEnabled(bl);
    }

    public void setErrorTextAppearance(int n) {
        this.indicatorViewController.setErrorTextAppearance(n);
    }

    public void setErrorTextColor(ColorStateList colorStateList) {
        this.indicatorViewController.setErrorViewTextColor(colorStateList);
    }

    public void setHelperText(CharSequence charSequence) {
        if (TextUtils.isEmpty((CharSequence)charSequence)) {
            if (this.isHelperTextEnabled()) {
                this.setHelperTextEnabled(false);
            }
        } else {
            if (!this.isHelperTextEnabled()) {
                this.setHelperTextEnabled(true);
            }
            this.indicatorViewController.showHelper(charSequence);
        }
    }

    public void setHelperTextColor(ColorStateList colorStateList) {
        this.indicatorViewController.setHelperTextViewTextColor(colorStateList);
    }

    public void setHelperTextEnabled(boolean bl) {
        this.indicatorViewController.setHelperTextEnabled(bl);
    }

    public void setHelperTextTextAppearance(int n) {
        this.indicatorViewController.setHelperTextAppearance(n);
    }

    public void setHint(CharSequence charSequence) {
        if (this.hintEnabled) {
            this.setHintInternal(charSequence);
            this.sendAccessibilityEvent(2048);
        }
    }

    public void setHintAnimationEnabled(boolean bl) {
        this.hintAnimationEnabled = bl;
    }

    public void setHintEnabled(boolean bl) {
        if (bl != this.hintEnabled) {
            this.hintEnabled = bl;
            if (!bl) {
                this.isProvidingHint = false;
                if (!TextUtils.isEmpty((CharSequence)this.hint) && TextUtils.isEmpty((CharSequence)this.editText.getHint())) {
                    this.editText.setHint(this.hint);
                }
                this.setHintInternal(null);
            } else {
                CharSequence charSequence = this.editText.getHint();
                if (!TextUtils.isEmpty((CharSequence)charSequence)) {
                    if (TextUtils.isEmpty((CharSequence)this.hint)) {
                        this.setHint(charSequence);
                    }
                    this.editText.setHint(null);
                }
                this.isProvidingHint = true;
            }
            if (this.editText != null) {
                this.updateInputLayoutMargins();
            }
        }
    }

    public void setHintTextAppearance(int n) {
        this.collapsingTextHelper.setCollapsedTextAppearance(n);
        this.focusedTextColor = this.collapsingTextHelper.getCollapsedTextColor();
        if (this.editText != null) {
            this.updateLabelState(false);
            this.updateInputLayoutMargins();
        }
    }

    public void setPasswordVisibilityToggleContentDescription(int n) {
        CharSequence charSequence = n != 0 ? this.getResources().getText(n) : null;
        this.setPasswordVisibilityToggleContentDescription(charSequence);
    }

    public void setPasswordVisibilityToggleContentDescription(CharSequence charSequence) {
        this.passwordToggleContentDesc = charSequence;
        CheckableImageButton checkableImageButton = this.passwordToggleView;
        if (checkableImageButton != null) {
            checkableImageButton.setContentDescription(charSequence);
        }
    }

    public void setPasswordVisibilityToggleDrawable(int n) {
        Drawable drawable2 = n != 0 ? AppCompatResources.getDrawable(this.getContext(), n) : null;
        this.setPasswordVisibilityToggleDrawable(drawable2);
    }

    public void setPasswordVisibilityToggleDrawable(Drawable drawable2) {
        this.passwordToggleDrawable = drawable2;
        CheckableImageButton checkableImageButton = this.passwordToggleView;
        if (checkableImageButton != null) {
            checkableImageButton.setImageDrawable(drawable2);
        }
    }

    public void setPasswordVisibilityToggleEnabled(boolean bl) {
        if (this.passwordToggleEnabled != bl) {
            EditText editText;
            this.passwordToggleEnabled = bl;
            if (!bl && this.passwordToggledVisible && (editText = this.editText) != null) {
                editText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
            }
            this.passwordToggledVisible = false;
            this.updatePasswordToggleView();
        }
    }

    public void setPasswordVisibilityToggleTintList(ColorStateList colorStateList) {
        this.passwordToggleTintList = colorStateList;
        this.hasPasswordToggleTintList = true;
        this.applyPasswordToggleTint();
    }

    public void setPasswordVisibilityToggleTintMode(PorterDuff.Mode mode) {
        this.passwordToggleTintMode = mode;
        this.hasPasswordToggleTintMode = true;
        this.applyPasswordToggleTint();
    }

    void setTextAppearanceCompatWithErrorFallback(TextView textView, int n) {
        int n2 = 0;
        TextViewCompat.setTextAppearance(textView, n);
        n = n2;
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                int n3 = textView.getTextColors().getDefaultColor();
                n = n2;
                if (n3 == -65281) {
                    n = 1;
                }
            }
        }
        catch (Exception exception) {
            n = 1;
        }
        if (n != 0) {
            TextViewCompat.setTextAppearance(textView, R.style.TextAppearance_AppCompat_Caption);
            textView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.design_error));
        }
    }

    public void setTextInputAccessibilityDelegate(AccessibilityDelegate accessibilityDelegate) {
        EditText editText = this.editText;
        if (editText != null) {
            ViewCompat.setAccessibilityDelegate((View)editText, accessibilityDelegate);
        }
    }

    public void setTypeface(Typeface typeface) {
        if (typeface != this.typeface) {
            this.typeface = typeface;
            this.collapsingTextHelper.setTypefaces(typeface);
            this.indicatorViewController.setTypefaces(typeface);
            TextView textView = this.counterView;
            if (textView != null) {
                textView.setTypeface(typeface);
            }
        }
    }

    void updateCounter(int n) {
        boolean bl = this.counterOverflowed;
        if (this.counterMaxLength == -1) {
            this.counterView.setText((CharSequence)String.valueOf(n));
            this.counterView.setContentDescription(null);
            this.counterOverflowed = false;
        } else {
            if (ViewCompat.getAccessibilityLiveRegion((View)this.counterView) == 1) {
                ViewCompat.setAccessibilityLiveRegion((View)this.counterView, 0);
            }
            boolean bl2 = n > this.counterMaxLength;
            this.counterOverflowed = bl2;
            if (bl != bl2) {
                TextView textView = this.counterView;
                int n2 = bl2 ? this.counterOverflowTextAppearance : this.counterTextAppearance;
                this.setTextAppearanceCompatWithErrorFallback(textView, n2);
                if (this.counterOverflowed) {
                    ViewCompat.setAccessibilityLiveRegion((View)this.counterView, 1);
                }
            }
            this.counterView.setText((CharSequence)this.getContext().getString(R.string.character_counter_pattern, new Object[]{n, this.counterMaxLength}));
            this.counterView.setContentDescription((CharSequence)this.getContext().getString(R.string.character_counter_content_description, new Object[]{n, this.counterMaxLength}));
        }
        if (this.editText != null && bl != this.counterOverflowed) {
            this.updateLabelState(false);
            this.updateTextInputBoxState();
            this.updateEditTextBackground();
        }
    }

    void updateEditTextBackground() {
        EditText editText = this.editText;
        if (editText == null) {
            return;
        }
        Drawable drawable2 = editText.getBackground();
        if (drawable2 == null) {
            return;
        }
        this.ensureBackgroundDrawableStateWorkaround();
        editText = drawable2;
        if (DrawableUtils.canSafelyMutateDrawable(drawable2)) {
            editText = drawable2.mutate();
        }
        if (this.indicatorViewController.errorShouldBeShown()) {
            editText.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(this.indicatorViewController.getErrorViewCurrentTextColor(), PorterDuff.Mode.SRC_IN));
        } else if (this.counterOverflowed && (drawable2 = this.counterView) != null) {
            editText.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(drawable2.getCurrentTextColor(), PorterDuff.Mode.SRC_IN));
        } else {
            DrawableCompat.clearColorFilter((Drawable)editText);
            this.editText.refreshDrawableState();
        }
    }

    void updateLabelState(boolean bl) {
        this.updateLabelState(bl, false);
    }

    void updateTextInputBoxState() {
        if (this.boxBackground != null && this.boxBackgroundMode != 0) {
            EditText editText = this.editText;
            boolean bl = true;
            boolean bl2 = editText != null && editText.hasFocus();
            editText = this.editText;
            if (editText == null || !editText.isHovered()) {
                bl = false;
            }
            if (this.boxBackgroundMode == 2) {
                this.boxStrokeColor = !this.isEnabled() ? this.disabledColor : (this.indicatorViewController.errorShouldBeShown() ? this.indicatorViewController.getErrorViewCurrentTextColor() : (this.counterOverflowed && (editText = this.counterView) != null ? editText.getCurrentTextColor() : (bl2 ? this.focusedStrokeColor : (bl ? this.hoveredStrokeColor : this.defaultStrokeColor))));
                this.boxStrokeWidthPx = (bl || bl2) && this.isEnabled() ? this.boxStrokeWidthFocusedPx : this.boxStrokeWidthDefaultPx;
                this.applyBoxAttributes();
            }
            return;
        }
    }

    public static class AccessibilityDelegate
    extends AccessibilityDelegateCompat {
        private final TextInputLayout layout;

        public AccessibilityDelegate(TextInputLayout textInputLayout) {
            this.layout = textInputLayout;
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View object, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo((View)object, accessibilityNodeInfoCompat);
            object = this.layout.getEditText();
            object = object != null ? object.getText() : null;
            CharSequence charSequence = this.layout.getHint();
            CharSequence charSequence2 = this.layout.getError();
            CharSequence charSequence3 = this.layout.getCounterOverflowDescription();
            boolean bl = TextUtils.isEmpty((CharSequence)object) ^ true;
            boolean bl2 = TextUtils.isEmpty((CharSequence)charSequence) ^ true;
            boolean bl3 = TextUtils.isEmpty((CharSequence)charSequence2) ^ true;
            boolean bl4 = false;
            boolean bl5 = bl3 || !TextUtils.isEmpty((CharSequence)charSequence3);
            if (bl) {
                accessibilityNodeInfoCompat.setText((CharSequence)object);
            } else if (bl2) {
                accessibilityNodeInfoCompat.setText(charSequence);
            }
            if (bl2) {
                accessibilityNodeInfoCompat.setHintText(charSequence);
                boolean bl6 = bl4;
                if (!bl) {
                    bl6 = bl4;
                    if (bl2) {
                        bl6 = true;
                    }
                }
                accessibilityNodeInfoCompat.setShowingHintText(bl6);
            }
            if (bl5) {
                object = bl3 ? charSequence2 : charSequence3;
                accessibilityNodeInfoCompat.setError((CharSequence)object);
                accessibilityNodeInfoCompat.setContentInvalid(true);
            }
        }

        @Override
        public void onPopulateAccessibilityEvent(View object, AccessibilityEvent accessibilityEvent) {
            super.onPopulateAccessibilityEvent((View)object, accessibilityEvent);
            object = this.layout.getEditText();
            object = object != null ? object.getText() : null;
            if (TextUtils.isEmpty((CharSequence)object)) {
                object = this.layout.getHint();
            }
            if (!TextUtils.isEmpty((CharSequence)object)) {
                accessibilityEvent.getText().add(object);
            }
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface BoxBackgroundMode {
    }

    static class SavedState
    extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        CharSequence error;
        boolean isPasswordToggledVisible;

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.error = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            int n = parcel.readInt();
            boolean bl = true;
            if (n != 1) {
                bl = false;
            }
            this.isPasswordToggledVisible = bl;
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("TextInputLayout.SavedState{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" error=");
            stringBuilder.append((Object)this.error);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            TextUtils.writeToParcel((CharSequence)this.error, (Parcel)parcel, (int)n);
            parcel.writeInt(this.isPasswordToggledVisible ? 1 : 0);
        }
    }
}

