/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.exception;

import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import java.io.IOException;

public class ResumeFailedException
extends IOException {
    private final ResumeFailedCause resumeFailedCause;

    public ResumeFailedException(ResumeFailedCause resumeFailedCause) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Resume failed because of ");
        stringBuilder.append((Object)resumeFailedCause);
        super(stringBuilder.toString());
        this.resumeFailedCause = resumeFailedCause;
    }

    public ResumeFailedCause getResumeFailedCause() {
        return this.resumeFailedCause;
    }
}

