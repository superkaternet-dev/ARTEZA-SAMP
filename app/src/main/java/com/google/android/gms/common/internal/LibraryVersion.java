/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.internal;

import com.google.android.gms.common.internal.GmsLogger;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.IOUtils;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class LibraryVersion {
    private static final GmsLogger zza = new GmsLogger("LibraryVersion", "");
    private static LibraryVersion zzb = new LibraryVersion();
    private ConcurrentHashMap<String, String> zzc = new ConcurrentHashMap();

    protected LibraryVersion() {
    }

    public static LibraryVersion getInstance() {
        return zzb;
    }

    /*
     * Loose catch block
     * WARNING - void declaration
     */
    public String getVersion(String string2) {
        void var1_5;
        Object object;
        block32: {
            Object object2;
            block35: {
                block34: {
                    Object object3;
                    Object object4;
                    Object object5;
                    block33: {
                        block29: {
                            Object object6;
                            block31: {
                                block30: {
                                    block28: {
                                        Preconditions.checkNotEmpty(string2, "Please provide a valid libraryName");
                                        if (this.zzc.containsKey(string2)) {
                                            return this.zzc.get(string2);
                                        }
                                        object6 = new Properties();
                                        object5 = null;
                                        object = null;
                                        object2 = null;
                                        object4 = null;
                                        object3 = LibraryVersion.class.getResourceAsStream(String.format("/%s.properties", string2));
                                        if (object3 == null) break block28;
                                        object = object5;
                                        ((Properties)object6).load((InputStream)object3);
                                        object = object5;
                                        object = object2 = ((Properties)object6).getProperty("version", null);
                                        object4 = zza;
                                        object = object2;
                                        int n = String.valueOf(string2).length();
                                        object = object2;
                                        int n2 = String.valueOf(object2).length();
                                        object = object2;
                                        object = object2;
                                        object5 = new StringBuilder(n + 12 + n2);
                                        object = object2;
                                        ((StringBuilder)object5).append(string2);
                                        object = object2;
                                        ((StringBuilder)object5).append(" version is ");
                                        object = object2;
                                        ((StringBuilder)object5).append((String)object2);
                                        object = object2;
                                        ((GmsLogger)object4).v("LibraryVersion", ((StringBuilder)object5).toString());
                                        break block29;
                                    }
                                    object = object5;
                                    object6 = zza;
                                    object = object5;
                                    object2 = String.valueOf(string2);
                                    object = object5;
                                    if (((String)object2).length() == 0) break block30;
                                    object = object5;
                                    object2 = "Failed to get app version for libraryName: ".concat((String)object2);
                                    break block31;
                                }
                                object = object5;
                                object2 = new String("Failed to get app version for libraryName: ");
                            }
                            object = object5;
                            try {
                                ((GmsLogger)object6).w("LibraryVersion", (String)object2);
                                object2 = object4;
                            }
                            catch (Throwable throwable) {
                                object = object3;
                                break block32;
                            }
                            catch (IOException iOException) {
                                object4 = object3;
                                object3 = iOException;
                                object2 = object;
                                object = object4;
                                break block33;
                            }
                        }
                        object = object2;
                        if (object3 != null) {
                            IOUtils.closeQuietly((Closeable)object3);
                            object = object2;
                        }
                        break block35;
                        catch (Throwable throwable) {
                            object = object2;
                            break block32;
                        }
                        catch (IOException iOException) {
                            object2 = null;
                        }
                    }
                    try {
                        object5 = zza;
                        object4 = String.valueOf(string2);
                        object4 = ((String)object4).length() != 0 ? "Failed to get app version for libraryName: ".concat((String)object4) : new String("Failed to get app version for libraryName: ");
                        ((GmsLogger)object5).e("LibraryVersion", (String)object4, (Throwable)object3);
                        if (object == null) break block34;
                    }
                    catch (Throwable throwable) {
                        // empty catch block
                    }
                    IOUtils.closeQuietly((Closeable)object);
                }
                object = object2;
            }
            object2 = object;
            if (object == null) {
                zza.d("LibraryVersion", ".properties file is dropped during release process. Failure to read app version is expected during Google internal testing where locally-built libraries are used");
                object2 = "UNKNOWN";
            }
            this.zzc.put(string2, (String)object2);
            return object2;
        }
        if (object != null) {
            IOUtils.closeQuietly(object);
        }
        throw var1_5;
    }
}

