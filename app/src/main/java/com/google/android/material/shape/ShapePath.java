/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.graphics.Path
 *  android.graphics.RectF
 */
package com.google.android.material.shape;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import java.util.ArrayList;
import java.util.List;

public class ShapePath {
    public float endX;
    public float endY;
    private final List<PathOperation> operations = new ArrayList<PathOperation>();
    public float startX;
    public float startY;

    public ShapePath() {
        this.reset(0.0f, 0.0f);
    }

    public ShapePath(float f, float f2) {
        this.reset(f, f2);
    }

    public void addArc(float f, float f2, float f3, float f4, float f5, float f6) {
        PathArcOperation pathArcOperation = new PathArcOperation(f, f2, f3, f4);
        pathArcOperation.startAngle = f5;
        pathArcOperation.sweepAngle = f6;
        this.operations.add(pathArcOperation);
        this.endX = (f + f3) * 0.5f + (f3 - f) / 2.0f * (float)Math.cos(Math.toRadians(f5 + f6));
        this.endY = (f2 + f4) * 0.5f + (f4 - f2) / 2.0f * (float)Math.sin(Math.toRadians(f5 + f6));
    }

    public void applyToPath(Matrix matrix, Path path) {
        int n = this.operations.size();
        for (int i = 0; i < n; ++i) {
            this.operations.get(i).applyToPath(matrix, path);
        }
    }

    public void lineTo(float f, float f2) {
        PathLineOperation pathLineOperation = new PathLineOperation();
        PathLineOperation.access$002(pathLineOperation, f);
        PathLineOperation.access$102(pathLineOperation, f2);
        this.operations.add(pathLineOperation);
        this.endX = f;
        this.endY = f2;
    }

    public void quadToPoint(float f, float f2, float f3, float f4) {
        PathQuadOperation pathQuadOperation = new PathQuadOperation();
        pathQuadOperation.controlX = f;
        pathQuadOperation.controlY = f2;
        pathQuadOperation.endX = f3;
        pathQuadOperation.endY = f4;
        this.operations.add(pathQuadOperation);
        this.endX = f3;
        this.endY = f4;
    }

    public void reset(float f, float f2) {
        this.startX = f;
        this.startY = f2;
        this.endX = f;
        this.endY = f2;
        this.operations.clear();
    }

    public static class PathArcOperation
    extends PathOperation {
        private static final RectF rectF = new RectF();
        public float bottom;
        public float left;
        public float right;
        public float startAngle;
        public float sweepAngle;
        public float top;

        public PathArcOperation(float f, float f2, float f3, float f4) {
            this.left = f;
            this.top = f2;
            this.right = f3;
            this.bottom = f4;
        }

        @Override
        public void applyToPath(Matrix matrix, Path path) {
            Matrix matrix2 = this.matrix;
            matrix.invert(matrix2);
            path.transform(matrix2);
            matrix2 = rectF;
            matrix2.set(this.left, this.top, this.right, this.bottom);
            path.arcTo((RectF)matrix2, this.startAngle, this.sweepAngle, false);
            path.transform(matrix);
        }
    }

    public static class PathLineOperation
    extends PathOperation {
        private float x;
        private float y;

        static /* synthetic */ float access$002(PathLineOperation pathLineOperation, float f) {
            pathLineOperation.x = f;
            return f;
        }

        static /* synthetic */ float access$102(PathLineOperation pathLineOperation, float f) {
            pathLineOperation.y = f;
            return f;
        }

        @Override
        public void applyToPath(Matrix matrix, Path path) {
            Matrix matrix2 = this.matrix;
            matrix.invert(matrix2);
            path.transform(matrix2);
            path.lineTo(this.x, this.y);
            path.transform(matrix);
        }
    }

    public static abstract class PathOperation {
        protected final Matrix matrix = new Matrix();

        public abstract void applyToPath(Matrix var1, Path var2);
    }

    public static class PathQuadOperation
    extends PathOperation {
        public float controlX;
        public float controlY;
        public float endX;
        public float endY;

        @Override
        public void applyToPath(Matrix matrix, Path path) {
            Matrix matrix2 = this.matrix;
            matrix.invert(matrix2);
            path.transform(matrix2);
            path.quadTo(this.controlX, this.controlY, this.endX, this.endY);
            path.transform(matrix);
        }
    }
}

