package network;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FailureLoggingInterceptor implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(FailureLoggingInterceptor.class);
    private static final int MAX_BODY_LENGTH = 4096;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startNanos = System.nanoTime();

        Response response;
        try {
            response = chain.proceed(request);
        } catch (IOException e) {
            log.error("Request failed — {} {} | error: {}",
                    request.method(), request.url(), e.getMessage(), e);
            throw e;
        }

        if (response.isSuccessful()) {
            String bodyPreview = bufferBodyPreview(response);
            response = rebuildBodyPreview(response, bodyPreview);
            log.info("{} {} → HTTP {} | time={}ms | size={}{}",
                    request.method(), request.url(),
                    response.code(),
                    durationMs(startNanos),
                    response.body() != null ? response.body().contentLength() : 0,
                    bodyPreview != null ? " | body: " + bodyPreview : "");
            return response;
        }

        String requestBody = readRequestBody(request);
        String responseBody = bufferResponseBody(response);
        response = rebuildResponse(response, responseBody);

        log.warn("Request failed — {} {} → HTTP {} | time={}ms{}\n" +
                         "  request headers: {}\n" +
                         "{}\n" +
                         "  response headers: {}\n" +
                         "  response body: {}",
                request.method(), request.url(),
                response.code(),
                durationMs(startNanos),
                requestBody != null ? "\n  request body: " + requestBody : "",
                headersString(request),
                requestBody != null ? "" : "",
                headersString(response),
                responseBody);

        return response;
    }

    private String bufferBodyPreview(Response response) throws IOException {
        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }

        BufferedSource source = body.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.getBuffer();
        if (buffer.size() == 0) {
            return null;
        }
        return truncate(buffer.clone().readString(StandardCharsets.UTF_8));
    }

    private Response rebuildBodyPreview(Response response, String bodyContent) {
        ResponseBody body = response.body();
        if (body == null) {
            return response;
        }

        return response.newBuilder()
                .body(ResponseBody.create(body.contentType(), bodyContent))
                .build();
    }

    private String readRequestBody(Request request) {
        RequestBody body = request.body();
        if (body == null) {
            return null;
        }

        try {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            return truncate(buffer.readString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            return "<unreadable>";
        }
    }

    private String bufferResponseBody(Response response) throws IOException {
        ResponseBody body = response.body();
        if (body == null) {
            return "<empty>";
        }

        BufferedSource source = body.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.getBuffer();
        return truncate(buffer.clone().readString(StandardCharsets.UTF_8));
    }

    private Response rebuildResponse(Response response, String bodyContent) {
        ResponseBody body = response.body();
        if (body == null) {
            return response;
        }

        return response.newBuilder()
                .body(ResponseBody.create(body.contentType(), bodyContent))
                .build();
    }

    private String truncate(String value) {
        if (value == null) {
            return null;
        }
        if (value.length() <= MAX_BODY_LENGTH) {
            return value;
        }
        return value.substring(0, MAX_BODY_LENGTH) + "... [truncated]";
    }

    private String headersString(Request request) {
        return request.headers().toString().replace("\n", "\n  ");
    }

    private String headersString(Response response) {
        return response.headers().toString().replace("\n", "\n  ");
    }

    private long durationMs(long startNanos) {
        return (System.nanoTime() - startNanos) / 1_000_000;
    }
}
