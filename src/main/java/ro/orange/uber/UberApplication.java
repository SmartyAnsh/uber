package ro.orange.uber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class UberApplication {

    public static void main(String[] args) {
        SpringApplication.run(UberApplication.class, args);
    }

}
