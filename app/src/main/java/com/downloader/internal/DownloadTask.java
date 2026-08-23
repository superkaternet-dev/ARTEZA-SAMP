/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.internal;

import com.downloader.Progress;
import com.downloader.Response;
import com.downloader.Status;
import com.downloader.database.DownloadModel;
import com.downloader.handler.ProgressHandler;
import com.downloader.httpclient.HttpClient;
import com.downloader.internal.ComponentHolder;
import com.downloader.internal.stream.FileDownloadOutputStream;
import com.downloader.request.DownloadRequest;
import com.downloader.utils.Utils;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DownloadTask {
    private static final int BUFFER_SIZE = 4096;
    private static final long MIN_BYTES_FOR_SYNC = 65536L;
    private static final long TIME_GAP_FOR_SYNC = 2000L;
    private String eTag;
    private HttpClient httpClient;
    private InputStream inputStream;
    private boolean isResumeSupported;
    private long lastSyncBytes;
    private long lastSyncTime;
    private FileDownloadOutputStream outputStream;
    private ProgressHandler progressHandler;
    private final DownloadRequest request;
    private int responseCode;
    private String tempPath;
    private long totalBytes;

    private DownloadTask(DownloadRequest downloadRequest) {
        this.request = downloadRequest;
    }

    private boolean checkIfFreshStartRequiredAndStart(DownloadModel object) throws IOException, IllegalAccessException {
        if (this.responseCode != 416 && !this.isETagChanged((DownloadModel)object)) {
            return false;
        }
        if (object != null) {
            this.removeNoMoreNeededModelFromDatabase();
        }
        this.deleteTempFile();
        this.request.setDownloadedBytes(0L);
        this.request.setTotalBytes(0L);
        this.httpClient = object = ComponentHolder.getInstance().getHttpClient();
        object.connect(this.request);
        this.httpClient = object = Utils.getRedirectedConnectionIfAny(this.httpClient, this.request);
        this.responseCode = object.getResponseCode();
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void closeAllSafely(FileDownloadOutputStream fileDownloadOutputStream) {
        block15: {
            Object object = this.httpClient;
            if (object != null) {
                try {
                    object.close();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            if ((object = this.inputStream) != null) {
                try {
                    ((InputStream)object).close();
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                }
            }
            if (fileDownloadOutputStream != null) {
                try {
                    try {
                        this.sync(fileDownloadOutputStream);
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    break block15;
                }
                catch (Throwable throwable) {}
                if (fileDownloadOutputStream == null) throw throwable;
                try {
                    fileDownloadOutputStream.close();
                    throw throwable;
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                }
                throw throwable;
            }
        }
        if (fileDownloadOutputStream == null) return;
        try {
            fileDownloadOutputStream.close();
            return;
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private String convertStreamToString(InputStream closeable) {
        StringBuilder stringBuilder = new StringBuilder();
        if (closeable == null) return stringBuilder.toString();
        BufferedReader bufferedReader = null;
        Object object = null;
        Closeable closeable2 = object;
        Closeable closeable3 = bufferedReader;
        try {
            closeable2 = object;
            closeable3 = bufferedReader;
            closeable2 = object;
            closeable3 = bufferedReader;
            InputStreamReader inputStreamReader = new InputStreamReader((InputStream)closeable);
            closeable2 = object;
            closeable3 = bufferedReader;
            BufferedReader bufferedReader2 = new BufferedReader(inputStreamReader);
            closeable = bufferedReader2;
            while (true) {
                closeable2 = closeable;
                closeable3 = closeable;
                object = ((BufferedReader)closeable).readLine();
                if (object != null) {
                    closeable2 = closeable;
                    closeable3 = closeable;
                    stringBuilder.append((String)object);
                    continue;
                }
                break;
            }
        }
        catch (Throwable throwable) {
            if (closeable2 == null) throw throwable;
            try {
                closeable2.close();
                throw throwable;
            }
            catch (IOException iOException) {
                throw throwable;
            }
            catch (NullPointerException nullPointerException) {
                // empty catch block
            }
            throw throwable;
        }
        catch (IOException iOException) {
            if (closeable3 == null) return stringBuilder.toString();
            try {
                closeable3.close();
                return stringBuilder.toString();
            }
            catch (IOException iOException2) {
                return stringBuilder.toString();
            }
            catch (NullPointerException nullPointerException) {
                // empty catch block
                return stringBuilder.toString();
            }
        }
        try {
            ((BufferedReader)closeable).close();
            return stringBuilder.toString();
        }
        catch (IOException iOException) {
            return stringBuilder.toString();
        }
        catch (NullPointerException nullPointerException) {
            return stringBuilder.toString();
        }
    }

    static DownloadTask create(DownloadRequest downloadRequest) {
        return new DownloadTask(downloadRequest);
    }

    private void createAndInsertNewModel() {
        DownloadModel downloadModel = new DownloadModel();
        downloadModel.setId(this.request.getDownloadId());
        downloadModel.setUrl(this.request.getUrl());
        downloadModel.setETag(this.eTag);
        downloadModel.setDirPath(this.request.getDirPath());
        downloadModel.setFileName(this.request.getFileName());
        downloadModel.setDownloadedBytes(this.request.getDownloadedBytes());
        downloadModel.setTotalBytes(this.totalBytes);
        downloadModel.setLastModifiedAt(System.currentTimeMillis());
        ComponentHolder.getInstance().getDbHelper().insert(downloadModel);
    }

    private void deleteTempFile() {
        File file = new File(this.tempPath);
        if (file.exists()) {
            file.delete();
        }
    }

    private DownloadModel getDownloadModelIfAlreadyPresentInDatabase() {
        return ComponentHolder.getInstance().getDbHelper().find(this.request.getDownloadId());
    }

    private boolean isETagChanged(DownloadModel downloadModel) {
        boolean bl = this.eTag != null && downloadModel != null && downloadModel.getETag() != null && !downloadModel.getETag().equals(this.eTag);
        return bl;
    }

    private boolean isSuccessful() {
        int n = this.responseCode;
        boolean bl = n >= 200 && n < 300;
        return bl;
    }

    private void removeNoMoreNeededModelFromDatabase() {
        ComponentHolder.getInstance().getDbHelper().remove(this.request.getDownloadId());
    }

    private void sendProgress() {
        ProgressHandler progressHandler;
        if (this.request.getStatus() != Status.CANCELLED && (progressHandler = this.progressHandler) != null) {
            progressHandler.obtainMessage(1, new Progress(this.request.getDownloadedBytes(), this.totalBytes)).sendToTarget();
        }
    }

    private void setResumeSupportedOrNot() {
        boolean bl = this.responseCode == 206;
        this.isResumeSupported = bl;
    }

    private void sync(FileDownloadOutputStream fileDownloadOutputStream) {
        boolean bl;
        try {
            fileDownloadOutputStream.flushAndSync();
            bl = true;
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            bl = false;
        }
        if (bl && this.isResumeSupported) {
            ComponentHolder.getInstance().getDbHelper().updateProgress(this.request.getDownloadId(), this.request.getDownloadedBytes(), System.currentTimeMillis());
        }
    }

    private void syncIfRequired(FileDownloadOutputStream fileDownloadOutputStream) {
        long l = this.request.getDownloadedBytes();
        long l2 = System.currentTimeMillis();
        long l3 = this.lastSyncBytes;
        long l4 = this.lastSyncTime;
        if (l - l3 > 65536L && l2 - l4 > 2000L) {
            this.sync(fileDownloadOutputStream);
            this.lastSyncBytes = l;
            this.lastSyncTime = l2;
        }
    }

    /*
     * Exception decompiling
     */
    Response run() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 16[TRYBLOCK] [72 : 939->980)] java.lang.Throwable
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.insertExceptionBlocks(Op02WithProcessedDataAndRefs.java:2283)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:415)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }
}

