/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.database.Cursor
 *  android.widget.Filter
 *  android.widget.Filter$FilterResults
 */
package androidx.cursoradapter.widget;

import android.database.Cursor;
import android.widget.Filter;

class CursorFilter
extends Filter {
    CursorFilterClient mClient;

    CursorFilter(CursorFilterClient cursorFilterClient) {
        this.mClient = cursorFilterClient;
    }

    public CharSequence convertResultToString(Object object) {
        return this.mClient.convertToString((Cursor)object);
    }

    protected Filter.FilterResults performFiltering(CharSequence charSequence) {
        Cursor cursor = this.mClient.runQueryOnBackgroundThread(charSequence);
        charSequence = new Filter.FilterResults();
        if (cursor != null) {
            ((Filter.FilterResults)charSequence).count = cursor.getCount();
            ((Filter.FilterResults)charSequence).values = cursor;
        } else {
            ((Filter.FilterResults)charSequence).count = 0;
            ((Filter.FilterResults)charSequence).values = null;
        }
        return charSequence;
    }

    protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
        charSequence = this.mClient.getCursor();
        if (filterResults.values != null && filterResults.values != charSequence) {
            this.mClient.changeCursor((Cursor)filterResults.values);
        }
    }

    static interface CursorFilterClient {
        public void changeCursor(Cursor var1);

        public CharSequence convertToString(Cursor var1);

        public Cursor getCursor();

        public Cursor runQueryOnBackgroundThread(CharSequence var1);
    }
}

