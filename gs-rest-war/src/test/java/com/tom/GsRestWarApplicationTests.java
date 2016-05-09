package com.tom;

import com.tom.web.ServletInitializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServletInitializer.class)
@WebAppConfiguration
public class GsRestWarApplicationTests {

	@Test
	public void contextLoads() {
	}

}
