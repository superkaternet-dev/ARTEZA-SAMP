/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import net.lingala.zip4j.core.HeaderWriter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.SplitOutputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jUtil;

public class ArchiveMaintainer {
    private long calculateTotalWorkForMergeOp(ZipModel zipModel) throws ZipException {
        long l;
        long l2 = l = 0L;
        if (zipModel.isSplitArchive()) {
            int n = zipModel.getEndCentralDirRecord().getNoOfThisDisk();
            String string2 = zipModel.getZipFile();
            int n2 = 0;
            while (true) {
                CharSequence charSequence;
                l2 = l;
                if (n2 > n) break;
                if (zipModel.getEndCentralDirRecord().getNoOfThisDisk() == 0) {
                    charSequence = zipModel.getZipFile();
                } else if (9 >= 0) {
                    charSequence = new StringBuilder();
                    ((StringBuilder)charSequence).append(string2.substring(0, string2.lastIndexOf(".")));
                    ((StringBuilder)charSequence).append(".z");
                    ((StringBuilder)charSequence).append(0 + 1);
                    charSequence = ((StringBuilder)charSequence).toString();
                } else {
                    charSequence = new StringBuilder();
                    ((StringBuilder)charSequence).append(string2.substring(0, string2.lastIndexOf(".")));
                    ((StringBuilder)charSequence).append(".z0");
                    ((StringBuilder)charSequence).append(0 + 1);
                    charSequence = ((StringBuilder)charSequence).toString();
                }
                l += Zip4jUtil.getFileLengh(new File((String)charSequence));
                ++n2;
            }
        }
        return l2;
    }

    private long calculateTotalWorkForRemoveOp(ZipModel zipModel, FileHeader fileHeader) throws ZipException {
        return Zip4jUtil.getFileLengh(new File(zipModel.getZipFile())) - fileHeader.getCompressedSize();
    }

    private void copyFile(RandomAccessFile object, OutputStream outputStream, long l, long l2, ProgressMonitor progressMonitor) throws ZipException {
        if (object != null && outputStream != null) {
            if (l >= 0L) {
                if (l2 >= 0L) {
                    if (l <= l2) {
                        byte[] byArray;
                        long l3;
                        long l4;
                        block15: {
                            if (l == l2) {
                                return;
                            }
                            if (progressMonitor.isCancelAllTasks()) {
                                progressMonitor.setResult(3);
                                progressMonitor.setState(0);
                                return;
                            }
                            ((RandomAccessFile)object).seek(l);
                            l4 = 0L;
                            l3 = l2 - l;
                            if (l2 - l >= 4096L) break block15;
                            byArray = new byte[(int)(l2 - l)];
                            l = l4;
                        }
                        try {
                            byArray = new byte[4096];
                            l = l4;
                        }
                        catch (Exception exception) {
                            throw new ZipException(exception);
                        }
                        catch (IOException iOException) {
                            throw new ZipException(iOException);
                        }
                        while (true) {
                            int n;
                            block16: {
                                n = ((RandomAccessFile)object).read(byArray);
                                if (n == -1) break;
                                outputStream.write(byArray, 0, n);
                                progressMonitor.updateWorkCompleted(n);
                                if (!progressMonitor.isCancelAllTasks()) break block16;
                                progressMonitor.setResult(3);
                                return;
                            }
                            l2 = l + (long)n;
                            if (l2 == l3) break;
                            l = l2;
                            if ((long)byArray.length + l2 <= l3) continue;
                            byArray = new byte[(int)(l3 - l2)];
                            l = l2;
                            continue;
                            break;
                        }
                        return;
                    }
                    throw new ZipException("start offset is greater than end offset, cannot copy file");
                }
                throw new ZipException("end offset is negative, cannot copy file");
            }
            throw new ZipException("starting offset is negative, cannot copy file");
        }
        object = new ZipException("input or output stream is null, cannot copy file");
        throw object;
    }

