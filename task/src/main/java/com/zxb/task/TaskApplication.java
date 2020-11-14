package com.zxb.task;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
@MapperScan("com.zxb.task.dao")
@EnableAsync
@EnableScheduling
@Slf4j
public class TaskApplication {

    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
        InetAddress localHost = Inet4Address.getLocalHost();
        String contextPath = "http://".concat(localHost.getHostAddress()).concat(":11000");
        log.info("定时任务管理页面：{}", contextPath.concat("/scheduleManagement/taskList"));
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("scheduleTask-");
        executor.setAwaitTerminationSeconds(60 * 5);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

}
