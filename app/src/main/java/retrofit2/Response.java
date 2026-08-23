/*
 * Decompiled with CFR 0.152.
 */
package retrofit2;

import okhttp3.Headers;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class Response<T> {
    private final T body;
    private final ResponseBody errorBody;
    private final okhttp3.Response rawResponse;

    private Response(okhttp3.Response response, T t, ResponseBody responseBody) {
        this.rawResponse = response;
        this.body = t;
        this.errorBody = responseBody;
    }

    public static <T> Response<T> error(int n, ResponseBody object) {
        if (n >= 400) {
            return Response.error((ResponseBody)object, new Response.Builder().code(n).protocol(Protocol.HTTP_1_1).request(new Request.Builder().url("http://localhost/").build()).build());
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("code < 400: ");
        ((StringBuilder)object).append(n);
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    public static <T> Response<T> error(ResponseBody responseBody, okhttp3.Response response) {
        if (responseBody != null) {
            if (response != null) {
                if (!response.isSuccessful()) {
                    return new Response<Object>(response, null, responseBody);
                }
                throw new IllegalArgumentException("rawResponse should not be successful response");
            }
            throw new NullPointerException("rawResponse == null");
        }
        throw new NullPointerException("body == null");
    }

    public static <T> Response<T> success(T t) {
        return Response.success(t, new Response.Builder().code(200).message("OK").protocol(Protocol.HTTP_1_1).request(new Request.Builder().url("http://localhost/").build()).build());
    }

    public static <T> Response<T> success(T t, Headers headers) {
        if (headers != null) {
            return Response.success(t, new Response.Builder().code(200).message("OK").protocol(Protocol.HTTP_1_1).headers(headers).request(new Request.Builder().url("http://localhost/").build()).build());
        }
        throw new NullPointerException("headers == null");
    }

    public static <T> Response<T> success(T t, okhttp3.Response response) {
        if (response != null) {
            if (response.isSuccessful()) {
                return new Response<T>(response, t, null);
            }
            throw new IllegalArgumentException("rawResponse must be successful response");
        }
        throw new NullPointerException("rawResponse == null");
    }

    public T body() {
        return this.body;
    }

    public int code() {
        return this.rawResponse.code();
    }

    public ResponseBody errorBody() {
        return this.errorBody;
    }

    public Headers headers() {
        return this.rawResponse.headers();
    }

    public boolean isSuccessful() {
        return this.rawResponse.isSuccessful();
    }

    public String message() {
        return this.rawResponse.message();
    }

    public okhttp3.Response raw() {
        return this.rawResponse;
    }
}

