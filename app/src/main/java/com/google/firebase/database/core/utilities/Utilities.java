/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 *  android.util.Base64
 *  android.util.Log
 */
package com.google.firebase.database.core.utilities;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.RepoInfo;
import com.google.firebase.database.core.utilities.Pair;
import com.google.firebase.database.core.utilities.ParsedUrl;
import com.google.firebase.database.core.utilities.Validation;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Map;

public class Utilities {
    private static final char[] HEX_CHARACTERS = "0123456789abcdef".toCharArray();

    public static <C> C castOrNull(Object object, Class<C> clazz) {
        if (clazz.isAssignableFrom(object.getClass())) {
            return (C)object;
        }
        return null;
    }

    public static int compareInts(int n, int n2) {
        if (n < n2) {
            return -1;
        }
        if (n == n2) {
            return 0;
        }
        return 1;
    }

    public static int compareLongs(long l, long l2) {
        if (l < l2) {
            return -1;
        }
        if (l == l2) {
            return 0;
        }
        return 1;
    }

    public static String doubleToHashString(double d) {
        StringBuilder stringBuilder = new StringBuilder(16);
        long l = Double.doubleToLongBits(d);
        for (int i = 7; i >= 0; --i) {
            int n = (int)(l >>> i * 8 & 0xFFL);
            char[] cArray = HEX_CHARACTERS;
            stringBuilder.append(cArray[n >> 4 & 0xF]);
            stringBuilder.append(cArray[n & 0xF]);
        }
        return stringBuilder.toString();
    }

    public static boolean equals(Object object, Object object2) {
        if (object == object2) {
            return true;
        }
        if (object != null && object2 != null) {
            return object.equals(object2);
        }
        return false;
    }

    private static String extractPathString(String string2) {
        int n = string2.indexOf("//");
        if (n != -1) {
            int n2 = (string2 = string2.substring(n + 2)).indexOf("/");
            if (n2 != -1) {
                n = string2.indexOf("?");
                if (n != -1) {
                    return string2.substring(n2 + 1, n);
                }
                return string2.substring(n2 + 1);
            }
            return "";
        }
        throw new DatabaseException("Firebase Database URL is missing URL scheme");
    }

    public static <C> C getOrNull(Object object, String string2, Class<C> clazz) {
        if (object == null) {
            return null;
        }
        if ((object = Utilities.castOrNull(object, Map.class).get(string2)) != null) {
            return Utilities.castOrNull(object, clazz);
        }
        return null;
    }

    public static void hardAssert(boolean bl) {
        Utilities.hardAssert(bl, "");
    }

    public static void hardAssert(boolean bl, String string2) {
        if (!bl) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Assertion failed: ");
            stringBuilder.append(string2);
            Log.w((String)"FirebaseDatabase", (String)stringBuilder.toString());
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ParsedUrl parseUrl(String string2) throws DatabaseException {
        try {
            Path path;
            Uri uri = Uri.parse((String)string2);
            String string3 = uri.getScheme();
            if (string3 == null) {
                IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Database URL does not specify a URL scheme");
                throw illegalArgumentException;
            }
            CharSequence charSequence = uri.getHost();
            if (charSequence == null) {
                IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Database URL does not specify a valid host");
                throw illegalArgumentException;
            }
            Object object = uri.getQueryParameter("ns");
            boolean bl = false;
            Object object2 = object;
            if (object == null) {
                object2 = ((String)charSequence).split("\\.", -1)[0].toLowerCase(Locale.US);
            }
            object = new RepoInfo();
            ((RepoInfo)object).host = ((String)charSequence).toLowerCase(Locale.US);
            int n = uri.getPort();
            if (n != -1) {
                if (string3.equals("https") || string3.equals("wss")) {
                    bl = true;
                }
                ((RepoInfo)object).secure = bl;
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append(((RepoInfo)object).host);
                ((StringBuilder)charSequence).append(":");
                ((StringBuilder)charSequence).append(n);
                ((RepoInfo)object).host = ((StringBuilder)charSequence).toString();
            } else {
                ((RepoInfo)object).secure = true;
            }
            ((RepoInfo)object).internalHost = ((RepoInfo)object).host;
            ((RepoInfo)object).namespace = object2;
            charSequence = Utilities.extractPathString(string2).replace("+", " ");
            Validation.validateRootPathString((String)charSequence);
            object2 = new ParsedUrl();
            ((ParsedUrl)object2).path = path = new Path((String)charSequence);
            ((ParsedUrl)object2).repoInfo = object;
            return object2;
        }
        catch (Exception exception) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid Firebase Database url specified: ");
            stringBuilder.append(string2);
            throw new DatabaseException(stringBuilder.toString(), exception);
        }
    }

    public static String sha1HexDigest(String string2) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(string2.getBytes("UTF-8"));
            string2 = Base64.encodeToString((byte[])messageDigest.digest(), (int)2);
            return string2;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RuntimeException("UTF-8 encoding is required for Firebase Database to run!");
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new RuntimeException("Missing SHA-1 MessageDigest provider.", noSuchAlgorithmException);
        }
    }

    public static String stringHashV2Representation(String charSequence) {
        String string2;
        String string3 = string2 = charSequence;
        if (((String)charSequence).indexOf(92) != -1) {
            string3 = string2.replace("\\", "\\\\");
        }
        string2 = string3;
        if (((String)charSequence).indexOf(34) != -1) {
            string2 = string3.replace("\"", "\\\"");
        }
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append('\"');
        ((StringBuilder)charSequence).append(string2);
        ((StringBuilder)charSequence).append('\"');
        return ((StringBuilder)charSequence).toString();
    }

    public static Integer tryParseInt(String string2) {
        if (string2.length() <= 11 && string2.length() != 0) {
            int n = 0;
            boolean bl = false;
            if (string2.charAt(0) == '-') {
                if (string2.length() == 1) {
                    return null;
                }
                bl = true;
                n = 1;
            }
            long l = 0L;
            while (n < string2.length()) {
                char c = string2.charAt(n);
                if (c >= '0' && c <= '9') {
                    l = 10L * l + (long)(c - 48);
                    ++n;
                    continue;
                }
                return null;
            }
            if (bl) {
                if (-l < Integer.MIN_VALUE) {
                    return null;
                }
                return (int)(-l);
            }
            if (l > Integer.MAX_VALUE) {
                return null;
            }
            return (int)l;
        }
        return null;
    }

    public static Pair<Task<Void>, DatabaseReference.CompletionListener> wrapOnComplete(DatabaseReference.CompletionListener object) {
        if (object == null) {
            object = new TaskCompletionSource();
            DatabaseReference.CompletionListener completionListener = new DatabaseReference.CompletionListener((TaskCompletionSource)object){
                final TaskCompletionSource val$source;
                {
                    this.val$source = taskCompletionSource;
                }

                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        this.val$source.setException(databaseError.toException());
                    } else {
                        this.val$source.setResult(null);
                    }
                }
            };
            return new Pair<Task<Void>, DatabaseReference.CompletionListener>(((TaskCompletionSource)object).getTask(), completionListener);
        }
        return new Pair<Object, Object>(null, object);
    }
}

