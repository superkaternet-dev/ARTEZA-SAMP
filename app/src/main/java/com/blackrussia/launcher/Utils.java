/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package com.blackrussia.launcher;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;

public class Utils
extends AppCompatActivity {
    public static final String GAME_PATH2 = "/storage/emulated/0/";

    public static boolean getGameFile(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(GAME_PATH2);
        stringBuilder.append(string2);
        return new File(stringBuilder.toString()).exists();
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }
}

