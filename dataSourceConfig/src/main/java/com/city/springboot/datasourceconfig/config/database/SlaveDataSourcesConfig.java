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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.StringUtils;

@Slf4j
@Configuration
//@MapperScan(basePackages = {"com.example.springbootdruidmultsource.mapper.slave"}, sqlSessionFactoryRef = "slaveSqlSessionFactory")
public class SlaveDataSourcesConfig {

    @Value("${slave_mapper_local}")
    private String slaveMapperLocal;

    @Bean(name = "slaveDataSource")
    @ConfigurationProperties("spring.datasource.druid.slave")
    public DruidDataSource druidDataSource() {
        log.info("初始化 slaveDataSource Start");
        DruidDataSource druidDataSource = new DruidDataSource();
        log.info("初始化 slaveDataSource End");
        return druidDataSource;
    }


    /**
     * 从数据库事务使用方式
     *
     * @return DataSourceTransactionManager
     * @Override
     * @Transactional(value = "slaveTransactionManager")
     * public void update(User user) {
     * userMapper.update(user);
     * int i = 10 / 0;
     * }
     */
    //其他数据源的事务管理器
    @Bean(name = "slaveTransactionManager")
    public DataSourceTransactionManager slaveTransactionManager() {
        log.info("初始化 slaveTransactionManager Start");
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(druidDataSource());
        log.info("初始化 slaveTransactionManager End");
        return dataSourceTransactionManager;
    }

    @Bean(name = "slaveSqlSessionFactory")
    public SqlSessionFactory slaveSqlSessionFactory(@Qualifier("slaveDataSource") DruidDataSource dataSource) throws Exception {
        log.info("初始化 slaveSqlSessionFactory Start");
        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        if(StringUtils.isEmpty(slaveMapperLocal)){
            sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(slaveMapperLocal));
        }
        log.info("初始化 slaveSqlSessionFactory End");
        return sessionFactoryBean.getObject();
    }
}
