/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import java.util.Arrays;
import kotlin.KotlinNullPointerException;
import kotlin.UninitializedPropertyAccessException;

public class Intrinsics {
    private Intrinsics() {
    }

    public static boolean areEqual(double d, Double d2) {
        boolean bl = d2 != null && d == d2;
        return bl;
    }

    public static boolean areEqual(float f, Float f2) {
        boolean bl = f2 != null && f == f2.floatValue();
        return bl;
    }

    public static boolean areEqual(Double d, double d2) {
        boolean bl = d != null && d == d2;
        return bl;
    }

    public static boolean areEqual(Double d, Double d2) {
        boolean bl = true;
        if (!(d == null ? d2 == null : d2 != null && d.doubleValue() == d2.doubleValue())) {
            bl = false;
        }
        return bl;
    }

    public static boolean areEqual(Float f, float f2) {
        boolean bl = f != null && f.floatValue() == f2;
        return bl;
    }

    public static boolean areEqual(Float f, Float f2) {
        boolean bl = true;
        if (!(f == null ? f2 == null : f2 != null && f.floatValue() == f2.floatValue())) {
            bl = false;
        }
        return bl;
    }

    public static boolean areEqual(Object object, Object object2) {
        boolean bl = object == null ? object2 == null : object.equals(object2);
        return bl;
    }

