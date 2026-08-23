/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u00c0\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0014\u001a\u00020\u0004H\u0002J\u0010\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0010H\u0002J\u0018\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u0004H\u0002J\u0018\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fJ\b\u0010 \u001a\u00020!H\u0002J\b\u0010\"\u001a\u00020!H\u0002J\u0010\u0010#\u001a\u00020!2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0011\u001a\u00060\u0012j\u0002`\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2={"Lkotlin/io/LineReader;", "", "()V", "BUFFER_SIZE", "", "byteBuf", "Ljava/nio/ByteBuffer;", "bytes", "", "charBuf", "Ljava/nio/CharBuffer;", "chars", "", "decoder", "Ljava/nio/charset/CharsetDecoder;", "directEOL", "", "sb", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "compactBytes", "decode", "endOfInput", "decodeEndOfInput", "nBytes", "nChars", "readLine", "", "inputStream", "Ljava/io/InputStream;", "charset", "Ljava/nio/charset/Charset;", "resetAll", "", "trimStringBuilder", "updateCharset", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class LineReader {
    private static final int BUFFER_SIZE = 32;
    public static final LineReader INSTANCE = new LineReader();
    private static final ByteBuffer byteBuf;
    private static final byte[] bytes;
    private static final CharBuffer charBuf;
    private static final char[] chars;
    private static CharsetDecoder decoder;
    private static boolean directEOL;
    private static final StringBuilder sb;

    static {
        Object object = new byte[32];
        bytes = object;
        Object object2 = new char[32];
        chars = object2;
        object = ByteBuffer.wrap(object);
        Intrinsics.checkNotNullExpressionValue(object, "ByteBuffer.wrap(bytes)");
        byteBuf = object;
        object2 = CharBuffer.wrap(object2);
        Intrinsics.checkNotNullExpressionValue(object2, "CharBuffer.wrap(chars)");
        charBuf = object2;
        sb = new StringBuilder();
    }

    private LineReader() {
    }

    public static final /* synthetic */ CharsetDecoder access$getDecoder$p(LineReader object) {
        object = decoder;
        if (object == null) {
            Intrinsics.throwUninitializedPropertyAccessException("decoder");
        }
        return object;
    }

    public static final /* synthetic */ void access$setDecoder$p(LineReader lineReader, CharsetDecoder charsetDecoder) {
        decoder = charsetDecoder;
    }

    private final int compactBytes() {
        ByteBuffer byteBuffer = byteBuf;
        byteBuffer.compact();
        int n = byteBuffer.position();
        byteBuffer.position(0);
        return n;
    }

    private final int decode(boolean bl) {
        while (true) {
            Object object;
            if ((object = decoder) == null) {
                Intrinsics.throwUninitializedPropertyAccessException("decoder");
            }
            Comparable<ByteBuffer> comparable = byteBuf;
            CharBuffer charBuffer = charBuf;
            object = ((CharsetDecoder)object).decode((ByteBuffer)comparable, charBuffer, bl);
            Intrinsics.checkNotNullExpressionValue(object, "decoder.decode(byteBuf, charBuf, endOfInput)");
            if (((CoderResult)object).isError()) {
                this.resetAll();
                ((CoderResult)object).throwException();
            }
            int n = charBuffer.position();
            if (!((CoderResult)object).isOverflow()) {
                return n;
            }
            comparable = sb;
            object = chars;
            ((StringBuilder)comparable).append((char[])object, 0, n - 1);
            charBuffer.position(0);
            charBuffer.limit(32);
            charBuffer.put((char)object[n - 1]);
        }
    }

    private final int decodeEndOfInput(int n, int n2) {
        ByteBuffer byteBuffer = byteBuf;
        byteBuffer.limit(n);
        charBuf.position(n2);
        n = this.decode(true);
        CharsetDecoder charsetDecoder = decoder;
        if (charsetDecoder == null) {
            Intrinsics.throwUninitializedPropertyAccessException("decoder");
        }
        charsetDecoder.reset();
        byteBuffer.position(0);
        return n;
    }

    private final void resetAll() {
        CharsetDecoder charsetDecoder = decoder;
        if (charsetDecoder == null) {
            Intrinsics.throwUninitializedPropertyAccessException("decoder");
        }
        charsetDecoder.reset();
        byteBuf.position(0);
        sb.setLength(0);
    }

    private final void trimStringBuilder() {
        StringBuilder stringBuilder = sb;
        stringBuilder.setLength(32);
        stringBuilder.trimToSize();
    }

    private final void updateCharset(Charset object) {
        object = ((Charset)object).newDecoder();
        Intrinsics.checkNotNullExpressionValue(object, "charset.newDecoder()");
        decoder = object;
        ByteBuffer byteBuffer = byteBuf;
        byteBuffer.clear();
        CharBuffer charBuffer = charBuf;
        charBuffer.clear();
        byteBuffer.put((byte)10);
        byteBuffer.flip();
        object = decoder;
        if (object == null) {
            Intrinsics.throwUninitializedPropertyAccessException("decoder");
        }
        boolean bl = false;
        ((CharsetDecoder)object).decode(byteBuffer, charBuffer, false);
        boolean bl2 = bl;
        if (charBuffer.position() == 1) {
            bl2 = bl;
            if (charBuffer.get(0) == '\n') {
                bl2 = true;
            }
        }
        directEOL = bl2;
        this.resetAll();
    }

    /*
     * Unable to fully structure code
     */
    public final String readLine(InputStream var1_1, Charset var2_3) {
        synchronized (this) {
            Intrinsics.checkNotNullParameter(var1_1, "inputStream");
            Intrinsics.checkNotNullParameter(var2_3, "charset");
            var8_4 = LineReader.decoder;
            var6_5 = 1;
            if (var8_4 == null) ** GOTO lbl12
            if (var8_4 != null) ** GOTO lbl11
            try {
                block31: {
                    Intrinsics.throwUninitializedPropertyAccessException("decoder");
lbl11:
                    // 2 sources

                    if (!(Intrinsics.areEqual(var8_4.charset(), var2_3) ^ true)) break block31;
lbl12:
                    // 2 sources

                    this.updateCharset((Charset)var2_3);
                }
                var3_6 = 0;
                var4_7 = 0;
            }
            catch (Throwable var1_2) {}
            while (true) {
                var7_9 = var1_1.read();
                if (var7_9 != -1) ** GOTO lbl32
                break;
            }
            {
                throw var1_2;
            }
            {
                block29: {
                    block30: {
                        block27: {
                            block28: {
                                block26: {
                                    var5_8 = ((CharSequence)LineReader.sb).length();
                                    var5_8 = var5_8 == 0 ? 1 : 0;
                                    if (var5_8 == 0 || var3_6 != 0 || var4_7 != 0) break block26;
                                    return null;
                                }
                                var4_7 = this.decodeEndOfInput(var3_6, var4_7);
                                break block27;
lbl32:
                                // 1 sources

                                var2_3 = LineReader.bytes;
                                var5_8 = var3_6 + 1;
                                var2_3[var3_6] = (byte)var7_9;
                                if (var7_9 != 10 && var5_8 != 32) {
                                    if (!LineReader.directEOL) break block28;
                                    var3_6 = var5_8;
                                    continue;
                                }
                            }
                            var2_3 = LineReader.byteBuf;
                            var2_3.limit(var5_8);
                            LineReader.charBuf.position(var4_7);
                            var4_7 = this.decode(false);
                            if (var4_7 <= 0) break block29;
                            if (LineReader.chars[var4_7 - 1] != '\n') break block29;
                            var2_3.position(0);
                        }
                        var3_6 = var4_7;
                        if (var4_7 > 0) {
                            var1_1 = LineReader.chars;
                            var3_6 = var4_7;
                            if (var1_1[var4_7 - 1] == 10) {
                                var3_6 = --var4_7;
                                if (var4_7 > 0) {
                                    var3_6 = var4_7;
                                    if (var1_1[var4_7 - 1] == 13) {
                                        var3_6 = var4_7 - 1;
                                    }
                                }
                            }
                        }
                        var1_1 = LineReader.sb;
                        var4_7 = ((CharSequence)var1_1).length() == 0 ? var6_5 : 0;
                        if (var4_7 == 0) break block30;
                        var1_1 = new String(LineReader.chars, 0, var3_6);
                        return var1_1;
                    }
                    var1_1.append(LineReader.chars, 0, var3_6);
                    var2_3 = var1_1.toString();
                    Intrinsics.checkNotNullExpressionValue(var2_3, "sb.toString()");
                    if (var1_1.length() > 32) {
                        this.trimStringBuilder();
                    }
                    var1_1.setLength(0);
                    return var2_3;
                }
                var3_6 = this.compactBytes();
                continue;
            }
        }
    }
}

