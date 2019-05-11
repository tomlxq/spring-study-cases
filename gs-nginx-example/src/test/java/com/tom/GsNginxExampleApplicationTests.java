package com.tom;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GsNginxExampleApplicationTests {

    @Test
    public void contextLoads() {
    }

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        // TODO 因为我们修改了 content-path 所以请求后面要带上
        this.base = new URL("http://localhost:" + port + "/getIP");
    }

    @Test
    public void getIP() throws Exception {
        ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
        //assertEquals(response.getBody(), "127.0.0.1->null->null->localhost:57952");
    }

}
