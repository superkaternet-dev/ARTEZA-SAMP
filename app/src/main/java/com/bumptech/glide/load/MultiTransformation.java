/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.bumptech.glide.load;

import android.content.Context;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class MultiTransformation<T>
implements Transformation<T> {
    private final Collection<? extends Transformation<T>> transformations;

    public MultiTransformation(Collection<? extends Transformation<T>> collection) {
        if (!collection.isEmpty()) {
            this.transformations = collection;
            return;
        }
        throw new IllegalArgumentException("MultiTransformation must contain at least one Transformation");
    }

    @SafeVarargs
    public MultiTransformation(Transformation<T> ... transformationArray) {
        if (transformationArray.length != 0) {
            this.transformations = Arrays.asList(transformationArray);
            return;
        }
        throw new IllegalArgumentException("MultiTransformation must contain at least one Transformation");
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof MultiTransformation) {
            object = (MultiTransformation)object;
            return this.transformations.equals(((MultiTransformation)object).transformations);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.transformations.hashCode();
    }

    @Override
    public Resource<T> transform(Context context, Resource<T> resource, int n, int n2) {
        Resource<T> resource2 = resource;
        Iterator<Transformation<T>> iterator2 = this.transformations.iterator();
        while (iterator2.hasNext()) {
            Resource<T> resource3 = iterator2.next().transform(context, resource2, n, n2);
            if (resource2 != null && !resource2.equals(resource) && !resource2.equals(resource3)) {
                resource2.recycle();
            }
            resource2 = resource3;
        }
        return resource2;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        Iterator<Transformation<T>> iterator2 = this.transformations.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().updateDiskCacheKey(messageDigest);
        }
    }
}

