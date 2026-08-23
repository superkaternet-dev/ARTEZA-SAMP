/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 *  android.util.Log
 *  android.webkit.MimeTypeMap
 */
package androidx.documentfile.provider;

import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import androidx.documentfile.provider.DocumentFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class RawDocumentFile
extends DocumentFile {
    private File mFile;

    RawDocumentFile(DocumentFile documentFile, File file) {
        super(documentFile);
        this.mFile = file;
    }

    private static boolean deleteContents(File comparable) {
        File[] fileArray = ((File)comparable).listFiles();
        boolean bl = true;
        boolean bl2 = true;
        if (fileArray != null) {
            int n = fileArray.length;
            int n2 = 0;
            while (true) {
                bl = bl2;
                if (n2 >= n) break;
                File file = fileArray[n2];
                bl = bl2;
                if (file.isDirectory()) {
                    bl = bl2 & RawDocumentFile.deleteContents(file);
                }
                bl2 = bl;
                if (!file.delete()) {
                    comparable = new StringBuilder();
                    ((StringBuilder)comparable).append("Failed to delete ");
                    ((StringBuilder)comparable).append(file);
                    Log.w((String)"DocumentFile", (String)((StringBuilder)comparable).toString());
                    bl2 = false;
                }
                ++n2;
            }
        }
        return bl;
    }

    private static String getTypeForName(String string2) {
        int n = string2.lastIndexOf(46);
        if (n >= 0) {
            string2 = string2.substring(n + 1).toLowerCase();
            string2 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(string2);
            if (string2 != null) {
                return string2;
            }
        }
        return "application/octet-stream";
    }

    @Override
    public boolean canRead() {
        return this.mFile.canRead();
    }

    @Override
    public boolean canWrite() {
        return this.mFile.canWrite();
    }

    @Override
    public DocumentFile createDirectory(String object) {
        if (!((File)(object = new File(this.mFile, (String)object))).isDirectory() && !((File)object).mkdir()) {
            return null;
        }
        return new RawDocumentFile(this, (File)object);
    }

    @Override
    public DocumentFile createFile(String object, String charSequence) {
        String string2 = MimeTypeMap.getSingleton().getExtensionFromMimeType((String)object);
        object = charSequence;
        if (string2 != null) {
            object = new StringBuilder();
            ((StringBuilder)object).append((String)charSequence);
            ((StringBuilder)object).append(".");
            ((StringBuilder)object).append(string2);
            object = ((StringBuilder)object).toString();
        }
        object = new File(this.mFile, (String)object);
        try {
            ((File)object).createNewFile();
            object = new RawDocumentFile(this, (File)object);
            return object;
        }
        catch (IOException iOException) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("Failed to createFile: ");
            ((StringBuilder)charSequence).append(iOException);
            Log.w((String)"DocumentFile", (String)((StringBuilder)charSequence).toString());
            return null;
        }
    }

    @Override
    public boolean delete() {
        RawDocumentFile.deleteContents(this.mFile);
        return this.mFile.delete();
    }

    @Override
    public boolean exists() {
        return this.mFile.exists();
    }

    @Override
    public String getName() {
        return this.mFile.getName();
    }

    @Override
    public String getType() {
        if (this.mFile.isDirectory()) {
            return null;
        }
        return RawDocumentFile.getTypeForName(this.mFile.getName());
    }

    @Override
    public Uri getUri() {
        return Uri.fromFile((File)this.mFile);
    }

    @Override
    public boolean isDirectory() {
        return this.mFile.isDirectory();
    }

    @Override
    public boolean isFile() {
        return this.mFile.isFile();
    }

    @Override
    public boolean isVirtual() {
        return false;
    }

    @Override
    public long lastModified() {
        return this.mFile.lastModified();
    }

    @Override
    public long length() {
        return this.mFile.length();
    }

    @Override
    public DocumentFile[] listFiles() {
        ArrayList<RawDocumentFile> arrayList = new ArrayList<RawDocumentFile>();
        File[] fileArray = this.mFile.listFiles();
        if (fileArray != null) {
            int n = fileArray.length;
            for (int i = 0; i < n; ++i) {
                arrayList.add(new RawDocumentFile(this, fileArray[i]));
            }
        }
        return arrayList.toArray(new DocumentFile[arrayList.size()]);
    }

    @Override
    public boolean renameTo(String object) {
        object = new File(this.mFile.getParentFile(), (String)object);
        if (this.mFile.renameTo((File)object)) {
            this.mFile = object;
            return true;
        }
        return false;
    }
}

