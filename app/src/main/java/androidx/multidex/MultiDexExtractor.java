/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.os.Build$VERSION
 *  android.util.Log
 */
package androidx.multidex;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import androidx.multidex.ZipUtil;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

final class MultiDexExtractor
implements Closeable {
    private static final int BUFFER_SIZE = 16384;
    private static final String DEX_PREFIX = "classes";
    static final String DEX_SUFFIX = ".dex";
    private static final String EXTRACTED_NAME_EXT = ".classes";
    static final String EXTRACTED_SUFFIX = ".zip";
    private static final String KEY_CRC = "crc";
    private static final String KEY_DEX_CRC = "dex.crc.";
    private static final String KEY_DEX_NUMBER = "dex.number";
    private static final String KEY_DEX_TIME = "dex.time.";
    private static final String KEY_TIME_STAMP = "timestamp";
    private static final String LOCK_FILENAME = "MultiDex.lock";
    private static final int MAX_EXTRACT_ATTEMPTS = 3;
    private static final long NO_VALUE = -1L;
    private static final String PREFS_FILE = "multidex.version";
    private static final String TAG = "MultiDex";
    private final FileLock cacheLock;
    private final File dexDir;
    private final FileChannel lockChannel;
    private final RandomAccessFile lockRaf;
    private final File sourceApk;
    private final long sourceCrc;

    /*
     * Loose catch block
     * WARNING - void declaration
     */
    MultiDexExtractor(File file, File object) throws IOException {
        void iOException;
        Object object2 = new StringBuilder();
        ((StringBuilder)object2).append("MultiDexExtractor(");
        ((StringBuilder)object2).append(file.getPath());
        ((StringBuilder)object2).append(", ");
        ((StringBuilder)object2).append(((File)object).getPath());
        ((StringBuilder)object2).append(")");
        Log.i((String)TAG, (String)((StringBuilder)object2).toString());
        this.sourceApk = file;
        this.dexDir = object;
        this.sourceCrc = MultiDexExtractor.getZipCrc(file);
        file = new File((File)object, LOCK_FILENAME);
        this.lockRaf = object = new RandomAccessFile(file, "rw");
        this.lockChannel = object2 = ((RandomAccessFile)object).getChannel();
        object = new StringBuilder();
        ((StringBuilder)object).append("Blocking on lock ");
        ((StringBuilder)object).append(file.getPath());
        Log.i((String)TAG, (String)((StringBuilder)object).toString());
        this.cacheLock = ((FileChannel)object2).lock();
        object = new StringBuilder();
        ((StringBuilder)object).append(file.getPath());
        ((StringBuilder)object).append(" locked");
        Log.i((String)TAG, (String)((StringBuilder)object).toString());
        return;
        catch (Error error2) {
        }
        catch (RuntimeException runtimeException) {
        }
        catch (IOException iOException) {
            // empty catch block
        }
        try {
            MultiDexExtractor.closeQuietly(this.lockChannel);
            throw file;
        }
        catch (Error error) {
        }
        catch (RuntimeException runtimeException) {
        }
        catch (IOException iOException) {
            // empty catch block
        }
        MultiDexExtractor.closeQuietly(this.lockRaf);
        throw iOException;
    }

    private void clearDexDir() {
        File[] fileArray = this.dexDir.listFiles(new FileFilter(this){
            final MultiDexExtractor this$0;
            {
                this.this$0 = multiDexExtractor;
            }

            @Override
            public boolean accept(File file) {
                return file.getName().equals(MultiDexExtractor.LOCK_FILENAME) ^ true;
            }
        });
        if (fileArray == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to list secondary dex dir content (");
            stringBuilder.append(this.dexDir.getPath());
            stringBuilder.append(").");
            Log.w((String)TAG, (String)stringBuilder.toString());
            return;
        }
        for (File file : fileArray) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Trying to delete old file ");
            stringBuilder.append(file.getPath());
            stringBuilder.append(" of size ");
            stringBuilder.append(file.length());
            Log.i((String)TAG, (String)stringBuilder.toString());
            if (!file.delete()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to delete old file ");
                stringBuilder.append(file.getPath());
                Log.w((String)TAG, (String)stringBuilder.toString());
                continue;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Deleted old file ");
            stringBuilder.append(file.getPath());
            Log.i((String)TAG, (String)stringBuilder.toString());
        }
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        }
        catch (IOException iOException) {
            Log.w((String)TAG, (String)"Failed to close resource", (Throwable)iOException);
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void extract(ZipFile closeable, ZipEntry object, File file, String object2) throws IOException, FileNotFoundException {
        closeable = ((ZipFile)closeable).getInputStream((ZipEntry)object);
        Object object3 = new StringBuilder();
        ((StringBuilder)object3).append("tmp-");
        ((StringBuilder)object3).append((String)object2);
        object2 = File.createTempFile(((StringBuilder)object3).toString(), EXTRACTED_SUFFIX, file.getParentFile());
        object3 = new StringBuilder();
        ((StringBuilder)object3).append("Extracting ");
        ((StringBuilder)object3).append(((File)object2).getPath());
        Log.i((String)TAG, (String)((StringBuilder)object3).toString());
        Object object4 = new FileOutputStream((File)object2);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream((OutputStream)object4);
        object3 = new ZipOutputStream(bufferedOutputStream);
        object4 = new ZipEntry("classes.dex");
        ((ZipEntry)object4).setTime(((ZipEntry)object).getTime());
        ((ZipOutputStream)object3).putNextEntry((ZipEntry)object4);
        object = new byte[16384];
        int n = ((InputStream)closeable).read((byte[])object);
        while (n != -1) {
            ((ZipOutputStream)object3).write((byte[])object, 0, n);
            n = ((InputStream)closeable).read((byte[])object);
        }
        ((ZipOutputStream)object3).closeEntry();
        {
            catch (Throwable throwable) {
                ((ZipOutputStream)object3).close();
                throw throwable;
            }
        }
        try {
            ((ZipOutputStream)object3).close();
            if (((File)object2).setReadOnly()) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Renaming to ");
                ((StringBuilder)object).append(file.getPath());
                Log.i((String)TAG, (String)((StringBuilder)object).toString());
                boolean bl = ((File)object2).renameTo(file);
                if (bl) {
                    return;
                }
                object3 = new StringBuilder();
                ((StringBuilder)object3).append("Failed to rename \"");
                ((StringBuilder)object3).append(((File)object2).getAbsolutePath());
                ((StringBuilder)object3).append("\" to \"");
                ((StringBuilder)object3).append(file.getAbsolutePath());
                ((StringBuilder)object3).append("\"");
                object = new IOException(((StringBuilder)object3).toString());
                throw object;
            }
            object3 = new StringBuilder();
            ((StringBuilder)object3).append("Failed to mark readonly \"");
            ((StringBuilder)object3).append(((File)object2).getAbsolutePath());
            ((StringBuilder)object3).append("\" (tmp of \"");
            ((StringBuilder)object3).append(file.getAbsolutePath());
            ((StringBuilder)object3).append("\")");
            object = new IOException(((StringBuilder)object3).toString());
            throw object;
        }
        catch (Throwable throwable) {
            throw throwable;
        }
        finally {
            MultiDexExtractor.closeQuietly(closeable);
            ((File)object2).delete();
        }
    }

    private static SharedPreferences getMultiDexPreferences(Context context) {
        int n = Build.VERSION.SDK_INT < 11 ? 0 : 4;
        return context.getSharedPreferences(PREFS_FILE, n);
    }

    private static long getTimeStamp(File file) {
        long l;
        long l2 = l = file.lastModified();
        if (l == -1L) {
            l2 = l - 1L;
        }
        return l2;
    }

    private static long getZipCrc(File file) throws IOException {
        long l;
        long l2 = l = ZipUtil.getZipCrc(file);
        if (l == -1L) {
            l2 = l - 1L;
        }
        return l2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static boolean isModified(Context context, File comparable, long l, String string2) {
        context = MultiDexExtractor.getMultiDexPreferences(context);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(KEY_TIME_STAMP);
        if (context.getLong(stringBuilder.toString(), -1L) != MultiDexExtractor.getTimeStamp(comparable)) return true;
        comparable = new StringBuilder();
        ((StringBuilder)comparable).append(string2);
        ((StringBuilder)comparable).append(KEY_CRC);
        if (context.getLong(((StringBuilder)comparable).toString(), -1L) == l) return false;
        return true;
    }

    private List<ExtractedDex> loadExistingExtractions(Context object, String string2) throws IOException {
        Log.i((String)TAG, (String)"loading existing secondary dex files");
        CharSequence charSequence = new StringBuilder();
        charSequence.append(this.sourceApk.getName());
        charSequence.append(EXTRACTED_NAME_EXT);
        charSequence = charSequence.toString();
        object = MultiDexExtractor.getMultiDexPreferences((Context)object);
        Serializable serializable = new StringBuilder();
        ((StringBuilder)serializable).append(string2);
        ((StringBuilder)serializable).append(KEY_DEX_NUMBER);
        int n = object.getInt(((StringBuilder)serializable).toString(), 1);
        serializable = new ArrayList(n - 1);
        for (int i = 2; i <= n; ++i) {
            Object object2 = new StringBuilder();
            ((StringBuilder)object2).append((String)charSequence);
            ((StringBuilder)object2).append(i);
            ((StringBuilder)object2).append(EXTRACTED_SUFFIX);
            object2 = ((StringBuilder)object2).toString();
            object2 = new ExtractedDex(this.dexDir, (String)object2);
            if (((File)object2).isFile()) {
                ((ExtractedDex)object2).crc = MultiDexExtractor.getZipCrc((File)object2);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(string2);
                stringBuilder.append(KEY_DEX_CRC);
                stringBuilder.append(i);
                long l = object.getLong(stringBuilder.toString(), -1L);
                stringBuilder = new StringBuilder();
                stringBuilder.append(string2);
                stringBuilder.append(KEY_DEX_TIME);
                stringBuilder.append(i);
                long l2 = object.getLong(stringBuilder.toString(), -1L);
                long l3 = ((File)object2).lastModified();
                if (l2 == l3 && l == ((ExtractedDex)object2).crc) {
                    serializable.add(object2);
                    continue;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("Invalid extracted dex: ");
                ((StringBuilder)object).append(object2);
                ((StringBuilder)object).append(" (key \"");
                ((StringBuilder)object).append(string2);
                ((StringBuilder)object).append("\"), expected modification time: ");
                ((StringBuilder)object).append(l2);
                ((StringBuilder)object).append(", modification time: ");
                ((StringBuilder)object).append(l3);
                ((StringBuilder)object).append(", expected crc: ");
                ((StringBuilder)object).append(l);
                ((StringBuilder)object).append(", file crc: ");
                ((StringBuilder)object).append(((ExtractedDex)object2).crc);
                throw new IOException(((StringBuilder)object).toString());
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Missing extracted secondary dex file '");
            ((StringBuilder)object).append(((File)object2).getPath());
            ((StringBuilder)object).append("'");
            throw new IOException(((StringBuilder)object).toString());
        }
        return serializable;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private List<ExtractedDex> performExtractions() throws IOException {
        Object object = new StringBuilder();
        ((StringBuilder)object).append(this.sourceApk.getName());
        ((StringBuilder)object).append(EXTRACTED_NAME_EXT);
        String string2 = ((StringBuilder)object).toString();
        this.clearDexDir();
        ArrayList<ExtractedDex> arrayList = new ArrayList<ExtractedDex>();
        ZipFile zipFile = new ZipFile(this.sourceApk);
        try {
            object = new StringBuilder();
            ((StringBuilder)object).append(DEX_PREFIX);
            ((StringBuilder)object).append(2);
            ((StringBuilder)object).append(DEX_SUFFIX);
            object = zipFile.getEntry(((StringBuilder)object).toString());
            int n = 2;
            while (object != null) {
                CharSequence charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append(string2);
                ((StringBuilder)charSequence).append(n);
                ((StringBuilder)charSequence).append(EXTRACTED_SUFFIX);
                charSequence = ((StringBuilder)charSequence).toString();
                ExtractedDex extractedDex = new ExtractedDex(this.dexDir, (String)charSequence);
                arrayList.add(extractedDex);
                CharSequence charSequence2 = new StringBuilder();
                ((StringBuilder)charSequence2).append("Extraction is needed for file ");
                ((StringBuilder)charSequence2).append(extractedDex);
                Log.i((String)TAG, (String)((StringBuilder)charSequence2).toString());
                boolean bl = false;
                for (int i = 0; i < 3 && !bl; ++i) {
                    StringBuilder stringBuilder;
                    MultiDexExtractor.extract(zipFile, (ZipEntry)object, extractedDex, string2);
                    try {
                        extractedDex.crc = MultiDexExtractor.getZipCrc(extractedDex);
                        bl = true;
                    }
                    catch (IOException iOException) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Failed to read crc from ");
                        stringBuilder.append(extractedDex.getAbsolutePath());
                        Log.w((String)TAG, (String)stringBuilder.toString(), (Throwable)iOException);
                        bl = false;
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Extraction ");
                    charSequence2 = bl ? "succeeded" : "failed";
                    stringBuilder.append((String)charSequence2);
                    stringBuilder.append(" '");
                    stringBuilder.append(extractedDex.getAbsolutePath());
                    stringBuilder.append("': length ");
                    stringBuilder.append(extractedDex.length());
                    stringBuilder.append(" - crc: ");
                    stringBuilder.append(extractedDex.crc);
                    Log.i((String)TAG, (String)stringBuilder.toString());
                    if (bl) continue;
                    extractedDex.delete();
                    if (!extractedDex.exists()) continue;
                    charSequence2 = new StringBuilder();
                    ((StringBuilder)charSequence2).append("Failed to delete corrupted secondary dex '");
                    ((StringBuilder)charSequence2).append(extractedDex.getPath());
                    ((StringBuilder)charSequence2).append("'");
                    Log.w((String)TAG, (String)((StringBuilder)charSequence2).toString());
                }
                if (bl) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append(DEX_PREFIX);
                    ((StringBuilder)object).append(++n);
                    ((StringBuilder)object).append(DEX_SUFFIX);
                    object = zipFile.getEntry(((StringBuilder)object).toString());
                    continue;
                }
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("Could not create zip file ");
                ((StringBuilder)charSequence).append(extractedDex.getAbsolutePath());
                ((StringBuilder)charSequence).append(" for secondary dex (");
                ((StringBuilder)charSequence).append(n);
                ((StringBuilder)charSequence).append(")");
                object = new IOException(((StringBuilder)charSequence).toString());
                throw object;
            }
        }
        catch (Throwable throwable) {
            try {
                zipFile.close();
                throw throwable;
            }
            catch (IOException iOException) {
                Log.w((String)TAG, (String)"Failed to close resource", (Throwable)iOException);
                throw throwable;
            }
        }
        try {
            zipFile.close();
            return arrayList;
        }
        catch (IOException iOException) {
            Log.w((String)TAG, (String)"Failed to close resource", (Throwable)iOException);
        }
        return arrayList;
    }

    private static void putStoredApkInfo(Context context, String string2, long l, long l2, List<ExtractedDex> object) {
        context = MultiDexExtractor.getMultiDexPreferences(context).edit();
        Object object2 = new StringBuilder();
        ((StringBuilder)object2).append(string2);
        ((StringBuilder)object2).append(KEY_TIME_STAMP);
        context.putLong(((StringBuilder)object2).toString(), l);
        object2 = new StringBuilder();
        ((StringBuilder)object2).append(string2);
        ((StringBuilder)object2).append(KEY_CRC);
        context.putLong(((StringBuilder)object2).toString(), l2);
        object2 = new StringBuilder();
        ((StringBuilder)object2).append(string2);
        ((StringBuilder)object2).append(KEY_DEX_NUMBER);
        context.putInt(((StringBuilder)object2).toString(), object.size() + 1);
        int n = 2;
        object2 = object.iterator();
        while (object2.hasNext()) {
            object = (ExtractedDex)object2.next();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(KEY_DEX_CRC);
            stringBuilder.append(n);
            context.putLong(stringBuilder.toString(), ((ExtractedDex)object).crc);
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(KEY_DEX_TIME);
            stringBuilder.append(n);
            context.putLong(stringBuilder.toString(), ((File)object).lastModified());
            ++n;
        }
        context.commit();
    }

    @Override
    public void close() throws IOException {
        this.cacheLock.release();
        this.lockChannel.close();
        this.lockRaf.close();
    }

    List<? extends File> load(Context object, String charSequence, boolean bl) throws IOException {
        List<ExtractedDex> list = new StringBuilder();
        ((StringBuilder)((Object)list)).append("MultiDexExtractor.load(");
        ((StringBuilder)((Object)list)).append(this.sourceApk.getPath());
        ((StringBuilder)((Object)list)).append(", ");
        ((StringBuilder)((Object)list)).append(bl);
        ((StringBuilder)((Object)list)).append(", ");
        ((StringBuilder)((Object)list)).append((String)charSequence);
        ((StringBuilder)((Object)list)).append(")");
        Log.i((String)TAG, (String)((StringBuilder)((Object)list)).toString());
        if (this.cacheLock.isValid()) {
            if (!bl && !MultiDexExtractor.isModified((Context)object, this.sourceApk, this.sourceCrc, (String)charSequence)) {
                try {
                    list = this.loadExistingExtractions((Context)object, (String)charSequence);
                    object = list;
                }
                catch (IOException iOException) {
                    Log.w((String)TAG, (String)"Failed to reload existing extracted secondary dex files, falling back to fresh extraction", (Throwable)iOException);
                    List<ExtractedDex> list2 = this.performExtractions();
                    MultiDexExtractor.putStoredApkInfo((Context)object, (String)charSequence, MultiDexExtractor.getTimeStamp(this.sourceApk), this.sourceCrc, list2);
                    object = list2;
                }
            } else {
                if (bl) {
                    Log.i((String)TAG, (String)"Forced extraction must be performed.");
                } else {
                    Log.i((String)TAG, (String)"Detected that extraction must be performed.");
                }
                list = this.performExtractions();
                MultiDexExtractor.putStoredApkInfo((Context)object, (String)charSequence, MultiDexExtractor.getTimeStamp(this.sourceApk), this.sourceCrc, list);
                object = list;
            }
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("load found ");
            ((StringBuilder)charSequence).append(object.size());
            ((StringBuilder)charSequence).append(" secondary dex files");
            Log.i((String)TAG, (String)((StringBuilder)charSequence).toString());
            return object;
        }
        throw new IllegalStateException("MultiDexExtractor was closed");
    }

    private static class ExtractedDex
    extends File {
        public long crc = -1L;

        public ExtractedDex(File file, String string2) {
            super(file, string2);
        }
    }
}

