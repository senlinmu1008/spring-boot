package com.zxb.task;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
@MapperScan("com.zxb.task.dao")
@EnableAsync
@EnableScheduling
@Slf4j
public class TaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }

    @Bean
    @ConditionalOnMissingBean(name = "scheduleTaskExecutor")
    public Executor scheduleTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20); // 核心线程数量，线程池创建时候初始化的线程数
        executor.setMaxPoolSize(50); // 最大线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setQueueCapacity(200); // 缓冲队列，用来缓冲执行任务的队列
        executor.setKeepAliveSeconds(60); // 当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        executor.setThreadNamePrefix("scheduleTask-"); // 线程名前缀
        executor.setWaitForTasksToCompleteOnShutdown(true); // 用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setAwaitTerminationSeconds(60 * 5); // 该方法用来设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
        /*
        1.AbortPolicy，ThreadPoolExecutor中默认的拒绝策略就是AbortPolicy，直接抛出异常。
        2.CallerRunsPolicy，CallerRunsPolicy在任务被拒绝添加后，会调用当前线程池的所在的线程去执行被拒绝的任务。
        3.DiscardPolicy，采用这个拒绝策略，会让被线程池拒绝的任务直接抛弃，不会抛异常也不会执行。
        4.DiscardOldestPolicy，当任务被拒绝添加时，会抛弃任务队列中最旧的任务也就是最先加入队列的，再把这个新任务添加进去。
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        return executor;
    }

}
