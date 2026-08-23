/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.google.android.gms.common.config;

import android.util.Log;
import com.google.android.gms.common.config.zza;
import com.google.android.gms.common.config.zzb;
import com.google.android.gms.common.config.zzc;
import com.google.android.gms.common.config.zzd;
import com.google.android.gms.common.config.zze;

public abstract class GservicesValue<T> {
    private static final Object zzc = new Object();
    protected final String zza;
    protected final T zzb;
    private T zzd = null;

    protected GservicesValue(String string2, T t) {
        this.zza = string2;
        this.zzb = t;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public static boolean isInitialized() {
        Object object = zzc;
        // MONITORENTER : object
        // MONITOREXIT : object
        return false;
    }

    public static GservicesValue<Float> value(String string2, Float f) {
        return new zzd(string2, f);
    }

    public static GservicesValue<Integer> value(String string2, Integer n) {
        return new zzc(string2, n);
    }

    public static GservicesValue<Long> value(String string2, Long l) {
        return new zzb(string2, l);
    }

    public static GservicesValue<String> value(String string2, String string3) {
        return new zze(string2, string3);
    }

    public static GservicesValue<Boolean> value(String string2, boolean bl) {
        return new zza(string2, bl);
    }

    /*
     * Exception decompiling
     */
    public final T get() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 3[TRYBLOCK] [4 : 56->60)] java.lang.Throwable
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

    @Deprecated
    public final T getBinderSafe() {
        return this.get();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void override(T object) {
        Log.w((String)"GservicesValue", (String)"GservicesValue.override(): test should probably call initForTests() first");
        this.zzd = object;
        object = zzc;
        // MONITORENTER : object
        // MONITORENTER : object
        // MONITOREXIT : object
        // MONITOREXIT : object
    }

    public void resetOverride() {
        this.zzd = null;
    }

    protected abstract T zza(String var1);
}

