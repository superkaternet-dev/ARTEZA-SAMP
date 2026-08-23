/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.Canvas
 *  android.graphics.Outline
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.Typeface
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.RippleDrawable
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.text.TextPaint
 *  android.text.TextUtils
 *  android.text.TextUtils$TruncateAt
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.PointerIcon
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewOutlineProvider
 *  android.view.ViewParent
 *  android.widget.CompoundButton
 *  android.widget.CompoundButton$OnCheckedChangeListener
 *  android.widget.TextView$BufferType
 */
package com.google.android.material.chip;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.BidiFormatter;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.google.android.material.R;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.ripple.RippleUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Chip
extends AppCompatCheckBox
implements ChipDrawable.Delegate {
    private static final int CLOSE_ICON_VIRTUAL_ID = 0;
    private static final Rect EMPTY_BOUNDS = new Rect();
    private static final String NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";
    private static final int[] SELECTED_STATE = new int[]{0x10100A1};
    private static final String TAG = "Chip";
    private ChipDrawable chipDrawable;
    private boolean closeIconFocused;
    private boolean closeIconHovered;
    private boolean closeIconPressed;
    private boolean deferredCheckedValue;
    private int focusedVirtualView = Integer.MIN_VALUE;
    private final ResourcesCompat.FontCallback fontCallback;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListenerInternal;
    private View.OnClickListener onCloseIconClickListener;
    private final Rect rect = new Rect();
    private final RectF rectF = new RectF();
    private RippleDrawable ripple;
    private final ChipTouchHelper touchHelper;

    public Chip(Context context) {
        this(context, null);
    }

    public Chip(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.chipStyle);
    }

    public Chip(Context object, AttributeSet object2, int n) {
        super((Context)object, (AttributeSet)object2, n);
        this.fontCallback = new ResourcesCompat.FontCallback(this){
            final Chip this$0;
            {
                this.this$0 = chip;
            }

            @Override
            public void onFontRetrievalFailed(int n) {
            }

            @Override
            public void onFontRetrieved(Typeface object) {
                object = this.this$0;
                object.setText(((Chip)object).getText());
                this.this$0.requestLayout();
                this.this$0.invalidate();
            }
        };
        this.validateAttributes((AttributeSet)object2);
        object = ChipDrawable.createFromAttributes((Context)object, object2, n, R.style.Widget_MaterialComponents_Chip_Action);
        this.setChipDrawable((ChipDrawable)object);
        object2 = new ChipTouchHelper(this, this);
        this.touchHelper = object2;
        ViewCompat.setAccessibilityDelegate((View)this, (AccessibilityDelegateCompat)object2);
        this.initOutlineProvider();
        this.setChecked(this.deferredCheckedValue);
        ((ChipDrawable)object).setShouldDrawText(false);
        this.setText(((ChipDrawable)object).getText());
        this.setEllipsize(((ChipDrawable)object).getEllipsize());
        this.setIncludeFontPadding(false);
        if (this.getTextAppearance() != null) {
            this.updateTextPaintDrawState(this.getTextAppearance());
        }
        this.setSingleLine();
        this.setGravity(8388627);
        this.updatePaddingInternal();
    }

    private void applyChipDrawable(ChipDrawable chipDrawable) {
        chipDrawable.setDelegate(this);
    }

    private float calculateTextOffsetFromStart(ChipDrawable chipDrawable) {
        float f = this.getChipStartPadding() + chipDrawable.calculateChipIconWidth() + this.getTextStartPadding();
        if (ViewCompat.getLayoutDirection((View)this) == 0) {
            return f;
        }
        return -f;
    }

    private int[] createCloseIconDrawableState() {
        int n = 0;
        if (this.isEnabled()) {
            n = 0 + 1;
        }
        int n2 = n;
        if (this.closeIconFocused) {
            n2 = n + 1;
        }
        n = n2;
        if (this.closeIconHovered) {
            n = n2 + 1;
        }
        n2 = n;
        if (this.closeIconPressed) {
            n2 = n + 1;
        }
        n = n2;
        if (this.isChecked()) {
            n = n2 + 1;
        }
        int[] nArray = new int[n];
        n2 = 0;
        if (this.isEnabled()) {
            nArray[0] = 16842910;
            n2 = 0 + 1;
        }
        n = n2;
        if (this.closeIconFocused) {
            nArray[n2] = 16842908;
            n = n2 + 1;
        }
        n2 = n;
        if (this.closeIconHovered) {
            nArray[n] = 16843623;
            n2 = n + 1;
        }
        n = n2;
        if (this.closeIconPressed) {
            nArray[n2] = 16842919;
            n = n2 + 1;
        }
        if (this.isChecked()) {
            nArray[n] = 0x10100A1;
        }
        return nArray;
    }

    private void ensureFocus() {
        if (this.focusedVirtualView == Integer.MIN_VALUE) {
            this.setFocusedVirtualView(-1);
        }
    }

    private RectF getCloseIconTouchBounds() {
        this.rectF.setEmpty();
        if (this.hasCloseIcon()) {
            this.chipDrawable.getCloseIconTouchBounds(this.rectF);
        }
        return this.rectF;
    }

    private Rect getCloseIconTouchBoundsInt() {
        RectF rectF = this.getCloseIconTouchBounds();
        this.rect.set((int)rectF.left, (int)rectF.top, (int)rectF.right, (int)rectF.bottom);
        return this.rect;
    }

    private TextAppearance getTextAppearance() {
        Object object = this.chipDrawable;
        object = object != null ? ((ChipDrawable)object).getTextAppearance() : null;
        return object;
    }

    private boolean handleAccessibilityExit(MotionEvent object) {
        if (object.getAction() == 10) {
            try {
                object = ExploreByTouchHelper.class.getDeclaredField("mHoveredVirtualViewId");
                ((Field)object).setAccessible(true);
                if ((Integer)((Field)object).get(this.touchHelper) != Integer.MIN_VALUE) {
                    object = ExploreByTouchHelper.class.getDeclaredMethod("updateHoveredVirtualView", Integer.TYPE);
                    ((Method)object).setAccessible(true);
                    ((Method)object).invoke((Object)this.touchHelper, Integer.MIN_VALUE);
                    return true;
                }
            }
            catch (NoSuchFieldException noSuchFieldException) {
                Log.e((String)TAG, (String)"Unable to send Accessibility Exit event", (Throwable)noSuchFieldException);
            }
            catch (InvocationTargetException invocationTargetException) {
                Log.e((String)TAG, (String)"Unable to send Accessibility Exit event", (Throwable)invocationTargetException);
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.e((String)TAG, (String)"Unable to send Accessibility Exit event", (Throwable)illegalAccessException);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.e((String)TAG, (String)"Unable to send Accessibility Exit event", (Throwable)noSuchMethodException);
            }
        }
        return false;
    }

    private boolean hasCloseIcon() {
        ChipDrawable chipDrawable = this.chipDrawable;
        boolean bl = chipDrawable != null && chipDrawable.getCloseIcon() != null;
        return bl;
    }

    private void initOutlineProvider() {
        if (Build.VERSION.SDK_INT >= 21) {
            this.setOutlineProvider(new ViewOutlineProvider(this){
                final Chip this$0;
                {
                    this.this$0 = chip;
                }

                public void getOutline(View view, Outline outline) {
                    if (this.this$0.chipDrawable != null) {
                        this.this$0.chipDrawable.getOutline(outline);
                    } else {
                        outline.setAlpha(0.0f);
                    }
                }
            });
        }
    }

    private boolean moveFocus(boolean bl) {
        this.ensureFocus();
        boolean bl2 = false;
        if (bl) {
            bl = bl2;
            if (this.focusedVirtualView == -1) {
                this.setFocusedVirtualView(0);
                bl = true;
            }
        } else {
            bl = bl2;
            if (this.focusedVirtualView == 0) {
                this.setFocusedVirtualView(-1);
                bl = true;
            }
        }
        return bl;
    }

    private void setCloseIconFocused(boolean bl) {
        if (this.closeIconFocused != bl) {
            this.closeIconFocused = bl;
            this.refreshDrawableState();
        }
    }

    private void setCloseIconHovered(boolean bl) {
        if (this.closeIconHovered != bl) {
            this.closeIconHovered = bl;
            this.refreshDrawableState();
        }
    }

    private void setCloseIconPressed(boolean bl) {
        if (this.closeIconPressed != bl) {
            this.closeIconPressed = bl;
            this.refreshDrawableState();
        }
    }

    private void setFocusedVirtualView(int n) {
        int n2 = this.focusedVirtualView;
        if (n2 != n) {
            if (n2 == 0) {
                this.setCloseIconFocused(false);
            }
            this.focusedVirtualView = n;
            if (n == 0) {
                this.setCloseIconFocused(true);
            }
        }
    }

    private void unapplyChipDrawable(ChipDrawable chipDrawable) {
        if (chipDrawable != null) {
            chipDrawable.setDelegate(null);
        }
    }

    private void updatePaddingInternal() {
        block6: {
            float f;
            float f2;
            block8: {
                block7: {
                    ChipDrawable chipDrawable;
                    if (TextUtils.isEmpty((CharSequence)this.getText()) || (chipDrawable = this.chipDrawable) == null) break block6;
                    f2 = chipDrawable.getChipStartPadding() + this.chipDrawable.getChipEndPadding() + this.chipDrawable.getTextStartPadding() + this.chipDrawable.getTextEndPadding();
                    if (this.chipDrawable.isChipIconVisible() && this.chipDrawable.getChipIcon() != null) break block7;
                    f = f2;
                    if (this.chipDrawable.getCheckedIcon() == null) break block8;
                    f = f2;
                    if (!this.chipDrawable.isCheckedIconVisible()) break block8;
                    f = f2;
                    if (!this.isChecked()) break block8;
                }
                f = f2 + (this.chipDrawable.getIconStartPadding() + this.chipDrawable.getIconEndPadding() + this.chipDrawable.getChipIconSize());
            }
            f2 = f;
            if (this.chipDrawable.isCloseIconVisible()) {
                f2 = f;
                if (this.chipDrawable.getCloseIcon() != null) {
                    f2 = f + (this.chipDrawable.getCloseIconStartPadding() + this.chipDrawable.getCloseIconEndPadding() + this.chipDrawable.getCloseIconSize());
                }
            }
            if ((float)ViewCompat.getPaddingEnd((View)this) != f2) {
                ViewCompat.setPaddingRelative((View)this, ViewCompat.getPaddingStart((View)this), this.getPaddingTop(), (int)f2, this.getPaddingBottom());
            }
            return;
        }
    }

    private void updateTextPaintDrawState(TextAppearance textAppearance) {
        TextPaint textPaint = this.getPaint();
        textPaint.drawableState = this.chipDrawable.getState();
        textAppearance.updateDrawState(this.getContext(), textPaint, this.fontCallback);
    }

    private void validateAttributes(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "background") == null) {
            if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableLeft") == null) {
                if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableStart") == null) {
                    if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableEnd") == null) {
                        if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableRight") == null) {
                            if (attributeSet.getAttributeBooleanValue(NAMESPACE_ANDROID, "singleLine", true) && attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "lines", 1) == 1 && attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "minLines", 1) == 1 && attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "maxLines", 1) == 1) {
                                if (attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "gravity", 8388627) != 8388627) {
                                    Log.w((String)TAG, (String)"Chip text must be vertically center and start aligned");
                                }
                                return;
                            }
                            throw new UnsupportedOperationException("Chip does not support multi-line text");
                        }
                        throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
                    }
                    throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
                }
                throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
            }
            throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
        }
        throw new UnsupportedOperationException("Do not set the background; Chip manages its own background drawable.");
    }

    protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
        boolean bl = this.handleAccessibilityExit(motionEvent) || this.touchHelper.dispatchHoverEvent(motionEvent) || super.dispatchHoverEvent(motionEvent);
        return bl;
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        boolean bl = this.touchHelper.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent);
        return bl;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        boolean bl = false;
        ChipDrawable chipDrawable = this.chipDrawable;
        boolean bl2 = bl;
        if (chipDrawable != null) {
            bl2 = bl;
            if (chipDrawable.isCloseIconStateful()) {
                bl2 = this.chipDrawable.setCloseIconState(this.createCloseIconDrawableState());
            }
        }
        if (bl2) {
            this.invalidate();
        }
    }

    public Drawable getCheckedIcon() {
        ChipDrawable chipDrawable = this.chipDrawable;
        chipDrawable = chipDrawable != null ? chipDrawable.getCheckedIcon() : null;
        return chipDrawable;
    }

    public ColorStateList getChipBackgroundColor() {
        ChipDrawable chipDrawable = this.chipDrawable;
        chipDrawable = chipDrawable != null ? chipDrawable.getChipBackgroundColor() : null;
        return chipDrawable;
    }

    public float getChipCornerRadius() {
        ChipDrawable chipDrawable = this.chipDrawable;
        float f = chipDrawable != null ? chipDrawable.getChipCornerRadius() : 0.0f;
        return f;
    }

    public Drawable getChipDrawable() {
        return this.chipDrawable;
    }

    public float getChipEndPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        float f = chipDrawable != null ? chipDrawable.getChipEndPadding() : 0.0f;
        return f;
    }

    public Drawable getChipIcon() {
        ChipDrawable chipDrawable = this.chipDrawable;
        chipDrawable = chipDrawable != null ? chipDrawable.getChipIcon() : null;
        return chipDrawable;
    }

    public float getChipIconSize() {
        ChipDrawable chipDrawable = this.chipDrawable;
        float f = chipDrawable != null ? chipDrawable.getChipIconSize() : 0.0f;
        return f;
    }

    public ColorStateList getChipIconTint() {
        ChipDrawable chipDrawable = this.chipDrawable;
        chipDrawable = chipDrawable != null ? chipDrawable.getChipIconTint() : null;
        return chipDrawable;
    }

    public float getChipMinHeight() {
        ChipDrawable chipDrawable = this.chipDrawable;
        float f = chipDrawable != null ? chipDrawable.getChipMinHeight() : 0.0f;
        return f;
    }

    public float getChipStartPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        float f = chipDrawable != null ? chipDrawable.getChipStartPadding() : 0.0f;
        return f;
    }

    public ColorStateList getChipStrokeColor() {
        ChipDrawable chipDrawable = this.chipDrawable;
        chipDrawable = chipDrawable != null ? chipDrawable.getChipStrokeColor() : null;
        return chipDrawable;
    }

    public float getChipStrokeWidth() {
        ChipDrawable chipDrawable = this.chipDrawable;
        float f = chipDrawable != null ? chipDrawable.getChipStrokeWidth() : 0.0f;
        return f;
    }

    @Deprecated
    public CharSequence getChipText() {
        return this.getText();
    }

    public Drawable getCloseIcon() {
        ChipDrawable chipDrawable = this.chipDrawable;
        chipDrawable = chipDrawable != null ? chipDrawable.getCloseIcon() : null;
        return chipDrawable;
    }

    public CharSequence getCloseIconContentDescription() {
        Object object = this.chipDrawable;
        object = object != null ? ((ChipDrawable)object).getCloseIconContentDescription() : null;
        return object;
    }

    public float getCloseIconEndPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        float f = chipDrawable != null ? chipDrawable.getCloseIconEndPadding() : 0.0f;
        return f;
    }

    public float getCloseIconSize() {
        ChipDrawable chipDrawable = this.chipDrawable;
        float f = chipDrawable != null ? chipDrawable.getCloseIconSize() : 0.0f;
        return f;
    }

    public float getCloseIconStartPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        float f = chipDrawable != null ? chipDrawable.getCloseIconStartPadding() : 0.0f;
        return f;
    }

    public ColorStateList getCloseIconTint() {
        ChipDrawable chipDrawable = this.chipDrawable;
        chipDrawable = chipDrawable != null ? chipDrawable.getCloseIconTint() : null;
        return chipDrawable;
    }

    public TextUtils.TruncateAt getEllipsize() {
        ChipDrawable chipDrawable = this.chipDrawable;
        chipDrawable = chipDrawable != null ? chipDrawable.getEllipsize() : null;
        return chipDrawable;
    }

    public void getFocusedRect(Rect rect) {
        if (this.focusedVirtualView == 0) {
            rect.set(this.getCloseIconTouchBoundsInt());
        } else {
            super.getFocusedRect(rect);
        }
    }

    public MotionSpec getHideMotionSpec() {
        Object object = this.chipDrawable;
        object = object != null ? ((ChipDrawable)object).getHideMotionSpec() : null;
        return object;
    }

    public float getIconEndPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        float f = chipDrawable != null ? chipDrawable.getIconEndPadding() : 0.0f;
        return f;
    }

    public float getIconStartPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        float f = chipDrawable != null ? chipDrawable.getIconStartPadding() : 0.0f;
        return f;
    }

    public ColorStateList getRippleColor() {
        ChipDrawable chipDrawable = this.chipDrawable;
        chipDrawable = chipDrawable != null ? chipDrawable.getRippleColor() : null;
        return chipDrawable;
    }

    public MotionSpec getShowMotionSpec() {
        Object object = this.chipDrawable;
        object = object != null ? ((ChipDrawable)object).getShowMotionSpec() : null;
        return object;
    }

    public CharSequence getText() {
        Object object = this.chipDrawable;
        object = object != null ? ((ChipDrawable)object).getText() : "";
        return object;
    }

    public float getTextEndPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        float f = chipDrawable != null ? chipDrawable.getTextEndPadding() : 0.0f;
        return f;
    }

    public float getTextStartPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        float f = chipDrawable != null ? chipDrawable.getTextStartPadding() : 0.0f;
        return f;
    }

    public boolean isCheckable() {
        ChipDrawable chipDrawable = this.chipDrawable;
        boolean bl = chipDrawable != null && chipDrawable.isCheckable();
        return bl;
    }

    @Deprecated
    public boolean isCheckedIconEnabled() {
        return this.isCheckedIconVisible();
    }

    public boolean isCheckedIconVisible() {
        ChipDrawable chipDrawable = this.chipDrawable;
        boolean bl = chipDrawable != null && chipDrawable.isCheckedIconVisible();
        return bl;
    }

    @Deprecated
    public boolean isChipIconEnabled() {
        return this.isChipIconVisible();
    }

    public boolean isChipIconVisible() {
        ChipDrawable chipDrawable = this.chipDrawable;
        boolean bl = chipDrawable != null && chipDrawable.isChipIconVisible();
        return bl;
    }

    @Deprecated
    public boolean isCloseIconEnabled() {
        return this.isCloseIconVisible();
    }

    public boolean isCloseIconVisible() {
        ChipDrawable chipDrawable = this.chipDrawable;
        boolean bl = chipDrawable != null && chipDrawable.isCloseIconVisible();
        return bl;
    }

    @Override
    public void onChipDrawableSizeChange() {
        this.updatePaddingInternal();
        this.requestLayout();
        if (Build.VERSION.SDK_INT >= 21) {
            this.invalidateOutline();
        }
    }

    protected int[] onCreateDrawableState(int n) {
        int[] nArray = super.onCreateDrawableState(n + 1);
        if (this.isChecked()) {
            Chip.mergeDrawableStates((int[])nArray, (int[])SELECTED_STATE);
        }
        return nArray;
    }

    protected void onDraw(Canvas canvas) {
        ChipDrawable chipDrawable;
        if (!TextUtils.isEmpty((CharSequence)this.getText()) && (chipDrawable = this.chipDrawable) != null && !chipDrawable.shouldDrawText()) {
            int n = canvas.save();
            canvas.translate(this.calculateTextOffsetFromStart(this.chipDrawable), 0.0f);
            super.onDraw(canvas);
            canvas.restoreToCount(n);
            return;
        }
        super.onDraw(canvas);
    }

    protected void onFocusChanged(boolean bl, int n, Rect rect) {
        if (bl) {
            this.setFocusedVirtualView(-1);
        } else {
            this.setFocusedVirtualView(Integer.MIN_VALUE);
        }
        this.invalidate();
        super.onFocusChanged(bl, n, rect);
        this.touchHelper.onFocusChanged(bl, n, rect);
    }

    public boolean onHoverEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            default: {
                break;
            }
            case 10: {
                this.setCloseIconHovered(false);
                break;
            }
            case 7: {
                this.setCloseIconHovered(this.getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY()));
            }
        }
        return super.onHoverEvent(motionEvent);
    }

    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        boolean bl;
        boolean bl2 = false;
        block0 : switch (keyEvent.getKeyCode()) {
            default: {
                bl = bl2;
                break;
            }
            case 61: {
                View view;
                int n2 = 0;
                if (keyEvent.hasNoModifiers()) {
                    n2 = 2;
                } else if (keyEvent.hasModifiers(1)) {
                    n2 = 1;
                }
                bl = bl2;
                if (n2 == 0) break;
                ViewParent viewParent = this.getParent();
                Chip chip = this;
                while ((view = chip.focusSearch(n2)) != null && view != this) {
                    chip = view;
                    if (view.getParent() == viewParent) continue;
                }
                if (view != null) {
                    view.requestFocus();
                    return true;
                }
                bl = bl2;
                break;
            }
            case 23: 
            case 66: {
                switch (this.focusedVirtualView) {
                    default: {
                        bl = bl2;
                        break block0;
                    }
                    case 0: {
                        this.performCloseIconClick();
                        return true;
                    }
                    case -1: 
                }
                this.performClick();
                return true;
            }
            case 22: {
                bl = bl2;
                if (!keyEvent.hasNoModifiers()) break;
                bl = this.moveFocus(ViewUtils.isLayoutRtl((View)this) ^ true);
                break;
            }
            case 21: {
                bl = bl2;
                if (!keyEvent.hasNoModifiers()) break;
                bl = this.moveFocus(ViewUtils.isLayoutRtl((View)this));
            }
        }
        if (bl) {
            this.invalidate();
            return true;
        }
        return super.onKeyDown(n, keyEvent);
    }

    public PointerIcon onResolvePointerIcon(MotionEvent motionEvent, int n) {
        if (this.getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY()) && this.isEnabled()) {
            return PointerIcon.getSystemIcon((Context)this.getContext(), (int)1002);
        }
        return null;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean bl = false;
        boolean bl2 = false;
        int n = motionEvent.getActionMasked();
        boolean bl3 = this.getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY());
        boolean bl4 = true;
        boolean bl5 = bl2;
        switch (n) {
            default: {
                bl5 = bl;
                break;
            }
            case 2: {
                bl5 = bl;
                if (!this.closeIconPressed) break;
                if (!bl3) {
                    this.setCloseIconPressed(false);
                }
                bl5 = true;
                break;
            }
            case 1: {
                bl5 = bl2;
                if (this.closeIconPressed) {
                    this.performCloseIconClick();
                    bl5 = true;
                }
            }
            case 3: {
                this.setCloseIconPressed(false);
                break;
            }
            case 0: {
                bl5 = bl;
                if (!bl3) break;
                this.setCloseIconPressed(true);
                bl5 = true;
            }
        }
        bl3 = bl4;
        if (!bl5) {
            bl3 = super.onTouchEvent(motionEvent) ? bl4 : false;
        }
        return bl3;
    }

    public boolean performCloseIconClick() {
        boolean bl;
        this.playSoundEffect(0);
        View.OnClickListener onClickListener = this.onCloseIconClickListener;
        if (onClickListener != null) {
            onClickListener.onClick((View)this);
            bl = true;
        } else {
            bl = false;
        }
        this.touchHelper.sendEventForVirtualView(0, 1);
        return bl;
    }

    public void setBackground(Drawable drawable2) {
        if (drawable2 != this.chipDrawable && drawable2 != this.ripple) {
            throw new UnsupportedOperationException("Do not set the background; Chip manages its own background drawable.");
        }
        super.setBackground(drawable2);
    }

    public void setBackgroundColor(int n) {
        throw new UnsupportedOperationException("Do not set the background color; Chip manages its own background drawable.");
    }

    @Override
    public void setBackgroundDrawable(Drawable drawable2) {
        if (drawable2 != this.chipDrawable && drawable2 != this.ripple) {
            throw new UnsupportedOperationException("Do not set the background drawable; Chip manages its own background drawable.");
        }
        super.setBackgroundDrawable(drawable2);
    }

    @Override
    public void setBackgroundResource(int n) {
        throw new UnsupportedOperationException("Do not set the background resource; Chip manages its own background drawable.");
    }

    public void setBackgroundTintList(ColorStateList colorStateList) {
        throw new UnsupportedOperationException("Do not set the background tint list; Chip manages its own background drawable.");
    }

    public void setBackgroundTintMode(PorterDuff.Mode mode) {
        throw new UnsupportedOperationException("Do not set the background tint mode; Chip manages its own background drawable.");
    }

    public void setCheckable(boolean bl) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckable(bl);
        }
    }

    public void setCheckableResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckableResource(n);
        }
    }

    public void setChecked(boolean bl) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable == null) {
            this.deferredCheckedValue = bl;
        } else if (chipDrawable.isCheckable()) {
            boolean bl2 = this.isChecked();
            super.setChecked(bl);
            if (bl2 != bl && (chipDrawable = this.onCheckedChangeListenerInternal) != null) {
                chipDrawable.onCheckedChanged((CompoundButton)this, bl);
            }
        }
    }

    public void setCheckedIcon(Drawable drawable2) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckedIcon(drawable2);
        }
    }

    @Deprecated
    public void setCheckedIconEnabled(boolean bl) {
        this.setCheckedIconVisible(bl);
    }

    @Deprecated
    public void setCheckedIconEnabledResource(int n) {
        this.setCheckedIconVisible(n);
    }

    public void setCheckedIconResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckedIconResource(n);
        }
    }

    public void setCheckedIconVisible(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckedIconVisible(n);
        }
    }

    public void setCheckedIconVisible(boolean bl) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckedIconVisible(bl);
        }
    }

    public void setChipBackgroundColor(ColorStateList colorStateList) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipBackgroundColor(colorStateList);
        }
    }

    public void setChipBackgroundColorResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipBackgroundColorResource(n);
        }
    }

    public void setChipCornerRadius(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipCornerRadius(f);
        }
    }

    public void setChipCornerRadiusResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipCornerRadiusResource(n);
        }
    }

    public void setChipDrawable(ChipDrawable chipDrawable) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != chipDrawable) {
            this.unapplyChipDrawable(chipDrawable2);
            this.chipDrawable = chipDrawable;
            this.applyChipDrawable(chipDrawable);
            if (RippleUtils.USE_FRAMEWORK_RIPPLE) {
                this.ripple = new RippleDrawable(RippleUtils.convertToRippleDrawableColor(this.chipDrawable.getRippleColor()), (Drawable)this.chipDrawable, null);
                this.chipDrawable.setUseCompatRipple(false);
                ViewCompat.setBackground((View)this, (Drawable)this.ripple);
            } else {
                this.chipDrawable.setUseCompatRipple(true);
                ViewCompat.setBackground((View)this, this.chipDrawable);
            }
        }
    }

    public void setChipEndPadding(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipEndPadding(f);
        }
    }

    public void setChipEndPaddingResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipEndPaddingResource(n);
        }
    }

    public void setChipIcon(Drawable drawable2) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIcon(drawable2);
        }
    }

    @Deprecated
    public void setChipIconEnabled(boolean bl) {
        this.setChipIconVisible(bl);
    }

    @Deprecated
    public void setChipIconEnabledResource(int n) {
        this.setChipIconVisible(n);
    }

    public void setChipIconResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconResource(n);
        }
    }

    public void setChipIconSize(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconSize(f);
        }
    }

    public void setChipIconSizeResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconSizeResource(n);
        }
    }

    public void setChipIconTint(ColorStateList colorStateList) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconTint(colorStateList);
        }
    }

    public void setChipIconTintResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconTintResource(n);
        }
    }

    public void setChipIconVisible(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconVisible(n);
        }
    }

    public void setChipIconVisible(boolean bl) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconVisible(bl);
        }
    }

    public void setChipMinHeight(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipMinHeight(f);
        }
    }

    public void setChipMinHeightResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipMinHeightResource(n);
        }
    }

    public void setChipStartPadding(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStartPadding(f);
        }
    }

    public void setChipStartPaddingResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStartPaddingResource(n);
        }
    }

    public void setChipStrokeColor(ColorStateList colorStateList) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStrokeColor(colorStateList);
        }
    }

    public void setChipStrokeColorResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStrokeColorResource(n);
        }
    }

    public void setChipStrokeWidth(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStrokeWidth(f);
        }
    }

    public void setChipStrokeWidthResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStrokeWidthResource(n);
        }
    }

    @Deprecated
    public void setChipText(CharSequence charSequence) {
        this.setText(charSequence);
    }

    @Deprecated
    public void setChipTextResource(int n) {
        this.setText(this.getResources().getString(n));
    }

    public void setCloseIcon(Drawable drawable2) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIcon(drawable2);
        }
    }

    public void setCloseIconContentDescription(CharSequence charSequence) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconContentDescription(charSequence);
        }
    }

    @Deprecated
    public void setCloseIconEnabled(boolean bl) {
        this.setCloseIconVisible(bl);
    }

    @Deprecated
    public void setCloseIconEnabledResource(int n) {
        this.setCloseIconVisible(n);
    }

    public void setCloseIconEndPadding(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconEndPadding(f);
        }
    }

    public void setCloseIconEndPaddingResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconEndPaddingResource(n);
        }
    }

    public void setCloseIconResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconResource(n);
        }
    }

    public void setCloseIconSize(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconSize(f);
        }
    }

    public void setCloseIconSizeResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconSizeResource(n);
        }
    }

    public void setCloseIconStartPadding(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconStartPadding(f);
        }
    }

    public void setCloseIconStartPaddingResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconStartPaddingResource(n);
        }
    }

    public void setCloseIconTint(ColorStateList colorStateList) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconTint(colorStateList);
        }
    }

    public void setCloseIconTintResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconTintResource(n);
        }
    }

    public void setCloseIconVisible(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconVisible(n);
        }
    }

    public void setCloseIconVisible(boolean bl) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconVisible(bl);
        }
    }

    public void setCompoundDrawables(Drawable drawable2, Drawable drawable3, Drawable drawable4, Drawable drawable5) {
        if (drawable2 == null) {
            if (drawable4 == null) {
                super.setCompoundDrawables(drawable2, drawable3, drawable4, drawable5);
                return;
            }
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
    }

    public void setCompoundDrawablesRelative(Drawable drawable2, Drawable drawable3, Drawable drawable4, Drawable drawable5) {
        if (drawable2 == null) {
            if (drawable4 == null) {
                super.setCompoundDrawablesRelative(drawable2, drawable3, drawable4, drawable5);
                return;
            }
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
    }

    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int n, int n2, int n3, int n4) {
        if (n == 0) {
            if (n3 == 0) {
                super.setCompoundDrawablesRelativeWithIntrinsicBounds(n, n2, n3, n4);
                return;
            }
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
    }

    public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable drawable2, Drawable drawable3, Drawable drawable4, Drawable drawable5) {
        if (drawable2 == null) {
            if (drawable4 == null) {
                super.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable5);
                return;
            }
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
    }

    public void setCompoundDrawablesWithIntrinsicBounds(int n, int n2, int n3, int n4) {
        if (n == 0) {
            if (n3 == 0) {
                super.setCompoundDrawablesWithIntrinsicBounds(n, n2, n3, n4);
                return;
            }
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
    }

    public void setCompoundDrawablesWithIntrinsicBounds(Drawable drawable2, Drawable drawable3, Drawable drawable4, Drawable drawable5) {
        if (drawable2 == null) {
            if (drawable4 == null) {
                super.setCompoundDrawablesWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable5);
                return;
            }
            throw new UnsupportedOperationException("Please set right drawable using R.attr#closeIcon.");
        }
        throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
    }

    public void setEllipsize(TextUtils.TruncateAt truncateAt) {
        if (this.chipDrawable == null) {
            return;
        }
        if (truncateAt != TextUtils.TruncateAt.MARQUEE) {
            super.setEllipsize(truncateAt);
            ChipDrawable chipDrawable = this.chipDrawable;
            if (chipDrawable != null) {
                chipDrawable.setEllipsize(truncateAt);
            }
            return;
        }
        throw new UnsupportedOperationException("Text within a chip are not allowed to scroll.");
    }

    public void setGravity(int n) {
        if (n != 8388627) {
            Log.w((String)TAG, (String)"Chip text must be vertically center and start aligned");
        } else {
            super.setGravity(n);
        }
    }

    public void setHideMotionSpec(MotionSpec motionSpec) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setHideMotionSpec(motionSpec);
        }
    }

    public void setHideMotionSpecResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setHideMotionSpecResource(n);
        }
    }

    public void setIconEndPadding(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setIconEndPadding(f);
        }
    }

    public void setIconEndPaddingResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setIconEndPaddingResource(n);
        }
    }

    public void setIconStartPadding(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setIconStartPadding(f);
        }
    }

    public void setIconStartPaddingResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setIconStartPaddingResource(n);
        }
    }

    public void setLines(int n) {
        if (n <= 1) {
            super.setLines(n);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    public void setMaxLines(int n) {
        if (n <= 1) {
            super.setMaxLines(n);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    public void setMaxWidth(int n) {
        super.setMaxWidth(n);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setMaxWidth(n);
        }
    }

    public void setMinLines(int n) {
        if (n <= 1) {
            super.setMinLines(n);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    void setOnCheckedChangeListenerInternal(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListenerInternal = onCheckedChangeListener;
    }

    public void setOnCloseIconClickListener(View.OnClickListener onClickListener) {
        this.onCloseIconClickListener = onClickListener;
    }

    public void setRippleColor(ColorStateList colorStateList) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setRippleColor(colorStateList);
        }
    }

    public void setRippleColorResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setRippleColorResource(n);
        }
    }

    public void setShowMotionSpec(MotionSpec motionSpec) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setShowMotionSpec(motionSpec);
        }
    }

    public void setShowMotionSpecResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setShowMotionSpecResource(n);
        }
    }

    public void setSingleLine(boolean bl) {
        if (bl) {
            super.setSingleLine(bl);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    public void setText(CharSequence object, TextView.BufferType bufferType) {
        if (this.chipDrawable == null) {
            return;
        }
        CharSequence charSequence = object;
        if (object == null) {
            charSequence = "";
        }
        object = BidiFormatter.getInstance().unicodeWrap(charSequence);
        if (this.chipDrawable.shouldDrawText()) {
            object = null;
        }
        super.setText((CharSequence)object, bufferType);
        object = this.chipDrawable;
        if (object != null) {
            ((ChipDrawable)object).setText(charSequence);
        }
    }

    public void setTextAppearance(int n) {
        super.setTextAppearance(n);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextAppearanceResource(n);
        }
        if (this.getTextAppearance() != null) {
            this.getTextAppearance().updateMeasureState(this.getContext(), this.getPaint(), this.fontCallback);
            this.updateTextPaintDrawState(this.getTextAppearance());
        }
    }

    public void setTextAppearance(Context context, int n) {
        super.setTextAppearance(context, n);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextAppearanceResource(n);
        }
        if (this.getTextAppearance() != null) {
            this.getTextAppearance().updateMeasureState(context, this.getPaint(), this.fontCallback);
            this.updateTextPaintDrawState(this.getTextAppearance());
        }
    }

    public void setTextAppearance(TextAppearance textAppearance) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextAppearance(textAppearance);
        }
        if (this.getTextAppearance() != null) {
            this.getTextAppearance().updateMeasureState(this.getContext(), this.getPaint(), this.fontCallback);
            this.updateTextPaintDrawState(textAppearance);
        }
    }

    public void setTextAppearanceResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextAppearanceResource(n);
        }
        this.setTextAppearance(this.getContext(), n);
    }

    public void setTextEndPadding(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextEndPadding(f);
        }
    }

    public void setTextEndPaddingResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextEndPaddingResource(n);
        }
    }

    public void setTextStartPadding(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextStartPadding(f);
        }
    }

    public void setTextStartPaddingResource(int n) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextStartPaddingResource(n);
        }
    }

    private class ChipTouchHelper
    extends ExploreByTouchHelper {
        final Chip this$0;

        ChipTouchHelper(Chip chip, Chip chip2) {
            this.this$0 = chip;
            super((View)chip2);
        }

        @Override
        protected int getVirtualViewAt(float f, float f2) {
            int n = this.this$0.hasCloseIcon() && this.this$0.getCloseIconTouchBounds().contains(f, f2) ? 0 : -1;
            return n;
        }

        @Override
        protected void getVisibleVirtualViews(List<Integer> list) {
            if (this.this$0.hasCloseIcon()) {
                list.add(0);
            }
        }

        @Override
        protected boolean onPerformActionForVirtualView(int n, int n2, Bundle bundle) {
            if (n2 == 16 && n == 0) {
                return this.this$0.performCloseIconClick();
            }
            return false;
        }

        @Override
        protected void onPopulateNodeForHost(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            boolean bl = this.this$0.chipDrawable != null && this.this$0.chipDrawable.isCheckable();
            accessibilityNodeInfoCompat.setCheckable(bl);
            accessibilityNodeInfoCompat.setClassName(Chip.class.getName());
            CharSequence charSequence = this.this$0.getText();
            if (Build.VERSION.SDK_INT >= 23) {
                accessibilityNodeInfoCompat.setText(charSequence);
            } else {
                accessibilityNodeInfoCompat.setContentDescription(charSequence);
            }
        }

        @Override
        protected void onPopulateNodeForVirtualView(int n, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            boolean bl = this.this$0.hasCloseIcon();
            CharSequence charSequence = "";
            if (bl) {
                CharSequence charSequence2 = this.this$0.getCloseIconContentDescription();
                if (charSequence2 != null) {
                    accessibilityNodeInfoCompat.setContentDescription(charSequence2);
                } else {
                    charSequence2 = this.this$0.getText();
                    Context context = this.this$0.getContext();
                    n = R.string.mtrl_chip_close_icon_content_description;
                    if (!TextUtils.isEmpty((CharSequence)charSequence2)) {
                        charSequence = charSequence2;
                    }
                    accessibilityNodeInfoCompat.setContentDescription(context.getString(n, new Object[]{charSequence}).trim());
                }
                accessibilityNodeInfoCompat.setBoundsInParent(this.this$0.getCloseIconTouchBoundsInt());
                accessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
                accessibilityNodeInfoCompat.setEnabled(this.this$0.isEnabled());
            } else {
                accessibilityNodeInfoCompat.setContentDescription("");
                accessibilityNodeInfoCompat.setBoundsInParent(EMPTY_BOUNDS);
            }
        }
    }
}

