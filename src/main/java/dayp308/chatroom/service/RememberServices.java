package dayp308.chatroom.service;

import dayp308.chatroom.entity.PersistentToken;
import dayp308.chatroom.repository.RedisPersistentTokenRepository;
import dayp308.chatroom.security.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.*;
import dayp308.chatroom.entity.user.User;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;

public class RememberServices extends AbstractRememberMeServices {

    private final RedisPersistentTokenRepository tokenRepository;
    private final int tokenLength = 32;
    private final int serialLength = 24;
    private final SecureRandom random;
    private final int expirySeconds = TWO_WEEKS_S;

    private Log logger = LogFactory.getLog(getClass());

    public RememberServices(String key, UserDetailsServiceImpl userDetailsService, RedisPersistentTokenRepository tokenRepository) {
        super(key, userDetailsService);
        this.tokenRepository = tokenRepository;
        this.random = new SecureRandom();
    }

    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        long userId = ( (User) successfulAuthentication.getPrincipal() ).getId();
        PersistentToken newToken = new PersistentToken(userId, generateTokenData());
        String serial = generateSerialData();
        try {
            tokenRepository.createNewToken(serial, newToken, Instant.now().plusSeconds(expirySeconds));
            setCookie(new String[]{serial, newToken.getToken()}, expirySeconds, request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) throws RememberMeAuthenticationException, UsernameNotFoundException {
        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain " + 2 + " tokens, but contained '"
                    + Arrays.asList(cookieTokens) + "'");
        }
        String presentedSerial = cookieTokens[0];
        String presentedToken = cookieTokens[1];
        PersistentToken persistedToken = tokenRepository.getTokenForSerial(presentedSerial);

        if (persistedToken == null) {
            throw new RememberMeAuthenticationException("No persistent token found for serial id: " + presentedSerial);
        }
        if (!presentedToken.equals(persistedToken.getToken())) {
            // Token doesn't match series value. Delete all logins for this user and throw
            // an exception to warn them.
            this.tokenRepository.removeUserTokens(persistedToken.getUserId());
            throw new CookieTheftException(this.messages.getMessage(
                    "PersistentTokenBasedRememberMeServices.cookieStolen",
                    "Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack."));
        }
        String newToken = generateTokenData();
        Instant expiryAt = Instant.now().plusSeconds(expirySeconds);
        try {
            this.tokenRepository.updateToken(presentedSerial, newToken, expiryAt);
            setCookie(new String[]{presentedSerial, newToken}, expirySeconds, request, response);
        } catch (Exception ex) {
            this.logger.error("Failed to update token: ", ex);
            throw new RememberMeAuthenticationException("Autologin failed due to data access problem");
        }

        this.logger.info("Remember-me login has been authenticated, refreshing token");


        return ( (UserDetailsServiceImpl) this.getUserDetailsService()).loadUserById(persistedToken.getUserId());
    }

    protected String generateTokenData() {
        byte[] newToken = new byte[this.tokenLength];
        this.random.nextBytes(newToken);
        return new String(Base64.getEncoder().encode(newToken));
    }
    protected String generateSerialData() {
        byte[] newSeries = new byte[this.serialLength];
        this.random.nextBytes(newSeries);
        return new String(Base64.getEncoder().encode(newSeries));
    }

}
