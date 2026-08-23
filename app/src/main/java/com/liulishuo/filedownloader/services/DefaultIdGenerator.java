/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.services;

import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

public class DefaultIdGenerator
implements FileDownloadHelper.IdGenerator {
    @Override
    public int generateId(String string2, String string3, boolean bl) {
        if (bl) {
            return FileDownloadUtils.md5(FileDownloadUtils.formatString("%sp%s@dir", string2, string3)).hashCode();
        }
        return FileDownloadUtils.md5(FileDownloadUtils.formatString("%sp%s", string2, string3)).hashCode();
    }

    @Override
    public int transOldId(int n, String string2, String string3, boolean bl) {
        return this.generateId(string2, string3, bl);
    }
}

