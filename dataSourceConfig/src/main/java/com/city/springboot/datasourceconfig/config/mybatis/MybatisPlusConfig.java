package com.city.springboot.datasourceconfig.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  MybatisPlus 相关配置
 *  @author CITY
 */
@Slf4j
@Configuration
public class MybatisPlusConfig {

    /**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        log.info("初始化 mybatis-plus SQL执行效率插件 Start");
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        log.info("初始化 mybatis-plus SQL执行效率插件 End");
        return performanceInterceptor;
    }

    /**
     * mybatis-plus 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        log.info("初始化 mybatis-plus 分页插件 Start");
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        log.info("初始化 mybatis-plus 分页插件 End");
        return paginationInterceptor;
    }
}
