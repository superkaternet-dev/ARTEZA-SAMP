/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.KeyEvent
 *  android.widget.TextView
 *  android.widget.TextView$OnEditorActionListener
 */
package com.blackrussia.game.gui.util;

import android.view.KeyEvent;
import android.widget.TextView;
import com.blackrussia.game.gui.util.MaskedEditText;

public final class $$Lambda$MaskedEditText$uX_YztYUHcbq6dV_J1fDsTb0DjM
implements TextView.OnEditorActionListener {
    public static final $$Lambda$MaskedEditText$uX_YztYUHcbq6dV_J1fDsTb0DjM INSTANCE = new $$Lambda$MaskedEditText$uX_YztYUHcbq6dV_J1fDsTb0DjM();

    private $$Lambda$MaskedEditText$uX_YztYUHcbq6dV_J1fDsTb0DjM() {
    }

    public final boolean onEditorAction(TextView textView, int n, KeyEvent keyEvent) {
        return MaskedEditText.lambda$new$0(textView, n, keyEvent);
    }
}

