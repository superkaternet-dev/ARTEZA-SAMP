/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.google.firebase.components;

import android.util.Log;
import com.google.firebase.components.AbstractComponentContainer;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.ComponentRuntime$$ExternalSyntheticLambda0;
import com.google.firebase.components.ComponentRuntime$$ExternalSyntheticLambda1;
import com.google.firebase.components.ComponentRuntime$$ExternalSyntheticLambda2;
import com.google.firebase.components.ComponentRuntime$$ExternalSyntheticLambda3;
import com.google.firebase.components.ComponentRuntime$$ExternalSyntheticLambda4;
import com.google.firebase.components.ComponentRuntime$Builder$$ExternalSyntheticLambda0;
import com.google.firebase.components.CycleDetector;
import com.google.firebase.components.Dependency;
import com.google.firebase.components.EventBus;
import com.google.firebase.components.InvalidRegistrarException;
import com.google.firebase.components.Lazy;
import com.google.firebase.components.LazySet;
import com.google.firebase.components.MissingDependencyException;
import com.google.firebase.components.OptionalProvider;
import com.google.firebase.components.Preconditions;
import com.google.firebase.components.RestrictedComponentContainer;
import com.google.firebase.dynamicloading.ComponentLoader;
import com.google.firebase.events.Publisher;
import com.google.firebase.events.Subscriber;
import com.google.firebase.inject.Deferred;
import com.google.firebase.inject.Provider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class ComponentRuntime
extends AbstractComponentContainer
implements ComponentLoader {
    private static final Provider<Set<Object>> EMPTY_PROVIDER = ComponentRuntime$$ExternalSyntheticLambda2.INSTANCE;
    private final Map<Component<?>, Provider<?>> components = new HashMap();
    private final AtomicReference<Boolean> eagerComponentsInitializedWith;
    private final EventBus eventBus;
    private final Map<Class<?>, Provider<?>> lazyInstanceMap = new HashMap();
    private final Map<Class<?>, LazySet<?>> lazySetMap = new HashMap();
    private final List<Provider<ComponentRegistrar>> unprocessedRegistrarProviders;

    private ComponentRuntime(Executor object, Iterable<Provider<ComponentRegistrar>> iterable, Collection<Component<?>> object2) {
        this.eagerComponentsInitializedWith = new AtomicReference();
        Object object3 = new EventBus((Executor)object);
        this.eventBus = object3;
        object = new ArrayList();
        object.add(Component.of(object3, EventBus.class, Subscriber.class, Publisher.class));
        object.add(Component.of(this, ComponentLoader.class, new Class[0]));
        object2 = object2.iterator();
        while (object2.hasNext()) {
            object3 = (Component)object2.next();
            if (object3 == null) continue;
            object.add(object3);
        }
        this.unprocessedRegistrarProviders = ComponentRuntime.iterableToList(iterable);
        this.discoverComponents((List<Component<?>>)object);
    }

    @Deprecated
    public ComponentRuntime(Executor executor, Iterable<ComponentRegistrar> iterable, Component<?> ... componentArray) {
        this(executor, ComponentRuntime.toProviders(iterable), Arrays.asList(componentArray));
    }

    public static Builder builder(Executor executor) {
        return new Builder(executor);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void discoverComponents(List<Component<?>> object) {
        ArrayList<Runnable> arrayList = new ArrayList<Runnable>();
        synchronized (this) {
            Iterator<Component<?>> iterator2;
            Object object2 = this.unprocessedRegistrarProviders.iterator();
            while (object2.hasNext()) {
                iterator2 = object2.next();
                try {
                    if ((iterator2 = iterator2.get()) == null) continue;
                    object.addAll(iterator2.getComponents());
                    object2.remove();
                }
                catch (InvalidRegistrarException invalidRegistrarException) {
                    object2.remove();
                    Log.w((String)"ComponentDiscovery", (String)"Invalid component registrar.", (Throwable)invalidRegistrarException);
                }
            }
            if (this.components.isEmpty()) {
                CycleDetector.detect(object);
            } else {
                object2 = new ArrayList(this.components.keySet());
                ((ArrayList)object2).addAll(object);
                CycleDetector.detect(object2);
            }
            iterator2 = object.iterator();
            while (iterator2.hasNext()) {
                object2 = iterator2.next();
                ComponentRuntime$$ExternalSyntheticLambda1 componentRuntime$$ExternalSyntheticLambda1 = new ComponentRuntime$$ExternalSyntheticLambda1(this, (Component)object2);
                Lazy<ComponentRuntime$$ExternalSyntheticLambda1> lazy = new Lazy<ComponentRuntime$$ExternalSyntheticLambda1>(componentRuntime$$ExternalSyntheticLambda1);
                this.components.put((Component<?>)object2, (Provider<?>)lazy);
            }
            arrayList.addAll(this.processInstanceComponents((List<Component<?>>)object));
            arrayList.addAll(this.processSetComponents());
            this.processDependencies();
            // MONITOREXIT @DISABLED, blocks:[0, 3] lbl38 : MonitorExitStatement: MONITOREXIT : this
            object = arrayList.iterator();
            {
                catch (Throwable throwable) {}
                {
                    throw throwable;
                }
            }
        }
        while (true) {
            if (!object.hasNext()) {
                this.maybeInitializeEagerComponents();
                return;
            }
            ((Runnable)object.next()).run();
        }
    }

    private void doInitializeEagerComponents(Map<Component<?>, Provider<?>> object, boolean bl) {
        for (Map.Entry entry : object.entrySet()) {
            Component component = (Component)entry.getKey();
            Provider object2 = (Provider)entry.getValue();
            if (!component.isAlwaysEager() && (!component.isEagerInDefaultApp() || !bl)) continue;
            object2.get();
        }
        this.eventBus.enablePublishingAndFlushPending();
    }

    private static <T> List<T> iterableToList(Iterable<T> object) {
        ArrayList arrayList = new ArrayList();
        object = object.iterator();
        while (object.hasNext()) {
            arrayList.add(object.next());
        }
        return arrayList;
    }

    static /* synthetic */ void lambda$processInstanceComponents$2(OptionalProvider optionalProvider, Provider provider) {
        optionalProvider.set(provider);
    }

    static /* synthetic */ void lambda$processSetComponents$3(LazySet lazySet, Provider provider) {
        lazySet.add(provider);
    }

    static /* synthetic */ ComponentRegistrar lambda$toProviders$1(ComponentRegistrar componentRegistrar) {
        return componentRegistrar;
    }

    private void maybeInitializeEagerComponents() {
        Boolean bl = this.eagerComponentsInitializedWith.get();
        if (bl != null) {
            this.doInitializeEagerComponents(this.components, bl);
        }
    }

    private void processDependencies() {
        for (Component<?> component : this.components.keySet()) {
            for (Dependency dependency : component.getDependencies()) {
                if (dependency.isSet() && !this.lazySetMap.containsKey(dependency.getInterface())) {
                    this.lazySetMap.put(dependency.getInterface(), LazySet.fromCollection(Collections.<Provider<?>>emptySet()));
                    continue;
                }
                if (this.lazyInstanceMap.containsKey(dependency.getInterface())) continue;
                if (!dependency.isRequired()) {
                    if (dependency.isSet()) continue;
                    this.lazyInstanceMap.put(dependency.getInterface(), OptionalProvider.empty());
                    continue;
                }
                throw new MissingDependencyException(String.format("Unsatisfied dependency for component %s: %s", component, dependency.getInterface()));
            }
        }
    }

    private List<Runnable> processInstanceComponents(List<Component<?>> object) {
        ArrayList<Runnable> arrayList = new ArrayList<Runnable>();
        object = object.iterator();
        while (object.hasNext()) {
            Class clazz2 = (Component)object.next();
            if (!((Component)((Object)clazz2)).isValue()) continue;
            Provider<?> provider = this.components.get(clazz2);
            for (Class clazz2 : ((Component)((Object)clazz2)).getProvidedInterfaces()) {
                if (!this.lazyInstanceMap.containsKey(clazz2)) {
                    this.lazyInstanceMap.put(clazz2, provider);
                    continue;
                }
                arrayList.add(new ComponentRuntime$$ExternalSyntheticLambda4((OptionalProvider)this.lazyInstanceMap.get(clazz2), provider));
            }
        }
        return arrayList;
    }

    private List<Runnable> processSetComponents() {
        ArrayList<Runnable> arrayList = new ArrayList<Runnable>();
        HashMap hashMap = new HashMap();
        for (Map.Entry<Component<?>, Provider<?>> entry : this.components.entrySet()) {
            Component<?> component = entry.getKey();
            if (component.isValue()) continue;
            Provider<?> provider = entry.getValue();
            for (Class<?> clazz : component.getProvidedInterfaces()) {
                if (!hashMap.containsKey(clazz)) {
                    hashMap.put(clazz, new HashSet());
                }
                ((Set)hashMap.get(clazz)).add(provider);
            }
        }
        for (Map.Entry entry : hashMap.entrySet()) {
            if (!this.lazySetMap.containsKey(entry.getKey())) {
                this.lazySetMap.put((Class)entry.getKey(), LazySet.fromCollection((Collection)entry.getValue()));
                continue;
            }
            LazySet<?> lazySet = this.lazySetMap.get(entry.getKey());
            Iterator iterator2 = ((Set)entry.getValue()).iterator();
            while (iterator2.hasNext()) {
                arrayList.add(new ComponentRuntime$$ExternalSyntheticLambda3(lazySet, (Provider)iterator2.next()));
            }
        }
        return arrayList;
    }

    private static Iterable<Provider<ComponentRegistrar>> toProviders(Iterable<ComponentRegistrar> object) {
        ArrayList<Provider<ComponentRegistrar>> arrayList = new ArrayList<Provider<ComponentRegistrar>>();
        object = object.iterator();
        while (object.hasNext()) {
            arrayList.add(new ComponentRuntime$$ExternalSyntheticLambda0((ComponentRegistrar)object.next()));
        }
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void discoverComponents() {
        synchronized (this) {
            if (this.unprocessedRegistrarProviders.isEmpty()) {
                return;
            }
        }
        this.discoverComponents(new ArrayList());
    }

    @Override
    public <T> Deferred<T> getDeferred(Class<T> object) {
        if ((object = this.getProvider((Class<T>)object)) == null) {
            return OptionalProvider.empty();
        }
        if (object instanceof OptionalProvider) {
            return (OptionalProvider)object;
        }
        return OptionalProvider.of(object);
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> object) {
        synchronized (this) {
            Preconditions.checkNotNull(object, "Null interface requested.");
            object = this.lazyInstanceMap.get(object);
            return object;
        }
    }

    public void initializeAllComponentsForTests() {
        Iterator<Provider<?>> iterator2 = this.components.values().iterator();
        while (iterator2.hasNext()) {
            iterator2.next().get();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void initializeEagerComponents(boolean bl) {
        HashMap hashMap;
        if (!this.eagerComponentsInitializedWith.compareAndSet(null, bl)) {
            return;
        }
        synchronized (this) {
            hashMap = new HashMap(this.components);
        }
        this.doInitializeEagerComponents(hashMap, bl);
    }

    public /* synthetic */ Object lambda$discoverComponents$0$com-google-firebase-components-ComponentRuntime(Component component) {
        return component.getFactory().create(new RestrictedComponentContainer(component, this));
    }

    @Override
    public <T> Provider<Set<T>> setOfProvider(Class<T> provider) {
        synchronized (this) {
            block4: {
                provider = this.lazySetMap.get(provider);
                if (provider == null) break block4;
                return provider;
            }
            provider = EMPTY_PROVIDER;
            return provider;
        }
    }

    public static final class Builder {
        private final List<Component<?>> additionalComponents;
        private final Executor defaultExecutor;
        private final List<Provider<ComponentRegistrar>> lazyRegistrars = new ArrayList<Provider<ComponentRegistrar>>();

        Builder(Executor executor) {
            this.additionalComponents = new ArrayList();
            this.defaultExecutor = executor;
        }

        static /* synthetic */ ComponentRegistrar lambda$addComponentRegistrar$0(ComponentRegistrar componentRegistrar) {
            return componentRegistrar;
        }

        public Builder addComponent(Component<?> component) {
            this.additionalComponents.add(component);
            return this;
        }

        public Builder addComponentRegistrar(ComponentRegistrar componentRegistrar) {
            this.lazyRegistrars.add(new ComponentRuntime$Builder$$ExternalSyntheticLambda0(componentRegistrar));
            return this;
        }

        public Builder addLazyComponentRegistrars(Collection<Provider<ComponentRegistrar>> collection) {
            this.lazyRegistrars.addAll(collection);
            return this;
        }

        public ComponentRuntime build() {
            return new ComponentRuntime(this.defaultExecutor, this.lazyRegistrars, this.additionalComponents);
        }
    }
}

