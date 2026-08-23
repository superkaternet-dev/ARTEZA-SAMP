/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import okio.Buffer;
import okio.ByteString;
import okio.ForwardingSink;
import okio.Segment;
import okio.Sink;
import okio.Util;

public final class HashingSink
extends ForwardingSink {
    private final MessageDigest messageDigest;

    private HashingSink(Sink sink, String string2) {
        super(sink);
        try {
            this.messageDigest = MessageDigest.getInstance(string2);
            return;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new AssertionError();
        }
    }

    public static HashingSink md5(Sink sink) {
        return new HashingSink(sink, "MD5");
    }

    public static HashingSink sha1(Sink sink) {
        return new HashingSink(sink, "SHA-1");
    }

    public static HashingSink sha256(Sink sink) {
        return new HashingSink(sink, "SHA-256");
    }

    public ByteString hash() {
        return ByteString.of(this.messageDigest.digest());
    }

    @Override
    public void write(Buffer buffer, long l) throws IOException {
        int n;
        Util.checkOffsetAndCount(buffer.size, 0L, l);
        Segment segment = buffer.head;
        for (long i = 0L; i < l; i += (long)n) {
            n = (int)Math.min(l - i, (long)(segment.limit - segment.pos));
            this.messageDigest.update(segment.data, segment.pos, n);
            segment = segment.next;
        }
        super.write(buffer, l);
    }
}

