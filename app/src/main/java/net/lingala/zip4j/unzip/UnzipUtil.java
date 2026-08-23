/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.unzip;

import java.io.File;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.util.Zip4jUtil;

public class UnzipUtil {
    public static void applyFileAttributes(FileHeader fileHeader, File file) throws ZipException {
        UnzipUtil.applyFileAttributes(fileHeader, file, null);
    }

    public static void applyFileAttributes(FileHeader fileHeader, File file, UnzipParameters unzipParameters) throws ZipException {
        if (fileHeader != null) {
            if (file != null) {
                if (Zip4jUtil.checkFileExists(file)) {
                    if (unzipParameters == null || !unzipParameters.isIgnoreDateTimeAttributes()) {
                        UnzipUtil.setFileLastModifiedTime(fileHeader, file);
                    }
                    if (unzipParameters == null) {
                        UnzipUtil.setFileAttributes(fileHeader, file, true, true, true, true);
                    } else if (unzipParameters.isIgnoreAllFileAttributes()) {
                        UnzipUtil.setFileAttributes(fileHeader, file, false, false, false, false);
                    } else {
                        UnzipUtil.setFileAttributes(fileHeader, file, unzipParameters.isIgnoreReadOnlyFileAttribute() ^ true, unzipParameters.isIgnoreHiddenFileAttribute() ^ true, unzipParameters.isIgnoreArchiveFileAttribute() ^ true, unzipParameters.isIgnoreSystemFileAttribute() ^ true);
                    }
                    return;
                }
                throw new ZipException("cannot set file properties: file doesnot exist");
            }
            throw new ZipException("cannot set file properties: output file is null");
        }
        throw new ZipException("cannot set file properties: file header is null");
    }

    private static void setFileAttributes(FileHeader object, File file, boolean bl, boolean bl2, boolean bl3, boolean bl4) throws ZipException {
        if (object != null) {
            if ((object = (Object)((FileHeader)object).getExternalFileAttr()) == null) {
                return;
            }
            switch (object[0]) {
                default: {
                    break;
                }
                case 38: {
                    if (bl) {
                        Zip4jUtil.setFileReadOnly(file);
                    }
                    if (bl2) {
                        Zip4jUtil.setFileHidden(file);
                    }
                    if (!bl4) break;
                    Zip4jUtil.setFileSystemMode(file);
                    break;
                }
                case 35: {
                    if (bl3) {
                        Zip4jUtil.setFileArchive(file);
                    }
                    if (bl) {
                        Zip4jUtil.setFileReadOnly(file);
                    }
                    if (!bl2) break;
                    Zip4jUtil.setFileHidden(file);
                    break;
                }
                case 34: 
                case 50: {
                    if (bl3) {
                        Zip4jUtil.setFileArchive(file);
                    }
                    if (!bl2) break;
                    Zip4jUtil.setFileHidden(file);
                    break;
                }
                case 33: {
                    if (bl3) {
                        Zip4jUtil.setFileArchive(file);
                    }
                    if (!bl) break;
                    Zip4jUtil.setFileReadOnly(file);
                    break;
                }
                case 32: 
                case 48: {
                    if (!bl3) break;
                    Zip4jUtil.setFileArchive(file);
                    break;
                }
                case 3: {
                    if (bl) {
                        Zip4jUtil.setFileReadOnly(file);
                    }
                    if (!bl2) break;
                    Zip4jUtil.setFileHidden(file);
                    break;
                }
                case 2: 
                case 18: {
                    if (!bl2) break;
                    Zip4jUtil.setFileHidden(file);
                    break;
                }
                case 1: {
                    if (!bl) break;
                    Zip4jUtil.setFileReadOnly(file);
                }
            }
            return;
        }
        throw new ZipException("invalid file header. cannot set file attributes");
    }

    private static void setFileLastModifiedTime(FileHeader fileHeader, File file) throws ZipException {
        if (fileHeader.getLastModFileTime() <= 0) {
            return;
        }
        if (file.exists()) {
            file.setLastModified(Zip4jUtil.dosToJavaTme(fileHeader.getLastModFileTime()));
        }
    }
}

