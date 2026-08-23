/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.database.AbstractWindowedCursor
 *  android.database.CrossProcessCursor
 *  android.database.Cursor
 *  android.database.CursorWindow
 *  android.database.CursorWrapper
 */
package com.google.android.gms.common.sqlite;

import android.database.AbstractWindowedCursor;
import android.database.CrossProcessCursor;
import android.database.Cursor;
import android.database.CursorWindow;

public class CursorWrapper
extends android.database.CursorWrapper
implements CrossProcessCursor {
    private AbstractWindowedCursor zza;

    public CursorWrapper(Cursor object) {
        super((Cursor)object);
        for (int i = 0; i < 10 && object instanceof android.database.CursorWrapper; ++i) {
            object = ((android.database.CursorWrapper)object).getWrappedCursor();
        }
        if (!(object instanceof AbstractWindowedCursor)) {
            object = ((String)(object = String.valueOf(object.getClass().getName()))).length() != 0 ? "Unknown type: ".concat((String)object) : new String("Unknown type: ");
            throw new IllegalArgumentException((String)object);
        }
        this.zza = (AbstractWindowedCursor)object;
    }

    public void fillWindow(int n, CursorWindow cursorWindow) {
        this.zza.fillWindow(n, cursorWindow);
    }

    public CursorWindow getWindow() {
        return this.zza.getWindow();
    }

    public final /* synthetic */ Cursor getWrappedCursor() {
        return this.zza;
    }

    public final boolean onMove(int n, int n2) {
        return this.zza.onMove(n, n2);
    }

    public void setWindow(CursorWindow cursorWindow) {
        this.zza.setWindow(cursorWindow);
    }
}