    private RandomAccessFile createFileHandler(ZipModel object, String string2) throws ZipException {
        if (object != null && Zip4jUtil.isStringNotNullAndNotEmpty(((ZipModel)object).getZipFile())) {
            try {
                File file = new File(((ZipModel)object).getZipFile());
                object = new RandomAccessFile(file, string2);
                return object;
            }
            catch (FileNotFoundException fileNotFoundException) {
                throw new ZipException(fileNotFoundException);
            }
        }
        throw new ZipException("input parameter is null in getFilePointer, cannot create file handler to remove file");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private RandomAccessFile createSplitZipFileHandler(ZipModel object, int n) throws ZipException {
        if (object == null) {
            throw new ZipException("zip model is null, cannot create split file handler");
        }
        if (n < 0) {
            throw new ZipException("invlaid part number, cannot create split file handler");
        }
        try {
            Object object2 = ((ZipModel)object).getZipFile();
            if (n == ((ZipModel)object).getEndCentralDirRecord().getNoOfThisDisk()) {
                object = ((ZipModel)object).getZipFile();
            } else if (n >= 9) {
                object = new StringBuilder();
                ((StringBuilder)object).append(((String)object2).substring(0, ((String)object2).lastIndexOf(".")));
                ((StringBuilder)object).append(".z");
                ((StringBuilder)object).append(n + 1);
                object = ((StringBuilder)object).toString();
            } else {
                object = new StringBuilder();
                ((StringBuilder)object).append(((String)object2).substring(0, ((String)object2).lastIndexOf(".")));
                ((StringBuilder)object).append(".z0");
                ((StringBuilder)object).append(n + 1);
                object = ((StringBuilder)object).toString();
            }
            object2 = new File((String)object);
            if (Zip4jUtil.checkFileExists((File)object2)) {
                return new RandomAccessFile((File)object2, "r");
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("split file does not exist: ");
            stringBuilder.append((String)object);
            object2 = new ZipException(stringBuilder.toString());
            throw object2;
        }
        catch (Exception exception) {
            throw new ZipException(exception);
        }
        catch (FileNotFoundException fileNotFoundException) {
            throw new ZipException(fileNotFoundException);
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void initMergeSplitZipFile(ZipModel object, File object2, ProgressMonitor object3) throws ZipException {
        block61: {
            Object object4;
            Object object5;
            Object object6;
            block63: {
                block62: {
                    block67: {
                        long l;
                        int n;
                        boolean bl;
                        ArrayList<Object> arrayList;
                        block60: {
                            if (object == null) {
                                object = new ZipException("one of the input parameters is null, cannot merge split zip file");
                                ((ProgressMonitor)object3).endProgressMonitorError((Throwable)object);
                                throw object;
                            }
                            if (!((ZipModel)object).isSplitArchive()) {
                                object = new ZipException("archive not a split zip file");
                                ((ProgressMonitor)object3).endProgressMonitorError((Throwable)object);
                                throw object;
                            }
                            Object var17_26 = null;
                            Object var18_27 = null;
                            Object object7 = null;
                            Object object8 = null;
                            object6 = null;
                            Object object9 = null;
                            Object object10 = null;
                            arrayList = new ArrayList<Object>();
                            bl = false;
                            object5 = object7;
                            object4 = object8;
                            try {
                                n = ((ZipModel)object).getEndCentralDirRecord().getNoOfThisDisk();
                                if (n > 0) {
                                    object5 = object7;
                                    object4 = object8;
                                    object2 = this.prepareOutputStreamForMerge((File)object2);
                                    l = 0L;
                                    object4 = object10;
                                    break block60;
                                }
                                object5 = object7;
                                object4 = object8;
                                object5 = object7;
                                object4 = object8;
                                object = new ZipException("corrupt zip model, archive not a split zip file");
                                object5 = object7;
                                object4 = object8;
                                throw object;
                            }
                            catch (Throwable throwable) {
                                object = object5;
                                object3 = object4;
                                break block61;
                            }
                            catch (Exception exception) {
                                object = var17_26;
                                break block62;
                            }
                            catch (IOException iOException) {
                                object6 = object9;
                                object = var18_27;
                                break block63;
                            }
                        }
                        for (int i = 0; i <= n; ++i) {
                            block65: {
                                block66: {
                                    int n2;
                                    block68: {
                                        block64: {
                                            object6 = this.createSplitZipFileHandler((ZipModel)object, i);
                                            object4 = new Long(((RandomAccessFile)object6).length());
                                            if (i != 0) break block64;
                                            try {
                                                if (((ZipModel)object).getCentralDirectory() == null || ((ZipModel)object).getCentralDirectory().getFileHeaders() == null || ((ZipModel)object).getCentralDirectory().getFileHeaders().size() <= 0) break block64;
                                                object5 = new byte[4];
                                                ((RandomAccessFile)object6).seek(0L);
                                                ((RandomAccessFile)object6).read((byte[])object5);
                                                n2 = Raw.readIntLittleEndian((byte[])object5, 0);
                                            }
                                            catch (Throwable throwable) {
                                                object3 = object6;
                                                object = object2;
                                                object2 = throwable;
                                                break block61;
                                            }
                                            catch (Exception exception) {
                                                object4 = object2;
                                                object2 = exception;
                                                object = object4;
                                                break block62;
                                            }
                                            catch (IOException iOException) {
                                                object4 = object2;
                                                object2 = iOException;
                                                object = object4;
                                                break block63;
                                            }
                                            if ((long)n2 != 134695760L) break block64;
                                            n2 = 4;
                                            bl = true;
                                            break block68;
                                        }
                                        n2 = 0;
                                    }
                                    if (i == n) {
                                        try {
                                            object4 = new Long(((ZipModel)object).getEndCentralDirRecord().getOffsetOfStartOfCentralDir());
                                        }
                                        catch (Throwable throwable) {
                                            object3 = object6;
                                            object = object2;
                                            object2 = throwable;
                                            break block61;
                                        }
                                        catch (Exception exception) {
                                            object4 = object2;
                                            object2 = exception;
                                            object = object4;
                                            break block62;
                                        }
                                        catch (IOException iOException) {
                                            object4 = object2;
                                            object2 = iOException;
                                            object = object4;
                                            break block63;
                                        }
                                    }
                                    long l2 = n2;
                                    long l3 = (Long)object4;
                                    this.copyFile((RandomAccessFile)object6, (OutputStream)object2, l2, l3, (ProgressMonitor)object3);
                                    l3 = (Long)object4;
                                    l += l3 - (long)n2;
                                    if (!((ProgressMonitor)object3).isCancelAllTasks()) break block65;
                                    ((ProgressMonitor)object3).setResult(3);
                                    ((ProgressMonitor)object3).setState(0);
                                    if (object2 == null) break block66;
                                    try {
                                        ((OutputStream)object2).close();
                                    }
                                    catch (IOException iOException) {
                                        // empty catch block
                                    }
                                }
                                if (object6 == null) return;
                                try {
                                    ((RandomAccessFile)object6).close();
                                    return;
                                }
                                catch (IOException iOException) {
                                    // empty catch block
                                }
                                return;
                            }
                            try {
                                arrayList.add(object4);
                                try {
                                    ((RandomAccessFile)object6).close();
                                }
                                catch (IOException iOException) {
                                    // empty catch block
                                }
                                object4 = object6;
                                continue;
                            }
                            catch (Throwable throwable) {
                                object = object2;
                                object3 = object6;
                                object2 = throwable;
                                break block61;
                            }
                            catch (Exception exception) {
                                object4 = object2;
                                object2 = exception;
                                object = object4;
                                break block62;
                            }
                            catch (IOException iOException) {
                                object = object2;
                                object2 = iOException;
                                break block63;
                            }
                            catch (Throwable throwable) {
                                object = object2;
                                object3 = object6;
                                object2 = throwable;
                                break block61;
                            }
                            catch (Exception exception) {
                                object4 = object2;
                                object2 = exception;
                                object = object4;
                                break block62;
                            }
                            catch (IOException iOException) {
                                object4 = object2;
                                object2 = iOException;
                                object = object4;
                                break block63;
                            }
                            catch (Throwable throwable) {
                                object = object2;
                                object3 = object6;
                                object2 = throwable;
                                break block61;
                            }
                            catch (Exception exception) {
                                object4 = object2;
                                object2 = exception;
                                object = object4;
                                break block62;
                            }
                            catch (IOException iOException) {
                                object4 = object2;
                                object2 = iOException;
                                object = object4;
                                break block63;
                            }
                            catch (Throwable throwable) {
                                object = object2;
                                object3 = object6;
                                object2 = throwable;
                                break block61;
                            }
                            catch (Exception exception) {
                                object4 = object2;
                                object2 = exception;
                                object = object4;
                                break block62;
                            }
                            catch (IOException iOException) {
                                object4 = object2;
                                object2 = iOException;
                                object = object4;
                                break block63;
                            }
                            catch (Throwable throwable) {
                                object = object2;
                                object3 = object4;
                                object2 = throwable;
                                break block61;
                            }
                            catch (Exception exception) {
                                object6 = object2;
                                object2 = exception;
                                object = object6;
                                object6 = object4;
                                break block62;
                            }
                            catch (IOException iOException) {
                                object6 = object2;
                                object2 = iOException;
                                object = object6;
                                object6 = object4;
                                break block63;
                            }
                        }
                        object = (ZipModel)((ZipModel)object).clone();
                        ((ZipModel)object).getEndCentralDirRecord().setOffsetOfStartOfCentralDir(l);
                        this.updateSplitZipModel((ZipModel)object, arrayList, bl);
                        object6 = new HeaderWriter();
                        ((HeaderWriter)object6).finalizeZipFileWithoutValidations((ZipModel)object, (OutputStream)object2);
                        ((ProgressMonitor)object3).endProgressMonitorSuccess();
                        if (object2 == null) break block67;
                        try {
                            ((OutputStream)object2).close();
                        }
                        catch (IOException iOException) {
                            // empty catch block
                        }
                    }
                    if (object4 == null) return;
                    try {
                        ((RandomAccessFile)object4).close();
                        return;
                    }
                    catch (IOException iOException) {
                        return;
                    }
                    catch (Throwable throwable) {
                        object = object2;
                        object3 = object4;
                        object2 = throwable;
                        break block61;
                    }
                    catch (Exception exception) {
                        object = object2;
                        object2 = exception;
                        object6 = object4;
                    }
                    catch (IOException iOException) {
                        object = object2;
                        object2 = iOException;
                        object6 = object4;
                        break block63;
                    }
                }
                object5 = object;
                object4 = object6;
                ((ProgressMonitor)object3).endProgressMonitorError((Throwable)object2);
                object5 = object;
                object4 = object6;
                object5 = object;
                object4 = object6;
                object3 = new ZipException((Throwable)object2);
                object5 = object;
                object4 = object6;
                throw object3;
            }
            object5 = object;
            object4 = object6;
            ((ProgressMonitor)object3).endProgressMonitorError((Throwable)object2);
            object5 = object;
            object4 = object6;
            object5 = object;
            object4 = object6;
            object3 = new ZipException((Throwable)object2);
            object5 = object;
            object4 = object6;
            throw object3;
        }
        if (object != null) {
            try {
                ((OutputStream)object).close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        if (object3 == null) throw object2;
        try {
            ((RandomAccessFile)object3).close();
            throw object2;
        }
        catch (IOException iOException) {
            // empty catch block
        }
        throw object2;
    }

    private OutputStream prepareOutputStreamForMerge(File object) throws ZipException {
        if (object != null) {
            try {
                object = new FileOutputStream((File)object);
                return object;
            }
            catch (Exception exception) {
                throw new ZipException(exception);
            }
            catch (FileNotFoundException fileNotFoundException) {
                throw new ZipException(fileNotFoundException);
            }
        }
        throw new ZipException("outFile is null, cannot create outputstream");
    }

    private void restoreFileName(File file, String string2) throws ZipException {
        if (file.delete()) {
            if (new File(string2).renameTo(file)) {
                return;
            }
            throw new ZipException("cannot rename modified zip file");
        }
        throw new ZipException("cannot delete old zip file");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void updateSplitEndCentralDirectory(ZipModel object) throws ZipException {
        if (object != null) {
            try {
                if (((ZipModel)object).getCentralDirectory() != null) {
                    ((ZipModel)object).getEndCentralDirRecord().setNoOfThisDisk(0);
                    ((ZipModel)object).getEndCentralDirRecord().setNoOfThisDiskStartOfCentralDir(0);
                    ((ZipModel)object).getEndCentralDirRecord().setTotNoOfEntriesInCentralDir(((ZipModel)object).getCentralDirectory().getFileHeaders().size());
                    ((ZipModel)object).getEndCentralDirRecord().setTotNoOfEntriesInCentralDirOnThisDisk(((ZipModel)object).getCentralDirectory().getFileHeaders().size());
                    return;
                }
                object = new ZipException("corrupt zip model - getCentralDirectory, cannot update split zip model");
                throw object;
            }
            catch (Exception exception) {
                throw new ZipException(exception);
            }
            catch (ZipException zipException) {
                throw zipException;
            }
        }
        object = new ZipException("zip model is null - cannot update end of central directory for split zip model");
        throw object;
    }

    private void updateSplitFileHeader(ZipModel object, ArrayList arrayList, boolean bl) throws ZipException {
        block8: {
            int n;
            int n2;
            block9: {
                if (((ZipModel)object).getCentralDirectory() == null) break block8;
                n2 = ((ZipModel)object).getCentralDirectory().getFileHeaders().size();
                n = 0;
                if (!bl) break block9;
                n = 4;
            }
            for (int i = 0; i < n2; ++i) {
                long l = 0L;
                int n3 = 0;
                while (true) {
                    if (n3 >= ((FileHeader)((ZipModel)object).getCentralDirectory().getFileHeaders().get(i)).getDiskNumberStart()) break;
                    l += ((Long)arrayList.get(n3)).longValue();
                    ++n3;
                    continue;
                    break;
                }
                ((FileHeader)((ZipModel)object).getCentralDirectory().getFileHeaders().get(i)).setOffsetLocalHeader(((FileHeader)((ZipModel)object).getCentralDirectory().getFileHeaders().get(i)).getOffsetLocalHeader() + l - (long)n);
                ((FileHeader)((ZipModel)object).getCentralDirectory().getFileHeaders().get(i)).setDiskNumberStart(0);
                continue;
            }
            return;
        }
        try {
            object = new ZipException("corrupt zip model - getCentralDirectory, cannot update split zip model");
            throw object;
        }
        catch (Exception exception) {
            throw new ZipException(exception);
        }
        catch (ZipException zipException) {
            throw zipException;
        }
    }

    private void updateSplitZip64EndCentralDirLocator(ZipModel object, ArrayList arrayList) throws ZipException {
        if (object != null) {
            if (((ZipModel)object).getZip64EndCentralDirLocator() == null) {
                return;
            }
            ((ZipModel)object).getZip64EndCentralDirLocator().setNoOfDiskStartOfZip64EndOfCentralDirRec(0);
            long l = 0L;
            for (int i = 0; i < arrayList.size(); ++i) {
                l += ((Long)arrayList.get(i)).longValue();
            }
            ((ZipModel)object).getZip64EndCentralDirLocator().setOffsetZip64EndOfCentralDirRec(((ZipModel)object).getZip64EndCentralDirLocator().getOffsetZip64EndOfCentralDirRec() + l);
            ((ZipModel)object).getZip64EndCentralDirLocator().setTotNumberOfDiscs(1);
            return;
        }
        object = new ZipException("zip model is null, cannot update split Zip64 end of central directory locator");
        throw object;
    }

    private void updateSplitZip64EndCentralDirRec(ZipModel object, ArrayList arrayList) throws ZipException {
        if (object != null) {
            if (((ZipModel)object).getZip64EndCentralDirRecord() == null) {
                return;
            }
            ((ZipModel)object).getZip64EndCentralDirRecord().setNoOfThisDisk(0);
            ((ZipModel)object).getZip64EndCentralDirRecord().setNoOfThisDiskStartOfCentralDir(0);
            ((ZipModel)object).getZip64EndCentralDirRecord().setTotNoOfEntriesInCentralDirOnThisDisk(((ZipModel)object).getEndCentralDirRecord().getTotNoOfEntriesInCentralDir());
            long l = 0L;
            for (int i = 0; i < arrayList.size(); ++i) {
                l += ((Long)arrayList.get(i)).longValue();
            }
            ((ZipModel)object).getZip64EndCentralDirRecord().setOffsetStartCenDirWRTStartDiskNo(((ZipModel)object).getZip64EndCentralDirRecord().getOffsetStartCenDirWRTStartDiskNo() + l);
            return;
        }
        object = new ZipException("zip model is null, cannot update split Zip64 end of central directory record");
        throw object;
    }

    private void updateSplitZipModel(ZipModel zipModel, ArrayList arrayList, boolean bl) throws ZipException {
        if (zipModel != null) {
            zipModel.setSplitArchive(false);
            this.updateSplitFileHeader(zipModel, arrayList, bl);
            this.updateSplitEndCentralDirectory(zipModel);
            if (zipModel.isZip64Format()) {
                this.updateSplitZip64EndCentralDirLocator(zipModel, arrayList);
                this.updateSplitZip64EndCentralDirRec(zipModel, arrayList);
            }
            return;
        }
        throw new ZipException("zip model is null, cannot update split zip model");
    }

    public void initProgressMonitorForMergeOp(ZipModel zipModel, ProgressMonitor progressMonitor) throws ZipException {
        if (zipModel != null) {
            progressMonitor.setCurrentOperation(4);
            progressMonitor.setFileName(zipModel.getZipFile());
            progressMonitor.setTotalWork(this.calculateTotalWorkForMergeOp(zipModel));
            progressMonitor.setState(1);
            return;
        }
        throw new ZipException("zip model is null, cannot calculate total work for merge op");
    }

    public void initProgressMonitorForRemoveOp(ZipModel zipModel, FileHeader fileHeader, ProgressMonitor progressMonitor) throws ZipException {
        if (zipModel != null && fileHeader != null && progressMonitor != null) {
            progressMonitor.setCurrentOperation(2);
            progressMonitor.setFileName(fileHeader.getFileName());
            progressMonitor.setTotalWork(this.calculateTotalWorkForRemoveOp(zipModel, fileHeader));
            progressMonitor.setState(1);
            return;
        }
        throw new ZipException("one of the input parameters is null, cannot calculate total work");
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public HashMap initRemoveZipFile(ZipModel var1_1, FileHeader var2_55, ProgressMonitor var3_56) throws ZipException {
        block98: {
            block97: {
                if (var2_55 == null || var1_1 == null) ** GOTO lbl117
                var31_57 = null;
                var32_58 = null;
                var30_59 = null;
                var5_60 = 0;
                var6_61 = 0;
                var10_62 = 0;
                var9_63 = 0;
                var13_64 = 0;
                var16_65 = 0;
                var14_66 = 0;
                var15_67 = 0;
                var12_68 = 0;
                var7_69 = 0;
                var8_70 = 0;
                var11_71 = 0;
                var34_72 = null;
                var33_73 = null;
                var26_74 = null;
                var29_75 = null;
                var28_76 = null;
                var35_77 = null;
                var36_78 = new HashMap<String, Object>();
                var4_79 = Zip4jUtil.getIndexOfFileHeader((ZipModel)var1_1, (FileHeader)var2_55);
                if (var4_79 < 0) ** GOTO lbl75
                if (var1_1.isSplitArchive()) break block97;
                var17_80 = System.currentTimeMillis();
                var37_81 = new StringBuilder();
                var37_81.append(var1_1.getZipFile());
                var28_76 = var35_77;
                var27_82 = var34_72;
                var29_75 = var33_73;
                try {
                    var37_81.append(var17_80 % 1000L);
                    var28_76 = var35_77;
                    var27_82 = var34_72;
                    var29_75 = var33_73;
                    var28_76 = var26_74 = var37_81.toString();
                    var27_82 = var26_74;
                    var29_75 = var26_74;
                    var28_76 = var26_74;
                    var27_82 = var26_74;
                    var29_75 = var26_74;
                    var27_82 = var33_73 = new Comparable<File>((String)var26_74);
                    ** GOTO lbl119
                }
                catch (Throwable var1_44) {
                    var2_55 = null;
                    var26_74 = null;
                    var27_82 = var32_58;
                    var3_56 = var28_76;
                    var4_79 = var8_70;
                    break block98;
                }
                catch (Exception var1_45) {
                    var2_55 = null;
                    var29_75 = null;
                    var28_76 = var30_59;
                    var26_74 = var27_82;
                    var27_82 = var29_75;
                    var4_79 = var12_68;
                    ** GOTO lbl558
                }
                catch (ZipException var1_46) {
                    var2_55 = null;
                    var27_82 = null;
                    var28_76 = var31_57;
                    var26_74 = var29_75;
                    var4_79 = var7_69;
                    ** GOTO lbl583
                }
            }
            try {
                var1_1 = new ZipException("This is a split archive. Zip file format does not allow updating split/spanned files");
                throw var1_1;
lbl75:
                // 1 sources

                var1_1 = new ZipException("file header not found in zip model, cannot remove file");
                throw var1_1;
            }
            catch (Throwable var1_47) {
                var2_55 = null;
                var26_74 = null;
                var27_82 = var32_58;
                var3_56 = var28_76;
                var4_79 = var8_70;
                break block98;
            }
            catch (Exception var1_48) {
                var2_55 = null;
                var27_82 = null;
                var28_76 = var30_59;
                var4_79 = var12_68;
                ** GOTO lbl558
            }
            catch (ZipException var1_49) {
                var2_55 = null;
                var27_82 = null;
                var28_76 = var31_57;
                var26_74 = var29_75;
                var4_79 = var7_69;
                ** GOTO lbl583
            }
            catch (Throwable var1_50) {
                var2_55 = null;
                var26_74 = null;
                var27_82 = var32_58;
                var3_56 = var28_76;
                var4_79 = var8_70;
                break block98;
            }
            catch (Exception var1_51) {
                var2_55 = null;
                var27_82 = null;
                var4_79 = var12_68;
                var28_76 = var30_59;
                ** GOTO lbl558
            }
            {
                block99: {
                    block100: {
                        block112: {
                            block111: {
                                block110: {
                                    block109: {
                                        block114: {
                                            block107: {
                                                block108: {
                                                    block106: {
                                                        block113: {
                                                            block104: {
                                                                block105: {
                                                                    block103: {
                                                                        block102: {
                                                                            block101: {
                                                                                catch (ZipException var1_52) {
                                                                                    var2_55 = null;
                                                                                    var27_82 = null;
                                                                                    var4_79 = var7_69;
                                                                                    var26_74 = var29_75;
                                                                                    var28_76 = var31_57;
                                                                                    break block99;
                                                                                }
lbl117:
                                                                                // 1 sources

                                                                                var1_1 = new ZipException("input parameters is null in maintain zip file, cannot remove file from archive");
                                                                                throw var1_1;
lbl119:
                                                                                // 1 sources

                                                                                while (true) {
                                                                                    var25_83 = var27_82.exists();
                                                                                    if (!var25_83) break;
                                                                                    var28_76 = var26_74;
                                                                                    var27_82 = var26_74;
                                                                                    var29_75 = var26_74;
                                                                                    try {
                                                                                        var17_80 = System.currentTimeMillis();
                                                                                        var28_76 = var26_74;
                                                                                        var27_82 = var26_74;
                                                                                        var29_75 = var26_74;
                                                                                        var28_76 = var26_74;
                                                                                        var27_82 = var26_74;
                                                                                        var29_75 = var26_74;
                                                                                        var33_73 = new Comparable<File>();
                                                                                        var28_76 = var26_74;
                                                                                        var27_82 = var26_74;
                                                                                        var29_75 = var26_74;
                                                                                        var33_73.append(var1_1.getZipFile());
                                                                                        var28_76 = var26_74;
                                                                                        var27_82 = var26_74;
                                                                                        var29_75 = var26_74;
                                                                                        var33_73.append(var17_80 % 1000L);
                                                                                        var28_76 = var26_74;
                                                                                        var27_82 = var26_74;
                                                                                        var29_75 = var26_74;
                                                                                        var28_76 = var26_74 = var33_73.toString();
                                                                                        var27_82 = var26_74;
                                                                                        var29_75 = var26_74;
                                                                                        var27_82 = var33_73 = new File((String)var26_74);
                                                                                    }
                                                                                    catch (Throwable var1_2) {
                                                                                        var3_56 = var28_76;
                                                                                        var2_55 = null;
                                                                                        var26_74 = null;
                                                                                        var27_82 = var32_58;
                                                                                        var4_79 = var8_70;
                                                                                        break block98;
                                                                                    }
                                                                                    catch (Exception var1_3) {
                                                                                        var26_74 = var27_82;
                                                                                        var2_55 = null;
                                                                                        var27_82 = null;
                                                                                        var28_76 = var30_59;
                                                                                        var4_79 = var12_68;
                                                                                        break block100;
                                                                                    }
                                                                                    catch (ZipException var1_4) {
                                                                                        var26_74 = var29_75;
                                                                                        var2_55 = null;
                                                                                        var27_82 = null;
                                                                                        var28_76 = var31_57;
                                                                                        var4_79 = var7_69;
                                                                                        break block99;
                                                                                    }
                                                                                }
                                                                                var28_76 = new Comparable<File>((String)var26_74);
                                                                                var27_82 = new Comparable<File>((File)var28_76);
                                                                                var29_75 = new Comparable<File>(var1_1.getZipFile());
                                                                                var28_76 = this.createFileHandler((ZipModel)var1_1, "r");
                                                                                var30_59 = new Object((RandomAccessFile)var28_76);
                                                                                if (var30_59.readLocalFileHeader((FileHeader)var2_55) == null) ** GOTO lbl351
                                                                                var19_84 = var2_55.getOffsetLocalHeader();
                                                                                var30_59 = var2_55.getZip64ExtendedInfo();
                                                                                if (var30_59 == null) break block101;
                                                                                if (var2_55.getZip64ExtendedInfo().getOffsetLocalHeader() == -1L) break block101;
                                                                                var19_84 = var2_55.getZip64ExtendedInfo().getOffsetLocalHeader();
                                                                            }
                                                                            var17_80 = var1_1.getEndCentralDirRecord().getOffsetOfStartOfCentralDir();
                                                                            var25_83 = var1_1.isZip64Format();
                                                                            var21_85 = var17_80;
                                                                            if (!var25_83) break block102;
                                                                            var21_85 = var17_80;
                                                                            if (var1_1.getZip64EndCentralDirRecord() == null) break block102;
                                                                            var21_85 = var1_1.getZip64EndCentralDirRecord().getOffsetStartCenDirWRTStartDiskNo();
                                                                        }
                                                                        var2_55 = var1_1.getCentralDirectory().getFileHeaders();
                                                                        if (var4_79 != var2_55.size() - 1) break block103;
                                                                        var17_80 = var21_85 - 1L;
                                                                        break block104;
                                                                    }
                                                                    var30_59 = (FileHeader)var2_55.get(var4_79 + 1);
                                                                    if (var30_59 == null) break block105;
                                                                    try {
                                                                        var17_80 = var23_86 = var30_59.getOffsetLocalHeader() - 1L;
                                                                        if (var30_59.getZip64ExtendedInfo() == null) break block104;
                                                                        var17_80 = var23_86;
                                                                        if (var30_59.getZip64ExtendedInfo().getOffsetLocalHeader() != -1L) {
                                                                            var17_80 = var30_59.getZip64ExtendedInfo().getOffsetLocalHeader();
                                                                            --var17_80;
                                                                        }
                                                                        break block104;
                                                                    }
                                                                    catch (Throwable var1_5) {
                                                                        var3_56 = var26_74;
                                                                        var2_55 = var27_82;
                                                                        var27_82 = var28_76;
                                                                        var26_74 = var29_75;
                                                                        var4_79 = var8_70;
                                                                        break block98;
                                                                    }
                                                                    catch (Exception var1_6) {
                                                                        var2_55 = var27_82;
                                                                        var27_82 = var29_75;
                                                                        var4_79 = var12_68;
                                                                        break block100;
                                                                    }
                                                                    catch (ZipException var1_7) {
                                                                        var2_55 = var27_82;
                                                                        var27_82 = var29_75;
                                                                        var4_79 = var7_69;
                                                                        break block99;
                                                                    }
                                                                }
                                                                var17_80 = -1L;
                                                            }
                                                            if (var19_84 < 0L || var17_80 < 0L) ** GOTO lbl338
                                                            if (var4_79 != 0) break block113;
                                                            var14_66 = var1_1.getCentralDirectory().getFileHeaders().size();
                                                            if (var14_66 <= 1) ** GOTO lbl275
                                                            this.copyFile((RandomAccessFile)var28_76, (OutputStream)var27_82, var17_80 + 1L, var21_85, (ProgressMonitor)var3_56);
                                                            ** GOTO lbl275
                                                            catch (Throwable var1_8) {
                                                                var3_56 = var26_74;
                                                                var2_55 = var27_82;
                                                                var27_82 = var28_76;
                                                                var26_74 = var29_75;
                                                                var4_79 = var8_70;
                                                                break block98;
                                                            }
                                                            catch (Exception var1_9) {
                                                                var2_55 = var27_82;
                                                                var27_82 = var29_75;
                                                                var4_79 = var12_68;
                                                                break block100;
                                                            }
                                                            catch (ZipException var1_10) {
                                                                var2_55 = var27_82;
                                                                var27_82 = var29_75;
                                                                var4_79 = var7_69;
                                                                break block99;
                                                            }
                                                        }
                                                        var14_66 = var2_55.size();
                                                        if (var4_79 != var14_66 - 1) break block106;
                                                        this.copyFile((RandomAccessFile)var28_76, (OutputStream)var27_82, 0L, var19_84, (ProgressMonitor)var3_56);
                                                        ** GOTO lbl275
                                                    }
                                                    this.copyFile((RandomAccessFile)var28_76, (OutputStream)var27_82, 0L, var19_84, (ProgressMonitor)var3_56);
                                                    this.copyFile((RandomAccessFile)var28_76, (OutputStream)var27_82, var17_80 + 1L, var21_85, (ProgressMonitor)var3_56);
lbl275:
                                                    // 4 sources

                                                    var30_59 = var29_75;
                                                    var25_83 = var3_56.isCancelAllTasks();
                                                    if (!var25_83) break block107;
                                                    try {
                                                        var3_56.setResult(3);
                                                        var3_56.setState(0);
                                                        if (var28_76 == null) break block108;
                                                    }
                                                    catch (Throwable var1_11) {
                                                        var3_56 = var26_74;
                                                        var2_55 = var27_82;
                                                        var27_82 = var28_76;
                                                        var26_74 = var29_75;
                                                        var4_79 = var8_70;
                                                        break block98;
                                                    }
                                                    catch (Exception var1_12) {
                                                        var2_55 = var27_82;
                                                        var27_82 = var29_75;
                                                        var4_79 = var12_68;
                                                        break block100;
                                                    }
                                                    catch (ZipException var1_13) {
                                                        var2_55 = var27_82;
                                                        var27_82 = var29_75;
                                                        var4_79 = var7_69;
                                                        break block99;
                                                    }
                                                    try {
                                                        var28_76.close();
                                                    }
                                                    catch (IOException var1_14) {
                                                        throw new ZipException("cannot close input stream or output stream when trying to delete a file from zip file");
                                                    }
                                                }
                                                var27_82.close();
                                                new File((String)var26_74).delete();
                                                return null;
                                            }
                                            var31_57 = var1_1.getEndCentralDirRecord();
                                            var2_55 = var27_82;
                                            var31_57.setOffsetOfStartOfCentralDir(((SplitOutputStream)var2_55).getFilePointer());
                                            var1_1.getEndCentralDirRecord().setTotNoOfEntriesInCentralDir(var1_1.getEndCentralDirRecord().getTotNoOfEntriesInCentralDir() - 1);
                                            var1_1.getEndCentralDirRecord().setTotNoOfEntriesInCentralDirOnThisDisk(var1_1.getEndCentralDirRecord().getTotNoOfEntriesInCentralDirOnThisDisk() - 1);
                                            var1_1.getCentralDirectory().getFileHeaders().remove(var4_79);
                                            break block114;
                                            catch (Throwable var1_25) {
                                                var2_55 = var27_82;
                                                var3_56 = var26_74;
                                                var27_82 = var28_76;
                                                var26_74 = var29_75;
                                                var4_79 = var8_70;
                                                break block98;
                                            }
                                            catch (Exception var1_26) {
                                                var2_55 = var27_82;
                                                var27_82 = var29_75;
                                                var4_79 = var12_68;
                                                break block100;
                                            }
                                            catch (ZipException var1_27) {
                                                var2_55 = var27_82;
                                                var27_82 = var29_75;
                                                var4_79 = var7_69;
                                                break block99;
                                            }
lbl338:
                                            // 1 sources

                                            var1_1 = var27_82;
                                            var4_79 = var16_65;
                                            var5_60 = var14_66;
                                            var6_61 = var15_67;
                                            var4_79 = var16_65;
                                            var5_60 = var14_66;
                                            var6_61 = var15_67;
                                            var1_1 = new ZipException("invalid offset for start and end of local file, cannot remove file");
                                            var4_79 = var16_65;
                                            var5_60 = var14_66;
                                            var6_61 = var15_67;
                                            throw var1_1;
lbl351:
                                            // 1 sources

                                            var1_1 = var27_82;
                                            var4_79 = var16_65;
                                            var5_60 = var14_66;
                                            var6_61 = var15_67;
                                            var4_79 = var16_65;
                                            var5_60 = var14_66;
                                            var6_61 = var15_67;
                                            var1_1 = new ZipException("invalid local file header, cannot remove file from archive");
                                            var4_79 = var16_65;
                                            var5_60 = var14_66;
                                            var6_61 = var15_67;
                                            throw var1_1;
                                            catch (Throwable var1_28) {
                                                var2_55 = var27_82;
                                                var3_56 = var26_74;
                                                var27_82 = var28_76;
                                                var26_74 = var29_75;
                                                var4_79 = var8_70;
                                                break block98;
                                            }
                                            catch (Exception var1_29) {
                                                var2_55 = var27_82;
                                                var27_82 = var29_75;
                                                var4_79 = var12_68;
                                                break block100;
                                            }
                                            catch (ZipException var1_30) {
                                                var2_55 = var27_82;
                                                var27_82 = var29_75;
                                                var4_79 = var7_69;
                                                break block99;
                                            }
                                            catch (Throwable var1_31) {
                                                var2_55 = var27_82;
                                                var3_56 = var26_74;
                                                var27_82 = var32_58;
                                                var26_74 = var29_75;
                                                var4_79 = var8_70;
                                                break block98;
                                            }
                                            catch (Exception var1_32) {
                                                var2_55 = var27_82;
                                                var28_76 = var30_59;
                                                var27_82 = var29_75;
                                                var4_79 = var12_68;
                                                break block100;
                                            }
                                            catch (ZipException var1_33) {
                                                var2_55 = var27_82;
                                                var28_76 = var31_57;
                                                var27_82 = var29_75;
                                                var4_79 = var7_69;
                                                break block99;
                                            }
                                            catch (Throwable var1_34) {
                                                var2_55 = var27_82;
                                                var3_56 = var26_74;
                                                var26_74 = null;
                                                var27_82 = var32_58;
                                                var4_79 = var8_70;
                                                break block98;
                                            }
                                            catch (Exception var1_35) {
                                                var2_55 = var27_82;
                                                var27_82 = null;
                                                var28_76 = var30_59;
                                                var4_79 = var12_68;
                                                break block100;
                                            }
                                            catch (ZipException var1_36) {
                                                var2_55 = var27_82;
                                                var27_82 = null;
                                                var28_76 = var31_57;
                                                var4_79 = var7_69;
                                                break block99;
                                            }
                                            catch (FileNotFoundException var1_37) {
                                                try {
                                                    var2_55 = new Object((Throwable)var1_37);
                                                    throw var2_55;
                                                }
                                                catch (Throwable var1_38) {
                                                    var2_55 = null;
                                                    var3_56 = var26_74;
                                                    var26_74 = null;
                                                    var27_82 = var32_58;
                                                    var4_79 = var8_70;
                                                    break block98;
                                                }
                                                catch (Exception var1_39) {
                                                    var2_55 = null;
                                                    var27_82 = null;
                                                    var28_76 = var30_59;
                                                    var4_79 = var12_68;
                                                    break block100;
                                                }
                                                catch (ZipException var1_40) {
                                                    var2_55 = null;
                                                    var27_82 = null;
                                                    var28_76 = var31_57;
                                                    var4_79 = var7_69;
                                                    break block99;
                                                }
                                            }
                                            catch (Throwable var1_41) {
                                                var2_55 = null;
                                                var3_56 = var26_74;
                                                var26_74 = null;
                                                var27_82 = var32_58;
                                                var4_79 = var8_70;
                                                break block98;
                                            }
                                            catch (Exception var1_42) {
                                                var2_55 = null;
                                                var27_82 = null;
                                                var28_76 = var30_59;
                                                var4_79 = var12_68;
                                                break block100;
                                            }
                                            catch (ZipException var1_43) {
                                                var2_55 = null;
                                                var27_82 = null;
                                                var28_76 = var31_57;
                                                var4_79 = var7_69;
                                                break block99;
                                            }
                                        }
                                        ** while (var4_79 < (var14_66 = var1_1.getCentralDirectory().getFileHeaders().size()))
lbl-1000:
                                        // 1 sources

                                        {
                                            try {
                                                var21_85 = var23_86 = ((FileHeader)var1_1.getCentralDirectory().getFileHeaders().get(var4_79)).getOffsetLocalHeader();
                                                if (((FileHeader)var1_1.getCentralDirectory().getFileHeaders().get(var4_79)).getZip64ExtendedInfo() != null) {
                                                    var21_85 = var23_86;
                                                    if (((FileHeader)var1_1.getCentralDirectory().getFileHeaders().get(var4_79)).getZip64ExtendedInfo().getOffsetLocalHeader() != -1L) {
                                                        var21_85 = ((FileHeader)var1_1.getCentralDirectory().getFileHeaders().get(var4_79)).getZip64ExtendedInfo().getOffsetLocalHeader();
                                                    }
                                                }
                                                ((FileHeader)var1_1.getCentralDirectory().getFileHeaders().get(var4_79)).setOffsetLocalHeader(var21_85 - (var17_80 - var19_84) - 1L);
                                                ++var4_79;
                                                continue;
                                            }
                                            catch (Throwable var1_15) {
                                                var3_56 = var26_74;
                                                var27_82 = var28_76;
                                                var26_74 = var30_59;
                                                var4_79 = var8_70;
                                                break block98;
                                            }
                                            catch (Exception var1_16) {
                                                var27_82 = var30_59;
                                                var4_79 = var12_68;
                                                break block100;
                                            }
                                            catch (ZipException var1_17) {
                                                var27_82 = var30_59;
                                                var4_79 = var7_69;
                                                break block99;
                                            }
                                        }
lbl486:
                                        // 2 sources

                                        var31_57 = new HeaderWriter();
                                        var4_79 = var11_71;
                                        var31_57.finalizeZipFile((ZipModel)var1_1, (OutputStream)var2_55);
                                        var5_60 = 1;
                                        var6_61 = 1;
                                        var9_63 = 1;
                                        var8_70 = 1;
                                        var7_69 = 1;
                                        var4_79 = 1;
                                        var1_1 = Long.toString(var1_1.getEndCentralDirRecord().getOffsetOfStartOfCentralDir());
                                        var4_79 = var9_63;
                                        var5_60 = var8_70;
                                        var6_61 = var7_69;
                                        var36_78.put("offsetCentralDir", var1_1);
                                        if (var28_76 == null) break block109;
                                        try {
                                            var28_76.close();
                                        }
                                        catch (IOException var1_18) {
                                            throw new ZipException("cannot close input stream or output stream when trying to delete a file from zip file");
                                        }
                                    }
                                    var2_55.close();
                                    this.restoreFileName((File)var30_59, (String)var26_74);
                                    return var36_78;
                                    catch (Throwable var1_19) {
                                        break block110;
                                    }
                                    catch (Exception var1_20) {
                                        var4_79 = var5_60;
                                        break block111;
                                    }
                                    catch (ZipException var1_21) {
                                        var4_79 = var6_61;
                                        break block112;
                                    }
                                    catch (Throwable var1_22) {
                                        var4_79 = var10_62;
                                    }
                                }
                                var3_56 = var26_74;
                                var27_82 = var28_76;
                                var26_74 = var30_59;
                                break block98;
                                catch (Exception var1_23) {
                                    var4_79 = var9_63;
                                }
                            }
                            var27_82 = var30_59;
                            break block100;
                            catch (ZipException var1_24) {
                                var4_79 = var13_64;
                            }
                        }
                        var27_82 = var30_59;
                        break block99;
                        catch (Throwable v0) {
                            var2_55 = var27_82;
                            var1_1 = v0;
                            var3_56 = var26_74;
                            var27_82 = var28_76;
                            var26_74 = var29_75;
                            break block98;
                        }
                        catch (Exception v1) {
                            var2_55 = var27_82;
                            var27_82 = var29_75;
                            var1_1 = v1;
                            var4_79 = var5_60;
                            break block100;
                        }
                        catch (ZipException v2) {
                            var2_55 = var27_82;
                            var27_82 = var29_75;
                            var1_1 = v2;
                            var4_79 = var6_61;
                            break block99;
                        }
                    }
                    var29_75 = var2_55;
                    var30_59 = var28_76;
                    var31_57 = var26_74;
                    var32_58 = var27_82;
                    var5_60 = var4_79;
                    ** try [egrp 31[TRYBLOCK] [164 : 2207->2343)] { 
lbl565:
                    // 1 sources

                    var3_56.endProgressMonitorError((Throwable)var1_1);
                    var29_75 = var2_55;
                    var30_59 = var28_76;
                    var31_57 = var26_74;
                    var32_58 = var27_82;
                    var5_60 = var4_79;
                    var29_75 = var2_55;
                    var30_59 = var28_76;
                    var31_57 = var26_74;
                    var32_58 = var27_82;
                    var5_60 = var4_79;
                    var3_56 = new Object((Throwable)var1_1);
                    var29_75 = var2_55;
                    var30_59 = var28_76;
                    var31_57 = var26_74;
                    var32_58 = var27_82;
                    var5_60 = var4_79;
                    throw var3_56;
                }
                var29_75 = var2_55;
                var30_59 = var28_76;
                var31_57 = var26_74;
                var32_58 = var27_82;
                var5_60 = var4_79;
                var3_56.endProgressMonitorError((Throwable)var1_1);
                var29_75 = var2_55;
                var30_59 = var28_76;
                var31_57 = var26_74;
                var32_58 = var27_82;
                var5_60 = var4_79;
                throw var1_1;
            }
lbl596:
            // 1 sources

            catch (Throwable var1_53) {
                var4_79 = var5_60;
                var26_74 = var32_58;
                var3_56 = var31_57;
                var27_82 = var30_59;
                var2_55 = var29_75;
            }
        }
        if (var27_82 != null) {
            try {
                var27_82.close();
            }
            catch (IOException var1_54) {
                throw new ZipException("cannot close input stream or output stream when trying to delete a file from zip file");
            }
        }
        if (var2_55 != null) {
            var2_55.close();
        }
        if (var4_79 != 0) {
            this.restoreFileName((File)var26_74, (String)var3_56);
            throw var1_1;
        }
        new File((String)var3_56).delete();
        throw var1_1;
    }

    public void mergeSplitZipFiles(ZipModel zipModel, File file, ProgressMonitor progressMonitor, boolean bl) throws ZipException {
        if (bl) {
            new Thread(this, "Zip4j", zipModel, file, progressMonitor){
                final ArchiveMaintainer this$0;
                final File val$outputZipFile;
                final ProgressMonitor val$progressMonitor;
                final ZipModel val$zipModel;
                {
                    this.this$0 = archiveMaintainer;
                    this.val$zipModel = zipModel;
                    this.val$outputZipFile = file;
                    this.val$progressMonitor = progressMonitor;
                    super(string2);
                }

                @Override
                public void run() {
                    try {
                        this.this$0.initMergeSplitZipFile(this.val$zipModel, this.val$outputZipFile, this.val$progressMonitor);
                    }
                    catch (ZipException zipException) {
                        // empty catch block
                    }
                }
            }.start();
        } else {
            this.initMergeSplitZipFile(zipModel, file, progressMonitor);
        }
    }

    public HashMap removeZipFile(ZipModel cloneable, FileHeader fileHeader, ProgressMonitor progressMonitor, boolean bl) throws ZipException {
        if (bl) {
            new Thread(this, "Zip4j", (ZipModel)cloneable, fileHeader, progressMonitor){
                final ArchiveMaintainer this$0;
                final FileHeader val$fileHeader;
                final ProgressMonitor val$progressMonitor;
                final ZipModel val$zipModel;
                {
                    this.this$0 = archiveMaintainer;
                    this.val$zipModel = zipModel;
                    this.val$fileHeader = fileHeader;
                    this.val$progressMonitor = progressMonitor;
                    super(string2);
                }

                @Override
                public void run() {
                    try {
                        this.this$0.initRemoveZipFile(this.val$zipModel, this.val$fileHeader, this.val$progressMonitor);
                        this.val$progressMonitor.endProgressMonitorSuccess();
                    }
                    catch (ZipException zipException) {
                        // empty catch block
                    }
                }
            }.start();
            return null;
        }
        cloneable = this.initRemoveZipFile((ZipModel)cloneable, fileHeader, progressMonitor);
        progressMonitor.endProgressMonitorSuccess();
        return cloneable;
    }

    /*
     * Exception decompiling
     */
    public void setComment(ZipModel var1_1, String var2_5) throws ZipException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 10[TRYBLOCK] [27 : 304->308)] java.lang.Throwable
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
}

