package com.tom.demo.web;

import com.tom.demo.service.AccountService;
import com.tom.framework.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/web")
public class HomeController {
    @Autowire
    private AccountService accountService;

    @RequestMapping("/hello")
    @ResponseBody
    public void hello(HttpServletRequest req, HttpServletResponse res, @RequestPara("name") String name) {

    }
}
