/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources$NotFoundException
 *  android.util.Log
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 */
package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.widget.MotionLayout;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Debug {
    public static void dumpLayoutParams(ViewGroup.LayoutParams object, String string2) {
        Object object2 = new Throwable().getStackTrace()[1];
        Appendable appendable = new StringBuilder();
        ((StringBuilder)appendable).append(".(");
        ((StringBuilder)appendable).append(((StackTraceElement)object2).getFileName());
        ((StringBuilder)appendable).append(":");
        ((StringBuilder)appendable).append(((StackTraceElement)object2).getLineNumber());
        ((StringBuilder)appendable).append(") ");
        ((StringBuilder)appendable).append(string2);
        ((StringBuilder)appendable).append("  ");
        string2 = ((StringBuilder)appendable).toString();
        appendable = System.out;
        object2 = new StringBuilder();
        ((StringBuilder)object2).append(" >>>>>>>>>>>>>>>>>>. dump ");
        ((StringBuilder)object2).append(string2);
        ((StringBuilder)object2).append("  ");
        ((StringBuilder)object2).append(object.getClass().getName());
        ((PrintStream)appendable).println(((StringBuilder)object2).toString());
        appendable = object.getClass().getFields();
        for (int i = 0; i < ((Appendable)appendable).length; ++i) {
            Appendable appendable2 = appendable[i];
            try {
                object2 = ((Field)((Object)appendable2)).get(object);
                String string3 = ((Field)((Object)appendable2)).getName();
                if (!string3.contains("To") || object2.toString().equals("-1")) continue;
                PrintStream printStream = System.out;
                appendable2 = new StringBuilder();
                ((StringBuilder)appendable2).append(string2);
                ((StringBuilder)appendable2).append("       ");
                ((StringBuilder)appendable2).append(string3);
                ((StringBuilder)appendable2).append(" ");
                ((StringBuilder)appendable2).append(object2);
                printStream.println(((StringBuilder)appendable2).toString());
                continue;
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }
        object = System.out;
        appendable = new StringBuilder();
        ((StringBuilder)appendable).append(" <<<<<<<<<<<<<<<<< dump ");
        ((StringBuilder)appendable).append(string2);
        ((PrintStream)object).println(((StringBuilder)appendable).toString());
    }

    public static void dumpLayoutParams(ViewGroup viewGroup, String charSequence) {
        Field[] fieldArray = new Throwable().getStackTrace()[1];
        CharSequence charSequence2 = new StringBuilder();
        charSequence2.append(".(");
        charSequence2.append(fieldArray.getFileName());
        charSequence2.append(":");
        charSequence2.append(fieldArray.getLineNumber());
        charSequence2.append(") ");
        charSequence2.append((String)charSequence);
        charSequence2.append("  ");
        charSequence2 = charSequence2.toString();
        int n = viewGroup.getChildCount();
        Appendable appendable = System.out;
        fieldArray = new StringBuilder();
        fieldArray.append((String)charSequence);
        fieldArray.append(" children ");
        fieldArray.append(n);
        ((PrintStream)appendable).println(fieldArray.toString());
        for (int i = 0; i < n; ++i) {
            appendable = viewGroup.getChildAt(i);
            fieldArray = System.out;
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)charSequence2);
            ((StringBuilder)charSequence).append("     ");
            ((StringBuilder)charSequence).append(Debug.getName((View)appendable));
            fieldArray.println(((StringBuilder)charSequence).toString());
            charSequence = appendable.getLayoutParams();
            fieldArray = charSequence.getClass().getFields();
            for (int j = 0; j < fieldArray.length; ++j) {
                Field field = fieldArray[j];
                try {
                    Object object = field.get(charSequence);
                    if (!field.getName().contains("To") || object.toString().equals("-1")) continue;
                    PrintStream printStream = System.out;
                    appendable = new StringBuilder();
                    ((StringBuilder)appendable).append((String)charSequence2);
                    ((StringBuilder)appendable).append("       ");
                    ((StringBuilder)appendable).append(field.getName());
                    ((StringBuilder)appendable).append(" ");
                    ((StringBuilder)appendable).append(object);
                    printStream.println(((StringBuilder)appendable).toString());
                    continue;
                }
                catch (IllegalAccessException illegalAccessException) {
                    // empty catch block
                }
            }
        }
    }

    public static void dumpPoc(Object object) {
        Serializable serializable = new Throwable().getStackTrace()[1];
        CharSequence charSequence = new StringBuilder();
        charSequence.append(".(");
        charSequence.append(((StackTraceElement)serializable).getFileName());
        charSequence.append(":");
        charSequence.append(((StackTraceElement)serializable).getLineNumber());
        charSequence.append(")");
        charSequence = charSequence.toString();
        serializable = object.getClass();
        Object object2 = System.out;
        Object object3 = new StringBuilder();
        ((StringBuilder)object3).append((String)charSequence);
        ((StringBuilder)object3).append("------------- ");
        ((StringBuilder)object3).append(((Class)serializable).getName());
        ((StringBuilder)object3).append(" --------------------");
        ((PrintStream)object2).println(((StringBuilder)object3).toString());
        object2 = ((Class)serializable).getFields();
        for (int i = 0; i < ((Field[])object2).length; ++i) {
            object3 = object2[i];
            try {
                Object object4 = ((Field)object3).get(object);
                if (!((Field)object3).getName().startsWith("layout_constraint") || object4 instanceof Integer && object4.toString().equals("-1") || object4 instanceof Integer && object4.toString().equals("0") || object4 instanceof Float && object4.toString().equals("1.0") || object4 instanceof Float && object4.toString().equals("0.5")) continue;
                PrintStream printStream = System.out;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append((String)charSequence);
                stringBuilder.append("    ");
                stringBuilder.append(((Field)object3).getName());
                stringBuilder.append(" ");
                stringBuilder.append(object4);
                printStream.println(stringBuilder.toString());
                continue;
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }
        object2 = System.out;
        object = new StringBuilder();
        ((StringBuilder)object).append((String)charSequence);
        ((StringBuilder)object).append("------------- ");
        ((StringBuilder)object).append(((Class)serializable).getSimpleName());
        ((StringBuilder)object).append(" --------------------");
        ((PrintStream)object2).println(((StringBuilder)object).toString());
    }

    public static String getActionType(MotionEvent fieldArray) {
        int n = fieldArray.getAction();
        fieldArray = MotionEvent.class.getFields();
        for (int i = 0; i < fieldArray.length; ++i) {
            Object object = fieldArray[i];
            try {
                if (!Modifier.isStatic(((Field)object).getModifiers()) || !((Field)object).getType().equals(Integer.TYPE) || ((Field)object).getInt(null) != n) continue;
                object = ((Field)object).getName();
                return object;
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }
        return "---";
    }

    public static String getCallFrom(int n) {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[n + 2];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(".(");
        stringBuilder.append(stackTraceElement.getFileName());
        stringBuilder.append(":");
        stringBuilder.append(stackTraceElement.getLineNumber());
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public static String getLoc() {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(".(");
        stringBuilder.append(stackTraceElement.getFileName());
        stringBuilder.append(":");
        stringBuilder.append(stackTraceElement.getLineNumber());
        stringBuilder.append(") ");
        stringBuilder.append(stackTraceElement.getMethodName());
        stringBuilder.append("()");
        return stringBuilder.toString();
    }

    public static String getLocation() {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(".(");
        stringBuilder.append(stackTraceElement.getFileName());
        stringBuilder.append(":");
        stringBuilder.append(stackTraceElement.getLineNumber());
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public static String getLocation2() {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[2];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(".(");
        stringBuilder.append(stackTraceElement.getFileName());
        stringBuilder.append(":");
        stringBuilder.append(stackTraceElement.getLineNumber());
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String getName(Context context, int n) {
        if (n == -1) return "UNKNOWN";
        try {
            return context.getResources().getResourceEntryName(n);
        }
        catch (Exception exception) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("?");
            stringBuilder.append(n);
            return stringBuilder.toString();
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String getName(Context object, int[] nArray) {
        CharSequence charSequence;
        try {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append(nArray.length);
            ((StringBuilder)charSequence).append("[");
            charSequence = ((StringBuilder)charSequence).toString();
            for (int i = 0; i < nArray.length; ++i) {
                CharSequence charSequence2 = new StringBuilder();
                charSequence2.append((String)charSequence);
                charSequence = i == 0 ? "" : " ";
                charSequence2.append((String)charSequence);
                charSequence2 = charSequence2.toString();
                try {
                    charSequence = object.getResources().getResourceEntryName(nArray[i]);
                }
                catch (Resources.NotFoundException notFoundException) {
                    charSequence = new StringBuilder();
                    ((StringBuilder)charSequence).append("? ");
                    ((StringBuilder)charSequence).append(nArray[i]);
                    ((StringBuilder)charSequence).append(" ");
                    charSequence = ((StringBuilder)charSequence).toString();
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append((String)charSequence2);
                stringBuilder.append((String)charSequence);
                charSequence = stringBuilder.toString();
            }
        }
        catch (Exception exception) {
            Log.v((String)"DEBUG", (String)exception.toString());
            return "UNKNOWN";
        }
        {
            object = new StringBuilder();
            ((StringBuilder)object).append((String)charSequence);
            ((StringBuilder)object).append("]");
            return ((StringBuilder)object).toString();
        }
    }

    public static String getName(View object) {
        try {
            object = object.getContext().getResources().getResourceEntryName(object.getId());
            return object;
        }
        catch (Exception exception) {
            return "UNKNOWN";
        }
    }

    public static String getState(MotionLayout motionLayout, int n) {
        if (n == -1) {
            return "UNDEFINED";
        }
        return motionLayout.getContext().getResources().getResourceEntryName(n);
    }

    public static void logStack(String string2, String string3, int n) {
        StackTraceElement[] stackTraceElementArray = new Throwable().getStackTrace();
        String string4 = " ";
        int n2 = Math.min(n, stackTraceElementArray.length - 1);
        for (n = 1; n <= n2; ++n) {
            Object object = stackTraceElementArray[n];
            object = new StringBuilder();
            ((StringBuilder)object).append(".(");
            ((StringBuilder)object).append(stackTraceElementArray[n].getFileName());
            ((StringBuilder)object).append(":");
            ((StringBuilder)object).append(stackTraceElementArray[n].getLineNumber());
            ((StringBuilder)object).append(") ");
            ((StringBuilder)object).append(stackTraceElementArray[n].getMethodName());
            object = ((StringBuilder)object).toString();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string4);
            stringBuilder.append(" ");
            string4 = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(string3);
            stringBuilder.append(string4);
            stringBuilder.append((String)object);
            stringBuilder.append(string4);
            Log.v((String)string2, (String)stringBuilder.toString());
        }
    }

    public static void printStack(String string2, int n) {
        StackTraceElement[] stackTraceElementArray = new Throwable().getStackTrace();
        String string3 = " ";
        int n2 = Math.min(n, stackTraceElementArray.length - 1);
        for (n = 1; n <= n2; ++n) {
            Object object = stackTraceElementArray[n];
            object = new StringBuilder();
            ((StringBuilder)object).append(".(");
            ((StringBuilder)object).append(stackTraceElementArray[n].getFileName());
            ((StringBuilder)object).append(":");
            ((StringBuilder)object).append(stackTraceElementArray[n].getLineNumber());
            ((StringBuilder)object).append(") ");
            object = ((StringBuilder)object).toString();
            Appendable appendable = new StringBuilder();
            ((StringBuilder)appendable).append(string3);
            ((StringBuilder)appendable).append(" ");
            string3 = ((StringBuilder)appendable).toString();
            appendable = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(string3);
            stringBuilder.append((String)object);
            stringBuilder.append(string3);
            ((PrintStream)appendable).println(stringBuilder.toString());
        }
    }
}

