/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.unzip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Arrays;
import java.util.zip.CRC32;
import net.lingala.zip4j.core.HeaderReader;
import net.lingala.zip4j.crypto.AESDecrypter;
import net.lingala.zip4j.crypto.IDecrypter;
import net.lingala.zip4j.crypto.StandardDecrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jUtil;

public class UnzipEngine {
    private CRC32 crc;
    private int currSplitFileCounter = 0;
    private IDecrypter decrypter;
    private FileHeader fileHeader;
    private LocalFileHeader localFileHeader;
    private ZipModel zipModel;

    public UnzipEngine(ZipModel zipModel, FileHeader fileHeader) throws ZipException {
        if (zipModel != null && fileHeader != null) {
            this.zipModel = zipModel;
            this.fileHeader = fileHeader;
            this.crc = new CRC32();
            return;
        }
        throw new ZipException("Invalid parameters passed to StoreUnzip. One or more of the parameters were null");
    }

    private int calculateAESSaltLength(AESExtraDataRecord aESExtraDataRecord) throws ZipException {
        if (aESExtraDataRecord != null) {
            switch (aESExtraDataRecord.getAesStrength()) {
                default: {
                    throw new ZipException("unable to determine salt length: invalid aes key strength");
                }
                case 3: {
                    return 16;
                }
                case 2: {
                    return 12;
                }
                case 1: 
            }
            return 8;
        }
        throw new ZipException("unable to determine salt length: AESExtraDataRecord is null");
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean checkLocalHeader() throws ZipException {
        Object object;
        Object object2;
        Object object3;
        Object object4;
        block15: {
            block16: {
                object4 = null;
                object3 = null;
                try {
                    object = object2 = this.checkSplitFile();
                    if (object2 == null) {
                        object3 = object2;
                        object4 = object2;
                        object3 = object2;
                        object4 = object2;
                        object3 = object2;
                        object4 = object2;
                        File file = new File(this.zipModel.getZipFile());
                        object3 = object2;
                        object4 = object2;
                        object = new RandomAccessFile(file, "r");
                    }
                    object3 = object;
                    object4 = object;
                    object3 = object;
                    object4 = object;
                    object2 = new HeaderReader((RandomAccessFile)object);
                    object3 = object;
                    object4 = object;
                    object2 = ((HeaderReader)object2).readLocalFileHeader(this.fileHeader);
                    object3 = object;
                    object4 = object;
                    this.localFileHeader = object2;
                    if (object2 == null) break block15;
                    object3 = object;
                    object4 = object;
                    int n = ((LocalFileHeader)object2).getCompressionMethod();
                    object3 = object;
                    object4 = object;
                    int n2 = this.fileHeader.getCompressionMethod();
                    if (n == n2) break block16;
                    if (object == null) return false;
                }
                catch (FileNotFoundException fileNotFoundException) {
                    object3 = object4;
                    object3 = object4;
                    object = new ZipException(fileNotFoundException);
                    object3 = object4;
                    throw object;
                }
                try {
                    ((RandomAccessFile)object).close();
                    return false;
                }
                catch (Exception exception) {
                    return false;
                }
                catch (IOException iOException) {
                    return false;
                }
            }
            if (object == null) return true;
            try {
                ((RandomAccessFile)object).close();
                return true;
            }
            catch (Exception exception) {
                return true;
            }
            catch (IOException iOException) {
                return true;
            }
        }
        object3 = object;
        object4 = object;
        {
            object3 = object;
            object4 = object;
            object2 = new ZipException("error reading local file header. Is this a valid zip file?");
            object3 = object;
            object4 = object;
            throw object2;
        }
        catch (Throwable throwable2222222) {}
        if (object3 == null) throw throwable2222222;
        try {
            ((RandomAccessFile)object3).close();
            throw throwable2222222;
        }
        catch (Exception exception) {
            throw throwable2222222;
        }
        catch (IOException iOException) {
            throw throwable2222222;
        }
    }

    private RandomAccessFile checkSplitFile() throws ZipException {
        if (this.zipModel.isSplitArchive()) {
            Object object;
            int n = this.fileHeader.getDiskNumberStart();
            this.currSplitFileCounter = n + 1;
            Object object2 = this.zipModel.getZipFile();
            if (n == this.zipModel.getEndCentralDirRecord().getNoOfThisDisk()) {
                object2 = this.zipModel.getZipFile();
            } else if (n >= 9) {
                object = new StringBuilder();
                ((StringBuilder)object).append(((String)object2).substring(0, ((String)object2).lastIndexOf(".")));
                ((StringBuilder)object).append(".z");
                ((StringBuilder)object).append(n + 1);
                object2 = ((StringBuilder)object).toString();
            } else {
                object = new StringBuilder();
                ((StringBuilder)object).append(((String)object2).substring(0, ((String)object2).lastIndexOf(".")));
                ((StringBuilder)object).append(".z0");
                ((StringBuilder)object).append(n + 1);
                object2 = ((StringBuilder)object).toString();
            }
            try {
                object = new RandomAccessFile((String)object2, "r");
                if (this.currSplitFileCounter == 1) {
                    object2 = new byte[4];
                    ((RandomAccessFile)object).read((byte[])object2);
                    if ((long)Raw.readIntLittleEndian((byte[])object2, 0) != 134695760L) {
                        object2 = new ZipException("invalid first part split file signature");
                        throw object2;
                    }
                }
                return object;
            }
            catch (IOException iOException) {
                throw new ZipException(iOException);
            }
            catch (FileNotFoundException fileNotFoundException) {
                throw new ZipException(fileNotFoundException);
            }
        }
        return null;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void closeStreams(InputStream object, OutputStream outputStream) throws ZipException {
        block11: {
            if (object != null) {
                try {
                    try {
                        ((InputStream)object).close();
                        break block11;
                    }
                    catch (IOException iOException) {
                        if (Zip4jUtil.isStringNotNullAndNotEmpty(iOException.getMessage()) && iOException.getMessage().indexOf(" - Wrong Password?") >= 0) {
                            object = new ZipException(iOException.getMessage());
                            throw object;
                        }
                        if (outputStream == null) return;
                        try {
                            outputStream.close();
                            return;
                        }
                        catch (IOException iOException2) {
                            return;
                        }
                    }
                }
                catch (Throwable throwable) {}
                if (outputStream == null) throw throwable;
                try {
                    outputStream.close();
                    throw throwable;
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                throw throwable;
            }
        }
        if (outputStream == null) return;
        {
            outputStream.close();
            return;
        }
    }

    private RandomAccessFile createFileHandler(String object) throws ZipException {
        Object object2 = this.zipModel;
        if (object2 != null && Zip4jUtil.isStringNotNullAndNotEmpty(((ZipModel)object2).getZipFile())) {
            try {
                if (this.zipModel.isSplitArchive()) {
                    object = this.checkSplitFile();
                } else {
                    object2 = new File(this.zipModel.getZipFile());
                    object = new RandomAccessFile((File)object2, (String)object);
                }
                return object;
            }
            catch (Exception exception) {
                throw new ZipException(exception);
            }
            catch (FileNotFoundException fileNotFoundException) {
                throw new ZipException(fileNotFoundException);
            }
        }
        throw new ZipException("input parameter is null in getFilePointer");
    }

    private byte[] getAESPasswordVerifier(RandomAccessFile randomAccessFile) throws ZipException {
        try {
            byte[] byArray = new byte[2];
            randomAccessFile.read(byArray);
            return byArray;
        }
        catch (IOException iOException) {
            throw new ZipException(iOException);
        }
    }

    private byte[] getAESSalt(RandomAccessFile randomAccessFile) throws ZipException {
        if (this.localFileHeader.getAesExtraDataRecord() == null) {
            return null;
        }
        try {
            byte[] byArray = new byte[this.calculateAESSaltLength(this.localFileHeader.getAesExtraDataRecord())];
            randomAccessFile.seek(this.localFileHeader.getOffsetStartOfData());
            randomAccessFile.read(byArray);
            return byArray;
        }
        catch (IOException iOException) {
            throw new ZipException(iOException);
        }
    }

    private String getOutputFileNameWithPath(String string2, String string3) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(string3)) {
            string3 = this.fileHeader.getFileName();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(System.getProperty("file.separator"));
        stringBuilder.append(string3);
        return stringBuilder.toString();
    }

    private FileOutputStream getOutputStream(String object, String string2) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty((String)object)) {
            try {
                File file = new File(this.getOutputFileNameWithPath((String)object, string2));
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (file.exists()) {
                    file.delete();
                }
                object = new FileOutputStream(file);
                return object;
            }
            catch (FileNotFoundException fileNotFoundException) {
                throw new ZipException(fileNotFoundException);
            }
        }
        throw new ZipException("invalid output path");
    }

