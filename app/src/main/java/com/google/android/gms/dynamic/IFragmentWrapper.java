/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.RemoteException
 */
package com.google.android.gms.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.zza;
import com.google.android.gms.internal.common.zzb;
import com.google.android.gms.internal.common.zzc;

public interface IFragmentWrapper
extends IInterface {
    public boolean zzA() throws RemoteException;

    public int zzb() throws RemoteException;

    public int zzc() throws RemoteException;

    public Bundle zzd() throws RemoteException;

    public IFragmentWrapper zze() throws RemoteException;

    public IFragmentWrapper zzf() throws RemoteException;

    public IObjectWrapper zzg() throws RemoteException;

    public IObjectWrapper zzh() throws RemoteException;

    public IObjectWrapper zzi() throws RemoteException;

    public String zzj() throws RemoteException;

    public void zzk(IObjectWrapper var1) throws RemoteException;

    public void zzl(boolean var1) throws RemoteException;

    public void zzm(boolean var1) throws RemoteException;

    public void zzn(boolean var1) throws RemoteException;

    public void zzo(boolean var1) throws RemoteException;

    public void zzp(Intent var1) throws RemoteException;

    public void zzq(Intent var1, int var2) throws RemoteException;

    public void zzr(IObjectWrapper var1) throws RemoteException;

    public boolean zzs() throws RemoteException;

    public boolean zzt() throws RemoteException;

    public boolean zzu() throws RemoteException;

    public boolean zzv() throws RemoteException;

    public boolean zzw() throws RemoteException;

    public boolean zzx() throws RemoteException;

    public boolean zzy() throws RemoteException;

    public boolean zzz() throws RemoteException;

    public static abstract class Stub
    extends zzb
    implements IFragmentWrapper {
        public Stub() {
            super("com.google.android.gms.dynamic.IFragmentWrapper");
        }

        public static IFragmentWrapper asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamic.IFragmentWrapper");
            if (iInterface instanceof IFragmentWrapper) {
                return (IFragmentWrapper)iInterface;
            }
            return new zza(iBinder);
        }

        @Override
        protected final boolean zza(int n, Parcel object, Parcel parcel, int n2) throws RemoteException {
            switch (n) {
                default: {
                    return false;
                }
                case 27: {
                    this.zzr(IObjectWrapper.Stub.asInterface(object.readStrongBinder()));
                    parcel.writeNoException();
                    break;
                }
                case 26: {
                    this.zzq((Intent)zzc.zza(object, Intent.CREATOR), object.readInt());
                    parcel.writeNoException();
                    break;
                }
                case 25: {
                    this.zzp((Intent)zzc.zza(object, Intent.CREATOR));
                    parcel.writeNoException();
                    break;
                }
                case 24: {
                    this.zzo(zzc.zzf(object));
                    parcel.writeNoException();
                    break;
                }
                case 23: {
                    this.zzn(zzc.zzf(object));
                    parcel.writeNoException();
                    break;
                }
                case 22: {
                    this.zzm(zzc.zzf(object));
                    parcel.writeNoException();
                    break;
                }
                case 21: {
                    this.zzl(zzc.zzf(object));
                    parcel.writeNoException();
                    break;
                }
                case 20: {
                    this.zzk(IObjectWrapper.Stub.asInterface(object.readStrongBinder()));
                    parcel.writeNoException();
                    break;
                }
                case 19: {
                    boolean bl = this.zzA();
                    parcel.writeNoException();
                    zzc.zzb(parcel, bl);
                    break;
                }
                case 18: {
                    boolean bl = this.zzz();
                    parcel.writeNoException();
                    zzc.zzb(parcel, bl);
                    break;
                }
                case 17: {
                    boolean bl = this.zzy();
                    parcel.writeNoException();
                    zzc.zzb(parcel, bl);
                    break;
                }
                case 16: {
                    boolean bl = this.zzx();
                    parcel.writeNoException();
                    zzc.zzb(parcel, bl);
                    break;
                }
                case 15: {
                    boolean bl = this.zzw();
                    parcel.writeNoException();
                    zzc.zzb(parcel, bl);
                    break;
                }
                case 14: {
                    boolean bl = this.zzv();
                    parcel.writeNoException();
                    zzc.zzb(parcel, bl);
                    break;
                }
                case 13: {
                    boolean bl = this.zzu();
                    parcel.writeNoException();
                    zzc.zzb(parcel, bl);
                    break;
                }
                case 12: {
                    object = this.zzi();
                    parcel.writeNoException();
                    zzc.zze(parcel, (IInterface)object);
                    break;
                }
                case 11: {
                    boolean bl = this.zzt();
                    parcel.writeNoException();
                    zzc.zzb(parcel, bl);
                    break;
                }
                case 10: {
                    n = this.zzc();
                    parcel.writeNoException();
                    parcel.writeInt(n);
                    break;
                }
                case 9: {
                    object = this.zzf();
                    parcel.writeNoException();
                    zzc.zze(parcel, (IInterface)object);
                    break;
                }
                case 8: {
                    object = this.zzj();
                    parcel.writeNoException();
                    parcel.writeString((String)object);
                    break;
                }
                case 7: {
                    boolean bl = this.zzs();
                    parcel.writeNoException();
                    zzc.zzb(parcel, bl);
                    break;
                }
                case 6: {
                    object = this.zzh();
                    parcel.writeNoException();
                    zzc.zze(parcel, (IInterface)object);
                    break;
                }
                case 5: {
                    object = this.zze();
                    parcel.writeNoException();
                    zzc.zze(parcel, (IInterface)object);
                    break;
                }
                case 4: {
                    n = this.zzb();
                    parcel.writeNoException();
                    parcel.writeInt(n);
                    break;
                }
                case 3: {
                    object = this.zzd();
                    parcel.writeNoException();
                    zzc.zzd(parcel, (Parcelable)object);
                    break;
                }
                case 2: {
                    object = this.zzg();
                    parcel.writeNoException();
                    zzc.zze(parcel, (IInterface)object);
                }
            }
            return true;
        }
    }
}

