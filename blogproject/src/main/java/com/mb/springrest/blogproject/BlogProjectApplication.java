package com.mb.springrest.blogproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableSwagger2
public class BlogProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogProjectApplication.class, args);
	}

    @Bean
    public Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/**"))
                .apis(RequestHandlerSelectors.basePackage("com.mb.springrest.blogproject"))
                .build()
                .apiInfo(apiDetails());
    }

    public ApiInfo apiDetails() {
	    return new ApiInfo(
	            "Blog API",
                "API for operations on Blog",
                "1.0",
                "Free to use",
                new springfox.documentation.service.Contact("Sagar Kumar", "", "sagar.singh@mountblue.tech"),
                "API License",
                "",
                Collections.emptyList()
        );
    }
}
