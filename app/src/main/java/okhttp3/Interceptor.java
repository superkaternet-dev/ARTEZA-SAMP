/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.io.IOException;
import okhttp3.Connection;
import okhttp3.Request;
import okhttp3.Response;

public interface Interceptor {
    public Response intercept(Chain var1) throws IOException;

    public static interface Chain {
        public Connection connection();

        public Response proceed(Request var1) throws IOException;

        public Request request();
    }
}

