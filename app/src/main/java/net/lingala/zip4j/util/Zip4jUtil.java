/*
 * Decompiled with CFR 0.152.
 */
package net.lingala.zip4j.util;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.util.InternalZipConstants;

public class Zip4jUtil {
    public static boolean checkArrayListTypes(ArrayList serializable, int n) throws ZipException {
        if (serializable != null) {
            if (((ArrayList)serializable).size() <= 0) {
                return true;
            }
            int n2 = 0;
            int n3 = 0;
            int n4 = 0;
            block0 : switch (n) {
                default: {
                    n = n3;
                    break;
                }
                case 2: {
                    n3 = 0;
                    while (true) {
                        n = n4;
                        if (n3 >= ((ArrayList)serializable).size()) break block0;
                        if (!(((ArrayList)serializable).get(n3) instanceof String)) {
                            n = 1;
                            break block0;
                        }
                        ++n3;
                    }
                }
                case 1: {
                    n3 = 0;
                    while (true) {
                        n = n2;
                        if (n3 >= ((ArrayList)serializable).size()) break block0;
                        if (!(((ArrayList)serializable).get(n3) instanceof File)) {
                            n = 1;
                            break block0;
                        }
                        ++n3;
                    }
                }
            }
            return (n ^ 1) != 0;
        }
        serializable = new ZipException("input arraylist is null, cannot check types");
        throw serializable;
    }

    public static boolean checkFileExists(File file) throws ZipException {
        if (file != null) {
            return file.exists();
        }
        throw new ZipException("cannot check if file exists: input file is null");
    }

