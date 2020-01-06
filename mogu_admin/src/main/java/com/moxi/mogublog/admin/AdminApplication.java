package com.moxi.mogublog.admin;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableTransactionManagement
//redis和velocity的包会起冲突
@SpringBootApplication
@EnableSwagger2
@EnableEurekaClient
@EnableCaching
@EnableRabbit
@EnableFeignClients("com.moxi.mogublog.admin.feign")
@ComponentScan(basePackages = {
        "com.moxi.mogublog.config",
        "com.moxi.mogublog.admin.log",
        "com.moxi.mogublog.admin.security",
        "com.moxi.mogublog.admin.config",
        "com.moxi.mogublog.admin.restapi",
        "com.moxi.mogublog.xo.service",
        "com.moxi.mogublog.utils"
})
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }

    /**
     * 跨域过滤器
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 4  
        return new CorsFilter(source);
    }

}
