/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io;

import java.io.InputStream;
import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.io.LineReader;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u0005\n\u0002\u0010\f\n\u0002\u0010\u0019\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\u001a\u0013\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0004H\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0005H\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0007H\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\bH\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\tH\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\nH\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000bH\u0087\b\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\fH\u0087\b\u001a\t\u0010\r\u001a\u00020\u0001H\u0087\b\u001a\u0013\u0010\r\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0004H\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0005H\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0007H\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\bH\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\tH\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\nH\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000bH\u0087\b\u001a\u0011\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\fH\u0087\b\u001a\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f\u00a8\u0006\u0010"}, d2={"print", "", "message", "", "", "", "", "", "", "", "", "", "", "println", "readLine", "", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class ConsoleKt {
    private static final void print(byte by) {
        System.out.print((Object)by);
    }

    private static final void print(char c) {
        System.out.print(c);
    }

    private static final void print(double d) {
        System.out.print(d);
    }

    private static final void print(float f) {
        System.out.print(f);
    }

    private static final void print(int n) {
        System.out.print(n);
    }

    private static final void print(long l) {
        System.out.print(l);
    }

    private static final void print(Object object) {
        System.out.print(object);
    }

    private static final void print(short s) {
        System.out.print((Object)s);
    }

    private static final void print(boolean bl) {
        System.out.print(bl);
    }

    private static final void print(char[] cArray) {
        System.out.print(cArray);
    }

    private static final void println() {
        System.out.println();
    }

    private static final void println(byte by) {
        System.out.println((Object)by);
    }

    private static final void println(char c) {
        System.out.println(c);
    }

    private static final void println(double d) {
        System.out.println(d);
    }

    private static final void println(float f) {
        System.out.println(f);
    }

    private static final void println(int n) {
        System.out.println(n);
    }

    private static final void println(long l) {
        System.out.println(l);
    }

    private static final void println(Object object) {
        System.out.println(object);
    }

    private static final void println(short s) {
        System.out.println((Object)s);
    }

    private static final void println(boolean bl) {
        System.out.println(bl);
    }

    private static final void println(char[] cArray) {
        System.out.println(cArray);
    }

    public static final String readLine() {
        LineReader lineReader = LineReader.INSTANCE;
        InputStream inputStream = System.in;
        Intrinsics.checkNotNullExpressionValue(inputStream, "System.`in`");
        Charset charset = Charset.defaultCharset();
        Intrinsics.checkNotNullExpressionValue(charset, "Charset.defaultCharset()");
        return lineReader.readLine(inputStream, charset);
    }
}

