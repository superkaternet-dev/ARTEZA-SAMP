/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import net.lingala.zip4j.core.HeaderReader;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.unzip.Unzip;
import net.lingala.zip4j.util.ArchiveMaintainer;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Zip4jUtil;
import net.lingala.zip4j.zip.ZipEngine;

public class ZipFile {
    private String file;
    private String fileNameCharset;
    private boolean isEncrypted;
    private int mode;
    private ProgressMonitor progressMonitor;
    private boolean runInThread;
    private ZipModel zipModel;

    public ZipFile(File file) throws ZipException {
        if (file != null) {
            this.file = file.getPath();
            this.mode = 2;
            this.progressMonitor = new ProgressMonitor();
            this.runInThread = false;
            return;
        }
        throw new ZipException("Input zip file parameter is not null", 1);
    }

    public ZipFile(String string2) throws ZipException {
        this(new File(string2));
    }

    private void addFolder(File file, ZipParameters zipParameters, boolean bl) throws ZipException {
        this.checkZipModel();
        ZipModel zipModel = this.zipModel;
        if (zipModel != null) {
            if (bl && zipModel.isSplitArchive()) {
                throw new ZipException("This is a split archive. Zip file format does not allow updating split/spanned files");
            }
            new ZipEngine(this.zipModel).addFolderToZip(file, zipParameters, this.progressMonitor, this.runInThread);
            return;
        }
        throw new ZipException("internal error: zip model is null");
    }

    private void checkZipModel() throws ZipException {
        if (this.zipModel == null) {
            if (Zip4jUtil.checkFileExists(this.file)) {
                this.readZipInfo();
            } else {
                this.createNewZipModel();
            }
        }
    }

