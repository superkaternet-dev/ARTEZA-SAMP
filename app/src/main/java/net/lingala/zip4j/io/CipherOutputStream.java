/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.io;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.CRC32;
import net.lingala.zip4j.core.HeaderWriter;
import net.lingala.zip4j.crypto.AESEncrpyter;
import net.lingala.zip4j.crypto.IEncrypter;
import net.lingala.zip4j.crypto.StandardEncrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.BaseOutputStream;
import net.lingala.zip4j.io.SplitOutputStream;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.CentralDirectory;
import net.lingala.zip4j.model.EndCentralDirRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jUtil;

public class CipherOutputStream
extends BaseOutputStream {
    private long bytesWrittenForThisFile;
    protected CRC32 crc;
    private IEncrypter encrypter;
    protected FileHeader fileHeader;
    protected LocalFileHeader localFileHeader;
    protected OutputStream outputStream;
    private byte[] pendingBuffer;
    private int pendingBufferLength;
    private File sourceFile;
    private long totalBytesRead;
    private long totalBytesWritten;
    protected ZipModel zipModel;
    protected ZipParameters zipParameters;

    public CipherOutputStream(OutputStream outputStream, ZipModel zipModel) {
        this.outputStream = outputStream;
        this.initZipModel(zipModel);
        this.crc = new CRC32();
        this.totalBytesWritten = 0L;
        this.bytesWrittenForThisFile = 0L;
        this.pendingBuffer = new byte[16];
        this.pendingBufferLength = 0;
        this.totalBytesRead = 0L;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void createFileHeader() throws ZipException {
        boolean bl;
        Object object = new FileHeader();
        this.fileHeader = object;
        ((FileHeader)object).setSignature(33639248);
        this.fileHeader.setVersionMadeBy(20);
        this.fileHeader.setVersionNeededToExtract(20);
        if (this.zipParameters.isEncryptFiles() && this.zipParameters.getEncryptionMethod() == 99) {
            this.fileHeader.setCompressionMethod(99);
            this.fileHeader.setAesExtraDataRecord(this.generateAESExtraDataRecord(this.zipParameters));
        } else {
            this.fileHeader.setCompressionMethod(this.zipParameters.getCompressionMethod());
        }
        if (this.zipParameters.isEncryptFiles()) {
            this.fileHeader.setEncrypted(true);
            this.fileHeader.setEncryptionMethod(this.zipParameters.getEncryptionMethod());
        }
        if (this.zipParameters.isSourceExternalStream()) {
            this.fileHeader.setLastModFileTime((int)Zip4jUtil.javaToDosTime(System.currentTimeMillis()));
            if (!Zip4jUtil.isStringNotNullAndNotEmpty(this.zipParameters.getFileNameInZip())) throw new ZipException("fileNameInZip is null or empty");
            object = this.zipParameters.getFileNameInZip();
        } else {
            this.fileHeader.setLastModFileTime((int)Zip4jUtil.javaToDosTime(Zip4jUtil.getLastModifiedFileTime(this.sourceFile, this.zipParameters.getTimeZone())));
            this.fileHeader.setUncompressedSize(this.sourceFile.length());
            object = Zip4jUtil.getRelativeFileName(this.sourceFile.getAbsolutePath(), this.zipParameters.getRootFolderInZip(), this.zipParameters.getDefaultFolderPath());
        }
        if (!Zip4jUtil.isStringNotNullAndNotEmpty((String)object)) throw new ZipException("fileName is null or empty. unable to create file header");
        this.fileHeader.setFileName((String)object);
        if (Zip4jUtil.isStringNotNullAndNotEmpty(this.zipModel.getFileNameCharset())) {
            this.fileHeader.setFileNameLength(Zip4jUtil.getEncodedStringLength((String)object, this.zipModel.getFileNameCharset()));
        } else {
            this.fileHeader.setFileNameLength(Zip4jUtil.getEncodedStringLength((String)object));
        }
        Object object2 = this.outputStream;
        if (object2 instanceof SplitOutputStream) {
            this.fileHeader.setDiskNumberStart(((SplitOutputStream)object2).getCurrSplitFileCounter());
        } else {
            this.fileHeader.setDiskNumberStart(0);
        }
        int n = 0;
        if (!this.zipParameters.isSourceExternalStream()) {
            n = this.getFileAttributes(this.sourceFile);
        }
        byte by = (byte)n;
        this.fileHeader.setExternalFileAttr(new byte[]{by, 0, 0, 0});
        if (this.zipParameters.isSourceExternalStream()) {
            object2 = this.fileHeader;
            bl = ((String)object).endsWith("/") || ((String)object).endsWith("\\");
            ((FileHeader)object2).setDirectory(bl);
        } else {
            this.fileHeader.setDirectory(this.sourceFile.isDirectory());
        }
        if (this.fileHeader.isDirectory()) {
            this.fileHeader.setCompressedSize(0L);
            this.fileHeader.setUncompressedSize(0L);
        } else if (!this.zipParameters.isSourceExternalStream()) {
            long l = Zip4jUtil.getFileLengh(this.sourceFile);
            if (this.zipParameters.getCompressionMethod() == 0) {
                if (this.zipParameters.getEncryptionMethod() == 0) {
                    this.fileHeader.setCompressedSize(12L + l);
                } else if (this.zipParameters.getEncryptionMethod() == 99) {
                    switch (this.zipParameters.getAesKeyStrength()) {
                        default: {
                            throw new ZipException("invalid aes key strength, cannot determine key sizes");
                        }
                        case 3: {
                            n = 16;
                            break;
                        }
                        case 1: {
                            n = 8;
                        }
                    }
                    this.fileHeader.setCompressedSize((long)n + l + 10L + 2L);
                } else {
                    this.fileHeader.setCompressedSize(0L);
                }
            } else {
                this.fileHeader.setCompressedSize(0L);
            }
            this.fileHeader.setUncompressedSize(l);
        }
        if (this.zipParameters.isEncryptFiles() && this.zipParameters.getEncryptionMethod() == 0) {
            this.fileHeader.setCrc32(this.zipParameters.getSourceFileCRC());
        }
        object = new byte[2];
        object[0] = Raw.bitArrayToByte(this.generateGeneralPurposeBitArray(this.fileHeader.isEncrypted(), this.zipParameters.getCompressionMethod()));
        bl = Zip4jUtil.isStringNotNullAndNotEmpty(this.zipModel.getFileNameCharset());
        object[1] = bl && this.zipModel.getFileNameCharset().equalsIgnoreCase("UTF8") || !bl && Zip4jUtil.detectCharSet(this.fileHeader.getFileName()).equals("UTF8") ? (Object)8 : (Object)false;
        this.fileHeader.setGeneralPurposeFlag((byte[])object);
    }

    private void createLocalFileHeader() throws ZipException {
        if (this.fileHeader != null) {
            LocalFileHeader localFileHeader;
            this.localFileHeader = localFileHeader = new LocalFileHeader();
            localFileHeader.setSignature(67324752);
            this.localFileHeader.setVersionNeededToExtract(this.fileHeader.getVersionNeededToExtract());
            this.localFileHeader.setCompressionMethod(this.fileHeader.getCompressionMethod());
            this.localFileHeader.setLastModFileTime(this.fileHeader.getLastModFileTime());
            this.localFileHeader.setUncompressedSize(this.fileHeader.getUncompressedSize());
            this.localFileHeader.setFileNameLength(this.fileHeader.getFileNameLength());
            this.localFileHeader.setFileName(this.fileHeader.getFileName());
            this.localFileHeader.setEncrypted(this.fileHeader.isEncrypted());
            this.localFileHeader.setEncryptionMethod(this.fileHeader.getEncryptionMethod());
            this.localFileHeader.setAesExtraDataRecord(this.fileHeader.getAesExtraDataRecord());
            this.localFileHeader.setCrc32(this.fileHeader.getCrc32());
            this.localFileHeader.setCompressedSize(this.fileHeader.getCompressedSize());
            this.localFileHeader.setGeneralPurposeFlag((byte[])this.fileHeader.getGeneralPurposeFlag().clone());
            return;
        }
        throw new ZipException("file header is null, cannot create local file header");
    }

    private void encryptAndWrite(byte[] byArray, int n, int n2) throws IOException {
        IEncrypter iEncrypter = this.encrypter;
        if (iEncrypter != null) {
            try {
                iEncrypter.encryptData(byArray, n, n2);
            }
            catch (ZipException zipException) {
                throw new IOException(zipException.getMessage());
            }
        }
        this.outputStream.write(byArray, n, n2);
        this.totalBytesWritten += (long)n2;
        this.bytesWrittenForThisFile += (long)n2;
    }

    private AESExtraDataRecord generateAESExtraDataRecord(ZipParameters zipParameters) throws ZipException {
        block2: {
            block5: {
                AESExtraDataRecord aESExtraDataRecord;
                block4: {
                    block3: {
                        if (zipParameters == null) break block2;
                        aESExtraDataRecord = new AESExtraDataRecord();
                        aESExtraDataRecord.setSignature(39169L);
                        aESExtraDataRecord.setDataSize(7);
                        aESExtraDataRecord.setVendorID("AE");
                        aESExtraDataRecord.setVersionNumber(2);
                        if (zipParameters.getAesKeyStrength() != 1) break block3;
                        aESExtraDataRecord.setAesStrength(1);
                        break block4;
                    }
                    if (zipParameters.getAesKeyStrength() != 3) break block5;
                    aESExtraDataRecord.setAesStrength(3);
                }
                aESExtraDataRecord.setCompressionMethod(zipParameters.getCompressionMethod());
                return aESExtraDataRecord;
            }
            throw new ZipException("invalid AES key strength, cannot generate AES Extra data record");
        }
        throw new ZipException("zip parameters are null, cannot generate AES Extra Data record");
    }

    private int[] generateGeneralPurposeBitArray(boolean bl, int n) {
        int[] nArray = new int[8];
        nArray[0] = bl ? 1 : 0;
        if (n != 8) {
            nArray[1] = 0;
            nArray[2] = 0;
        }
        nArray[3] = 1;
        return nArray;
    }

    private int getFileAttributes(File file) throws ZipException {
        if (file != null) {
            if (!file.exists()) {
                return 0;
            }
            if (file.isDirectory()) {
                if (file.isHidden()) {
                    return 18;
                }
                return 16;
            }
            if (!file.canWrite() && file.isHidden()) {
                return 3;
            }
            if (!file.canWrite()) {
                return 1;
            }
            if (file.isHidden()) {
                return 2;
            }
            return 0;
        }
        throw new ZipException("input file is null, cannot get file attributes");
    }

    private void initEncrypter() throws ZipException {
        if (!this.zipParameters.isEncryptFiles()) {
            this.encrypter = null;
            return;
        }
        switch (this.zipParameters.getEncryptionMethod()) {
            default: {
                throw new ZipException("invalid encprytion method");
            }
            case 99: {
                this.encrypter = new AESEncrpyter(this.zipParameters.getPassword(), this.zipParameters.getAesKeyStrength());
                break;
            }
            case 0: {
                this.encrypter = new StandardEncrypter(this.zipParameters.getPassword(), (this.localFileHeader.getLastModFileTime() & 0xFFFF) << 16);
            }
        }
    }

    private void initZipModel(ZipModel object) {
        this.zipModel = object == null ? new ZipModel() : object;
        if (this.zipModel.getEndCentralDirRecord() == null) {
            this.zipModel.setEndCentralDirRecord(new EndCentralDirRecord());
        }
        if (this.zipModel.getCentralDirectory() == null) {
            this.zipModel.setCentralDirectory(new CentralDirectory());
        }
        if (this.zipModel.getCentralDirectory().getFileHeaders() == null) {
            this.zipModel.getCentralDirectory().setFileHeaders(new ArrayList());
        }
        if (this.zipModel.getLocalFileHeaderList() == null) {
            this.zipModel.setLocalFileHeaderList(new ArrayList());
        }
        if ((object = this.outputStream) instanceof SplitOutputStream && ((SplitOutputStream)object).isSplitZipFile()) {
            this.zipModel.setSplitArchive(true);
            this.zipModel.setSplitLength(((SplitOutputStream)this.outputStream).getSplitLength());
        }
        this.zipModel.getEndCentralDirRecord().setSignature(101010256L);
    }

    @Override
    public void close() throws IOException {
        OutputStream outputStream = this.outputStream;
        if (outputStream != null) {
            outputStream.close();
        }
    }

    public void closeEntry() throws IOException, ZipException {
        long l;
        long l2;
        Object object;
        int n = this.pendingBufferLength;
        if (n != 0) {
            this.encryptAndWrite(this.pendingBuffer, 0, n);
            this.pendingBufferLength = 0;
        }
        if (this.zipParameters.isEncryptFiles() && this.zipParameters.getEncryptionMethod() == 99) {
            object = this.encrypter;
            if (object instanceof AESEncrpyter) {
                this.outputStream.write(((AESEncrpyter)object).getFinalMac());
                this.bytesWrittenForThisFile += 10L;
                this.totalBytesWritten += 10L;
            } else {
                throw new ZipException("invalid encrypter for AES encrypted file");
            }
        }
        this.fileHeader.setCompressedSize(this.bytesWrittenForThisFile);
        this.localFileHeader.setCompressedSize(this.bytesWrittenForThisFile);
        if (this.zipParameters.isSourceExternalStream()) {
            this.fileHeader.setUncompressedSize(this.totalBytesRead);
            l2 = this.localFileHeader.getUncompressedSize();
            l = this.totalBytesRead;
            if (l2 != l) {
                this.localFileHeader.setUncompressedSize(l);
            }
        }
        l2 = l = this.crc.getValue();
        if (this.fileHeader.isEncrypted()) {
            l2 = l;
            if (this.fileHeader.getEncryptionMethod() == 99) {
                l2 = 0L;
            }
        }
        if (this.zipParameters.isEncryptFiles() && this.zipParameters.getEncryptionMethod() == 99) {
            this.fileHeader.setCrc32(0L);
            this.localFileHeader.setCrc32(0L);
        } else {
            this.fileHeader.setCrc32(l2);
            this.localFileHeader.setCrc32(l2);
        }
        this.zipModel.getLocalFileHeaderList().add(this.localFileHeader);
        this.zipModel.getCentralDirectory().getFileHeaders().add(this.fileHeader);
        object = new HeaderWriter();
        this.totalBytesWritten += (long)((HeaderWriter)object).writeExtendedLocalHeader(this.localFileHeader, this.outputStream);
        this.crc.reset();
        this.bytesWrittenForThisFile = 0L;
        this.encrypter = null;
        this.totalBytesRead = 0L;
    }

    public void decrementCompressedFileSize(int n) {
        if (n <= 0) {
            return;
        }
        long l = n;
        long l2 = this.bytesWrittenForThisFile;
        if (l <= l2) {
            this.bytesWrittenForThisFile = l2 - (long)n;
        }
    }

    public void finish() throws IOException, ZipException {
        this.zipModel.getEndCentralDirRecord().setOffsetOfStartOfCentralDir(this.totalBytesWritten);
        new HeaderWriter().finalizeZipFile(this.zipModel, this.outputStream);
    }

    public File getSourceFile() {
        return this.sourceFile;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void putNextEntry(File object, ZipParameters object2) throws ZipException {
        if (!((ZipParameters)object2).isSourceExternalStream() && object == null) {
            throw new ZipException("input file is null");
        }
        if (!((ZipParameters)object2).isSourceExternalStream() && !Zip4jUtil.checkFileExists((File)object)) {
            throw new ZipException("input file does not exist");
        }
        try {
            this.sourceFile = object;
            this.zipParameters = (ZipParameters)((ZipParameters)object2).clone();
            if (!((ZipParameters)object2).isSourceExternalStream()) {
                if (this.sourceFile.isDirectory()) {
                    this.zipParameters.setEncryptFiles(false);
                    this.zipParameters.setEncryptionMethod(-1);
                    this.zipParameters.setCompressionMethod(0);
                }
            } else {
                if (!Zip4jUtil.isStringNotNullAndNotEmpty(this.zipParameters.getFileNameInZip())) {
                    object = new ZipException("file name is empty for external stream");
                    throw object;
                }
                if (this.zipParameters.getFileNameInZip().endsWith("/") || this.zipParameters.getFileNameInZip().endsWith("\\")) {
                    this.zipParameters.setEncryptFiles(false);
                    this.zipParameters.setEncryptionMethod(-1);
                    this.zipParameters.setCompressionMethod(0);
                }
            }
            this.createFileHeader();
            this.createLocalFileHeader();
            if (this.zipModel.isSplitArchive() && (this.zipModel.getCentralDirectory() == null || this.zipModel.getCentralDirectory().getFileHeaders() == null || this.zipModel.getCentralDirectory().getFileHeaders().size() == 0)) {
                object = new byte[4];
                Raw.writeIntLittleEndian((byte[])object, 0, 134695760);
                this.outputStream.write((byte[])object);
                this.totalBytesWritten += 4L;
            }
            if ((object = this.outputStream) instanceof SplitOutputStream) {
                if (this.totalBytesWritten == 4L) {
                    this.fileHeader.setOffsetLocalHeader(4L);
                } else {
                    this.fileHeader.setOffsetLocalHeader(((SplitOutputStream)object).getFilePointer());
                }
            } else {
                long l = this.totalBytesWritten;
                if (l == 4L) {
                    this.fileHeader.setOffsetLocalHeader(4L);
                } else {
                    this.fileHeader.setOffsetLocalHeader(l);
                }
            }
            object = new HeaderWriter();
            this.totalBytesWritten += (long)((HeaderWriter)object).writeLocalFileHeader(this.zipModel, this.localFileHeader, this.outputStream);
            if (this.zipParameters.isEncryptFiles()) {
                this.initEncrypter();
                if (this.encrypter != null) {
                    if (((ZipParameters)object2).getEncryptionMethod() == 0) {
                        object = ((StandardEncrypter)this.encrypter).getHeaderBytes();
                        this.outputStream.write((byte[])object);
                        this.totalBytesWritten += (long)((Object)object).length;
                        this.bytesWrittenForThisFile += (long)((Object)object).length;
                    } else if (((ZipParameters)object2).getEncryptionMethod() == 99) {
                        object2 = ((AESEncrpyter)this.encrypter).getSaltBytes();
                        object = ((AESEncrpyter)this.encrypter).getDerivedPasswordVerifier();
                        this.outputStream.write((byte[])object2);
                        this.outputStream.write((byte[])object);
                        this.totalBytesWritten += (long)(((Object)object2).length + ((Object)object).length);
                        this.bytesWrittenForThisFile += (long)(((Object)object2).length + ((Object)object).length);
                    }
                }
            }
            this.crc.reset();
            return;
        }
        catch (Exception exception) {
            throw new ZipException(exception);
        }
        catch (ZipException zipException) {
            throw zipException;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new ZipException(cloneNotSupportedException);
        }
    }

    public void setSourceFile(File file) {
        this.sourceFile = file;
    }

    protected void updateTotalBytesRead(int n) {
        if (n > 0) {
            this.totalBytesRead += (long)n;
        }
    }

    @Override
    public void write(int n) throws IOException {
        this.write(new byte[]{(byte)n}, 0, 1);
    }

    @Override
    public void write(byte[] byArray) throws IOException {
        if (byArray != null) {
            if (byArray.length == 0) {
                return;
            }
            this.write(byArray, 0, byArray.length);
            return;
        }
        throw new NullPointerException();
    }

    @Override
    public void write(byte[] byArray, int n, int n2) throws IOException {
        if (n2 == 0) {
            return;
        }
        int n3 = n;
        int n4 = n2;
        if (this.zipParameters.isEncryptFiles()) {
            n3 = n;
            n4 = n2;
            if (this.zipParameters.getEncryptionMethod() == 99) {
                n4 = this.pendingBufferLength;
                int n5 = n;
                int n6 = n2;
                if (n4 != 0) {
                    if (n2 >= 16 - n4) {
                        System.arraycopy(byArray, n, this.pendingBuffer, n4, 16 - n4);
                        byte[] byArray2 = this.pendingBuffer;
                        this.encryptAndWrite(byArray2, 0, byArray2.length);
                        n5 = 16 - this.pendingBufferLength;
                        n6 = n2 - n5;
                        this.pendingBufferLength = 0;
                    } else {
                        System.arraycopy(byArray, n, this.pendingBuffer, n4, n2);
                        this.pendingBufferLength += n2;
                        return;
                    }
                }
                n3 = n5;
                n4 = n6;
                if (n6 != 0) {
                    n3 = n5;
                    n4 = n6;
                    if (n6 % 16 != 0) {
                        System.arraycopy(byArray, n6 + n5 - n6 % 16, this.pendingBuffer, 0, n6 % 16);
                        this.pendingBufferLength = n = n6 % 16;
                        n4 = n6 - n;
                        n3 = n5;
                    }
                }
            }
        }
        if (n4 != 0) {
            this.encryptAndWrite(byArray, n3, n4);
        }
    }
}

