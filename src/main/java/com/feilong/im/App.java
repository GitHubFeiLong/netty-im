package com.feilong.im;

import com.feilong.im.util.LogApplicationStartupUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StopWatch;

/**
 * @author cfl 2026/03/18
 */
@SpringBootApplication
@MapperScan("com.feilong.im.dao")
public class App {
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext app = SpringApplication.run(App.class, args);
        stopWatch.stop();
        Environment env = app.getEnvironment();
        LogApplicationStartupUtil.print(env, (int)stopWatch.getTotalTimeSeconds());
    }
}
