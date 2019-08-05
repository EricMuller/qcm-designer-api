package com.emu.apps.qcm.web.rest.config;

import com.emu.apps.qcm.web.rest.QcmApi;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.Collections;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.builders.RequestHandlerSelectors.withClassAnnotation;

@Configuration
@EnableSwagger2WebMvc
@Import({springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration.class})
public class SwaggerQcmConfig {

    private static final String AUTHORIZATION ="Authorization";

    @Bean
    public Docket productApiv1() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(QcmApi.API_V1)
                .select()
                .apis(withClassAnnotation(RestController.class))
                .paths(regex(QcmApi.API_V1 + ".*"))
                .build()
                .enable(true)
                .apiInfo(metaData(QcmApi.VERSION))
                .produces(Collections.singleton("application/json"))
                .securityContexts(Lists.newArrayList(buildSecurityContext()))
                .securitySchemes(Lists.newArrayList(buildSecurityScheme()));

    }


    private ApiKey buildSecurityScheme() {
        return new ApiKey(AUTHORIZATION, AUTHORIZATION, "header");
    }

    private SecurityContext buildSecurityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(regex(QcmApi.API_V1 + ".*"))
                .build();
    }

    private List <SecurityReference> defaultAuth() {

        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference(AUTHORIZATION, authorizationScopes));
    }

    private ApiInfo metaData(String version) {

        return new ApiInfo(
                "QCM Rest API",
                "",
                version,
                "Terms of service",
                new Contact("Eric MULLER", "https://qcm-rest-api.herokuapp.com/swagger-ui.html#", "eric.pierre.muller@gmail.com"),
                "MIT License",
                "https://opensource.org/licenses/mit-license.php", Lists.newArrayList());
    }

}
