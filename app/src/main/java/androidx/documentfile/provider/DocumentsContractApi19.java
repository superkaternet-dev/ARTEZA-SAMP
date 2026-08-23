/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.net.Uri
 *  android.provider.DocumentsContract
 *  android.text.TextUtils
 *  android.util.Log
 */
package androidx.documentfile.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.util.Log;

class DocumentsContractApi19 {
    private static final int FLAG_VIRTUAL_DOCUMENT = 512;
    private static final String TAG = "DocumentFile";

    private DocumentsContractApi19() {
    }

    public static boolean canRead(Context context, Uri uri) {
        if (context.checkCallingOrSelfUriPermission(uri, 1) != 0) {
            return false;
        }
        return !TextUtils.isEmpty((CharSequence)DocumentsContractApi19.getRawType(context, uri));
    }

    public static boolean canWrite(Context context, Uri uri) {
        if (context.checkCallingOrSelfUriPermission(uri, 2) != 0) {
            return false;
        }
        String string2 = DocumentsContractApi19.getRawType(context, uri);
        int n = DocumentsContractApi19.queryForInt(context, uri, "flags", 0);
        if (TextUtils.isEmpty((CharSequence)string2)) {
            return false;
        }
        if ((n & 4) != 0) {
            return true;
        }
        if ("vnd.android.document/directory".equals(string2) && (n & 8) != 0) {
            return true;
        }
        return !TextUtils.isEmpty((CharSequence)string2) && (n & 2) != 0;
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

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static boolean exists(Context object, Uri object2) {
        Throwable throwable2222222;
        block5: {
            ContentResolver contentResolver = object.getContentResolver();
            Object object3 = null;
            object = null;
            boolean bl = true;
            object2 = contentResolver.query((Uri)object2, new String[]{"document_id"}, null, null, null);
            object = object2;
            object3 = object2;
            int n = object2.getCount();
            if (n <= 0) {
                bl = false;
            }
            {
                catch (Throwable throwable2222222) {
                    break block5;
                }
                catch (Exception exception) {}
                object = object3;
                {
                    object = object3;
                    object2 = new StringBuilder();
                    object = object3;
                    ((StringBuilder)object2).append("Failed query: ");
                    object = object3;
                    ((StringBuilder)object2).append(exception);
                    object = object3;
                    Log.w((String)TAG, (String)((StringBuilder)object2).toString());
                }
                DocumentsContractApi19.closeQuietly((AutoCloseable)object3);
                return false;
            }
            DocumentsContractApi19.closeQuietly((AutoCloseable)object2);
            return bl;
        }
        DocumentsContractApi19.closeQuietly((AutoCloseable)object);
        throw throwable2222222;
    }

    public static long getFlags(Context context, Uri uri) {
        return DocumentsContractApi19.queryForLong(context, uri, "flags", 0L);
    }

    public static String getName(Context context, Uri uri) {
        return DocumentsContractApi19.queryForString(context, uri, "_display_name", null);
    }

    private static String getRawType(Context context, Uri uri) {
        return DocumentsContractApi19.queryForString(context, uri, "mime_type", null);
    }

    public static String getType(Context object, Uri uri) {
        if ("vnd.android.document/directory".equals(object = DocumentsContractApi19.getRawType(object, uri))) {
            return null;
        }
        return object;
    }

    public static boolean isDirectory(Context context, Uri uri) {
        return "vnd.android.document/directory".equals(DocumentsContractApi19.getRawType(context, uri));
    }

    public static boolean isFile(Context object, Uri uri) {
        return !"vnd.android.document/directory".equals(object = DocumentsContractApi19.getRawType(object, uri)) && !TextUtils.isEmpty((CharSequence)object);
        {
        }
    }

    public static boolean isVirtual(Context context, Uri uri) {
        boolean bl = DocumentsContract.isDocumentUri((Context)context, (Uri)uri);
        boolean bl2 = false;
        if (!bl) {
            return false;
        }
        if ((DocumentsContractApi19.getFlags(context, uri) & 0x200L) != 0L) {
            bl2 = true;
        }
        return bl2;
    }

    public static long lastModified(Context context, Uri uri) {
        return DocumentsContractApi19.queryForLong(context, uri, "last_modified", 0L);
    }

    public static long length(Context context, Uri uri) {
        return DocumentsContractApi19.queryForLong(context, uri, "_size", 0L);
    }

    private static int queryForInt(Context context, Uri uri, String string2, int n) {
        return (int)DocumentsContractApi19.queryForLong(context, uri, string2, n);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static long queryForLong(Context object, Uri object2, String string2, long l) {
        Throwable throwable2222222;
        block5: {
            block4: {
                ContentResolver contentResolver = object.getContentResolver();
                Object object3 = null;
                object = null;
                object2 = contentResolver.query((Uri)object2, new String[]{string2}, null, null, null);
                object = object2;
                object3 = object2;
                if (!object2.moveToFirst()) break block4;
                object = object2;
                object3 = object2;
                if (object2.isNull(0)) break block4;
                object = object2;
                object3 = object2;
                long l2 = object2.getLong(0);
                {
                    catch (Throwable throwable2222222) {
                        break block5;
                    }
                    catch (Exception exception) {}
                    object = object3;
                    {
                        object = object3;
                        object2 = new StringBuilder();
                        object = object3;
                        ((StringBuilder)object2).append("Failed query: ");
                        object = object3;
                        ((StringBuilder)object2).append(exception);
                        object = object3;
                        Log.w((String)TAG, (String)((StringBuilder)object2).toString());
                    }
                    DocumentsContractApi19.closeQuietly((AutoCloseable)object3);
                    return l;
                }
                DocumentsContractApi19.closeQuietly((AutoCloseable)object2);
                return l2;
            }
            DocumentsContractApi19.closeQuietly((AutoCloseable)object2);
            return l;
        }
        DocumentsContractApi19.closeQuietly((AutoCloseable)object);
        throw throwable2222222;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static String queryForString(Context object, Uri object2, String string2, String string3) {
        Throwable throwable2222222;
        block5: {
            block4: {
                ContentResolver contentResolver = object.getContentResolver();
                Object object3 = null;
                object = null;
                object2 = contentResolver.query((Uri)object2, new String[]{string2}, null, null, null);
                object = object2;
                object3 = object2;
                if (!object2.moveToFirst()) break block4;
                object = object2;
                object3 = object2;
                if (object2.isNull(0)) break block4;
                object = object2;
                object3 = object2;
                string2 = object2.getString(0);
                {
                    catch (Throwable throwable2222222) {
                        break block5;
                    }
                    catch (Exception exception) {}
                    object = object3;
                    {
                        object = object3;
                        object2 = new StringBuilder();
                        object = object3;
                        ((StringBuilder)object2).append("Failed query: ");
                        object = object3;
                        ((StringBuilder)object2).append(exception);
                        object = object3;
                        Log.w((String)TAG, (String)((StringBuilder)object2).toString());
                    }
                    DocumentsContractApi19.closeQuietly((AutoCloseable)object3);
                    return string3;
                }
                DocumentsContractApi19.closeQuietly((AutoCloseable)object2);
                return string2;
            }
            DocumentsContractApi19.closeQuietly((AutoCloseable)object2);
            return string3;
        }
        DocumentsContractApi19.closeQuietly((AutoCloseable)object);
        throw throwable2222222;
    }
}

