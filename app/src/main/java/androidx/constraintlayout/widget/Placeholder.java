/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Paint$Align
 *  android.graphics.Rect
 *  android.graphics.Typeface
 *  android.util.AttributeSet
 *  android.view.View
 */
package androidx.constraintlayout.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.R;

public class Placeholder
extends View {
    private View mContent = null;
    private int mContentId = -1;
    private int mEmptyVisibility = 4;

    public Placeholder(Context context) {
        super(context);
        this.init(null);
    }

    public Placeholder(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.init(attributeSet);
    }

    public Placeholder(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.init(attributeSet);
    }

    public Placeholder(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n);
        this.init(attributeSet);
    }

    private void init(AttributeSet attributeSet) {
        super.setVisibility(this.mEmptyVisibility);
        this.mContentId = -1;
        if (attributeSet != null) {
            attributeSet = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.ConstraintLayout_placeholder);
            int n = attributeSet.getIndexCount();
            for (int i = 0; i < n; ++i) {
                int n2 = attributeSet.getIndex(i);
                if (n2 == R.styleable.ConstraintLayout_placeholder_content) {
                    this.mContentId = attributeSet.getResourceId(n2, this.mContentId);
                    continue;
                }
                if (n2 != R.styleable.ConstraintLayout_placeholder_placeholder_emptyVisibility) continue;
                this.mEmptyVisibility = attributeSet.getInt(n2, this.mEmptyVisibility);
            }
            attributeSet.recycle();
        }
    }

    public View getContent() {
        return this.mContent;
    }

    public int getEmptyVisibility() {
        return this.mEmptyVisibility;
    }

    public void onDraw(Canvas canvas) {
        if (this.isInEditMode()) {
            canvas.drawRGB(223, 223, 223);
            Paint paint = new Paint();
            paint.setARGB(255, 210, 210, 210);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(Typeface.create((Typeface)Typeface.DEFAULT, (int)0));
            Rect rect = new Rect();
            canvas.getClipBounds(rect);
            paint.setTextSize((float)rect.height());
            int n = rect.height();
            int n2 = rect.width();
            paint.setTextAlign(Paint.Align.LEFT);
            paint.getTextBounds("?", 0, "?".length(), rect);
            canvas.drawText("?", (float)n2 / 2.0f - (float)rect.width() / 2.0f - (float)rect.left, (float)n / 2.0f + (float)rect.height() / 2.0f - (float)rect.bottom, paint);
        }
    }

    public void setContentId(int n) {
        if (this.mContentId == n) {
            return;
        }
        View view = this.mContent;
        if (view != null) {
            view.setVisibility(0);
            ((ConstraintLayout.LayoutParams)this.mContent.getLayoutParams()).isInPlaceholder = false;
            this.mContent = null;
        }
        this.mContentId = n;
        if (n != -1 && (view = ((View)this.getParent()).findViewById(n)) != null) {
            view.setVisibility(8);
        }
    }

    public void setEmptyVisibility(int n) {
        this.mEmptyVisibility = n;
    }

    public void updatePostMeasure(ConstraintLayout object) {
        if (this.mContent == null) {
            return;
        }
        object = (ConstraintLayout.LayoutParams)this.getLayoutParams();
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)this.mContent.getLayoutParams();
        layoutParams.widget.setVisibility(0);
        if (((ConstraintLayout.LayoutParams)((Object)object)).widget.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.FIXED) {
            ((ConstraintLayout.LayoutParams)((Object)object)).widget.setWidth(layoutParams.widget.getWidth());
        }
        if (((ConstraintLayout.LayoutParams)((Object)object)).widget.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.FIXED) {
            ((ConstraintLayout.LayoutParams)((Object)object)).widget.setHeight(layoutParams.widget.getHeight());
        }
        layoutParams.widget.setVisibility(8);
    }

    public void updatePreLayout(ConstraintLayout constraintLayout) {
        if (this.mContentId == -1 && !this.isInEditMode()) {
            this.setVisibility(this.mEmptyVisibility);
        }
        constraintLayout = constraintLayout.findViewById(this.mContentId);
        this.mContent = constraintLayout;
        if (constraintLayout != null) {
            ((ConstraintLayout.LayoutParams)constraintLayout.getLayoutParams()).isInPlaceholder = true;
            this.mContent.setVisibility(0);
            this.setVisibility(0);
        }
    }
}

