/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.AlertDialog$Builder
 *  android.app.Dialog
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnClickListener
 *  android.os.Bundle
 */
package com.blackrussia.game.core;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

public class NotificationDialogFragment
extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        bundle = new AlertDialog.Builder((Context)this.getActivity());
        bundle.setMessage((CharSequence)"\u0414\u043b\u044f \u043f\u0440\u0438\u043c\u0435\u043d\u0435\u043d\u0438\u044f \u0431\u0435\u0437\u0433\u0440\u0430\u043d\u0438\u0447\u043d\u043e\u0441\u0442\u0438 \u044d\u043a\u0440\u0430\u043d\u0430 \u043d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u043e \u043f\u0435\u0440\u0435\u0437\u0430\u0439\u0442\u0438 \u0432 \u0438\u0433\u0440\u0443").setPositiveButton((CharSequence)"\u0417\u0430\u043a\u0440\u044b\u0442\u044c", new DialogInterface.OnClickListener(this){
            final NotificationDialogFragment this$0;
            {
                this.this$0 = notificationDialogFragment;
            }

            public void onClick(DialogInterface dialogInterface, int n) {
            }
        });
        return bundle.create();
    }
}

