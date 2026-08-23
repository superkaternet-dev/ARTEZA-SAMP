/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.io.FileAlreadyExistsException;
import kotlin.io.FilePathComponents;
import kotlin.io.FileSystemException;
import kotlin.io.FileTreeWalk;
import kotlin.io.FilesKt;
import kotlin.io.FilesKt__FileTreeWalkKt;
import kotlin.io.FilesKt__UtilsKt;
import kotlin.io.NoSuchFileException;
import kotlin.io.OnErrorAction;
import kotlin.io.TerminateException;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000<\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u001a*\u0010\t\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0007\u001a*\u0010\r\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0007\u001a8\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\u001a\b\u0002\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u0013\u001a&\u0010\u0016\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u001a\n\u0010\u0019\u001a\u00020\u000f*\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\n\u0010\u001c\u001a\u00020\u0002*\u00020\u0002\u001a\u001d\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00020\u001d*\b\u0012\u0004\u0012\u00020\u00020\u001dH\u0002\u00a2\u0006\u0002\b\u001e\u001a\u0011\u0010\u001c\u001a\u00020\u001f*\u00020\u001fH\u0002\u00a2\u0006\u0002\b\u001e\u001a\u0012\u0010 \u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0014\u0010\"\u001a\u0004\u0018\u00010\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010#\u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\u0012\u0010(\u001a\u00020\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u001b\u0010)\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002H\u0002\u00a2\u0006\u0002\b*\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0004\"\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\u0004\u00a8\u0006+"}, d2={"extension", "", "Ljava/io/File;", "getExtension", "(Ljava/io/File;)Ljava/lang/String;", "invariantSeparatorsPath", "getInvariantSeparatorsPath", "nameWithoutExtension", "getNameWithoutExtension", "createTempDir", "prefix", "suffix", "directory", "createTempFile", "copyRecursively", "", "target", "overwrite", "onError", "Lkotlin/Function2;", "Ljava/io/IOException;", "Lkotlin/io/OnErrorAction;", "copyTo", "bufferSize", "", "deleteRecursively", "endsWith", "other", "normalize", "", "normalize$FilesKt__UtilsKt", "Lkotlin/io/FilePathComponents;", "relativeTo", "base", "relativeToOrNull", "relativeToOrSelf", "resolve", "relative", "resolveSibling", "startsWith", "toRelativeString", "toRelativeStringOrNull", "toRelativeStringOrNull$FilesKt__UtilsKt", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/io/FilesKt")
class FilesKt__UtilsKt
extends FilesKt__FileTreeWalkKt {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final boolean copyRecursively(File file, File file2, boolean bl, Function2<? super File, ? super IOException, ? extends OnErrorAction> function2) {
        Intrinsics.checkNotNullParameter(file, "$this$copyRecursively");
        Intrinsics.checkNotNullParameter(file2, "target");
        Intrinsics.checkNotNullParameter(function2, "onError");
        boolean bl2 = file.exists();
        boolean bl3 = true;
        if (!bl2) {
            if (function2.invoke(file, new NoSuchFileException(file, null, "The source file doesn't exist.", 2, null)) == OnErrorAction.TERMINATE) return false;
            return bl3;
        }
        try {
            FileTreeWalk fileTreeWalk = FilesKt.walkTopDown(file);
            Function2<File, IOException, Unit> function22 = new Function2<File, IOException, Unit>(function2){
                final Function2 $onError;
                {
                    this.$onError = function2;
                    super(2);
                }

                public final void invoke(File file, IOException iOException) {
                    Intrinsics.checkNotNullParameter(file, "f");
                    Intrinsics.checkNotNullParameter(iOException, "e");
                    if ((OnErrorAction)((Object)this.$onError.invoke(file, iOException)) != OnErrorAction.TERMINATE) {
                        return;
                    }
                    throw (Throwable)new TerminateException(file);
                }
            };
            function22 = fileTreeWalk.onFail((Function2<? super File, ? super IOException, Unit>)function22).iterator();
            while (function22.hasNext()) {
                OnErrorAction onErrorAction;
                IOException iOException;
                OnErrorAction onErrorAction2;
                File file3 = (File)function22.next();
                if (!file3.exists()) {
                    NoSuchFileException noSuchFileException = new NoSuchFileException(file3, null, "The source file doesn't exist.", 2, null);
                    if (function2.invoke(file3, noSuchFileException) != OnErrorAction.TERMINATE) continue;
                    return false;
                }
                Object object = FilesKt.toRelativeString(file3, file);
                File file4 = new File(file2, (String)object);
                if (!(!file4.exists() || file3.isDirectory() && file4.isDirectory())) {
                    boolean bl4 = !bl ? true : (file4.isDirectory() ? !FilesKt.deleteRecursively(file4) : !file4.delete());
                    if (bl4) {
                        object = new FileAlreadyExistsException(file3, file4, "The destination file already exists.");
                        if (function2.invoke(file4, (IOException)object) != OnErrorAction.TERMINATE) continue;
                        return false;
                    }
                }
                if (file3.isDirectory()) {
                    file4.mkdirs();
                    continue;
                }
                if (FilesKt.copyTo$default(file3, file4, bl, 0, 4, null).length() != file3.length() && (onErrorAction2 = function2.invoke(file3, iOException = new IOException("Source file wasn't copied completely, length of destination file differs."))) == (onErrorAction = OnErrorAction.TERMINATE)) return false;
            }
            return true;
        }
        catch (TerminateException terminateException) {
            return false;
        }
    }

    public static /* synthetic */ boolean copyRecursively$default(File file, File file2, boolean bl, Function2 function2, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        if ((n & 4) != 0) {
            function2 = copyRecursively.1.INSTANCE;
        }
        return FilesKt.copyRecursively(file, file2, bl, function2);
    }

    /*
     * Loose catch block
     */
    public static final File copyTo(File object, File file, boolean bl, int n) {
        Intrinsics.checkNotNullParameter(object, "$this$copyTo");
        Intrinsics.checkNotNullParameter(file, "target");
        if (((File)object).exists()) {
            Object object2;
            if (file.exists()) {
                if (bl) {
                    if (!file.delete()) {
                        throw (Throwable)new FileAlreadyExistsException((File)object, file, "Tried to overwrite the destination, but failed to delete it.");
                    }
                } else {
                    throw (Throwable)new FileAlreadyExistsException((File)object, file, "The destination file already exists.");
                }
            }
            if (((File)object).isDirectory()) {
                if (!file.mkdirs()) {
                    throw (Throwable)new FileSystemException((File)object, file, "Failed to create target directory.");
                }
            } else {
                object2 = file.getParentFile();
                if (object2 != null) {
                    ((File)object2).mkdirs();
                }
                object = new FileInputStream((File)object);
                Throwable throwable = null;
                FileInputStream fileInputStream = (FileInputStream)object;
                object2 = new FileOutputStream(file);
                object2 = (Closeable)object2;
                Throwable throwable2 = null;
                FileOutputStream fileOutputStream = (FileOutputStream)object2;
                ByteStreamsKt.copyTo(fileInputStream, fileOutputStream, n);
                CloseableKt.closeFinally((Closeable)object2, throwable2);
                CloseableKt.closeFinally((Closeable)object, throwable);
            }
            return file;
            catch (Throwable throwable) {
                try {
                    throw throwable;
                }
                catch (Throwable throwable3) {
                    try {
                        CloseableKt.closeFinally((Closeable)object2, throwable);
                        throw throwable3;
                    }
                    catch (Throwable throwable4) {
                        try {
                            throw throwable4;
                        }
                        catch (Throwable throwable5) {
                            CloseableKt.closeFinally((Closeable)object, throwable4);
                            throw throwable5;
                        }
                    }
                }
            }
        }
        throw (Throwable)new NoSuchFileException((File)object, null, "The source file doesn't exist.", 2, null);
    }

    public static /* synthetic */ File copyTo$default(File file, File file2, boolean bl, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        if ((n2 & 4) != 0) {
            n = 8192;
        }
        return FilesKt.copyTo(file, file2, bl, n);
    }

    @Deprecated(message="Avoid creating temporary directories in the default temp location with this function due to too wide permissions on the newly created directory. Use kotlin.io.path.createTempDirectory instead.")
    public static final File createTempDir(String charSequence, String object, File file) {
        Intrinsics.checkNotNullParameter(charSequence, "prefix");
        object = File.createTempFile((String)charSequence, (String)object, file);
        ((File)object).delete();
        if (((File)object).mkdir()) {
            Intrinsics.checkNotNullExpressionValue(object, "dir");
            return object;
        }
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append("Unable to create temporary directory ");
        ((StringBuilder)charSequence).append(object);
        ((StringBuilder)charSequence).append('.');
        throw (Throwable)new IOException(((StringBuilder)charSequence).toString());
    }

    public static /* synthetic */ File createTempDir$default(String string2, String string3, File file, int n, Object object) {
        if ((n & 1) != 0) {
            string2 = "tmp";
        }
        if ((n & 2) != 0) {
            string3 = null;
        }
        if ((n & 4) != 0) {
            file = null;
        }
        return FilesKt.createTempDir(string2, string3, file);
    }

    @Deprecated(message="Avoid creating temporary files in the default temp location with this function due to too wide permissions on the newly created file. Use kotlin.io.path.createTempFile instead or resort to java.io.File.createTempFile.")
    public static final File createTempFile(String object, String string2, File file) {
        Intrinsics.checkNotNullParameter(object, "prefix");
        object = File.createTempFile((String)object, string2, file);
        Intrinsics.checkNotNullExpressionValue(object, "File.createTempFile(prefix, suffix, directory)");
        return object;
    }

    public static /* synthetic */ File createTempFile$default(String string2, String string3, File file, int n, Object object) {
        if ((n & 1) != 0) {
            string2 = "tmp";
        }
        if ((n & 2) != 0) {
            string3 = null;
        }
        if ((n & 4) != 0) {
            file = null;
        }
        return FilesKt.createTempFile(string2, string3, file);
    }

    public static final boolean deleteRecursively(File object) {
        Intrinsics.checkNotNullParameter(object, "$this$deleteRecursively");
        object = FilesKt.walkBottomUp((File)object);
        boolean bl = true;
        Iterator iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            object = (File)iterator2.next();
            if ((((File)object).delete() || !((File)object).exists()) && bl) {
                bl = true;
                continue;
            }
            bl = false;
        }
        return bl;
    }

    public static final boolean endsWith(File file, File file2) {
        Intrinsics.checkNotNullParameter(file, "$this$endsWith");
        Intrinsics.checkNotNullParameter(file2, "other");
        FilePathComponents filePathComponents = FilesKt.toComponents(file);
        FilePathComponents filePathComponents2 = FilesKt.toComponents(file2);
        if (filePathComponents2.isRooted()) {
            return Intrinsics.areEqual(file, file2);
        }
        int n = filePathComponents.getSize() - filePathComponents2.getSize();
        boolean bl = n < 0 ? false : ((Object)filePathComponents.getSegments().subList(n, filePathComponents.getSize())).equals(filePathComponents2.getSegments());
        return bl;
    }

    public static final boolean endsWith(File file, String string2) {
        Intrinsics.checkNotNullParameter(file, "$this$endsWith");
        Intrinsics.checkNotNullParameter(string2, "other");
        return FilesKt.endsWith(file, new File(string2));
    }

    public static final String getExtension(File object) {
        Intrinsics.checkNotNullParameter(object, "$this$extension");
        object = ((File)object).getName();
        Intrinsics.checkNotNullExpressionValue(object, "name");
        return StringsKt.substringAfterLast((String)object, '.', "");
    }

    public static final String getInvariantSeparatorsPath(File object) {
        Intrinsics.checkNotNullParameter(object, "$this$invariantSeparatorsPath");
        if (File.separatorChar != '/') {
            object = ((File)object).getPath();
            Intrinsics.checkNotNullExpressionValue(object, "path");
            object = StringsKt.replace$default((String)object, File.separatorChar, '/', false, 4, null);
        } else {
            object = ((File)object).getPath();
            Intrinsics.checkNotNullExpressionValue(object, "path");
        }
        return object;
    }

    public static final String getNameWithoutExtension(File object) {
        Intrinsics.checkNotNullParameter(object, "$this$nameWithoutExtension");
        object = ((File)object).getName();
        Intrinsics.checkNotNullExpressionValue(object, "name");
        return StringsKt.substringBeforeLast$default((String)object, ".", null, 2, null);
    }

    public static final File normalize(File file) {
        Intrinsics.checkNotNullParameter(file, "$this$normalize");
        Object object = FilesKt.toComponents(file);
        file = ((FilePathComponents)object).getRoot();
        object = FilesKt__UtilsKt.normalize$FilesKt__UtilsKt(((FilePathComponents)object).getSegments());
        String string2 = File.separator;
        Intrinsics.checkNotNullExpressionValue(string2, "File.separator");
        return FilesKt.resolve(file, CollectionsKt.joinToString$default((Iterable)object, string2, null, null, 0, null, null, 62, null));
    }

    private static final List<File> normalize$FilesKt__UtilsKt(List<? extends File> object) {
        List list = new ArrayList(object.size());
        object = object.iterator();
        block4: while (object.hasNext()) {
            File file = (File)object.next();
            String string2 = file.getName();
            if (string2 != null) {
                switch (string2.hashCode()) {
                    default: {
                        break;
                    }
                    case 1472: {
                        if (!string2.equals("..")) break;
                        if (!list.isEmpty() && Intrinsics.areEqual(((File)CollectionsKt.last(list)).getName(), "..") ^ true) {
                            list.remove(list.size() - 1);
                            continue block4;
                        }
                        list.add(file);
                        continue block4;
                    }
                    case 46: {
                        if (string2.equals(".")) continue block4;
                    }
                }
            }
            list.add(file);
        }
        return list;
    }

    private static final FilePathComponents normalize$FilesKt__UtilsKt(FilePathComponents filePathComponents) {
        return new FilePathComponents(filePathComponents.getRoot(), FilesKt__UtilsKt.normalize$FilesKt__UtilsKt(filePathComponents.getSegments()));
    }

    public static final File relativeTo(File file, File file2) {
        Intrinsics.checkNotNullParameter(file, "$this$relativeTo");
        Intrinsics.checkNotNullParameter(file2, "base");
        return new File(FilesKt.toRelativeString(file, file2));
    }

    public static final File relativeToOrNull(File object, File file) {
        Intrinsics.checkNotNullParameter(object, "$this$relativeToOrNull");
        Intrinsics.checkNotNullParameter(file, "base");
        object = FilesKt__UtilsKt.toRelativeStringOrNull$FilesKt__UtilsKt((File)object, file);
        object = object != null ? new File((String)object) : null;
        return object;
    }

    public static final File relativeToOrSelf(File file, File object) {
        block0: {
            Intrinsics.checkNotNullParameter(file, "$this$relativeToOrSelf");
            Intrinsics.checkNotNullParameter(object, "base");
            object = FilesKt__UtilsKt.toRelativeStringOrNull$FilesKt__UtilsKt(file, (File)object);
            if (object == null) break block0;
            file = new File((String)object);
        }
        return file;
    }

    public static final File resolve(File object, File file) {
        Intrinsics.checkNotNullParameter(object, "$this$resolve");
        Intrinsics.checkNotNullParameter(file, "relative");
        if (FilesKt.isRooted(file)) {
            return file;
        }
        object = ((File)object).toString();
        Intrinsics.checkNotNullExpressionValue(object, "this.toString()");
        boolean bl = ((CharSequence)object).length() == 0;
        if (!bl && !StringsKt.endsWith$default((CharSequence)object, File.separatorChar, false, 2, null)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)object);
            stringBuilder.append(File.separatorChar);
            stringBuilder.append(file);
            object = new File(stringBuilder.toString());
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)object);
            stringBuilder.append(file);
            object = new File(stringBuilder.toString());
        }
        return object;
    }

    public static final File resolve(File file, String string2) {
        Intrinsics.checkNotNullParameter(file, "$this$resolve");
        Intrinsics.checkNotNullParameter(string2, "relative");
        return FilesKt.resolve(file, new File(string2));
    }

    public static final File resolveSibling(File file, File file2) {
        Intrinsics.checkNotNullParameter(file, "$this$resolveSibling");
        Intrinsics.checkNotNullParameter(file2, "relative");
        FilePathComponents filePathComponents = FilesKt.toComponents(file);
        file = filePathComponents.getSize() == 0 ? new File("..") : filePathComponents.subPath(0, filePathComponents.getSize() - 1);
        return FilesKt.resolve(FilesKt.resolve(filePathComponents.getRoot(), file), file2);
    }

    public static final File resolveSibling(File file, String string2) {
        Intrinsics.checkNotNullParameter(file, "$this$resolveSibling");
        Intrinsics.checkNotNullParameter(string2, "relative");
        return FilesKt.resolveSibling(file, new File(string2));
    }

    public static final boolean startsWith(File object, File object2) {
        Intrinsics.checkNotNullParameter(object, "$this$startsWith");
        Intrinsics.checkNotNullParameter(object2, "other");
        object = FilesKt.toComponents((File)object);
        object2 = FilesKt.toComponents((File)object2);
        boolean bl = Intrinsics.areEqual(((FilePathComponents)object).getRoot(), ((FilePathComponents)object2).getRoot());
        boolean bl2 = false;
        if (bl ^ true) {
            return false;
        }
        if (((FilePathComponents)object).getSize() >= ((FilePathComponents)object2).getSize()) {
            bl2 = ((Object)((FilePathComponents)object).getSegments().subList(0, ((FilePathComponents)object2).getSize())).equals(((FilePathComponents)object2).getSegments());
        }
        return bl2;
    }

    public static final boolean startsWith(File file, String string2) {
        Intrinsics.checkNotNullParameter(file, "$this$startsWith");
        Intrinsics.checkNotNullParameter(string2, "other");
        return FilesKt.startsWith(file, new File(string2));
    }

    public static final String toRelativeString(File file, File file2) {
        Intrinsics.checkNotNullParameter(file, "$this$toRelativeString");
        Intrinsics.checkNotNullParameter(file2, "base");
        CharSequence charSequence = FilesKt__UtilsKt.toRelativeStringOrNull$FilesKt__UtilsKt(file, file2);
        if (charSequence != null) {
            return charSequence;
        }
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append("this and base files have different roots: ");
        ((StringBuilder)charSequence).append(file);
        ((StringBuilder)charSequence).append(" and ");
        ((StringBuilder)charSequence).append(file2);
        ((StringBuilder)charSequence).append('.');
        throw (Throwable)new IllegalArgumentException(((StringBuilder)charSequence).toString());
    }

    private static final String toRelativeStringOrNull$FilesKt__UtilsKt(File comparable, File object) {
        int n;
        Object object2 = FilesKt__UtilsKt.normalize$FilesKt__UtilsKt(FilesKt.toComponents((File)comparable));
        object = FilesKt__UtilsKt.normalize$FilesKt__UtilsKt(FilesKt.toComponents((File)object));
        if (Intrinsics.areEqual(((FilePathComponents)object2).getRoot(), ((FilePathComponents)object).getRoot()) ^ true) {
            return null;
        }
        int n2 = ((FilePathComponents)object).getSize();
        int n3 = ((FilePathComponents)object2).getSize();
        int n4 = Math.min(n3, n2);
        for (n = 0; n < n4 && Intrinsics.areEqual(((FilePathComponents)object2).getSegments().get(n), ((FilePathComponents)object).getSegments().get(n)); ++n) {
        }
        comparable = new StringBuilder();
        n4 = n2 - 1;
        if (n4 >= n) {
            while (true) {
                if (Intrinsics.areEqual(((FilePathComponents)object).getSegments().get(n4).getName(), "..")) {
                    return null;
                }
                ((StringBuilder)comparable).append("..");
                if (n4 != n) {
                    ((StringBuilder)comparable).append(File.separatorChar);
                }
                if (n4 == n) break;
                --n4;
            }
        }
        if (n < n3) {
            if (n < n2) {
                ((StringBuilder)comparable).append(File.separatorChar);
            }
            object = CollectionsKt.drop((Iterable)((FilePathComponents)object2).getSegments(), n);
            Appendable appendable = (Appendable)((Object)comparable);
            object2 = File.separator;
            Intrinsics.checkNotNullExpressionValue(object2, "File.separator");
            CollectionsKt.joinTo$default((Iterable)object, appendable, (CharSequence)object2, null, null, 0, null, null, 124, null);
        }
        return ((StringBuilder)comparable).toString();
    }
}

