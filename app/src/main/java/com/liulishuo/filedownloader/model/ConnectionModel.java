/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 */
package com.liulishuo.filedownloader.model;

import android.content.ContentValues;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.util.Iterator;
import java.util.List;

public class ConnectionModel {
    public static final String CURRENT_OFFSET = "currentOffset";
    public static final String END_OFFSET = "endOffset";
    public static final String ID = "id";
    public static final String INDEX = "connectionIndex";
    public static final String START_OFFSET = "startOffset";
    private long currentOffset;
    private long endOffset;
    private int id;
    private int index;
    private long startOffset;

    public static long getTotalOffset(List<ConnectionModel> object) {
        long l = 0L;
        Iterator<ConnectionModel> iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            object = iterator2.next();
            l += ((ConnectionModel)object).getCurrentOffset() - ((ConnectionModel)object).getStartOffset();
        }
        return l;
    }

    public long getCurrentOffset() {
        return this.currentOffset;
    }

    public long getEndOffset() {
        return this.endOffset;
    }

    public int getId() {
        return this.id;
    }

    public int getIndex() {
        return this.index;
    }

    public long getStartOffset() {
        return this.startOffset;
    }

    public void setCurrentOffset(long l) {
        this.currentOffset = l;
    }

    public void setEndOffset(long l) {
        this.endOffset = l;
    }

    public void setId(int n) {
        this.id = n;
    }

    public void setIndex(int n) {
        this.index = n;
    }

    public void setStartOffset(long l) {
        this.startOffset = l;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, Integer.valueOf(this.id));
        contentValues.put(INDEX, Integer.valueOf(this.index));
        contentValues.put(START_OFFSET, Long.valueOf(this.startOffset));
        contentValues.put(CURRENT_OFFSET, Long.valueOf(this.currentOffset));
        contentValues.put(END_OFFSET, Long.valueOf(this.endOffset));
        return contentValues;
    }

    public String toString() {
        return FileDownloadUtils.formatString("id[%d] index[%d] range[%d, %d) current offset(%d)", this.id, this.index, this.startOffset, this.endOffset, this.currentOffset);
    }
}

