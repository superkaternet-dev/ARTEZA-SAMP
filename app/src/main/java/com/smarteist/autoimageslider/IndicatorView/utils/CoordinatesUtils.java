/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Pair
 */
package com.smarteist.autoimageslider.IndicatorView.utils;

import android.util.Pair;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Orientation;

public class CoordinatesUtils {
    public static int getCoordinate(Indicator indicator, int n) {
        if (indicator == null) {
            return 0;
        }
        if (indicator.getOrientation() == Orientation.HORIZONTAL) {
            return CoordinatesUtils.getXCoordinate(indicator, n);
        }
        return CoordinatesUtils.getYCoordinate(indicator, n);
    }

    private static int getFitPosition(Indicator indicator, float f, float f2) {
        int n = indicator.getCount();
        int n2 = indicator.getRadius();
        int n3 = indicator.getStroke();
        int n4 = indicator.getPadding();
        int n5 = indicator.getOrientation() == Orientation.HORIZONTAL ? indicator.getHeight() : indicator.getWidth();
        int n6 = 0;
        int n7 = 0;
        while (true) {
            int n8 = n6;
            if (n7 >= n) break;
            n6 = n7 > 0 ? n4 : n4 / 2;
            int n9 = n8 + (n2 * 2 + n3 / 2 + n6);
            float f3 = n8;
            int n10 = 0;
            n6 = f >= f3 && f <= (float)n9 ? 1 : 0;
            n8 = n10;
            if (f2 >= 0.0f) {
                n8 = n10;
                if (f2 <= (float)n5) {
                    n8 = 1;
                }
            }
            if (n6 != 0 && n8 != 0) {
                return n7;
            }
            ++n7;
            n6 = n9;
        }
        return -1;
    }

    private static int getHorizontalCoordinate(Indicator indicator, int n) {
        int n2 = indicator.getCount();
        int n3 = indicator.getRadius();
        int n4 = indicator.getStroke();
        int n5 = indicator.getPadding();
        int n6 = 0;
        for (int i = 0; i < n2; ++i) {
            n6 += n4 / 2 + n3;
            if (n == i) {
                return n6;
            }
            n6 += n3 + n5 + n4 / 2;
        }
        n = n6;
        if (indicator.getAnimationType() == IndicatorAnimationType.DROP) {
            n = n6 + n3 * 2;
        }
        return n;
    }

    public static int getPosition(Indicator indicator, float f, float f2) {
        float f3;
        if (indicator == null) {
            return -1;
        }
        if (indicator.getOrientation() == Orientation.HORIZONTAL) {
            f3 = f;
            f = f2;
        } else {
            f3 = f2;
        }
        return CoordinatesUtils.getFitPosition(indicator, f3, f);
    }

    public static Pair<Integer, Float> getProgress(Indicator indicator, int n, float f, boolean bl) {
        float f2;
        int n2;
        int n3;
        int n4;
        block15: {
            block14: {
                n4 = indicator.getCount();
                int n5 = indicator.getSelectedPosition();
                n3 = n;
                if (bl) {
                    n3 = n4 - 1 - n;
                }
                if (n3 < 0) {
                    n = 0;
                } else {
                    n = n3;
                    if (n3 > n4 - 1) {
                        n = n4 - 1;
                    }
                }
                n2 = 0;
                n4 = n > n5 ? 1 : 0;
                n3 = bl ? (n - 1 < n5 ? 1 : 0) : (n + 1 < n5 ? 1 : 0);
                if (n4 != 0) break block14;
                n4 = n5;
                if (n3 == 0) break block15;
            }
            n4 = n;
            indicator.setSelectedPosition(n4);
        }
        n3 = n2;
        if (n4 == n) {
            n3 = n2;
            if (f != 0.0f) {
                n3 = 1;
            }
        }
        if (n3 != 0) {
            n = bl ? --n : ++n;
            f2 = f;
        } else {
            f2 = 1.0f - f;
        }
        if (f2 > 1.0f) {
            f = 1.0f;
        } else {
            f = f2;
            if (f2 < 0.0f) {
                f = 0.0f;
            }
        }
        return new Pair((Object)n, (Object)Float.valueOf(f));
    }

    private static int getVerticalCoordinate(Indicator indicator) {
        int n;
        block0: {
            n = indicator.getRadius();
            if (indicator.getAnimationType() != IndicatorAnimationType.DROP) break block0;
            n *= 3;
        }
        return n;
    }

    public static int getXCoordinate(Indicator indicator, int n) {
        if (indicator == null) {
            return 0;
        }
        n = indicator.getOrientation() == Orientation.HORIZONTAL ? CoordinatesUtils.getHorizontalCoordinate(indicator, n) : CoordinatesUtils.getVerticalCoordinate(indicator);
        return n + indicator.getPaddingLeft();
    }

    public static int getYCoordinate(Indicator indicator, int n) {
        if (indicator == null) {
            return 0;
        }
        n = indicator.getOrientation() == Orientation.HORIZONTAL ? CoordinatesUtils.getVerticalCoordinate(indicator) : CoordinatesUtils.getHorizontalCoordinate(indicator, n);
        return n + indicator.getPaddingTop();
    }
}

