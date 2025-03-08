package dayp308.chatroom.security;

import dayp308.chatroom.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class FormLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final String parameter = Constants.REMEMBER_ME_PARAM;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");

        addTokenToResponseHeader(request, response, (UserDetails) authentication.getPrincipal());
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("login successful, welcome, "+ authentication.getPrincipal());
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
