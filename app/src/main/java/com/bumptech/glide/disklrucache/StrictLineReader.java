/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.disklrucache;

import com.bumptech.glide.disklrucache.Util;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

class StrictLineReader
implements Closeable {
    private static final byte CR = 13;
    private static final byte LF = 10;
    private byte[] buf;
    private final Charset charset;
    private int end;
    private final InputStream in;
    private int pos;

    public StrictLineReader(InputStream inputStream, int n, Charset charset) {
        if (inputStream != null && charset != null) {
            if (n >= 0) {
                if (charset.equals(Util.US_ASCII)) {
                    this.in = inputStream;
                    this.charset = charset;
                    this.buf = new byte[n];
                    return;
                }
                throw new IllegalArgumentException("Unsupported encoding");
            }
            throw new IllegalArgumentException("capacity <= 0");
        }
        throw new NullPointerException();
    }

    public StrictLineReader(InputStream inputStream, Charset charset) {
        this(inputStream, 8192, charset);
    }

    private void fillBuf() throws IOException {
        InputStream inputStream = this.in;
        byte[] byArray = this.buf;
        int n = inputStream.read(byArray, 0, byArray.length);
        if (n != -1) {
            this.pos = 0;
            this.end = n;
            return;
        }
        throw new EOFException();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void close() throws IOException {
        InputStream inputStream = this.in;
        synchronized (inputStream) {
            if (this.buf != null) {
                this.buf = null;
                this.in.close();
            }
            return;
        }
    }

    public boolean hasUnterminatedLine() {
        boolean bl = this.end == -1;
        return bl;
    }

    /*
     * Exception decompiling
     */
    public String readLine() throws IOException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 7[TRYBLOCK] [12 : 283->289)] java.lang.Throwable
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

