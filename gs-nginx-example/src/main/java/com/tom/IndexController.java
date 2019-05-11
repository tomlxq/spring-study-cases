package com.tom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class IndexController {
    @RequestMapping("/")
    String home(ModelMap modal) {
        modal.addAttribute("title", "Dear Learner");
        modal.addAttribute("message", "Welcome to SpringBoot");
        return "hello";
    }
}
