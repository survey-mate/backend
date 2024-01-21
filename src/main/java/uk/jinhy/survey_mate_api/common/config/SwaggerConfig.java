package uk.jinhy.survey_mate_api.common.config;

import io.swagger.models.auth.In;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
public class SwaggerConfig {
        @Bean
        public Docket api() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("src.main.java.uk.jinhy.survey_mate_api"))
                    .paths(PathSelectors.any())
                    .build()
                    .securitySchemes(Arrays.asList(apiKey()))
                    .securityContexts(Arrays.asList(securityContext()))
                    .apiInfo(apiInfo());
        }

        private ApiKey apiKey() {
            return new ApiKey("JWT", HttpHeaders.AUTHORIZATION, In.HEADER.name());
        }

        private SecurityContext securityContext() {
            return SecurityContext.builder()
                    .securityReferences(defaultAuth())
                    .forPaths(PathSelectors.any())
                    .build();
        }

        private List<SecurityReference> defaultAuth() {
            AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
            AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
            return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
        }

        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title("Your API Title")
                    .description("Your API Description")
                    .version("1.0")
                    .build();
        }


    @Bean
    public OpenAPI SpringBootAPI() {
        Info info = new Info()
                .title("썰매 API 문서")
                .description("썰매 스프링 부트 서버 API 문저")
                .version("1.0.0");

        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .info(info);
    }
}