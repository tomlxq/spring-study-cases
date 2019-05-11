package com.tom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@Slf4j
public class HomeController {
    @RequestMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return "Howdy " + name + ", have a good day";
    }

    @GetMapping("getIP")
    public String getIp(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String nginxIP = request.getHeader("X-Real_IP");
        String interfaceVer = request.getHeader("interface_version");
        String host = request.getHeader("host");
        return remoteAddr + "->" + nginxIP + "->" + interfaceVer + "->" + host;
    }

    @GetMapping("/hello")
    public Collection<String> sayHello() {
        return IntStream.range(0, 10)
                .mapToObj(i -> "Hello number " + i)
                .collect(Collectors.toList());
    }

}
