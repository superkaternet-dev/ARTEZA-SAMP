/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package androidx.transition;

import android.view.View;
import androidx.transition.Transition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransitionValues {
    final ArrayList<Transition> mTargetedTransitions;
    public final Map<String, Object> values = new HashMap<String, Object>();
    public View view;

    public TransitionValues() {
        this.mTargetedTransitions = new ArrayList();
    }

    public boolean equals(Object object) {
        return object instanceof TransitionValues && this.view == ((TransitionValues)object).view && this.values.equals(((TransitionValues)object).values);
    }

    public int hashCode() {
        return this.view.hashCode() * 31 + this.values.hashCode();
    }

    public String toString() {
        CharSequence charSequence = new StringBuilder();
        charSequence.append("TransitionValues@");
        charSequence.append(Integer.toHexString(this.hashCode()));
        charSequence.append(":\n");
        Object object = charSequence.toString();
        charSequence = new StringBuilder();
        charSequence.append((String)object);
        charSequence.append("    view = ");
        charSequence.append(this.view);
        charSequence.append("\n");
        charSequence = charSequence.toString();
        object = new StringBuilder();
        ((StringBuilder)object).append((String)charSequence);
        ((StringBuilder)object).append("    values:");
        charSequence = ((StringBuilder)object).toString();
        for (String string2 : this.values.keySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)charSequence);
            stringBuilder.append("    ");
            stringBuilder.append(string2);
            stringBuilder.append(": ");
            stringBuilder.append(this.values.get(string2));
            stringBuilder.append("\n");
            charSequence = stringBuilder.toString();
        }
        return charSequence;
    }
}

