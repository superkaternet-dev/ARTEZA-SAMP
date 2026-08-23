/*
 * Decompiled with CFR 0.152.
 */
package kotlin.annotation;

import kotlin.Metadata;

@Metadata(bv={1, 0, 3}, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0011\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011\u00a8\u0006\u0012"}, d2={"Lkotlin/annotation/AnnotationTarget;", "", "(Ljava/lang/String;I)V", "CLASS", "ANNOTATION_CLASS", "TYPE_PARAMETER", "PROPERTY", "FIELD", "LOCAL_VARIABLE", "VALUE_PARAMETER", "CONSTRUCTOR", "FUNCTION", "PROPERTY_GETTER", "PROPERTY_SETTER", "TYPE", "EXPRESSION", "FILE", "TYPEALIAS", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class AnnotationTarget
extends Enum<AnnotationTarget> {
    private static final AnnotationTarget[] $VALUES;
    public static final /* enum */ AnnotationTarget ANNOTATION_CLASS;
    public static final /* enum */ AnnotationTarget CLASS;
    public static final /* enum */ AnnotationTarget CONSTRUCTOR;
    public static final /* enum */ AnnotationTarget EXPRESSION;
    public static final /* enum */ AnnotationTarget FIELD;
    public static final /* enum */ AnnotationTarget FILE;
    public static final /* enum */ AnnotationTarget FUNCTION;
    public static final /* enum */ AnnotationTarget LOCAL_VARIABLE;
    public static final /* enum */ AnnotationTarget PROPERTY;
    public static final /* enum */ AnnotationTarget PROPERTY_GETTER;
    public static final /* enum */ AnnotationTarget PROPERTY_SETTER;
    public static final /* enum */ AnnotationTarget TYPE;
    public static final /* enum */ AnnotationTarget TYPEALIAS;
    public static final /* enum */ AnnotationTarget TYPE_PARAMETER;
    public static final /* enum */ AnnotationTarget VALUE_PARAMETER;

    static {
        AnnotationTarget annotationTarget;
        AnnotationTarget annotationTarget2;
        AnnotationTarget annotationTarget3;
        AnnotationTarget annotationTarget4;
        AnnotationTarget annotationTarget5;
        AnnotationTarget annotationTarget6;
        AnnotationTarget annotationTarget7;
        AnnotationTarget annotationTarget8;
        AnnotationTarget annotationTarget9;
        AnnotationTarget annotationTarget10;
        AnnotationTarget annotationTarget11;
        AnnotationTarget annotationTarget12;
        AnnotationTarget annotationTarget13;
        AnnotationTarget annotationTarget14;
        AnnotationTarget annotationTarget15;
        CLASS = annotationTarget15 = new AnnotationTarget();
        ANNOTATION_CLASS = annotationTarget14 = new AnnotationTarget();
        TYPE_PARAMETER = annotationTarget13 = new AnnotationTarget();
        PROPERTY = annotationTarget12 = new AnnotationTarget();
        FIELD = annotationTarget11 = new AnnotationTarget();
        LOCAL_VARIABLE = annotationTarget10 = new AnnotationTarget();
        VALUE_PARAMETER = annotationTarget9 = new AnnotationTarget();
        CONSTRUCTOR = annotationTarget8 = new AnnotationTarget();
        FUNCTION = annotationTarget7 = new AnnotationTarget();
        PROPERTY_GETTER = annotationTarget6 = new AnnotationTarget();
        PROPERTY_SETTER = annotationTarget5 = new AnnotationTarget();
        TYPE = annotationTarget4 = new AnnotationTarget();
        EXPRESSION = annotationTarget3 = new AnnotationTarget();
        FILE = annotationTarget2 = new AnnotationTarget();
        TYPEALIAS = annotationTarget = new AnnotationTarget();
        $VALUES = new AnnotationTarget[]{annotationTarget15, annotationTarget14, annotationTarget13, annotationTarget12, annotationTarget11, annotationTarget10, annotationTarget9, annotationTarget8, annotationTarget7, annotationTarget6, annotationTarget5, annotationTarget4, annotationTarget3, annotationTarget2, annotationTarget};
    }

    public static AnnotationTarget valueOf(String string2) {
        return Enum.valueOf(AnnotationTarget.class, string2);
    }

    public static AnnotationTarget[] values() {
        return (AnnotationTarget[])$VALUES.clone();
    }
}

