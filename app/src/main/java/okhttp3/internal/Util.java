/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.IDN;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import okhttp3.HttpUrl;
import okio.Buffer;
import okio.ByteString;
import okio.Source;

public final class Util {
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final TimeZone UTC;
    public static final Charset UTF_8;
    private static final Pattern VERIFY_AS_IP_ADDRESS;

    static {
        UTF_8 = Charset.forName("UTF-8");
        UTC = TimeZone.getTimeZone("GMT");
        VERIFY_AS_IP_ADDRESS = Pattern.compile("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
    }

    private Util() {
    }

    public static void checkOffsetAndCount(long l, long l2, long l3) {
        if ((l2 | l3) >= 0L && l2 <= l && l - l2 >= l3) {
            return;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public static void closeAll(Closeable closeable, Closeable object) throws IOException {
        block8: {
            Object var2_3 = null;
            try {
                closeable.close();
                closeable = var2_3;
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            try {
                object.close();
                object = closeable;
            }
            catch (Throwable throwable) {
                object = closeable;
                if (closeable != null) break block8;
                object = throwable;
            }
        }
        if (object == null) {
            return;
        }
        if (!(object instanceof IOException)) {
            if (!(object instanceof RuntimeException)) {
                if (object instanceof Error) {
                    throw (Error)object;
                }
                throw new AssertionError(object);
            }
            throw (RuntimeException)object;
        }
        throw (IOException)object;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (Exception exception) {
            }
            catch (RuntimeException runtimeException) {
                throw runtimeException;
            }
        }
    }

    public static void closeQuietly(ServerSocket serverSocket) {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            }
            catch (Exception exception) {
            }
            catch (RuntimeException runtimeException) {
                throw runtimeException;
            }
        }
    }

    public static void closeQuietly(Socket socket) {
        block5: {
            if (socket != null) {
                try {
                    socket.close();
                }
                catch (Exception exception) {
                }
                catch (RuntimeException runtimeException) {
                    throw runtimeException;
                }
                catch (AssertionError assertionError) {
                    if (Util.isAndroidGetsocknameError(assertionError)) break block5;
                    throw assertionError;
                }
            }
        }
    }

    public static String[] concat(String[] stringArray, String string2) {
        String[] stringArray2 = new String[stringArray.length + 1];
        System.arraycopy(stringArray, 0, stringArray2, 0, stringArray.length);
        stringArray2[stringArray2.length - 1] = string2;
        return stringArray2;
    }

    public static boolean contains(String[] stringArray, String string2) {
        return Arrays.asList(stringArray).contains(string2);
    }

    private static boolean containsInvalidHostnameAsciiCodes(String string2) {
        for (int i = 0; i < string2.length(); ++i) {
            char c = string2.charAt(i);
            if (c > '\u001f' && c < '\u007f') {
                if (" #%/:?@[\\]".indexOf(c) == -1) continue;
                return true;
            }
            return true;
        }
        return false;
    }

    public static int delimiterOffset(String string2, int n, int n2, char c) {
        while (n < n2) {
            if (string2.charAt(n) == c) {
                return n;
            }
            ++n;
        }
        return n2;
    }

    public static int delimiterOffset(String string2, int n, int n2, String string3) {
        while (n < n2) {
            if (string3.indexOf(string2.charAt(n)) != -1) {
                return n;
            }
            ++n;
        }
        return n2;
    }

    public static boolean discard(Source source, int n, TimeUnit timeUnit) {
        try {
            boolean bl = Util.skipAll(source, n, timeUnit);
            return bl;
        }
        catch (IOException iOException) {
            return false;
        }
    }

    public static String domainToAscii(String string2) {
        block4: {
            try {
                string2 = IDN.toASCII(string2).toLowerCase(Locale.US);
                if (!string2.isEmpty()) break block4;
                return null;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                return null;
            }
        }
        boolean bl = Util.containsInvalidHostnameAsciiCodes(string2);
        if (bl) {
            return null;
        }
        return string2;
    }

    public static boolean equal(Object object, Object object2) {
        boolean bl = object == object2 || object != null && object.equals(object2);
        return bl;
    }

    public static String format(String string2, Object ... objectArray) {
        return String.format(Locale.US, string2, objectArray);
    }

    public static String hostHeader(HttpUrl object, boolean bl) {
        CharSequence charSequence;
        if (((HttpUrl)object).host().contains(":")) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("[");
            ((StringBuilder)charSequence).append(((HttpUrl)object).host());
            ((StringBuilder)charSequence).append("]");
            charSequence = ((StringBuilder)charSequence).toString();
        } else {
            charSequence = ((HttpUrl)object).host();
        }
        if (!bl && ((HttpUrl)object).port() == HttpUrl.defaultPort(((HttpUrl)object).scheme())) {
            object = charSequence;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)charSequence);
            stringBuilder.append(":");
            stringBuilder.append(((HttpUrl)object).port());
            object = stringBuilder.toString();
        }
        return object;
    }

    public static <T> List<T> immutableList(List<T> list) {
        return Collections.unmodifiableList(new ArrayList<T>(list));
    }

    public static <T> List<T> immutableList(T ... TArray) {
        return Collections.unmodifiableList(Arrays.asList((Object[])TArray.clone()));
    }

    public static <K, V> Map<K, V> immutableMap(Map<K, V> map) {
        return Collections.unmodifiableMap(new LinkedHashMap<K, V>(map));
    }

    private static <T> List<T> intersect(T[] TArray, T[] TArray2) {
        ArrayList<T> arrayList = new ArrayList<T>();
        block0: for (T t : TArray) {
            for (T t2 : TArray2) {
                if (!t.equals(t2)) continue;
                arrayList.add(t2);
                continue block0;
            }
        }
        return arrayList;
    }

    public static <T> T[] intersect(Class<T> clazz, T[] object, T[] TArray) {
        object = Util.intersect(object, TArray);
        return object.toArray((Object[])Array.newInstance(clazz, object.size()));
    }

    public static boolean isAndroidGetsocknameError(AssertionError assertionError) {
        boolean bl = ((Throwable)((Object)assertionError)).getCause() != null && ((Throwable)((Object)assertionError)).getMessage() != null && ((Throwable)((Object)assertionError)).getMessage().contains("getsockname failed");
        return bl;
    }

    /*
     * WARNING - void declaration
     */
    public static String md5Hex(String string2) {
        void var0_3;
        try {
            string2 = ByteString.of(MessageDigest.getInstance("MD5").digest(string2.getBytes("UTF-8"))).hex();
            return string2;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            // empty catch block
        }
        throw new AssertionError(var0_3);
    }

    public static ByteString sha1(ByteString byteString) {
        try {
            byteString = ByteString.of(MessageDigest.getInstance("SHA-1").digest(byteString.toByteArray()));
            return byteString;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new AssertionError((Object)noSuchAlgorithmException);
        }
    }

    public static ByteString sha256(ByteString byteString) {
        try {
            byteString = ByteString.of(MessageDigest.getInstance("SHA-256").digest(byteString.toByteArray()));
            return byteString;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new AssertionError((Object)noSuchAlgorithmException);
        }
    }

    /*
     * WARNING - void declaration
     */
    public static String shaBase64(String string2) {
        void var0_3;
        try {
            string2 = ByteString.of(MessageDigest.getInstance("SHA-1").digest(string2.getBytes("UTF-8"))).base64();
            return string2;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            // empty catch block
        }
        throw new AssertionError(var0_3);
    }

    public static boolean skipAll(Source source, int n, TimeUnit object) throws IOException {
        long l = System.nanoTime();
        long l2 = source.timeout().hasDeadline() ? source.timeout().deadlineNanoTime() - l : Long.MAX_VALUE;
        source.timeout().deadlineNanoTime(Math.min(l2, ((TimeUnit)((Object)object)).toNanos(n)) + l);
        try {
            object = new Buffer();
            while (source.read((Buffer)object, 8192L) != -1L) {
                ((Buffer)object).clear();
            }
            return true;
        }
        catch (InterruptedIOException interruptedIOException) {
            return false;
        }
        finally {
            if (l2 == Long.MAX_VALUE) {
                source.timeout().clearDeadline();
            } else {
                source.timeout().deadlineNanoTime(l + l2);
            }
        }
    }

    public static int skipLeadingAsciiWhitespace(String string2, int n, int n2) {
        while (n < n2) {
            switch (string2.charAt(n)) {
                default: {
                    return n;
                }
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': 
            }
            ++n;
        }
        return n2;
    }

    public static int skipTrailingAsciiWhitespace(String string2, int n, int n2) {
        --n2;
        while (n2 >= n) {
            switch (string2.charAt(n2)) {
                default: {
                    return n2 + 1;
                }
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case ' ': 
            }
            --n2;
        }
        return n;
    }

    public static ThreadFactory threadFactory(String string2, boolean bl) {
        return new ThreadFactory(string2, bl){
            final boolean val$daemon;
            final String val$name;
            {
                this.val$name = string2;
                this.val$daemon = bl;
            }

            @Override
            public Thread newThread(Runnable runnable) {
                runnable = new Thread(runnable, this.val$name);
                ((Thread)runnable).setDaemon(this.val$daemon);
                return runnable;
            }
        };
    }

    public static String toHumanReadableAscii(String string2) {
        int n;
        int n2 = string2.length();
        for (int i = 0; i < n2; i += Character.charCount(n)) {
            n = string2.codePointAt(i);
            if (n > 31 && n < 127) {
                continue;
            }
            Buffer buffer = new Buffer();
            buffer.writeUtf8(string2, 0, i);
            while (i < n2) {
                int n3 = string2.codePointAt(i);
                n = n3 > 31 && n3 < 127 ? n3 : 63;
                buffer.writeUtf8CodePoint(n);
                i += Character.charCount(n3);
            }
            return buffer.readUtf8();
        }
        return string2;
    }

    public static String trimSubstring(String string2, int n, int n2) {
        n = Util.skipLeadingAsciiWhitespace(string2, n, n2);
        return string2.substring(n, Util.skipTrailingAsciiWhitespace(string2, n, n2));
    }

    public static boolean verifyAsIpAddress(String string2) {
        return VERIFY_AS_IP_ADDRESS.matcher(string2).matches();
    }
}

