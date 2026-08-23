/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal;

import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import okhttp3.internal.FaultHidingSink;
import okhttp3.internal.Platform;
import okhttp3.internal.Util;
import okhttp3.internal.io.FileSystem;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class DiskLruCache
implements Closeable,
Flushable {
    static final boolean $assertionsDisabled = false;
    static final long ANY_SEQUENCE_NUMBER = -1L;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final Pattern LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,120}");
    static final String MAGIC = "libcore.io.DiskLruCache";
    private static final Sink NULL_SINK = new Sink(){

        @Override
        public void close() throws IOException {
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public Timeout timeout() {
            return Timeout.NONE;
        }

        @Override
        public void write(Buffer buffer, long l) throws IOException {
            buffer.skip(l);
        }
    };
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    static final String VERSION_1 = "1";
    private final int appVersion;
    private final Runnable cleanupRunnable;
    private boolean closed;
    private final File directory;
    private final Executor executor;
    private final FileSystem fileSystem;
    private boolean hasJournalErrors;
    private boolean initialized;
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    private BufferedSink journalWriter;
    private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap(0, 0.75f, true);
    private long maxSize;
    private boolean mostRecentRebuildFailed;
    private boolean mostRecentTrimFailed;
    private long nextSequenceNumber = 0L;
    private int redundantOpCount;
    private long size = 0L;
    private final int valueCount;

    DiskLruCache(FileSystem fileSystem, File file, int n, int n2, long l, Executor executor) {
        this.cleanupRunnable = new Runnable(this){
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
            public void run() {
                DiskLruCache diskLruCache = this.this$0;
                synchronized (diskLruCache) {
                    boolean bl = !this.this$0.initialized;
                    if (bl | this.this$0.closed) {
                        return;
                    }
                    try {
                        this.this$0.trimToSize();
                    }
                    catch (IOException iOException) {
                        DiskLruCache.access$302(this.this$0, true);
                    }
                    try {
                        if (this.this$0.journalRebuildRequired()) {
                            this.this$0.rebuildJournal();
                            DiskLruCache.access$602(this.this$0, 0);
                        }
                    }
                    catch (IOException iOException) {
                        DiskLruCache.access$702(this.this$0, true);
                        DiskLruCache.access$802(this.this$0, Okio.buffer(NULL_SINK));
                    }
                    return;
                }
            }
        };
        this.fileSystem = fileSystem;
        this.directory = file;
        this.appVersion = n;
        this.journalFile = new File(file, JOURNAL_FILE);
        this.journalFileTmp = new File(file, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(file, JOURNAL_FILE_BACKUP);
        this.valueCount = n2;
        this.maxSize = l;
        this.executor = executor;
    }

    static /* synthetic */ boolean access$1002(DiskLruCache diskLruCache, boolean bl) {
        diskLruCache.hasJournalErrors = bl;
        return bl;
    }

    static /* synthetic */ boolean access$302(DiskLruCache diskLruCache, boolean bl) {
        diskLruCache.mostRecentTrimFailed = bl;
        return bl;
    }

    static /* synthetic */ int access$602(DiskLruCache diskLruCache, int n) {
        diskLruCache.redundantOpCount = n;
        return n;
    }

    static /* synthetic */ boolean access$702(DiskLruCache diskLruCache, boolean bl) {
        diskLruCache.mostRecentRebuildFailed = bl;
        return bl;
    }

    static /* synthetic */ BufferedSink access$802(DiskLruCache diskLruCache, BufferedSink bufferedSink) {
        diskLruCache.journalWriter = bufferedSink;
        return bufferedSink;
    }

    private void checkNotClosed() {
        synchronized (this) {
            block4: {
                boolean bl = this.isClosed();
                if (bl) break block4;
                return;
            }
            IllegalStateException illegalStateException = new IllegalStateException("cache is closed");
            throw illegalStateException;
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
                        if (this.fileSystem.exists(((Entry)object2).dirtyFiles[n])) continue;
                        ((Editor)object).abort();
                        return;
                    }
                    ((Editor)object).abort();
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append("Newly created entry didn't create value for index ");
                    ((StringBuilder)object2).append(n);
                    object = new IllegalStateException(((StringBuilder)object2).toString());
                    throw object;
                }
            }
            for (n = 0; n < this.valueCount; ++n) {
                object = ((Entry)object2).dirtyFiles[n];
                if (bl) {
                    long l2;
                    if (!this.fileSystem.exists((File)object)) continue;
                    File file = ((Entry)object2).cleanFiles[n];
                    this.fileSystem.rename((File)object, file);
                    l = ((Entry)object2).lengths[n];
                    ((Entry)object2).lengths[n] = l2 = this.fileSystem.size(file);
                    this.size = this.size - l + l2;
                    continue;
                }
                this.fileSystem.delete((File)object);
            }
            ++this.redundantOpCount;
            Entry.access$1302((Entry)object2, null);
            if (((Entry)object2).readable | bl) {
                Entry.access$1202((Entry)object2, true);
                this.journalWriter.writeUtf8(CLEAN).writeByte(32);
                this.journalWriter.writeUtf8(((Entry)object2).key);
                ((Entry)object2).writeLengths(this.journalWriter);
                this.journalWriter.writeByte(10);
                if (bl) {
                    l = this.nextSequenceNumber;
                    this.nextSequenceNumber = 1L + l;
                    Entry.access$2002((Entry)object2, l);
                }
            } else {
                this.lruEntries.remove(((Entry)object2).key);
                this.journalWriter.writeUtf8(REMOVE).writeByte(32);
                this.journalWriter.writeUtf8(((Entry)object2).key);
                this.journalWriter.writeByte(10);
            }
            this.journalWriter.flush();
            if (this.size <= this.maxSize) {
                if (!this.journalRebuildRequired()) return;
            }
            this.executor.execute(this.cleanupRunnable);
            return;
        }
    }

    public static DiskLruCache create(FileSystem fileSystem, File file, int n, int n2, long l) {
        if (l > 0L) {
            if (n2 > 0) {
                return new DiskLruCache(fileSystem, file, n, n2, l, new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Util.threadFactory("OkHttp DiskLruCache", true)));
            }
            throw new IllegalArgumentException("valueCount <= 0");
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Editor edit(String object, long l) throws IOException {
        synchronized (this) {
            Object object2;
            long l2;
            void var2_2;
            this.initialize();
            this.checkNotClosed();
            this.validateKey((String)object);
            Entry entry = this.lruEntries.get(object);
            if (var2_2 != -1L && (entry == null || (l2 = entry.sequenceNumber) != var2_2)) {
                return null;
            }
            if (entry != null && (object2 = entry.currentEditor) != null) {
                return null;
            }
            if (!this.mostRecentTrimFailed && !this.mostRecentRebuildFailed) {
                this.journalWriter.writeUtf8(DIRTY).writeByte(32).writeUtf8((String)object).writeByte(10);
                this.journalWriter.flush();
                boolean bl = this.hasJournalErrors;
                if (bl) {
                    return null;
                }
                object2 = entry;
                if (entry == null) {
                    object2 = new Entry(this, (String)object);
                    this.lruEntries.put((String)object, (Entry)object2);
                }
                object = new Editor(this, (Entry)object2);
                Entry.access$1302((Entry)object2, (Editor)object);
                return object;
            }
            this.executor.execute(this.cleanupRunnable);
            return null;
        }
    }

    private boolean journalRebuildRequired() {
        int n = this.redundantOpCount;
        boolean bl = n >= 2000 && n >= this.lruEntries.size();
        return bl;
    }

    private BufferedSink newJournalWriter() throws FileNotFoundException {
        return Okio.buffer(new FaultHidingSink(this, this.fileSystem.appendingSink(this.journalFile)){
            static final boolean $assertionsDisabled = false;
            final DiskLruCache this$0;
            {
                this.this$0 = diskLruCache;
                super(sink);
            }

            @Override
            protected void onException(IOException iOException) {
                if (Thread.holdsLock(this.this$0)) {
                    DiskLruCache.access$1002(this.this$0, true);
                    return;
                }
                throw new AssertionError();
            }
        });
    }

    private void processJournal() throws IOException {
        this.fileSystem.delete(this.journalFileTmp);
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
            Entry.access$1302(entry, null);
            for (n = 0; n < this.valueCount; ++n) {
                this.fileSystem.delete(entry.cleanFiles[n]);
                this.fileSystem.delete(entry.dirtyFiles[n]);
            }
            iterator2.remove();
        }
    }

    /*
     * Loose catch block
     */
    private void readJournal() throws IOException {
        String string2;
        String string3;
        Object object;
        String string4;
        String string5;
        BufferedSource bufferedSource;
        block8: {
            boolean bl;
            bufferedSource = Okio.buffer(this.fileSystem.source(this.journalFile));
            string5 = bufferedSource.readUtf8LineStrict();
            string4 = bufferedSource.readUtf8LineStrict();
            object = bufferedSource.readUtf8LineStrict();
            string3 = bufferedSource.readUtf8LineStrict();
            string2 = bufferedSource.readUtf8LineStrict();
            if (!MAGIC.equals(string5) || !VERSION_1.equals(string4) || !Integer.toString(this.appVersion).equals(object) || !Integer.toString(this.valueCount).equals(string3) || !(bl = "".equals(string2))) break block8;
            int n = 0;
            while (true) {
                try {
                    this.readJournalLine(bufferedSource.readUtf8LineStrict());
                    ++n;
                }
                catch (EOFException eOFException) {
                    block9: {
                        this.redundantOpCount = n - this.lruEntries.size();
                        if (!bufferedSource.exhausted()) {
                            this.rebuildJournal();
                            break block9;
                        }
                        this.journalWriter = this.newJournalWriter();
                    }
                    Util.closeQuietly(bufferedSource);
                    return;
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unexpected journal header: [");
        stringBuilder.append(string5);
        stringBuilder.append(", ");
        stringBuilder.append(string4);
        stringBuilder.append(", ");
        stringBuilder.append(string3);
        stringBuilder.append(", ");
        stringBuilder.append(string2);
        stringBuilder.append("]");
        object = new IOException(stringBuilder.toString());
        throw object;
        {
            catch (Throwable throwable) {
                Util.closeQuietly(bufferedSource);
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
                            Entry.access$1202((Entry)object, true);
                            Entry.access$1302((Entry)object, null);
                            ((Entry)object).setLengths(stringArray);
                            break block11;
                        }
                        if (n != -1 || n2 != DIRTY.length() || !stringArray.startsWith(DIRTY)) break block12;
                        Entry.access$1302((Entry)object, new Editor(this, (Entry)object));
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
            BufferedSink bufferedSink = this.journalWriter;
            if (bufferedSink != null) {
                bufferedSink.close();
            }
            bufferedSink = Okio.buffer(this.fileSystem.sink(this.journalFileTmp));
            bufferedSink.writeUtf8(MAGIC).writeByte(10);
            bufferedSink.writeUtf8(VERSION_1).writeByte(10);
            bufferedSink.writeDecimalLong(this.appVersion).writeByte(10);
            bufferedSink.writeDecimalLong(this.valueCount).writeByte(10);
            bufferedSink.writeByte(10);
            for (Entry entry : this.lruEntries.values()) {
                if (entry.currentEditor != null) {
                    bufferedSink.writeUtf8(DIRTY).writeByte(32);
                    bufferedSink.writeUtf8(entry.key);
                    bufferedSink.writeByte(10);
                    continue;
                }
                bufferedSink.writeUtf8(CLEAN).writeByte(32);
                bufferedSink.writeUtf8(entry.key);
                entry.writeLengths(bufferedSink);
                bufferedSink.writeByte(10);
            }
            {
                catch (Throwable throwable) {
                    bufferedSink.close();
                    throw throwable;
                }
            }
            bufferedSink.close();
            if (this.fileSystem.exists(this.journalFile)) {
                this.fileSystem.rename(this.journalFile, this.journalFileBackup);
            }
            this.fileSystem.rename(this.journalFileTmp, this.journalFile);
            this.fileSystem.delete(this.journalFileBackup);
            this.journalWriter = this.newJournalWriter();
            this.hasJournalErrors = false;
            this.mostRecentRebuildFailed = false;
            return;
        }
    }

    private boolean removeEntry(Entry entry) throws IOException {
        if (entry.currentEditor != null) {
            entry.currentEditor.detach();
        }
        for (int i = 0; i < this.valueCount; ++i) {
            this.fileSystem.delete(entry.cleanFiles[i]);
            this.size -= entry.lengths[i];
            ((Entry)entry).lengths[i] = 0L;
        }
        ++this.redundantOpCount;
        this.journalWriter.writeUtf8(REMOVE).writeByte(32).writeUtf8(entry.key).writeByte(10);
        this.lruEntries.remove(entry.key);
        if (this.journalRebuildRequired()) {
            this.executor.execute(this.cleanupRunnable);
        }
        return true;
    }

    private void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            this.removeEntry(this.lruEntries.values().iterator().next());
        }
        this.mostRecentTrimFailed = false;
    }

    private void validateKey(String string2) {
        if (LEGAL_KEY_PATTERN.matcher(string2).matches()) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("keys must match regex [a-z0-9_-]{1,120}: \"");
        stringBuilder.append(string2);
        stringBuilder.append("\"");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void close() throws IOException {
        synchronized (this) {
            int n;
            Entry[] entryArray;
            if (this.initialized && !this.closed) {
                entryArray = this.lruEntries.values().toArray(new Entry[this.lruEntries.size()]);
                n = entryArray.length;
            } else {
                this.closed = true;
                return;
            }
            for (int i = 0; i < n; ++i) {
                Entry entry = entryArray[i];
                if (entry.currentEditor == null) continue;
                entry.currentEditor.abort();
                continue;
            }
            this.trimToSize();
            this.journalWriter.close();
            this.journalWriter = null;
            this.closed = true;
            return;
        }
    }

    public void delete() throws IOException {
        this.close();
        this.fileSystem.deleteContents(this.directory);
    }

    public Editor edit(String string2) throws IOException {
        return this.edit(string2, -1L);
    }

    public void evictAll() throws IOException {
        synchronized (this) {
            this.initialize();
            Entry[] entryArray = this.lruEntries.values().toArray(new Entry[this.lruEntries.size()]);
            int n = entryArray.length;
            for (int i = 0; i < n; ++i) {
                this.removeEntry(entryArray[i]);
                continue;
            }
            try {
                this.mostRecentTrimFailed = false;
                return;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    @Override
    public void flush() throws IOException {
        synchronized (this) {
            block4: {
                boolean bl = this.initialized;
                if (bl) break block4;
                return;
            }
            this.checkNotClosed();
            this.trimToSize();
            this.journalWriter.flush();
            return;
        }
    }

    public Snapshot get(String string2) throws IOException {
        synchronized (this) {
            block6: {
                Object object;
                block7: {
                    this.initialize();
                    this.checkNotClosed();
                    this.validateKey(string2);
                    object = this.lruEntries.get(string2);
                    if (object == null) break block6;
                    if (!((Entry)object).readable) break block6;
                    if ((object = ((Entry)object).snapshot()) != null) break block7;
                    return null;
                }
                ++this.redundantOpCount;
                this.journalWriter.writeUtf8(READ).writeByte(32).writeUtf8(string2).writeByte(10);
                if (this.journalRebuildRequired()) {
                    this.executor.execute(this.cleanupRunnable);
                }
                return object;
            }
            return null;
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

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void initialize() throws IOException {
        synchronized (this) {
            Throwable throwable2;
            block12: {
                block10: {
                    boolean bl;
                    block11: {
                        try {
                            if (!Thread.holdsLock(this)) break block10;
                            bl = this.initialized;
                            if (!bl) break block11;
                        }
                        catch (Throwable throwable2) {
                            break block12;
                        }
                        return;
                    }
                    if (this.fileSystem.exists(this.journalFileBackup)) {
                        if (this.fileSystem.exists(this.journalFile)) {
                            this.fileSystem.delete(this.journalFileBackup);
                        } else {
                            this.fileSystem.rename(this.journalFileBackup, this.journalFile);
                        }
                    }
                    if (bl = this.fileSystem.exists(this.journalFile)) {
                        try {
                            this.readJournal();
                            this.processJournal();
                            this.initialized = true;
                            return;
                        }
                        catch (IOException iOException) {
                            Platform platform = Platform.get();
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("DiskLruCache ");
                            stringBuilder.append(this.directory);
                            stringBuilder.append(" is corrupt: ");
                            stringBuilder.append(iOException.getMessage());
                            stringBuilder.append(", removing");
                            platform.log(5, stringBuilder.toString(), iOException);
                            this.delete();
                            this.closed = false;
                        }
                    }
                    this.rebuildJournal();
                    this.initialized = true;
                    return;
                }
                AssertionError assertionError = new AssertionError();
                throw assertionError;
            }
            throw throwable2;
        }
    }

    public boolean isClosed() {
        synchronized (this) {
            boolean bl = this.closed;
            return bl;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean remove(String object) throws IOException {
        synchronized (this) {
            this.initialize();
            this.checkNotClosed();
            this.validateKey((String)object);
            object = this.lruEntries.get(object);
            if (object == null) {
                return false;
            }
            boolean bl = this.removeEntry((Entry)object);
            if (bl && this.size <= this.maxSize) {
                this.mostRecentTrimFailed = false;
            }
            return bl;
        }
    }

    public void setMaxSize(long l) {
        synchronized (this) {
            this.maxSize = l;
            if (this.initialized) {
                this.executor.execute(this.cleanupRunnable);
            }
            return;
        }
    }

    public long size() throws IOException {
        synchronized (this) {
            this.initialize();
            long l = this.size;
            return l;
        }
    }

    public Iterator<Snapshot> snapshots() throws IOException {
        synchronized (this) {
            this.initialize();
            Iterator<Snapshot> iterator2 = new Iterator<Snapshot>(this){
                final Iterator<Entry> delegate;
                Snapshot nextSnapshot;
                Snapshot removeSnapshot;
                final DiskLruCache this$0;
                {
                    this.this$0 = diskLruCache;
                    this.delegate = new ArrayList(diskLruCache.lruEntries.values()).iterator();
                }

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 */
                @Override
                public boolean hasNext() {
                    if (this.nextSnapshot != null) {
                        return true;
                    }
                    DiskLruCache diskLruCache = this.this$0;
                    synchronized (diskLruCache) {
                        Snapshot snapshot;
                        if (this.this$0.closed) {
                            return false;
                        }
                        do {
                            if (!this.delegate.hasNext()) return false;
                        } while ((snapshot = this.delegate.next().snapshot()) == null);
                        this.nextSnapshot = snapshot;
                        return true;
                    }
                }

                @Override
                public Snapshot next() {
                    if (this.hasNext()) {
                        Snapshot snapshot;
                        this.removeSnapshot = snapshot = this.nextSnapshot;
                        this.nextSnapshot = null;
                        return snapshot;
                    }
                    throw new NoSuchElementException();
                }

                @Override
                public void remove() {
                    Snapshot snapshot = this.removeSnapshot;
                    if (snapshot != null) {
                        try {
                            this.this$0.remove(snapshot.key);
                        }
                        catch (Throwable throwable) {
                            this.removeSnapshot = null;
                            throw throwable;
                        }
                        catch (IOException iOException) {
                            // empty catch block
                        }
                        this.removeSnapshot = null;
                        return;
                    }
                    throw new IllegalStateException("remove() before next()");
                }
            };
            return iterator2;
        }
    }

    public final class Editor {
        private boolean done;
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
        public void abort() throws IOException {
            DiskLruCache diskLruCache = this.this$0;
            synchronized (diskLruCache) {
                if (this.done) {
                    IllegalStateException illegalStateException = new IllegalStateException();
                    throw illegalStateException;
                }
                if (this.entry.currentEditor == this) {
                    this.this$0.completeEdit(this, false);
                }
                this.done = true;
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void abortUnlessCommitted() {
            DiskLruCache diskLruCache = this.this$0;
            synchronized (diskLruCache) {
                Editor editor;
                if (!this.done && (editor = this.entry.currentEditor) == this) {
                    try {
                        this.this$0.completeEdit(this, false);
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void commit() throws IOException {
            DiskLruCache diskLruCache = this.this$0;
            synchronized (diskLruCache) {
                if (this.done) {
                    IllegalStateException illegalStateException = new IllegalStateException();
                    throw illegalStateException;
                }
                if (this.entry.currentEditor == this) {
                    this.this$0.completeEdit(this, true);
                }
                this.done = true;
                return;
            }
        }

        void detach() {
            if (this.entry.currentEditor == this) {
                for (int i = 0; i < this.this$0.valueCount; ++i) {
                    try {
                        this.this$0.fileSystem.delete(this.entry.dirtyFiles[i]);
                        continue;
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
                Entry.access$1302(this.entry, null);
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public Sink newSink(int n) throws IOException {
            DiskLruCache diskLruCache = this.this$0;
            synchronized (diskLruCache) {
                if (this.done) {
                    IllegalStateException illegalStateException = new IllegalStateException();
                    throw illegalStateException;
                }
                if (this.entry.currentEditor != this) {
                    return NULL_SINK;
                }
                if (!this.entry.readable) {
                    this.written[n] = true;
                }
                Object object = this.entry.dirtyFiles[n];
                try {
                    object = this.this$0.fileSystem.sink((File)object);
                    return new FaultHidingSink(this, (Sink)object){
                        final Editor this$1;
                        {
                            this.this$1 = editor;
                            super(sink);
                        }

                        /*
                         * Enabled aggressive block sorting
                         * Enabled unnecessary exception pruning
                         * Enabled aggressive exception aggregation
                         */
                        @Override
                        protected void onException(IOException object) {
                            object = this.this$1.this$0;
                            synchronized (object) {
                                this.this$1.detach();
                                return;
                            }
                        }
                    };
                }
                catch (FileNotFoundException fileNotFoundException) {
                    return NULL_SINK;
                }
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public Source newSource(int n) throws IOException {
            DiskLruCache diskLruCache = this.this$0;
            synchronized (diskLruCache) {
                if (this.done) {
                    IllegalStateException illegalStateException = new IllegalStateException();
                    throw illegalStateException;
                }
                if (!this.entry.readable) return null;
                Editor editor = this.entry.currentEditor;
                if (editor == this) {
                    try {
                        return this.this$0.fileSystem.source(this.entry.cleanFiles[n]);
                    }
                    catch (FileNotFoundException fileNotFoundException) {
                        return null;
                    }
                }
                return null;
            }
        }
    }

    private final class Entry {
        private final File[] cleanFiles;
        private Editor currentEditor;
        private final File[] dirtyFiles;
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

        static /* synthetic */ boolean access$1202(Entry entry, boolean bl) {
            entry.readable = bl;
            return bl;
        }

        static /* synthetic */ Editor access$1302(Entry entry, Editor editor) {
            entry.currentEditor = editor;
            return editor;
        }

        static /* synthetic */ long access$2002(Entry entry, long l) {
            entry.sequenceNumber = l;
            return l;
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

        Snapshot snapshot() {
            if (Thread.holdsLock(this.this$0)) {
                Source[] sourceArray = new Source[this.this$0.valueCount];
                Object object = (long[])this.lengths.clone();
                int n = 0;
                while (true) {
                    if (n >= this.this$0.valueCount) break;
                    sourceArray[n] = this.this$0.fileSystem.source(this.cleanFiles[n]);
                    ++n;
                    continue;
                    break;
                }
                try {
                    object = new Snapshot(this.this$0, this.key, this.sequenceNumber, sourceArray, (long[])object);
                    return object;
                }
                catch (FileNotFoundException fileNotFoundException) {
                    for (n = 0; n < this.this$0.valueCount && sourceArray[n] != null; ++n) {
                        Util.closeQuietly(sourceArray[n]);
                    }
                    try {
                        this.this$0.removeEntry(this);
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                    return null;
                }
            }
            AssertionError assertionError = new AssertionError();
            throw assertionError;
        }

        void writeLengths(BufferedSink bufferedSink) throws IOException {
            for (long l : this.lengths) {
                bufferedSink.writeByte(32).writeDecimalLong(l);
            }
        }
    }

    public final class Snapshot
    implements Closeable {
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;
        private final Source[] sources;
        final DiskLruCache this$0;

        private Snapshot(DiskLruCache diskLruCache, String string2, long l, Source[] sourceArray, long[] lArray) {
            this.this$0 = diskLruCache;
            this.key = string2;
            this.sequenceNumber = l;
            this.sources = sourceArray;
            this.lengths = lArray;
        }

        @Override
        public void close() {
            Source[] sourceArray = this.sources;
            int n = sourceArray.length;
            for (int i = 0; i < n; ++i) {
                Util.closeQuietly(sourceArray[i]);
            }
        }

        public Editor edit() throws IOException {
            return this.this$0.edit(this.key, this.sequenceNumber);
        }

        public long getLength(int n) {
            return this.lengths[n];
        }

        public Source getSource(int n) {
            return this.sources[n];
        }

        public String key() {
            return this.key;
        }
    }
}

