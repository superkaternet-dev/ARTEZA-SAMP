/*
 * Decompiled with CFR 0.152.
 */
package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.CLASS)
@Target(value={ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE})
public @interface RestrictTo {
    public Scope[] value();

    public static final class Scope
    extends Enum<Scope> {
        private static final Scope[] $VALUES;
        @Deprecated
        public static final /* enum */ Scope GROUP_ID;
        public static final /* enum */ Scope LIBRARY;
        public static final /* enum */ Scope LIBRARY_GROUP;
        public static final /* enum */ Scope LIBRARY_GROUP_PREFIX;
        public static final /* enum */ Scope SUBCLASSES;
        public static final /* enum */ Scope TESTS;

        static {
            Scope scope;
            Scope scope2;
            Scope scope3;
            Scope scope4;
            Scope scope5;
            Scope scope6;
            LIBRARY = scope6 = new Scope();
            LIBRARY_GROUP = scope5 = new Scope();
            LIBRARY_GROUP_PREFIX = scope4 = new Scope();
            GROUP_ID = scope3 = new Scope();
            TESTS = scope2 = new Scope();
            SUBCLASSES = scope = new Scope();
            $VALUES = new Scope[]{scope6, scope5, scope4, scope3, scope2, scope};
        }

        public static Scope valueOf(String string2) {
            return Enum.valueOf(Scope.class, string2);
        }

        public static Scope[] values() {
            return (Scope[])$VALUES.clone();
        }
    }
}

