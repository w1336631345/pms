package com.kry.pms.config;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import org.springframework.context.annotation.Bean;
@org.springframework.context.annotation.Configuration
public class SqlFreeMakerConfig {
    @Bean
    public Configuration configuration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        configuration.setTemplateLoader(templateLoader);
        configuration.setDefaultEncoding("UTF-8");
        return configuration;
    }
}
