/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.SeekBar
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import androidx.appcompat.R;
import androidx.appcompat.widget.AppCompatSeekBarHelper;
import androidx.appcompat.widget.ThemeUtils;

public class AppCompatSeekBar
extends SeekBar {
    private final AppCompatSeekBarHelper mAppCompatSeekBarHelper;

    public AppCompatSeekBar(Context context) {
        this(context, null);
    }

    public AppCompatSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.seekBarStyle);
    }

    public AppCompatSeekBar(Context object, AttributeSet attributeSet, int n) {
        super((Context)object, attributeSet, n);
        ThemeUtils.checkAppCompatTheme((View)this, this.getContext());
        object = new AppCompatSeekBarHelper(this);
        this.mAppCompatSeekBarHelper = object;
        ((AppCompatSeekBarHelper)object).loadFromAttributes(attributeSet, n);
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.mAppCompatSeekBarHelper.drawableStateChanged();
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        this.mAppCompatSeekBarHelper.jumpDrawablesToCurrentState();
    }

    protected void onDraw(Canvas canvas) {
        synchronized (this) {
            super.onDraw(canvas);
            this.mAppCompatSeekBarHelper.drawTickMarks(canvas);
            return;
        }
    }
}

