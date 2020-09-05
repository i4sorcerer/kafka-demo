import lombok.extern.slf4j.Slf4j;
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
public class BootstrapApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootstrapApplication.class);
    }
}
