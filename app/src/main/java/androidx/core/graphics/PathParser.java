/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Path
 *  android.util.Log
 */
package androidx.core.graphics;

import android.graphics.Path;
import android.util.Log;
import java.util.ArrayList;

public class PathParser {
    private static final String LOGTAG = "PathParser";

    private PathParser() {
    }

    private static void addNode(ArrayList<PathDataNode> arrayList, char c, float[] fArray) {
        arrayList.add(new PathDataNode(c, fArray));
    }

    public static boolean canMorph(PathDataNode[] pathDataNodeArray, PathDataNode[] pathDataNodeArray2) {
        if (pathDataNodeArray != null && pathDataNodeArray2 != null) {
            if (pathDataNodeArray.length != pathDataNodeArray2.length) {
                return false;
            }
            for (int i = 0; i < pathDataNodeArray.length; ++i) {
                if (pathDataNodeArray[i].mType == pathDataNodeArray2[i].mType && pathDataNodeArray[i].mParams.length == pathDataNodeArray2[i].mParams.length) {
                    continue;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    static float[] copyOfRange(float[] fArray, int n, int n2) {
        if (n <= n2) {
            int n3 = fArray.length;
            if (n >= 0 && n <= n3) {
                n3 = Math.min(n2 -= n, n3 - n);
                float[] fArray2 = new float[n2];
                System.arraycopy(fArray, n, fArray2, 0, n3);
                return fArray2;
            }
            throw new ArrayIndexOutOfBoundsException();
        }
        throw new IllegalArgumentException();
    }

    public static PathDataNode[] createNodesFromPathData(String string2) {
        if (string2 == null) {
            return null;
        }
        int n = 0;
        int n2 = 1;
        ArrayList<PathDataNode> arrayList = new ArrayList<PathDataNode>();
        while (n2 < string2.length()) {
            String string3 = string2.substring(n, n2 = PathParser.nextStart(string2, n2)).trim();
            if (string3.length() > 0) {
                float[] fArray = PathParser.getFloats(string3);
                PathParser.addNode(arrayList, string3.charAt(0), fArray);
            }
            n = n2++;
        }
        if (n2 - n == 1 && n < string2.length()) {
            PathParser.addNode(arrayList, string2.charAt(n), new float[0]);
        }
        return arrayList.toArray(new PathDataNode[arrayList.size()]);
    }

    public static Path createPathFromPathData(String string2) {
        Path path = new Path();
        Object object = PathParser.createNodesFromPathData(string2);
        if (object != null) {
            try {
                PathDataNode.nodesToPath((PathDataNode[])object, path);
                return path;
            }
            catch (RuntimeException runtimeException) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Error in parsing ");
                ((StringBuilder)object).append(string2);
                throw new RuntimeException(((StringBuilder)object).toString(), runtimeException);
            }
        }
        return null;
    }

    public static PathDataNode[] deepCopyNodes(PathDataNode[] pathDataNodeArray) {
        if (pathDataNodeArray == null) {
            return null;
        }
        PathDataNode[] pathDataNodeArray2 = new PathDataNode[pathDataNodeArray.length];
        for (int i = 0; i < pathDataNodeArray.length; ++i) {
            pathDataNodeArray2[i] = new PathDataNode(pathDataNodeArray[i]);
        }
        return pathDataNodeArray2;
    }

    private static void extract(String string2, int n, ExtractFloatResult extractFloatResult) {
        int n2;
        boolean bl = false;
        extractFloatResult.mEndWithNegOrDot = false;
        boolean bl2 = false;
        boolean bl3 = false;
        for (n2 = n; n2 < string2.length(); ++n2) {
            boolean bl4;
            boolean bl5;
            boolean bl6;
            boolean bl7 = false;
            switch (string2.charAt(n2)) {
                default: {
                    bl6 = bl;
                    bl5 = bl2;
                    bl4 = bl7;
                    break;
                }
                case 'E': 
                case 'e': {
                    bl4 = true;
                    bl6 = bl;
                    bl5 = bl2;
                    break;
                }
                case '.': {
                    if (!bl2) {
                        bl5 = true;
                        bl6 = bl;
                        bl4 = bl7;
                        break;
                    }
                    bl6 = true;
                    extractFloatResult.mEndWithNegOrDot = true;
                    bl5 = bl2;
                    bl4 = bl7;
                    break;
                }
                case '-': {
                    bl6 = bl;
                    bl5 = bl2;
                    bl4 = bl7;
                    if (n2 == n) break;
                    bl6 = bl;
                    bl5 = bl2;
                    bl4 = bl7;
                    if (bl3) break;
                    bl6 = true;
                    extractFloatResult.mEndWithNegOrDot = true;
                    bl5 = bl2;
                    bl4 = bl7;
                    break;
                }
                case ' ': 
                case ',': {
                    bl6 = true;
                    bl4 = bl7;
                    bl5 = bl2;
                }
            }
            if (bl6) break;
            bl = bl6;
            bl2 = bl5;
            bl3 = bl4;
        }
        extractFloatResult.mEndPosition = n2;
    }

    private static float[] getFloats(String string2) {
        if (string2.charAt(0) != 'z' && string2.charAt(0) != 'Z') {
            Object object = new float[string2.length()];
            int n = 0;
            int n2 = 1;
            ExtractFloatResult extractFloatResult = new ExtractFloatResult();
            int n3 = string2.length();
            while (n2 < n3) {
                int n4;
                int n5;
                block10: {
                    PathParser.extract(string2, n2, extractFloatResult);
                    n5 = extractFloatResult.mEndPosition;
                    n4 = n;
                    if (n2 >= n5) break block10;
                    object[n] = Float.parseFloat(string2.substring(n2, n5));
                    n4 = n + 1;
                }
                if (extractFloatResult.mEndWithNegOrDot) {
                    n2 = n5;
                    n = n4;
                    continue;
                }
                n2 = n5 + 1;
                n = n4;
            }
            try {
                object = PathParser.copyOfRange(object, 0, n);
                return object;
            }
            catch (NumberFormatException numberFormatException) {
                object = new StringBuilder();
                object.append("error in parsing \"");
                object.append(string2);
                object.append("\"");
                throw new RuntimeException(object.toString(), numberFormatException);
            }
        }
        return new float[0];
    }

    public static boolean interpolatePathDataNodes(PathDataNode[] object, PathDataNode[] pathDataNodeArray, PathDataNode[] pathDataNodeArray2, float f) {
        if (object != null && pathDataNodeArray != null && pathDataNodeArray2 != null) {
            if (((PathDataNode[])object).length == pathDataNodeArray.length && pathDataNodeArray.length == pathDataNodeArray2.length) {
                if (!PathParser.canMorph(pathDataNodeArray, pathDataNodeArray2)) {
                    return false;
                }
                for (int i = 0; i < ((PathDataNode[])object).length; ++i) {
                    object[i].interpolatePathDataNode(pathDataNodeArray[i], pathDataNodeArray2[i], f);
                }
                return true;
            }
            throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes must have the same length");
        }
        object = new IllegalArgumentException("The nodes to be interpolated and resulting nodes cannot be null");
        throw object;
    }

    private static int nextStart(String string2, int n) {
        while (n < string2.length()) {
            char c = string2.charAt(n);
            if (((c - 65) * (c - 90) <= 0 || (c - 97) * (c - 122) <= 0) && c != 'e' && c != 'E') {
                return n;
            }
            ++n;
        }
        return n;
    }

    public static void updateNodes(PathDataNode[] pathDataNodeArray, PathDataNode[] pathDataNodeArray2) {
        for (int i = 0; i < pathDataNodeArray2.length; ++i) {
            pathDataNodeArray[i].mType = pathDataNodeArray2[i].mType;
            for (int j = 0; j < pathDataNodeArray2[i].mParams.length; ++j) {
                pathDataNodeArray[i].mParams[j] = pathDataNodeArray2[i].mParams[j];
            }
        }
    }

    private static class ExtractFloatResult {
        int mEndPosition;
        boolean mEndWithNegOrDot;

        ExtractFloatResult() {
        }
    }

    public static class PathDataNode {
        public float[] mParams;
        public char mType;

        PathDataNode(char c, float[] fArray) {
            this.mType = c;
            this.mParams = fArray;
        }

        PathDataNode(PathDataNode object) {
            this.mType = ((PathDataNode)object).mType;
            object = ((PathDataNode)object).mParams;
            this.mParams = PathParser.copyOfRange((float[])object, 0, ((Object)object).length);
        }

        private static void addCommand(Path path, float[] fArray, char c, char c2, float[] fArray2) {
            int n;
            Path path2 = path;
            float f = fArray[0];
            float f2 = fArray[1];
            float f3 = fArray[2];
            float f4 = fArray[3];
            float f5 = fArray[4];
            float f6 = fArray[5];
            switch (c2) {
                default: {
                    n = 2;
                    break;
                }
                case 'Z': 
                case 'z': {
                    path.close();
                    f = f5;
                    f2 = f6;
                    f3 = f5;
                    f4 = f6;
                    path2.moveTo(f, f2);
                    n = 2;
                    break;
                }
                case 'Q': 
                case 'S': 
                case 'q': 
                case 's': {
                    n = 4;
                    break;
                }
                case 'L': 
                case 'M': 
                case 'T': 
                case 'l': 
                case 'm': 
                case 't': {
                    n = 2;
                    break;
                }
                case 'H': 
                case 'V': 
                case 'h': 
                case 'v': {
                    n = 1;
                    break;
                }
                case 'C': 
                case 'c': {
                    n = 6;
                    break;
                }
                case 'A': 
                case 'a': {
                    n = 7;
                }
            }
            float f7 = f3;
            float f8 = f4;
            f3 = f5;
            f5 = f2;
            f4 = f6;
            f6 = f;
            for (int i = 0; i < fArray2.length; i += n) {
                switch (c2) {
                    default: {
                        f2 = f7;
                        f = f8;
                        break;
                    }
                    case 'v': {
                        path2.rLineTo(0.0f, fArray2[i + 0]);
                        f5 += fArray2[i + 0];
                        f2 = f7;
                        f = f8;
                        break;
                    }
                    case 't': {
                        f = 0.0f;
                        f2 = 0.0f;
                        if (c == 'q' || c == 't' || c == 'Q' || c == 'T') {
                            f = f6 - f7;
                            f2 = f5 - f8;
                        }
                        path2.rQuadTo(f, f2, fArray2[i + 0], fArray2[i + 1]);
                        f8 = f6 + fArray2[i + 0];
                        f7 = f5 + fArray2[i + 1];
                        f = f6 + f;
                        float f9 = f5 + f2;
                        f5 = f7;
                        f6 = f8;
                        f2 = f;
                        f = f9;
                        break;
                    }
                    case 's': {
                        if (c != 'c' && c != 's' && c != 'C' && c != 'S') {
                            f2 = 0.0f;
                            f = 0.0f;
                        } else {
                            f2 = f6 - f7;
                            f = f5 - f8;
                        }
                        path.rCubicTo(f2, f, fArray2[i + 0], fArray2[i + 1], fArray2[i + 2], fArray2[i + 3]);
                        f = fArray2[i + 0];
                        f8 = fArray2[i + 1];
                        f2 = f6 + fArray2[i + 2];
                        f7 = fArray2[i + 3];
                        f8 = f5 + f8;
                        f5 = f7 + f5;
                        f6 = f2;
                        f2 = f += f6;
                        f = f8;
                        break;
                    }
                    case 'q': {
                        path2.rQuadTo(fArray2[i + 0], fArray2[i + 1], fArray2[i + 2], fArray2[i + 3]);
                        f = fArray2[i + 0];
                        f8 = fArray2[i + 1];
                        f2 = f6 + fArray2[i + 2];
                        f7 = fArray2[i + 3];
                        f8 = f5 + f8;
                        f5 = f7 + f5;
                        f6 = f2;
                        f2 = f += f6;
                        f = f8;
                        break;
                    }
                    case 'm': {
                        f6 += fArray2[i + 0];
                        f5 += fArray2[i + 1];
                        if (i > 0) {
                            path2.rLineTo(fArray2[i + 0], fArray2[i + 1]);
                            f2 = f7;
                            f = f8;
                            break;
                        }
                        path2.rMoveTo(fArray2[i + 0], fArray2[i + 1]);
                        f3 = f6;
                        f4 = f5;
                        f2 = f7;
                        f = f8;
                        break;
                    }
                    case 'l': {
                        path2.rLineTo(fArray2[i + 0], fArray2[i + 1]);
                        f6 += fArray2[i + 0];
                        f5 += fArray2[i + 1];
                        f2 = f7;
                        f = f8;
                        break;
                    }
                    case 'h': {
                        path2.rLineTo(fArray2[i + 0], 0.0f);
                        f6 += fArray2[i + 0];
                        f2 = f7;
                        f = f8;
                        break;
                    }
                    case 'c': {
                        path.rCubicTo(fArray2[i + 0], fArray2[i + 1], fArray2[i + 2], fArray2[i + 3], fArray2[i + 4], fArray2[i + 5]);
                        f = fArray2[i + 2];
                        f8 = fArray2[i + 3];
                        f2 = f6 + fArray2[i + 4];
                        f7 = fArray2[i + 5];
                        f8 = f5 + f8;
                        f5 = f7 + f5;
                        f6 = f2;
                        f2 = f += f6;
                        f = f8;
                        break;
                    }
                    case 'a': {
                        f = fArray2[i + 5];
                        float f9 = fArray2[i + 6];
                        f8 = fArray2[i + 0];
                        f7 = fArray2[i + 1];
                        f2 = fArray2[i + 2];
                        boolean bl = fArray2[i + 3] != 0.0f;
                        boolean bl2 = fArray2[i + 4] != 0.0f;
                        PathDataNode.drawArc(path, f6, f5, f + f6, f9 + f5, f8, f7, f2, bl, bl2);
                        path2 = path;
                        f2 = f6 += fArray2[i + 5];
                        f = f5 += fArray2[i + 6];
                        break;
                    }
                    case 'V': {
                        f5 = fArray2[i + 0];
                        path2 = path;
                        path2.lineTo(f6, f5);
                        f5 = fArray2[i + 0];
                        f2 = f7;
                        f = f8;
                        break;
                    }
                    case 'T': {
                        f = f6;
                        f2 = f5;
                        if (c == 'q' || c == 't' || c == 'Q' || c == 'T') {
                            f = f6 * 2.0f - f7;
                            f2 = f5 * 2.0f - f8;
                        }
                        path2.quadTo(f, f2, fArray2[i + 0], fArray2[i + 1]);
                        f6 = fArray2[i + 0];
                        f5 = fArray2[i + 1];
                        f8 = f;
                        f = f2;
                        f2 = f8;
                        break;
                    }
                    case 'S': {
                        if (c == 'c' || c == 's' || c == 'C' || c == 'S') {
                            f6 = f6 * 2.0f - f7;
                            f5 = f5 * 2.0f - f8;
                        }
                        path.cubicTo(f6, f5, fArray2[i + 0], fArray2[i + 1], fArray2[i + 2], fArray2[i + 3]);
                        f2 = fArray2[i + 0];
                        f = fArray2[i + 1];
                        f6 = fArray2[i + 2];
                        f5 = fArray2[i + 3];
                        break;
                    }
                    case 'Q': {
                        path2.quadTo(fArray2[i + 0], fArray2[i + 1], fArray2[i + 2], fArray2[i + 3]);
                        f2 = fArray2[i + 0];
                        f = fArray2[i + 1];
                        f6 = fArray2[i + 2];
                        f5 = fArray2[i + 3];
                        break;
                    }
                    case 'M': {
                        f5 = fArray2[i + 0];
                        f6 = fArray2[i + 1];
                        if (i > 0) {
                            path2.lineTo(fArray2[i + 0], fArray2[i + 1]);
                            f2 = f5;
                            f5 = f6;
                            f6 = f2;
                            f2 = f7;
                            f = f8;
                            break;
                        }
                        path2.moveTo(fArray2[i + 0], fArray2[i + 1]);
                        f2 = f5;
                        f = f6;
                        f3 = f5;
                        f4 = f6;
                        f5 = f;
                        f6 = f2;
                        f2 = f7;
                        f = f8;
                        break;
                    }
                    case 'L': {
                        path2.lineTo(fArray2[i + 0], fArray2[i + 1]);
                        f6 = fArray2[i + 0];
                        f5 = fArray2[i + 1];
                        f2 = f7;
                        f = f8;
                        break;
                    }
                    case 'H': {
                        path2.lineTo(fArray2[i + 0], f5);
                        f6 = fArray2[i + 0];
                        f2 = f7;
                        f = f8;
                        break;
                    }
                    case 'C': {
                        path.cubicTo(fArray2[i + 0], fArray2[i + 1], fArray2[i + 2], fArray2[i + 3], fArray2[i + 4], fArray2[i + 5]);
                        f6 = fArray2[i + 4];
                        f5 = fArray2[i + 5];
                        f2 = fArray2[i + 2];
                        f = fArray2[i + 3];
                        break;
                    }
                    case 'A': {
                        f = fArray2[i + 5];
                        float f9 = fArray2[i + 6];
                        f2 = fArray2[i + 0];
                        f7 = fArray2[i + 1];
                        f8 = fArray2[i + 2];
                        boolean bl = fArray2[i + 3] != 0.0f;
                        boolean bl2 = fArray2[i + 4] != 0.0f;
                        PathDataNode.drawArc(path, f6, f5, f, f9, f2, f7, f8, bl, bl2);
                        f2 = fArray2[i + 5];
                        f = fArray2[i + 6];
                        f6 = f2;
                        f5 = f;
                    }
                }
                c = c2;
                f7 = f2;
                f8 = f;
            }
            fArray[0] = f6;
            fArray[1] = f5;
            fArray[2] = f7;
            fArray[3] = f8;
            fArray[4] = f3;
            fArray[5] = f4;
        }

        private static void arcToBezier(Path path, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
            int n = (int)Math.ceil(Math.abs(d9 * 4.0 / Math.PI));
            double d10 = Math.cos(d7);
            double d11 = Math.sin(d7);
            d7 = Math.cos(d8);
            double d12 = Math.sin(d8);
            double d13 = -d3;
            double d14 = -d3 * d11 * d12 + d4 * d10 * d7;
            double d15 = n;
            Double.isNaN(d15);
            double d16 = d9 / d15;
            d15 = d5;
            d13 = d13 * d10 * d12 - d4 * d11 * d7;
            double d17 = d8;
            d9 = d14;
            d14 = d6;
            d8 = d12;
            d5 = d11;
            d6 = d10;
            d10 = d16;
            for (int i = 0; i < n; ++i) {
                double d18 = d17 + d10;
                double d19 = Math.sin(d18);
                d12 = Math.cos(d18);
                double d20 = d + d3 * d6 * d12 - d4 * d5 * d19;
                d11 = d2 + d3 * d5 * d12 + d4 * d6 * d19;
                d16 = -d3 * d6 * d19 - d4 * d5 * d12;
                d12 = -d3 * d5 * d19 + d4 * d6 * d12;
                d19 = Math.tan((d18 - d17) / 2.0);
                d17 = Math.sin(d18 - d17) * (Math.sqrt(d19 * 3.0 * d19 + 4.0) - 1.0) / 3.0;
                path.rLineTo(0.0f, 0.0f);
                path.cubicTo((float)(d15 + d17 * d13), (float)(d14 + d17 * d9), (float)(d20 - d17 * d16), (float)(d11 - d17 * d12), (float)d20, (float)d11);
                d17 = d18;
                d15 = d20;
                d14 = d11;
                d13 = d16;
                d9 = d12;
            }
        }

        private static void drawArc(Path path, float f, float f2, float f3, float f4, float f5, float f6, float f7, boolean bl, boolean bl2) {
            double d = Math.toRadians(f7);
            double d2 = Math.cos(d);
            double d3 = Math.sin(d);
            double d4 = f;
            Double.isNaN(d4);
            double d5 = f2;
            Double.isNaN(d5);
            double d6 = f5;
            Double.isNaN(d6);
            d4 = (d4 * d2 + d5 * d3) / d6;
            double d7 = -f;
            Double.isNaN(d7);
            d6 = f2;
            Double.isNaN(d6);
            d5 = f6;
            Double.isNaN(d5);
            d7 = (d7 * d3 + d6 * d2) / d5;
            d5 = f3;
            Double.isNaN(d5);
            d6 = f4;
            Double.isNaN(d6);
            double d8 = f5;
            Double.isNaN(d8);
            d8 = (d5 * d2 + d6 * d3) / d8;
            d5 = -f3;
            Double.isNaN(d5);
            double d9 = f4;
            Double.isNaN(d9);
            d6 = f6;
            Double.isNaN(d6);
            d9 = (d5 * d3 + d9 * d2) / d6;
            double d10 = d4 - d8;
            double d11 = d7 - d9;
            d5 = (d4 + d8) / 2.0;
            d6 = (d7 + d9) / 2.0;
            double d12 = d10 * d10 + d11 * d11;
            if (d12 == 0.0) {
                Log.w((String)PathParser.LOGTAG, (String)" Points are coincident");
                return;
            }
            double d13 = 1.0 / d12 - 0.25;
            if (d13 < 0.0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Points are too far apart ");
                stringBuilder.append(d12);
                Log.w((String)PathParser.LOGTAG, (String)stringBuilder.toString());
                float f8 = (float)(Math.sqrt(d12) / 1.99999);
                PathDataNode.drawArc(path, f, f2, f3, f4, f5 * f8, f6 * f8, f7, bl, bl2);
                return;
            }
            d12 = Math.sqrt(d13);
            d10 = d12 * d10;
            d11 = d12 * d11;
            if (bl == bl2) {
                d5 -= d11;
                d6 += d10;
            } else {
                d5 += d11;
                d6 -= d10;
            }
            d11 = Math.atan2(d7 - d6, d4 - d5);
            d7 = Math.atan2(d9 - d6, d8 - d5) - d11;
            bl = d7 >= 0.0;
            d4 = d7;
            if (bl2 != bl) {
                d4 = d7 > 0.0 ? d7 - Math.PI * 2 : d7 + Math.PI * 2;
            }
            d7 = f5;
            Double.isNaN(d7);
            d5 *= d7;
            d7 = f6;
            Double.isNaN(d7);
            d6 = d7 * d6;
            PathDataNode.arcToBezier(path, d5 * d2 - d6 * d3, d5 * d3 + d6 * d2, f5, f6, f, f2, d, d11, d4);
        }

        public static void nodesToPath(PathDataNode[] pathDataNodeArray, Path path) {
            float[] fArray = new float[6];
            char c = 'm';
            for (int i = 0; i < pathDataNodeArray.length; ++i) {
                PathDataNode.addCommand(path, fArray, c, pathDataNodeArray[i].mType, pathDataNodeArray[i].mParams);
                c = pathDataNodeArray[i].mType;
            }
        }

        public void interpolatePathDataNode(PathDataNode pathDataNode, PathDataNode pathDataNode2, float f) {
            float[] fArray;
            this.mType = pathDataNode.mType;
            for (int i = 0; i < (fArray = pathDataNode.mParams).length; ++i) {
                this.mParams[i] = fArray[i] * (1.0f - f) + pathDataNode2.mParams[i] * f;
            }
        }
    }
}

