package com.example;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GsSpringCloudEurekaServerApplicationTests {

    @Test
    public void contextLoads() {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        System.out.println(restTemplate.getForObject("http://localhost:6060/env", Map.class));
        System.out.println(restTemplate.getForObject("http://localhost:6060/env", String.class));

    }

}