    public static boolean checkFileExists(String string2) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
            return Zip4jUtil.checkFileExists(new File(string2));
        }
        throw new ZipException("path is null");
    }

    public static boolean checkFileReadAccess(String string2) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
            if (Zip4jUtil.checkFileExists(string2)) {
                try {
                    File file = new File(string2);
                    boolean bl = file.canRead();
                    return bl;
                }
                catch (Exception exception) {
                    throw new ZipException("cannot read zip file");
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("file does not exist: ");
            stringBuilder.append(string2);
            throw new ZipException(stringBuilder.toString());
        }
        throw new ZipException("path is null");
    }

    public static boolean checkFileWriteAccess(String string2) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
            if (Zip4jUtil.checkFileExists(string2)) {
                try {
                    File file = new File(string2);
                    boolean bl = file.canWrite();
                    return bl;
                }
                catch (Exception exception) {
                    throw new ZipException("cannot read zip file");
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("file does not exist: ");
            stringBuilder.append(string2);
            throw new ZipException(stringBuilder.toString());
        }
        throw new ZipException("path is null");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static boolean checkOutputFolder(String object) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty((String)object)) throw new ZipException(new NullPointerException("output path is null"));
        if (((File)(object = new File((String)object))).exists()) {
            if (!((File)object).isDirectory()) throw new ZipException("output folder is not valid");
            if (!((File)object).canWrite()) throw new ZipException("no write access to output folder");
            return true;
        }
        try {
            ((File)object).mkdirs();
            if (!((File)object).isDirectory()) {
                object = new ZipException("output folder is not valid");
                throw object;
            }
            if (((File)object).canWrite()) {
                return true;
            }
            object = new ZipException("no write access to destination folder");
            throw object;
        }
        catch (Exception exception) {
            throw new ZipException("Cannot create destination folder");
        }
    }

    public static byte[] convertCharset(String object) throws ZipException {
        block6: {
            Object object2;
            block7: {
                block5: {
                    object2 = Zip4jUtil.detectCharSet((String)object);
                    if (!((String)object2).equals("Cp850")) break block5;
                    object = object2 = (Object)((String)object).getBytes("Cp850");
                    break block6;
                }
                if (!((String)object2).equals("UTF8")) break block7;
                object = object2 = (Object)((String)object).getBytes("UTF8");
                break block6;
            }
            try {
                object = object2 = (Object)((String)object).getBytes();
            }
            catch (Exception exception) {
                throw new ZipException(exception);
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                return ((String)object).getBytes();
            }
        }
        return object;
    }

    public static String decodeFileName(byte[] byArray, boolean bl) {
        if (bl) {
            try {
                String string2 = new String(byArray, "UTF8");
                return string2;
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                return new String(byArray);
            }
        }
        return Zip4jUtil.getCp850EncodedString(byArray);
    }

    public static String detectCharSet(String string2) throws ZipException {
        if (string2 != null) {
            block7: {
                String string3;
                byte[] byArray;
                block6: {
                    byArray = string2.getBytes("Cp850");
                    string3 = new String(byArray, "Cp850");
                    if (!string2.equals(string3)) break block6;
                    return "Cp850";
                }
                byArray = string2.getBytes("UTF8");
                string3 = new String(byArray, "UTF8");
                if (!string2.equals(string3)) break block7;
                return "UTF8";
            }
            try {
                string2 = InternalZipConstants.CHARSET_DEFAULT;
                return string2;
            }
            catch (Exception exception) {
                return InternalZipConstants.CHARSET_DEFAULT;
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                return InternalZipConstants.CHARSET_DEFAULT;
            }
        }
        throw new ZipException("input string is null, cannot detect charset");
    }

    public static long dosToJavaTme(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.set((n >> 25 & 0x7F) + 1980, (n >> 21 & 0xF) - 1, n >> 16 & 0x1F, n >> 11 & 0x1F, n >> 5 & 0x3F, (n & 0x1F) * 2);
        calendar.set(14, 0);
        return calendar.getTime().getTime();
    }

    public static String getAbsoluteFilePath(String string2) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
            return new File(string2).getAbsolutePath();
        }
        throw new ZipException("filePath is null or empty, cannot get absolute file path");
    }

    public static long[] getAllHeaderSignatures() {
        return new long[]{67324752L, 134695760L, 33639248L, 101010256L, 84233040L, 134630224L, 134695760L, 117853008L, 101075792L, 1L, 39169L};
    }

    public static String getCp850EncodedString(byte[] byArray) {
        try {
            String string2 = new String(byArray, "Cp850");
            return string2;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            return new String(byArray);
        }
    }

    public static int getEncodedStringLength(String string2) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
            return Zip4jUtil.getEncodedStringLength(string2, Zip4jUtil.detectCharSet(string2));
        }
        throw new ZipException("input string is null, cannot calculate encoded String length");
    }

    public static int getEncodedStringLength(String object, String object2) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty((String)object)) {
            if (Zip4jUtil.isStringNotNullAndNotEmpty((String)object2)) {
                block8: {
                    block7: {
                        if (!((String)object2).equals("Cp850")) break block7;
                        object = object2 = ByteBuffer.wrap(((String)object).getBytes("Cp850"));
                    }
                    if (!((String)object2).equals("UTF8")) break block8;
                    object = object2 = ByteBuffer.wrap(((String)object).getBytes("UTF8"));
                }
                try {
                    object = object2 = ByteBuffer.wrap(((String)object).getBytes((String)object2));
                }
                catch (Exception exception) {
                    throw new ZipException(exception);
                }
                catch (UnsupportedEncodingException unsupportedEncodingException) {
                    object = ByteBuffer.wrap(((String)object).getBytes());
                }
                return ((Buffer)object).limit();
            }
            throw new ZipException("encoding is not defined, cannot calculate string length");
        }
        throw new ZipException("input string is null, cannot calculate encoded String length");
    }

    public static FileHeader getFileHeader(ZipModel object, String object2) throws ZipException {
        if (object != null) {
            if (Zip4jUtil.isStringNotNullAndNotEmpty((String)object2)) {
                Object object3;
                Object object4 = object3 = Zip4jUtil.getFileHeaderWithExactMatch((ZipModel)object, (String)object2);
                if (object3 == null) {
                    object3 = ((String)object2).replaceAll("\\\\", "/");
                    object4 = object2 = Zip4jUtil.getFileHeaderWithExactMatch((ZipModel)object, (String)object3);
                    if (object2 == null) {
                        object4 = Zip4jUtil.getFileHeaderWithExactMatch((ZipModel)object, ((String)object3).replaceAll("/", "\\\\"));
                    }
                }
                return object4;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("file name is null, cannot determine file header for fileName: ");
            ((StringBuilder)object).append((String)object2);
            throw new ZipException(((StringBuilder)object).toString());
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("zip model is null, cannot determine file header for fileName: ");
        ((StringBuilder)object).append((String)object2);
        throw new ZipException(((StringBuilder)object).toString());
    }

    public static FileHeader getFileHeaderWithExactMatch(ZipModel object, String string2) throws ZipException {
        if (object != null) {
            if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
                if (((ZipModel)object).getCentralDirectory() != null) {
                    if (((ZipModel)object).getCentralDirectory().getFileHeaders() != null) {
                        if (((ZipModel)object).getCentralDirectory().getFileHeaders().size() <= 0) {
                            return null;
                        }
                        ArrayList arrayList = ((ZipModel)object).getCentralDirectory().getFileHeaders();
                        for (int i = 0; i < arrayList.size(); ++i) {
                            FileHeader fileHeader = (FileHeader)arrayList.get(i);
                            object = fileHeader.getFileName();
                            if (!Zip4jUtil.isStringNotNullAndNotEmpty((String)object) || !string2.equalsIgnoreCase((String)object)) continue;
                            return fileHeader;
                        }
                        return null;
                    }
                    object = new StringBuilder();
                    ((StringBuilder)object).append("file Headers are null, cannot determine file header with exact match for fileName: ");
                    ((StringBuilder)object).append(string2);
                    throw new ZipException(((StringBuilder)object).toString());
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("central directory is null, cannot determine file header with exact match for fileName: ");
                ((StringBuilder)object).append(string2);
                throw new ZipException(((StringBuilder)object).toString());
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("file name is null, cannot determine file header with exact match for fileName: ");
            ((StringBuilder)object).append(string2);
            throw new ZipException(((StringBuilder)object).toString());
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("zip model is null, cannot determine file header with exact match for fileName: ");
        ((StringBuilder)object).append(string2);
        object = new ZipException(((StringBuilder)object).toString());
        throw object;
    }

    public static long getFileLengh(File file) throws ZipException {
        if (file != null) {
            if (file.isDirectory()) {
                return -1L;
            }
            return file.length();
        }
        throw new ZipException("input file is null, cannot calculate file length");
    }

    public static long getFileLengh(String string2) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
            return Zip4jUtil.getFileLengh(new File(string2));
        }
        throw new ZipException("invalid file name");
    }

    public static String getFileNameFromFilePath(File file) throws ZipException {
        if (file != null) {
            if (file.isDirectory()) {
                return null;
            }
            return file.getName();
        }
        throw new ZipException("input file is null, cannot get file name");
    }

    public static ArrayList getFilesInDirectoryRec(File serializable, boolean bl) throws ZipException {
        if (serializable != null) {
            ArrayList<Serializable> arrayList = new ArrayList<Serializable>();
            List<File> list = Arrays.asList(((File)serializable).listFiles());
            if (!((File)serializable).canRead()) {
                return arrayList;
            }
            for (int i = 0; i < list.size(); ++i) {
                serializable = list.get(i);
                if (((File)serializable).isHidden() && !bl) {
                    return arrayList;
                }
                arrayList.add(serializable);
                if (!((File)serializable).isDirectory()) continue;
                arrayList.addAll(Zip4jUtil.getFilesInDirectoryRec((File)serializable, bl));
            }
            return arrayList;
        }
        serializable = new ZipException("input path is null, cannot read files in the directory");
        throw serializable;
    }

    public static int getIndexOfFileHeader(ZipModel object, FileHeader object2) throws ZipException {
        if (object != null && object2 != null) {
            if (((ZipModel)object).getCentralDirectory() != null) {
                if (((ZipModel)object).getCentralDirectory().getFileHeaders() != null) {
                    if (((ZipModel)object).getCentralDirectory().getFileHeaders().size() <= 0) {
                        return -1;
                    }
                    if (Zip4jUtil.isStringNotNullAndNotEmpty((String)(object2 = ((FileHeader)object2).getFileName()))) {
                        object = ((ZipModel)object).getCentralDirectory().getFileHeaders();
                        for (int i = 0; i < ((ArrayList)object).size(); ++i) {
                            String string2 = ((FileHeader)((ArrayList)object).get(i)).getFileName();
                            if (!Zip4jUtil.isStringNotNullAndNotEmpty(string2) || !((String)object2).equalsIgnoreCase(string2)) continue;
                            return i;
                        }
                        return -1;
                    }
                    throw new ZipException("file name in file header is empty or null, cannot determine index of file header");
                }
                throw new ZipException("file Headers are null, cannot determine index of file header");
            }
            throw new ZipException("central directory is null, ccannot determine index of file header");
        }
        object = new ZipException("input parameters is null, cannot determine index of file header");
        throw object;
    }

    public static long getLastModifiedFileTime(File file, TimeZone timeZone) throws ZipException {
        if (file != null) {
            if (file.exists()) {
                return file.lastModified();
            }
            throw new ZipException("input file does not exist, cannot read last modified file time");
        }
        throw new ZipException("input file is null, cannot read last modified file time");
    }

    public static String getRelativeFileName(String object, String string2, String object2) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty((String)object)) {
            if (Zip4jUtil.isStringNotNullAndNotEmpty((String)object2)) {
                CharSequence charSequence = new File((String)object2).getPath();
                object2 = charSequence;
                if (!((String)charSequence).endsWith(InternalZipConstants.FILE_SEPARATOR)) {
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append((String)charSequence);
                    ((StringBuilder)object2).append(InternalZipConstants.FILE_SEPARATOR);
                    object2 = ((StringBuilder)object2).toString();
                }
                charSequence = ((String)object).substring(((String)object2).length());
                object2 = charSequence;
                if (((String)charSequence).startsWith(System.getProperty("file.separator"))) {
                    object2 = ((String)charSequence).substring(1);
                }
                if (((File)(object = new File((String)object))).isDirectory()) {
                    object2 = ((String)object2).replaceAll("\\\\", "/");
                    object = new StringBuilder();
                    ((StringBuilder)object).append((String)object2);
                    ((StringBuilder)object).append("/");
                    object = ((StringBuilder)object).toString();
                } else {
                    object2 = ((String)object2).substring(0, ((String)object2).lastIndexOf(((File)object).getName())).replaceAll("\\\\", "/");
                    charSequence = new StringBuilder();
                    ((StringBuilder)charSequence).append((String)object2);
                    ((StringBuilder)charSequence).append(((File)object).getName());
                    object = ((StringBuilder)charSequence).toString();
                }
            } else {
                object2 = new File((String)object);
                if (((File)object2).isDirectory()) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append(((File)object2).getName());
                    ((StringBuilder)object).append("/");
                    object = ((StringBuilder)object).toString();
                } else {
                    object = Zip4jUtil.getFileNameFromFilePath(new File((String)object));
                }
            }
            object2 = object;
            if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
                object2 = new StringBuilder();
                ((StringBuilder)object2).append(string2);
                ((StringBuilder)object2).append((String)object);
                object2 = ((StringBuilder)object2).toString();
            }
            if (Zip4jUtil.isStringNotNullAndNotEmpty((String)object2)) {
                return object2;
            }
            throw new ZipException("Error determining file name");
        }
        throw new ZipException("input file path/name is empty, cannot calculate relative file name");
    }

    public static ArrayList getSplitZipFiles(ZipModel object) throws ZipException {
        if (object != null) {
            if (((ZipModel)object).getEndCentralDirRecord() == null) {
                return null;
            }
            ArrayList<String> arrayList = new ArrayList<String>();
            String string2 = ((ZipModel)object).getZipFile();
            String string3 = new File(string2).getName();
            if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
                if (!((ZipModel)object).isSplitArchive()) {
                    arrayList.add(string2);
                    return arrayList;
                }
                int n = ((ZipModel)object).getEndCentralDirRecord().getNoOfThisDisk();
                if (n == 0) {
                    arrayList.add(string2);
                    return arrayList;
                }
                for (int i = 0; i <= n; ++i) {
                    if (i == n) {
                        arrayList.add(((ZipModel)object).getZipFile());
                        continue;
                    }
                    String string4 = ".z0";
                    if (i > 9) {
                        string4 = ".z";
                    }
                    String string5 = string3.indexOf(".") >= 0 ? string2.substring(0, string2.lastIndexOf(".")) : string2;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(string5);
                    stringBuilder.append(string4);
                    stringBuilder.append(i + 1);
                    arrayList.add(stringBuilder.toString());
                }
                return arrayList;
            }
            throw new ZipException("cannot get split zip files: zipfile is null");
        }
        object = new ZipException("cannot get split zip files: zipmodel is null");
        throw object;
    }

    public static String getZipFileNameWithoutExt(String string2) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
            String string3 = string2;
            if (string2.indexOf(System.getProperty("file.separator")) >= 0) {
                string3 = string2.substring(string2.lastIndexOf(System.getProperty("file.separator")));
            }
            string2 = string3;
            if (string3.indexOf(".") > 0) {
                string2 = string3.substring(0, string3.lastIndexOf("."));
            }
            return string2;
        }
        throw new ZipException("zip file name is empty or null, cannot determine zip file name");
    }

    public static boolean isStringNotNullAndNotEmpty(String string2) {
        return string2 != null && string2.trim().length() > 0;
        {
        }
    }

    public static boolean isSupportedCharset(String string2) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(string2)) {
            try {
                new String("a".getBytes(), string2);
                return true;
            }
            catch (Exception exception) {
                throw new ZipException(exception);
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                return false;
            }
        }
        throw new ZipException("charset is null or empty, cannot check if it is supported");
    }

    public static boolean isWindows() {
        boolean bl = System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
        return bl;
    }

    public static long javaToDosTime(long l) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(l);
        int n = calendar.get(1);
        if (n < 1980) {
            return 0x210000L;
        }
        int n2 = calendar.get(2);
        int n3 = calendar.get(5);
        int n4 = calendar.get(11);
        int n5 = calendar.get(12);
        return calendar.get(13) >> 1 | (n - 1980 << 25 | n2 + 1 << 21 | n3 << 16 | n4 << 11 | n5 << 5);
    }

    public static void setFileArchive(File file) throws ZipException {
    }

    public static void setFileHidden(File file) throws ZipException {
    }

    public static void setFileReadOnly(File file) throws ZipException {
        if (file != null) {
            if (file.exists()) {
                file.setReadOnly();
            }
            return;
        }
        throw new ZipException("input file is null. cannot set read only file attribute");
    }

    public static void setFileSystemMode(File file) throws ZipException {
    }
}

