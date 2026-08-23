/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Bundle
 *  android.os.Environment
 */
package com.wardrumstudios.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import com.nvidia.devtech.NvUtil;
import com.wardrumstudios.utils.WarGamepad;
import java.io.File;
import java.io.PrintStream;

public class WarMedia
extends WarGamepad {
    private String baseDirectory;
    private String baseDirectoryRoot;

    public boolean DeleteFile(String string2) {
        System.out.println("**** DeleteFile");
        return true;
    }

    public String FileGetArchiveName(int n) {
        System.out.println("**** FileGetArchiveName");
        switch (n) {
            default: {
                return "";
            }
            case 2: {
                return "";
            }
            case 1: {
                return "";
            }
            case 0: 
        }
        return "";
    }

    public boolean FileRename(String string2, String string3, int n) {
        System.out.println("**** FileRename");
        return true;
    }

    public String GetAndroidBuildinfo(int n) {
        System.out.println("**** GetAndroidBuildinfo");
        switch (n) {
            default: {
                return "UNKNOWN";
            }
            case 2: {
                return Build.MODEL;
            }
            case 1: {
                return Build.PRODUCT;
            }
            case 0: 
        }
        return Build.MANUFACTURER;
    }

    public String GetAppId() {
        System.out.println("**** GetAppId");
        return "";
    }

    public int GetAvailableMemory() {
        System.out.println("**** GetAvailableMemory");
        return 0;
    }

    public int GetDeviceInfo(int n) {
        System.out.println("**** GetDeviceInfo");
        switch (n) {
            default: {
                return -1;
            }
            case 1: {
                System.out.println("Return for touchsreen 1");
                return 1;
            }
            case 0: 
        }
        return 1;
    }

    public int GetDeviceLocale() {
        System.out.println("**** GetDeviceLocale");
        return 0;
    }

    public int GetDeviceType() {
        int n = 0;
        Appendable appendable = System.out;
        Appendable appendable2 = new StringBuilder();
        ((StringBuilder)appendable2).append("Build info version device  ");
        ((StringBuilder)appendable2).append(Build.DEVICE);
        ((PrintStream)appendable).println(((StringBuilder)appendable2).toString());
        appendable2 = System.out;
        appendable = new StringBuilder();
        ((StringBuilder)appendable).append("Build MANUFACTURER  ");
        ((StringBuilder)appendable).append(Build.MANUFACTURER);
        ((PrintStream)appendable2).println(((StringBuilder)appendable).toString());
        appendable2 = System.out;
        appendable = new StringBuilder();
        ((StringBuilder)appendable).append("Build BOARD  ");
        ((StringBuilder)appendable).append(Build.BOARD);
        ((PrintStream)appendable2).println(((StringBuilder)appendable).toString());
        appendable2 = System.out;
        appendable = new StringBuilder();
        ((StringBuilder)appendable).append("Build DISPLAY  ");
        ((StringBuilder)appendable).append(Build.DISPLAY);
        ((PrintStream)appendable2).println(((StringBuilder)appendable).toString());
        appendable2 = System.out;
        appendable = new StringBuilder();
        ((StringBuilder)appendable).append("Build CPU_ABI  ");
        ((StringBuilder)appendable).append(Build.CPU_ABI);
        ((PrintStream)appendable2).println(((StringBuilder)appendable).toString());
        appendable = System.out;
        appendable2 = new StringBuilder();
        ((StringBuilder)appendable2).append("Build CPU_ABI2  ");
        ((StringBuilder)appendable2).append(Build.CPU_ABI2);
        ((PrintStream)appendable).println(((StringBuilder)appendable2).toString());
        appendable2 = System.out;
        appendable = new StringBuilder();
        ((StringBuilder)appendable).append("Build HARDWARE  ");
        ((StringBuilder)appendable).append(Build.HARDWARE);
        ((PrintStream)appendable2).println(((StringBuilder)appendable).toString());
        appendable2 = System.out;
        appendable = new StringBuilder();
        ((StringBuilder)appendable).append("Build MODEL  ");
        ((StringBuilder)appendable).append(Build.MODEL);
        ((PrintStream)appendable2).println(((StringBuilder)appendable).toString());
        appendable = System.out;
        appendable2 = new StringBuilder();
        ((StringBuilder)appendable2).append("Build PRODUCT  ");
        ((StringBuilder)appendable2).append(Build.PRODUCT);
        ((PrintStream)appendable).println(((StringBuilder)appendable2).toString());
        if (this.IsPhone()) {
            n = 1;
        }
        return n + 0 + 4 + 512;
    }

    public String GetGameBaseDirectory() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            try {
                Object object = this.getExternalFilesDir(null);
                CharSequence charSequence = ((File)object).getAbsolutePath();
                this.baseDirectoryRoot = ((String)charSequence).substring(0, ((String)charSequence).indexOf("/Android"));
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append(((File)object).getAbsolutePath());
                ((StringBuilder)charSequence).append("/");
                object = ((StringBuilder)charSequence).toString();
                return object;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return "";
    }

    public int GetLowThreshhold() {
        System.out.println("**** GetLowThreshhold");
        return 0;
    }

    public float GetScreenWidthInches() {
        System.out.println("**** GetScreenWidthInches");
        return 0.0f;
    }

    public int GetSpecialBuildType() {
        System.out.println("**** GetSpecialBuildType");
        return 0;
    }

    public int GetTotalMemory() {
        System.out.println("**** GetTotalMemory");
        return 0;
    }

    public boolean IsAppInstalled(String string2) {
        System.out.println("**** IsAppInstalled");
        return false;
    }

    public boolean IsCloudAvailable() {
        System.out.println("**** IsCloudAvailable");
        return false;
    }

    public boolean IsKeyboardShown() {
        System.out.println("**** IsKeyboardShown");
        return false;
    }

    public int IsMoviePlaying() {
        System.out.println("**** IsMoviePlaying");
        return 0;
    }

    public boolean IsPhone() {
        System.out.println("**** IsPhone");
        return true;
    }

    public void LoadAllGamesFromCloud() {
        System.out.println("**** LoadAllGamesFromCloud");
    }

    public String LoadGameFromCloud(int n, byte[] byArray) {
        System.out.println("**** LoadGameFromCloud");
        return "";
    }

    public void MovieClearText(boolean bl) {
        System.out.println("**** MovieClearText");
    }

    public void MovieDisplayText(boolean bl) {
        System.out.println("**** MovieDisplayText");
    }

    public void MovieKeepAspectRatio(boolean bl) {
        System.out.println("**** MovieKeepAspectRatio");
    }

    public void MovieSetSkippable(boolean bl) {
        System.out.println("**** MovieSetSkippable");
    }

    public void MovieSetText(String string2, boolean bl, boolean bl2) {
        System.out.println("**** MovieSetText");
    }

    public void MovieSetTextScale(int n) {
        System.out.println("**** MovieSetTextScale");
    }

    public boolean NewCloudSaveAvailable(int n) {
        System.out.println("**** NewCloudSaveAvailable");
        return false;
    }

    public String OBFU_GetDeviceID() {
        System.out.println("**** OBFU_GetDeviceID");
        return "no id";
    }

    public void OpenLink(String string2) {
        this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)string2)));
        System.out.println("**** OpenLink");
    }

    public void PlayMovie(String string2, float f) {
        System.out.println("**** PlayMovie");
    }

    public void PlayMovieInFile(String string2, float f, int n, int n2) {
        System.out.println("**** PlayMovieInFile");
    }

    public void PlayMovieInWindow(String string2, int n, int n2, int n3, int n4, float f, int n5, int n6, int n7) {
        System.out.println("**** PlayMovieInWindow");
    }

    public void SaveGameToCloud(int n, byte[] byArray, int n2) {
        System.out.println("**** SaveGameToCloud");
    }

    public void ScreenSetWakeLock(boolean bl) {
        System.out.println("**** ScreenSetWakeLock");
    }

    public void SendStatEvent(String string2) {
        System.out.println("**** SendStatEvent");
    }

    public void SendStatEvent(String string2, String string3, String string4) {
        System.out.println("**** SendStatEvent1");
    }

    public boolean ServiceAppCommand(String string2, String string3) {
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("**** ServiceAppCommand ");
        stringBuilder.append(string2);
        stringBuilder.append(" ");
        stringBuilder.append(string3);
        printStream.println(stringBuilder.toString());
        return false;
    }

    public int ServiceAppCommandValue(String string2, String string3) {
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("**** ServiceAppCommandValue ");
        stringBuilder.append(string2);
        stringBuilder.append(" ");
        stringBuilder.append(string3);
        printStream.println(stringBuilder.toString());
        return 0;
    }

    public void ShowKeyboard(int n) {
        System.out.println("**** ShowKeyboard");
    }

    public void StopMovie() {
        System.out.println("**** StopMovie");
    }

    @Override
    public void onCreate(Bundle bundle) {
        this.baseDirectory = this.GetGameBaseDirectory();
        NvUtil.getInstance().setActivity(this);
        NvUtil.getInstance().setAppLocalValue("STORAGE_ROOT", this.baseDirectory);
        NvUtil.getInstance().setAppLocalValue("STORAGE_ROOT_BASE", this.baseDirectoryRoot);
        super.onCreate(bundle);
    }
}

