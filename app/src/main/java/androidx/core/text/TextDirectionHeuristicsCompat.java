/*
 * Decompiled with CFR 0.152.
 */
package androidx.core.text;

import androidx.core.text.TextDirectionHeuristicCompat;
import androidx.core.text.TextUtilsCompat;
import java.nio.CharBuffer;
import java.util.Locale;

public final class TextDirectionHeuristicsCompat {
    public static final TextDirectionHeuristicCompat ANYRTL_LTR;
    public static final TextDirectionHeuristicCompat FIRSTSTRONG_LTR;
    public static final TextDirectionHeuristicCompat FIRSTSTRONG_RTL;
    public static final TextDirectionHeuristicCompat LOCALE;
    public static final TextDirectionHeuristicCompat LTR;
    public static final TextDirectionHeuristicCompat RTL;
    private static final int STATE_FALSE = 1;
    private static final int STATE_TRUE = 0;
    private static final int STATE_UNKNOWN = 2;

    static {
        LTR = new TextDirectionHeuristicInternal(null, false);
        RTL = new TextDirectionHeuristicInternal(null, true);
        FIRSTSTRONG_LTR = new TextDirectionHeuristicInternal(FirstStrong.INSTANCE, false);
        FIRSTSTRONG_RTL = new TextDirectionHeuristicInternal(FirstStrong.INSTANCE, true);
        ANYRTL_LTR = new TextDirectionHeuristicInternal(AnyStrong.INSTANCE_RTL, false);
        LOCALE = TextDirectionHeuristicLocale.INSTANCE;
    }

    private TextDirectionHeuristicsCompat() {
    }

    static int isRtlText(int n) {
        switch (n) {
            default: {
                return 2;
            }
            case 1: 
            case 2: {
                return 0;
            }
            case 0: 
        }
        return 1;
    }

    static int isRtlTextOrFormat(int n) {
        switch (n) {
            default: {
                return 2;
            }
            case 1: 
            case 2: 
            case 16: 
            case 17: {
                return 0;
            }
            case 0: 
            case 14: 
            case 15: 
        }
        return 1;
    }

    private static class AnyStrong
    implements TextDirectionAlgorithm {
        static final AnyStrong INSTANCE_RTL = new AnyStrong(true);
        private final boolean mLookForRtl;

        private AnyStrong(boolean bl) {
            this.mLookForRtl = bl;
        }

        @Override
        public int checkRtl(CharSequence charSequence, int n, int n2) {
            boolean bl = false;
            block4: for (int i = n; i < n + n2; ++i) {
                switch (TextDirectionHeuristicsCompat.isRtlText(Character.getDirectionality(charSequence.charAt(i)))) {
                    default: {
                        continue block4;
                    }
                    case 1: {
                        if (!this.mLookForRtl) {
                            return 1;
                        }
                        bl = true;
                        continue block4;
                    }
                    case 0: {
                        if (this.mLookForRtl) {
                            return 0;
                        }
                        bl = true;
                    }
                }
            }
            if (bl) {
                return this.mLookForRtl ? 1 : 0;
            }
            return 2;
        }
    }

    private static class FirstStrong
    implements TextDirectionAlgorithm {
        static final FirstStrong INSTANCE = new FirstStrong();

        private FirstStrong() {
        }

        @Override
        public int checkRtl(CharSequence charSequence, int n, int n2) {
            int n3 = 2;
            for (int i = n; i < n + n2 && n3 == 2; ++i) {
                n3 = TextDirectionHeuristicsCompat.isRtlTextOrFormat(Character.getDirectionality(charSequence.charAt(i)));
            }
            return n3;
        }
    }

    private static interface TextDirectionAlgorithm {
        public int checkRtl(CharSequence var1, int var2, int var3);
    }

    private static abstract class TextDirectionHeuristicImpl
    implements TextDirectionHeuristicCompat {
        private final TextDirectionAlgorithm mAlgorithm;

        TextDirectionHeuristicImpl(TextDirectionAlgorithm textDirectionAlgorithm) {
            this.mAlgorithm = textDirectionAlgorithm;
        }

        private boolean doCheck(CharSequence charSequence, int n, int n2) {
            switch (this.mAlgorithm.checkRtl(charSequence, n, n2)) {
                default: {
                    return this.defaultIsRtl();
                }
                case 1: {
                    return false;
                }
                case 0: 
            }
            return true;
        }

        protected abstract boolean defaultIsRtl();

        @Override
        public boolean isRtl(CharSequence charSequence, int n, int n2) {
            if (charSequence != null && n >= 0 && n2 >= 0 && charSequence.length() - n2 >= n) {
                if (this.mAlgorithm == null) {
                    return this.defaultIsRtl();
                }
                return this.doCheck(charSequence, n, n2);
            }
            throw new IllegalArgumentException();
        }

        @Override
        public boolean isRtl(char[] cArray, int n, int n2) {
            return this.isRtl(CharBuffer.wrap(cArray), n, n2);
        }
    }

    private static class TextDirectionHeuristicInternal
    extends TextDirectionHeuristicImpl {
        private final boolean mDefaultIsRtl;

        TextDirectionHeuristicInternal(TextDirectionAlgorithm textDirectionAlgorithm, boolean bl) {
            super(textDirectionAlgorithm);
            this.mDefaultIsRtl = bl;
        }

        @Override
        protected boolean defaultIsRtl() {
            return this.mDefaultIsRtl;
        }
    }

    private static class TextDirectionHeuristicLocale
    extends TextDirectionHeuristicImpl {
        static final TextDirectionHeuristicLocale INSTANCE = new TextDirectionHeuristicLocale();

        TextDirectionHeuristicLocale() {
            super(null);
        }

        @Override
        protected boolean defaultIsRtl() {
            int n = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault());
            boolean bl = true;
            if (n != 1) {
                bl = false;
            }
            return bl;
        }
    }
}

