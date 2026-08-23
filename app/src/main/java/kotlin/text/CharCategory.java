/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import java.util.Map;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.text.CharCategory;

@Metadata(bv={1, 0, 3}, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\f\n\u0002\b \b\u0086\u0001\u0018\u0000 -2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001-B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0086\u0002R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nj\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013j\u0002\b\u0014j\u0002\b\u0015j\u0002\b\u0016j\u0002\b\u0017j\u0002\b\u0018j\u0002\b\u0019j\u0002\b\u001aj\u0002\b\u001bj\u0002\b\u001cj\u0002\b\u001dj\u0002\b\u001ej\u0002\b\u001fj\u0002\b j\u0002\b!j\u0002\b\"j\u0002\b#j\u0002\b$j\u0002\b%j\u0002\b&j\u0002\b'j\u0002\b(j\u0002\b)j\u0002\b*j\u0002\b+j\u0002\b,\u00a8\u0006."}, d2={"Lkotlin/text/CharCategory;", "", "value", "", "code", "", "(Ljava/lang/String;IILjava/lang/String;)V", "getCode", "()Ljava/lang/String;", "getValue", "()I", "contains", "", "char", "", "UNASSIGNED", "UPPERCASE_LETTER", "LOWERCASE_LETTER", "TITLECASE_LETTER", "MODIFIER_LETTER", "OTHER_LETTER", "NON_SPACING_MARK", "ENCLOSING_MARK", "COMBINING_SPACING_MARK", "DECIMAL_DIGIT_NUMBER", "LETTER_NUMBER", "OTHER_NUMBER", "SPACE_SEPARATOR", "LINE_SEPARATOR", "PARAGRAPH_SEPARATOR", "CONTROL", "FORMAT", "PRIVATE_USE", "SURROGATE", "DASH_PUNCTUATION", "START_PUNCTUATION", "END_PUNCTUATION", "CONNECTOR_PUNCTUATION", "OTHER_PUNCTUATION", "MATH_SYMBOL", "CURRENCY_SYMBOL", "MODIFIER_SYMBOL", "OTHER_SYMBOL", "INITIAL_QUOTE_PUNCTUATION", "FINAL_QUOTE_PUNCTUATION", "Companion", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class CharCategory
extends Enum<CharCategory> {
    private static final CharCategory[] $VALUES;
    public static final /* enum */ CharCategory COMBINING_SPACING_MARK;
    public static final /* enum */ CharCategory CONNECTOR_PUNCTUATION;
    public static final /* enum */ CharCategory CONTROL;
    public static final /* enum */ CharCategory CURRENCY_SYMBOL;
    public static final Companion Companion;
    public static final /* enum */ CharCategory DASH_PUNCTUATION;
    public static final /* enum */ CharCategory DECIMAL_DIGIT_NUMBER;
    public static final /* enum */ CharCategory ENCLOSING_MARK;
    public static final /* enum */ CharCategory END_PUNCTUATION;
    public static final /* enum */ CharCategory FINAL_QUOTE_PUNCTUATION;
    public static final /* enum */ CharCategory FORMAT;
    public static final /* enum */ CharCategory INITIAL_QUOTE_PUNCTUATION;
    public static final /* enum */ CharCategory LETTER_NUMBER;
    public static final /* enum */ CharCategory LINE_SEPARATOR;
    public static final /* enum */ CharCategory LOWERCASE_LETTER;
    public static final /* enum */ CharCategory MATH_SYMBOL;
    public static final /* enum */ CharCategory MODIFIER_LETTER;
    public static final /* enum */ CharCategory MODIFIER_SYMBOL;
    public static final /* enum */ CharCategory NON_SPACING_MARK;
    public static final /* enum */ CharCategory OTHER_LETTER;
    public static final /* enum */ CharCategory OTHER_NUMBER;
    public static final /* enum */ CharCategory OTHER_PUNCTUATION;
    public static final /* enum */ CharCategory OTHER_SYMBOL;
    public static final /* enum */ CharCategory PARAGRAPH_SEPARATOR;
    public static final /* enum */ CharCategory PRIVATE_USE;
    public static final /* enum */ CharCategory SPACE_SEPARATOR;
    public static final /* enum */ CharCategory START_PUNCTUATION;
    public static final /* enum */ CharCategory SURROGATE;
    public static final /* enum */ CharCategory TITLECASE_LETTER;
    public static final /* enum */ CharCategory UNASSIGNED;
    public static final /* enum */ CharCategory UPPERCASE_LETTER;
    private static final Lazy categoryMap$delegate;
    private final String code;
    private final int value;

    static {
        CharCategory charCategory;
        CharCategory charCategory2;
        CharCategory charCategory3;
        CharCategory charCategory4;
        CharCategory charCategory5;
        CharCategory charCategory6;
        CharCategory charCategory7;
        CharCategory charCategory8;
        CharCategory charCategory9;
        CharCategory charCategory10;
        CharCategory charCategory11;
        CharCategory charCategory12;
        CharCategory charCategory13;
        CharCategory charCategory14;
        CharCategory charCategory15;
        CharCategory charCategory16;
        CharCategory charCategory17;
        CharCategory charCategory18;
        CharCategory charCategory19;
        CharCategory charCategory20;
        CharCategory charCategory21;
        CharCategory charCategory22;
        CharCategory charCategory23;
        CharCategory charCategory24;
        CharCategory charCategory25;
        CharCategory charCategory26;
        CharCategory charCategory27;
        CharCategory charCategory28;
        CharCategory charCategory29;
        CharCategory charCategory30;
        UNASSIGNED = charCategory30 = new CharCategory(0, "Cn");
        UPPERCASE_LETTER = charCategory29 = new CharCategory(1, "Lu");
        LOWERCASE_LETTER = charCategory28 = new CharCategory(2, "Ll");
        TITLECASE_LETTER = charCategory27 = new CharCategory(3, "Lt");
        MODIFIER_LETTER = charCategory26 = new CharCategory(4, "Lm");
        OTHER_LETTER = charCategory25 = new CharCategory(5, "Lo");
        NON_SPACING_MARK = charCategory24 = new CharCategory(6, "Mn");
        ENCLOSING_MARK = charCategory23 = new CharCategory(7, "Me");
        COMBINING_SPACING_MARK = charCategory22 = new CharCategory(8, "Mc");
        DECIMAL_DIGIT_NUMBER = charCategory21 = new CharCategory(9, "Nd");
        LETTER_NUMBER = charCategory20 = new CharCategory(10, "Nl");
        OTHER_NUMBER = charCategory19 = new CharCategory(11, "No");
        SPACE_SEPARATOR = charCategory18 = new CharCategory(12, "Zs");
        LINE_SEPARATOR = charCategory17 = new CharCategory(13, "Zl");
        PARAGRAPH_SEPARATOR = charCategory16 = new CharCategory(14, "Zp");
        CONTROL = charCategory15 = new CharCategory(15, "Cc");
        FORMAT = charCategory14 = new CharCategory(16, "Cf");
        PRIVATE_USE = charCategory13 = new CharCategory(18, "Co");
        SURROGATE = charCategory12 = new CharCategory(19, "Cs");
        DASH_PUNCTUATION = charCategory11 = new CharCategory(20, "Pd");
        START_PUNCTUATION = charCategory10 = new CharCategory(21, "Ps");
        END_PUNCTUATION = charCategory9 = new CharCategory(22, "Pe");
        CONNECTOR_PUNCTUATION = charCategory8 = new CharCategory(23, "Pc");
        OTHER_PUNCTUATION = charCategory7 = new CharCategory(24, "Po");
        MATH_SYMBOL = charCategory6 = new CharCategory(25, "Sm");
        CURRENCY_SYMBOL = charCategory5 = new CharCategory(26, "Sc");
        MODIFIER_SYMBOL = charCategory4 = new CharCategory(27, "Sk");
        OTHER_SYMBOL = charCategory3 = new CharCategory(28, "So");
        INITIAL_QUOTE_PUNCTUATION = charCategory2 = new CharCategory(29, "Pi");
        FINAL_QUOTE_PUNCTUATION = charCategory = new CharCategory(30, "Pf");
        $VALUES = new CharCategory[]{charCategory30, charCategory29, charCategory28, charCategory27, charCategory26, charCategory25, charCategory24, charCategory23, charCategory22, charCategory21, charCategory20, charCategory19, charCategory18, charCategory17, charCategory16, charCategory15, charCategory14, charCategory13, charCategory12, charCategory11, charCategory10, charCategory9, charCategory8, charCategory7, charCategory6, charCategory5, charCategory4, charCategory3, charCategory2, charCategory};
        Companion = new Companion(null);
        categoryMap$delegate = LazyKt.lazy(Companion.categoryMap.2.INSTANCE);
    }

    private CharCategory(int n2, String string3) {
        this.value = n2;
        this.code = string3;
    }

    public static CharCategory valueOf(String string2) {
        return Enum.valueOf(CharCategory.class, string2);
    }

    public static CharCategory[] values() {
        return (CharCategory[])$VALUES.clone();
    }

    public final boolean contains(char c) {
        boolean bl = Character.getType(c) == this.value;
        return bl;
    }

    public final String getCode() {
        return this.code;
    }

    public final int getValue() {
        return this.value;
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u0005R'\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u00048BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\r"}, d2={"Lkotlin/text/CharCategory$Companion;", "", "()V", "categoryMap", "", "", "Lkotlin/text/CharCategory;", "getCategoryMap", "()Ljava/util/Map;", "categoryMap$delegate", "Lkotlin/Lazy;", "valueOf", "category", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final Map<Integer, CharCategory> getCategoryMap() {
            Lazy lazy = categoryMap$delegate;
            Companion companion = Companion;
            return (Map)lazy.getValue();
        }

        public final CharCategory valueOf(int n) {
            Object object = this.getCategoryMap().get(n);
            if (object != null) {
                return object;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Category #");
            ((StringBuilder)object).append(n);
            ((StringBuilder)object).append(" is not defined.");
            throw (Throwable)new IllegalArgumentException(((StringBuilder)object).toString());
        }
    }
}

