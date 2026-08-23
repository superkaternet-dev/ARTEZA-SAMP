/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.View$OnClickListener
 */
package com.blackrussia.game.gui.dialogs;

import android.view.View;
import com.blackrussia.game.gui.dialogs.DialogAdapter;

public final class DialogAdapter$$ExternalSyntheticLambda0
implements View.OnClickListener {
    public final DialogAdapter f$0;
    public final DialogAdapter.ViewHolder f$1;

    public /* synthetic */ DialogAdapter$$ExternalSyntheticLambda0(DialogAdapter dialogAdapter, DialogAdapter.ViewHolder viewHolder) {
        this.f$0 = dialogAdapter;
        this.f$1 = viewHolder;
    }

    public final void onClick(View view) {
        this.f$0.lambda$onBindViewHolder$0$com-blackrussia-game-gui-dialogs-DialogAdapter(this.f$1, view);
    }
}

