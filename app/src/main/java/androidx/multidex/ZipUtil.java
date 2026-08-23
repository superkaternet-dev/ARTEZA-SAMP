/*
 * Decompiled with CFR 0.152.
 */
package androidx.multidex;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.CRC32;
import java.util.zip.ZipException;

final class ZipUtil {
    private static final int BUFFER_SIZE = 16384;
    private static final int ENDHDR = 22;
    private static final int ENDSIG = 101010256;

    ZipUtil() {
    }

    static long computeCrcOfCentralDir(RandomAccessFile randomAccessFile, CentralDirectory object) throws IOException {
        CRC32 cRC32 = new CRC32();
        long l = ((CentralDirectory)object).size;
        randomAccessFile.seek(((CentralDirectory)object).offset);
        int n = (int)Math.min(16384L, l);
        object = new byte[16384];
        n = randomAccessFile.read((byte[])object, 0, n);
        while (n != -1) {
            cRC32.update((byte[])object, 0, n);
            if ((l -= (long)n) == 0L) break;
            n = randomAccessFile.read((byte[])object, 0, (int)Math.min(16384L, l));
        }
        return cRC32.getValue();
    }

    static CentralDirectory findCentralDirectory(RandomAccessFile object) throws IOException, ZipException {
        long l = ((RandomAccessFile)object).length() - 22L;
        if (l >= 0L) {
            long l2;
            long l3 = l2 = l - 65536L;
            if (l2 < 0L) {
                l3 = 0L;
            }
            int n = Integer.reverseBytes(101010256);
            do {
                ((RandomAccessFile)object).seek(l);
                if (((RandomAccessFile)object).readInt() != n) continue;
                ((RandomAccessFile)object).skipBytes(2);
                ((RandomAccessFile)object).skipBytes(2);
                ((RandomAccessFile)object).skipBytes(2);
                ((RandomAccessFile)object).skipBytes(2);
                CentralDirectory centralDirectory = new CentralDirectory();
                centralDirectory.size = (long)Integer.reverseBytes(((RandomAccessFile)object).readInt()) & 0xFFFFFFFFL;
                centralDirectory.offset = (long)Integer.reverseBytes(((RandomAccessFile)object).readInt()) & 0xFFFFFFFFL;
                return centralDirectory;
            } while (--l >= l3);
            throw new ZipException("End Of Central Directory signature not found");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("File too short to be a zip file: ");
        stringBuilder.append(((RandomAccessFile)object).length());
        object = new ZipException(stringBuilder.toString());
        throw object;
    }

    static long getZipCrc(File file) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        try {
            long l = ZipUtil.computeCrcOfCentralDir(randomAccessFile, ZipUtil.findCentralDirectory(randomAccessFile));
            return l;
        }
        finally {
            randomAccessFile.close();
        }
    }

    static class CentralDirectory {
        long offset;
        long size;

        CentralDirectory() {
        }
    }
}

