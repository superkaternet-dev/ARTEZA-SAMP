/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 */
package androidx.lifecycle;

import android.app.Application;
import androidx.lifecycle.ViewModel;

public class AndroidViewModel
extends ViewModel {
    private Application mApplication;

    public AndroidViewModel(Application application) {
        this.mApplication = application;
    }

    public <T extends Application> T getApplication() {
        return (T)this.mApplication;
    }
}

