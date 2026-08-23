/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.SplitOutputStream;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.Zip64EndCentralDirLocator;
import net.lingala.zip4j.model.Zip64EndCentralDirRecord;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jUtil;

public class HeaderWriter {
    private final int ZIP64_EXTRA_BUF;

    public HeaderWriter() {
        this.ZIP64_EXTRA_BUF = 50;
    }

    private byte[] byteArrayListToByteArray(List object) throws ZipException {
        if (object != null) {
            if (object.size() <= 0) {
                return null;
            }
            byte[] byArray = new byte[object.size()];
            for (int i = 0; i < object.size(); ++i) {
                byArray[i] = Byte.parseByte((String)object.get(i));
            }
            return byArray;
        }
        object = new ZipException("input byte array list is null, cannot conver to byte array");
        throw object;
    }

    private void copyByteArrayToArrayList(byte[] object, List list) throws ZipException {
        if (list != null && object != null) {
            for (int i = 0; i < ((byte[])object).length; ++i) {
                list.add(Byte.toString(object[i]));
            }
            return;
        }
        object = new ZipException("one of the input parameters is null, cannot copy byte array to array list");
        throw object;
    }

    private int countNumberOfFileHeaderEntriesOnDisk(ArrayList serializable, int n) throws ZipException {
        if (serializable != null) {
            int n2 = 0;
            for (int i = 0; i < ((ArrayList)serializable).size(); ++i) {
                int n3 = n2;
                if (((FileHeader)((ArrayList)serializable).get(i)).getDiskNumberStart() == n) {
                    n3 = n2 + 1;
                }
                n2 = n3;
            }
            return n2;
        }
        serializable = new ZipException("file headers are null, cannot calculate number of entries on this disk");
        throw serializable;
    }

