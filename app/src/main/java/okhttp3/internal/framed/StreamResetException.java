/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.framed;

import java.io.IOException;
import okhttp3.internal.framed.ErrorCode;

public final class StreamResetException
extends IOException {
    public final ErrorCode errorCode;

    public StreamResetException(ErrorCode errorCode) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("stream was reset: ");
        stringBuilder.append((Object)errorCode);
        super(stringBuilder.toString());
        this.errorCode = errorCode;
    }
}

