/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.NoSuchElementException;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.ByteIterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

@Metadata(bv={1, 0, 3}, d1={"\u0000Z\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0017\u0010\u0000\u001a\u00020\u0005*\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0017\u0010\u0007\u001a\u00020\b*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\u000b\u001a\u00020\f*\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\r\u001a\u00020\u000e*\u00020\u000f2\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u001c\u0010\u0010\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\r\u0010\u0013\u001a\u00020\u000e*\u00020\u0014H\u0087\b\u001a\u001d\u0010\u0013\u001a\u00020\u000e*\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0004H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u0018*\u00020\u0001H\u0086\u0002\u001a\f\u0010\u0019\u001a\u00020\u0014*\u00020\u0002H\u0007\u001a\u0016\u0010\u0019\u001a\u00020\u0014*\u00020\u00022\b\b\u0002\u0010\u001a\u001a\u00020\u0004H\u0007\u001a\u0017\u0010\u001b\u001a\u00020\u001c*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\u001d\u001a\u00020\u001e*\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u00a8\u0006\u001f"}, d2={"buffered", "Ljava/io/BufferedInputStream;", "Ljava/io/InputStream;", "bufferSize", "", "Ljava/io/BufferedOutputStream;", "Ljava/io/OutputStream;", "bufferedReader", "Ljava/io/BufferedReader;", "charset", "Ljava/nio/charset/Charset;", "bufferedWriter", "Ljava/io/BufferedWriter;", "byteInputStream", "Ljava/io/ByteArrayInputStream;", "", "copyTo", "", "out", "inputStream", "", "offset", "length", "iterator", "Lkotlin/collections/ByteIterator;", "readBytes", "estimatedSize", "reader", "Ljava/io/InputStreamReader;", "writer", "Ljava/io/OutputStreamWriter;", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class ByteStreamsKt {
    private static final BufferedInputStream buffered(InputStream inputStream, int n) {
        inputStream = inputStream instanceof BufferedInputStream ? (BufferedInputStream)inputStream : new BufferedInputStream(inputStream, n);
        return inputStream;
    }

    private static final BufferedOutputStream buffered(OutputStream outputStream, int n) {
        outputStream = outputStream instanceof BufferedOutputStream ? (BufferedOutputStream)outputStream : new BufferedOutputStream(outputStream, n);
        return outputStream;
    }

    static /* synthetic */ BufferedInputStream buffered$default(InputStream inputStream, int n, int n2, Object object) {
        if ((n2 & 1) != 0) {
            n = 8192;
        }
        inputStream = inputStream instanceof BufferedInputStream ? (BufferedInputStream)inputStream : new BufferedInputStream(inputStream, n);
        return inputStream;
    }

    static /* synthetic */ BufferedOutputStream buffered$default(OutputStream outputStream, int n, int n2, Object object) {
        if ((n2 & 1) != 0) {
            n = 8192;
        }
        outputStream = outputStream instanceof BufferedOutputStream ? (BufferedOutputStream)outputStream : new BufferedOutputStream(outputStream, n);
        return outputStream;
    }

    private static final BufferedReader bufferedReader(InputStream closeable, Charset charset) {
        closeable = (closeable = (Reader)new InputStreamReader((InputStream)closeable, charset)) instanceof BufferedReader ? (BufferedReader)closeable : new BufferedReader((Reader)closeable, 8192);
        return closeable;
    }

    static /* synthetic */ BufferedReader bufferedReader$default(InputStream closeable, Charset charset, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        closeable = (closeable = (Reader)new InputStreamReader((InputStream)closeable, charset)) instanceof BufferedReader ? (BufferedReader)closeable : new BufferedReader((Reader)closeable, 8192);
        return closeable;
    }

    private static final BufferedWriter bufferedWriter(OutputStream closeable, Charset charset) {
        closeable = (closeable = (Writer)new OutputStreamWriter((OutputStream)closeable, charset)) instanceof BufferedWriter ? (BufferedWriter)closeable : new BufferedWriter((Writer)closeable, 8192);
        return closeable;
    }

    static /* synthetic */ BufferedWriter bufferedWriter$default(OutputStream closeable, Charset charset, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        closeable = (closeable = (Writer)new OutputStreamWriter((OutputStream)closeable, charset)) instanceof BufferedWriter ? (BufferedWriter)closeable : new BufferedWriter((Writer)closeable, 8192);
        return closeable;
    }

    private static final ByteArrayInputStream byteInputStream(String object, Charset charset) {
        if (object != null) {
            object = ((String)object).getBytes(charset);
            Intrinsics.checkNotNullExpressionValue(object, "(this as java.lang.String).getBytes(charset)");
            return new ByteArrayInputStream((byte[])object);
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    static /* synthetic */ ByteArrayInputStream byteInputStream$default(String object, Charset charset, int n, Object object2) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        if (object != null) {
            object = ((String)object).getBytes(charset);
            Intrinsics.checkNotNullExpressionValue(object, "(this as java.lang.String).getBytes(charset)");
            return new ByteArrayInputStream((byte[])object);
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public static final long copyTo(InputStream inputStream, OutputStream outputStream, int n) {
        Intrinsics.checkNotNullParameter(inputStream, "$this$copyTo");
        Intrinsics.checkNotNullParameter(outputStream, "out");
        long l = 0L;
        byte[] byArray = new byte[n];
        n = inputStream.read(byArray);
        while (n >= 0) {
            outputStream.write(byArray, 0, n);
            l += (long)n;
            n = inputStream.read(byArray);
        }
        return l;
    }

    public static /* synthetic */ long copyTo$default(InputStream inputStream, OutputStream outputStream, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 8192;
        }
        return ByteStreamsKt.copyTo(inputStream, outputStream, n);
    }

    private static final ByteArrayInputStream inputStream(byte[] byArray) {
        return new ByteArrayInputStream(byArray);
    }

    private static final ByteArrayInputStream inputStream(byte[] byArray, int n, int n2) {
        return new ByteArrayInputStream(byArray, n, n2);
    }

    public static final ByteIterator iterator(BufferedInputStream bufferedInputStream) {
        Intrinsics.checkNotNullParameter(bufferedInputStream, "$this$iterator");
        return new ByteIterator(bufferedInputStream){
            final BufferedInputStream $this_iterator;
            private boolean finished;
            private int nextByte;
            private boolean nextPrepared;
            {
                this.$this_iterator = bufferedInputStream;
                this.nextByte = -1;
            }

            private final void prepareNext() {
                if (!this.nextPrepared && !this.finished) {
                    int n;
                    this.nextByte = n = this.$this_iterator.read();
                    boolean bl = true;
                    this.nextPrepared = true;
                    if (n != -1) {
                        bl = false;
                    }
                    this.finished = bl;
                }
            }

            public final boolean getFinished() {
                return this.finished;
            }

            public final int getNextByte() {
                return this.nextByte;
            }

            public final boolean getNextPrepared() {
                return this.nextPrepared;
            }

            public boolean hasNext() {
                this.prepareNext();
                return this.finished ^ true;
            }

            public byte nextByte() {
                this.prepareNext();
                if (!this.finished) {
                    byte by = (byte)this.nextByte;
                    this.nextPrepared = false;
                    return by;
                }
                throw (Throwable)new NoSuchElementException("Input stream is over.");
            }

            public final void setFinished(boolean bl) {
                this.finished = bl;
            }

            public final void setNextByte(int n) {
                this.nextByte = n;
            }

            public final void setNextPrepared(boolean bl) {
                this.nextPrepared = bl;
            }
        };
    }

    public static final byte[] readBytes(InputStream object) {
        Intrinsics.checkNotNullParameter(object, "$this$readBytes");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(Math.max(8192, ((InputStream)object).available()));
        ByteStreamsKt.copyTo$default((InputStream)object, byteArrayOutputStream, 0, 2, null);
        object = byteArrayOutputStream.toByteArray();
        Intrinsics.checkNotNullExpressionValue(object, "buffer.toByteArray()");
        return object;
    }

    @Deprecated(message="Use readBytes() overload without estimatedSize parameter", replaceWith=@ReplaceWith(expression="readBytes()", imports={}))
    public static final byte[] readBytes(InputStream object, int n) {
        Intrinsics.checkNotNullParameter(object, "$this$readBytes");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(Math.max(n, ((InputStream)object).available()));
        ByteStreamsKt.copyTo$default((InputStream)object, byteArrayOutputStream, 0, 2, null);
        object = byteArrayOutputStream.toByteArray();
        Intrinsics.checkNotNullExpressionValue(object, "buffer.toByteArray()");
        return object;
    }

    public static /* synthetic */ byte[] readBytes$default(InputStream inputStream, int n, int n2, Object object) {
        if ((n2 & 1) != 0) {
            n = 8192;
        }
        return ByteStreamsKt.readBytes(inputStream, n);
    }

    private static final InputStreamReader reader(InputStream inputStream, Charset charset) {
        return new InputStreamReader(inputStream, charset);
    }

    static /* synthetic */ InputStreamReader reader$default(InputStream inputStream, Charset charset, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return new InputStreamReader(inputStream, charset);
    }

    private static final OutputStreamWriter writer(OutputStream outputStream, Charset charset) {
        return new OutputStreamWriter(outputStream, charset);
    }

    static /* synthetic */ OutputStreamWriter writer$default(OutputStream outputStream, Charset charset, int n, Object object) {
        if ((n & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return new OutputStreamWriter(outputStream, charset);
    }
}

