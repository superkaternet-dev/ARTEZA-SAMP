/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.Bitmap
 *  android.graphics.ColorFilter
 *  android.graphics.Shader$TileMode
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.LayerDrawable
 *  android.net.Uri
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.widget.ImageView
 *  android.widget.ImageView$ScaleType
 */
package com.makeramen.roundedimageview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import com.makeramen.roundedimageview.R;
import com.makeramen.roundedimageview.RoundedDrawable;

public class RoundedImageView
extends ImageView {
    static final boolean $assertionsDisabled = false;
    public static final float DEFAULT_BORDER_WIDTH = 0.0f;
    public static final float DEFAULT_RADIUS = 0.0f;
    public static final Shader.TileMode DEFAULT_TILE_MODE = Shader.TileMode.CLAMP;
    private static final ImageView.ScaleType[] SCALE_TYPES = new ImageView.ScaleType[]{ImageView.ScaleType.MATRIX, ImageView.ScaleType.FIT_XY, ImageView.ScaleType.FIT_START, ImageView.ScaleType.FIT_CENTER, ImageView.ScaleType.FIT_END, ImageView.ScaleType.CENTER, ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.CENTER_INSIDE};
    public static final String TAG = "RoundedImageView";
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_MIRROR = 2;
    private static final int TILE_MODE_REPEAT = 1;
    private static final int TILE_MODE_UNDEFINED = -2;
    private Drawable mBackgroundDrawable;
    private int mBackgroundResource;
    private ColorStateList mBorderColor;
    private float mBorderWidth;
    private ColorFilter mColorFilter;
    private boolean mColorMod;
    private final float[] mCornerRadii;
    private Drawable mDrawable;
    private boolean mHasColorFilter;
    private boolean mIsOval;
    private boolean mMutateBackground;
    private int mResource;
    private ImageView.ScaleType mScaleType;
    private Shader.TileMode mTileModeX;
    private Shader.TileMode mTileModeY;

    public RoundedImageView(Context context) {
        super(context);
        this.mCornerRadii = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        this.mBorderColor = ColorStateList.valueOf((int)-16777216);
        this.mBorderWidth = 0.0f;
        this.mColorFilter = null;
        this.mColorMod = false;
        this.mHasColorFilter = false;
        this.mIsOval = false;
        this.mMutateBackground = false;
        context = DEFAULT_TILE_MODE;
        this.mTileModeX = context;
        this.mTileModeY = context;
    }

    public RoundedImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RoundedImageView(Context context, AttributeSet object, int n) {
        super(context, object, n);
        float f;
        Shader.TileMode tileMode;
        float[] fArray;
        float[] fArray2 = fArray = new float[4];
        fArray[0] = 0.0f;
        fArray2[1] = 0.0f;
        fArray2[2] = 0.0f;
        fArray2[3] = 0.0f;
        this.mCornerRadii = fArray;
        this.mBorderColor = ColorStateList.valueOf((int)-16777216);
        this.mBorderWidth = 0.0f;
        this.mColorFilter = null;
        this.mColorMod = false;
        this.mHasColorFilter = false;
        this.mIsOval = false;
        this.mMutateBackground = false;
        this.mTileModeX = tileMode = DEFAULT_TILE_MODE;
        this.mTileModeY = tileMode;
        context = context.obtainStyledAttributes(object, R.styleable.RoundedImageView, n, 0);
        n = context.getInt(R.styleable.RoundedImageView_android_scaleType, -1);
        if (n >= 0) {
            this.setScaleType(SCALE_TYPES[n]);
        } else {
            this.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        float f2 = context.getDimensionPixelSize(R.styleable.RoundedImageView_riv_corner_radius, -1);
        fArray[0] = context.getDimensionPixelSize(R.styleable.RoundedImageView_riv_corner_radius_top_left, -1);
        fArray[1] = context.getDimensionPixelSize(R.styleable.RoundedImageView_riv_corner_radius_top_right, -1);
        fArray[2] = context.getDimensionPixelSize(R.styleable.RoundedImageView_riv_corner_radius_bottom_right, -1);
        fArray[3] = context.getDimensionPixelSize(R.styleable.RoundedImageView_riv_corner_radius_bottom_left, -1);
        int n2 = 0;
        int n3 = fArray.length;
        for (n = 0; n < n3; ++n) {
            object = this.mCornerRadii;
            if (object[n] < 0.0f) {
                object[n] = (AttributeSet)0.0f;
                continue;
            }
            n2 = 1;
        }
        if (n2 == 0) {
            f = f2;
            if (f2 < 0.0f) {
                f = 0.0f;
            }
            n2 = this.mCornerRadii.length;
            for (n = 0; n < n2; ++n) {
                this.mCornerRadii[n] = f;
            }
        }
        this.mBorderWidth = f = (float)context.getDimensionPixelSize(R.styleable.RoundedImageView_riv_border_width, -1);
        if (f < 0.0f) {
            this.mBorderWidth = 0.0f;
        }
        object = context.getColorStateList(R.styleable.RoundedImageView_riv_border_color);
        this.mBorderColor = object;
        if (object == null) {
            this.mBorderColor = ColorStateList.valueOf((int)-16777216);
        }
        this.mMutateBackground = context.getBoolean(R.styleable.RoundedImageView_riv_mutate_background, false);
        this.mIsOval = context.getBoolean(R.styleable.RoundedImageView_riv_oval, false);
        n = context.getInt(R.styleable.RoundedImageView_riv_tile_mode, -2);
        if (n != -2) {
            this.setTileModeX(RoundedImageView.parseTileMode(n));
            this.setTileModeY(RoundedImageView.parseTileMode(n));
        }
        if ((n = context.getInt(R.styleable.RoundedImageView_riv_tile_mode_x, -2)) != -2) {
            this.setTileModeX(RoundedImageView.parseTileMode(n));
        }
        if ((n = context.getInt(R.styleable.RoundedImageView_riv_tile_mode_y, -2)) != -2) {
            this.setTileModeY(RoundedImageView.parseTileMode(n));
        }
        this.updateDrawableAttrs();
        this.updateBackgroundDrawableAttrs(true);
        if (this.mMutateBackground) {
            super.setBackgroundDrawable(this.mBackgroundDrawable);
        }
        context.recycle();
    }

    private void applyColorMod() {
        Drawable drawable2 = this.mDrawable;
        if (drawable2 != null && this.mColorMod) {
            this.mDrawable = drawable2 = drawable2.mutate();
            if (this.mHasColorFilter) {
                drawable2.setColorFilter(this.mColorFilter);
            }
        }
    }

    private static Shader.TileMode parseTileMode(int n) {
        switch (n) {
            default: {
                return null;
            }
            case 2: {
                return Shader.TileMode.MIRROR;
            }
            case 1: {
                return Shader.TileMode.REPEAT;
            }
            case 0: 
        }
        return Shader.TileMode.CLAMP;
    }

    private Drawable resolveBackgroundResource() {
        Object object = this.getResources();
        if (object == null) {
            return null;
        }
        Drawable drawable2 = null;
        int n = this.mBackgroundResource;
        Drawable drawable3 = drawable2;
        if (n != 0) {
            try {
                drawable3 = object.getDrawable(n);
            }
            catch (Exception exception) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Unable to find resource: ");
                ((StringBuilder)object).append(this.mBackgroundResource);
                Log.w((String)TAG, (String)((StringBuilder)object).toString(), (Throwable)exception);
                this.mBackgroundResource = 0;
                drawable3 = drawable2;
            }
        }
        return RoundedDrawable.fromDrawable(drawable3);
    }

    private Drawable resolveResource() {
        Object object = this.getResources();
        if (object == null) {
            return null;
        }
        Drawable drawable2 = null;
        int n = this.mResource;
        Drawable drawable3 = drawable2;
        if (n != 0) {
            try {
                drawable3 = object.getDrawable(n);
            }
            catch (Exception exception) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Unable to find resource: ");
                ((StringBuilder)object).append(this.mResource);
                Log.w((String)TAG, (String)((StringBuilder)object).toString(), (Throwable)exception);
                this.mResource = 0;
                drawable3 = drawable2;
            }
        }
        return RoundedDrawable.fromDrawable(drawable3);
    }

    private void updateAttrs(Drawable drawable2, ImageView.ScaleType object) {
        if (drawable2 == null) {
            return;
        }
        if (drawable2 instanceof RoundedDrawable) {
            ((RoundedDrawable)drawable2).setScaleType((ImageView.ScaleType)object).setBorderWidth(this.mBorderWidth).setBorderColor(this.mBorderColor).setOval(this.mIsOval).setTileModeX(this.mTileModeX).setTileModeY(this.mTileModeY);
            object = this.mCornerRadii;
            if (object != null) {
                ((RoundedDrawable)drawable2).setCornerRadius((float)object[0], (float)object[1], (float)object[2], (float)object[3]);
            }
            this.applyColorMod();
        } else if (drawable2 instanceof LayerDrawable) {
            drawable2 = (LayerDrawable)drawable2;
            int n = drawable2.getNumberOfLayers();
            for (int i = 0; i < n; ++i) {
                this.updateAttrs(drawable2.getDrawable(i), (ImageView.ScaleType)object);
            }
        }
    }

    private void updateBackgroundDrawableAttrs(boolean bl) {
        if (this.mMutateBackground) {
            if (bl) {
                this.mBackgroundDrawable = RoundedDrawable.fromDrawable(this.mBackgroundDrawable);
            }
            this.updateAttrs(this.mBackgroundDrawable, ImageView.ScaleType.FIT_XY);
        }
    }

    private void updateDrawableAttrs() {
        this.updateAttrs(this.mDrawable, this.mScaleType);
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.invalidate();
    }

    public int getBorderColor() {
        return this.mBorderColor.getDefaultColor();
    }

    public ColorStateList getBorderColors() {
        return this.mBorderColor;
    }

    public float getBorderWidth() {
        return this.mBorderWidth;
    }

    public float getCornerRadius() {
        return this.getMaxCornerRadius();
    }

    public float getCornerRadius(int n) {
        return this.mCornerRadii[n];
    }

    public float getMaxCornerRadius() {
        float f = 0.0f;
        float[] fArray = this.mCornerRadii;
        int n = fArray.length;
        for (int i = 0; i < n; ++i) {
            f = Math.max(fArray[i], f);
        }
        return f;
    }

    public ImageView.ScaleType getScaleType() {
        return this.mScaleType;
    }

    public Shader.TileMode getTileModeX() {
        return this.mTileModeX;
    }

    public Shader.TileMode getTileModeY() {
        return this.mTileModeY;
    }

    public boolean isOval() {
        return this.mIsOval;
    }

    public void mutateBackground(boolean bl) {
        if (this.mMutateBackground == bl) {
            return;
        }
        this.mMutateBackground = bl;
        this.updateBackgroundDrawableAttrs(true);
        this.invalidate();
    }

    public boolean mutatesBackground() {
        return this.mMutateBackground;
    }

    public void setBackground(Drawable drawable2) {
        this.setBackgroundDrawable(drawable2);
    }

    public void setBackgroundColor(int n) {
        ColorDrawable colorDrawable = new ColorDrawable(n);
        this.mBackgroundDrawable = colorDrawable;
        this.setBackgroundDrawable((Drawable)colorDrawable);
    }

    @Deprecated
    public void setBackgroundDrawable(Drawable drawable2) {
        this.mBackgroundDrawable = drawable2;
        this.updateBackgroundDrawableAttrs(true);
        super.setBackgroundDrawable(this.mBackgroundDrawable);
    }

    public void setBackgroundResource(int n) {
        if (this.mBackgroundResource != n) {
            Drawable drawable2;
            this.mBackgroundResource = n;
            this.mBackgroundDrawable = drawable2 = this.resolveBackgroundResource();
            this.setBackgroundDrawable(drawable2);
        }
    }

    public void setBorderColor(int n) {
        this.setBorderColor(ColorStateList.valueOf((int)n));
    }

    public void setBorderColor(ColorStateList colorStateList) {
        if (this.mBorderColor.equals(colorStateList)) {
            return;
        }
        if (colorStateList == null) {
            colorStateList = ColorStateList.valueOf((int)-16777216);
        }
        this.mBorderColor = colorStateList;
        this.updateDrawableAttrs();
        this.updateBackgroundDrawableAttrs(false);
        if (this.mBorderWidth > 0.0f) {
            this.invalidate();
        }
    }

    public void setBorderWidth(float f) {
        if (this.mBorderWidth == f) {
            return;
        }
        this.mBorderWidth = f;
        this.updateDrawableAttrs();
        this.updateBackgroundDrawableAttrs(false);
        this.invalidate();
    }

    public void setBorderWidth(int n) {
        this.setBorderWidth(this.getResources().getDimension(n));
    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (this.mColorFilter != colorFilter) {
            this.mColorFilter = colorFilter;
            this.mHasColorFilter = true;
            this.mColorMod = true;
            this.applyColorMod();
            this.invalidate();
        }
    }

    public void setCornerRadius(float f) {
        this.setCornerRadius(f, f, f, f);
    }

    public void setCornerRadius(float f, float f2, float f3, float f4) {
        float[] fArray = this.mCornerRadii;
        if (fArray[0] == f && fArray[1] == f2 && fArray[2] == f4 && fArray[3] == f3) {
            return;
        }
        fArray[0] = f;
        fArray[1] = f2;
        fArray[3] = f3;
        fArray[2] = f4;
        this.updateDrawableAttrs();
        this.updateBackgroundDrawableAttrs(false);
        this.invalidate();
    }

    public void setCornerRadius(int n, float f) {
        float[] fArray = this.mCornerRadii;
        if (fArray[n] == f) {
            return;
        }
        fArray[n] = f;
        this.updateDrawableAttrs();
        this.updateBackgroundDrawableAttrs(false);
        this.invalidate();
    }

    public void setCornerRadiusDimen(int n) {
        float f = this.getResources().getDimension(n);
        this.setCornerRadius(f, f, f, f);
    }

    public void setCornerRadiusDimen(int n, int n2) {
        this.setCornerRadius(n, this.getResources().getDimensionPixelSize(n2));
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.mResource = 0;
        this.mDrawable = RoundedDrawable.fromBitmap(bitmap);
        this.updateDrawableAttrs();
        super.setImageDrawable(this.mDrawable);
    }

    public void setImageDrawable(Drawable drawable2) {
        this.mResource = 0;
        this.mDrawable = RoundedDrawable.fromDrawable(drawable2);
        this.updateDrawableAttrs();
        super.setImageDrawable(this.mDrawable);
    }

    public void setImageResource(int n) {
        if (this.mResource != n) {
            this.mResource = n;
            this.mDrawable = this.resolveResource();
            this.updateDrawableAttrs();
            super.setImageDrawable(this.mDrawable);
        }
    }

    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        this.setImageDrawable(this.getDrawable());
    }

    public void setOval(boolean bl) {
        this.mIsOval = bl;
        this.updateDrawableAttrs();
        this.updateBackgroundDrawableAttrs(false);
        this.invalidate();
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        if (scaleType != null) {
            if (this.mScaleType != scaleType) {
                this.mScaleType = scaleType;
                switch (1.$SwitchMap$android$widget$ImageView$ScaleType[scaleType.ordinal()]) {
                    default: {
                        super.setScaleType(scaleType);
                        break;
                    }
                    case 1: 
                    case 2: 
                    case 3: 
                    case 4: 
                    case 5: 
                    case 6: 
                    case 7: {
                        super.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                }
                this.updateDrawableAttrs();
                this.updateBackgroundDrawableAttrs(false);
                this.invalidate();
            }
            return;
        }
        throw new AssertionError();
    }

    public void setTileModeX(Shader.TileMode tileMode) {
        if (this.mTileModeX == tileMode) {
            return;
        }
        this.mTileModeX = tileMode;
        this.updateDrawableAttrs();
        this.updateBackgroundDrawableAttrs(false);
        this.invalidate();
    }

    public void setTileModeY(Shader.TileMode tileMode) {
        if (this.mTileModeY == tileMode) {
            return;
        }
        this.mTileModeY = tileMode;
        this.updateDrawableAttrs();
        this.updateBackgroundDrawableAttrs(false);
        this.invalidate();
    }
}

