/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.text.CharCategory;
import kotlin.text.CharDirectionality;
import kotlin.text.CharsKt;

@Metadata(bv={1, 0, 3}, d1={"\u00004\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u001a\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0001\u001a\u0018\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\nH\u0000\u001a\r\u0010\u000e\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0010\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0011\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0012\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0013\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0014\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0016\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0018\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0019\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u001a\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u001b\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\n\u0010\u001c\u001a\u00020\u000f*\u00020\u0002\u001a\r\u0010\u001d\u001a\u00020\u001e*\u00020\u0002H\u0087\b\u001a\u0014\u0010\u001d\u001a\u00020\u001e*\u00020\u00022\u0006\u0010\u001f\u001a\u00020 H\u0007\u001a\r\u0010!\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\f\u0010\"\u001a\u00020\u001e*\u00020\u0002H\u0007\u001a\u0014\u0010\"\u001a\u00020\u001e*\u00020\u00022\u0006\u0010\u001f\u001a\u00020 H\u0007\u001a\r\u0010#\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010$\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010%\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010&\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010'\u001a\u00020\u001e*\u00020\u0002H\u0087\b\u001a\u0014\u0010'\u001a\u00020\u001e*\u00020\u00022\u0006\u0010\u001f\u001a\u00020 H\u0007\u001a\r\u0010(\u001a\u00020\u0002*\u00020\u0002H\u0087\b\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\b\u00a8\u0006)"}, d2={"category", "Lkotlin/text/CharCategory;", "", "getCategory", "(C)Lkotlin/text/CharCategory;", "directionality", "Lkotlin/text/CharDirectionality;", "getDirectionality", "(C)Lkotlin/text/CharDirectionality;", "checkRadix", "", "radix", "digitOf", "char", "isDefined", "", "isDigit", "isHighSurrogate", "isISOControl", "isIdentifierIgnorable", "isJavaIdentifierPart", "isJavaIdentifierStart", "isLetter", "isLetterOrDigit", "isLowSurrogate", "isLowerCase", "isTitleCase", "isUpperCase", "isWhitespace", "lowercase", "", "locale", "Ljava/util/Locale;", "lowercaseChar", "titlecase", "titlecaseChar", "toLowerCase", "toTitleCase", "toUpperCase", "uppercase", "uppercaseChar", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/text/CharsKt")
class CharsKt__CharJVMKt {
    public static final int checkRadix(int n) {
        if (2 <= n && 36 >= n) {
            return n;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("radix ");
        stringBuilder.append(n);
        stringBuilder.append(" was not in valid range ");
        stringBuilder.append(new IntRange(2, 36));
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final int digitOf(char c, int n) {
        return Character.digit((int)c, n);
    }

    public static final CharCategory getCategory(char c) {
        return CharCategory.Companion.valueOf(Character.getType(c));
    }

    public static final CharDirectionality getDirectionality(char c) {
        return CharDirectionality.Companion.valueOf(Character.getDirectionality(c));
    }

    private static final boolean isDefined(char c) {
        return Character.isDefined(c);
    }

    private static final boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    private static final boolean isHighSurrogate(char c) {
        return Character.isHighSurrogate(c);
    }

    private static final boolean isISOControl(char c) {
        return Character.isISOControl(c);
    }

    private static final boolean isIdentifierIgnorable(char c) {
        return Character.isIdentifierIgnorable(c);
    }

    private static final boolean isJavaIdentifierPart(char c) {
        return Character.isJavaIdentifierPart(c);
    }

    private static final boolean isJavaIdentifierStart(char c) {
        return Character.isJavaIdentifierStart(c);
    }

    private static final boolean isLetter(char c) {
        return Character.isLetter(c);
    }

    private static final boolean isLetterOrDigit(char c) {
        return Character.isLetterOrDigit(c);
    }

    private static final boolean isLowSurrogate(char c) {
        return Character.isLowSurrogate(c);
    }

    private static final boolean isLowerCase(char c) {
        return Character.isLowerCase(c);
    }

    private static final boolean isTitleCase(char c) {
        return Character.isTitleCase(c);
    }

    private static final boolean isUpperCase(char c) {
        return Character.isUpperCase(c);
    }

    public static final boolean isWhitespace(char c) {
        boolean bl = Character.isWhitespace(c) || Character.isSpaceChar(c);
        return bl;
    }

    private static final String lowercase(char c) {
        String string2 = String.valueOf(c);
        if (string2 != null) {
            string2 = string2.toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026.toLowerCase(Locale.ROOT)");
            return string2;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public static final String lowercase(char c, Locale object) {
        Intrinsics.checkNotNullParameter(object, "locale");
        String string2 = String.valueOf(c);
        if (string2 != null) {
            object = string2.toLowerCase((Locale)object);
            Intrinsics.checkNotNullExpressionValue(object, "(this as java.lang.String).toLowerCase(locale)");
            return object;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final char lowercaseChar(char c) {
        return Character.toLowerCase(c);
    }

    public static final String titlecase(char c) {
        block2: {
            block3: {
                block5: {
                    block6: {
                        String string2;
                        block4: {
                            string2 = String.valueOf(c);
                            if (string2 == null) break block2;
                            string2 = string2.toUpperCase(Locale.ROOT);
                            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026.toUpperCase(Locale.ROOT)");
                            if (string2.length() <= 1) break block3;
                            if (c == '\u0149') break block4;
                            c = string2.charAt(0);
                            if (string2 == null) break block5;
                            string2 = string2.substring(1);
                            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.String).substring(startIndex)");
                            if (string2 == null) break block6;
                            string2 = string2.toLowerCase(Locale.ROOT);
                            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026.toLowerCase(Locale.ROOT)");
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(String.valueOf(c));
                            stringBuilder.append(string2);
                            string2 = stringBuilder.toString();
                        }
                        return string2;
                    }
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            return String.valueOf(Character.toTitleCase(c));
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public static final String titlecase(char c, Locale object) {
        block5: {
            block7: {
                block8: {
                    block6: {
                        Intrinsics.checkNotNullParameter(object, "locale");
                        object = CharsKt.uppercase(c, (Locale)object);
                        if (((String)object).length() <= 1) break block5;
                        if (c == '\u0149') break block6;
                        c = ((String)object).charAt(0);
                        if (object == null) break block7;
                        object = ((String)object).substring(1);
                        Intrinsics.checkNotNullExpressionValue(object, "(this as java.lang.String).substring(startIndex)");
                        if (object == null) break block8;
                        object = ((String)object).toLowerCase(Locale.ROOT);
                        Intrinsics.checkNotNullExpressionValue(object, "(this as java.lang.Strin\u2026.toLowerCase(Locale.ROOT)");
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(String.valueOf(c));
                        stringBuilder.append((String)object);
                        object = stringBuilder.toString();
                    }
                    return object;
                }
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        String string2 = String.valueOf(c);
        if (string2 != null) {
            string2 = string2.toUpperCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026.toUpperCase(Locale.ROOT)");
            if (Intrinsics.areEqual(object, string2) ^ true) {
                return object;
            }
            return String.valueOf(Character.toTitleCase(c));
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final char titlecaseChar(char c) {
        return Character.toTitleCase(c);
    }

    private static final char toLowerCase(char c) {
        return Character.toLowerCase(c);
    }

    private static final char toTitleCase(char c) {
        return Character.toTitleCase(c);
    }

    private static final char toUpperCase(char c) {
        return Character.toUpperCase(c);
    }

    private static final String uppercase(char c) {
        String string2 = String.valueOf(c);
        if (string2 != null) {
            string2 = string2.toUpperCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026.toUpperCase(Locale.ROOT)");
            return string2;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public static final String uppercase(char c, Locale object) {
        Intrinsics.checkNotNullParameter(object, "locale");
        String string2 = String.valueOf(c);
        if (string2 != null) {
            object = string2.toUpperCase((Locale)object);
            Intrinsics.checkNotNullExpressionValue(object, "(this as java.lang.String).toUpperCase(locale)");
            return object;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final char uppercaseChar(char c) {
        return Character.toUpperCase(c);
    }
}

