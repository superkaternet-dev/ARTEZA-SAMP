/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.analyzer.Dependency;
import androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetRun;
import java.util.ArrayList;
import java.util.List;

public class DependencyNode
implements Dependency {
    public boolean delegateToWidgetRun = false;
    List<Dependency> dependencies;
    int margin;
    DimensionDependency marginDependency = null;
    int marginFactor = 1;
    public boolean readyToSolve = false;
    public boolean resolved = false;
    WidgetRun run;
    List<DependencyNode> targets;
    Type type = Type.UNKNOWN;
    public Dependency updateDelegate = null;
    public int value;

    public DependencyNode(WidgetRun widgetRun) {
        this.dependencies = new ArrayList<Dependency>();
        this.targets = new ArrayList<DependencyNode>();
        this.run = widgetRun;
    }

    public void addDependency(Dependency dependency) {
        this.dependencies.add(dependency);
        if (this.resolved) {
            dependency.update(dependency);
        }
    }

    public void clear() {
        this.targets.clear();
        this.dependencies.clear();
        this.resolved = false;
        this.value = 0;
        this.readyToSolve = false;
        this.delegateToWidgetRun = false;
    }

    public String name() {
        StringBuilder stringBuilder;
        String string2 = this.run.widget.getDebugName();
        if (this.type != Type.LEFT && this.type != Type.RIGHT) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("_VERTICAL");
            string2 = stringBuilder.toString();
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("_HORIZONTAL");
            string2 = stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(":");
        stringBuilder.append(this.type.name());
        return stringBuilder.toString();
    }

    public void resolve(int n) {
        if (this.resolved) {
            return;
        }
        this.resolved = true;
        this.value = n;
        for (Dependency dependency : this.dependencies) {
            dependency.update(dependency);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.run.widget.getDebugName());
        stringBuilder.append(":");
        stringBuilder.append((Object)this.type);
        stringBuilder.append("(");
        Object object = this.resolved ? Integer.valueOf(this.value) : "unresolved";
        stringBuilder.append(object);
        stringBuilder.append(") <t=");
        stringBuilder.append(this.targets.size());
        stringBuilder.append(":d=");
        stringBuilder.append(this.dependencies.size());
        stringBuilder.append(">");
        return stringBuilder.toString();
    }

    @Override
    public void update(Dependency object) {
        object = this.targets.iterator();
        while (object.hasNext()) {
            if (((DependencyNode)object.next()).resolved) continue;
            return;
        }
        this.readyToSolve = true;
        object = this.updateDelegate;
        if (object != null) {
            object.update(this);
        }
        if (this.delegateToWidgetRun) {
            this.run.update(this);
            return;
        }
        object = null;
        int n = 0;
        for (DependencyNode dependencyNode : this.targets) {
            if (dependencyNode instanceof DimensionDependency) continue;
            object = dependencyNode;
            ++n;
        }
        if (object != null && n == 1 && ((DependencyNode)object).resolved) {
            DependencyNode dependencyNode;
            dependencyNode = this.marginDependency;
            if (dependencyNode != null) {
                if (((DimensionDependency)dependencyNode).resolved) {
                    this.margin = this.marginFactor * this.marginDependency.value;
                } else {
                    return;
                }
            }
            this.resolve(((DependencyNode)object).value + this.margin);
        }
        if ((object = this.updateDelegate) != null) {
            object.update(this);
        }
    }

    static final class Type
    extends Enum<Type> {
        private static final Type[] $VALUES;
        public static final /* enum */ Type BASELINE;
        public static final /* enum */ Type BOTTOM;
        public static final /* enum */ Type HORIZONTAL_DIMENSION;
        public static final /* enum */ Type LEFT;
        public static final /* enum */ Type RIGHT;
        public static final /* enum */ Type TOP;
        public static final /* enum */ Type UNKNOWN;
        public static final /* enum */ Type VERTICAL_DIMENSION;

        static {
            Type type;
            Type type2;
            Type type3;
            Type type4;
            Type type5;
            Type type6;
            Type type7;
            Type type8;
            UNKNOWN = type8 = new Type();
            HORIZONTAL_DIMENSION = type7 = new Type();
            VERTICAL_DIMENSION = type6 = new Type();
            LEFT = type5 = new Type();
            RIGHT = type4 = new Type();
            TOP = type3 = new Type();
            BOTTOM = type2 = new Type();
            BASELINE = type = new Type();
            $VALUES = new Type[]{type8, type7, type6, type5, type4, type3, type2, type};
        }

        public static Type valueOf(String string2) {
            return Enum.valueOf(Type.class, string2);
        }

        public static Type[] values() {
            return (Type[])$VALUES.clone();
        }
    }
}

