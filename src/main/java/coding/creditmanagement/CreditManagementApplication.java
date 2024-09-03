package coding.creditmanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

/**
 * 额度管理服务
 *
 */
@SpringBootApplication
@MapperScan("coding.creditmanagement.mapper")
@EnableScheduling
public class CreditManagementApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(CreditManagementApplication.class, args);
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(8); // 设置线程池大小
        scheduler.setThreadNamePrefix("scheduled-task-");
        return scheduler;
    }
}
