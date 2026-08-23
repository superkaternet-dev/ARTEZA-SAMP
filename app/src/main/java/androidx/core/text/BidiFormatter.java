/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.SpannableStringBuilder
 */
package androidx.core.text;

import android.text.SpannableStringBuilder;
import androidx.core.text.TextDirectionHeuristicCompat;
import androidx.core.text.TextDirectionHeuristicsCompat;
import androidx.core.text.TextUtilsCompat;
import java.util.Locale;

public final class BidiFormatter {
    private static final int DEFAULT_FLAGS = 2;
    static final BidiFormatter DEFAULT_LTR_INSTANCE;
    static final BidiFormatter DEFAULT_RTL_INSTANCE;
    static final TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC;
    private static final int DIR_LTR = -1;
    private static final int DIR_RTL = 1;
    private static final int DIR_UNKNOWN = 0;
    private static final String EMPTY_STRING = "";
    private static final int FLAG_STEREO_RESET = 2;
    private static final char LRE = '\u202a';
    private static final char LRM = '\u200e';
    private static final String LRM_STRING;
    private static final char PDF = '\u202c';
    private static final char RLE = '\u202b';
    private static final char RLM = '\u200f';
    private static final String RLM_STRING;
    private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
    private final int mFlags;
    private final boolean mIsRtlContext;

