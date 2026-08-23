/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.TypeEvaluator
 *  android.graphics.Canvas
 *  android.graphics.drawable.Drawable
 *  android.util.Property
 */
package com.google.android.material.circularreveal;

import android.animation.TypeEvaluator;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Property;
import com.google.android.material.circularreveal.CircularRevealHelper;
import com.google.android.material.math.MathUtils;

public interface CircularRevealWidget
extends CircularRevealHelper.Delegate {
    public void buildCircularRevealCache();

    public void destroyCircularRevealCache();

    public void draw(Canvas var1);

    public Drawable getCircularRevealOverlayDrawable();

    public int getCircularRevealScrimColor();

    public RevealInfo getRevealInfo();

    public boolean isOpaque();

    public void setCircularRevealOverlayDrawable(Drawable var1);

    public void setCircularRevealScrimColor(int var1);

    public void setRevealInfo(RevealInfo var1);

    public static class CircularRevealEvaluator
    implements TypeEvaluator<RevealInfo> {
        public static final TypeEvaluator<RevealInfo> CIRCULAR_REVEAL = new CircularRevealEvaluator();
        private final RevealInfo revealInfo = new RevealInfo();

        public RevealInfo evaluate(float f, RevealInfo revealInfo, RevealInfo revealInfo2) {
            this.revealInfo.set(MathUtils.lerp(revealInfo.centerX, revealInfo2.centerX, f), MathUtils.lerp(revealInfo.centerY, revealInfo2.centerY, f), MathUtils.lerp(revealInfo.radius, revealInfo2.radius, f));
            return this.revealInfo;
        }
    }

    public static class CircularRevealProperty
    extends Property<CircularRevealWidget, RevealInfo> {
        public static final Property<CircularRevealWidget, RevealInfo> CIRCULAR_REVEAL = new CircularRevealProperty("circularReveal");

        private CircularRevealProperty(String string2) {
            super(RevealInfo.class, string2);
        }

        public RevealInfo get(CircularRevealWidget circularRevealWidget) {
            return circularRevealWidget.getRevealInfo();
        }

        public void set(CircularRevealWidget circularRevealWidget, RevealInfo revealInfo) {
            circularRevealWidget.setRevealInfo(revealInfo);
        }
    }

    public static class CircularRevealScrimColorProperty
    extends Property<CircularRevealWidget, Integer> {
        public static final Property<CircularRevealWidget, Integer> CIRCULAR_REVEAL_SCRIM_COLOR = new CircularRevealScrimColorProperty("circularRevealScrimColor");

        private CircularRevealScrimColorProperty(String string2) {
            super(Integer.class, string2);
        }

        public Integer get(CircularRevealWidget circularRevealWidget) {
            return circularRevealWidget.getCircularRevealScrimColor();
        }

        public void set(CircularRevealWidget circularRevealWidget, Integer n) {
            circularRevealWidget.setCircularRevealScrimColor(n);
        }
    }

    public static class RevealInfo {
        public static final float INVALID_RADIUS = Float.MAX_VALUE;
        public float centerX;
        public float centerY;
        public float radius;

        private RevealInfo() {
        }

        public RevealInfo(float f, float f2, float f3) {
            this.centerX = f;
            this.centerY = f2;
            this.radius = f3;
        }

        public RevealInfo(RevealInfo revealInfo) {
            this(revealInfo.centerX, revealInfo.centerY, revealInfo.radius);
        }

        public boolean isInvalid() {
            boolean bl = this.radius == Float.MAX_VALUE;
            return bl;
        }

        public void set(float f, float f2, float f3) {
            this.centerX = f;
            this.centerY = f2;
            this.radius = f3;
        }

        public void set(RevealInfo revealInfo) {
            this.set(revealInfo.centerX, revealInfo.centerY, revealInfo.radius);
        }
    }
}

