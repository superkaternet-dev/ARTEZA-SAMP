/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package com.google.firebase;

import android.content.Context;
import android.os.Build;
import com.google.firebase.FirebaseCommonRegistrar$$ExternalSyntheticLambda0;
import com.google.firebase.FirebaseCommonRegistrar$$ExternalSyntheticLambda1;
import com.google.firebase.FirebaseCommonRegistrar$$ExternalSyntheticLambda2;
import com.google.firebase.FirebaseCommonRegistrar$$ExternalSyntheticLambda3;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.heartbeatinfo.DefaultHeartBeatController;
import com.google.firebase.platforminfo.DefaultUserAgentPublisher;
import com.google.firebase.platforminfo.KotlinDetector;
import com.google.firebase.platforminfo.LibraryVersionComponent;
import java.util.ArrayList;
import java.util.List;

public class FirebaseCommonRegistrar
implements ComponentRegistrar {
    private static final String ANDROID_INSTALLER = "android-installer";
    private static final String ANDROID_PLATFORM = "android-platform";
    private static final String DEVICE_BRAND = "device-brand";
    private static final String DEVICE_MODEL = "device-model";
    private static final String DEVICE_NAME = "device-name";
    private static final String FIREBASE_ANDROID = "fire-android";
    private static final String FIREBASE_COMMON = "fire-core";
    private static final String KOTLIN = "kotlin";
    private static final String MIN_SDK = "android-min-sdk";
    private static final String TARGET_SDK = "android-target-sdk";

    static /* synthetic */ String lambda$getComponents$0(Context context) {
        if ((context = context.getApplicationInfo()) != null) {
            return String.valueOf(context.targetSdkVersion);
        }
        return "";
    }

    static /* synthetic */ String lambda$getComponents$1(Context context) {
        if ((context = context.getApplicationInfo()) != null && Build.VERSION.SDK_INT >= 24) {
            return String.valueOf(context.minSdkVersion);
        }
        return "";
    }

    static /* synthetic */ String lambda$getComponents$2(Context context) {
        if (Build.VERSION.SDK_INT >= 16 && context.getPackageManager().hasSystemFeature("android.hardware.type.television")) {
            return "tv";
        }
        if (Build.VERSION.SDK_INT >= 20 && context.getPackageManager().hasSystemFeature("android.hardware.type.watch")) {
            return "watch";
        }
        if (Build.VERSION.SDK_INT >= 23 && context.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
            return "auto";
        }
        if (Build.VERSION.SDK_INT >= 26 && context.getPackageManager().hasSystemFeature("android.hardware.type.embedded")) {
            return "embedded";
        }
        return "";
    }

    static /* synthetic */ String lambda$getComponents$3(Context object) {
        object = (object = object.getPackageManager().getInstallerPackageName(object.getPackageName())) != null ? FirebaseCommonRegistrar.safeValue((String)object) : "";
        return object;
    }

    private static String safeValue(String string2) {
        return string2.replace(' ', '_').replace('/', '_');
    }

    @Override
    public List<Component<?>> getComponents() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(DefaultUserAgentPublisher.component());
        arrayList.add(DefaultHeartBeatController.component());
        arrayList.add(LibraryVersionComponent.create(FIREBASE_ANDROID, String.valueOf(Build.VERSION.SDK_INT)));
        arrayList.add(LibraryVersionComponent.create(FIREBASE_COMMON, "20.1.0"));
        arrayList.add(LibraryVersionComponent.create(DEVICE_NAME, FirebaseCommonRegistrar.safeValue(Build.PRODUCT)));
        arrayList.add(LibraryVersionComponent.create(DEVICE_MODEL, FirebaseCommonRegistrar.safeValue(Build.DEVICE)));
        arrayList.add(LibraryVersionComponent.create(DEVICE_BRAND, FirebaseCommonRegistrar.safeValue(Build.BRAND)));
        arrayList.add(LibraryVersionComponent.fromContext(TARGET_SDK, FirebaseCommonRegistrar$$ExternalSyntheticLambda0.INSTANCE));
        arrayList.add(LibraryVersionComponent.fromContext(MIN_SDK, FirebaseCommonRegistrar$$ExternalSyntheticLambda1.INSTANCE));
        arrayList.add(LibraryVersionComponent.fromContext(ANDROID_PLATFORM, FirebaseCommonRegistrar$$ExternalSyntheticLambda2.INSTANCE));
        arrayList.add(LibraryVersionComponent.fromContext(ANDROID_INSTALLER, FirebaseCommonRegistrar$$ExternalSyntheticLambda3.INSTANCE));
        String string2 = KotlinDetector.detectVersion();
        if (string2 != null) {
            arrayList.add(LibraryVersionComponent.create(KOTLIN, string2));
        }
        return arrayList;
    }
}

