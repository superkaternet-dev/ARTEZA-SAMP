/*
 * Decompiled with CFR 0.152.
 */
package kotlin.coroutines.jvm.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.coroutines.jvm.internal.BaseContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.ModuleNameRetriever;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u00000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0002\u001a\u000e\u0010\u0006\u001a\u0004\u0018\u00010\u0007*\u00020\bH\u0002\u001a\f\u0010\t\u001a\u00020\u0001*\u00020\bH\u0002\u001a\u0019\u0010\n\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u000b*\u00020\bH\u0001\u00a2\u0006\u0002\u0010\r\u001a\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u000f*\u00020\bH\u0001\u00a2\u0006\u0002\b\u0010\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2={"COROUTINES_DEBUG_METADATA_VERSION", "", "checkDebugMetadataVersion", "", "expected", "actual", "getDebugMetadataAnnotation", "Lkotlin/coroutines/jvm/internal/DebugMetadata;", "Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;", "getLabel", "getSpilledVariableFieldMapping", "", "", "(Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;)[Ljava/lang/String;", "getStackTraceElementImpl", "Ljava/lang/StackTraceElement;", "getStackTraceElement", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class DebugMetadataKt {
    private static final int COROUTINES_DEBUG_METADATA_VERSION = 1;

    private static final void checkDebugMetadataVersion(int n, int n2) {
        if (n2 <= n) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Debug metadata version mismatch. Expected: ");
        stringBuilder.append(n);
        stringBuilder.append(", got ");
        stringBuilder.append(n2);
        stringBuilder.append(". Please update the Kotlin standard library.");
        throw (Throwable)new IllegalStateException(stringBuilder.toString().toString());
    }

    private static final DebugMetadata getDebugMetadataAnnotation(BaseContinuationImpl baseContinuationImpl) {
        return baseContinuationImpl.getClass().getAnnotation(DebugMetadata.class);
    }

    private static final int getLabel(BaseContinuationImpl object) {
        int n;
        block7: {
            block6: {
                block5: {
                    Object object2 = object.getClass().getDeclaredField("label");
                    Intrinsics.checkNotNullExpressionValue(object2, "field");
                    ((Field)object2).setAccessible(true);
                    object = object2 = ((Field)object2).get(object);
                    if (object2 instanceof Integer) break block5;
                    object = null;
                }
                object = (Integer)object;
                if (object == null) break block6;
                try {
                    n = (Integer)object;
                    break block7;
                }
                catch (Exception exception) {
                    n = -1;
                }
            }
            n = 0;
        }
        --n;
        return n;
    }

    public static final String[] getSpilledVariableFieldMapping(BaseContinuationImpl stringArray) {
        Intrinsics.checkNotNullParameter(stringArray, "$this$getSpilledVariableFieldMapping");
        DebugMetadata debugMetadata = DebugMetadataKt.getDebugMetadataAnnotation((BaseContinuationImpl)stringArray);
        if (debugMetadata != null) {
            DebugMetadataKt.checkDebugMetadataVersion(1, debugMetadata.v());
            ArrayList<String> arrayList = new ArrayList<String>();
            int n = DebugMetadataKt.getLabel((BaseContinuationImpl)stringArray);
            stringArray = (String[])debugMetadata.i();
            int n2 = stringArray.length;
            for (int i = 0; i < n2; ++i) {
                if (stringArray[i] != n) continue;
                arrayList.add(debugMetadata.s()[i]);
                arrayList.add(debugMetadata.n()[i]);
            }
            stringArray = ((Collection)arrayList).toArray(new String[0]);
            if (stringArray != null) {
                return stringArray;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return null;
    }

    public static final StackTraceElement getStackTraceElement(BaseContinuationImpl object) {
        Intrinsics.checkNotNullParameter(object, "$this$getStackTraceElementImpl");
        DebugMetadata debugMetadata = DebugMetadataKt.getDebugMetadataAnnotation((BaseContinuationImpl)object);
        if (debugMetadata != null) {
            DebugMetadataKt.checkDebugMetadataVersion(1, debugMetadata.v());
            int n = DebugMetadataKt.getLabel((BaseContinuationImpl)object);
            n = n < 0 ? -1 : debugMetadata.l()[n];
            object = ModuleNameRetriever.INSTANCE.getModuleName((BaseContinuationImpl)object);
            if (object == null) {
                object = debugMetadata.c();
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append((String)object);
                stringBuilder.append('/');
                stringBuilder.append(debugMetadata.c());
                object = stringBuilder.toString();
            }
            return new StackTraceElement((String)object, debugMetadata.m(), debugMetadata.f(), n);
        }
        return null;
    }
}

