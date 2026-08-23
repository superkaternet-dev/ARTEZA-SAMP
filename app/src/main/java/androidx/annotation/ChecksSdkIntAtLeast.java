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
@Target(value={ElementType.METHOD, ElementType.FIELD})
public @interface ChecksSdkIntAtLeast {
    public int api() default -1;

    public String codename() default "";

    public int lambda() default -1;

    public int parameter() default -1;
}

