/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  android.text.TextUtils
 *  android.util.SparseArray
 */
package com.liulishuo.filedownloader.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.SparseArray;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.database.SqliteDatabaseOpenHelper;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SqliteDatabaseImpl
implements FileDownloadDatabase {
    public static final String CONNECTION_TABLE_NAME = "filedownloaderConnection";
    public static final String TABLE_NAME = "filedownloader";
    private final SQLiteDatabase db = new SqliteDatabaseOpenHelper(FileDownloadHelper.getAppContext()).getWritableDatabase();

    private static FileDownloadModel createFromCursor(Cursor cursor) {
        FileDownloadModel fileDownloadModel = new FileDownloadModel();
        fileDownloadModel.setId(cursor.getInt(cursor.getColumnIndex("_id")));
        fileDownloadModel.setUrl(cursor.getString(cursor.getColumnIndex("url")));
        String string2 = cursor.getString(cursor.getColumnIndex("path"));
        short s = cursor.getShort(cursor.getColumnIndex("pathAsDirectory"));
        boolean bl = true;
        if (s != 1) {
            bl = false;
        }
        fileDownloadModel.setPath(string2, bl);
        fileDownloadModel.setStatus((byte)cursor.getShort(cursor.getColumnIndex("status")));
        fileDownloadModel.setSoFar(cursor.getLong(cursor.getColumnIndex("sofar")));
        fileDownloadModel.setTotal(cursor.getLong(cursor.getColumnIndex("total")));
        fileDownloadModel.setErrMsg(cursor.getString(cursor.getColumnIndex("errMsg")));
        fileDownloadModel.setETag(cursor.getString(cursor.getColumnIndex("etag")));
        fileDownloadModel.setFilename(cursor.getString(cursor.getColumnIndex("filename")));
        fileDownloadModel.setConnectionCount(cursor.getInt(cursor.getColumnIndex("connectionCount")));
        return fileDownloadModel;
    }

    public static Maker createMaker() {
        return new Maker();
    }

    private void update(int n, ContentValues contentValues) {
        this.db.update(TABLE_NAME, contentValues, "_id = ? ", new String[]{String.valueOf(n)});
    }

    @Override
    public void clear() {
        this.db.delete(TABLE_NAME, null, null);
        this.db.delete(CONNECTION_TABLE_NAME, null, null);
    }

    @Override
    public FileDownloadModel find(int n) {
        Cursor cursor;
        block6: {
            FileDownloadModel fileDownloadModel;
            block7: {
                Cursor cursor2 = null;
                try {
                    cursor2 = cursor = this.db.rawQuery(FileDownloadUtils.formatString("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, "_id"), new String[]{Integer.toString(n)});
                }
                catch (Throwable throwable) {
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    throw throwable;
                }
                if (!cursor.moveToNext()) break block6;
                cursor2 = cursor;
                fileDownloadModel = SqliteDatabaseImpl.createFromCursor(cursor);
                if (cursor == null) break block7;
                cursor.close();
            }
            return fileDownloadModel;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    @Override
    public List<ConnectionModel> findConnectionModel(int n) {
        Cursor cursor;
        ArrayList<ConnectionModel> arrayList = new ArrayList<ConnectionModel>();
        Cursor cursor2 = null;
        try {
            cursor = this.db.rawQuery(FileDownloadUtils.formatString("SELECT * FROM %s WHERE %s = ?", CONNECTION_TABLE_NAME, "id"), new String[]{Integer.toString(n)});
        }
        catch (Throwable throwable) {
            block13: {
                if (cursor2 == null) break block13;
                cursor2.close();
            }
            throw throwable;
        }
        while (true) {
            cursor2 = cursor;
            if (!cursor.moveToNext()) break;
            cursor2 = cursor;
            cursor2 = cursor;
            ConnectionModel connectionModel = new ConnectionModel();
            cursor2 = cursor;
            connectionModel.setId(n);
            cursor2 = cursor;
            connectionModel.setIndex(cursor.getInt(cursor.getColumnIndex("connectionIndex")));
            cursor2 = cursor;
            connectionModel.setStartOffset(cursor.getLong(cursor.getColumnIndex("startOffset")));
            cursor2 = cursor;
            connectionModel.setCurrentOffset(cursor.getLong(cursor.getColumnIndex("currentOffset")));
            cursor2 = cursor;
            connectionModel.setEndOffset(cursor.getLong(cursor.getColumnIndex("endOffset")));
            cursor2 = cursor;
            arrayList.add(connectionModel);
            continue;
            break;
        }
        if (cursor != null) {
            cursor.close();
        }
        return arrayList;
    }

    @Override
    public void insert(FileDownloadModel fileDownloadModel) {
        this.db.insert(TABLE_NAME, null, fileDownloadModel.toContentValues());
    }

    @Override
    public void insertConnectionModel(ConnectionModel connectionModel) {
        this.db.insert(CONNECTION_TABLE_NAME, null, connectionModel.toContentValues());
    }

    @Override
    public FileDownloadDatabase.Maintainer maintainer() {
        return new Maintainer(this);
    }

    public FileDownloadDatabase.Maintainer maintainer(SparseArray<FileDownloadModel> sparseArray, SparseArray<List<ConnectionModel>> sparseArray2) {
        return new Maintainer(this, sparseArray, sparseArray2);
    }

    @Override
    public void onTaskStart(int n) {
    }

    @Override
    public boolean remove(int n) {
        SQLiteDatabase sQLiteDatabase = this.db;
        boolean bl = true;
        if (sQLiteDatabase.delete(TABLE_NAME, "_id = ?", new String[]{String.valueOf(n)}) == 0) {
            bl = false;
        }
        return bl;
    }

    @Override
    public void removeConnections(int n) {
        SQLiteDatabase sQLiteDatabase = this.db;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM filedownloaderConnection WHERE id = ");
        stringBuilder.append(n);
        sQLiteDatabase.execSQL(stringBuilder.toString());
    }

    @Override
    public void update(FileDownloadModel fileDownloadModel) {
        if (fileDownloadModel == null) {
            FileDownloadLog.w(this, "update but model == null!", new Object[0]);
            return;
        }
        if (this.find(fileDownloadModel.getId()) != null) {
            ContentValues contentValues = fileDownloadModel.toContentValues();
            this.db.update(TABLE_NAME, contentValues, "_id = ? ", new String[]{String.valueOf(fileDownloadModel.getId())});
        } else {
            this.insert(fileDownloadModel);
        }
    }

    @Override
    public void updateCompleted(int n, long l) {
        this.remove(n);
    }

    @Override
    public void updateConnected(int n, long l, String string2, String string3) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", Byte.valueOf((byte)2));
        contentValues.put("total", Long.valueOf(l));
        contentValues.put("etag", string2);
        contentValues.put("filename", string3);
        this.update(n, contentValues);
    }

    @Override
    public void updateConnectionCount(int n, int n2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("connectionCount", Integer.valueOf(n2));
        this.db.update(TABLE_NAME, contentValues, "_id = ? ", new String[]{Integer.toString(n)});
    }

    @Override
    public void updateConnectionModel(int n, int n2, long l) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("currentOffset", Long.valueOf(l));
        this.db.update(CONNECTION_TABLE_NAME, contentValues, "id = ? AND connectionIndex = ?", new String[]{Integer.toString(n), Integer.toString(n2)});
    }

    @Override
    public void updateError(int n, Throwable throwable, long l) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("errMsg", throwable.toString());
        contentValues.put("status", Byte.valueOf((byte)-1));
        contentValues.put("sofar", Long.valueOf(l));
        this.update(n, contentValues);
    }

    @Override
    public void updateOldEtagOverdue(int n, String string2, long l, long l2, int n2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("sofar", Long.valueOf(l));
        contentValues.put("total", Long.valueOf(l2));
        contentValues.put("etag", string2);
        contentValues.put("connectionCount", Integer.valueOf(n2));
        this.update(n, contentValues);
    }

    @Override
    public void updatePause(int n, long l) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", Byte.valueOf((byte)-2));
        contentValues.put("sofar", Long.valueOf(l));
        this.update(n, contentValues);
    }

    @Override
    public void updatePending(int n) {
    }

    @Override
    public void updateProgress(int n, long l) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", Byte.valueOf((byte)3));
        contentValues.put("sofar", Long.valueOf(l));
        this.update(n, contentValues);
    }

    @Override
    public void updateRetry(int n, Throwable throwable) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("errMsg", throwable.toString());
        contentValues.put("status", Byte.valueOf((byte)5));
        this.update(n, contentValues);
    }

    public class Maintainer
    implements FileDownloadDatabase.Maintainer {
        private final SparseArray<List<ConnectionModel>> connectionModelListMap;
        private MaintainerIterator currentIterator;
        private final SparseArray<FileDownloadModel> downloaderModelMap;
        private final SparseArray<FileDownloadModel> needChangeIdList;
        final SqliteDatabaseImpl this$0;

        Maintainer(SqliteDatabaseImpl sqliteDatabaseImpl) {
            this(sqliteDatabaseImpl, null, null);
        }

        Maintainer(SqliteDatabaseImpl sqliteDatabaseImpl, SparseArray<FileDownloadModel> sparseArray, SparseArray<List<ConnectionModel>> sparseArray2) {
            this.this$0 = sqliteDatabaseImpl;
            this.needChangeIdList = new SparseArray();
            this.downloaderModelMap = sparseArray;
            this.connectionModelListMap = sparseArray2;
        }

        @Override
        public void changeFileDownloadModelId(int n, FileDownloadModel fileDownloadModel) {
            this.needChangeIdList.put(n, (Object)fileDownloadModel);
        }

        @Override
        public Iterator<FileDownloadModel> iterator() {
            MaintainerIterator maintainerIterator;
            this.currentIterator = maintainerIterator = new MaintainerIterator(this.this$0);
            return maintainerIterator;
        }

        @Override
        public void onFinishMaintain() {
            block12: {
                int n;
                int n2;
                int n3;
                Object object = this.currentIterator;
                if (object != null) {
                    ((MaintainerIterator)object).onFinishMaintain();
                }
                if ((n3 = this.needChangeIdList.size()) < 0) {
                    return;
                }
                this.this$0.db.beginTransaction();
                for (n2 = 0; n2 < n3; ++n2) {
                    Object object2;
                    n = this.needChangeIdList.keyAt(n2);
                    object = (FileDownloadModel)this.needChangeIdList.get(n);
                    this.this$0.db.delete(SqliteDatabaseImpl.TABLE_NAME, "_id = ?", new String[]{String.valueOf(n)});
                    this.this$0.db.insert(SqliteDatabaseImpl.TABLE_NAME, null, ((FileDownloadModel)object).toContentValues());
                    if (((FileDownloadModel)object).getConnectionCount() <= 1 || (object2 = this.this$0.findConnectionModel(n)).size() <= 0) continue;
                    this.this$0.db.delete(SqliteDatabaseImpl.CONNECTION_TABLE_NAME, "id = ?", new String[]{String.valueOf(n)});
                    Iterator<ConnectionModel> iterator2 = object2.iterator();
                    while (iterator2.hasNext()) {
                        object2 = iterator2.next();
                        ((ConnectionModel)object2).setId(((FileDownloadModel)object).getId());
                        this.this$0.db.insert(SqliteDatabaseImpl.CONNECTION_TABLE_NAME, null, ((ConnectionModel)object2).toContentValues());
                    }
                    continue;
                }
                object = this.downloaderModelMap;
                if (object == null) break block12;
                if (this.connectionModelListMap == null) break block12;
                n3 = object.size();
                for (n2 = 0; n2 < n3; ++n2) {
                    n = ((FileDownloadModel)this.downloaderModelMap.valueAt(n2)).getId();
                    object = this.this$0.findConnectionModel(n);
                    if (object == null) continue;
                    if (object.size() <= 0) continue;
                    this.connectionModelListMap.put(n, object);
                }
            }
            try {
                this.this$0.db.setTransactionSuccessful();
            }
            catch (Throwable throwable) {
                this.this$0.db.endTransaction();
                throw throwable;
            }
            this.this$0.db.endTransaction();
        }

        @Override
        public void onRefreshedValidData(FileDownloadModel fileDownloadModel) {
            SparseArray<FileDownloadModel> sparseArray = this.downloaderModelMap;
            if (sparseArray != null) {
                sparseArray.put(fileDownloadModel.getId(), (Object)fileDownloadModel);
            }
        }

        @Override
        public void onRemovedInvalidData(FileDownloadModel fileDownloadModel) {
        }
    }

    class MaintainerIterator
    implements Iterator<FileDownloadModel> {
        private final Cursor c;
        private int currentId;
        private final List<Integer> needRemoveId;
        final SqliteDatabaseImpl this$0;

        MaintainerIterator(SqliteDatabaseImpl sqliteDatabaseImpl) {
            this.this$0 = sqliteDatabaseImpl;
            this.needRemoveId = new ArrayList<Integer>();
            this.c = sqliteDatabaseImpl.db.rawQuery("SELECT * FROM filedownloader", null);
        }

        @Override
        public boolean hasNext() {
            return this.c.moveToNext();
        }

        @Override
        public FileDownloadModel next() {
            FileDownloadModel fileDownloadModel = SqliteDatabaseImpl.createFromCursor(this.c);
            this.currentId = fileDownloadModel.getId();
            return fileDownloadModel;
        }

        void onFinishMaintain() {
            this.c.close();
            if (!this.needRemoveId.isEmpty()) {
                String string2 = TextUtils.join((CharSequence)", ", this.needRemoveId);
                if (FileDownloadLog.NEED_LOG) {
                    FileDownloadLog.d(this, "delete %s", string2);
                }
                this.this$0.db.execSQL(FileDownloadUtils.formatString("DELETE FROM %s WHERE %s IN (%s);", SqliteDatabaseImpl.TABLE_NAME, "_id", string2));
                this.this$0.db.execSQL(FileDownloadUtils.formatString("DELETE FROM %s WHERE %s IN (%s);", SqliteDatabaseImpl.CONNECTION_TABLE_NAME, "id", string2));
            }
        }

        @Override
        public void remove() {
            this.needRemoveId.add(this.currentId);
        }
    }

    public static class Maker
    implements FileDownloadHelper.DatabaseCustomMaker {
        @Override
        public FileDownloadDatabase customMake() {
            return new SqliteDatabaseImpl();
        }
    }
}

