/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.io.LinesSequence;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.Charsets;

@Metadata(bv={1, 0, 3}, d1={"\u0000X\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0017\u0010\u0000\u001a\u00020\u0005*\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u001c\u0010\u0007\u001a\u00020\b*\u00020\u00022\u0006\u0010\t\u001a\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\u001e\u0010\n\u001a\u00020\u000b*\u00020\u00022\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000b0\r\u001a\u0010\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0010*\u00020\u0001\u001a\n\u0010\u0011\u001a\u00020\u0012*\u00020\u0013\u001a\u0010\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0015*\u00020\u0002\u001a\n\u0010\u0016\u001a\u00020\u000e*\u00020\u0002\u001a\u0017\u0010\u0016\u001a\u00020\u000e*\u00020\u00132\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\u0087\b\u001a\r\u0010\u0019\u001a\u00020\u001a*\u00020\u000eH\u0087\b\u001a8\u0010\u001b\u001a\u0002H\u001c\"\u0004\b\u0000\u0010\u001c*\u00020\u00022\u0018\u0010\u001d\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u0010\u0012\u0004\u0012\u0002H\u001c0\rH\u0086\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0002\u0010\u001f\u0082\u0002\u000f\n\u0006\b\u0011(\u001e0\u0001\n\u0005\b\u009920\u0001\u00a8\u0006 "}, d2={"buffered", "Ljava/io/BufferedReader;", "Ljava/io/Reader;", "bufferSize", "", "Ljava/io/BufferedWriter;", "Ljava/io/Writer;", "copyTo", "", "out", "forEachLine", "", "action", "Lkotlin/Function1;", "", "lineSequence", "Lkotlin/sequences/Sequence;", "readBytes", "", "Ljava/net/URL;", "readLines", "", "readText", "charset", "Ljava/nio/charset/Charset;", "reader", "Ljava/io/StringReader;", "useLines", "T", "block", "Requires newer compiler version to be inlined correctly.", "(Ljava/io/Reader;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class TextStreamsKt {
    private static final BufferedReader buffered(Reader reader, int n) {
        reader = reader instanceof BufferedReader ? (BufferedReader)reader : new BufferedReader(reader, n);
        return reader;
    }

    private static final BufferedWriter buffered(Writer writer, int n) {
        writer = writer instanceof BufferedWriter ? (BufferedWriter)writer : new BufferedWriter(writer, n);
        return writer;
    }

    static /* synthetic */ BufferedReader buffered$default(Reader reader, int n, int n2, Object object) {
        if ((n2 & 1) != 0) {
            n = 8192;
        }
        reader = reader instanceof BufferedReader ? (BufferedReader)reader : new BufferedReader(reader, n);
        return reader;
    }

    static /* synthetic */ BufferedWriter buffered$default(Writer writer, int n, int n2, Object object) {
        if ((n2 & 1) != 0) {
            n = 8192;
        }
        writer = writer instanceof BufferedWriter ? (BufferedWriter)writer : new BufferedWriter(writer, n);
        return writer;
    }

    public static final long copyTo(Reader reader, Writer writer, int n) {
        Intrinsics.checkNotNullParameter(reader, "$this$copyTo");
        Intrinsics.checkNotNullParameter(writer, "out");
        long l = 0L;
        char[] cArray = new char[n];
        n = reader.read(cArray);
        while (n >= 0) {
            writer.write(cArray, 0, n);
            l += (long)n;
            n = reader.read(cArray);
        }
        return l;
    }

    public static /* synthetic */ long copyTo$default(Reader reader, Writer writer, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 8192;
        }
        return TextStreamsKt.copyTo(reader, writer, n);
    }

    public static final void forEachLine(Reader closeable, Function1<? super String, Unit> object) {
        Intrinsics.checkNotNullParameter(closeable, "$this$forEachLine");
        Intrinsics.checkNotNullParameter(object, "action");
        closeable = closeable instanceof BufferedReader ? (BufferedReader)closeable : new BufferedReader((Reader)closeable, 8192);
        closeable = closeable;
        Throwable throwable = null;
        try {
            Iterator<String> iterator2 = TextStreamsKt.lineSequence((BufferedReader)closeable).iterator();
            while (iterator2.hasNext()) {
                object.invoke((String)iterator2.next());
            }
            object = Unit.INSTANCE;
        }
        catch (Throwable throwable2) {
            try {
                throw throwable2;
            }
            catch (Throwable throwable3) {
                CloseableKt.closeFinally(closeable, throwable2);
                throw throwable3;
            }
        }
        CloseableKt.closeFinally(closeable, throwable);
    }

    public static final Sequence<String> lineSequence(BufferedReader bufferedReader) {
        Intrinsics.checkNotNullParameter(bufferedReader, "$this$lineSequence");
        return SequencesKt.constrainOnce(new LinesSequence(bufferedReader));
    }

    public static final byte[] readBytes(URL object) {
        Object object2;
        Intrinsics.checkNotNullParameter(object, "$this$readBytes");
        object = ((URL)object).openStream();
        Throwable throwable = null;
        try {
            object2 = (InputStream)object;
            Intrinsics.checkNotNullExpressionValue(object2, "it");
            object2 = ByteStreamsKt.readBytes((InputStream)object2);
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

    public static final List<String> readLines(Reader reader) {
        Intrinsics.checkNotNullParameter(reader, "$this$readLines");
        ArrayList arrayList = new ArrayList();
        TextStreamsKt.forEachLine(reader, (Function1<? super String, Unit>)new Function1<String, Unit>(arrayList){
            final ArrayList $result;
            {
                this.$result = arrayList;
                super(1);
            }

            public final void invoke(String string2) {
                Intrinsics.checkNotNullParameter(string2, "it");
                this.$result.add(string2);
            }
        });
        return arrayList;
    }

    public static final String readText(Reader object) {
        Intrinsics.checkNotNullParameter(object, "$this$readText");
        StringWriter stringWriter = new StringWriter();
        TextStreamsKt.copyTo$default((Reader)object, stringWriter, 0, 2, null);
        object = stringWriter.toString();
        Intrinsics.checkNotNullExpressionValue(object, "buffer.toString()");
        return object;
    }

    private static final String readText(URL uRL, Charset charset) {
        return new String(TextStreamsKt.readBytes(uRL), charset);
    }

    static /* synthetic */ String readText$default(URL uRL, Charset charset, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return new String(TextStreamsKt.readBytes(uRL), charset);
    }

    private static final StringReader reader(String string2) {
        return new StringReader(string2);
    }

    public static final <T> T useLines(Reader closeable, Function1<? super Sequence<String>, ? extends T> function1) {
        Intrinsics.checkNotNullParameter(closeable, "$this$useLines");
        Intrinsics.checkNotNullParameter(function1, "block");
        closeable = closeable instanceof BufferedReader ? (BufferedReader)closeable : new BufferedReader((Reader)closeable, 8192);
        closeable = closeable;
        Throwable throwable = null;
        try {
            function1 = function1.invoke(TextStreamsKt.lineSequence((BufferedReader)closeable));
        }
        catch (Throwable throwable2) {
            try {
                throw throwable2;
            }
            catch (Throwable throwable3) {
                InlineMarker.finallyStart(1);
                if (!PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                    try {
                        closeable.close();
                    }
                    catch (Throwable throwable4) {}
                } else {
                    CloseableKt.closeFinally(closeable, throwable2);
                }
                InlineMarker.finallyEnd(1);
                throw throwable3;
            }
        }
        InlineMarker.finallyStart(1);
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
            CloseableKt.closeFinally(closeable, throwable);
        } else {
            closeable.close();
        }
        InlineMarker.finallyEnd(1);
        return (T)function1;
    }
}

