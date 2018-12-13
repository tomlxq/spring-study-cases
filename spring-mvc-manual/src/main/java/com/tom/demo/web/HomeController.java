package com.tom.demo.web;

import com.google.common.collect.Maps;
import com.tom.demo.service.AccountService;
import com.tom.framework.annotation.Autowire;
import com.tom.framework.annotation.Controller;
import com.tom.framework.annotation.RequestMapping;
import com.tom.framework.annotation.RequestPara;
import com.tom.framework.web.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/web")
public class HomeController {
    @Autowire
    private AccountService accountService;

    @RequestMapping("/hello/*")
    //@ResponseBody
    public ModelAndView hello(HttpServletRequest req, HttpServletResponse res, @RequestPara("name") String name,
                              @RequestPara(value = "address", required = false) String addr, @RequestPara("age") int age) {

        Map<String, Object> model = Maps.newHashMap();
        model.put("name", name);
        model.put("address", addr);
        model.put("age", age);
        return new ModelAndView("hello.ftl", model);
    }
}
