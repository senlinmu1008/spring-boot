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
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(0);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("scheduleTask-");
        executor.setAwaitTerminationSeconds(60 * 5);
        /*
        1.AbortPolicy，ThreadPoolExecutor中默认的拒绝策略就是AbortPolicy，直接抛出异常。
        2.CallerRunsPolicy，CallerRunsPolicy在任务被拒绝添加后，会调用当前线程池的所在的线程去执行被拒绝的任务。
        3.DiscardPolicy，采用这个拒绝策略，会让被线程池拒绝的任务直接抛弃，不会抛异常也不会执行。
        4.DiscardOldestPolicy，当任务呗拒绝添加时，会抛弃任务队列中最旧的任务也就是最先加入队列的，再把这个新任务添加进去。
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        return executor;
    }

}
