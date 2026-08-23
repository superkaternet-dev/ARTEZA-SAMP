/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.platforminfo;

import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.Dependency;
import com.google.firebase.platforminfo.DefaultUserAgentPublisher$$ExternalSyntheticLambda0;
import com.google.firebase.platforminfo.GlobalLibraryVersionRegistrar;
import com.google.firebase.platforminfo.LibraryVersion;
import com.google.firebase.platforminfo.UserAgentPublisher;
import java.util.Iterator;
import java.util.Set;

public class DefaultUserAgentPublisher
implements UserAgentPublisher {
    private final GlobalLibraryVersionRegistrar gamesSDKRegistrar;
    private final String javaSDKVersionUserAgent;

    DefaultUserAgentPublisher(Set<LibraryVersion> set, GlobalLibraryVersionRegistrar globalLibraryVersionRegistrar) {
        this.javaSDKVersionUserAgent = DefaultUserAgentPublisher.toUserAgent(set);
        this.gamesSDKRegistrar = globalLibraryVersionRegistrar;
    }

    public static Component<UserAgentPublisher> component() {
        return Component.builder(UserAgentPublisher.class).add(Dependency.setOf(LibraryVersion.class)).factory(DefaultUserAgentPublisher$$ExternalSyntheticLambda0.INSTANCE).build();
    }

    static /* synthetic */ UserAgentPublisher lambda$component$0(ComponentContainer componentContainer) {
        return new DefaultUserAgentPublisher(componentContainer.setOf(LibraryVersion.class), GlobalLibraryVersionRegistrar.getInstance());
    }

    private static String toUserAgent(Set<LibraryVersion> object) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<LibraryVersion> iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            object = iterator2.next();
            stringBuilder.append(((LibraryVersion)object).getLibraryName());
            stringBuilder.append('/');
            stringBuilder.append(((LibraryVersion)object).getVersion());
            if (!iterator2.hasNext()) continue;
            stringBuilder.append(' ');
        }
        return stringBuilder.toString();
    }

    @Override
    public String getUserAgent() {
        if (this.gamesSDKRegistrar.getRegisteredVersions().isEmpty()) {
            return this.javaSDKVersionUserAgent;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.javaSDKVersionUserAgent);
        stringBuilder.append(' ');
        stringBuilder.append(DefaultUserAgentPublisher.toUserAgent(this.gamesSDKRegistrar.getRegisteredVersions()));
        return stringBuilder.toString();
    }
}

