package com.tom.demo.web.admin;

import com.tom.demo.service.AccountService;
import com.tom.demo.service.CourseService;
import com.tom.framework.annotation.Autowire;
import com.tom.framework.annotation.Controller;

@Controller(value = "adminLogin")
public class LoginController {
    @Autowire
    private AccountService accountService;
    @Autowire(value = "classes")
    private CourseService courseService;
}
