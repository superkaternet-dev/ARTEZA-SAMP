/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.StateSet
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package androidx.appcompat.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.StateSet;
import androidx.appcompat.graphics.drawable.DrawableContainer;
import androidx.appcompat.resources.R;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.core.content.res.TypedArrayUtils;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class StateListDrawable
extends DrawableContainer {
    private static final boolean DEBUG = false;
    private static final String TAG = "StateListDrawable";
    private boolean mMutated;
    private StateListState mStateListState;

    StateListDrawable() {
        this(null, null);
    }

    StateListDrawable(StateListState stateListState) {
        if (stateListState != null) {
            this.setConstantState(stateListState);
        }
    }

    StateListDrawable(StateListState stateListState, Resources resources) {
        this.setConstantState(new StateListState(stateListState, this, resources));
        this.onStateChange(this.getState());
    }

    private void inflateChildElements(Context object, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        int n;
        int n2;
        StateListState stateListState = this.mStateListState;
        int n3 = xmlPullParser.getDepth() + 1;
        while ((n2 = xmlPullParser.next()) != 1 && ((n = xmlPullParser.getDepth()) >= n3 || n2 != 3)) {
            if (n2 != 2 || n > n3 || !xmlPullParser.getName().equals("item")) continue;
            TypedArray typedArray = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, R.styleable.StateListDrawableItem);
            Drawable drawable2 = null;
            n2 = typedArray.getResourceId(R.styleable.StateListDrawableItem_android_drawable, -1);
            if (n2 > 0) {
                drawable2 = ResourceManagerInternal.get().getDrawable((Context)object, n2);
            }
            typedArray.recycle();
            int[] nArray = this.extractStateSet(attributeSet);
            typedArray = drawable2;
            if (drawable2 == null) {
                while ((n2 = xmlPullParser.next()) == 4) {
                }
                if (n2 == 2) {
                    typedArray = Build.VERSION.SDK_INT >= 21 ? Drawable.createFromXmlInner((Resources)resources, (XmlPullParser)xmlPullParser, (AttributeSet)attributeSet, (Resources.Theme)theme) : Drawable.createFromXmlInner((Resources)resources, (XmlPullParser)xmlPullParser, (AttributeSet)attributeSet);
                } else {
                    object = new StringBuilder();
                    ((StringBuilder)object).append(xmlPullParser.getPositionDescription());
                    ((StringBuilder)object).append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
                    throw new XmlPullParserException(((StringBuilder)object).toString());
                }
            }
            stateListState.addStateSet(nArray, (Drawable)typedArray);
        }
    }

    private void updateStateFromTypedArray(TypedArray typedArray) {
        StateListState stateListState = this.mStateListState;
        if (Build.VERSION.SDK_INT >= 21) {
            stateListState.mChangingConfigurations |= typedArray.getChangingConfigurations();
        }
        stateListState.mVariablePadding = typedArray.getBoolean(R.styleable.StateListDrawable_android_variablePadding, stateListState.mVariablePadding);
        stateListState.mConstantSize = typedArray.getBoolean(R.styleable.StateListDrawable_android_constantSize, stateListState.mConstantSize);
        stateListState.mEnterFadeDuration = typedArray.getInt(R.styleable.StateListDrawable_android_enterFadeDuration, stateListState.mEnterFadeDuration);
        stateListState.mExitFadeDuration = typedArray.getInt(R.styleable.StateListDrawable_android_exitFadeDuration, stateListState.mExitFadeDuration);
        stateListState.mDither = typedArray.getBoolean(R.styleable.StateListDrawable_android_dither, stateListState.mDither);
    }

    public void addState(int[] nArray, Drawable drawable2) {
        if (drawable2 != null) {
            this.mStateListState.addStateSet(nArray, drawable2);
            this.onStateChange(this.getState());
        }
    }

    @Override
    public void applyTheme(Resources.Theme theme) {
        super.applyTheme(theme);
        this.onStateChange(this.getState());
    }

    @Override
    void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    @Override
    StateListState cloneConstantState() {
        return new StateListState(this.mStateListState, this, null);
    }

    int[] extractStateSet(AttributeSet attributeSet) {
        int n = 0;
        int n2 = attributeSet.getAttributeCount();
        int[] nArray = new int[n2];
        block4: for (int i = 0; i < n2; ++i) {
            int n3;
            block5: {
                n3 = attributeSet.getAttributeNameResource(i);
                switch (n3) {
                    default: {
                        if (!attributeSet.getAttributeBooleanValue(i, false)) break;
                        break block5;
                    }
                    case 0x10100D0: 
                    case 0x1010199: {
                        continue block4;
                    }
                    case 0: {
                        continue block4;
                    }
                }
                n3 = -n3;
            }
            nArray[n] = n3;
            ++n;
        }
        return StateSet.trimStateSet((int[])nArray, (int)n);
    }

    int getStateCount() {
        return this.mStateListState.getChildCount();
    }

    Drawable getStateDrawable(int n) {
        return this.mStateListState.getChild(n);
    }

    int getStateDrawableIndex(int[] nArray) {
        return this.mStateListState.indexOfStateSet(nArray);
    }

    StateListState getStateListState() {
        return this.mStateListState;
    }

    int[] getStateSet(int n) {
        return this.mStateListState.mStateSets[n];
    }

    public void inflate(Context context, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        TypedArray typedArray = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, R.styleable.StateListDrawable);
        this.setVisible(typedArray.getBoolean(R.styleable.StateListDrawable_android_visible, true), true);
        this.updateStateFromTypedArray(typedArray);
        this.updateDensity(resources);
        typedArray.recycle();
        this.inflateChildElements(context, resources, xmlPullParser, attributeSet, theme);
        this.onStateChange(this.getState());
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mStateListState.mutate();
            this.mMutated = true;
        }
        return this;
    }

    @Override
    protected boolean onStateChange(int[] nArray) {
        int n;
        boolean bl = super.onStateChange(nArray);
        int n2 = n = this.mStateListState.indexOfStateSet(nArray);
        if (n < 0) {
            n2 = this.mStateListState.indexOfStateSet(StateSet.WILD_CARD);
        }
        bl = this.selectDrawable(n2) || bl;
        return bl;
    }

    @Override
    void setConstantState(DrawableContainer.DrawableContainerState drawableContainerState) {
        super.setConstantState(drawableContainerState);
        if (drawableContainerState instanceof StateListState) {
            this.mStateListState = (StateListState)drawableContainerState;
        }
    }

    static class StateListState
    extends DrawableContainer.DrawableContainerState {
        int[][] mStateSets;

        StateListState(StateListState stateListState, StateListDrawable stateListDrawable, Resources resources) {
            super(stateListState, stateListDrawable, resources);
            this.mStateSets = stateListState != null ? stateListState.mStateSets : (int[][])new int[this.getCapacity()][];
        }

        int addStateSet(int[] nArray, Drawable drawable2) {
            int n = this.addChild(drawable2);
            this.mStateSets[n] = nArray;
            return n;
        }

        @Override
        public void growArray(int n, int n2) {
            super.growArray(n, n2);
            int[][] nArrayArray = new int[n2][];
            System.arraycopy(this.mStateSets, 0, nArrayArray, 0, n);
            this.mStateSets = nArrayArray;
        }

        int indexOfStateSet(int[] nArray) {
            int[][] nArray2 = this.mStateSets;
            int n = this.getChildCount();
            for (int i = 0; i < n; ++i) {
                if (!StateSet.stateSetMatches((int[])nArray2[i], (int[])nArray)) continue;
                return i;
            }
            return -1;
        }

        @Override
        void mutate() {
            Object object = this.mStateSets;
            int[][] nArrayArray = new int[((int[][])object).length][];
            for (int i = ((int[][])object).length - 1; i >= 0; --i) {
                object = this.mStateSets;
                object = object[i] != null ? (Object)((int[])object[i].clone()) : null;
                nArrayArray[i] = (int[])object;
            }
            this.mStateSets = nArrayArray;
        }

        public Drawable newDrawable() {
            return new StateListDrawable(this, null);
        }

        public Drawable newDrawable(Resources resources) {
            return new StateListDrawable(this, resources);
        }
    }
}

