package dayp308.chatroom.controller;

import dayp308.chatroom.entity.business.LoginForm;
import dayp308.chatroom.service.UserService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;

@RestController
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody LoginForm form,
                      HttpServletRequest req,
                      HttpServletResponse resp) {
        Authentication auth = UsernamePasswordAuthenticationToken
                .unauthenticated(form.loginName(), form.password());
        System.out.println("remember: " + form.remember());
        if ( authenticationManager.authenticate(auth).isAuthenticated() )
            return new ResponseEntity<>("successfully logged in", HttpStatus.OK);
        else return new ResponseEntity<>("login failed", HttpStatus.UNAUTHORIZED);
    }

}
