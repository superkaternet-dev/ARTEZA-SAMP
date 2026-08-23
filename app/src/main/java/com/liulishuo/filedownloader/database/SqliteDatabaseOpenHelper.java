/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteOpenHelper
 *  android.os.Build$VERSION
 */
package com.liulishuo.filedownloader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class SqliteDatabaseOpenHelper
extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "filedownloader.db";
    private static final int DATABASE_VERSION = 4;

    public SqliteDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS filedownloader( _id INTEGER PRIMARY KEY, url VARCHAR, path VARCHAR, status TINYINT(7), sofar INTEGER, total INTEGER, errMsg VARCHAR, etag VARCHAR, pathAsDirectory TINYINT(1) DEFAULT 0, filename VARCHAR, connectionCount INTEGER DEFAULT 1)");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS filedownloaderConnection( id INTEGER, connectionIndex INTEGER, startOffset INTEGER, currentOffset INTEGER, endOffset INTEGER, PRIMARY KEY ( id, connectionIndex ))");
    }

    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int n, int n2) {
        sQLiteDatabase.delete("filedownloader", null, null);
        sQLiteDatabase.delete("filedownloaderConnection", null, null);
    }

    public void onOpen(SQLiteDatabase sQLiteDatabase) {
        super.onOpen(sQLiteDatabase);
        if (Build.VERSION.SDK_INT >= 16) {
            this.setWriteAheadLoggingEnabled(true);
        } else if (Build.VERSION.SDK_INT >= 11) {
            sQLiteDatabase.enableWriteAheadLogging();
        }
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int n, int n2) {
        if (n < 2) {
            sQLiteDatabase.execSQL("ALTER TABLE filedownloader ADD COLUMN pathAsDirectory TINYINT(1) DEFAULT 0");
            sQLiteDatabase.execSQL("ALTER TABLE filedownloader ADD COLUMN filename VARCHAR");
        }
        if (n < 3) {
            sQLiteDatabase.execSQL("ALTER TABLE filedownloader ADD COLUMN connectionCount INTEGER DEFAULT 1");
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS filedownloaderConnection( id INTEGER, connectionIndex INTEGER, startOffset INTEGER, currentOffset INTEGER, endOffset INTEGER, PRIMARY KEY ( id, connectionIndex ))");
        }
        if (n < 4) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("endOffset", Integer.valueOf(-1));
            sQLiteDatabase.update("filedownloaderConnection", contentValues, "endOffset = ? AND startOffset > ?", new String[]{"0", "0"});
        }
    }
}

