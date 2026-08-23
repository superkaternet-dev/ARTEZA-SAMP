/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.ViewParent
 */
package com.wardrumstudios.utils;

import android.view.ViewParent;
import com.wardrumstudios.utils.WarBilling;

public class WarGamepad
extends WarBilling {
    public float GetGamepadAxis(int n) {
        System.out.println("**** GetGamepadAxis()");
        return 0.0f;
    }

    public int GetGamepadButtons() {
        System.out.println("**** GetGamepadButtons()");
        return 0;
    }

    public int GetGamepadTrack(int n, int n2) {
        System.out.println("**** GetGamepadTrack()");
        return 0;
    }

    public int GetGamepadType() {
        return -1;
    }

    @Override
    public native boolean processTouchpadAsPointer(ViewParent var1, boolean var2);
}

