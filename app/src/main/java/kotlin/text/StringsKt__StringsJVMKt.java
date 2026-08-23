/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.collections.IntIterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.ranges.RangesKt;
import kotlin.text.CharsKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import kotlin.text.StringsKt__StringNumberConversionsKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000~\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0019\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\r\n\u0002\b\t\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\n\n\u0002\u0010\f\n\u0002\b\u0011\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\tH\u0087\b\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\u000bH\u0087\b\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\u0019\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0087\b\u001a!\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0087\b\u001a)\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u000e\u001a\u00020\u000fH\u0087\b\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0014H\u0087\b\u001a!\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0087\b\u001a!\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0087\b\u001a\n\u0010\u0017\u001a\u00020\u0002*\u00020\u0002\u001a\u0014\u0010\u0017\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0007\u001a\u0015\u0010\u001a\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0011H\u0087\b\u001a\u0015\u0010\u001c\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0011H\u0087\b\u001a\u001d\u0010\u001d\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u0011H\u0087\b\u001a\u001c\u0010 \u001a\u00020\u0011*\u00020\u00022\u0006\u0010!\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a\f\u0010$\u001a\u00020\u0002*\u00020\u0014H\u0007\u001a \u0010$\u001a\u00020\u0002*\u00020\u00142\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u0011H\u0007\u001a\u0015\u0010&\u001a\u00020#*\u00020\u00022\u0006\u0010\n\u001a\u00020\tH\u0087\b\u001a\u0015\u0010&\u001a\u00020#*\u00020\u00022\u0006\u0010'\u001a\u00020(H\u0087\b\u001a\n\u0010)\u001a\u00020\u0002*\u00020\u0002\u001a\u0014\u0010)\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0007\u001a\f\u0010*\u001a\u00020\u0002*\u00020\rH\u0007\u001a*\u0010*\u001a\u00020\u0002*\u00020\r2\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u00112\b\b\u0002\u0010+\u001a\u00020#H\u0007\u001a\f\u0010,\u001a\u00020\r*\u00020\u0002H\u0007\u001a*\u0010,\u001a\u00020\r*\u00020\u00022\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u00112\b\b\u0002\u0010+\u001a\u00020#H\u0007\u001a\u001c\u0010-\u001a\u00020#*\u00020\u00022\u0006\u0010.\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a \u0010/\u001a\u00020#*\u0004\u0018\u00010\u00022\b\u0010!\u001a\u0004\u0018\u00010\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a2\u00100\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u00192\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0002\u00104\u001a6\u00100\u001a\u00020\u0002*\u00020\u00022\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0004\b5\u00104\u001a*\u00100\u001a\u00020\u0002*\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0002\u00106\u001a:\u00100\u001a\u00020\u0002*\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u00100\u001a\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0002\u00107\u001a>\u00100\u001a\u00020\u0002*\u00020\u00042\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u00100\u001a\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0004\b5\u00107\u001a2\u00100\u001a\u00020\u0002*\u00020\u00042\u0006\u00100\u001a\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b\u00a2\u0006\u0002\u00108\u001a\r\u00109\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\n\u0010:\u001a\u00020#*\u00020(\u001a\r\u0010;\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010;\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\u001a\u001d\u0010<\u001a\u00020\u0011*\u00020\u00022\u0006\u0010=\u001a\u00020>2\u0006\u0010?\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010<\u001a\u00020\u0011*\u00020\u00022\u0006\u0010@\u001a\u00020\u00022\u0006\u0010?\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010A\u001a\u00020\u0011*\u00020\u00022\u0006\u0010=\u001a\u00020>2\u0006\u0010?\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010A\u001a\u00020\u0011*\u00020\u00022\u0006\u0010@\u001a\u00020\u00022\u0006\u0010?\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010B\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u00112\u0006\u0010C\u001a\u00020\u0011H\u0087\b\u001a4\u0010D\u001a\u00020#*\u00020(2\u0006\u0010E\u001a\u00020\u00112\u0006\u0010!\u001a\u00020(2\u0006\u0010F\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\b\b\u0002\u0010\"\u001a\u00020#\u001a4\u0010D\u001a\u00020#*\u00020\u00022\u0006\u0010E\u001a\u00020\u00112\u0006\u0010!\u001a\u00020\u00022\u0006\u0010F\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\b\b\u0002\u0010\"\u001a\u00020#\u001a\u0012\u0010G\u001a\u00020\u0002*\u00020(2\u0006\u0010H\u001a\u00020\u0011\u001a$\u0010I\u001a\u00020\u0002*\u00020\u00022\u0006\u0010J\u001a\u00020>2\u0006\u0010K\u001a\u00020>2\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010I\u001a\u00020\u0002*\u00020\u00022\u0006\u0010L\u001a\u00020\u00022\u0006\u0010M\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010N\u001a\u00020\u0002*\u00020\u00022\u0006\u0010J\u001a\u00020>2\u0006\u0010K\u001a\u00020>2\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010N\u001a\u00020\u0002*\u00020\u00022\u0006\u0010L\u001a\u00020\u00022\u0006\u0010M\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a\"\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00020P*\u00020(2\u0006\u0010Q\u001a\u00020R2\b\b\u0002\u0010S\u001a\u00020\u0011\u001a\u001c\u0010T\u001a\u00020#*\u00020\u00022\u0006\u0010U\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010T\u001a\u00020#*\u00020\u00022\u0006\u0010U\u001a\u00020\u00022\u0006\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\"\u001a\u00020#\u001a\u0015\u0010V\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0011H\u0087\b\u001a\u001d\u0010V\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u0011H\u0087\b\u001a\u0017\u0010W\u001a\u00020\r*\u00020\u00022\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0087\b\u001a\r\u0010X\u001a\u00020\u0014*\u00020\u0002H\u0087\b\u001a3\u0010X\u001a\u00020\u0014*\u00020\u00022\u0006\u0010Y\u001a\u00020\u00142\b\b\u0002\u0010Z\u001a\u00020\u00112\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u0011H\u0087\b\u001a \u0010X\u001a\u00020\u0014*\u00020\u00022\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u0011H\u0007\u001a\r\u0010[\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010[\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\u001a\u0017\u0010\\\u001a\u00020R*\u00020\u00022\b\b\u0002\u0010]\u001a\u00020\u0011H\u0087\b\u001a\r\u0010^\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010^\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\u001a\r\u0010_\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010_\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\"%\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u00020\u00020\u0001j\b\u0012\u0004\u0012\u00020\u0002`\u0003*\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006`"}, d2={"CASE_INSENSITIVE_ORDER", "Ljava/util/Comparator;", "", "Lkotlin/Comparator;", "Lkotlin/String$Companion;", "getCASE_INSENSITIVE_ORDER", "(Lkotlin/jvm/internal/StringCompanionObject;)Ljava/util/Comparator;", "String", "stringBuffer", "Ljava/lang/StringBuffer;", "stringBuilder", "Ljava/lang/StringBuilder;", "bytes", "", "charset", "Ljava/nio/charset/Charset;", "offset", "", "length", "chars", "", "codePoints", "", "capitalize", "locale", "Ljava/util/Locale;", "codePointAt", "index", "codePointBefore", "codePointCount", "beginIndex", "endIndex", "compareTo", "other", "ignoreCase", "", "concatToString", "startIndex", "contentEquals", "charSequence", "", "decapitalize", "decodeToString", "throwOnInvalidSequence", "encodeToByteArray", "endsWith", "suffix", "equals", "format", "args", "", "", "(Ljava/lang/String;Ljava/util/Locale;[Ljava/lang/Object;)Ljava/lang/String;", "formatNullable", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "(Lkotlin/jvm/internal/StringCompanionObject;Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "(Lkotlin/jvm/internal/StringCompanionObject;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "intern", "isBlank", "lowercase", "nativeIndexOf", "ch", "", "fromIndex", "str", "nativeLastIndexOf", "offsetByCodePoints", "codePointOffset", "regionMatches", "thisOffset", "otherOffset", "repeat", "n", "replace", "oldChar", "newChar", "oldValue", "newValue", "replaceFirst", "split", "", "regex", "Ljava/util/regex/Pattern;", "limit", "startsWith", "prefix", "substring", "toByteArray", "toCharArray", "destination", "destinationOffset", "toLowerCase", "toPattern", "flags", "toUpperCase", "uppercase", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/text/StringsKt")
class StringsKt__StringsJVMKt
extends StringsKt__StringNumberConversionsKt {
    private static final String String(StringBuffer stringBuffer) {
        return new String(stringBuffer);
    }

    private static final String String(StringBuilder stringBuilder) {
        return new String(stringBuilder);
    }

    private static final String String(byte[] byArray) {
        return new String(byArray, Charsets.UTF_8);
    }

    private static final String String(byte[] byArray, int n, int n2) {
        return new String(byArray, n, n2, Charsets.UTF_8);
    }

    private static final String String(byte[] byArray, int n, int n2, Charset charset) {
        return new String(byArray, n, n2, charset);
    }

    private static final String String(byte[] byArray, Charset charset) {
        return new String(byArray, charset);
    }

    private static final String String(char[] cArray) {
        return new String(cArray);
    }

    private static final String String(char[] cArray, int n, int n2) {
        return new String(cArray, n, n2);
    }

    private static final String String(int[] nArray, int n, int n2) {
        return new String(nArray, n, n2);
    }

    public static final String capitalize(String string2) {
        Intrinsics.checkNotNullParameter(string2, "$this$capitalize");
        Locale locale = Locale.getDefault();
        Intrinsics.checkNotNullExpressionValue(locale, "Locale.getDefault()");
        return StringsKt.capitalize(string2, locale);
    }

    public static final String capitalize(String string2, Locale object) {
        block2: {
            block5: {
                StringBuilder stringBuilder;
                block4: {
                    block3: {
                        char c;
                        Intrinsics.checkNotNullParameter(string2, "$this$capitalize");
                        Intrinsics.checkNotNullParameter(object, "locale");
                        boolean bl = ((CharSequence)string2).length() > 0;
                        if (!bl || !Character.isLowerCase(c = string2.charAt(0))) break block2;
                        stringBuilder = new StringBuilder();
                        char c2 = Character.toTitleCase(c);
                        if (c2 == Character.toUpperCase(c)) break block3;
                        stringBuilder.append(c2);
                        break block4;
                    }
                    String string3 = string2.substring(0, 1);
                    Intrinsics.checkNotNullExpressionValue(string3, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                    if (string3 == null) break block5;
                    object = string3.toUpperCase((Locale)object);
                    Intrinsics.checkNotNullExpressionValue(object, "(this as java.lang.String).toUpperCase(locale)");
                    stringBuilder.append((String)object);
                }
                string2 = string2.substring(1);
                Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.String).substring(startIndex)");
                stringBuilder.append(string2);
                string2 = stringBuilder.toString();
                Intrinsics.checkNotNullExpressionValue(string2, "StringBuilder().apply(builderAction).toString()");
                return string2;
            }
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        return string2;
    }

    private static final int codePointAt(String string2, int n) {
        if (string2 != null) {
            return string2.codePointAt(n);
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final int codePointBefore(String string2, int n) {
        if (string2 != null) {
            return string2.codePointBefore(n);
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final int codePointCount(String string2, int n, int n2) {
        if (string2 != null) {
            return string2.codePointCount(n, n2);
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public static final int compareTo(String string2, String string3, boolean bl) {
        Intrinsics.checkNotNullParameter(string2, "$this$compareTo");
        Intrinsics.checkNotNullParameter(string3, "other");
        if (bl) {
            return string2.compareToIgnoreCase(string3);
        }
        return string2.compareTo(string3);
    }

    public static /* synthetic */ int compareTo$default(String string2, String string3, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.compareTo(string2, string3, bl);
    }

    public static final String concatToString(char[] cArray) {
        Intrinsics.checkNotNullParameter(cArray, "$this$concatToString");
        return new String(cArray);
    }

    public static final String concatToString(char[] cArray, int n, int n2) {
        Intrinsics.checkNotNullParameter(cArray, "$this$concatToString");
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(n, n2, cArray.length);
        return new String(cArray, n, n2 - n);
    }

    public static /* synthetic */ String concatToString$default(char[] cArray, int n, int n2, int n3, Object object) {
        if ((n3 & 1) != 0) {
            n = 0;
        }
        if ((n3 & 2) != 0) {
            n2 = cArray.length;
        }
        return StringsKt.concatToString(cArray, n, n2);
    }

    private static final boolean contentEquals(String string2, CharSequence charSequence) {
        if (string2 != null) {
            return string2.contentEquals(charSequence);
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final boolean contentEquals(String string2, StringBuffer stringBuffer) {
        if (string2 != null) {
            return string2.contentEquals(stringBuffer);
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public static final String decapitalize(String string2) {
        Intrinsics.checkNotNullParameter(string2, "$this$decapitalize");
        boolean bl = ((CharSequence)string2).length() > 0;
        if (bl && !Character.isLowerCase(string2.charAt(0))) {
            StringBuilder stringBuilder = new StringBuilder();
            String string3 = string2.substring(0, 1);
            Intrinsics.checkNotNullExpressionValue(string3, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            if (string3 != null) {
                string3 = string3.toLowerCase();
                Intrinsics.checkNotNullExpressionValue(string3, "(this as java.lang.String).toLowerCase()");
                stringBuilder.append(string3);
                string2 = string2.substring(1);
                Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.String).substring(startIndex)");
                stringBuilder.append(string2);
                string2 = stringBuilder.toString();
            } else {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
        }
        return string2;
    }

    public static final String decapitalize(String string2, Locale object) {
        Intrinsics.checkNotNullParameter(string2, "$this$decapitalize");
        Intrinsics.checkNotNullParameter(object, "locale");
        boolean bl = ((CharSequence)string2).length() > 0;
        if (bl && !Character.isLowerCase(string2.charAt(0))) {
            StringBuilder stringBuilder = new StringBuilder();
            String string3 = string2.substring(0, 1);
            Intrinsics.checkNotNullExpressionValue(string3, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            if (string3 != null) {
                object = string3.toLowerCase((Locale)object);
                Intrinsics.checkNotNullExpressionValue(object, "(this as java.lang.String).toLowerCase(locale)");
                stringBuilder.append((String)object);
                string2 = string2.substring(1);
                Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.String).substring(startIndex)");
                stringBuilder.append(string2);
                string2 = stringBuilder.toString();
            } else {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
        }
        return string2;
    }

    public static final String decodeToString(byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "$this$decodeToString");
        return new String(byArray, Charsets.UTF_8);
    }

    public static final String decodeToString(byte[] object, int n, int n2, boolean bl) {
        Intrinsics.checkNotNullParameter(object, "$this$decodeToString");
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(n, n2, ((byte[])object).length);
        if (!bl) {
            return new String((byte[])object, n, n2 - n, Charsets.UTF_8);
        }
        CharsetDecoder charsetDecoder = Charsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
        object = charsetDecoder.decode(ByteBuffer.wrap(object, n, n2 - n)).toString();
        Intrinsics.checkNotNullExpressionValue(object, "decoder.decode(ByteBuffe\u2026- startIndex)).toString()");
        return object;
    }

    public static /* synthetic */ String decodeToString$default(byte[] byArray, int n, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 1) != 0) {
            n = 0;
        }
        if ((n3 & 2) != 0) {
            n2 = byArray.length;
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.decodeToString(byArray, n, n2, bl);
    }

    public static final byte[] encodeToByteArray(String object) {
        Intrinsics.checkNotNullParameter(object, "$this$encodeToByteArray");
        object = ((String)object).getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(object, "(this as java.lang.String).getBytes(charset)");
        return object;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static final byte[] encodeToByteArray(String object, int n, int n2, boolean bl) {
        Intrinsics.checkNotNullParameter(object, "$this$encodeToByteArray");
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(n, n2, ((String)object).length());
        if (!bl) {
            object = ((String)object).substring(n, n2);
            Intrinsics.checkNotNullExpressionValue(object, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            Charset charset = Charsets.UTF_8;
            if (object == null) throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            object = ((String)object).getBytes(charset);
            Intrinsics.checkNotNullExpressionValue(object, "(this as java.lang.String).getBytes(charset)");
            return object;
        }
        Object object2 = Charsets.UTF_8.newEncoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
        if (((ByteBuffer)(object2 = ((CharsetEncoder)object2).encode(CharBuffer.wrap((CharSequence)object, n, n2)))).hasArray() && ((ByteBuffer)object2).arrayOffset() == 0) {
            n = ((Buffer)object2).remaining();
            object = ((ByteBuffer)object2).array();
            Intrinsics.checkNotNull(object);
            if (n == ((Object)object).length) {
                object = ((ByteBuffer)object2).array();
                Intrinsics.checkNotNullExpressionValue(object, "byteBuffer.array()");
                return object;
            }
        }
        object = new byte[((Buffer)object2).remaining()];
        ((ByteBuffer)object2).get((byte[])object);
        return object;
    }

    public static /* synthetic */ byte[] encodeToByteArray$default(String string2, int n, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 1) != 0) {
            n = 0;
        }
        if ((n3 & 2) != 0) {
            n2 = string2.length();
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.encodeToByteArray(string2, n, n2, bl);
    }

    public static final boolean endsWith(String string2, String string3, boolean bl) {
        Intrinsics.checkNotNullParameter(string2, "$this$endsWith");
        Intrinsics.checkNotNullParameter(string3, "suffix");
        if (!bl) {
            return string2.endsWith(string3);
        }
        return StringsKt.regionMatches(string2, string2.length() - string3.length(), string3, 0, string3.length(), true);
    }

    public static /* synthetic */ boolean endsWith$default(String string2, String string3, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.endsWith(string2, string3, bl);
    }

    public static final boolean equals(String string2, String string3, boolean bl) {
        if (string2 == null) {
            bl = string3 == null;
            return bl;
        }
        bl = !bl ? string2.equals(string3) : string2.equalsIgnoreCase(string3);
        return bl;
    }

    public static /* synthetic */ boolean equals$default(String string2, String string3, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.equals(string2, string3, bl);
    }

    @Deprecated(message="Use Kotlin compiler 1.4 to avoid deprecation warning.")
    @DeprecatedSinceKotlin(hiddenSince="1.4")
    private static final /* synthetic */ String format(String string2, Locale locale, Object ... objectArray) {
        string2 = String.format(locale, string2, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkNotNullExpressionValue(string2, "java.lang.String.format(locale, this, *args)");
        return string2;
    }

    private static final String format(String string2, Object ... objectArray) {
        string2 = String.format(string2, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkNotNullExpressionValue(string2, "java.lang.String.format(this, *args)");
        return string2;
    }

    private static final String format(StringCompanionObject object, String string2, Object ... objectArray) {
        object = String.format(string2, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkNotNullExpressionValue(object, "java.lang.String.format(format, *args)");
        return object;
    }

    @Deprecated(message="Use Kotlin compiler 1.4 to avoid deprecation warning.")
    @DeprecatedSinceKotlin(hiddenSince="1.4")
    private static final /* synthetic */ String format(StringCompanionObject object, Locale locale, String string2, Object ... objectArray) {
        object = String.format(locale, string2, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkNotNullExpressionValue(object, "java.lang.String.format(locale, format, *args)");
        return object;
    }

    private static final String formatNullable(String string2, Locale locale, Object ... objectArray) {
        string2 = String.format(locale, string2, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkNotNullExpressionValue(string2, "java.lang.String.format(locale, this, *args)");
        return string2;
    }

    private static final String formatNullable(StringCompanionObject object, Locale locale, String string2, Object ... objectArray) {
        object = String.format(locale, string2, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkNotNullExpressionValue(object, "java.lang.String.format(locale, format, *args)");
        return object;
    }

    public static final Comparator<String> getCASE_INSENSITIVE_ORDER(StringCompanionObject object) {
        Intrinsics.checkNotNullParameter(object, "$this$CASE_INSENSITIVE_ORDER");
        object = String.CASE_INSENSITIVE_ORDER;
        Intrinsics.checkNotNullExpressionValue(object, "java.lang.String.CASE_INSENSITIVE_ORDER");
        return object;
    }

    private static final String intern(String string2) {
        if (string2 != null) {
            string2 = string2.intern();
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.String).intern()");
            return string2;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public static final boolean isBlank(CharSequence charSequence) {
        boolean bl;
        block9: {
            block8: {
                int n;
                block7: {
                    Intrinsics.checkNotNullParameter(charSequence, "$this$isBlank");
                    n = charSequence.length();
                    bl = false;
                    if (n == 0) break block8;
                    Object object = StringsKt.getIndices(charSequence);
                    if (object instanceof Collection && ((Collection)object).isEmpty()) {
                        n = 1;
                    } else {
                        object = object.iterator();
                        while (object.hasNext()) {
                            if (CharsKt.isWhitespace(charSequence.charAt(((IntIterator)object).nextInt()))) continue;
                            n = 0;
                            break block7;
                        }
                        n = 1;
                    }
                }
                if (n == 0) break block9;
            }
            bl = true;
        }
        return bl;
    }

    private static final String lowercase(String string2) {
        if (string2 != null) {
            string2 = string2.toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026.toLowerCase(Locale.ROOT)");
            return string2;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final String lowercase(String string2, Locale locale) {
        if (string2 != null) {
            string2 = string2.toLowerCase(locale);
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.String).toLowerCase(locale)");
            return string2;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final int nativeIndexOf(String string2, char c, int n) {
        if (string2 != null) {
            return string2.indexOf(c, n);
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final int nativeIndexOf(String string2, String string3, int n) {
        if (string2 != null) {
            return string2.indexOf(string3, n);
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final int nativeLastIndexOf(String string2, char c, int n) {
        if (string2 != null) {
            return string2.lastIndexOf(c, n);
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final int nativeLastIndexOf(String string2, String string3, int n) {
        if (string2 != null) {
            return string2.lastIndexOf(string3, n);
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final int offsetByCodePoints(String string2, int n, int n2) {
        if (string2 != null) {
            return string2.offsetByCodePoints(n, n2);
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public static final boolean regionMatches(CharSequence charSequence, int n, CharSequence charSequence2, int n2, int n3, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$regionMatches");
        Intrinsics.checkNotNullParameter(charSequence2, "other");
        if (charSequence instanceof String && charSequence2 instanceof String) {
            return StringsKt.regionMatches((String)charSequence, n, (String)charSequence2, n2, n3, bl);
        }
        return StringsKt.regionMatchesImpl(charSequence, n, charSequence2, n2, n3, bl);
    }

    public static final boolean regionMatches(String string2, int n, String string3, int n2, int n3, boolean bl) {
        Intrinsics.checkNotNullParameter(string2, "$this$regionMatches");
        Intrinsics.checkNotNullParameter(string3, "other");
        bl = !bl ? string2.regionMatches(n, string3, n2, n3) : string2.regionMatches(bl, n, string3, n2, n3);
        return bl;
    }

    public static /* synthetic */ boolean regionMatches$default(CharSequence charSequence, int n, CharSequence charSequence2, int n2, int n3, boolean bl, int n4, Object object) {
        block0: {
            if ((n4 & 0x10) == 0) break block0;
            bl = false;
        }
        return StringsKt.regionMatches(charSequence, n, charSequence2, n2, n3, bl);
    }

    public static /* synthetic */ boolean regionMatches$default(String string2, int n, String string3, int n2, int n3, boolean bl, int n4, Object object) {
        block0: {
            if ((n4 & 0x10) == 0) break block0;
            bl = false;
        }
        return StringsKt.regionMatches(string2, n, string3, n2, n3, bl);
    }

    /*
     * Exception decompiling
     */
    public static final String repeat(CharSequence var0, int var1_1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[CASE]], but top level block is 4[SWITCH]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public static final String replace(String charSequence, char c, char c2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$replace");
        if (!bl) {
            charSequence = ((String)charSequence).replace(c, c2);
            Intrinsics.checkNotNullExpressionValue(charSequence, "(this as java.lang.Strin\u2026replace(oldChar, newChar)");
            return charSequence;
        }
        StringBuilder stringBuilder = new StringBuilder(((String)charSequence).length());
        charSequence = charSequence;
        for (int i = 0; i < charSequence.length(); ++i) {
            char c3 = charSequence.charAt(i);
            if (CharsKt.equals(c3, c, bl)) {
                c3 = c2;
            }
            stringBuilder.append(c3);
        }
        charSequence = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(charSequence, "StringBuilder(capacity).\u2026builderAction).toString()");
        return charSequence;
    }

    public static final String replace(String object, String string2, String string3, boolean bl) {
        Intrinsics.checkNotNullParameter(object, "$this$replace");
        Intrinsics.checkNotNullParameter(string2, "oldValue");
        Intrinsics.checkNotNullParameter(string3, "newValue");
        int n = StringsKt.indexOf((CharSequence)object, string2, 0, bl);
        if (n < 0) {
            return object;
        }
        int n2 = string2.length();
        int n3 = RangesKt.coerceAtLeast(n2, 1);
        int n4 = ((String)object).length() - n2 + string3.length();
        if (n4 >= 0) {
            int n5;
            int n6;
            StringBuilder stringBuilder = new StringBuilder(n4);
            n4 = 0;
            do {
                stringBuilder.append((CharSequence)object, n4, n);
                stringBuilder.append(string3);
                n5 = n + n2;
                if (n >= ((String)object).length()) break;
                n = n6 = StringsKt.indexOf((CharSequence)object, string2, n + n3, bl);
                n4 = n5;
            } while (n6 > 0);
            stringBuilder.append((CharSequence)object, n5, ((String)object).length());
            object = stringBuilder.toString();
            Intrinsics.checkNotNullExpressionValue(object, "stringBuilder.append(this, i, length).toString()");
            return object;
        }
        object = new OutOfMemoryError();
        throw object;
    }

    public static /* synthetic */ String replace$default(String string2, char c, char c2, boolean bl, int n, Object object) {
        if ((n & 4) != 0) {
            bl = false;
        }
        return StringsKt.replace(string2, c, c2, bl);
    }

    public static /* synthetic */ String replace$default(String string2, String string3, String string4, boolean bl, int n, Object object) {
        if ((n & 4) != 0) {
            bl = false;
        }
        return StringsKt.replace(string2, string3, string4, bl);
    }

    public static final String replaceFirst(String string2, char c, char c2, boolean bl) {
        Intrinsics.checkNotNullParameter(string2, "$this$replaceFirst");
        int n = StringsKt.indexOf$default((CharSequence)string2, c, 0, bl, 2, null);
        if (n >= 0) {
            CharSequence charSequence = String.valueOf(c2);
            string2 = ((Object)StringsKt.replaceRange((CharSequence)string2, n, n + 1, charSequence)).toString();
        }
        return string2;
    }

    public static final String replaceFirst(String string2, String string3, String string4, boolean bl) {
        Intrinsics.checkNotNullParameter(string2, "$this$replaceFirst");
        Intrinsics.checkNotNullParameter(string3, "oldValue");
        Intrinsics.checkNotNullParameter(string4, "newValue");
        int n = StringsKt.indexOf$default((CharSequence)string2, string3, 0, bl, 2, null);
        if (n >= 0) {
            int n2 = string3.length();
            string2 = ((Object)StringsKt.replaceRange((CharSequence)string2, n, n2 + n, (CharSequence)string4)).toString();
        }
        return string2;
    }

    public static /* synthetic */ String replaceFirst$default(String string2, char c, char c2, boolean bl, int n, Object object) {
        if ((n & 4) != 0) {
            bl = false;
        }
        return StringsKt.replaceFirst(string2, c, c2, bl);
    }

    public static /* synthetic */ String replaceFirst$default(String string2, String string3, String string4, boolean bl, int n, Object object) {
        if ((n & 4) != 0) {
            bl = false;
        }
        return StringsKt.replaceFirst(string2, string3, string4, bl);
    }

    public static final List<String> split(CharSequence stringArray, Pattern pattern, int n) {
        Intrinsics.checkNotNullParameter(stringArray, "$this$split");
        Intrinsics.checkNotNullParameter(pattern, "regex");
        boolean bl = n >= 0;
        if (bl) {
            if (n == 0) {
                n = -1;
            }
            stringArray = pattern.split((CharSequence)stringArray, n);
            Intrinsics.checkNotNullExpressionValue(stringArray, "regex.split(this, if (limit == 0) -1 else limit)");
            return ArraysKt.asList(stringArray);
        }
        stringArray = new StringBuilder();
        stringArray.append("Limit must be non-negative, but was ");
        stringArray.append(n);
        stringArray.append('.');
        throw (Throwable)new IllegalArgumentException(stringArray.toString().toString());
    }

    public static /* synthetic */ List split$default(CharSequence charSequence, Pattern pattern, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 0;
        }
        return StringsKt.split(charSequence, pattern, n);
    }

    public static final boolean startsWith(String string2, String string3, int n, boolean bl) {
        Intrinsics.checkNotNullParameter(string2, "$this$startsWith");
        Intrinsics.checkNotNullParameter(string3, "prefix");
        if (!bl) {
            return string2.startsWith(string3, n);
        }
        return StringsKt.regionMatches(string2, n, string3, 0, string3.length(), bl);
    }

    public static final boolean startsWith(String string2, String string3, boolean bl) {
        Intrinsics.checkNotNullParameter(string2, "$this$startsWith");
        Intrinsics.checkNotNullParameter(string3, "prefix");
        if (!bl) {
            return string2.startsWith(string3);
        }
        return StringsKt.regionMatches(string2, 0, string3, 0, string3.length(), bl);
    }

    public static /* synthetic */ boolean startsWith$default(String string2, String string3, int n, boolean bl, int n2, Object object) {
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.startsWith(string2, string3, n, bl);
    }

    public static /* synthetic */ boolean startsWith$default(String string2, String string3, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.startsWith(string2, string3, bl);
    }

    private static final String substring(String string2, int n) {
        if (string2 != null) {
            string2 = string2.substring(n);
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.String).substring(startIndex)");
            return string2;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final String substring(String string2, int n, int n2) {
        if (string2 != null) {
            string2 = string2.substring(n, n2);
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            return string2;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final byte[] toByteArray(String object, Charset charset) {
        if (object != null) {
            object = ((String)object).getBytes(charset);
            Intrinsics.checkNotNullExpressionValue(object, "(this as java.lang.String).getBytes(charset)");
            return object;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    static /* synthetic */ byte[] toByteArray$default(String object, Charset charset, int n, Object object2) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        if (object != null) {
            object = ((String)object).getBytes(charset);
            Intrinsics.checkNotNullExpressionValue(object, "(this as java.lang.String).getBytes(charset)");
            return object;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final char[] toCharArray(String object) {
        if (object != null) {
            object = ((String)object).toCharArray();
            Intrinsics.checkNotNullExpressionValue(object, "(this as java.lang.String).toCharArray()");
            return object;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public static final char[] toCharArray(String string2, int n, int n2) {
        Intrinsics.checkNotNullParameter(string2, "$this$toCharArray");
        AbstractList.Companion.checkBoundsIndexes$kotlin_stdlib(n, n2, string2.length());
        char[] cArray = new char[n2 - n];
        string2.getChars(n, n2, cArray, 0);
        return cArray;
    }

    private static final char[] toCharArray(String string2, char[] cArray, int n, int n2, int n3) {
        if (string2 != null) {
            string2.getChars(n2, n3, cArray, n);
            return cArray;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public static /* synthetic */ char[] toCharArray$default(String string2, int n, int n2, int n3, Object object) {
        if ((n3 & 1) != 0) {
            n = 0;
        }
        if ((n3 & 2) != 0) {
            n2 = string2.length();
        }
        return StringsKt.toCharArray(string2, n, n2);
    }

    static /* synthetic */ char[] toCharArray$default(String string2, char[] cArray, int n, int n2, int n3, int n4, Object object) {
        if ((n4 & 2) != 0) {
            n = 0;
        }
        if ((n4 & 4) != 0) {
            n2 = 0;
        }
        if ((n4 & 8) != 0) {
            n3 = string2.length();
        }
        if (string2 != null) {
            string2.getChars(n2, n3, cArray, n);
            return cArray;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final String toLowerCase(String string2) {
        if (string2 != null) {
            string2 = string2.toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.String).toLowerCase()");
            return string2;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final String toLowerCase(String string2, Locale locale) {
        if (string2 != null) {
            string2 = string2.toLowerCase(locale);
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.String).toLowerCase(locale)");
            return string2;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final Pattern toPattern(String object, int n) {
        object = Pattern.compile((String)object, n);
        Intrinsics.checkNotNullExpressionValue(object, "java.util.regex.Pattern.compile(this, flags)");
        return object;
    }

    static /* synthetic */ Pattern toPattern$default(String object, int n, int n2, Object object2) {
        if ((n2 & 1) != 0) {
            n = 0;
        }
        object = Pattern.compile((String)object, n);
        Intrinsics.checkNotNullExpressionValue(object, "java.util.regex.Pattern.compile(this, flags)");
        return object;
    }

    private static final String toUpperCase(String string2) {
        if (string2 != null) {
            string2 = string2.toUpperCase();
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.String).toUpperCase()");
            return string2;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final String toUpperCase(String string2, Locale locale) {
        if (string2 != null) {
            string2 = string2.toUpperCase(locale);
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.String).toUpperCase(locale)");
            return string2;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final String uppercase(String string2) {
        if (string2 != null) {
            string2 = string2.toUpperCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026.toUpperCase(Locale.ROOT)");
            return string2;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    private static final String uppercase(String string2, Locale locale) {
        if (string2 != null) {
            string2 = string2.toUpperCase(locale);
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.String).toUpperCase(locale)");
            return string2;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }
}

