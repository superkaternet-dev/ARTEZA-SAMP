/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Dialog
 *  android.os.Bundle
 */
package androidx.appcompat.app;

import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;

public class AppCompatDialogFragment
extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        return new AppCompatDialog(this.getContext(), this.getTheme());
    }

    @Override
    public void setupDialog(Dialog dialog, int n) {
        if (dialog instanceof AppCompatDialog) {
            AppCompatDialog appCompatDialog = (AppCompatDialog)dialog;
            switch (n) {
                default: {
                    break;
                }
                case 3: {
                    dialog.getWindow().addFlags(24);
                }
                case 1: 
                case 2: {
                    appCompatDialog.supportRequestWindowFeature(1);
                    break;
                }
            }
        } else {
            super.setupDialog(dialog, n);
        }
    }
}

