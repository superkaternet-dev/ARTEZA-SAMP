/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View$MeasureSpec
 *  android.widget.ImageView
 */
package androidx.preference.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import androidx.preference.R;

public class PreferenceImageView
extends ImageView {
    private int mMaxHeight;
    private int mMaxWidth = Integer.MAX_VALUE;

    public PreferenceImageView(Context context) {
        this(context, null);
    }

    public PreferenceImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PreferenceImageView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.mMaxHeight = Integer.MAX_VALUE;
        context = context.obtainStyledAttributes(attributeSet, R.styleable.PreferenceImageView, n, 0);
        this.setMaxWidth(context.getDimensionPixelSize(R.styleable.PreferenceImageView_maxWidth, Integer.MAX_VALUE));
        this.setMaxHeight(context.getDimensionPixelSize(R.styleable.PreferenceImageView_maxHeight, Integer.MAX_VALUE));
        context.recycle();
    }

    public int getMaxHeight() {
        return this.mMaxHeight;
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    protected void onMeasure(int n, int n2) {
        int n3;
        block8: {
            int n4;
            block9: {
                int n5;
                int n6;
                block7: {
                    block5: {
                        block6: {
                            block4: {
                                n4 = View.MeasureSpec.getMode((int)n);
                                if (n4 == Integer.MIN_VALUE) break block4;
                                n3 = n;
                                if (n4 != 0) break block5;
                            }
                            n6 = View.MeasureSpec.getSize((int)n);
                            n5 = this.getMaxWidth();
                            n3 = n;
                            if (n5 == Integer.MAX_VALUE) break block5;
                            if (n5 < n6) break block6;
                            n3 = n;
                            if (n4 != 0) break block5;
                        }
                        n3 = View.MeasureSpec.makeMeasureSpec((int)n5, (int)Integer.MIN_VALUE);
                    }
                    if ((n5 = View.MeasureSpec.getMode((int)n2)) == Integer.MIN_VALUE) break block7;
                    n = n2;
                    if (n5 != 0) break block8;
                }
                n6 = View.MeasureSpec.getSize((int)n2);
                n4 = this.getMaxHeight();
                n = n2;
                if (n4 == Integer.MAX_VALUE) break block8;
                if (n4 < n6) break block9;
                n = n2;
                if (n5 != 0) break block8;
            }
            n = View.MeasureSpec.makeMeasureSpec((int)n4, (int)Integer.MIN_VALUE);
        }
        super.onMeasure(n3, n);
    }

    public void setMaxHeight(int n) {
        this.mMaxHeight = n;
        super.setMaxHeight(n);
    }

    public void setMaxWidth(int n) {
        this.mMaxWidth = n;
        super.setMaxWidth(n);
    }
}