    public static void checkExpressionValueIsNotNull(Object object, String string2) {
        if (object != null) {
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append(string2);
        ((StringBuilder)object).append(" must not be null");
        throw Intrinsics.sanitizeStackTrace(new IllegalStateException(((StringBuilder)object).toString()));
    }

    public static void checkFieldIsNotNull(Object object, String string2) {
        if (object != null) {
            return;
        }
        throw Intrinsics.sanitizeStackTrace(new IllegalStateException(string2));
    }

    public static void checkFieldIsNotNull(Object object, String string2, String string3) {
        if (object != null) {
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Field specified as non-null is null: ");
        ((StringBuilder)object).append(string2);
        ((StringBuilder)object).append(".");
        ((StringBuilder)object).append(string3);
        throw Intrinsics.sanitizeStackTrace(new IllegalStateException(((StringBuilder)object).toString()));
    }

    public static void checkHasClass(String string2) throws ClassNotFoundException {
        String string3 = string2.replace('/', '.');
        try {
            Class.forName(string3);
            return;
        }
        catch (ClassNotFoundException classNotFoundException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Class ");
            stringBuilder.append(string3);
            stringBuilder.append(" is not found. Please update the Kotlin runtime to the latest version");
            throw Intrinsics.sanitizeStackTrace(new ClassNotFoundException(stringBuilder.toString(), classNotFoundException));
        }
    }

    public static void checkHasClass(String string2, String string3) throws ClassNotFoundException {
        string2 = string2.replace('/', '.');
        try {
            Class.forName(string2);
            return;
        }
        catch (ClassNotFoundException classNotFoundException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Class ");
            stringBuilder.append(string2);
            stringBuilder.append(" is not found: this code requires the Kotlin runtime of version at least ");
            stringBuilder.append(string3);
            throw Intrinsics.sanitizeStackTrace(new ClassNotFoundException(stringBuilder.toString(), classNotFoundException));
        }
    }

    public static void checkNotNull(Object object) {
        if (object == null) {
            Intrinsics.throwJavaNpe();
        }
    }

    public static void checkNotNull(Object object, String string2) {
        if (object == null) {
            Intrinsics.throwJavaNpe(string2);
        }
    }

    public static void checkNotNullExpressionValue(Object object, String string2) {
        if (object != null) {
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append(string2);
        ((StringBuilder)object).append(" must not be null");
        throw Intrinsics.sanitizeStackTrace(new NullPointerException(((StringBuilder)object).toString()));
    }

    public static void checkNotNullParameter(Object object, String string2) {
        if (object == null) {
            Intrinsics.throwParameterIsNullNPE(string2);
        }
    }

    public static void checkParameterIsNotNull(Object object, String string2) {
        if (object == null) {
            Intrinsics.throwParameterIsNullIAE(string2);
        }
    }

    public static void checkReturnedValueIsNotNull(Object object, String string2) {
        if (object != null) {
            return;
        }
        throw Intrinsics.sanitizeStackTrace(new IllegalStateException(string2));
    }

    public static void checkReturnedValueIsNotNull(Object object, String string2, String string3) {
        if (object != null) {
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Method specified as non-null returned null: ");
        ((StringBuilder)object).append(string2);
        ((StringBuilder)object).append(".");
        ((StringBuilder)object).append(string3);
        throw Intrinsics.sanitizeStackTrace(new IllegalStateException(((StringBuilder)object).toString()));
    }

    public static int compare(int n, int n2) {
        n = n < n2 ? -1 : (n == n2 ? 0 : 1);
        return n;
    }

    public static int compare(long l, long l2) {
        int n = l < l2 ? -1 : (l == l2 ? 0 : 1);
        return n;
    }

    private static String createParameterIsNullExceptionMessage(String string2) {
        Object object = Thread.currentThread().getStackTrace()[4];
        String string3 = ((StackTraceElement)object).getClassName();
        object = ((StackTraceElement)object).getMethodName();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Parameter specified as non-null is null: method ");
        stringBuilder.append(string3);
        stringBuilder.append(".");
        stringBuilder.append((String)object);
        stringBuilder.append(", parameter ");
        stringBuilder.append(string2);
        return stringBuilder.toString();
    }

    public static void needClassReification() {
        Intrinsics.throwUndefinedForReified();
    }

    public static void needClassReification(String string2) {
        Intrinsics.throwUndefinedForReified(string2);
    }

    public static void reifiedOperationMarker(int n, String string2) {
        Intrinsics.throwUndefinedForReified();
    }

    public static void reifiedOperationMarker(int n, String string2, String string3) {
        Intrinsics.throwUndefinedForReified(string3);
    }

    private static <T extends Throwable> T sanitizeStackTrace(T t) {
        return Intrinsics.sanitizeStackTrace(t, Intrinsics.class.getName());
    }

    static <T extends Throwable> T sanitizeStackTrace(T t, String string2) {
        StackTraceElement[] stackTraceElementArray = t.getStackTrace();
        int n = stackTraceElementArray.length;
        int n2 = -1;
        for (int i = 0; i < n; ++i) {
            if (!string2.equals(stackTraceElementArray[i].getClassName())) continue;
            n2 = i;
        }
        t.setStackTrace(Arrays.copyOfRange(stackTraceElementArray, n2 + 1, n));
        return t;
    }

    public static String stringPlus(String string2, Object object) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(object);
        return stringBuilder.toString();
    }

    public static void throwAssert() {
        throw Intrinsics.sanitizeStackTrace(new AssertionError());
    }

    public static void throwAssert(String string2) {
        throw Intrinsics.sanitizeStackTrace(new AssertionError((Object)string2));
    }

    public static void throwIllegalArgument() {
        throw Intrinsics.sanitizeStackTrace(new IllegalArgumentException());
    }

    public static void throwIllegalArgument(String string2) {
        throw Intrinsics.sanitizeStackTrace(new IllegalArgumentException(string2));
    }

    public static void throwIllegalState() {
        throw Intrinsics.sanitizeStackTrace(new IllegalStateException());
    }

    public static void throwIllegalState(String string2) {
        throw Intrinsics.sanitizeStackTrace(new IllegalStateException(string2));
    }

    public static void throwJavaNpe() {
        throw Intrinsics.sanitizeStackTrace(new NullPointerException());
    }

    public static void throwJavaNpe(String string2) {
        throw Intrinsics.sanitizeStackTrace(new NullPointerException(string2));
    }

    public static void throwNpe() {
        throw Intrinsics.sanitizeStackTrace(new KotlinNullPointerException());
    }

    public static void throwNpe(String string2) {
        throw Intrinsics.sanitizeStackTrace(new KotlinNullPointerException(string2));
    }

    private static void throwParameterIsNullIAE(String string2) {
        throw Intrinsics.sanitizeStackTrace(new IllegalArgumentException(Intrinsics.createParameterIsNullExceptionMessage(string2)));
    }

    private static void throwParameterIsNullNPE(String string2) {
        throw Intrinsics.sanitizeStackTrace(new NullPointerException(Intrinsics.createParameterIsNullExceptionMessage(string2)));
    }

    public static void throwUndefinedForReified() {
        Intrinsics.throwUndefinedForReified("This function has a reified type parameter and thus can only be inlined at compilation time, not called directly.");
    }

    public static void throwUndefinedForReified(String string2) {
        throw new UnsupportedOperationException(string2);
    }

    public static void throwUninitializedProperty(String string2) {
        throw Intrinsics.sanitizeStackTrace(new UninitializedPropertyAccessException(string2));
    }

    public static void throwUninitializedPropertyAccessException(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("lateinit property ");
        stringBuilder.append(string2);
        stringBuilder.append(" has not been initialized");
        Intrinsics.throwUninitializedProperty(stringBuilder.toString());
    }

    public static class Kotlin {
        private Kotlin() {
        }
    }
}

