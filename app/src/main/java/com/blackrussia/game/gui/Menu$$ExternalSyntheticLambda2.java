/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package com.blackrussia.game.gui;

import android.view.View;
import com.blackrussia.game.gui.Menu;
import com.blackrussia.game.gui.adapters.DialogMenuAdapter;
import com.blackrussia.game.gui.models.DataDialogMenu;

public final class Menu$$ExternalSyntheticLambda2
implements DialogMenuAdapter.OnUserClickListener {
    public final Menu f$0;

    public /* synthetic */ Menu$$ExternalSyntheticLambda2(Menu menu) {
        this.f$0 = menu;
    }

    @Override
    public final void click(DataDialogMenu dataDialogMenu, View view) {
        this.f$0.lambda$Update$4$com-blackrussia-game-gui-Menu(dataDialogMenu, view);
    }
}

