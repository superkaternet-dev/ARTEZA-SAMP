/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 */
package androidx.legacy.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

@Deprecated
public class Space
extends View {
    @Deprecated
    public Space(Context context) {
        this(context, null);
    }

    @Deprecated
    public Space(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Deprecated
    public Space(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        if (this.getVisibility() == 0) {
            this.setVisibility(4);
        }
    }

    private static int getDefaultSize2(int n, int n2) {
        int n3 = n;
        int n4 = View.MeasureSpec.getMode((int)n2);
        n2 = View.MeasureSpec.getSize((int)n2);
        switch (n4) {
            default: {
                n = n3;
                break;
            }
            case 0x40000000: {
                n = n2;
                break;
            }
            case 0: {
                break;
            }
            case -2147483648: {
                n = Math.min(n, n2);
            }
        }
        return n;
    }

    @Deprecated
    public void draw(Canvas canvas) {
    }

    @Deprecated
    protected void onMeasure(int n, int n2) {
        this.setMeasuredDimension(Space.getDefaultSize2(this.getSuggestedMinimumWidth(), n), Space.getDefaultSize2(this.getSuggestedMinimumHeight(), n2));
    }
}

