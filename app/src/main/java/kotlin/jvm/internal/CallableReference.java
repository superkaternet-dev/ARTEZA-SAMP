/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import kotlin.jvm.KotlinReflectionNotSupportedError;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KCallable;
import kotlin.reflect.KDeclarationContainer;
import kotlin.reflect.KParameter;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeParameter;
import kotlin.reflect.KVisibility;

public abstract class CallableReference
implements KCallable,
Serializable {
    public static final Object NO_RECEIVER = NoReceiver.access$000();
    private final boolean isTopLevel;
    private final String name;
    private final Class owner;
    protected final Object receiver;
    private transient KCallable reflected;
    private final String signature;

    public CallableReference() {
        this(NO_RECEIVER);
    }

    protected CallableReference(Object object) {
        this(object, null, null, null, false);
    }

    protected CallableReference(Object object, Class clazz, String string2, String string3, boolean bl) {
        this.receiver = object;
        this.owner = clazz;
        this.name = string2;
        this.signature = string3;
        this.isTopLevel = bl;
    }

    public Object call(Object ... objectArray) {
        return this.getReflected().call(objectArray);
    }

    public Object callBy(Map map) {
        return this.getReflected().callBy(map);
    }

    public KCallable compute() {
        KCallable kCallable;
        KCallable kCallable2 = kCallable = this.reflected;
        if (kCallable == null) {
            this.reflected = kCallable2 = this.computeReflected();
        }
        return kCallable2;
    }

    protected abstract KCallable computeReflected();

    @Override
    public List<Annotation> getAnnotations() {
        return this.getReflected().getAnnotations();
    }

    public Object getBoundReceiver() {
        return this.receiver;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public KDeclarationContainer getOwner() {
        Object object = this.owner;
        object = object == null ? null : (this.isTopLevel ? Reflection.getOrCreateKotlinPackage((Class)object) : Reflection.getOrCreateKotlinClass((Class)object));
        return object;
    }

    @Override
    public List<KParameter> getParameters() {
        return this.getReflected().getParameters();
    }

    protected KCallable getReflected() {
        KCallable kCallable = this.compute();
        if (kCallable != this) {
            return kCallable;
        }
        throw new KotlinReflectionNotSupportedError();
    }

    @Override
    public KType getReturnType() {
        return this.getReflected().getReturnType();
    }

    public String getSignature() {
        return this.signature;
    }

    @Override
    public List<KTypeParameter> getTypeParameters() {
        return this.getReflected().getTypeParameters();
    }

    @Override
    public KVisibility getVisibility() {
        return this.getReflected().getVisibility();
    }

    @Override
    public boolean isAbstract() {
        return this.getReflected().isAbstract();
    }

    @Override
    public boolean isFinal() {
        return this.getReflected().isFinal();
    }

    @Override
    public boolean isOpen() {
        return this.getReflected().isOpen();
    }

    @Override
    public boolean isSuspend() {
        return this.getReflected().isSuspend();
    }

    private static class NoReceiver
    implements Serializable {
        private static final NoReceiver INSTANCE = new NoReceiver();

        private NoReceiver() {
        }

        static /* synthetic */ NoReceiver access$000() {
            return INSTANCE;
        }

        private Object readResolve() throws ObjectStreamException {
            return INSTANCE;
        }
    }
}

