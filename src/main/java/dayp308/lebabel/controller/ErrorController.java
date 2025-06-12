package dayp308.lebabel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ErrorController {
    @RequestMapping("/error")
    public String requestMethodName() {
        return "error";
    }
    
}
