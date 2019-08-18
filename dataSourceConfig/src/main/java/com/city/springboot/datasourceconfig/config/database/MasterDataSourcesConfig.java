package com.city.springboot.datasourceconfig.config.database;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

@Slf4j
@Configuration
//master mapper目录
//@MapperScan(basePackages = {"com.example.springbootdruidmultsource.mapper.master"}, sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MasterDataSourcesConfig {

    @Value("${master_mapper_local}")
    private String masterMapperLocal;
    // 注解@Primary表示是主数据源

    @Primary
    @Bean(name = "masterDataSource")
    @ConfigurationProperties("spring.datasource.druid.master")
    public DruidDataSource druidDataSource() {
        log.info("初始化 masterDataSource Start");
        DruidDataSource druidDataSource = new DruidDataSource();
        log.info("初始化 masterDataSource End");
        return druidDataSource;
    }

    /**
     * 从数据库事务使用方式
     * 直接添加注解或声明使用的事务
     *
     * @return DataSourceTransactionManager
     * @Override
     * @Transactional
     * @Transactional(value = "masterSqlSessionFactory")
     * public void update(User user) {
     * userMapper.update(user);
     * int i = 10 / 0;
     * }
     */
    @Primary
    @Bean(name = "masterTransactionManager")
    public DataSourceTransactionManager masterTransactionManager() {
        log.info("初始化 masterTransactionManager Start");
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(druidDataSource());
        log.info("初始化 masterTransactionManager End");
        return dataSourceTransactionManager;
    }

    @Primary
    @Bean(name = "masterSqlSessionFactory")
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDataSource") DataSource dataSource) throws Exception {
        log.info("初始化 masterSqlSessionFactory Start");
        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        if(StringUtils.isEmpty(masterMapperLocal)){
            sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(masterMapperLocal));
        }
        log.info("初始化 masterSqlSessionFactory End");
        return sessionFactoryBean.getObject();
    }
}
