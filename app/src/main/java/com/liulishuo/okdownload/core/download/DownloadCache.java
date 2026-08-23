/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.download;

import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.exception.FileBusyAfterRunException;
import com.liulishuo.okdownload.core.exception.InterruptException;
import com.liulishuo.okdownload.core.exception.PreAllocateException;
import com.liulishuo.okdownload.core.exception.ResumeFailedException;
import com.liulishuo.okdownload.core.exception.ServerCanceledException;
import com.liulishuo.okdownload.core.file.MultiPointOutputStream;
import java.io.IOException;
import java.net.SocketException;

public class DownloadCache {
    private volatile boolean fileBusyAfterRun;
    private final MultiPointOutputStream outputStream;
    private volatile boolean preAllocateFailed;
    private volatile boolean preconditionFailed;
    private volatile IOException realCause;
    private String redirectLocation;
    private volatile boolean serverCanceled;
    private volatile boolean unknownError;
    private volatile boolean userCanceled;

    private DownloadCache() {
        this.outputStream = null;
    }

    DownloadCache(MultiPointOutputStream multiPointOutputStream) {
        this.outputStream = multiPointOutputStream;
    }

    public void catchException(IOException iOException) {
        if (this.isUserCanceled()) {
            return;
        }
        if (iOException instanceof ResumeFailedException) {
            this.setPreconditionFailed(iOException);
        } else if (iOException instanceof ServerCanceledException) {
            this.setServerCanceled(iOException);
        } else if (iOException == FileBusyAfterRunException.SIGNAL) {
            this.setFileBusyAfterRun();
        } else if (iOException instanceof PreAllocateException) {
            this.setPreAllocateFailed(iOException);
        } else if (iOException != InterruptException.SIGNAL) {
            this.setUnknownError(iOException);
            if (!(iOException instanceof SocketException)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("catch unknown error ");
                stringBuilder.append(iOException);
                Util.d("DownloadCache", stringBuilder.toString());
            }
        }
    }

    MultiPointOutputStream getOutputStream() {
        MultiPointOutputStream multiPointOutputStream = this.outputStream;
        if (multiPointOutputStream != null) {
            return multiPointOutputStream;
        }
        throw new IllegalArgumentException();
    }

    IOException getRealCause() {
        return this.realCause;
    }

    String getRedirectLocation() {
        return this.redirectLocation;
    }

    ResumeFailedCause getResumeFailedCause() {
        return ((ResumeFailedException)this.realCause).getResumeFailedCause();
    }

    boolean isFileBusyAfterRun() {
        return this.fileBusyAfterRun;
    }

    public boolean isInterrupt() {
        boolean bl = this.preconditionFailed || this.userCanceled || this.serverCanceled || this.unknownError || this.fileBusyAfterRun || this.preAllocateFailed;
        return bl;
    }

    public boolean isPreAllocateFailed() {
        return this.preAllocateFailed;
    }

    boolean isPreconditionFailed() {
        return this.preconditionFailed;
    }

    boolean isServerCanceled() {
        return this.serverCanceled;
    }

    boolean isUnknownError() {
        return this.unknownError;
    }

    public boolean isUserCanceled() {
        return this.userCanceled;
    }

    public void setFileBusyAfterRun() {
        this.fileBusyAfterRun = true;
    }

    public void setPreAllocateFailed(IOException iOException) {
        this.preAllocateFailed = true;
        this.realCause = iOException;
    }

    public void setPreconditionFailed(IOException iOException) {
        this.preconditionFailed = true;
        this.realCause = iOException;
    }

    void setRedirectLocation(String string2) {
        this.redirectLocation = string2;
    }

    public void setServerCanceled(IOException iOException) {
        this.serverCanceled = true;
        this.realCause = iOException;
    }

    public void setUnknownError(IOException iOException) {
        this.unknownError = true;
        this.realCause = iOException;
    }

    void setUserCanceled() {
        this.userCanceled = true;
    }

    static class PreError
    extends DownloadCache {
        PreError(IOException iOException) {
            super(null);
            this.setUnknownError(iOException);
        }
    }
}

