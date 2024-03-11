package dev.saravan.reactiveapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(servers = {@Server(url = "https://saravanjs.com:10201/", description = "URL"),
        @Server(url = "http://saravanjs.com:10200/", description = "URL"),
        @Server(url = "http://localhost:10200/", description = "URL"),
        })

public class ReactiveRestApiDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveRestApiDemoApplication.class, args);
    }
}
