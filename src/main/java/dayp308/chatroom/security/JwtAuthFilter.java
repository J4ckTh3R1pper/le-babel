package dayp308.chatroom.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dayp308.chatroom.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();

    public JwtAuthFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    private final String parameter = Constants.REMEMBER_ME_PARAM;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        JwtToken token;
        try {
            token = resolveToken(request);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        Date now = new Date();
        Claims accessClaims = JwtOperations.parse(token.getAccessToken());
        if (accessClaims == null) { filterChain.doFilter(request, response); return; }

        boolean accessTokenExpired = now.after(accessClaims.getExpiration());

        Claims refreshClaims = JwtOperations.parse(token.getRefreshToken());
        boolean hasRefreshToken = refreshClaims != null
                && now.after(refreshClaims.getExpiration())
                && now.before(refreshClaims.getNotBefore());

        if ( accessTokenExpired ) {
            if ( hasRefreshToken ) {
                UserDetails accessSub = userDetailsService.loadUserByUsername(accessClaims.getSubject());
                UserDetails refreshSub = userDetailsService.loadUserByUsername(refreshClaims.getSubject());
                if ( !accessSub.equals(refreshSub) ) { filterChain.doFilter(request, response); return; }
                userDetailsChecker.check(accessSub);
                addTokenToResponseHeader(request, response, accessSub);
            }
            else {
                filterChain.doFilter(request, response); return;
            }
        }
        UserDetails user = userDetailsService.loadUserByUsername(accessClaims.getSubject());
        userDetailsChecker.check(user);

        OneTimeTokenAuthenticationToken authToken = new OneTimeTokenAuthenticationToken(user, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }

    protected void addTokenToResponseHeader(HttpServletRequest req, HttpServletResponse resp, UserDetails user) {
        String newToken = JwtOperations.issueToken(user, requestedRememberMe(req, parameter)).toString();
        resp.addHeader(Constants.JWT_HEADER_NAME, Constants.TOKEN_PREFIX + newToken);
    }

    private JwtToken resolveToken(HttpServletRequest request) throws JsonProcessingException {
        if (! request.getHeader(Constants.JWT_HEADER_NAME).startsWith(Constants.TOKEN_PREFIX))
            throw new JwtException("Invalid token prefix");
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
}
