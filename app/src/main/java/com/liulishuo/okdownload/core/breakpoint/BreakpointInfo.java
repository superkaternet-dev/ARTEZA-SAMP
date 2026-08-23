/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.breakpoint;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.download.DownloadStrategy;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BreakpointInfo {
    private final List<BlockInfo> blockInfoList;
    private boolean chunked;
    private String etag;
    private final DownloadStrategy.FilenameHolder filenameHolder;
    final int id;
    final File parentFile;
    private File targetFile;
    private final boolean taskOnlyProvidedParentPath;
    private final String url;

    public BreakpointInfo(int n, String string2, File file, String string3) {
        this.id = n;
        this.url = string2;
        this.parentFile = file;
        this.blockInfoList = new ArrayList<BlockInfo>();
        if (Util.isEmpty(string3)) {
            this.filenameHolder = new DownloadStrategy.FilenameHolder();
            this.taskOnlyProvidedParentPath = true;
        } else {
            this.filenameHolder = new DownloadStrategy.FilenameHolder(string3);
            this.taskOnlyProvidedParentPath = false;
            this.targetFile = new File(file, string3);
        }
    }

    BreakpointInfo(int n, String string2, File file, String string3, boolean bl) {
        this.id = n;
        this.url = string2;
        this.parentFile = file;
        this.blockInfoList = new ArrayList<BlockInfo>();
        this.filenameHolder = Util.isEmpty(string3) ? new DownloadStrategy.FilenameHolder() : new DownloadStrategy.FilenameHolder(string3);
        this.taskOnlyProvidedParentPath = bl;
    }

    public void addBlock(BlockInfo blockInfo) {
        this.blockInfoList.add(blockInfo);
    }

    public BreakpointInfo copy() {
        BreakpointInfo breakpointInfo = new BreakpointInfo(this.id, this.url, this.parentFile, this.filenameHolder.get(), this.taskOnlyProvidedParentPath);
        breakpointInfo.chunked = this.chunked;
        for (BlockInfo blockInfo : this.blockInfoList) {
            breakpointInfo.blockInfoList.add(blockInfo.copy());
        }
        return breakpointInfo;
    }

    public BreakpointInfo copyWithReplaceId(int n) {
        BreakpointInfo breakpointInfo = new BreakpointInfo(n, this.url, this.parentFile, this.filenameHolder.get(), this.taskOnlyProvidedParentPath);
        breakpointInfo.chunked = this.chunked;
        for (BlockInfo blockInfo : this.blockInfoList) {
            breakpointInfo.blockInfoList.add(blockInfo.copy());
        }
        return breakpointInfo;
    }

    public BreakpointInfo copyWithReplaceIdAndUrl(int n, String object) {
        object = new BreakpointInfo(n, (String)object, this.parentFile, this.filenameHolder.get(), this.taskOnlyProvidedParentPath);
        ((BreakpointInfo)object).chunked = this.chunked;
        for (BlockInfo blockInfo : this.blockInfoList) {
            ((BreakpointInfo)object).blockInfoList.add(blockInfo.copy());
        }
        return object;
    }

    public BlockInfo getBlock(int n) {
        return this.blockInfoList.get(n);
    }

    public int getBlockCount() {
        return this.blockInfoList.size();
    }

    public String getEtag() {
        return this.etag;
    }

    public File getFile() {
        String string2 = this.filenameHolder.get();
        if (string2 == null) {
            return null;
        }
        if (this.targetFile == null) {
            this.targetFile = new File(this.parentFile, string2);
        }
        return this.targetFile;
    }

    public String getFilename() {
        return this.filenameHolder.get();
    }

    public DownloadStrategy.FilenameHolder getFilenameHolder() {
        return this.filenameHolder;
    }

    public int getId() {
        return this.id;
    }

    public long getTotalLength() {
        if (this.isChunked()) {
            return this.getTotalOffset();
        }
        long l = 0L;
        Iterator iterator2 = ((ArrayList)((ArrayList)this.blockInfoList).clone()).iterator();
        while (iterator2.hasNext()) {
            l += ((BlockInfo)iterator2.next()).getContentLength();
        }
        return l;
    }

    public long getTotalOffset() {
        long l = 0L;
        ArrayList arrayList = (ArrayList)((ArrayList)this.blockInfoList).clone();
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            l += ((BlockInfo)arrayList.get(i)).getCurrentOffset();
        }
        return l;
    }

    public String getUrl() {
        return this.url;
    }

    public boolean isChunked() {
        return this.chunked;
    }

    public boolean isLastBlock(int n) {
        int n2 = this.blockInfoList.size();
        boolean bl = true;
        if (n != n2 - 1) {
            bl = false;
        }
        return bl;
    }

    public boolean isSameFrom(DownloadTask downloadTask) {
        boolean bl = this.parentFile.equals(downloadTask.getParentFile());
        boolean bl2 = false;
        if (!bl) {
            return false;
        }
        if (!this.url.equals(downloadTask.getUrl())) {
            return false;
        }
        String string2 = downloadTask.getFilename();
        if (string2 != null && string2.equals(this.filenameHolder.get())) {
            return true;
        }
        if (this.taskOnlyProvidedParentPath) {
            if (!downloadTask.isFilenameFromResponse()) {
                return false;
            }
            if (string2 == null || string2.equals(this.filenameHolder.get())) {
                bl2 = true;
            }
            return bl2;
        }
        return false;
    }

    public boolean isSingleBlock() {
        int n = this.blockInfoList.size();
        boolean bl = true;
        if (n != 1) {
            bl = false;
        }
        return bl;
    }

    boolean isTaskOnlyProvidedParentPath() {
        return this.taskOnlyProvidedParentPath;
    }

    public void resetBlockInfos() {
        this.blockInfoList.clear();
    }

    public void resetInfo() {
        this.blockInfoList.clear();
        this.etag = null;
    }

    public void reuseBlocks(BreakpointInfo breakpointInfo) {
        this.blockInfoList.clear();
        this.blockInfoList.addAll(breakpointInfo.blockInfoList);
    }

    public void setChunked(boolean bl) {
        this.chunked = bl;
    }

    public void setEtag(String string2) {
        this.etag = string2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id[");
        stringBuilder.append(this.id);
        stringBuilder.append("]");
        stringBuilder.append(" url[");
        stringBuilder.append(this.url);
        stringBuilder.append("]");
        stringBuilder.append(" etag[");
        stringBuilder.append(this.etag);
        stringBuilder.append("]");
        stringBuilder.append(" taskOnlyProvidedParentPath[");
        stringBuilder.append(this.taskOnlyProvidedParentPath);
        stringBuilder.append("]");
        stringBuilder.append(" parent path[");
        stringBuilder.append(this.parentFile);
        stringBuilder.append("]");
        stringBuilder.append(" filename[");
        stringBuilder.append(this.filenameHolder.get());
        stringBuilder.append("]");
        stringBuilder.append(" block(s):");
        stringBuilder.append(this.blockInfoList.toString());
        return stringBuilder.toString();
    }
}

