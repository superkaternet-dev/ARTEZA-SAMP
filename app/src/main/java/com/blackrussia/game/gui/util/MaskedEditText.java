/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.text.Editable
 *  android.text.SpannableStringBuilder
 *  android.text.TextWatcher
 *  android.text.style.ForegroundColorSpan
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.KeyEvent
 *  android.view.View
 *  android.view.View$OnFocusChangeListener
 *  android.widget.TextView
 *  android.widget.TextView$BufferType
 */
package com.blackrussia.game.gui.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatEditText;
import com.blackrussia.game.R;
import com.blackrussia.game.gui.util.$$Lambda$MaskedEditText$uX_YztYUHcbq6dV_J1fDsTb0DjM;
import com.blackrussia.game.gui.util.Range;
import com.blackrussia.game.gui.util.RawText;

public class MaskedEditText
extends AppCompatEditText
implements TextWatcher {
    public static final String SPACE = " ";
    private String allowedChars;
    private char charRepresentation;
    private String deniedChars;
    private boolean editingAfter;
    private boolean editingBefore;
    private boolean editingOnChanged;
    private View.OnFocusChangeListener focusChangeListener;
    private boolean ignore;
    private boolean initialized;
    private boolean keepHint;
    private int lastValidMaskPosition;
    private String mask;
    private int[] maskToRaw;
    protected int maxRawLength;
    private RawText rawText;
    private int[] rawToMask;
    private int selection;
    private boolean selectionChanged;

    public MaskedEditText(Context context) {
        super(context);
        this.init();
    }

    public MaskedEditText(Context context, AttributeSet object) {
        super(context, (AttributeSet)object);
        this.init();
        context = context.obtainStyledAttributes((AttributeSet)object, R.styleable.MaskedEditText);
        this.mask = context.getString(4);
        this.allowedChars = context.getString(0);
        this.deniedChars = context.getString(2);
        object = context.getString(1);
        this.charRepresentation = object == null ? (char)35 : ((String)object).charAt(0);
        this.keepHint = context.getBoolean(3, false);
        this.cleanUp();
        this.setOnEditorActionListener($$Lambda$MaskedEditText$uX_YztYUHcbq6dV_J1fDsTb0DjM.INSTANCE);
        context.recycle();
    }

    public MaskedEditText(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.init();
    }

    static /* synthetic */ boolean access$402(MaskedEditText maskedEditText, boolean bl) {
        maskedEditText.selectionChanged = bl;
        return bl;
    }

    private Range calculateRange(int n, int n2) {
        Range range = new Range();
        for (int i = n; i <= n2 && i < this.mask.length(); ++i) {
            if (this.maskToRaw[i] == -1) continue;
            if (range.getStart() == -1) {
                range.setStart(this.maskToRaw[i]);
            }
            range.setEnd(this.maskToRaw[i]);
        }
        if (n2 == this.mask.length()) {
            range.setEnd(this.rawText.length());
        }
        if (range.getStart() == range.getEnd() && n < n2 && (n = this.previousValidPosition(range.getStart() - 1)) < range.getStart()) {
            range.setStart(n);
        }
        return range;
    }

    private void cleanUp() {
        this.initialized = false;
        this.generatePositionArrays();
        this.rawText = new RawText();
        this.selection = this.rawToMask[0];
        this.editingBefore = true;
        this.editingOnChanged = true;
        this.editingAfter = true;
        if (this.hasHint() && this.rawText.length() == 0) {
            this.setText(this.makeMaskedTextWithHint());
        } else {
            this.setText(this.makeMaskedText());
        }
        this.editingBefore = false;
        this.editingOnChanged = false;
        this.editingAfter = false;
        this.maxRawLength = this.maskToRaw[this.previousValidPosition(this.mask.length() - 1)] + 1;
        this.lastValidMaskPosition = this.findLastValidMaskPosition();
        this.initialized = true;
        MaskedEditText.super.setOnFocusChangeListener(new View.OnFocusChangeListener(this){
            final MaskedEditText this$0;
            {
                this.this$0 = maskedEditText;
            }

            public void onFocusChange(View object, boolean bl) {
                if (this.this$0.focusChangeListener != null) {
                    this.this$0.focusChangeListener.onFocusChange(object, bl);
                }
                if (this.this$0.hasFocus()) {
                    MaskedEditText.access$402(this.this$0, false);
                    object = this.this$0;
                    object.setSelection(((MaskedEditText)object).lastValidPosition());
                }
            }
        });
    }

    private String clear(String charSequence) {
        int n;
        int n2;
        Object object = this.deniedChars;
        int n3 = 0;
        Object object2 = charSequence;
        if (object != null) {
            object = ((String)object).toCharArray();
            n2 = ((Object)object).length;
            n = 0;
            while (true) {
                object2 = charSequence;
                if (n >= n2) break;
                charSequence = ((String)charSequence).replace(Character.toString((char)object[n]), "");
                ++n;
            }
        }
        if (this.allowedChars == null) {
            return object2;
        }
        charSequence = new StringBuilder(((String)object2).length());
        object2 = ((String)object2).toCharArray();
        n2 = ((Object)object2).length;
        for (n = n3; n < n2; ++n) {
            Object object3 = object2[n];
            if (!this.allowedChars.contains(String.valueOf((char)object3))) continue;
            ((StringBuilder)charSequence).append((char)object3);
        }
        return ((StringBuilder)charSequence).toString();
    }

    private int erasingStart(int n) {
        while (n > 0 && this.maskToRaw[n] == -1) {
            --n;
        }
        return n;
    }

    private int findLastValidMaskPosition() {
        for (int i = this.maskToRaw.length - 1; i >= 0; --i) {
            if (this.maskToRaw[i] == -1) continue;
            return i;
        }
        RuntimeException runtimeException = new RuntimeException("Mask must contain at least one representation char");
        throw runtimeException;
    }

    private int fixSelection(int n) {
        if (n > this.lastValidPosition()) {
            return this.lastValidPosition();
        }
        return this.nextValidPosition(n);
    }

    private void generatePositionArrays() {
        CharSequence charSequence;
        int n;
        int[] nArray = new int[this.mask.length()];
        this.maskToRaw = new int[this.mask.length()];
        CharSequence charSequence2 = "";
        int n2 = 0;
        for (n = 0; n < this.mask.length(); ++n) {
            char c = this.mask.charAt(n);
            if (c == this.charRepresentation) {
                nArray[n2] = n;
                this.maskToRaw[n] = n2++;
                continue;
            }
            String string2 = Character.toString(c);
            charSequence = charSequence2;
            if (!charSequence2.contains(string2)) {
                charSequence = charSequence2.concat(string2);
            }
            this.maskToRaw[n] = -1;
            charSequence2 = charSequence;
        }
        charSequence = charSequence2;
        if (charSequence2.indexOf(32) < 0) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)charSequence2);
            ((StringBuilder)charSequence).append(SPACE);
            charSequence = ((StringBuilder)charSequence).toString();
        }
        ((String)charSequence).toCharArray();
        this.rawToMask = new int[n2];
        for (n = 0; n < n2; ++n) {
            this.rawToMask[n] = nArray[n];
        }
    }

    private boolean hasHint() {
        boolean bl = this.getHint() != null;
        return bl;
    }

    private void init() {
        this.addTextChangedListener(this);
    }

    static boolean lambda$new$0(TextView textView, int n, KeyEvent keyEvent) {
        return true;
    }

    private int lastValidPosition() {
        if (this.rawText.length() == this.maxRawLength) {
            return this.rawToMask[this.rawText.length() - 1] + 1;
        }
        return this.nextValidPosition(this.rawToMask[this.rawText.length()]);
    }

    private String makeMaskedText() {
        Object[] objectArray;
        int n = this.rawText.length();
        n = n < (objectArray = this.rawToMask).length ? objectArray[this.rawText.length()] : this.mask.length();
        objectArray = new char[n];
        for (n = 0; n < objectArray.length; ++n) {
            int n2 = this.maskToRaw[n];
            objectArray[n] = n2 == -1 ? (int)this.mask.charAt(n) : (int)this.rawText.charAt(n2);
        }
        return new String((char[])objectArray);
    }

    private CharSequence makeMaskedTextWithHint() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        int n = this.rawToMask[0];
        for (n = 0; n < this.mask.length(); ++n) {
            int n2 = this.maskToRaw[n];
            if (n2 == -1) {
                spannableStringBuilder.append(this.mask.charAt(n));
            } else if (n2 < this.rawText.length()) {
                spannableStringBuilder.append(this.rawText.charAt(n2));
            } else {
                spannableStringBuilder.append(this.getHint().charAt(this.maskToRaw[n]));
            }
            if (this.keepHint) {
                this.rawText.length();
                n2 = this.rawToMask.length;
            }
            if (this.keepHint) continue;
            spannableStringBuilder.setSpan((Object)new ForegroundColorSpan(this.getCurrentHintTextColor()), n, n + 1, 0);
        }
        return spannableStringBuilder;
    }

    private int nextValidPosition(int n) {
        int n2;
        while (n < (n2 = this.lastValidMaskPosition) && this.maskToRaw[n] == -1) {
            ++n;
        }
        if (n > n2) {
            return n2 + 1;
        }
        return n;
    }

    private int previousValidPosition(int n) {
        while (n >= 0 && this.maskToRaw[n] == -1) {
            int n2;
            n = n2 = n - 1;
            if (n2 >= 0) continue;
            return this.nextValidPosition(0);
        }
        return n;
    }

    public void afterTextChanged(Editable editable) {
        if (!this.editingAfter && this.editingBefore && this.editingOnChanged) {
            this.editingAfter = true;
            if (this.hasHint() && (this.keepHint || this.rawText.length() == 0)) {
                this.setText(this.makeMaskedTextWithHint());
            } else {
                this.setText(this.makeMaskedText());
            }
            this.selectionChanged = false;
            this.setSelection(this.selection);
            this.editingBefore = false;
            this.editingOnChanged = false;
            this.editingAfter = false;
            this.ignore = false;
        }
    }

    public void beforeTextChanged(CharSequence object, int n, int n2, int n3) {
        if (!this.editingBefore) {
            this.editingBefore = true;
            if (n > this.lastValidMaskPosition) {
                this.ignore = true;
            }
            int n4 = n;
            if (n3 == 0) {
                n4 = this.erasingStart(n);
            }
            if (((Range)(object = this.calculateRange(n4, n + n2))).getStart() != -1) {
                this.rawText.subtractFromString((Range)object);
            }
            if (n2 > 0) {
                this.selection = this.previousValidPosition(n);
            }
        }
    }

    public char getCharRepresentation() {
        return this.charRepresentation;
    }

    public String getMask() {
        return this.mask;
    }

    public String getRawText() {
        return this.rawText.getText();
    }

    public boolean isKeepHint() {
        return this.keepHint;
    }

    public void onRestoreInstanceState(Parcelable object) {
        MaskedEditText.super.onRestoreInstanceState(((Bundle)object).getParcelable("super"));
        object = ((Bundle)object).getString("text");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onRestoreInstanceState: ");
        stringBuilder.append((String)object);
        Log.d((String)"ContentValues", (String)stringBuilder.toString());
        this.setText((CharSequence)object);
    }

    public Parcelable onSaveInstanceState() {
        Parcelable parcelable = MaskedEditText.super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("super", parcelable);
        bundle.putString("text", this.getRawText());
        return bundle;
    }

    public void onSelectionChanged(int n, int n2) {
        int n3 = n;
        int n4 = n2;
        if (this.initialized) {
            if (!this.selectionChanged) {
                n = this.fixSelection(n);
                n4 = this.fixSelection(n2);
                n2 = n;
                if (n > this.getText().length()) {
                    n2 = this.getText().length();
                }
                n = n2;
                if (n2 < 0) {
                    n = 0;
                }
                n2 = n4;
                if (n4 > this.getText().length()) {
                    n2 = this.getText().length();
                }
                n4 = n2;
                if (n2 < 0) {
                    n4 = 0;
                }
                this.setSelection(n, n4);
                this.selectionChanged = true;
                n3 = n;
            } else {
                n3 = n;
                n4 = n2;
                if (n > this.rawText.length() - 1) {
                    int n5 = this.fixSelection(n);
                    int n6 = this.fixSelection(n2);
                    n3 = n;
                    n4 = n2;
                    if (n5 >= 0) {
                        n3 = n;
                        n4 = n2;
                        if (n6 < this.getText().length()) {
                            this.setSelection(n5, n6);
                            n4 = n2;
                            n3 = n;
                        }
                    }
                }
            }
        }
        MaskedEditText.super.onSelectionChanged(n3, n4);
    }

    public void onTextChanged(CharSequence object, int object2, int n, int n2) {
        if (!this.editingOnChanged && this.editingBefore) {
            this.editingOnChanged = true;
            if (!this.ignore && n2 > 0) {
                n = this.maskToRaw[this.nextValidPosition((int)object2)];
                object2 = this.rawText.addToString(this.clear(object.subSequence((int)object2, object2 + n2).toString()), n, this.maxRawLength);
                if (this.initialized) {
                    object = this.rawToMask;
                    object2 = n + object2 < ((Object)object).length ? (Object)object[n + object2] : 1 + this.lastValidMaskPosition;
                    this.selection = this.nextValidPosition((int)object2);
                }
            }
        }
    }

    public void setCharRepresentation(char c) {
        this.charRepresentation = c;
        this.cleanUp();
    }

    public void setKeepHint(boolean bl) {
        this.keepHint = bl;
        this.setText(this.getRawText());
    }

    public void setMask(String string2) {
        this.mask = string2;
        this.cleanUp();
    }

    public void setOnFocusChangeListener(View.OnFocusChangeListener onFocusChangeListener) {
        this.focusChangeListener = onFocusChangeListener;
    }

    public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        MaskedEditText.super.setText(charSequence, bufferType);
    }
}

