/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.connection;

import com.liulishuo.filedownloader.connection.FileDownloadConnection;
import com.liulishuo.filedownloader.download.CustomComponentHolder;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RedirectHandler {
    private static final int HTTP_PERMANENT_REDIRECT = 308;
    private static final int HTTP_TEMPORARY_REDIRECT = 307;
    private static final int MAX_REDIRECT_TIMES = 10;

    private static FileDownloadConnection buildRedirectConnection(Map<String, List<String>> object, String object2) throws IOException {
        object2 = CustomComponentHolder.getImpl().createConnection((String)object2);
        for (Map.Entry entry : object.entrySet()) {
            object = (String)entry.getKey();
            List list = (List)entry.getValue();
            if (list == null) continue;
            Iterator iterator2 = list.iterator();
            while (iterator2.hasNext()) {
                object2.addHeader((String)object, (String)iterator2.next());
            }
        }
        return object2;
    }

    private static boolean isRedirect(int n) {
        boolean bl = n == 301 || n == 302 || n == 303 || n == 300 || n == 307 || n == 308;
        return bl;
    }

    public static FileDownloadConnection process(Map<String, List<String>> map, FileDownloadConnection fileDownloadConnection, List<String> list) throws IOException, IllegalAccessException {
        int n = fileDownloadConnection.getResponseCode();
        String string2 = fileDownloadConnection.getResponseHeaderField("Location");
        ArrayList<String> arrayList = new ArrayList<String>();
        int n2 = 0;
        while (RedirectHandler.isRedirect(n)) {
            if (string2 != null) {
                if (FileDownloadLog.NEED_LOG) {
                    FileDownloadLog.d(RedirectHandler.class, "redirect to %s with %d, %s", string2, n, arrayList);
                }
                fileDownloadConnection.ending();
                fileDownloadConnection = RedirectHandler.buildRedirectConnection(map, string2);
                arrayList.add(string2);
                fileDownloadConnection.execute();
                n = fileDownloadConnection.getResponseCode();
                string2 = fileDownloadConnection.getResponseHeaderField("Location");
                if (++n2 < 10) continue;
                throw new IllegalAccessException(FileDownloadUtils.formatString("redirect too many times! %s", arrayList));
            }
            throw new IllegalAccessException(FileDownloadUtils.formatString("receive %d (redirect) but the location is null with response [%s]", n, fileDownloadConnection.getResponseHeaderFields()));
        }
        if (list != null) {
            list.addAll(arrayList);
        }
        return fileDownloadConnection;
    }
}

