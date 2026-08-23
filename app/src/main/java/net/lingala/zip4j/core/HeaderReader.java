/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.core;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.CentralDirectory;
import net.lingala.zip4j.model.DigitalSignature;
import net.lingala.zip4j.model.EndCentralDirRecord;
import net.lingala.zip4j.model.ExtraDataRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.Zip64EndCentralDirLocator;
import net.lingala.zip4j.model.Zip64EndCentralDirRecord;
import net.lingala.zip4j.model.Zip64ExtendedInfo;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jUtil;

public class HeaderReader {
    private RandomAccessFile zip4jRaf = null;
    private ZipModel zipModel;

    public HeaderReader(RandomAccessFile randomAccessFile) {
        this.zip4jRaf = randomAccessFile;
    }

    private byte[] getLongByteFromIntByte(byte[] byArray) throws ZipException {
        if (byArray != null) {
            if (byArray.length == 4) {
                return new byte[]{byArray[0], byArray[1], byArray[2], byArray[3], 0, 0, 0, 0};
            }
            throw new ZipException("invalid byte length, cannot expand to 8 bytes");
        }
        throw new ZipException("input parameter is null, cannot expand to 8 bytes");
    }

    private AESExtraDataRecord readAESExtraDataRecord(ArrayList object) throws ZipException {
        if (object == null) {
            return null;
        }
        for (int i = 0; i < ((ArrayList)object).size(); ++i) {
            Object object2 = (ExtraDataRecord)((ArrayList)object).get(i);
            if (object2 == null || ((ExtraDataRecord)object2).getHeader() != 39169L) continue;
            if (((ExtraDataRecord)object2).getData() != null) {
                object = new AESExtraDataRecord();
                ((AESExtraDataRecord)object).setSignature(39169L);
                ((AESExtraDataRecord)object).setDataSize(((ExtraDataRecord)object2).getSizeOfData());
                byte[] byArray = ((ExtraDataRecord)object2).getData();
                ((AESExtraDataRecord)object).setVersionNumber(Raw.readShortLittleEndian(byArray, 0));
                object2 = new byte[2];
                System.arraycopy(byArray, 2, object2, 0, 2);
                ((AESExtraDataRecord)object).setVendorID(new String((byte[])object2));
                ((AESExtraDataRecord)object).setAesStrength(byArray[4] & 0xFF);
                ((AESExtraDataRecord)object).setCompressionMethod(Raw.readShortLittleEndian(byArray, 5));
                return object;
            }
            throw new ZipException("corrput AES extra data records");
        }
        return null;
    }

    private void readAndSaveAESExtraDataRecord(FileHeader fileHeader) throws ZipException {
        if (fileHeader != null) {
            if (fileHeader.getExtraDataRecords() != null && fileHeader.getExtraDataRecords().size() > 0) {
                AESExtraDataRecord aESExtraDataRecord = this.readAESExtraDataRecord(fileHeader.getExtraDataRecords());
                if (aESExtraDataRecord != null) {
                    fileHeader.setAesExtraDataRecord(aESExtraDataRecord);
                    fileHeader.setEncryptionMethod(99);
                }
                return;
            }
            return;
        }
        throw new ZipException("file header is null in reading Zip64 Extended Info");
    }

    private void readAndSaveAESExtraDataRecord(LocalFileHeader localFileHeader) throws ZipException {
        if (localFileHeader != null) {
            if (localFileHeader.getExtraDataRecords() != null && localFileHeader.getExtraDataRecords().size() > 0) {
                AESExtraDataRecord aESExtraDataRecord = this.readAESExtraDataRecord(localFileHeader.getExtraDataRecords());
                if (aESExtraDataRecord != null) {
                    localFileHeader.setAesExtraDataRecord(aESExtraDataRecord);
                    localFileHeader.setEncryptionMethod(99);
                }
                return;
            }
            return;
        }
        throw new ZipException("file header is null in reading Zip64 Extended Info");
    }

    private void readAndSaveExtraDataRecord(FileHeader fileHeader) throws ZipException {
        if (this.zip4jRaf != null) {
            if (fileHeader != null) {
                int n = fileHeader.getExtraFieldLength();
                if (n <= 0) {
                    return;
                }
                fileHeader.setExtraDataRecords(this.readExtraDataRecords(n));
                return;
            }
            throw new ZipException("file header is null");
        }
        throw new ZipException("invalid file handler when trying to read extra data record");
    }

    private void readAndSaveExtraDataRecord(LocalFileHeader localFileHeader) throws ZipException {
        if (this.zip4jRaf != null) {
            if (localFileHeader != null) {
                int n = localFileHeader.getExtraFieldLength();
                if (n <= 0) {
                    return;
                }
                localFileHeader.setExtraDataRecords(this.readExtraDataRecords(n));
                return;
            }
            throw new ZipException("file header is null");
        }
        throw new ZipException("invalid file handler when trying to read extra data record");
    }