    private void createNewZipModel() {
        ZipModel zipModel;
        this.zipModel = zipModel = new ZipModel();
        zipModel.setZipFile(this.file);
        this.zipModel.setFileNameCharset(this.fileNameCharset);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void readZipInfo() throws ZipException {
        Throwable throwable2222222;
        Object object;
        block11: {
            Object object2;
            block10: {
                Object object3;
                if (!Zip4jUtil.checkFileExists(this.file)) {
                    ZipException zipException = new ZipException("zip file does not exist");
                    throw zipException;
                }
                if (!Zip4jUtil.checkFileReadAccess(this.file)) throw new ZipException("no read access for the input zip file");
                if (this.mode != 2) throw new ZipException("Invalid mode");
                object2 = null;
                ZipException zipException = null;
                object = zipException;
                Object object4 = object2;
                object = zipException;
                object4 = object2;
                object = zipException;
                object4 = object2;
                File file = new File(this.file);
                object = zipException;
                object4 = object2;
                object = object2 = (object3 = new RandomAccessFile(file, "r"));
                object4 = object2;
                if (this.zipModel != null) break block10;
                object = object2;
                object4 = object2;
                object = object2;
                object4 = object2;
                object3 = new HeaderReader((RandomAccessFile)object2);
                object = object2;
                object4 = object2;
                object3 = ((HeaderReader)object3).readAllHeaders(this.fileNameCharset);
                object = object2;
                object4 = object2;
                this.zipModel = object3;
                if (object3 != null) {
                    object = object2;
                    object4 = object2;
                    ((ZipModel)object3).setZipFile(this.file);
                }
                {
                    catch (Throwable throwable2222222) {
                        break block11;
                    }
                    catch (FileNotFoundException fileNotFoundException) {}
                    object = object4;
                    {
                        object = object4;
                        object2 = new ZipException(fileNotFoundException);
                        object = object4;
                        throw object2;
                    }
                }
            }
            try {
                ((RandomAccessFile)object2).close();
                return;
            }
            catch (IOException iOException) {
                return;
            }
        }
        if (object == null) throw throwable2222222;
        try {
            ((RandomAccessFile)object).close();
            throw throwable2222222;
        }
        catch (IOException iOException) {
            // empty catch block
        }
        throw throwable2222222;
    }

    public void addFile(File file, ZipParameters zipParameters) throws ZipException {
        ArrayList<File> arrayList = new ArrayList<File>();
        arrayList.add(file);
        this.addFiles(arrayList, zipParameters);
    }

    public void addFiles(ArrayList arrayList, ZipParameters zipParameters) throws ZipException {
        this.checkZipModel();
        if (this.zipModel != null) {
            if (arrayList != null) {
                if (Zip4jUtil.checkArrayListTypes(arrayList, 1)) {
                    if (zipParameters != null) {
                        if (this.progressMonitor.getState() != 1) {
                            if (Zip4jUtil.checkFileExists(this.file) && this.zipModel.isSplitArchive()) {
                                throw new ZipException("Zip file already exists. Zip file format does not allow updating split/spanned files");
                            }
                            new ZipEngine(this.zipModel).addFiles(arrayList, zipParameters, this.progressMonitor, this.runInThread);
                            return;
                        }
                        throw new ZipException("invalid operation - Zip4j is in busy state");
                    }
                    throw new ZipException("input parameters are null, cannot add files to zip");
                }
                throw new ZipException("One or more elements in the input ArrayList is not of type File");
            }
            throw new ZipException("input file ArrayList is null, cannot add files");
        }
        throw new ZipException("internal error: zip model is null");
    }

    public void addFolder(File file, ZipParameters zipParameters) throws ZipException {
        if (file != null) {
            if (zipParameters != null) {
                this.addFolder(file, zipParameters, true);
                return;
            }
            throw new ZipException("input parameters are null, cannot add folder to zip file");
        }
        throw new ZipException("input path is null, cannot add folder to zip file");
    }

    public void addFolder(String string2, ZipParameters zipParameters) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
            this.addFolder(new File(string2), zipParameters);
            return;
        }
        throw new ZipException("input path is null or empty, cannot add folder to zip file");
    }

    public void addStream(InputStream inputStream, ZipParameters zipParameters) throws ZipException {
        if (inputStream != null) {
            if (zipParameters != null) {
                this.setRunInThread(false);
                this.checkZipModel();
                if (this.zipModel != null) {
                    if (Zip4jUtil.checkFileExists(this.file) && this.zipModel.isSplitArchive()) {
                        throw new ZipException("Zip file already exists. Zip file format does not allow updating split/spanned files");
                    }
                    new ZipEngine(this.zipModel).addStreamToZip(inputStream, zipParameters);
                    return;
                }
                throw new ZipException("internal error: zip model is null");
            }
            throw new ZipException("zip parameters are null");
        }
        throw new ZipException("inputstream is null, cannot add file to zip");
    }

    public void createZipFile(File file, ZipParameters zipParameters) throws ZipException {
        ArrayList<File> arrayList = new ArrayList<File>();
        arrayList.add(file);
        this.createZipFile(arrayList, zipParameters, false, -1L);
    }

    public void createZipFile(File file, ZipParameters zipParameters, boolean bl, long l) throws ZipException {
        ArrayList<File> arrayList = new ArrayList<File>();
        arrayList.add(file);
        this.createZipFile(arrayList, zipParameters, bl, l);
    }

    public void createZipFile(ArrayList arrayList, ZipParameters zipParameters) throws ZipException {
        this.createZipFile(arrayList, zipParameters, false, -1L);
    }

    public void createZipFile(ArrayList serializable, ZipParameters zipParameters, boolean bl, long l) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(this.file)) {
            if (!Zip4jUtil.checkFileExists(this.file)) {
                if (serializable != null) {
                    if (Zip4jUtil.checkArrayListTypes((ArrayList)serializable, 1)) {
                        this.createNewZipModel();
                        this.zipModel.setSplitArchive(bl);
                        this.zipModel.setSplitLength(l);
                        this.addFiles((ArrayList)serializable, zipParameters);
                        return;
                    }
                    throw new ZipException("One or more elements in the input ArrayList is not of type File");
                }
                throw new ZipException("input file ArrayList is null, cannot create zip file");
            }
            serializable = new StringBuilder();
            ((StringBuilder)serializable).append("zip file: ");
            ((StringBuilder)serializable).append(this.file);
            ((StringBuilder)serializable).append(" already exists. To add files to existing zip file use addFile method");
            throw new ZipException(((StringBuilder)serializable).toString());
        }
        throw new ZipException("zip file path is empty");
    }

    public void createZipFileFromFolder(File comparable, ZipParameters zipParameters, boolean bl, long l) throws ZipException {
        if (comparable != null) {
            if (zipParameters != null) {
                if (!Zip4jUtil.checkFileExists(this.file)) {
                    this.createNewZipModel();
                    this.zipModel.setSplitArchive(bl);
                    if (bl) {
                        this.zipModel.setSplitLength(l);
                    }
                    this.addFolder((File)comparable, zipParameters, false);
                    return;
                }
                comparable = new StringBuilder();
                ((StringBuilder)comparable).append("zip file: ");
                ((StringBuilder)comparable).append(this.file);
                ((StringBuilder)comparable).append(" already exists. To add files to existing zip file use addFolder method");
                throw new ZipException(((StringBuilder)comparable).toString());
            }
            throw new ZipException("input parameters are null, cannot create zip file from folder");
        }
        throw new ZipException("folderToAdd is null, cannot create zip file from folder");
    }

    public void createZipFileFromFolder(String string2, ZipParameters zipParameters, boolean bl, long l) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
            this.createZipFileFromFolder(new File(string2), zipParameters, bl, l);
            return;
        }
        throw new ZipException("folderToAdd is empty or null, cannot create Zip File from folder");
    }

    public void extractAll(String string2) throws ZipException {
        this.extractAll(string2, null);
    }

    public void extractAll(String string2, UnzipParameters unzipParameters) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
            if (Zip4jUtil.checkOutputFolder(string2)) {
                if (this.zipModel == null) {
                    this.readZipInfo();
                }
                if (this.zipModel != null) {
                    if (this.progressMonitor.getState() != 1) {
                        new Unzip(this.zipModel).extractAll(unzipParameters, string2, this.progressMonitor, this.runInThread);
                        return;
                    }
                    throw new ZipException("invalid operation - Zip4j is in busy state");
                }
                throw new ZipException("Internal error occurred when extracting zip file");
            }
            throw new ZipException("invalid output path");
        }
        throw new ZipException("output path is null or invalid");
    }

    public void extractFile(String string2, String string3) throws ZipException {
        this.extractFile(string2, string3, null);
    }

    public void extractFile(String string2, String string3, UnzipParameters unzipParameters) throws ZipException {
        this.extractFile(string2, string3, unzipParameters, null);
    }

    public void extractFile(String object, String string2, UnzipParameters unzipParameters, String string3) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty((String)object)) {
            if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
                this.readZipInfo();
                object = Zip4jUtil.getFileHeader(this.zipModel, (String)object);
                if (object != null) {
                    if (this.progressMonitor.getState() != 1) {
                        ((FileHeader)object).extractFile(this.zipModel, string2, unzipParameters, string3, this.progressMonitor, this.runInThread);
                        return;
                    }
                    throw new ZipException("invalid operation - Zip4j is in busy state");
                }
                throw new ZipException("file header not found for given file name, cannot extract file");
            }
            throw new ZipException("destination string path is empty or null, cannot extract file");
        }
        throw new ZipException("file to extract is null or empty, cannot extract file");
    }

    public void extractFile(FileHeader fileHeader, String string2) throws ZipException {
        this.extractFile(fileHeader, string2, null);
    }

    public void extractFile(FileHeader fileHeader, String string2, UnzipParameters unzipParameters) throws ZipException {
        this.extractFile(fileHeader, string2, unzipParameters, null);
    }

    public void extractFile(FileHeader fileHeader, String string2, UnzipParameters unzipParameters, String string3) throws ZipException {
        if (fileHeader != null) {
            if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
                this.readZipInfo();
                if (this.progressMonitor.getState() != 1) {
                    fileHeader.extractFile(this.zipModel, string2, unzipParameters, string3, this.progressMonitor, this.runInThread);
                    return;
                }
                throw new ZipException("invalid operation - Zip4j is in busy state");
            }
            throw new ZipException("destination path is empty or null, cannot extract file");
        }
        throw new ZipException("input file header is null, cannot extract file");
    }

    public String getComment() throws ZipException {
        return this.getComment(null);
    }

    public String getComment(String object) throws ZipException {
        String string2 = object;
        if (object == null) {
            string2 = Zip4jUtil.isSupportedCharset("windows-1254") ? "windows-1254" : InternalZipConstants.CHARSET_DEFAULT;
        }
        if (Zip4jUtil.checkFileExists(this.file)) {
            this.checkZipModel();
            object = this.zipModel;
            if (object != null) {
                if (((ZipModel)object).getEndCentralDirRecord() != null) {
                    if (this.zipModel.getEndCentralDirRecord().getCommentBytes() != null && this.zipModel.getEndCentralDirRecord().getCommentBytes().length > 0) {
                        try {
                            object = new String(this.zipModel.getEndCentralDirRecord().getCommentBytes(), string2);
                            return object;
                        }
                        catch (UnsupportedEncodingException unsupportedEncodingException) {
                            throw new ZipException(unsupportedEncodingException);
                        }
                    }
                    return null;
                }
                throw new ZipException("end of central directory record is null, cannot read comment");
            }
            throw new ZipException("zip model is null, cannot read comment");
        }
        throw new ZipException("zip file does not exist, cannot read comment");
    }

    public File getFile() {
        return new File(this.file);
    }

    public FileHeader getFileHeader(String string2) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
            this.readZipInfo();
            ZipModel zipModel = this.zipModel;
            if (zipModel != null && zipModel.getCentralDirectory() != null) {
                return Zip4jUtil.getFileHeader(this.zipModel, string2);
            }
            return null;
        }
        throw new ZipException("input file name is emtpy or null, cannot get FileHeader");
    }

    public List getFileHeaders() throws ZipException {
        this.readZipInfo();
        ZipModel zipModel = this.zipModel;
        if (zipModel != null && zipModel.getCentralDirectory() != null) {
            return this.zipModel.getCentralDirectory().getFileHeaders();
        }
        return null;
    }

    public ZipInputStream getInputStream(FileHeader fileHeader) throws ZipException {
        if (fileHeader != null) {
            this.checkZipModel();
            ZipModel zipModel = this.zipModel;
            if (zipModel != null) {
                return new Unzip(zipModel).getInputStream(fileHeader);
            }
            throw new ZipException("zip model is null, cannot get inputstream");
        }
        throw new ZipException("FileHeader is null, cannot get InputStream");
    }

    public ProgressMonitor getProgressMonitor() {
        return this.progressMonitor;
    }

    public ArrayList getSplitZipFiles() throws ZipException {
        this.checkZipModel();
        return Zip4jUtil.getSplitZipFiles(this.zipModel);
    }

    public boolean isEncrypted() throws ZipException {
        if (this.zipModel == null) {
            this.readZipInfo();
            if (this.zipModel == null) {
                throw new ZipException("Zip Model is null");
            }
        }
        if (this.zipModel.getCentralDirectory() != null && this.zipModel.getCentralDirectory().getFileHeaders() != null) {
            ArrayList arrayList = this.zipModel.getCentralDirectory().getFileHeaders();
            for (int i = 0; i < arrayList.size(); ++i) {
                FileHeader fileHeader = (FileHeader)arrayList.get(i);
                if (fileHeader == null || !fileHeader.isEncrypted()) continue;
                this.isEncrypted = true;
                break;
            }
            return this.isEncrypted;
        }
        ZipException zipException = new ZipException("invalid zip file");
        throw zipException;
    }

    public boolean isRunInThread() {
        return this.runInThread;
    }

    public boolean isSplitArchive() throws ZipException {
        if (this.zipModel == null) {
            this.readZipInfo();
            if (this.zipModel == null) {
                throw new ZipException("Zip Model is null");
            }
        }
        return this.zipModel.isSplitArchive();
    }

    public boolean isValidZipFile() {
        try {
            this.readZipInfo();
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public void mergeSplitFiles(File file) throws ZipException {
        if (file != null) {
            if (!file.exists()) {
                this.checkZipModel();
                if (this.zipModel != null) {
                    ArchiveMaintainer archiveMaintainer = new ArchiveMaintainer();
                    archiveMaintainer.initProgressMonitorForMergeOp(this.zipModel, this.progressMonitor);
                    archiveMaintainer.mergeSplitZipFiles(this.zipModel, file, this.progressMonitor, this.runInThread);
                    return;
                }
                throw new ZipException("zip model is null, corrupt zip file?");
            }
            throw new ZipException("output Zip File already exists");
        }
        throw new ZipException("outputZipFile is null, cannot merge split files");
    }

    public void removeFile(String string2) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
            if (this.zipModel == null && Zip4jUtil.checkFileExists(this.file)) {
                this.readZipInfo();
            }
            if (!this.zipModel.isSplitArchive()) {
                Object object = Zip4jUtil.getFileHeader(this.zipModel, string2);
                if (object != null) {
                    this.removeFile((FileHeader)object);
                    return;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("could not find file header for file: ");
                ((StringBuilder)object).append(string2);
                throw new ZipException(((StringBuilder)object).toString());
            }
            throw new ZipException("Zip file format does not allow updating split/spanned files");
        }
        throw new ZipException("file name is empty or null, cannot remove file");
    }

    public void removeFile(FileHeader fileHeader) throws ZipException {
        if (fileHeader != null) {
            if (this.zipModel == null && Zip4jUtil.checkFileExists(this.file)) {
                this.readZipInfo();
            }
            if (!this.zipModel.isSplitArchive()) {
                ArchiveMaintainer archiveMaintainer = new ArchiveMaintainer();
                archiveMaintainer.initProgressMonitorForRemoveOp(this.zipModel, fileHeader, this.progressMonitor);
                archiveMaintainer.removeZipFile(this.zipModel, fileHeader, this.progressMonitor, this.runInThread);
                return;
            }
            throw new ZipException("Zip file format does not allow updating split/spanned files");
        }
        throw new ZipException("file header is null, cannot remove file");
    }

    public void setComment(String string2) throws ZipException {
        if (string2 != null) {
            if (Zip4jUtil.checkFileExists(this.file)) {
                this.readZipInfo();
                ZipModel zipModel = this.zipModel;
                if (zipModel != null) {
                    if (zipModel.getEndCentralDirRecord() != null) {
                        new ArchiveMaintainer().setComment(this.zipModel, string2);
                        return;
                    }
                    throw new ZipException("end of central directory is null, cannot set comment");
                }
                throw new ZipException("zipModel is null, cannot update zip file");
            }
            throw new ZipException("zip file does not exist, cannot set comment for zip file");
        }
        throw new ZipException("input comment is null, cannot update zip file");
    }

    public void setFileNameCharset(String string2) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
            if (Zip4jUtil.isSupportedCharset(string2)) {
                this.fileNameCharset = string2;
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unsupported charset: ");
            stringBuilder.append(string2);
            throw new ZipException(stringBuilder.toString());
        }
        throw new ZipException("null or empty charset name");
    }

    public void setPassword(String string2) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
            this.setPassword(string2.toCharArray());
            return;
        }
        throw new NullPointerException();
    }

    public void setPassword(char[] object) throws ZipException {
        if (this.zipModel == null) {
            this.readZipInfo();
            if (this.zipModel == null) {
                throw new ZipException("Zip Model is null");
            }
        }
        if (this.zipModel.getCentralDirectory() != null && this.zipModel.getCentralDirectory().getFileHeaders() != null) {
            for (int i = 0; i < this.zipModel.getCentralDirectory().getFileHeaders().size(); ++i) {
                if (this.zipModel.getCentralDirectory().getFileHeaders().get(i) == null || !((FileHeader)this.zipModel.getCentralDirectory().getFileHeaders().get(i)).isEncrypted()) continue;
                ((FileHeader)this.zipModel.getCentralDirectory().getFileHeaders().get(i)).setPassword((char[])object);
            }
            return;
        }
        object = new ZipException("invalid zip file");
        throw object;
    }

    public void setRunInThread(boolean bl) {
        this.runInThread = bl;
    }
}

