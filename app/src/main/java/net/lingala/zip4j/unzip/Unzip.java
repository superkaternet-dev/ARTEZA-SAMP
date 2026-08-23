/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.unzip;

import java.io.File;
import java.util.ArrayList;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.CentralDirectory;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.unzip.UnzipEngine;
import net.lingala.zip4j.util.Zip4jUtil;

public class Unzip {
    private ZipModel zipModel;

    public Unzip(ZipModel zipModel) throws ZipException {
        if (zipModel != null) {
            this.zipModel = zipModel;
            return;
        }
        throw new ZipException("ZipModel is null");
    }

    private long calculateTotalWork(ArrayList serializable) throws ZipException {
        if (serializable != null) {
            long l = 0L;
            for (int i = 0; i < ((ArrayList)serializable).size(); ++i) {
                FileHeader fileHeader = (FileHeader)((ArrayList)serializable).get(i);
                if (fileHeader.getZip64ExtendedInfo() != null && fileHeader.getZip64ExtendedInfo().getUnCompressedSize() > 0L) {
                    l += fileHeader.getZip64ExtendedInfo().getCompressedSize();
                    continue;
                }
                l += fileHeader.getCompressedSize();
            }
            return l;
        }
        serializable = new ZipException("fileHeaders is null, cannot calculate total work");
        throw serializable;
    }

    private void checkOutputDirectoryStructure(FileHeader object, String object2, String charSequence) throws ZipException {
        if (object != null && Zip4jUtil.isStringNotNullAndNotEmpty((String)object2)) {
            object = ((FileHeader)object).getFileName();
            if (Zip4jUtil.isStringNotNullAndNotEmpty((String)charSequence)) {
                object = charSequence;
            }
            if (!Zip4jUtil.isStringNotNullAndNotEmpty((String)object)) {
                return;
            }
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)object2);
            ((StringBuilder)charSequence).append((String)object);
            object = ((StringBuilder)charSequence).toString();
            try {
                object2 = new File((String)object);
                object = ((File)object2).getParent();
                object2 = new File((String)object);
                if (!((File)object2).exists()) {
                    ((File)object2).mkdirs();
                }
                return;
            }
            catch (Exception exception) {
                throw new ZipException(exception);
            }
        }
        throw new ZipException("Cannot check output directory structure...one of the parameters was null");
    }

    private void initExtractAll(ArrayList arrayList, UnzipParameters unzipParameters, ProgressMonitor progressMonitor, String string2) throws ZipException {
        for (int i = 0; i < arrayList.size(); ++i) {
            this.initExtractFile((FileHeader)arrayList.get(i), string2, unzipParameters, null, progressMonitor);
            if (!progressMonitor.isCancelAllTasks()) continue;
            progressMonitor.setResult(3);
            progressMonitor.setState(0);
            return;
        }
    }

    /*
     * Exception decompiling
     */
    private void initExtractFile(FileHeader var1_1, String var2_4, UnzipParameters var3_7, String var4_8, ProgressMonitor var5_9) throws ZipException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [7[CATCHBLOCK]], but top level block is 4[TRYBLOCK]
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

    public void extractAll(UnzipParameters unzipParameters, String string2, ProgressMonitor progressMonitor, boolean bl) throws ZipException {
        Object object = this.zipModel.getCentralDirectory();
        if (object != null && ((CentralDirectory)object).getFileHeaders() != null) {
            object = ((CentralDirectory)object).getFileHeaders();
            progressMonitor.setCurrentOperation(1);
            progressMonitor.setTotalWork(this.calculateTotalWork((ArrayList)object));
            progressMonitor.setState(1);
            if (bl) {
                new Thread(this, "Zip4j", (ArrayList)object, unzipParameters, progressMonitor, string2){
                    final Unzip this$0;
                    final ArrayList val$fileHeaders;
                    final String val$outPath;
                    final ProgressMonitor val$progressMonitor;
                    final UnzipParameters val$unzipParameters;
                    {
                        this.this$0 = unzip;
                        this.val$fileHeaders = arrayList;
                        this.val$unzipParameters = unzipParameters;
                        this.val$progressMonitor = progressMonitor;
                        this.val$outPath = string3;
                        super(string2);
                    }

                    @Override
                    public void run() {
                        try {
                            this.this$0.initExtractAll(this.val$fileHeaders, this.val$unzipParameters, this.val$progressMonitor, this.val$outPath);
                            this.val$progressMonitor.endProgressMonitorSuccess();
                        }
                        catch (ZipException zipException) {
                            // empty catch block
                        }
                    }
                }.start();
            } else {
                this.initExtractAll((ArrayList)object, unzipParameters, progressMonitor, string2);
            }
            return;
        }
        throw new ZipException("invalid central directory in zipModel");
    }

    public void extractFile(FileHeader fileHeader, String string2, UnzipParameters unzipParameters, String string3, ProgressMonitor progressMonitor, boolean bl) throws ZipException {
        if (fileHeader != null) {
            progressMonitor.setCurrentOperation(1);
            progressMonitor.setTotalWork(fileHeader.getCompressedSize());
            progressMonitor.setState(1);
            progressMonitor.setPercentDone(0);
            progressMonitor.setFileName(fileHeader.getFileName());
            if (bl) {
                new Thread(this, "Zip4j", fileHeader, string2, unzipParameters, string3, progressMonitor){
                    final Unzip this$0;
                    final FileHeader val$fileHeader;
                    final String val$newFileName;
                    final String val$outPath;
                    final ProgressMonitor val$progressMonitor;
                    final UnzipParameters val$unzipParameters;
                    {
                        this.this$0 = unzip;
                        this.val$fileHeader = fileHeader;
                        this.val$outPath = string3;
                        this.val$unzipParameters = unzipParameters;
                        this.val$newFileName = string4;
                        this.val$progressMonitor = progressMonitor;
                        super(string2);
                    }

                    @Override
                    public void run() {
                        try {
                            this.this$0.initExtractFile(this.val$fileHeader, this.val$outPath, this.val$unzipParameters, this.val$newFileName, this.val$progressMonitor);
                            this.val$progressMonitor.endProgressMonitorSuccess();
                        }
                        catch (ZipException zipException) {
                            // empty catch block
                        }
                    }
                }.start();
            } else {
                this.initExtractFile(fileHeader, string2, unzipParameters, string3, progressMonitor);
                progressMonitor.endProgressMonitorSuccess();
            }
            return;
        }
        throw new ZipException("fileHeader is null");
    }

    public ZipInputStream getInputStream(FileHeader fileHeader) throws ZipException {
        return new UnzipEngine(this.zipModel, fileHeader).getInputStream();
    }
}

