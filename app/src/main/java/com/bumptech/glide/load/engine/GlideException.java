/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.bumptech.glide.load.engine;

import android.util.Log;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GlideException
extends Exception {
    private static final StackTraceElement[] EMPTY_ELEMENTS = new StackTraceElement[0];
    private static final long serialVersionUID = 1L;
    private final List<Throwable> causes;
    private Class<?> dataClass;
    private DataSource dataSource;
    private String detailMessage;
    private Exception exception;
    private Key key;

    public GlideException(String string2) {
        this(string2, Collections.emptyList());
    }

    public GlideException(String string2, Throwable throwable) {
        this(string2, Collections.singletonList(throwable));
    }

    public GlideException(String string2, List<Throwable> list) {
        this.detailMessage = string2;
        this.setStackTrace(EMPTY_ELEMENTS);
        this.causes = list;
    }

    private void addRootCauses(Throwable object, List<Throwable> list) {
        if (object instanceof GlideException) {
            object = ((GlideException)object).getCauses().iterator();
            while (object.hasNext()) {
                this.addRootCauses((Throwable)object.next(), list);
            }
        } else {
            list.add((Throwable)object);
        }
    }

    private static void appendCauses(List<Throwable> list, Appendable appendable) {
        try {
            GlideException.appendCausesWrapped(list, appendable);
            return;
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    private static void appendCausesWrapped(List<Throwable> list, Appendable appendable) throws IOException {
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            appendable.append("Cause (").append(String.valueOf(i + 1)).append(" of ").append(String.valueOf(n)).append("): ");
            Throwable throwable = list.get(i);
            if (throwable instanceof GlideException) {
                ((GlideException)throwable).printStackTrace(appendable);
                continue;
            }
            GlideException.appendExceptionMessage(throwable, appendable);
        }
    }

    private static void appendExceptionMessage(Throwable throwable, Appendable appendable) {
        try {
            appendable.append(throwable.getClass().toString()).append(": ").append(throwable.getMessage()).append('\n');
            return;
        }
        catch (IOException iOException) {
            throw new RuntimeException(throwable);
        }
    }

    private void printStackTrace(Appendable appendable) {
        GlideException.appendExceptionMessage(this, appendable);
        GlideException.appendCauses(this.getCauses(), new IndentedAppendable(appendable));
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    public List<Throwable> getCauses() {
        return this.causes;
    }

    @Override
    public String getMessage() {
        StringBuilder stringBuilder = new StringBuilder(71);
        stringBuilder.append(this.detailMessage);
        Object object = this.dataClass;
        String list2 = "";
        if (object != null) {
            object = new StringBuilder();
            ((StringBuilder)object).append(", ");
            ((StringBuilder)object).append(this.dataClass);
            object = ((StringBuilder)object).toString();
        } else {
            object = "";
        }
        stringBuilder.append((String)object);
        if (this.dataSource != null) {
            object = new StringBuilder();
            ((StringBuilder)object).append(", ");
            ((StringBuilder)object).append((Object)this.dataSource);
            object = ((StringBuilder)object).toString();
        } else {
            object = "";
        }
        stringBuilder.append((String)object);
        object = list2;
        if (this.key != null) {
            object = new StringBuilder();
            ((StringBuilder)object).append(", ");
            ((StringBuilder)object).append(this.key);
            object = ((StringBuilder)object).toString();
        }
        object = stringBuilder.append((String)object);
        List<Throwable> list = this.getRootCauses();
        if (list.isEmpty()) {
            return ((StringBuilder)object).toString();
        }
        if (list.size() == 1) {
            ((StringBuilder)object).append("\nThere was 1 root cause:");
        } else {
            ((StringBuilder)object).append("\nThere were ");
            ((StringBuilder)object).append(list.size());
            ((StringBuilder)object).append(" root causes:");
        }
        for (Throwable throwable : list) {
            ((StringBuilder)object).append('\n');
            ((StringBuilder)object).append(throwable.getClass().getName());
            ((StringBuilder)object).append('(');
            ((StringBuilder)object).append(throwable.getMessage());
            ((StringBuilder)object).append(')');
        }
        ((StringBuilder)object).append("\n call GlideException#logRootCauses(String) for more detail");
        return ((StringBuilder)object).toString();
    }

    public Exception getOrigin() {
        return this.exception;
    }

    public List<Throwable> getRootCauses() {
        ArrayList<Throwable> arrayList = new ArrayList<Throwable>();
        this.addRootCauses(this, arrayList);
        return arrayList;
    }

    public void logRootCauses(String string2) {
        List<Throwable> list = this.getRootCauses();
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Root cause (");
            stringBuilder.append(i + 1);
            stringBuilder.append(" of ");
            stringBuilder.append(n);
            stringBuilder.append(")");
            Log.i((String)string2, (String)stringBuilder.toString(), (Throwable)list.get(i));
        }
    }

    @Override
    public void printStackTrace() {
        this.printStackTrace(System.err);
    }

    @Override
    public void printStackTrace(PrintStream printStream) {
        this.printStackTrace((Appendable)printStream);
    }

    @Override
    public void printStackTrace(PrintWriter printWriter) {
        this.printStackTrace((Appendable)printWriter);
    }

    void setLoggingDetails(Key key, DataSource dataSource) {
        this.setLoggingDetails(key, dataSource, null);
    }

    void setLoggingDetails(Key key, DataSource dataSource, Class<?> clazz) {
        this.key = key;
        this.dataSource = dataSource;
        this.dataClass = clazz;
    }

    public void setOrigin(Exception exception) {
        this.exception = exception;
    }

    private static final class IndentedAppendable
    implements Appendable {
        private static final String EMPTY_SEQUENCE = "";
        private static final String INDENT = "  ";
        private final Appendable appendable;
        private boolean printedNewLine = true;

        IndentedAppendable(Appendable appendable) {
            this.appendable = appendable;
        }

        private CharSequence safeSequence(CharSequence charSequence) {
            if (charSequence == null) {
                return EMPTY_SEQUENCE;
            }
            return charSequence;
        }

        @Override
        public Appendable append(char c) throws IOException {
            boolean bl = this.printedNewLine;
            boolean bl2 = false;
            if (bl) {
                this.printedNewLine = false;
                this.appendable.append(INDENT);
            }
            if (c == '\n') {
                bl2 = true;
            }
            this.printedNewLine = bl2;
            this.appendable.append(c);
            return this;
        }

        @Override
        public Appendable append(CharSequence charSequence) throws IOException {
            charSequence = this.safeSequence(charSequence);
            return this.append(charSequence, 0, charSequence.length());
        }

        @Override
        public Appendable append(CharSequence charSequence, int n, int n2) throws IOException {
            charSequence = this.safeSequence(charSequence);
            boolean bl = this.printedNewLine;
            boolean bl2 = false;
            if (bl) {
                this.printedNewLine = false;
                this.appendable.append(INDENT);
            }
            bl = bl2;
            if (charSequence.length() > 0) {
                bl = bl2;
                if (charSequence.charAt(n2 - 1) == '\n') {
                    bl = true;
                }
            }
            this.printedNewLine = bl;
            this.appendable.append(charSequence, n, n2);
            return this;
        }
    }
}

