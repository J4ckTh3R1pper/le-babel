package dayp308.chatroom.security;

import dayp308.chatroom.repository.RedisCaptchaRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class CaptchaUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final RedisCaptchaRepository captchaRepository;

    private final String uuidParameter = "uuid";
    private final String captchaParameter = "captcha";

    @Autowired
    public CaptchaUsernamePasswordAuthenticationFilter(RedisCaptchaRepository redisCaptchaRepository, AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.captchaRepository = redisCaptchaRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if ( !request.getMethod().equals("POST") ) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String uuid = obtainUuid(request);
        String captcha = obtainCaptcha(request);
        if (captcha == null || captcha.isBlank() || uuid == null || uuid.isBlank()) {
            throw new AuthenticationServiceException("Captcha and UUID cannot be empty");
        }
        uuid = uuid.trim();
        captcha = captcha.trim();
        if ( !captchaRepository.checkCaptcha(uuid, captcha) )
            throw new AuthenticationServiceException("Invalid captcha, please refresh captcha and try again");
        String username = obtainUsername(request);
        username = (username != null) ? username.trim() : "";
        String password = obtainPassword(request);
        password = (password != null) ? password : "";
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,
                password);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }


    String obtainCaptcha(HttpServletRequest request) {
        return request.getParameter(this.captchaParameter);
    }

    String obtainUuid(HttpServletRequest request) {
        return request.getParameter(this.uuidParameter);
    }
}
