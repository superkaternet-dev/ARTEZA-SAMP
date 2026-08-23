/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core;

import java.io.File;

public abstract class IdentifiedTask {
    public static final File EMPTY_FILE = new File("");
    public static final String EMPTY_URL = "";

    public boolean compareIgnoreId(IdentifiedTask object) {
        boolean bl = this.getUrl().equals(((IdentifiedTask)object).getUrl());
        boolean bl2 = false;
        if (!bl) {
            return false;
        }
        if (!this.getUrl().equals(EMPTY_URL) && !this.getParentFile().equals(EMPTY_FILE)) {
            if (this.getProvidedPathFile().equals(((IdentifiedTask)object).getProvidedPathFile())) {
                return true;
            }
            if (!this.getParentFile().equals(((IdentifiedTask)object).getParentFile())) {
                return false;
            }
            String string2 = this.getFilename();
            object = ((IdentifiedTask)object).getFilename();
            bl = bl2;
            if (object != null) {
                bl = bl2;
                if (string2 != null) {
                    bl = bl2;
                    if (((String)object).equals(string2)) {
                        bl = true;
                    }
                }
            }
            return bl;
        }
        return false;
    }

    public abstract String getFilename();

    public abstract int getId();

    public abstract File getParentFile();

    protected abstract File getProvidedPathFile();

    public abstract String getUrl();
}

