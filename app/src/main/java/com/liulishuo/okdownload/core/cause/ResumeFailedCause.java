/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.cause;

public final class ResumeFailedCause
extends Enum<ResumeFailedCause> {
    private static final ResumeFailedCause[] $VALUES;
    public static final /* enum */ ResumeFailedCause CONTENT_LENGTH_CHANGED;
    public static final /* enum */ ResumeFailedCause FILE_NOT_EXIST;
    public static final /* enum */ ResumeFailedCause INFO_DIRTY;
    public static final /* enum */ ResumeFailedCause OUTPUT_STREAM_NOT_SUPPORT;
    public static final /* enum */ ResumeFailedCause RESPONSE_CREATED_RANGE_NOT_FROM_0;
    public static final /* enum */ ResumeFailedCause RESPONSE_ETAG_CHANGED;
    public static final /* enum */ ResumeFailedCause RESPONSE_PRECONDITION_FAILED;
    public static final /* enum */ ResumeFailedCause RESPONSE_RESET_RANGE_NOT_FROM_0;

    static {
        ResumeFailedCause resumeFailedCause;
        ResumeFailedCause resumeFailedCause2;
        ResumeFailedCause resumeFailedCause3;
        ResumeFailedCause resumeFailedCause4;
        ResumeFailedCause resumeFailedCause5;
        ResumeFailedCause resumeFailedCause6;
        ResumeFailedCause resumeFailedCause7;
        ResumeFailedCause resumeFailedCause8;
        INFO_DIRTY = resumeFailedCause8 = new ResumeFailedCause();
        FILE_NOT_EXIST = resumeFailedCause7 = new ResumeFailedCause();
        OUTPUT_STREAM_NOT_SUPPORT = resumeFailedCause6 = new ResumeFailedCause();
        RESPONSE_ETAG_CHANGED = resumeFailedCause5 = new ResumeFailedCause();
        RESPONSE_PRECONDITION_FAILED = resumeFailedCause4 = new ResumeFailedCause();
        RESPONSE_CREATED_RANGE_NOT_FROM_0 = resumeFailedCause3 = new ResumeFailedCause();
        RESPONSE_RESET_RANGE_NOT_FROM_0 = resumeFailedCause2 = new ResumeFailedCause();
        CONTENT_LENGTH_CHANGED = resumeFailedCause = new ResumeFailedCause();
        $VALUES = new ResumeFailedCause[]{resumeFailedCause8, resumeFailedCause7, resumeFailedCause6, resumeFailedCause5, resumeFailedCause4, resumeFailedCause3, resumeFailedCause2, resumeFailedCause};
    }

    public static ResumeFailedCause valueOf(String string2) {
        return Enum.valueOf(ResumeFailedCause.class, string2);
    }

    public static ResumeFailedCause[] values() {
        return (ResumeFailedCause[])$VALUES.clone();
    }
}

