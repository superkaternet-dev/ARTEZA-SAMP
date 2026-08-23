/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

import com.google.firebase.components.Component;
import com.google.firebase.components.Dependency;
import com.google.firebase.components.DependencyCycleException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class CycleDetector {
    CycleDetector() {
    }

    static void detect(List<Component<?>> object) {
        Object object2;
        Object object3 = CycleDetector.toGraph(object);
        Set<ComponentNode> set = CycleDetector.getRoots(object3);
        int n = 0;
        while (!set.isEmpty()) {
            object2 = set.iterator().next();
            set.remove(object2);
            ++n;
            for (ComponentNode componentNode : ((ComponentNode)object2).getDependencies()) {
                componentNode.removeDependent((ComponentNode)object2);
                if (!componentNode.isRoot()) continue;
                set.add(componentNode);
            }
        }
        if (n == object.size()) {
            return;
        }
        object = new ArrayList();
        object2 = object3.iterator();
        while (object2.hasNext()) {
            object3 = (ComponentNode)object2.next();
            if (((ComponentNode)object3).isRoot() || ((ComponentNode)object3).isLeaf()) continue;
            object.add(((ComponentNode)object3).getComponent());
        }
        object = new DependencyCycleException((List<Component<?>>)object);
        throw object;
    }

    private static Set<ComponentNode> getRoots(Set<ComponentNode> object) {
        HashSet<ComponentNode> hashSet = new HashSet<ComponentNode>();
        object = object.iterator();
        while (object.hasNext()) {
            ComponentNode componentNode = (ComponentNode)object.next();
            if (!componentNode.isRoot()) continue;
            hashSet.add(componentNode);
        }
        return hashSet;
    }

    private static Set<ComponentNode> toGraph(List<Component<?>> object) {
        Object object3 = new HashMap(object.size());
        object = object.iterator();
        while (object.hasNext()) {
            Component component = (Component)object.next();
            ComponentNode componentNode = new ComponentNode(component);
            for (Class clazz : component.getProvidedInterfaces()) {
                Object object2;
                Object object4 = new Dep(clazz, component.isValue() ^ true);
                if (!object3.containsKey(object4)) {
                    object3.put(object4, new HashSet());
                }
                if (!(object2 = (Set)object3.get(object4)).isEmpty() && !((Dep)object4).set) {
                    throw new IllegalArgumentException(String.format("Multiple components provide %s.", clazz));
                }
                object2.add(componentNode);
            }
        }
        object = object3.values().iterator();
        while (object.hasNext()) {
            for (Object object4 : (Set)object.next()) {
                for (Dependency dependency : ((ComponentNode)object4).getComponent().getDependencies()) {
                    Set set;
                    if (!dependency.isDirectInjection() || (set = (Set)object3.get(new Dep(dependency.getInterface(), dependency.isSet()))) == null) continue;
                    for (ComponentNode componentNode : set) {
                        ((ComponentNode)object4).addDependency(componentNode);
                        componentNode.addDependent((ComponentNode)object4);
                    }
                }
            }
        }
        object = new HashSet();
        object3 = object3.values().iterator();
        while (object3.hasNext()) {
            ((AbstractCollection)object).addAll((Set)object3.next());
        }
        return object;
    }

    private static class ComponentNode {
        private final Component<?> component;
        private final Set<ComponentNode> dependencies = new HashSet<ComponentNode>();
        private final Set<ComponentNode> dependents = new HashSet<ComponentNode>();

        ComponentNode(Component<?> component) {
            this.component = component;
        }

        void addDependency(ComponentNode componentNode) {
            this.dependencies.add(componentNode);
        }

        void addDependent(ComponentNode componentNode) {
            this.dependents.add(componentNode);
        }

        Component<?> getComponent() {
            return this.component;
        }

        Set<ComponentNode> getDependencies() {
            return this.dependencies;
        }

        boolean isLeaf() {
            return this.dependencies.isEmpty();
        }

        boolean isRoot() {
            return this.dependents.isEmpty();
        }

        void removeDependent(ComponentNode componentNode) {
            this.dependents.remove(componentNode);
        }
    }

    private static class Dep {
        private final Class<?> anInterface;
        private final boolean set;

        private Dep(Class<?> clazz, boolean bl) {
            this.anInterface = clazz;
            this.set = bl;
        }

        public boolean equals(Object object) {
            boolean bl = object instanceof Dep;
            boolean bl2 = false;
            if (bl) {
                object = (Dep)object;
                bl = bl2;
                if (((Dep)object).anInterface.equals(this.anInterface)) {
                    bl = bl2;
                    if (((Dep)object).set == this.set) {
                        bl = true;
                    }
                }
                return bl;
            }
            return false;
        }

        public int hashCode() {
            return (0xF4243 ^ this.anInterface.hashCode()) * 1000003 ^ Boolean.valueOf(this.set).hashCode();
        }
    }
}

