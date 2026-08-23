/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package androidx.core.util;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AtomicFile {
    private final File mBackupName;
    private final File mBaseName;

    public AtomicFile(File file) {
        this.mBaseName = file;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(file.getPath());
        stringBuilder.append(".bak");
        this.mBackupName = new File(stringBuilder.toString());
    }

    private static boolean sync(FileOutputStream fileOutputStream) {
        try {
            fileOutputStream.getFD().sync();
            return true;
        }
        catch (IOException iOException) {
            return false;
        }
    }

    public void delete() {
        this.mBaseName.delete();
        this.mBackupName.delete();
    }

    public void failWrite(FileOutputStream fileOutputStream) {
        if (fileOutputStream != null) {
            AtomicFile.sync(fileOutputStream);
            try {
                fileOutputStream.close();
                this.mBaseName.delete();
                this.mBackupName.renameTo(this.mBaseName);
            }
            catch (IOException iOException) {
                Log.w((String)"AtomicFile", (String)"failWrite: Got exception:", (Throwable)iOException);
            }
        }
    }

    public void finishWrite(FileOutputStream fileOutputStream) {
        if (fileOutputStream != null) {
            AtomicFile.sync(fileOutputStream);
            try {
                fileOutputStream.close();
                this.mBackupName.delete();
            }
            catch (IOException iOException) {
                Log.w((String)"AtomicFile", (String)"finishWrite: Got exception:", (Throwable)iOException);
            }
        }
    }

    public File getBaseFile() {
        return this.mBaseName;
    }

    public FileInputStream openRead() throws FileNotFoundException {
        if (this.mBackupName.exists()) {
            this.mBaseName.delete();
            this.mBackupName.renameTo(this.mBaseName);
        }
        return new FileInputStream(this.mBaseName);
    }

    public byte[] readFully() throws IOException {
        int n;
        byte[] byArray;
        FileInputStream fileInputStream = this.openRead();
        int n2 = 0;
        try {
            byArray = new byte[fileInputStream.available()];
            while (true) {
                if ((n = fileInputStream.read(byArray, n2, byArray.length - n2)) > 0) break block6;
                break;
            }
        }
        catch (Throwable throwable) {
            fileInputStream.close();
            throw throwable;
        }
        {
            block6: {
                fileInputStream.close();
                return byArray;
            }
            n2 += n;
            n = fileInputStream.available();
            byte[] byArray2 = byArray;
            if (n > byArray.length - n2) {
                byArray2 = new byte[n2 + n];
                System.arraycopy(byArray, 0, byArray2, 0, n2);
            }
            byArray = byArray2;
            continue;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public FileOutputStream startWrite() throws IOException {
        block8: {
            if (this.mBaseName.exists()) {
                if (!this.mBackupName.exists()) {
                    if (!this.mBaseName.renameTo(this.mBackupName)) {
                        StringBuilder object = new StringBuilder();
                        object.append("Couldn't rename file ");
                        object.append(this.mBaseName);
                        object.append(" to backup file ");
                        object.append(this.mBackupName);
                        Log.w((String)"AtomicFile", (String)object.toString());
                    }
                } else {
                    this.mBaseName.delete();
                }
            }
            try {
                return new FileOutputStream(this.mBaseName);
            }
            catch (FileNotFoundException fileNotFoundException) {
                if (!this.mBaseName.getParentFile().mkdirs()) break block8;
                try {
                    return new FileOutputStream(this.mBaseName);
                }
                catch (FileNotFoundException fileNotFoundException2) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Couldn't create ");
                    stringBuilder.append(this.mBaseName);
                    throw new IOException(stringBuilder.toString());
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Couldn't create directory ");
        stringBuilder.append(this.mBaseName);
        throw new IOException(stringBuilder.toString());
    }
}

