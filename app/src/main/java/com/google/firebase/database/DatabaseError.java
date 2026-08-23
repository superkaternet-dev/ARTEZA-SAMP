/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database;

import com.google.firebase.database.DatabaseException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DatabaseError {
    public static final int DATA_STALE = -1;
    public static final int DISCONNECTED = -4;
    public static final int EXPIRED_TOKEN = -6;
    public static final int INVALID_TOKEN = -7;
    public static final int MAX_RETRIES = -8;
    public static final int NETWORK_ERROR = -24;
    public static final int OPERATION_FAILED = -2;
    public static final int OVERRIDDEN_BY_SET = -9;
    public static final int PERMISSION_DENIED = -3;
    public static final int UNAVAILABLE = -10;
    public static final int UNKNOWN_ERROR = -999;
    public static final int USER_CODE_EXCEPTION = -11;
    public static final int WRITE_CANCELED = -25;
    private static final Map<String, Integer> errorCodes;
    private static final Map<Integer, String> errorReasons;
    private final int code;
    private final String details;
    private final String message;

    static {
        HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
        errorReasons = hashMap;
        Integer n = -1;
        hashMap.put(n, "The transaction needs to be run again with current data");
        Integer n2 = -2;
        hashMap.put(n2, "The server indicated that this operation failed");
        Integer n3 = -3;
        hashMap.put(n3, "This client does not have permission to perform this operation");
        Integer n4 = -4;
        hashMap.put(n4, "The operation had to be aborted due to a network disconnect");
        Integer n5 = -6;
        hashMap.put(n5, "The supplied auth token has expired");
        Integer n6 = -7;
        hashMap.put(n6, "The supplied auth token was invalid");
        Integer n7 = -8;
        hashMap.put(n7, "The transaction had too many retries");
        Integer n8 = -9;
        hashMap.put(n8, "The transaction was overridden by a subsequent set");
        Integer n9 = -10;
        hashMap.put(n9, "The service is unavailable");
        hashMap.put(-11, "User code called from the Firebase Database runloop threw an exception:\n");
        Integer n10 = -24;
        hashMap.put(n10, "The operation could not be performed due to a network error");
        Integer n11 = -25;
        hashMap.put(n11, "The write was canceled by the user.");
        hashMap.put(-999, "An unknown error occurred");
        hashMap = new HashMap();
        errorCodes = hashMap;
        hashMap.put((Integer)((Object)"datastale"), (String)((Object)n));
        hashMap.put((Integer)((Object)"failure"), (String)((Object)n2));
        hashMap.put((Integer)((Object)"permission_denied"), (String)((Object)n3));
        hashMap.put((Integer)((Object)"disconnected"), (String)((Object)n4));
        hashMap.put((Integer)((Object)"expired_token"), (String)((Object)n5));
        hashMap.put((Integer)((Object)"invalid_token"), (String)((Object)n6));
        hashMap.put((Integer)((Object)"maxretries"), (String)((Object)n7));
        hashMap.put((Integer)((Object)"overriddenbyset"), (String)((Object)n8));
        hashMap.put((Integer)((Object)"unavailable"), (String)((Object)n9));
        hashMap.put((Integer)((Object)"network_error"), (String)((Object)n10));
        hashMap.put((Integer)((Object)"write_canceled"), (String)((Object)n11));
    }

    private DatabaseError(int n, String string2) {
        this(n, string2, null);
    }

    private DatabaseError(int n, String string2, String string3) {
        this.code = n;
        this.message = string2;
        string2 = string3 == null ? "" : string3;
        this.details = string2;
    }

    public static DatabaseError fromCode(int n) {
        Map<Integer, String> map = errorReasons;
        if (map.containsKey(n)) {
            return new DatabaseError(n, map.get(n), null);
        }
        map = new StringBuilder();
        ((StringBuilder)((Object)map)).append("Invalid Firebase Database error code: ");
        ((StringBuilder)((Object)map)).append(n);
        throw new IllegalArgumentException(((StringBuilder)((Object)map)).toString());
    }

    public static DatabaseError fromException(Throwable serializable) {
        StringWriter stringWriter = new StringWriter();
        ((Throwable)serializable).printStackTrace(new PrintWriter(stringWriter));
        serializable = new StringBuilder();
        ((StringBuilder)serializable).append(errorReasons.get(-11));
        ((StringBuilder)serializable).append(stringWriter.toString());
        return new DatabaseError(-11, ((StringBuilder)serializable).toString());
    }

    public static DatabaseError fromStatus(String string2) {
        return DatabaseError.fromStatus(string2, null);
    }

    public static DatabaseError fromStatus(String string2, String string3) {
        return DatabaseError.fromStatus(string2, string3, null);
    }

    public static DatabaseError fromStatus(String object, String string2, String string3) {
        Integer n = errorCodes.get(((String)object).toLowerCase(Locale.US));
        object = n;
        if (n == null) {
            object = -999;
        }
        if (string2 == null) {
            string2 = errorReasons.get(object);
        }
        return new DatabaseError((Integer)object, string2, string3);
    }

    public int getCode() {
        return this.code;
    }

    public String getDetails() {
        return this.details;
    }

    public String getMessage() {
        return this.message;
    }

    public DatabaseException toException() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Firebase Database error: ");
        stringBuilder.append(this.message);
        return new DatabaseException(stringBuilder.toString());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DatabaseError: ");
        stringBuilder.append(this.message);
        return stringBuilder.toString();
    }
}

