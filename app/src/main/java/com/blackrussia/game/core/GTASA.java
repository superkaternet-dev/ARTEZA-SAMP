/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.content.res.Configuration
 *  android.os.Bundle
 *  android.view.KeyEvent
 */
package com.blackrussia.game.core;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import com.blackrussia.launcher.activity.SplashActivity;
import com.wardrumstudios.utils.WarMedia;
import java.io.PrintStream;

public class GTASA
extends WarMedia {
    public static GTASA gtasaSelf = null;
    static String vmVersion = null;
    private boolean once = false;

    static {
        System.out.println("**** Loading SO's");
        try {
            vmVersion = System.getProperty("java.vm.version");
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("vmVersion ");
            stringBuilder.append(vmVersion);
            printStream.println(stringBuilder.toString());
            System.loadLibrary("ImmEmulatorJ");
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
        }
        catch (ExceptionInInitializerError exceptionInInitializerError) {
            // empty catch block
        }
        System.loadLibrary("GTASA");
        System.loadLibrary("sampvoice");
    }

    public static void staticEnterSocialClub() {
        gtasaSelf.EnterSocialClub();
    }

    public static void staticExitSocialClub() {
        gtasaSelf.ExitSocialClub();
    }

    public void AfterDownloadFunction() {
    }

    public void EnterSocialClub() {
    }

    public void ExitSocialClub() {
    }

    @Override
    public boolean ServiceAppCommand(String string2, String string3) {
        return false;
    }

    @Override
    public int ServiceAppCommandValue(String string2, String string3) {
        return 0;
    }

    public native void main();

    @Override
    public void onActivityResult(int n, int n2, Intent intent) {
        super.onActivityResult(n, n2, intent);
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override
    public void onCreate(Bundle bundle) {
        if (!this.once) {
            this.once = true;
        }
        System.out.println("GTASA onCreate");
        gtasaSelf = this;
        this.wantsMultitouch = true;
        this.wantsAccelerometer = true;
        super.onCreate(bundle);
    }

    @Override
    public void onDestroy() {
        System.out.println("GTASA onDestroy");
        this.startActivity(new Intent(this.getApplicationContext(), SplashActivity.class));
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        return super.onKeyDown(n, keyEvent);
    }

    @Override
    public void onPause() {
        System.out.println("GTASA onPause");
        super.onPause();
    }

    @Override
    public void onRestart() {
        System.out.println("GTASA onRestart");
        super.onRestart();
    }

    @Override
    public void onResume() {
        System.out.println("GTASA onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        System.out.println("GTASA onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        System.out.println("GTASA onStop");
        super.onStop();
    }

    public native void setCurrentScreenSize(int var1, int var2);
}

