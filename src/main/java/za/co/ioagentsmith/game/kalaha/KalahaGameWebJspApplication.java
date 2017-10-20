package za.co.ioagentsmith.game.kalaha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class KalahaGameWebJspApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(KalahaGameWebJspApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(KalahaGameWebJspApplication.class, args);
    }

}
