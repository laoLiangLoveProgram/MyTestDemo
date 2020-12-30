package cn.fivestars.pagehelperdemo.configuration;

import com.github.pagehelper.PageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class PageHelperConfig {

    @Resource
    PageHelperProperties properties;


    @Bean
    public PageInterceptor pageInterceptor() {
        System.out.println("pageHelper");
        PageInterceptor pageInterceptor = new PageInterceptor();
        pageInterceptor.setProperties(properties.getProperties());
        return pageInterceptor;
    }

}
