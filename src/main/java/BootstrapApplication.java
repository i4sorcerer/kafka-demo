import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * @author : sk
 */
@SpringBootApplication
@ImportResource(locations = "kafkaConsumer.xml")
@Slf4j
@ComponentScan("kafka.demo")
// 添加注解，扫描mapper接口
@MapperScan("kafka.demo.domain.dao")
public class BootstrapApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootstrapApplication.class);
    }
}
