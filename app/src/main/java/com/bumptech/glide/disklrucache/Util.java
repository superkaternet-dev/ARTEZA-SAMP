/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.disklrucache;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;

final class Util {
    static final Charset US_ASCII = Charset.forName("US-ASCII");
    static final Charset UTF_8 = Charset.forName("UTF-8");

    private Util() {
    }

    static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (Exception exception) {
            }
            catch (RuntimeException runtimeException) {
                throw runtimeException;
            }
        }
    }

    static void deleteContents(File object) throws IOException {
        Object object2 = ((File)object).listFiles();
        if (object2 != null) {
            int n = ((File[])object2).length;
            for (int i = 0; i < n; ++i) {
                object = object2[i];
                if (((File)object).isDirectory()) {
                    Util.deleteContents((File)object);
                }
                if (((File)object).delete()) {
                    continue;
                }
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("failed to delete file: ");
                ((StringBuilder)object2).append(object);
                throw new IOException(((StringBuilder)object2).toString());
            }
            return;
        }
        object2 = new StringBuilder();
        ((StringBuilder)object2).append("not a readable directory: ");
        ((StringBuilder)object2).append(object);
        object = new IOException(((StringBuilder)object2).toString());
        throw object;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static String readFully(Reader reader) throws IOException {
        try {
            int n;
            Object object = new StringWriter();
            char[] cArray = new char[1024];
            while ((n = reader.read(cArray)) != -1) {
                ((StringWriter)object).write(cArray, 0, n);
            }
            object = ((StringWriter)object).toString();
            return object;
        }
        finally {
            reader.close();
        }
    }
}

