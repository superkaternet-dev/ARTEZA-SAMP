/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.text.Layout$Alignment
 *  android.text.PrecomputedText
 *  android.text.PrecomputedText$Params
 *  android.text.PrecomputedText$Params$Builder
 *  android.text.Spannable
 *  android.text.SpannableString
 *  android.text.StaticLayout
 *  android.text.StaticLayout$Builder
 *  android.text.TextDirectionHeuristic
 *  android.text.TextDirectionHeuristics
 *  android.text.TextPaint
 *  android.text.TextUtils
 *  android.text.style.MetricAffectingSpan
 */
package androidx.core.text;

import android.os.Build;
import android.text.Layout;
import android.text.PrecomputedText;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
import androidx.core.os.TraceCompat;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class PrecomputedTextCompat
implements Spannable {
    private static final char LINE_FEED = '\n';
    private static Executor sExecutor;
    private static final Object sLock;
    private final int[] mParagraphEnds;
    private final Params mParams;
    private final Spannable mText;
    private final PrecomputedText mWrapped;

    static {
        sLock = new Object();
        sExecutor = null;
    }

    private PrecomputedTextCompat(PrecomputedText precomputedText, Params params) {
        this.mText = precomputedText;
        this.mParams = params;
        params = null;
        this.mParagraphEnds = null;
        if (Build.VERSION.SDK_INT >= 29) {
            params = precomputedText;
        }
        this.mWrapped = params;
    }

    private PrecomputedTextCompat(CharSequence charSequence, Params params, int[] nArray) {
        this.mText = new SpannableString(charSequence);
        this.mParams = params;
        this.mParagraphEnds = nArray;
        this.mWrapped = null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static PrecomputedTextCompat create(CharSequence object, Params params) {
        Preconditions.checkNotNull(object);
        Preconditions.checkNotNull(params);
        try {
            TraceCompat.beginSection("PrecomputedText");
            if (Build.VERSION.SDK_INT >= 29 && params.mWrapped != null) {
                object = new PrecomputedTextCompat(PrecomputedText.create((CharSequence)object, (PrecomputedText.Params)params.mWrapped), params);
                return object;
            }
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            int n = object.length();
            int n2 = 0;
            while (n2 < n) {
                n2 = (n2 = TextUtils.indexOf((CharSequence)object, (char)'\n', (int)n2, (int)n)) < 0 ? n : ++n2;
                arrayList.add(n2);
            }
            int[] nArray = new int[arrayList.size()];
            for (n2 = 0; n2 < arrayList.size(); ++n2) {
                nArray[n2] = (Integer)arrayList.get(n2);
            }
            if (Build.VERSION.SDK_INT >= 23) {
                StaticLayout.Builder.obtain((CharSequence)object, (int)0, (int)object.length(), (TextPaint)params.getTextPaint(), (int)Integer.MAX_VALUE).setBreakStrategy(params.getBreakStrategy()).setHyphenationFrequency(params.getHyphenationFrequency()).setTextDirection(params.getTextDirection()).build();
            } else if (Build.VERSION.SDK_INT >= 21) {
                new StaticLayout((CharSequence)object, params.getTextPaint(), Integer.MAX_VALUE, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
            object = new PrecomputedTextCompat((CharSequence)object, params, nArray);
            return object;
        }
        finally {
            TraceCompat.endSection();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Future<PrecomputedTextCompat> getTextFuture(CharSequence object, Params object2, Executor object3) {
        object2 = new PrecomputedTextFutureTask((Params)object2, (CharSequence)object);
        object = object3;
        if (object3 == null) {
            object3 = sLock;
            synchronized (object3) {
                if (sExecutor == null) {
                    sExecutor = Executors.newFixedThreadPool(1);
                }
                object = sExecutor;
            }
        }
        object.execute((Runnable)object2);
        return object2;
    }

    public char charAt(int n) {
        return this.mText.charAt(n);
    }

    public int getParagraphCount() {
        if (Build.VERSION.SDK_INT >= 29) {
            return this.mWrapped.getParagraphCount();
        }
        return this.mParagraphEnds.length;
    }

    public int getParagraphEnd(int n) {
        Preconditions.checkArgumentInRange(n, 0, this.getParagraphCount(), "paraIndex");
        if (Build.VERSION.SDK_INT >= 29) {
            return this.mWrapped.getParagraphEnd(n);
        }
        return this.mParagraphEnds[n];
    }

    public int getParagraphStart(int n) {
        int n2 = this.getParagraphCount();
        int n3 = 0;
        Preconditions.checkArgumentInRange(n, 0, n2, "paraIndex");
        if (Build.VERSION.SDK_INT >= 29) {
            return this.mWrapped.getParagraphStart(n);
        }
        n = n == 0 ? n3 : this.mParagraphEnds[n - 1];
        return n;
    }

    public Params getParams() {
        return this.mParams;
    }

    public PrecomputedText getPrecomputedText() {
        Spannable spannable = this.mText;
        if (spannable instanceof PrecomputedText) {
            return (PrecomputedText)spannable;
        }
        return null;
    }

    public int getSpanEnd(Object object) {
        return this.mText.getSpanEnd(object);
    }

    public int getSpanFlags(Object object) {
        return this.mText.getSpanFlags(object);
    }

    public int getSpanStart(Object object) {
        return this.mText.getSpanStart(object);
    }

    public <T> T[] getSpans(int n, int n2, Class<T> clazz) {
        if (Build.VERSION.SDK_INT >= 29) {
            return this.mWrapped.getSpans(n, n2, clazz);
        }
        return this.mText.getSpans(n, n2, clazz);
    }

    public int length() {
        return this.mText.length();
    }

    public int nextSpanTransition(int n, int n2, Class clazz) {
        return this.mText.nextSpanTransition(n, n2, clazz);
    }

    public void removeSpan(Object object) {
        if (!(object instanceof MetricAffectingSpan)) {
            if (Build.VERSION.SDK_INT >= 29) {
                this.mWrapped.removeSpan(object);
            } else {
                this.mText.removeSpan(object);
            }
            return;
        }
        throw new IllegalArgumentException("MetricAffectingSpan can not be removed from PrecomputedText.");
    }

    public void setSpan(Object object, int n, int n2, int n3) {
        if (!(object instanceof MetricAffectingSpan)) {
            if (Build.VERSION.SDK_INT >= 29) {
                this.mWrapped.setSpan(object, n, n2, n3);
            } else {
                this.mText.setSpan(object, n, n2, n3);
            }
            return;
        }
        throw new IllegalArgumentException("MetricAffectingSpan can not be set to PrecomputedText.");
    }

    public CharSequence subSequence(int n, int n2) {
        return this.mText.subSequence(n, n2);
    }

    public String toString() {
        return this.mText.toString();
    }

    public static final class Params {
        private final int mBreakStrategy;
        private final int mHyphenationFrequency;
        private final TextPaint mPaint;
        private final TextDirectionHeuristic mTextDir;
        final PrecomputedText.Params mWrapped;

        public Params(PrecomputedText.Params params) {
            this.mPaint = params.getTextPaint();
            this.mTextDir = params.getTextDirection();
            this.mBreakStrategy = params.getBreakStrategy();
            this.mHyphenationFrequency = params.getHyphenationFrequency();
            if (Build.VERSION.SDK_INT < 29) {
                params = null;
            }
            this.mWrapped = params;
        }

        Params(TextPaint textPaint, TextDirectionHeuristic textDirectionHeuristic, int n, int n2) {
            this.mWrapped = Build.VERSION.SDK_INT >= 29 ? new PrecomputedText.Params.Builder(textPaint).setBreakStrategy(n).setHyphenationFrequency(n2).setTextDirection(textDirectionHeuristic).build() : null;
            this.mPaint = textPaint;
            this.mTextDir = textDirectionHeuristic;
            this.mBreakStrategy = n;
            this.mHyphenationFrequency = n2;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof Params)) {
                return false;
            }
            if (!this.equalsWithoutTextDirection((Params)(object = (Params)object))) {
                return false;
            }
            return Build.VERSION.SDK_INT < 18 || this.mTextDir == ((Params)object).getTextDirection();
        }

        public boolean equalsWithoutTextDirection(Params params) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (this.mBreakStrategy != params.getBreakStrategy()) {
                    return false;
                }
                if (this.mHyphenationFrequency != params.getHyphenationFrequency()) {
                    return false;
                }
            }
            if (this.mPaint.getTextSize() != params.getTextPaint().getTextSize()) {
                return false;
            }
            if (this.mPaint.getTextScaleX() != params.getTextPaint().getTextScaleX()) {
                return false;
            }
            if (this.mPaint.getTextSkewX() != params.getTextPaint().getTextSkewX()) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= 21) {
                if (this.mPaint.getLetterSpacing() != params.getTextPaint().getLetterSpacing()) {
                    return false;
                }
                if (!TextUtils.equals((CharSequence)this.mPaint.getFontFeatureSettings(), (CharSequence)params.getTextPaint().getFontFeatureSettings())) {
                    return false;
                }
            }
            if (this.mPaint.getFlags() != params.getTextPaint().getFlags()) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= 24 ? !this.mPaint.getTextLocales().equals((Object)params.getTextPaint().getTextLocales()) : Build.VERSION.SDK_INT >= 17 && !this.mPaint.getTextLocale().equals(params.getTextPaint().getTextLocale())) {
                return false;
            }
            return !(this.mPaint.getTypeface() == null ? params.getTextPaint().getTypeface() != null : !this.mPaint.getTypeface().equals((Object)params.getTextPaint().getTypeface()));
        }

        public int getBreakStrategy() {
            return this.mBreakStrategy;
        }

        public int getHyphenationFrequency() {
            return this.mHyphenationFrequency;
        }

        public TextDirectionHeuristic getTextDirection() {
            return this.mTextDir;
        }

        public TextPaint getTextPaint() {
            return this.mPaint;
        }

        public int hashCode() {
            if (Build.VERSION.SDK_INT >= 24) {
                return ObjectsCompat.hash(Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Float.valueOf(this.mPaint.getLetterSpacing()), this.mPaint.getFlags(), this.mPaint.getTextLocales(), this.mPaint.getTypeface(), this.mPaint.isElegantTextHeight(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                return ObjectsCompat.hash(Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Float.valueOf(this.mPaint.getLetterSpacing()), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mPaint.isElegantTextHeight(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }
            if (Build.VERSION.SDK_INT >= 18) {
                return ObjectsCompat.hash(Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }
            if (Build.VERSION.SDK_INT >= 17) {
                return ObjectsCompat.hash(Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }
            return ObjectsCompat.hash(Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), this.mPaint.getFlags(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("{");
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("textSize=");
            stringBuilder2.append(this.mPaint.getTextSize());
            stringBuilder.append(stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", textScaleX=");
            stringBuilder2.append(this.mPaint.getTextScaleX());
            stringBuilder.append(stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", textSkewX=");
            stringBuilder2.append(this.mPaint.getTextSkewX());
            stringBuilder.append(stringBuilder2.toString());
            if (Build.VERSION.SDK_INT >= 21) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(", letterSpacing=");
                stringBuilder2.append(this.mPaint.getLetterSpacing());
                stringBuilder.append(stringBuilder2.toString());
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(", elegantTextHeight=");
                stringBuilder2.append(this.mPaint.isElegantTextHeight());
                stringBuilder.append(stringBuilder2.toString());
            }
            if (Build.VERSION.SDK_INT >= 24) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(", textLocale=");
                stringBuilder2.append(this.mPaint.getTextLocales());
                stringBuilder.append(stringBuilder2.toString());
            } else if (Build.VERSION.SDK_INT >= 17) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(", textLocale=");
                stringBuilder2.append(this.mPaint.getTextLocale());
                stringBuilder.append(stringBuilder2.toString());
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", typeface=");
            stringBuilder2.append(this.mPaint.getTypeface());
            stringBuilder.append(stringBuilder2.toString());
            if (Build.VERSION.SDK_INT >= 26) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(", variationSettings=");
                stringBuilder2.append(this.mPaint.getFontVariationSettings());
                stringBuilder.append(stringBuilder2.toString());
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", textDir=");
            stringBuilder2.append(this.mTextDir);
            stringBuilder.append(stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", breakStrategy=");
            stringBuilder2.append(this.mBreakStrategy);
            stringBuilder.append(stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", hyphenationFrequency=");
            stringBuilder2.append(this.mHyphenationFrequency);
            stringBuilder.append(stringBuilder2.toString());
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        public static class Builder {
            private int mBreakStrategy;
            private int mHyphenationFrequency;
            private final TextPaint mPaint;
            private TextDirectionHeuristic mTextDir;

            public Builder(TextPaint textPaint) {
                this.mPaint = textPaint;
                if (Build.VERSION.SDK_INT >= 23) {
                    this.mBreakStrategy = 1;
                    this.mHyphenationFrequency = 1;
                } else {
                    this.mHyphenationFrequency = 0;
                    this.mBreakStrategy = 0;
                }
                this.mTextDir = Build.VERSION.SDK_INT >= 18 ? TextDirectionHeuristics.FIRSTSTRONG_LTR : null;
            }

            public Params build() {
                return new Params(this.mPaint, this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }

            public Builder setBreakStrategy(int n) {
                this.mBreakStrategy = n;
                return this;
            }

            public Builder setHyphenationFrequency(int n) {
                this.mHyphenationFrequency = n;
                return this;
            }

            public Builder setTextDirection(TextDirectionHeuristic textDirectionHeuristic) {
                this.mTextDir = textDirectionHeuristic;
                return this;
            }
        }
    }

    private static class PrecomputedTextFutureTask
    extends FutureTask<PrecomputedTextCompat> {
        PrecomputedTextFutureTask(Params params, CharSequence charSequence) {
            super(new PrecomputedTextCallback(params, charSequence));
        }

        private static class PrecomputedTextCallback
        implements Callable<PrecomputedTextCompat> {
            private Params mParams;
            private CharSequence mText;

            PrecomputedTextCallback(Params params, CharSequence charSequence) {
                this.mParams = params;
                this.mText = charSequence;
            }

            @Override
            public PrecomputedTextCompat call() throws Exception {
                return PrecomputedTextCompat.create(this.mText, this.mParams);
            }
        }
    }
}

