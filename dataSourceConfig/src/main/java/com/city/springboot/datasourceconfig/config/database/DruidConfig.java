package com.city.springboot.datasourceconfig.config.database;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Slf4j
//@Data
@Profile({ "dev", "prod" })
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DruidConfig {

    @Value("${spring.datasource.loginUsername}")
    private String loginUsername;
    @Value("${spring.datasource.loginPassword}")
    private String loginPassword;
    @Value("${spring.datasource.IPWhitelist}")
    private String IPWhitelist;
//    @Value("#{'${IPWhitelist}'.split(',')}")
//    private List<String> IPWhitelist;
    @Value("${spring.datasource.IPBlacklist}")
    private String IPBlacklist;
//    @Value("#{'${IPBlacklist}'.split(',')}")
//    private List<String> IPBlacklist;
    @Value("${spring.datasource.Prohibit}")
    private String Prohibit;

    @Bean
    public ServletRegistrationBean druidServlet() {
        //创建servlet注册实体
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String,String> initParams = new HashMap<>();

        //设置登录查看信息的账号密码.
        initParams.put("loginUsername", loginUsername);
        initParams.put("loginPassword", loginPassword);
        // IP白名单 默认就是允许所有访问 多个ip逗号隔开
        initParams.put("allow", IPWhitelist);
        // IP黑名单(共同存在时，deny优先于allow) 多个ip逗号隔开
        initParams.put("deny", IPBlacklist);
        //控制台管理用户
        initParams.put("loginUsername", loginUsername);
        initParams.put("loginPassword", loginPassword);
        //是否能够重置数据 禁用HTML页面上的“Reset All”功能
        initParams.put("resetEnable", "false");
        servletRegistrationBean.setInitParameters(initParams);
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        //创建过滤器
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        //设置过滤器过滤路径
        filterRegistrationBean.addUrlPatterns("/*");
        //忽略过滤的形式
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

}
