/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.os.SystemClock
 */
package com.google.android.gms.internal.base;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import com.google.android.gms.internal.base.zag;
import com.google.android.gms.internal.base.zah;

public final class zai
extends Drawable
implements Drawable.Callback {
    private int zaa = 0;
    private long zab;
    private int zac;
    private int zad = 255;
    private int zae;
    private int zaf = 0;
    private boolean zag = true;
    private boolean zah;
    private zah zai;
    private Drawable zaj;
    private Drawable zak;
    private boolean zal;
    private boolean zam;
    private boolean zan;
    private int zao;

    public zai(Drawable object, Drawable drawable2) {
        this(null);
        Drawable drawable3 = object;
        if (object == null) {
            drawable3 = com.google.android.gms.internal.base.zag.zaa();
        }
        this.zaj = drawable3;
        drawable3.setCallback((Drawable.Callback)this);
        object = this.zai;
        int n = object.zab;
        object.zab = drawable3.getChangingConfigurations() | n;
        if (drawable2 == null) {
            drawable2 = com.google.android.gms.internal.base.zag.zaa();
        }
        this.zak = drawable2;
        drawable2.setCallback((Drawable.Callback)this);
        object = this.zai;
        n = object.zab;
        object.zab = drawable2.getChangingConfigurations() | n;
    }

    zai(zah zah2) {
        this.zai = new zah(zah2);
    }

    public final void draw(Canvas canvas) {
        Drawable drawable2;
        Drawable drawable3;
        boolean bl;
        int n;
        block9: {
            int n2;
            block11: {
                block12: {
                    block10: {
                        int n3 = this.zaa;
                        n2 = 1;
                        n = 1;
                        int n4 = 0;
                        switch (n3) {
                            default: {
                                break;
                            }
                            case 2: {
                                if (this.zab < 0L) break;
                                float f = (float)(SystemClock.uptimeMillis() - this.zab) / (float)this.zae;
                                n2 = f >= 1.0f ? n : 0;
                                if (n2 != 0) {
                                    this.zaa = 0;
                                }
                                f = Math.min(f, 1.0f);
                                this.zaf = (int)((float)this.zac * f + 0.0f);
                                break;
                            }
                            case 1: {
                                this.zab = SystemClock.uptimeMillis();
                                this.zaa = 2;
                                n2 = 0;
                            }
                        }
                        n = this.zaf;
                        bl = this.zag;
                        drawable3 = this.zaj;
                        drawable2 = this.zak;
                        if (n2 == 0) break block9;
                        if (!bl) break block10;
                        if (n != 0) break block11;
                        n2 = n4;
                        break block12;
                    }
                    n2 = n;
                }
                drawable3.draw(canvas);
                n = n2;
            }
            n2 = this.zad;
            if (n == n2) {
                drawable2.setAlpha(n2);
                drawable2.draw(canvas);
            }
            return;
        }
        if (bl) {
            drawable3.setAlpha(this.zad - n);
        }
        drawable3.draw(canvas);
        if (bl) {
            drawable3.setAlpha(this.zad);
        }
        if (n > 0) {
            drawable2.setAlpha(n);
            drawable2.draw(canvas);
            drawable2.setAlpha(this.zad);
        }
        this.invalidateSelf();
    }

    public final int getChangingConfigurations() {
        int n = super.getChangingConfigurations();
        zah zah2 = this.zai;
        return n | zah2.zaa | zah2.zab;
    }

    public final Drawable.ConstantState getConstantState() {
        if (this.zac()) {
            this.zai.zaa = this.getChangingConfigurations();
            return this.zai;
        }
        return null;
    }

    public final int getIntrinsicHeight() {
        return Math.max(this.zaj.getIntrinsicHeight(), this.zak.getIntrinsicHeight());
    }

    public final int getIntrinsicWidth() {
        return Math.max(this.zaj.getIntrinsicWidth(), this.zak.getIntrinsicWidth());
    }

    public final int getOpacity() {
        if (!this.zan) {
            this.zao = Drawable.resolveOpacity((int)this.zaj.getOpacity(), (int)this.zak.getOpacity());
            this.zan = true;
        }
        return this.zao;
    }

    public final void invalidateDrawable(Drawable drawable2) {
        drawable2 = this.getCallback();
        if (drawable2 != null) {
            drawable2.invalidateDrawable((Drawable)this);
        }
    }

    public final Drawable mutate() {
        if (!this.zah && super.mutate() == this) {
            if (this.zac()) {
                this.zaj.mutate();
                this.zak.mutate();
                this.zah = true;
            } else {
                throw new IllegalStateException("One or more children of this LayerDrawable does not have constant state; this drawable cannot be mutated.");
            }
        }
        return this;
    }

    protected final void onBoundsChange(Rect rect) {
        this.zaj.setBounds(rect);
        this.zak.setBounds(rect);
    }

    public final void scheduleDrawable(Drawable drawable2, Runnable runnable, long l) {
        drawable2 = this.getCallback();
        if (drawable2 != null) {
            drawable2.scheduleDrawable((Drawable)this, runnable, l);
        }
    }

    public final void setAlpha(int n) {
        if (this.zaf == this.zad) {
            this.zaf = n;
        }
        this.zad = n;
        this.invalidateSelf();
    }

    public final void setColorFilter(ColorFilter colorFilter) {
        this.zaj.setColorFilter(colorFilter);
        this.zak.setColorFilter(colorFilter);
    }

    public final void unscheduleDrawable(Drawable drawable2, Runnable runnable) {
        drawable2 = this.getCallback();
        if (drawable2 != null) {
            drawable2.unscheduleDrawable((Drawable)this, runnable);
        }
    }

    public final Drawable zaa() {
        return this.zak;
    }

    public final void zab(int n) {
        this.zac = this.zad;
        this.zaf = 0;
        this.zae = 250;
        this.zaa = 1;
        this.invalidateSelf();
    }

    public final boolean zac() {
        if (!this.zal) {
            boolean bl;
            Drawable.ConstantState constantState = this.zaj.getConstantState();
            boolean bl2 = bl = false;
            if (constantState != null) {
                bl2 = bl;
                if (this.zak.getConstantState() != null) {
                    bl2 = true;
                }
            }
            this.zam = bl2;
            this.zal = true;
        }
        return this.zam;
    }
}

