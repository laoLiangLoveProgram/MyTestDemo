package cn.fivestars.pagehelperdemo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "pagehelper")
public class PageHelperProperties {

    private Properties properties = new Properties();

    public Properties getProperties() {
        return properties;
    }

    public void setDialect(String dialectClassName) {
        System.out.println(dialectClassName);
        properties.setProperty("dialect", dialectClassName);
    }

    public void setHelperDialect(String helperDialectClassName) {
        System.out.println(helperDialectClassName);
        properties.setProperty("helperDialect", helperDialectClassName);
    }

}
