/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.Cursor
 *  android.net.ConnectivityManager
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.StatFs
 *  android.util.Log
 */
package com.liulishuo.okdownload.core;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.StatFs;
import android.util.Log;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointStoreOnCache;
import com.liulishuo.okdownload.core.breakpoint.DownloadStore;
import com.liulishuo.okdownload.core.connection.DownloadConnection;
import com.liulishuo.okdownload.core.connection.DownloadUrlConnection;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final int CHUNKED_CONTENT_LENGTH = -1;
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String ETAG = "Etag";
    public static final String IF_MATCH = "If-Match";
    public static final String METHOD_HEAD = "HEAD";
    public static final String RANGE = "Range";
    public static final int RANGE_NOT_SATISFIABLE = 416;
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";
    public static final String USER_AGENT = "User-Agent";
    public static final String VALUE_CHUNKED = "chunked";
    private static Logger logger = new EmptyLogger();

    public static void addDefaultUserAgent(DownloadConnection downloadConnection) {
        downloadConnection.addHeader(USER_AGENT, "OkDownload/1.0.3");
    }

    public static void addUserRequestHeaderField(Map<String, List<String>> object, DownloadConnection downloadConnection) throws IOException {
        Util.inspectUserHeader((Map<String, List<String>>)object);
        for (Map.Entry entry : object.entrySet()) {
            object = (String)entry.getKey();
            Iterator object2 = ((List)entry.getValue()).iterator();
            while (object2.hasNext()) {
                downloadConnection.addHeader((String)object, (String)object2.next());
            }
        }
    }

    public static void assembleBlock(DownloadTask downloadTask, BreakpointInfo breakpointInfo, long l, boolean bl) {
        int n = OkDownload.with().downloadStrategy().isUseMultiBlock(bl) ? OkDownload.with().downloadStrategy().determineBlockCount(downloadTask, l) : 1;
        breakpointInfo.resetBlockInfos();
        long l2 = l / (long)n;
        long l3 = 0L;
        long l4 = 0L;
        for (int i = 0; i < n; ++i) {
            l4 = i == 0 ? l % (long)n + l2 : l2;
            breakpointInfo.addBlock(new BlockInfo(l3 += l4, l4));
        }
    }

    public static boolean checkPermission(String string2) {
        boolean bl = OkDownload.with().context().checkCallingOrSelfPermission(string2) == 0;
        return bl;
    }

    public static DownloadConnection.Factory createDefaultConnectionFactory() {
        try {
            DownloadConnection.Factory factory = (DownloadConnection.Factory)Class.forName("com.liulishuo.okdownload.core.connection.DownloadOkHttp3Connection$Factory").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            return factory;
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (NoSuchMethodException noSuchMethodException) {
        }
        catch (IllegalAccessException illegalAccessException) {
        }
        catch (InstantiationException instantiationException) {
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        return new DownloadUrlConnection.Factory();
    }

    public static DownloadStore createDefaultDatabase(Context object) {
        try {
            object = (DownloadStore)Class.forName("com.liulishuo.okdownload.core.breakpoint.BreakpointStoreOnSQLite").getDeclaredConstructor(Context.class).newInstance(object);
            return object;
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (NoSuchMethodException noSuchMethodException) {
        }
        catch (IllegalAccessException illegalAccessException) {
        }
        catch (InstantiationException instantiationException) {
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        return new BreakpointStoreOnCache();
    }

    public static DownloadStore createRemitDatabase(DownloadStore object) {
        Object object2 = object;
        try {
            object2 = object = (DownloadStore)object.getClass().getMethod("createRemitSelf", new Class[0]).invoke(object, new Object[0]);
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (NoSuchMethodException noSuchMethodException) {
        }
        catch (IllegalAccessException illegalAccessException) {}
        object = new StringBuilder();
        ((StringBuilder)object).append("Get final download store is ");
        ((StringBuilder)object).append(object2);
        Util.d("Util", ((StringBuilder)object).toString());
        return object2;
    }

    public static void d(String string2, String string3) {
        Logger logger = Util.logger;
        if (logger != null) {
            logger.d(string2, string3);
            return;
        }
        Log.d((String)string2, (String)string3);
    }

    public static void e(String string2, String string3, Exception exception) {
        Logger logger = Util.logger;
        if (logger != null) {
            logger.e(string2, string3, exception);
            return;
        }
        Log.e((String)string2, (String)string3, (Throwable)exception);
    }

    public static void enableConsoleLog() {
        logger = null;
    }

    public static String getFilenameFromContentUri(Uri uri) {
        uri = OkDownload.with().context().getContentResolver().query(uri, null, null, null, null);
        if (uri != null) {
            try {
                uri.moveToFirst();
                String string2 = uri.getString(uri.getColumnIndex("_display_name"));
                return string2;
            }
            finally {
                uri.close();
            }
        }
        return null;
    }

    public static long getFreeSpaceBytes(StatFs statFs) {
        long l = Build.VERSION.SDK_INT >= 18 ? statFs.getAvailableBytes() : (long)statFs.getAvailableBlocks() * (long)statFs.getBlockSize();
        return l;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static File getParentFile(File file) {
        block0: {
            if ((file = file.getParentFile()) != null) break block0;
            file = new File("/");
        }
        return file;
    }

    public static long getSizeFromContentUri(Uri uri) {
        Cursor cursor = OkDownload.with().context().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            try {
                cursor.moveToFirst();
                long l = cursor.getLong(cursor.getColumnIndex("_size"));
                return l;
            }
            finally {
                cursor.close();
            }
        }
        return 0L;
    }

    public static String humanReadableBytes(long l, boolean bl) {
        int n = bl ? 1000 : 1024;
        if (l < (long)n) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(l);
            stringBuilder.append(" B");
            return stringBuilder.toString();
        }
        int n2 = (int)(Math.log(l) / Math.log(n));
        CharSequence charSequence = new StringBuilder();
        Object object = bl ? "kMGTPE" : "KMGTPE";
        charSequence.append(((String)object).charAt(n2 - 1));
        object = bl ? "" : "i";
        charSequence.append((String)object);
        charSequence = charSequence.toString();
        object = Locale.ENGLISH;
        double d = l;
        double d2 = Math.pow(n, n2);
        Double.isNaN(d);
        return String.format((Locale)object, "%.1f %sB", d / d2, charSequence);
    }

    public static void i(String string2, String string3) {
        Logger logger = Util.logger;
        if (logger != null) {
            logger.i(string2, string3);
            return;
        }
        Log.i((String)string2, (String)string3);
    }

    public static void inspectUserHeader(Map<String, List<String>> map) throws IOException {
        if (!map.containsKey(IF_MATCH) && !map.containsKey(RANGE)) {
            return;
        }
        throw new IOException("If-Match and Range only can be handle by internal!");
    }

    public static boolean isCorrectFull(long l, long l2) {
        boolean bl = l == l2;
        return bl;
    }

    public static boolean isEmpty(CharSequence charSequence) {
        boolean bl = charSequence == null || charSequence.length() == 0;
        return bl;
    }

    public static boolean isNetworkAvailable(ConnectivityManager connectivityManager) {
        boolean bl = true;
        if (connectivityManager == null) {
            Util.w("Util", "failed to get connectivity manager!");
            return true;
        }
        if ((connectivityManager = connectivityManager.getActiveNetworkInfo()) == null || !connectivityManager.isConnected()) {
            bl = false;
        }
        return bl;
    }

    public static boolean isNetworkNotOnWifiType(ConnectivityManager connectivityManager) {
        boolean bl = true;
        if (connectivityManager == null) {
            Util.w("Util", "failed to get connectivity manager!");
            return true;
        }
        connectivityManager = connectivityManager.getActiveNetworkInfo();
        boolean bl2 = bl;
        if (connectivityManager != null) {
            bl2 = connectivityManager.getType() != 1 ? bl : false;
        }
        return bl2;
    }

    public static boolean isUriContentScheme(Uri uri) {
        return uri.getScheme().equals("content");
    }

    public static boolean isUriFileScheme(Uri uri) {
        return uri.getScheme().equals("file");
    }

    public static String md5(String object) {
        StringBuilder stringBuilder = null;
        try {
            object = MessageDigest.getInstance("MD5").digest(((String)object).getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            object = stringBuilder;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            object = stringBuilder;
        }
        if (object != null) {
            stringBuilder = new StringBuilder(((Object)object).length * 2);
            for (Object object2 : object) {
                if ((object2 & 0xFF) < 16) {
                    stringBuilder.append('0');
                }
                stringBuilder.append(Integer.toHexString(object2 & 0xFF));
            }
            return stringBuilder.toString();
        }
        return null;
    }

    public static long parseContentLength(String string2) {
        if (string2 == null) {
            return -1L;
        }
        return Long.parseLong(string2);
    }

    public static long parseContentLengthFromContentRange(String object) {
        if (object != null && ((String)object).length() != 0) {
            try {
                object = Pattern.compile("bytes (\\d+)-(\\d+)/\\d+").matcher((CharSequence)object);
                if (((Matcher)object).find()) {
                    long l = Long.parseLong(((Matcher)object).group(1));
                    long l2 = Long.parseLong(((Matcher)object).group(2));
                    return l2 - l + 1L;
                }
            }
            catch (Exception exception) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("parse content-length from content-range failed ");
                stringBuilder.append(exception);
                Util.w("Util", stringBuilder.toString());
            }
            return -1L;
        }
        return -1L;
    }

    public static void resetBlockIfDirty(BlockInfo blockInfo) {
        boolean bl = false;
        if (blockInfo.getCurrentOffset() < 0L) {
            bl = true;
        } else if (blockInfo.getCurrentOffset() > blockInfo.getContentLength()) {
            bl = true;
        }
        if (bl) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("block is dirty so have to reset: ");
            stringBuilder.append(blockInfo);
            Util.w("resetBlockIfDirty", stringBuilder.toString());
            blockInfo.resetBlock();
        }
    }

    public static void setLogger(Logger logger) {
        Util.logger = logger;
    }

    public static ThreadFactory threadFactory(String string2, boolean bl) {
        return new ThreadFactory(string2, bl){
            final boolean val$daemon;
            final String val$name;
            {
                this.val$name = string2;
                this.val$daemon = bl;
            }

            @Override
            public Thread newThread(Runnable runnable) {
                runnable = new Thread(runnable, this.val$name);
                ((Thread)runnable).setDaemon(this.val$daemon);
                return runnable;
            }
        };
    }

    public static void w(String string2, String string3) {
        Logger logger = Util.logger;
        if (logger != null) {
            logger.w(string2, string3);
            return;
        }
        Log.w((String)string2, (String)string3);
    }

    public static class EmptyLogger
    implements Logger {
        @Override
        public void d(String string2, String string3) {
        }

        @Override
        public void e(String string2, String string3, Exception exception) {
        }

        @Override
        public void i(String string2, String string3) {
        }

        @Override
        public void w(String string2, String string3) {
        }
    }

    public static interface Logger {
        public void d(String var1, String var2);

        public void e(String var1, String var2, Exception var3);

        public void i(String var1, String var2);

        public void w(String var1, String var2);
    }
}

