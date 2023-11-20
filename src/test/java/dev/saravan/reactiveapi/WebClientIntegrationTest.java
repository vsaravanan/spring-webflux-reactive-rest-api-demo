package dev.saravan.reactiveapi;

import dev.saravan.reactiveapi.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest(classes = ReactiveRestApiDemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WebClientIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    public void givenUser_whenCreateUser_thenReturnSuccessWithUser() {
        var score = ThreadLocalRandom.current().nextInt(1, 100);
        User request = new User(1,"saravandev", score);

        webTestClient.post().uri("/api/v1/users/webflux")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), User.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isNotEmpty()
                .jsonPath("$.name").isEqualTo("saravandev")
                .jsonPath("$.score").isEqualTo(score);
    }

    @Test
    @Order(2)
    public void givenUserId_whenRetrieveUser_thenReturnSuccessWithUser() {
        webTestClient.get().uri("/api/v1/users/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isNotEmpty();
    }


    @Test
    @Order(3)
    public void givenUserId_whenDeleteUser_thenReturnEmptyResponse() {
        webTestClient.delete().uri("/api/v1/users/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

}
