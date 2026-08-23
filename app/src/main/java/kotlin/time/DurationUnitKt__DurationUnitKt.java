/*
 * Decompiled with CFR 0.152.
 */
package kotlin.time;

import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.time.DurationUnitKt$WhenMappings;
import kotlin.time.DurationUnitKt__DurationUnitJvmKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0000\u001a\u00020\u0001*\u00060\u0002j\u0002`\u0003H\u0001\u00a8\u0006\u0004"}, d2={"shortName", "", "Ljava/util/concurrent/TimeUnit;", "Lkotlin/time/DurationUnit;", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/time/DurationUnitKt")
class DurationUnitKt__DurationUnitKt
extends DurationUnitKt__DurationUnitJvmKt {
    public static final String shortName(TimeUnit object) {
        Intrinsics.checkNotNullParameter(object, "$this$shortName");
        switch (DurationUnitKt$WhenMappings.$EnumSwitchMapping$0[object.ordinal()]) {
            default: {
                throw new NoWhenBranchMatchedException();
            }
            case 7: {
                object = "d";
                break;
            }
            case 6: {
                object = "h";
                break;
            }
            case 5: {
                object = "m";
                break;
            }
            case 4: {
                object = "s";
                break;
            }
            case 3: {
                object = "ms";
                break;
            }
            case 2: {
                object = "us";
                break;
            }
            case 1: {
                object = "ns";
            }
        }
        return object;
    }
}

