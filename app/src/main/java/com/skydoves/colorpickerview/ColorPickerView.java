/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Color
 *  android.graphics.Matrix
 *  android.graphics.Point
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.util.AttributeSet
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.ImageView
 */
package com.skydoves.colorpickerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import com.skydoves.colorpickerview.ActionMode;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorHsvPalette;
import com.skydoves.colorpickerview.Dp;
import com.skydoves.colorpickerview.PointMapper;
import com.skydoves.colorpickerview.R;
import com.skydoves.colorpickerview.SizeUtils;
import com.skydoves.colorpickerview.flag.FlagMode;
import com.skydoves.colorpickerview.flag.FlagView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.listeners.ColorListener;
import com.skydoves.colorpickerview.listeners.ColorPickerViewListener;
import com.skydoves.colorpickerview.preference.ColorPickerPreferenceManager;
import com.skydoves.colorpickerview.sliders.AbstractSlider;
import com.skydoves.colorpickerview.sliders.AlphaSlideBar;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

public class ColorPickerView
extends FrameLayout
implements LifecycleObserver {
    private boolean VISIBLE_FLAG = false;
    private ActionMode actionMode;
    private AlphaSlideBar alphaSlideBar;
    private float alpha_flag = 1.0f;
    private float alpha_selector = 1.0f;
    private BrightnessSlideBar brightnessSlider;
    public ColorPickerViewListener colorListener;
    private long debounceDuration = 0L;
    private Handler debounceHandler = new Handler();
    private FlagView flagView;
    private ImageView palette;
    private Drawable paletteDrawable;
    private ColorPickerPreferenceManager preferenceManager;
    private String preferenceName;
    private int selectedColor;
    private Point selectedPoint;
    private int selectedPureColor;
    private ImageView selector;
    private Drawable selectorDrawable;
    private int selectorSize = 0;

    public ColorPickerView(Context context) {
        super(context);
        this.actionMode = ActionMode.ALWAYS;
        this.preferenceManager = ColorPickerPreferenceManager.getInstance(this.getContext());
    }

    public ColorPickerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.actionMode = ActionMode.ALWAYS;
        this.preferenceManager = ColorPickerPreferenceManager.getInstance(this.getContext());
        this.getAttrs(attributeSet);
        this.onCreate();
    }

    public ColorPickerView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.actionMode = ActionMode.ALWAYS;
        this.preferenceManager = ColorPickerPreferenceManager.getInstance(this.getContext());
        this.getAttrs(attributeSet);
        this.onCreate();
    }

    public ColorPickerView(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        this.actionMode = ActionMode.ALWAYS;
        this.preferenceManager = ColorPickerPreferenceManager.getInstance(this.getContext());
        this.getAttrs(attributeSet);
        this.onCreate();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void getAttrs(AttributeSet attributeSet) {
        TypedArray typedArray = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.ColorPickerView);
        try {
            int n;
            if (typedArray.hasValue(R.styleable.ColorPickerView_palette)) {
                this.paletteDrawable = typedArray.getDrawable(R.styleable.ColorPickerView_palette);
            }
            if (typedArray.hasValue(R.styleable.ColorPickerView_selector) && (n = typedArray.getResourceId(R.styleable.ColorPickerView_selector, -1)) != -1) {
                this.selectorDrawable = AppCompatResources.getDrawable(this.getContext(), n);
            }
            if (typedArray.hasValue(R.styleable.ColorPickerView_alpha_selector)) {
                this.alpha_selector = typedArray.getFloat(R.styleable.ColorPickerView_alpha_selector, this.alpha_selector);
            }
            if (typedArray.hasValue(R.styleable.ColorPickerView_selector_size)) {
                this.selectorSize = typedArray.getDimensionPixelSize(R.styleable.ColorPickerView_selector_size, this.selectorSize);
            }
            if (typedArray.hasValue(R.styleable.ColorPickerView_alpha_flag)) {
                this.alpha_flag = typedArray.getFloat(R.styleable.ColorPickerView_alpha_flag, this.alpha_flag);
            }
            if (typedArray.hasValue(R.styleable.ColorPickerView_actionMode)) {
                n = typedArray.getInteger(R.styleable.ColorPickerView_actionMode, 0);
                if (n == 0) {
                    this.actionMode = ActionMode.ALWAYS;
                } else if (n == 1) {
                    this.actionMode = ActionMode.LAST;
                }
            }
            if (typedArray.hasValue(R.styleable.ColorPickerView_debounceDuration)) {
                this.debounceDuration = typedArray.getInteger(R.styleable.ColorPickerView_debounceDuration, (int)this.debounceDuration);
            }
            if (typedArray.hasValue(R.styleable.ColorPickerView_preferenceName)) {
                this.preferenceName = typedArray.getString(R.styleable.ColorPickerView_preferenceName);
            }
            if (!typedArray.hasValue(R.styleable.ColorPickerView_initialColor)) return;
            this.setInitialColor(typedArray.getColor(R.styleable.ColorPickerView_initialColor, -1));
            return;
        }
        finally {
            typedArray.recycle();
        }
    }

    private Point getCenterPoint(int n, int n2) {
        return new Point(n - this.selector.getMeasuredWidth() / 2, n2 - this.selector.getMeasuredHeight() / 2);
    }

    private void notifyColorChanged() {
        this.debounceHandler.removeCallbacksAndMessages(null);
        Runnable runnable = new Runnable(this){
            final ColorPickerView this$0;
            {
                this.this$0 = colorPickerView;
            }

            @Override
            public void run() {
                ColorPickerView colorPickerView = this.this$0;
                colorPickerView.fireColorListener(colorPickerView.getColor(), true);
                colorPickerView = this.this$0;
                colorPickerView.notifyToFlagView(colorPickerView.selectedPoint);
            }
        };
        this.debounceHandler.postDelayed(runnable, this.debounceDuration);
    }

    private void notifyToFlagView(Point object) {
        Point point = this.getCenterPoint(object.x, object.y);
        object = this.flagView;
        if (object != null) {
            if (object.getFlagMode() == FlagMode.ALWAYS) {
                this.flagView.visible();
            }
            int n = point.x - this.flagView.getWidth() / 2 + this.selector.getWidth() / 2;
            if (point.y - this.flagView.getHeight() > 0) {
                this.flagView.setRotation(0.0f);
                this.flagView.setX(n);
                this.flagView.setY(point.y - this.flagView.getHeight());
                this.flagView.onRefresh(this.getColorEnvelope());
            } else if (this.flagView.isFlipAble()) {
                this.flagView.setRotation(180.0f);
                this.flagView.setX(n);
                this.flagView.setY((float)(point.y + this.flagView.getHeight()) - (float)this.selector.getHeight() * 0.5f);
                this.flagView.onRefresh(this.getColorEnvelope());
            }
            if (n < 0) {
                this.flagView.setX(0.0f);
            }
            if (this.flagView.getMeasuredWidth() + n > this.getMeasuredWidth()) {
                this.flagView.setX(this.getMeasuredWidth() - this.flagView.getMeasuredWidth());
            }
        }
    }

    private void notifyToSlideBars() {
        AbstractSlider abstractSlider = this.alphaSlideBar;
        if (abstractSlider != null) {
            abstractSlider.notifyColor();
        }
        if ((abstractSlider = this.brightnessSlider) != null) {
            abstractSlider.notifyColor();
            if (this.brightnessSlider.assembleColor() != -1) {
                this.selectedColor = this.brightnessSlider.assembleColor();
            } else {
                abstractSlider = this.alphaSlideBar;
                if (abstractSlider != null) {
                    this.selectedColor = ((AlphaSlideBar)abstractSlider).assembleColor();
                }
            }
        }
    }

    private void onCreate() {
        ImageView imageView;
        this.setPadding(0, 0, 0, 0);
        this.palette = imageView = new ImageView(this.getContext());
        Drawable drawable2 = this.paletteDrawable;
        if (drawable2 != null) {
            imageView.setImageDrawable(drawable2);
        }
        imageView = new FrameLayout.LayoutParams(-1, -1);
        imageView.gravity = 17;
        this.addView((View)this.palette, (ViewGroup.LayoutParams)imageView);
        drawable2 = new ImageView(this.getContext());
        this.selector = drawable2;
        imageView = this.selectorDrawable;
        if (imageView != null) {
            drawable2.setImageDrawable((Drawable)imageView);
        } else {
            drawable2.setImageDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.wheel));
        }
        imageView = new FrameLayout.LayoutParams(-2, -2);
        if (this.selectorSize != 0) {
            imageView.width = SizeUtils.dp2Px(this.getContext(), this.selectorSize);
            imageView.height = SizeUtils.dp2Px(this.getContext(), this.selectorSize);
        }
        imageView.gravity = 17;
        this.addView((View)this.selector, (ViewGroup.LayoutParams)imageView);
        this.selector.setAlpha(this.alpha_selector);
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(this){
            final ColorPickerView this$0;
            {
                this.this$0 = colorPickerView;
            }

            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    this.this$0.getViewTreeObserver().removeGlobalOnLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
                } else {
                    this.this$0.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
                }
                this.this$0.onFinishInflated();
            }
        });
    }

    private void onFinishInflated() {
        if (this.getParent() != null && this.getParent() instanceof ViewGroup) {
            ((ViewGroup)this.getParent()).setClipChildren(false);
        }
        if (this.getPreferenceName() != null) {
            this.preferenceManager.restoreColorPickerData(this);
        } else {
            this.selectCenter();
        }
    }

    private boolean onTouchReceived(MotionEvent motionEvent) {
        int n;
        Point point = PointMapper.getColorPoint(this, new Point((int)motionEvent.getX(), (int)motionEvent.getY()));
        this.selectedPureColor = n = this.getColorFromBitmap(point.x, point.y);
        this.selectedColor = n;
        this.selectedPoint = PointMapper.getColorPoint(this, new Point(point.x, point.y));
        this.setCoordinate(point.x, point.y);
        if (this.actionMode == ActionMode.LAST) {
            if (motionEvent.getAction() == 1) {
                this.notifyColorChanged();
            }
        } else {
            this.notifyColorChanged();
        }
        return true;
    }

    public void attachAlphaSlider(AlphaSlideBar alphaSlideBar) {
        this.alphaSlideBar = alphaSlideBar;
        alphaSlideBar.attachColorPickerView(this);
        alphaSlideBar.notifyColor();
        if (this.getPreferenceName() != null) {
            alphaSlideBar.setPreferenceName(this.getPreferenceName());
        }
    }

    public void attachBrightnessSlider(BrightnessSlideBar brightnessSlideBar) {
        this.brightnessSlider = brightnessSlideBar;
        brightnessSlideBar.attachColorPickerView(this);
        brightnessSlideBar.notifyColor();
        if (this.getPreferenceName() != null) {
            brightnessSlideBar.setPreferenceName(this.getPreferenceName());
        }
    }

    public void fireColorListener(int n, boolean bl) {
        if (this.colorListener != null) {
            Object object;
            this.selectedColor = n;
            if (this.getAlphaSlideBar() != null) {
                this.getAlphaSlideBar().notifyColor();
                this.selectedColor = this.getAlphaSlideBar().assembleColor();
            }
            if (this.getBrightnessSlider() != null) {
                this.getBrightnessSlider().notifyColor();
                this.selectedColor = this.getBrightnessSlider().assembleColor();
            }
            if ((object = this.colorListener) instanceof ColorListener) {
                ((ColorListener)object).onColorSelected(this.selectedColor, bl);
            } else if (object instanceof ColorEnvelopeListener) {
                object = new ColorEnvelope(this.selectedColor);
                ((ColorEnvelopeListener)this.colorListener).onColorSelected((ColorEnvelope)object, bl);
            }
            object = this.flagView;
            if (object != null) {
                ((FlagView)((Object)object)).onRefresh(this.getColorEnvelope());
                this.invalidate();
            }
            if (this.VISIBLE_FLAG) {
                this.VISIBLE_FLAG = false;
                object = this.selector;
                if (object != null) {
                    object.setAlpha(this.alpha_selector);
                }
                if ((object = this.flagView) != null) {
                    object.setAlpha(this.alpha_flag);
                }
            }
        }
    }

    public ActionMode getActionMode() {
        return this.actionMode;
    }

    public float getAlpha() {
        return (float)Color.alpha((int)this.getColor()) / 255.0f;
    }

    public AlphaSlideBar getAlphaSlideBar() {
        return this.alphaSlideBar;
    }

    public BrightnessSlideBar getBrightnessSlider() {
        return this.brightnessSlider;
    }

    public int getColor() {
        return this.selectedColor;
    }

    public ColorEnvelope getColorEnvelope() {
        return new ColorEnvelope(this.getColor());
    }

    protected int getColorFromBitmap(float f, float f2) {
        Matrix matrix = new Matrix();
        this.palette.getImageMatrix().invert(matrix);
        float[] fArray = new float[]{f, f2};
        matrix.mapPoints(fArray);
        if (this.palette.getDrawable() != null && this.palette.getDrawable() instanceof BitmapDrawable && fArray[0] >= 0.0f && fArray[1] >= 0.0f && fArray[0] < (float)this.palette.getDrawable().getIntrinsicWidth() && fArray[1] < (float)this.palette.getDrawable().getIntrinsicHeight()) {
            this.invalidate();
            if (this.palette.getDrawable() instanceof ColorHsvPalette) {
                double d = Math.sqrt((f -= (float)this.getWidth() * 0.5f) * f + (f2 -= (float)this.getHeight() * 0.5f) * f2);
                float f3 = Math.min(this.getWidth(), this.getHeight());
                float[] fArray2 = fArray = new float[3];
                fArray[0] = 0.0f;
                fArray2[1] = 0.0f;
                fArray2[2] = 1.0f;
                fArray[0] = (float)(Math.atan2(f2, -f) / Math.PI * 180.0) + 180.0f;
                double d2 = f3 * 0.5f;
                Double.isNaN(d2);
                fArray[1] = Math.max(0.0f, Math.min(1.0f, (float)(d / d2)));
                return Color.HSVToColor((float[])fArray);
            }
            matrix = this.palette.getDrawable().getBounds();
            f = fArray[0] / (float)matrix.width();
            int n = (int)((float)((BitmapDrawable)this.palette.getDrawable()).getBitmap().getWidth() * f);
            f = fArray[1] / (float)matrix.height();
            int n2 = (int)((float)((BitmapDrawable)this.palette.getDrawable()).getBitmap().getHeight() * f);
            return ((BitmapDrawable)this.palette.getDrawable()).getBitmap().getPixel(n, n2);
        }
        return 0;
    }

    public long getDebounceDuration() {
        return this.debounceDuration;
    }

    public FlagView getFlagView() {
        return this.flagView;
    }

    public String getPreferenceName() {
        return this.preferenceName;
    }

    public int getPureColor() {
        return this.selectedPureColor;
    }

    public Point getSelectedPoint() {
        return this.selectedPoint;
    }

    public float getSelectorX() {
        return this.selector.getX() - (float)this.selector.getMeasuredWidth() * 0.5f;
    }

    public float getSelectorY() {
        return this.selector.getY() - (float)this.selector.getMeasuredHeight() * 0.5f;
    }

    public boolean isHuePalette() {
        boolean bl = this.palette.getDrawable() != null && this.palette.getDrawable() instanceof ColorHsvPalette;
        return bl;
    }

    public void moveSelectorPoint(int n, int n2, int n3) {
        this.selectedPureColor = n3;
        this.selectedColor = n3;
        this.selectedPoint = new Point(n, n2);
        this.setCoordinate(n, n2);
        this.fireColorListener(this.getColor(), false);
        this.notifyToFlagView(this.selectedPoint);
    }

    protected void onCreateByBuilder(Builder builder) {
        this.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(SizeUtils.dp2Px(this.getContext(), builder.width), SizeUtils.dp2Px(this.getContext(), builder.height)));
        this.paletteDrawable = builder.paletteDrawable;
        this.selectorDrawable = builder.selectorDrawable;
        this.alpha_selector = builder.alpha_selector;
        this.alpha_flag = builder.alpha_flag;
        this.selectorSize = builder.selectorSize;
        this.debounceDuration = builder.debounceDuration;
        this.onCreate();
        if (builder.colorPickerViewListener != null) {
            this.setColorListener(builder.colorPickerViewListener);
        }
        if (builder.alphaSlideBar != null) {
            this.attachAlphaSlider(builder.alphaSlideBar);
        }
        if (builder.brightnessSlider != null) {
            this.attachBrightnessSlider(builder.brightnessSlider);
        }
        if (builder.actionMode != null) {
            this.actionMode = builder.actionMode;
        }
        if (builder.flagView != null) {
            this.setFlagView(builder.flagView);
        }
        if (builder.preferenceName != null) {
            this.setPreferenceName(builder.preferenceName);
        }
        if (builder.initialColor != 0) {
            this.setInitialColor(builder.initialColor);
        }
        if (builder.lifecycleOwner != null) {
            this.setLifecycleOwner(builder.lifecycleOwner);
        }
    }

    @OnLifecycleEvent(value=Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        this.preferenceManager.saveColorPickerData(this);
    }

    protected void onSizeChanged(int n, int n2, int n3, int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (this.palette.getDrawable() == null) {
            Bitmap bitmap = Bitmap.createBitmap((int)n, (int)n2, (Bitmap.Config)Bitmap.Config.ARGB_8888);
            this.palette.setImageDrawable((Drawable)new ColorHsvPalette(this.getResources(), bitmap));
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            default: {
                this.selector.setPressed(false);
                return false;
            }
            case 0: 
            case 1: 
            case 2: 
        }
        if (this.getFlagView() != null) {
            this.getFlagView().receiveOnTouchEvent(motionEvent);
        }
        this.selector.setPressed(true);
        return this.onTouchReceived(motionEvent);
    }

    public void removeLifecycleOwner(LifecycleOwner lifecycleOwner) {
        lifecycleOwner.getLifecycle().removeObserver(this);
    }

    public void selectByHsvColor(int n) throws IllegalAccessException {
        if (this.palette.getDrawable() instanceof ColorHsvPalette) {
            float[] fArray = new float[3];
            Color.colorToHSV((int)n, (float[])fArray);
            float f = (float)this.getWidth() * 0.5f;
            float f2 = (float)this.getHeight() * 0.5f;
            float f3 = fArray[1] * Math.min(f, f2);
            double d = f3;
            double d2 = Math.cos(Math.toRadians(fArray[0]));
            Double.isNaN(d);
            double d3 = f;
            Double.isNaN(d3);
            int n2 = (int)(d * d2 + d3);
            d2 = -f3;
            d3 = Math.sin(Math.toRadians(fArray[0]));
            Double.isNaN(d2);
            d = f2;
            Double.isNaN(d);
            Point point = PointMapper.getColorPoint(this, new Point(n2, (int)(d2 * d3 + d)));
            this.selectedPureColor = n;
            this.selectedColor = n;
            this.selectedPoint = new Point(point.x, point.y);
            if (this.getAlphaSlideBar() != null) {
                this.getAlphaSlideBar().setSelectorPosition(this.getAlpha());
            }
            if (this.getBrightnessSlider() != null) {
                this.getBrightnessSlider().setSelectorPosition(fArray[2]);
            }
            this.setCoordinate(point.x, point.y);
            this.fireColorListener(this.getColor(), false);
            this.notifyToFlagView(this.selectedPoint);
            return;
        }
        throw new IllegalAccessException("selectByHsvColor(@ColorInt int color) can be called only when the palette is an instance of ColorHsvPalette. Use setHsvPaletteDrawable();");
    }

    public void selectByHsvColorRes(int n) throws IllegalAccessException {
        this.selectByHsvColor(ContextCompat.getColor(this.getContext(), n));
    }

    public void selectCenter() {
        this.setSelectorPoint(this.getMeasuredWidth() / 2, this.getMeasuredHeight() / 2);
    }

    public void setActionMode(ActionMode actionMode) {
        this.actionMode = actionMode;
    }

    public void setColorListener(ColorPickerViewListener colorPickerViewListener) {
        this.colorListener = colorPickerViewListener;
    }

    public void setCoordinate(int n, int n2) {
        ImageView imageView = this.selector;
        imageView.setX((float)n - (float)imageView.getMeasuredWidth() * 0.5f);
        imageView = this.selector;
        imageView.setY((float)n2 - (float)imageView.getMeasuredHeight() * 0.5f);
    }

    public void setDebounceDuration(long l) {
        this.debounceDuration = l;
    }

    public void setFlagView(FlagView flagView) {
        flagView.gone();
        this.addView((View)flagView);
        this.flagView = flagView;
        flagView.setAlpha(this.alpha_flag);
    }

    public void setHsvPaletteDrawable() {
        Bitmap bitmap = Bitmap.createBitmap((int)this.getWidth(), (int)this.getHeight(), (Bitmap.Config)Bitmap.Config.ARGB_8888);
        this.setPaletteDrawable((Drawable)new ColorHsvPalette(this.getResources(), bitmap));
    }

    public void setInitialColor(int n) {
        if (this.getPreferenceName() == null || this.getPreferenceName() != null && this.preferenceManager.getColor(this.getPreferenceName(), -1) == -1) {
            this.post(new Runnable(this, n){
                final ColorPickerView this$0;
                final int val$color;
                {
                    this.this$0 = colorPickerView;
                    this.val$color = n;
                }

                @Override
                public void run() {
                    try {
                        this.this$0.selectByHsvColor(this.val$color);
                    }
                    catch (IllegalAccessException illegalAccessException) {
                        illegalAccessException.printStackTrace();
                    }
                }
            });
        }
    }

    public void setInitialColorRes(int n) {
        this.setInitialColor(ContextCompat.getColor(this.getContext(), n));
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    public void setPaletteDrawable(Drawable object) {
        ImageView imageView;
        this.removeView((View)this.palette);
        this.palette = imageView = new ImageView(this.getContext());
        this.paletteDrawable = object;
        imageView.setImageDrawable(object);
        this.addView((View)this.palette);
        this.removeView((View)this.selector);
        this.addView((View)this.selector);
        this.selectedPureColor = -1;
        this.notifyToSlideBars();
        object = this.flagView;
        if (object != null) {
            this.removeView((View)object);
            this.addView((View)this.flagView);
        }
        if (!this.VISIBLE_FLAG) {
            this.VISIBLE_FLAG = true;
            object = this.selector;
            if (object != null) {
                this.alpha_selector = object.getAlpha();
                this.selector.setAlpha(0.0f);
            }
            if ((object = this.flagView) != null) {
                this.alpha_flag = object.getAlpha();
                this.flagView.setAlpha(0.0f);
            }
        }
    }

    public void setPreferenceName(String string2) {
        this.preferenceName = string2;
        AbstractSlider abstractSlider = this.alphaSlideBar;
        if (abstractSlider != null) {
            abstractSlider.setPreferenceName(string2);
        }
        if ((abstractSlider = this.brightnessSlider) != null) {
            abstractSlider.setPreferenceName(string2);
        }
    }

    public void setPureColor(int n) {
        this.selectedPureColor = n;
    }

    public void setSelectorDrawable(Drawable drawable2) {
        this.selector.setImageDrawable(drawable2);
    }

    public void setSelectorPoint(int n, int n2) {
        Point point = PointMapper.getColorPoint(this, new Point(n, n2));
        this.selectedPureColor = n = this.getColorFromBitmap(point.x, point.y);
        this.selectedColor = n;
        this.selectedPoint = new Point(point.x, point.y);
        this.setCoordinate(point.x, point.y);
        this.fireColorListener(this.getColor(), false);
        this.notifyToFlagView(this.selectedPoint);
    }

    public static class Builder {
        private ActionMode actionMode = ActionMode.ALWAYS;
        private AlphaSlideBar alphaSlideBar;
        private float alpha_flag = 1.0f;
        private float alpha_selector = 1.0f;
        private BrightnessSlideBar brightnessSlider;
        private ColorPickerViewListener colorPickerViewListener;
        private Context context;
        private int debounceDuration = 0;
        private FlagView flagView;
        @Dp
        private int height = -1;
        private int initialColor = 0;
        private LifecycleOwner lifecycleOwner;
        private Drawable paletteDrawable;
        private String preferenceName;
        private Drawable selectorDrawable;
        @Dp
        private int selectorSize = 0;
        @Dp
        private int width = -1;

        public Builder(Context context) {
            this.context = context;
        }

        public ColorPickerView build() {
            ColorPickerView colorPickerView = new ColorPickerView(this.context);
            colorPickerView.onCreateByBuilder(this);
            return colorPickerView;
        }

        public Builder setActionMode(ActionMode actionMode) {
            this.actionMode = actionMode;
            return this;
        }

        public Builder setAlphaSlideBar(AlphaSlideBar alphaSlideBar) {
            this.alphaSlideBar = alphaSlideBar;
            return this;
        }

        public Builder setBrightnessSlideBar(BrightnessSlideBar brightnessSlideBar) {
            this.brightnessSlider = brightnessSlideBar;
            return this;
        }

        public Builder setColorListener(ColorPickerViewListener colorPickerViewListener) {
            this.colorPickerViewListener = colorPickerViewListener;
            return this;
        }

        public Builder setDebounceDuration(int n) {
            this.debounceDuration = n;
            return this;
        }

        public Builder setFlagAlpha(float f) {
            this.alpha_flag = f;
            return this;
        }

        public Builder setFlagView(FlagView flagView) {
            this.flagView = flagView;
            return this;
        }

        public Builder setHeight(@Dp int n) {
            this.height = n;
            return this;
        }

        public Builder setInitialColor(int n) {
            this.initialColor = n;
            return this;
        }

        public Builder setInitialColorRes(int n) {
            this.initialColor = ContextCompat.getColor(this.context, n);
            return this;
        }

        public Builder setLifecycleOwner(LifecycleOwner lifecycleOwner) {
            this.lifecycleOwner = lifecycleOwner;
            return this;
        }

        public Builder setPaletteDrawable(Drawable drawable2) {
            this.paletteDrawable = drawable2;
            return this;
        }

        public Builder setPreferenceName(String string2) {
            this.preferenceName = string2;
            return this;
        }

        public Builder setSelectorAlpha(float f) {
            this.alpha_selector = f;
            return this;
        }

        public Builder setSelectorDrawable(Drawable drawable2) {
            this.selectorDrawable = drawable2;
            return this;
        }

        public Builder setSelectorSize(@Dp int n) {
            this.selectorSize = n;
            return this;
        }

        public Builder setWidth(@Dp int n) {
            this.width = n;
            return this;
        }
    }
}

