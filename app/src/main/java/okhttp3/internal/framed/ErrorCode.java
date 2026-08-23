/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.framed;

public final class ErrorCode
extends Enum<ErrorCode> {
    private static final ErrorCode[] $VALUES;
    public static final /* enum */ ErrorCode CANCEL;
    public static final /* enum */ ErrorCode COMPRESSION_ERROR;
    public static final /* enum */ ErrorCode CONNECT_ERROR;
    public static final /* enum */ ErrorCode ENHANCE_YOUR_CALM;
    public static final /* enum */ ErrorCode FLOW_CONTROL_ERROR;
    public static final /* enum */ ErrorCode FRAME_TOO_LARGE;
    public static final /* enum */ ErrorCode HTTP_1_1_REQUIRED;
    public static final /* enum */ ErrorCode INADEQUATE_SECURITY;
    public static final /* enum */ ErrorCode INTERNAL_ERROR;
    public static final /* enum */ ErrorCode INVALID_CREDENTIALS;
    public static final /* enum */ ErrorCode INVALID_STREAM;
    public static final /* enum */ ErrorCode NO_ERROR;
    public static final /* enum */ ErrorCode PROTOCOL_ERROR;
    public static final /* enum */ ErrorCode REFUSED_STREAM;
    public static final /* enum */ ErrorCode STREAM_ALREADY_CLOSED;
    public static final /* enum */ ErrorCode STREAM_CLOSED;
    public static final /* enum */ ErrorCode STREAM_IN_USE;
    public static final /* enum */ ErrorCode UNSUPPORTED_VERSION;
    public final int httpCode;
    public final int spdyGoAwayCode;
    public final int spdyRstCode;

    static {
        ErrorCode errorCode;
        ErrorCode errorCode2;
        ErrorCode errorCode3;
        ErrorCode errorCode4;
        ErrorCode errorCode5;
        ErrorCode errorCode6;
        ErrorCode errorCode7;
        ErrorCode errorCode8;
        ErrorCode errorCode9;
        ErrorCode errorCode10;
        ErrorCode errorCode11;
        ErrorCode errorCode12;
        ErrorCode errorCode13;
        ErrorCode errorCode14;
        ErrorCode errorCode15;
        ErrorCode errorCode16;
        ErrorCode errorCode17;
        ErrorCode errorCode18;
        NO_ERROR = errorCode18 = new ErrorCode(0, -1, 0);
        PROTOCOL_ERROR = errorCode17 = new ErrorCode(1, 1, 1);
        INVALID_STREAM = errorCode16 = new ErrorCode(1, 2, -1);
        UNSUPPORTED_VERSION = errorCode15 = new ErrorCode(1, 4, -1);
        STREAM_IN_USE = errorCode14 = new ErrorCode(1, 8, -1);
        STREAM_ALREADY_CLOSED = errorCode13 = new ErrorCode(1, 9, -1);
        INTERNAL_ERROR = errorCode12 = new ErrorCode(2, 6, 2);
        FLOW_CONTROL_ERROR = errorCode11 = new ErrorCode(3, 7, -1);
        STREAM_CLOSED = errorCode10 = new ErrorCode(5, -1, -1);
        FRAME_TOO_LARGE = errorCode9 = new ErrorCode(6, 11, -1);
        REFUSED_STREAM = errorCode8 = new ErrorCode(7, 3, -1);
        CANCEL = errorCode7 = new ErrorCode(8, 5, -1);
        COMPRESSION_ERROR = errorCode6 = new ErrorCode(9, -1, -1);
        CONNECT_ERROR = errorCode5 = new ErrorCode(10, -1, -1);
        ENHANCE_YOUR_CALM = errorCode4 = new ErrorCode(11, -1, -1);
        INADEQUATE_SECURITY = errorCode3 = new ErrorCode(12, -1, -1);
        HTTP_1_1_REQUIRED = errorCode2 = new ErrorCode(13, -1, -1);
        INVALID_CREDENTIALS = errorCode = new ErrorCode(-1, 10, -1);
        $VALUES = new ErrorCode[]{errorCode18, errorCode17, errorCode16, errorCode15, errorCode14, errorCode13, errorCode12, errorCode11, errorCode10, errorCode9, errorCode8, errorCode7, errorCode6, errorCode5, errorCode4, errorCode3, errorCode2, errorCode};
    }

    private ErrorCode(int n2, int n3, int n4) {
        this.httpCode = n2;
        this.spdyRstCode = n3;
        this.spdyGoAwayCode = n4;
    }

    public static ErrorCode fromHttp2(int n) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.httpCode != n) continue;
            return errorCode;
        }
        return null;
    }

    public static ErrorCode fromSpdy3Rst(int n) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.spdyRstCode != n) continue;
            return errorCode;
        }
        return null;
    }

    public static ErrorCode fromSpdyGoAway(int n) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.spdyGoAwayCode != n) continue;
            return errorCode;
        }
        return null;
    }

    public static ErrorCode valueOf(String string2) {
        return Enum.valueOf(ErrorCode.class, string2);
    }

    public static ErrorCode[] values() {
        return (ErrorCode[])$VALUES.clone();
    }
}

