package cn.fivestars.pagehelperdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class PageHelperDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PageHelperDemoApplication.class, args);
    }

}