    static {
        TextDirectionHeuristicCompat textDirectionHeuristicCompat;
        DEFAULT_TEXT_DIRECTION_HEURISTIC = textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
        LRM_STRING = Character.toString('\u200e');
        RLM_STRING = Character.toString('\u200f');
        DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, textDirectionHeuristicCompat);
        DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, textDirectionHeuristicCompat);
    }

    BidiFormatter(boolean bl, int n, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        this.mIsRtlContext = bl;
        this.mFlags = n;
        this.mDefaultTextDirectionHeuristicCompat = textDirectionHeuristicCompat;
    }

    private static int getEntryDir(CharSequence charSequence) {
        return new DirectionalityEstimator(charSequence, false).getEntryDir();
    }

    private static int getExitDir(CharSequence charSequence) {
        return new DirectionalityEstimator(charSequence, false).getExitDir();
    }

    public static BidiFormatter getInstance() {
        return new Builder().build();
    }

    public static BidiFormatter getInstance(Locale locale) {
        return new Builder(locale).build();
    }

    public static BidiFormatter getInstance(boolean bl) {
        return new Builder(bl).build();
    }

    static boolean isRtlLocale(Locale locale) {
        int n = TextUtilsCompat.getLayoutDirectionFromLocale(locale);
        boolean bl = true;
        if (n != 1) {
            bl = false;
        }
        return bl;
    }

    private String markAfter(CharSequence charSequence, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        boolean bl = textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
        if (!this.mIsRtlContext && (bl || BidiFormatter.getExitDir(charSequence) == 1)) {
            return LRM_STRING;
        }
        if (this.mIsRtlContext && (!bl || BidiFormatter.getExitDir(charSequence) == -1)) {
            return RLM_STRING;
        }
        return EMPTY_STRING;
    }

    private String markBefore(CharSequence charSequence, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        boolean bl = textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
        if (!this.mIsRtlContext && (bl || BidiFormatter.getEntryDir(charSequence) == 1)) {
            return LRM_STRING;
        }
        if (this.mIsRtlContext && (!bl || BidiFormatter.getEntryDir(charSequence) == -1)) {
            return RLM_STRING;
        }
        return EMPTY_STRING;
    }

    public boolean getStereoReset() {
        boolean bl = (this.mFlags & 2) != 0;
        return bl;
    }

    public boolean isRtl(CharSequence charSequence) {
        return this.mDefaultTextDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
    }

    public boolean isRtl(String string2) {
        return this.isRtl((CharSequence)string2);
    }

    public boolean isRtlContext() {
        return this.mIsRtlContext;
    }

    public CharSequence unicodeWrap(CharSequence charSequence) {
        return this.unicodeWrap(charSequence, this.mDefaultTextDirectionHeuristicCompat, true);
    }

    public CharSequence unicodeWrap(CharSequence charSequence, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        return this.unicodeWrap(charSequence, textDirectionHeuristicCompat, true);
    }

    public CharSequence unicodeWrap(CharSequence charSequence, TextDirectionHeuristicCompat textDirectionHeuristicCompat, boolean bl) {
        if (charSequence == null) {
            return null;
        }
        boolean bl2 = textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (this.getStereoReset() && bl) {
            textDirectionHeuristicCompat = bl2 ? TextDirectionHeuristicsCompat.RTL : TextDirectionHeuristicsCompat.LTR;
            spannableStringBuilder.append((CharSequence)this.markBefore(charSequence, textDirectionHeuristicCompat));
        }
        if (bl2 != this.mIsRtlContext) {
            char c = bl2 ? (char)'\u202b' : '\u202a';
            spannableStringBuilder.append(c);
            spannableStringBuilder.append(charSequence);
            spannableStringBuilder.append('\u202c');
        } else {
            spannableStringBuilder.append(charSequence);
        }
        if (bl) {
            textDirectionHeuristicCompat = bl2 ? TextDirectionHeuristicsCompat.RTL : TextDirectionHeuristicsCompat.LTR;
            spannableStringBuilder.append((CharSequence)this.markAfter(charSequence, textDirectionHeuristicCompat));
        }
        return spannableStringBuilder;
    }

    public CharSequence unicodeWrap(CharSequence charSequence, boolean bl) {
        return this.unicodeWrap(charSequence, this.mDefaultTextDirectionHeuristicCompat, bl);
    }

    public String unicodeWrap(String string2) {
        return this.unicodeWrap(string2, this.mDefaultTextDirectionHeuristicCompat, true);
    }

    public String unicodeWrap(String string2, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        return this.unicodeWrap(string2, textDirectionHeuristicCompat, true);
    }

    public String unicodeWrap(String string2, TextDirectionHeuristicCompat textDirectionHeuristicCompat, boolean bl) {
        if (string2 == null) {
            return null;
        }
        return this.unicodeWrap((CharSequence)string2, textDirectionHeuristicCompat, bl).toString();
    }

    public String unicodeWrap(String string2, boolean bl) {
        return this.unicodeWrap(string2, this.mDefaultTextDirectionHeuristicCompat, bl);
    }

    public static final class Builder {
        private int mFlags;
        private boolean mIsRtlContext;
        private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;

        public Builder() {
            this.initialize(BidiFormatter.isRtlLocale(Locale.getDefault()));
        }

        public Builder(Locale locale) {
            this.initialize(BidiFormatter.isRtlLocale(locale));
        }

        public Builder(boolean bl) {
            this.initialize(bl);
        }

        private static BidiFormatter getDefaultInstanceFromContext(boolean bl) {
            BidiFormatter bidiFormatter = bl ? DEFAULT_RTL_INSTANCE : DEFAULT_LTR_INSTANCE;
            return bidiFormatter;
        }

        private void initialize(boolean bl) {
            this.mIsRtlContext = bl;
            this.mTextDirectionHeuristicCompat = DEFAULT_TEXT_DIRECTION_HEURISTIC;
            this.mFlags = 2;
        }

        public BidiFormatter build() {
            if (this.mFlags == 2 && this.mTextDirectionHeuristicCompat == DEFAULT_TEXT_DIRECTION_HEURISTIC) {
                return Builder.getDefaultInstanceFromContext(this.mIsRtlContext);
            }
            return new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristicCompat);
        }

        public Builder setTextDirectionHeuristic(TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
            this.mTextDirectionHeuristicCompat = textDirectionHeuristicCompat;
            return this;
        }

        public Builder stereoReset(boolean bl) {
            this.mFlags = bl ? (this.mFlags |= 2) : (this.mFlags &= 0xFFFFFFFD);
            return this;
        }
    }

    private static class DirectionalityEstimator {
        private static final byte[] DIR_TYPE_CACHE = new byte[1792];
        private static final int DIR_TYPE_CACHE_SIZE = 1792;
        private int charIndex;
        private final boolean isHtml;
        private char lastChar;
        private final int length;
        private final CharSequence text;

        static {
            for (int i = 0; i < 1792; ++i) {
                DirectionalityEstimator.DIR_TYPE_CACHE[i] = Character.getDirectionality(i);
            }
        }

        DirectionalityEstimator(CharSequence charSequence, boolean bl) {
            this.text = charSequence;
            this.isHtml = bl;
            this.length = charSequence.length();
        }

        private static byte getCachedDirectionality(char c) {
            byte by = c < '\u0700' ? DIR_TYPE_CACHE[c] : Character.getDirectionality(c);
            return by;
        }

        private byte skipEntityBackward() {
            int n;
            int n2 = this.charIndex;
            while ((n = this.charIndex) > 0) {
                char c;
                CharSequence charSequence = this.text;
                this.charIndex = --n;
                this.lastChar = c = charSequence.charAt(n);
                if (c == '&') {
                    return 12;
                }
                if (c != ';') continue;
            }
            this.charIndex = n2;
            this.lastChar = (char)59;
            return 13;
        }

        private byte skipEntityForward() {
            int n;
            while ((n = this.charIndex) < this.length) {
                char c;
                CharSequence charSequence = this.text;
                this.charIndex = n + 1;
                this.lastChar = c = charSequence.charAt(n);
                if (c != ';') continue;
            }
            return 12;
        }

        private byte skipTagBackward() {
            int n;
            int n2 = this.charIndex;
            block0: while ((n = this.charIndex) > 0) {
                int n3;
                char c;
                CharSequence charSequence = this.text;
                this.charIndex = --n;
                this.lastChar = c = charSequence.charAt(n);
                if (c == '<') {
                    return 12;
                }
                if (c == '>') break;
                if (c != '\"' && c != '\'') continue;
                n = this.lastChar;
                while ((n3 = this.charIndex) > 0) {
                    charSequence = this.text;
                    this.charIndex = --n3;
                    this.lastChar = c = charSequence.charAt(n3);
                    if (c == n) continue block0;
                }
            }
            this.charIndex = n2;
            this.lastChar = (char)62;
            return 13;
        }

        private byte skipTagForward() {
            int n;
            int n2 = this.charIndex;
            block0: while ((n = this.charIndex) < this.length) {
                char c;
                CharSequence charSequence = this.text;
                this.charIndex = n + 1;
                this.lastChar = c = charSequence.charAt(n);
                if (c == '>') {
                    return 12;
                }
                if (c != '\"' && c != '\'') continue;
                char c2 = this.lastChar;
                while ((n = this.charIndex) < this.length) {
                    charSequence = this.text;
                    this.charIndex = n + 1;
                    this.lastChar = c = charSequence.charAt(n);
                    if (c == c2) continue block0;
                }
            }
            this.charIndex = n2;
            this.lastChar = (char)60;
            return 13;
        }

        byte dirTypeBackward() {
            byte by;
            char c;
            this.lastChar = c = this.text.charAt(this.charIndex - 1);
            if (Character.isLowSurrogate(c)) {
                int n = Character.codePointBefore(this.text, this.charIndex);
                this.charIndex -= Character.charCount(n);
                return Character.getDirectionality(n);
            }
            --this.charIndex;
            byte by2 = by = DirectionalityEstimator.getCachedDirectionality(this.lastChar);
            if (this.isHtml) {
                char c2 = this.lastChar;
                if (c2 == '>') {
                    by2 = this.skipTagBackward();
                } else {
                    by2 = by;
                    if (c2 == ';') {
                        by2 = this.skipEntityBackward();
                    }
                }
            }
            return by2;
        }

        byte dirTypeForward() {
            byte by;
            char c;
            this.lastChar = c = this.text.charAt(this.charIndex);
            if (Character.isHighSurrogate(c)) {
                int n = Character.codePointAt(this.text, this.charIndex);
                this.charIndex += Character.charCount(n);
                return Character.getDirectionality(n);
            }
            ++this.charIndex;
            byte by2 = by = DirectionalityEstimator.getCachedDirectionality(this.lastChar);
            if (this.isHtml) {
                char c2 = this.lastChar;
                if (c2 == '<') {
                    by2 = this.skipTagForward();
                } else {
                    by2 = by;
                    if (c2 == '&') {
                        by2 = this.skipEntityForward();
                    }
                }
            }
            return by2;
        }

        int getEntryDir() {
            this.charIndex = 0;
            int n = 0;
            int n2 = 0;
            int n3 = 0;
            block13: while (this.charIndex < this.length && n3 == 0) {
                switch (this.dirTypeForward()) {
                    default: {
                        n3 = n;
                        continue block13;
                    }
                    case 18: {
                        --n;
                        n2 = 0;
                        continue block13;
                    }
                    case 16: 
                    case 17: {
                        ++n;
                        n2 = 1;
                        continue block13;
                    }
                    case 14: 
                    case 15: {
                        ++n;
                        n2 = -1;
                        continue block13;
                    }
                    case 9: {
                        continue block13;
                    }
                    case 1: 
                    case 2: {
                        if (n == 0) {
                            return 1;
                        }
                        n3 = n;
                        continue block13;
                    }
                    case 0: 
                }
                if (n == 0) {
                    return -1;
                }
                n3 = n;
            }
            if (n3 == 0) {
                return 0;
            }
            if (n2 != 0) {
                return n2;
            }
            block14: while (this.charIndex > 0) {
                switch (this.dirTypeBackward()) {
                    default: {
                        continue block14;
                    }
                    case 18: {
                        ++n;
                        continue block14;
                    }
                    case 16: 
                    case 17: {
                        if (n3 == n) {
                            return 1;
                        }
                        --n;
                        continue block14;
                    }
                    case 14: 
                    case 15: 
                }
                if (n3 == n) {
                    return -1;
                }
                --n;
            }
            return 0;
        }

        int getExitDir() {
            this.charIndex = this.length;
            int n = 0;
            int n2 = 0;
            block8: while (this.charIndex > 0) {
                switch (this.dirTypeBackward()) {
                    default: {
                        if (n2 != 0) continue block8;
                        n2 = n;
                        continue block8;
                    }
                    case 18: {
                        ++n;
                        continue block8;
                    }
                    case 16: 
                    case 17: {
                        if (n2 == n) {
                            return 1;
                        }
                        --n;
                        continue block8;
                    }
                    case 14: 
                    case 15: {
                        if (n2 == n) {
                            return -1;
                        }
                        --n;
                        continue block8;
                    }
                    case 9: {
                        continue block8;
                    }
                    case 1: 
                    case 2: {
                        if (n == 0) {
                            return 1;
                        }
                        if (n2 != 0) continue block8;
                        n2 = n;
                        continue block8;
                    }
                    case 0: 
                }
                if (n == 0) {
                    return -1;
                }
                if (n2 != 0) continue;
                n2 = n;
            }
            return 0;
        }
    }
}

