package com.feilong.im.config;

import com.feilong.im.properties.SecurityProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenApi配置
 * @author cfl 2026/4/10
 */
@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    private final Environment environment;
    private final SecurityProperties securityProperties;

    @Bean
    public OpenAPI openApi() {
        String appVersion = environment.getProperty("project.version", "1.0.0");

        return new OpenAPI()
                .info(new Info()
                        .title("用户系统API")
                        .description( "AC项目文档")
                        .version(appVersion)
                        .contact(new Contact().name("feilong").email("1696741038@qq.com"))
                        .license(new License().name("Apache 2.0"))
                );
    }

    /**
     * 给所有接口，全局添加请求头信息
     * @return GlobalOperationCustomizer
     */
    @Bean
    public GlobalOperationCustomizer myGlobalOperationCustomizer() {
        return  (Operation operation, HandlerMethod handlerMethod) -> {
            // 添加认证头，使用全局的请求头信息
            List<SecurityRequirement> security = operation.getSecurity();
            if (security == null) {
                security = List.of(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION));
                operation.setSecurity(security);
            }

            // 添加
            List<Parameter> parameters = operation.getParameters();
            if (parameters == null) {
                parameters = new ArrayList<>();
            }

            // 添加请求头
            parameters.add(new Parameter()
                    .in("header")
                    .name(HttpHeaders.AUTHORIZATION)
                    .schema(new StringSchema())
                    // 开放资源不需要请求头
                    .required(false)
                    .description("token"));

            operation.setParameters(parameters);
            return operation;
        };
    }
}
