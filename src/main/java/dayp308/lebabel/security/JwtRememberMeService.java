package dayp308.lebabel.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.lebabel.Constants;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.Assert;

import java.util.Date;

public class JwtRememberMeService implements RememberMeServices, LogoutHandler, InitializingBean, MessageSourceAware {

    private final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Setter
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public static final String DEFAULT_PARAM_NAME = "remember-me";
    @Setter
    private String parameter = DEFAULT_PARAM_NAME;

    private final UserDetailsService userDetailsService;
    private final UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();
    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();


    public JwtRememberMeService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
        JwtToken token;
        try {
            token = resolveToken(request);
        } catch (JsonProcessingException e) {
            return null;
        }

        Date now = new Date();
        Claims accessClaims = JwtOperations.parse(token.getAccessToken());
        if (accessClaims == null) return null;

        boolean accessTokenExpired = now.after(accessClaims.getExpiration());

        Claims refreshClaims = JwtOperations.parse(token.getRefreshToken());
        boolean hasRefreshToken = refreshClaims != null
                && now.after(refreshClaims.getExpiration())
                && now.before(refreshClaims.getNotBefore());

        if ( accessTokenExpired ) {
            if ( hasRefreshToken ) {
                UserDetails accessSub = userDetailsService.loadUserByUsername(accessClaims.getSubject());
                UserDetails refreshSub = userDetailsService.loadUserByUsername(refreshClaims.getSubject());
                if ( !accessSub.equals(refreshSub) ) return null;
                userDetailsChecker.check(accessSub);
                addTokenToResponseHeader(request, response, accessSub);
            }
            else return null;
        }
        UserDetails user = userDetailsService.loadUserByUsername(accessClaims.getSubject());
        userDetailsChecker.check(user);
        return null;

    }

    private Authentication createSuccessAuthentication(HttpServletRequest req, UserDetails user) {
        RememberMeAuthenticationToken auth = new RememberMeAuthenticationToken(DEFAULT_PARAM_NAME, user,
                this.authoritiesMapper.mapAuthorities( user.getAuthorities() ) );
        auth.setDetails(authenticationDetailsSource.buildDetails(req));
        return auth;
    }

    private void addTokenToResponseHeader(HttpServletRequest req, HttpServletResponse resp, UserDetails user) {
        String newToken = JwtOperations.issueToken(user, requestedRememberMe(req, parameter)).toString();
        resp.addHeader(Constants.JWT_HEADER_NAME, Constants.TOKEN_PREFIX + newToken);
    }

    @Override
    public void loginFail(HttpServletRequest request, HttpServletResponse response) {
        this.logger.debug("Interactive login attempt was unsuccessful.");
    }

    @Override
    public void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        if ( !requestedRememberMe(request, parameter) ) {
            this.logger.debug("Remember-me login not requested.");
            return;
        }

        addTokenToResponseHeader(request, response, (UserDetails) successfulAuthentication.getPrincipal());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasLength(this.parameter, "parameter name cannot be empty or null");
        Assert.notNull(this.userDetailsService, "A UserDetailsService is required");
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        Assert.notNull(messageSource, "messageSource cannot be null");
        this.messages = new MessageSourceAccessor(messageSource);
    }

    private JwtToken resolveToken(HttpServletRequest request) throws JsonProcessingException {
        String token = request.getHeader(Constants.JWT_HEADER_NAME).replaceFirst(Constants.TOKEN_PREFIX, "");
        return objectMapper.readValue(token, JwtToken.class);
    }

    protected boolean requestedRememberMe(HttpServletRequest request, String parameter) {
        String paramValue = request.getParameter(parameter);
        if (paramValue != null) {
            if (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on")
                    || paramValue.equalsIgnoreCase("yes") || paramValue.equals("1")) {
                return true;
            }
        }
        this.logger.debug(
                LogMessage.format("Did not send '%s' header (principal did not set parameter '%s')",
                        Constants.JWT_HEADER_NAME, parameter));
        return false;

    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

    }
}
