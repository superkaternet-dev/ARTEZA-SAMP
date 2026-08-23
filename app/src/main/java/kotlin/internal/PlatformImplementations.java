/*
 * Decompiled with CFR 0.152.
 */
package kotlin.internal;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.MatchResult;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.FallbackThreadLocalRandom;
import kotlin.random.Random;
import kotlin.text.MatchGroup;

@Metadata(bv={1, 0, 3}, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\b\u0010\u0018\u00002\u00020\u0001:\u0001\u0012B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\b\u001a\u00020\tH\u0016J\u001a\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00060\u00112\u0006\u0010\u0007\u001a\u00020\u0006H\u0016\u00a8\u0006\u0013"}, d2={"Lkotlin/internal/PlatformImplementations;", "", "()V", "addSuppressed", "", "cause", "", "exception", "defaultPlatformRandom", "Lkotlin/random/Random;", "getMatchResultNamedGroup", "Lkotlin/text/MatchGroup;", "matchResult", "Ljava/util/regex/MatchResult;", "name", "", "getSuppressed", "", "ReflectThrowable", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public class PlatformImplementations {
    public void addSuppressed(Throwable throwable, Throwable throwable2) {
        Intrinsics.checkNotNullParameter(throwable, "cause");
        Intrinsics.checkNotNullParameter(throwable2, "exception");
        Method method = ReflectThrowable.addSuppressed;
        if (method != null) {
            method.invoke((Object)throwable, throwable2);
        }
    }

    public Random defaultPlatformRandom() {
        return new FallbackThreadLocalRandom();
    }

    public MatchGroup getMatchResultNamedGroup(MatchResult matchResult, String string2) {
        Intrinsics.checkNotNullParameter(matchResult, "matchResult");
        Intrinsics.checkNotNullParameter(string2, "name");
        throw (Throwable)new UnsupportedOperationException("Retrieving groups by name is not supported on this platform.");
    }

    public List<Throwable> getSuppressed(Throwable list) {
        block2: {
            block0: {
                block1: {
                    Intrinsics.checkNotNullParameter(list, "exception");
                    Method method = ReflectThrowable.getSuppressed;
                    if (method == null || (list = method.invoke((Object)list, new Object[0])) == null) break block0;
                    if (list == null) break block1;
                    if ((list = ArraysKt.asList((Throwable[])list)) == null) break block0;
                    break block2;
                }
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<kotlin.Throwable>");
            }
            list = CollectionsKt.emptyList();
        }
        return list;
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c2\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2={"Lkotlin/internal/PlatformImplementations$ReflectThrowable;", "", "()V", "addSuppressed", "Ljava/lang/reflect/Method;", "getSuppressed", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    private static final class ReflectThrowable {
        public static final ReflectThrowable INSTANCE;
        public static final Method addSuppressed;
        public static final Method getSuppressed;

        /*
         * Unable to fully structure code
         */
        static {
            block5: {
                ReflectThrowable.INSTANCE = new ReflectThrowable();
                var6 = Throwable.class.getMethods();
                Intrinsics.checkNotNullExpressionValue(var6, "throwableMethods");
                var3_1 = var6.length;
                var2_2 = 0;
                var0_3 = 0;
                while (true) {
                    var5_6 = null;
                    if (var0_3 >= var3_1) break;
                    var4_5 = var6[var0_3];
                    Intrinsics.checkNotNullExpressionValue(var4_5, "it");
                    if (!Intrinsics.areEqual(var4_5.getName(), "addSuppressed")) ** GOTO lbl-1000
                    var7_7 = var4_5.getParameterTypes();
                    Intrinsics.checkNotNullExpressionValue(var7_7, "it.parameterTypes");
                    if (Intrinsics.areEqual(ArraysKt.singleOrNull(var7_7), Throwable.class)) {
                        var1_4 = 1;
                    } else lbl-1000:
                    // 2 sources

                    {
                        var1_4 = 0;
                    }
                    if (var1_4 == 0) {
                        ++var0_3;
                        continue;
                    }
                    break block5;
                    break;
                }
                var4_5 = null;
            }
            ReflectThrowable.addSuppressed = var4_5;
            var1_4 = var6.length;
            var0_3 = var2_2;
            while (true) {
                var4_5 = var5_6;
                if (var0_3 >= var1_4) break;
                var4_5 = var6[var0_3];
                Intrinsics.checkNotNullExpressionValue(var4_5, "it");
                if (Intrinsics.areEqual(var4_5.getName(), "getSuppressed")) break;
                ++var0_3;
            }
            ReflectThrowable.getSuppressed = var4_5;
        }

        private ReflectThrowable() {
        }
    }
}

