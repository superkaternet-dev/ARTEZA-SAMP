/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.platforminfo;

import com.google.firebase.platforminfo.LibraryVersion;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GlobalLibraryVersionRegistrar {
    private static volatile GlobalLibraryVersionRegistrar INSTANCE;
    private final Set<LibraryVersion> infos = new HashSet<LibraryVersion>();

    GlobalLibraryVersionRegistrar() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static GlobalLibraryVersionRegistrar getInstance() {
        GlobalLibraryVersionRegistrar globalLibraryVersionRegistrar;
        GlobalLibraryVersionRegistrar globalLibraryVersionRegistrar2 = globalLibraryVersionRegistrar = INSTANCE;
        if (globalLibraryVersionRegistrar != null) return globalLibraryVersionRegistrar2;
        synchronized (GlobalLibraryVersionRegistrar.class) {
            globalLibraryVersionRegistrar2 = globalLibraryVersionRegistrar = INSTANCE;
            if (globalLibraryVersionRegistrar != null) return globalLibraryVersionRegistrar2;
            globalLibraryVersionRegistrar2 = globalLibraryVersionRegistrar = new GlobalLibraryVersionRegistrar();
            INSTANCE = globalLibraryVersionRegistrar;
            return globalLibraryVersionRegistrar2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    Set<LibraryVersion> getRegisteredVersions() {
        Set<LibraryVersion> set = this.infos;
        synchronized (set) {
            return Collections.unmodifiableSet(this.infos);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void registerVersion(String string2, String string3) {
        Set<LibraryVersion> set = this.infos;
        synchronized (set) {
            this.infos.add(LibraryVersion.create(string2, string3));
            return;
        }
    }
}

