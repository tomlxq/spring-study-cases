
package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.alibaba.fastjson.JSON;
import com.example.domain.Person;

@SpringBootApplication
@PropertySources({@PropertySource(value = {"config.properties"}, encoding = "UTF8"),
    @PropertySource(value = {"config1.properties"}, encoding = "GBK")})

@ImportResource("abc.xml")
public class GsSpringCloudClientApplication implements CommandLineRunner {

    /**
     * @Autowired Person person;
     */
    private final Person person;

    @Autowired
    public GsSpringCloudClientApplication(Person person) {
        this.person = person;
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(GsSpringCloudClientApplication.class);
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(JSON.toJSONString(person, true));
    }

}
