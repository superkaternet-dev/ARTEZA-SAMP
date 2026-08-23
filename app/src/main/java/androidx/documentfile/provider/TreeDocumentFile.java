/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.net.Uri
 *  android.provider.DocumentsContract
 *  android.util.Log
 */
package androidx.documentfile.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.util.Log;
import androidx.documentfile.provider.DocumentFile;
import androidx.documentfile.provider.DocumentsContractApi19;
import java.util.ArrayList;

class TreeDocumentFile
extends DocumentFile {
    private Context mContext;
    private Uri mUri;

    TreeDocumentFile(DocumentFile documentFile, Context context, Uri uri) {
        super(documentFile);
        this.mContext = context;
        this.mUri = uri;
    }

    private static void closeQuietly(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            }
            catch (Exception exception) {
            }
            catch (RuntimeException runtimeException) {
                throw runtimeException;
            }
        }
    }

    private static Uri createFile(Context context, Uri uri, String string2, String string3) {
        try {
            context = DocumentsContract.createDocument((ContentResolver)context.getContentResolver(), (Uri)uri, (String)string2, (String)string3);
            return context;
        }
        catch (Exception exception) {
            return null;
        }
    }

    @Override
    public boolean canRead() {
        return DocumentsContractApi19.canRead(this.mContext, this.mUri);
    }

    @Override
    public boolean canWrite() {
        return DocumentsContractApi19.canWrite(this.mContext, this.mUri);
    }

    @Override
    public DocumentFile createDirectory(String object) {
        object = (object = TreeDocumentFile.createFile(this.mContext, this.mUri, "vnd.android.document/directory", (String)object)) != null ? new TreeDocumentFile(this, this.mContext, (Uri)object) : null;
        return object;
    }

    @Override
    public DocumentFile createFile(String object, String string2) {
        object = (object = TreeDocumentFile.createFile(this.mContext, this.mUri, (String)object, string2)) != null ? new TreeDocumentFile(this, this.mContext, (Uri)object) : null;
        return object;
    }

    @Override
    public boolean delete() {
        try {
            boolean bl = DocumentsContract.deleteDocument((ContentResolver)this.mContext.getContentResolver(), (Uri)this.mUri);
            return bl;
        }
        catch (Exception exception) {
            return false;
        }
    }

    @Override
    public boolean exists() {
        return DocumentsContractApi19.exists(this.mContext, this.mUri);
    }

    @Override
    public String getName() {
        return DocumentsContractApi19.getName(this.mContext, this.mUri);
    }

    @Override
    public String getType() {
        return DocumentsContractApi19.getType(this.mContext, this.mUri);
    }

    @Override
    public Uri getUri() {
        return this.mUri;
    }

    @Override
    public boolean isDirectory() {
        return DocumentsContractApi19.isDirectory(this.mContext, this.mUri);
    }

    @Override
    public boolean isFile() {
        return DocumentsContractApi19.isFile(this.mContext, this.mUri);
    }

    @Override
    public boolean isVirtual() {
        return DocumentsContractApi19.isVirtual(this.mContext, this.mUri);
    }

    @Override
    public long lastModified() {
        return DocumentsContractApi19.lastModified(this.mContext, this.mUri);
    }

    @Override
    public long length() {
        return DocumentsContractApi19.length(this.mContext, this.mUri);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public DocumentFile[] listFiles() {
        Throwable throwable2;
        DocumentFile[] documentFileArray;
        block6: {
            Object object = this.mContext.getContentResolver();
            Uri[] uriArray = this.mUri;
            Object object2 = DocumentsContract.buildChildDocumentsUriUsingTree((Uri)uriArray, (String)DocumentsContract.getDocumentId((Uri)uriArray));
            ArrayList<Uri> arrayList = new ArrayList<Uri>();
            uriArray = null;
            documentFileArray = null;
            object = object.query(object2, new String[]{"document_id"}, null, null, null);
            while (true) {
                documentFileArray = object;
                uriArray = object;
                if (!object.moveToNext()) break;
                documentFileArray = object;
                uriArray = object;
                object2 = object.getString(0);
                documentFileArray = object;
                uriArray = object;
                arrayList.add(DocumentsContract.buildDocumentUriUsingTree((Uri)this.mUri, (String)object2));
            }
            uriArray = object;
            {
                catch (Throwable throwable2) {
                    break block6;
                }
                catch (Exception exception) {}
                documentFileArray = uriArray;
                {
                    documentFileArray = uriArray;
                    object = new StringBuilder();
                    documentFileArray = uriArray;
                    ((StringBuilder)object).append("Failed query: ");
                    documentFileArray = uriArray;
                    ((StringBuilder)object).append(exception);
                    documentFileArray = uriArray;
                    Log.w((String)"DocumentFile", (String)((StringBuilder)object).toString());
                }
            }
            TreeDocumentFile.closeQuietly((AutoCloseable)uriArray);
            uriArray = arrayList.toArray(new Uri[arrayList.size()]);
            documentFileArray = new DocumentFile[uriArray.length];
            int n = 0;
            while (n < uriArray.length) {
                documentFileArray[n] = new TreeDocumentFile(this, this.mContext, uriArray[n]);
                ++n;
            }
            return documentFileArray;
        }
        TreeDocumentFile.closeQuietly((AutoCloseable)documentFileArray);
        throw throwable2;
    }

    @Override
    public boolean renameTo(String string2) {
        block3: {
            try {
                string2 = DocumentsContract.renameDocument((ContentResolver)this.mContext.getContentResolver(), (Uri)this.mUri, (String)string2);
                if (string2 == null) break block3;
            }
            catch (Exception exception) {
                return false;
            }
            this.mUri = string2;
            return true;
        }
        return false;
    }
}