    private void readAndSaveZip64ExtendedInfo(FileHeader fileHeader) throws ZipException {
        if (fileHeader != null) {
            if (fileHeader.getExtraDataRecords() != null && fileHeader.getExtraDataRecords().size() > 0) {
                Zip64ExtendedInfo zip64ExtendedInfo = this.readZip64ExtendedInfo(fileHeader.getExtraDataRecords(), fileHeader.getUncompressedSize(), fileHeader.getCompressedSize(), fileHeader.getOffsetLocalHeader(), fileHeader.getDiskNumberStart());
                if (zip64ExtendedInfo != null) {
                    fileHeader.setZip64ExtendedInfo(zip64ExtendedInfo);
                    if (zip64ExtendedInfo.getUnCompressedSize() != -1L) {
                        fileHeader.setUncompressedSize(zip64ExtendedInfo.getUnCompressedSize());
                    }
                    if (zip64ExtendedInfo.getCompressedSize() != -1L) {
                        fileHeader.setCompressedSize(zip64ExtendedInfo.getCompressedSize());
                    }
                    if (zip64ExtendedInfo.getOffsetLocalHeader() != -1L) {
                        fileHeader.setOffsetLocalHeader(zip64ExtendedInfo.getOffsetLocalHeader());
                    }
                    if (zip64ExtendedInfo.getDiskNumberStart() != -1) {
                        fileHeader.setDiskNumberStart(zip64ExtendedInfo.getDiskNumberStart());
                    }
                }
                return;
            }
            return;
        }
        throw new ZipException("file header is null in reading Zip64 Extended Info");
    }

