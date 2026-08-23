/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.net.Uri
 *  android.os.CancellationSignal
 *  android.os.Process
 *  android.os.StrictMode
 *  android.os.StrictMode$ThreadPolicy
 *  android.util.Log
 */
package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Process;
import android.os.StrictMode;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TypefaceCompatUtil {
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";

    private TypefaceCompatUtil() {
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    public static ByteBuffer copyToDirectBuffer(Context object, Resources object2, int n) {
        block5: {
            if ((object = TypefaceCompatUtil.getTempFile((Context)object)) == null) {
                return null;
            }
            boolean bl = TypefaceCompatUtil.copyToFile((File)object, object2, n);
            if (bl) break block5;
            ((File)object).delete();
            return null;
        }
        try {
            object2 = TypefaceCompatUtil.mmap((File)object);
            return object2;
        }
        finally {
            ((File)object).delete();
        }
    }

    public static boolean copyToFile(File file, Resources object, int n) {
        Object object2 = null;
        try {
            object2 = object = object.openRawResource(n);
        }
        catch (Throwable throwable) {
            TypefaceCompatUtil.closeQuietly(object2);
            throw throwable;
        }
        boolean bl = TypefaceCompatUtil.copyToFile(file, (InputStream)object);
        TypefaceCompatUtil.closeQuietly((Closeable)object);
        return bl;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static boolean copyToFile(File object, InputStream object2) {
        Throwable throwable2222222;
        Object object3;
        StrictMode.ThreadPolicy threadPolicy;
        block6: {
            Object object4 = null;
            Object object5 = null;
            threadPolicy = StrictMode.allowThreadDiskWrites();
            object3 = object5;
            Object object6 = object4;
            object3 = object5;
            object6 = object4;
            FileOutputStream fileOutputStream = new FileOutputStream((File)object, false);
            object3 = object = fileOutputStream;
            object6 = object;
            object4 = new byte[1024];
            while (true) {
                object3 = object;
                object6 = object;
                int n = ((InputStream)object2).read((byte[])object4);
                if (n != -1) {
                    object3 = object;
                    object6 = object;
                    ((FileOutputStream)object).write((byte[])object4, 0, n);
                    continue;
                }
                break;
            }
            {
                catch (Throwable throwable2222222) {
                    break block6;
                }
                catch (IOException iOException) {}
                object3 = object6;
                {
                    object3 = object6;
                    object2 = new StringBuilder();
                    object3 = object6;
                    ((StringBuilder)object2).append("Error copying resource contents to temp file: ");
                    object3 = object6;
                    ((StringBuilder)object2).append(iOException.getMessage());
                    object3 = object6;
                    Log.e((String)TAG, (String)((StringBuilder)object2).toString());
                }
                TypefaceCompatUtil.closeQuietly((Closeable)object6);
                StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
                return false;
            }
            TypefaceCompatUtil.closeQuietly((Closeable)object);
            StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
            return true;
        }
        TypefaceCompatUtil.closeQuietly(object3);
        StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
        throw throwable2222222;
    }

    public static File getTempFile(Context object) {
        if ((object = object.getCacheDir()) == null) {
            return null;
        }
        CharSequence charSequence = new StringBuilder();
        charSequence.append(CACHE_FILE_PREFIX);
        charSequence.append(Process.myPid());
        charSequence.append("-");
        charSequence.append(Process.myTid());
        charSequence.append("-");
        charSequence = charSequence.toString();
        for (int i = 0; i < 100; ++i) {
            Comparable<StringBuilder> comparable = new StringBuilder();
            ((StringBuilder)comparable).append((String)charSequence);
            ((StringBuilder)comparable).append(i);
            comparable = new File((File)object, ((StringBuilder)comparable).toString());
            try {
                boolean bl = ((File)comparable).createNewFile();
                if (!bl) continue;
                return comparable;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        return null;
    }

    /*
     * Exception decompiling
     */
    public static ByteBuffer mmap(Context var0, CancellationSignal var1_3, Uri var2_6) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
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

    /*
     * Loose catch block
     */
    private static ByteBuffer mmap(File object) {
        FileInputStream fileInputStream = new FileInputStream((File)object);
        {
            catch (IOException iOException) {
                return null;
            }
        }
        object = fileInputStream.getChannel();
        long l = ((FileChannel)object).size();
        object = ((FileChannel)object).map(FileChannel.MapMode.READ_ONLY, 0L, l);
        fileInputStream.close();
        return object;
        catch (Throwable throwable) {
            try {
                fileInputStream.close();
            }
            catch (Throwable throwable2) {
                // empty catch block
            }
            throw throwable;
        }
    }
}

