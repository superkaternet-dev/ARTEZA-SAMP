/*
 * Decompiled with CFR 0.152.
 */
package kotlin.sequences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.sequences.ConstrainedOnceSequence;
import kotlin.sequences.EmptySequence;
import kotlin.sequences.FlatteningSequence;
import kotlin.sequences.GeneratorSequence;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequenceScope;
import kotlin.sequences.SequencesKt;
import kotlin.sequences.SequencesKt__SequencesJVMKt;
import kotlin.sequences.SequencesKt__SequencesKt;
import kotlin.sequences.TransformingSequence;

@Metadata(bv={1, 0, 3}, d1={"\u0000L\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\u001c\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\u001a.\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0014\b\u0004\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\u0004H\u0087\b\u00f8\u0001\u0000\u001a\u0012\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002\u001ab\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\b0\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\t\"\u0004\b\u0002\u0010\b2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0018\u0010\u000b\u001a\u0014\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\f2\u0018\u0010\u0003\u001a\u0014\u0012\u0004\u0012\u0002H\t\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00050\u000eH\u0000\u001a&\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u00102\u000e\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0004\u001a<\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u00102\u000e\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u00042\u0014\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000e\u001a=\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u00102\b\u0010\u0013\u001a\u0004\u0018\u0001H\u00022\u0014\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000eH\u0007\u00a2\u0006\u0002\u0010\u0014\u001a+\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0016\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0017\"\u0002H\u0002\u00a2\u0006\u0002\u0010\u0018\u001a\u001c\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0005\u001a\u001c\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001\u001aC\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\b0\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\b*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0018\u0010\u0003\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00050\u000eH\u0002\u00a2\u0006\u0002\b\u001c\u001a)\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u001d0\u0001H\u0007\u00a2\u0006\u0002\b\u001e\u001a\"\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0001\u001a2\u0010\u001f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0012\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0004H\u0007\u001a!\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0004\u0012\u0002H\u0002\u0018\u00010\u0001H\u0087\b\u001a\u001e\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0007\u001a&\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010#\u001a\u00020$H\u0007\u001a@\u0010%\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020'\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0'0&\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\b*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\b0&0\u0001\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006("}, d2={"Sequence", "Lkotlin/sequences/Sequence;", "T", "iterator", "Lkotlin/Function0;", "", "emptySequence", "flatMapIndexed", "R", "C", "source", "transform", "Lkotlin/Function2;", "", "Lkotlin/Function1;", "generateSequence", "", "nextFunction", "seedFunction", "seed", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Lkotlin/sequences/Sequence;", "sequenceOf", "elements", "", "([Ljava/lang/Object;)Lkotlin/sequences/Sequence;", "asSequence", "constrainOnce", "flatten", "flatten$SequencesKt__SequencesKt", "", "flattenSequenceOfIterable", "ifEmpty", "defaultValue", "orEmpty", "shuffled", "random", "Lkotlin/random/Random;", "unzip", "Lkotlin/Pair;", "", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/sequences/SequencesKt")
class SequencesKt__SequencesKt
extends SequencesKt__SequencesJVMKt {
    private static final <T> Sequence<T> Sequence(Function0<? extends Iterator<? extends T>> function0) {
        return new Sequence<T>(function0){
            final Function0 $iterator;
            {
                this.$iterator = function0;
            }

            public Iterator<T> iterator() {
                return (Iterator)this.$iterator.invoke();
            }
        };
    }

    public static final <T> Sequence<T> asSequence(Iterator<? extends T> iterator2) {
        Intrinsics.checkNotNullParameter(iterator2, "$this$asSequence");
        return SequencesKt.constrainOnce(new Sequence<T>(iterator2){
            final Iterator $this_asSequence$inlined;
            {
                this.$this_asSequence$inlined = iterator2;
            }

            public Iterator<T> iterator() {
                return this.$this_asSequence$inlined;
            }
        });
    }

    public static final <T> Sequence<T> constrainOnce(Sequence<? extends T> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "$this$constrainOnce");
        if (!(sequence instanceof ConstrainedOnceSequence)) {
            sequence = new ConstrainedOnceSequence<T>(sequence);
        }
        return sequence;
    }

    public static final <T> Sequence<T> emptySequence() {
        return EmptySequence.INSTANCE;
    }

    public static final <T, C, R> Sequence<R> flatMapIndexed(Sequence<? extends T> sequence, Function2<? super Integer, ? super T, ? extends C> function2, Function1<? super C, ? extends Iterator<? extends R>> function1) {
        Intrinsics.checkNotNullParameter(sequence, "source");
        Intrinsics.checkNotNullParameter(function2, "transform");
        Intrinsics.checkNotNullParameter(function1, "iterator");
        return SequencesKt.sequence(new Function2<SequenceScope<? super R>, Continuation<? super Unit>, Object>(sequence, function2, function1, null){
            final Function1 $iterator;
            final Sequence $source;
            final Function2 $transform;
            int I$0;
            private Object L$0;
            Object L$1;
            int label;
            {
                this.$source = sequence;
                this.$transform = function2;
                this.$iterator = function1;
                super(2, continuation);
            }

            public final Continuation<Unit> create(Object object, Continuation<?> function2) {
                Intrinsics.checkNotNullParameter(function2, "completion");
                function2 = new /* invalid duplicate definition of identical inner class */;
                function2.L$0 = object;
                return function2;
            }

            public final Object invoke(Object object, Object object2) {
                return (this.create(object, (Continuation)object2)).invokeSuspend(Unit.INSTANCE);
            }

            public final Object invokeSuspend(Object object) {
                Object object2;
                Iterator<T> iterator2;
                int n;
                flatMapIndexed.1 var4_3;
                Object object3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    default: {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    case 1: {
                        var4_3 = this;
                        n = var4_3.I$0;
                        iterator2 = (Iterator<T>)var4_3.L$1;
                        object2 = (SequenceScope)var4_3.L$0;
                        ResultKt.throwOnFailure(object);
                        object = object2;
                        break;
                    }
                    case 0: {
                        ResultKt.throwOnFailure(object);
                        var4_3 = this;
                        object = (SequenceScope)var4_3.L$0;
                        n = 0;
                        iterator2 = var4_3.$source.iterator();
                    }
                }
                while (iterator2.hasNext()) {
                    T t = iterator2.next();
                    object2 = var4_3.$transform;
                    int n2 = n + 1;
                    if (n < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    object2 = object2.invoke(Boxing.boxInt(n), t);
                    object2 = (Iterator)var4_3.$iterator.invoke(object2);
                    var4_3.L$0 = object;
                    var4_3.L$1 = iterator2;
                    var4_3.I$0 = n2;
                    var4_3.label = 1;
                    if (((SequenceScope)object).yieldAll(object2, (Continuation<? super Unit>)var4_3) == object3) {
                        return object3;
                    }
                    n = n2;
                }
                return Unit.INSTANCE;
            }
        });
    }

    public static final <T> Sequence<T> flatten(Sequence<? extends Sequence<? extends T>> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "$this$flatten");
        return SequencesKt__SequencesKt.flatten$SequencesKt__SequencesKt(sequence, flatten.1.INSTANCE);
    }

    private static final <T, R> Sequence<R> flatten$SequencesKt__SequencesKt(Sequence<? extends T> sequence, Function1<? super T, ? extends Iterator<? extends R>> function1) {
        if (sequence instanceof TransformingSequence) {
            return ((TransformingSequence)sequence).flatten$kotlin_stdlib(function1);
        }
        return new FlatteningSequence(sequence, flatten.3.INSTANCE, function1);
    }

    public static final <T> Sequence<T> flattenSequenceOfIterable(Sequence<? extends Iterable<? extends T>> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "$this$flatten");
        return SequencesKt__SequencesKt.flatten$SequencesKt__SequencesKt(sequence, flatten.2.INSTANCE);
    }

    public static final <T> Sequence<T> generateSequence(T object, Function1<? super T, ? extends T> function1) {
        Intrinsics.checkNotNullParameter(function1, "nextFunction");
        object = object == null ? (Sequence)EmptySequence.INSTANCE : (Sequence)new GeneratorSequence<T>(new Function0<T>(object){
            final Object $seed;
            {
                this.$seed = object;
                super(0);
            }

            public final T invoke() {
                return (T)this.$seed;
            }
        }, function1);
        return object;
    }

    public static final <T> Sequence<T> generateSequence(Function0<? extends T> function0) {
        Intrinsics.checkNotNullParameter(function0, "nextFunction");
        return SequencesKt.constrainOnce((Sequence)new GeneratorSequence<T>(function0, new Function1<T, T>(function0){
            final Function0 $nextFunction;
            {
                this.$nextFunction = function0;
                super(1);
            }

            public final T invoke(T t) {
                Intrinsics.checkNotNullParameter(t, "it");
                return (T)this.$nextFunction.invoke();
            }
        }));
    }

    public static final <T> Sequence<T> generateSequence(Function0<? extends T> function0, Function1<? super T, ? extends T> function1) {
        Intrinsics.checkNotNullParameter(function0, "seedFunction");
        Intrinsics.checkNotNullParameter(function1, "nextFunction");
        return new GeneratorSequence<T>(function0, function1);
    }

    public static final <T> Sequence<T> ifEmpty(Sequence<? extends T> sequence, Function0<? extends Sequence<? extends T>> function0) {
        Intrinsics.checkNotNullParameter(sequence, "$this$ifEmpty");
        Intrinsics.checkNotNullParameter(function0, "defaultValue");
        return SequencesKt.sequence(new Function2<SequenceScope<? super T>, Continuation<? super Unit>, Object>(sequence, function0, null){
            final Function0 $defaultValue;
            final Sequence $this_ifEmpty;
            private Object L$0;
            int label;
            {
                this.$this_ifEmpty = sequence;
                this.$defaultValue = function0;
                super(2, continuation);
            }

            public final Continuation<Unit> create(Object object, Continuation<?> function2) {
                Intrinsics.checkNotNullParameter(function2, "completion");
                function2 = new /* invalid duplicate definition of identical inner class */;
                function2.L$0 = object;
                return function2;
            }

            public final Object invoke(Object object, Object object2) {
                return (this.create(object, (Continuation)object2)).invokeSuspend(Unit.INSTANCE);
            }

            public final Object invokeSuspend(Object object) {
                Object object2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    default: {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    case 2: {
                        ResultKt.throwOnFailure(object);
                        break;
                    }
                    case 1: {
                        ResultKt.throwOnFailure(object);
                        break;
                    }
                    case 0: {
                        ResultKt.throwOnFailure(object);
                        object = (SequenceScope)this.L$0;
                        Object object3 = this.$this_ifEmpty.iterator();
                        if (object3.hasNext()) {
                            this.label = 1;
                            if (((SequenceScope)object).yieldAll(object3, (Continuation<? super Unit>)this) != object2) break;
                            return object2;
                        }
                        object3 = (Sequence)this.$defaultValue.invoke();
                        this.label = 2;
                        if (((SequenceScope)object).yieldAll(object3, (Continuation<? super Unit>)this) != object2) break;
                        return object2;
                    }
                }
                return Unit.INSTANCE;
            }
        });
    }

    private static final <T> Sequence<T> orEmpty(Sequence<? extends T> sequence) {
        if (sequence == null) {
            sequence = SequencesKt.emptySequence();
        }
        return sequence;
    }

    public static final <T> Sequence<T> sequenceOf(T ... object) {
        Intrinsics.checkNotNullParameter(object, "elements");
        boolean bl = ((T[])object).length == 0;
        object = bl ? SequencesKt.emptySequence() : ArraysKt.asSequence(object);
        return object;
    }

    public static final <T> Sequence<T> shuffled(Sequence<? extends T> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "$this$shuffled");
        return SequencesKt.shuffled(sequence, Random.Default);
    }

    public static final <T> Sequence<T> shuffled(Sequence<? extends T> sequence, Random random) {
        Intrinsics.checkNotNullParameter(sequence, "$this$shuffled");
        Intrinsics.checkNotNullParameter(random, "random");
        return SequencesKt.sequence(new Function2<SequenceScope<? super T>, Continuation<? super Unit>, Object>(sequence, random, null){
            final Random $random;
            final Sequence $this_shuffled;
            private Object L$0;
            Object L$1;
            int label;
            {
                this.$this_shuffled = sequence;
                this.$random = random;
                super(2, continuation);
            }

            public final Continuation<Unit> create(Object object, Continuation<?> function2) {
                Intrinsics.checkNotNullParameter(function2, "completion");
                function2 = new /* invalid duplicate definition of identical inner class */;
                function2.L$0 = object;
                return function2;
            }

            public final Object invoke(Object object, Object object2) {
                return (this.create(object, (Continuation)object2)).invokeSuspend(Unit.INSTANCE);
            }

            /*
             * Exception decompiling
             */
            public final Object invokeSuspend(Object var1_1) {
                /*
                 * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
                 * 
                 * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[SWITCH], 3[CASE]], but top level block is 4[WHILELOOP]
                 *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
                 *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
                 *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
                 *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
                 *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
                 *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
                 *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
                 *     at org.benf.cfr.reader.entities.Method.dump(Method.java:598)
                 *     at org.benf.cfr.reader.entities.classfilehelpers.ClassFileDumperAnonymousInner.dumpWithArgs(ClassFileDumperAnonymousInner.java:87)
                 *     at org.benf.cfr.reader.bytecode.analysis.parse.expression.ConstructorInvokationAnonymousInner.dumpInner(ConstructorInvokationAnonymousInner.java:82)
                 *     at org.benf.cfr.reader.bytecode.analysis.parse.expression.AbstractExpression.dumpWithOuterPrecedence(AbstractExpression.java:142)
                 *     at org.benf.cfr.reader.bytecode.analysis.parse.expression.AbstractExpression.dump(AbstractExpression.java:98)
                 *     at org.benf.cfr.reader.state.TypeUsageCollectingDumper.dump(TypeUsageCollectingDumper.java:194)
                 *     at org.benf.cfr.reader.bytecode.analysis.parse.expression.StaticFunctionInvokation.dumpInner(StaticFunctionInvokation.java:143)
                 *     at org.benf.cfr.reader.bytecode.analysis.parse.expression.AbstractExpression.dumpWithOuterPrecedence(AbstractExpression.java:142)
                 *     at org.benf.cfr.reader.bytecode.analysis.parse.expression.AbstractExpression.dump(AbstractExpression.java:98)
                 *     at org.benf.cfr.reader.state.TypeUsageCollectingDumper.dump(TypeUsageCollectingDumper.java:194)
                 *     at org.benf.cfr.reader.bytecode.analysis.structured.statement.StructuredReturn.dump(StructuredReturn.java:60)
                 *     at org.benf.cfr.reader.state.TypeUsageCollectingDumper.dump(TypeUsageCollectingDumper.java:194)
                 *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.dump(Op04StructuredStatement.java:220)
                 *     at org.benf.cfr.reader.bytecode.analysis.structured.statement.Block.dump(Block.java:564)
                 *     at org.benf.cfr.reader.state.TypeUsageCollectingDumper.dump(TypeUsageCollectingDumper.java:194)
                 *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.dump(Op04StructuredStatement.java:220)
                 *     at org.benf.cfr.reader.entities.attributes.AttributeCode.dump(AttributeCode.java:135)
                 *     at org.benf.cfr.reader.state.TypeUsageCollectingDumper.dump(TypeUsageCollectingDumper.java:194)
                 *     at org.benf.cfr.reader.entities.Method.dump(Method.java:627)
                 *     at org.benf.cfr.reader.entities.classfilehelpers.AbstractClassFileDumper.dumpMethods(AbstractClassFileDumper.java:211)
                 *     at org.benf.cfr.reader.entities.classfilehelpers.ClassFileDumperNormal.dump(ClassFileDumperNormal.java:70)
                 *     at org.benf.cfr.reader.entities.ClassFile.dump(ClassFile.java:1167)
                 *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:952)
                 *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
                 *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
                 *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
                 *     at org.benf.cfr.reader.Main.main(Main.java:54)
                 */
                throw new IllegalStateException("Decompilation failed");
            }
        });
    }

    public static final <T, R> Pair<List<T>, List<R>> unzip(Sequence<? extends Pair<? extends T, ? extends R>> object) {
        Intrinsics.checkNotNullParameter(object, "$this$unzip");
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        Iterator<Pair<T, R>> iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            object = iterator2.next();
            arrayList.add(((Pair)object).getFirst());
            arrayList2.add(((Pair)object).getSecond());
        }
        return TuplesKt.to(arrayList, arrayList2);
    }
}

