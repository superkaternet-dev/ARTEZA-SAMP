/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

import com.google.firebase.components.AbstractComponentContainer;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.Dependency;
import com.google.firebase.components.DependencyException;
import com.google.firebase.events.Event;
import com.google.firebase.events.Publisher;
import com.google.firebase.inject.Deferred;
import com.google.firebase.inject.Provider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final class RestrictedComponentContainer
extends AbstractComponentContainer {
    private final Set<Class<?>> allowedDeferredInterfaces;
    private final Set<Class<?>> allowedDirectInterfaces;
    private final Set<Class<?>> allowedProviderInterfaces;
    private final Set<Class<?>> allowedPublishedEvents;
    private final Set<Class<?>> allowedSetDirectInterfaces;
    private final Set<Class<?>> allowedSetProviderInterfaces;
    private final ComponentContainer delegateContainer;

    RestrictedComponentContainer(Component<?> component, ComponentContainer componentContainer) {
        HashSet hashSet = new HashSet();
        HashSet hashSet2 = new HashSet();
        HashSet hashSet3 = new HashSet();
        HashSet hashSet4 = new HashSet();
        HashSet hashSet5 = new HashSet();
        for (Dependency dependency : component.getDependencies()) {
            if (dependency.isDirectInjection()) {
                if (dependency.isSet()) {
                    hashSet4.add(dependency.getInterface());
                    continue;
                }
                hashSet.add(dependency.getInterface());
                continue;
            }
            if (dependency.isDeferred()) {
                hashSet3.add(dependency.getInterface());
                continue;
            }
            if (dependency.isSet()) {
                hashSet5.add(dependency.getInterface());
                continue;
            }
            hashSet2.add(dependency.getInterface());
        }
        if (!component.getPublishedEvents().isEmpty()) {
            hashSet.add(Publisher.class);
        }
        this.allowedDirectInterfaces = Collections.unmodifiableSet(hashSet);
        this.allowedProviderInterfaces = Collections.unmodifiableSet(hashSet2);
        this.allowedDeferredInterfaces = Collections.unmodifiableSet(hashSet3);
        this.allowedSetDirectInterfaces = Collections.unmodifiableSet(hashSet4);
        this.allowedSetProviderInterfaces = Collections.unmodifiableSet(hashSet5);
        this.allowedPublishedEvents = component.getPublishedEvents();
        this.delegateContainer = componentContainer;
    }

    @Override
    public <T> T get(Class<T> clazz) {
        if (this.allowedDirectInterfaces.contains(clazz)) {
            T t = this.delegateContainer.get(clazz);
            if (!clazz.equals(Publisher.class)) {
                return t;
            }
            return (T)new RestrictedPublisher(this.allowedPublishedEvents, (Publisher)t);
        }
        throw new DependencyException(String.format("Attempting to request an undeclared dependency %s.", clazz));
    }

    @Override
    public <T> Deferred<T> getDeferred(Class<T> clazz) {
        if (this.allowedDeferredInterfaces.contains(clazz)) {
            return this.delegateContainer.getDeferred(clazz);
        }
        throw new DependencyException(String.format("Attempting to request an undeclared dependency Deferred<%s>.", clazz));
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> clazz) {
        if (this.allowedProviderInterfaces.contains(clazz)) {
            return this.delegateContainer.getProvider(clazz);
        }
        throw new DependencyException(String.format("Attempting to request an undeclared dependency Provider<%s>.", clazz));
    }

    @Override
    public <T> Set<T> setOf(Class<T> clazz) {
        if (this.allowedSetDirectInterfaces.contains(clazz)) {
            return this.delegateContainer.setOf(clazz);
        }
        throw new DependencyException(String.format("Attempting to request an undeclared dependency Set<%s>.", clazz));
    }

    @Override
    public <T> Provider<Set<T>> setOfProvider(Class<T> clazz) {
        if (this.allowedSetProviderInterfaces.contains(clazz)) {
            return this.delegateContainer.setOfProvider(clazz);
        }
        throw new DependencyException(String.format("Attempting to request an undeclared dependency Provider<Set<%s>>.", clazz));
    }

    private static class RestrictedPublisher
    implements Publisher {
        private final Set<Class<?>> allowedPublishedEvents;
        private final Publisher delegate;

        public RestrictedPublisher(Set<Class<?>> set, Publisher publisher) {
            this.allowedPublishedEvents = set;
            this.delegate = publisher;
        }

        @Override
        public void publish(Event<?> event) {
            if (this.allowedPublishedEvents.contains(event.getType())) {
                this.delegate.publish(event);
                return;
            }
            throw new DependencyException(String.format("Attempting to publish an undeclared event %s.", event));
        }
    }
}

