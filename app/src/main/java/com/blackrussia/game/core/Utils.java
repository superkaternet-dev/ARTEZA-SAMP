/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package com.blackrussia.game.core;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;

public class Utils
extends AppCompatActivity {
    private static final String GAME_PATH = "/storage/emulated/0/BlackRussia/";

    public static boolean getGameFile(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(GAME_PATH);
        stringBuilder.append(string2);
        return new File(stringBuilder.toString()).exists();
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }
}

