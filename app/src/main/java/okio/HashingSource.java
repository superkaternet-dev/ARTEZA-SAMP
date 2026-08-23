/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import okio.Buffer;
import okio.ByteString;
import okio.ForwardingSource;
import okio.Segment;
import okio.Source;

public final class HashingSource
extends ForwardingSource {
    private final MessageDigest messageDigest;

    private HashingSource(Source source, String string2) {
        super(source);
        try {
            this.messageDigest = MessageDigest.getInstance(string2);
            return;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new AssertionError();
        }
    }

    public static HashingSource md5(Source source) {
        return new HashingSource(source, "MD5");
    }

    public static HashingSource sha1(Source source) {
        return new HashingSource(source, "SHA-1");
    }

    public static HashingSource sha256(Source source) {
        return new HashingSource(source, "SHA-256");
    }

    public ByteString hash() {
        return ByteString.of(this.messageDigest.digest());
    }

    @Override
    public long read(Buffer buffer, long l) throws IOException {
        long l2 = super.read(buffer, l);
        if (l2 != -1L) {
            long l3;
            long l4;
            long l5 = buffer.size - l2;
            l = buffer.size;
            Segment segment = buffer.head;
            while (true) {
                l4 = l5;
                l3 = l;
                if (l <= buffer.size - l2) break;
                segment = segment.prev;
                l -= (long)(segment.limit - segment.pos);
            }
            while (l3 < buffer.size) {
                int n = (int)((long)segment.pos + l4 - l3);
                this.messageDigest.update(segment.data, n, segment.limit - n);
                l4 = l3 += (long)(segment.limit - segment.pos);
            }
        }
        return l2;
    }
}

