package network;

import dto.auth.AuthResponse;
import dto.auth.RefreshTokenRequest;
import model.TokenStorage;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;

import java.io.IOException;

public class AuthAuthenticator {

    private static final Logger log = LoggerFactory.getLogger(AuthAuthenticator.class);

    private AuthAuthenticator() {
    }

    public static Request authenticate(Response response) throws IOException {
        if (response.code() != 498) {
            return null;
        }

        synchronized (TokenStorage.class) {
            if (!TokenStorage.hasRefreshToken()) {
                log.warn("Token refresh aborted — no refresh token stored");
                TokenStorage.clear();
                return null;
            }

            String currentRefreshToken = TokenStorage.getRefreshToken();
            if (currentRefreshToken == null || currentRefreshToken.isBlank()) {
                log.warn("Token refresh aborted — stored refresh token is blank");
                TokenStorage.clear();
                return null;
            }

            try {
                Call<AuthResponse> refreshCall =
                        ApiClient.publicApi.refresh(new RefreshTokenRequest(currentRefreshToken));
                retrofit2.Response<AuthResponse> refreshResponse = refreshCall.execute();

                if (refreshResponse.isSuccessful() && refreshResponse.body() != null) {
                    TokenStorage.setTokens(refreshResponse.body());

                    String newAuthorization = TokenStorage.authorizationHeader();
                    if (newAuthorization == null) {
                        log.warn("Token refresh succeeded but new authorization header is null — clearing session");
                        TokenStorage.clear();
                        return null;
                    }

                    return response.request().newBuilder()
                            .removeHeader("Authorization")
                            .addHeader("Authorization", newAuthorization)
                            .build();
                }

                log.warn("Token refresh failed — /refresh returned HTTP {}",
                        refreshResponse.code());
                TokenStorage.clear();
                return null;
            } catch (IOException e) {
                log.error("Token refresh failed — network error: {}", e.getMessage(), e);
                TokenStorage.clear();
                return null;
            }
        }
    }
}
