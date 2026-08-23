/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class SpreadBuilder {
    private final ArrayList<Object> list;

    public SpreadBuilder(int n) {
        this.list = new ArrayList(n);
    }

    public void add(Object object) {
        this.list.add(object);
    }

    public void addSpread(Object object2) {
        block9: {
            block6: {
                block8: {
                    block7: {
                        block5: {
                            if (object2 == null) {
                                return;
                            }
                            if (!(object2 instanceof Object[])) break block5;
                            Object[] objectArray = (Object[])object2;
                            if (objectArray.length > 0) {
                                object2 = this.list;
                                ((ArrayList)object2).ensureCapacity(((ArrayList)object2).size() + objectArray.length);
                                Collections.addAll(this.list, objectArray);
                            }
                            break block6;
                        }
                        if (!(object2 instanceof Collection)) break block7;
                        this.list.addAll((Collection)object2);
                        break block6;
                    }
                    if (!(object2 instanceof Iterable)) break block8;
                    for (Object object2 : (Iterable)object2) {
                        this.list.add(object2);
                    }
                    break block6;
                }
                if (object2 instanceof Iterator) {
                    object2 = (Iterator)object2;
                    while (object2.hasNext()) {
                        this.list.add(object2.next());
                    }
                }
                break block9;
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Don't know how to spread ");
        stringBuilder.append(object2.getClass());
        object2 = new UnsupportedOperationException(stringBuilder.toString());
        throw object2;
    }

    public int size() {
        return this.list.size();
    }

    public Object[] toArray(Object[] objectArray) {
        return this.list.toArray(objectArray);
    }
}

