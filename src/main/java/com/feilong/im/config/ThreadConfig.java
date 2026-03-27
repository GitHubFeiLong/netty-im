package com.feilong.im.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 配置线程池
 * @author cfl 2026/03/26
 */
@Configuration
public class ThreadConfig {

    /**
     * 处理业务的线程池
     */
    @Bean("processLogicThreadPool")
    public Executor processLogicThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int cpuNum = Runtime.getRuntime().availableProcessors();
        // 配置核心线程数
        executor.setCorePoolSize(cpuNum * 2);
        // 配置最大线程数，IO情况多余计算
        executor.setMaxPoolSize(cpuNum * 25);
        // 配置队列大小
        executor.setQueueCapacity(1000);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("im-process-logic");
        /*
            核心线程运行被销毁，空闲市场3h
         */
        executor.setAllowCoreThreadTimeOut(true);
        executor.setKeepAliveSeconds(3 * 60 * 60);

        // rejection-policy: 当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS: 不在新线程中执行任务，而是有调用者所在的线程来执行
        // DiscardPolicy: 丢弃
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        // 等待任务完成在关闭
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置阻塞最大时长(需要根据后期任务数量来修改),在 kill -15 时最长阻塞时长
        executor.setAwaitTerminationSeconds(180);

        return executor;
    }

}
