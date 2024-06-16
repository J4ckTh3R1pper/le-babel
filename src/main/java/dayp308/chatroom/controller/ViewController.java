package dayp308.chatroom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {
    @RequestMapping(value = "/login")
    public ModelAndView loginView() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/chat")
    public ModelAndView chatView() {
        return new ModelAndView("chat");
    }
}
