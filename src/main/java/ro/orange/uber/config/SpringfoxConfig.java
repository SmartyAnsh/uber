package ro.orange.uber.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SpringfoxConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ro.orange.uber"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Uber Backend",
                "Uber's backend APIs",
                "1.0",
                "https://docs.google.com/document/d/1iqcuVCaO3yYcKfXe7Qxg_w6fzPDOGHFD/edit",
                new Contact("Anshul Bansal", null, "smartdiscover17@gmail.com"),
                "License", null, Collections.emptyList());
    }
}
