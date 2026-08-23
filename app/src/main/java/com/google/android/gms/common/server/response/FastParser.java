/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.server.response;

import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.android.gms.common.server.response.zaa;
import com.google.android.gms.common.server.response.zab;
import com.google.android.gms.common.server.response.zac;
import com.google.android.gms.common.server.response.zad;
import com.google.android.gms.common.server.response.zae;
import com.google.android.gms.common.server.response.zaf;
import com.google.android.gms.common.server.response.zag;
import com.google.android.gms.common.server.response.zah;
import com.google.android.gms.common.server.response.zai;
import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.common.util.JsonUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class FastParser<T extends FastJsonResponse> {
    private static final char[] zaa = new char[]{'u', 'l', 'l'};
    private static final char[] zab = new char[]{'r', 'u', 'e'};
    private static final char[] zac = new char[]{'r', 'u', 'e', '\"'};
    private static final char[] zad = new char[]{'a', 'l', 's', 'e'};
    private static final char[] zae = new char[]{'a', 'l', 's', 'e', '\"'};
    private static final char[] zaf = new char[]{'\n'};
    private static final zai<Integer> zag = new zaa();
    private static final zai<Long> zah = new zab();
    private static final zai<Float> zai = new zac();
    private static final zai<Double> zaj = new zad();
    private static final zai<Boolean> zak = new zae();
    private static final zai<String> zal = new zaf();
    private static final zai<BigInteger> zam = new zag();
    private static final zai<BigDecimal> zan = new zah();
    private final char[] zao = new char[1];
    private final char[] zap = new char[32];
    private final char[] zaq = new char[1024];
    private final StringBuilder zar = new StringBuilder(32);
    private final StringBuilder zas = new StringBuilder(1024);
    private final Stack<Integer> zat = new Stack();

    private static final String zaA(BufferedReader object, char[] cArray, StringBuilder stringBuilder, char[] cArray2) throws ParseException, IOException {
        int n;
        stringBuilder.setLength(0);
        ((BufferedReader)object).mark(cArray.length);
        boolean bl = false;
        boolean bl2 = false;
        while ((n = ((Reader)object).read(cArray)) != -1) {
            for (int i = 0; i < n; ++i) {
                char c = cArray[i];
                if (Character.isISOControl(c)) {
                    if (cArray2 != null) {
                        for (int j = 0; j <= 0; ++j) {
                            if (cArray2[j] != c) {
                                continue;
                            }
                            break;
                        }
                    } else {
                        throw new ParseException("Unexpected control character while reading string");
                    }
                }
                if (c == '\"') {
                    if (!bl2) {
                        stringBuilder.append(cArray, 0, i);
                        ((BufferedReader)object).reset();
                        ((BufferedReader)object).skip(i + 1);
                        if (bl) {
                            return JsonUtils.unescapeString(stringBuilder.toString());
                        }
                        return stringBuilder.toString();
                    }
                } else if (c == '\\') {
                    bl2 ^= true;
                    bl = true;
                    continue;
                }
                bl2 = false;
            }
            stringBuilder.append(cArray, 0, n);
            ((BufferedReader)object).mark(cArray.length);
        }
        object = new ParseException("Unexpected EOF while parsing string");
        throw object;
    }

    static /* bridge */ /* synthetic */ double zaa(FastParser fastParser, BufferedReader bufferedReader) {
        return fastParser.zaj(bufferedReader);
    }

    static /* bridge */ /* synthetic */ float zab(FastParser fastParser, BufferedReader bufferedReader) {
        return fastParser.zak(bufferedReader);
    }

    static /* bridge */ /* synthetic */ int zac(FastParser fastParser, BufferedReader bufferedReader) {
        return fastParser.zal(bufferedReader);
    }

    static /* bridge */ /* synthetic */ long zad(FastParser fastParser, BufferedReader bufferedReader) {
        return fastParser.zan(bufferedReader);
    }

    static /* bridge */ /* synthetic */ String zae(FastParser fastParser, BufferedReader bufferedReader) {
        return fastParser.zao(bufferedReader);
    }

    static /* bridge */ /* synthetic */ BigDecimal zaf(FastParser fastParser, BufferedReader bufferedReader) {
        return fastParser.zas(bufferedReader);
    }

    static /* bridge */ /* synthetic */ BigInteger zag(FastParser fastParser, BufferedReader bufferedReader) {
        return fastParser.zat(bufferedReader);
    }

    static /* bridge */ /* synthetic */ boolean zah(FastParser fastParser, BufferedReader bufferedReader, boolean bl) {
        return fastParser.zay(bufferedReader, false);
    }

    private final char zai(BufferedReader bufferedReader) throws ParseException, IOException {
        if (bufferedReader.read(this.zao) != -1) {
            while (Character.isWhitespace(this.zao[0])) {
                if (bufferedReader.read(this.zao) != -1) continue;
                return '\u0000';
            }
            return this.zao[0];
        }
        return '\u0000';
    }

    private final double zaj(BufferedReader bufferedReader) throws ParseException, IOException {
        int n = this.zam(bufferedReader, this.zaq);
        if (n == 0) {
            return 0.0;
        }
        return Double.parseDouble(new String(this.zaq, 0, n));
    }

    private final float zak(BufferedReader bufferedReader) throws ParseException, IOException {
        int n = this.zam(bufferedReader, this.zaq);
        if (n == 0) {
            return 0.0f;
        }
        return Float.parseFloat(new String(this.zaq, 0, n));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final int zal(BufferedReader object) throws ParseException, IOException {
        int n = this.zam((BufferedReader)object, this.zaq);
        if (n == 0) {
            return 0;
        }
        object = this.zaq;
        if (n > 0) {
            int n2;
            Object object2 = object[0];
            int n3 = object2 == 45 ? Integer.MIN_VALUE : -2147483647;
            int n4 = object2 == 45 ? 1 : 0;
            if (n4 < n) {
                n2 = n4 + 1;
                object2 = Character.digit((char)object[n4], 10);
                if (object2 < 0) throw new ParseException("Unexpected non-digit character");
                object2 = -object2;
            } else {
                n2 = n4;
                object2 = false;
            }
            while (n2 < n) {
                int n5 = Character.digit((char)object[n2], 10);
                if (n5 < 0) throw new ParseException("Unexpected non-digit character");
                if (object2 < -214748364) throw new ParseException("Number too large");
                if ((object2 *= 10) < n3 + n5) throw new ParseException("Number too large");
                object2 -= n5;
                ++n2;
            }
            if (n4 == 0) return (int)(-object2);
            if (n2 <= 1) throw new ParseException("No digits to parse");
            return (int)object2;
        }
        object = new ParseException("No number to parse");
        throw object;
    }

    /*
     * Enabled aggressive block sorting
     */
    private final int zam(BufferedReader object, char[] cArray) throws ParseException, IOException {
        int n;
        block10: {
            char c = this.zai((BufferedReader)object);
            if (c == '\u0000') {
                object = new ParseException("Unexpected EOF");
                throw object;
            }
            if (c == ',') throw new ParseException("Missing value");
            if (c == 'n') {
                this.zax((BufferedReader)object, zaa);
                return 0;
            }
            ((BufferedReader)object).mark(1024);
            if (c == '\"') {
                int n2 = 0;
                boolean bl = false;
                while (true) {
                    n = n2;
                    if (n2 < 1024) {
                        n = n2;
                        if (((BufferedReader)object).read(cArray, n2, 1) != -1) {
                            c = cArray[n2];
                            if (Character.isISOControl(c)) throw new ParseException("Unexpected control character while reading string");
                            if (c == '\"') {
                                if (!bl) {
                                    ((BufferedReader)object).reset();
                                    ((BufferedReader)object).skip(n2 + 1);
                                    return n2;
                                }
                                bl = false;
                            } else {
                                bl = c == '\\' ? (bl ^= true) : false;
                            }
                            ++n2;
                            continue;
                        }
                    }
                    break block10;
                    break;
                }
            }
            cArray[0] = c;
            int n3 = 1;
            while (true) {
                n = n3;
                if (n3 >= 1024) break block10;
                n = n3;
                if (((BufferedReader)object).read(cArray, n3, 1) == -1) break block10;
                c = cArray[n3];
                if (c == '}' || c == ',' || Character.isWhitespace(c) || cArray[n3] == ']') break;
                ++n3;
            }
            ((BufferedReader)object).reset();
            ((BufferedReader)object).skip(n3 - 1);
            cArray[n3] = '\u0000';
            return n3;
        }
        if (n != 1024) throw new ParseException("Unexpected EOF");
        throw new ParseException("Absurdly long value");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final long zan(BufferedReader object) throws ParseException, IOException {
        int n = this.zam((BufferedReader)object, this.zaq);
        if (n == 0) {
            return 0L;
        }
        object = this.zaq;
        if (n > 0) {
            long l;
            int n2;
            int n3 = 0;
            Object object2 = object[0];
            long l2 = object2 == 45 ? Long.MIN_VALUE : -9223372036854775807L;
            if (object2 == 45) {
                n3 = 1;
            }
            if (n3 < n) {
                object2 = n3 + 1;
                n2 = Character.digit((char)object[n3], 10);
                if (n2 < 0) throw new ParseException("Unexpected non-digit character");
                l = -n2;
            } else {
                l = 0L;
                object2 = n3;
            }
            while (object2 < n) {
                n2 = Character.digit((char)object[object2], 10);
                if (n2 < 0) throw new ParseException("Unexpected non-digit character");
                if (l < -922337203685477580L) throw new ParseException("Number too large");
                long l3 = l * 10L;
                if (l3 < l2 + (l = (long)n2)) throw new ParseException("Number too large");
                l = l3 - l;
                ++object2;
            }
            if (n3 == 0) return -l;
            if (object2 <= true) throw new ParseException("No digits to parse");
            return l;
        }
        object = new ParseException("No number to parse");
        throw object;
    }

    private final String zao(BufferedReader bufferedReader) throws ParseException, IOException {
        return this.zap(bufferedReader, this.zap, this.zar, null);
    }

    private final String zap(BufferedReader bufferedReader, char[] cArray, StringBuilder stringBuilder, char[] cArray2) throws ParseException, IOException {
        switch (this.zai(bufferedReader)) {
            default: {
                throw new ParseException("Expected string");
            }
            case 'n': {
                this.zax(bufferedReader, zaa);
                return null;
            }
            case '\"': 
        }
        return FastParser.zaA(bufferedReader, cArray, stringBuilder, cArray2);
    }

    private final String zaq(BufferedReader object) throws ParseException, IOException {
        this.zat.push(2);
        char c = this.zai((BufferedReader)object);
        switch (c) {
            default: {
                object = new StringBuilder(19);
                ((StringBuilder)object).append("Unexpected token: ");
                ((StringBuilder)object).append(c);
                throw new ParseException(((StringBuilder)object).toString());
            }
            case '}': {
                this.zaw(2);
                return null;
            }
            case ']': {
                this.zaw(2);
                this.zaw(1);
                this.zaw(5);
                return null;
            }
            case '\"': 
        }
        this.zat.push(3);
        String string2 = FastParser.zaA((BufferedReader)object, this.zap, this.zar, null);
        this.zaw(3);
        if (this.zai((BufferedReader)object) == ':') {
            return string2;
        }
        throw new ParseException("Expected key/value separator");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final String zar(BufferedReader object) throws ParseException, IOException {
        int n;
        ((BufferedReader)object).mark(1024);
        int n2 = this.zai((BufferedReader)object);
        int n3 = 1;
        block0 : switch (n2) {
            default: {
                ((BufferedReader)object).reset();
                this.zam((BufferedReader)object, this.zaq);
                break;
            }
            case 123: {
                this.zat.push(1);
                ((BufferedReader)object).mark(32);
                n = this.zai((BufferedReader)object);
                if (n == 125) {
                    this.zaw(1);
                    break;
                }
                if (n == 34) {
                    ((BufferedReader)object).reset();
                    this.zaq((BufferedReader)object);
                    while (this.zar((BufferedReader)object) != null) {
                    }
                    this.zaw(1);
                    break;
                }
                object = new StringBuilder(18);
                ((StringBuilder)object).append("Unexpected token ");
                ((StringBuilder)object).append((char)n);
                throw new ParseException(((StringBuilder)object).toString());
            }
            case 91: {
                this.zat.push(5);
                ((BufferedReader)object).mark(32);
                if (this.zai((BufferedReader)object) == ']') {
                    this.zaw(5);
                    break;
                }
                ((BufferedReader)object).reset();
                int n4 = 0;
                n2 = 0;
                while (n3 > 0) {
                    n = this.zai((BufferedReader)object);
                    if (n == 0) throw new ParseException("Unexpected EOF while parsing array");
                    if (Character.isISOControl((char)n)) throw new ParseException("Unexpected control character while reading array");
                    int n5 = n4;
                    int n6 = n;
                    if (n == 34) {
                        n5 = n4;
                        if (n2 == 0) {
                            n5 = n4 ^ 1;
                        }
                        n6 = 34;
                    }
                    n4 = n3;
                    int n7 = n6;
                    if (n6 == 91) {
                        n4 = n3;
                        if (n5 == 0) {
                            n4 = n3 + 1;
                        }
                        n7 = 91;
                    }
                    n3 = n4;
                    if (n7 == 93) {
                        n3 = n4;
                        if (n5 == 0) {
                            n3 = n4 - 1;
                        }
                    }
                    if (n7 == 92 && n5 != 0) {
                        n2 ^= 1;
                        n4 = n5;
                        continue;
                    }
                    n2 = 0;
                    n4 = n5;
                }
                this.zaw(5);
                break;
            }
            case 44: {
                throw new ParseException("Missing value");
            }
            case 34: {
                if (((Reader)object).read(this.zao) == -1) throw new ParseException("Unexpected EOF while parsing string");
                n2 = this.zao[0];
                n3 = 0;
                while (true) {
                    if (n2 == 34) {
                        if (n3 == 0) break block0;
                        n2 = 34;
                        n3 = 1;
                    }
                    n3 = n2 == 92 ? (n3 ^= 1) : 0;
                    if (((Reader)object).read(this.zao) == -1) throw new ParseException("Unexpected EOF while parsing string");
                    n = this.zao[0];
                    if (Character.isISOControl((char)n)) throw new ParseException("Unexpected control character while reading string");
                    n2 = n;
                }
            }
        }
        n = this.zai((BufferedReader)object);
        switch (n) {
            default: {
                object = new StringBuilder(18);
                ((StringBuilder)object).append("Unexpected token ");
                ((StringBuilder)object).append((char)n);
                throw new ParseException(((StringBuilder)object).toString());
            }
            case 125: {
                this.zaw(2);
                return null;
            }
            case 44: 
        }
        this.zaw(2);
        return this.zaq((BufferedReader)object);
    }

    private final BigDecimal zas(BufferedReader bufferedReader) throws ParseException, IOException {
        int n = this.zam(bufferedReader, this.zaq);
        if (n == 0) {
            return null;
        }
        return new BigDecimal(new String(this.zaq, 0, n));
    }

    private final BigInteger zat(BufferedReader bufferedReader) throws ParseException, IOException {
        int n = this.zam(bufferedReader, this.zaq);
        if (n == 0) {
            return null;
        }
        return new BigInteger(new String(this.zaq, 0, n));
    }

    private final <O> ArrayList<O> zau(BufferedReader object, zai<O> zai2) throws ParseException, IOException {
        char c = this.zai((BufferedReader)object);
        if (c == 'n') {
            this.zax((BufferedReader)object, zaa);
            return null;
        }
        if (c == '[') {
            this.zat.push(5);
            ArrayList<O> arrayList = new ArrayList<O>();
            block5: while (true) {
                ((BufferedReader)object).mark(1024);
                switch (this.zai((BufferedReader)object)) {
                    case ',': {
                        continue block5;
                    }
                    default: {
                        ((BufferedReader)object).reset();
                        arrayList.add(zai2.zaa(this, (BufferedReader)object));
                        continue block5;
                    }
                    case ']': {
                        this.zaw(5);
                        return arrayList;
                    }
                    case '\u0000': 
                }
                break;
            }
            throw new ParseException("Unexpected EOF");
        }
        object = new ParseException("Expected start of array");
        throw object;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final <T extends FastJsonResponse> ArrayList<T> zav(BufferedReader object, FastJsonResponse.Field<?, ?> field) throws ParseException, IOException {
        ArrayList<FastJsonResponse> arrayList = new ArrayList<FastJsonResponse>();
        char c = this.zai((BufferedReader)object);
        switch (c) {
            default: {
                object = new StringBuilder(19);
                ((StringBuilder)object).append("Unexpected token: ");
                ((StringBuilder)object).append(c);
                throw new ParseException(((StringBuilder)object).toString());
            }
            case '{': {
                this.zat.push(1);
                while (true) {
                    try {
                        FastJsonResponse fastJsonResponse = field.zad();
                        if (!this.zaz((BufferedReader)object, fastJsonResponse)) break;
                        arrayList.add(fastJsonResponse);
                        c = this.zai((BufferedReader)object);
                    }
                    catch (IllegalAccessException illegalAccessException) {
                        throw new ParseException("Error instantiating inner object", illegalAccessException);
                    }
                    catch (InstantiationException instantiationException) {
                        throw new ParseException("Error instantiating inner object", instantiationException);
                    }
                    switch (c) {
                        default: {
                            object = new StringBuilder(19);
                            ((StringBuilder)object).append("Unexpected token: ");
                            ((StringBuilder)object).append(c);
                            throw new ParseException(((StringBuilder)object).toString());
                        }
                        case ']': {
                            this.zaw(5);
                            return arrayList;
                        }
                        case ',': 
                    }
                    if (this.zai((BufferedReader)object) != '{') {
                        throw new ParseException("Expected start of next object in array");
                    }
                    this.zat.push(1);
                }
                return arrayList;
            }
            case 'n': {
                this.zax((BufferedReader)object, zaa);
                this.zaw(5);
                return null;
            }
            case ']': 
        }
        this.zaw(5);
        return arrayList;
    }

    private final void zaw(int n) throws ParseException {
        if (!this.zat.isEmpty()) {
            int n2 = this.zat.pop();
            if (n2 == n) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder(46);
            stringBuilder.append("Expected state ");
            stringBuilder.append(n);
            stringBuilder.append(" but had ");
            stringBuilder.append(n2);
            throw new ParseException(stringBuilder.toString());
        }
        StringBuilder stringBuilder = new StringBuilder(46);
        stringBuilder.append("Expected state ");
        stringBuilder.append(n);
        stringBuilder.append(" but had empty stack");
        throw new ParseException(stringBuilder.toString());
    }

    private final void zax(BufferedReader bufferedReader, char[] cArray) throws ParseException, IOException {
        int n;
        int n2;
        for (int i = 0; i < (n = cArray.length); i += n2) {
            n2 = bufferedReader.read(this.zap, 0, n - i);
            if (n2 != -1) {
                for (n = 0; n < n2; ++n) {
                    if (cArray[n + i] == this.zap[n]) {
                        continue;
                    }
                    throw new ParseException("Unexpected character");
                }
                continue;
            }
            throw new ParseException("Unexpected EOF");
        }
    }

    private final boolean zay(BufferedReader object, boolean bl) throws ParseException, IOException {
        char c = this.zai((BufferedReader)object);
        switch (c) {
            default: {
                object = new StringBuilder(19);
                ((StringBuilder)object).append("Unexpected token: ");
                ((StringBuilder)object).append(c);
                throw new ParseException(((StringBuilder)object).toString());
            }
            case 't': {
                char[] cArray = bl ? zac : zab;
                this.zax((BufferedReader)object, cArray);
                return true;
            }
            case 'n': {
                this.zax((BufferedReader)object, zaa);
                return false;
            }
            case 'f': {
                char[] cArray = bl ? zae : zad;
                this.zax((BufferedReader)object, cArray);
                return false;
            }
            case '\"': 
        }
        if (!bl) {
            return this.zay((BufferedReader)object, true);
        }
        throw new ParseException("No boolean value found in string");
    }

    /*
     * Unable to fully structure code
     */
    private final boolean zaz(BufferedReader var1_1, FastJsonResponse var2_4) throws ParseException, IOException {
        block47: {
            var7_5 = var2_4.getFieldMappings();
            var5_6 = this.zaq((BufferedReader)var1_1);
            var6_7 = 1;
            if (var5_6 == null) break block47;
            block26: while (var5_6 != null) {
                var8_10 = var7_5.get(var5_6);
                if (var8_10 == null) {
                    var5_6 = this.zar((BufferedReader)var1_1);
                    continue;
                }
                this.zat.push(4);
                var4_9 = var8_10.zaa;
                block1 : switch (var4_9) {
                    default: {
                        var1_1 = new StringBuilder(30);
                        var1_1.append("Invalid field type ");
                        var1_1.append(var4_9);
                        throw new ParseException(var1_1.toString());
                    }
                    case 11: {
                        if (var8_10.zab) {
                            var4_9 = this.zai((BufferedReader)var1_1);
                            if (var4_9 == 110) {
                                this.zax((BufferedReader)var1_1, FastParser.zaa);
                                var2_4.addConcreteTypeArrayInternal(var8_10, var8_10.zae, null);
                                break;
                            }
                            this.zat.push(5);
                            if (var4_9 == 91) {
                                var2_4.addConcreteTypeArrayInternal(var8_10, var8_10.zae, this.zav((BufferedReader)var1_1, var8_10));
                                break;
                            }
                            throw new ParseException("Expected array start");
                        }
                        var4_9 = this.zai((BufferedReader)var1_1);
                        if (var4_9 == 110) {
                            this.zax((BufferedReader)var1_1, FastParser.zaa);
                            var2_4.addConcreteTypeInternal(var8_10, var8_10.zae, null);
                            break;
                        }
                        this.zat.push(var6_7);
                        if (var4_9 == 123) {
                            try {
                                var5_6 = var8_10.zad();
                                this.zaz((BufferedReader)var1_1, (FastJsonResponse)var5_6);
                                var2_4.addConcreteTypeInternal(var8_10, var8_10.zae, var5_6);
                                break;
                            }
                            catch (IllegalAccessException var1_2) {
                                throw new ParseException("Error instantiating inner object", var1_2);
                            }
                            catch (InstantiationException var1_3) {
                                throw new ParseException("Error instantiating inner object", var1_3);
                            }
                        }
                        throw new ParseException("Expected start of object");
                    }
                    case 10: {
                        var4_9 = this.zai((BufferedReader)var1_1);
                        if (var4_9 != 110) ** GOTO lbl59
                        this.zax((BufferedReader)var1_1, FastParser.zaa);
                        var5_6 = null;
                        ** GOTO lbl87
lbl59:
                        // 1 sources

                        if (var4_9 != 123) ** GOTO lbl97
                        this.zat.push(var6_7);
                        var5_6 = new HashMap<K, V>();
                        block27: while (true) {
                            switch (this.zai((BufferedReader)var1_1)) {
                                default: {
                                    continue block27;
                                }
                                case '}': {
                                    this.zaw(1);
                                    ** GOTO lbl87
                                }
                                case '\"': {
                                    var9_11 = FastParser.zaA((BufferedReader)var1_1, this.zap, this.zar, null);
                                    if (this.zai((BufferedReader)var1_1) != ':') {
                                        var1_1 = String.valueOf(var9_11);
                                        var1_1 = var1_1.length() != 0 ? "No map value found for key ".concat((String)var1_1) : new String("No map value found for key ");
                                        throw new ParseException((String)var1_1);
                                    }
                                    if (this.zai((BufferedReader)var1_1) != '\"') {
                                        var1_1 = String.valueOf(var9_11);
                                        var1_1 = var1_1.length() != 0 ? "Expected String value for key ".concat((String)var1_1) : new String("Expected String value for key ");
                                        throw new ParseException((String)var1_1);
                                    }
                                    var5_6.put(var9_11, FastParser.zaA((BufferedReader)var1_1, this.zap, this.zar, null));
                                    var3_8 = this.zai((BufferedReader)var1_1);
                                    if (var3_8 != ',') ** break;
                                    continue block27;
                                    if (var3_8 != '}') ** GOTO lbl89
                                    this.zaw(1);
lbl87:
                                    // 3 sources

                                    var2_4.zaB(var8_10, (Map<String, String>)var5_6);
                                    break block1;
lbl89:
                                    // 1 sources

                                    var1_1 = new StringBuilder(48);
                                    var1_1.append("Unexpected character while parsing string map: ");
                                    var1_1.append(var3_8);
                                    throw new ParseException(var1_1.toString());
                                }
                                case '\u0000': 
                            }
                            break;
                        }
                        throw new ParseException("Unexpected EOF");
lbl97:
                        // 1 sources

                        throw new ParseException("Expected start of a map object");
                    }
                    case 9: {
                        var2_4.zal(var8_10, Base64Utils.decodeUrlSafe(this.zap((BufferedReader)var1_1, this.zaq, this.zas, FastParser.zaf)));
                        break;
                    }
                    case 8: {
                        var2_4.zal(var8_10, Base64Utils.decode(this.zap((BufferedReader)var1_1, this.zaq, this.zas, FastParser.zaf)));
                        break;
                    }
                    case 7: {
                        if (var8_10.zab) {
                            var2_4.zaC(var8_10, this.zau((BufferedReader)var1_1, FastParser.zal));
                            break;
                        }
                        var2_4.zaA(var8_10, this.zao((BufferedReader)var1_1));
                        break;
                    }
                    case 6: {
                        if (var8_10.zab) {
                            var2_4.zaj(var8_10, this.zau((BufferedReader)var1_1, FastParser.zak));
                            break;
                        }
                        var2_4.zai(var8_10, this.zay((BufferedReader)var1_1, false));
                        break;
                    }
                    case 5: {
                        if (var8_10.zab) {
                            var2_4.zac(var8_10, this.zau((BufferedReader)var1_1, FastParser.zan));
                            break;
                        }
                        var2_4.zaa(var8_10, this.zas((BufferedReader)var1_1));
                        break;
                    }
                    case 4: {
                        if (var8_10.zab) {
                            var2_4.zao(var8_10, this.zau((BufferedReader)var1_1, FastParser.zaj));
                            break;
                        }
                        var2_4.zam(var8_10, this.zaj((BufferedReader)var1_1));
                        break;
                    }
                    case 3: {
                        if (var8_10.zab) {
                            var2_4.zas(var8_10, this.zau((BufferedReader)var1_1, FastParser.zai));
                            break;
                        }
                        var2_4.zaq(var8_10, this.zak((BufferedReader)var1_1));
                        break;
                    }
                    case 2: {
                        if (var8_10.zab) {
                            var2_4.zay(var8_10, this.zau((BufferedReader)var1_1, FastParser.zah));
                            break;
                        }
                        var2_4.zax(var8_10, this.zan((BufferedReader)var1_1));
                        break;
                    }
                    case 1: {
                        if (var8_10.zab) {
                            var2_4.zag(var8_10, this.zau((BufferedReader)var1_1, FastParser.zam));
                            break;
                        }
                        var2_4.zae(var8_10, this.zat((BufferedReader)var1_1));
                        break;
                    }
                    case 0: {
                        if (var8_10.zab) {
                            var2_4.zav(var8_10, this.zau((BufferedReader)var1_1, FastParser.zag));
                            break;
                        }
                        var2_4.zau(var8_10, this.zal((BufferedReader)var1_1));
                    }
                }
                this.zaw(4);
                this.zaw(2);
                var3_8 = this.zai((BufferedReader)var1_1);
                switch (var3_8) {
                    default: {
                        var1_1 = new StringBuilder(55);
                        var1_1.append("Expected end of object or field separator, but found: ");
                        var1_1.append(var3_8);
                        throw new ParseException(var1_1.toString());
                    }
                    case '}': {
                        var5_6 = null;
                        continue block26;
                    }
                    case ',': 
                }
                var5_6 = this.zaq((BufferedReader)var1_1);
            }
            this.zaw(1);
            return true;
        }
        this.zaw(1);
        return false;
    }

    /*
     * Exception decompiling
     */
    public void parse(InputStream var1_1, T var2_4) throws ParseException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 4[TRYBLOCK] [17 : 273->285)] java.lang.Throwable
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.insertExceptionBlocks(Op02WithProcessedDataAndRefs.java:2283)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:415)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public static class ParseException
    extends Exception {
        public ParseException(String string2) {
            super(string2);
        }

        public ParseException(String string2, Throwable throwable) {
            super("Error instantiating inner object", throwable);
        }

        public ParseException(Throwable throwable) {
            super(throwable);
        }
    }
}

