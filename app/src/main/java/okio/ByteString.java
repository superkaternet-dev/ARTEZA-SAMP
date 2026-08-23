/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import okio.Base64;
import okio.Buffer;
import okio.Util;

public class ByteString
implements Serializable,
Comparable<ByteString> {
    public static final ByteString EMPTY;
    static final char[] HEX_DIGITS;
    private static final long serialVersionUID = 1L;
    final byte[] data;
    transient int hashCode;
    transient String utf8;

    static {
        HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        EMPTY = ByteString.of(new byte[0]);
    }

    ByteString(byte[] byArray) {
        this.data = byArray;
    }

    static int codePointIndexToCharIndex(String string2, int n) {
        int n2;
        int n3 = 0;
        int n4 = string2.length();
        for (int i = 0; i < n4; i += Character.charCount(n2)) {
            if (n3 == n) {
                return i;
            }
            n2 = string2.codePointAt(i);
            if (Character.isISOControl(n2) && n2 != 10 && n2 != 13 || n2 == 65533) {
                return -1;
            }
            ++n3;
        }
        return string2.length();
    }

    public static ByteString decodeBase64(String object) {
        if (object != null) {
            object = (object = (Object)Base64.decode((String)object)) != null ? new ByteString((byte[])object) : null;
            return object;
        }
        throw new IllegalArgumentException("base64 == null");
    }

    public static ByteString decodeHex(String object) {
        if (object != null) {
            if (((String)object).length() % 2 == 0) {
                byte[] byArray = new byte[((String)object).length() / 2];
                for (int i = 0; i < byArray.length; ++i) {
                    byArray[i] = (byte)((ByteString.decodeHexDigit(((String)object).charAt(i * 2)) << 4) + ByteString.decodeHexDigit(((String)object).charAt(i * 2 + 1)));
                }
                return ByteString.of(byArray);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected hex string: ");
            stringBuilder.append((String)object);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        object = new IllegalArgumentException("hex == null");
        throw object;
    }

    private static int decodeHexDigit(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        if (c >= 'a' && c <= 'f') {
            return c - 97 + 10;
        }
        if (c >= 'A' && c <= 'F') {
            return c - 65 + 10;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unexpected hex digit: ");
        stringBuilder.append(c);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private ByteString digest(String object) {
        try {
            object = ByteString.of(MessageDigest.getInstance((String)object).digest(this.data));
            return object;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new AssertionError((Object)noSuchAlgorithmException);
        }
    }

    public static ByteString encodeUtf8(String string2) {
        if (string2 != null) {
            ByteString byteString = new ByteString(string2.getBytes(Util.UTF_8));
            byteString.utf8 = string2;
            return byteString;
        }
        throw new IllegalArgumentException("s == null");
    }

    public static ByteString of(byte ... byArray) {
        if (byArray != null) {
            return new ByteString((byte[])byArray.clone());
        }
        throw new IllegalArgumentException("data == null");
    }

    public static ByteString of(byte[] byArray, int n, int n2) {
        if (byArray != null) {
            Util.checkOffsetAndCount(byArray.length, n, n2);
            byte[] byArray2 = new byte[n2];
            System.arraycopy(byArray, n, byArray2, 0, n2);
            return new ByteString(byArray2);
        }
        throw new IllegalArgumentException("data == null");
    }

    public static ByteString read(InputStream object, int n) throws IOException {
        if (object != null) {
            if (n >= 0) {
                int n2;
                byte[] byArray = new byte[n];
                for (int i = 0; i < n; i += n2) {
                    n2 = ((InputStream)object).read(byArray, i, n - i);
                    if (n2 != -1) {
                        continue;
                    }
                    throw new EOFException();
                }
                return new ByteString(byArray);
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("byteCount < 0: ");
            ((StringBuilder)object).append(n);
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }
        object = new IllegalArgumentException("in == null");
        throw object;
    }

    private void readObject(ObjectInputStream object) throws IOException {
        object = ByteString.read((InputStream)object, ((ObjectInputStream)object).readInt());
        try {
            Field field = ByteString.class.getDeclaredField("data");
            field.setAccessible(true);
            field.set(this, ((ByteString)object).data);
            return;
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new AssertionError();
        }
        catch (NoSuchFieldException noSuchFieldException) {
            throw new AssertionError();
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeInt(this.data.length);
        objectOutputStream.write(this.data);
    }

    public ByteBuffer asByteBuffer() {
        return ByteBuffer.wrap(this.data).asReadOnlyBuffer();
    }

    public String base64() {
        return Base64.encode(this.data);
    }

    public String base64Url() {
        return Base64.encodeUrl(this.data);
    }

    @Override
    public int compareTo(ByteString byteString) {
        int n;
        int n2;
        int n3;
        block4: {
            int n4;
            int n5;
            n3 = this.size();
            n2 = byteString.size();
            int n6 = 0;
            int n7 = Math.min(n3, n2);
            while (true) {
                n = -1;
                if (n6 >= n7) break block4;
                n5 = this.getByte(n6) & 0xFF;
                if (n5 != (n4 = byteString.getByte(n6) & 0xFF)) break;
                ++n6;
            }
            if (n5 >= n4) {
                n = 1;
            }
            return n;
        }
        if (n3 == n2) {
            return 0;
        }
        if (n3 >= n2) {
            n = 1;
        }
        return n;
    }

    public final boolean endsWith(ByteString byteString) {
        return this.rangeEquals(this.size() - byteString.size(), byteString, 0, byteString.size());
    }

    public final boolean endsWith(byte[] byArray) {
        return this.rangeEquals(this.size() - byArray.length, byArray, 0, byArray.length);
    }

    public boolean equals(Object object) {
        byte[] byArray;
        int n;
        boolean bl = true;
        if (object == this) {
            return true;
        }
        if (!(object instanceof ByteString) || (n = ((ByteString)object).size()) != (byArray = this.data).length || !((ByteString)object).rangeEquals(0, byArray, 0, byArray.length)) {
            bl = false;
        }
        return bl;
    }

    public byte getByte(int n) {
        return this.data[n];
    }

    public int hashCode() {
        int n = this.hashCode;
        if (n == 0) {
            this.hashCode = n = Arrays.hashCode(this.data);
        }
        return n;
    }

    public String hex() {
        byte[] byArray = this.data;
        char[] cArray = new char[byArray.length * 2];
        int n = 0;
        for (byte by : byArray) {
            int n2 = n + 1;
            char[] cArray2 = HEX_DIGITS;
            cArray[n] = cArray2[by >> 4 & 0xF];
            n = n2 + 1;
            cArray[n2] = cArray2[by & 0xF];
        }
        return new String(cArray);
    }

    public final int indexOf(ByteString byteString) {
        return this.indexOf(byteString.internalArray(), 0);
    }

    public final int indexOf(ByteString byteString, int n) {
        return this.indexOf(byteString.internalArray(), n);
    }

    public final int indexOf(byte[] byArray) {
        return this.indexOf(byArray, 0);
    }

    public int indexOf(byte[] byArray, int n) {
        int n2 = this.data.length;
        int n3 = byArray.length;
        for (n = Math.max(n, 0); n <= n2 - n3; ++n) {
            if (!Util.arrayRangeEquals(this.data, n, byArray, 0, byArray.length)) continue;
            return n;
        }
        return -1;
    }

    byte[] internalArray() {
        return this.data;
    }

    public final int lastIndexOf(ByteString byteString) {
        return this.lastIndexOf(byteString.internalArray(), this.size());
    }

    public final int lastIndexOf(ByteString byteString, int n) {
        return this.lastIndexOf(byteString.internalArray(), n);
    }

    public final int lastIndexOf(byte[] byArray) {
        return this.lastIndexOf(byArray, this.size());
    }

    public int lastIndexOf(byte[] byArray, int n) {
        for (n = Math.min(n, this.data.length - byArray.length); n >= 0; --n) {
            if (!Util.arrayRangeEquals(this.data, n, byArray, 0, byArray.length)) continue;
            return n;
        }
        return -1;
    }

    public ByteString md5() {
        return this.digest("MD5");
    }

    public boolean rangeEquals(int n, ByteString byteString, int n2, int n3) {
        return byteString.rangeEquals(n2, this.data, n, n3);
    }

    public boolean rangeEquals(int n, byte[] byArray, int n2, int n3) {
        byte[] byArray2;
        boolean bl = n >= 0 && n <= (byArray2 = this.data).length - n3 && n2 >= 0 && n2 <= byArray.length - n3 && Util.arrayRangeEquals(byArray2, n, byArray, n2, n3);
        return bl;
    }

    public ByteString sha1() {
        return this.digest("SHA-1");
    }

    public ByteString sha256() {
        return this.digest("SHA-256");
    }

    public int size() {
        return this.data.length;
    }

    public final boolean startsWith(ByteString byteString) {
        return this.rangeEquals(0, byteString, 0, byteString.size());
    }

    public final boolean startsWith(byte[] byArray) {
        return this.rangeEquals(0, byArray, 0, byArray.length);
    }

    public ByteString substring(int n) {
        return this.substring(n, this.data.length);
    }

    public ByteString substring(int n, int n2) {
        if (n >= 0) {
            byte[] byArray = this.data;
            if (n2 <= byArray.length) {
                int n3 = n2 - n;
                if (n3 >= 0) {
                    if (n == 0 && n2 == byArray.length) {
                        return this;
                    }
                    byte[] byArray2 = new byte[n3];
                    System.arraycopy(byArray, n, byArray2, 0, n3);
                    return new ByteString(byArray2);
                }
                throw new IllegalArgumentException("endIndex < beginIndex");
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("endIndex > length(");
            stringBuilder.append(this.data.length);
            stringBuilder.append(")");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        throw new IllegalArgumentException("beginIndex < 0");
    }

    public ByteString toAsciiLowercase() {
        byte[] byArray;
        for (int i = 0; i < (byArray = this.data).length; ++i) {
            byte by = byArray[i];
            if (by < 65 || by > 90) continue;
            byArray = (byte[])byArray.clone();
            int n = i + 1;
            byArray[i] = (byte)(by + 32);
            for (i = n; i < byArray.length; ++i) {
                n = byArray[i];
                if (n < 65 || n > 90) continue;
                byArray[i] = (byte)(n + 32);
            }
            return new ByteString(byArray);
        }
        return this;
    }

    public ByteString toAsciiUppercase() {
        byte[] byArray;
        for (int i = 0; i < (byArray = this.data).length; ++i) {
            byte by = byArray[i];
            if (by < 97 || by > 122) continue;
            byArray = (byte[])byArray.clone();
            int n = i + 1;
            byArray[i] = (byte)(by - 32);
            for (i = n; i < byArray.length; ++i) {
                n = byArray[i];
                if (n < 97 || n > 122) continue;
                byArray[i] = (byte)(n - 32);
            }
            return new ByteString(byArray);
        }
        return this;
    }

    public byte[] toByteArray() {
        return (byte[])this.data.clone();
    }

    public String toString() {
        if (this.data.length == 0) {
            return "[size=0]";
        }
        CharSequence charSequence = this.utf8();
        int n = ByteString.codePointIndexToCharIndex((String)charSequence, 64);
        if (n == -1) {
            if (this.data.length <= 64) {
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("[hex=");
                ((StringBuilder)charSequence).append(this.hex());
                ((StringBuilder)charSequence).append("]");
                charSequence = ((StringBuilder)charSequence).toString();
            } else {
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("[size=");
                ((StringBuilder)charSequence).append(this.data.length);
                ((StringBuilder)charSequence).append(" hex=");
                ((StringBuilder)charSequence).append(this.substring(0, 64).hex());
                ((StringBuilder)charSequence).append("\u2026]");
                charSequence = ((StringBuilder)charSequence).toString();
            }
            return charSequence;
        }
        String string2 = ((String)charSequence).substring(0, n).replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r");
        if (n < ((String)charSequence).length()) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("[size=");
            ((StringBuilder)charSequence).append(this.data.length);
            ((StringBuilder)charSequence).append(" text=");
            ((StringBuilder)charSequence).append(string2);
            ((StringBuilder)charSequence).append("\u2026]");
        } else {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("[text=");
            ((StringBuilder)charSequence).append(string2);
            ((StringBuilder)charSequence).append("]");
        }
        return ((StringBuilder)charSequence).toString();
    }

    public String utf8() {
        String string2 = this.utf8;
        if (string2 == null) {
            this.utf8 = string2 = new String(this.data, Util.UTF_8);
        }
        return string2;
    }

    public void write(OutputStream outputStream) throws IOException {
        if (outputStream != null) {
            outputStream.write(this.data);
            return;
        }
        throw new IllegalArgumentException("out == null");
    }

    void write(Buffer buffer) {
        byte[] byArray = this.data;
        buffer.write(byArray, 0, byArray.length);
    }
}

