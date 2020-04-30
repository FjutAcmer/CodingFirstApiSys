package team.fjut.cf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import team.fjut.cf.utils.IpUtils;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 初始化Springboot
 *
 * @author axiang [2019/10/07]
 */
@SpringBootApplication
@MapperScan(basePackages = {"team.fjut.cf.mapper"})
@EnableTransactionManagement
@EnableScheduling
@Slf4j
public class CodingFirstApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(CodingFirstApplication.class, args);
        log.info("【CodingFirst】- Spring初始化成功");
        String url = String.format("http://%s:%s%s",
                IpUtils.getLocalHostAddress(),
                app.getEnvironment().getProperty("server.port"),
                app.getEnvironment().getProperty("server.servlet.context-path"));
        log.info("【CodingFirst】- 项目启动于 {}", url);
        log.info("【CodingFirst】- 在线API文档于 {}", url + "/swagger-ui.html");
        log.info("【CodingFirst】- Druid后台管理于 {}", url + "/druid");
    }
}
