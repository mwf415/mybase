package cn.onlov.evaluate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = "cn.onlov.evaluate.core.dao.mapper")
@EnableTransactionManagement
@EnableScheduling
public class EvaluateApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvaluateApplication.class, args);
    }
}
