package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


//public class SpringInternationalizationApplication {

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
@EnableAutoConfiguration
@ComponentScan
//@SpringBootApplication
@Controller
public class SpringInternationalizationApplication extends WebMvcConfigurerAdapter {
    Logger logger= LoggerFactory.getLogger(SpringInternationalizationApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SpringInternationalizationApplication.class, args);
    }
    @Bean
    public MessageSource messageSource() {
        logger.debug("@@@@ {} ","测试是不是加载了语言文件");

         ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        // messageSource.setBasename("locale/messages");
         messageSource.setBasenames("locale/messages","locale/validation");
         messageSource.setDefaultEncoding("UTF-8");

        logger.debug("{}", messageSource.getMessage("field3", new Object[]{"tom"}, Locale.US));
        logger.debug("{}", messageSource.getMessage("field4", new Object[]{"tom"}, Locale.CHINA));
        return messageSource;
    }
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * 默认页<br/>
     * @RequestMapping("/") 和 @RequestMapping 是有区别的
     * 如果不写参数，则为全局默认页，加入输入404页面，也会自动访问到这个页面。
     * 如果加了参数“/”，则只认为是根页面。
     * @return
     */
    @RequestMapping(value = {"/","/index"})
    public String index() {
        return "index";
    }
}
