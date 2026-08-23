/*
 * Decompiled with CFR 0.152.
 */
package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.SOURCE)
@Target(value={ElementType.METHOD})
public @interface InspectableProperty {
    public int attributeId() default 0;

    public EnumEntry[] enumMapping() default {};

    public FlagEntry[] flagMapping() default {};

    public boolean hasAttributeId() default true;

    public String name() default "";

    public ValueType valueType() default ValueType.INFERRED;

    @Retention(value=RetentionPolicy.SOURCE)
    @Target(value={ElementType.TYPE})
    public static @interface EnumEntry {
        public String name();

        public int value();
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @Target(value={ElementType.TYPE})
    public static @interface FlagEntry {
        public int mask() default 0;

        public String name();

        public int target();
    }

    public static final class ValueType
    extends Enum<ValueType> {
        private static final ValueType[] $VALUES;
        public static final /* enum */ ValueType COLOR;
        public static final /* enum */ ValueType GRAVITY;
        public static final /* enum */ ValueType INFERRED;
        public static final /* enum */ ValueType INT_ENUM;
        public static final /* enum */ ValueType INT_FLAG;
        public static final /* enum */ ValueType NONE;
        public static final /* enum */ ValueType RESOURCE_ID;

        static {
            ValueType valueType;
            ValueType valueType2;
            ValueType valueType3;
            ValueType valueType4;
            ValueType valueType5;
            ValueType valueType6;
            ValueType valueType7;
            NONE = valueType7 = new ValueType();
            INFERRED = valueType6 = new ValueType();
            INT_ENUM = valueType5 = new ValueType();
            INT_FLAG = valueType4 = new ValueType();
            COLOR = valueType3 = new ValueType();
            GRAVITY = valueType2 = new ValueType();
            RESOURCE_ID = valueType = new ValueType();
            $VALUES = new ValueType[]{valueType7, valueType6, valueType5, valueType4, valueType3, valueType2, valueType};
        }

        public static ValueType valueOf(String string2) {
            return Enum.valueOf(ValueType.class, string2);
        }

        public static ValueType[] values() {
            return (ValueType[])$VALUES.clone();
        }
    }
}

