/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentProvider
 *  android.content.ContentValues
 *  android.content.Context
 *  android.content.pm.ProviderInfo
 *  android.content.res.XmlResourceParser
 *  android.database.Cursor
 *  android.database.MatrixCursor
 *  android.net.Uri
 *  android.net.Uri$Builder
 *  android.os.Build$VERSION
 *  android.os.Environment
 *  android.os.ParcelFileDescriptor
 *  android.text.TextUtils
 *  android.webkit.MimeTypeMap
 *  org.xmlpull.v1.XmlPullParserException
 */
package androidx.core.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider
extends ContentProvider {
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final String[] COLUMNS = new String[]{"_display_name", "_size"};
    private static final File DEVICE_ROOT = new File("/");
    private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String TAG_EXTERNAL_CACHE = "external-cache-path";
    private static final String TAG_EXTERNAL_FILES = "external-files-path";
    private static final String TAG_EXTERNAL_MEDIA = "external-media-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_ROOT_PATH = "root-path";
    private static HashMap<String, PathStrategy> sCache = new HashMap();
    private PathStrategy mStrategy;

    private static File buildPath(File file, String ... stringArray) {
        for (String string2 : stringArray) {
            File file2 = file;
            if (string2 != null) {
                file2 = new File(file, string2);
            }
            file = file2;
        }
        return file;
    }

    private static Object[] copyOf(Object[] objectArray, int n) {
        Object[] objectArray2 = new Object[n];
        System.arraycopy(objectArray, 0, objectArray2, 0, n);
        return objectArray2;
    }

    private static String[] copyOf(String[] stringArray, int n) {
        String[] stringArray2 = new String[n];
        System.arraycopy(stringArray, 0, stringArray2, 0, n);
        return stringArray2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static PathStrategy getPathStrategy(Context object, String object2) {
        HashMap<String, PathStrategy> hashMap = sCache;
        synchronized (hashMap) {
            PathStrategy pathStrategy;
            PathStrategy pathStrategy2 = pathStrategy = sCache.get(object2);
            if (pathStrategy == null) {
                try {
                    pathStrategy2 = FileProvider.parsePathStrategy(object, (String)object2);
                    sCache.put((String)object2, pathStrategy2);
                }
                catch (XmlPullParserException xmlPullParserException) {
                    object = new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", xmlPullParserException);
                    throw object;
                }
                catch (IOException iOException) {
                    object2 = new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", iOException);
                    throw object2;
                }
            }
            return pathStrategy2;
        }
    }

    public static Uri getUriForFile(Context context, String string2, File file) {
        return FileProvider.getPathStrategy(context, string2).getUriForFile(file);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static int modeToMode(String string2) {
        if ("r".equals(string2)) {
            return 0x10000000;
        }
        if ("w".equals(string2)) return 0x2C000000;
        if ("wt".equals(string2)) return 0x2C000000;
        if ("wa".equals(string2)) {
            return 0x2A000000;
        }
        if ("rw".equals(string2)) {
            return 0x38000000;
        }
        if ("rwt".equals(string2)) {
            return 0x3C000000;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid mode: ");
        stringBuilder.append(string2);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private static PathStrategy parsePathStrategy(Context object, String object2) throws IOException, XmlPullParserException {
        SimplePathStrategy simplePathStrategy = new SimplePathStrategy((String)object2);
        File[] fileArray = object.getPackageManager().resolveContentProvider((String)object2, 128);
        if (fileArray != null) {
            XmlResourceParser xmlResourceParser = fileArray.loadXmlMetaData(object.getPackageManager(), META_DATA_FILE_PROVIDER_PATHS);
            if (xmlResourceParser != null) {
                int n;
                while ((n = xmlResourceParser.next()) != 1) {
                    if (n != 2) continue;
                    String string2 = xmlResourceParser.getName();
                    String string3 = xmlResourceParser.getAttributeValue(null, ATTR_NAME);
                    String string4 = xmlResourceParser.getAttributeValue(null, ATTR_PATH);
                    File[] fileArray2 = null;
                    fileArray = null;
                    object2 = null;
                    if (TAG_ROOT_PATH.equals(string2)) {
                        object2 = DEVICE_ROOT;
                    } else if (TAG_FILES_PATH.equals(string2)) {
                        object2 = object.getFilesDir();
                    } else if (TAG_CACHE_PATH.equals(string2)) {
                        object2 = object.getCacheDir();
                    } else if (TAG_EXTERNAL.equals(string2)) {
                        object2 = Environment.getExternalStorageDirectory();
                    } else if (TAG_EXTERNAL_FILES.equals(string2)) {
                        fileArray = ContextCompat.getExternalFilesDirs((Context)object, null);
                        if (fileArray.length > 0) {
                            object2 = fileArray[0];
                        }
                    } else if (TAG_EXTERNAL_CACHE.equals(string2)) {
                        fileArray = ContextCompat.getExternalCacheDirs((Context)object);
                        object2 = fileArray2;
                        if (fileArray.length > 0) {
                            object2 = fileArray[0];
                        }
                    } else {
                        object2 = fileArray2;
                        if (Build.VERSION.SDK_INT >= 21) {
                            object2 = fileArray;
                            if (TAG_EXTERNAL_MEDIA.equals(string2)) {
                                fileArray2 = object.getExternalMediaDirs();
                                object2 = fileArray;
                                if (fileArray2.length > 0) {
                                    object2 = fileArray2[0];
                                }
                            }
                        }
                    }
                    if (object2 == null) continue;
                    simplePathStrategy.addRoot(string3, FileProvider.buildPath((File)object2, string4));
                }
                return simplePathStrategy;
            }
            throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Couldn't find meta-data for provider with authority ");
        ((StringBuilder)object).append((String)object2);
        object = new IllegalArgumentException(((StringBuilder)object).toString());
        throw object;
    }

    public void attachInfo(Context context, ProviderInfo providerInfo) {
        super.attachInfo(context, providerInfo);
        if (!providerInfo.exported) {
            if (providerInfo.grantUriPermissions) {
                this.mStrategy = FileProvider.getPathStrategy(context, providerInfo.authority);
                return;
            }
            throw new SecurityException("Provider must grant uri permissions");
        }
        throw new SecurityException("Provider must not be exported");
    }

    public int delete(Uri uri, String string2, String[] stringArray) {
        return this.mStrategy.getFileForUri(uri).delete() ? 1 : 0;
    }

    public String getType(Uri object) {
        int n = ((File)(object = this.mStrategy.getFileForUri((Uri)object))).getName().lastIndexOf(46);
        if (n >= 0) {
            object = ((File)object).getName().substring(n + 1);
            object = MimeTypeMap.getSingleton().getMimeTypeFromExtension((String)object);
            if (object != null) {
                return object;
            }
        }
        return "application/octet-stream";
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        throw new UnsupportedOperationException("No external inserts");
    }

    public boolean onCreate() {
        return true;
    }

    public ParcelFileDescriptor openFile(Uri uri, String string2) throws FileNotFoundException {
        return ParcelFileDescriptor.open((File)this.mStrategy.getFileForUri(uri), (int)FileProvider.modeToMode(string2));
    }

    public Cursor query(Uri matrixCursor, String[] objectArray, String object, String[] stringArray, String string22) {
        object = this.mStrategy.getFileForUri((Uri)matrixCursor);
        matrixCursor = objectArray;
        if (objectArray == null) {
            matrixCursor = COLUMNS;
        }
        stringArray = new String[((String[])matrixCursor).length];
        objectArray = new Object[((String[])matrixCursor).length];
        int n = 0;
        for (String string22 : matrixCursor) {
            int n2;
            if ("_display_name".equals(string22)) {
                stringArray[n] = "_display_name";
                objectArray[n] = ((File)object).getName();
                n2 = n + 1;
            } else {
                n2 = n;
                if ("_size".equals(string22)) {
                    stringArray[n] = "_size";
                    objectArray[n] = ((File)object).length();
                    n2 = n + 1;
                }
            }
            n = n2;
        }
        matrixCursor = FileProvider.copyOf(stringArray, n);
        objectArray = FileProvider.copyOf(objectArray, n);
        matrixCursor = new MatrixCursor((String[])matrixCursor, 1);
        matrixCursor.addRow(objectArray);
        return matrixCursor;
    }

    public int update(Uri uri, ContentValues contentValues, String string2, String[] stringArray) {
        throw new UnsupportedOperationException("No external updates");
    }

    static interface PathStrategy {
        public File getFileForUri(Uri var1);

        public Uri getUriForFile(File var1);
    }

    static class SimplePathStrategy
    implements PathStrategy {
        private final String mAuthority;
        private final HashMap<String, File> mRoots = new HashMap();

        SimplePathStrategy(String string2) {
            this.mAuthority = string2;
        }

        void addRoot(String string2, File file) {
            if (!TextUtils.isEmpty((CharSequence)string2)) {
                try {
                    File file2 = file.getCanonicalFile();
                    this.mRoots.put(string2, file2);
                }
                catch (IOException iOException) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to resolve canonical path for ");
                    stringBuilder.append(file);
                    throw new IllegalArgumentException(stringBuilder.toString(), iOException);
                }
                return;
            }
            throw new IllegalArgumentException("Name must not be empty");
        }

        @Override
        public File getFileForUri(Uri object) {
            Object object2 = object.getEncodedPath();
            int n = ((String)object2).indexOf(47, 1);
            Object object3 = Uri.decode((String)((String)object2).substring(1, n));
            object2 = Uri.decode((String)((String)object2).substring(n + 1));
            if ((object3 = this.mRoots.get(object3)) != null) {
                object = new File((File)object3, (String)object2);
                try {
                    object2 = ((File)object).getCanonicalFile();
                    if (((File)object2).getPath().startsWith(((File)object3).getPath())) {
                        return object2;
                    }
                    throw new SecurityException("Resolved path jumped beyond configured root");
                }
                catch (IOException iOException) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to resolve canonical path for ");
                    stringBuilder.append(object);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            object3 = new StringBuilder();
            ((StringBuilder)object3).append("Unable to find configured root for ");
            ((StringBuilder)object3).append(object);
            throw new IllegalArgumentException(((StringBuilder)object3).toString());
        }

        @Override
        public Uri getUriForFile(File object) {
            Object object2;
            String string2;
            try {
                string2 = ((File)object).getCanonicalPath();
                object = null;
            }
            catch (IOException iOException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to resolve canonical path for ");
                stringBuilder.append(object);
                object = new IllegalArgumentException(stringBuilder.toString());
                throw object;
            }
            for (Map.Entry<String, File> object3 : this.mRoots.entrySet()) {
                block7: {
                    block8: {
                        String string3 = object3.getValue().getPath();
                        object2 = object;
                        if (!string2.startsWith(string3)) break block7;
                        if (object == null) break block8;
                        object2 = object;
                        if (string3.length() <= ((File)object.getValue()).getPath().length()) break block7;
                    }
                    object2 = object3;
                }
                object = object2;
            }
            if (object != null) {
                object2 = ((File)object.getValue()).getPath();
                object2 = ((String)object2).endsWith("/") ? string2.substring(((String)object2).length()) : string2.substring(((String)object2).length() + 1);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Uri.encode((String)((String)object.getKey())));
                stringBuilder.append('/');
                stringBuilder.append(Uri.encode(object2, (String)"/"));
                object = stringBuilder.toString();
                return new Uri.Builder().scheme("content").authority(this.mAuthority).encodedPath((String)object).build();
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Failed to find configured root that contains ");
            ((StringBuilder)object).append(string2);
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }
    }
}

