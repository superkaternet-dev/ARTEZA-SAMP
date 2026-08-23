/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.analyzer.Dependency;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyNode;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetRun;
import java.util.ArrayList;
import java.util.Iterator;

public class ChainRun
extends WidgetRun {
    private int chainStyle;
    ArrayList<WidgetRun> widgets = new ArrayList();

    public ChainRun(ConstraintWidget constraintWidget, int n) {
        super(constraintWidget);
        this.orientation = n;
        this.build();
    }

    /*
     * WARNING - void declaration
     */
    private void build() {
        void var3_2;
        ConstraintWidget object2 = this.widget;
        ArrayList<WidgetRun> arrayList = object2.getPreviousChainMember(this.orientation);
        while (arrayList != null) {
            ConstraintWidget constraintWidget = ((ConstraintWidget)((Object)arrayList)).getPreviousChainMember(this.orientation);
            ConstraintWidget constraintWidget2 = arrayList;
            arrayList = constraintWidget;
        }
        this.widget = var3_2;
        this.widgets.add(var3_2.getRun(this.orientation));
        for (arrayList = var3_2.getNextChainMember(this.orientation); arrayList != null; arrayList = ((ConstraintWidget)((Object)arrayList)).getNextChainMember(this.orientation)) {
            this.widgets.add(((ConstraintWidget)((Object)arrayList)).getRun(this.orientation));
        }
        for (WidgetRun widgetRun : this.widgets) {
            if (this.orientation == 0) {
                widgetRun.widget.horizontalChainRun = this;
                continue;
            }
            if (this.orientation != 1) continue;
            widgetRun.widget.verticalChainRun = this;
        }
        int n = this.orientation == 0 && ((ConstraintWidgetContainer)this.widget.getParent()).isRtl() ? 1 : 0;
        if (n != 0 && this.widgets.size() > 1) {
            arrayList = this.widgets;
            this.widget = arrayList.get((int)(arrayList.size() - 1)).widget;
        }
        n = this.orientation == 0 ? this.widget.getHorizontalChainStyle() : this.widget.getVerticalChainStyle();
        this.chainStyle = n;
    }

    private ConstraintWidget getFirstVisibleWidget() {
        for (int i = 0; i < this.widgets.size(); ++i) {
            WidgetRun widgetRun = this.widgets.get(i);
            if (widgetRun.widget.getVisibility() == 8) continue;
            return widgetRun.widget;
        }
        return null;
    }

    private ConstraintWidget getLastVisibleWidget() {
        for (int i = this.widgets.size() - 1; i >= 0; --i) {
            WidgetRun widgetRun = this.widgets.get(i);
            if (widgetRun.widget.getVisibility() == 8) continue;
            return widgetRun.widget;
        }
        return null;
    }

    @Override
    void apply() {
        Object object = this.widgets.iterator();
        while (object.hasNext()) {
            object.next().apply();
        }
        int n = this.widgets.size();
        if (n < 1) {
            return;
        }
        Object object2 = this.widgets.get((int)0).widget;
        object = this.widgets.get((int)(n - 1)).widget;
        if (this.orientation == 0) {
            Object object3 = ((ConstraintWidget)object2).mLeft;
            object = ((ConstraintWidget)object).mRight;
            object2 = this.getTarget((ConstraintAnchor)object3, 0);
            n = ((ConstraintAnchor)object3).getMargin();
            object3 = this.getFirstVisibleWidget();
            if (object3 != null) {
                n = ((ConstraintWidget)object3).mLeft.getMargin();
            }
            if (object2 != null) {
                this.addTarget(this.start, (DependencyNode)object2, n);
            }
            object2 = this.getTarget((ConstraintAnchor)object, 0);
            n = ((ConstraintAnchor)object).getMargin();
            object = this.getLastVisibleWidget();
            if (object != null) {
                n = ((ConstraintWidget)object).mRight.getMargin();
            }
            if (object2 != null) {
                this.addTarget(this.end, (DependencyNode)object2, -n);
            }
        } else {
            object2 = ((ConstraintWidget)object2).mTop;
            object = ((ConstraintWidget)object).mBottom;
            DependencyNode dependencyNode = this.getTarget((ConstraintAnchor)object2, 1);
            n = ((ConstraintAnchor)object2).getMargin();
            object2 = this.getFirstVisibleWidget();
            if (object2 != null) {
                n = ((ConstraintWidget)object2).mTop.getMargin();
            }
            if (dependencyNode != null) {
                this.addTarget(this.start, dependencyNode, n);
            }
            object2 = this.getTarget((ConstraintAnchor)object, 1);
            n = ((ConstraintAnchor)object).getMargin();
            object = this.getLastVisibleWidget();
            if (object != null) {
                n = ((ConstraintWidget)object).mBottom.getMargin();
            }
            if (object2 != null) {
                this.addTarget(this.end, (DependencyNode)object2, -n);
            }
        }
        this.start.updateDelegate = this;
        this.end.updateDelegate = this;
    }

    @Override
    public void applyToWidget() {
        for (int i = 0; i < this.widgets.size(); ++i) {
            this.widgets.get(i).applyToWidget();
        }
    }

    @Override
    void clear() {
        this.runGroup = null;
        Iterator<WidgetRun> iterator2 = this.widgets.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().clear();
        }
    }

    @Override
    public long getWrapDimension() {
        int n = this.widgets.size();
        long l = 0L;
        for (int i = 0; i < n; ++i) {
            WidgetRun widgetRun = this.widgets.get(i);
            l = l + (long)widgetRun.start.margin + widgetRun.getWrapDimension() + (long)widgetRun.end.margin;
        }
        return l;
    }

    @Override
    void reset() {
        this.start.resolved = false;
        this.end.resolved = false;
    }

    @Override
    boolean supportsWrapComputation() {
        int n = this.widgets.size();
        for (int i = 0; i < n; ++i) {
            if (this.widgets.get(i).supportsWrapComputation()) continue;
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ChainRun ");
        CharSequence charSequence = this.orientation == 0 ? "horizontal : " : "vertical : ";
        stringBuilder.append((String)charSequence);
        charSequence = stringBuilder.toString();
        for (WidgetRun widgetRun : this.widgets) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append((String)charSequence);
            stringBuilder2.append("<");
            charSequence = stringBuilder2.toString();
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append((String)charSequence);
            stringBuilder2.append(widgetRun);
            String object = stringBuilder2.toString();
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append(object);
            ((StringBuilder)charSequence).append("> ");
            charSequence = ((StringBuilder)charSequence).toString();
        }
        return charSequence;
    }

    @Override
    public void update(Dependency object) {
        if (this.start.resolved && this.end.resolved) {
            int n;
            int n2;
            int n3;
            int n4;
            float f;
            float f2;
            int n5;
            int n6;
            int n7;
            int n8;
            int n9;
            int n10;
            int n11;
            int n12;
            boolean bl;
            block94: {
                block93: {
                    boolean bl2;
                    object = this.widget.getParent();
                    bl = bl2 = false;
                    if (object != null) {
                        bl = bl2;
                        if (object instanceof ConstraintWidgetContainer) {
                            bl = ((ConstraintWidgetContainer)object).isRtl();
                        }
                    }
                    n12 = this.end.value - this.start.value;
                    n11 = this.widgets.size();
                    n10 = -1;
                    n9 = 0;
                    while (true) {
                        n8 = n10;
                        if (n9 >= n11) break block93;
                        if (this.widgets.get((int)n9).widget.getVisibility() != 8) break;
                        ++n9;
                    }
                    n8 = n9;
                }
                n10 = -1;
                n9 = n11 - 1;
                while (true) {
                    n7 = n10;
                    if (n9 < 0) break block94;
                    if (this.widgets.get((int)n9).widget.getVisibility() != 8) break;
                    --n9;
                }
                n7 = n9;
            }
            int n13 = 0;
            while (true) {
                n6 = 0;
                n5 = 0;
                f2 = 0.0f;
                f = 0.0f;
                n10 = 0;
                n9 = 0;
                n4 = 0;
                n3 = 0;
                if (n13 >= 2) break;
                for (n4 = 0; n4 < n11; ++n4) {
                    WidgetRun widgetRun = this.widgets.get(n4);
                    if (widgetRun.widget.getVisibility() == 8) {
                        n2 = n9;
                        f2 = f;
                    } else {
                        n = n5 + 1;
                        n10 = n3;
                        if (n4 > 0) {
                            n10 = n3;
                            if (n4 >= n8) {
                                n10 = n3 + widgetRun.start.margin;
                            }
                        }
                        n5 = widgetRun.dimension.value;
                        n3 = widgetRun.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? 1 : 0;
                        if (n3 != 0) {
                            if (this.orientation == 0 && !widgetRun.widget.horizontalRun.dimension.resolved) {
                                return;
                            }
                            if (this.orientation == 1 && !widgetRun.widget.verticalRun.dimension.resolved) {
                                return;
                            }
                        } else if (widgetRun.matchConstraintsType == 1 && n13 == 0) {
                            n3 = 1;
                            n5 = widgetRun.dimension.wrapValue;
                            ++n9;
                        } else if (widgetRun.dimension.resolved) {
                            n3 = 1;
                        }
                        if (n3 == 0) {
                            ++n9;
                            float f3 = widgetRun.widget.mWeight[this.orientation];
                            f2 = f;
                            if (f3 >= 0.0f) {
                                f2 = f + f3;
                            }
                            f = f2;
                        } else {
                            n10 += n5;
                        }
                        n3 = n10;
                        n2 = n9;
                        f2 = f;
                        n5 = n;
                        if (n4 < n11 - 1) {
                            n3 = n10;
                            n2 = n9;
                            f2 = f;
                            n5 = n;
                            if (n4 < n7) {
                                n3 = n10 + -widgetRun.end.margin;
                                n5 = n;
                                f2 = f;
                                n2 = n9;
                            }
                        }
                    }
                    n9 = n2;
                    f = f2;
                }
                n4 = n3;
                n10 = n9;
                f2 = f;
                n6 = n5;
                if (n3 < n12) break;
                if (n9 == 0) {
                    n4 = n3;
                    n10 = n9;
                    f2 = f;
                    n6 = n5;
                    break;
                }
                ++n13;
            }
            n3 = this.start.value;
            if (bl) {
                n3 = this.end.value;
            }
            n9 = n3;
            if (n4 > n12) {
                n9 = bl ? n3 + (int)((float)(n4 - n12) / 2.0f + 0.5f) : n3 - (int)((float)(n4 - n12) / 2.0f + 0.5f);
            }
            if (n10 > 0) {
                n5 = (int)((float)(n12 - n4) / (float)n10 + 0.5f);
                n3 = 0;
                n13 = n4;
                n2 = n9;
                for (int i = 0; i < n11; ++i) {
                    int n14;
                    object = this.widgets.get(i);
                    if (((WidgetRun)object).widget.getVisibility() == 8 || ((WidgetRun)object).dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || ((WidgetRun)object).dimension.resolved) continue;
                    n9 = n5;
                    if (f2 > 0.0f) {
                        f = ((WidgetRun)object).widget.mWeight[this.orientation];
                        n9 = (int)((float)(n12 - n13) * f / f2 + 0.5f);
                    }
                    if (this.orientation == 0) {
                        n14 = ((WidgetRun)object).widget.mMatchConstraintMaxWidth;
                        n = ((WidgetRun)object).widget.mMatchConstraintMinWidth;
                        n4 = ((WidgetRun)object).matchConstraintsType == 1 ? Math.min(n9, ((WidgetRun)object).dimension.wrapValue) : n9;
                        n4 = n = Math.max(n, n4);
                        if (n14 > 0) {
                            n4 = Math.min(n14, n);
                        }
                        n14 = n9;
                        n = n3;
                        if (n4 != n9) {
                            n = n3 + 1;
                            n14 = n4;
                        }
                        n4 = n;
                    } else {
                        n14 = ((WidgetRun)object).widget.mMatchConstraintMaxHeight;
                        int n15 = ((WidgetRun)object).widget.mMatchConstraintMinHeight;
                        n4 = n = n9;
                        if (((WidgetRun)object).matchConstraintsType == 1) {
                            n4 = Math.min(n, ((WidgetRun)object).dimension.wrapValue);
                        }
                        n = n4 = Math.max(n15, n4);
                        if (n14 > 0) {
                            n = Math.min(n14, n4);
                        }
                        n14 = n9;
                        n4 = n3;
                        if (n != n9) {
                            n4 = n3 + 1;
                            n14 = n;
                        }
                    }
                    ((WidgetRun)object).dimension.resolve(n14);
                    n3 = n4;
                }
                if (n3 > 0) {
                    n5 = n10 - n3;
                    n9 = 0;
                    for (n10 = 0; n10 < n11; ++n10) {
                        object = this.widgets.get(n10);
                        if (((WidgetRun)object).widget.getVisibility() == 8) continue;
                        n4 = n9;
                        if (n10 > 0) {
                            n4 = n9;
                            if (n10 >= n8) {
                                n4 = n9 + ((WidgetRun)object).start.margin;
                            }
                        }
                        n9 = n4 += ((WidgetRun)object).dimension.value;
                        if (n10 >= n11 - 1) continue;
                        n9 = n4;
                        if (n10 >= n7) continue;
                        n9 = n4 + -((WidgetRun)object).end.margin;
                    }
                    n10 = n5;
                } else {
                    n9 = n13;
                }
                if (this.chainStyle == 2 && n3 == 0) {
                    this.chainStyle = 0;
                }
                n4 = n9;
                n5 = n10;
                n9 = n2;
            } else {
                n5 = n10;
            }
            if (n4 > n12) {
                this.chainStyle = 2;
            }
            if (n6 > 0 && n5 == 0 && n8 == n7) {
                this.chainStyle = 2;
            }
            if ((n10 = this.chainStyle) == 1) {
                n10 = 0;
                if (n6 > 1) {
                    n10 = (n12 - n4) / (n6 - 1);
                } else if (n6 == 1) {
                    n10 = (n12 - n4) / 2;
                }
                n4 = n10;
                if (n5 > 0) {
                    n4 = 0;
                }
                n3 = n9;
                for (n10 = 0; n10 < n11; ++n10) {
                    n9 = n10;
                    if (bl) {
                        n9 = n11 - (n10 + 1);
                    }
                    object = this.widgets.get(n9);
                    if (((WidgetRun)object).widget.getVisibility() == 8) {
                        ((WidgetRun)object).start.resolve(n3);
                        ((WidgetRun)object).end.resolve(n3);
                        n9 = n3;
                    } else {
                        n9 = n3;
                        if (n10 > 0) {
                            n9 = bl ? n3 - n4 : n3 + n4;
                        }
                        n3 = n9;
                        if (n10 > 0) {
                            n3 = n9;
                            if (n10 >= n8) {
                                n3 = bl ? n9 - ((WidgetRun)object).start.margin : n9 + ((WidgetRun)object).start.margin;
                            }
                        }
                        if (bl) {
                            ((WidgetRun)object).end.resolve(n3);
                        } else {
                            ((WidgetRun)object).start.resolve(n3);
                        }
                        n9 = n5 = ((WidgetRun)object).dimension.value;
                        if (((WidgetRun)object).dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                            n9 = n5;
                            if (((WidgetRun)object).matchConstraintsType == 1) {
                                n9 = ((WidgetRun)object).dimension.wrapValue;
                            }
                        }
                        n3 = bl ? (n3 -= n9) : (n3 += n9);
                        if (bl) {
                            ((WidgetRun)object).start.resolve(n3);
                        } else {
                            ((WidgetRun)object).end.resolve(n3);
                        }
                        ((WidgetRun)object).resolved = true;
                        n9 = n3;
                        if (n10 < n11 - 1) {
                            n9 = n3;
                            if (n10 < n7) {
                                n9 = bl ? n3 - -((WidgetRun)object).end.margin : n3 + -((WidgetRun)object).end.margin;
                            }
                        }
                    }
                    n3 = n9;
                }
            } else if (n10 == 0) {
                n3 = (n12 - n4) / (n6 + 1);
                if (n5 > 0) {
                    n3 = 0;
                }
                n4 = n3;
                for (n10 = 0; n10 < n11; ++n10) {
                    n3 = n10;
                    if (bl) {
                        n3 = n11 - (n10 + 1);
                    }
                    object = this.widgets.get(n3);
                    if (((WidgetRun)object).widget.getVisibility() == 8) {
                        ((WidgetRun)object).start.resolve(n9);
                        ((WidgetRun)object).end.resolve(n9);
                        continue;
                    }
                    n3 = bl ? n9 - n4 : n9 + n4;
                    n9 = n3;
                    if (n10 > 0) {
                        n9 = n3;
                        if (n10 >= n8) {
                            n9 = bl ? n3 - ((WidgetRun)object).start.margin : n3 + ((WidgetRun)object).start.margin;
                        }
                    }
                    if (bl) {
                        ((WidgetRun)object).end.resolve(n9);
                    } else {
                        ((WidgetRun)object).start.resolve(n9);
                    }
                    n3 = n5 = ((WidgetRun)object).dimension.value;
                    if (((WidgetRun)object).dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        n3 = n5;
                        if (((WidgetRun)object).matchConstraintsType == 1) {
                            n3 = Math.min(n5, ((WidgetRun)object).dimension.wrapValue);
                        }
                    }
                    n3 = bl ? n9 - n3 : n9 + n3;
                    if (bl) {
                        ((WidgetRun)object).start.resolve(n3);
                    } else {
                        ((WidgetRun)object).end.resolve(n3);
                    }
                    n9 = n3;
                    if (n10 >= n11 - 1) continue;
                    n9 = n3;
                    if (n10 >= n7) continue;
                    n9 = bl ? n3 - -((WidgetRun)object).end.margin : n3 + -((WidgetRun)object).end.margin;
                }
            } else if (n10 == 2) {
                f = this.orientation == 0 ? this.widget.getHorizontalBiasPercent() : this.widget.getVerticalBiasPercent();
                f2 = f;
                if (bl) {
                    f2 = 1.0f - f;
                }
                if ((n10 = (int)((float)(n12 - n4) * f2 + 0.5f)) < 0 || n5 > 0) {
                    n10 = 0;
                }
                n9 = bl ? (n9 -= n10) : (n9 += n10);
                for (n10 = 0; n10 < n11; ++n10) {
                    n3 = n10;
                    if (bl) {
                        n3 = n11 - (n10 + 1);
                    }
                    object = this.widgets.get(n3);
                    if (((WidgetRun)object).widget.getVisibility() == 8) {
                        ((WidgetRun)object).start.resolve(n9);
                        ((WidgetRun)object).end.resolve(n9);
                        continue;
                    }
                    n3 = n9;
                    if (n10 > 0) {
                        n3 = n9;
                        if (n10 >= n8) {
                            n3 = bl ? n9 - ((WidgetRun)object).start.margin : n9 + ((WidgetRun)object).start.margin;
                        }
                    }
                    if (bl) {
                        ((WidgetRun)object).end.resolve(n3);
                    } else {
                        ((WidgetRun)object).start.resolve(n3);
                    }
                    n9 = ((WidgetRun)object).dimension.value;
                    if (((WidgetRun)object).dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && ((WidgetRun)object).matchConstraintsType == 1) {
                        n9 = ((WidgetRun)object).dimension.wrapValue;
                    }
                    n3 = bl ? (n3 -= n9) : (n3 += n9);
                    if (bl) {
                        ((WidgetRun)object).start.resolve(n3);
                    } else {
                        ((WidgetRun)object).end.resolve(n3);
                    }
                    n9 = n3;
                    if (n10 >= n11 - 1) continue;
                    n9 = n3;
                    if (n10 >= n7) continue;
                    n9 = bl ? n3 - -((WidgetRun)object).end.margin : n3 + -((WidgetRun)object).end.margin;
                }
            }
            return;
        }
    }
}