    private byte[] getStandardDecrypterHeaderBytes(RandomAccessFile randomAccessFile) throws ZipException {
        try {
            byte[] byArray = new byte[12];
            randomAccessFile.seek(this.localFileHeader.getOffsetStartOfData());
            randomAccessFile.read(byArray, 0, 12);
            return byArray;
        }
        catch (Exception exception) {
            throw new ZipException(exception);
        }
        catch (IOException iOException) {
            throw new ZipException(iOException);
        }
    }

    private void init(RandomAccessFile randomAccessFile) throws ZipException {
        if (this.localFileHeader != null) {
            try {
                this.initDecrypter(randomAccessFile);
                return;
            }
            catch (Exception exception) {
                throw new ZipException(exception);
            }
            catch (ZipException zipException) {
                throw zipException;
            }
        }
        throw new ZipException("local file header is null, cannot initialize input stream");
    }

    private void initDecrypter(RandomAccessFile randomAccessFile) throws ZipException {
        LocalFileHeader localFileHeader = this.localFileHeader;
        if (localFileHeader != null) {
            if (localFileHeader.isEncrypted()) {
                if (this.localFileHeader.getEncryptionMethod() == 0) {
                    this.decrypter = new StandardDecrypter(this.fileHeader, this.getStandardDecrypterHeaderBytes(randomAccessFile));
                } else if (this.localFileHeader.getEncryptionMethod() == 99) {
                    this.decrypter = new AESDecrypter(this.localFileHeader, this.getAESSalt(randomAccessFile), this.getAESPasswordVerifier(randomAccessFile));
                } else {
                    throw new ZipException("unsupported encryption method");
                }
            }
            return;
        }
        throw new ZipException("local file header is null, cannot init decrypter");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void checkCRC() throws ZipException {
        Object object = this.fileHeader;
        if (object == null) return;
        if (((FileHeader)object).getEncryptionMethod() == 99) {
            object = this.decrypter;
            if (object == null || !(object instanceof AESDecrypter)) return;
            byte[] byArray = ((AESDecrypter)object).getCalculatedAuthenticationBytes();
            byte[] byArray2 = ((AESDecrypter)this.decrypter).getStoredMac();
            object = new byte[10];
            if (byArray2 != null) {
                System.arraycopy(byArray, 0, object, 0, 10);
                if (Arrays.equals((byte[])object, byArray2)) return;
                object = new StringBuilder();
                ((StringBuilder)object).append("invalid CRC (MAC) for file: ");
                ((StringBuilder)object).append(this.fileHeader.getFileName());
                throw new ZipException(((StringBuilder)object).toString());
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("CRC (MAC) check failed for ");
            ((StringBuilder)object).append(this.fileHeader.getFileName());
            throw new ZipException(((StringBuilder)object).toString());
        }
        if ((this.crc.getValue() & 0xFFFFFFFFL) == this.fileHeader.getCrc32()) return;
        object = new StringBuilder();
        ((StringBuilder)object).append("invalid CRC for file: ");
        ((StringBuilder)object).append(this.fileHeader.getFileName());
        String string2 = ((StringBuilder)object).toString();
        object = string2;
        if (!this.localFileHeader.isEncrypted()) throw new ZipException((String)object);
        object = string2;
        if (this.localFileHeader.getEncryptionMethod() != 0) throw new ZipException((String)object);
        object = new StringBuilder();
        ((StringBuilder)object).append(string2);
        ((StringBuilder)object).append(" - Wrong Password?");
        object = ((StringBuilder)object).toString();
        throw new ZipException((String)object);
    }

    public IDecrypter getDecrypter() {
        return this.decrypter;
    }

    public FileHeader getFileHeader() {
        return this.fileHeader;
    }

    /*
     * Exception decompiling
     */
    public ZipInputStream getInputStream() throws ZipException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CASE]], but top level block is 4[TRYBLOCK]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public LocalFileHeader getLocalFileHeader() {
        return this.localFileHeader;
    }

