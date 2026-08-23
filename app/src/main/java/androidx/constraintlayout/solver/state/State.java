/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.state;

import androidx.constraintlayout.solver.state.ConstraintReference;
import androidx.constraintlayout.solver.state.Dimension;
import androidx.constraintlayout.solver.state.HelperReference;
import androidx.constraintlayout.solver.state.Reference;
import androidx.constraintlayout.solver.state.helpers.AlignHorizontallyReference;
import androidx.constraintlayout.solver.state.helpers.AlignVerticallyReference;
import androidx.constraintlayout.solver.state.helpers.BarrierReference;
import androidx.constraintlayout.solver.state.helpers.GuidelineReference;
import androidx.constraintlayout.solver.state.helpers.HorizontalChainReference;
import androidx.constraintlayout.solver.state.helpers.VerticalChainReference;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.HelperWidget;
import androidx.constraintlayout.solver.widgets.WidgetContainer;
import java.util.HashMap;

public class State {
    static final int CONSTRAINT_RATIO = 2;
    static final int CONSTRAINT_SPREAD = 0;
    static final int CONSTRAINT_WRAP = 1;
    public static final Integer PARENT = 0;
    static final int UNKNOWN = -1;
    protected HashMap<Object, HelperReference> mHelperReferences;
    public final ConstraintReference mParent;
    protected HashMap<Object, Reference> mReferences = new HashMap();
    private int numHelpers;

    public State() {
        ConstraintReference constraintReference;
        this.mHelperReferences = new HashMap();
        this.mParent = constraintReference = new ConstraintReference(this);
        this.numHelpers = 0;
        this.mReferences.put(PARENT, constraintReference);
    }

