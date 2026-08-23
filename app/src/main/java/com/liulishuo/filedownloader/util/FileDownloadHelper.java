/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.liulishuo.filedownloader.util;

import android.content.Context;
import com.liulishuo.filedownloader.IThreadPoolMonitor;
import com.liulishuo.filedownloader.connection.FileDownloadConnection;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.exception.PathConflictException;
import com.liulishuo.filedownloader.message.MessageSnapshotFlow;
import com.liulishuo.filedownloader.message.MessageSnapshotTaker;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.stream.FileDownloadOutputStream;
import java.io.File;
import java.io.IOException;

public class FileDownloadHelper {
    private static Context APP_CONTEXT;

    public static Context getAppContext() {
        return APP_CONTEXT;
    }

    public static void holdContext(Context context) {
        APP_CONTEXT = context;
    }

    public static boolean inspectAndInflowConflictPath(int n, long l, String string2, String string3, IThreadPoolMonitor iThreadPoolMonitor) {
        int n2;
        if (string3 != null && string2 != null && (n2 = iThreadPoolMonitor.findRunningTaskIdBySameTempPath(string2, n)) != 0) {
            MessageSnapshotFlow.getImpl().inflow(MessageSnapshotTaker.catchException(n, l, new PathConflictException(n2, string2, string3)));
            return true;
        }
        return false;
    }

    public static boolean inspectAndInflowDownloaded(int n, String object, boolean bl, boolean bl2) {
        if (bl) {
            return false;
        }
        if (object != null && ((File)(object = new File((String)object))).exists()) {
            MessageSnapshotFlow.getImpl().inflow(MessageSnapshotTaker.catchCanReusedOldFile(n, (File)object, bl2));
            return true;
        }
        return false;
    }

    public static boolean inspectAndInflowDownloading(int n, FileDownloadModel fileDownloadModel, IThreadPoolMonitor iThreadPoolMonitor, boolean bl) {
        if (iThreadPoolMonitor.isDownloading(fileDownloadModel)) {
            MessageSnapshotFlow.getImpl().inflow(MessageSnapshotTaker.catchWarn(n, fileDownloadModel.getSoFar(), fileDownloadModel.getTotal(), bl));
            return true;
        }
        return false;
    }

    public static interface ConnectionCountAdapter {
        public int determineConnectionCount(int var1, String var2, String var3, long var4);
    }

    public static interface ConnectionCreator {
        public FileDownloadConnection create(String var1) throws IOException;
    }

    public static interface DatabaseCustomMaker {
        public FileDownloadDatabase customMake();
    }

    public static interface IdGenerator {
        public int generateId(String var1, String var2, boolean var3);

        public int transOldId(int var1, String var2, String var3, boolean var4);
    }

    public static interface OutputStreamCreator {
        public FileDownloadOutputStream create(File var1) throws IOException;

        public boolean supportSeek();
    }
}

