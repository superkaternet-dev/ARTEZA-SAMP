/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.widget.Button
 *  android.widget.LinearLayout
 *  android.widget.TextView
 */
package com.google.android.material.snackbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.snackbar.ContentViewCallback;

public class SnackbarContentLayout
extends LinearLayout
implements ContentViewCallback {
    private Button actionView;
    private int maxInlineActionWidth;
    private int maxWidth;
    private TextView messageView;

    public SnackbarContentLayout(Context context) {
        this(context, null);
    }

    public SnackbarContentLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        context = context.obtainStyledAttributes(attributeSet, R.styleable.SnackbarLayout);
        this.maxWidth = context.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1);
        this.maxInlineActionWidth = context.getDimensionPixelSize(R.styleable.SnackbarLayout_maxActionInlineWidth, -1);
        context.recycle();
    }

    private static void updateTopBottomPadding(View view, int n, int n2) {
        if (ViewCompat.isPaddingRelative(view)) {
            ViewCompat.setPaddingRelative(view, ViewCompat.getPaddingStart(view), n, ViewCompat.getPaddingEnd(view), n2);
        } else {
            view.setPadding(view.getPaddingLeft(), n, view.getPaddingRight(), n2);
        }
    }

    private boolean updateViewsWithinLayout(int n, int n2, int n3) {
        boolean bl = false;
        if (n != this.getOrientation()) {
            this.setOrientation(n);
            bl = true;
        }
        if (this.messageView.getPaddingTop() != n2 || this.messageView.getPaddingBottom() != n3) {
            SnackbarContentLayout.updateTopBottomPadding((View)this.messageView, n2, n3);
            bl = true;
        }
        return bl;
    }

    @Override
    public void animateContentIn(int n, int n2) {
        this.messageView.setAlpha(0.0f);
        this.messageView.animate().alpha(1.0f).setDuration((long)n2).setStartDelay((long)n).start();
        if (this.actionView.getVisibility() == 0) {
            this.actionView.setAlpha(0.0f);
            this.actionView.animate().alpha(1.0f).setDuration((long)n2).setStartDelay((long)n).start();
        }
    }

    @Override
    public void animateContentOut(int n, int n2) {
        this.messageView.setAlpha(1.0f);
        this.messageView.animate().alpha(0.0f).setDuration((long)n2).setStartDelay((long)n).start();
        if (this.actionView.getVisibility() == 0) {
            this.actionView.setAlpha(1.0f);
            this.actionView.animate().alpha(0.0f).setDuration((long)n2).setStartDelay((long)n).start();
        }
    }

    public Button getActionView() {
        return this.actionView;
    }

    public TextView getMessageView() {
        return this.messageView;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.messageView = (TextView)this.findViewById(R.id.snackbar_text);
        this.actionView = (Button)this.findViewById(R.id.snackbar_action);
    }

    protected void onMeasure(int n, int n2) {
        int n3;
        int n4;
        super.onMeasure(n, n2);
        int n5 = n;
        if (this.maxWidth > 0) {
            n4 = this.getMeasuredWidth();
            n3 = this.maxWidth;
            n5 = n;
            if (n4 > n3) {
                n5 = View.MeasureSpec.makeMeasureSpec((int)n3, (int)0x40000000);
                super.onMeasure(n5, n2);
            }
        }
        int n6 = this.getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical_2lines);
        n3 = this.getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical);
        n = this.messageView.getLayout().getLineCount() > 1 ? 1 : 0;
        n4 = 0;
        if (n != 0 && this.maxInlineActionWidth > 0 && this.actionView.getMeasuredWidth() > this.maxInlineActionWidth) {
            n = n4;
            if (this.updateViewsWithinLayout(1, n6, n6 - n3)) {
                n = 1;
            }
        } else {
            if (n != 0) {
                n3 = n6;
            }
            n = n4;
            if (this.updateViewsWithinLayout(0, n3, n3)) {
                n = 1;
            }
        }
        if (n != 0) {
            super.onMeasure(n5, n2);
        }
    }
}

