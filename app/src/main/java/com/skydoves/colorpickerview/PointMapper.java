/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Point
 */
package com.skydoves.colorpickerview;

import android.graphics.Point;
import com.skydoves.colorpickerview.ColorPickerView;

class PointMapper {
    private PointMapper() {
    }

    private static Point approximatedPoint(ColorPickerView colorPickerView, Point point, Point point2) {
        if (PointMapper.getDistance(point, point2) <= 3) {
            return point2;
        }
        Point point3 = PointMapper.getCenterPoint(point, point2);
        if (colorPickerView.getColorFromBitmap(point3.x, point3.y) == 0) {
            return PointMapper.approximatedPoint(colorPickerView, point3, point2);
        }
        return PointMapper.approximatedPoint(colorPickerView, point, point3);
    }

    private static Point getCenterPoint(Point point, Point point2) {
        return new Point((point2.x + point.x) / 2, (point2.y + point.y) / 2);
    }

    protected static Point getColorPoint(ColorPickerView colorPickerView, Point point) {
        Point point2 = new Point(colorPickerView.getMeasuredWidth() / 2, colorPickerView.getMeasuredHeight() / 2);
        if (colorPickerView.isHuePalette()) {
            return PointMapper.getHuePoint(colorPickerView, point);
        }
        return PointMapper.approximatedPoint(colorPickerView, point, point2);
    }

    private static int getDistance(Point point, Point point2) {
        return (int)Math.sqrt(Math.abs(point2.x - point.x) * Math.abs(point2.x - point.x) + Math.abs(point2.y - point.y) * Math.abs(point2.y - point.y));
    }

    private static Point getHuePoint(ColorPickerView colorPickerView, Point point) {
        float f = (float)colorPickerView.getWidth() * 0.5f;
        float f2 = (float)colorPickerView.getHeight() * 0.5f;
        float f3 = (float)point.x - f;
        float f4 = (float)point.y - f2;
        float f5 = Math.min(f, f2);
        double d = Math.sqrt(f3 * f3 + f4 * f4);
        float f6 = f3;
        float f7 = f4;
        if (d > (double)f5) {
            double d2 = f3;
            double d3 = f5;
            Double.isNaN(d3);
            Double.isNaN(d2);
            f6 = (float)(d2 * (d3 /= d));
            d2 = f4;
            d3 = f5;
            Double.isNaN(d3);
            d = d3 / d;
            Double.isNaN(d2);
            f7 = (float)(d2 * d);
        }
        return new Point((int)(f6 + f), (int)(f7 + f2));
    }
}

