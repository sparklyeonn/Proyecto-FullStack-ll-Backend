package cl.ritmolab.ritmolab_backend; 

import cl.ritmolab.ritmolab_backend.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "cl.ritmolab.ritmolab_backend") 
public class RitmolabBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RitmolabBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(UserService userService) {
        return args -> {
            userService.createAdminUserIfNotExists();
        };
    }
}