/*
 * Decompiled with CFR 0.152.
 */
package androidx.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(value=RetentionPolicy.CLASS)
@Target(value={ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Size {
    public long max() default 0x7FFFFFFFFFFFFFFFL;

    public long min() default -9223372036854775808L;

    public long multiple() default 1L;

    public long value() default -1L;
}

