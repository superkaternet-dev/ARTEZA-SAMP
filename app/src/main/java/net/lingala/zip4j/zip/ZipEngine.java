/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.zip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.EndCentralDirRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.ArchiveMaintainer;
import net.lingala.zip4j.util.Zip4jUtil;

public class ZipEngine {
    private ZipModel zipModel;

    public ZipEngine(ZipModel zipModel) throws ZipException {
        if (zipModel != null) {
            this.zipModel = zipModel;
            return;
        }
        throw new ZipException("zip model is null in ZipEngine constructor");
    }

    private long calculateTotalWork(ArrayList serializable, ZipParameters zipParameters) throws ZipException {
        if (serializable != null) {
            long l = 0L;
            for (int i = 0; i < ((ArrayList)serializable).size(); ++i) {
                long l2 = l;
                if (((ArrayList)serializable).get(i) instanceof File) {
                    l2 = l;
                    if (((File)((ArrayList)serializable).get(i)).exists()) {
                        l = zipParameters.isEncryptFiles() && zipParameters.getEncryptionMethod() == 0 ? (l += Zip4jUtil.getFileLengh((File)((ArrayList)serializable).get(i)) * 2L) : (l += Zip4jUtil.getFileLengh((File)((ArrayList)serializable).get(i)));
                        l2 = l;
                        if (this.zipModel.getCentralDirectory() != null) {
                            l2 = l;
                            if (this.zipModel.getCentralDirectory().getFileHeaders() != null) {
                                l2 = l;
                                if (this.zipModel.getCentralDirectory().getFileHeaders().size() > 0) {
                                    Object object = Zip4jUtil.getRelativeFileName(((File)((ArrayList)serializable).get(i)).getAbsolutePath(), zipParameters.getRootFolderInZip(), zipParameters.getDefaultFolderPath());
                                    object = Zip4jUtil.getFileHeader(this.zipModel, (String)object);
                                    l2 = l;
                                    if (object != null) {
                                        l2 = l + (Zip4jUtil.getFileLengh(new File(this.zipModel.getZipFile())) - ((FileHeader)object).getCompressedSize());
                                    }
                                }
                            }
                        }
                    }
                }
                l = l2;
            }
            return l;
        }
        serializable = new ZipException("file list is null, cannot calculate total work");
        throw serializable;
    }

    private void checkParameters(ZipParameters zipParameters) throws ZipException {
        if (zipParameters != null) {
            if (zipParameters.getCompressionMethod() != 0 && zipParameters.getCompressionMethod() != 8) {
                throw new ZipException("unsupported compression type");
            }
            if (zipParameters.getCompressionMethod() == 8 && zipParameters.getCompressionLevel() < 0 && zipParameters.getCompressionLevel() > 9) {
                throw new ZipException("invalid compression level. compression level dor deflate should be in the range of 0-9");
            }
            if (zipParameters.isEncryptFiles()) {
                if (zipParameters.getEncryptionMethod() != 0 && zipParameters.getEncryptionMethod() != 99) {
                    throw new ZipException("unsupported encryption method");
                }
                if (zipParameters.getPassword() == null || zipParameters.getPassword().length <= 0) {
                    throw new ZipException("input password is empty or null");
                }
            } else {
                zipParameters.setAesKeyStrength(-1);
                zipParameters.setEncryptionMethod(-1);
            }
            return;
        }
        throw new ZipException("cannot validate zip parameters");
    }

    private EndCentralDirRecord createEndOfCentralDirectoryRecord() {
        EndCentralDirRecord endCentralDirRecord = new EndCentralDirRecord();
        endCentralDirRecord.setSignature(101010256L);
        endCentralDirRecord.setNoOfThisDisk(0);
        endCentralDirRecord.setTotNoOfEntriesInCentralDir(0);
        endCentralDirRecord.setTotNoOfEntriesInCentralDirOnThisDisk(0);
        endCentralDirRecord.setOffsetOfStartOfCentralDir(0L);
        return endCentralDirRecord;
    }

