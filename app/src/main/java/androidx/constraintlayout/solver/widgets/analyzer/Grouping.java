/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.widgets.Barrier;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Flow;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.HelperWidget;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetGroup;
import java.util.ArrayList;
import java.util.Iterator;

public class Grouping {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_GROUPING = false;

    public static WidgetGroup findDependents(ConstraintWidget constraintWidget, int n, ArrayList<WidgetGroup> arrayList, WidgetGroup object) {
        Object object2;
        int n2;
        int n3;
        block14: {
            n3 = n == 0 ? constraintWidget.horizontalGroup : constraintWidget.verticalGroup;
            if (n3 != -1 && (object == null || n3 != ((WidgetGroup)object).id)) {
                n2 = 0;
                while (true) {
                    object2 = object;
                    if (n2 >= arrayList.size()) break block14;
                    object2 = arrayList.get(n2);
                    if (((WidgetGroup)object2).getId() == n3) {
                        if (object != null) {
                            ((WidgetGroup)object).moveTo(n, (WidgetGroup)object2);
                            arrayList.remove(object);
                        }
                        break block14;
                    }
                    ++n2;
                }
            }
            object2 = object;
            if (n3 != -1) {
                return object;
            }
        }
        object = object2;
        if (object2 == null) {
            object = object2;
            if (constraintWidget instanceof HelperWidget) {
                n2 = ((HelperWidget)constraintWidget).findGroupInDependents(n);
                object = object2;
                if (n2 != -1) {
                    n3 = 0;
                    while (true) {
                        object = object2;
                        if (n3 >= arrayList.size() || ((WidgetGroup)(object = arrayList.get(n3))).getId() == n2) break;
                        ++n3;
                    }
                }
            }
            object2 = object;
            if (object == null) {
                object2 = new WidgetGroup(n);
            }
            arrayList.add((WidgetGroup)object2);
            object = object2;
        }
        if (((WidgetGroup)object).add(constraintWidget)) {
            if (constraintWidget instanceof Guideline) {
                Guideline guideline = (Guideline)constraintWidget;
                object2 = guideline.getAnchor();
                n3 = guideline.getOrientation() == 0 ? 1 : 0;
                ((ConstraintAnchor)object2).findDependents(n3, arrayList, (WidgetGroup)object);
            }
            if (n == 0) {
                constraintWidget.horizontalGroup = ((WidgetGroup)object).getId();
                constraintWidget.mLeft.findDependents(n, arrayList, (WidgetGroup)object);
                constraintWidget.mRight.findDependents(n, arrayList, (WidgetGroup)object);
            } else {
                constraintWidget.verticalGroup = ((WidgetGroup)object).getId();
                constraintWidget.mTop.findDependents(n, arrayList, (WidgetGroup)object);
                constraintWidget.mBaseline.findDependents(n, arrayList, (WidgetGroup)object);
                constraintWidget.mBottom.findDependents(n, arrayList, (WidgetGroup)object);
            }
            constraintWidget.mCenter.findDependents(n, arrayList, (WidgetGroup)object);
        }
        return object;
    }

    private static WidgetGroup findGroup(ArrayList<WidgetGroup> arrayList, int n) {
        int n2 = arrayList.size();
        for (int i = 0; i < n2; ++i) {
            WidgetGroup widgetGroup = arrayList.get(i);
            if (n != widgetGroup.id) continue;
            return widgetGroup;
        }
        return null;
    }

