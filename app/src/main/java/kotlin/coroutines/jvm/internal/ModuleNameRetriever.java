/*
 * Decompiled with CFR 0.152.
 */
package kotlin.coroutines.jvm.internal;

import java.lang.reflect.Method;
import kotlin.Metadata;
import kotlin.coroutines.jvm.internal.BaseContinuationImpl;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u00c2\u0002\u0018\u00002\u00020\u0001:\u0001\u000bB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0010\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0007\u001a\u00020\bR\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2={"Lkotlin/coroutines/jvm/internal/ModuleNameRetriever;", "", "()V", "cache", "Lkotlin/coroutines/jvm/internal/ModuleNameRetriever$Cache;", "notOnJava9", "buildCache", "continuation", "Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;", "getModuleName", "", "Cache", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
final class ModuleNameRetriever {
    public static final ModuleNameRetriever INSTANCE = new ModuleNameRetriever();
    public static Cache cache;
    private static final Cache notOnJava9;

    static {
        notOnJava9 = new Cache(null, null, null);
    }

    private ModuleNameRetriever() {
    }

    private final Cache buildCache(BaseContinuationImpl object) {
        try {
            Method method = Class.class.getDeclaredMethod("getModule", new Class[0]);
            Method method2 = object.getClass().getClassLoader().loadClass("java.lang.Module").getDeclaredMethod("getDescriptor", new Class[0]);
            Method method3 = object.getClass().getClassLoader().loadClass("java.lang.module.ModuleDescriptor").getDeclaredMethod("name", new Class[0]);
            cache = object = new Cache(method, method2, method3);
            return object;
        }
        catch (Exception exception) {
            Cache cache;
            ModuleNameRetriever.cache = cache = notOnJava9;
            return cache;
        }
    }

    public final String getModuleName(BaseContinuationImpl object) {
        Intrinsics.checkNotNullParameter(object, "continuation");
        Object object2 = cache;
        if (object2 == null) {
            object2 = this.buildCache((BaseContinuationImpl)object);
        }
        Object object3 = notOnJava9;
        Object var3_4 = null;
        if (object2 == object3) {
            return null;
        }
        object3 = ((Cache)object2).getModuleMethod;
        if (object3 != null && (object = ((Method)object3).invoke(object.getClass(), new Object[0])) != null) {
            object3 = ((Cache)object2).getDescriptorMethod;
            if (object3 != null && (object = ((Method)object3).invoke(object, new Object[0])) != null) {
                object2 = ((Cache)object2).nameMethod;
                object = object2 != null ? ((Method)object2).invoke(object, new Object[0]) : null;
                if (!(object instanceof String)) {
                    object = var3_4;
                }
                return (String)object;
            }
            return null;
        }
        return null;
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B#\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0006R\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2={"Lkotlin/coroutines/jvm/internal/ModuleNameRetriever$Cache;", "", "getModuleMethod", "Ljava/lang/reflect/Method;", "getDescriptorMethod", "nameMethod", "(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;)V", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    private static final class Cache {
        public final Method getDescriptorMethod;
        public final Method getModuleMethod;
        public final Method nameMethod;

        public Cache(Method method, Method method2, Method method3) {
            this.getModuleMethod = method;
            this.getDescriptorMethod = method2;
            this.nameMethod = method3;
        }
    }
}

