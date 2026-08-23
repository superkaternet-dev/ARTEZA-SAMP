/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Process
 *  android.os.StrictMode
 *  android.os.StrictMode$ThreadPolicy
 *  javax.annotation.Nullable
 */
package com.google.android.gms.common.util;

import android.os.Process;
import android.os.StrictMode;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.IOUtils;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import javax.annotation.Nullable;

public class ProcessUtils {
    @Nullable
    private static String zza = null;
    private static int zzb = 0;

    private ProcessUtils() {
    }

    /*
     * Loose catch block
     * WARNING - void declaration
     */
    public static String getMyProcessName() {
        if (zza == null) {
            int n;
            int n2 = n = zzb;
            if (n == 0) {
                zzb = n2 = Process.myPid();
            }
            CharSequence charSequence = null;
            Object object = null;
            String string2 = null;
            if (n2 <= 0) {
                charSequence = object;
            } else {
                block14: {
                    void var2_5;
                    block15: {
                        object = new StringBuilder(25);
                        ((StringBuilder)object).append("/proc/");
                        ((StringBuilder)object).append(n2);
                        ((StringBuilder)object).append("/cmdline");
                        String string3 = ((StringBuilder)object).toString();
                        StrictMode.ThreadPolicy threadPolicy = StrictMode.allowThreadDiskReads();
                        FileReader fileReader = new FileReader(string3);
                        object = new BufferedReader(fileReader);
                        StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
                        try {
                            string2 = ((BufferedReader)object).readLine();
                            Preconditions.checkNotNull(string2);
                            string2 = string2.trim();
                            charSequence = string2;
                            break block14;
                        }
                        catch (Throwable throwable) {
                            break block15;
                        }
                        catch (IOException iOException) {
                            break block14;
                        }
                        catch (Throwable throwable) {
                            try {
                                StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
                                throw throwable;
                            }
                            catch (Throwable throwable2) {
                                object = string2;
                            }
                        }
                    }
                    IOUtils.closeQuietly((Closeable)object);
                    throw var2_5;
                    {
                        catch (IOException iOException) {
                            object = null;
                        }
                    }
                }
                IOUtils.closeQuietly((Closeable)object);
            }
            zza = charSequence;
        }
        return zza;
    }
}

