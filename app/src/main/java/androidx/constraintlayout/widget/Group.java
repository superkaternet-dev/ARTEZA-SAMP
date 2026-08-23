/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 */
package androidx.constraintlayout.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Group
extends ConstraintHelper {
    public Group(Context context) {
        super(context);
    }

    public Group(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Group(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    @Override
    protected void init(AttributeSet attributeSet) {
        super.init(attributeSet);
        this.mUseViewMeasure = false;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.applyLayoutFeatures();
    }

    public void setElevation(float f) {
        super.setElevation(f);
        this.applyLayoutFeatures();
    }

    public void setVisibility(int n) {
        super.setVisibility(n);
        this.applyLayoutFeatures();
    }

    @Override
    public void updatePostLayout(ConstraintLayout object) {
        object = (ConstraintLayout.LayoutParams)this.getLayoutParams();
        ((ConstraintLayout.LayoutParams)((Object)object)).widget.setWidth(0);
        ((ConstraintLayout.LayoutParams)((Object)object)).widget.setHeight(0);
    }
}

