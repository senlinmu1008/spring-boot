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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 从库配置类
 *
 * @author zhaoxb
 * @create 2018-09-09 13:08
 */
@Configuration
@MapperScan(
        basePackages = "com.zxb.mybatis.dao.cluster",
        sqlSessionFactoryRef = "clusterSqlSessionFactory")
public class ClusterDataSourceConfig {

    /**
     * 数据源配置
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "cluster.datasource")
    public DataSource clusterDataSource() {
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
    public DataSourceTransactionManager clusterTransactionManager() {
        return new DataSourceTransactionManager(clusterDataSource());
    }

    /**
     * 创建mybatis会话工厂
     * 指定数据源和mybatis映射文件(多数据源需要分开)
     *
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory clusterSqlSessionFactory() throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(clusterDataSource());
        sessionFactory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/cluster/*.xml"));
        return sessionFactory.getObject();
    }
}