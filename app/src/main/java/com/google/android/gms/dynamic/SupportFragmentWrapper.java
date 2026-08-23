/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.Bundle
 *  android.view.View
 */
package com.google.android.gms.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;

public final class SupportFragmentWrapper
extends IFragmentWrapper.Stub {
    private Fragment zza;

    private SupportFragmentWrapper(Fragment fragment) {
        this.zza = fragment;
    }

    public static SupportFragmentWrapper wrap(Fragment fragment) {
        if (fragment != null) {
            return new SupportFragmentWrapper(fragment);
        }
        return null;
    }

    @Override
    public final boolean zzA() {
        return this.zza.isVisible();
    }

    @Override
    public final int zzb() {
        return this.zza.getId();
    }

    @Override
    public final int zzc() {
        return this.zza.getTargetRequestCode();
    }

    @Override
    public final Bundle zzd() {
        return this.zza.getArguments();
    }

    @Override
    public final IFragmentWrapper zze() {
        return SupportFragmentWrapper.wrap(this.zza.getParentFragment());
    }

    @Override
    public final IFragmentWrapper zzf() {
        return SupportFragmentWrapper.wrap(this.zza.getTargetFragment());
    }

    @Override
    public final IObjectWrapper zzg() {
        return ObjectWrapper.wrap(this.zza.getActivity());
    }

    @Override
    public final IObjectWrapper zzh() {
        return ObjectWrapper.wrap(this.zza.getResources());
    }

    @Override
    public final IObjectWrapper zzi() {
        return ObjectWrapper.wrap(this.zza.getView());
    }

    @Override
    public final String zzj() {
        return this.zza.getTag();
    }

    @Override
    public final void zzk(IObjectWrapper object) {
        View view = (View)ObjectWrapper.unwrap((IObjectWrapper)object);
        object = this.zza;
        Preconditions.checkNotNull(view);
        ((Fragment)object).registerForContextMenu(view);
    }

    @Override
    public final void zzl(boolean bl) {
        this.zza.setHasOptionsMenu(bl);
    }

    @Override
    public final void zzm(boolean bl) {
        this.zza.setMenuVisibility(bl);
    }

    @Override
    public final void zzn(boolean bl) {
        this.zza.setRetainInstance(bl);
    }

    @Override
    public final void zzo(boolean bl) {
        this.zza.setUserVisibleHint(bl);
    }

    @Override
    public final void zzp(Intent intent) {
        this.zza.startActivity(intent);
    }

    @Override
    public final void zzq(Intent intent, int n) {
        this.zza.startActivityForResult(intent, n);
    }

    @Override
    public final void zzr(IObjectWrapper object) {
        View view = (View)ObjectWrapper.unwrap((IObjectWrapper)object);
        object = this.zza;
        Preconditions.checkNotNull(view);
        ((Fragment)object).unregisterForContextMenu(view);
    }

    @Override
    public final boolean zzs() {
        return this.zza.getRetainInstance();
    }

    @Override
    public final boolean zzt() {
        return this.zza.getUserVisibleHint();
    }

    @Override
    public final boolean zzu() {
        return this.zza.isAdded();
    }

    @Override
    public final boolean zzv() {
        return this.zza.isDetached();
    }

    @Override
    public final boolean zzw() {
        return this.zza.isHidden();
    }

    @Override
    public final boolean zzx() {
        return this.zza.isInLayout();
    }

    @Override
    public final boolean zzy() {
        return this.zza.isRemoving();
    }

    @Override
    public final boolean zzz() {
        return this.zza.isResumed();
    }
}

