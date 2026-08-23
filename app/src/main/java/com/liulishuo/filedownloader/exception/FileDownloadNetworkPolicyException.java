/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.exception;

import com.liulishuo.filedownloader.exception.FileDownloadGiveUpRetryException;

public class FileDownloadNetworkPolicyException
extends FileDownloadGiveUpRetryException {
    public FileDownloadNetworkPolicyException() {
        super("Only allows downloading this task on the wifi network type");
    }
}

