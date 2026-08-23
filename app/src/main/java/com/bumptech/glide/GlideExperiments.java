/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GlideExperiments {
    private final Map<Class<?>, Experiment> experiments;

    GlideExperiments(Builder builder) {
        this.experiments = Collections.unmodifiableMap(new HashMap(builder.experiments));
    }

    <T extends Experiment> T get(Class<T> clazz) {
        return (T)this.experiments.get(clazz);
    }

    public boolean isEnabled(Class<? extends Experiment> clazz) {
        return this.experiments.containsKey(clazz);
    }

    static final class Builder {
        private final Map<Class<?>, Experiment> experiments = new HashMap();

        Builder() {
        }

        Builder add(Experiment experiment) {
            this.experiments.put(experiment.getClass(), experiment);
            return this;
        }

        GlideExperiments build() {
            return new GlideExperiments(this);
        }

        Builder update(Experiment experiment, boolean bl) {
            if (bl) {
                this.add(experiment);
            } else {
                this.experiments.remove(experiment.getClass());
            }
            return this;
        }
    }

    static interface Experiment {
    }
}

