package dayp308.lebabel.security;

import dayp308.lebabel.Constants;
import dayp308.lebabel.entity.user.User;
import dayp308.lebabel.repository.RedisCaptchaRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

import static dayp308.lebabel.security.CaptchaUsernamePasswordAuthenticationFilter.obtainUuid;

public class FormLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final String parameter = Constants.REMEMBER_ME_PARAM;
    private final RedisCaptchaRepository redisCaptchaRepository;

    public FormLoginAuthenticationSuccessHandler(RedisCaptchaRepository redisCaptchaRepository) {
        this.redisCaptchaRepository = redisCaptchaRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");

        addTokenToResponseHeader(request, response, (UserDetails) authentication.getPrincipal());
        redisCaptchaRepository.removeCaptcha(obtainUuid(request));
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("login successful, welcome, "+ ((User) authentication.getPrincipal()).getNickName());
    }

    protected void addTokenToResponseHeader(HttpServletRequest req, HttpServletResponse resp, UserDetails user) {
        String newToken = JwtOperations.issueToken(user, requestedRememberMe(req, parameter)).toString();
        resp.addHeader(Constants.JWT_HEADER_NAME, Constants.TOKEN_PREFIX + newToken);
    }

    protected boolean requestedRememberMe(HttpServletRequest request, String parameter) {
        String paramValue = request.getParameter(parameter);
        if (paramValue != null) {
            return paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on")
                    || paramValue.equalsIgnoreCase("yes") || paramValue.equals("1");
        }
        return false;
    }

}
