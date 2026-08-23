/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.CheckForNull
 */
package com.google.android.gms.internal.common;

import com.google.android.gms.internal.common.zzac;
import com.google.android.gms.internal.common.zzad;
import com.google.android.gms.internal.common.zzae;
import com.google.android.gms.internal.common.zzaf;
import com.google.android.gms.internal.common.zzah;
import com.google.android.gms.internal.common.zzai;
import com.google.android.gms.internal.common.zzaj;
import com.google.android.gms.internal.common.zzak;
import com.google.android.gms.internal.common.zzr;
import com.google.android.gms.internal.common.zzs;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javax.annotation.CheckForNull;

public abstract class zzag<E>
extends zzac<E>
implements List<E>,
RandomAccess {
    private static final zzak<Object> zza = new zzae<Object>(zzai.zza, 0);

    zzag() {
    }

    static <E> zzag<E> zzi(Object[] objectArray, int n) {
        if (n == 0) {
            return zzai.zza;
        }
        return new zzai(objectArray, n);
    }

    public static <E> zzag<E> zzj(Iterable<? extends E> zzag2) {
        if (zzag2 != null) {
            if (zzag2 instanceof Collection) {
                zzag2 = zzag.zzk((Collection)zzag2);
            } else if (!(zzag2 = zzag2.iterator()).hasNext()) {
                zzag2 = zzai.zza;
            } else {
                Object e = zzag2.next();
                if (!zzag2.hasNext()) {
                    zzag2 = zzag.zzm(e);
                } else {
                    zzad<E> zzad2 = new zzad<E>(4);
                    zzad2.zzb(e);
                    zzad2.zzc((Iterator<E>)((Object)zzag2));
                    zzad2.zzc = true;
                    zzag2 = zzag.zzi(zzad2.zza, zzad2.zzb);
                }
            }
            return zzag2;
        }
        throw null;
    }

    public static <E> zzag<E> zzk(Collection<? extends E> object) {
        if (object instanceof zzac) {
            zzag zzag2 = ((zzac)object).zzd();
            object = zzag2;
            if (zzag2.zzf()) {
                object = zzag2.toArray();
                object = zzag.zzi(object, ((Object[])object).length);
            }
            return object;
        }
        object = object.toArray();
        int n = ((Object[])object).length;
        zzah.zza(object, n);
        return zzag.zzi(object, n);
    }

    public static <E> zzag<E> zzl() {
        return zzai.zza;
    }

    public static <E> zzag<E> zzm(E e) {
        Object[] objectArray = new Object[]{e};
        zzah.zza(objectArray, 1);
        return zzag.zzi(objectArray, 1);
    }

    public static <E> zzag<E> zzn(E e, E e2) {
        Object[] objectArray = new Object[]{e, e2};
        zzah.zza(objectArray, 2);
        return zzag.zzi(objectArray, 2);
    }

    @Override
    @Deprecated
    public final void add(int n, E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final boolean addAll(int n, Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean contains(@CheckForNull Object object) {
        return this.indexOf(object) >= 0;
    }

    @Override
    public final boolean equals(@CheckForNull Object iterator2) {
        boolean bl;
        block12: {
            bl = true;
            if (iterator2 != this) {
                if (!(iterator2 instanceof List)) {
                    bl = false;
                } else {
                    Object object = (List)((Object)iterator2);
                    int n = this.size();
                    if (n != object.size()) {
                        bl = false;
                    } else if (object instanceof RandomAccess) {
                        for (int i = 0; i < n; ++i) {
                            if (zzr.zza(this.get(i), object.get(i))) continue;
                            bl = false;
                            break;
                        }
                    } else {
                        iterator2 = this.iterator();
                        object = object.iterator();
                        while (iterator2.hasNext()) {
                            if (!object.hasNext()) {
                                bl = false;
                            } else {
                                if (zzr.zza(iterator2.next(), object.next())) continue;
                                bl = false;
                            }
                            break block12;
                        }
                        if (object.hasNext()) {
                            bl = false;
                        }
                    }
                }
            }
        }
        return bl;
    }

    @Override
    public final int hashCode() {
        int n = this.size();
        int n2 = 1;
        for (int i = 0; i < n; ++i) {
            n2 = n2 * 31 + this.get(i).hashCode();
        }
        return n2;
    }

    @Override
    public final int indexOf(@CheckForNull Object object) {
        int n;
        int n2 = -1;
        if (object == null) {
            return -1;
        }
        int n3 = this.size();
        int n4 = 0;
        while (true) {
            n = n2;
            if (n4 >= n3) break;
            if (object.equals(this.get(n4))) {
                n = n4;
                break;
            }
            ++n4;
        }
        return n;
    }

    @Override
    public final /* synthetic */ Iterator iterator() {
        return this.zzo(0);
    }

    @Override
    public final int lastIndexOf(@CheckForNull Object object) {
        int n;
        int n2 = -1;
        if (object == null) {
            return -1;
        }
        int n3 = this.size() - 1;
        while (true) {
            n = n2;
            if (n3 < 0) break;
            if (object.equals(this.get(n3))) {
                n = n3;
                break;
            }
            --n3;
        }
        return n;
    }

    @Override
    public final /* synthetic */ ListIterator listIterator() {
        return this.zzo(0);
    }

    @Override
    @Deprecated
    public final E remove(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final E set(int n, E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    int zza(Object[] objectArray, int n) {
        int n2 = this.size();
        for (n = 0; n < n2; ++n) {
            objectArray[n] = this.get(n);
        }
        return n2;
    }

    @Override
    @Deprecated
    public final zzag<E> zzd() {
        return this;
    }

    @Override
    public final zzaj<E> zze() {
        return this.zzo(0);
    }

    public zzag<E> zzh(int n, int n2) {
        zzs.zzc(n, n2, this.size());
        if ((n2 -= n) == this.size()) {
            return this;
        }
        if (n2 == 0) {
            return zzai.zza;
        }
        return new zzaf(this, n, n2);
    }

    public final zzak<E> zzo(int n) {
        zzs.zzb(n, this.size(), "index");
        if (this.isEmpty()) {
            return zza;
        }
        return new zzae(this, n);
    }
}