    /*
     * WARNING - void declaration
     */
    public static boolean simpleSolvingPass(ConstraintWidgetContainer constraintWidgetContainer, BasicMeasure.Measurer object) {
        int n;
        void var8_7;
        ArrayList arrayList;
        ArrayList<WidgetGroup> arrayList2;
        int n2;
        ArrayList<ConstraintWidget> arrayList3 = constraintWidgetContainer.getChildren();
        int n3 = arrayList3.size();
        Object object2 = null;
        ArrayList arrayList4 = null;
        Object object32 = null;
        Object object3 = null;
        ArrayList arrayList5 = null;
        Object object4 = null;
        for (n2 = 0; n2 < n3; ++n2) {
            arrayList2 = arrayList3.get(n2);
            if (!Grouping.validInGroup(constraintWidgetContainer.getHorizontalDimensionBehaviour(), constraintWidgetContainer.getVerticalDimensionBehaviour(), ((ConstraintWidget)((Object)arrayList2)).getHorizontalDimensionBehaviour(), ((ConstraintWidget)((Object)arrayList2)).getVerticalDimensionBehaviour())) {
                return false;
            }
            if (!(arrayList2 instanceof Flow)) continue;
            return false;
        }
        if (constraintWidgetContainer.mMetrics != null) {
            arrayList2 = constraintWidgetContainer.mMetrics;
            ++((Metrics)((Object)arrayList2)).grouping;
        }
        for (n2 = 0; n2 < n3; ++n2) {
            ConstraintWidget constraintWidget = arrayList3.get(n2);
            if (!Grouping.validInGroup(constraintWidgetContainer.getHorizontalDimensionBehaviour(), constraintWidgetContainer.getVerticalDimensionBehaviour(), constraintWidget.getHorizontalDimensionBehaviour(), constraintWidget.getVerticalDimensionBehaviour())) {
                ConstraintWidgetContainer.measure(constraintWidget, (BasicMeasure.Measurer)object, constraintWidgetContainer.mMeasure, BasicMeasure.Measure.SELF_DIMENSIONS);
            }
            ArrayList arrayList6 = object2;
            ArrayList arrayList7 = arrayList4;
            if (constraintWidget instanceof Guideline) {
                arrayList = (Guideline)constraintWidget;
                arrayList2 = arrayList4;
                if (((Guideline)((Object)arrayList)).getOrientation() == 0) {
                    arrayList2 = arrayList4;
                    if (arrayList4 == null) {
                        arrayList2 = new ArrayList();
                    }
                    arrayList2.add((WidgetGroup)((Object)arrayList));
                }
                arrayList6 = object2;
                arrayList7 = arrayList2;
                if (((Guideline)((Object)arrayList)).getOrientation() == 1) {
                    arrayList4 = object2;
                    if (object2 == null) {
                        arrayList4 = new ArrayList();
                    }
                    arrayList4.add(arrayList);
                    arrayList7 = arrayList2;
                    arrayList6 = arrayList4;
                }
            }
            arrayList = var8_7;
            arrayList2 = object3;
            if (constraintWidget instanceof HelperWidget) {
                if (constraintWidget instanceof Barrier) {
                    arrayList4 = (Barrier)constraintWidget;
                    arrayList = var8_7;
                    if (((Barrier)((Object)arrayList4)).getOrientation() == 0) {
                        arrayList = var8_7;
                        if (var8_7 == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(arrayList4);
                    }
                    arrayList2 = object3;
                    if (((Barrier)((Object)arrayList4)).getOrientation() == 1) {
                        arrayList2 = object3;
                        if (object3 == null) {
                            arrayList2 = new ArrayList();
                        }
                        arrayList2.add((WidgetGroup)((Object)arrayList4));
                    }
                } else {
                    arrayList4 = (HelperWidget)constraintWidget;
                    arrayList = var8_7;
                    if (var8_7 == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(arrayList4);
                    arrayList2 = object3;
                    if (object3 == null) {
                        arrayList2 = new ArrayList<WidgetGroup>();
                    }
                    arrayList2.add((WidgetGroup)((Object)arrayList4));
                }
            }
            Object object5 = arrayList5;
            if (constraintWidget.mLeft.mTarget == null) {
                object5 = arrayList5;
                if (constraintWidget.mRight.mTarget == null) {
                    object5 = arrayList5;
                    if (!(constraintWidget instanceof Guideline)) {
                        object5 = arrayList5;
                        if (!(constraintWidget instanceof Barrier)) {
                            object3 = arrayList5;
                            if (arrayList5 == null) {
                                object3 = new ArrayList();
                            }
                            ((ArrayList)object3).add((ConstraintWidget)constraintWidget);
                            object5 = object3;
                        }
                    }
                }
            }
            Object object6 = object4;
            if (constraintWidget.mTop.mTarget == null) {
                object6 = object4;
                if (constraintWidget.mBottom.mTarget == null) {
                    object6 = object4;
                    if (constraintWidget.mBaseline.mTarget == null) {
                        object6 = object4;
                        if (!(constraintWidget instanceof Guideline)) {
                            object6 = object4;
                            if (!(constraintWidget instanceof Barrier)) {
                                object3 = object4;
                                if (object4 == null) {
                                    object3 = new ArrayList();
                                }
                                ((ArrayList)object3).add((ConstraintWidget)constraintWidget);
                                object6 = object3;
                            }
                        }
                    }
                }
            }
            object2 = arrayList6;
            arrayList4 = arrayList7;
            ArrayList arrayList8 = arrayList;
            object3 = arrayList2;
            arrayList5 = object5;
            object4 = object6;
        }
        arrayList2 = new ArrayList();
        if (object2 != null) {
            object = ((ArrayList)object2).iterator();
            while (object.hasNext()) {
                Grouping.findDependents((Guideline)object.next(), 0, arrayList2, null);
            }
        }
        if (var8_7 != null) {
            object = var8_7.iterator();
            while (object.hasNext()) {
                HelperWidget helperWidget = (HelperWidget)object.next();
                arrayList = Grouping.findDependents(helperWidget, 0, arrayList2, null);
                helperWidget.addDependents(arrayList2, 0, (WidgetGroup)((Object)arrayList));
                ((WidgetGroup)((Object)arrayList)).cleanup(arrayList2);
            }
        }
        if (((ConstraintAnchor)(object = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.LEFT))).getDependents() != null) {
            Iterator<ConstraintAnchor> iterator2 = ((ConstraintAnchor)object).getDependents().iterator();
            while (iterator2.hasNext()) {
                Grouping.findDependents(iterator2.next().mOwner, 0, arrayList2, null);
            }
        }
        if (((ConstraintAnchor)(object = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.RIGHT))).getDependents() != null) {
            Iterator<ConstraintAnchor> iterator3 = ((ConstraintAnchor)object).getDependents().iterator();
            while (iterator3.hasNext()) {
                Grouping.findDependents(iterator3.next().mOwner, 0, arrayList2, null);
            }
        }
        if (((ConstraintAnchor)(object = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.CENTER))).getDependents() != null) {
            Iterator<ConstraintAnchor> iterator4 = ((ConstraintAnchor)object).getDependents().iterator();
            while (iterator4.hasNext()) {
                Grouping.findDependents(iterator4.next().mOwner, 0, arrayList2, null);
            }
        }
        if (arrayList5 != null) {
            object = arrayList5.iterator();
            while (object.hasNext()) {
                Grouping.findDependents((ConstraintWidget)object.next(), 0, arrayList2, null);
            }
        }
        if (arrayList4 != null) {
            object = arrayList4.iterator();
            while (object.hasNext()) {
                Grouping.findDependents((Guideline)object.next(), 1, arrayList2, null);
            }
        }
        if (object3 != null) {
            Iterator iterator5 = ((ArrayList)object3).iterator();
            while (iterator5.hasNext()) {
                object3 = (HelperWidget)iterator5.next();
                object = Grouping.findDependents((ConstraintWidget)object3, 1, arrayList2, null);
                ((HelperWidget)object3).addDependents(arrayList2, 1, (WidgetGroup)object);
                ((WidgetGroup)object).cleanup(arrayList2);
            }
        }
        if (((ConstraintAnchor)(object = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.TOP))).getDependents() != null) {
            object = ((ConstraintAnchor)object).getDependents().iterator();
            while (object.hasNext()) {
                Grouping.findDependents(((ConstraintAnchor)object.next()).mOwner, 1, arrayList2, null);
            }
        }
        if (((ConstraintAnchor)(object = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.BASELINE))).getDependents() != null) {
            object3 = ((ConstraintAnchor)object).getDependents().iterator();
            while (object3.hasNext()) {
                Grouping.findDependents(object3.next().mOwner, 1, arrayList2, null);
            }
        }
        if (((ConstraintAnchor)(object = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.BOTTOM))).getDependents() != null) {
            object3 = ((ConstraintAnchor)object).getDependents().iterator();
            while (object3.hasNext()) {
                Grouping.findDependents(object3.next().mOwner, 1, arrayList2, null);
            }
        }
        if (((ConstraintAnchor)(object = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.CENTER))).getDependents() != null) {
            object3 = ((ConstraintAnchor)object).getDependents().iterator();
            while (object3.hasNext()) {
                Grouping.findDependents(((ConstraintAnchor)object3.next()).mOwner, 1, arrayList2, null);
            }
        }
        if (object4 != null) {
            object = ((ArrayList)object4).iterator();
            while (object.hasNext()) {
                Grouping.findDependents((ConstraintWidget)object.next(), 1, arrayList2, null);
            }
        }
        for (n2 = 0; n2 < n3; ++n2) {
            object3 = arrayList3.get(n2);
            if (!((ConstraintWidget)object3).oppositeDimensionsTied()) continue;
            object = Grouping.findGroup(arrayList2, ((ConstraintWidget)object3).horizontalGroup);
            object3 = Grouping.findGroup(arrayList2, ((ConstraintWidget)object3).verticalGroup);
            if (object == null || object3 == null) continue;
            ((WidgetGroup)object).moveTo(0, (WidgetGroup)object3);
            ((WidgetGroup)object3).setOrientation(2);
            arrayList2.remove(object);
        }
        if (arrayList2.size() <= 1) {
            return false;
        }
        Object var8_19 = null;
        arrayList4 = null;
        if (constraintWidgetContainer.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            n2 = 0;
            object = null;
            object4 = arrayList2.iterator();
            object3 = arrayList3;
            while (object4.hasNext()) {
                object2 = (WidgetGroup)object4.next();
                if (((WidgetGroup)object2).getOrientation() == 1) continue;
                ((WidgetGroup)object2).setAuthoritative(false);
                n = ((WidgetGroup)object2).measureWrap(constraintWidgetContainer.getSystem(), 0);
                n3 = n2;
                if (n > n2) {
                    n3 = n;
                    object = object2;
                }
                n2 = n3;
            }
            object3 = var8_19;
            if (object != null) {
                constraintWidgetContainer.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                constraintWidgetContainer.setWidth(n2);
                ((WidgetGroup)object).setAuthoritative(true);
                object3 = object;
            }
        } else {
            object3 = var8_19;
        }
        if (constraintWidgetContainer.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            n2 = 0;
            object = null;
            for (WidgetGroup widgetGroup : arrayList2) {
                if (widgetGroup.getOrientation() == 0) continue;
                widgetGroup.setAuthoritative(false);
                n = widgetGroup.measureWrap(constraintWidgetContainer.getSystem(), 1);
                n3 = n2;
                if (n > n2) {
                    object = widgetGroup;
                    n3 = n;
                }
                n2 = n3;
            }
            if (object != null) {
                constraintWidgetContainer.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                constraintWidgetContainer.setHeight(n2);
                ((WidgetGroup)object).setAuthoritative(true);
            } else {
                object = arrayList4;
            }
        } else {
            object = arrayList4;
        }
        boolean bl = object3 != null || object != null;
        return bl;
    }

    public static boolean validInGroup(ConstraintWidget.DimensionBehaviour dimensionBehaviour, ConstraintWidget.DimensionBehaviour dimensionBehaviour2, ConstraintWidget.DimensionBehaviour dimensionBehaviour3, ConstraintWidget.DimensionBehaviour dimensionBehaviour4) {
        boolean bl = dimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.FIXED || dimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.MATCH_PARENT && dimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        boolean bl2 = dimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.FIXED || dimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.MATCH_PARENT && dimensionBehaviour2 != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        return bl || bl2;
        {
        }
    }
}

