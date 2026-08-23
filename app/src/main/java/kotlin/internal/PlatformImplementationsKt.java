/*
 * Decompiled with CFR 0.152.
 */
package kotlin.internal;

import kotlin.KotlinVersion;
import kotlin.Metadata;
import kotlin.internal.PlatformImplementations;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0004\u001a \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H\u0001\u001a\"\u0010\b\u001a\u0002H\t\"\n\b\u0000\u0010\t\u0018\u0001*\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0083\b\u00a2\u0006\u0002\u0010\f\u001a\b\u0010\r\u001a\u00020\u0005H\u0002\"\u0010\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2={"IMPLEMENTATIONS", "Lkotlin/internal/PlatformImplementations;", "apiVersionIsAtLeast", "", "major", "", "minor", "patch", "castToBaseType", "T", "", "instance", "(Ljava/lang/Object;)Ljava/lang/Object;", "getJavaVersion", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class PlatformImplementationsKt {
    public static final PlatformImplementations IMPLEMENTATIONS;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static {
        PlatformImplementations platformImplementations;
        block22: {
            int n = PlatformImplementationsKt.getJavaVersion();
            if (n >= 65544) {
                try {
                    ClassCastException classCastException2;
                    Object object;
                    block23: {
                        object = Class.forName("kotlin.internal.jdk8.JDK8PlatformImplementations").newInstance();
                        Intrinsics.checkNotNullExpressionValue(object, "Class.forName(\"kotlin.in\u2026entations\").newInstance()");
                        if (object != null) {
                            try {
                                platformImplementations = (PlatformImplementations)object;
                                break block22;
                            }
                            catch (ClassCastException classCastException2) {
                                break block23;
                            }
                        }
                        NullPointerException nullPointerException = new NullPointerException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                        throw nullPointerException;
                    }
                    ClassLoader classLoader = object.getClass().getClassLoader();
                    object = PlatformImplementations.class.getClassLoader();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Instance classloader: ");
                    stringBuilder.append(classLoader);
                    stringBuilder.append(", base type classloader: ");
                    stringBuilder.append(object);
                    ClassCastException classCastException3 = new ClassCastException(stringBuilder.toString());
                    Throwable throwable = classCastException3.initCause(classCastException2);
                    Intrinsics.checkNotNullExpressionValue(throwable, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
                    throw throwable;
                }
                catch (ClassNotFoundException classNotFoundException) {
                    try {
                        ClassCastException classCastException4;
                        Object object;
                        block24: {
                            object = Class.forName("kotlin.internal.JRE8PlatformImplementations").newInstance();
                            Intrinsics.checkNotNullExpressionValue(object, "Class.forName(\"kotlin.in\u2026entations\").newInstance()");
                            if (object != null) {
                                try {
                                    platformImplementations = (PlatformImplementations)object;
                                    break block22;
                                }
                                catch (ClassCastException classCastException4) {
                                    break block24;
                                }
                            }
                            NullPointerException nullPointerException = new NullPointerException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                            throw nullPointerException;
                        }
                        ClassLoader classLoader = object.getClass().getClassLoader();
                        ClassLoader classLoader2 = PlatformImplementations.class.getClassLoader();
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Instance classloader: ");
                        ((StringBuilder)object).append(classLoader);
                        ((StringBuilder)object).append(", base type classloader: ");
                        ((StringBuilder)object).append(classLoader2);
                        ClassCastException classCastException5 = new ClassCastException(((StringBuilder)object).toString());
                        Throwable throwable = classCastException5.initCause(classCastException4);
                        Intrinsics.checkNotNullExpressionValue(throwable, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
                        throw throwable;
                    }
                    catch (ClassNotFoundException classNotFoundException2) {
                        // empty catch block
                    }
                }
            }
            if (n >= 65543) {
                try {
                    ClassCastException classCastException6;
                    Object object;
                    block25: {
                        object = Class.forName("kotlin.internal.jdk7.JDK7PlatformImplementations").newInstance();
                        Intrinsics.checkNotNullExpressionValue(object, "Class.forName(\"kotlin.in\u2026entations\").newInstance()");
                        if (object != null) {
                            try {
                                platformImplementations = (PlatformImplementations)object;
                                break block22;
                            }
                            catch (ClassCastException classCastException6) {
                                break block25;
                            }
                        }
                        NullPointerException nullPointerException = new NullPointerException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                        throw nullPointerException;
                    }
                    object = object.getClass().getClassLoader();
                    ClassLoader classLoader = PlatformImplementations.class.getClassLoader();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Instance classloader: ");
                    stringBuilder.append(object);
                    stringBuilder.append(", base type classloader: ");
                    stringBuilder.append(classLoader);
                    ClassCastException classCastException7 = new ClassCastException(stringBuilder.toString());
                    Throwable throwable = classCastException7.initCause(classCastException6);
                    Intrinsics.checkNotNullExpressionValue(throwable, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
                    throw throwable;
                }
                catch (ClassNotFoundException classNotFoundException) {
                    try {
                        ClassCastException classCastException8;
                        Object object;
                        block26: {
                            object = Class.forName("kotlin.internal.JRE7PlatformImplementations").newInstance();
                            Intrinsics.checkNotNullExpressionValue(object, "Class.forName(\"kotlin.in\u2026entations\").newInstance()");
                            if (object != null) {
                                try {
                                    platformImplementations = (PlatformImplementations)object;
                                    break block22;
                                }
                                catch (ClassCastException classCastException8) {
                                    break block26;
                                }
                            }
                            NullPointerException nullPointerException = new NullPointerException("null cannot be cast to non-null type kotlin.internal.PlatformImplementations");
                            throw nullPointerException;
                        }
                        ClassLoader classLoader = object.getClass().getClassLoader();
                        ClassLoader classLoader3 = PlatformImplementations.class.getClassLoader();
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Instance classloader: ");
                        ((StringBuilder)object).append(classLoader);
                        ((StringBuilder)object).append(", base type classloader: ");
                        ((StringBuilder)object).append(classLoader3);
                        ClassCastException classCastException9 = new ClassCastException(((StringBuilder)object).toString());
                        Throwable throwable = classCastException9.initCause(classCastException8);
                        Intrinsics.checkNotNullExpressionValue(throwable, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
                        throw throwable;
                    }
                    catch (ClassNotFoundException classNotFoundException3) {
                        // empty catch block
                    }
                }
            }
            platformImplementations = new PlatformImplementations();
        }
        IMPLEMENTATIONS = platformImplementations;
    }

    public static final boolean apiVersionIsAtLeast(int n, int n2, int n3) {
        return KotlinVersion.CURRENT.isAtLeast(n, n2, n3);
    }

    private static final /* synthetic */ <T> T castToBaseType(Object object) {
        Object object2;
        try {
            Intrinsics.reifiedOperationMarker(1, "T");
            object2 = object;
        }
        catch (ClassCastException classCastException) {
            ClassLoader classLoader = object.getClass().getClassLoader();
            Intrinsics.reifiedOperationMarker(4, "T");
            object = Object.class.getClassLoader();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Instance classloader: ");
            stringBuilder.append(classLoader);
            stringBuilder.append(", base type classloader: ");
            stringBuilder.append(object);
            object = new ClassCastException(stringBuilder.toString()).initCause(classCastException);
            Intrinsics.checkNotNullExpressionValue(object, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
            throw object;
        }
        return (T)object2;
    }

    private static final int getJavaVersion() {
        String string2 = System.getProperty("java.specification.version");
        if (string2 != null) {
            int n;
            int n2 = StringsKt.indexOf$default((CharSequence)string2, '.', 0, false, 6, null);
            if (n2 < 0) {
                int n3;
                try {
                    n3 = Integer.parseInt(string2);
                    n3 *= 65536;
                }
                catch (NumberFormatException numberFormatException) {
                    n3 = 65542;
                }
                return n3;
            }
            int n4 = n = StringsKt.indexOf$default((CharSequence)string2, '.', n2 + 1, false, 4, null);
            if (n < 0) {
                n4 = string2.length();
            }
            if (string2 != null) {
                String string3 = string2.substring(0, n2);
                Intrinsics.checkNotNullExpressionValue(string3, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                if (string2 != null) {
                    string2 = string2.substring(n2 + 1, n4);
                    Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                    try {
                        n = Integer.parseInt(string3);
                        n4 = Integer.parseInt(string2);
                        n4 = n * 65536 + n4;
                    }
                    catch (NumberFormatException numberFormatException) {
                        n4 = 65542;
                    }
                    return n4;
                }
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        return 65542;
    }
}

