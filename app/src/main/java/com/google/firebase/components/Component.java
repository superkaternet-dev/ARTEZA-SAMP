/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

import com.google.firebase.components.Component$$ExternalSyntheticLambda0;
import com.google.firebase.components.Component$$ExternalSyntheticLambda1;
import com.google.firebase.components.Component$$ExternalSyntheticLambda2;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentFactory;
import com.google.firebase.components.Dependency;
import com.google.firebase.components.Preconditions;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Component<T> {
    private final Set<Dependency> dependencies;
    private final ComponentFactory<T> factory;
    private final int instantiation;
    private final Set<Class<? super T>> providedInterfaces;
    private final Set<Class<?>> publishedEvents;
    private final int type;

    private Component(Set<Class<? super T>> set, Set<Dependency> set2, int n, int n2, ComponentFactory<T> componentFactory, Set<Class<?>> set3) {
        this.providedInterfaces = Collections.unmodifiableSet(set);
        this.dependencies = Collections.unmodifiableSet(set2);
        this.instantiation = n;
        this.type = n2;
        this.factory = componentFactory;
        this.publishedEvents = Collections.unmodifiableSet(set3);
    }

    public static <T> Builder<T> builder(Class<T> clazz) {
        return new Builder((Class)clazz, new Class[0]);
    }

    @SafeVarargs
    public static <T> Builder<T> builder(Class<T> clazz, Class<? super T> ... classArray) {
        return new Builder((Class)clazz, (Class[])classArray);
    }

    public static <T> Component<T> intoSet(T t, Class<T> clazz) {
        return Component.intoSetBuilder(clazz).factory(new Component$$ExternalSyntheticLambda0(t)).build();
    }

    public static <T> Builder<T> intoSetBuilder(Class<T> clazz) {
        return ((Builder)Component.builder(clazz)).intoSet();
    }

    static /* synthetic */ Object lambda$intoSet$2(Object object, ComponentContainer componentContainer) {
        return object;
    }

    static /* synthetic */ Object lambda$of$0(Object object, ComponentContainer componentContainer) {
        return object;
    }

    static /* synthetic */ Object lambda$of$1(Object object, ComponentContainer componentContainer) {
        return object;
    }

    @Deprecated
    public static <T> Component<T> of(Class<T> clazz, T t) {
        return Component.builder(clazz).factory(new Component$$ExternalSyntheticLambda1(t)).build();
    }

    @SafeVarargs
    public static <T> Component<T> of(T t, Class<T> clazz, Class<? super T> ... classArray) {
        return Component.builder(clazz, classArray).factory(new Component$$ExternalSyntheticLambda2(t)).build();
    }

    public Set<Dependency> getDependencies() {
        return this.dependencies;
    }

    public ComponentFactory<T> getFactory() {
        return this.factory;
    }

    public Set<Class<? super T>> getProvidedInterfaces() {
        return this.providedInterfaces;
    }

    public Set<Class<?>> getPublishedEvents() {
        return this.publishedEvents;
    }

    public boolean isAlwaysEager() {
        int n = this.instantiation;
        boolean bl = true;
        if (n != 1) {
            bl = false;
        }
        return bl;
    }

    public boolean isEagerInDefaultApp() {
        boolean bl = this.instantiation == 2;
        return bl;
    }

    public boolean isLazy() {
        boolean bl = this.instantiation == 0;
        return bl;
    }

    public boolean isValue() {
        boolean bl = this.type == 0;
        return bl;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Component<");
        stringBuilder.append(Arrays.toString(this.providedInterfaces.toArray()));
        stringBuilder.append(">{");
        stringBuilder.append(this.instantiation);
        stringBuilder.append(", type=");
        stringBuilder.append(this.type);
        stringBuilder.append(", deps=");
        stringBuilder.append(Arrays.toString(this.dependencies.toArray()));
        return stringBuilder.append("}").toString();
    }

    public static class Builder<T> {
        private final Set<Dependency> dependencies;
        private ComponentFactory<T> factory;
        private int instantiation;
        private final Set<Class<? super T>> providedInterfaces;
        private Set<Class<?>> publishedEvents;
        private int type;

        @SafeVarargs
        private Builder(Class<T> clazz, Class<? super T> ... classArray) {
            HashSet<Class<T>> hashSet = new HashSet<Class<T>>();
            this.providedInterfaces = hashSet;
            this.dependencies = new HashSet<Dependency>();
            this.instantiation = 0;
            this.type = 0;
            this.publishedEvents = new HashSet();
            Preconditions.checkNotNull(clazz, "Null interface");
            hashSet.add(clazz);
            int n = classArray.length;
            for (int i = 0; i < n; ++i) {
                Preconditions.checkNotNull(classArray[i], "Null interface");
            }
            Collections.addAll(this.providedInterfaces, classArray);
        }

        private Builder<T> intoSet() {
            this.type = 1;
            return this;
        }

        private Builder<T> setInstantiation(int n) {
            boolean bl = this.instantiation == 0;
            Preconditions.checkState(bl, "Instantiation type has already been set.");
            this.instantiation = n;
            return this;
        }

        private void validateInterface(Class<?> clazz) {
            Preconditions.checkArgument(this.providedInterfaces.contains(clazz) ^ true, "Components are not allowed to depend on interfaces they themselves provide.");
        }

        public Builder<T> add(Dependency dependency) {
            Preconditions.checkNotNull(dependency, "Null dependency");
            this.validateInterface(dependency.getInterface());
            this.dependencies.add(dependency);
            return this;
        }

        public Builder<T> alwaysEager() {
            return this.setInstantiation(1);
        }

        public Component<T> build() {
            boolean bl = this.factory != null;
            Preconditions.checkState(bl, "Missing required property: factory.");
            return new Component(new HashSet<Class<? super T>>(this.providedInterfaces), new HashSet<Dependency>(this.dependencies), this.instantiation, this.type, this.factory, this.publishedEvents);
        }

        public Builder<T> eagerInDefaultApp() {
            return this.setInstantiation(2);
        }

        public Builder<T> factory(ComponentFactory<T> componentFactory) {
            this.factory = Preconditions.checkNotNull(componentFactory, "Null factory");
            return this;
        }

        public Builder<T> publishes(Class<?> clazz) {
            this.publishedEvents.add(clazz);
            return this;
        }
    }
}

