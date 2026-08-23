/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 */
package com.downloader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.downloader.database.DatabaseOpenHelper;
import com.downloader.database.DbHelper;
import com.downloader.database.DownloadModel;
import java.util.ArrayList;
import java.util.List;

public class AppDbHelper
implements DbHelper {
    public static final String TABLE_NAME = "prdownloader";
    private final SQLiteDatabase db;

    public AppDbHelper(Context context) {
        this.db = new DatabaseOpenHelper(context).getWritableDatabase();
    }

    @Override
    public void clear() {
        try {
            this.db.delete(TABLE_NAME, null, null);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public DownloadModel find(int n) {
        Throwable throwable2;
        Cursor cursor;
        block6: {
            Cursor cursor2 = null;
            Object object = null;
            Cursor cursor3 = null;
            Object var8_6 = null;
            cursor = object;
            Cursor cursor4 = cursor2;
            Object object2 = cursor3;
            SQLiteDatabase sQLiteDatabase = this.db;
            cursor = object;
            cursor4 = cursor2;
            object2 = cursor3;
            cursor = object;
            cursor4 = cursor2;
            object2 = cursor3;
            StringBuilder stringBuilder = new StringBuilder();
            cursor = object;
            cursor4 = cursor2;
            object2 = cursor3;
            stringBuilder.append("SELECT * FROM prdownloader WHERE id = ");
            cursor = object;
            cursor4 = cursor2;
            object2 = cursor3;
            stringBuilder.append(n);
            cursor = object;
            cursor4 = cursor2;
            object2 = cursor3;
            cursor2 = sQLiteDatabase.rawQuery(stringBuilder.toString(), null);
            object = var8_6;
            if (cursor2 != null) {
                object = var8_6;
                cursor = cursor2;
                cursor4 = cursor2;
                object2 = cursor3;
                if (cursor2.moveToFirst()) {
                    cursor = cursor2;
                    cursor4 = cursor2;
                    object2 = cursor3;
                    cursor = cursor2;
                    cursor4 = cursor2;
                    object2 = cursor3;
                    object = new DownloadModel();
                    cursor = cursor2;
                    cursor4 = cursor2;
                    object2 = object;
                    ((DownloadModel)object).setId(n);
                    cursor = cursor2;
                    cursor4 = cursor2;
                    object2 = object;
                    ((DownloadModel)object).setUrl(cursor2.getString(cursor2.getColumnIndex("url")));
                    cursor = cursor2;
                    cursor4 = cursor2;
                    object2 = object;
                    ((DownloadModel)object).setETag(cursor2.getString(cursor2.getColumnIndex("etag")));
                    cursor = cursor2;
                    cursor4 = cursor2;
                    object2 = object;
                    ((DownloadModel)object).setDirPath(cursor2.getString(cursor2.getColumnIndex("dir_path")));
                    cursor = cursor2;
                    cursor4 = cursor2;
                    object2 = object;
                    ((DownloadModel)object).setFileName(cursor2.getString(cursor2.getColumnIndex("file_name")));
                    cursor = cursor2;
                    cursor4 = cursor2;
                    object2 = object;
                    ((DownloadModel)object).setTotalBytes(cursor2.getLong(cursor2.getColumnIndex("total_bytes")));
                    cursor = cursor2;
                    cursor4 = cursor2;
                    object2 = object;
                    ((DownloadModel)object).setDownloadedBytes(cursor2.getLong(cursor2.getColumnIndex("downloaded_bytes")));
                    cursor = cursor2;
                    cursor4 = cursor2;
                    object2 = object;
                    ((DownloadModel)object).setLastModifiedAt(cursor2.getLong(cursor2.getColumnIndex("last_modified_at")));
                }
            }
            cursor = object;
            if (cursor2 == null) return cursor;
            object2 = object;
            cursor4 = cursor2;
            {
                catch (Throwable throwable2) {
                    break block6;
                }
                catch (Exception exception) {}
                cursor = cursor4;
                {
                    exception.printStackTrace();
                    cursor = object2;
                    if (cursor4 == null) return cursor;
                }
            }
            cursor4.close();
            return object2;
        }
        if (cursor == null) throw throwable2;
        cursor.close();
        throw throwable2;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public List<DownloadModel> getUnwantedModels(int n) {
        Throwable throwable2;
        Cursor cursor;
        block7: {
            ArrayList<DownloadModel> arrayList = new ArrayList<DownloadModel>();
            Cursor cursor2 = null;
            Object object = null;
            long l = n * 24 * 60 * 60;
            cursor = object;
            Cursor cursor3 = cursor2;
            long l2 = System.currentTimeMillis();
            cursor = object;
            cursor3 = cursor2;
            SQLiteDatabase sQLiteDatabase = this.db;
            cursor = object;
            cursor3 = cursor2;
            cursor = object;
            cursor3 = cursor2;
            StringBuilder stringBuilder = new StringBuilder();
            cursor = object;
            cursor3 = cursor2;
            stringBuilder.append("SELECT * FROM prdownloader WHERE last_modified_at <= ");
            cursor = object;
            cursor3 = cursor2;
            stringBuilder.append(l2 - l * 1000L);
            cursor = object;
            cursor3 = cursor2;
            cursor2 = sQLiteDatabase.rawQuery(stringBuilder.toString(), null);
            if (cursor2 != null) {
                cursor = cursor2;
                cursor3 = cursor2;
                if (cursor2.moveToFirst()) {
                    boolean bl;
                    do {
                        cursor = cursor2;
                        cursor3 = cursor2;
                        cursor = cursor2;
                        cursor3 = cursor2;
                        object = new DownloadModel();
                        cursor = cursor2;
                        cursor3 = cursor2;
                        ((DownloadModel)object).setId(cursor2.getInt(cursor2.getColumnIndex("id")));
                        cursor = cursor2;
                        cursor3 = cursor2;
                        ((DownloadModel)object).setUrl(cursor2.getString(cursor2.getColumnIndex("url")));
                        cursor = cursor2;
                        cursor3 = cursor2;
                        ((DownloadModel)object).setETag(cursor2.getString(cursor2.getColumnIndex("etag")));
                        cursor = cursor2;
                        cursor3 = cursor2;
                        ((DownloadModel)object).setDirPath(cursor2.getString(cursor2.getColumnIndex("dir_path")));
                        cursor = cursor2;
                        cursor3 = cursor2;
                        ((DownloadModel)object).setFileName(cursor2.getString(cursor2.getColumnIndex("file_name")));
                        cursor = cursor2;
                        cursor3 = cursor2;
                        ((DownloadModel)object).setTotalBytes(cursor2.getLong(cursor2.getColumnIndex("total_bytes")));
                        cursor = cursor2;
                        cursor3 = cursor2;
                        ((DownloadModel)object).setDownloadedBytes(cursor2.getLong(cursor2.getColumnIndex("downloaded_bytes")));
                        cursor = cursor2;
                        cursor3 = cursor2;
                        ((DownloadModel)object).setLastModifiedAt(cursor2.getLong(cursor2.getColumnIndex("last_modified_at")));
                        cursor = cursor2;
                        cursor3 = cursor2;
                        arrayList.add((DownloadModel)object);
                        cursor = cursor2;
                        cursor3 = cursor2;
                    } while (bl = cursor2.moveToNext());
                }
            }
            if (cursor2 == null) return arrayList;
            cursor3 = cursor2;
            {
                catch (Throwable throwable2) {
                    break block7;
                }
                catch (Exception exception) {}
                cursor = cursor3;
                {
                    exception.printStackTrace();
                    if (cursor3 == null) return arrayList;
                }
            }
            cursor3.close();
            return arrayList;
        }
        if (cursor == null) throw throwable2;
        cursor.close();
        throw throwable2;
    }

    @Override
    public void insert(DownloadModel downloadModel) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", Integer.valueOf(downloadModel.getId()));
            contentValues.put("url", downloadModel.getUrl());
            contentValues.put("etag", downloadModel.getETag());
            contentValues.put("dir_path", downloadModel.getDirPath());
            contentValues.put("file_name", downloadModel.getFileName());
            contentValues.put("total_bytes", Long.valueOf(downloadModel.getTotalBytes()));
            contentValues.put("downloaded_bytes", Long.valueOf(downloadModel.getDownloadedBytes()));
            contentValues.put("last_modified_at", Long.valueOf(downloadModel.getLastModifiedAt()));
            this.db.insert(TABLE_NAME, null, contentValues);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void remove(int n) {
        try {
            SQLiteDatabase sQLiteDatabase = this.db;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DELETE FROM prdownloader WHERE id = ");
            stringBuilder.append(n);
            sQLiteDatabase.execSQL(stringBuilder.toString());
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void update(DownloadModel downloadModel) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("url", downloadModel.getUrl());
            contentValues.put("etag", downloadModel.getETag());
            contentValues.put("dir_path", downloadModel.getDirPath());
            contentValues.put("file_name", downloadModel.getFileName());
            contentValues.put("total_bytes", Long.valueOf(downloadModel.getTotalBytes()));
            contentValues.put("downloaded_bytes", Long.valueOf(downloadModel.getDownloadedBytes()));
            contentValues.put("last_modified_at", Long.valueOf(downloadModel.getLastModifiedAt()));
            this.db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{String.valueOf(downloadModel.getId())});
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void updateProgress(int n, long l, long l2) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("downloaded_bytes", Long.valueOf(l));
            contentValues.put("last_modified_at", Long.valueOf(l2));
            this.db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{String.valueOf(n)});
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

