package org.socialculture.platform;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SocialCultureApplication {

    public static void main(String[] args) {


        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        Map<String, Object> env = new HashMap<>();
        dotenv.entries().forEach(entry -> env.put(entry.getKey(), entry.getValue()));

        SpringApplication app = new SpringApplication(SocialCultureApplication.class);
        app.setDefaultProperties(env); // 여기에 반드시 주입해야 함
        app.run(args);

        ;
    }

}
