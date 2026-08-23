/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jUtil;

public class SplitOutputStream
extends OutputStream {
    private long bytesWrittenForThisPart;
    private int currSplitFileCounter;
    private File outFile;
    private RandomAccessFile raf;
    private long splitLength;
    private File zipFile;

    public SplitOutputStream(File file) throws FileNotFoundException, ZipException {
        this(file, -1L);
    }

    public SplitOutputStream(File file, long l) throws FileNotFoundException, ZipException {
        if (l >= 0L && l < 65536L) {
            throw new ZipException("split length less than minimum allowed split length of 65536 Bytes");
        }
        this.raf = new RandomAccessFile(file, "rw");
        this.splitLength = l;
        this.outFile = file;
        this.zipFile = file;
        this.currSplitFileCounter = 0;
        this.bytesWrittenForThisPart = 0L;
    }

    public SplitOutputStream(String object) throws FileNotFoundException, ZipException {
        object = Zip4jUtil.isStringNotNullAndNotEmpty((String)object) ? new File((String)object) : null;
        this((File)object);
    }

    public SplitOutputStream(String object, long l) throws FileNotFoundException, ZipException {
        object = !Zip4jUtil.isStringNotNullAndNotEmpty((String)object) ? new File((String)object) : null;
        this((File)object, l);
    }

    private boolean isHeaderData(byte[] objectArray) {
        if (objectArray != null && objectArray.length >= 4) {
            int n = Raw.readIntLittleEndian(objectArray, 0);
            objectArray = Zip4jUtil.getAllHeaderSignatures();
            if (objectArray != null && objectArray.length > 0) {
                for (int i = 0; i < objectArray.length; ++i) {
                    if (objectArray[i] == 134695760L || objectArray[i] != (long)n) continue;
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void startNextSplitFile() throws IOException {
        try {
            Object object;
            String string2 = Zip4jUtil.getZipFileNameWithoutExt(this.outFile.getName());
            CharSequence charSequence = this.zipFile.getAbsolutePath();
            if (this.outFile.getParent() == null) {
                object = "";
            } else {
                object = new StringBuilder();
                ((StringBuilder)object).append(this.outFile.getParent());
                ((StringBuilder)object).append(System.getProperty("file.separator"));
                object = ((StringBuilder)object).toString();
            }
            if (this.currSplitFileCounter < 9) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append((String)object);
                stringBuilder.append(string2);
                stringBuilder.append(".z0");
                stringBuilder.append(this.currSplitFileCounter + 1);
                File file = new File(stringBuilder.toString());
                object = file;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append((String)object);
                stringBuilder.append(string2);
                stringBuilder.append(".z");
                stringBuilder.append(this.currSplitFileCounter + 1);
                object = new File(stringBuilder.toString());
            }
            this.raf.close();
            if (((File)object).exists()) {
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("split file: ");
                ((StringBuilder)charSequence).append(((File)object).getName());
                ((StringBuilder)charSequence).append(" already exists in the current directory, cannot rename this file");
                IOException iOException = new IOException(((StringBuilder)charSequence).toString());
                throw iOException;
            }
            if (this.zipFile.renameTo((File)object)) {
                this.zipFile = object = new File((String)charSequence);
                this.raf = object = new RandomAccessFile(this.zipFile, "rw");
                ++this.currSplitFileCounter;
                return;
            }
            object = new IOException("cannot rename newly created split file");
            throw object;
        }
        catch (ZipException zipException) {
            throw new IOException(zipException.getMessage());
        }
    }

    public boolean checkBuffSizeAndStartNextSplitFile(int n) throws ZipException {
        if (n >= 0) {
            if (!this.isBuffSizeFitForCurrSplitFile(n)) {
                try {
                    this.startNextSplitFile();
                    this.bytesWrittenForThisPart = 0L;
                    return true;
                }
                catch (IOException iOException) {
                    throw new ZipException(iOException);
                }
            }
            return false;
        }
        throw new ZipException("negative buffersize for checkBuffSizeAndStartNextSplitFile");
    }

    @Override
    public void close() throws IOException {
        RandomAccessFile randomAccessFile = this.raf;
        if (randomAccessFile != null) {
            randomAccessFile.close();
        }
    }

    @Override
    public void flush() throws IOException {
    }

    public int getCurrSplitFileCounter() {
        return this.currSplitFileCounter;
    }

    public long getFilePointer() throws IOException {
        return this.raf.getFilePointer();
    }

    public long getSplitLength() {
        return this.splitLength;
    }

    public boolean isBuffSizeFitForCurrSplitFile(int n) throws ZipException {
        if (n >= 0) {
            long l = this.splitLength;
            boolean bl = true;
            if (l >= 65536L) {
                if (this.bytesWrittenForThisPart + (long)n > l) {
                    bl = false;
                }
                return bl;
            }
            return true;
        }
        throw new ZipException("negative buffersize for isBuffSizeFitForCurrSplitFile");
    }

    public boolean isSplitZipFile() {
        boolean bl = this.splitLength != -1L;
        return bl;
    }

    public void seek(long l) throws IOException {
        this.raf.seek(l);
    }

    @Override
    public void write(int n) throws IOException {
        this.write(new byte[]{(byte)n}, 0, 1);
    }

    @Override
    public void write(byte[] byArray) throws IOException {
        this.write(byArray, 0, byArray.length);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void write(byte[] byArray, int n, int n2) throws IOException {
        if (n2 <= 0) {
            return;
        }
        long l = this.splitLength;
        if (l != -1L) {
            if (l < 65536L) throw new IOException("split length less than minimum allowed split length of 65536 Bytes");
            long l2 = this.bytesWrittenForThisPart;
            if (l2 >= l) {
                this.startNextSplitFile();
                this.raf.write(byArray, n, n2);
                this.bytesWrittenForThisPart = n2;
                return;
            } else if (l2 + (long)n2 > l) {
                if (this.isHeaderData(byArray)) {
                    this.startNextSplitFile();
                    this.raf.write(byArray, n, n2);
                    this.bytesWrittenForThisPart = n2;
                    return;
                } else {
                    this.raf.write(byArray, n, (int)(this.splitLength - this.bytesWrittenForThisPart));
                    this.startNextSplitFile();
                    RandomAccessFile randomAccessFile = this.raf;
                    l2 = this.splitLength;
                    l = this.bytesWrittenForThisPart;
                    randomAccessFile.write(byArray, (int)(l2 - l) + n, (int)((long)n2 - (l2 - l)));
                    this.bytesWrittenForThisPart = (long)n2 - (this.splitLength - this.bytesWrittenForThisPart);
                }
                return;
            } else {
                this.raf.write(byArray, n, n2);
                this.bytesWrittenForThisPart += (long)n2;
            }
            return;
        } else {
            this.raf.write(byArray, n, n2);
            this.bytesWrittenForThisPart += (long)n2;
        }
    }
}

