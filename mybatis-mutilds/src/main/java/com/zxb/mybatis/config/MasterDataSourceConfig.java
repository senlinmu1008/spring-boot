/**
 * Copyright (C), 2015-2018
 */
package com.zxb.mybatis.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 主库配置类
 *
 * @author zhaoxb
 * @create 2018-09-08 17:37
 */
@Configuration
@MapperScan(
        basePackages = "com.zxb.mybatis.dao.master", // Mapper映射接口，多数据源需要分开
        sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MasterDataSourceConfig {

    /**
     * 数据源配置
     * 多数据源配置的时候必须要有一个主数据源，用@Primary标志该Bean
     *
     * @return
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "master.datasource")
    public DataSource masterDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        return dataSource;
    }

    /**
     * 事务管理器
     * 使用代理对象获取数据源创建事务管理器
     *
     * @return
     */
    @Bean
    @Primary
    public DataSourceTransactionManager masterTransactionManager() {
        return new DataSourceTransactionManager(masterDataSource());
    }

    /**
     * 创建mybatis会话工厂
     * 指定数据源和mybatis映射文件(多数据源需要分开)
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Primary
    public SqlSessionFactory masterSqlSessionFactory() throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource());
        sessionFactory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/master/*.xml"));
        return sessionFactory.getObject();
    }
}