    private String createHelperKey() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("__HELPER_KEY_");
        int n = this.numHelpers;
        this.numHelpers = n + 1;
        stringBuilder.append(n);
        stringBuilder.append("__");
        return stringBuilder.toString();
    }

    /*
     * WARNING - void declaration
     */
    public void apply(ConstraintWidgetContainer iterator2) {
        Object object4;
        ((WidgetContainer)((Object)iterator2)).removeAllChildren();
        this.mParent.getWidth().apply(this, (ConstraintWidget)((Object)iterator2), 0);
        this.mParent.getHeight().apply(this, (ConstraintWidget)((Object)iterator2), 1);
        for (Object object2 : this.mHelperReferences.keySet()) {
            void iterator3;
            HelperWidget helperWidget = this.mHelperReferences.get(object2).getHelperWidget();
            if (helperWidget == null) continue;
            Object object3 = object4 = this.mReferences.get(object2);
            if (object4 == null) {
                ConstraintReference constraintReference = this.constraints(object2);
            }
            iterator3.setConstraintWidget(helperWidget);
        }
        for (Object object4 : this.mReferences.keySet()) {
            Object object5 = this.mReferences.get(object4);
            if (object5 != this.mParent) {
                object4 = object5.getConstraintWidget();
                ((ConstraintWidget)object4).setParent(null);
                if (object5 instanceof GuidelineReference) {
                    object5.apply();
                }
                ((WidgetContainer)((Object)iterator2)).add((ConstraintWidget)object4);
                continue;
            }
            object5.setConstraintWidget((ConstraintWidget)((Object)iterator2));
        }
        for (Object object6 : this.mHelperReferences.keySet()) {
            object4 = this.mHelperReferences.get(object6);
            if (((HelperReference)object4).getHelperWidget() == null) continue;
            for (Object object5 : ((HelperReference)object4).mReferences) {
                object5 = this.mReferences.get(object5);
                ((HelperReference)object4).getHelperWidget().add(object5.getConstraintWidget());
            }
            ((HelperReference)object4).apply();
        }
        for (Object object7 : this.mReferences.keySet()) {
            this.mReferences.get(object7).apply();
        }
    }

    public BarrierReference barrier(Object object, Direction direction) {
        object = (BarrierReference)this.helper(object, Helper.BARRIER);
        ((BarrierReference)object).setBarrierDirection(direction);
        return object;
    }

    public AlignHorizontallyReference centerHorizontally(Object ... objectArray) {
        AlignHorizontallyReference alignHorizontallyReference = (AlignHorizontallyReference)this.helper(null, Helper.ALIGN_HORIZONTALLY);
        alignHorizontallyReference.add(objectArray);
        return alignHorizontallyReference;
    }

    public AlignVerticallyReference centerVertically(Object ... objectArray) {
        AlignVerticallyReference alignVerticallyReference = (AlignVerticallyReference)this.helper(null, Helper.ALIGN_VERTICALLY);
        alignVerticallyReference.add(objectArray);
        return alignVerticallyReference;
    }

    public ConstraintReference constraints(Object object) {
        Reference reference;
        Reference reference2 = reference = this.mReferences.get(object);
        if (reference == null) {
            reference2 = this.createConstraintReference(object);
            this.mReferences.put(object, reference2);
            reference2.setKey(object);
        }
        if (reference2 instanceof ConstraintReference) {
            return (ConstraintReference)reference2;
        }
        return null;
    }

    public int convertDimension(Object object) {
        if (object instanceof Float) {
            return ((Float)object).intValue();
        }
        if (object instanceof Integer) {
            return (Integer)object;
        }
        return 0;
    }

    public ConstraintReference createConstraintReference(Object object) {
        return new ConstraintReference(this);
    }

    public void directMapping() {
        for (Object object : this.mReferences.keySet()) {
            this.constraints(object).setView(object);
        }
    }

    public GuidelineReference guideline(Object object, int n) {
        Reference reference;
        Reference reference2 = reference = this.mReferences.get(object);
        if (reference == null) {
            reference2 = new GuidelineReference(this);
            ((GuidelineReference)reference2).setOrientation(n);
            ((GuidelineReference)reference2).setKey(object);
            this.mReferences.put(object, reference2);
        }
        return (GuidelineReference)reference2;
    }

    public State height(Dimension dimension) {
        return this.setHeight(dimension);
    }

    public HelperReference helper(Object object, Helper helper) {
        Object object2 = object;
        if (object == null) {
            object2 = this.createHelperKey();
        }
        HelperReference helperReference = this.mHelperReferences.get(object2);
        object = helperReference;
        if (helperReference == null) {
            switch (1.$SwitchMap$androidx$constraintlayout$solver$state$State$Helper[helper.ordinal()]) {
                default: {
                    object = new HelperReference(this, helper);
                    break;
                }
                case 5: {
                    object = new BarrierReference(this);
                    break;
                }
                case 4: {
                    object = new AlignVerticallyReference(this);
                    break;
                }
                case 3: {
                    object = new AlignHorizontallyReference(this);
                    break;
                }
                case 2: {
                    object = new VerticalChainReference(this);
                    break;
                }
                case 1: {
                    object = new HorizontalChainReference(this);
                }
            }
            this.mHelperReferences.put(object2, (HelperReference)object);
        }
        return object;
    }

    public HorizontalChainReference horizontalChain(Object ... objectArray) {
        HorizontalChainReference horizontalChainReference = (HorizontalChainReference)this.helper(null, Helper.HORIZONTAL_CHAIN);
        horizontalChainReference.add(objectArray);
        return horizontalChainReference;
    }

    public GuidelineReference horizontalGuideline(Object object) {
        return this.guideline(object, 0);
    }

    public void map(Object object, Object object2) {
        this.constraints(object).setView(object2);
    }

    Reference reference(Object object) {
        return this.mReferences.get(object);
    }

    public void reset() {
        this.mHelperReferences.clear();
    }

    public State setHeight(Dimension dimension) {
        this.mParent.setHeight(dimension);
        return this;
    }

    public State setWidth(Dimension dimension) {
        this.mParent.setWidth(dimension);
        return this;
    }

    public VerticalChainReference verticalChain(Object ... objectArray) {
        VerticalChainReference verticalChainReference = (VerticalChainReference)this.helper(null, Helper.VERTICAL_CHAIN);
        verticalChainReference.add(objectArray);
        return verticalChainReference;
    }

    public GuidelineReference verticalGuideline(Object object) {
        return this.guideline(object, 1);
    }

    public State width(Dimension dimension) {
        return this.setWidth(dimension);
    }

    public static final class Chain
    extends Enum<Chain> {
        private static final Chain[] $VALUES;
        public static final /* enum */ Chain PACKED;
        public static final /* enum */ Chain SPREAD;
        public static final /* enum */ Chain SPREAD_INSIDE;

        static {
            Chain chain;
            Chain chain2;
            Chain chain3;
            SPREAD = chain3 = new Chain();
            SPREAD_INSIDE = chain2 = new Chain();
            PACKED = chain = new Chain();
            $VALUES = new Chain[]{chain3, chain2, chain};
        }

        public static Chain valueOf(String string2) {
            return Enum.valueOf(Chain.class, string2);
        }

        public static Chain[] values() {
            return (Chain[])$VALUES.clone();
        }
    }

    public static final class Constraint
    extends Enum<Constraint> {
        private static final Constraint[] $VALUES;
        public static final /* enum */ Constraint BASELINE_TO_BASELINE;
        public static final /* enum */ Constraint BOTTOM_TO_BOTTOM;
        public static final /* enum */ Constraint BOTTOM_TO_TOP;
        public static final /* enum */ Constraint CENTER_HORIZONTALLY;
        public static final /* enum */ Constraint CENTER_VERTICALLY;
        public static final /* enum */ Constraint END_TO_END;
        public static final /* enum */ Constraint END_TO_START;
        public static final /* enum */ Constraint LEFT_TO_LEFT;
        public static final /* enum */ Constraint LEFT_TO_RIGHT;
        public static final /* enum */ Constraint RIGHT_TO_LEFT;
        public static final /* enum */ Constraint RIGHT_TO_RIGHT;
        public static final /* enum */ Constraint START_TO_END;
        public static final /* enum */ Constraint START_TO_START;
        public static final /* enum */ Constraint TOP_TO_BOTTOM;
        public static final /* enum */ Constraint TOP_TO_TOP;

        static {
            Constraint constraint;
            Constraint constraint2;
            Constraint constraint3;
            Constraint constraint4;
            Constraint constraint5;
            Constraint constraint6;
            Constraint constraint7;
            Constraint constraint8;
            Constraint constraint9;
            Constraint constraint10;
            Constraint constraint11;
            Constraint constraint12;
            Constraint constraint13;
            Constraint constraint14;
            Constraint constraint15;
            LEFT_TO_LEFT = constraint15 = new Constraint();
            LEFT_TO_RIGHT = constraint14 = new Constraint();
            RIGHT_TO_LEFT = constraint13 = new Constraint();
            RIGHT_TO_RIGHT = constraint12 = new Constraint();
            START_TO_START = constraint11 = new Constraint();
            START_TO_END = constraint10 = new Constraint();
            END_TO_START = constraint9 = new Constraint();
            END_TO_END = constraint8 = new Constraint();
            TOP_TO_TOP = constraint7 = new Constraint();
            TOP_TO_BOTTOM = constraint6 = new Constraint();
            BOTTOM_TO_TOP = constraint5 = new Constraint();
            BOTTOM_TO_BOTTOM = constraint4 = new Constraint();
            BASELINE_TO_BASELINE = constraint3 = new Constraint();
            CENTER_HORIZONTALLY = constraint2 = new Constraint();
            CENTER_VERTICALLY = constraint = new Constraint();
            $VALUES = new Constraint[]{constraint15, constraint14, constraint13, constraint12, constraint11, constraint10, constraint9, constraint8, constraint7, constraint6, constraint5, constraint4, constraint3, constraint2, constraint};
        }

        public static Constraint valueOf(String string2) {
            return Enum.valueOf(Constraint.class, string2);
        }

        public static Constraint[] values() {
            return (Constraint[])$VALUES.clone();
        }
    }

    public static final class Direction
    extends Enum<Direction> {
        private static final Direction[] $VALUES;
        public static final /* enum */ Direction BOTTOM;
        public static final /* enum */ Direction END;
        public static final /* enum */ Direction LEFT;
        public static final /* enum */ Direction RIGHT;
        public static final /* enum */ Direction START;
        public static final /* enum */ Direction TOP;

        static {
            Direction direction;
            Direction direction2;
            Direction direction3;
            Direction direction4;
            Direction direction5;
            Direction direction6;
            LEFT = direction6 = new Direction();
            RIGHT = direction5 = new Direction();
            START = direction4 = new Direction();
            END = direction3 = new Direction();
            TOP = direction2 = new Direction();
            BOTTOM = direction = new Direction();
            $VALUES = new Direction[]{direction6, direction5, direction4, direction3, direction2, direction};
        }

        public static Direction valueOf(String string2) {
            return Enum.valueOf(Direction.class, string2);
        }

        public static Direction[] values() {
            return (Direction[])$VALUES.clone();
        }
    }

    public static final class Helper
    extends Enum<Helper> {
        private static final Helper[] $VALUES;
        public static final /* enum */ Helper ALIGN_HORIZONTALLY;
        public static final /* enum */ Helper ALIGN_VERTICALLY;
        public static final /* enum */ Helper BARRIER;
        public static final /* enum */ Helper FLOW;
        public static final /* enum */ Helper HORIZONTAL_CHAIN;
        public static final /* enum */ Helper LAYER;
        public static final /* enum */ Helper VERTICAL_CHAIN;

        static {
            Helper helper;
            Helper helper2;
            Helper helper3;
            Helper helper4;
            Helper helper5;
            Helper helper6;
            Helper helper7;
            HORIZONTAL_CHAIN = helper7 = new Helper();
            VERTICAL_CHAIN = helper6 = new Helper();
            ALIGN_HORIZONTALLY = helper5 = new Helper();
            ALIGN_VERTICALLY = helper4 = new Helper();
            BARRIER = helper3 = new Helper();
            LAYER = helper2 = new Helper();
            FLOW = helper = new Helper();
            $VALUES = new Helper[]{helper7, helper6, helper5, helper4, helper3, helper2, helper};
        }

        public static Helper valueOf(String string2) {
            return Enum.valueOf(Helper.class, string2);
        }

        public static Helper[] values() {
            return (Helper[])$VALUES.clone();
        }
    }
}

