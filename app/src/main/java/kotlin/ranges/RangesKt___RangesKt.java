/*
 * Decompiled with CFR 0.152.
 */
package kotlin.ranges;

import java.util.NoSuchElementException;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.random.RandomKt;
import kotlin.ranges.CharProgression;
import kotlin.ranges.CharRange;
import kotlin.ranges.ClosedFloatingPointRange;
import kotlin.ranges.ClosedRange;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.LongProgression;
import kotlin.ranges.LongRange;
import kotlin.ranges.RangesKt;
import kotlin.ranges.RangesKt__RangesKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000n\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001d\u001a'\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\u0006\u0010\u0003\u001a\u0002H\u0001\u00a2\u0006\u0002\u0010\u0004\u001a\u0012\u0010\u0000\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005\u001a\u0012\u0010\u0000\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0006\u001a\u0012\u0010\u0000\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0007\u001a\u0012\u0010\u0000\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\b\u001a\u0012\u0010\u0000\u001a\u00020\t*\u00020\t2\u0006\u0010\u0003\u001a\u00020\t\u001a\u0012\u0010\u0000\u001a\u00020\n*\u00020\n2\u0006\u0010\u0003\u001a\u00020\n\u001a'\u0010\u000b\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\u0006\u0010\f\u001a\u0002H\u0001\u00a2\u0006\u0002\u0010\u0004\u001a\u0012\u0010\u000b\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005\u001a\u0012\u0010\u000b\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006\u001a\u0012\u0010\u000b\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007\u001a\u0012\u0010\u000b\u001a\u00020\b*\u00020\b2\u0006\u0010\f\u001a\u00020\b\u001a\u0012\u0010\u000b\u001a\u00020\t*\u00020\t2\u0006\u0010\f\u001a\u00020\t\u001a\u0012\u0010\u000b\u001a\u00020\n*\u00020\n2\u0006\u0010\f\u001a\u00020\n\u001a3\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\b\u0010\u0003\u001a\u0004\u0018\u0001H\u00012\b\u0010\f\u001a\u0004\u0018\u0001H\u0001\u00a2\u0006\u0002\u0010\u000e\u001a/\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0010H\u0007\u00a2\u0006\u0002\u0010\u0011\u001a-\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0012\u00a2\u0006\u0002\u0010\u0013\u001a\u001a\u0010\r\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005\u001a\u001a\u0010\r\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006\u001a\u001a\u0010\r\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007\u001a\u001a\u0010\r\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b\u001a\u0018\u0010\r\u001a\u00020\b*\u00020\b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\b0\u0012\u001a\u001a\u0010\r\u001a\u00020\t*\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\t\u001a\u0018\u0010\r\u001a\u00020\t*\u00020\t2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\u0012\u001a\u001a\u0010\r\u001a\u00020\n*\u00020\n2\u0006\u0010\u0003\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\n\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0087\n\u00a2\u0006\u0002\u0010\u0019\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002\u00a2\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002\u00a2\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002\u00a2\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002\u00a2\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002\u00a2\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002\u00a2\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002\u00a2\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002\u00a2\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002\u00a2\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002\u00a2\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002\u00a2\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002\u00a2\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002\u00a2\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002\u00a2\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002\u00a2\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002\u00a2\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002\u00a2\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002\u00a2\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002\u00a2\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002\u00a2\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002\u00a2\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002\u00a2\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002\u00a2\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002\u00a2\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002\u00a2\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002\u00a2\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002\u00a2\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002\u00a2\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002\u00a2\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002\u00a2\u0006\u0002\b \u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020!2\b\u0010\u0017\u001a\u0004\u0018\u00010\bH\u0087\n\u00a2\u0006\u0002\u0010\"\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020#2\b\u0010\u0017\u001a\u0004\u0018\u00010\tH\u0087\n\u00a2\u0006\u0002\u0010$\u001a\u0015\u0010%\u001a\u00020&*\u00020\u00052\u0006\u0010'\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010%\u001a\u00020&*\u00020\u00052\u0006\u0010'\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020(*\u00020\u00052\u0006\u0010'\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020&*\u00020\u00052\u0006\u0010'\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020)*\u00020\u00182\u0006\u0010'\u001a\u00020\u0018H\u0086\u0004\u001a\u0015\u0010%\u001a\u00020&*\u00020\b2\u0006\u0010'\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010%\u001a\u00020&*\u00020\b2\u0006\u0010'\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020(*\u00020\b2\u0006\u0010'\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020&*\u00020\b2\u0006\u0010'\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020(*\u00020\t2\u0006\u0010'\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010%\u001a\u00020(*\u00020\t2\u0006\u0010'\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020(*\u00020\t2\u0006\u0010'\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020(*\u00020\t2\u0006\u0010'\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020&*\u00020\n2\u0006\u0010'\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010%\u001a\u00020&*\u00020\n2\u0006\u0010'\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020(*\u00020\n2\u0006\u0010'\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010%\u001a\u00020&*\u00020\n2\u0006\u0010'\u001a\u00020\nH\u0086\u0004\u001a\r\u0010*\u001a\u00020\u0018*\u00020\u0016H\u0087\b\u001a\u0014\u0010*\u001a\u00020\u0018*\u00020\u00162\u0006\u0010*\u001a\u00020+H\u0007\u001a\r\u0010*\u001a\u00020\b*\u00020!H\u0087\b\u001a\u0014\u0010*\u001a\u00020\b*\u00020!2\u0006\u0010*\u001a\u00020+H\u0007\u001a\r\u0010*\u001a\u00020\t*\u00020#H\u0087\b\u001a\u0014\u0010*\u001a\u00020\t*\u00020#2\u0006\u0010*\u001a\u00020+H\u0007\u001a\u0014\u0010,\u001a\u0004\u0018\u00010\u0018*\u00020\u0016H\u0087\b\u00a2\u0006\u0002\u0010-\u001a\u001b\u0010,\u001a\u0004\u0018\u00010\u0018*\u00020\u00162\u0006\u0010*\u001a\u00020+H\u0007\u00a2\u0006\u0002\u0010.\u001a\u0014\u0010,\u001a\u0004\u0018\u00010\b*\u00020!H\u0087\b\u00a2\u0006\u0002\u0010/\u001a\u001b\u0010,\u001a\u0004\u0018\u00010\b*\u00020!2\u0006\u0010*\u001a\u00020+H\u0007\u00a2\u0006\u0002\u00100\u001a\u0014\u0010,\u001a\u0004\u0018\u00010\t*\u00020#H\u0087\b\u00a2\u0006\u0002\u00101\u001a\u001b\u0010,\u001a\u0004\u0018\u00010\t*\u00020#2\u0006\u0010*\u001a\u00020+H\u0007\u00a2\u0006\u0002\u00102\u001a\n\u00103\u001a\u00020)*\u00020)\u001a\n\u00103\u001a\u00020&*\u00020&\u001a\n\u00103\u001a\u00020(*\u00020(\u001a\u0015\u00104\u001a\u00020)*\u00020)2\u0006\u00104\u001a\u00020\bH\u0086\u0004\u001a\u0015\u00104\u001a\u00020&*\u00020&2\u0006\u00104\u001a\u00020\bH\u0086\u0004\u001a\u0015\u00104\u001a\u00020(*\u00020(2\u0006\u00104\u001a\u00020\tH\u0086\u0004\u001a\u0013\u00105\u001a\u0004\u0018\u00010\u0005*\u00020\u0006H\u0000\u00a2\u0006\u0002\u00106\u001a\u0013\u00105\u001a\u0004\u0018\u00010\u0005*\u00020\u0007H\u0000\u00a2\u0006\u0002\u00107\u001a\u0013\u00105\u001a\u0004\u0018\u00010\u0005*\u00020\bH\u0000\u00a2\u0006\u0002\u00108\u001a\u0013\u00105\u001a\u0004\u0018\u00010\u0005*\u00020\tH\u0000\u00a2\u0006\u0002\u00109\u001a\u0013\u00105\u001a\u0004\u0018\u00010\u0005*\u00020\nH\u0000\u00a2\u0006\u0002\u0010:\u001a\u0013\u0010;\u001a\u0004\u0018\u00010\b*\u00020\u0006H\u0000\u00a2\u0006\u0002\u0010<\u001a\u0013\u0010;\u001a\u0004\u0018\u00010\b*\u00020\u0007H\u0000\u00a2\u0006\u0002\u0010=\u001a\u0013\u0010;\u001a\u0004\u0018\u00010\b*\u00020\tH\u0000\u00a2\u0006\u0002\u0010>\u001a\u0013\u0010?\u001a\u0004\u0018\u00010\t*\u00020\u0006H\u0000\u00a2\u0006\u0002\u0010@\u001a\u0013\u0010?\u001a\u0004\u0018\u00010\t*\u00020\u0007H\u0000\u00a2\u0006\u0002\u0010A\u001a\u0013\u0010B\u001a\u0004\u0018\u00010\n*\u00020\u0006H\u0000\u00a2\u0006\u0002\u0010C\u001a\u0013\u0010B\u001a\u0004\u0018\u00010\n*\u00020\u0007H\u0000\u00a2\u0006\u0002\u0010D\u001a\u0013\u0010B\u001a\u0004\u0018\u00010\n*\u00020\bH\u0000\u00a2\u0006\u0002\u0010E\u001a\u0013\u0010B\u001a\u0004\u0018\u00010\n*\u00020\tH\u0000\u00a2\u0006\u0002\u0010F\u001a\u0015\u0010G\u001a\u00020!*\u00020\u00052\u0006\u0010'\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010G\u001a\u00020!*\u00020\u00052\u0006\u0010'\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010G\u001a\u00020#*\u00020\u00052\u0006\u0010'\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010G\u001a\u00020!*\u00020\u00052\u0006\u0010'\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010G\u001a\u00020\u0016*\u00020\u00182\u0006\u0010'\u001a\u00020\u0018H\u0086\u0004\u001a\u0015\u0010G\u001a\u00020!*\u00020\b2\u0006\u0010'\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010G\u001a\u00020!*\u00020\b2\u0006\u0010'\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010G\u001a\u00020#*\u00020\b2\u0006\u0010'\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010G\u001a\u00020!*\u00020\b2\u0006\u0010'\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010G\u001a\u00020#*\u00020\t2\u0006\u0010'\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010G\u001a\u00020#*\u00020\t2\u0006\u0010'\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010G\u001a\u00020#*\u00020\t2\u0006\u0010'\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010G\u001a\u00020#*\u00020\t2\u0006\u0010'\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010G\u001a\u00020!*\u00020\n2\u0006\u0010'\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010G\u001a\u00020!*\u00020\n2\u0006\u0010'\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010G\u001a\u00020#*\u00020\n2\u0006\u0010'\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010G\u001a\u00020!*\u00020\n2\u0006\u0010'\u001a\u00020\nH\u0086\u0004\u00a8\u0006H"}, d2={"coerceAtLeast", "T", "", "minimumValue", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "", "", "", "", "", "", "coerceAtMost", "maximumValue", "coerceIn", "(Ljava/lang/Comparable;Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "range", "Lkotlin/ranges/ClosedFloatingPointRange;", "(Ljava/lang/Comparable;Lkotlin/ranges/ClosedFloatingPointRange;)Ljava/lang/Comparable;", "Lkotlin/ranges/ClosedRange;", "(Ljava/lang/Comparable;Lkotlin/ranges/ClosedRange;)Ljava/lang/Comparable;", "contains", "", "Lkotlin/ranges/CharRange;", "element", "", "(Lkotlin/ranges/CharRange;Ljava/lang/Character;)Z", "value", "byteRangeContains", "doubleRangeContains", "floatRangeContains", "intRangeContains", "longRangeContains", "shortRangeContains", "Lkotlin/ranges/IntRange;", "(Lkotlin/ranges/IntRange;Ljava/lang/Integer;)Z", "Lkotlin/ranges/LongRange;", "(Lkotlin/ranges/LongRange;Ljava/lang/Long;)Z", "downTo", "Lkotlin/ranges/IntProgression;", "to", "Lkotlin/ranges/LongProgression;", "Lkotlin/ranges/CharProgression;", "random", "Lkotlin/random/Random;", "randomOrNull", "(Lkotlin/ranges/CharRange;)Ljava/lang/Character;", "(Lkotlin/ranges/CharRange;Lkotlin/random/Random;)Ljava/lang/Character;", "(Lkotlin/ranges/IntRange;)Ljava/lang/Integer;", "(Lkotlin/ranges/IntRange;Lkotlin/random/Random;)Ljava/lang/Integer;", "(Lkotlin/ranges/LongRange;)Ljava/lang/Long;", "(Lkotlin/ranges/LongRange;Lkotlin/random/Random;)Ljava/lang/Long;", "reversed", "step", "toByteExactOrNull", "(D)Ljava/lang/Byte;", "(F)Ljava/lang/Byte;", "(I)Ljava/lang/Byte;", "(J)Ljava/lang/Byte;", "(S)Ljava/lang/Byte;", "toIntExactOrNull", "(D)Ljava/lang/Integer;", "(F)Ljava/lang/Integer;", "(J)Ljava/lang/Integer;", "toLongExactOrNull", "(D)Ljava/lang/Long;", "(F)Ljava/lang/Long;", "toShortExactOrNull", "(D)Ljava/lang/Short;", "(F)Ljava/lang/Short;", "(I)Ljava/lang/Short;", "(J)Ljava/lang/Short;", "until", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/ranges/RangesKt")
class RangesKt___RangesKt
extends RangesKt__RangesKt {
    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince="1.4", warningSince="1.3")
    public static final boolean byteRangeContains(ClosedRange<Byte> closedRange, double d) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        Byte by = RangesKt.toByteExactOrNull(d);
        boolean bl = by != null ? closedRange.contains((Byte)((Comparable)by)) : false;
        return bl;
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince="1.4", warningSince="1.3")
    public static final boolean byteRangeContains(ClosedRange<Byte> closedRange, float f) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        Byte by = RangesKt.toByteExactOrNull(f);
        boolean bl = by != null ? closedRange.contains((Byte)((Comparable)by)) : false;
        return bl;
    }

    public static final boolean byteRangeContains(ClosedRange<Byte> closedRange, int n) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        Byte by = RangesKt.toByteExactOrNull(n);
        boolean bl = by != null ? closedRange.contains((Byte)((Comparable)by)) : false;
        return bl;
    }

    public static final boolean byteRangeContains(ClosedRange<Byte> closedRange, long l) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        Byte by = RangesKt.toByteExactOrNull(l);
        boolean bl = by != null ? closedRange.contains((Byte)((Comparable)by)) : false;
        return bl;
    }

    public static final boolean byteRangeContains(ClosedRange<Byte> closedRange, short s) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        Byte by = RangesKt.toByteExactOrNull(s);
        boolean bl = by != null ? closedRange.contains((Byte)((Comparable)by)) : false;
        return bl;
    }

    public static final byte coerceAtLeast(byte by, byte by2) {
        block0: {
            if (by >= by2) break block0;
            by = by2;
        }
        return by;
    }

    public static final double coerceAtLeast(double d, double d2) {
        if (!(d < d2)) {
            d2 = d;
        }
        return d2;
    }

    public static final float coerceAtLeast(float f, float f2) {
        block0: {
            if (!(f < f2)) break block0;
            f = f2;
        }
        return f;
    }

    public static final int coerceAtLeast(int n, int n2) {
        if (n >= n2) {
            n2 = n;
        }
        return n2;
    }

    public static final long coerceAtLeast(long l, long l2) {
        block0: {
            if (l >= l2) break block0;
            l = l2;
        }
        return l;
    }

    public static final <T extends Comparable<? super T>> T coerceAtLeast(T t, T t2) {
        block0: {
            Intrinsics.checkNotNullParameter(t, "$this$coerceAtLeast");
            Intrinsics.checkNotNullParameter(t2, "minimumValue");
            if (t.compareTo(t2) >= 0) break block0;
            t = t2;
        }
        return t;
    }

    public static final short coerceAtLeast(short s, short s2) {
        block0: {
            if (s >= s2) break block0;
            s = s2;
        }
        return s;
    }

    public static final byte coerceAtMost(byte by, byte by2) {
        block0: {
            if (by <= by2) break block0;
            by = by2;
        }
        return by;
    }

    public static final double coerceAtMost(double d, double d2) {
        block0: {
            if (!(d > d2)) break block0;
            d = d2;
        }
        return d;
    }

    public static final float coerceAtMost(float f, float f2) {
        block0: {
            if (!(f > f2)) break block0;
            f = f2;
        }
        return f;
    }

    public static final int coerceAtMost(int n, int n2) {
        block0: {
            if (n <= n2) break block0;
            n = n2;
        }
        return n;
    }

    public static final long coerceAtMost(long l, long l2) {
        block0: {
            if (l <= l2) break block0;
            l = l2;
        }
        return l;
    }

    public static final <T extends Comparable<? super T>> T coerceAtMost(T t, T t2) {
        block0: {
            Intrinsics.checkNotNullParameter(t, "$this$coerceAtMost");
            Intrinsics.checkNotNullParameter(t2, "maximumValue");
            if (t.compareTo(t2) <= 0) break block0;
            t = t2;
        }
        return t;
    }

    public static final short coerceAtMost(short s, short s2) {
        if (s <= s2) {
            s2 = s;
        }
        return s2;
    }

    public static final byte coerceIn(byte by, byte by2, byte by3) {
        if (by2 <= by3) {
            if (by < by2) {
                return by2;
            }
            if (by > by3) {
                return by3;
            }
            return by;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot coerce value to an empty range: maximum ");
        stringBuilder.append(by3);
        stringBuilder.append(" is less than minimum ");
        stringBuilder.append(by2);
        stringBuilder.append('.');
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final double coerceIn(double d, double d2, double d3) {
        if (!(d2 > d3)) {
            if (d < d2) {
                return d2;
            }
            if (d > d3) {
                return d3;
            }
            return d;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot coerce value to an empty range: maximum ");
        stringBuilder.append(d3);
        stringBuilder.append(" is less than minimum ");
        stringBuilder.append(d2);
        stringBuilder.append('.');
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final float coerceIn(float f, float f2, float f3) {
        if (!(f2 > f3)) {
            if (f < f2) {
                return f2;
            }
            if (f > f3) {
                return f3;
            }
            return f;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot coerce value to an empty range: maximum ");
        stringBuilder.append(f3);
        stringBuilder.append(" is less than minimum ");
        stringBuilder.append(f2);
        stringBuilder.append('.');
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final int coerceIn(int n, int n2, int n3) {
        if (n2 <= n3) {
            if (n < n2) {
                return n2;
            }
            if (n > n3) {
                return n3;
            }
            return n;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot coerce value to an empty range: maximum ");
        stringBuilder.append(n3);
        stringBuilder.append(" is less than minimum ");
        stringBuilder.append(n2);
        stringBuilder.append('.');
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final int coerceIn(int n, ClosedRange<Integer> closedRange) {
        Intrinsics.checkNotNullParameter(closedRange, "range");
        if (closedRange instanceof ClosedFloatingPointRange) {
            return ((Number)((Object)RangesKt.coerceIn((Comparable)Integer.valueOf(n), (ClosedFloatingPointRange)closedRange))).intValue();
        }
        if (!closedRange.isEmpty()) {
            if (n < ((Number)closedRange.getStart()).intValue()) {
                n = ((Number)closedRange.getStart()).intValue();
            } else if (n > ((Number)closedRange.getEndInclusive()).intValue()) {
                n = ((Number)closedRange.getEndInclusive()).intValue();
            }
            return n;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot coerce value to an empty range: ");
        stringBuilder.append(closedRange);
        stringBuilder.append('.');
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final long coerceIn(long l, long l2, long l3) {
        if (l2 <= l3) {
            if (l < l2) {
                return l2;
            }
            if (l > l3) {
                return l3;
            }
            return l;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot coerce value to an empty range: maximum ");
        stringBuilder.append(l3);
        stringBuilder.append(" is less than minimum ");
        stringBuilder.append(l2);
        stringBuilder.append('.');
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final long coerceIn(long l, ClosedRange<Long> closedRange) {
        Intrinsics.checkNotNullParameter(closedRange, "range");
        if (closedRange instanceof ClosedFloatingPointRange) {
            return ((Number)((Object)RangesKt.coerceIn((Comparable)Long.valueOf(l), (ClosedFloatingPointRange)closedRange))).longValue();
        }
        if (!closedRange.isEmpty()) {
            if (l < ((Number)closedRange.getStart()).longValue()) {
                l = ((Number)closedRange.getStart()).longValue();
            } else if (l > ((Number)closedRange.getEndInclusive()).longValue()) {
                l = ((Number)closedRange.getEndInclusive()).longValue();
            }
            return l;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot coerce value to an empty range: ");
        stringBuilder.append(closedRange);
        stringBuilder.append('.');
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final <T extends Comparable<? super T>> T coerceIn(T object, T t, T t2) {
        Intrinsics.checkNotNullParameter(object, "$this$coerceIn");
        if (t != null && t2 != null) {
            if (t.compareTo(t2) <= 0) {
                if (object.compareTo(t) < 0) {
                    return t;
                }
                if (object.compareTo(t2) <= 0) return (T)object;
                return t2;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Cannot coerce value to an empty range: maximum ");
            ((StringBuilder)object).append(t2);
            ((StringBuilder)object).append(" is less than minimum ");
            ((StringBuilder)object).append(t);
            ((StringBuilder)object).append('.');
            throw (Throwable)new IllegalArgumentException(((StringBuilder)object).toString());
        }
        if (t != null && object.compareTo(t) < 0) {
            return t;
        }
        if (t2 == null || object.compareTo(t2) <= 0) return (T)object;
        return t2;
    }

    public static final <T extends Comparable<? super T>> T coerceIn(T object, ClosedFloatingPointRange<T> closedFloatingPointRange) {
        Intrinsics.checkNotNullParameter(object, "$this$coerceIn");
        Intrinsics.checkNotNullParameter(closedFloatingPointRange, "range");
        if (!closedFloatingPointRange.isEmpty()) {
            if (closedFloatingPointRange.lessThanOrEquals(object, closedFloatingPointRange.getStart()) && !closedFloatingPointRange.lessThanOrEquals(closedFloatingPointRange.getStart(), object)) {
                object = closedFloatingPointRange.getStart();
            } else if (closedFloatingPointRange.lessThanOrEquals(closedFloatingPointRange.getEndInclusive(), object) && !closedFloatingPointRange.lessThanOrEquals(object, closedFloatingPointRange.getEndInclusive())) {
                object = closedFloatingPointRange.getEndInclusive();
            }
            return object;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Cannot coerce value to an empty range: ");
        ((StringBuilder)object).append(closedFloatingPointRange);
        ((StringBuilder)object).append('.');
        throw (Throwable)new IllegalArgumentException(((StringBuilder)object).toString());
    }

    public static final <T extends Comparable<? super T>> T coerceIn(T object, ClosedRange<T> closedRange) {
        Intrinsics.checkNotNullParameter(object, "$this$coerceIn");
        Intrinsics.checkNotNullParameter(closedRange, "range");
        if (closedRange instanceof ClosedFloatingPointRange) {
            return RangesKt.coerceIn(object, (ClosedFloatingPointRange)closedRange);
        }
        if (!closedRange.isEmpty()) {
            if (object.compareTo(closedRange.getStart()) < 0) {
                object = closedRange.getStart();
            } else if (object.compareTo(closedRange.getEndInclusive()) > 0) {
                object = closedRange.getEndInclusive();
            }
            return (T)object;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Cannot coerce value to an empty range: ");
        ((StringBuilder)object).append(closedRange);
        ((StringBuilder)object).append('.');
        throw (Throwable)new IllegalArgumentException(((StringBuilder)object).toString());
    }

    public static final short coerceIn(short s, short s2, short s3) {
        if (s2 <= s3) {
            if (s < s2) {
                return s2;
            }
            if (s > s3) {
                return s3;
            }
            return s;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot coerce value to an empty range: maximum ");
        stringBuilder.append(s3);
        stringBuilder.append(" is less than minimum ");
        stringBuilder.append(s2);
        stringBuilder.append('.');
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    private static final boolean contains(CharRange charRange, Character c) {
        Intrinsics.checkNotNullParameter(charRange, "$this$contains");
        boolean bl = c != null && charRange.contains(c.charValue());
        return bl;
    }

    private static final boolean contains(IntRange intRange, Integer n) {
        Intrinsics.checkNotNullParameter(intRange, "$this$contains");
        boolean bl = n != null && intRange.contains(n);
        return bl;
    }

    private static final boolean contains(LongRange longRange, Long l) {
        Intrinsics.checkNotNullParameter(longRange, "$this$contains");
        boolean bl = l != null && longRange.contains(l);
        return bl;
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince="1.4", warningSince="1.3")
    public static final boolean doubleRangeContains(ClosedRange<Double> closedRange, byte by) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        return closedRange.contains((Double)((Comparable)Double.valueOf(by)));
    }

    public static final boolean doubleRangeContains(ClosedRange<Double> closedRange, float f) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        return closedRange.contains((Double)((Comparable)Double.valueOf(f)));
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince="1.4", warningSince="1.3")
    public static final boolean doubleRangeContains(ClosedRange<Double> closedRange, int n) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        return closedRange.contains((Double)((Comparable)Double.valueOf(n)));
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince="1.4", warningSince="1.3")
    public static final boolean doubleRangeContains(ClosedRange<Double> closedRange, long l) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        return closedRange.contains((Double)((Comparable)Double.valueOf(l)));
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince="1.4", warningSince="1.3")
    public static final boolean doubleRangeContains(ClosedRange<Double> closedRange, short s) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        return closedRange.contains((Double)((Comparable)Double.valueOf(s)));
    }

    public static final CharProgression downTo(char c, char c2) {
        return CharProgression.Companion.fromClosedRange(c, c2, -1);
    }

    public static final IntProgression downTo(byte by, byte by2) {
        return IntProgression.Companion.fromClosedRange(by, by2, -1);
    }

    public static final IntProgression downTo(byte by, int n) {
        return IntProgression.Companion.fromClosedRange(by, n, -1);
    }

    public static final IntProgression downTo(byte by, short s) {
        return IntProgression.Companion.fromClosedRange(by, s, -1);
    }

    public static final IntProgression downTo(int n, byte by) {
        return IntProgression.Companion.fromClosedRange(n, by, -1);
    }

    public static final IntProgression downTo(int n, int n2) {
        return IntProgression.Companion.fromClosedRange(n, n2, -1);
    }

    public static final IntProgression downTo(int n, short s) {
        return IntProgression.Companion.fromClosedRange(n, s, -1);
    }

    public static final IntProgression downTo(short s, byte by) {
        return IntProgression.Companion.fromClosedRange(s, by, -1);
    }

    public static final IntProgression downTo(short s, int n) {
        return IntProgression.Companion.fromClosedRange(s, n, -1);
    }

    public static final IntProgression downTo(short s, short s2) {
        return IntProgression.Companion.fromClosedRange(s, s2, -1);
    }

    public static final LongProgression downTo(byte by, long l) {
        return LongProgression.Companion.fromClosedRange(by, l, -1L);
    }

    public static final LongProgression downTo(int n, long l) {
        return LongProgression.Companion.fromClosedRange(n, l, -1L);
    }

    public static final LongProgression downTo(long l, byte by) {
        return LongProgression.Companion.fromClosedRange(l, by, -1L);
    }

    public static final LongProgression downTo(long l, int n) {
        return LongProgression.Companion.fromClosedRange(l, n, -1L);
    }

    public static final LongProgression downTo(long l, long l2) {
        return LongProgression.Companion.fromClosedRange(l, l2, -1L);
    }

    public static final LongProgression downTo(long l, short s) {
        return LongProgression.Companion.fromClosedRange(l, s, -1L);
    }

    public static final LongProgression downTo(short s, long l) {
        return LongProgression.Companion.fromClosedRange(s, l, -1L);
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince="1.4", warningSince="1.3")
    public static final boolean floatRangeContains(ClosedRange<Float> closedRange, byte by) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        return closedRange.contains((Float)((Comparable)Float.valueOf(by)));
    }

    public static final boolean floatRangeContains(ClosedRange<Float> closedRange, double d) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        return closedRange.contains((Float)((Comparable)Float.valueOf((float)d)));
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince="1.4", warningSince="1.3")
    public static final boolean floatRangeContains(ClosedRange<Float> closedRange, int n) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        return closedRange.contains((Float)((Comparable)Float.valueOf(n)));
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince="1.4", warningSince="1.3")
    public static final boolean floatRangeContains(ClosedRange<Float> closedRange, long l) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        return closedRange.contains((Float)((Comparable)Float.valueOf(l)));
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince="1.4", warningSince="1.3")
    public static final boolean floatRangeContains(ClosedRange<Float> closedRange, short s) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        return closedRange.contains((Float)((Comparable)Float.valueOf(s)));
    }

    public static final boolean intRangeContains(ClosedRange<Integer> closedRange, byte by) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        return closedRange.contains((Integer)((Comparable)Integer.valueOf(by)));
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince="1.4", warningSince="1.3")
    public static final boolean intRangeContains(ClosedRange<Integer> closedRange, double d) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        Integer n = RangesKt.toIntExactOrNull(d);
        boolean bl = n != null ? closedRange.contains((Integer)((Comparable)n)) : false;
        return bl;
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince="1.4", warningSince="1.3")
    public static final boolean intRangeContains(ClosedRange<Integer> closedRange, float f) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        Integer n = RangesKt.toIntExactOrNull(f);
        boolean bl = n != null ? closedRange.contains((Integer)((Comparable)n)) : false;
        return bl;
    }

    public static final boolean intRangeContains(ClosedRange<Integer> closedRange, long l) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        Integer n = RangesKt.toIntExactOrNull(l);
        boolean bl = n != null ? closedRange.contains((Integer)((Comparable)n)) : false;
        return bl;
    }

    public static final boolean intRangeContains(ClosedRange<Integer> closedRange, short s) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        return closedRange.contains((Integer)((Comparable)Integer.valueOf(s)));
    }

    public static final boolean longRangeContains(ClosedRange<Long> closedRange, byte by) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        return closedRange.contains((Long)((Comparable)Long.valueOf(by)));
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince="1.4", warningSince="1.3")
    public static final boolean longRangeContains(ClosedRange<Long> closedRange, double d) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        Long l = RangesKt.toLongExactOrNull(d);
        boolean bl = l != null ? closedRange.contains((Long)((Comparable)l)) : false;
        return bl;
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince="1.4", warningSince="1.3")
    public static final boolean longRangeContains(ClosedRange<Long> closedRange, float f) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        Long l = RangesKt.toLongExactOrNull(f);
        boolean bl = l != null ? closedRange.contains((Long)((Comparable)l)) : false;
        return bl;
    }

    public static final boolean longRangeContains(ClosedRange<Long> closedRange, int n) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        return closedRange.contains((Long)((Comparable)Long.valueOf(n)));
    }

    public static final boolean longRangeContains(ClosedRange<Long> closedRange, short s) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        return closedRange.contains((Long)((Comparable)Long.valueOf(s)));
    }

    private static final char random(CharRange charRange) {
        return RangesKt.random(charRange, (Random)Random.Default);
    }

    public static final char random(CharRange charRange, Random random) {
        int n;
        Intrinsics.checkNotNullParameter(charRange, "$this$random");
        Intrinsics.checkNotNullParameter(random, "random");
        try {
            n = random.nextInt(charRange.getFirst(), charRange.getLast() + '\u0001');
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw (Throwable)new NoSuchElementException(illegalArgumentException.getMessage());
        }
        return (char)n;
    }

    private static final int random(IntRange intRange) {
        return RangesKt.random(intRange, (Random)Random.Default);
    }

    public static final int random(IntRange intRange, Random random) {
        Intrinsics.checkNotNullParameter(intRange, "$this$random");
        Intrinsics.checkNotNullParameter(random, "random");
        try {
            int n = RandomKt.nextInt(random, intRange);
            return n;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw (Throwable)new NoSuchElementException(illegalArgumentException.getMessage());
        }
    }

    private static final long random(LongRange longRange) {
        return RangesKt.random(longRange, (Random)Random.Default);
    }

    public static final long random(LongRange longRange, Random random) {
        Intrinsics.checkNotNullParameter(longRange, "$this$random");
        Intrinsics.checkNotNullParameter(random, "random");
        try {
            long l = RandomKt.nextLong(random, longRange);
            return l;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw (Throwable)new NoSuchElementException(illegalArgumentException.getMessage());
        }
    }

    private static final Character randomOrNull(CharRange charRange) {
        return RangesKt.randomOrNull(charRange, (Random)Random.Default);
    }

    public static final Character randomOrNull(CharRange charRange, Random random) {
        Intrinsics.checkNotNullParameter(charRange, "$this$randomOrNull");
        Intrinsics.checkNotNullParameter(random, "random");
        if (charRange.isEmpty()) {
            return null;
        }
        return Character.valueOf((char)random.nextInt(charRange.getFirst(), charRange.getLast() + '\u0001'));
    }

    private static final Integer randomOrNull(IntRange intRange) {
        return RangesKt.randomOrNull(intRange, (Random)Random.Default);
    }

    public static final Integer randomOrNull(IntRange intRange, Random random) {
        Intrinsics.checkNotNullParameter(intRange, "$this$randomOrNull");
        Intrinsics.checkNotNullParameter(random, "random");
        if (intRange.isEmpty()) {
            return null;
        }
        return RandomKt.nextInt(random, intRange);
    }

    private static final Long randomOrNull(LongRange longRange) {
        return RangesKt.randomOrNull(longRange, (Random)Random.Default);
    }

    public static final Long randomOrNull(LongRange longRange, Random random) {
        Intrinsics.checkNotNullParameter(longRange, "$this$randomOrNull");
        Intrinsics.checkNotNullParameter(random, "random");
        if (longRange.isEmpty()) {
            return null;
        }
        return RandomKt.nextLong(random, longRange);
    }

    public static final CharProgression reversed(CharProgression charProgression) {
        Intrinsics.checkNotNullParameter(charProgression, "$this$reversed");
        return CharProgression.Companion.fromClosedRange(charProgression.getLast(), charProgression.getFirst(), -charProgression.getStep());
    }

    public static final IntProgression reversed(IntProgression intProgression) {
        Intrinsics.checkNotNullParameter(intProgression, "$this$reversed");
        return IntProgression.Companion.fromClosedRange(intProgression.getLast(), intProgression.getFirst(), -intProgression.getStep());
    }

    public static final LongProgression reversed(LongProgression longProgression) {
        Intrinsics.checkNotNullParameter(longProgression, "$this$reversed");
        return LongProgression.Companion.fromClosedRange(longProgression.getLast(), longProgression.getFirst(), -longProgression.getStep());
    }

    public static final boolean shortRangeContains(ClosedRange<Short> closedRange, byte by) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        return closedRange.contains((Short)((Comparable)Short.valueOf(by)));
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince="1.4", warningSince="1.3")
    public static final boolean shortRangeContains(ClosedRange<Short> closedRange, double d) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        Short s = RangesKt.toShortExactOrNull(d);
        boolean bl = s != null ? closedRange.contains((Short)((Comparable)s)) : false;
        return bl;
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince="1.4", warningSince="1.3")
    public static final boolean shortRangeContains(ClosedRange<Short> closedRange, float f) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        Short s = RangesKt.toShortExactOrNull(f);
        boolean bl = s != null ? closedRange.contains((Short)((Comparable)s)) : false;
        return bl;
    }

    public static final boolean shortRangeContains(ClosedRange<Short> closedRange, int n) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        Short s = RangesKt.toShortExactOrNull(n);
        boolean bl = s != null ? closedRange.contains((Short)((Comparable)s)) : false;
        return bl;
    }

    public static final boolean shortRangeContains(ClosedRange<Short> closedRange, long l) {
        Intrinsics.checkNotNullParameter(closedRange, "$this$contains");
        Short s = RangesKt.toShortExactOrNull(l);
        boolean bl = s != null ? closedRange.contains((Short)((Comparable)s)) : false;
        return bl;
    }

    public static final CharProgression step(CharProgression charProgression, int n) {
        Intrinsics.checkNotNullParameter(charProgression, "$this$step");
        boolean bl = n > 0;
        RangesKt.checkStepIsPositive(bl, n);
        CharProgression.Companion companion = CharProgression.Companion;
        char c = charProgression.getFirst();
        char c2 = charProgression.getLast();
        if (charProgression.getStep() <= 0) {
            n = -n;
        }
        return companion.fromClosedRange(c, c2, n);
    }

    public static final IntProgression step(IntProgression intProgression, int n) {
        Intrinsics.checkNotNullParameter(intProgression, "$this$step");
        boolean bl = n > 0;
        RangesKt.checkStepIsPositive(bl, n);
        IntProgression.Companion companion = IntProgression.Companion;
        int n2 = intProgression.getFirst();
        int n3 = intProgression.getLast();
        if (intProgression.getStep() <= 0) {
            n = -n;
        }
        return companion.fromClosedRange(n2, n3, n);
    }

    public static final LongProgression step(LongProgression longProgression, long l) {
        Intrinsics.checkNotNullParameter(longProgression, "$this$step");
        boolean bl = l > 0L;
        RangesKt.checkStepIsPositive(bl, l);
        LongProgression.Companion companion = LongProgression.Companion;
        long l2 = longProgression.getFirst();
        long l3 = longProgression.getLast();
        if (longProgression.getStep() <= 0L) {
            l = -l;
        }
        return companion.fromClosedRange(l2, l3, l);
    }

    public static final Byte toByteExactOrNull(double d) {
        double d2 = -128;
        double d3 = 127;
        Byte by = d >= d2 && d <= d3 ? Byte.valueOf((byte)d) : null;
        return by;
    }

    public static final Byte toByteExactOrNull(float f) {
        float f2 = -128;
        float f3 = 127;
        Byte by = f >= f2 && f <= f3 ? Byte.valueOf((byte)f) : null;
        return by;
    }

    public static final Byte toByteExactOrNull(int n) {
        Byte by = -128 <= n && 127 >= n ? Byte.valueOf((byte)n) : null;
        return by;
    }

    public static final Byte toByteExactOrNull(long l) {
        long l2 = -128;
        long l3 = 127;
        Byte by = l2 <= l && l3 >= l ? Byte.valueOf((byte)l) : null;
        return by;
    }

    public static final Byte toByteExactOrNull(short s) {
        short s2 = (short)-128;
        short s3 = (short)127;
        Byte by = s2 <= s && s3 >= s ? Byte.valueOf((byte)s) : null;
        return by;
    }

    public static final Integer toIntExactOrNull(double d) {
        double d2 = Integer.MIN_VALUE;
        double d3 = Integer.MAX_VALUE;
        Integer n = d >= d2 && d <= d3 ? Integer.valueOf((int)d) : null;
        return n;
    }

    public static final Integer toIntExactOrNull(float f) {
        float f2 = Integer.MIN_VALUE;
        float f3 = Integer.MAX_VALUE;
        Integer n = f >= f2 && f <= f3 ? Integer.valueOf((int)f) : null;
        return n;
    }

    public static final Integer toIntExactOrNull(long l) {
        long l2 = Integer.MIN_VALUE;
        long l3 = Integer.MAX_VALUE;
        Integer n = l2 <= l && l3 >= l ? Integer.valueOf((int)l) : null;
        return n;
    }

    public static final Long toLongExactOrNull(double d) {
        double d2 = Long.MIN_VALUE;
        double d3 = Long.MAX_VALUE;
        Long l = d >= d2 && d <= d3 ? Long.valueOf((long)d) : null;
        return l;
    }

    public static final Long toLongExactOrNull(float f) {
        float f2 = Long.MIN_VALUE;
        float f3 = Long.MAX_VALUE;
        Long l = f >= f2 && f <= f3 ? Long.valueOf((long)f) : null;
        return l;
    }

    public static final Short toShortExactOrNull(double d) {
        double d2 = Short.MIN_VALUE;
        double d3 = Short.MAX_VALUE;
        Short s = d >= d2 && d <= d3 ? Short.valueOf((short)d) : null;
        return s;
    }

    public static final Short toShortExactOrNull(float f) {
        float f2 = Short.MIN_VALUE;
        float f3 = Short.MAX_VALUE;
        Short s = f >= f2 && f <= f3 ? Short.valueOf((short)f) : null;
        return s;
    }

    public static final Short toShortExactOrNull(int n) {
        Short s = Short.MIN_VALUE <= n && Short.MAX_VALUE >= n ? Short.valueOf((short)n) : null;
        return s;
    }

    public static final Short toShortExactOrNull(long l) {
        long l2 = Short.MIN_VALUE;
        long l3 = Short.MAX_VALUE;
        Short s = l2 <= l && l3 >= l ? Short.valueOf((short)l) : null;
        return s;
    }

    public static final CharRange until(char c, char c2) {
        if (Intrinsics.compare(c2, 0) <= 0) {
            return CharRange.Companion.getEMPTY();
        }
        return new CharRange(c, (char)(c2 - '\u0001'));
    }

    public static final IntRange until(byte by, byte by2) {
        return new IntRange(by, by2 - 1);
    }

    public static final IntRange until(byte by, int n) {
        if (n <= Integer.MIN_VALUE) {
            return IntRange.Companion.getEMPTY();
        }
        return new IntRange(by, n - 1);
    }

    public static final IntRange until(byte by, short s) {
        return new IntRange(by, s - 1);
    }

    public static final IntRange until(int n, byte by) {
        return new IntRange(n, by - 1);
    }

    public static final IntRange until(int n, int n2) {
        if (n2 <= Integer.MIN_VALUE) {
            return IntRange.Companion.getEMPTY();
        }
        return new IntRange(n, n2 - 1);
    }

    public static final IntRange until(int n, short s) {
        return new IntRange(n, s - 1);
    }

    public static final IntRange until(short s, byte by) {
        return new IntRange(s, by - 1);
    }

    public static final IntRange until(short s, int n) {
        if (n <= Integer.MIN_VALUE) {
            return IntRange.Companion.getEMPTY();
        }
        return new IntRange(s, n - 1);
    }

    public static final IntRange until(short s, short s2) {
        return new IntRange(s, s2 - 1);
    }

    public static final LongRange until(byte by, long l) {
        if (l <= Long.MIN_VALUE) {
            return LongRange.Companion.getEMPTY();
        }
        return new LongRange(by, l - 1L);
    }

    public static final LongRange until(int n, long l) {
        if (l <= Long.MIN_VALUE) {
            return LongRange.Companion.getEMPTY();
        }
        return new LongRange(n, l - 1L);
    }

    public static final LongRange until(long l, byte by) {
        return new LongRange(l, (long)by - 1L);
    }

    public static final LongRange until(long l, int n) {
        return new LongRange(l, (long)n - 1L);
    }

    public static final LongRange until(long l, long l2) {
        if (l2 <= Long.MIN_VALUE) {
            return LongRange.Companion.getEMPTY();
        }
        return new LongRange(l, l2 - 1L);
    }

    public static final LongRange until(long l, short s) {
        return new LongRange(l, (long)s - 1L);
    }

    public static final LongRange until(short s, long l) {
        if (l <= Long.MIN_VALUE) {
            return LongRange.Companion.getEMPTY();
        }
        return new LongRange(s, l - 1L);
    }
}

