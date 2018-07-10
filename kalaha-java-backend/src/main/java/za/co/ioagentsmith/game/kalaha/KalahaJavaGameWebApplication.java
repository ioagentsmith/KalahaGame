package za.co.ioagentsmith.game.kalaha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class KalahaJavaGameWebApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(KalahaJavaGameWebApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(KalahaJavaGameWebApplication.class, args);
    }

}
