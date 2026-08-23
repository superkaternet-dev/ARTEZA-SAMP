/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory
 *  android.graphics.BitmapFactory$Options
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.ColorMatrix
 *  android.graphics.ColorMatrixColorFilter
 *  android.graphics.Matrix
 *  android.graphics.Paint
 *  android.graphics.RectF
 *  android.graphics.pdf.PdfDocument$Page
 *  android.net.Uri
 *  android.os.AsyncTask
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.CancellationSignal
 *  android.os.CancellationSignal$OnCancelListener
 *  android.os.ParcelFileDescriptor
 *  android.print.PageRange
 *  android.print.PrintAttributes
 *  android.print.PrintAttributes$Builder
 *  android.print.PrintAttributes$Margins
 *  android.print.PrintAttributes$MediaSize
 *  android.print.PrintDocumentAdapter
 *  android.print.PrintDocumentAdapter$LayoutResultCallback
 *  android.print.PrintDocumentAdapter$WriteResultCallback
 *  android.print.PrintDocumentInfo
 *  android.print.PrintDocumentInfo$Builder
 *  android.print.PrintManager
 *  android.print.pdf.PrintedPdfDocument
 *  android.util.Log
 */
package androidx.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class PrintHelper {
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    static final boolean IS_MIN_MARGINS_HANDLING_CORRECT;
    private static final String LOG_TAG = "PrintHelper";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    static final boolean PRINT_ACTIVITY_RESPECTS_ORIENTATION;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    int mColorMode = 2;
    final Context mContext;
    BitmapFactory.Options mDecodeOptions = null;
    final Object mLock = new Object();
    int mOrientation = 1;
    int mScaleMode = 2;

    static {
        int n = Build.VERSION.SDK_INT;
        boolean bl = false;
        boolean bl2 = n < 20 || Build.VERSION.SDK_INT > 23;
        PRINT_ACTIVITY_RESPECTS_ORIENTATION = bl2;
        bl2 = bl;
        if (Build.VERSION.SDK_INT != 23) {
            bl2 = true;
        }
        IS_MIN_MARGINS_HANDLING_CORRECT = bl2;
    }

    public PrintHelper(Context context) {
        this.mContext = context;
    }

    static Bitmap convertBitmapForColorMode(Bitmap bitmap, int n) {
        if (n != 1) {
            return bitmap;
        }
        Bitmap bitmap2 = Bitmap.createBitmap((int)bitmap.getWidth(), (int)bitmap.getHeight(), (Bitmap.Config)Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap2);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        canvas.setBitmap(null);
        return bitmap2;
    }

    private static PrintAttributes.Builder copyAttributes(PrintAttributes printAttributes) {
        PrintAttributes.Builder builder = new PrintAttributes.Builder().setMediaSize(printAttributes.getMediaSize()).setResolution(printAttributes.getResolution()).setMinMargins(printAttributes.getMinMargins());
        if (printAttributes.getColorMode() != 0) {
            builder.setColorMode(printAttributes.getColorMode());
        }
        if (Build.VERSION.SDK_INT >= 23 && printAttributes.getDuplexMode() != 0) {
            builder.setDuplexMode(printAttributes.getDuplexMode());
        }
        return builder;
    }

    static Matrix getMatrix(int n, int n2, RectF rectF, int n3) {
        Matrix matrix = new Matrix();
        float f = rectF.width() / (float)n;
        f = n3 == 2 ? Math.max(f, rectF.height() / (float)n2) : Math.min(f, rectF.height() / (float)n2);
        matrix.postScale(f, f);
        matrix.postTranslate((rectF.width() - (float)n * f) / 2.0f, (rectF.height() - (float)n2 * f) / 2.0f);
        return matrix;
    }

    static boolean isPortrait(Bitmap bitmap) {
        boolean bl = bitmap.getWidth() <= bitmap.getHeight();
        return bl;
    }

    private Bitmap loadBitmap(Uri object, BitmapFactory.Options options) throws FileNotFoundException {
        Context context;
        if (object != null && (context = this.mContext) != null) {
            block9: {
                Object object2 = null;
                try {
                    object2 = object = context.getContentResolver().openInputStream((Uri)object);
                }
                catch (Throwable throwable) {
                    if (object2 != null) {
                        try {
                            ((InputStream)object2).close();
                        }
                        catch (IOException iOException) {
                            Log.w((String)LOG_TAG, (String)"close fail ", (Throwable)iOException);
                        }
                    }
                    throw throwable;
                }
                options = BitmapFactory.decodeStream((InputStream)object, null, (BitmapFactory.Options)options);
                if (object == null) break block9;
                try {
                    ((InputStream)object).close();
                }
                catch (IOException iOException) {
                    Log.w((String)LOG_TAG, (String)"close fail ", (Throwable)iOException);
                }
            }
            return options;
        }
        throw new IllegalArgumentException("bad argument to loadBitmap");
    }

    public static boolean systemSupportsPrint() {
        boolean bl = Build.VERSION.SDK_INT >= 19;
        return bl;
    }

    public int getColorMode() {
        return this.mColorMode;
    }

    public int getOrientation() {
        if (Build.VERSION.SDK_INT >= 19 && this.mOrientation == 0) {
            return 1;
        }
        return this.mOrientation;
    }

    public int getScaleMode() {
        return this.mScaleMode;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    Bitmap loadConstrainedBitmap(Uri object) throws FileNotFoundException {
        if (object != null && this.mContext != null) {
            BitmapFactory.Options options;
            Object object2 = new BitmapFactory.Options();
            object2.inJustDecodeBounds = true;
            this.loadBitmap((Uri)object, (BitmapFactory.Options)object2);
            int n = object2.outWidth;
            int n2 = object2.outHeight;
            if (n <= 0) return null;
            if (n2 <= 0) {
                return null;
            }
            int n3 = 1;
            for (int i = Math.max(n, n2); i > 3500; i >>>= 1, n3 <<= 1) {
            }
            if (n3 <= 0) return null;
            if (Math.min(n, n2) / n3 <= 0) {
                return null;
            }
            object2 = this.mLock;
            synchronized (object2) {
                this.mDecodeOptions = options = new BitmapFactory.Options();
                options.inMutable = true;
                this.mDecodeOptions.inSampleSize = n3;
                options = this.mDecodeOptions;
            }
            try {
                object2 = this.loadBitmap((Uri)object, options);
                return object2;
            }
            finally {
                object = this.mLock;
                synchronized (object) {
                    this.mDecodeOptions = null;
                }
            }
        }
        object = new IllegalArgumentException("bad argument to getScaledBitmap");
        throw object;
    }

    public void printBitmap(String string2, Bitmap bitmap) {
        this.printBitmap(string2, bitmap, null);
    }

    public void printBitmap(String string2, Bitmap bitmap, OnPrintFinishCallback onPrintFinishCallback) {
        if (Build.VERSION.SDK_INT >= 19 && bitmap != null) {
            PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
            PrintAttributes.MediaSize mediaSize = PrintHelper.isPortrait(bitmap) ? PrintAttributes.MediaSize.UNKNOWN_PORTRAIT : PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE;
            mediaSize = new PrintAttributes.Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build();
            printManager.print(string2, (PrintDocumentAdapter)new PrintBitmapAdapter(this, string2, this.mScaleMode, bitmap, onPrintFinishCallback), (PrintAttributes)mediaSize);
            return;
        }
    }

    public void printBitmap(String string2, Uri uri) throws FileNotFoundException {
        this.printBitmap(string2, uri, null);
    }

    public void printBitmap(String string2, Uri uri, OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        PrintUriAdapter printUriAdapter = new PrintUriAdapter(this, string2, uri, onPrintFinishCallback, this.mScaleMode);
        onPrintFinishCallback = (PrintManager)this.mContext.getSystemService("print");
        uri = new PrintAttributes.Builder();
        uri.setColorMode(this.mColorMode);
        int n = this.mOrientation;
        if (n != 1 && n != 0) {
            if (n == 2) {
                uri.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT);
            }
        } else {
            uri.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
        }
        onPrintFinishCallback.print(string2, printUriAdapter, uri.build());
    }

    public void setColorMode(int n) {
        this.mColorMode = n;
    }

    public void setOrientation(int n) {
        this.mOrientation = n;
    }

    public void setScaleMode(int n) {
        this.mScaleMode = n;
    }

    void writeBitmap(PrintAttributes printAttributes, int n, Bitmap bitmap, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
        PrintAttributes printAttributes2 = IS_MIN_MARGINS_HANDLING_CORRECT ? printAttributes : PrintHelper.copyAttributes(printAttributes).setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0)).build();
        new AsyncTask<Void, Void, Throwable>(this, cancellationSignal, printAttributes2, bitmap, printAttributes, n, parcelFileDescriptor, writeResultCallback){
            final PrintHelper this$0;
            final PrintAttributes val$attributes;
            final Bitmap val$bitmap;
            final CancellationSignal val$cancellationSignal;
            final ParcelFileDescriptor val$fileDescriptor;
            final int val$fittingMode;
            final PrintAttributes val$pdfAttributes;
            final PrintDocumentAdapter.WriteResultCallback val$writeResultCallback;
            {
                this.this$0 = printHelper;
                this.val$cancellationSignal = cancellationSignal;
                this.val$pdfAttributes = printAttributes;
                this.val$bitmap = bitmap;
                this.val$attributes = printAttributes2;
                this.val$fittingMode = n;
                this.val$fileDescriptor = parcelFileDescriptor;
                this.val$writeResultCallback = writeResultCallback;
            }

            /*
             * Loose catch block
             */
            protected Throwable doInBackground(Void ... object) {
                Bitmap bitmap;
                PrintedPdfDocument printedPdfDocument;
                block28: {
                    block26: {
                        block27: {
                            PrintedPdfDocument printedPdfDocument2;
                            boolean bl;
                            block25: {
                                block24: {
                                    if (!this.val$cancellationSignal.isCanceled()) break block24;
                                    return null;
                                    {
                                        catch (Throwable throwable) {
                                            return throwable;
                                        }
                                    }
                                }
                                printedPdfDocument = new PrintedPdfDocument(this.this$0.mContext, this.val$pdfAttributes);
                                bitmap = PrintHelper.convertBitmapForColorMode(this.val$bitmap, this.val$pdfAttributes.getColorMode());
                                bl = this.val$cancellationSignal.isCanceled();
                                if (!bl) break block25;
                                return null;
                            }
                            PdfDocument.Page page = printedPdfDocument.startPage(1);
                            if (IS_MIN_MARGINS_HANDLING_CORRECT) {
                                object = new RectF(page.getInfo().getContentRect());
                            } else {
                                printedPdfDocument2 = new PrintedPdfDocument(this.this$0.mContext, this.val$attributes);
                                PdfDocument.Page page2 = printedPdfDocument2.startPage(1);
                                object = new RectF(page2.getInfo().getContentRect());
                                printedPdfDocument2.finishPage(page2);
                                printedPdfDocument2.close();
                            }
                            printedPdfDocument2 = PrintHelper.getMatrix(bitmap.getWidth(), bitmap.getHeight(), object, this.val$fittingMode);
                            if (!IS_MIN_MARGINS_HANDLING_CORRECT) {
                                printedPdfDocument2.postTranslate(object.left, object.top);
                                page.getCanvas().clipRect(object);
                            }
                            page.getCanvas().drawBitmap(bitmap, (Matrix)printedPdfDocument2, null);
                            printedPdfDocument.finishPage(page);
                            bl = this.val$cancellationSignal.isCanceled();
                            if (!bl) break block26;
                            printedPdfDocument.close();
                            object = this.val$fileDescriptor;
                            if (object == null) break block27;
                            try {
                                object.close();
                            }
                            catch (IOException iOException) {
                                // empty catch block
                            }
                        }
                        if (bitmap != this.val$bitmap) {
                            bitmap.recycle();
                        }
                        return null;
                    }
                    object = new FileOutputStream(this.val$fileDescriptor.getFileDescriptor());
                    printedPdfDocument.writeTo((OutputStream)object);
                    printedPdfDocument.close();
                    object = this.val$fileDescriptor;
                    if (object == null) break block28;
                    try {
                        object.close();
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
                if (bitmap != this.val$bitmap) {
                    bitmap.recycle();
                }
                return null;
                catch (Throwable throwable) {
                    block29: {
                        printedPdfDocument.close();
                        printedPdfDocument = this.val$fileDescriptor;
                        if (printedPdfDocument == null) break block29;
                        try {
                            printedPdfDocument.close();
                        }
                        catch (IOException iOException) {
                            // empty catch block
                        }
                    }
                    if (bitmap != this.val$bitmap) {
                        bitmap.recycle();
                    }
                    throw throwable;
                }
            }

            protected void onPostExecute(Throwable throwable) {
                if (this.val$cancellationSignal.isCanceled()) {
                    this.val$writeResultCallback.onWriteCancelled();
                } else if (throwable == null) {
                    this.val$writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                } else {
                    Log.e((String)PrintHelper.LOG_TAG, (String)"Error writing printed content", (Throwable)throwable);
                    this.val$writeResultCallback.onWriteFailed(null);
                }
            }
        }.execute((Object[])new Void[0]);
    }

    public static interface OnPrintFinishCallback {
        public void onFinish();
    }

    private class PrintBitmapAdapter
    extends PrintDocumentAdapter {
        private PrintAttributes mAttributes;
        private final Bitmap mBitmap;
        private final OnPrintFinishCallback mCallback;
        private final int mFittingMode;
        private final String mJobName;
        final PrintHelper this$0;

        PrintBitmapAdapter(PrintHelper printHelper, String string2, int n, Bitmap bitmap, OnPrintFinishCallback onPrintFinishCallback) {
            this.this$0 = printHelper;
            this.mJobName = string2;
            this.mFittingMode = n;
            this.mBitmap = bitmap;
            this.mCallback = onPrintFinishCallback;
        }

        public void onFinish() {
            OnPrintFinishCallback onPrintFinishCallback = this.mCallback;
            if (onPrintFinishCallback != null) {
                onPrintFinishCallback.onFinish();
            }
        }

        public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes2, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, Bundle bundle) {
            this.mAttributes = printAttributes2;
            layoutResultCallback.onLayoutFinished(new PrintDocumentInfo.Builder(this.mJobName).setContentType(1).setPageCount(1).build(), true ^ printAttributes2.equals((Object)printAttributes));
        }

        public void onWrite(PageRange[] pageRangeArray, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
            this.this$0.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, parcelFileDescriptor, cancellationSignal, writeResultCallback);
        }
    }

    private class PrintUriAdapter
    extends PrintDocumentAdapter {
        PrintAttributes mAttributes;
        Bitmap mBitmap;
        final OnPrintFinishCallback mCallback;
        final int mFittingMode;
        final Uri mImageFile;
        final String mJobName;
        AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
        final PrintHelper this$0;

        PrintUriAdapter(PrintHelper printHelper, String string2, Uri uri, OnPrintFinishCallback onPrintFinishCallback, int n) {
            this.this$0 = printHelper;
            this.mJobName = string2;
            this.mImageFile = uri;
            this.mCallback = onPrintFinishCallback;
            this.mFittingMode = n;
            this.mBitmap = null;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        void cancelLoad() {
            Object object = this.this$0.mLock;
            synchronized (object) {
                if (this.this$0.mDecodeOptions != null) {
                    if (Build.VERSION.SDK_INT < 24) {
                        this.this$0.mDecodeOptions.requestCancelDecode();
                    }
                    this.this$0.mDecodeOptions = null;
                }
                return;
            }
        }

        public void onFinish() {
            super.onFinish();
            this.cancelLoad();
            Object object = this.mLoadBitmap;
            if (object != null) {
                object.cancel(true);
            }
            if ((object = this.mCallback) != null) {
                object.onFinish();
            }
            if ((object = this.mBitmap) != null) {
                object.recycle();
                this.mBitmap = null;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes2, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, Bundle bundle) {
            // MONITORENTER : this
            this.mAttributes = printAttributes2;
            // MONITOREXIT : this
            if (cancellationSignal.isCanceled()) {
                layoutResultCallback.onLayoutCancelled();
                return;
            }
            if (this.mBitmap != null) {
                layoutResultCallback.onLayoutFinished(new PrintDocumentInfo.Builder(this.mJobName).setContentType(1).setPageCount(1).build(), true ^ printAttributes2.equals((Object)printAttributes));
                return;
            }
            this.mLoadBitmap = new AsyncTask<Uri, Boolean, Bitmap>(this, cancellationSignal, printAttributes2, printAttributes, layoutResultCallback){
                final PrintUriAdapter this$1;
                final CancellationSignal val$cancellationSignal;
                final PrintDocumentAdapter.LayoutResultCallback val$layoutResultCallback;
                final PrintAttributes val$newPrintAttributes;
                final PrintAttributes val$oldPrintAttributes;
                {
                    this.this$1 = printUriAdapter;
                    this.val$cancellationSignal = cancellationSignal;
                    this.val$newPrintAttributes = printAttributes;
                    this.val$oldPrintAttributes = printAttributes2;
                    this.val$layoutResultCallback = layoutResultCallback;
                }

                protected Bitmap doInBackground(Uri ... bitmap) {
                    try {
                        bitmap = this.this$1.this$0.loadConstrainedBitmap(this.this$1.mImageFile);
                        return bitmap;
                    }
                    catch (FileNotFoundException fileNotFoundException) {
                        return null;
                    }
                }

                protected void onCancelled(Bitmap bitmap) {
                    this.val$layoutResultCallback.onLayoutCancelled();
                    this.this$1.mLoadBitmap = null;
                }

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 */
                protected void onPostExecute(Bitmap bitmap) {
                    Bitmap bitmap2;
                    block10: {
                        PrintAttributes.MediaSize mediaSize;
                        block11: {
                            super.onPostExecute((Object)bitmap);
                            bitmap2 = bitmap;
                            if (bitmap == null) break block10;
                            if (!PRINT_ACTIVITY_RESPECTS_ORIENTATION) break block11;
                            bitmap2 = bitmap;
                            if (this.this$1.this$0.mOrientation != 0) break block10;
                        }
                        synchronized (this) {
                            mediaSize = this.this$1.mAttributes.getMediaSize();
                        }
                        bitmap2 = bitmap;
                        if (mediaSize != null) {
                            bitmap2 = bitmap;
                            if (mediaSize.isPortrait() != PrintHelper.isPortrait(bitmap)) {
                                bitmap2 = new Matrix();
                                bitmap2.postRotate(90.0f);
                                bitmap2 = Bitmap.createBitmap((Bitmap)bitmap, (int)0, (int)0, (int)bitmap.getWidth(), (int)bitmap.getHeight(), (Matrix)bitmap2, (boolean)true);
                            }
                        }
                    }
                    this.this$1.mBitmap = bitmap2;
                    if (bitmap2 != null) {
                        bitmap = new PrintDocumentInfo.Builder(this.this$1.mJobName).setContentType(1).setPageCount(1).build();
                        boolean bl = this.val$newPrintAttributes.equals((Object)this.val$oldPrintAttributes);
                        this.val$layoutResultCallback.onLayoutFinished((PrintDocumentInfo)bitmap, true ^ bl);
                    } else {
                        this.val$layoutResultCallback.onLayoutFailed(null);
                    }
                    this.this$1.mLoadBitmap = null;
                }

                protected void onPreExecute() {
                    this.val$cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener(this){
                        final 1 this$2;
                        {
                            this.this$2 = var1_1;
                        }

                        public void onCancel() {
                            this.this$2.this$1.cancelLoad();
                            this.this$2.cancel(false);
                        }
                    });
                }
            }.execute((Object[])new Uri[0]);
        }

        public void onWrite(PageRange[] pageRangeArray, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
            this.this$0.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, parcelFileDescriptor, cancellationSignal, writeResultCallback);
        }
    }
}

