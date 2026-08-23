/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Outline
 *  android.graphics.Path
 *  android.graphics.Path$Direction
 *  android.graphics.RectF
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.LayerDrawable
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewOutlineProvider
 *  android.widget.ImageView
 */
package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.constraintlayout.widget.R;

public class ImageFilterButton
extends AppCompatImageButton {
    private float mCrossfade = 0.0f;
    private ImageFilterView.ImageMatrix mImageMatrix = new ImageFilterView.ImageMatrix();
    LayerDrawable mLayer;
    Drawable[] mLayers;
    private boolean mOverlay = true;
    private Path mPath;
    RectF mRect;
    private float mRound = Float.NaN;
    private float mRoundPercent = 0.0f;
    ViewOutlineProvider mViewOutlineProvider;

    public ImageFilterButton(Context context) {
        super(context);
        this.init(context, null);
    }

    public ImageFilterButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.init(context, attributeSet);
    }

    public ImageFilterButton(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.init(context, attributeSet);
    }

    private void init(Context context, AttributeSet drawableArray) {
        this.setPadding(0, 0, 0, 0);
        if (drawableArray != null) {
            drawableArray = this.getContext().obtainStyledAttributes((AttributeSet)drawableArray, R.styleable.ImageFilterView);
            int n = drawableArray.getIndexCount();
            context = drawableArray.getDrawable(R.styleable.ImageFilterView_altSrc);
            for (int i = 0; i < n; ++i) {
                int n2 = drawableArray.getIndex(i);
                if (n2 == R.styleable.ImageFilterView_crossfade) {
                    this.mCrossfade = drawableArray.getFloat(n2, 0.0f);
                    continue;
                }
                if (n2 == R.styleable.ImageFilterView_warmth) {
                    this.setWarmth(drawableArray.getFloat(n2, 0.0f));
                    continue;
                }
                if (n2 == R.styleable.ImageFilterView_saturation) {
                    this.setSaturation(drawableArray.getFloat(n2, 0.0f));
                    continue;
                }
                if (n2 == R.styleable.ImageFilterView_contrast) {
                    this.setContrast(drawableArray.getFloat(n2, 0.0f));
                    continue;
                }
                if (n2 == R.styleable.ImageFilterView_round) {
                    if (Build.VERSION.SDK_INT < 21) continue;
                    this.setRound(drawableArray.getDimension(n2, 0.0f));
                    continue;
                }
                if (n2 == R.styleable.ImageFilterView_roundPercent) {
                    if (Build.VERSION.SDK_INT < 21) continue;
                    this.setRoundPercent(drawableArray.getFloat(n2, 0.0f));
                    continue;
                }
                if (n2 != R.styleable.ImageFilterView_overlay) continue;
                this.setOverlay(drawableArray.getBoolean(n2, this.mOverlay));
            }
            drawableArray.recycle();
            if (context != null) {
                drawableArray = new Drawable[2];
                this.mLayers = drawableArray;
                drawableArray[0] = this.getDrawable();
                this.mLayers[1] = context;
                context = new LayerDrawable(this.mLayers);
                this.mLayer = context;
                context.getDrawable(1).setAlpha((int)(this.mCrossfade * 255.0f));
                super.setImageDrawable((Drawable)this.mLayer);
            }
        }
    }

    private void setOverlay(boolean bl) {
        this.mOverlay = bl;
    }

    public void draw(Canvas canvas) {
        boolean bl;
        boolean bl2 = bl = false;
        if (Build.VERSION.SDK_INT < 21) {
            bl2 = bl;
            if (this.mRound != 0.0f) {
                bl2 = bl;
                if (this.mPath != null) {
                    bl2 = true;
                    canvas.save();
                    canvas.clipPath(this.mPath);
                }
            }
        }
        super.draw(canvas);
        if (bl2) {
            canvas.restore();
        }
    }

    public float getContrast() {
        return this.mImageMatrix.mContrast;
    }

    public float getCrossfade() {
        return this.mCrossfade;
    }

    public float getRound() {
        return this.mRound;
    }

    public float getRoundPercent() {
        return this.mRoundPercent;
    }

    public float getSaturation() {
        return this.mImageMatrix.mSaturation;
    }

    public float getWarmth() {
        return this.mImageMatrix.mWarmth;
    }

    public void setBrightness(float f) {
        this.mImageMatrix.mBrightness = f;
        this.mImageMatrix.updateMatrix((ImageView)this);
    }

    public void setContrast(float f) {
        this.mImageMatrix.mContrast = f;
        this.mImageMatrix.updateMatrix((ImageView)this);
    }

    public void setCrossfade(float f) {
        this.mCrossfade = f;
        if (this.mLayers != null) {
            if (!this.mOverlay) {
                this.mLayer.getDrawable(0).setAlpha((int)((1.0f - this.mCrossfade) * 255.0f));
            }
            this.mLayer.getDrawable(1).setAlpha((int)(this.mCrossfade * 255.0f));
            super.setImageDrawable((Drawable)this.mLayer);
        }
    }

    public void setRound(float f) {
        if (Float.isNaN(f)) {
            this.mRound = f;
            f = this.mRoundPercent;
            this.mRoundPercent = -1.0f;
            this.setRoundPercent(f);
            return;
        }
        boolean bl = this.mRound != f;
        this.mRound = f;
        if (f != 0.0f) {
            ViewOutlineProvider viewOutlineProvider;
            if (this.mPath == null) {
                this.mPath = new Path();
            }
            if (this.mRect == null) {
                this.mRect = new RectF();
            }
            if (Build.VERSION.SDK_INT >= 21) {
                if (this.mViewOutlineProvider == null) {
                    this.mViewOutlineProvider = viewOutlineProvider = new ViewOutlineProvider(this){
                        final ImageFilterButton this$0;
                        {
                            this.this$0 = imageFilterButton;
                        }

                        public void getOutline(View view, Outline outline) {
                            outline.setRoundRect(0, 0, this.this$0.getWidth(), this.this$0.getHeight(), this.this$0.mRound);
                        }
                    };
                    this.setOutlineProvider(viewOutlineProvider);
                }
                this.setClipToOutline(true);
            }
            int n = this.getWidth();
            int n2 = this.getHeight();
            this.mRect.set(0.0f, 0.0f, (float)n, (float)n2);
            this.mPath.reset();
            viewOutlineProvider = this.mPath;
            RectF rectF = this.mRect;
            f = this.mRound;
            viewOutlineProvider.addRoundRect(rectF, f, f, Path.Direction.CW);
        } else if (Build.VERSION.SDK_INT >= 21) {
            this.setClipToOutline(false);
        }
        if (bl && Build.VERSION.SDK_INT >= 21) {
            this.invalidateOutline();
        }
    }

    public void setRoundPercent(float f) {
        boolean bl = this.mRoundPercent != f;
        this.mRoundPercent = f;
        if (f != 0.0f) {
            if (this.mPath == null) {
                this.mPath = new Path();
            }
            if (this.mRect == null) {
                this.mRect = new RectF();
            }
            if (Build.VERSION.SDK_INT >= 21) {
                if (this.mViewOutlineProvider == null) {
                    ViewOutlineProvider viewOutlineProvider;
                    this.mViewOutlineProvider = viewOutlineProvider = new ViewOutlineProvider(this){
                        final ImageFilterButton this$0;
                        {
                            this.this$0 = imageFilterButton;
                        }

                        public void getOutline(View view, Outline outline) {
                            int n = this.this$0.getWidth();
                            int n2 = this.this$0.getHeight();
                            outline.setRoundRect(0, 0, n, n2, (float)Math.min(n, n2) * this.this$0.mRoundPercent / 2.0f);
                        }
                    };
                    this.setOutlineProvider(viewOutlineProvider);
                }
                this.setClipToOutline(true);
            }
            int n = this.getWidth();
            int n2 = this.getHeight();
            f = (float)Math.min(n, n2) * this.mRoundPercent / 2.0f;
            this.mRect.set(0.0f, 0.0f, (float)n, (float)n2);
            this.mPath.reset();
            this.mPath.addRoundRect(this.mRect, f, f, Path.Direction.CW);
        } else if (Build.VERSION.SDK_INT >= 21) {
            this.setClipToOutline(false);
        }
        if (bl && Build.VERSION.SDK_INT >= 21) {
            this.invalidateOutline();
        }
    }

    public void setSaturation(float f) {
        this.mImageMatrix.mSaturation = f;
        this.mImageMatrix.updateMatrix((ImageView)this);
    }

    public void setWarmth(float f) {
        this.mImageMatrix.mWarmth = f;
        this.mImageMatrix.updateMatrix((ImageView)this);
    }
}

