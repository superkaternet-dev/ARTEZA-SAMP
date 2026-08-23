/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteDatabaseLockedException
 *  android.database.sqlite.SQLiteException
 *  android.database.sqlite.SQLiteOpenHelper
 */
package com.google.firebase.database.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.core.CompoundWrite;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.UserWriteRecord;
import com.google.firebase.database.core.persistence.PersistenceStorageEngine;
import com.google.firebase.database.core.persistence.PruneForest;
import com.google.firebase.database.core.persistence.TrackedQuery;
import com.google.firebase.database.core.utilities.ImmutableTree;
import com.google.firebase.database.core.utilities.NodeSizeEstimator;
import com.google.firebase.database.core.utilities.Pair;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.QuerySpec;
import com.google.firebase.database.logging.LogWrapper;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.ChildrenNode;
import com.google.firebase.database.snapshot.EmptyNode;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.NodeUtilities;
import com.google.firebase.database.util.JsonMapper;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class SqlPersistenceStorageEngine
implements PersistenceStorageEngine {
    private static final int CHILDREN_NODE_SPLIT_SIZE_THRESHOLD = 16384;
    private static final String CREATE_SERVER_CACHE = "CREATE TABLE serverCache (path TEXT PRIMARY KEY, value BLOB);";
    private static final String CREATE_TRACKED_KEYS = "CREATE TABLE trackedKeys (id INTEGER, key TEXT);";
    private static final String CREATE_TRACKED_QUERIES = "CREATE TABLE trackedQueries (id INTEGER PRIMARY KEY, path TEXT, queryParams TEXT, lastUse INTEGER, complete INTEGER, active INTEGER);";
    private static final String CREATE_WRITES = "CREATE TABLE writes (id INTEGER, path TEXT, type TEXT, part INTEGER, node BLOB, UNIQUE (id, part));";
    private static final String FIRST_PART_KEY = ".part-0000";
    private static final String LOGGER_COMPONENT = "Persistence";
    private static final String PART_KEY_FORMAT = ".part-%04d";
    private static final String PART_KEY_PREFIX = ".part-";
    private static final String PATH_COLUMN_NAME = "path";
    private static final String ROW_ID_COLUMN_NAME = "rowid";
    private static final int ROW_SPLIT_SIZE = 262144;
    private static final String SERVER_CACHE_TABLE = "serverCache";
    private static final String TRACKED_KEYS_ID_COLUMN_NAME = "id";
    private static final String TRACKED_KEYS_KEY_COLUMN_NAME = "key";
    private static final String TRACKED_KEYS_TABLE = "trackedKeys";
    private static final String TRACKED_QUERY_ACTIVE_COLUMN_NAME = "active";
    private static final String TRACKED_QUERY_COMPLETE_COLUMN_NAME = "complete";
    private static final String TRACKED_QUERY_ID_COLUMN_NAME = "id";
    private static final String TRACKED_QUERY_LAST_USE_COLUMN_NAME = "lastUse";
    private static final String TRACKED_QUERY_PARAMS_COLUMN_NAME = "queryParams";
    private static final String TRACKED_QUERY_PATH_COLUMN_NAME = "path";
    private static final String TRACKED_QUERY_TABLE = "trackedQueries";
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    private static final String VALUE_COLUMN_NAME = "value";
    private static final String WRITES_TABLE = "writes";
    private static final String WRITE_ID_COLUMN_NAME = "id";
    private static final String WRITE_NODE_COLUMN_NAME = "node";
    private static final String WRITE_PART_COLUMN_NAME = "part";
    private static final String WRITE_TYPE_COLUMN_NAME = "type";
    private static final String WRITE_TYPE_MERGE = "m";
    private static final String WRITE_TYPE_OVERWRITE = "o";
    private final SQLiteDatabase database;
    private boolean insideTransaction;
    private final LogWrapper logger;
    private long transactionStart = 0L;

    public SqlPersistenceStorageEngine(Context context, com.google.firebase.database.core.Context context2, String string2) {
        try {
            string2 = URLEncoder.encode(string2, "utf-8");
            this.logger = context2.getLogger(LOGGER_COMPONENT);
            this.database = this.openDatabase(context, string2);
            return;
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    private static String buildAncestorWhereClause(Path path, String[] stringArray) {
        int n = stringArray.length;
        int n2 = path.size();
        boolean bl = true;
        if (n < n2 + 1) {
            bl = false;
        }
        Utilities.hardAssert(bl);
        n2 = 0;
        StringBuilder stringBuilder = new StringBuilder("(");
        while (!path.isEmpty()) {
            stringBuilder.append("path");
            stringBuilder.append(" = ? OR ");
            stringArray[n2] = SqlPersistenceStorageEngine.pathToKey(path);
            path = path.getParent();
            ++n2;
        }
        stringBuilder.append("path");
        stringBuilder.append(" = ?)");
        stringArray[n2] = SqlPersistenceStorageEngine.pathToKey(Path.getEmptyPath());
        return stringBuilder.toString();
    }

    private String commaSeparatedList(Collection<Long> object) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl = true;
        object = object.iterator();
        while (object.hasNext()) {
            long l = (Long)object.next();
            if (!bl) {
                stringBuilder.append(",");
            }
            bl = false;
            stringBuilder.append(l);
        }
        return stringBuilder.toString();
    }

    private Node deserializeNode(byte[] object) {
        try {
            Object object2 = new String((byte[])object, UTF8_CHARSET);
            object2 = NodeUtilities.NodeFromJSON(JsonMapper.parseJsonValue((String)object2));
            return object2;
        }
        catch (IOException iOException) {
            object = new String((byte[])object, UTF8_CHARSET);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not deserialize node: ");
            stringBuilder.append((String)object);
            throw new RuntimeException(stringBuilder.toString(), iOException);
        }
    }

    private byte[] joinBytes(List<byte[]> object) {
        int n = 0;
        Object object2 = object.iterator();
        while (object2.hasNext()) {
            n += object2.next().length;
        }
        object2 = new byte[n];
        n = 0;
        Iterator<byte[]> iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            object = iterator2.next();
            System.arraycopy(object, 0, object2, n, ((Object)object).length);
            n += ((Object)object).length;
        }
        return object2;
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Node loadNested(Path path) {
        void var1_4;
        Object object;
        block14: {
            ArrayList<String> arrayList = new ArrayList<String>();
            ArrayList<byte[]> arrayList2 = new ArrayList<byte[]>();
            long l = System.currentTimeMillis();
            object = this.loadNestedQuery(path, new String[]{"path", VALUE_COLUMN_NAME});
            Cursor object22 = object;
            long l2 = System.currentTimeMillis();
            long l3 = System.currentTimeMillis();
            while (true) {
                try {
                    boolean bl = object22.moveToNext();
                    if (!bl) break;
                }
                catch (Throwable throwable) {
                    // empty catch block
                    break block14;
                }
                try {
                    arrayList.add(object22.getString(0));
                    arrayList2.add(object22.getBlob(1));
                }
                catch (Throwable throwable) {
                    break block14;
                }
            }
            object22.close();
            long l4 = System.currentTimeMillis();
            long l5 = System.currentTimeMillis();
            object = EmptyNode.Empty();
            boolean bl = false;
            Object hashMap = new HashMap();
            long l6 = l3;
            for (int i = 0; i < arrayList2.size(); ++i) {
                Path path2;
                Object object2;
                if (((String)arrayList.get(i)).endsWith(FIRST_PART_KEY)) {
                    object2 = (String)arrayList.get(i);
                    path2 = new Path(((String)object2).substring(0, ((String)object2).length() - FIRST_PART_KEY.length()));
                    int n = this.splitNodeRunLength(path2, arrayList, i);
                    if (this.logger.logsDebug()) {
                        LogWrapper logWrapper = this.logger;
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append("Loading split node with ");
                        ((StringBuilder)object2).append(n);
                        ((StringBuilder)object2).append(" parts.");
                        logWrapper.debug(((StringBuilder)object2).toString(), new Object[0]);
                    }
                    object2 = this.deserializeNode(this.joinBytes(arrayList2.subList(i, i + n)));
                    i = i + n - 1;
                } else {
                    object2 = this.deserializeNode((byte[])arrayList2.get(i));
                    path2 = new Path((String)arrayList.get(i));
                }
                if (path2.getBack() != null && path2.getBack().isPriorityChildName()) {
                    hashMap.put(path2, object2);
                    continue;
                }
                if (path2.contains(path)) {
                    Utilities.hardAssert(bl ^ true, "Descendants of path must come after ancestors.");
                    object = object2.getChild(Path.getRelative(path2, path));
                    continue;
                }
                if (!path.contains(path2)) {
                    throw new IllegalStateException(String.format("Loading an unrelated row with path %s for %s", path2, path));
                }
                object = object.updateChild(Path.getRelative(path, path2), (Node)object2);
                bl = true;
            }
            hashMap = hashMap.entrySet().iterator();
            while (true) {
                if (!hashMap.hasNext()) {
                    l6 = System.currentTimeMillis();
                    long l7 = System.currentTimeMillis();
                    if (!this.logger.logsDebug()) return object;
                    this.logger.debug(String.format(Locale.US, "Loaded a total of %d rows for a total of %d nodes at %s in %dms (Query: %dms, Loading: %dms, Serializing: %dms)", arrayList2.size(), NodeSizeEstimator.nodeCount((Node)object), path, l7 - l, l2 - l, l4 - l3, l6 - l5), new Object[0]);
                    return object;
                }
                Map.Entry entry = (Map.Entry)hashMap.next();
                object = object.updateChild(Path.getRelative(path, (Path)entry.getKey()), (Node)entry.getValue());
            }
        }
        object.close();
        throw var1_4;
    }

    private Cursor loadNestedQuery(Path path, String[] stringArray) {
        String string2 = SqlPersistenceStorageEngine.pathToKey(path);
        String string3 = SqlPersistenceStorageEngine.pathPrefixStartToPrefixEnd(string2);
        String[] stringArray2 = new String[path.size() + 3];
        String string4 = SqlPersistenceStorageEngine.buildAncestorWhereClause(path, stringArray2);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string4);
        stringBuilder.append(" OR (path > ? AND path < ?)");
        string4 = stringBuilder.toString();
        stringArray2[path.size() + 1] = string2;
        stringArray2[path.size() + 2] = string3;
        return this.database.query(SERVER_CACHE_TABLE, stringArray, string4, stringArray2, null, null, "path");
    }

    private SQLiteDatabase openDatabase(Context object, String string2) {
        object = new PersistentCacheOpenHelper((Context)object, string2);
        try {
            object = object.getWritableDatabase();
            object.rawQuery("PRAGMA locking_mode = EXCLUSIVE", null).close();
            object.beginTransaction();
            object.endTransaction();
            return object;
        }
        catch (SQLiteException sQLiteException) {
            if (sQLiteException instanceof SQLiteDatabaseLockedException) {
                throw new DatabaseException("Failed to gain exclusive lock to Firebase Database's offline persistence. This generally means you are using Firebase Database from multiple processes in your app. Keep in mind that multi-process Android apps execute the code in your Application class in all processes, so you may need to avoid initializing FirebaseDatabase in your Application class. If you are intentionally using Firebase Database from multiple processes, you can only enable offline persistence (i.e. call setPersistenceEnabled(true)) in one of them.", sQLiteException);
            }
            throw sQLiteException;
        }
    }

    private String partKey(Path path, int n) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SqlPersistenceStorageEngine.pathToKey(path));
        stringBuilder.append(String.format(Locale.US, PART_KEY_FORMAT, n));
        return stringBuilder.toString();
    }

    private static String pathPrefixStartToPrefixEnd(String string2) {
        Utilities.hardAssert(string2.endsWith("/"), "Path keys must end with a '/'");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2.substring(0, string2.length() - 1));
        stringBuilder.append('0');
        return stringBuilder.toString();
    }

    private static String pathToKey(Path path) {
        if (path.isEmpty()) {
            return "/";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(path.toString());
        stringBuilder.append("/");
        return stringBuilder.toString();
    }

    private void pruneTreeRecursive(Path path, Path path2, ImmutableTree<Long> object, ImmutableTree<Long> immutableTree, PruneForest pruneForest, List<Pair<Path, Node>> list) {
        if (((ImmutableTree)object).getValue() != null) {
            int n = pruneForest.foldKeptNodes(0, new ImmutableTree.TreeVisitor<Void, Integer>(this, immutableTree){
                final SqlPersistenceStorageEngine this$0;
                final ImmutableTree val$rowIdsToKeep;
                {
                    this.this$0 = sqlPersistenceStorageEngine;
                    this.val$rowIdsToKeep = immutableTree;
                }

                @Override
                public Integer onNodeValue(Path path, Void void_, Integer n) {
                    int n2 = this.val$rowIdsToKeep.get(path) == null ? n + 1 : n;
                    return n2;
                }
            });
            if (n > 0) {
                path = path.child(path2);
                if (this.logger.logsDebug()) {
                    this.logger.debug(String.format(Locale.US, "Need to rewrite %d nodes below path %s", n, path), new Object[0]);
                }
                pruneForest.foldKeptNodes(null, new ImmutableTree.TreeVisitor<Void, Void>(this, immutableTree, list, path2, this.loadNested(path)){
                    final SqlPersistenceStorageEngine this$0;
                    final Node val$currentNode;
                    final Path val$relativePath;
                    final ImmutableTree val$rowIdsToKeep;
                    final List val$rowsToResaveAccumulator;
                    {
                        this.this$0 = sqlPersistenceStorageEngine;
                        this.val$rowIdsToKeep = immutableTree;
                        this.val$rowsToResaveAccumulator = list;
                        this.val$relativePath = path;
                        this.val$currentNode = node;
                    }

                    @Override
                    public Void onNodeValue(Path path, Void void_, Void void_2) {
                        if (this.val$rowIdsToKeep.get(path) == null) {
                            this.val$rowsToResaveAccumulator.add(new Pair<Path, Node>(this.val$relativePath.child(path), this.val$currentNode.getChild(path)));
                        }
                        return null;
                    }
                });
            }
        } else {
            for (Map.Entry entry : ((ImmutableTree)object).getChildren()) {
                object = entry.getKey();
                PruneForest pruneForest2 = pruneForest.child(entry.getKey());
                this.pruneTreeRecursive(path, path2.child((ChildKey)object), entry.getValue(), immutableTree.getChild((ChildKey)object), pruneForest2, list);
            }
        }
    }

    private int removeNested(String string2, Path object) {
        String string3 = SqlPersistenceStorageEngine.pathToKey((Path)object);
        object = SqlPersistenceStorageEngine.pathPrefixStartToPrefixEnd(string3);
        return this.database.delete(string2, "path >= ? AND path < ?", new String[]{string3, object});
    }

    private int saveNested(Path path, Node node) {
        long l = NodeSizeEstimator.estimateSerializedNodeSize(node);
        if (node instanceof ChildrenNode && l > 16384L) {
            if (this.logger.logsDebug()) {
                this.logger.debug(String.format(Locale.US, "Node estimated serialized size at path %s of %d bytes exceeds limit of %d bytes. Splitting up.", path, l, 16384), new Object[0]);
            }
            int n = 0;
            for (NamedNode namedNode : node) {
                n += this.saveNested(path.child(namedNode.getName()), namedNode.getNode());
            }
            int n2 = n;
            if (!node.getPriority().isEmpty()) {
                this.saveNode(path.child(ChildKey.getPriorityKey()), node.getPriority());
                n2 = n + 1;
            }
            this.saveNode(path, EmptyNode.Empty());
            return n2 + 1;
        }
        this.saveNode(path, node);
        return 1;
    }

    private void saveNode(Path path, Node iterable) {
        Object object = this.serializeObject(iterable.getValue(true));
        if (((byte[])object).length >= 262144) {
            iterable = SqlPersistenceStorageEngine.splitBytes(object, 262144);
            if (this.logger.logsDebug()) {
                object = this.logger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Saving huge leaf node with ");
                stringBuilder.append(iterable.size());
                stringBuilder.append(" parts.");
                object.debug(stringBuilder.toString(), new Object[0]);
            }
            for (int i = 0; i < iterable.size(); ++i) {
                object = new ContentValues();
                object.put("path", this.partKey(path, i));
                object.put(VALUE_COLUMN_NAME, (byte[])iterable.get(i));
                this.database.insertWithOnConflict(SERVER_CACHE_TABLE, null, (ContentValues)object, 5);
            }
        } else {
            iterable = new ContentValues();
            iterable.put("path", SqlPersistenceStorageEngine.pathToKey(path));
            iterable.put(VALUE_COLUMN_NAME, (byte[])object);
            this.database.insertWithOnConflict(SERVER_CACHE_TABLE, null, iterable, 5);
        }
    }

    private void saveWrite(Path path, long l, String string2, byte[] object) {
        this.verifyInsideTransaction();
        this.database.delete(WRITES_TABLE, "id = ?", new String[]{String.valueOf(l)});
        if (((byte[])object).length >= 262144) {
            List<byte[]> list = SqlPersistenceStorageEngine.splitBytes(object, 262144);
            for (int i = 0; i < list.size(); ++i) {
                object = new ContentValues();
                object.put("id", Long.valueOf(l));
                object.put("path", SqlPersistenceStorageEngine.pathToKey(path));
                object.put(WRITE_TYPE_COLUMN_NAME, string2);
                object.put(WRITE_PART_COLUMN_NAME, Integer.valueOf(i));
                object.put(WRITE_NODE_COLUMN_NAME, list.get(i));
                this.database.insertWithOnConflict(WRITES_TABLE, null, (ContentValues)object, 5);
            }
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", Long.valueOf(l));
            contentValues.put("path", SqlPersistenceStorageEngine.pathToKey(path));
            contentValues.put(WRITE_TYPE_COLUMN_NAME, string2);
            contentValues.put(WRITE_PART_COLUMN_NAME, (Integer)null);
            contentValues.put(WRITE_NODE_COLUMN_NAME, object);
            this.database.insertWithOnConflict(WRITES_TABLE, null, contentValues, 5);
        }
    }

    private byte[] serializeObject(Object object) {
        try {
            object = JsonMapper.serializeJsonValue(object).getBytes(UTF8_CHARSET);
            return object;
        }
        catch (IOException iOException) {
            throw new RuntimeException("Could not serialize leaf node", iOException);
        }
    }

    private static List<byte[]> splitBytes(byte[] byArray, int n) {
        int n2 = (byArray.length - 1) / n + 1;
        ArrayList<byte[]> arrayList = new ArrayList<byte[]>(n2);
        for (int i = 0; i < n2; ++i) {
            int n3 = Math.min(n, byArray.length - i * n);
            byte[] byArray2 = new byte[n3];
            System.arraycopy(byArray, i * n, byArray2, 0, n3);
            arrayList.add(byArray2);
        }
        return arrayList;
    }

    private int splitNodeRunLength(Path object, List<String> object2, int n) {
        String string2 = SqlPersistenceStorageEngine.pathToKey((Path)object);
        if (object2.get(n).startsWith(string2)) {
            int n2;
            for (n2 = n + 1; n2 < object2.size() && object2.get(n2).equals(this.partKey((Path)object, n2 - n)); ++n2) {
            }
            if (n2 < object2.size()) {
                object2 = object2.get(n2);
                object = new StringBuilder();
                ((StringBuilder)object).append(string2);
                ((StringBuilder)object).append(PART_KEY_PREFIX);
                if (((String)object2).startsWith(((StringBuilder)object).toString())) {
                    throw new IllegalStateException("Run did not finish with all parts");
                }
            }
            return n2 - n;
        }
        object = new IllegalStateException("Extracting split nodes needs to start with path prefix");
        throw object;
    }

    private void updateServerCache(Path path, Node object, boolean bl) {
        int n;
        int n2;
        long l = System.currentTimeMillis();
        if (!bl) {
            n2 = this.removeNested(SERVER_CACHE_TABLE, path);
            n = this.saveNested(path, (Node)object);
        } else {
            n2 = 0;
            n = 0;
            object = object.iterator();
            while (object.hasNext()) {
                NamedNode namedNode = (NamedNode)object.next();
                n2 += this.removeNested(SERVER_CACHE_TABLE, path.child(namedNode.getName()));
                n += this.saveNested(path.child(namedNode.getName()), namedNode.getNode());
            }
        }
        long l2 = System.currentTimeMillis();
        if (this.logger.logsDebug()) {
            this.logger.debug(String.format(Locale.US, "Persisted a total of %d rows and deleted %d rows for a set at %s in %dms", n, n2, path.toString(), l2 - l), new Object[0]);
        }
    }

    private void verifyInsideTransaction() {
        Utilities.hardAssert(this.insideTransaction, "Transaction expected to already be in progress.");
    }

    @Override
    public void beginTransaction() {
        Utilities.hardAssert(this.insideTransaction ^ true, "runInTransaction called when an existing transaction is already in progress.");
        if (this.logger.logsDebug()) {
            this.logger.debug("Starting transaction.", new Object[0]);
        }
        this.database.beginTransaction();
        this.insideTransaction = true;
        this.transactionStart = System.currentTimeMillis();
    }

    @Override
    public void close() {
        this.database.close();
    }

    @Override
    public void deleteTrackedQuery(long l) {
        this.verifyInsideTransaction();
        String string2 = String.valueOf(l);
        this.database.delete(TRACKED_QUERY_TABLE, "id = ?", new String[]{string2});
        this.database.delete(TRACKED_KEYS_TABLE, "id = ?", new String[]{string2});
    }

    @Override
    public void endTransaction() {
        this.database.endTransaction();
        this.insideTransaction = false;
        long l = System.currentTimeMillis();
        long l2 = this.transactionStart;
        if (this.logger.logsDebug()) {
            this.logger.debug(String.format(Locale.US, "Transaction completed. Elapsed: %dms", l - l2), new Object[0]);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public List<TrackedQuery> loadTrackedQueries() {
        long l = System.currentTimeMillis();
        Cursor cursor = this.database.query(TRACKED_QUERY_TABLE, new String[]{"id", "path", TRACKED_QUERY_PARAMS_COLUMN_NAME, TRACKED_QUERY_LAST_USE_COLUMN_NAME, TRACKED_QUERY_COMPLETE_COLUMN_NAME, TRACKED_QUERY_ACTIVE_COLUMN_NAME}, null, null, null, null, "id");
        ArrayList<TrackedQuery> arrayList = new ArrayList<TrackedQuery>();
        try {
            long l2;
            while (cursor.moveToNext()) {
                l2 = cursor.getLong(0);
                Object object = new Path(cursor.getString(1));
                Object object2 = cursor.getString(2);
                try {
                    object2 = JsonMapper.parseJson((String)object2);
                }
                catch (IOException iOException) {
                    object = new RuntimeException(iOException);
                    throw object;
                }
                object2 = QuerySpec.fromPathAndQueryObject((Path)object, (Map<String, Object>)object2);
                long l3 = cursor.getLong(3);
                boolean bl = cursor.getInt(4) != 0;
                boolean bl2 = cursor.getInt(5) != 0;
                object = new TrackedQuery(l2, (QuerySpec)object2, l3, bl, bl2);
                arrayList.add((TrackedQuery)object);
            }
            l2 = System.currentTimeMillis();
            if (!this.logger.logsDebug()) return arrayList;
            this.logger.debug(String.format(Locale.US, "Loaded %d tracked queries in %dms", arrayList.size(), l2 - l), new Object[0]);
            return arrayList;
        }
        finally {
            cursor.close();
        }
    }

    @Override
    public Set<ChildKey> loadTrackedQueryKeys(long l) {
        return this.loadTrackedQueryKeys(Collections.singleton(l));
    }

    @Override
    public Set<ChildKey> loadTrackedQueryKeys(Set<Long> set) {
        HashSet<ChildKey> hashSet;
        CharSequence charSequence;
        block3: {
            long l = System.currentTimeMillis();
            charSequence = new StringBuilder();
            charSequence.append("id IN (");
            charSequence.append(this.commaSeparatedList(set));
            charSequence.append(")");
            charSequence = charSequence.toString();
            charSequence = this.database.query(true, TRACKED_KEYS_TABLE, new String[]{TRACKED_KEYS_KEY_COLUMN_NAME}, (String)charSequence, null, null, null, null, null);
            hashSet = new HashSet<ChildKey>();
            try {
                while (charSequence.moveToNext()) {
                    hashSet.add(ChildKey.fromString(charSequence.getString(0)));
                }
                long l2 = System.currentTimeMillis();
                if (!this.logger.logsDebug()) break block3;
                this.logger.debug(String.format(Locale.US, "Loaded %d tracked queries keys for tracked queries %s in %dms", hashSet.size(), set.toString(), l2 - l), new Object[0]);
            }
            catch (Throwable throwable) {
                charSequence.close();
                throw throwable;
            }
        }
        charSequence.close();
        return hashSet;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public List<UserWriteRecord> loadUserWrites() {
        Throwable throwable2222222;
        Cursor cursor;
        block12: {
            long l;
            long l2 = System.currentTimeMillis();
            cursor = this.database.query(WRITES_TABLE, new String[]{"id", "path", WRITE_TYPE_COLUMN_NAME, WRITE_PART_COLUMN_NAME, WRITE_NODE_COLUMN_NAME}, null, null, null, null, "id, part");
            Serializable serializable = new ArrayList();
            while (cursor.moveToNext()) {
                Object object;
                l = cursor.getLong(0);
                Path path = new Path(cursor.getString(1));
                Object object2 = cursor.getString(2);
                if (cursor.isNull(3)) {
                    object = cursor.getBlob(4);
                } else {
                    object = new ArrayList();
                    do {
                        object.add(cursor.getBlob(4));
                    } while (cursor.moveToNext() && cursor.getLong(0) == l);
                    cursor.moveToPrevious();
                    object = this.joinBytes((List<byte[]>)object);
                }
                String string2 = new String((byte[])object, UTF8_CHARSET);
                object = JsonMapper.parseJsonValue(string2);
                if (WRITE_TYPE_OVERWRITE.equals(object2)) {
                    object2 = NodeUtilities.NodeFromJSON(object);
                    object = new UserWriteRecord(l, path, (Node)object2, true);
                } else {
                    if (!WRITE_TYPE_MERGE.equals(object2)) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Got invalid write type: ");
                        ((StringBuilder)object).append((String)object2);
                        serializable = new IllegalStateException(((StringBuilder)object).toString());
                        throw serializable;
                    }
                    object = new UserWriteRecord(l, path, CompoundWrite.fromValue((Map)object));
                }
                serializable.add(object);
            }
            l = System.currentTimeMillis();
            if (this.logger.logsDebug()) {
                this.logger.debug(String.format(Locale.US, "Loaded %d writes in %dms", serializable.size(), l - l2), new Object[0]);
            }
            {
                catch (Throwable throwable2222222) {
                    break block12;
                }
                catch (IOException iOException) {}
                {
                    serializable = new RuntimeException("Failed to load writes", iOException);
                    throw serializable;
                }
            }
            cursor.close();
            return serializable;
        }
        cursor.close();
        throw throwable2222222;
    }

    @Override
    public void mergeIntoServerCache(Path path, CompoundWrite object) {
        this.verifyInsideTransaction();
        long l = System.currentTimeMillis();
        int n = 0;
        int n2 = 0;
        object = ((CompoundWrite)object).iterator();
        while (object.hasNext()) {
            Map.Entry entry = (Map.Entry)object.next();
            n2 += this.removeNested(SERVER_CACHE_TABLE, path.child((Path)entry.getKey()));
            n += this.saveNested(path.child((Path)entry.getKey()), (Node)entry.getValue());
        }
        long l2 = System.currentTimeMillis();
        if (this.logger.logsDebug()) {
            this.logger.debug(String.format(Locale.US, "Persisted a total of %d rows and deleted %d rows for a merge at %s in %dms", n, n2, path.toString(), l2 - l), new Object[0]);
        }
    }

    @Override
    public void mergeIntoServerCache(Path path, Node node) {
        this.verifyInsideTransaction();
        this.updateServerCache(path, node, true);
    }

    @Override
    public void overwriteServerCache(Path path, Node node) {
        this.verifyInsideTransaction();
        this.updateServerCache(path, node, false);
    }

    @Override
    public void pruneCache(Path path, PruneForest object) {
        long l;
        if (!((PruneForest)object).prunesAnything()) {
            return;
        }
        this.verifyInsideTransaction();
        long l2 = System.currentTimeMillis();
        Object object2 = this.loadNestedQuery(path, new String[]{ROW_ID_COLUMN_NAME, "path"});
        Iterator iterator2 = new ImmutableTree<Object>(null);
        Object object3 = new ImmutableTree<Object>(null);
        while (object2.moveToNext()) {
            StringBuilder stringBuilder;
            Object object4;
            l = object2.getLong(0);
            Path path2 = new Path(object2.getString(1));
            if (!path.contains(path2)) {
                object4 = this.logger;
                stringBuilder = new StringBuilder();
                stringBuilder.append("We are pruning at ");
                stringBuilder.append(path);
                stringBuilder.append(" but we have data stored higher up at ");
                stringBuilder.append(path2);
                stringBuilder.append(". Ignoring.");
                ((LogWrapper)object4).warn(stringBuilder.toString());
                continue;
            }
            object4 = Path.getRelative(path, path2);
            if (((PruneForest)object).shouldPruneUnkeptDescendants((Path)object4)) {
                iterator2 = ((ImmutableTree)((Object)iterator2)).set((Path)object4, l);
                continue;
            }
            if (((PruneForest)object).shouldKeep((Path)object4)) {
                object3 = ((ImmutableTree)object3).set((Path)object4, l);
                continue;
            }
            object4 = this.logger;
            stringBuilder = new StringBuilder();
            stringBuilder.append("We are pruning at ");
            stringBuilder.append(path);
            stringBuilder.append(" and have data at ");
            stringBuilder.append(path2);
            stringBuilder.append(" that isn't marked for pruning or keeping. Ignoring.");
            ((LogWrapper)object4).warn(stringBuilder.toString());
        }
        int n = 0;
        int n2 = 0;
        if (!((ImmutableTree)((Object)iterator2)).isEmpty()) {
            object2 = new ArrayList();
            this.pruneTreeRecursive(path, Path.getEmptyPath(), (ImmutableTree<Long>)((Object)iterator2), (ImmutableTree<Long>)object3, (PruneForest)object, (List<Pair<Path, Node>>)object2);
            object = ((ImmutableTree)((Object)iterator2)).values();
            iterator2 = new StringBuilder();
            ((StringBuilder)((Object)iterator2)).append("rowid IN (");
            ((StringBuilder)((Object)iterator2)).append(this.commaSeparatedList((Collection<Long>)object));
            ((StringBuilder)((Object)iterator2)).append(")");
            iterator2 = ((StringBuilder)((Object)iterator2)).toString();
            this.database.delete(SERVER_CACHE_TABLE, (String)((Object)iterator2), null);
            iterator2 = object2.iterator();
            while (iterator2.hasNext()) {
                object3 = (Pair)iterator2.next();
                this.saveNested(path.child((Path)((Pair)object3).getFirst()), (Node)((Pair)object3).getSecond());
            }
            n = object.size();
            n2 = object2.size();
        }
        l = System.currentTimeMillis();
        if (this.logger.logsDebug()) {
            this.logger.debug(String.format(Locale.US, "Pruned %d rows with %d nodes resaved in %dms", n, n2, l - l2), new Object[0]);
        }
    }

    public void purgeCache() {
        this.verifyInsideTransaction();
        this.database.delete(SERVER_CACHE_TABLE, null, null);
        this.database.delete(WRITES_TABLE, null, null);
        this.database.delete(TRACKED_QUERY_TABLE, null, null);
        this.database.delete(TRACKED_KEYS_TABLE, null, null);
    }

    @Override
    public void removeAllUserWrites() {
        this.verifyInsideTransaction();
        long l = System.currentTimeMillis();
        int n = this.database.delete(WRITES_TABLE, null, null);
        long l2 = System.currentTimeMillis();
        if (this.logger.logsDebug()) {
            this.logger.debug(String.format(Locale.US, "Deleted %d (all) write(s) in %dms", n, l2 - l), new Object[0]);
        }
    }

    @Override
    public void removeUserWrite(long l) {
        this.verifyInsideTransaction();
        long l2 = System.currentTimeMillis();
        int n = this.database.delete(WRITES_TABLE, "id = ?", new String[]{String.valueOf(l)});
        long l3 = System.currentTimeMillis();
        if (this.logger.logsDebug()) {
            this.logger.debug(String.format(Locale.US, "Deleted %d write(s) with writeId %d in %dms", n, l, l3 - l2), new Object[0]);
        }
    }

    @Override
    public void resetPreviouslyActiveTrackedQueries(long l) {
        this.verifyInsideTransaction();
        long l2 = System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRACKED_QUERY_ACTIVE_COLUMN_NAME, Boolean.valueOf(false));
        contentValues.put(TRACKED_QUERY_LAST_USE_COLUMN_NAME, Long.valueOf(l));
        this.database.updateWithOnConflict(TRACKED_QUERY_TABLE, contentValues, "active = 1", new String[0], 5);
        l = System.currentTimeMillis();
        if (this.logger.logsDebug()) {
            this.logger.debug(String.format(Locale.US, "Reset active tracked queries in %dms", l - l2), new Object[0]);
        }
    }

    @Override
    public void saveTrackedQuery(TrackedQuery trackedQuery) {
        this.verifyInsideTransaction();
        long l = System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", Long.valueOf(trackedQuery.id));
        contentValues.put("path", SqlPersistenceStorageEngine.pathToKey(trackedQuery.querySpec.getPath()));
        contentValues.put(TRACKED_QUERY_PARAMS_COLUMN_NAME, trackedQuery.querySpec.getParams().toJSON());
        contentValues.put(TRACKED_QUERY_LAST_USE_COLUMN_NAME, Long.valueOf(trackedQuery.lastUse));
        contentValues.put(TRACKED_QUERY_COMPLETE_COLUMN_NAME, Boolean.valueOf(trackedQuery.complete));
        contentValues.put(TRACKED_QUERY_ACTIVE_COLUMN_NAME, Boolean.valueOf(trackedQuery.active));
        this.database.insertWithOnConflict(TRACKED_QUERY_TABLE, null, contentValues, 5);
        long l2 = System.currentTimeMillis();
        if (this.logger.logsDebug()) {
            this.logger.debug(String.format(Locale.US, "Saved new tracked query in %dms", l2 - l), new Object[0]);
        }
    }

    @Override
    public void saveTrackedQueryKeys(long l, Set<ChildKey> set) {
        this.verifyInsideTransaction();
        long l2 = System.currentTimeMillis();
        this.database.delete(TRACKED_KEYS_TABLE, "id = ?", new String[]{String.valueOf(l)});
        for (ChildKey childKey : set) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", Long.valueOf(l));
            contentValues.put(TRACKED_KEYS_KEY_COLUMN_NAME, childKey.asString());
            this.database.insertWithOnConflict(TRACKED_KEYS_TABLE, null, contentValues, 5);
        }
        long l3 = System.currentTimeMillis();
        if (this.logger.logsDebug()) {
            this.logger.debug(String.format(Locale.US, "Set %d tracked query keys for tracked query %d in %dms", set.size(), l, l3 - l2), new Object[0]);
        }
    }

    @Override
    public void saveUserMerge(Path path, CompoundWrite compoundWrite, long l) {
        this.verifyInsideTransaction();
        long l2 = System.currentTimeMillis();
        this.saveWrite(path, l, WRITE_TYPE_MERGE, this.serializeObject(compoundWrite.getValue(true)));
        l = System.currentTimeMillis();
        if (this.logger.logsDebug()) {
            this.logger.debug(String.format(Locale.US, "Persisted user merge in %dms", l - l2), new Object[0]);
        }
    }

    @Override
    public void saveUserOverwrite(Path path, Node node, long l) {
        this.verifyInsideTransaction();
        long l2 = System.currentTimeMillis();
        this.saveWrite(path, l, WRITE_TYPE_OVERWRITE, this.serializeObject(node.getValue(true)));
        l = System.currentTimeMillis();
        if (this.logger.logsDebug()) {
            this.logger.debug(String.format(Locale.US, "Persisted user overwrite in %dms", l - l2), new Object[0]);
        }
    }

    @Override
    public Node serverCache(Path path) {
        return this.loadNested(path);
    }

    @Override
    public long serverCacheEstimatedSizeInBytes() {
        String string2 = String.format("SELECT sum(length(%s) + length(%s)) FROM %s", VALUE_COLUMN_NAME, "path", SERVER_CACHE_TABLE);
        string2 = this.database.rawQuery(string2, null);
        try {
            if (string2.moveToFirst()) {
                long l = string2.getLong(0);
                return l;
            }
            IllegalStateException illegalStateException = new IllegalStateException("Couldn't read database result!");
            throw illegalStateException;
        }
        finally {
            string2.close();
        }
    }

    @Override
    public void setTransactionSuccessful() {
        this.database.setTransactionSuccessful();
    }

    @Override
    public void updateTrackedQueryKeys(long l, Set<ChildKey> set, Set<ChildKey> set2) {
        this.verifyInsideTransaction();
        long l2 = System.currentTimeMillis();
        for (ChildKey object : set2) {
            this.database.delete(TRACKED_KEYS_TABLE, "id = ? AND key = ?", new String[]{String.valueOf(l), object.asString()});
        }
        for (ChildKey childKey : set) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", Long.valueOf(l));
            contentValues.put(TRACKED_KEYS_KEY_COLUMN_NAME, childKey.asString());
            this.database.insertWithOnConflict(TRACKED_KEYS_TABLE, null, contentValues, 5);
        }
        long l3 = System.currentTimeMillis();
        if (this.logger.logsDebug()) {
            this.logger.debug(String.format(Locale.US, "Updated tracked query keys (%d added, %d removed) for tracked query id %d in %dms", set.size(), set2.size(), l, l3 - l2), new Object[0]);
        }
    }

    private static class PersistentCacheOpenHelper
    extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 2;

        public PersistentCacheOpenHelper(Context context, String string2) {
            super(context, string2, null, 2);
        }

        private void dropTable(SQLiteDatabase sQLiteDatabase, String string2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DROP TABLE IF EXISTS ");
            stringBuilder.append(string2);
            sQLiteDatabase.execSQL(stringBuilder.toString());
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL(SqlPersistenceStorageEngine.CREATE_SERVER_CACHE);
            sQLiteDatabase.execSQL(SqlPersistenceStorageEngine.CREATE_WRITES);
            sQLiteDatabase.execSQL(SqlPersistenceStorageEngine.CREATE_TRACKED_QUERIES);
            sQLiteDatabase.execSQL(SqlPersistenceStorageEngine.CREATE_TRACKED_KEYS);
        }

        public void onUpgrade(SQLiteDatabase object, int n, int n2) {
            boolean bl = n2 == 2;
            Utilities.hardAssert(bl, "Why is onUpgrade() called with a different version?");
            if (n <= 1) {
                this.dropTable((SQLiteDatabase)object, SqlPersistenceStorageEngine.SERVER_CACHE_TABLE);
                object.execSQL(SqlPersistenceStorageEngine.CREATE_SERVER_CACHE);
                this.dropTable((SQLiteDatabase)object, SqlPersistenceStorageEngine.TRACKED_QUERY_COMPLETE_COLUMN_NAME);
                object.execSQL(SqlPersistenceStorageEngine.CREATE_TRACKED_KEYS);
                object.execSQL(SqlPersistenceStorageEngine.CREATE_TRACKED_QUERIES);
                return;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("We don't handle upgrading to ");
            ((StringBuilder)object).append(n2);
            throw new AssertionError((Object)((StringBuilder)object).toString());
        }
    }
}

