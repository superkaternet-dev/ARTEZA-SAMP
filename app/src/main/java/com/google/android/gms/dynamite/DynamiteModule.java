/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.database.Cursor
 *  android.net.Uri$Builder
 *  android.os.Build$VERSION
 *  android.os.IBinder
 *  android.os.RemoteException
 *  android.util.Log
 *  dalvik.system.DelegateLastClassLoader
 */
package com.google.android.gms.dynamite;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.CrashUtils;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamite.zzb;
import com.google.android.gms.dynamite.zzc;
import com.google.android.gms.dynamite.zzd;
import com.google.android.gms.dynamite.zze;
import com.google.android.gms.dynamite.zzf;
import com.google.android.gms.dynamite.zzg;
import com.google.android.gms.dynamite.zzh;
import com.google.android.gms.dynamite.zzi;
import com.google.android.gms.dynamite.zzj;
import com.google.android.gms.dynamite.zzk;
import com.google.android.gms.dynamite.zzl;
import com.google.android.gms.dynamite.zzn;
import com.google.android.gms.dynamite.zzp;
import com.google.android.gms.dynamite.zzq;
import com.google.android.gms.dynamite.zzr;
import dalvik.system.DelegateLastClassLoader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public final class DynamiteModule {
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION;
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING;
    public static final VersionPolicy PREFER_HIGHEST_OR_REMOTE_VERSION;
    public static final VersionPolicy PREFER_LOCAL;
    public static final VersionPolicy PREFER_REMOTE;
    public static final VersionPolicy PREFER_REMOTE_VERSION_NO_FORCE_STAGING;
    public static final VersionPolicy zza;
    private static Boolean zzb;
    private static String zzc;
    private static boolean zzd;
    private static int zze;
    private static final ThreadLocal<zzn> zzf;
    private static final ThreadLocal<Long> zzg;
    private static final VersionPolicy.IVersions zzh;
    private static zzq zzj;
    private static zzr zzk;
    private final Context zzi;

    static {
        zze = -1;
        zzf = new ThreadLocal();
        zzg = new zzd();
        zzh = new zze();
        PREFER_REMOTE = new zzf();
        PREFER_LOCAL = new zzg();
        PREFER_REMOTE_VERSION_NO_FORCE_STAGING = new zzh();
        PREFER_HIGHEST_OR_LOCAL_VERSION = new zzi();
        PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING = new zzj();
        PREFER_HIGHEST_OR_REMOTE_VERSION = new zzk();
        zza = new zzl();
    }

    private DynamiteModule(Context context) {
        Preconditions.checkNotNull(context);
        this.zzi = context;
    }

    public static int getLocalVersion(Context object, String string2) {
        Object object2;
        int n;
        block4: {
            object = object.getApplicationContext().getClassLoader();
            n = String.valueOf(string2).length();
            object2 = new StringBuilder(n + 61);
            ((StringBuilder)object2).append("com.google.android.gms.dynamite.descriptors.");
            ((StringBuilder)object2).append(string2);
            ((StringBuilder)object2).append(".");
            ((StringBuilder)object2).append("ModuleDescriptor");
            object2 = ((ClassLoader)object).loadClass(((StringBuilder)object2).toString());
            object = ((Class)object2).getDeclaredField("MODULE_ID");
            object2 = ((Class)object2).getDeclaredField("MODULE_VERSION");
            if (Objects.equal(((Field)object).get(null), string2)) break block4;
            object = String.valueOf(((Field)object).get(null));
            n = String.valueOf(object).length();
            int n2 = String.valueOf(string2).length();
            object2 = new StringBuilder(n + 51 + n2);
            ((StringBuilder)object2).append("Module descriptor id '");
            ((StringBuilder)object2).append((String)object);
            ((StringBuilder)object2).append("' didn't match expected id '");
            ((StringBuilder)object2).append(string2);
            ((StringBuilder)object2).append("'");
            Log.e((String)"DynamiteModule", (String)((StringBuilder)object2).toString());
            return 0;
        }
        try {
            n = ((Field)object2).getInt(null);
            return n;
        }
        catch (Exception exception) {
            String string3 = String.valueOf(exception.getMessage());
            string3 = string3.length() != 0 ? "Failed to load module descriptor class: ".concat(string3) : new String("Failed to load module descriptor class: ");
            Log.e((String)"DynamiteModule", (String)string3);
        }
        catch (ClassNotFoundException classNotFoundException) {
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(string2).length() + 45);
            stringBuilder.append("Local module descriptor class for ");
            stringBuilder.append(string2);
            stringBuilder.append(" not found.");
            Log.w((String)"DynamiteModule", (String)stringBuilder.toString());
        }
        return 0;
    }

    public static int getRemoteVersion(Context context, String string2) {
        return DynamiteModule.zza(context, string2, false);
    }

    /*
     * Exception decompiling
     */
    public static DynamiteModule load(Context var0, VersionPolicy var1_2, String var2_3) throws LoadingException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 16[TRYBLOCK] [37 : 606->609)] java.lang.Throwable
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

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static int zza(Context var0, String var1_1, boolean var2_5) {
        block55: {
            block54: {
                block53: {
                    try {
                        synchronized (DynamiteModule.class) {
                            var10_6 = DynamiteModule.zzb;
                            var7_7 = null;
                            var8_8 = null;
                            var9_9 = null;
                            var6_10 /* !! */  = var10_6;
                            if (var10_6 != null) ** break block49
                        }
                    }
                    catch (Throwable var1_4) {
                        CrashUtils.addDynamiteErrorToDropBox(var0, var1_4);
                        throw var1_4;
                    }
                    {
                        block58: {
                            block50: {
                                try {
                                    var11_23 = var0.getApplicationContext().getClassLoader().loadClass(DynamiteLoaderClassLoader.class.getName()).getDeclaredField("sClassLoader");
                                    var10_6 = var11_23.getDeclaringClass();
                                    synchronized (var10_6) {
                                    }
                                }
                                catch (NoSuchFieldException var6_13) {
                                    break block50;
                                }
                                catch (IllegalAccessException var6_14) {
                                    break block50;
                                }
                                catch (ClassNotFoundException var6_15) {
                                    // empty catch block
                                    break block50;
                                }
                                {
                                    block52: {
                                        block57: {
                                            var6_10 /* !! */  = (ClassLoader)var11_23.get(null);
                                            if (var6_10 /* !! */  == null) break block57;
                                            if (var6_10 /* !! */  == ClassLoader.getSystemClassLoader()) {
                                                var6_10 /* !! */  = Boolean.FALSE;
                                                break block52;
                                            } else {
                                                try {
                                                    DynamiteModule.zzd((ClassLoader)var6_10 /* !! */ );
                                                }
                                                catch (LoadingException var6_11) {
                                                    // empty catch block
                                                }
                                                var6_10 /* !! */  = Boolean.TRUE;
                                            }
                                            break block52;
                                        }
                                        if (!DynamiteModule.zzd && !(var5_24 = Boolean.TRUE.equals(null))) {
                                            block51: {
                                                try {
                                                    var3_25 = DynamiteModule.zzb(var0, var1_1, var2_5);
                                                    var6_10 /* !! */  = DynamiteModule.zzc;
                                                    if (var6_10 /* !! */  == null) return var3_25;
                                                    if (var6_10 /* !! */ .isEmpty()) break block51;
                                                    var6_10 /* !! */  = com.google.android.gms.dynamite.zzb.zza();
                                                    if (var6_10 /* !! */  == null) {
                                                        if (Build.VERSION.SDK_INT >= 29) {
                                                            var6_10 /* !! */  = DynamiteModule.zzc;
                                                            Preconditions.checkNotNull(var6_10 /* !! */ );
                                                            var6_10 /* !! */  = new DelegateLastClassLoader((String)var6_10 /* !! */ , ClassLoader.getSystemClassLoader());
                                                        } else {
                                                            var6_10 /* !! */  = DynamiteModule.zzc;
                                                            Preconditions.checkNotNull(var6_10 /* !! */ );
                                                            var6_10 /* !! */  = new zzc((String)var6_10 /* !! */ , ClassLoader.getSystemClassLoader());
                                                        }
                                                    }
                                                    DynamiteModule.zzd((ClassLoader)var6_10 /* !! */ );
                                                    var11_23.set(null, var6_10 /* !! */ );
                                                    DynamiteModule.zzb = Boolean.TRUE;
                                                }
                                                catch (LoadingException var6_12) {
                                                    var11_23.set(null, ClassLoader.getSystemClassLoader());
                                                    var6_10 /* !! */  = Boolean.FALSE;
                                                    break block52;
                                                }
                                                return var3_25;
                                            }
                                            return var3_25;
                                        }
                                        var11_23.set(null, ClassLoader.getSystemClassLoader());
                                        var6_10 /* !! */  = Boolean.FALSE;
                                    }
                                    break block58;
                                }
                            }
                            var10_6 = var6_10 /* !! */ .toString();
                            var3_26 = var10_6.length();
                            var6_10 /* !! */  = new StringBuilder(var3_26 + 30);
                            var6_10 /* !! */ .append("Failed to load module via V2: ");
                            var6_10 /* !! */ .append((String)var10_6);
                            Log.w((String)"DynamiteModule", (String)var6_10 /* !! */ .toString());
                            var6_10 /* !! */  = Boolean.FALSE;
                        }
                        DynamiteModule.zzb = var6_10 /* !! */ ;
                    }
                    {
                        var5_24 = var6_10 /* !! */ .booleanValue();
                        var4_27 = 0;
                        if (var5_24) {
                            try {
                                return DynamiteModule.zzb(var0, var1_1, var2_5);
                            }
                            catch (LoadingException var1_2) {
                                var1_3 = String.valueOf(var1_2.getMessage());
                                var1_3 = var1_3.length() != 0 ? "Failed to retrieve remote module version: ".concat(var1_3) : new String("Failed to retrieve remote module version: ");
                                Log.w((String)"DynamiteModule", (String)var1_3);
                                return 0;
                            }
                        }
                        var6_10 /* !! */  = DynamiteModule.zzf(var0);
                        if (var6_10 /* !! */  != null) break block53;
                        return var4_27;
                    }
                }
                try {
                    var3_26 = var6_10 /* !! */ .zze();
                    if (var3_26 < 3) break block54;
                    var10_6 = DynamiteModule.zzf.get();
                    if (var10_6 != null && (var10_6 = var10_6.zza) != null) {
                        return var10_6.getInt(0);
                    }
                    var1_1 = (Cursor)ObjectWrapper.unwrap(var6_10 /* !! */ .zzk(ObjectWrapper.wrap(var0), var1_1, var2_5, DynamiteModule.zzg.get()));
                    if (var1_1 != null) {
                    }
                    ** GOTO lbl-1000
                }
                catch (Throwable var6_18) {
                    var1_1 = var8_8;
                    ** GOTO lbl159
                }
                catch (RemoteException var6_19) {
                    var1_1 = var7_7;
                    break block55;
                }
                try {
                    if (var1_1.moveToFirst()) {
                        var3_26 = var1_1.getInt(0);
                        if (var3_26 > 0 && (var2_5 = DynamiteModule.zze((Cursor)var1_1))) {
                            var1_1 = var9_9;
                        }
                        if (var1_1 == null) return var3_26;
                    }
                    ** GOTO lbl-1000
                }
                catch (Throwable var6_16) {
                    ** GOTO lbl159
                }
                catch (RemoteException var6_17) {
                    break block55;
                }
                {
                    var1_1.close();
                    return var3_26;
                }
lbl-1000:
                // 2 sources

                {
                    Log.w((String)"DynamiteModule", (String)"Failed to retrieve remote module version.");
                    var3_26 = var4_27;
                    if (var1_1 == null) return var3_26;
                }
                {
                    var1_1.close();
                    return var4_27;
                }
            }
            if (var3_26 != 2) ** GOTO lbl142
            {
                Log.w((String)"DynamiteModule", (String)"IDynamite loader version = 2, no high precision latency measurement.");
                return var6_10 /* !! */ .zzg(ObjectWrapper.wrap(var0), var1_1, var2_5);
lbl142:
                // 1 sources

                Log.w((String)"DynamiteModule", (String)"IDynamite loader version < 2, falling back to getModuleVersion2");
                return var6_10 /* !! */ .zzf(ObjectWrapper.wrap(var0), var1_1, var2_5);
            }
        }
        var6_20 = String.valueOf(var6_20.getMessage());
        var6_20 = var6_20.length() != 0 ? "Failed to retrieve remote module version: ".concat(var6_20) : new String("Failed to retrieve remote module version: ");
        Log.w((String)"DynamiteModule", (String)var6_20);
        var3_26 = var4_27;
        if (var1_1 == null) return var3_26;
        {
            block56: {
                catch (Throwable var6_21) {
                    // empty catch block
                    break block56;
                }
                var1_1.close();
                return var4_27;
            }
            if (var1_1 == null) throw var6_22;
            var1_1.close();
            throw var6_22;
        }
    }

    /*
     * Loose catch block
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private static int zzb(Context object, String object2, boolean bl) throws LoadingException {
        void var1_10;
        block29: {
            LoadingException loadingException;
            block26: {
                int n;
                boolean bl2;
                block28: {
                    block27: {
                        Uri.Builder builder;
                        loadingException = null;
                        long l = zzg.get();
                        ContentResolver contentResolver = object.getContentResolver();
                        object = "api_force_staging";
                        bl2 = true;
                        if (!bl) {
                            object = "api";
                        }
                        if ((object = contentResolver.query((builder = new Uri.Builder()).scheme("content").authority("com.google.android.gms.chimera").path((String)object).appendPath((String)object2).appendQueryParameter("requestStartTime", String.valueOf(l)).build(), null, null, null, null)) == null) break block26;
                        if (!object.moveToFirst()) break block26;
                        bl = false;
                        boolean bl3 = false;
                        n = object.getInt(0);
                        if (n <= 0) break block27;
                        // MONITORENTER : com.google.android.gms.dynamite.DynamiteModule.class
                        zzc = object.getString(2);
                        int n2 = object.getColumnIndex("loaderVersion");
                        if (n2 >= 0) {
                            zze = object.getInt(n2);
                        }
                        if ((n2 = object.getColumnIndex("disableStandaloneDynamiteLoader")) >= 0) {
                            bl = object.getInt(n2) != 0 ? bl2 : false;
                            zzd = bl;
                        } else {
                            bl = bl3;
                        }
                        // MONITOREXIT : com.google.android.gms.dynamite.DynamiteModule.class
                        bl3 = DynamiteModule.zze((Cursor)object);
                        bl2 = bl;
                        if (bl3) {
                            object = null;
                            bl2 = bl;
                        }
                        break block28;
                    }
                    bl2 = bl;
                }
                if (!bl2) {
                    if (object == null) return n;
                    object.close();
                    return n;
                }
                try {
                    object2 = new LoadingException("forcing fallback to container DynamiteLoader impl", null);
                    throw object2;
                }
                catch (Exception exception) {}
                finally {
                    break block29;
                }
            }
            try {
                Log.w((String)"DynamiteModule", (String)"Failed to retrieve remote module version.");
                object2 = new LoadingException("Failed to connect to dynamite module ContentResolver.", null);
                throw object2;
            }
            catch (Throwable throwable) {}
            catch (Exception exception) {}
            finally {
                break block29;
            }
            catch (Throwable throwable) {
                object = loadingException;
                break block29;
            }
            catch (Exception exception) {
                object = null;
            }
            try {
                void var1_8;
                if (var1_8 instanceof LoadingException) {
                    throw var1_8;
                }
                loadingException = new LoadingException("V2 version check failed", (Throwable)var1_8, null);
                throw loadingException;
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        if (object == null) throw var1_10;
        object.close();
        throw var1_10;
    }

    private static DynamiteModule zzc(Context context, String string2) {
        string2 = (string2 = String.valueOf(string2)).length() != 0 ? "Selected local version of ".concat(string2) : new String("Selected local version of ");
        Log.i((String)"DynamiteModule", (String)string2);
        return new DynamiteModule(context.getApplicationContext());
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void zzd(ClassLoader object) throws LoadingException {
        void var0_6;
        try {
            IBinder iBinder = (IBinder)((ClassLoader)object).loadClass("com.google.android.gms.dynamiteloader.DynamiteLoaderV2").getConstructor(new Class[0]).newInstance(new Object[0]);
            object = iBinder == null ? null : ((object = iBinder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoaderV2")) instanceof zzr ? (zzr)object : new zzr(iBinder));
            zzk = object;
            return;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new LoadingException("Failed to instantiate dynamite loader", (Throwable)var0_6, null);
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new LoadingException("Failed to instantiate dynamite loader", (Throwable)var0_6, null);
        }
        catch (InstantiationException instantiationException) {
            throw new LoadingException("Failed to instantiate dynamite loader", (Throwable)var0_6, null);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new LoadingException("Failed to instantiate dynamite loader", (Throwable)var0_6, null);
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        throw new LoadingException("Failed to instantiate dynamite loader", (Throwable)var0_6, null);
    }

    private static boolean zze(Cursor cursor) {
        zzn zzn2 = zzf.get();
        if (zzn2 != null && zzn2.zza == null) {
            zzn2.zza = cursor;
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static zzq zzf(Context object) {
        synchronized (DynamiteModule.class) {
            block6: {
                zzq zzq2 = zzj;
                if (zzq2 != null) {
                    return zzq2;
                }
                try {
                    object = (IBinder)object.createPackageContext("com.google.android.gms", 3).getClassLoader().loadClass("com.google.android.gms.chimera.container.DynamiteLoaderImpl").newInstance();
                    if (object == null) {
                        return null;
                    }
                    zzq2 = object.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoader");
                    object = zzq2 instanceof zzq ? zzq2 : new zzq((IBinder)object);
                    if (object == null) return null;
                    zzj = object;
                }
                catch (Exception exception) {
                    String string2 = String.valueOf(exception.getMessage());
                    string2 = string2.length() != 0 ? "Failed to load IDynamiteLoader from GmsCore: ".concat(string2) : new String("Failed to load IDynamiteLoader from GmsCore: ");
                    Log.e((String)"DynamiteModule", (String)string2);
                    break block6;
                }
                return object;
            }
            return null;
        }
    }

    public Context getModuleContext() {
        return this.zzi;
    }

    /*
     * WARNING - void declaration
     */
    public IBinder instantiate(String string2) throws LoadingException {
        void var2_6;
        try {
            IBinder iBinder = (IBinder)this.zzi.getClassLoader().loadClass(string2).newInstance();
            return iBinder;
        }
        catch (IllegalAccessException illegalAccessException) {
        }
        catch (InstantiationException instantiationException) {
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        string2 = String.valueOf(string2);
        string2 = string2.length() != 0 ? "Failed to instantiate module class: ".concat(string2) : new String("Failed to instantiate module class: ");
        throw new LoadingException(string2, (Throwable)var2_6, null);
    }

    public static class DynamiteLoaderClassLoader {
        public static ClassLoader sClassLoader;
    }

    public static class LoadingException
    extends Exception {
        /* synthetic */ LoadingException(String string2, zzp zzp2) {
            super(string2);
        }

        /* synthetic */ LoadingException(String string2, Throwable throwable, zzp zzp2) {
            super(string2, throwable);
        }
    }

    public static interface VersionPolicy {
        public SelectionResult selectModule(Context var1, String var2, IVersions var3) throws LoadingException;

        public static interface IVersions {
            public int zza(Context var1, String var2);

            public int zzb(Context var1, String var2, boolean var3) throws LoadingException;
        }

        public static class SelectionResult {
            public int localVersion = 0;
            public int remoteVersion = 0;
            public int selection = 0;
        }
    }
}

