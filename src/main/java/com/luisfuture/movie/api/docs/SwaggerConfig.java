package com.luisfuture.movie.api.docs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by LuisFuture on 03/07/2016.
 * Project: MovieAPI
 */
@Configuration
@EnableSwagger2
@ComponentScan( "com.luisfuture.movie.api.controller" )
public class SwaggerConfig {

    public static final String DEFAULT_INCLUDE_PATTERNS = "/.*";


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERNS))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "Movie Api Rest Spring Boot",
                "This Api is a practical exercise",
                "1.0",
                "",
                new Contact("Luis Ramos", "", "luisfuture@gmail.com"),
                "",
                ""
        );
        return apiInfo;
    }
}
