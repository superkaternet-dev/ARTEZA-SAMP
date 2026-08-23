/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.ContentObserver
 *  android.database.Cursor
 *  android.net.Uri
 */
package androidx.loader.content;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import androidx.core.content.ContentResolverCompat;
import androidx.core.os.CancellationSignal;
import androidx.core.os.OperationCanceledException;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;

public class CursorLoader
extends AsyncTaskLoader<Cursor> {
    CancellationSignal mCancellationSignal;
    Cursor mCursor;
    final Loader.ForceLoadContentObserver mObserver = new Loader.ForceLoadContentObserver(this);
    String[] mProjection;
    String mSelection;
    String[] mSelectionArgs;
    String mSortOrder;
    Uri mUri;

    public CursorLoader(Context context) {
        super(context);
    }

    public CursorLoader(Context context, Uri uri, String[] stringArray, String string2, String[] stringArray2, String string3) {
        super(context);
        this.mUri = uri;
        this.mProjection = stringArray;
        this.mSelection = string2;
        this.mSelectionArgs = stringArray2;
        this.mSortOrder = string3;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void cancelLoadInBackground() {
        super.cancelLoadInBackground();
        synchronized (this) {
            CancellationSignal cancellationSignal = this.mCancellationSignal;
            if (cancellationSignal != null) {
                cancellationSignal.cancel();
            }
            return;
        }
    }

    @Override
    public void deliverResult(Cursor cursor) {
        if (this.isReset()) {
            if (cursor != null) {
                cursor.close();
            }
            return;
        }
        Cursor cursor2 = this.mCursor;
        this.mCursor = cursor;
        if (this.isStarted()) {
            super.deliverResult(cursor);
        }
        if (cursor2 != null && cursor2 != cursor && !cursor2.isClosed()) {
            cursor2.close();
        }
    }

    @Override
    @Deprecated
    public void dump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] stringArray) {
        super.dump(string2, fileDescriptor, printWriter, stringArray);
        printWriter.print(string2);
        printWriter.print("mUri=");
        printWriter.println(this.mUri);
        printWriter.print(string2);
        printWriter.print("mProjection=");
        printWriter.println(Arrays.toString(this.mProjection));
        printWriter.print(string2);
        printWriter.print("mSelection=");
        printWriter.println(this.mSelection);
        printWriter.print(string2);
        printWriter.print("mSelectionArgs=");
        printWriter.println(Arrays.toString(this.mSelectionArgs));
        printWriter.print(string2);
        printWriter.print("mSortOrder=");
        printWriter.println(this.mSortOrder);
        printWriter.print(string2);
        printWriter.print("mCursor=");
        printWriter.println(this.mCursor);
        printWriter.print(string2);
        printWriter.print("mContentChanged=");
        printWriter.println(this.mContentChanged);
    }

    public String[] getProjection() {
        return this.mProjection;
    }

    public String getSelection() {
        return this.mSelection;
    }

    public String[] getSelectionArgs() {
        return this.mSelectionArgs;
    }

    public String getSortOrder() {
        return this.mSortOrder;
    }

    public Uri getUri() {
        return this.mUri;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public Cursor loadInBackground() {
        synchronized (this) {
            CancellationSignal cancellationSignal;
            if (this.isLoadInBackgroundCanceled()) {
                OperationCanceledException operationCanceledException = new OperationCanceledException();
                throw operationCanceledException;
            }
            this.mCancellationSignal = cancellationSignal = new CancellationSignal();
        }
        try {
            Cursor cursor = ContentResolverCompat.query(this.getContext().getContentResolver(), this.mUri, this.mProjection, this.mSelection, this.mSelectionArgs, this.mSortOrder, this.mCancellationSignal);
            if (cursor == null) return cursor;
            try {
                cursor.getCount();
                cursor.registerContentObserver((ContentObserver)this.mObserver);
                return cursor;
            }
            catch (RuntimeException runtimeException) {
                cursor.close();
                throw runtimeException;
            }
        }
        finally {
            synchronized (this) {
                this.mCancellationSignal = null;
            }
        }
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        this.onStopLoading();
        Cursor cursor = this.mCursor;
        if (cursor != null && !cursor.isClosed()) {
            this.mCursor.close();
        }
        this.mCursor = null;
    }

    @Override
    protected void onStartLoading() {
        Cursor cursor = this.mCursor;
        if (cursor != null) {
            this.deliverResult(cursor);
        }
        if (this.takeContentChanged() || this.mCursor == null) {
            this.forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        this.cancelLoad();
    }

    public void setProjection(String[] stringArray) {
        this.mProjection = stringArray;
    }

    public void setSelection(String string2) {
        this.mSelection = string2;
    }

    public void setSelectionArgs(String[] stringArray) {
        this.mSelectionArgs = stringArray;
    }

    public void setSortOrder(String string2) {
        this.mSortOrder = string2;
    }

    public void setUri(Uri uri) {
        this.mUri = uri;
    }
}