    private void processHeaderData(ZipModel zipModel, OutputStream object) throws ZipException {
        int n = 0;
        try {
            if (object instanceof SplitOutputStream) {
                zipModel.getEndCentralDirRecord().setOffsetOfStartOfCentralDir(((SplitOutputStream)object).getFilePointer());
                n = ((SplitOutputStream)object).getCurrSplitFileCounter();
            }
            if (zipModel.isZip64Format()) {
                if (zipModel.getZip64EndCentralDirRecord() == null) {
                    object = new Zip64EndCentralDirRecord();
                    zipModel.setZip64EndCentralDirRecord((Zip64EndCentralDirRecord)object);
                }
                if (zipModel.getZip64EndCentralDirLocator() == null) {
                    object = new Zip64EndCentralDirLocator();
                    zipModel.setZip64EndCentralDirLocator((Zip64EndCentralDirLocator)object);
                }
                zipModel.getZip64EndCentralDirLocator().setNoOfDiskStartOfZip64EndOfCentralDirRec(n);
                zipModel.getZip64EndCentralDirLocator().setTotNumberOfDiscs(n + 1);
            }
            zipModel.getEndCentralDirRecord().setNoOfThisDisk(n);
            zipModel.getEndCentralDirRecord().setNoOfThisDiskStartOfCentralDir(n);
            return;
        }
        catch (IOException iOException) {
            throw new ZipException(iOException);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void updateCompressedSizeInLocalFileHeader(SplitOutputStream object, LocalFileHeader localFileHeader, long l, long l2, byte[] byArray, boolean bl) throws ZipException {
        if (object == null) throw new ZipException("invalid output stream, cannot update compressed size for local file header");
        try {
            long l3;
            if (!localFileHeader.isWriteComprSizeInZip64ExtraRecord()) {
                ((SplitOutputStream)object).seek(l + l2);
                ((SplitOutputStream)object).write(byArray);
                return;
            }
            if (byArray.length != 8) {
                object = new ZipException("attempting to write a non 8-byte compressed size block for a zip64 file");
                throw object;
            }
            l = l3 = l + l2 + 4L + 4L + 2L + 2L + (long)localFileHeader.getFileNameLength() + 2L + 2L + 8L;
            if (l2 == 22L) {
                l = l3 + 8L;
            }
            ((SplitOutputStream)object).seek(l);
            ((SplitOutputStream)object).write(byArray);
            return;
        }
        catch (IOException iOException) {
            throw new ZipException(iOException);
        }
    }

    private int writeCentralDirectory(ZipModel object, OutputStream outputStream, List list) throws ZipException {
        if (object != null && outputStream != null) {
            if (((ZipModel)object).getCentralDirectory() != null && ((ZipModel)object).getCentralDirectory().getFileHeaders() != null && ((ZipModel)object).getCentralDirectory().getFileHeaders().size() > 0) {
                int n = 0;
                for (int i = 0; i < ((ZipModel)object).getCentralDirectory().getFileHeaders().size(); ++i) {
                    n += this.writeFileHeader((ZipModel)object, (FileHeader)((ZipModel)object).getCentralDirectory().getFileHeaders().get(i), outputStream, list);
                }
                return n;
            }
            return 0;
        }
        object = new ZipException("input parameters is null, cannot write central directory");
        throw object;
    }

    /*
     * Unable to fully structure code
     */
    private void writeEndOfCentralDirectoryRecord(ZipModel var1_1, OutputStream var2_5, int var3_6, long var4_7, List var6_8) throws ZipException {
        block9: {
            block11: {
                block10: {
                    if (var1_1 == null || var2_5 == null) break block9;
                    var2_5 = new byte[2];
                    var10_9 = new byte[4];
                    var9_10 = new byte[8];
                    Raw.writeIntLittleEndian(var10_9, 0, (int)var1_1.getEndCentralDirRecord().getSignature());
                    this.copyByteArrayToArrayList(var10_9, var6_8);
                    Raw.writeShortLittleEndian((byte[])var2_5, 0, (short)var1_1.getEndCentralDirRecord().getNoOfThisDisk());
                    this.copyByteArrayToArrayList((byte[])var2_5, var6_8);
                    Raw.writeShortLittleEndian((byte[])var2_5, 0, (short)var1_1.getEndCentralDirRecord().getNoOfThisDiskStartOfCentralDir());
                    this.copyByteArrayToArrayList((byte[])var2_5, var6_8);
                    if (var1_1.getCentralDirectory() == null || var1_1.getCentralDirectory().getFileHeaders() == null) ** GOTO lbl44
                    var8_11 = var1_1.getCentralDirectory().getFileHeaders().size();
                    var7_12 = var1_1.isSplitArchive() != false ? this.countNumberOfFileHeaderEntriesOnDisk(var1_1.getCentralDirectory().getFileHeaders(), var1_1.getEndCentralDirRecord().getNoOfThisDisk()) : var8_11;
                    Raw.writeShortLittleEndian((byte[])var2_5, 0, (short)var7_12);
                    this.copyByteArrayToArrayList((byte[])var2_5, var6_8);
                    Raw.writeShortLittleEndian((byte[])var2_5, 0, (short)var8_11);
                    this.copyByteArrayToArrayList((byte[])var2_5, var6_8);
                    try {
                        Raw.writeIntLittleEndian(var10_9, 0, var3_6);
                        this.copyByteArrayToArrayList(var10_9, var6_8);
                        if (var4_7 <= 0xFFFFFFFFL) ** GOTO lbl30
                    }
                    catch (Exception var1_2) {}
                    Raw.writeLongLittleEndian(var9_10, 0, 0xFFFFFFFFL);
                    System.arraycopy(var9_10, 0, var10_9, 0, 4);
                    this.copyByteArrayToArrayList(var10_9, var6_8);
                    break block10;
lbl30:
                    // 1 sources

                    Raw.writeLongLittleEndian(var9_10, 0, var4_7);
                    System.arraycopy(var9_10, 0, var10_9, 0, 4);
                    this.copyByteArrayToArrayList(var10_9, var6_8);
                }
                var3_6 = 0;
                if (var1_1.getEndCentralDirRecord().getComment() != null) {
                    var3_6 = var1_1.getEndCentralDirRecord().getCommentLength();
                }
                Raw.writeShortLittleEndian((byte[])var2_5, 0, (short)var3_6);
                this.copyByteArrayToArrayList((byte[])var2_5, var6_8);
                if (var3_6 <= 0) ** GOTO lbl43
                this.copyByteArrayToArrayList(var1_1.getEndCentralDirRecord().getCommentBytes(), var6_8);
lbl43:
                // 2 sources

                return;
lbl44:
                // 1 sources

                var1_1 = new ZipException("invalid central directory/file headers, cannot write end of central directory record");
                throw var1_1;
                break block11;
                catch (Exception var1_3) {
                    // empty catch block
                }
            }
            throw new ZipException((Throwable)var1_4);
        }
        throw new ZipException("zip model or output stream is null, cannot write end of central directory record");
    }

    /*
     * Loose catch block
     * WARNING - void declaration
     */
    private int writeFileHeader(ZipModel object, FileHeader fileHeader, OutputStream object2, List list) throws ZipException {
        block34: {
            void var1_4;
            block35: {
                int n;
                int n2;
                block31: {
                    boolean bl;
                    byte[] byArray;
                    block33: {
                        int n3;
                        boolean bl2;
                        block32: {
                            byte[] byArray2;
                            byte[] byArray3;
                            block30: {
                                block29: {
                                    block28: {
                                        block27: {
                                            block26: {
                                                if (fileHeader == null || object2 == null) break block34;
                                                object2 = new byte[2];
                                                byArray3 = new byte[4];
                                                byArray = new byte[8];
                                                byArray2 = new byte[]{0, 0};
                                                bl2 = false;
                                                bl = false;
                                                Raw.writeIntLittleEndian(byArray3, 0, fileHeader.getSignature());
                                                this.copyByteArrayToArrayList(byArray3, list);
                                                Raw.writeShortLittleEndian((byte[])object2, 0, (short)fileHeader.getVersionMadeBy());
                                                this.copyByteArrayToArrayList((byte[])object2, list);
                                                Raw.writeShortLittleEndian((byte[])object2, 0, (short)fileHeader.getVersionNeededToExtract());
                                                this.copyByteArrayToArrayList((byte[])object2, list);
                                                this.copyByteArrayToArrayList(fileHeader.getGeneralPurposeFlag(), list);
                                                Raw.writeShortLittleEndian((byte[])object2, 0, (short)fileHeader.getCompressionMethod());
                                                this.copyByteArrayToArrayList((byte[])object2, list);
                                                Raw.writeIntLittleEndian(byArray3, 0, fileHeader.getLastModFileTime());
                                                this.copyByteArrayToArrayList(byArray3, list);
                                                Raw.writeIntLittleEndian(byArray3, 0, (int)fileHeader.getCrc32());
                                                this.copyByteArrayToArrayList(byArray3, list);
                                                n2 = 0 + 4 + 2 + 2 + 2 + 2 + 4 + 4;
                                                if (fileHeader.getCompressedSize() >= 0xFFFFFFFFL || fileHeader.getUncompressedSize() + 50L >= 0xFFFFFFFFL) break block26;
                                                Raw.writeLongLittleEndian(byArray, 0, fileHeader.getCompressedSize());
                                                System.arraycopy(byArray, 0, byArray3, 0, 4);
                                                this.copyByteArrayToArrayList(byArray3, list);
                                                Raw.writeLongLittleEndian(byArray, 0, fileHeader.getUncompressedSize());
                                                System.arraycopy(byArray, 0, byArray3, 0, 4);
                                                this.copyByteArrayToArrayList(byArray3, list);
                                                n3 = n2 + 4 + 4;
                                                break block27;
                                            }
                                            Raw.writeLongLittleEndian(byArray, 0, 0xFFFFFFFFL);
                                            System.arraycopy(byArray, 0, byArray3, 0, 4);
                                            this.copyByteArrayToArrayList(byArray3, list);
                                            this.copyByteArrayToArrayList(byArray3, list);
                                            n3 = n2 + 4 + 4;
                                            bl2 = true;
                                        }
                                        Raw.writeShortLittleEndian((byte[])object2, 0, (short)fileHeader.getFileNameLength());
                                        this.copyByteArrayToArrayList((byte[])object2, list);
                                        byArray3 = new byte[4];
                                        if (fileHeader.getOffsetLocalHeader() <= 0xFFFFFFFFL) break block28;
                                        Raw.writeLongLittleEndian(byArray, 0, 0xFFFFFFFFL);
                                        System.arraycopy(byArray, 0, byArray3, 0, 4);
                                        bl = true;
                                        break block29;
                                    }
                                    Raw.writeLongLittleEndian(byArray, 0, fileHeader.getOffsetLocalHeader());
                                    System.arraycopy(byArray, 0, byArray3, 0, 4);
                                }
                                n2 = 0;
                                if (bl2 || bl) {
                                    n = n2 = 0 + 4;
                                    if (bl2) {
                                        n = n2 + 16;
                                    }
                                    n2 = n;
                                    if (bl) {
                                        n2 = n + 8;
                                    }
                                }
                                n = n2;
                                if (fileHeader.getAesExtraDataRecord() == null) break block30;
                                n = n2 + 11;
                            }
                            Raw.writeShortLittleEndian((byte[])object2, 0, (short)n);
                            this.copyByteArrayToArrayList((byte[])object2, list);
                            this.copyByteArrayToArrayList(byArray2, list);
                            Raw.writeShortLittleEndian((byte[])object2, 0, (short)fileHeader.getDiskNumberStart());
                            this.copyByteArrayToArrayList((byte[])object2, list);
                            this.copyByteArrayToArrayList(byArray2, list);
                            if (fileHeader.getExternalFileAttr() != null) {
                                this.copyByteArrayToArrayList(fileHeader.getExternalFileAttr(), list);
                            } else {
                                this.copyByteArrayToArrayList(new byte[]{0, 0, 0, 0}, list);
                            }
                            this.copyByteArrayToArrayList(byArray3, list);
                            n = n3 + 2 + 2 + 2 + 2 + 2 + 4 + 4;
                            if (Zip4jUtil.isStringNotNullAndNotEmpty(((ZipModel)object).getFileNameCharset())) {
                                byArray2 = fileHeader.getFileName().getBytes(((ZipModel)object).getFileNameCharset());
                                this.copyByteArrayToArrayList(byArray2, list);
                                n2 = n + byArray2.length;
                            } else {
                                this.copyByteArrayToArrayList(Zip4jUtil.convertCharset(fileHeader.getFileName()), list);
                                n2 = Zip4jUtil.getEncodedStringLength(fileHeader.getFileName());
                                n2 = n + n2;
                            }
                            if (!bl2 && !bl) break block31;
                            try {
                                ((ZipModel)object).setZip64Format(true);
                                Raw.writeShortLittleEndian((byte[])object2, 0, (short)1);
                                this.copyByteArrayToArrayList((byte[])object2, list);
                                n = 0;
                                if (bl2) {
                                    n = 0 + 16;
                                }
                                n3 = n;
                                if (!bl) break block32;
                                n3 = n + 8;
                            }
                            catch (Exception exception) {}
                        }
                        Raw.writeShortLittleEndian((byte[])object2, 0, (short)n3);
                        this.copyByteArrayToArrayList((byte[])object2, list);
                        n = n2 + 2 + 2;
                        if (!bl2) break block33;
                        Raw.writeLongLittleEndian(byArray, 0, fileHeader.getUncompressedSize());
                        this.copyByteArrayToArrayList(byArray, list);
                        Raw.writeLongLittleEndian(byArray, 0, fileHeader.getCompressedSize());
                        this.copyByteArrayToArrayList(byArray, list);
                        n = n + 8 + 8;
                    }
                    n2 = n;
                    if (bl) {
                        Raw.writeLongLittleEndian(byArray, 0, fileHeader.getOffsetLocalHeader());
                        this.copyByteArrayToArrayList(byArray, list);
                        n2 = n + 8;
                    }
                }
                n = n2;
                if (fileHeader.getAesExtraDataRecord() != null) {
                    object = fileHeader.getAesExtraDataRecord();
                    Raw.writeShortLittleEndian((byte[])object2, 0, (short)((AESExtraDataRecord)object).getSignature());
                    this.copyByteArrayToArrayList((byte[])object2, list);
                    Raw.writeShortLittleEndian((byte[])object2, 0, (short)((AESExtraDataRecord)object).getDataSize());
                    this.copyByteArrayToArrayList((byte[])object2, list);
                    Raw.writeShortLittleEndian((byte[])object2, 0, (short)((AESExtraDataRecord)object).getVersionNumber());
                    this.copyByteArrayToArrayList((byte[])object2, list);
                    this.copyByteArrayToArrayList(((AESExtraDataRecord)object).getVendorID().getBytes(), list);
                    this.copyByteArrayToArrayList(new byte[]{(byte)((AESExtraDataRecord)object).getAesStrength()}, list);
                    Raw.writeShortLittleEndian((byte[])object2, 0, (short)((AESExtraDataRecord)object).getCompressionMethod());
                    this.copyByteArrayToArrayList((byte[])object2, list);
                    n = n2 + 11;
                }
                return n;
                break block35;
                catch (Exception exception) {
                    // empty catch block
                }
            }
            throw new ZipException((Throwable)var1_4);
        }
        throw new ZipException("input parameters is null, cannot write local file header");
    }

    private void writeZip64EndOfCentralDirectoryLocator(ZipModel zipModel, OutputStream object, List list) throws ZipException {
        if (zipModel != null && object != null) {
            try {
                byte[] byArray = new byte[4];
                object = new byte[8];
                Raw.writeIntLittleEndian(byArray, 0, 117853008);
                this.copyByteArrayToArrayList(byArray, list);
                Raw.writeIntLittleEndian(byArray, 0, zipModel.getZip64EndCentralDirLocator().getNoOfDiskStartOfZip64EndOfCentralDirRec());
                this.copyByteArrayToArrayList(byArray, list);
                Raw.writeLongLittleEndian((byte[])object, 0, zipModel.getZip64EndCentralDirLocator().getOffsetZip64EndOfCentralDirRec());
                this.copyByteArrayToArrayList((byte[])object, list);
                Raw.writeIntLittleEndian(byArray, 0, zipModel.getZip64EndCentralDirLocator().getTotNumberOfDiscs());
                this.copyByteArrayToArrayList(byArray, list);
                return;
            }
            catch (Exception exception) {
                throw new ZipException(exception);
            }
            catch (ZipException zipException) {
                throw zipException;
            }
        }
        throw new ZipException("zip model or output stream is null, cannot write zip64 end of central directory locator");
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void writeZip64EndOfCentralDirectoryRecord(ZipModel object, OutputStream object2, int n, long l, List list) throws ZipException {
        if (object != null && object2 != null) {
            byte[] byArray;
            byte[] byArray2;
            try {
                byArray2 = new byte[2];
                byArray = new byte[]{0, 0};
            }
            catch (Exception exception) {
                throw new ZipException(exception);
            }
            catch (ZipException zipException) {
                throw zipException;
            }
            {
                byte[] byArray3 = new byte[4];
                object2 = new byte[8];
                Raw.writeIntLittleEndian(byArray3, 0, 101075792);
                this.copyByteArrayToArrayList(byArray3, list);
                Raw.writeLongLittleEndian((byte[])object2, 0, 44L);
                this.copyByteArrayToArrayList((byte[])object2, list);
                if (((ZipModel)object).getCentralDirectory() != null && ((ZipModel)object).getCentralDirectory().getFileHeaders() != null && ((ZipModel)object).getCentralDirectory().getFileHeaders().size() > 0) {
                    Raw.writeShortLittleEndian(byArray2, 0, (short)((FileHeader)((ZipModel)object).getCentralDirectory().getFileHeaders().get(0)).getVersionMadeBy());
                    this.copyByteArrayToArrayList(byArray2, list);
                    Raw.writeShortLittleEndian(byArray2, 0, (short)((FileHeader)((ZipModel)object).getCentralDirectory().getFileHeaders().get(0)).getVersionNeededToExtract());
                    this.copyByteArrayToArrayList(byArray2, list);
                } else {
                    this.copyByteArrayToArrayList(byArray, list);
                    this.copyByteArrayToArrayList(byArray, list);
                }
                Raw.writeIntLittleEndian(byArray3, 0, ((ZipModel)object).getEndCentralDirRecord().getNoOfThisDisk());
                this.copyByteArrayToArrayList(byArray3, list);
                Raw.writeIntLittleEndian(byArray3, 0, ((ZipModel)object).getEndCentralDirRecord().getNoOfThisDiskStartOfCentralDir());
                this.copyByteArrayToArrayList(byArray3, list);
                int n2 = 0;
                if (((ZipModel)object).getCentralDirectory() != null && ((ZipModel)object).getCentralDirectory().getFileHeaders() != null) {
                    int n3 = ((ZipModel)object).getCentralDirectory().getFileHeaders().size();
                    if (((ZipModel)object).isSplitArchive()) {
                        this.countNumberOfFileHeaderEntriesOnDisk(((ZipModel)object).getCentralDirectory().getFileHeaders(), ((ZipModel)object).getEndCentralDirRecord().getNoOfThisDisk());
                    } else {
                        n2 = n3;
                    }
                    Raw.writeLongLittleEndian((byte[])object2, 0, n2);
                    this.copyByteArrayToArrayList((byte[])object2, list);
                    Raw.writeLongLittleEndian((byte[])object2, 0, n3);
                    this.copyByteArrayToArrayList((byte[])object2, list);
                    Raw.writeLongLittleEndian((byte[])object2, 0, n);
                    this.copyByteArrayToArrayList((byte[])object2, list);
                    Raw.writeLongLittleEndian((byte[])object2, 0, l);
                    this.copyByteArrayToArrayList((byte[])object2, list);
                    return;
                }
                object = new ZipException("invalid central directory/file headers, cannot write end of central directory record");
                throw object;
            }
        }
        throw new ZipException("zip model or output stream is null, cannot write zip64 end of central directory record");
    }

    private void writeZipHeaderBytes(ZipModel zipModel, OutputStream outputStream, byte[] byArray) throws ZipException {
        if (byArray != null) {
            try {
                if (outputStream instanceof SplitOutputStream && ((SplitOutputStream)outputStream).checkBuffSizeAndStartNextSplitFile(byArray.length)) {
                    this.finalizeZipFile(zipModel, outputStream);
                    return;
                }
                outputStream.write(byArray);
                return;
            }
            catch (IOException iOException) {
                throw new ZipException(iOException);
            }
        }
        throw new ZipException("invalid buff to write as zip headers");
    }

    public void finalizeZipFile(ZipModel zipModel, OutputStream outputStream) throws ZipException {
        if (zipModel != null && outputStream != null) {
            try {
                this.processHeaderData(zipModel, outputStream);
                long l = zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir();
                ArrayList arrayList = new ArrayList();
                int n = this.writeCentralDirectory(zipModel, outputStream, arrayList);
                if (zipModel.isZip64Format()) {
                    Object object;
                    if (zipModel.getZip64EndCentralDirRecord() == null) {
                        object = new Zip64EndCentralDirRecord();
                        zipModel.setZip64EndCentralDirRecord((Zip64EndCentralDirRecord)object);
                    }
                    if (zipModel.getZip64EndCentralDirLocator() == null) {
                        object = new Zip64EndCentralDirLocator();
                        zipModel.setZip64EndCentralDirLocator((Zip64EndCentralDirLocator)object);
                    }
                    zipModel.getZip64EndCentralDirLocator().setOffsetZip64EndOfCentralDirRec((long)n + l);
                    if (outputStream instanceof SplitOutputStream) {
                        zipModel.getZip64EndCentralDirLocator().setNoOfDiskStartOfZip64EndOfCentralDirRec(((SplitOutputStream)outputStream).getCurrSplitFileCounter());
                        zipModel.getZip64EndCentralDirLocator().setTotNumberOfDiscs(((SplitOutputStream)outputStream).getCurrSplitFileCounter() + 1);
                    } else {
                        zipModel.getZip64EndCentralDirLocator().setNoOfDiskStartOfZip64EndOfCentralDirRec(0);
                        zipModel.getZip64EndCentralDirLocator().setTotNumberOfDiscs(1);
                    }
                    this.writeZip64EndOfCentralDirectoryRecord(zipModel, outputStream, n, l, arrayList);
                    this.writeZip64EndOfCentralDirectoryLocator(zipModel, outputStream, arrayList);
                }
                this.writeEndOfCentralDirectoryRecord(zipModel, outputStream, n, l, arrayList);
                this.writeZipHeaderBytes(zipModel, outputStream, this.byteArrayListToByteArray(arrayList));
                return;
            }
            catch (Exception exception) {
                throw new ZipException(exception);
            }
            catch (ZipException zipException) {
                throw zipException;
            }
        }
        throw new ZipException("input parameters is null, cannot finalize zip file");
    }

    public void finalizeZipFileWithoutValidations(ZipModel zipModel, OutputStream outputStream) throws ZipException {
        if (zipModel != null && outputStream != null) {
            try {
                ArrayList arrayList = new ArrayList();
                long l = zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir();
                int n = this.writeCentralDirectory(zipModel, outputStream, arrayList);
                if (zipModel.isZip64Format()) {
                    Object object;
                    if (zipModel.getZip64EndCentralDirRecord() == null) {
                        object = new Zip64EndCentralDirRecord();
                        zipModel.setZip64EndCentralDirRecord((Zip64EndCentralDirRecord)object);
                    }
                    if (zipModel.getZip64EndCentralDirLocator() == null) {
                        object = new Zip64EndCentralDirLocator();
                        zipModel.setZip64EndCentralDirLocator((Zip64EndCentralDirLocator)object);
                    }
                    zipModel.getZip64EndCentralDirLocator().setOffsetZip64EndOfCentralDirRec((long)n + l);
                    this.writeZip64EndOfCentralDirectoryRecord(zipModel, outputStream, n, l, arrayList);
                    this.writeZip64EndOfCentralDirectoryLocator(zipModel, outputStream, arrayList);
                }
                this.writeEndOfCentralDirectoryRecord(zipModel, outputStream, n, l, arrayList);
                this.writeZipHeaderBytes(zipModel, outputStream, this.byteArrayListToByteArray(arrayList));
                return;
            }
            catch (Exception exception) {
                throw new ZipException(exception);
            }
            catch (ZipException zipException) {
                throw zipException;
            }
        }
        throw new ZipException("input parameters is null, cannot finalize zip file without validations");
    }

    /*
     * Exception decompiling
     */
    public void updateLocalFileHeader(LocalFileHeader var1_1, long var2_6, int var4_7, ZipModel var5_8, byte[] var6_9, int var7_10, SplitOutputStream var8_11) throws ZipException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CASE]], but top level block is 3[TRYBLOCK]
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

    public int writeExtendedLocalHeader(LocalFileHeader object, OutputStream outputStream) throws ZipException, IOException {
        if (object != null && outputStream != null) {
            long l;
            ArrayList arrayList = new ArrayList();
            byte[] byArray = new byte[4];
            Raw.writeIntLittleEndian(byArray, 0, 134695760);
            this.copyByteArrayToArrayList(byArray, arrayList);
            Raw.writeIntLittleEndian(byArray, 0, (int)((LocalFileHeader)object).getCrc32());
            this.copyByteArrayToArrayList(byArray, arrayList);
            long l2 = l = ((LocalFileHeader)object).getCompressedSize();
            if (l >= Integer.MAX_VALUE) {
                l2 = Integer.MAX_VALUE;
            }
            Raw.writeIntLittleEndian(byArray, 0, (int)l2);
            this.copyByteArrayToArrayList(byArray, arrayList);
            l2 = l = ((LocalFileHeader)object).getUncompressedSize();
            if (l >= Integer.MAX_VALUE) {
                l2 = Integer.MAX_VALUE;
            }
            Raw.writeIntLittleEndian(byArray, 0, (int)l2);
            this.copyByteArrayToArrayList(byArray, arrayList);
            object = this.byteArrayListToByteArray(arrayList);
            outputStream.write((byte[])object);
            return ((Object)object).length;
        }
        throw new ZipException("input parameters is null, cannot write extended local header");
    }

    /*
     * Unable to fully structure code
     */
    public int writeLocalFileHeader(ZipModel var1_1, LocalFileHeader var2_10, OutputStream var3_11) throws ZipException {
        block25: {
            block23: {
                block22: {
                    block24: {
                        block21: {
                            block20: {
                                if (var2_10 == null) break block25;
                                var9_12 = new ArrayList<E>();
                                var10_13 = new byte[2];
                                var11_14 = new byte[4];
                                var12_15 = new byte[8];
                                Raw.writeIntLittleEndian(var11_14, 0, var2_10.getSignature());
                                this.copyByteArrayToArrayList(var11_14, var9_12);
                                Raw.writeShortLittleEndian(var10_13, 0, (short)var2_10.getVersionNeededToExtract());
                                this.copyByteArrayToArrayList(var10_13, var9_12);
                                this.copyByteArrayToArrayList(var2_10.getGeneralPurposeFlag(), var9_12);
                                Raw.writeShortLittleEndian(var10_13, 0, (short)var2_10.getCompressionMethod());
                                this.copyByteArrayToArrayList(var10_13, var9_12);
                                Raw.writeIntLittleEndian(var11_14, 0, var2_10.getLastModFileTime());
                                this.copyByteArrayToArrayList(var11_14, var9_12);
                                Raw.writeIntLittleEndian(var11_14, 0, (int)var2_10.getCrc32());
                                this.copyByteArrayToArrayList(var11_14, var9_12);
                                var7_16 = var2_10.getUncompressedSize();
                                if (50L + var7_16 < 0xFFFFFFFFL) break block20;
                                Raw.writeLongLittleEndian(var12_15, 0, 0xFFFFFFFFL);
                                System.arraycopy(var12_15, 0, var11_14, 0, 4);
                                this.copyByteArrayToArrayList(var11_14, var9_12);
                                this.copyByteArrayToArrayList(var11_14, var9_12);
                                var1_1.setZip64Format(true);
                                var5_17 = true;
                                var2_10.setWriteComprSizeInZip64ExtraRecord(true);
                                break block21;
                                catch (Exception var1_2) {
                                    break block22;
                                }
                                catch (ZipException var1_3) {
                                    break block23;
                                }
                            }
                            Raw.writeLongLittleEndian(var12_15, 0, var2_10.getCompressedSize());
                            System.arraycopy(var12_15, 0, var11_14, 0, 4);
                            this.copyByteArrayToArrayList(var11_14, var9_12);
                            Raw.writeLongLittleEndian(var12_15, 0, var2_10.getUncompressedSize());
                            System.arraycopy(var12_15, 0, var11_14, 0, 4);
                            this.copyByteArrayToArrayList(var11_14, var9_12);
                            var2_10.setWriteComprSizeInZip64ExtraRecord(false);
                            var5_17 = false;
                        }
                        Raw.writeShortLittleEndian(var10_13, 0, (short)var2_10.getFileNameLength());
                        this.copyByteArrayToArrayList(var10_13, var9_12);
                        var4_18 = 0;
                        if (var5_17) {
                            var4_18 = 0 + 20;
                        }
                        var6_19 = var4_18;
                        if (var2_10.getAesExtraDataRecord() == null) break block24;
                        var6_19 = var4_18 + 11;
                    }
                    Raw.writeShortLittleEndian(var10_13, 0, (short)var6_19);
                    this.copyByteArrayToArrayList(var10_13, var9_12);
                    if (Zip4jUtil.isStringNotNullAndNotEmpty(var1_1.getFileNameCharset())) {
                        this.copyByteArrayToArrayList(var2_10.getFileName().getBytes(var1_1.getFileNameCharset()), var9_12);
                    } else {
                        this.copyByteArrayToArrayList(Zip4jUtil.convertCharset(var2_10.getFileName()), var9_12);
                    }
                    if (!var5_17) ** GOTO lbl73
                    Raw.writeShortLittleEndian(var10_13, 0, (short)1);
                    this.copyByteArrayToArrayList(var10_13, var9_12);
                    Raw.writeShortLittleEndian(var10_13, 0, (short)16);
                    this.copyByteArrayToArrayList(var10_13, var9_12);
                    Raw.writeLongLittleEndian(var12_15, 0, var2_10.getUncompressedSize());
                    this.copyByteArrayToArrayList(var12_15, var9_12);
                    this.copyByteArrayToArrayList(new byte[]{0, 0, 0, 0, 0, 0, 0, 0}, var9_12);
lbl73:
                    // 2 sources

                    if (var2_10.getAesExtraDataRecord() != null) {
                        var1_1 = var2_10.getAesExtraDataRecord();
                        Raw.writeShortLittleEndian(var10_13, 0, (short)var1_1.getSignature());
                        this.copyByteArrayToArrayList(var10_13, var9_12);
                        Raw.writeShortLittleEndian(var10_13, 0, (short)var1_1.getDataSize());
                        this.copyByteArrayToArrayList(var10_13, var9_12);
                        Raw.writeShortLittleEndian(var10_13, 0, (short)var1_1.getVersionNumber());
                        this.copyByteArrayToArrayList(var10_13, var9_12);
                        this.copyByteArrayToArrayList(var1_1.getVendorID().getBytes(), var9_12);
                        this.copyByteArrayToArrayList(new byte[]{(byte)var1_1.getAesStrength()}, var9_12);
                        Raw.writeShortLittleEndian(var10_13, 0, (short)var1_1.getCompressionMethod());
                        this.copyByteArrayToArrayList(var10_13, var9_12);
                    }
                    var1_1 = this.byteArrayListToByteArray(var9_12);
                    try {
                        var3_11.write((byte[])var1_1);
                        var4_18 = ((Object)var1_1).length;
                        return var4_18;
                    }
                    catch (Exception var1_4) {
                        break block22;
                    }
                    catch (ZipException var1_5) {
                        break block23;
                    }
                    catch (Exception var1_6) {
                        // empty catch block
                    }
                }
                throw new ZipException((Throwable)var1_7);
                catch (ZipException var1_8) {
                    // empty catch block
                }
            }
            throw var1_9;
        }
        throw new ZipException("input parameters are null, cannot write local file header");
    }
}

