package dayp308.chatroom.controller;

import dayp308.chatroom.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class TestController {

    @PostMapping("/api/omgwtfbbq")
    public ResponseEntity<String> testLogin(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        System.out.println(Arrays.toString(request.getCookies()));
        System.out.println(authentication.getPrincipal());
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PreAuthorize("hasMemberShip(#categoryId)")
    @PostMapping("/api/test_moderator")
    public ResponseEntity<String> testMembership(@RequestParam("categoryId") int categoryId, Authentication auth) {
        String response =
                ( (User) auth.getPrincipal() ).getUsername() + " has membership in category " + categoryId;
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
