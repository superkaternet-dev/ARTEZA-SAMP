/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;
import kotlin.text.StringsKt__AppendableKt;
import kotlin.text.StringsKt__IndentKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u000b\u001a!\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0002\u00a2\u0006\u0002\b\u0004\u001a\u0011\u0010\u0005\u001a\u00020\u0006*\u00020\u0002H\u0002\u00a2\u0006\u0002\b\u0007\u001a\u0014\u0010\b\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0002\u001aJ\u0010\t\u001a\u00020\u0002*\b\u0012\u0004\u0012\u00020\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0014\u0010\r\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001H\u0082\b\u00a2\u0006\u0002\b\u000e\u001a\u0014\u0010\u000f\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u0002\u001a\u001e\u0010\u0011\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002\u001a\n\u0010\u0013\u001a\u00020\u0002*\u00020\u0002\u001a\u0014\u0010\u0014\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002\u00a8\u0006\u0015"}, d2={"getIndentFunction", "Lkotlin/Function1;", "", "indent", "getIndentFunction$StringsKt__IndentKt", "indentWidth", "", "indentWidth$StringsKt__IndentKt", "prependIndent", "reindent", "", "resultSizeEstimate", "indentAddFunction", "indentCutFunction", "reindent$StringsKt__IndentKt", "replaceIndent", "newIndent", "replaceIndentByMargin", "marginPrefix", "trimIndent", "trimMargin", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/text/StringsKt")
class StringsKt__IndentKt
extends StringsKt__AppendableKt {
    private static final Function1<String, String> getIndentFunction$StringsKt__IndentKt(String object) {
        boolean bl = ((CharSequence)object).length() == 0;
        object = bl ? (Function1)getIndentFunction.1.INSTANCE : (Function1)new Function1<String, String>((String)object){
            final String $indent;
            {
                this.$indent = string2;
                super(1);
            }

            public final String invoke(String string2) {
                Intrinsics.checkNotNullParameter(string2, "line");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.$indent);
                stringBuilder.append(string2);
                return stringBuilder.toString();
            }
        };
        return object;
    }

    private static final int indentWidth$StringsKt__IndentKt(String string2) {
        int n;
        int n2;
        block3: {
            CharSequence charSequence = string2;
            n2 = charSequence.length();
            for (n = 0; n < n2; ++n) {
                if (!(CharsKt.isWhitespace(charSequence.charAt(n)) ^ true)) {
                    continue;
                }
                break block3;
            }
            n = -1;
        }
        n2 = n;
        if (n == -1) {
            n2 = string2.length();
        }
        return n2;
    }

    public static final String prependIndent(String string2, String string3) {
        Intrinsics.checkNotNullParameter(string2, "$this$prependIndent");
        Intrinsics.checkNotNullParameter(string3, "indent");
        return SequencesKt.joinToString$default(SequencesKt.map(StringsKt.lineSequence(string2), (Function1)new Function1<String, String>(string3){
            final String $indent;
            {
                this.$indent = string2;
                super(1);
            }

            public final String invoke(String string2) {
                Intrinsics.checkNotNullParameter(string2, "it");
                if (StringsKt.isBlank(string2)) {
                    if (string2.length() < this.$indent.length()) {
                        string2 = this.$indent;
                    }
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(this.$indent);
                    stringBuilder.append(string2);
                    string2 = stringBuilder.toString();
                }
                return string2;
            }
        }), "\n", null, null, 0, null, null, 62, null);
    }

    public static /* synthetic */ String prependIndent$default(String string2, String string3, int n, Object object) {
        if ((n & 1) != 0) {
            string3 = "    ";
        }
        return StringsKt.prependIndent(string2, string3);
    }

    private static final String reindent$StringsKt__IndentKt(List<String> object, int n, Function1<? super String, String> function1, Function1<? super String, String> function12) {
        boolean bl = false;
        int n2 = CollectionsKt.getLastIndex(object);
        object = (Iterable)object;
        Collection collection = new ArrayList();
        int n3 = 0;
        Iterator<String> iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            object = iterator2.next();
            if (n3 < 0) {
                if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
                    CollectionsKt.throwIndexOverflow();
                } else {
                    throw (Throwable)new ArithmeticException("Index overflow has happened.");
                }
            }
            object = (String)object;
            if ((n3 == 0 || n3 == n2) && StringsKt.isBlank((CharSequence)object)) {
                object = null;
            } else {
                String string2 = function12.invoke((String)object);
                if (string2 != null && (string2 = function1.invoke(string2)) != null) {
                    object = string2;
                }
            }
            if (object != null) {
                collection.add(object);
            }
            ++n3;
        }
        object = (List)collection;
        object = ((StringBuilder)CollectionsKt.joinTo$default((Iterable)object, new StringBuilder(n), "\n", null, null, 0, null, null, 124, null)).toString();
        Intrinsics.checkNotNullExpressionValue(object, "mapIndexedNotNull { inde\u2026\"\\n\")\n        .toString()");
        return object;
    }

    public static final String replaceIndent(String object, String string2) {
        Intrinsics.checkNotNullParameter(object, "$this$replaceIndent");
        Intrinsics.checkNotNullParameter(string2, "newIndent");
        Collection<String> collection = StringsKt.lines((CharSequence)object);
        Iterator iterator2 = collection;
        Function1<String, String> function1 = new ArrayList();
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            Object e = iterator2.next();
            if (!(StringsKt.isBlank((String)e) ^ true)) continue;
            function1.add(e);
        }
        function1 = (List)((Object)function1);
        iterator2 = (Iterable)((Object)function1);
        function1 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterator2, 10));
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            function1.add(StringsKt__IndentKt.indentWidth$StringsKt__IndentKt((String)iterator2.next()));
        }
        function1 = (List)((Object)function1);
        int n = (function1 = (Integer)CollectionsKt.minOrNull((Iterable)((Object)function1))) != null ? (Integer)((Object)function1) : 0;
        int n2 = ((String)object).length();
        int n3 = string2.length();
        int n4 = collection.size();
        function1 = StringsKt__IndentKt.getIndentFunction$StringsKt__IndentKt(string2);
        int n5 = CollectionsKt.getLastIndex(collection);
        object = collection;
        collection = new ArrayList();
        int n6 = 0;
        iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            object = iterator2.next();
            if (n6 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            object = (String)object;
            if ((n6 == 0 || n6 == n5) && StringsKt.isBlank((CharSequence)object)) {
                object = null;
            } else {
                string2 = StringsKt.drop((String)object, n);
                if (string2 != null && (string2 = function1.invoke(string2)) != null) {
                    object = string2;
                }
            }
            if (object != null) {
                collection.add((String)object);
            }
            ++n6;
        }
        object = collection;
        object = ((StringBuilder)CollectionsKt.joinTo$default((Iterable)object, new StringBuilder(n2 + n3 * n4), "\n", null, null, 0, null, null, 124, null)).toString();
        Intrinsics.checkNotNullExpressionValue(object, "mapIndexedNotNull { inde\u2026\"\\n\")\n        .toString()");
        return object;
    }

    public static /* synthetic */ String replaceIndent$default(String string2, String string3, int n, Object object) {
        if ((n & 1) != 0) {
            string3 = "";
        }
        return StringsKt.replaceIndent(string2, string3);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final String replaceIndentByMargin(String object, String object2, String string2) {
        Intrinsics.checkNotNullParameter(object, "$this$replaceIndentByMargin");
        Intrinsics.checkNotNullParameter(object2, "newIndent");
        Intrinsics.checkNotNullParameter(string2, "marginPrefix");
        if (StringsKt.isBlank(string2) ^ true) {
            Object object3 = StringsKt.lines((CharSequence)object);
            int n = ((String)object).length();
            int n2 = ((String)object2).length();
            int n3 = object3.size();
            Function1<String, String> function1 = StringsKt__IndentKt.getIndentFunction$StringsKt__IndentKt((String)object2);
            int n4 = CollectionsKt.getLastIndex(object3);
            object = (Iterable)object3;
            object2 = new ArrayList();
            int n5 = 0;
            Iterator iterator2 = object.iterator();
            while (iterator2.hasNext()) {
                object = iterator2.next();
                if (n5 < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                object3 = (String)object;
                object = null;
                if (n5 != 0 && n5 != n4 || !StringsKt.isBlank((CharSequence)object3)) {
                    int n6;
                    int n7;
                    block12: {
                        object = (CharSequence)object3;
                        boolean bl = false;
                        n7 = object.length();
                        for (n6 = 0; n6 < n7; ++n6) {
                            if (!(CharsKt.isWhitespace(object.charAt(n6)) ^ true)) {
                                continue;
                            }
                            break block12;
                        }
                        n6 = -1;
                    }
                    if (n6 == -1) {
                        object = null;
                    } else if (StringsKt.startsWith$default((String)object3, string2, n6, false, 4, null)) {
                        n7 = string2.length();
                        if (object3 == null) throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        object = ((String)object3).substring(n7 + n6);
                        Intrinsics.checkNotNullExpressionValue(object, "(this as java.lang.String).substring(startIndex)");
                    } else {
                        object = null;
                    }
                    if (object == null || (object = function1.invoke((String)object)) == null) {
                        object = object3;
                    }
                }
                if (object != null) {
                    object2.add(object);
                }
                ++n5;
            }
            object = (List)object2;
            object = ((StringBuilder)CollectionsKt.joinTo$default((Iterable)object, new StringBuilder(n + n2 * n3), "\n", null, null, 0, null, null, 124, null)).toString();
            Intrinsics.checkNotNullExpressionValue(object, "mapIndexedNotNull { inde\u2026\"\\n\")\n        .toString()");
            return object;
        }
        object = new IllegalArgumentException("marginPrefix must be non-blank string.".toString());
        throw object;
    }

    public static /* synthetic */ String replaceIndentByMargin$default(String string2, String string3, String string4, int n, Object object) {
        if ((n & 1) != 0) {
            string3 = "";
        }
        if ((n & 2) != 0) {
            string4 = "|";
        }
        return StringsKt.replaceIndentByMargin(string2, string3, string4);
    }

    public static final String trimIndent(String string2) {
        Intrinsics.checkNotNullParameter(string2, "$this$trimIndent");
        return StringsKt.replaceIndent(string2, "");
    }

    public static final String trimMargin(String string2, String string3) {
        Intrinsics.checkNotNullParameter(string2, "$this$trimMargin");
        Intrinsics.checkNotNullParameter(string3, "marginPrefix");
        return StringsKt.replaceIndentByMargin(string2, "", string3);
    }

    public static /* synthetic */ String trimMargin$default(String string2, String string3, int n, Object object) {
        if ((n & 1) != 0) {
            string3 = "|";
        }
        return StringsKt.trimMargin(string2, string3);
    }
}

