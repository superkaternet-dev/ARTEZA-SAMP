/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.utils;

import com.downloader.core.Core;
import com.downloader.database.DownloadModel;
import com.downloader.httpclient.HttpClient;
import com.downloader.internal.ComponentHolder;
import com.downloader.request.DownloadRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public final class Utils {
    private static final int MAX_REDIRECTION = 10;

    private Utils() {
    }

    public static void deleteTempFileAndDatabaseEntryInBackground(String string2, int n) {
        Core.getInstance().getExecutorSupplier().forBackgroundTasks().execute(new Runnable(n, string2){
            final int val$downloadId;
            final String val$path;
            {
                this.val$downloadId = n;
                this.val$path = string2;
            }

            @Override
            public void run() {
                ComponentHolder.getInstance().getDbHelper().remove(this.val$downloadId);
                File file = new File(this.val$path);
                if (file.exists()) {
                    file.delete();
                }
            }
        });
    }

    public static void deleteUnwantedModelsAndTempFiles(int n) {
        Core.getInstance().getExecutorSupplier().forBackgroundTasks().execute(new Runnable(n){
            final int val$days;
            {
                this.val$days = n;
            }

            @Override
            public void run() {
                List<DownloadModel> list = ComponentHolder.getInstance().getDbHelper().getUnwantedModels(this.val$days);
                if (list != null) {
                    for (DownloadModel downloadModel : list) {
                        String string2 = Utils.getTempPath(downloadModel.getDirPath(), downloadModel.getFileName());
                        ComponentHolder.getInstance().getDbHelper().remove(downloadModel.getId());
                        File object = new File(string2);
                        if (!object.exists()) continue;
                        object.delete();
                    }
                }
            }
        });
    }

    public static String getPath(String string2, String string3) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(File.separator);
        stringBuilder.append(string3);
        return stringBuilder.toString();
    }

    public static HttpClient getRedirectedConnectionIfAny(HttpClient object, DownloadRequest downloadRequest) throws IOException, IllegalAccessException {
        int n = 0;
        int n2 = object.getResponseCode();
        String string2 = object.getResponseHeader("Location");
        HttpClient httpClient = object;
        object = string2;
        while (Utils.isRedirection(n2)) {
            if (object != null) {
                httpClient.close();
                downloadRequest.setUrl((String)object);
                httpClient = ComponentHolder.getInstance().getHttpClient();
                httpClient.connect(downloadRequest);
                n2 = httpClient.getResponseCode();
                object = httpClient.getResponseHeader("Location");
                if (++n < 10) continue;
                throw new IllegalAccessException("Max redirection done");
            }
            throw new IllegalAccessException("Location is null");
        }
        return httpClient;
    }

    public static String getTempPath(String string2, String string3) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Utils.getPath(string2, string3));
        stringBuilder.append(".temp");
        return stringBuilder.toString();
    }

    public static int getUniqueId(String charSequence, String object, String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((String)charSequence);
        stringBuilder.append(File.separator);
        stringBuilder.append((String)object);
        stringBuilder.append(File.separator);
        stringBuilder.append(string2);
        charSequence = stringBuilder.toString();
        try {
            object = MessageDigest.getInstance("MD5").digest(((String)charSequence).getBytes("UTF-8"));
            charSequence = new StringBuilder(((Object)object).length * 2);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RuntimeException("UnsupportedEncodingException", unsupportedEncodingException);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            RuntimeException runtimeException = new RuntimeException("NoSuchAlgorithmException", noSuchAlgorithmException);
            throw runtimeException;
        }
        for (Object object2 : object) {
            if ((object2 & 0xFF) < 16) {
                ((StringBuilder)charSequence).append("0");
            }
            ((StringBuilder)charSequence).append(Integer.toHexString(object2 & 0xFF));
        }
        return ((StringBuilder)charSequence).toString().hashCode();
    }

    private static boolean isRedirection(int n) {
        boolean bl = n == 301 || n == 302 || n == 303 || n == 300 || n == 307 || n == 308;
        return bl;
    }

    public static void renameFileName(String object, String object2) throws IOException {
        block5: {
            block6: {
                object = new File((String)object);
                try {
                    File file = new File((String)object2);
                    if (file.exists() && !file.delete()) {
                        object2 = new IOException("Deletion Failed");
                        throw object2;
                    }
                    boolean bl = ((File)object).renameTo(file);
                    if (!bl) break block5;
                    if (!((File)object).exists()) break block6;
                    ((File)object).delete();
                }
                catch (Throwable throwable) {
                    if (((File)object).exists()) {
                        ((File)object).delete();
                    }
                    throw throwable;
                }
            }
            return;
        }
        object2 = new IOException("Rename Failed");
        throw object2;
    }
}