    public ZipModel getZipModel() {
        return this.zipModel;
    }

    public RandomAccessFile startNextSplitFile() throws IOException, FileNotFoundException {
        Serializable serializable;
        String string2;
        block7: {
            string2 = this.zipModel.getZipFile();
            if (this.currSplitFileCounter == this.zipModel.getEndCentralDirRecord().getNoOfThisDisk()) {
                string2 = this.zipModel.getZipFile();
            } else if (this.currSplitFileCounter >= 9) {
                serializable = new StringBuilder();
                ((StringBuilder)serializable).append(string2.substring(0, string2.lastIndexOf(".")));
                ((StringBuilder)serializable).append(".z");
                ((StringBuilder)serializable).append(this.currSplitFileCounter + 1);
                string2 = ((StringBuilder)serializable).toString();
            } else {
                serializable = new StringBuilder();
                ((StringBuilder)serializable).append(string2.substring(0, string2.lastIndexOf(".")));
                ((StringBuilder)serializable).append(".z0");
                ((StringBuilder)serializable).append(this.currSplitFileCounter + 1);
                string2 = ((StringBuilder)serializable).toString();
            }
            ++this.currSplitFileCounter;
            try {
                boolean bl = Zip4jUtil.checkFileExists(string2);
                if (!bl) break block7;
                return new RandomAccessFile(string2, "r");
            }
            catch (ZipException zipException) {
                throw new IOException(zipException.getMessage());
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("zip split file does not exist: ");
        stringBuilder.append(string2);
        serializable = new IOException(stringBuilder.toString());
        throw serializable;
    }

    /*
     * Exception decompiling
     */
    public void unzipFile(ProgressMonitor var1_1, String var2_4, String var3_6, UnzipParameters var4_7) throws ZipException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 14[TRYBLOCK] [42 : 513->517)] java.lang.Throwable
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.insertExceptionBlocks(Op02WithProcessedDataAndRefs.java:2283)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:415)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public void updateCRC(int n) {
        this.crc.update(n);
    }

    public void updateCRC(byte[] byArray, int n, int n2) {
        if (byArray != null) {
            this.crc.update(byArray, n, n2);
        }
    }
}

