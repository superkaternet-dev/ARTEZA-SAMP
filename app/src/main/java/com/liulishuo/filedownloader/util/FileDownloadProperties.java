/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.util;

import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileDownloadProperties {
    private static final String FALSE_STRING = "false";
    private static final String KEY_BROADCAST_COMPLETED = "broadcast.completed";
    private static final String KEY_DOWNLOAD_MAX_NETWORK_THREAD_COUNT = "download.max-network-thread-count";
    private static final String KEY_DOWNLOAD_MIN_PROGRESS_STEP = "download.min-progress-step";
    private static final String KEY_DOWNLOAD_MIN_PROGRESS_TIME = "download.min-progress-time";
    private static final String KEY_FILE_NON_PRE_ALLOCATION = "file.non-pre-allocation";
    private static final String KEY_HTTP_LENIENT = "http.lenient";
    private static final String KEY_PROCESS_NON_SEPARATE = "process.non-separate";
    private static final String KEY_TRIAL_CONNECTION_HEAD_METHOD = "download.trial-connection-head-method";
    private static final String TRUE_STRING = "true";
    public final boolean broadcastCompleted;
    public final int downloadMaxNetworkThreadCount;
    public final int downloadMinProgressStep;
    public final long downloadMinProgressTime;
    public final boolean fileNonPreAllocation;
    public final boolean httpLenient;
    public final boolean processNonSeparate;
    public final boolean trialConnectionHeadMethod;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private FileDownloadProperties() {
        Object object;
        Object object2;
        block45: {
            Object object3;
            Object object4;
            Object object5;
            Object object6;
            Object object7;
            Object object8;
            long l;
            block50: {
                Object object9;
                Object object10;
                block49: {
                    block48: {
                        block46: {
                            block47: {
                                Object object11;
                                Object object12;
                                Object object13;
                                Object object14;
                                block44: {
                                    if (FileDownloadHelper.getAppContext() == null) {
                                        IllegalStateException illegalStateException = new IllegalStateException("Please invoke the 'FileDownloader#setup' before using FileDownloader. If you want to register some components on FileDownloader please invoke the 'FileDownloader#setupOnApplicationOnCreate' on the 'Application#onCreate' first.");
                                        throw illegalStateException;
                                    }
                                    l = System.currentTimeMillis();
                                    object10 = null;
                                    object8 = null;
                                    Object var21_4 = null;
                                    object14 = null;
                                    object7 = null;
                                    String string2 = null;
                                    object6 = null;
                                    object9 = null;
                                    String string3 = null;
                                    object13 = null;
                                    object5 = null;
                                    String string4 = null;
                                    object12 = null;
                                    object4 = null;
                                    String string5 = null;
                                    object11 = null;
                                    Properties properties = new Properties();
                                    object3 = null;
                                    object2 = null;
                                    try {
                                        object = FileDownloadHelper.getAppContext().getAssets().open("filedownloader.properties");
                                        if (object == null) break block44;
                                        object10 = null;
                                    }
                                    catch (Throwable throwable) {
                                        object2 = object3;
                                        break block45;
                                    }
                                    catch (IOException iOException) {
                                        object6 = null;
                                        object = null;
                                        break block46;
                                    }
                                    try {
                                        properties.load((InputStream)object);
                                        object2 = properties.getProperty(KEY_HTTP_LENIENT);
                                    }
                                    catch (Throwable throwable) {
                                        object3 = object;
                                        object = throwable;
                                        object2 = object3;
                                        break block45;
                                    }
                                    catch (IOException iOException) {
                                        object2 = object;
                                        object = null;
                                        object6 = object10;
                                        break block46;
                                    }
                                    try {
                                        object3 = properties.getProperty(KEY_PROCESS_NON_SEPARATE);
                                        object8 = var21_4;
                                        object7 = string2;
                                        object9 = string3;
                                        object4 = string4;
                                        object6 = string5;
                                    }
                                    catch (Throwable throwable) {
                                        object2 = object;
                                        object = throwable;
                                        break block45;
                                    }
                                    catch (IOException iOException) {
                                        object6 = object;
                                        object = object2;
                                        object2 = object6;
                                        object6 = object10;
                                        break block46;
                                    }
                                    try {
                                        object8 = object14 = properties.getProperty(KEY_DOWNLOAD_MIN_PROGRESS_STEP);
                                        object7 = string2;
                                        object9 = string3;
                                        object4 = string4;
                                        object6 = string5;
                                        object10 = properties.getProperty(KEY_DOWNLOAD_MIN_PROGRESS_TIME);
                                        object8 = object14;
                                        object7 = object10;
                                        object9 = string3;
                                        object4 = string4;
                                        object6 = string5;
                                        object5 = properties.getProperty(KEY_DOWNLOAD_MAX_NETWORK_THREAD_COUNT);
                                        object8 = object14;
                                        object7 = object10;
                                        object9 = object5;
                                        object4 = string4;
                                        object6 = string5;
                                        string4 = properties.getProperty(KEY_FILE_NON_PRE_ALLOCATION);
                                        object8 = object14;
                                        object7 = object10;
                                        object9 = object5;
                                        object4 = string4;
                                        object6 = string5;
                                        string5 = properties.getProperty(KEY_BROADCAST_COMPLETED);
                                        object8 = object14;
                                        object7 = object10;
                                        object9 = object5;
                                        object4 = string4;
                                        object6 = string5;
                                        string3 = properties.getProperty(KEY_TRIAL_CONNECTION_HEAD_METHOD);
                                        object4 = string3;
                                        object8 = object14;
                                        object7 = object10;
                                        object6 = object5;
                                        object5 = string4;
                                        object9 = string5;
                                        break block47;
                                    }
                                    catch (Throwable throwable) {
                                        object2 = object;
                                        object = throwable;
                                        break block45;
                                    }
                                    catch (IOException iOException) {
                                        object10 = object3;
                                        object14 = object;
                                        object = object2;
                                        object3 = iOException;
                                        object5 = object4;
                                        object4 = object6;
                                        object2 = object14;
                                        object6 = object10;
                                        break block46;
                                    }
                                }
                                object2 = null;
                                object3 = null;
                                object4 = object11;
                                object9 = object12;
                                object5 = object13;
                                object7 = object14;
                                object8 = object10;
                            }
                            if (object != null) {
                                try {
                                    ((InputStream)object).close();
                                }
                                catch (IOException iOException) {
                                    iOException.printStackTrace();
                                }
                            }
                            object = object9;
                            object9 = object3;
                            object3 = object4;
                            object4 = object9;
                            break block50;
                        }
                        try {
                            boolean bl = object3 instanceof FileNotFoundException;
                            if (!bl) break block48;
                        }
                        catch (Throwable throwable) {
                            // empty catch block
                            break block45;
                        }
                        try {
                            if (!FileDownloadLog.NEED_LOG) break block49;
                        }
                        catch (Throwable throwable) {
                            break block45;
                        }
                        try {
                            FileDownloadLog.d(FileDownloadProperties.class, "not found filedownloader.properties", new Object[0]);
                        }
                        catch (Throwable throwable) {
                            break block45;
                        }
                    }
                    try {
                        ((Throwable)object3).printStackTrace();
                    }
                    catch (Throwable throwable) {
                        break block45;
                    }
                }
                if (object2 != null) {
                    try {
                        ((InputStream)object2).close();
                    }
                    catch (IOException iOException) {
                        iOException.printStackTrace();
                    }
                }
                object3 = object4;
                object10 = null;
                object2 = object;
                object4 = object6;
                object6 = object9;
                object = object3;
                object3 = object10;
            }
            if (object2 != null) {
                if (!((String)object2).equals(TRUE_STRING) && !((String)object2).equals(FALSE_STRING)) {
                    throw new IllegalStateException(FileDownloadUtils.formatString("the value of '%s' must be '%s' or '%s'", KEY_HTTP_LENIENT, TRUE_STRING, FALSE_STRING));
                }
                this.httpLenient = ((String)object2).equals(TRUE_STRING);
            } else {
                this.httpLenient = false;
            }
            if (object4 != null) {
                if (!((String)object4).equals(TRUE_STRING) && !((String)object4).equals(FALSE_STRING)) {
                    throw new IllegalStateException(FileDownloadUtils.formatString("the value of '%s' must be '%s' or '%s'", KEY_PROCESS_NON_SEPARATE, TRUE_STRING, FALSE_STRING));
                }
                this.processNonSeparate = ((String)object4).equals(TRUE_STRING);
            } else {
                this.processNonSeparate = false;
            }
            this.downloadMinProgressStep = object8 != null ? Math.max(0, Integer.valueOf((String)object8)) : 65536;
            this.downloadMinProgressTime = object7 != null ? Math.max(0L, Long.valueOf((String)object7)) : 2000L;
            this.downloadMaxNetworkThreadCount = object6 != null ? FileDownloadProperties.getValidNetworkThreadCount(Integer.valueOf((String)object6)) : 3;
            if (object5 != null) {
                if (!((String)object5).equals(TRUE_STRING) && !((String)object5).equals(FALSE_STRING)) {
                    throw new IllegalStateException(FileDownloadUtils.formatString("the value of '%s' must be '%s' or '%s'", KEY_FILE_NON_PRE_ALLOCATION, TRUE_STRING, FALSE_STRING));
                }
                this.fileNonPreAllocation = ((String)object5).equals(TRUE_STRING);
            } else {
                this.fileNonPreAllocation = false;
            }
            if (object != null) {
                if (!((String)object).equals(TRUE_STRING) && !((String)object).equals(FALSE_STRING)) {
                    throw new IllegalStateException(FileDownloadUtils.formatString("the value of '%s' must be '%s' or '%s'", KEY_BROADCAST_COMPLETED, TRUE_STRING, FALSE_STRING));
                }
                this.broadcastCompleted = ((String)object).equals(TRUE_STRING);
            } else {
                this.broadcastCompleted = false;
            }
            if (object3 != null) {
                if (!((String)object3).equals(TRUE_STRING) && !((String)object3).equals(FALSE_STRING)) {
                    throw new IllegalStateException(FileDownloadUtils.formatString("the value of '%s' must be '%s' or '%s'", KEY_TRIAL_CONNECTION_HEAD_METHOD, TRUE_STRING, FALSE_STRING));
                }
                this.trialConnectionHeadMethod = ((String)object3).equals(TRUE_STRING);
            } else {
                this.trialConnectionHeadMethod = false;
            }
            if (!FileDownloadLog.NEED_LOG) return;
            FileDownloadLog.i(FileDownloadProperties.class, "init properties %d\n load properties: %s=%B; %s=%B; %s=%d; %s=%d; %s=%d; %s=%B; %s=%B; %s=%B", System.currentTimeMillis() - l, KEY_HTTP_LENIENT, this.httpLenient, KEY_PROCESS_NON_SEPARATE, this.processNonSeparate, KEY_DOWNLOAD_MIN_PROGRESS_STEP, this.downloadMinProgressStep, KEY_DOWNLOAD_MIN_PROGRESS_TIME, this.downloadMinProgressTime, KEY_DOWNLOAD_MAX_NETWORK_THREAD_COUNT, this.downloadMaxNetworkThreadCount, KEY_FILE_NON_PRE_ALLOCATION, this.fileNonPreAllocation, KEY_BROADCAST_COMPLETED, this.broadcastCompleted, KEY_TRIAL_CONNECTION_HEAD_METHOD, this.trialConnectionHeadMethod);
            return;
        }
        if (object2 == null) throw object;
        try {
            ((InputStream)object2).close();
            throw object;
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        throw object;
    }

    public static FileDownloadProperties getImpl() {
        return HolderClass.INSTANCE;
    }

    public static int getValidNetworkThreadCount(int n) {
        Integer n2 = 12;
        Integer n3 = 1;
        if (n > 12) {
            FileDownloadLog.w(FileDownloadProperties.class, "require the count of network thread  is %d, what is more than the max valid count(%d), so adjust to %d auto", n, n2, n2);
            return 12;
        }
        if (n < 1) {
            FileDownloadLog.w(FileDownloadProperties.class, "require the count of network thread  is %d, what is less than the min valid count(%d), so adjust to %d auto", n, n3, n3);
            return 1;
        }
        return n;
    }

    public static class HolderClass {
        private static final FileDownloadProperties INSTANCE = new FileDownloadProperties();
    }
}

