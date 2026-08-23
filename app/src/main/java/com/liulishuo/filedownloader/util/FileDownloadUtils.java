/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager
 *  android.app.ActivityManager$RunningAppProcessInfo
 *  android.content.Context
 *  android.net.ConnectivityManager
 *  android.os.Build$VERSION
 *  android.os.Environment
 *  android.os.PowerManager
 *  android.os.Process
 *  android.os.StatFs
 *  android.text.TextUtils
 */
package com.liulishuo.filedownloader.util;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.Process;
import android.os.StatFs;
import android.text.TextUtils;
import com.liulishuo.filedownloader.connection.FileDownloadConnection;
import com.liulishuo.filedownloader.download.CustomComponentHolder;
import com.liulishuo.filedownloader.exception.FileDownloadGiveUpRetryException;
import com.liulishuo.filedownloader.exception.FileDownloadSecurityException;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.stream.FileDownloadOutputStream;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadProperties;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileDownloadUtils {
    private static final Pattern CONTENT_DISPOSITION_WITHOUT_ASTERISK_PATTERN;
    private static final Pattern CONTENT_DISPOSITION_WITH_ASTERISK_PATTERN;
    private static final String FILEDOWNLOADER_PREFIX = "FileDownloader";
    private static final String INTERNAL_DOCUMENT_NAME = "filedownloader";
    private static final String OLD_FILE_CONVERTED_FILE_NAME = ".old_file_converted";
    private static String defaultSaveRootPath;
    private static Boolean filenameConverted;
    private static Boolean isDownloaderProcess;
    private static int minProgressStep;
    private static long minProgressTime;

    static {
        minProgressStep = 65536;
        minProgressTime = 2000L;
        filenameConverted = null;
        CONTENT_DISPOSITION_WITH_ASTERISK_PATTERN = Pattern.compile("attachment;\\s*filename\\*\\s*=\\s*\"*([^\"]*)'\\S*'([^\"]*)\"*");
        CONTENT_DISPOSITION_WITHOUT_ASTERISK_PATTERN = Pattern.compile("attachment;\\s*filename\\s*=\\s*\"*([^\"\\n]*)\"*");
    }

    public static boolean checkPermission(String string2) {
        boolean bl = FileDownloadHelper.getAppContext().checkCallingOrSelfPermission(string2) == 0;
        return bl;
    }

    public static long convertContentLengthString(String string2) {
        if (string2 == null) {
            return -1L;
        }
        try {
            long l = Long.parseLong(string2);
            return l;
        }
        catch (NumberFormatException numberFormatException) {
            return -1L;
        }
    }

    public static String[] convertHeaderString(String stringArray) {
        String[] stringArray2 = stringArray.split("\n");
        stringArray = new String[stringArray2.length * 2];
        for (int i = 0; i < stringArray2.length; ++i) {
            String[] stringArray3 = stringArray2[i].split(": ");
            stringArray[i * 2] = stringArray3[0];
            stringArray[i * 2 + 1] = stringArray3[1];
        }
        return stringArray;
    }

    public static FileDownloadOutputStream createOutputStream(String string2) throws IOException {
        if (!TextUtils.isEmpty((CharSequence)string2)) {
            if (FileDownloadUtils.isFilenameValid(string2)) {
                File file = new File(string2);
                if (file.exists() && file.isDirectory()) {
                    throw new RuntimeException(FileDownloadUtils.formatString("found invalid internal destination path[%s], & path is directory[%B]", string2, file.isDirectory()));
                }
                if (!file.exists() && !file.createNewFile()) {
                    throw new IOException(FileDownloadUtils.formatString("create new file error  %s", file.getAbsolutePath()));
                }
                return CustomComponentHolder.getImpl().createOutputStream(file);
            }
            throw new RuntimeException(FileDownloadUtils.formatString("found invalid internal destination filename %s", string2));
        }
        throw new RuntimeException("found invalid internal destination path, empty");
    }

    public static String defaultUserAgent() {
        return FileDownloadUtils.formatString("FileDownloader/%s", "1.7.7");
    }

    public static void deleteTargetFile(String object) {
        if (object != null && ((File)(object = new File((String)object))).exists()) {
            ((File)object).delete();
        }
    }

    public static void deleteTaskFiles(String string2, String string3) {
        FileDownloadUtils.deleteTempFile(string3);
        FileDownloadUtils.deleteTargetFile(string2);
    }

    public static void deleteTempFile(String object) {
        if (object != null && ((File)(object = new File((String)object))).exists()) {
            ((File)object).delete();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static long findContentLength(int n, FileDownloadConnection object) {
        long l = FileDownloadUtils.convertContentLengthString(object.getResponseHeaderField("Content-Length"));
        object = object.getResponseHeaderField("Transfer-Encoding");
        long l2 = l;
        if (l >= 0L) return l2;
        if (object != null && ((String)object).equals("chunked")) {
            return -1L;
        }
        boolean bl = false;
        if (bl) return -1L;
        if (!FileDownloadProperties.getImpl().httpLenient) throw new FileDownloadGiveUpRetryException("can't know the size of the download file, and its Transfer-Encoding is not Chunked either.\nyou can ignore such exception by add http.lenient=true to the filedownloader.properties");
        l2 = l = -1L;
        if (!FileDownloadLog.NEED_LOG) return l2;
        FileDownloadLog.d(FileDownloadUtils.class, "%d response header is not legal but HTTP lenient is true, so handle as the case of transfer encoding chunk", n);
        return l;
    }

    public static long findContentLengthFromContentRange(FileDownloadConnection fileDownloadConnection) {
        long l;
        long l2 = l = FileDownloadUtils.parseContentLengthFromContentRange(FileDownloadUtils.getContentRangeHeader(fileDownloadConnection));
        if (l < 0L) {
            l2 = -1L;
        }
        return l2;
    }

    public static String findEtag(int n, FileDownloadConnection object) {
        if (object != null) {
            object = object.getResponseHeaderField("Etag");
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(FileDownloadUtils.class, "etag find %s for task(%d)", object, n);
            }
            return object;
        }
        throw new RuntimeException("connection is null when findEtag");
    }

    static String findFileNameFromUrl(String string2) {
        if (string2 != null && !string2.isEmpty()) {
            try {
                URL uRL = new URL(string2);
                string2 = uRL.getPath();
                string2 = string2.substring(string2.lastIndexOf(47) + 1);
                boolean bl = string2.isEmpty();
                if (bl) {
                    return null;
                }
                return string2;
            }
            catch (MalformedURLException malformedURLException) {
                return null;
            }
        }
        return null;
    }

    public static String findFilename(FileDownloadConnection object, String string2) throws FileDownloadSecurityException {
        block6: {
            block5: {
                block4: {
                    String string3 = FileDownloadUtils.parseContentDisposition(object.getResponseHeaderField("Content-Disposition"));
                    object = string3;
                    if (TextUtils.isEmpty((CharSequence)string3)) {
                        object = FileDownloadUtils.findFileNameFromUrl(string2);
                    }
                    if (!TextUtils.isEmpty((CharSequence)object)) break block4;
                    object = FileDownloadUtils.generateFileName(string2);
                    break block5;
                }
                if (((String)object).contains("../")) break block6;
            }
            return object;
        }
        throw new FileDownloadSecurityException(FileDownloadUtils.formatString("The filename [%s] from the response is not allowable, because it contains '../', which can raise the directory traversal vulnerability", object));
    }

    public static long findInstanceLengthForTrial(FileDownloadConnection fileDownloadConnection) {
        long l;
        long l2 = l = FileDownloadUtils.findInstanceLengthFromContentRange(fileDownloadConnection);
        if (l < 0L) {
            l2 = -1L;
            FileDownloadLog.w(FileDownloadUtils.class, "don't get instance length fromContent-Range header", new Object[0]);
        }
        l = l2;
        if (l2 == 0L) {
            l = l2;
            if (FileDownloadProperties.getImpl().trialConnectionHeadMethod) {
                l = -1L;
            }
        }
        return l;
    }

    public static long findInstanceLengthFromContentRange(FileDownloadConnection fileDownloadConnection) {
        return FileDownloadUtils.parseContentRangeFoInstanceLength(FileDownloadUtils.getContentRangeHeader(fileDownloadConnection));
    }

    public static String formatString(String string2, Object ... objectArray) {
        return String.format(Locale.ENGLISH, string2, objectArray);
    }

    public static String generateFileName(String string2) {
        return FileDownloadUtils.md5(string2);
    }

    public static String generateFilePath(String string2, String string3) {
        if (string3 != null) {
            if (string2 != null) {
                return FileDownloadUtils.formatString("%s%s%s", string2, File.separator, string3);
            }
            throw new IllegalStateException("can't generate real path, the directory is null");
        }
        throw new IllegalStateException("can't generate real path, the file name is null");
    }

    public static int generateId(String string2, String string3) {
        return CustomComponentHolder.getImpl().getIdGeneratorInstance().generateId(string2, string3, false);
    }

    public static int generateId(String string2, String string3, boolean bl) {
        return CustomComponentHolder.getImpl().getIdGeneratorInstance().generateId(string2, string3, bl);
    }

    private static String getContentRangeHeader(FileDownloadConnection fileDownloadConnection) {
        return fileDownloadConnection.getResponseHeaderField("Content-Range");
    }

    public static File getConvertedMarkedFile(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getFilesDir().getAbsolutePath());
        stringBuilder.append(File.separator);
        stringBuilder.append(INTERNAL_DOCUMENT_NAME);
        return new File(stringBuilder.toString(), OLD_FILE_CONVERTED_FILE_NAME);
    }

    public static String getDefaultSaveFilePath(String string2) {
        return FileDownloadUtils.generateFilePath(FileDownloadUtils.getDefaultSaveRootPath(), FileDownloadUtils.generateFileName(string2));
    }

    public static String getDefaultSaveRootPath() {
        boolean bl;
        if (!TextUtils.isEmpty((CharSequence)defaultSaveRootPath)) {
            return defaultSaveRootPath;
        }
        boolean bl2 = bl = false;
        if (FileDownloadHelper.getAppContext().getExternalCacheDir() != null) {
            bl2 = bl;
            if (Environment.getExternalStorageState().equals("mounted")) {
                bl2 = bl;
                if (Environment.getExternalStorageDirectory().getFreeSpace() > 0L) {
                    bl2 = true;
                }
            }
        }
        if (bl2) {
            return FileDownloadHelper.getAppContext().getExternalCacheDir().getAbsolutePath();
        }
        return FileDownloadHelper.getAppContext().getCacheDir().getAbsolutePath();
    }

    public static long getFreeSpaceBytes(String string2) {
        string2 = new StatFs(string2);
        long l = Build.VERSION.SDK_INT >= 18 ? string2.getAvailableBytes() : (long)string2.getAvailableBlocks() * (long)string2.getBlockSize();
        return l;
    }

    public static int getMinProgressStep() {
        return minProgressStep;
    }

    public static long getMinProgressTime() {
        return minProgressTime;
    }

    public static String getParent(String string2) {
        int n;
        int n2;
        int n3 = string2.length();
        int n4 = n2 = 0;
        if (File.separatorChar == '\\') {
            n4 = n2;
            if (n3 > 2) {
                n4 = n2;
                if (string2.charAt(1) == ':') {
                    n4 = 2;
                }
            }
        }
        n2 = n = string2.lastIndexOf(File.separatorChar);
        if (n == -1) {
            n2 = n;
            if (n4 > 0) {
                n2 = 2;
            }
        }
        if (n2 != -1 && string2.charAt(n3 - 1) != File.separatorChar) {
            if (string2.indexOf(File.separatorChar) == n2 && string2.charAt(n4) == File.separatorChar) {
                return string2.substring(0, n2 + 1);
            }
            return string2.substring(0, n2);
        }
        return null;
    }

    public static String getStack() {
        return FileDownloadUtils.getStack(true);
    }

    public static String getStack(boolean bl) {
        return FileDownloadUtils.getStack(new Throwable().getStackTrace(), bl);
    }

    public static String getStack(StackTraceElement[] stackTraceElementArray, boolean bl) {
        if (stackTraceElementArray != null && stackTraceElementArray.length >= 4) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 3; i < stackTraceElementArray.length; ++i) {
                if (!stackTraceElementArray[i].getClassName().contains("com.liulishuo.filedownloader")) continue;
                stringBuilder.append("[");
                stringBuilder.append(stackTraceElementArray[i].getClassName().substring("com.liulishuo.filedownloader".length()));
                stringBuilder.append(":");
                stringBuilder.append(stackTraceElementArray[i].getMethodName());
                if (bl) {
                    stringBuilder.append("(");
                    stringBuilder.append(stackTraceElementArray[i].getLineNumber());
                    stringBuilder.append(")]");
                    continue;
                }
                stringBuilder.append("]");
            }
            return stringBuilder.toString();
        }
        return "";
    }

    public static String getTargetFilePath(String string2, boolean bl, String string3) {
        if (string2 == null) {
            return null;
        }
        if (bl) {
            if (string3 == null) {
                return null;
            }
            return FileDownloadUtils.generateFilePath(string2, string3);
        }
        return string2;
    }

    public static String getTempPath(String string2) {
        return FileDownloadUtils.formatString("%s.temp", string2);
    }

    public static String getThreadPoolName(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FileDownloader-");
        stringBuilder.append(string2);
        return stringBuilder.toString();
    }

    public static boolean isAcceptRange(int n, FileDownloadConnection fileDownloadConnection) {
        if (n != 206 && n != 1) {
            return "bytes".equals(fileDownloadConnection.getResponseHeaderField("Accept-Ranges"));
        }
        return true;
    }

    private static boolean isAppOnForeground(Context object) {
        Object object2 = (ActivityManager)object.getApplicationContext().getSystemService("activity");
        if (object2 == null) {
            return false;
        }
        if ((object2 = object2.getRunningAppProcesses()) == null) {
            return false;
        }
        Object object3 = (PowerManager)object.getSystemService("power");
        if (object3 == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT > 19 ? !object3.isInteractive() : !object3.isScreenOn()) {
            return false;
        }
        object = object.getApplicationContext().getPackageName();
        object3 = object2.iterator();
        while (object3.hasNext()) {
            object2 = (ActivityManager.RunningAppProcessInfo)object3.next();
            if (!((ActivityManager.RunningAppProcessInfo)object2).processName.equals(object) || ((ActivityManager.RunningAppProcessInfo)object2).importance != 100) continue;
            return true;
        }
        return false;
    }

    public static boolean isBreakpointAvailable(int n, FileDownloadModel fileDownloadModel) {
        return FileDownloadUtils.isBreakpointAvailable(n, fileDownloadModel, null);
    }

    public static boolean isBreakpointAvailable(int n, FileDownloadModel fileDownloadModel, Boolean bl) {
        if (fileDownloadModel == null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(FileDownloadUtils.class, "can't continue %d model == null", n);
            }
            return false;
        }
        if (fileDownloadModel.getTempFilePath() == null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(FileDownloadUtils.class, "can't continue %d temp path == null", n);
            }
            return false;
        }
        return FileDownloadUtils.isBreakpointAvailable(n, fileDownloadModel, fileDownloadModel.getTempFilePath(), bl);
    }

    public static boolean isBreakpointAvailable(int n, FileDownloadModel fileDownloadModel, String object, Boolean bl) {
        boolean bl2;
        boolean bl3 = false;
        if (object == null) {
            bl2 = bl3;
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(FileDownloadUtils.class, "can't continue %d path = null", n);
                bl2 = bl3;
            }
        } else {
            object = new File((String)object);
            boolean bl4 = ((File)object).exists();
            boolean bl5 = ((File)object).isDirectory();
            if (bl4 && !bl5) {
                long l = ((File)object).length();
                long l2 = fileDownloadModel.getSoFar();
                if (fileDownloadModel.getConnectionCount() <= 1 && l2 == 0L) {
                    bl2 = bl3;
                    if (FileDownloadLog.NEED_LOG) {
                        FileDownloadLog.d(FileDownloadUtils.class, "can't continue %d the downloaded-record is zero.", n);
                        bl2 = bl3;
                    }
                } else {
                    long l3 = fileDownloadModel.getTotal();
                    if (l >= l2 && (l3 == -1L || l <= l3 && l2 < l3)) {
                        if (bl != null && !bl.booleanValue() && l3 == l) {
                            bl2 = bl3;
                            if (FileDownloadLog.NEED_LOG) {
                                FileDownloadLog.d(FileDownloadUtils.class, "can't continue %d, because of the output stream doesn't support seek, but the task has already pre-allocated, so we only can download it from the very beginning.", n);
                                bl2 = bl3;
                            }
                        } else {
                            bl2 = true;
                        }
                    } else {
                        bl2 = bl3;
                        if (FileDownloadLog.NEED_LOG) {
                            FileDownloadLog.d(FileDownloadUtils.class, "can't continue %d dirty data fileLength[%d] sofar[%d] total[%d]", n, l, l2, l3);
                            bl2 = bl3;
                        }
                    }
                }
            } else {
                bl2 = bl3;
                if (FileDownloadLog.NEED_LOG) {
                    FileDownloadLog.d(FileDownloadUtils.class, "can't continue %d file not suit, exists[%B], directory[%B]", n, bl4, bl5);
                    bl2 = bl3;
                }
            }
        }
        return bl2;
    }

    public static boolean isDownloaderProcess(Context object) {
        block6: {
            boolean bl;
            block4: {
                boolean bl2;
                Boolean bl3;
                block5: {
                    bl3 = isDownloaderProcess;
                    if (bl3 != null) {
                        return bl3;
                    }
                    bl2 = false;
                    if (!FileDownloadProperties.getImpl().processNonSeparate) break block5;
                    bl = true;
                    break block4;
                }
                int n = Process.myPid();
                if ((object = (ActivityManager)object.getSystemService("activity")) == null) {
                    FileDownloadLog.w(FileDownloadUtils.class, "fail to get the activity manager!", new Object[0]);
                    return false;
                }
                if ((object = object.getRunningAppProcesses()) == null || object.isEmpty()) break block6;
                object = object.iterator();
                do {
                    bl = bl2;
                    if (!object.hasNext()) break block4;
                    bl3 = (ActivityManager.RunningAppProcessInfo)object.next();
                } while (((ActivityManager.RunningAppProcessInfo)bl3).pid != n);
                bl = ((ActivityManager.RunningAppProcessInfo)bl3).processName.endsWith(":filedownloader");
            }
            object = bl;
            isDownloaderProcess = object;
            return (Boolean)object;
        }
        FileDownloadLog.w(FileDownloadUtils.class, "The running app process info list from ActivityManager is null or empty, maybe current App is not running.", new Object[0]);
        return false;
    }

    public static boolean isFilenameConverted(Context context) {
        if (filenameConverted == null) {
            filenameConverted = FileDownloadUtils.getConvertedMarkedFile(context).exists();
        }
        return filenameConverted;
    }

    public static boolean isFilenameValid(String string2) {
        return true;
    }

    public static boolean isNeedSync(long l, long l2) {
        boolean bl = l > (long)FileDownloadUtils.getMinProgressStep() && l2 > FileDownloadUtils.getMinProgressTime();
        return bl;
    }

    public static boolean isNetworkNotOnWifiType() {
        ConnectivityManager connectivityManager = (ConnectivityManager)FileDownloadHelper.getAppContext().getSystemService("connectivity");
        boolean bl = false;
        if (connectivityManager == null) {
            FileDownloadLog.w(FileDownloadUtils.class, "failed to get connectivity manager!", new Object[0]);
            return true;
        }
        if ((connectivityManager = connectivityManager.getActiveNetworkInfo()) == null || connectivityManager.getType() != 1) {
            bl = true;
        }
        return bl;
    }

    public static void markConverted(Context object) {
        object = FileDownloadUtils.getConvertedMarkedFile((Context)object);
        try {
            ((File)object).getParentFile().mkdirs();
            ((File)object).createNewFile();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static String md5(String charSequence) {
        byte[] byArray;
        try {
            byArray = MessageDigest.getInstance("MD5").digest(((String)charSequence).getBytes("UTF-8"));
            charSequence = new StringBuilder(byArray.length * 2);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", unsupportedEncodingException);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            RuntimeException runtimeException = new RuntimeException("Huh, MD5 should be supported?", noSuchAlgorithmException);
            throw runtimeException;
        }
        for (byte by : byArray) {
            if ((by & 0xFF) < 16) {
                ((StringBuilder)charSequence).append("0");
            }
            ((StringBuilder)charSequence).append(Integer.toHexString(by & 0xFF));
        }
        return ((StringBuilder)charSequence).toString();
    }

    public static boolean needMakeServiceForeground(Context context) {
        boolean bl = Build.VERSION.SDK_INT >= 26 && !FileDownloadUtils.isAppOnForeground(context);
        return bl;
    }

    public static String parseContentDisposition(String object) {
        if (object == null) {
            return null;
        }
        try {
            Matcher matcher = CONTENT_DISPOSITION_WITH_ASTERISK_PATTERN.matcher((CharSequence)object);
            if (matcher.find()) {
                object = matcher.group(1);
                return URLDecoder.decode(matcher.group(2), (String)object);
            }
            if (((Matcher)(object = CONTENT_DISPOSITION_WITHOUT_ASTERISK_PATTERN.matcher((CharSequence)object))).find()) {
                object = ((Matcher)object).group(1);
                return object;
            }
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
        }
        catch (IllegalStateException illegalStateException) {
            // empty catch block
        }
        return null;
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
                FileDownloadLog.e(FileDownloadUtils.class, exception, "parse content length from content range error", new Object[0]);
            }
            return -1L;
        }
        return -1L;
    }

    public static long parseContentRangeFoInstanceLength(String string2) {
        if (string2 == null) {
            return -1L;
        }
        String[] stringArray = string2.split("/");
        if (stringArray.length >= 2) {
            try {
                long l = Long.parseLong(stringArray[1]);
                return l;
            }
            catch (NumberFormatException numberFormatException) {
                FileDownloadLog.w(FileDownloadUtils.class, "parse instance length failed with %s", string2);
            }
        }
        return -1L;
    }

    public static void setDefaultSaveRootPath(String string2) {
        defaultSaveRootPath = string2;
    }

    public static void setMinProgressStep(int n) throws IllegalAccessException {
        if (FileDownloadUtils.isDownloaderProcess(FileDownloadHelper.getAppContext())) {
            minProgressStep = n;
            return;
        }
        throw new IllegalAccessException("This value is used in the :filedownloader process, so set this value in your process is without effect. You can add 'process.non-separate=true' in 'filedownloader.properties' to share the main process to FileDownloadService. Or you can configure this value in 'filedownloader.properties' by 'download.min-progress-step'.");
    }

    public static void setMinProgressTime(long l) throws IllegalAccessException {
        if (FileDownloadUtils.isDownloaderProcess(FileDownloadHelper.getAppContext())) {
            minProgressTime = l;
            return;
        }
        throw new IllegalAccessException("This value is used in the :filedownloader process, so set this value in your process is without effect. You can add 'process.non-separate=true' in 'filedownloader.properties' to share the main process to FileDownloadService. Or you can configure this value in 'filedownloader.properties' by 'download.min-progress-time'.");
    }
}

