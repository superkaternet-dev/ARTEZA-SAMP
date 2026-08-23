/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.view.View
 */
package com.google.android.gms.common.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.view.View;
import com.google.android.gms.common.internal.zam;
import com.google.android.gms.common.internal.zax;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamic.RemoteCreator;

public final class zaz
extends RemoteCreator<zam> {
    private static final zaz zaa = new zaz();

    private zaz() {
        super("com.google.android.gms.common.ui.SignInButtonCreatorImpl");
    }

    public static View zaa(Context object, int n, int n2) throws RemoteCreator.RemoteCreatorException {
        zaz zaz2 = zaa;
        try {
            zax zax2 = new zax(1, n, n2, null);
            IObjectWrapper iObjectWrapper = ObjectWrapper.wrap(object);
            object = (View)ObjectWrapper.unwrap(((zam)zaz2.getRemoteCreatorInstance((Context)object)).zae(iObjectWrapper, zax2));
            return object;
        }
        catch (Exception exception) {
            object = new StringBuilder(64);
            ((StringBuilder)object).append("Could not get button with size ");
            ((StringBuilder)object).append(n);
            ((StringBuilder)object).append(" and color ");
            ((StringBuilder)object).append(n2);
            throw new RemoteCreator.RemoteCreatorException(((StringBuilder)object).toString(), exception);
        }
    }

    @Override
    public final /* synthetic */ Object getRemoteCreator(IBinder object) {
        IInterface iInterface;
        object = object == null ? null : ((iInterface = object.queryLocalInterface("com.google.android.gms.common.internal.ISignInButtonCreator")) instanceof zam ? (zam)iInterface : new zam((IBinder)object));
        return object;
    }
}

