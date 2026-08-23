/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilePathComponents;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000$\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0011\u0010\u000b\u001a\u00020\f*\u00020\bH\u0002\u00a2\u0006\u0002\b\r\u001a\u001c\u0010\u000e\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\fH\u0000\u001a\f\u0010\u0011\u001a\u00020\u0012*\u00020\u0002H\u0000\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0000\u0010\u0003\"\u0018\u0010\u0004\u001a\u00020\u0002*\u00020\u00028@X\u0080\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\"\u0018\u0010\u0007\u001a\u00020\b*\u00020\u00028@X\u0080\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0013"}, d2={"isRooted", "", "Ljava/io/File;", "(Ljava/io/File;)Z", "root", "getRoot", "(Ljava/io/File;)Ljava/io/File;", "rootName", "", "getRootName", "(Ljava/io/File;)Ljava/lang/String;", "getRootLength", "", "getRootLength$FilesKt__FilePathComponentsKt", "subPath", "beginIndex", "endIndex", "toComponents", "Lkotlin/io/FilePathComponents;", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/io/FilesKt")
class FilesKt__FilePathComponentsKt {
    public static final File getRoot(File file) {
        Intrinsics.checkNotNullParameter(file, "$this$root");
        return new File(FilesKt.getRootName(file));
    }

    private static final int getRootLength$FilesKt__FilePathComponentsKt(String string2) {
        int n = StringsKt.indexOf$default((CharSequence)string2, File.separatorChar, 0, false, 4, null);
        if (n == 0) {
            if (string2.length() > 1 && string2.charAt(1) == File.separatorChar && (n = StringsKt.indexOf$default((CharSequence)string2, File.separatorChar, 2, false, 4, null)) >= 0) {
                if ((n = StringsKt.indexOf$default((CharSequence)string2, File.separatorChar, n + 1, false, 4, null)) >= 0) {
                    return n + 1;
                }
                return string2.length();
            }
            return 1;
        }
        if (n > 0 && string2.charAt(n - 1) == ':') {
            return n + 1;
        }
        if (n == -1 && StringsKt.endsWith$default((CharSequence)string2, ':', false, 2, null)) {
            return string2.length();
        }
        return 0;
    }

    public static final String getRootName(File object) {
        Intrinsics.checkNotNullParameter(object, "$this$rootName");
        String string2 = ((File)object).getPath();
        Intrinsics.checkNotNullExpressionValue(string2, "path");
        object = ((File)object).getPath();
        Intrinsics.checkNotNullExpressionValue(object, "path");
        int n = FilesKt__FilePathComponentsKt.getRootLength$FilesKt__FilePathComponentsKt((String)object);
        if (string2 != null) {
            object = string2.substring(0, n);
            Intrinsics.checkNotNullExpressionValue(object, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            return object;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public static final boolean isRooted(File object) {
        Intrinsics.checkNotNullParameter(object, "$this$isRooted");
        object = ((File)object).getPath();
        Intrinsics.checkNotNullExpressionValue(object, "path");
        boolean bl = FilesKt__FilePathComponentsKt.getRootLength$FilesKt__FilePathComponentsKt((String)object) > 0;
        return bl;
    }

    public static final File subPath(File file, int n, int n2) {
        Intrinsics.checkNotNullParameter(file, "$this$subPath");
        return FilesKt.toComponents(file).subPath(n, n2);
    }

    public static final FilePathComponents toComponents(File collection) {
        Intrinsics.checkNotNullParameter(collection, "$this$toComponents");
        collection = ((File)((Object)collection)).getPath();
        Intrinsics.checkNotNullExpressionValue(collection, "path");
        int n = FilesKt__FilePathComponentsKt.getRootLength$FilesKt__FilePathComponentsKt((String)((Object)collection));
        String string2 = ((String)((Object)collection)).substring(0, n);
        Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        collection = ((String)((Object)collection)).substring(n);
        Intrinsics.checkNotNullExpressionValue(collection, "(this as java.lang.String).substring(startIndex)");
        n = ((CharSequence)((Object)collection)).length() == 0 ? 1 : 0;
        if (n != 0) {
            collection = CollectionsKt.emptyList();
        } else {
            Object object = StringsKt.split$default((CharSequence)((Object)collection), new char[]{File.separatorChar}, false, 0, 6, null);
            collection = new ArrayList(CollectionsKt.collectionSizeOrDefault(object, 10));
            object = object.iterator();
            while (object.hasNext()) {
                collection.add(new File((String)object.next()));
            }
        }
        return new FilePathComponents(new File(string2), (List<? extends File>)collection);
    }
}

