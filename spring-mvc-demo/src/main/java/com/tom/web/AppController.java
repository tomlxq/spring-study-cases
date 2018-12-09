package com.tom.web;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/web")
public class AppController {
    @RequestMapping(value = "/")
    public String home(@ModelAttribute("model") ModelMap model) {

        model.addAttribute("MsTime", System.currentTimeMillis());
        model.addAttribute("auther", "罗小强");
        return "Home";

    }

    @RequestMapping("/hello/{id}/index")
    public ModelAndView getLogin(@PathVariable("id") String id, @RequestParam("name") String name,
                                 HttpServletRequest request, HttpServletResponse response) {
        System.out.println(id + ":" + name);
        Map map = Maps.newHashMap();
        map.put("id", id);
        map.put("name", name);
        map.put("friend", "张三");
        ModelAndView index = new ModelAndView("index", map);
        System.out.println(JSON.toJSONString(index));
        return index;
    }
}
