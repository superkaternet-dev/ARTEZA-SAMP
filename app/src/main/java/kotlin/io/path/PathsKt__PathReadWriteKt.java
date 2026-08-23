/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io.path;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.io.CloseableKt;
import kotlin.io.TextStreamsKt;
import kotlin.io.path.PathsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.Charsets;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u0082\u0001\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a%\u0010\u0005\u001a\u00020\u0002*\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a%\u0010\u0005\u001a\u00020\u0002*\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u000b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u001e\u0010\f\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001a:\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u0010\u0015\u001a:\u0010\u0016\u001a\u00020\u0017*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u0010\u0018\u001a=\u0010\u0019\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2!\u0010\u001a\u001a\u001d\u0012\u0013\u0012\u00110\u001c\u00a2\u0006\f\b\u001d\u0012\b\b\u001e\u0012\u0004\b\b(\u001f\u0012\u0004\u0012\u00020\u00010\u001bH\u0087\b\u00f8\u0001\u0000\u001a&\u0010 \u001a\u00020!*\u00020\u00022\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u0010\"\u001a&\u0010#\u001a\u00020$*\u00020\u00022\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u0010%\u001a\r\u0010&\u001a\u00020\u0004*\u00020\u0002H\u0087\b\u001a\u001d\u0010'\u001a\b\u0012\u0004\u0012\u00020\u001c0(*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0016\u0010)\u001a\u00020\u001c*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001a0\u0010*\u001a\u00020+*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u0010,\u001a?\u0010-\u001a\u0002H.\"\u0004\b\u0000\u0010.*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\u0018\u0010/\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0\u000b\u0012\u0004\u0012\u0002H.0\u001bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u00100\u001a.\u00101\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u00102\u001a>\u00103\u001a\u00020\u0002*\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u00104\u001a>\u00103\u001a\u00020\u0002*\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u000b2\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u00105\u001a7\u00106\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0007\u00a2\u0006\u0002\u00107\u001a0\u00108\u001a\u000209*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u0010:\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006;"}, d2={"appendBytes", "", "Ljava/nio/file/Path;", "array", "", "appendLines", "lines", "", "", "charset", "Ljava/nio/charset/Charset;", "Lkotlin/sequences/Sequence;", "appendText", "text", "bufferedReader", "Ljava/io/BufferedReader;", "bufferSize", "", "options", "", "Ljava/nio/file/OpenOption;", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;I[Ljava/nio/file/OpenOption;)Ljava/io/BufferedReader;", "bufferedWriter", "Ljava/io/BufferedWriter;", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;I[Ljava/nio/file/OpenOption;)Ljava/io/BufferedWriter;", "forEachLine", "action", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "line", "inputStream", "Ljava/io/InputStream;", "(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Ljava/io/InputStream;", "outputStream", "Ljava/io/OutputStream;", "(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Ljava/io/OutputStream;", "readBytes", "readLines", "", "readText", "reader", "Ljava/io/InputStreamReader;", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)Ljava/io/InputStreamReader;", "useLines", "T", "block", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "writeBytes", "(Ljava/nio/file/Path;[B[Ljava/nio/file/OpenOption;)V", "writeLines", "(Ljava/nio/file/Path;Ljava/lang/Iterable;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)Ljava/nio/file/Path;", "(Ljava/nio/file/Path;Lkotlin/sequences/Sequence;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)Ljava/nio/file/Path;", "writeText", "(Ljava/nio/file/Path;Ljava/lang/CharSequence;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)V", "writer", "Ljava/io/OutputStreamWriter;", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)Ljava/io/OutputStreamWriter;", "kotlin-stdlib-jdk7"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/io/path/PathsKt")
class PathsKt__PathReadWriteKt {
    private static final void appendBytes(Path path, byte[] byArray) {
        Files.write(path, byArray, StandardOpenOption.APPEND);
    }

    private static final Path appendLines(Path path, Iterable<? extends CharSequence> iterable, Charset charset) {
        path = Files.write(path, iterable, charset, StandardOpenOption.APPEND);
        Intrinsics.checkNotNullExpressionValue(path, "Files.write(this, lines,\u2026tandardOpenOption.APPEND)");
        return path;
    }

    private static final Path appendLines(Path path, Sequence<? extends CharSequence> sequence, Charset charset) {
        path = Files.write(path, SequencesKt.asIterable(sequence), charset, StandardOpenOption.APPEND);
        Intrinsics.checkNotNullExpressionValue(path, "Files.write(this, lines.\u2026tandardOpenOption.APPEND)");
        return path;
    }

    static /* synthetic */ Path appendLines$default(Path path, Iterable iterable, Charset charset, int n, Object object) {
        if ((n & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        path = Files.write(path, (Iterable<? extends CharSequence>)iterable, charset, StandardOpenOption.APPEND);
        Intrinsics.checkNotNullExpressionValue(path, "Files.write(this, lines,\u2026tandardOpenOption.APPEND)");
        return path;
    }

    static /* synthetic */ Path appendLines$default(Path path, Sequence sequence, Charset charset, int n, Object object) {
        if ((n & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        path = Files.write(path, SequencesKt.asIterable(sequence), charset, StandardOpenOption.APPEND);
        Intrinsics.checkNotNullExpressionValue(path, "Files.write(this, lines.\u2026tandardOpenOption.APPEND)");
        return path;
    }

    public static final void appendText(Path object, CharSequence charSequence, Charset object2) {
        Intrinsics.checkNotNullParameter(object, "$this$appendText");
        Intrinsics.checkNotNullParameter(charSequence, "text");
        Intrinsics.checkNotNullParameter(object2, "charset");
        object = Files.newOutputStream((Path)object, StandardOpenOption.APPEND);
        Intrinsics.checkNotNullExpressionValue(object, "Files.newOutputStream(th\u2026tandardOpenOption.APPEND)");
        object = new OutputStreamWriter((OutputStream)object, (Charset)object2);
        object2 = null;
        try {
            ((OutputStreamWriter)object).append(charSequence);
        }
        catch (Throwable throwable) {
            try {
                throw throwable;
            }
            catch (Throwable throwable2) {
                CloseableKt.closeFinally((Closeable)object, throwable);
                throw throwable2;
            }
        }
        CloseableKt.closeFinally((Closeable)object, (Throwable)object2);
    }

    public static /* synthetic */ void appendText$default(Path path, CharSequence charSequence, Charset charset, int n, Object object) {
        if ((n & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        PathsKt.appendText(path, charSequence, charset);
    }

    private static final BufferedReader bufferedReader(Path object, Charset charset, int n, OpenOption ... openOptionArray) {
        object = Files.newInputStream((Path)object, Arrays.copyOf(openOptionArray, openOptionArray.length));
        object = new InputStreamReader((InputStream)object, charset);
        return new BufferedReader((Reader)object, n);
    }

    static /* synthetic */ BufferedReader bufferedReader$default(Path object, Charset charset, int n, OpenOption[] openOptionArray, int n2, Object object2) {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        if ((n2 & 2) != 0) {
            n = 8192;
        }
        object = Files.newInputStream((Path)object, Arrays.copyOf(openOptionArray, openOptionArray.length));
        object = new InputStreamReader((InputStream)object, charset);
        return new BufferedReader((Reader)object, n);
    }

    private static final BufferedWriter bufferedWriter(Path object, Charset charset, int n, OpenOption ... openOptionArray) {
        object = Files.newOutputStream((Path)object, Arrays.copyOf(openOptionArray, openOptionArray.length));
        object = new OutputStreamWriter((OutputStream)object, charset);
        return new BufferedWriter((Writer)object, n);
    }

    static /* synthetic */ BufferedWriter bufferedWriter$default(Path object, Charset charset, int n, OpenOption[] openOptionArray, int n2, Object object2) {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        if ((n2 & 2) != 0) {
            n = 8192;
        }
        object = Files.newOutputStream((Path)object, Arrays.copyOf(openOptionArray, openOptionArray.length));
        object = new OutputStreamWriter((OutputStream)object, charset);
        return new BufferedWriter((Writer)object, n);
    }

    /*
     * Exception decompiling
     */
    private static final void forEachLine(Path var0, Charset var1_5, Function1<? super String, Unit> var2_6) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 8[WHILELOOP]
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

    /*
     * Exception decompiling
     */
    static /* synthetic */ void forEachLine$default(Path var0, Charset var1_5, Function1 var2_6, int var3_8, Object var4_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 8[WHILELOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1050)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private static final InputStream inputStream(Path object, OpenOption ... openOptionArray) {
        object = Files.newInputStream((Path)object, Arrays.copyOf(openOptionArray, openOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(object, "Files.newInputStream(this, *options)");
        return object;
    }

    private static final OutputStream outputStream(Path object, OpenOption ... openOptionArray) {
        object = Files.newOutputStream((Path)object, Arrays.copyOf(openOptionArray, openOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(object, "Files.newOutputStream(this, *options)");
        return object;
    }

    private static final byte[] readBytes(Path object) {
        object = Files.readAllBytes((Path)object);
        Intrinsics.checkNotNullExpressionValue(object, "Files.readAllBytes(this)");
        return object;
    }

    private static final List<String> readLines(Path iterable, Charset charset) {
        iterable = Files.readAllLines(iterable, charset);
        Intrinsics.checkNotNullExpressionValue(iterable, "Files.readAllLines(this, charset)");
        return iterable;
    }

    static /* synthetic */ List readLines$default(Path iterable, Charset charset, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        iterable = Files.readAllLines(iterable, charset);
        Intrinsics.checkNotNullExpressionValue(iterable, "Files.readAllLines(this, charset)");
        return iterable;
    }

    public static final String readText(Path object, Charset object2) {
        Intrinsics.checkNotNullParameter(object, "$this$readText");
        Intrinsics.checkNotNullParameter(object2, "charset");
        object = new InputStreamReader(Files.newInputStream((Path)object, Arrays.copyOf(new OpenOption[0], 0)), (Charset)object2);
        Throwable throwable = null;
        try {
            object2 = TextStreamsKt.readText((InputStreamReader)object);
        }
        catch (Throwable throwable2) {
            try {
                throw throwable2;
            }
            catch (Throwable throwable3) {
                CloseableKt.closeFinally((Closeable)object, throwable2);
                throw throwable3;
            }
        }
        CloseableKt.closeFinally((Closeable)object, throwable);
        return object2;
    }

    public static /* synthetic */ String readText$default(Path path, Charset charset, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return PathsKt.readText(path, charset);
    }

    private static final InputStreamReader reader(Path path, Charset charset, OpenOption ... openOptionArray) {
        return new InputStreamReader(Files.newInputStream(path, Arrays.copyOf(openOptionArray, openOptionArray.length)), charset);
    }

    static /* synthetic */ InputStreamReader reader$default(Path path, Charset charset, OpenOption[] openOptionArray, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return new InputStreamReader(Files.newInputStream(path, Arrays.copyOf(openOptionArray, openOptionArray.length)), charset);
    }

    private static final <T> T useLines(Path object, Charset object2, Function1<? super Sequence<String>, ? extends T> function1) {
        object = Files.newBufferedReader((Path)object, (Charset)object2);
        object2 = null;
        try {
            BufferedReader bufferedReader = (BufferedReader)object;
            Intrinsics.checkNotNullExpressionValue(bufferedReader, "it");
            function1 = function1.invoke(TextStreamsKt.lineSequence(bufferedReader));
        }
        catch (Throwable throwable) {
            try {
                throw throwable;
            }
            catch (Throwable throwable2) {
                InlineMarker.finallyStart(1);
                if (!PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                    if (object != null) {
                        try {
                            object.close();
                        }
                        catch (Throwable throwable3) {}
                    }
                } else {
                    CloseableKt.closeFinally((Closeable)object, throwable);
                }
                InlineMarker.finallyEnd(1);
                throw throwable2;
            }
        }
        InlineMarker.finallyStart(1);
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
            CloseableKt.closeFinally((Closeable)object, (Throwable)object2);
        } else if (object != null) {
            object.close();
        }
        InlineMarker.finallyEnd(1);
        return (T)function1;
    }

    static /* synthetic */ Object useLines$default(Path object, Charset object2, Function1 function1, int n, Object object3) {
        if ((n & 1) != 0) {
            object2 = Charsets.UTF_8;
        }
        object = Files.newBufferedReader((Path)object, (Charset)object2);
        object2 = null;
        try {
            object3 = (BufferedReader)object;
            Intrinsics.checkNotNullExpressionValue(object3, "it");
            function1 = function1.invoke(TextStreamsKt.lineSequence((BufferedReader)object3));
        }
        catch (Throwable throwable) {
            try {
                throw throwable;
            }
            catch (Throwable throwable2) {
                InlineMarker.finallyStart(1);
                if (!PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                    if (object != null) {
                        try {
                            object.close();
                        }
                        catch (Throwable throwable3) {}
                    }
                } else {
                    CloseableKt.closeFinally((Closeable)object, throwable);
                }
                InlineMarker.finallyEnd(1);
                throw throwable2;
            }
        }
        InlineMarker.finallyStart(1);
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
            CloseableKt.closeFinally((Closeable)object, (Throwable)object2);
        } else if (object != null) {
            object.close();
        }
        InlineMarker.finallyEnd(1);
        return function1;
    }

    private static final void writeBytes(Path path, byte[] byArray, OpenOption ... openOptionArray) {
        Files.write(path, byArray, Arrays.copyOf(openOptionArray, openOptionArray.length));
    }

    private static final Path writeLines(Path path, Iterable<? extends CharSequence> iterable, Charset charset, OpenOption ... openOptionArray) {
        path = Files.write(path, iterable, charset, Arrays.copyOf(openOptionArray, openOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(path, "Files.write(this, lines, charset, *options)");
        return path;
    }

    private static final Path writeLines(Path path, Sequence<? extends CharSequence> sequence, Charset charset, OpenOption ... openOptionArray) {
        path = Files.write(path, SequencesKt.asIterable(sequence), charset, Arrays.copyOf(openOptionArray, openOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(path, "Files.write(this, lines.\u2026ble(), charset, *options)");
        return path;
    }

    static /* synthetic */ Path writeLines$default(Path path, Iterable iterable, Charset charset, OpenOption[] openOptionArray, int n, Object object) {
        if ((n & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        path = Files.write(path, (Iterable<? extends CharSequence>)iterable, charset, Arrays.copyOf(openOptionArray, openOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(path, "Files.write(this, lines, charset, *options)");
        return path;
    }

    static /* synthetic */ Path writeLines$default(Path path, Sequence sequence, Charset charset, OpenOption[] openOptionArray, int n, Object object) {
        if ((n & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        path = Files.write(path, SequencesKt.asIterable(sequence), charset, Arrays.copyOf(openOptionArray, openOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(path, "Files.write(this, lines.\u2026ble(), charset, *options)");
        return path;
    }

    public static final void writeText(Path object, CharSequence charSequence, Charset object2, OpenOption ... openOptionArray) {
        Intrinsics.checkNotNullParameter(object, "$this$writeText");
        Intrinsics.checkNotNullParameter(charSequence, "text");
        Intrinsics.checkNotNullParameter(object2, "charset");
        Intrinsics.checkNotNullParameter(openOptionArray, "options");
        object = Files.newOutputStream((Path)object, Arrays.copyOf(openOptionArray, openOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(object, "Files.newOutputStream(this, *options)");
        object = new OutputStreamWriter((OutputStream)object, (Charset)object2);
        object2 = null;
        try {
            ((OutputStreamWriter)object).append(charSequence);
        }
        catch (Throwable throwable) {
            try {
                throw throwable;
            }
            catch (Throwable throwable2) {
                CloseableKt.closeFinally((Closeable)object, throwable);
                throw throwable2;
            }
        }
        CloseableKt.closeFinally((Closeable)object, (Throwable)object2);
    }

    public static /* synthetic */ void writeText$default(Path path, CharSequence charSequence, Charset charset, OpenOption[] openOptionArray, int n, Object object) {
        if ((n & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        PathsKt.writeText(path, charSequence, charset, openOptionArray);
    }

    private static final OutputStreamWriter writer(Path path, Charset charset, OpenOption ... openOptionArray) {
        return new OutputStreamWriter(Files.newOutputStream(path, Arrays.copyOf(openOptionArray, openOptionArray.length)), charset);
    }

    static /* synthetic */ OutputStreamWriter writer$default(Path path, Charset charset, OpenOption[] openOptionArray, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return new OutputStreamWriter(Files.newOutputStream(path, Arrays.copyOf(openOptionArray, openOptionArray.length)), charset);
    }
}