    /*
     * Exception decompiling
     */
    private void initAddFiles(ArrayList var1_1, ZipParameters var2_13, ProgressMonitor var3_22) throws ZipException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 55[TRYBLOCK] [149 : 1244->1249)] java.lang.Throwable
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

    private RandomAccessFile prepareFileOutputStream() throws ZipException {
        Object object = this.zipModel.getZipFile();
        if (Zip4jUtil.isStringNotNullAndNotEmpty((String)object)) {
            try {
                File file = new File((String)object);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                object = new RandomAccessFile(file, "rw");
                return object;
            }
            catch (FileNotFoundException fileNotFoundException) {
                throw new ZipException(fileNotFoundException);
            }
        }
        throw new ZipException("invalid output path");
    }

    /*
     * Loose catch block
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void removeFilesIfExists(ArrayList serializable, ZipParameters object, ProgressMonitor progressMonitor) throws ZipException {
        void var1_10;
        Object object2;
        block24: {
            ZipException zipException;
            Object object3;
            block23: {
                block20: {
                    Object object4;
                    object2 = this.zipModel;
                    if (object2 == null) return;
                    if (((ZipModel)object2).getCentralDirectory() == null) return;
                    if (this.zipModel.getCentralDirectory().getFileHeaders() == null) return;
                    if (this.zipModel.getCentralDirectory().getFileHeaders().size() <= 0) {
                        return;
                    }
                    object2 = null;
                    int n = 0;
                    while (true) {
                        block21: {
                            long l;
                            void var3_15;
                            Object object5;
                            Object object6;
                            block22: {
                                int n2 = ((ArrayList)serializable).size();
                                if (n >= n2) break block20;
                                object4 = object2;
                                object3 = object2;
                                object6 = Zip4jUtil.getRelativeFileName(((File)((ArrayList)serializable).get(n)).getAbsolutePath(), ((ZipParameters)((Object)zipException)).getRootFolderInZip(), ((ZipParameters)((Object)zipException)).getDefaultFolderPath());
                                object4 = object2;
                                object3 = object2;
                                object5 = Zip4jUtil.getFileHeader(this.zipModel, (String)object6);
                                object3 = object2;
                                if (object5 == null) break block21;
                                object6 = object2;
                                if (object2 != null) {
                                    object4 = object2;
                                    object3 = object2;
                                    ((RandomAccessFile)object2).close();
                                    object6 = null;
                                }
                                object4 = object6;
                                object3 = object6;
                                object4 = object6;
                                object3 = object6;
                                object2 = new ArchiveMaintainer();
                                object4 = object6;
                                object3 = object6;
                                var3_15.setCurrentOperation(2);
                                object4 = object6;
                                object3 = object6;
                                object5 = ((ArchiveMaintainer)object2).initRemoveZipFile(this.zipModel, (FileHeader)object5, (ProgressMonitor)var3_15);
                                object4 = object6;
                                object3 = object6;
                                if (!var3_15.isCancelAllTasks()) break block22;
                                object4 = object6;
                                object3 = object6;
                                var3_15.setResult(3);
                                object4 = object6;
                                object3 = object6;
                                var3_15.setState(0);
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
                            object4 = object6;
                            object3 = object6;
                            var3_15.setCurrentOperation(0);
                            object3 = object6;
                            if (object6 != null) break block21;
                            object4 = object6;
                            object3 = object6;
                            object3 = object2 = this.prepareFileOutputStream();
                            if (object5 == null) break block21;
                            object4 = object2;
                            object3 = object2;
                            object6 = ((HashMap)object5).get("offsetCentralDir");
                            object3 = object2;
                            if (object6 == null) break block21;
                            object4 = object2;
                            object3 = object2;
                            try {
                                l = Long.parseLong((String)((HashMap)object5).get("offsetCentralDir"));
                                object3 = object2;
                                if (l < 0L) break block21;
                                object4 = object2;
                                object3 = object2;
                            }
                            catch (Exception exception) {
                                object4 = object2;
                                object3 = object2;
                                object4 = object2;
                                object3 = object2;
                                ZipException zipException2 = new ZipException("Error while parsing offset central directory. Cannot update already existing file header");
                                object4 = object2;
                                object3 = object2;
                                throw zipException2;
                            }
                            catch (NumberFormatException numberFormatException) {
                                object4 = object2;
                                object3 = object2;
                                object4 = object2;
                                object3 = object2;
                                ZipException zipException3 = new ZipException("NumberFormatException while parsing offset central directory. Cannot update already existing file header");
                                object4 = object2;
                                object3 = object2;
                                throw zipException3;
                            }
                            ((RandomAccessFile)object2).seek(l);
                            object3 = object2;
                        }
                        ++n;
                        object2 = object3;
                    }
                    catch (IOException iOException) {
                        object2 = object4;
                        break block23;
                    }
                }
                if (object2 == null) return;
                try {
                    ((RandomAccessFile)object2).close();
                    return;
                }
                catch (IOException iOException) {
                    return;
                }
                catch (Throwable throwable) {
                    break block24;
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
            object3 = object2;
            try {
                object3 = object2;
                zipException = new ZipException((Throwable)serializable);
                object3 = object2;
                throw zipException;
            }
            catch (Throwable throwable) {
                object2 = object3;
            }
        }
        if (object2 == null) throw var1_10;
        try {
            ((RandomAccessFile)object2).close();
            throw var1_10;
        }
        catch (IOException iOException) {
            // empty catch block
        }
        throw var1_10;
    }

    public void addFiles(ArrayList arrayList, ZipParameters zipParameters, ProgressMonitor progressMonitor, boolean bl) throws ZipException {
        if (arrayList != null && zipParameters != null) {
            if (arrayList.size() > 0) {
                progressMonitor.setCurrentOperation(0);
                progressMonitor.setState(1);
                progressMonitor.setResult(1);
                if (bl) {
                    progressMonitor.setTotalWork(this.calculateTotalWork(arrayList, zipParameters));
                    progressMonitor.setFileName(((File)arrayList.get(0)).getAbsolutePath());
                    new Thread(this, "Zip4j", arrayList, zipParameters, progressMonitor){
                        final ZipEngine this$0;
                        final ArrayList val$fileList;
                        final ZipParameters val$parameters;
                        final ProgressMonitor val$progressMonitor;
                        {
                            this.this$0 = zipEngine;
                            this.val$fileList = arrayList;
                            this.val$parameters = zipParameters;
                            this.val$progressMonitor = progressMonitor;
                            super(string2);
                        }

                        @Override
                        public void run() {
                            try {
                                this.this$0.initAddFiles(this.val$fileList, this.val$parameters, this.val$progressMonitor);
                            }
                            catch (ZipException zipException) {
                                // empty catch block
                            }
                        }
                    }.start();
                } else {
                    this.initAddFiles(arrayList, zipParameters, progressMonitor);
                }
                return;
            }
            throw new ZipException("no files to add");
        }
        throw new ZipException("one of the input parameters is null when adding files");
    }

    public void addFolderToZip(File file, ZipParameters object, ProgressMonitor progressMonitor, boolean bl) throws ZipException {
        if (file != null && object != null) {
            if (Zip4jUtil.checkFileExists(file.getAbsolutePath())) {
                if (file.isDirectory()) {
                    if (Zip4jUtil.checkFileReadAccess(file.getAbsolutePath())) {
                        ArrayList<File> arrayList;
                        Object object2;
                        if (((ZipParameters)object).isIncludeRootFolder()) {
                            object2 = file.getAbsolutePath();
                            arrayList = "";
                            if (object2 != null) {
                                if (file.getAbsoluteFile().getParentFile() != null) {
                                    arrayList = file.getAbsoluteFile().getParentFile().getAbsolutePath();
                                }
                            } else if (file.getParentFile() != null) {
                                arrayList = file.getParentFile().getAbsolutePath();
                            }
                        } else {
                            arrayList = file.getAbsolutePath();
                        }
                        ((ZipParameters)object).setDefaultFolderPath((String)((Object)arrayList));
                        object2 = Zip4jUtil.getFilesInDirectoryRec(file, ((ZipParameters)object).isReadHiddenFiles());
                        arrayList = object2;
                        if (((ZipParameters)object).isIncludeRootFolder()) {
                            arrayList = object2;
                            if (object2 == null) {
                                arrayList = new ArrayList<File>();
                            }
                            arrayList.add(file);
                        }
                        this.addFiles(arrayList, (ZipParameters)object, progressMonitor, bl);
                        return;
                    }
                    object = new StringBuilder();
                    ((StringBuilder)object).append("cannot read folder: ");
                    ((StringBuilder)object).append(file.getAbsolutePath());
                    throw new ZipException(((StringBuilder)object).toString());
                }
                throw new ZipException("input file is not a folder, user addFileToZip method to add files");
            }
            throw new ZipException("input folder does not exist");
        }
        throw new ZipException("one of the input parameters is null, cannot add folder to zip");
    }

    /*
     * Exception decompiling
     */
    public void addStreamToZip(InputStream var1_1, ZipParameters var2_6) throws ZipException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 22[TRYBLOCK] [64 : 483->487)] java.lang.Throwable
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

