package network;

import model.TokenStorage;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AuthInterceptor implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!requiresAuthHandling(request)) {
            return chain.proceed(request);
        }

        Request.Builder requestBuilder = request.newBuilder()
                .removeHeader("Authorization");

        String authorization = TokenStorage.authorizationHeader();
        if (authorization != null) {
            requestBuilder.addHeader("Authorization", authorization);
        }

        Response response = chain.proceed(requestBuilder.build());

        if (response.code() == 498) {
            Request retry = AuthAuthenticator.authenticate(response);
            if (retry != null) {
                response.close();
                Response retryResponse = chain.proceed(retry);
                if (retryResponse.isSuccessful()) {
                    log.info("Token auto-refreshed after 498 — {} {} → HTTP {}",
                            request.method(), request.url(), retryResponse.code());
                } else {
                    log.warn("Token auto-refresh failed after 498 — {} {} → HTTP {}",
                            request.method(), request.url(), retryResponse.code());
                }
                return retryResponse;
            }
            log.warn("Token refresh rejected — {} {} triggered /refresh but refresh was denied",
                    request.method(), request.url());
        }

        return response;
    }

    private boolean requiresAuthHandling(Request request) {
        String path = request.url().encodedPath();
        return !"/login".equals(path)
                && !"/register".equals(path)
                && !"/refresh".equals(path);
    }
}
