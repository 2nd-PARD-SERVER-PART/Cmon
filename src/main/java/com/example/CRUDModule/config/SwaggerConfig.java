package com.example.CRUDModule.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info().title("숏커톤 스웨거")
                .description("사용법 : api 선택 -> try it out -> 왼쪽 변수는 안건들고 오른쪽 변수에 원하는 값 입력 -> execute -> response body에 결과값 확인")
                .version("1.0.0");
    }
}
