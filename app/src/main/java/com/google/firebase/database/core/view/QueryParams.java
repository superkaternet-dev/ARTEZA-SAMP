/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.view;

import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.filter.IndexedFilter;
import com.google.firebase.database.core.view.filter.LimitedFilter;
import com.google.firebase.database.core.view.filter.NodeFilter;
import com.google.firebase.database.core.view.filter.RangedFilter;
import com.google.firebase.database.snapshot.BooleanNode;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.DoubleNode;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.database.snapshot.LongNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.NodeUtilities;
import com.google.firebase.database.snapshot.PriorityIndex;
import com.google.firebase.database.snapshot.PriorityUtilities;
import com.google.firebase.database.snapshot.StringNode;
import com.google.firebase.database.util.JsonMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class QueryParams {
    public static final QueryParams DEFAULT_PARAMS = new QueryParams();
    private static final String INDEX = "i";
    private static final String INDEX_END_NAME = "en";
    private static final String INDEX_END_VALUE = "ep";
    private static final String INDEX_START_NAME = "sn";
    private static final String INDEX_START_VALUE = "sp";
    private static final String LIMIT = "l";
    private static final String VIEW_FROM = "vf";
    private Index index = PriorityIndex.getInstance();
    private ChildKey indexEndName = null;
    private Node indexEndValue = null;
    private ChildKey indexStartName = null;
    private Node indexStartValue = null;
    private String jsonSerialization = null;
    private Integer limit;
    private ViewFrom viewFrom;

    private QueryParams copy() {
        QueryParams queryParams = new QueryParams();
        queryParams.limit = this.limit;
        queryParams.indexStartValue = this.indexStartValue;
        queryParams.indexStartName = this.indexStartName;
        queryParams.indexEndValue = this.indexEndValue;
        queryParams.indexEndName = this.indexEndName;
        queryParams.viewFrom = this.viewFrom;
        queryParams.index = this.index;
        return queryParams;
    }

    public static QueryParams fromQueryObject(Map<String, Object> object) {
        Object object2;
        QueryParams queryParams = new QueryParams();
        queryParams.limit = (Integer)object.get(LIMIT);
        if (object.containsKey(INDEX_START_VALUE)) {
            queryParams.indexStartValue = QueryParams.normalizeValue(NodeUtilities.NodeFromJSON(object.get(INDEX_START_VALUE)));
            object2 = (String)object.get(INDEX_START_NAME);
            if (object2 != null) {
                queryParams.indexStartName = ChildKey.fromString(object2);
            }
        }
        if (object.containsKey(INDEX_END_VALUE)) {
            queryParams.indexEndValue = QueryParams.normalizeValue(NodeUtilities.NodeFromJSON(object.get(INDEX_END_VALUE)));
            object2 = (String)object.get(INDEX_END_NAME);
            if (object2 != null) {
                queryParams.indexEndName = ChildKey.fromString(object2);
            }
        }
        if ((object2 = (String)object.get(VIEW_FROM)) != null) {
            object2 = object2.equals(LIMIT) ? ViewFrom.LEFT : ViewFrom.RIGHT;
            queryParams.viewFrom = object2;
        }
        if ((object = (String)object.get(INDEX)) != null) {
            queryParams.index = Index.fromQueryDefinition((String)object);
        }
        return queryParams;
    }

    private static Node normalizeValue(Node node) {
        if (!(node instanceof StringNode || node instanceof BooleanNode || node instanceof DoubleNode || node instanceof EmptyNode)) {
            if (node instanceof LongNode) {
                return new DoubleNode(((Long)node.getValue()).doubleValue(), PriorityUtilities.NullPriority());
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected value passed to normalizeValue: ");
            stringBuilder.append(node.getValue());
            throw new IllegalStateException(stringBuilder.toString());
        }
        return node;
    }

    public QueryParams endAt(Node node, ChildKey childKey) {
        boolean bl = node.isLeafNode() || node.isEmpty();
        Utilities.hardAssert(bl);
        Utilities.hardAssert(node instanceof LongNode ^ true);
        QueryParams queryParams = this.copy();
        queryParams.indexEndValue = node;
        queryParams.indexEndName = childKey;
        return queryParams;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            object = (QueryParams)object;
            Object object2 = this.limit;
            if (object2 != null ? !((Integer)object2).equals(((QueryParams)object).limit) : ((QueryParams)object).limit != null) {
                return false;
            }
            object2 = this.index;
            if (object2 != null ? !object2.equals(((QueryParams)object).index) : ((QueryParams)object).index != null) {
                return false;
            }
            object2 = this.indexEndName;
            if (object2 != null ? !((ChildKey)object2).equals(((QueryParams)object).indexEndName) : ((QueryParams)object).indexEndName != null) {
                return false;
            }
            object2 = this.indexEndValue;
            if (object2 != null ? !object2.equals(((QueryParams)object).indexEndValue) : ((QueryParams)object).indexEndValue != null) {
                return false;
            }
            object2 = this.indexStartName;
            if (object2 != null ? !((ChildKey)object2).equals(((QueryParams)object).indexStartName) : ((QueryParams)object).indexStartName != null) {
                return false;
            }
            object2 = this.indexStartValue;
            if (object2 != null ? !object2.equals(((QueryParams)object).indexStartValue) : ((QueryParams)object).indexStartValue != null) {
                return false;
            }
            return this.isViewFromLeft() == ((QueryParams)object).isViewFromLeft();
        }
        return false;
    }

    public Index getIndex() {
        return this.index;
    }

    public ChildKey getIndexEndName() {
        if (this.hasEnd()) {
            ChildKey childKey = this.indexEndName;
            if (childKey != null) {
                return childKey;
            }
            return ChildKey.getMaxName();
        }
        throw new IllegalArgumentException("Cannot get index end name if start has not been set");
    }

    public Node getIndexEndValue() {
        if (this.hasEnd()) {
            return this.indexEndValue;
        }
        throw new IllegalArgumentException("Cannot get index end value if start has not been set");
    }

    public ChildKey getIndexStartName() {
        if (this.hasStart()) {
            ChildKey childKey = this.indexStartName;
            if (childKey != null) {
                return childKey;
            }
            return ChildKey.getMinName();
        }
        throw new IllegalArgumentException("Cannot get index start name if start has not been set");
    }

    public Node getIndexStartValue() {
        if (this.hasStart()) {
            return this.indexStartValue;
        }
        throw new IllegalArgumentException("Cannot get index start value if start has not been set");
    }

    public int getLimit() {
        if (this.hasLimit()) {
            return this.limit;
        }
        throw new IllegalArgumentException("Cannot get limit if limit has not been set");
    }

    public NodeFilter getNodeFilter() {
        if (this.loadsAllData()) {
            return new IndexedFilter(this.getIndex());
        }
        if (this.hasLimit()) {
            return new LimitedFilter(this);
        }
        return new RangedFilter(this);
    }

    public Map<String, Object> getWireProtocolParams() {
        Object object;
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        if (this.hasStart()) {
            hashMap.put(INDEX_START_VALUE, this.indexStartValue.getValue());
            object = this.indexStartName;
            if (object != null) {
                hashMap.put(INDEX_START_NAME, ((ChildKey)object).asString());
            }
        }
        if (this.hasEnd()) {
            hashMap.put(INDEX_END_VALUE, this.indexEndValue.getValue());
            object = this.indexEndName;
            if (object != null) {
                hashMap.put(INDEX_END_NAME, ((ChildKey)object).asString());
            }
        }
        if ((object = this.limit) != null) {
            hashMap.put(LIMIT, object);
            ViewFrom viewFrom = this.viewFrom;
            object = viewFrom;
            if (viewFrom == null) {
                object = this.hasStart() ? ViewFrom.LEFT : ViewFrom.RIGHT;
            }
            switch (1.$SwitchMap$com$google$firebase$database$core$view$QueryParams$ViewFrom[((Enum)object).ordinal()]) {
                default: {
                    break;
                }
                case 2: {
                    hashMap.put(VIEW_FROM, "r");
                    break;
                }
                case 1: {
                    hashMap.put(VIEW_FROM, LIMIT);
                }
            }
        }
        if (!this.index.equals(PriorityIndex.getInstance())) {
            hashMap.put(INDEX, this.index.getQueryDefinition());
        }
        return hashMap;
    }

    public boolean hasAnchoredLimit() {
        boolean bl = this.hasLimit() && this.viewFrom != null;
        return bl;
    }

    public boolean hasEnd() {
        boolean bl = this.indexEndValue != null;
        return bl;
    }

    public boolean hasLimit() {
        boolean bl = this.limit != null;
        return bl;
    }

    public boolean hasStart() {
        boolean bl = this.indexStartValue != null;
        return bl;
    }

    public int hashCode() {
        Object object = this.limit;
        int n = 0;
        int n2 = object != null ? (Integer)object : 0;
        int n3 = this.isViewFromLeft() ? 1231 : 1237;
        object = this.indexStartValue;
        int n4 = object != null ? object.hashCode() : 0;
        object = this.indexStartName;
        int n5 = object != null ? ((ChildKey)object).hashCode() : 0;
        object = this.indexEndValue;
        int n6 = object != null ? object.hashCode() : 0;
        object = this.indexEndName;
        int n7 = object != null ? ((ChildKey)object).hashCode() : 0;
        object = this.index;
        if (object != null) {
            n = object.hashCode();
        }
        return (((((n2 * 31 + n3) * 31 + n4) * 31 + n5) * 31 + n6) * 31 + n7) * 31 + n;
    }

    public boolean isDefault() {
        boolean bl = this.loadsAllData() && this.index.equals(PriorityIndex.getInstance());
        return bl;
    }

    public boolean isValid() {
        boolean bl = !this.hasStart() || !this.hasEnd() || !this.hasLimit() || this.hasAnchoredLimit();
        return bl;
    }

    public boolean isViewFromLeft() {
        ViewFrom viewFrom = this.viewFrom;
        boolean bl = viewFrom != null ? viewFrom == ViewFrom.LEFT : this.hasStart();
        return bl;
    }

    public QueryParams limitToFirst(int n) {
        QueryParams queryParams = this.copy();
        queryParams.limit = n;
        queryParams.viewFrom = ViewFrom.LEFT;
        return queryParams;
    }

    public QueryParams limitToLast(int n) {
        QueryParams queryParams = this.copy();
        queryParams.limit = n;
        queryParams.viewFrom = ViewFrom.RIGHT;
        return queryParams;
    }

    public boolean loadsAllData() {
        boolean bl = !this.hasStart() && !this.hasEnd() && !this.hasLimit();
        return bl;
    }

    public QueryParams orderBy(Index index) {
        QueryParams queryParams = this.copy();
        queryParams.index = index;
        return queryParams;
    }

    public QueryParams startAt(Node node, ChildKey childKey) {
        boolean bl = node.isLeafNode() || node.isEmpty();
        Utilities.hardAssert(bl);
        Utilities.hardAssert(node instanceof LongNode ^ true);
        QueryParams queryParams = this.copy();
        queryParams.indexStartValue = node;
        queryParams.indexStartName = childKey;
        return queryParams;
    }

    public String toJSON() {
        if (this.jsonSerialization == null) {
            try {
                this.jsonSerialization = JsonMapper.serializeJson(this.getWireProtocolParams());
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }
        return this.jsonSerialization;
    }

    public String toString() {
        return this.getWireProtocolParams().toString();
    }

    private static final class ViewFrom
    extends Enum<ViewFrom> {
        private static final ViewFrom[] $VALUES;
        public static final /* enum */ ViewFrom LEFT;
        public static final /* enum */ ViewFrom RIGHT;

        static {
            ViewFrom viewFrom;
            ViewFrom viewFrom2;
            LEFT = viewFrom2 = new ViewFrom();
            RIGHT = viewFrom = new ViewFrom();
            $VALUES = new ViewFrom[]{viewFrom2, viewFrom};
        }

        public static ViewFrom valueOf(String string2) {
            return Enum.valueOf(ViewFrom.class, string2);
        }

        public static ViewFrom[] values() {
            return (ViewFrom[])$VALUES.clone();
        }
    }
}

