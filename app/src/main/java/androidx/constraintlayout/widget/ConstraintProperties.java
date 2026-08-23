/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 */
package androidx.constraintlayout.widget;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ConstraintProperties {
    public static final int BASELINE = 5;
    public static final int BOTTOM = 4;
    public static final int END = 7;
    public static final int LEFT = 1;
    public static final int MATCH_CONSTRAINT = 0;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    public static final int PARENT_ID = 0;
    public static final int RIGHT = 2;
    public static final int START = 6;
    public static final int TOP = 3;
    public static final int UNSET = -1;
    public static final int WRAP_CONTENT = -2;
    ConstraintLayout.LayoutParams mParams;
    View mView;

    public ConstraintProperties(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ConstraintLayout.LayoutParams) {
            this.mParams = (ConstraintLayout.LayoutParams)layoutParams;
            this.mView = view;
            return;
        }
        throw new RuntimeException("Only children of ConstraintLayout.LayoutParams supported");
    }

    private String sideToString(int n) {
        switch (n) {
            default: {
                return "undefined";
            }
            case 7: {
                return "end";
            }
            case 6: {
                return "start";
            }
            case 5: {
                return "baseline";
            }
            case 4: {
                return "bottom";
            }
            case 3: {
                return "top";
            }
            case 2: {
                return "right";
            }
            case 1: 
        }
        return "left";
    }

    public ConstraintProperties addToHorizontalChain(int n, int n2) {
        int n3 = n == 0 ? 1 : 2;
        this.connect(1, n, n3, 0);
        n3 = n2 == 0 ? 2 : 1;
        this.connect(2, n2, n3, 0);
        if (n != 0) {
            new ConstraintProperties(((ViewGroup)this.mView.getParent()).findViewById(n)).connect(2, this.mView.getId(), 1, 0);
        }
        if (n2 != 0) {
            new ConstraintProperties(((ViewGroup)this.mView.getParent()).findViewById(n2)).connect(1, this.mView.getId(), 2, 0);
        }
        return this;
    }

    public ConstraintProperties addToHorizontalChainRTL(int n, int n2) {
        int n3 = n == 0 ? 6 : 7;
        this.connect(6, n, n3, 0);
        n3 = n2 == 0 ? 7 : 6;
        this.connect(7, n2, n3, 0);
        if (n != 0) {
            new ConstraintProperties(((ViewGroup)this.mView.getParent()).findViewById(n)).connect(7, this.mView.getId(), 6, 0);
        }
        if (n2 != 0) {
            new ConstraintProperties(((ViewGroup)this.mView.getParent()).findViewById(n2)).connect(6, this.mView.getId(), 7, 0);
        }
        return this;
    }

    public ConstraintProperties addToVerticalChain(int n, int n2) {
        int n3 = n == 0 ? 3 : 4;
        this.connect(3, n, n3, 0);
        n3 = n2 == 0 ? 4 : 3;
        this.connect(4, n2, n3, 0);
        if (n != 0) {
            new ConstraintProperties(((ViewGroup)this.mView.getParent()).findViewById(n)).connect(4, this.mView.getId(), 3, 0);
        }
        if (n2 != 0) {
            new ConstraintProperties(((ViewGroup)this.mView.getParent()).findViewById(n2)).connect(3, this.mView.getId(), 4, 0);
        }
        return this;
    }

    public ConstraintProperties alpha(float f) {
        this.mView.setAlpha(f);
        return this;
    }

    public void apply() {
    }

    public ConstraintProperties center(int n, int n2, int n3, int n4, int n5, int n6, float f) {
        if (n3 >= 0) {
            if (n6 >= 0) {
                if (!(f <= 0.0f) && !(f > 1.0f)) {
                    if (n2 != 1 && n2 != 2) {
                        if (n2 != 6 && n2 != 7) {
                            this.connect(3, n, n2, n3);
                            this.connect(4, n4, n5, n6);
                            this.mParams.verticalBias = f;
                        } else {
                            this.connect(6, n, n2, n3);
                            this.connect(7, n4, n5, n6);
                            this.mParams.horizontalBias = f;
                        }
                    } else {
                        this.connect(1, n, n2, n3);
                        this.connect(2, n4, n5, n6);
                        this.mParams.horizontalBias = f;
                    }
                    return this;
                }
                throw new IllegalArgumentException("bias must be between 0 and 1 inclusive");
            }
            throw new IllegalArgumentException("margin must be > 0");
        }
        throw new IllegalArgumentException("margin must be > 0");
    }

    public ConstraintProperties centerHorizontally(int n) {
        if (n == 0) {
            this.center(0, 1, 0, 0, 2, 0, 0.5f);
        } else {
            this.center(n, 2, 0, n, 1, 0, 0.5f);
        }
        return this;
    }

    public ConstraintProperties centerHorizontally(int n, int n2, int n3, int n4, int n5, int n6, float f) {
        this.connect(1, n, n2, n3);
        this.connect(2, n4, n5, n6);
        this.mParams.horizontalBias = f;
        return this;
    }

    public ConstraintProperties centerHorizontallyRtl(int n) {
        if (n == 0) {
            this.center(0, 6, 0, 0, 7, 0, 0.5f);
        } else {
            this.center(n, 7, 0, n, 6, 0, 0.5f);
        }
        return this;
    }

    public ConstraintProperties centerHorizontallyRtl(int n, int n2, int n3, int n4, int n5, int n6, float f) {
        this.connect(6, n, n2, n3);
        this.connect(7, n4, n5, n6);
        this.mParams.horizontalBias = f;
        return this;
    }

    public ConstraintProperties centerVertically(int n) {
        if (n == 0) {
            this.center(0, 3, 0, 0, 4, 0, 0.5f);
        } else {
            this.center(n, 4, 0, n, 3, 0, 0.5f);
        }
        return this;
    }

    public ConstraintProperties centerVertically(int n, int n2, int n3, int n4, int n5, int n6, float f) {
        this.connect(3, n, n2, n3);
        this.connect(4, n4, n5, n6);
        this.mParams.verticalBias = f;
        return this;
    }

    /*
     * Enabled aggressive block sorting
     */
    public ConstraintProperties connect(int n, int n2, int n3, int n4) {
        block30: {
            block29: {
                block28: {
                    switch (n) {
                        default: {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(this.sideToString(n));
                            stringBuilder.append(" to ");
                            stringBuilder.append(this.sideToString(n3));
                            stringBuilder.append(" unknown");
                            throw new IllegalArgumentException(stringBuilder.toString());
                        }
                        case 7: {
                            if (n3 == 7) {
                                this.mParams.endToEnd = n2;
                                this.mParams.endToStart = -1;
                            } else {
                                if (n3 != 6) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("right to ");
                                    stringBuilder.append(this.sideToString(n3));
                                    stringBuilder.append(" undefined");
                                    throw new IllegalArgumentException(stringBuilder.toString());
                                }
                                this.mParams.endToStart = n2;
                                this.mParams.endToEnd = -1;
                            }
                            if (Build.VERSION.SDK_INT < 17) return this;
                            this.mParams.setMarginEnd(n4);
                            return this;
                        }
                        case 6: {
                            if (n3 == 6) {
                                this.mParams.startToStart = n2;
                                this.mParams.startToEnd = -1;
                            } else {
                                if (n3 != 7) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("right to ");
                                    stringBuilder.append(this.sideToString(n3));
                                    stringBuilder.append(" undefined");
                                    throw new IllegalArgumentException(stringBuilder.toString());
                                }
                                this.mParams.startToEnd = n2;
                                this.mParams.startToStart = -1;
                            }
                            if (Build.VERSION.SDK_INT < 17) return this;
                            this.mParams.setMarginStart(n4);
                            return this;
                        }
                        case 5: {
                            if (n3 == 5) {
                                this.mParams.baselineToBaseline = n2;
                                this.mParams.bottomToBottom = -1;
                                this.mParams.bottomToTop = -1;
                                this.mParams.topToTop = -1;
                                this.mParams.topToBottom = -1;
                                return this;
                            }
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("right to ");
                            stringBuilder.append(this.sideToString(n3));
                            stringBuilder.append(" undefined");
                            throw new IllegalArgumentException(stringBuilder.toString());
                        }
                        case 4: {
                            if (n3 == 4) {
                                this.mParams.bottomToBottom = n2;
                                this.mParams.bottomToTop = -1;
                                this.mParams.baselineToBaseline = -1;
                            } else {
                                if (n3 != 3) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("right to ");
                                    stringBuilder.append(this.sideToString(n3));
                                    stringBuilder.append(" undefined");
                                    throw new IllegalArgumentException(stringBuilder.toString());
                                }
                                this.mParams.bottomToTop = n2;
                                this.mParams.bottomToBottom = -1;
                                this.mParams.baselineToBaseline = -1;
                            }
                            this.mParams.bottomMargin = n4;
                            return this;
                        }
                        case 3: {
                            if (n3 == 3) {
                                this.mParams.topToTop = n2;
                                this.mParams.topToBottom = -1;
                                this.mParams.baselineToBaseline = -1;
                            } else {
                                if (n3 != 4) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("right to ");
                                    stringBuilder.append(this.sideToString(n3));
                                    stringBuilder.append(" undefined");
                                    throw new IllegalArgumentException(stringBuilder.toString());
                                }
                                this.mParams.topToBottom = n2;
                                this.mParams.topToTop = -1;
                                this.mParams.baselineToBaseline = -1;
                            }
                            this.mParams.topMargin = n4;
                            return this;
                        }
                        case 2: {
                            if (n3 == 1) {
                                this.mParams.rightToLeft = n2;
                                this.mParams.rightToRight = -1;
                            } else {
                                if (n3 != 2) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("right to ");
                                    stringBuilder.append(this.sideToString(n3));
                                    stringBuilder.append(" undefined");
                                    throw new IllegalArgumentException(stringBuilder.toString());
                                }
                                this.mParams.rightToRight = n2;
                                this.mParams.rightToLeft = -1;
                            }
                            this.mParams.rightMargin = n4;
                            return this;
                        }
                        case 1: 
                    }
                    if (n3 != 1) break block28;
                    this.mParams.leftToLeft = n2;
                    this.mParams.leftToRight = -1;
                    break block29;
                }
                if (n3 != 2) break block30;
                this.mParams.leftToRight = n2;
                this.mParams.leftToLeft = -1;
            }
            this.mParams.leftMargin = n4;
            return this;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Left to ");
        stringBuilder.append(this.sideToString(n3));
        stringBuilder.append(" undefined");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public ConstraintProperties constrainDefaultHeight(int n) {
        this.mParams.matchConstraintDefaultHeight = n;
        return this;
    }

    public ConstraintProperties constrainDefaultWidth(int n) {
        this.mParams.matchConstraintDefaultWidth = n;
        return this;
    }

    public ConstraintProperties constrainHeight(int n) {
        this.mParams.height = n;
        return this;
    }

    public ConstraintProperties constrainMaxHeight(int n) {
        this.mParams.matchConstraintMaxHeight = n;
        return this;
    }

    public ConstraintProperties constrainMaxWidth(int n) {
        this.mParams.matchConstraintMaxWidth = n;
        return this;
    }

    public ConstraintProperties constrainMinHeight(int n) {
        this.mParams.matchConstraintMinHeight = n;
        return this;
    }

    public ConstraintProperties constrainMinWidth(int n) {
        this.mParams.matchConstraintMinWidth = n;
        return this;
    }

    public ConstraintProperties constrainWidth(int n) {
        this.mParams.width = n;
        return this;
    }

    public ConstraintProperties dimensionRatio(String string2) {
        this.mParams.dimensionRatio = string2;
        return this;
    }

    public ConstraintProperties elevation(float f) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.mView.setElevation(f);
        }
        return this;
    }

    public ConstraintProperties goneMargin(int n, int n2) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("unknown constraint");
            }
            case 7: {
                this.mParams.goneEndMargin = n2;
                break;
            }
            case 6: {
                this.mParams.goneStartMargin = n2;
                break;
            }
            case 5: {
                throw new IllegalArgumentException("baseline does not support margins");
            }
            case 4: {
                this.mParams.goneBottomMargin = n2;
                break;
            }
            case 3: {
                this.mParams.goneTopMargin = n2;
                break;
            }
            case 2: {
                this.mParams.goneRightMargin = n2;
                break;
            }
            case 1: {
                this.mParams.goneLeftMargin = n2;
            }
        }
        return this;
    }

    public ConstraintProperties horizontalBias(float f) {
        this.mParams.horizontalBias = f;
        return this;
    }

    public ConstraintProperties horizontalChainStyle(int n) {
        this.mParams.horizontalChainStyle = n;
        return this;
    }

    public ConstraintProperties horizontalWeight(float f) {
        this.mParams.horizontalWeight = f;
        return this;
    }

    public ConstraintProperties margin(int n, int n2) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("unknown constraint");
            }
            case 7: {
                this.mParams.setMarginEnd(n2);
                break;
            }
            case 6: {
                this.mParams.setMarginStart(n2);
                break;
            }
            case 5: {
                throw new IllegalArgumentException("baseline does not support margins");
            }
            case 4: {
                this.mParams.bottomMargin = n2;
                break;
            }
            case 3: {
                this.mParams.topMargin = n2;
                break;
            }
            case 2: {
                this.mParams.rightMargin = n2;
                break;
            }
            case 1: {
                this.mParams.leftMargin = n2;
            }
        }
        return this;
    }

    public ConstraintProperties removeConstraints(int n) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("unknown constraint");
            }
            case 7: {
                this.mParams.endToStart = -1;
                this.mParams.endToEnd = -1;
                this.mParams.setMarginEnd(-1);
                this.mParams.goneEndMargin = -1;
                break;
            }
            case 6: {
                this.mParams.startToEnd = -1;
                this.mParams.startToStart = -1;
                this.mParams.setMarginStart(-1);
                this.mParams.goneStartMargin = -1;
                break;
            }
            case 5: {
                this.mParams.baselineToBaseline = -1;
                break;
            }
            case 4: {
                this.mParams.bottomToTop = -1;
                this.mParams.bottomToBottom = -1;
                this.mParams.bottomMargin = -1;
                this.mParams.goneBottomMargin = -1;
                break;
            }
            case 3: {
                this.mParams.topToBottom = -1;
                this.mParams.topToTop = -1;
                this.mParams.topMargin = -1;
                this.mParams.goneTopMargin = -1;
                break;
            }
            case 2: {
                this.mParams.rightToRight = -1;
                this.mParams.rightToLeft = -1;
                this.mParams.rightMargin = -1;
                this.mParams.goneRightMargin = -1;
                break;
            }
            case 1: {
                this.mParams.leftToRight = -1;
                this.mParams.leftToLeft = -1;
                this.mParams.leftMargin = -1;
                this.mParams.goneLeftMargin = -1;
            }
        }
        return this;
    }

    public ConstraintProperties removeFromHorizontalChain() {
        int n = this.mParams.leftToRight;
        int n2 = this.mParams.rightToLeft;
        Object object = this.mParams;
        if (n == -1 && n2 == -1) {
            n2 = ((ConstraintLayout.LayoutParams)((Object)object)).startToEnd;
            int n3 = this.mParams.endToStart;
            if (n2 != -1 || n3 != -1) {
                Object object2 = new ConstraintProperties(((ViewGroup)this.mView.getParent()).findViewById(n2));
                object = new ConstraintProperties(((ViewGroup)this.mView.getParent()).findViewById(n3));
                ConstraintLayout.LayoutParams layoutParams = this.mParams;
                if (n2 != -1 && n3 != -1) {
                    object2.connect(7, n3, 6, 0);
                    ((ConstraintProperties)object).connect(6, n, 7, 0);
                } else if (n != -1 || n3 != -1) {
                    n = layoutParams.rightToRight;
                    layoutParams = this.mParams;
                    if (n != -1) {
                        object2.connect(7, layoutParams.rightToRight, 7, 0);
                    } else {
                        n = layoutParams.leftToLeft;
                        object2 = this.mParams;
                        if (n != -1) {
                            ((ConstraintProperties)object).connect(6, ((ConstraintLayout.LayoutParams)((Object)object2)).leftToLeft, 6, 0);
                        }
                    }
                }
            }
            this.removeConstraints(6);
            this.removeConstraints(7);
        } else {
            Object object3 = new ConstraintProperties(((ViewGroup)this.mView.getParent()).findViewById(n));
            object = new ConstraintProperties(((ViewGroup)this.mView.getParent()).findViewById(n2));
            ConstraintLayout.LayoutParams layoutParams = this.mParams;
            if (n != -1 && n2 != -1) {
                object3.connect(2, n2, 1, 0);
                ((ConstraintProperties)object).connect(1, n, 2, 0);
            } else if (n != -1 || n2 != -1) {
                n = layoutParams.rightToRight;
                layoutParams = this.mParams;
                if (n != -1) {
                    object3.connect(2, layoutParams.rightToRight, 2, 0);
                } else {
                    n = layoutParams.leftToLeft;
                    object3 = this.mParams;
                    if (n != -1) {
                        ((ConstraintProperties)object).connect(1, ((ConstraintLayout.LayoutParams)((Object)object3)).leftToLeft, 1, 0);
                    }
                }
            }
            this.removeConstraints(1);
            this.removeConstraints(2);
        }
        return this;
    }

    public ConstraintProperties removeFromVerticalChain() {
        int n = this.mParams.topToBottom;
        int n2 = this.mParams.bottomToTop;
        if (n != -1 || n2 != -1) {
            Object object = new ConstraintProperties(((ViewGroup)this.mView.getParent()).findViewById(n));
            ConstraintProperties constraintProperties = new ConstraintProperties(((ViewGroup)this.mView.getParent()).findViewById(n2));
            ConstraintLayout.LayoutParams layoutParams = this.mParams;
            if (n != -1 && n2 != -1) {
                object.connect(4, n2, 3, 0);
                constraintProperties.connect(3, n, 4, 0);
            } else if (n != -1 || n2 != -1) {
                n2 = layoutParams.bottomToBottom;
                layoutParams = this.mParams;
                if (n2 != -1) {
                    object.connect(4, layoutParams.bottomToBottom, 4, 0);
                } else {
                    n2 = layoutParams.topToTop;
                    object = this.mParams;
                    if (n2 != -1) {
                        constraintProperties.connect(3, ((ConstraintLayout.LayoutParams)((Object)object)).topToTop, 3, 0);
                    }
                }
            }
        }
        this.removeConstraints(3);
        this.removeConstraints(4);
        return this;
    }

    public ConstraintProperties rotation(float f) {
        this.mView.setRotation(f);
        return this;
    }

    public ConstraintProperties rotationX(float f) {
        this.mView.setRotationX(f);
        return this;
    }

    public ConstraintProperties rotationY(float f) {
        this.mView.setRotationY(f);
        return this;
    }

    public ConstraintProperties scaleX(float f) {
        this.mView.setScaleY(f);
        return this;
    }

    public ConstraintProperties scaleY(float f) {
        return this;
    }

    public ConstraintProperties transformPivot(float f, float f2) {
        this.mView.setPivotX(f);
        this.mView.setPivotY(f2);
        return this;
    }

    public ConstraintProperties transformPivotX(float f) {
        this.mView.setPivotX(f);
        return this;
    }

    public ConstraintProperties transformPivotY(float f) {
        this.mView.setPivotY(f);
        return this;
    }

    public ConstraintProperties translation(float f, float f2) {
        this.mView.setTranslationX(f);
        this.mView.setTranslationY(f2);
        return this;
    }

    public ConstraintProperties translationX(float f) {
        this.mView.setTranslationX(f);
        return this;
    }

    public ConstraintProperties translationY(float f) {
        this.mView.setTranslationY(f);
        return this;
    }

    public ConstraintProperties translationZ(float f) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.mView.setTranslationZ(f);
        }
        return this;
    }

    public ConstraintProperties verticalBias(float f) {
        this.mParams.verticalBias = f;
        return this;
    }

    public ConstraintProperties verticalChainStyle(int n) {
        this.mParams.verticalChainStyle = n;
        return this;
    }

    public ConstraintProperties verticalWeight(float f) {
        this.mParams.verticalWeight = f;
        return this;
    }

    public ConstraintProperties visibility(int n) {
        this.mView.setVisibility(n);
        return this;
    }
}