    private void readAndSaveZip64ExtendedInfo(LocalFileHeader localFileHeader) throws ZipException {
        if (localFileHeader != null) {
            if (localFileHeader.getExtraDataRecords() != null && localFileHeader.getExtraDataRecords().size() > 0) {
                Zip64ExtendedInfo zip64ExtendedInfo = this.readZip64ExtendedInfo(localFileHeader.getExtraDataRecords(), localFileHeader.getUncompressedSize(), localFileHeader.getCompressedSize(), -1L, -1);
                if (zip64ExtendedInfo != null) {
                    localFileHeader.setZip64ExtendedInfo(zip64ExtendedInfo);
                    if (zip64ExtendedInfo.getUnCompressedSize() != -1L) {
                        localFileHeader.setUncompressedSize(zip64ExtendedInfo.getUnCompressedSize());
                    }
                    if (zip64ExtendedInfo.getCompressedSize() != -1L) {
                        localFileHeader.setCompressedSize(zip64ExtendedInfo.getCompressedSize());
                    }
                }
                return;
            }
            return;
        }
        throw new ZipException("file header is null in reading Zip64 Extended Info");
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private CentralDirectory readCentralDirectory() throws ZipException {
        block14: {
            if (this.zip4jRaf == null) {
                var8_10 = new ZipException("random access file was null", 3);
                throw var8_10;
            }
            if (this.zipModel.getEndCentralDirRecord() == null) throw new ZipException("EndCentralRecord was null, maybe a corrupt zip file");
            try {
                var11_1 = new CentralDirectory();
                var13_2 = new ArrayList<FileHeader>();
                var10_3 = this.zipModel.getEndCentralDirRecord();
                var6_4 = var10_3.getOffsetOfStartOfCentralDir();
                var1_5 = var10_3.getTotNoOfEntriesInCentralDir();
                if (this.zipModel.isZip64Format()) {
                    var6_4 = this.zipModel.getZip64EndCentralDirRecord().getOffsetStartCenDirWRTStartDiskNo();
                    var1_5 = (int)this.zipModel.getZip64EndCentralDirRecord().getTotNoOfEntriesInCentralDir();
                }
                this.zip4jRaf.seek(var6_4);
                var12_6 = new byte[4];
                var9_7 /* !! */  = new byte[2];
                var8_8 = new byte[8];
                var2_11 = 0;
lbl19:
                // 2 sources

                while (var2_11 < var1_5) {
                    var14_15 = new FileHeader();
                    this.readIntoBuff(this.zip4jRaf, var12_6);
                    var3_12 = Raw.readIntLittleEndian(var12_6, 0);
                    if ((long)var3_12 == 33639248L) {
                        var14_15.setSignature(var3_12);
                        this.readIntoBuff(this.zip4jRaf, var9_7 /* !! */ );
                        var14_15.setVersionMadeBy(Raw.readShortLittleEndian(var9_7 /* !! */ , 0));
                        this.readIntoBuff(this.zip4jRaf, var9_7 /* !! */ );
                        var14_15.setVersionNeededToExtract(Raw.readShortLittleEndian(var9_7 /* !! */ , 0));
                        this.readIntoBuff(this.zip4jRaf, var9_7 /* !! */ );
                        var5_14 = (Raw.readShortLittleEndian(var9_7 /* !! */ , 0) & 2048) != 0;
                        var14_15.setFileNameUTF8Encoded(var5_14);
                        break block14;
                    }
                    ** GOTO lbl-1000
                }
                ** GOTO lbl140
            }
            catch (IOException var8_9) {
                throw new ZipException(var8_9);
            }
        }
        var3_12 = var9_7 /* !! */ [0];
        if ((var3_12 & 1) == 0) ** GOTO lbl45
        {
            var14_15.setEncrypted(true);
lbl45:
            // 2 sources

            var14_15.setGeneralPurposeFlag((byte[])var9_7 /* !! */ .clone());
            var5_14 = var3_12 >> 3 == 1;
            var14_15.setDataDescriptorExists(var5_14);
            this.readIntoBuff(this.zip4jRaf, var9_7 /* !! */ );
            var14_15.setCompressionMethod(Raw.readShortLittleEndian(var9_7 /* !! */ , 0));
            this.readIntoBuff(this.zip4jRaf, var12_6);
            var14_15.setLastModFileTime(Raw.readIntLittleEndian(var12_6, 0));
            this.readIntoBuff(this.zip4jRaf, var12_6);
            var14_15.setCrc32(Raw.readIntLittleEndian(var12_6, 0));
            var14_15.setCrcBuff((byte[])var12_6.clone());
            this.readIntoBuff(this.zip4jRaf, var12_6);
            var14_15.setCompressedSize(Raw.readLongLittleEndian(this.getLongByteFromIntByte(var12_6), 0));
            this.readIntoBuff(this.zip4jRaf, var12_6);
            var14_15.setUncompressedSize(Raw.readLongLittleEndian(this.getLongByteFromIntByte(var12_6), 0));
            this.readIntoBuff(this.zip4jRaf, var9_7 /* !! */ );
            var3_12 = Raw.readShortLittleEndian(var9_7 /* !! */ , 0);
            var14_15.setFileNameLength(var3_12);
            this.readIntoBuff(this.zip4jRaf, var9_7 /* !! */ );
            var14_15.setExtraFieldLength(Raw.readShortLittleEndian(var9_7 /* !! */ , 0));
            this.readIntoBuff(this.zip4jRaf, var9_7 /* !! */ );
            var4_13 = Raw.readShortLittleEndian(var9_7 /* !! */ , 0);
            var8_8 = new String(var9_7 /* !! */ );
            var14_15.setFileComment((String)var8_8);
            this.readIntoBuff(this.zip4jRaf, var9_7 /* !! */ );
            var14_15.setDiskNumberStart(Raw.readShortLittleEndian(var9_7 /* !! */ , 0));
            this.readIntoBuff(this.zip4jRaf, var9_7 /* !! */ );
            var14_15.setInternalFileAttr((byte[])var9_7 /* !! */ .clone());
            this.readIntoBuff(this.zip4jRaf, var12_6);
            var14_15.setExternalFileAttr((byte[])var12_6.clone());
            this.readIntoBuff(this.zip4jRaf, var12_6);
            var14_15.setOffsetLocalHeader(Raw.readLongLittleEndian(this.getLongByteFromIntByte(var12_6), 0) & 0xFFFFFFFFL);
            if (var3_12 > 0) {
                var15_16 = new byte[var3_12];
                this.readIntoBuff(this.zip4jRaf, (byte[])var15_16);
                var8_8 = Zip4jUtil.isStringNotNullAndNotEmpty(this.zipModel.getFileNameCharset()) != false ? new String((byte[])var15_16, this.zipModel.getFileNameCharset()) : (Object)Zip4jUtil.decodeFileName((byte[])var15_16, var14_15.isFileNameUTF8Encoded());
                if (var8_8 == null) {
                    var8_8 = new ZipException;
                    var8_8 = new ZipException("fileName is null when reading central directory");
                    throw var8_8;
                }
                var15_16 = new StringBuilder;
                var15_16();
                var15_16.append(":");
                var15_16.append(System.getProperty("file.separator"));
                if (var8_8.indexOf(var15_16.toString()) >= 0) {
                    var15_16 = new StringBuilder();
                    var15_16.append(":");
                    var15_16.append(System.getProperty("file.separator"));
                    var8_8 = var8_8.substring(var8_8.indexOf(var15_16.toString()) + 2);
                }
                var14_15.setFileName((String)var8_8);
                var5_14 = var8_8.endsWith("/") || var8_8.endsWith("\\");
                var14_15.setDirectory(var5_14);
            } else {
                var14_15.setFileName(null);
            }
            this.readAndSaveExtraDataRecord(var14_15);
            this.readAndSaveZip64ExtendedInfo(var14_15);
            this.readAndSaveAESExtraDataRecord(var14_15);
            if (var4_13 > 0) {
                var8_8 = new byte[var4_13];
                this.readIntoBuff(this.zip4jRaf, (byte[])var8_8);
                var15_16 = new String;
                var15_16 = new String((byte[])var8_8);
                var14_15.setFileComment((String)var15_16);
            }
            var13_2.add(var14_15);
            ++var2_11;
            ** GOTO lbl19
        }
lbl-1000:
        // 1 sources

        {
            var9_7 /* !! */  = (byte[])new ZipException;
            var8_8 = new StringBuilder;
            var8_8 = new StringBuilder();
            var8_8.append("Expected central directory entry not found (#");
            var8_8.append(var2_11 + 1);
            var8_8.append(")");
            var9_7 /* !! */ (var8_8.toString());
            throw var9_7 /* !! */ ;
lbl140:
            // 1 sources

            var11_1.setFileHeaders(var13_2);
            var8_8 = new DigitalSignature;
            var8_8 = new DigitalSignature();
            this.readIntoBuff(this.zip4jRaf, var12_6);
            var1_5 = Raw.readIntLittleEndian(var12_6, 0);
            if ((long)var1_5 != 84233040L) {
                return var11_1;
            }
            var8_8.setHeaderSignature(var1_5);
            this.readIntoBuff(this.zip4jRaf, var9_7 /* !! */ );
            var1_5 = Raw.readShortLittleEndian(var9_7 /* !! */ , 0);
            var8_8.setSizeOfData(var1_5);
            if (var1_5 <= 0) return var11_1;
            var10_3 = new byte[var1_5];
            this.readIntoBuff(this.zip4jRaf, (byte[])var10_3);
            var9_7 /* !! */  = (byte[])new String;
            var9_7 /* !! */ ((byte[])var10_3);
            var8_8.setSignatureData((String)var9_7 /* !! */ );
            return var11_1;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private EndCentralDirRecord readEndOfCentralDirectoryRecord() throws ZipException {
        Object object = this.zip4jRaf;
        if (object == null) {
            object = new ZipException("random access file was null", 3);
            throw object;
        }
        try {
            byte[] byArray = new byte[4];
            long l = ((RandomAccessFile)object).length() - 22L;
            object = new EndCentralDirRecord();
            int n = 0;
            while (true) {
                this.zip4jRaf.seek(l);
                if ((long)Raw.readLeInt(this.zip4jRaf, byArray) == 101010256L || ++n > 3000) break;
                --l;
            }
            if ((long)Raw.readIntLittleEndian(byArray, 0) != 101010256L) {
                object = new ZipException("zip headers not found. probably not a zip file");
                throw object;
            }
            byArray = new byte[4];
            byte[] byArray2 = new byte[2];
            ((EndCentralDirRecord)object).setSignature(101010256L);
            this.readIntoBuff(this.zip4jRaf, byArray2);
            ((EndCentralDirRecord)object).setNoOfThisDisk(Raw.readShortLittleEndian(byArray2, 0));
            this.readIntoBuff(this.zip4jRaf, byArray2);
            ((EndCentralDirRecord)object).setNoOfThisDiskStartOfCentralDir(Raw.readShortLittleEndian(byArray2, 0));
            this.readIntoBuff(this.zip4jRaf, byArray2);
            ((EndCentralDirRecord)object).setTotNoOfEntriesInCentralDirOnThisDisk(Raw.readShortLittleEndian(byArray2, 0));
            this.readIntoBuff(this.zip4jRaf, byArray2);
            ((EndCentralDirRecord)object).setTotNoOfEntriesInCentralDir(Raw.readShortLittleEndian(byArray2, 0));
            this.readIntoBuff(this.zip4jRaf, byArray);
            ((EndCentralDirRecord)object).setSizeOfCentralDir(Raw.readIntLittleEndian(byArray, 0));
            this.readIntoBuff(this.zip4jRaf, byArray);
            ((EndCentralDirRecord)object).setOffsetOfStartOfCentralDir(Raw.readLongLittleEndian(this.getLongByteFromIntByte(byArray), 0));
            this.readIntoBuff(this.zip4jRaf, byArray2);
            n = Raw.readShortLittleEndian(byArray2, 0);
            ((EndCentralDirRecord)object).setCommentLength(n);
            if (n > 0) {
                byArray = new byte[n];
                this.readIntoBuff(this.zip4jRaf, byArray);
                String string2 = new String(byArray);
                ((EndCentralDirRecord)object).setComment(string2);
                ((EndCentralDirRecord)object).setCommentBytes(byArray);
            } else {
                ((EndCentralDirRecord)object).setComment(null);
            }
            if (((EndCentralDirRecord)object).getNoOfThisDisk() > 0) {
                this.zipModel.setSplitArchive(true);
                return object;
            }
            this.zipModel.setSplitArchive(false);
            return object;
        }
        catch (IOException iOException) {
            throw new ZipException("Probably not a zip file or a corrupted zip file", iOException, 4);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private ArrayList readExtraDataRecords(int n) throws ZipException {
        if (n <= 0) {
            return null;
        }
        try {
            byte[] byArray = new byte[n];
            this.zip4jRaf.read(byArray);
            int n2 = 0;
            ArrayList<ExtraDataRecord> arrayList = new ArrayList<ExtraDataRecord>();
            while (n2 < n) {
                int n3;
                ExtraDataRecord extraDataRecord = new ExtraDataRecord();
                extraDataRecord.setHeader(Raw.readShortLittleEndian(byArray, n2));
                int n4 = n2 + 2;
                n2 = n3 = Raw.readShortLittleEndian(byArray, n4);
                if (n3 + 2 > n) {
                    n2 = n3 = (int)Raw.readShortBigEndian(byArray, n4);
                    if (n3 + 2 > n) break;
                }
                extraDataRecord.setSizeOfData(n2);
                n3 = n4 + 2;
                if (n2 > 0) {
                    byte[] byArray2 = new byte[n2];
                    System.arraycopy(byArray, n3, byArray2, 0, n2);
                    extraDataRecord.setData(byArray2);
                }
                n2 = n3 + n2;
                arrayList.add(extraDataRecord);
            }
            if ((n = arrayList.size()) <= 0) return null;
            return arrayList;
        }
        catch (IOException iOException) {
            ZipException zipException = new ZipException(iOException);
            throw zipException;
        }
    }

    private byte[] readIntoBuff(RandomAccessFile object, byte[] byArray) throws ZipException {
        block3: {
            try {
                if (((RandomAccessFile)object).read(byArray, 0, byArray.length) == -1) break block3;
                return byArray;
            }
            catch (IOException iOException) {
                throw new ZipException("IOException when reading short buff", iOException);
            }
        }
        object = new ZipException("unexpected end of file when reading short buff");
        throw object;
    }

    private Zip64EndCentralDirLocator readZip64EndCentralDirLocator() throws ZipException {
        if (this.zip4jRaf != null) {
            int n;
            byte[] byArray;
            byte[] byArray2;
            Zip64EndCentralDirLocator zip64EndCentralDirLocator;
            try {
                zip64EndCentralDirLocator = new Zip64EndCentralDirLocator();
                this.setFilePointerToReadZip64EndCentralDirLoc();
                byArray2 = new byte[4];
                byArray = new byte[8];
                this.readIntoBuff(this.zip4jRaf, byArray2);
                n = Raw.readIntLittleEndian(byArray2, 0);
            }
            catch (Exception exception) {
                throw new ZipException(exception);
            }
            if ((long)n == 117853008L) {
                this.zipModel.setZip64Format(true);
                zip64EndCentralDirLocator.setSignature(n);
                this.readIntoBuff(this.zip4jRaf, byArray2);
                zip64EndCentralDirLocator.setNoOfDiskStartOfZip64EndOfCentralDirRec(Raw.readIntLittleEndian(byArray2, 0));
                this.readIntoBuff(this.zip4jRaf, byArray);
                zip64EndCentralDirLocator.setOffsetZip64EndOfCentralDirRec(Raw.readLongLittleEndian(byArray, 0));
                this.readIntoBuff(this.zip4jRaf, byArray2);
                zip64EndCentralDirLocator.setTotNumberOfDiscs(Raw.readIntLittleEndian(byArray2, 0));
                return zip64EndCentralDirLocator;
            }
            this.zipModel.setZip64Format(false);
            return null;
        }
        throw new ZipException("invalid file handler when trying to read Zip64EndCentralDirLocator");
    }

    private Zip64EndCentralDirRecord readZip64EndCentralDirRec() throws ZipException {
        if (this.zipModel.getZip64EndCentralDirLocator() != null) {
            long l = this.zipModel.getZip64EndCentralDirLocator().getOffsetZip64EndOfCentralDirRec();
            if (l >= 0L) {
                int n;
                byte[] byArray;
                byte[] byArray2;
                byte[] byArray3;
                Object object;
                try {
                    this.zip4jRaf.seek(l);
                    object = new Zip64EndCentralDirRecord();
                    byArray3 = new byte[2];
                    byArray2 = new byte[4];
                    byArray = new byte[8];
                    this.readIntoBuff(this.zip4jRaf, byArray2);
                    n = Raw.readIntLittleEndian(byArray2, 0);
                }
                catch (IOException iOException) {
                    throw new ZipException(iOException);
                }
                if ((long)n == 101075792L) {
                    block8: {
                        ((Zip64EndCentralDirRecord)object).setSignature(n);
                        this.readIntoBuff(this.zip4jRaf, byArray);
                        ((Zip64EndCentralDirRecord)object).setSizeOfZip64EndCentralDirRec(Raw.readLongLittleEndian(byArray, 0));
                        this.readIntoBuff(this.zip4jRaf, byArray3);
                        ((Zip64EndCentralDirRecord)object).setVersionMadeBy(Raw.readShortLittleEndian(byArray3, 0));
                        this.readIntoBuff(this.zip4jRaf, byArray3);
                        ((Zip64EndCentralDirRecord)object).setVersionNeededToExtract(Raw.readShortLittleEndian(byArray3, 0));
                        this.readIntoBuff(this.zip4jRaf, byArray2);
                        ((Zip64EndCentralDirRecord)object).setNoOfThisDisk(Raw.readIntLittleEndian(byArray2, 0));
                        this.readIntoBuff(this.zip4jRaf, byArray2);
                        ((Zip64EndCentralDirRecord)object).setNoOfThisDiskStartOfCentralDir(Raw.readIntLittleEndian(byArray2, 0));
                        this.readIntoBuff(this.zip4jRaf, byArray);
                        ((Zip64EndCentralDirRecord)object).setTotNoOfEntriesInCentralDirOnThisDisk(Raw.readLongLittleEndian(byArray, 0));
                        this.readIntoBuff(this.zip4jRaf, byArray);
                        ((Zip64EndCentralDirRecord)object).setTotNoOfEntriesInCentralDir(Raw.readLongLittleEndian(byArray, 0));
                        this.readIntoBuff(this.zip4jRaf, byArray);
                        ((Zip64EndCentralDirRecord)object).setSizeOfCentralDir(Raw.readLongLittleEndian(byArray, 0));
                        this.readIntoBuff(this.zip4jRaf, byArray);
                        ((Zip64EndCentralDirRecord)object).setOffsetStartCenDirWRTStartDiskNo(Raw.readLongLittleEndian(byArray, 0));
                        l = ((Zip64EndCentralDirRecord)object).getSizeOfZip64EndCentralDirRec() - 44L;
                        if (l <= 0L) break block8;
                        byArray2 = new byte[(int)l];
                        this.readIntoBuff(this.zip4jRaf, byArray2);
                        ((Zip64EndCentralDirRecord)object).setExtensibleDataSector(byArray2);
                    }
                    return object;
                }
                object = new ZipException("invalid signature for zip64 end of central directory record");
                throw object;
            }
            throw new ZipException("invalid offset for start of end of central directory record");
        }
        throw new ZipException("invalid zip64 end of central directory locator");
    }

    private Zip64ExtendedInfo readZip64ExtendedInfo(ArrayList object, long l, long l2, long l3, int n) throws ZipException {
        for (int i = 0; i < ((ArrayList)object).size(); ++i) {
            ExtraDataRecord extraDataRecord = (ExtraDataRecord)((ArrayList)object).get(i);
            if (extraDataRecord == null || extraDataRecord.getHeader() != 1L) continue;
            object = new Zip64ExtendedInfo();
            byte[] byArray = extraDataRecord.getData();
            if (extraDataRecord.getSizeOfData() <= 0) break;
            byte[] byArray2 = new byte[8];
            byte[] byArray3 = new byte[4];
            int n2 = 0;
            i = 0;
            int n3 = n2;
            int n4 = i;
            if ((l & 0xFFFFL) == 65535L) {
                n3 = n2;
                n4 = i;
                if (extraDataRecord.getSizeOfData() < 0) {
                    System.arraycopy(byArray, 0, byArray2, 0, 8);
                    ((Zip64ExtendedInfo)object).setUnCompressedSize(Raw.readLongLittleEndian(byArray2, 0));
                    n3 = 0 + 8;
                    n4 = 1;
                }
            }
            n2 = n3;
            i = n4;
            if ((l2 & 0xFFFFL) == 65535L) {
                n2 = n3;
                i = n4;
                if (n3 < extraDataRecord.getSizeOfData()) {
                    System.arraycopy(byArray, n3, byArray2, 0, 8);
                    ((Zip64ExtendedInfo)object).setCompressedSize(Raw.readLongLittleEndian(byArray2, 0));
                    n2 = n3 + 8;
                    i = 1;
                }
            }
            n3 = n2;
            n4 = i;
            if ((l3 & 0xFFFFL) == 65535L) {
                n3 = n2;
                n4 = i;
                if (n2 < extraDataRecord.getSizeOfData()) {
                    System.arraycopy(byArray, n2, byArray2, 0, 8);
                    ((Zip64ExtendedInfo)object).setOffsetLocalHeader(Raw.readLongLittleEndian(byArray2, 0));
                    n3 = n2 + 8;
                    n4 = 1;
                }
            }
            i = n4;
            if ((n & 0xFFFF) == 65535) {
                i = n4;
                if (n3 < extraDataRecord.getSizeOfData()) {
                    System.arraycopy(byArray, n3, byArray3, 0, 4);
                    ((Zip64ExtendedInfo)object).setDiskNumberStart(Raw.readIntLittleEndian(byArray3, 0));
                    i = 1;
                }
            }
            if (i == 0) break;
            return object;
        }
        return null;
    }

    private void setFilePointerToReadZip64EndCentralDirLoc() throws ZipException {
        try {
            Object object = new byte[4];
            long l = this.zip4jRaf.length() - 22L;
            while (true) {
                this.zip4jRaf.seek(l);
                if ((long)Raw.readLeInt(this.zip4jRaf, (byte[])object) == 101010256L) {
                    object = this.zip4jRaf;
                    ((RandomAccessFile)object).seek(((RandomAccessFile)object).getFilePointer() - 4L - 4L - 8L - 4L - 4L);
                    return;
                }
                --l;
            }
        }
        catch (IOException iOException) {
            ZipException zipException = new ZipException(iOException);
            throw zipException;
        }
    }

    public ZipModel readAllHeaders() throws ZipException {
        return this.readAllHeaders(null);
    }

    public ZipModel readAllHeaders(String string2) throws ZipException {
        ZipModel zipModel;
        this.zipModel = zipModel = new ZipModel();
        zipModel.setFileNameCharset(string2);
        this.zipModel.setEndCentralDirRecord(this.readEndOfCentralDirectoryRecord());
        this.zipModel.setZip64EndCentralDirLocator(this.readZip64EndCentralDirLocator());
        if (this.zipModel.isZip64Format()) {
            this.zipModel.setZip64EndCentralDirRecord(this.readZip64EndCentralDirRec());
            if (this.zipModel.getZip64EndCentralDirRecord() != null && this.zipModel.getZip64EndCentralDirRecord().getNoOfThisDisk() > 0) {
                this.zipModel.setSplitArchive(true);
            } else {
                this.zipModel.setSplitArchive(false);
            }
        }
        this.zipModel.setCentralDirectory(this.readCentralDirectory());
        return this.zipModel;
    }

    /*
     * Unable to fully structure code
     */
    public LocalFileHeader readLocalFileHeader(FileHeader var1_1) throws ZipException {
        block19: {
            block20: {
                block21: {
                    block22: {
                        block18: {
                            if (var1_1 == null || this.zip4jRaf == null) break block19;
                            var7_4 = var9_3 = var1_1.getOffsetLocalHeader();
                            if (var1_1.getZip64ExtendedInfo() != null) {
                                var7_4 = var9_3;
                                if (var1_1.getZip64ExtendedInfo().getOffsetLocalHeader() > 0L) {
                                    var7_4 = var1_1.getOffsetLocalHeader();
                                }
                            }
                            if (var7_4 < 0L) break block20;
                            try {
                                this.zip4jRaf.seek(var7_4);
                                var13_5 = new LocalFileHeader();
                                var12_6 = new byte[2];
                                var11_7 = new byte[4];
                                var14_8 = new byte[8];
                                this.readIntoBuff(this.zip4jRaf, (byte[])var11_7);
                                var2_9 = Raw.readIntLittleEndian((byte[])var11_7, 0);
                            }
                            catch (IOException var1_2) {
                                throw new ZipException(var1_2);
                            }
                            if ((long)var2_9 != 67324752L) break block21;
                            var13_5.setSignature(var2_9);
                            this.readIntoBuff(this.zip4jRaf, (byte[])var12_6);
                            var13_5.setVersionNeededToExtract(Raw.readShortLittleEndian((byte[])var12_6, 0));
                            this.readIntoBuff(this.zip4jRaf, (byte[])var12_6);
                            var6_10 = (Raw.readShortLittleEndian((byte[])var12_6, 0) & 2048) != 0;
                            var13_5.setFileNameUTF8Encoded(var6_10);
                            var4_11 = var12_6[0];
                            if ((var4_11 & 1) == 0) ** GOTO lbl34
                            var13_5.setEncrypted(true);
lbl34:
                            // 2 sources

                            var13_5.setGeneralPurposeFlag((byte[])var12_6);
                            var14_8 = Integer.toBinaryString((int)var4_11);
                            if (var14_8.length() < 4) ** GOTO lbl40
                            var6_10 = var14_8.charAt(3) == '1';
                            var13_5.setDataDescriptorExists(var6_10);
lbl40:
                            // 2 sources

                            this.readIntoBuff(this.zip4jRaf, (byte[])var12_6);
                            var13_5.setCompressionMethod(Raw.readShortLittleEndian((byte[])var12_6, 0));
                            this.readIntoBuff(this.zip4jRaf, (byte[])var11_7);
                            var13_5.setLastModFileTime(Raw.readIntLittleEndian((byte[])var11_7, 0));
                            this.readIntoBuff(this.zip4jRaf, (byte[])var11_7);
                            var13_5.setCrc32(Raw.readIntLittleEndian((byte[])var11_7, 0));
                            var13_5.setCrcBuff((byte[])var11_7.clone());
                            this.readIntoBuff(this.zip4jRaf, (byte[])var11_7);
                            var13_5.setCompressedSize(Raw.readLongLittleEndian(this.getLongByteFromIntByte((byte[])var11_7), 0));
                            this.readIntoBuff(this.zip4jRaf, (byte[])var11_7);
                            var13_5.setUncompressedSize(Raw.readLongLittleEndian(this.getLongByteFromIntByte((byte[])var11_7), 0));
                            this.readIntoBuff(this.zip4jRaf, (byte[])var12_6);
                            var5_12 = Raw.readShortLittleEndian((byte[])var12_6, 0);
                            var13_5.setFileNameLength(var5_12);
                            this.readIntoBuff(this.zip4jRaf, (byte[])var12_6);
                            var3_13 = Raw.readShortLittleEndian((byte[])var12_6, 0);
                            var13_5.setExtraFieldLength(var3_13);
                            var2_9 = 0 + 4 + 2 + 2 + 2 + 4 + 4 + 4 + 4 + 2 + 2;
                            if (var5_12 <= 0) ** GOTO lbl95
                            var11_7 = new byte[var5_12];
                            this.readIntoBuff(this.zip4jRaf, (byte[])var11_7);
                            var12_6 = Zip4jUtil.decodeFileName((byte[])var11_7, var13_5.isFileNameUTF8Encoded());
                            if (var12_6 == null) break block18;
                            var14_8 = new StringBuilder();
                            var14_8.append(":");
                            var14_8.append(System.getProperty("file.separator"));
                            var11_7 = var12_6;
                            if (var12_6.indexOf(var14_8.toString()) >= 0) {
                                var11_7 = new StringBuilder;
                                var11_7();
                                var11_7.append(":");
                                var11_7.append(System.getProperty("file.separator"));
                                var11_7 = var12_6.substring(var12_6.indexOf(var11_7.toString()) + 2);
                            }
                            var13_5.setFileName((String)var11_7);
                            var2_9 += var5_12;
                            ** GOTO lbl96
                        }
                        var1_1 = new ZipException("file name is null, cannot assign file name to local file header");
                        throw var1_1;
lbl95:
                        // 1 sources

                        var13_5.setFileName(null);
lbl96:
                        // 2 sources

                        this.readAndSaveExtraDataRecord(var13_5);
                        var13_5.setOffsetStartOfData((long)(var2_9 + var3_13) + var7_4);
                        var13_5.setPassword(var1_1.getPassword());
                        this.readAndSaveZip64ExtendedInfo(var13_5);
                        this.readAndSaveAESExtraDataRecord(var13_5);
                        if (!var13_5.isEncrypted() || var13_5.getEncryptionMethod() == 99) ** GOTO lbl107
                        if ((var4_11 & 64) != 64) ** GOTO lbl106
                        var13_5.setEncryptionMethod(1);
                        break block22;
lbl106:
                        // 1 sources

                        var13_5.setEncryptionMethod(0);
                    }
                    if (var13_5.getCrc32() <= 0L) {
                        var13_5.setCrc32(var1_1.getCrc32());
                        var13_5.setCrcBuff(var1_1.getCrcBuff());
                    }
                    if (var13_5.getCompressedSize() <= 0L) {
                        var13_5.setCompressedSize(var1_1.getCompressedSize());
                    }
                    if (var13_5.getUncompressedSize() <= 0L) {
                        var13_5.setUncompressedSize(var1_1.getUncompressedSize());
                    }
                    return var13_5;
                }
                var11_7 = new ZipException;
                var12_6 = new StringBuilder;
                var12_6();
                var12_6.append("invalid local header signature for file: ");
                var12_6.append(var1_1.getFileName());
                var11_7(var12_6.toString());
                throw var11_7;
            }
            throw new ZipException("invalid local header offset");
        }
        throw new ZipException("invalid read parameters for local header");
    }
}

