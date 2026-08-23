/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ReplaceWith;
import kotlin.TuplesKt;
import kotlin.collections.ArraysKt;
import kotlin.collections.CharIterator;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.CharsKt;
import kotlin.text.DelimitedRangesSequence;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import kotlin.text.StringsKt__StringsJVMKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000|\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0019\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\b\n\u0002\u0010\u0011\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u001e\u001a\u001c\u0010\t\u001a\u00020\n*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u001c\u0010\u000e\u001a\u00020\n*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u001f\u0010\u000f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\rH\u0086\u0002\u001a\u001f\u0010\u000f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\rH\u0086\u0002\u001a\u0015\u0010\u000f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0013H\u0087\n\u001a\u001c\u0010\u0014\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u001c\u0010\u0014\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r\u001a:\u0010\u0016\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n\u0018\u00010\u0017*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001aE\u0010\u0016\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n\u0018\u00010\u0017*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\rH\u0002\u00a2\u0006\u0002\b\u001c\u001a:\u0010\u001d\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n\u0018\u00010\u0017*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u0012\u0010\u001e\u001a\u00020\r*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u0006\u001a7\u0010 \u001a\u0002H!\"\f\b\u0000\u0010\"*\u00020\u0002*\u0002H!\"\u0004\b\u0001\u0010!*\u0002H\"2\f\u0010#\u001a\b\u0012\u0004\u0012\u0002H!0$H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010%\u001a7\u0010&\u001a\u0002H!\"\f\b\u0000\u0010\"*\u00020\u0002*\u0002H!\"\u0004\b\u0001\u0010!*\u0002H\"2\f\u0010#\u001a\b\u0012\u0004\u0012\u0002H!0$H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010%\u001a&\u0010'\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a;\u0010'\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u001b\u001a\u00020\rH\u0002\u00a2\u0006\u0002\b)\u001a&\u0010'\u001a\u00020\u0006*\u00020\u00022\u0006\u0010*\u001a\u00020\n2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a&\u0010+\u001a\u00020\u0006*\u00020\u00022\u0006\u0010,\u001a\u00020-2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a,\u0010+\u001a\u00020\u0006*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a\r\u0010.\u001a\u00020\r*\u00020\u0002H\u0087\b\u001a\r\u0010/\u001a\u00020\r*\u00020\u0002H\u0087\b\u001a\r\u00100\u001a\u00020\r*\u00020\u0002H\u0087\b\u001a \u00101\u001a\u00020\r*\u0004\u0018\u00010\u0002H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a \u00102\u001a\u00020\r*\u0004\u0018\u00010\u0002H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a\r\u00103\u001a\u000204*\u00020\u0002H\u0086\u0002\u001a&\u00105\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a&\u00105\u001a\u00020\u0006*\u00020\u00022\u0006\u0010*\u001a\u00020\n2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a&\u00106\u001a\u00020\u0006*\u00020\u00022\u0006\u0010,\u001a\u00020-2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a,\u00106\u001a\u00020\u0006*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u0010\u00107\u001a\b\u0012\u0004\u0012\u00020\n08*\u00020\u0002\u001a\u0010\u00109\u001a\b\u0012\u0004\u0012\u00020\n0:*\u00020\u0002\u001a\u0015\u0010;\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0013H\u0087\f\u001a\u000f\u0010<\u001a\u00020\n*\u0004\u0018\u00010\nH\u0087\b\u001a\u001c\u0010=\u001a\u00020\u0002*\u00020\u00022\u0006\u0010>\u001a\u00020\u00062\b\b\u0002\u0010?\u001a\u00020\u0011\u001a\u001c\u0010=\u001a\u00020\n*\u00020\n2\u0006\u0010>\u001a\u00020\u00062\b\b\u0002\u0010?\u001a\u00020\u0011\u001a\u001c\u0010@\u001a\u00020\u0002*\u00020\u00022\u0006\u0010>\u001a\u00020\u00062\b\b\u0002\u0010?\u001a\u00020\u0011\u001a\u001c\u0010@\u001a\u00020\n*\u00020\n2\u0006\u0010>\u001a\u00020\u00062\b\b\u0002\u0010?\u001a\u00020\u0011\u001aG\u0010A\u001a\b\u0012\u0004\u0012\u00020\u000108*\u00020\u00022\u000e\u0010B\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0C2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006H\u0002\u00a2\u0006\u0004\bE\u0010F\u001a=\u0010A\u001a\b\u0012\u0004\u0012\u00020\u000108*\u00020\u00022\u0006\u0010B\u001a\u00020-2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006H\u0002\u00a2\u0006\u0002\bE\u001a4\u0010G\u001a\u00020\r*\u00020\u00022\u0006\u0010H\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u00022\u0006\u0010I\u001a\u00020\u00062\u0006\u0010>\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\rH\u0000\u001a\u0012\u0010J\u001a\u00020\u0002*\u00020\u00022\u0006\u0010K\u001a\u00020\u0002\u001a\u0012\u0010J\u001a\u00020\n*\u00020\n2\u0006\u0010K\u001a\u00020\u0002\u001a\u001a\u0010L\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u0006\u001a\u0012\u0010L\u001a\u00020\u0002*\u00020\u00022\u0006\u0010M\u001a\u00020\u0001\u001a\u001d\u0010L\u001a\u00020\n*\u00020\n2\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u0006H\u0087\b\u001a\u0015\u0010L\u001a\u00020\n*\u00020\n2\u0006\u0010M\u001a\u00020\u0001H\u0087\b\u001a\u0012\u0010N\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0002\u001a\u0012\u0010N\u001a\u00020\n*\u00020\n2\u0006\u0010\u0015\u001a\u00020\u0002\u001a\u0012\u0010O\u001a\u00020\u0002*\u00020\u00022\u0006\u0010P\u001a\u00020\u0002\u001a\u001a\u0010O\u001a\u00020\u0002*\u00020\u00022\u0006\u0010K\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0002\u001a\u0012\u0010O\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u0002\u001a\u001a\u0010O\u001a\u00020\n*\u00020\n2\u0006\u0010K\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0002\u001a.\u0010Q\u001a\u00020\n*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0014\b\b\u0010R\u001a\u000e\u0012\u0004\u0012\u00020T\u0012\u0004\u0012\u00020\u00020SH\u0087\b\u00f8\u0001\u0000\u001a\u001d\u0010Q\u001a\u00020\n*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010U\u001a\u00020\nH\u0087\b\u001a$\u0010V\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010V\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010X\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010X\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010Y\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010Y\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010Z\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010Z\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001d\u0010[\u001a\u00020\n*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010U\u001a\u00020\nH\u0087\b\u001a)\u0010\\\u001a\u00020\n*\u00020\n2\u0012\u0010R\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110SH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\b]\u001a)\u0010\\\u001a\u00020\n*\u00020\n2\u0012\u0010R\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00020SH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\b^\u001a\"\u0010_\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u00062\u0006\u0010U\u001a\u00020\u0002\u001a\u001a\u0010_\u001a\u00020\u0002*\u00020\u00022\u0006\u0010M\u001a\u00020\u00012\u0006\u0010U\u001a\u00020\u0002\u001a%\u0010_\u001a\u00020\n*\u00020\n2\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u00062\u0006\u0010U\u001a\u00020\u0002H\u0087\b\u001a\u001d\u0010_\u001a\u00020\n*\u00020\n2\u0006\u0010M\u001a\u00020\u00012\u0006\u0010U\u001a\u00020\u0002H\u0087\b\u001a=\u0010`\u001a\b\u0012\u0004\u0012\u00020\n0:*\u00020\u00022\u0012\u0010B\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0C\"\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006\u00a2\u0006\u0002\u0010a\u001a0\u0010`\u001a\b\u0012\u0004\u0012\u00020\n0:*\u00020\u00022\n\u0010B\u001a\u00020-\"\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006\u001a/\u0010`\u001a\b\u0012\u0004\u0012\u00020\n0:*\u00020\u00022\u0006\u0010P\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010D\u001a\u00020\u0006H\u0002\u00a2\u0006\u0002\bb\u001a%\u0010`\u001a\b\u0012\u0004\u0012\u00020\n0:*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010D\u001a\u00020\u0006H\u0087\b\u001a=\u0010c\u001a\b\u0012\u0004\u0012\u00020\n08*\u00020\u00022\u0012\u0010B\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0C\"\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006\u00a2\u0006\u0002\u0010d\u001a0\u0010c\u001a\b\u0012\u0004\u0012\u00020\n08*\u00020\u00022\n\u0010B\u001a\u00020-\"\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006\u001a\u001c\u0010e\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u001c\u0010e\u001a\u00020\r*\u00020\u00022\u0006\u0010K\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r\u001a$\u0010e\u001a\u00020\r*\u00020\u00022\u0006\u0010K\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u0012\u0010f\u001a\u00020\u0002*\u00020\u00022\u0006\u0010M\u001a\u00020\u0001\u001a\u001d\u0010f\u001a\u00020\u0002*\u00020\n2\u0006\u0010g\u001a\u00020\u00062\u0006\u0010h\u001a\u00020\u0006H\u0087\b\u001a\u001f\u0010i\u001a\u00020\n*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010(\u001a\u00020\u0006H\u0087\b\u001a\u0012\u0010i\u001a\u00020\n*\u00020\u00022\u0006\u0010M\u001a\u00020\u0001\u001a\u0012\u0010i\u001a\u00020\n*\u00020\n2\u0006\u0010M\u001a\u00020\u0001\u001a\u001c\u0010j\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010j\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010k\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010k\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010l\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010l\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010m\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010m\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a\n\u0010n\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010n\u001a\u00020\u0002*\u00020\u00022\u0012\u0010o\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010n\u001a\u00020\u0002*\u00020\u00022\n\u0010,\u001a\u00020-\"\u00020\u0011\u001a\r\u0010n\u001a\u00020\n*\u00020\nH\u0087\b\u001a$\u0010n\u001a\u00020\n*\u00020\n2\u0012\u0010o\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010n\u001a\u00020\n*\u00020\n2\n\u0010,\u001a\u00020-\"\u00020\u0011\u001a\n\u0010p\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010p\u001a\u00020\u0002*\u00020\u00022\u0012\u0010o\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010p\u001a\u00020\u0002*\u00020\u00022\n\u0010,\u001a\u00020-\"\u00020\u0011\u001a\r\u0010p\u001a\u00020\n*\u00020\nH\u0087\b\u001a$\u0010p\u001a\u00020\n*\u00020\n2\u0012\u0010o\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010p\u001a\u00020\n*\u00020\n2\n\u0010,\u001a\u00020-\"\u00020\u0011\u001a\n\u0010q\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010q\u001a\u00020\u0002*\u00020\u00022\u0012\u0010o\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010q\u001a\u00020\u0002*\u00020\u00022\n\u0010,\u001a\u00020-\"\u00020\u0011\u001a\r\u0010q\u001a\u00020\n*\u00020\nH\u0087\b\u001a$\u0010q\u001a\u00020\n*\u00020\n2\u0012\u0010o\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010q\u001a\u00020\n*\u00020\n2\n\u0010,\u001a\u00020-\"\u00020\u0011\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\b\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006r"}, d2={"indices", "Lkotlin/ranges/IntRange;", "", "getIndices", "(Ljava/lang/CharSequence;)Lkotlin/ranges/IntRange;", "lastIndex", "", "getLastIndex", "(Ljava/lang/CharSequence;)I", "commonPrefixWith", "", "other", "ignoreCase", "", "commonSuffixWith", "contains", "char", "", "regex", "Lkotlin/text/Regex;", "endsWith", "suffix", "findAnyOf", "Lkotlin/Pair;", "strings", "", "startIndex", "last", "findAnyOf$StringsKt__StringsKt", "findLastAnyOf", "hasSurrogatePairAt", "index", "ifBlank", "R", "C", "defaultValue", "Lkotlin/Function0;", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "ifEmpty", "indexOf", "endIndex", "indexOf$StringsKt__StringsKt", "string", "indexOfAny", "chars", "", "isEmpty", "isNotBlank", "isNotEmpty", "isNullOrBlank", "isNullOrEmpty", "iterator", "Lkotlin/collections/CharIterator;", "lastIndexOf", "lastIndexOfAny", "lineSequence", "Lkotlin/sequences/Sequence;", "lines", "", "matches", "orEmpty", "padEnd", "length", "padChar", "padStart", "rangesDelimitedBy", "delimiters", "", "limit", "rangesDelimitedBy$StringsKt__StringsKt", "(Ljava/lang/CharSequence;[Ljava/lang/String;IZI)Lkotlin/sequences/Sequence;", "regionMatchesImpl", "thisOffset", "otherOffset", "removePrefix", "prefix", "removeRange", "range", "removeSuffix", "removeSurrounding", "delimiter", "replace", "transform", "Lkotlin/Function1;", "Lkotlin/text/MatchResult;", "replacement", "replaceAfter", "missingDelimiterValue", "replaceAfterLast", "replaceBefore", "replaceBeforeLast", "replaceFirst", "replaceFirstChar", "replaceFirstCharWithChar", "replaceFirstCharWithCharSequence", "replaceRange", "split", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Ljava/util/List;", "split$StringsKt__StringsKt", "splitToSequence", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Lkotlin/sequences/Sequence;", "startsWith", "subSequence", "start", "end", "substring", "substringAfter", "substringAfterLast", "substringBefore", "substringBeforeLast", "trim", "predicate", "trimEnd", "trimStart", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/text/StringsKt")
class StringsKt__StringsKt
extends StringsKt__StringsJVMKt {
    public static final /* synthetic */ Pair access$findAnyOf(CharSequence charSequence, Collection collection, int n, boolean bl, boolean bl2) {
        return StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt(charSequence, collection, n, bl, bl2);
    }

    public static final String commonPrefixWith(CharSequence charSequence, CharSequence charSequence2, boolean bl) {
        int n;
        block4: {
            int n2;
            block3: {
                Intrinsics.checkNotNullParameter(charSequence, "$this$commonPrefixWith");
                Intrinsics.checkNotNullParameter(charSequence2, "other");
                n = Math.min(charSequence.length(), charSequence2.length());
                for (n2 = 0; n2 < n && CharsKt.equals(charSequence.charAt(n2), charSequence2.charAt(n2), bl); ++n2) {
                }
                if (StringsKt.hasSurrogatePairAt(charSequence, n2 - 1)) break block3;
                n = n2;
                if (!StringsKt.hasSurrogatePairAt(charSequence2, n2 - 1)) break block4;
            }
            n = n2 - 1;
        }
        return ((Object)charSequence.subSequence(0, n)).toString();
    }

    public static /* synthetic */ String commonPrefixWith$default(CharSequence charSequence, CharSequence charSequence2, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.commonPrefixWith(charSequence, charSequence2, bl);
    }

    public static final String commonSuffixWith(CharSequence charSequence, CharSequence charSequence2, boolean bl) {
        int n;
        int n2;
        block4: {
            int n3;
            block3: {
                Intrinsics.checkNotNullParameter(charSequence, "$this$commonSuffixWith");
                Intrinsics.checkNotNullParameter(charSequence2, "other");
                n2 = charSequence.length();
                int n4 = charSequence2.length();
                n = Math.min(n2, n4);
                for (n3 = 0; n3 < n && CharsKt.equals(charSequence.charAt(n2 - n3 - 1), charSequence2.charAt(n4 - n3 - 1), bl); ++n3) {
                }
                if (StringsKt.hasSurrogatePairAt(charSequence, n2 - n3 - 1)) break block3;
                n = n3;
                if (!StringsKt.hasSurrogatePairAt(charSequence2, n4 - n3 - 1)) break block4;
            }
            n = n3 - 1;
        }
        return ((Object)charSequence.subSequence(n2 - n, n2)).toString();
    }

    public static /* synthetic */ String commonSuffixWith$default(CharSequence charSequence, CharSequence charSequence2, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.commonSuffixWith(charSequence, charSequence2, bl);
    }

    public static final boolean contains(CharSequence charSequence, char c, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$contains");
        bl = StringsKt.indexOf$default(charSequence, c, 0, bl, 2, null) >= 0;
        return bl;
    }

    public static final boolean contains(CharSequence charSequence, CharSequence charSequence2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$contains");
        Intrinsics.checkNotNullParameter(charSequence2, "other");
        boolean bl2 = charSequence2 instanceof String;
        boolean bl3 = true;
        bl = bl2 ? (StringsKt.indexOf$default(charSequence, (String)charSequence2, 0, bl, 2, null) >= 0 ? bl3 : false) : (StringsKt__StringsKt.indexOf$StringsKt__StringsKt$default(charSequence, charSequence2, 0, charSequence.length(), bl, false, 16, null) >= 0 ? bl3 : false);
        return bl;
    }

    private static final boolean contains(CharSequence charSequence, Regex regex) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$contains");
        return regex.containsMatchIn(charSequence);
    }

    public static /* synthetic */ boolean contains$default(CharSequence charSequence, char c, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.contains(charSequence, c, bl);
    }

    public static /* synthetic */ boolean contains$default(CharSequence charSequence, CharSequence charSequence2, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.contains(charSequence, charSequence2, bl);
    }

    public static final boolean endsWith(CharSequence charSequence, char c, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$endsWith");
        bl = charSequence.length() > 0 && CharsKt.equals(charSequence.charAt(StringsKt.getLastIndex(charSequence)), c, bl);
        return bl;
    }

    public static final boolean endsWith(CharSequence charSequence, CharSequence charSequence2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$endsWith");
        Intrinsics.checkNotNullParameter(charSequence2, "suffix");
        if (!bl && charSequence instanceof String && charSequence2 instanceof String) {
            return StringsKt.endsWith$default((String)charSequence, (String)charSequence2, false, 2, null);
        }
        return StringsKt.regionMatchesImpl(charSequence, charSequence.length() - charSequence2.length(), charSequence2, 0, charSequence2.length(), bl);
    }

    public static /* synthetic */ boolean endsWith$default(CharSequence charSequence, char c, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.endsWith(charSequence, c, bl);
    }

    public static /* synthetic */ boolean endsWith$default(CharSequence charSequence, CharSequence charSequence2, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.endsWith(charSequence, charSequence2, bl);
    }

    public static final Pair<Integer, String> findAnyOf(CharSequence charSequence, Collection<String> collection, int n, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$findAnyOf");
        Intrinsics.checkNotNullParameter(collection, "strings");
        return StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt(charSequence, collection, n, bl, false);
    }

    private static final Pair<Integer, String> findAnyOf$StringsKt__StringsKt(CharSequence object, Collection<String> object2, int n, boolean bl, boolean bl2) {
        Object object32 = null;
        if (!bl && object2.size() == 1) {
            object2 = (String)CollectionsKt.single((Iterable)object2);
            n = !bl2 ? StringsKt.indexOf$default((CharSequence)object, (String)object2, n, false, 4, null) : StringsKt.lastIndexOf$default((CharSequence)object, (String)object2, n, false, 4, null);
            object = n < 0 ? object32 : TuplesKt.to(n, object2);
            return object;
        }
        object32 = !bl2 ? (IntProgression)new IntRange(RangesKt.coerceAtLeast(n, 0), object.length()) : RangesKt.downTo(RangesKt.coerceAtMost(n, StringsKt.getLastIndex((CharSequence)object)), 0);
        if (object instanceof String) {
            n = ((IntProgression)object32).getFirst();
            int n2 = ((IntProgression)object32).getLast();
            int n3 = ((IntProgression)object32).getStep();
            if (n3 >= 0 ? n <= n2 : n >= n2) {
                while (true) {
                    block12: {
                        for (Object object32 : (Iterable)object2) {
                            String string2 = (String)object32;
                            if (!StringsKt.regionMatches(string2, 0, (String)object, n, string2.length(), bl)) continue;
                            break block12;
                        }
                        object32 = null;
                    }
                    object32 = (String)object32;
                    if (object32 != null) {
                        return TuplesKt.to(n, object32);
                    }
                    if (n != n2) {
                        n += n3;
                        continue;
                    }
                    break;
                }
            }
        } else {
            n = ((IntProgression)object32).getFirst();
            int n4 = ((IntProgression)object32).getLast();
            int n5 = ((IntProgression)object32).getStep();
            if (n5 >= 0 ? n <= n4 : n >= n4) {
                while (true) {
                    block14: {
                        for (Object object32 : (Iterable)object2) {
                            String string3 = (String)object32;
                            if (!StringsKt.regionMatchesImpl(string3, 0, object, n, string3.length(), bl)) continue;
                            break block14;
                        }
                        object32 = null;
                    }
                    object32 = (String)object32;
                    if (object32 != null) {
                        return TuplesKt.to(n, object32);
                    }
                    if (n == n4) break;
                    n += n5;
                }
            }
        }
        return null;
    }

    public static /* synthetic */ Pair findAnyOf$default(CharSequence charSequence, Collection collection, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 0;
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.findAnyOf(charSequence, collection, n, bl);
    }

    public static final Pair<Integer, String> findLastAnyOf(CharSequence charSequence, Collection<String> collection, int n, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$findLastAnyOf");
        Intrinsics.checkNotNullParameter(collection, "strings");
        return StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt(charSequence, collection, n, bl, true);
    }

    public static /* synthetic */ Pair findLastAnyOf$default(CharSequence charSequence, Collection collection, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = StringsKt.getLastIndex(charSequence);
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.findLastAnyOf(charSequence, collection, n, bl);
    }

    public static final IntRange getIndices(CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$indices");
        return new IntRange(0, charSequence.length() - 1);
    }

    public static final int getLastIndex(CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$lastIndex");
        return charSequence.length() - 1;
    }

    public static final boolean hasSurrogatePairAt(CharSequence charSequence, int n) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$hasSurrogatePairAt");
        int n2 = charSequence.length();
        boolean bl = n >= 0 && n2 - 2 >= n && Character.isHighSurrogate(charSequence.charAt(n)) && Character.isLowSurrogate(charSequence.charAt(n + 1));
        return bl;
    }

    private static final <C extends CharSequence & R, R> R ifBlank(C object, Function0<? extends R> function0) {
        block0: {
            if (!StringsKt.isBlank(object)) break block0;
            object = function0.invoke();
        }
        return object;
    }

    private static final <C extends CharSequence & R, R> R ifEmpty(C object, Function0<? extends R> function0) {
        block0: {
            boolean bl = object.length() == 0;
            if (!bl) break block0;
            object = function0.invoke();
        }
        return object;
    }

    public static final int indexOf(CharSequence charSequence, char c, int n, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$indexOf");
        n = !bl && charSequence instanceof String ? ((String)charSequence).indexOf(c, n) : StringsKt.indexOfAny(charSequence, new char[]{c}, n, bl);
        return n;
    }

    public static final int indexOf(CharSequence charSequence, String string2, int n, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$indexOf");
        Intrinsics.checkNotNullParameter(string2, "string");
        n = !bl && charSequence instanceof String ? ((String)charSequence).indexOf(string2, n) : StringsKt__StringsKt.indexOf$StringsKt__StringsKt$default(charSequence, string2, n, charSequence.length(), bl, false, 16, null);
        return n;
    }

    private static final int indexOf$StringsKt__StringsKt(CharSequence charSequence, CharSequence charSequence2, int n, int n2, boolean bl, boolean bl2) {
        IntProgression intProgression = !bl2 ? (IntProgression)new IntRange(RangesKt.coerceAtLeast(n, 0), RangesKt.coerceAtMost(n2, charSequence.length())) : RangesKt.downTo(RangesKt.coerceAtMost(n, StringsKt.getLastIndex(charSequence)), RangesKt.coerceAtLeast(n2, 0));
        if (charSequence instanceof String && charSequence2 instanceof String) {
            n = intProgression.getFirst();
            int n3 = intProgression.getLast();
            n2 = intProgression.getStep();
            if (n2 >= 0 ? n <= n3 : n >= n3) {
                while (true) {
                    if (StringsKt.regionMatches((String)charSequence2, 0, (String)charSequence, n, charSequence2.length(), bl)) {
                        return n;
                    }
                    if (n != n3) {
                        n += n2;
                        continue;
                    }
                    break;
                }
            }
        } else {
            n = intProgression.getFirst();
            n2 = intProgression.getLast();
            int n4 = intProgression.getStep();
            if (n4 >= 0 ? n <= n2 : n >= n2) {
                while (true) {
                    if (StringsKt.regionMatchesImpl(charSequence2, 0, charSequence, n, charSequence2.length(), bl)) {
                        return n;
                    }
                    if (n == n2) break;
                    n += n4;
                }
            }
        }
        return -1;
    }

    static /* synthetic */ int indexOf$StringsKt__StringsKt$default(CharSequence charSequence, CharSequence charSequence2, int n, int n2, boolean bl, boolean bl2, int n3, Object object) {
        block0: {
            if ((n3 & 0x10) == 0) break block0;
            bl2 = false;
        }
        return StringsKt__StringsKt.indexOf$StringsKt__StringsKt(charSequence, charSequence2, n, n2, bl, bl2);
    }

    public static /* synthetic */ int indexOf$default(CharSequence charSequence, char c, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 0;
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.indexOf(charSequence, c, n, bl);
    }

    public static /* synthetic */ int indexOf$default(CharSequence charSequence, String string2, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 0;
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.indexOf(charSequence, string2, n, bl);
    }

    public static final int indexOfAny(CharSequence object, Collection<String> collection, int n, boolean bl) {
        Intrinsics.checkNotNullParameter(object, "$this$indexOfAny");
        Intrinsics.checkNotNullParameter(collection, "strings");
        object = StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt((CharSequence)object, collection, n, bl, false);
        n = object != null && (object = (Integer)((Pair)object).getFirst()) != null ? (Integer)object : -1;
        return n;
    }

    public static final int indexOfAny(CharSequence charSequence, char[] cArray, int n, boolean bl) {
        int n2;
        Intrinsics.checkNotNullParameter(charSequence, "$this$indexOfAny");
        Intrinsics.checkNotNullParameter(cArray, "chars");
        if (!bl && cArray.length == 1 && charSequence instanceof String) {
            char c = ArraysKt.single(cArray);
            return ((String)charSequence).indexOf(c, n);
        }
        if ((n = RangesKt.coerceAtLeast(n, 0)) <= (n2 = StringsKt.getLastIndex(charSequence))) {
            while (true) {
                int n3;
                block5: {
                    char c = charSequence.charAt(n);
                    int n4 = cArray.length;
                    for (n3 = 0; n3 < n4; ++n3) {
                        if (!CharsKt.equals(cArray[n3], c, bl)) continue;
                        n3 = 1;
                        break block5;
                    }
                    n3 = 0;
                }
                if (n3 != 0) {
                    return n;
                }
                if (n == n2) break;
                ++n;
            }
        }
        return -1;
    }

    public static /* synthetic */ int indexOfAny$default(CharSequence charSequence, Collection collection, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 0;
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.indexOfAny(charSequence, collection, n, bl);
    }

    public static /* synthetic */ int indexOfAny$default(CharSequence charSequence, char[] cArray, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 0;
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.indexOfAny(charSequence, cArray, n, bl);
    }

    private static final boolean isEmpty(CharSequence charSequence) {
        boolean bl = charSequence.length() == 0;
        return bl;
    }

    private static final boolean isNotBlank(CharSequence charSequence) {
        return StringsKt.isBlank(charSequence) ^ true;
    }

    private static final boolean isNotEmpty(CharSequence charSequence) {
        boolean bl = charSequence.length() > 0;
        return bl;
    }

    private static final boolean isNullOrBlank(CharSequence charSequence) {
        boolean bl = charSequence == null || StringsKt.isBlank(charSequence);
        return bl;
    }

    private static final boolean isNullOrEmpty(CharSequence charSequence) {
        boolean bl = charSequence == null || charSequence.length() == 0;
        return bl;
    }

    public static final CharIterator iterator(CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$iterator");
        return new CharIterator(charSequence){
            final CharSequence $this_iterator;
            private int index;
            {
                this.$this_iterator = charSequence;
            }

            public boolean hasNext() {
                boolean bl = this.index < this.$this_iterator.length();
                return bl;
            }

            public char nextChar() {
                CharSequence charSequence = this.$this_iterator;
                int n = this.index;
                this.index = n + 1;
                return charSequence.charAt(n);
            }
        };
    }

    public static final int lastIndexOf(CharSequence charSequence, char c, int n, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$lastIndexOf");
        n = !bl && charSequence instanceof String ? ((String)charSequence).lastIndexOf(c, n) : StringsKt.lastIndexOfAny(charSequence, new char[]{c}, n, bl);
        return n;
    }

    public static final int lastIndexOf(CharSequence charSequence, String string2, int n, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$lastIndexOf");
        Intrinsics.checkNotNullParameter(string2, "string");
        n = !bl && charSequence instanceof String ? ((String)charSequence).lastIndexOf(string2, n) : StringsKt__StringsKt.indexOf$StringsKt__StringsKt(charSequence, string2, n, 0, bl, true);
        return n;
    }

    public static /* synthetic */ int lastIndexOf$default(CharSequence charSequence, char c, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = StringsKt.getLastIndex(charSequence);
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.lastIndexOf(charSequence, c, n, bl);
    }

    public static /* synthetic */ int lastIndexOf$default(CharSequence charSequence, String string2, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = StringsKt.getLastIndex(charSequence);
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.lastIndexOf(charSequence, string2, n, bl);
    }

    public static final int lastIndexOfAny(CharSequence object, Collection<String> collection, int n, boolean bl) {
        Intrinsics.checkNotNullParameter(object, "$this$lastIndexOfAny");
        Intrinsics.checkNotNullParameter(collection, "strings");
        object = StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt((CharSequence)object, collection, n, bl, true);
        n = object != null && (object = (Integer)((Pair)object).getFirst()) != null ? (Integer)object : -1;
        return n;
    }

    public static final int lastIndexOfAny(CharSequence charSequence, char[] cArray, int n, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$lastIndexOfAny");
        Intrinsics.checkNotNullParameter(cArray, "chars");
        if (!bl && cArray.length == 1 && charSequence instanceof String) {
            char c = ArraysKt.single(cArray);
            return ((String)charSequence).lastIndexOf(c, n);
        }
        for (n = RangesKt.coerceAtMost(n, StringsKt.getLastIndex(charSequence)); n >= 0; --n) {
            int n2;
            block3: {
                char c = charSequence.charAt(n);
                int n3 = cArray.length;
                int n4 = 0;
                for (n2 = 0; n2 < n3; ++n2) {
                    if (!CharsKt.equals(cArray[n2], c, bl)) continue;
                    n2 = 1;
                    break block3;
                }
                n2 = n4;
            }
            if (n2 == 0) continue;
            return n;
        }
        return -1;
    }

    public static /* synthetic */ int lastIndexOfAny$default(CharSequence charSequence, Collection collection, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = StringsKt.getLastIndex(charSequence);
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.lastIndexOfAny(charSequence, collection, n, bl);
    }

    public static /* synthetic */ int lastIndexOfAny$default(CharSequence charSequence, char[] cArray, int n, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = StringsKt.getLastIndex(charSequence);
        }
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.lastIndexOfAny(charSequence, cArray, n, bl);
    }

    public static final Sequence<String> lineSequence(CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$lineSequence");
        return StringsKt.splitToSequence$default(charSequence, new String[]{"\r\n", "\n", "\r"}, false, 0, 6, null);
    }

    public static final List<String> lines(CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$lines");
        return SequencesKt.toList(StringsKt.lineSequence(charSequence));
    }

    private static final boolean matches(CharSequence charSequence, Regex regex) {
        return regex.matches(charSequence);
    }

    private static final String orEmpty(String string2) {
        if (string2 == null) {
            string2 = "";
        }
        return string2;
    }

    public static final CharSequence padEnd(CharSequence object, int n, char c) {
        Intrinsics.checkNotNullParameter(object, "$this$padEnd");
        if (n >= 0) {
            if (n <= object.length()) {
                return object.subSequence(0, object.length());
            }
            StringBuilder stringBuilder = new StringBuilder(n);
            stringBuilder.append((CharSequence)object);
            int n2 = n - object.length();
            n = 1;
            if (1 <= n2) {
                while (true) {
                    stringBuilder.append(c);
                    if (n == n2) break;
                    ++n;
                }
            }
            return stringBuilder;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Desired length ");
        ((StringBuilder)object).append(n);
        ((StringBuilder)object).append(" is less than zero.");
        object = new IllegalArgumentException(((StringBuilder)object).toString());
        throw object;
    }

    public static final String padEnd(String string2, int n, char c) {
        Intrinsics.checkNotNullParameter(string2, "$this$padEnd");
        return ((Object)StringsKt.padEnd((CharSequence)string2, n, c)).toString();
    }

    public static /* synthetic */ CharSequence padEnd$default(CharSequence charSequence, int n, char c, int n2, Object object) {
        if ((n2 & 2) != 0) {
            c = (char)32;
        }
        return StringsKt.padEnd(charSequence, n, c);
    }

    public static /* synthetic */ String padEnd$default(String string2, int n, char c, int n2, Object object) {
        if ((n2 & 2) != 0) {
            c = (char)32;
        }
        return StringsKt.padEnd(string2, n, c);
    }

    public static final CharSequence padStart(CharSequence object, int n, char c) {
        Intrinsics.checkNotNullParameter(object, "$this$padStart");
        if (n >= 0) {
            if (n <= object.length()) {
                return object.subSequence(0, object.length());
            }
            StringBuilder stringBuilder = new StringBuilder(n);
            int n2 = n - object.length();
            n = 1;
            if (1 <= n2) {
                while (true) {
                    stringBuilder.append(c);
                    if (n == n2) break;
                    ++n;
                }
            }
            stringBuilder.append((CharSequence)object);
            return stringBuilder;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Desired length ");
        ((StringBuilder)object).append(n);
        ((StringBuilder)object).append(" is less than zero.");
        object = new IllegalArgumentException(((StringBuilder)object).toString());
        throw object;
    }

    public static final String padStart(String string2, int n, char c) {
        Intrinsics.checkNotNullParameter(string2, "$this$padStart");
        return ((Object)StringsKt.padStart((CharSequence)string2, n, c)).toString();
    }

    public static /* synthetic */ CharSequence padStart$default(CharSequence charSequence, int n, char c, int n2, Object object) {
        if ((n2 & 2) != 0) {
            c = (char)32;
        }
        return StringsKt.padStart(charSequence, n, c);
    }

    public static /* synthetic */ String padStart$default(String string2, int n, char c, int n2, Object object) {
        if ((n2 & 2) != 0) {
            c = (char)32;
        }
        return StringsKt.padStart(string2, n, c);
    }

    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(CharSequence charSequence, char[] cArray, int n, boolean bl, int n2) {
        boolean bl2 = n2 >= 0;
        if (bl2) {
            return new DelimitedRangesSequence(charSequence, n, n2, (Function2<? super CharSequence, ? super Integer, Pair<Integer, Integer>>)new Function2<CharSequence, Integer, Pair<? extends Integer, ? extends Integer>>(cArray, bl){
                final char[] $delimiters;
                final boolean $ignoreCase;
                {
                    this.$delimiters = cArray;
                    this.$ignoreCase = bl;
                    super(2);
                }

                public final Pair<Integer, Integer> invoke(CharSequence object, int n) {
                    Intrinsics.checkNotNullParameter(object, "$receiver");
                    n = StringsKt.indexOfAny((CharSequence)object, this.$delimiters, n, this.$ignoreCase);
                    object = n < 0 ? null : TuplesKt.to(n, 1);
                    return object;
                }
            });
        }
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append("Limit must be non-negative, but was ");
        ((StringBuilder)charSequence).append(n2);
        ((StringBuilder)charSequence).append('.');
        throw (Throwable)new IllegalArgumentException(((StringBuilder)charSequence).toString().toString());
    }

    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(CharSequence charSequence, String[] stringArray, int n, boolean bl, int n2) {
        boolean bl2 = n2 >= 0;
        if (bl2) {
            return new DelimitedRangesSequence(charSequence, n, n2, (Function2<? super CharSequence, ? super Integer, Pair<Integer, Integer>>)new Function2<CharSequence, Integer, Pair<? extends Integer, ? extends Integer>>(ArraysKt.asList(stringArray), bl){
                final List $delimitersList;
                final boolean $ignoreCase;
                {
                    this.$delimitersList = list;
                    this.$ignoreCase = bl;
                    super(2);
                }

                public final Pair<Integer, Integer> invoke(CharSequence pair, int n) {
                    Intrinsics.checkNotNullParameter(pair, "$receiver");
                    pair = StringsKt__StringsKt.access$findAnyOf((CharSequence)((Object)pair), this.$delimitersList, n, this.$ignoreCase, false);
                    pair = pair != null ? TuplesKt.to(pair.getFirst(), ((String)pair.getSecond()).length()) : null;
                    return pair;
                }
            });
        }
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append("Limit must be non-negative, but was ");
        ((StringBuilder)charSequence).append(n2);
        ((StringBuilder)charSequence).append('.');
        throw (Throwable)new IllegalArgumentException(((StringBuilder)charSequence).toString().toString());
    }

    static /* synthetic */ Sequence rangesDelimitedBy$StringsKt__StringsKt$default(CharSequence charSequence, char[] cArray, int n, boolean bl, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n = 0;
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        if ((n3 & 8) != 0) {
            n2 = 0;
        }
        return StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt(charSequence, cArray, n, bl, n2);
    }

    static /* synthetic */ Sequence rangesDelimitedBy$StringsKt__StringsKt$default(CharSequence charSequence, String[] stringArray, int n, boolean bl, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n = 0;
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        if ((n3 & 8) != 0) {
            n2 = 0;
        }
        return StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt(charSequence, stringArray, n, bl, n2);
    }

    public static final boolean regionMatchesImpl(CharSequence charSequence, int n, CharSequence charSequence2, int n2, int n3, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$regionMatchesImpl");
        Intrinsics.checkNotNullParameter(charSequence2, "other");
        if (n2 >= 0 && n >= 0 && n <= charSequence.length() - n3 && n2 <= charSequence2.length() - n3) {
            for (int i = 0; i < n3; ++i) {
                if (CharsKt.equals(charSequence.charAt(n + i), charSequence2.charAt(n2 + i), bl)) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    public static final CharSequence removePrefix(CharSequence charSequence, CharSequence charSequence2) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$removePrefix");
        Intrinsics.checkNotNullParameter(charSequence2, "prefix");
        if (StringsKt.startsWith$default(charSequence, charSequence2, false, 2, null)) {
            return charSequence.subSequence(charSequence2.length(), charSequence.length());
        }
        return charSequence.subSequence(0, charSequence.length());
    }

    public static final String removePrefix(String string2, CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(string2, "$this$removePrefix");
        Intrinsics.checkNotNullParameter(charSequence, "prefix");
        if (StringsKt.startsWith$default((CharSequence)string2, charSequence, false, 2, null)) {
            string2 = string2.substring(charSequence.length());
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.String).substring(startIndex)");
            return string2;
        }
        return string2;
    }

    public static final CharSequence removeRange(CharSequence charSequence, int n, int n2) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$removeRange");
        if (n2 >= n) {
            if (n2 == n) {
                return charSequence.subSequence(0, charSequence.length());
            }
            StringBuilder stringBuilder = new StringBuilder(charSequence.length() - (n2 - n));
            stringBuilder.append(charSequence, 0, n);
            Intrinsics.checkNotNullExpressionValue(stringBuilder, "this.append(value, startIndex, endIndex)");
            stringBuilder.append(charSequence, n2, charSequence.length());
            Intrinsics.checkNotNullExpressionValue(stringBuilder, "this.append(value, startIndex, endIndex)");
            return stringBuilder;
        }
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append("End index (");
        ((StringBuilder)charSequence).append(n2);
        ((StringBuilder)charSequence).append(") is less than start index (");
        ((StringBuilder)charSequence).append(n);
        ((StringBuilder)charSequence).append(").");
        throw (Throwable)new IndexOutOfBoundsException(((StringBuilder)charSequence).toString());
    }

    public static final CharSequence removeRange(CharSequence charSequence, IntRange intRange) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$removeRange");
        Intrinsics.checkNotNullParameter(intRange, "range");
        return StringsKt.removeRange(charSequence, ((Integer)intRange.getStart()).intValue(), (Integer)intRange.getEndInclusive() + 1);
    }

    private static final String removeRange(String string2, int n, int n2) {
        if (string2 != null) {
            return ((Object)StringsKt.removeRange((CharSequence)string2, n, n2)).toString();
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    private static final String removeRange(String string2, IntRange intRange) {
        if (string2 != null) {
            return ((Object)StringsKt.removeRange((CharSequence)string2, intRange)).toString();
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    public static final CharSequence removeSuffix(CharSequence charSequence, CharSequence charSequence2) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$removeSuffix");
        Intrinsics.checkNotNullParameter(charSequence2, "suffix");
        if (StringsKt.endsWith$default(charSequence, charSequence2, false, 2, null)) {
            return charSequence.subSequence(0, charSequence.length() - charSequence2.length());
        }
        return charSequence.subSequence(0, charSequence.length());
    }

    public static final String removeSuffix(String string2, CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(string2, "$this$removeSuffix");
        Intrinsics.checkNotNullParameter(charSequence, "suffix");
        if (StringsKt.endsWith$default((CharSequence)string2, charSequence, false, 2, null)) {
            string2 = string2.substring(0, string2.length() - charSequence.length());
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            return string2;
        }
        return string2;
    }

    public static final CharSequence removeSurrounding(CharSequence charSequence, CharSequence charSequence2) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$removeSurrounding");
        Intrinsics.checkNotNullParameter(charSequence2, "delimiter");
        return StringsKt.removeSurrounding(charSequence, charSequence2, charSequence2);
    }

    public static final CharSequence removeSurrounding(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$removeSurrounding");
        Intrinsics.checkNotNullParameter(charSequence2, "prefix");
        Intrinsics.checkNotNullParameter(charSequence3, "suffix");
        if (charSequence.length() >= charSequence2.length() + charSequence3.length() && StringsKt.startsWith$default(charSequence, charSequence2, false, 2, null) && StringsKt.endsWith$default(charSequence, charSequence3, false, 2, null)) {
            return charSequence.subSequence(charSequence2.length(), charSequence.length() - charSequence3.length());
        }
        return charSequence.subSequence(0, charSequence.length());
    }

    public static final String removeSurrounding(String string2, CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(string2, "$this$removeSurrounding");
        Intrinsics.checkNotNullParameter(charSequence, "delimiter");
        return StringsKt.removeSurrounding(string2, charSequence, charSequence);
    }

    public static final String removeSurrounding(String string2, CharSequence charSequence, CharSequence charSequence2) {
        Intrinsics.checkNotNullParameter(string2, "$this$removeSurrounding");
        Intrinsics.checkNotNullParameter(charSequence, "prefix");
        Intrinsics.checkNotNullParameter(charSequence2, "suffix");
        if (string2.length() >= charSequence.length() + charSequence2.length() && StringsKt.startsWith$default((CharSequence)string2, charSequence, false, 2, null) && StringsKt.endsWith$default((CharSequence)string2, charSequence2, false, 2, null)) {
            string2 = string2.substring(charSequence.length(), string2.length() - charSequence2.length());
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            return string2;
        }
        return string2;
    }

    private static final String replace(CharSequence charSequence, Regex regex, String string2) {
        return regex.replace(charSequence, string2);
    }

    private static final String replace(CharSequence charSequence, Regex regex, Function1<? super MatchResult, ? extends CharSequence> function1) {
        return regex.replace(charSequence, function1);
    }

    public static final String replaceAfter(String string2, char c, String string3, String string4) {
        Intrinsics.checkNotNullParameter(string2, "$this$replaceAfter");
        Intrinsics.checkNotNullParameter(string3, "replacement");
        Intrinsics.checkNotNullParameter(string4, "missingDelimiterValue");
        int n = StringsKt.indexOf$default((CharSequence)string2, c, 0, false, 6, null);
        if (n != -1) {
            int n2 = string2.length();
            string4 = ((Object)StringsKt.replaceRange((CharSequence)string2, n + 1, n2, (CharSequence)string3)).toString();
        }
        return string4;
    }

    public static final String replaceAfter(String string2, String string3, String string4, String string5) {
        Intrinsics.checkNotNullParameter(string2, "$this$replaceAfter");
        Intrinsics.checkNotNullParameter(string3, "delimiter");
        Intrinsics.checkNotNullParameter(string4, "replacement");
        Intrinsics.checkNotNullParameter(string5, "missingDelimiterValue");
        int n = StringsKt.indexOf$default((CharSequence)string2, string3, 0, false, 6, null);
        if (n == -1) {
            string2 = string5;
        } else {
            int n2 = string3.length();
            int n3 = string2.length();
            string2 = ((Object)StringsKt.replaceRange((CharSequence)string2, n2 + n, n3, (CharSequence)string4)).toString();
        }
        return string2;
    }

    public static /* synthetic */ String replaceAfter$default(String string2, char c, String string3, String string4, int n, Object object) {
        if ((n & 4) != 0) {
            string4 = string2;
        }
        return StringsKt.replaceAfter(string2, c, string3, string4);
    }

    public static /* synthetic */ String replaceAfter$default(String string2, String string3, String string4, String string5, int n, Object object) {
        if ((n & 4) != 0) {
            string5 = string2;
        }
        return StringsKt.replaceAfter(string2, string3, string4, string5);
    }

    public static final String replaceAfterLast(String string2, char c, String string3, String string4) {
        Intrinsics.checkNotNullParameter(string2, "$this$replaceAfterLast");
        Intrinsics.checkNotNullParameter(string3, "replacement");
        Intrinsics.checkNotNullParameter(string4, "missingDelimiterValue");
        int n = StringsKt.lastIndexOf$default((CharSequence)string2, c, 0, false, 6, null);
        if (n != -1) {
            int n2 = string2.length();
            string4 = ((Object)StringsKt.replaceRange((CharSequence)string2, n + 1, n2, (CharSequence)string3)).toString();
        }
        return string4;
    }

    public static final String replaceAfterLast(String string2, String string3, String string4, String string5) {
        Intrinsics.checkNotNullParameter(string2, "$this$replaceAfterLast");
        Intrinsics.checkNotNullParameter(string3, "delimiter");
        Intrinsics.checkNotNullParameter(string4, "replacement");
        Intrinsics.checkNotNullParameter(string5, "missingDelimiterValue");
        int n = StringsKt.lastIndexOf$default((CharSequence)string2, string3, 0, false, 6, null);
        if (n != -1) {
            int n2 = string3.length();
            int n3 = string2.length();
            string5 = ((Object)StringsKt.replaceRange((CharSequence)string2, n2 + n, n3, (CharSequence)string4)).toString();
        }
        return string5;
    }

    public static /* synthetic */ String replaceAfterLast$default(String string2, char c, String string3, String string4, int n, Object object) {
        if ((n & 4) != 0) {
            string4 = string2;
        }
        return StringsKt.replaceAfterLast(string2, c, string3, string4);
    }

    public static /* synthetic */ String replaceAfterLast$default(String string2, String string3, String string4, String string5, int n, Object object) {
        if ((n & 4) != 0) {
            string5 = string2;
        }
        return StringsKt.replaceAfterLast(string2, string3, string4, string5);
    }

    public static final String replaceBefore(String string2, char c, String string3, String string4) {
        Intrinsics.checkNotNullParameter(string2, "$this$replaceBefore");
        Intrinsics.checkNotNullParameter(string3, "replacement");
        Intrinsics.checkNotNullParameter(string4, "missingDelimiterValue");
        int n = StringsKt.indexOf$default((CharSequence)string2, c, 0, false, 6, null);
        if (n != -1) {
            string4 = ((Object)StringsKt.replaceRange((CharSequence)string2, 0, n, (CharSequence)string3)).toString();
        }
        return string4;
    }

    public static final String replaceBefore(String string2, String string3, String string4, String string5) {
        Intrinsics.checkNotNullParameter(string2, "$this$replaceBefore");
        Intrinsics.checkNotNullParameter(string3, "delimiter");
        Intrinsics.checkNotNullParameter(string4, "replacement");
        Intrinsics.checkNotNullParameter(string5, "missingDelimiterValue");
        int n = StringsKt.indexOf$default((CharSequence)string2, string3, 0, false, 6, null);
        if (n != -1) {
            string5 = ((Object)StringsKt.replaceRange((CharSequence)string2, 0, n, (CharSequence)string4)).toString();
        }
        return string5;
    }

    public static /* synthetic */ String replaceBefore$default(String string2, char c, String string3, String string4, int n, Object object) {
        if ((n & 4) != 0) {
            string4 = string2;
        }
        return StringsKt.replaceBefore(string2, c, string3, string4);
    }

    public static /* synthetic */ String replaceBefore$default(String string2, String string3, String string4, String string5, int n, Object object) {
        if ((n & 4) != 0) {
            string5 = string2;
        }
        return StringsKt.replaceBefore(string2, string3, string4, string5);
    }

    public static final String replaceBeforeLast(String string2, char c, String string3, String string4) {
        Intrinsics.checkNotNullParameter(string2, "$this$replaceBeforeLast");
        Intrinsics.checkNotNullParameter(string3, "replacement");
        Intrinsics.checkNotNullParameter(string4, "missingDelimiterValue");
        int n = StringsKt.lastIndexOf$default((CharSequence)string2, c, 0, false, 6, null);
        string2 = n == -1 ? string4 : ((Object)StringsKt.replaceRange((CharSequence)string2, 0, n, (CharSequence)string3)).toString();
        return string2;
    }

    public static final String replaceBeforeLast(String string2, String string3, String string4, String string5) {
        Intrinsics.checkNotNullParameter(string2, "$this$replaceBeforeLast");
        Intrinsics.checkNotNullParameter(string3, "delimiter");
        Intrinsics.checkNotNullParameter(string4, "replacement");
        Intrinsics.checkNotNullParameter(string5, "missingDelimiterValue");
        int n = StringsKt.lastIndexOf$default((CharSequence)string2, string3, 0, false, 6, null);
        if (n != -1) {
            string5 = ((Object)StringsKt.replaceRange((CharSequence)string2, 0, n, (CharSequence)string4)).toString();
        }
        return string5;
    }

    public static /* synthetic */ String replaceBeforeLast$default(String string2, char c, String string3, String string4, int n, Object object) {
        if ((n & 4) != 0) {
            string4 = string2;
        }
        return StringsKt.replaceBeforeLast(string2, c, string3, string4);
    }

    public static /* synthetic */ String replaceBeforeLast$default(String string2, String string3, String string4, String string5, int n, Object object) {
        if ((n & 4) != 0) {
            string5 = string2;
        }
        return StringsKt.replaceBeforeLast(string2, string3, string4, string5);
    }

    private static final String replaceFirst(CharSequence charSequence, Regex regex, String string2) {
        return regex.replaceFirst(charSequence, string2);
    }

    private static final String replaceFirstCharWithChar(String charSequence, Function1<? super Character, Character> object) {
        boolean bl = ((CharSequence)charSequence).length() > 0;
        if (bl) {
            char c = object.invoke((Character)Character.valueOf(((String)charSequence).charAt(0))).charValue();
            if (charSequence != null) {
                object = ((String)charSequence).substring(1);
                Intrinsics.checkNotNullExpressionValue(object, "(this as java.lang.String).substring(startIndex)");
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append(String.valueOf(c));
                ((StringBuilder)charSequence).append((String)object);
                charSequence = ((StringBuilder)charSequence).toString();
            } else {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
        }
        return charSequence;
    }

    private static final String replaceFirstCharWithCharSequence(String string2, Function1<? super Character, ? extends CharSequence> function1) {
        boolean bl = ((CharSequence)string2).length() > 0;
        if (bl) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(((Object)function1.invoke(Character.valueOf(string2.charAt(0)))).toString());
            if (string2 != null) {
                string2 = string2.substring(1);
                Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.String).substring(startIndex)");
                stringBuilder.append(string2);
                string2 = stringBuilder.toString();
            } else {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
        }
        return string2;
    }

    public static final CharSequence replaceRange(CharSequence charSequence, int n, int n2, CharSequence charSequence2) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$replaceRange");
        Intrinsics.checkNotNullParameter(charSequence2, "replacement");
        if (n2 >= n) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(charSequence, 0, n);
            Intrinsics.checkNotNullExpressionValue(stringBuilder, "this.append(value, startIndex, endIndex)");
            stringBuilder.append(charSequence2);
            stringBuilder.append(charSequence, n2, charSequence.length());
            Intrinsics.checkNotNullExpressionValue(stringBuilder, "this.append(value, startIndex, endIndex)");
            return stringBuilder;
        }
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append("End index (");
        ((StringBuilder)charSequence).append(n2);
        ((StringBuilder)charSequence).append(") is less than start index (");
        ((StringBuilder)charSequence).append(n);
        ((StringBuilder)charSequence).append(").");
        throw (Throwable)new IndexOutOfBoundsException(((StringBuilder)charSequence).toString());
    }

    public static final CharSequence replaceRange(CharSequence charSequence, IntRange intRange, CharSequence charSequence2) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$replaceRange");
        Intrinsics.checkNotNullParameter(intRange, "range");
        Intrinsics.checkNotNullParameter(charSequence2, "replacement");
        return StringsKt.replaceRange(charSequence, ((Integer)intRange.getStart()).intValue(), (Integer)intRange.getEndInclusive() + 1, charSequence2);
    }

    private static final String replaceRange(String string2, int n, int n2, CharSequence charSequence) {
        if (string2 != null) {
            return ((Object)StringsKt.replaceRange((CharSequence)string2, n, n2, charSequence)).toString();
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    private static final String replaceRange(String string2, IntRange intRange, CharSequence charSequence) {
        if (string2 != null) {
            return ((Object)StringsKt.replaceRange((CharSequence)string2, intRange, charSequence)).toString();
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    private static final List<String> split(CharSequence charSequence, Regex regex, int n) {
        return regex.split(charSequence, n);
    }

    public static final List<String> split(CharSequence object, char[] object2, boolean bl, int n) {
        Intrinsics.checkNotNullParameter(object, "$this$split");
        Intrinsics.checkNotNullParameter(object2, "delimiters");
        if (((char[])object2).length == 1) {
            return StringsKt__StringsKt.split$StringsKt__StringsKt((CharSequence)object, String.valueOf(object2[0]), bl, n);
        }
        Object object3 = SequencesKt.asIterable(StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt$default((CharSequence)object, (char[])object2, 0, bl, n, 2, null));
        object2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(object3, 10));
        object3 = object3.iterator();
        while (object3.hasNext()) {
            object2.add(StringsKt.substring((CharSequence)object, (IntRange)object3.next()));
        }
        object = (List)object2;
        return object;
    }

    public static final List<String> split(CharSequence object, String[] object2, boolean bl, int n) {
        Object object3;
        Intrinsics.checkNotNullParameter(object, "$this$split");
        Intrinsics.checkNotNullParameter(object2, "delimiters");
        int n2 = ((String[])object2).length;
        boolean bl2 = true;
        if (n2 == 1) {
            object3 = object2[0];
            if (((CharSequence)object3).length() != 0) {
                bl2 = false;
            }
            if (!bl2) {
                return StringsKt__StringsKt.split$StringsKt__StringsKt((CharSequence)object, (String)object3, bl, n);
            }
        }
        object3 = SequencesKt.asIterable(StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt$default((CharSequence)object, (String[])object2, 0, bl, n, 2, null));
        object2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(object3, 10));
        object3 = object3.iterator();
        while (object3.hasNext()) {
            object2.add(StringsKt.substring((CharSequence)object, (IntRange)object3.next()));
        }
        object = (List)object2;
        return object;
    }

    private static final List<String> split$StringsKt__StringsKt(CharSequence object, String string2, boolean bl, int n) {
        int n2 = 0;
        int n3 = n >= 0 ? 1 : 0;
        if (n3 != 0) {
            int n4 = 0;
            int n5 = StringsKt.indexOf((CharSequence)object, string2, 0, bl);
            if (n5 != -1 && n != 1) {
                int n6;
                n3 = n2;
                if (n > 0) {
                    n3 = 1;
                }
                n2 = 10;
                if (n3 != 0) {
                    n2 = RangesKt.coerceAtMost(n, 10);
                }
                ArrayList<String> arrayList = new ArrayList<String>(n2);
                n2 = n5;
                do {
                    arrayList.add(((Object)object.subSequence(n4, n2)).toString());
                    n5 = n2 + string2.length();
                    if (n3 != 0 && arrayList.size() == n - 1) break;
                    n6 = StringsKt.indexOf((CharSequence)object, string2, n5, bl);
                    n4 = n5;
                    n2 = n6;
                } while (n6 != -1);
                arrayList.add(((Object)object.subSequence(n5, object.length())).toString());
                return arrayList;
            }
            return CollectionsKt.listOf(object.toString());
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Limit must be non-negative, but was ");
        ((StringBuilder)object).append(n);
        ((StringBuilder)object).append('.');
        object = new IllegalArgumentException(((StringBuilder)object).toString().toString());
        throw object;
    }

    static /* synthetic */ List split$default(CharSequence charSequence, Regex regex, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 0;
        }
        return regex.split(charSequence, n);
    }

    public static /* synthetic */ List split$default(CharSequence charSequence, char[] cArray, boolean bl, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        if ((n2 & 4) != 0) {
            n = 0;
        }
        return StringsKt.split(charSequence, cArray, bl, n);
    }

    public static /* synthetic */ List split$default(CharSequence charSequence, String[] stringArray, boolean bl, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        if ((n2 & 4) != 0) {
            n = 0;
        }
        return StringsKt.split(charSequence, stringArray, bl, n);
    }

    public static final Sequence<String> splitToSequence(CharSequence charSequence, char[] cArray, boolean bl, int n) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$splitToSequence");
        Intrinsics.checkNotNullParameter(cArray, "delimiters");
        return SequencesKt.map(StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt$default(charSequence, cArray, 0, bl, n, 2, null), (Function1)new Function1<IntRange, String>(charSequence){
            final CharSequence $this_splitToSequence;
            {
                this.$this_splitToSequence = charSequence;
                super(1);
            }

            public final String invoke(IntRange intRange) {
                Intrinsics.checkNotNullParameter(intRange, "it");
                return StringsKt.substring(this.$this_splitToSequence, intRange);
            }
        });
    }

    public static final Sequence<String> splitToSequence(CharSequence charSequence, String[] stringArray, boolean bl, int n) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$splitToSequence");
        Intrinsics.checkNotNullParameter(stringArray, "delimiters");
        return SequencesKt.map(StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt$default(charSequence, stringArray, 0, bl, n, 2, null), (Function1)new Function1<IntRange, String>(charSequence){
            final CharSequence $this_splitToSequence;
            {
                this.$this_splitToSequence = charSequence;
                super(1);
            }

            public final String invoke(IntRange intRange) {
                Intrinsics.checkNotNullParameter(intRange, "it");
                return StringsKt.substring(this.$this_splitToSequence, intRange);
            }
        });
    }

    public static /* synthetic */ Sequence splitToSequence$default(CharSequence charSequence, char[] cArray, boolean bl, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        if ((n2 & 4) != 0) {
            n = 0;
        }
        return StringsKt.splitToSequence(charSequence, cArray, bl, n);
    }

    public static /* synthetic */ Sequence splitToSequence$default(CharSequence charSequence, String[] stringArray, boolean bl, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        if ((n2 & 4) != 0) {
            n = 0;
        }
        return StringsKt.splitToSequence(charSequence, stringArray, bl, n);
    }

    public static final boolean startsWith(CharSequence charSequence, char c, boolean bl) {
        boolean bl2;
        Intrinsics.checkNotNullParameter(charSequence, "$this$startsWith");
        int n = charSequence.length();
        boolean bl3 = bl2 = false;
        if (n > 0) {
            bl3 = bl2;
            if (CharsKt.equals(charSequence.charAt(0), c, bl)) {
                bl3 = true;
            }
        }
        return bl3;
    }

    public static final boolean startsWith(CharSequence charSequence, CharSequence charSequence2, int n, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$startsWith");
        Intrinsics.checkNotNullParameter(charSequence2, "prefix");
        if (!bl && charSequence instanceof String && charSequence2 instanceof String) {
            return StringsKt.startsWith$default((String)charSequence, (String)charSequence2, n, false, 4, null);
        }
        return StringsKt.regionMatchesImpl(charSequence, n, charSequence2, 0, charSequence2.length(), bl);
    }

    public static final boolean startsWith(CharSequence charSequence, CharSequence charSequence2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$startsWith");
        Intrinsics.checkNotNullParameter(charSequence2, "prefix");
        if (!bl && charSequence instanceof String && charSequence2 instanceof String) {
            return StringsKt.startsWith$default((String)charSequence, (String)charSequence2, false, 2, null);
        }
        return StringsKt.regionMatchesImpl(charSequence, 0, charSequence2, 0, charSequence2.length(), bl);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, char c, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.startsWith(charSequence, c, bl);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, CharSequence charSequence2, int n, boolean bl, int n2, Object object) {
        if ((n2 & 4) != 0) {
            bl = false;
        }
        return StringsKt.startsWith(charSequence, charSequence2, n, bl);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, CharSequence charSequence2, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return StringsKt.startsWith(charSequence, charSequence2, bl);
    }

    public static final CharSequence subSequence(CharSequence charSequence, IntRange intRange) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$subSequence");
        Intrinsics.checkNotNullParameter(intRange, "range");
        return charSequence.subSequence((Integer)intRange.getStart(), (Integer)intRange.getEndInclusive() + 1);
    }

    @Deprecated(message="Use parameters named startIndex and endIndex.", replaceWith=@ReplaceWith(expression="subSequence(startIndex = start, endIndex = end)", imports={}))
    private static final CharSequence subSequence(String string2, int n, int n2) {
        return string2.subSequence(n, n2);
    }

    private static final String substring(CharSequence charSequence, int n, int n2) {
        return ((Object)charSequence.subSequence(n, n2)).toString();
    }

    public static final String substring(CharSequence charSequence, IntRange intRange) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$substring");
        Intrinsics.checkNotNullParameter(intRange, "range");
        return ((Object)charSequence.subSequence((Integer)intRange.getStart(), (Integer)intRange.getEndInclusive() + 1)).toString();
    }

    public static final String substring(String string2, IntRange intRange) {
        Intrinsics.checkNotNullParameter(string2, "$this$substring");
        Intrinsics.checkNotNullParameter(intRange, "range");
        string2 = string2.substring((Integer)intRange.getStart(), (Integer)intRange.getEndInclusive() + 1);
        Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        return string2;
    }

    static /* synthetic */ String substring$default(CharSequence charSequence, int n, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = charSequence.length();
        }
        return ((Object)charSequence.subSequence(n, n2)).toString();
    }

    public static final String substringAfter(String string2, char c, String string3) {
        Intrinsics.checkNotNullParameter(string2, "$this$substringAfter");
        Intrinsics.checkNotNullParameter(string3, "missingDelimiterValue");
        int n = StringsKt.indexOf$default((CharSequence)string2, c, 0, false, 6, null);
        if (n == -1) {
            string2 = string3;
        } else {
            string2 = string2.substring(n + 1, string2.length());
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        return string2;
    }

    public static final String substringAfter(String string2, String string3, String string4) {
        Intrinsics.checkNotNullParameter(string2, "$this$substringAfter");
        Intrinsics.checkNotNullParameter(string3, "delimiter");
        Intrinsics.checkNotNullParameter(string4, "missingDelimiterValue");
        int n = StringsKt.indexOf$default((CharSequence)string2, string3, 0, false, 6, null);
        if (n == -1) {
            string2 = string4;
        } else {
            string2 = string2.substring(string3.length() + n, string2.length());
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        return string2;
    }

    public static /* synthetic */ String substringAfter$default(String string2, char c, String string3, int n, Object object) {
        if ((n & 2) != 0) {
            string3 = string2;
        }
        return StringsKt.substringAfter(string2, c, string3);
    }

    public static /* synthetic */ String substringAfter$default(String string2, String string3, String string4, int n, Object object) {
        if ((n & 2) != 0) {
            string4 = string2;
        }
        return StringsKt.substringAfter(string2, string3, string4);
    }

    public static final String substringAfterLast(String string2, char c, String string3) {
        Intrinsics.checkNotNullParameter(string2, "$this$substringAfterLast");
        Intrinsics.checkNotNullParameter(string3, "missingDelimiterValue");
        int n = StringsKt.lastIndexOf$default((CharSequence)string2, c, 0, false, 6, null);
        if (n != -1) {
            string3 = string2.substring(n + 1, string2.length());
            Intrinsics.checkNotNullExpressionValue(string3, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        return string3;
    }

    public static final String substringAfterLast(String string2, String string3, String string4) {
        Intrinsics.checkNotNullParameter(string2, "$this$substringAfterLast");
        Intrinsics.checkNotNullParameter(string3, "delimiter");
        Intrinsics.checkNotNullParameter(string4, "missingDelimiterValue");
        int n = StringsKt.lastIndexOf$default((CharSequence)string2, string3, 0, false, 6, null);
        if (n != -1) {
            string4 = string2.substring(string3.length() + n, string2.length());
            Intrinsics.checkNotNullExpressionValue(string4, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        return string4;
    }

    public static /* synthetic */ String substringAfterLast$default(String string2, char c, String string3, int n, Object object) {
        if ((n & 2) != 0) {
            string3 = string2;
        }
        return StringsKt.substringAfterLast(string2, c, string3);
    }

    public static /* synthetic */ String substringAfterLast$default(String string2, String string3, String string4, int n, Object object) {
        if ((n & 2) != 0) {
            string4 = string2;
        }
        return StringsKt.substringAfterLast(string2, string3, string4);
    }

    public static final String substringBefore(String string2, char c, String string3) {
        Intrinsics.checkNotNullParameter(string2, "$this$substringBefore");
        Intrinsics.checkNotNullParameter(string3, "missingDelimiterValue");
        int n = StringsKt.indexOf$default((CharSequence)string2, c, 0, false, 6, null);
        if (n == -1) {
            string2 = string3;
        } else {
            string2 = string2.substring(0, n);
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        return string2;
    }

    public static final String substringBefore(String string2, String string3, String string4) {
        Intrinsics.checkNotNullParameter(string2, "$this$substringBefore");
        Intrinsics.checkNotNullParameter(string3, "delimiter");
        Intrinsics.checkNotNullParameter(string4, "missingDelimiterValue");
        int n = StringsKt.indexOf$default((CharSequence)string2, string3, 0, false, 6, null);
        if (n == -1) {
            string2 = string4;
        } else {
            string2 = string2.substring(0, n);
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        return string2;
    }

    public static /* synthetic */ String substringBefore$default(String string2, char c, String string3, int n, Object object) {
        if ((n & 2) != 0) {
            string3 = string2;
        }
        return StringsKt.substringBefore(string2, c, string3);
    }

    public static /* synthetic */ String substringBefore$default(String string2, String string3, String string4, int n, Object object) {
        if ((n & 2) != 0) {
            string4 = string2;
        }
        return StringsKt.substringBefore(string2, string3, string4);
    }

    public static final String substringBeforeLast(String string2, char c, String string3) {
        Intrinsics.checkNotNullParameter(string2, "$this$substringBeforeLast");
        Intrinsics.checkNotNullParameter(string3, "missingDelimiterValue");
        int n = StringsKt.lastIndexOf$default((CharSequence)string2, c, 0, false, 6, null);
        if (n == -1) {
            string2 = string3;
        } else {
            string2 = string2.substring(0, n);
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        return string2;
    }

    public static final String substringBeforeLast(String string2, String string3, String string4) {
        Intrinsics.checkNotNullParameter(string2, "$this$substringBeforeLast");
        Intrinsics.checkNotNullParameter(string3, "delimiter");
        Intrinsics.checkNotNullParameter(string4, "missingDelimiterValue");
        int n = StringsKt.lastIndexOf$default((CharSequence)string2, string3, 0, false, 6, null);
        if (n == -1) {
            string2 = string4;
        } else {
            string2 = string2.substring(0, n);
            Intrinsics.checkNotNullExpressionValue(string2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        return string2;
    }

    public static /* synthetic */ String substringBeforeLast$default(String string2, char c, String string3, int n, Object object) {
        if ((n & 2) != 0) {
            string3 = string2;
        }
        return StringsKt.substringBeforeLast(string2, c, string3);
    }

    public static /* synthetic */ String substringBeforeLast$default(String string2, String string3, String string4, int n, Object object) {
        if ((n & 2) != 0) {
            string4 = string2;
        }
        return StringsKt.substringBeforeLast(string2, string3, string4);
    }

    public static final CharSequence trim(CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$trim");
        int n = 0;
        int n2 = charSequence.length() - 1;
        boolean bl = false;
        while (n <= n2) {
            int n3 = !bl ? n : n2;
            boolean bl2 = CharsKt.isWhitespace(charSequence.charAt(n3));
            if (!bl) {
                if (!bl2) {
                    bl = true;
                    continue;
                }
                ++n;
                continue;
            }
            if (!bl2) break;
            --n2;
        }
        return charSequence.subSequence(n, n2 + 1);
    }

    public static final CharSequence trim(CharSequence charSequence, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$trim");
        Intrinsics.checkNotNullParameter(function1, "predicate");
        int n = 0;
        int n2 = charSequence.length() - 1;
        boolean bl = false;
        while (n <= n2) {
            int n3 = !bl ? n : n2;
            boolean bl2 = function1.invoke(Character.valueOf(charSequence.charAt(n3)));
            if (!bl) {
                if (!bl2) {
                    bl = true;
                    continue;
                }
                ++n;
                continue;
            }
            if (!bl2) break;
            --n2;
        }
        return charSequence.subSequence(n, n2 + 1);
    }

    public static final CharSequence trim(CharSequence charSequence, char ... cArray) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$trim");
        Intrinsics.checkNotNullParameter(cArray, "chars");
        int n = 0;
        int n2 = charSequence.length() - 1;
        boolean bl = false;
        while (n <= n2) {
            int n3 = !bl ? n : n2;
            boolean bl2 = ArraysKt.contains(cArray, charSequence.charAt(n3));
            if (!bl) {
                if (!bl2) {
                    bl = true;
                    continue;
                }
                ++n;
                continue;
            }
            if (!bl2) break;
            --n2;
        }
        return charSequence.subSequence(n, n2 + 1);
    }

    private static final String trim(String string2) {
        if (string2 != null) {
            return ((Object)StringsKt.trim((CharSequence)string2)).toString();
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    public static final String trim(String charSequence, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$trim");
        Intrinsics.checkNotNullParameter(function1, "predicate");
        charSequence = charSequence;
        int n = 0;
        int n2 = charSequence.length() - 1;
        boolean bl = false;
        while (n <= n2) {
            int n3 = !bl ? n : n2;
            boolean bl2 = function1.invoke(Character.valueOf(charSequence.charAt(n3)));
            if (!bl) {
                if (!bl2) {
                    bl = true;
                    continue;
                }
                ++n;
                continue;
            }
            if (!bl2) break;
            --n2;
        }
        return ((Object)charSequence.subSequence(n, n2 + 1)).toString();
    }

    public static final String trim(String charSequence, char ... cArray) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$trim");
        Intrinsics.checkNotNullParameter(cArray, "chars");
        charSequence = charSequence;
        int n = 0;
        int n2 = charSequence.length() - 1;
        boolean bl = false;
        while (n <= n2) {
            int n3 = !bl ? n : n2;
            boolean bl2 = ArraysKt.contains(cArray, charSequence.charAt(n3));
            if (!bl) {
                if (!bl2) {
                    bl = true;
                    continue;
                }
                ++n;
                continue;
            }
            if (!bl2) break;
            --n2;
        }
        return ((Object)charSequence.subSequence(n, n2 + 1)).toString();
    }

    public static final CharSequence trimEnd(CharSequence charSequence) {
        block1: {
            Intrinsics.checkNotNullParameter(charSequence, "$this$trimEnd");
            int n = charSequence.length();
            while (--n >= 0) {
                if (CharsKt.isWhitespace(charSequence.charAt(n))) continue;
                charSequence = charSequence.subSequence(0, n + 1);
                break block1;
            }
            charSequence = "";
        }
        return charSequence;
    }

    public static final CharSequence trimEnd(CharSequence charSequence, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$trimEnd");
        Intrinsics.checkNotNullParameter(function1, "predicate");
        int n = charSequence.length();
        while (--n >= 0) {
            if (function1.invoke(Character.valueOf(charSequence.charAt(n))).booleanValue()) continue;
            return charSequence.subSequence(0, n + 1);
        }
        return "";
    }

    public static final CharSequence trimEnd(CharSequence charSequence, char ... cArray) {
        block1: {
            Intrinsics.checkNotNullParameter(charSequence, "$this$trimEnd");
            Intrinsics.checkNotNullParameter(cArray, "chars");
            int n = charSequence.length();
            while (--n >= 0) {
                if (ArraysKt.contains(cArray, charSequence.charAt(n))) continue;
                charSequence = charSequence.subSequence(0, n + 1);
                break block1;
            }
            charSequence = "";
        }
        return charSequence;
    }

    private static final String trimEnd(String string2) {
        if (string2 != null) {
            return ((Object)StringsKt.trimEnd((CharSequence)string2)).toString();
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    public static final String trimEnd(String charSequence, Function1<? super Character, Boolean> function1) {
        block1: {
            Intrinsics.checkNotNullParameter(charSequence, "$this$trimEnd");
            Intrinsics.checkNotNullParameter(function1, "predicate");
            charSequence = charSequence;
            int n = charSequence.length();
            while (--n >= 0) {
                if (function1.invoke(Character.valueOf(charSequence.charAt(n))).booleanValue()) continue;
                charSequence = charSequence.subSequence(0, n + 1);
                break block1;
            }
            charSequence = "";
        }
        return charSequence.toString();
    }

    public static final String trimEnd(String charSequence, char ... cArray) {
        block1: {
            Intrinsics.checkNotNullParameter(charSequence, "$this$trimEnd");
            Intrinsics.checkNotNullParameter(cArray, "chars");
            charSequence = charSequence;
            int n = charSequence.length();
            while (--n >= 0) {
                if (ArraysKt.contains(cArray, charSequence.charAt(n))) continue;
                charSequence = charSequence.subSequence(0, n + 1);
                break block1;
            }
            charSequence = "";
        }
        return charSequence.toString();
    }

    public static final CharSequence trimStart(CharSequence charSequence) {
        block1: {
            Intrinsics.checkNotNullParameter(charSequence, "$this$trimStart");
            int n = charSequence.length();
            for (int i = 0; i < n; ++i) {
                if (CharsKt.isWhitespace(charSequence.charAt(i))) continue;
                charSequence = charSequence.subSequence(i, charSequence.length());
                break block1;
            }
            charSequence = "";
        }
        return charSequence;
    }

    public static final CharSequence trimStart(CharSequence charSequence, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$trimStart");
        Intrinsics.checkNotNullParameter(function1, "predicate");
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            if (function1.invoke(Character.valueOf(charSequence.charAt(i))).booleanValue()) continue;
            return charSequence.subSequence(i, charSequence.length());
        }
        return "";
    }

    public static final CharSequence trimStart(CharSequence charSequence, char ... cArray) {
        block1: {
            Intrinsics.checkNotNullParameter(charSequence, "$this$trimStart");
            Intrinsics.checkNotNullParameter(cArray, "chars");
            int n = charSequence.length();
            for (int i = 0; i < n; ++i) {
                if (ArraysKt.contains(cArray, charSequence.charAt(i))) continue;
                charSequence = charSequence.subSequence(i, charSequence.length());
                break block1;
            }
            charSequence = "";
        }
        return charSequence;
    }

    private static final String trimStart(String string2) {
        if (string2 != null) {
            return ((Object)StringsKt.trimStart((CharSequence)string2)).toString();
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    public static final String trimStart(String charSequence, Function1<? super Character, Boolean> function1) {
        block1: {
            Intrinsics.checkNotNullParameter(charSequence, "$this$trimStart");
            Intrinsics.checkNotNullParameter(function1, "predicate");
            charSequence = charSequence;
            int n = charSequence.length();
            for (int i = 0; i < n; ++i) {
                if (function1.invoke(Character.valueOf(charSequence.charAt(i))).booleanValue()) continue;
                charSequence = charSequence.subSequence(i, charSequence.length());
                break block1;
            }
            charSequence = "";
        }
        return charSequence.toString();
    }

    public static final String trimStart(String charSequence, char ... cArray) {
        block1: {
            Intrinsics.checkNotNullParameter(charSequence, "$this$trimStart");
            Intrinsics.checkNotNullParameter(cArray, "chars");
            charSequence = charSequence;
            int n = charSequence.length();
            for (int i = 0; i < n; ++i) {
                if (ArraysKt.contains(cArray, charSequence.charAt(i))) continue;
                charSequence = charSequence.subSequence(i, charSequence.length());
                break block1;
            }
            charSequence = "";
        }
        return charSequence.toString();
    }
}

