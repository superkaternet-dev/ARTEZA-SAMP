/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.meta.TypeQualifierDefault
 */
package com.google.android.gms.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.meta.TypeQualifierDefault;

@Documented
@Retention(value=RetentionPolicy.CLASS)
@Target(value={ElementType.TYPE, ElementType.PACKAGE})
@TypeQualifierDefault(value={ElementType.METHOD, ElementType.PARAMETER})
public @interface NonNullApi {
}

