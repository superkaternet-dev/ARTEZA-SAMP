/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.os.StrictMode
 *  android.os.StrictMode$ThreadPolicy
 *  android.os.StrictMode$ThreadPolicy$Builder
 */
package com.bumptech.glide.disklrucache;

import android.os.Build;
import android.os.StrictMode;
import com.bumptech.glide.disklrucache.StrictLineReader;
import com.bumptech.glide.disklrucache.Util;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class DiskLruCache
implements Closeable {
    static final long ANY_SEQUENCE_NUMBER = -1L;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final String MAGIC = "libcore.io.DiskLruCache";
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    static final String VERSION_1 = "1";
    private final int appVersion;
    private final Callable<Void> cleanupCallable;
    private final File directory;
    final ThreadPoolExecutor executorService;
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    private Writer journalWriter;
    private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap(0, 0.75f, true);
    private long maxSize;
    private long nextSequenceNumber = 0L;
    private int redundantOpCount;
    private long size = 0L;
    private final int valueCount;

    private DiskLruCache(File file, int n, int n2, long l) {
        this.executorService = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new DiskLruCacheThreadFactory());
        this.cleanupCallable = new Callable<Void>(this){
            final DiskLruCache this$0;
            {
                this.this$0 = diskLruCache;
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public Void call() throws Exception {
                DiskLruCache diskLruCache = this.this$0;
                synchronized (diskLruCache) {
                    if (this.this$0.journalWriter == null) {
                        return null;
                    }
                    this.this$0.trimToSize();
                    if (this.this$0.journalRebuildRequired()) {
                        this.this$0.rebuildJournal();
                        DiskLruCache.access$502(this.this$0, 0);
                    }
                    return null;
                }
            }
        };
        this.directory = file;
        this.appVersion = n;
        this.journalFile = new File(file, JOURNAL_FILE);
        this.journalFileTmp = new File(file, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(file, JOURNAL_FILE_BACKUP);
        this.valueCount = n2;
        this.maxSize = l;
    }

    static /* synthetic */ int access$502(DiskLruCache diskLruCache, int n) {
        diskLruCache.redundantOpCount = n;
        return n;
    }

    private void checkNotClosed() {
        if (this.journalWriter != null) {
            return;
        }
        throw new IllegalStateException("cache is closed");
    }

    private static void closeWriter(Writer writer) throws IOException {
        if (Build.VERSION.SDK_INT < 26) {
            writer.close();
            return;
        }
        StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)new StrictMode.ThreadPolicy.Builder(threadPolicy).permitUnbufferedIo().build());
        try {
            writer.close();
            return;
        }
        finally {
            StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void completeEdit(Editor object, boolean bl) throws IOException {
        synchronized (this) {
            long l;
            int n;
            Object object2;
            block14: {
                block15: {
                    block13: {
                        object2 = ((Editor)object).entry;
                        if (((Entry)object2).currentEditor != object) break block13;
                        if (!bl || ((Entry)object2).readable) break block14;
                        break block15;
                    }
                    object = new IllegalStateException();
                    throw object;
                }
                for (n = 0; n < this.valueCount; ++n) {
                    if (((Editor)object).written[n]) {
                        if (((Entry)object2).getDirtyFile(n).exists()) continue;
                        ((Editor)object).abort();
                        return;
                    }
                    ((Editor)object).abort();
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Newly created entry didn't create value for index ");
                    ((StringBuilder)object).append(n);
                    object2 = new IllegalStateException(((StringBuilder)object).toString());
                    throw object2;
                }
            }
            for (n = 0; n < this.valueCount; ++n) {
                File file = ((Entry)object2).getDirtyFile(n);
                if (bl) {
                    if (!file.exists()) continue;
                    object = ((Entry)object2).getCleanFile(n);
                    file.renameTo((File)object);
                    long l2 = ((Entry)object2).lengths[n];
                    ((Entry)object2).lengths[n] = l = ((File)object).length();
                    this.size = this.size - l2 + l;
                    continue;
                }
                DiskLruCache.deleteIfExists(file);
            }
            ++this.redundantOpCount;
            Entry.access$802((Entry)object2, null);
            if (((Entry)object2).readable | bl) {
                Entry.access$702((Entry)object2, true);
                this.journalWriter.append(CLEAN);
                this.journalWriter.append(' ');
                this.journalWriter.append(((Entry)object2).key);
                this.journalWriter.append(((Entry)object2).getLengths());
                this.journalWriter.append('\n');
                if (bl) {
                    l = this.nextSequenceNumber;
                    this.nextSequenceNumber = 1L + l;
                    Entry.access$1302((Entry)object2, l);
                }
            } else {
                this.lruEntries.remove(((Entry)object2).key);
                this.journalWriter.append(REMOVE);
                this.journalWriter.append(' ');
                this.journalWriter.append(((Entry)object2).key);
                this.journalWriter.append('\n');
            }
            DiskLruCache.flushWriter(this.journalWriter);
            if (this.size <= this.maxSize) {
                if (!this.journalRebuildRequired()) return;
            }
            this.executorService.submit(this.cleanupCallable);
            return;
        }
    }

    private static void deleteIfExists(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Editor edit(String string2, long l) throws IOException {
        synchronized (this) {
            Editor editor;
            long l2;
            void var2_2;
            this.checkNotClosed();
            Entry entry = this.lruEntries.get(string2);
            if (var2_2 != -1L && (entry == null || (l2 = entry.sequenceNumber) != var2_2)) {
                return null;
            }
            if (entry == null) {
                entry = new Entry(this, string2);
                this.lruEntries.put(string2, entry);
            } else {
                editor = entry.currentEditor;
                if (editor != null) {
                    return null;
                }
            }
            editor = new Editor(this, entry);
            Entry.access$802(entry, editor);
            this.journalWriter.append(DIRTY);
            this.journalWriter.append(' ');
            this.journalWriter.append(string2);
            this.journalWriter.append('\n');
            DiskLruCache.flushWriter(this.journalWriter);
            return editor;
        }
    }

    private static void flushWriter(Writer writer) throws IOException {
        if (Build.VERSION.SDK_INT < 26) {
            writer.flush();
            return;
        }
        StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)new StrictMode.ThreadPolicy.Builder(threadPolicy).permitUnbufferedIo().build());
        try {
            writer.flush();
            return;
        }
        finally {
            StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
        }
    }

    private static String inputStreamToString(InputStream inputStream) throws IOException {
        return Util.readFully(new InputStreamReader(inputStream, Util.UTF_8));
    }

    private boolean journalRebuildRequired() {
        int n = this.redundantOpCount;
        boolean bl = n >= 2000 && n >= this.lruEntries.size();
        return bl;
    }

    public static DiskLruCache open(File object, int n, int n2, long l) throws IOException {
        if (l > 0L) {
            if (n2 > 0) {
                Comparable<File> comparable = new File((File)object, JOURNAL_FILE_BACKUP);
                if (((File)comparable).exists()) {
                    File file = new File((File)object, JOURNAL_FILE);
                    if (file.exists()) {
                        ((File)comparable).delete();
                    } else {
                        DiskLruCache.renameTo((File)comparable, file, false);
                    }
                }
                DiskLruCache diskLruCache = new DiskLruCache((File)object, n, n2, l);
                if (diskLruCache.journalFile.exists()) {
                    try {
                        diskLruCache.readJournal();
                        diskLruCache.processJournal();
                        return diskLruCache;
                    }
                    catch (IOException iOException) {
                        PrintStream printStream = System.out;
                        comparable = new StringBuilder();
                        ((StringBuilder)comparable).append("DiskLruCache ");
                        ((StringBuilder)comparable).append(object);
                        ((StringBuilder)comparable).append(" is corrupt: ");
                        ((StringBuilder)comparable).append(iOException.getMessage());
                        ((StringBuilder)comparable).append(", removing");
                        printStream.println(((StringBuilder)comparable).toString());
                        diskLruCache.delete();
                    }
                }
                ((File)object).mkdirs();
                object = new DiskLruCache((File)object, n, n2, l);
                super.rebuildJournal();
                return object;
            }
            throw new IllegalArgumentException("valueCount <= 0");
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }

    private void processJournal() throws IOException {
        DiskLruCache.deleteIfExists(this.journalFileTmp);
        Iterator<Entry> iterator2 = this.lruEntries.values().iterator();
        while (iterator2.hasNext()) {
            int n;
            Entry entry = iterator2.next();
            if (entry.currentEditor == null) {
                for (n = 0; n < this.valueCount; ++n) {
                    this.size += entry.lengths[n];
                }
                continue;
            }
            Entry.access$802(entry, null);
            for (n = 0; n < this.valueCount; ++n) {
                DiskLruCache.deleteIfExists(entry.getCleanFile(n));
                DiskLruCache.deleteIfExists(entry.getDirtyFile(n));
            }
            iterator2.remove();
        }
    }

    /*
     * Loose catch block
     */
    private void readJournal() throws IOException {
        String string2;
        Object object;
        CharSequence charSequence;
        String string3;
        Object object2;
        StrictLineReader strictLineReader;
        block8: {
            boolean bl;
            strictLineReader = new StrictLineReader(new FileInputStream(this.journalFile), Util.US_ASCII);
            object2 = strictLineReader.readLine();
            string3 = strictLineReader.readLine();
            charSequence = strictLineReader.readLine();
            object = strictLineReader.readLine();
            string2 = strictLineReader.readLine();
            if (!MAGIC.equals(object2) || !VERSION_1.equals(string3) || !Integer.toString(this.appVersion).equals(charSequence) || !Integer.toString(this.valueCount).equals(object) || !(bl = "".equals(string2))) break block8;
            int n = 0;
            while (true) {
                try {
                    this.readJournalLine(strictLineReader.readLine());
                    ++n;
                }
                catch (EOFException eOFException) {
                    block9: {
                        this.redundantOpCount = n - this.lruEntries.size();
                        if (strictLineReader.hasUnterminatedLine()) {
                            this.rebuildJournal();
                            break block9;
                        }
                        object2 = new FileOutputStream(this.journalFile, true);
                        object = new OutputStreamWriter((OutputStream)object2, Util.US_ASCII);
                        BufferedWriter bufferedWriter = new BufferedWriter((Writer)object);
                        this.journalWriter = bufferedWriter;
                    }
                    Util.closeQuietly(strictLineReader);
                    return;
                }
            }
        }
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append("unexpected journal header: [");
        ((StringBuilder)charSequence).append((String)object2);
        ((StringBuilder)charSequence).append(", ");
        ((StringBuilder)charSequence).append(string3);
        ((StringBuilder)charSequence).append(", ");
        ((StringBuilder)charSequence).append((String)object);
        ((StringBuilder)charSequence).append(", ");
        ((StringBuilder)charSequence).append(string2);
        ((StringBuilder)charSequence).append("]");
        IOException iOException = new IOException(((StringBuilder)charSequence).toString());
        throw iOException;
        {
            catch (Throwable throwable) {
                Util.closeQuietly(strictLineReader);
                throw throwable;
            }
        }
    }

    private void readJournalLine(String stringArray) throws IOException {
        block9: {
            CharSequence charSequence;
            block13: {
                block11: {
                    int n;
                    int n2;
                    block12: {
                        Object object;
                        block10: {
                            n2 = stringArray.indexOf(32);
                            if (n2 == -1) break block9;
                            int n3 = n2 + 1;
                            n = stringArray.indexOf(32, n3);
                            if (n == -1) {
                                object = stringArray.substring(n3);
                                charSequence = object;
                                if (n2 == REMOVE.length()) {
                                    charSequence = object;
                                    if (stringArray.startsWith(REMOVE)) {
                                        this.lruEntries.remove(object);
                                        return;
                                    }
                                }
                            } else {
                                charSequence = stringArray.substring(n3, n);
                            }
                            Entry entry = this.lruEntries.get(charSequence);
                            object = entry;
                            if (entry == null) {
                                object = new Entry(this, (String)charSequence);
                                this.lruEntries.put((String)charSequence, (Entry)object);
                            }
                            if (n == -1 || n2 != CLEAN.length() || !stringArray.startsWith(CLEAN)) break block10;
                            stringArray = stringArray.substring(n + 1).split(" ");
                            Entry.access$702((Entry)object, true);
                            Entry.access$802((Entry)object, null);
                            ((Entry)object).setLengths(stringArray);
                            break block11;
                        }
                        if (n != -1 || n2 != DIRTY.length() || !stringArray.startsWith(DIRTY)) break block12;
                        Entry.access$802((Entry)object, new Editor(this, (Entry)object));
                        break block11;
                    }
                    if (n != -1 || n2 != READ.length() || !stringArray.startsWith(READ)) break block13;
                }
                return;
            }
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("unexpected journal line: ");
            ((StringBuilder)charSequence).append((String)stringArray);
            throw new IOException(((StringBuilder)charSequence).toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unexpected journal line: ");
        stringBuilder.append((String)stringArray);
        throw new IOException(stringBuilder.toString());
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void rebuildJournal() throws IOException {
        synchronized (this) {
            Writer writer = this.journalWriter;
            if (writer != null) {
                DiskLruCache.closeWriter(writer);
            }
            Object object = new FileOutputStream(this.journalFileTmp);
            Object object2 = new OutputStreamWriter((OutputStream)object, Util.US_ASCII);
            writer = new BufferedWriter((Writer)object2);
            writer.write(MAGIC);
            writer.write("\n");
            writer.write(VERSION_1);
            writer.write("\n");
            writer.write(Integer.toString(this.appVersion));
            writer.write("\n");
            writer.write(Integer.toString(this.valueCount));
            writer.write("\n");
            writer.write("\n");
            object2 = this.lruEntries.values().iterator();
            while (object2.hasNext()) {
                StringBuilder stringBuilder;
                object = (Entry)object2.next();
                if (((Entry)object).currentEditor != null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("DIRTY ");
                    stringBuilder.append(((Entry)object).key);
                    stringBuilder.append('\n');
                    writer.write(stringBuilder.toString());
                    continue;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("CLEAN ");
                stringBuilder.append(((Entry)object).key);
                stringBuilder.append(((Entry)object).getLengths());
                stringBuilder.append('\n');
                writer.write(stringBuilder.toString());
            }
            {
                catch (Throwable throwable) {
                    DiskLruCache.closeWriter(writer);
                    throw throwable;
                }
            }
            DiskLruCache.closeWriter(writer);
            if (this.journalFile.exists()) {
                DiskLruCache.renameTo(this.journalFile, this.journalFileBackup, true);
            }
            DiskLruCache.renameTo(this.journalFileTmp, this.journalFile, false);
            this.journalFileBackup.delete();
            object = new FileOutputStream(this.journalFile, true);
            writer = new OutputStreamWriter((OutputStream)object, Util.US_ASCII);
            this.journalWriter = object2 = new BufferedWriter(writer);
            return;
        }
    }

    private static void renameTo(File file, File file2, boolean bl) throws IOException {
        if (bl) {
            DiskLruCache.deleteIfExists(file2);
        }
        if (file.renameTo(file2)) {
            return;
        }
        throw new IOException();
    }

    private void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            this.remove(this.lruEntries.entrySet().iterator().next().getKey());
        }
    }

    @Override
    public void close() throws IOException {
        synchronized (this) {
            Object object;
            block5: {
                object = this.journalWriter;
                if (object != null) break block5;
                return;
            }
            try {
                object = new ArrayList(this.lruEntries.values());
                object = ((ArrayList)object).iterator();
                while (object.hasNext()) {
                    Entry entry = (Entry)object.next();
                    if (entry.currentEditor == null) continue;
                    entry.currentEditor.abort();
                }
                this.trimToSize();
                DiskLruCache.closeWriter(this.journalWriter);
                this.journalWriter = null;
                return;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    public void delete() throws IOException {
        this.close();
        Util.deleteContents(this.directory);
    }

    public Editor edit(String string2) throws IOException {
        return this.edit(string2, -1L);
    }

    public void flush() throws IOException {
        synchronized (this) {
            this.checkNotClosed();
            this.trimToSize();
            DiskLruCache.flushWriter(this.journalWriter);
            return;
        }
    }

    public Value get(String object) throws IOException {
        synchronized (this) {
            boolean bl;
            Entry entry;
            block10: {
                block9: {
                    this.checkNotClosed();
                    entry = this.lruEntries.get(object);
                    if (entry != null) break block9;
                    return null;
                }
                bl = entry.readable;
                if (bl) break block10;
                return null;
            }
            File[] fileArray = entry.cleanFiles;
            int n = fileArray.length;
            for (int i = 0; i < n; ++i) {
                bl = fileArray[i].exists();
                if (bl) continue;
                return null;
            }
            try {
                ++this.redundantOpCount;
                this.journalWriter.append(READ);
                this.journalWriter.append(' ');
                this.journalWriter.append((CharSequence)object);
                this.journalWriter.append('\n');
                if (this.journalRebuildRequired()) {
                    this.executorService.submit(this.cleanupCallable);
                }
                object = new Value(this, (String)object, entry.sequenceNumber, entry.cleanFiles, entry.lengths);
                return object;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    public File getDirectory() {
        return this.directory;
    }

    public long getMaxSize() {
        synchronized (this) {
            long l = this.maxSize;
            return l;
        }
    }

    public boolean isClosed() {
        synchronized (this) {
            Writer writer = this.journalWriter;
            boolean bl = writer == null;
            return bl;
        }
    }

    public boolean remove(String object) throws IOException {
        synchronized (this) {
            block9: {
                this.checkNotClosed();
                Object object2 = this.lruEntries.get(object);
                if (object2 == null) break block9;
                if (((Entry)object2).currentEditor != null) break block9;
                int n = 0;
                while (true) {
                    if (n >= this.valueCount) break;
                    File file = ((Entry)object2).getCleanFile(n);
                    if (file.exists() && !file.delete()) {
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append("failed to delete ");
                        ((StringBuilder)object2).append(file);
                        object = new IOException(((StringBuilder)object2).toString());
                        throw object;
                    }
                    this.size -= ((Entry)object2).lengths[n];
                    ((Entry)object2).lengths[n] = 0L;
                    ++n;
                    continue;
                    break;
                }
                try {
                    ++this.redundantOpCount;
                    this.journalWriter.append(REMOVE);
                    this.journalWriter.append(' ');
                    this.journalWriter.append((CharSequence)object);
                    this.journalWriter.append('\n');
                    this.lruEntries.remove(object);
                    if (this.journalRebuildRequired()) {
                        this.executorService.submit(this.cleanupCallable);
                    }
                    return true;
                }
                catch (Throwable throwable) {}
                {
                    throw throwable;
                }
            }
            return false;
        }
    }

    public void setMaxSize(long l) {
        synchronized (this) {
            this.maxSize = l;
            this.executorService.submit(this.cleanupCallable);
            return;
        }
    }

    public long size() {
        synchronized (this) {
            long l = this.size;
            return l;
        }
    }

    private static final class DiskLruCacheThreadFactory
    implements ThreadFactory {
        private DiskLruCacheThreadFactory() {
        }

        @Override
        public Thread newThread(Runnable runnable) {
            synchronized (this) {
                Thread thread2 = new Thread(runnable, "glide-disk-lru-cache-thread");
                thread2.setPriority(1);
                return thread2;
            }
        }
    }

    public final class Editor {
        private boolean committed;
        private final Entry entry;
        final DiskLruCache this$0;
        private final boolean[] written;

        private Editor(DiskLruCache object, Entry entry) {
            this.this$0 = object;
            this.entry = entry;
            object = entry.readable ? null : (Object)new boolean[((DiskLruCache)object).valueCount];
            this.written = (boolean[])object;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private InputStream newInputStream(int n) throws IOException {
            DiskLruCache diskLruCache = this.this$0;
            synchronized (diskLruCache) {
                if (this.entry.currentEditor != this) {
                    IllegalStateException illegalStateException = new IllegalStateException();
                    throw illegalStateException;
                }
                if (!this.entry.readable) {
                    return null;
                }
                try {
                    return new FileInputStream(this.entry.getCleanFile(n));
                }
                catch (FileNotFoundException fileNotFoundException) {
                    return null;
                }
            }
        }

        public void abort() throws IOException {
            this.this$0.completeEdit(this, false);
        }

        public void abortUnlessCommitted() {
            if (!this.committed) {
                try {
                    this.abort();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
        }

        public void commit() throws IOException {
            this.this$0.completeEdit(this, true);
            this.committed = true;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public File getFile(int n) throws IOException {
            DiskLruCache diskLruCache = this.this$0;
            synchronized (diskLruCache) {
                if (this.entry.currentEditor != this) {
                    IllegalStateException illegalStateException = new IllegalStateException();
                    throw illegalStateException;
                }
                if (!this.entry.readable) {
                    this.written[n] = true;
                }
                File file = this.entry.getDirtyFile(n);
                this.this$0.directory.mkdirs();
                return file;
            }
        }

        public String getString(int n) throws IOException {
            Object object = this.newInputStream(n);
            object = object != null ? DiskLruCache.inputStreamToString((InputStream)object) : null;
            return object;
        }

        public void set(int n, String string2) throws IOException {
            OutputStreamWriter outputStreamWriter;
            FileOutputStream fileOutputStream;
            OutputStreamWriter outputStreamWriter2;
            OutputStreamWriter outputStreamWriter3 = outputStreamWriter2 = null;
            outputStreamWriter3 = outputStreamWriter2;
            try {
                fileOutputStream = new FileOutputStream(this.getFile(n));
                outputStreamWriter3 = outputStreamWriter2;
                outputStreamWriter3 = outputStreamWriter2;
            }
            catch (Throwable throwable) {
                Util.closeQuietly(outputStreamWriter3);
                throw throwable;
            }
            outputStreamWriter3 = outputStreamWriter = new OutputStreamWriter((OutputStream)fileOutputStream, Util.UTF_8);
            outputStreamWriter.write(string2);
            Util.closeQuietly(outputStreamWriter);
        }
    }

    private final class Entry {
        File[] cleanFiles;
        private Editor currentEditor;
        File[] dirtyFiles;
        private final String key;
        private final long[] lengths;
        private boolean readable;
        private long sequenceNumber;
        final DiskLruCache this$0;

        private Entry(DiskLruCache diskLruCache, String charSequence) {
            this.this$0 = diskLruCache;
            this.key = charSequence;
            this.lengths = new long[diskLruCache.valueCount];
            this.cleanFiles = new File[diskLruCache.valueCount];
            this.dirtyFiles = new File[diskLruCache.valueCount];
            charSequence = new StringBuilder((String)charSequence).append('.');
            int n = ((StringBuilder)charSequence).length();
            for (int i = 0; i < diskLruCache.valueCount; ++i) {
                ((StringBuilder)charSequence).append(i);
                this.cleanFiles[i] = new File(diskLruCache.directory, ((StringBuilder)charSequence).toString());
                ((StringBuilder)charSequence).append(".tmp");
                this.dirtyFiles[i] = new File(diskLruCache.directory, ((StringBuilder)charSequence).toString());
                ((StringBuilder)charSequence).setLength(n);
            }
        }

        static /* synthetic */ long access$1302(Entry entry, long l) {
            entry.sequenceNumber = l;
            return l;
        }

        static /* synthetic */ boolean access$702(Entry entry, boolean bl) {
            entry.readable = bl;
            return bl;
        }

        static /* synthetic */ Editor access$802(Entry entry, Editor editor) {
            entry.currentEditor = editor;
            return editor;
        }

        private IOException invalidLengths(String[] stringArray) throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unexpected journal line: ");
            stringBuilder.append(Arrays.toString(stringArray));
            throw new IOException(stringBuilder.toString());
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private void setLengths(String[] stringArray) throws IOException {
            if (stringArray.length != this.this$0.valueCount) {
                stringArray = this.invalidLengths(stringArray);
                throw stringArray;
            }
            int n = 0;
            try {
                while (n < stringArray.length) {
                    this.lengths[n] = Long.parseLong(stringArray[n]);
                    ++n;
                }
                return;
            }
            catch (NumberFormatException numberFormatException) {
                throw this.invalidLengths(stringArray);
            }
        }

        public File getCleanFile(int n) {
            return this.cleanFiles[n];
        }

        public File getDirtyFile(int n) {
            return this.dirtyFiles[n];
        }

        public String getLengths() throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            for (long l : this.lengths) {
                stringBuilder.append(' ');
                stringBuilder.append(l);
            }
            return stringBuilder.toString();
        }
    }

    public final class Value {
        private final File[] files;
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;
        final DiskLruCache this$0;

        private Value(DiskLruCache diskLruCache, String string2, long l, File[] fileArray, long[] lArray) {
            this.this$0 = diskLruCache;
            this.key = string2;
            this.sequenceNumber = l;
            this.files = fileArray;
            this.lengths = lArray;
        }

        public Editor edit() throws IOException {
            return this.this$0.edit(this.key, this.sequenceNumber);
        }

        public File getFile(int n) {
            return this.files[n];
        }

        public long getLength(int n) {
            return this.lengths[n];
        }

        public String getString(int n) throws IOException {
            return DiskLruCache.inputStreamToString(new FileInputStream(this.files[n]));
        }
    }
}

