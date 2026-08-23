/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.DataSetObserver
 *  android.graphics.drawable.Drawable
 *  android.text.TextUtils$TruncateAt
 *  android.text.method.SingleLineTransformationMethod
 *  android.text.method.TransformationMethod
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.widget.TextView
 */
package androidx.viewpager.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.method.SingleLineTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.widget.TextViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Locale;

@ViewPager.DecorView
public class PagerTitleStrip
extends ViewGroup {
    private static final int[] ATTRS = new int[]{16842804, 16842901, 16842904, 16842927};
    private static final float SIDE_ALPHA = 0.6f;
    private static final int[] TEXT_ATTRS = new int[]{16843660};
    private static final int TEXT_SPACING = 16;
    TextView mCurrText;
    private int mGravity;
    private int mLastKnownCurrentPage = -1;
    float mLastKnownPositionOffset = -1.0f;
    TextView mNextText;
    private int mNonPrimaryAlpha;
    private final PageListener mPageListener = new PageListener(this);
    ViewPager mPager;
    TextView mPrevText;
    private int mScaledTextSpacing;
    int mTextColor;
    private boolean mUpdatingPositions;
    private boolean mUpdatingText;
    private WeakReference<PagerAdapter> mWatchingAdapter;

    public PagerTitleStrip(Context context) {
        this(context, null);
    }

    public PagerTitleStrip(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int n;
        TextView textView;
        this.mPrevText = textView = new TextView(context);
        this.addView((View)textView);
        this.mCurrText = textView = new TextView(context);
        this.addView((View)textView);
        this.mNextText = textView = new TextView(context);
        this.addView((View)textView);
        attributeSet = context.obtainStyledAttributes(attributeSet, ATTRS);
        int n2 = attributeSet.getResourceId(0, 0);
        if (n2 != 0) {
            TextViewCompat.setTextAppearance(this.mPrevText, n2);
            TextViewCompat.setTextAppearance(this.mCurrText, n2);
            TextViewCompat.setTextAppearance(this.mNextText, n2);
        }
        if ((n = attributeSet.getDimensionPixelSize(1, 0)) != 0) {
            this.setTextSize(0, n);
        }
        if (attributeSet.hasValue(2)) {
            n = attributeSet.getColor(2, 0);
            this.mPrevText.setTextColor(n);
            this.mCurrText.setTextColor(n);
            this.mNextText.setTextColor(n);
        }
        this.mGravity = attributeSet.getInteger(3, 80);
        attributeSet.recycle();
        this.mTextColor = this.mCurrText.getTextColors().getDefaultColor();
        this.setNonPrimaryAlpha(0.6f);
        this.mPrevText.setEllipsize(TextUtils.TruncateAt.END);
        this.mCurrText.setEllipsize(TextUtils.TruncateAt.END);
        this.mNextText.setEllipsize(TextUtils.TruncateAt.END);
        boolean bl = false;
        if (n2 != 0) {
            attributeSet = context.obtainStyledAttributes(n2, TEXT_ATTRS);
            bl = attributeSet.getBoolean(0, false);
            attributeSet.recycle();
        }
        if (bl) {
            PagerTitleStrip.setSingleLineAllCaps(this.mPrevText);
            PagerTitleStrip.setSingleLineAllCaps(this.mCurrText);
            PagerTitleStrip.setSingleLineAllCaps(this.mNextText);
        } else {
            this.mPrevText.setSingleLine();
            this.mCurrText.setSingleLine();
            this.mNextText.setSingleLine();
        }
        this.mScaledTextSpacing = (int)(16.0f * context.getResources().getDisplayMetrics().density);
    }

    private static void setSingleLineAllCaps(TextView textView) {
        textView.setTransformationMethod((TransformationMethod)new SingleLineAllCapsTransform(textView.getContext()));
    }

    int getMinHeight() {
        int n = 0;
        Drawable drawable2 = this.getBackground();
        if (drawable2 != null) {
            n = drawable2.getIntrinsicHeight();
        }
        return n;
    }

    public int getTextSpacing() {
        return this.mScaledTextSpacing;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Object object = this.getParent();
        if (object instanceof ViewPager) {
            object = (ViewPager)((Object)object);
            PagerAdapter pagerAdapter = ((ViewPager)((Object)object)).getAdapter();
            ((ViewPager)((Object)object)).setInternalPageChangeListener(this.mPageListener);
            ((ViewPager)((Object)object)).addOnAdapterChangeListener(this.mPageListener);
            this.mPager = object;
            object = this.mWatchingAdapter;
            object = object != null ? (PagerAdapter)((Reference)object).get() : null;
            this.updateAdapter((PagerAdapter)object, pagerAdapter);
            return;
        }
        throw new IllegalStateException("PagerTitleStrip must be a direct child of a ViewPager.");
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ViewPager viewPager = this.mPager;
        if (viewPager != null) {
            this.updateAdapter(viewPager.getAdapter(), null);
            this.mPager.setInternalPageChangeListener(null);
            this.mPager.removeOnAdapterChangeListener(this.mPageListener);
            this.mPager = null;
        }
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        if (this.mPager != null) {
            float f = this.mLastKnownPositionOffset;
            if (!(f >= 0.0f)) {
                f = 0.0f;
            }
            this.updateTextPositions(this.mLastKnownCurrentPage, f, true);
        }
    }

    protected void onMeasure(int n, int n2) {
        if (View.MeasureSpec.getMode((int)n) == 0x40000000) {
            int n3 = this.getPaddingTop() + this.getPaddingBottom();
            int n4 = PagerTitleStrip.getChildMeasureSpec((int)n2, (int)n3, (int)-2);
            int n5 = View.MeasureSpec.getSize((int)n);
            n = PagerTitleStrip.getChildMeasureSpec((int)n, (int)((int)((float)n5 * 0.2f)), (int)-2);
            this.mPrevText.measure(n, n4);
            this.mCurrText.measure(n, n4);
            this.mNextText.measure(n, n4);
            if (View.MeasureSpec.getMode((int)n2) == 0x40000000) {
                n = View.MeasureSpec.getSize((int)n2);
            } else {
                n = this.mCurrText.getMeasuredHeight();
                n = Math.max(this.getMinHeight(), n + n3);
            }
            this.setMeasuredDimension(n5, View.resolveSizeAndState((int)n, (int)n2, (int)(this.mCurrText.getMeasuredState() << 16)));
            return;
        }
        throw new IllegalStateException("Must measure with an exact width");
    }

    public void requestLayout() {
        if (!this.mUpdatingText) {
            super.requestLayout();
        }
    }

    public void setGravity(int n) {
        this.mGravity = n;
        this.requestLayout();
    }

    public void setNonPrimaryAlpha(float f) {
        int n;
        this.mNonPrimaryAlpha = n = (int)(255.0f * f) & 0xFF;
        n = n << 24 | this.mTextColor & 0xFFFFFF;
        this.mPrevText.setTextColor(n);
        this.mNextText.setTextColor(n);
    }

    public void setTextColor(int n) {
        this.mTextColor = n;
        this.mCurrText.setTextColor(n);
        n = this.mNonPrimaryAlpha << 24 | this.mTextColor & 0xFFFFFF;
        this.mPrevText.setTextColor(n);
        this.mNextText.setTextColor(n);
    }

    public void setTextSize(int n, float f) {
        this.mPrevText.setTextSize(n, f);
        this.mCurrText.setTextSize(n, f);
        this.mNextText.setTextSize(n, f);
    }

    public void setTextSpacing(int n) {
        this.mScaledTextSpacing = n;
        this.requestLayout();
    }

    void updateAdapter(PagerAdapter object, PagerAdapter pagerAdapter) {
        if (object != null) {
            ((PagerAdapter)object).unregisterDataSetObserver(this.mPageListener);
            this.mWatchingAdapter = null;
        }
        if (pagerAdapter != null) {
            pagerAdapter.registerDataSetObserver(this.mPageListener);
            this.mWatchingAdapter = new WeakReference<PagerAdapter>(pagerAdapter);
        }
        if ((object = this.mPager) != null) {
            this.mLastKnownCurrentPage = -1;
            this.mLastKnownPositionOffset = -1.0f;
            this.updateText(((ViewPager)((Object)object)).getCurrentItem(), pagerAdapter);
            this.requestLayout();
        }
    }

    void updateText(int n, PagerAdapter pagerAdapter) {
        TextView textView;
        int n2 = pagerAdapter != null ? pagerAdapter.getCount() : 0;
        this.mUpdatingText = true;
        Object object = textView = null;
        if (n >= 1) {
            object = textView;
            if (pagerAdapter != null) {
                object = pagerAdapter.getPageTitle(n - 1);
            }
        }
        this.mPrevText.setText(object);
        textView = this.mCurrText;
        object = pagerAdapter != null && n < n2 ? pagerAdapter.getPageTitle(n) : null;
        textView.setText((CharSequence)object);
        object = textView = null;
        if (n + 1 < n2) {
            object = textView;
            if (pagerAdapter != null) {
                object = pagerAdapter.getPageTitle(n + 1);
            }
        }
        this.mNextText.setText((CharSequence)object);
        int n3 = View.MeasureSpec.makeMeasureSpec((int)Math.max(0, (int)((float)(this.getWidth() - this.getPaddingLeft() - this.getPaddingRight()) * 0.8f)), (int)Integer.MIN_VALUE);
        n2 = View.MeasureSpec.makeMeasureSpec((int)Math.max(0, this.getHeight() - this.getPaddingTop() - this.getPaddingBottom()), (int)Integer.MIN_VALUE);
        this.mPrevText.measure(n3, n2);
        this.mCurrText.measure(n3, n2);
        this.mNextText.measure(n3, n2);
        this.mLastKnownCurrentPage = n;
        if (!this.mUpdatingPositions) {
            this.updateTextPositions(n, this.mLastKnownPositionOffset, false);
        }
        this.mUpdatingText = false;
    }

    void updateTextPositions(int n, float f, boolean bl) {
        float f2;
        if (n != this.mLastKnownCurrentPage) {
            this.updateText(n, this.mPager.getAdapter());
        } else if (!bl && f == this.mLastKnownPositionOffset) {
            return;
        }
        this.mUpdatingPositions = true;
        int n2 = this.mPrevText.getMeasuredWidth();
        int n3 = this.mCurrText.getMeasuredWidth();
        int n4 = this.mNextText.getMeasuredWidth();
        int n5 = n3 / 2;
        int n6 = this.getWidth();
        int n7 = this.getHeight();
        int n8 = this.getPaddingLeft();
        int n9 = this.getPaddingRight();
        int n10 = this.getPaddingTop();
        n = this.getPaddingBottom();
        int n11 = n9 + n5;
        float f3 = f2 = 0.5f + f;
        if (f2 > 1.0f) {
            f3 = f2 - 1.0f;
        }
        n5 = n6 - n11 - (int)((float)(n6 - (n8 + n5) - n11) * f3) - n5;
        n11 = n3 + n5;
        int n12 = this.mPrevText.getBaseline();
        n3 = this.mCurrText.getBaseline();
        int n13 = this.mNextText.getBaseline();
        int n14 = Math.max(Math.max(n12, n3), n13);
        n12 = n14 - n12;
        n3 = n14 - n3;
        n13 = n14 - n13;
        int n15 = this.mPrevText.getMeasuredHeight();
        n14 = this.mCurrText.getMeasuredHeight();
        int n16 = this.mNextText.getMeasuredHeight();
        n14 = Math.max(Math.max(n15 + n12, n14 + n3), n16 + n13);
        switch (this.mGravity & 0x70) {
            default: {
                n = n12 + n10;
                n3 += n10;
                n10 += n13;
                break;
            }
            case 80: {
                n10 = n7 - n - n14;
                n = n12 + n10;
                n3 += n10;
                n10 += n13;
                break;
            }
            case 16: {
                n10 = (n7 - n10 - n - n14) / 2;
                n = n12 + n10;
                n3 += n10;
                n10 += n13;
            }
        }
        TextView textView = this.mCurrText;
        textView.layout(n5, n3, n11, textView.getMeasuredHeight() + n3);
        n3 = Math.min(n8, n5 - this.mScaledTextSpacing - n2);
        textView = this.mPrevText;
        textView.layout(n3, n, n2 + n3, textView.getMeasuredHeight() + n);
        n = Math.max(n6 - n9 - n4, n11 + this.mScaledTextSpacing);
        textView = this.mNextText;
        textView.layout(n, n10, n + n4, textView.getMeasuredHeight() + n10);
        this.mLastKnownPositionOffset = f;
        this.mUpdatingPositions = false;
    }

    private class PageListener
    extends DataSetObserver
    implements ViewPager.OnPageChangeListener,
    ViewPager.OnAdapterChangeListener {
        private int mScrollState;
        final PagerTitleStrip this$0;

        PageListener(PagerTitleStrip pagerTitleStrip) {
            this.this$0 = pagerTitleStrip;
        }

        @Override
        public void onAdapterChanged(ViewPager viewPager, PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2) {
            this.this$0.updateAdapter(pagerAdapter, pagerAdapter2);
        }

        public void onChanged() {
            PagerTitleStrip pagerTitleStrip = this.this$0;
            pagerTitleStrip.updateText(pagerTitleStrip.mPager.getCurrentItem(), this.this$0.mPager.getAdapter());
            float f = this.this$0.mLastKnownPositionOffset;
            float f2 = 0.0f;
            if (f >= 0.0f) {
                f2 = this.this$0.mLastKnownPositionOffset;
            }
            pagerTitleStrip = this.this$0;
            pagerTitleStrip.updateTextPositions(pagerTitleStrip.mPager.getCurrentItem(), f2, true);
        }

        @Override
        public void onPageScrollStateChanged(int n) {
            this.mScrollState = n;
        }

        @Override
        public void onPageScrolled(int n, float f, int n2) {
            n2 = n;
            if (f > 0.5f) {
                n2 = n + 1;
            }
            this.this$0.updateTextPositions(n2, f, false);
        }

        @Override
        public void onPageSelected(int n) {
            if (this.mScrollState == 0) {
                PagerTitleStrip pagerTitleStrip = this.this$0;
                pagerTitleStrip.updateText(pagerTitleStrip.mPager.getCurrentItem(), this.this$0.mPager.getAdapter());
                float f = this.this$0.mLastKnownPositionOffset;
                float f2 = 0.0f;
                if (f >= 0.0f) {
                    f2 = this.this$0.mLastKnownPositionOffset;
                }
                pagerTitleStrip = this.this$0;
                pagerTitleStrip.updateTextPositions(pagerTitleStrip.mPager.getCurrentItem(), f2, true);
            }
        }
    }

    private static class SingleLineAllCapsTransform
    extends SingleLineTransformationMethod {
        private Locale mLocale;

        SingleLineAllCapsTransform(Context context) {
            this.mLocale = context.getResources().getConfiguration().locale;
        }

        public CharSequence getTransformation(CharSequence charSequence, View view) {
            charSequence = (charSequence = super.getTransformation(charSequence, view)) != null ? charSequence.toString().toUpperCase(this.mLocale) : null;
            return charSequence;
        }
    }
}

