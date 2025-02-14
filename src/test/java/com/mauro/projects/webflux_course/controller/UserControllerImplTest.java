package com.mauro.projects.webflux_course.controller;

import com.mauro.projects.webflux_course.entity.User;
import com.mauro.projects.webflux_course.mapper.UserMapper;
import com.mauro.projects.webflux_course.model.request.UserRequest;
import com.mauro.projects.webflux_course.model.response.UserResponse;
import com.mauro.projects.webflux_course.service.UserService;
import com.mongodb.reactivestreams.client.MongoClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static reactor.core.publisher.Mono.just;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerImplTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService service;

    @MockBean
    private UserMapper mapper;

    @MockBean
    private MongoClient mongoClient;

    @Test
    @DisplayName("Test endpoint save with success")
    void TestSaveWithSuccess() {
        final var request = new UserRequest("Mauro", "maurovidal@mail.com", "123");

        when(service.save(any(UserRequest.class))).thenReturn(just(User.builder().build()));

        webTestClient.post().uri("/users")
                .contentType(APPLICATION_JSON)
                .body(fromValue(request))
                .exchange()
                .expectStatus().isCreated();


        verify(service, times(1)).save(any(UserRequest.class));

    }

    @Test
    @DisplayName("Test endpoint save with bad request")
    void TestSaveWithBadRequest() {
        final var request = new UserRequest(" Mauro", "maurovidal@mail.com", "123");

        webTestClient.post().uri("/users")
                .contentType(APPLICATION_JSON)
                .body(fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users")
                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("Validation Error")
                .jsonPath("$.message").isEqualTo("Error on validation attributes")
                .jsonPath("$.errors[0].fieldName").isEqualTo("name")
                .jsonPath("$.errors[0].message").isEqualTo("field cannot have blank spaces at the beginning or at end");
    }

    @Test
    @DisplayName("Test endpoint save with bad request for email invalid")
    void TestSaveWithBadRequestEmailInvalid() {
        final var request = new UserRequest("Mauro", " maurovidalmail.com", "123");

        webTestClient.post().uri("/users")
                .contentType(APPLICATION_JSON)
                .body(fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users")
                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("Validation Error")
                .jsonPath("$.message").isEqualTo("Error on validation attributes")

                .jsonPath("$.errors[0].fieldName").isEqualTo("email")
                .jsonPath("$.errors[0].message").isEqualTo("invalid email")

               .jsonPath("$.errors[1].fieldName").isEqualTo("email")
               .jsonPath("$.errors[1].message").isEqualTo("field cannot have blank spaces at the beginning or at end");
    }

    @Test
    @DisplayName("Test endpoint save with bad request for password invalid")
    void TestSaveWithBadRequestPasswordInvalid() {
        final var request = new UserRequest("Mauro", "maurovidal@mail.com", " 3");

        webTestClient.post().uri("/users")
                .contentType(APPLICATION_JSON)
                .body(fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users")
                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("Validation Error")
                .jsonPath("$.message").isEqualTo("Error on validation attributes")

                .jsonPath("$.errors[0].fieldName").isEqualTo("password")
                .jsonPath("$.errors[0].message").isEqualTo("field cannot have blank spaces at the beginning or at end")

                .jsonPath("$.errors[1].fieldName").isEqualTo("password")
                .jsonPath("$.errors[1].message").isEqualTo("must be between 3 and 50 characters");
    }

    @Test
    @DisplayName("Test find by id endpoint with success")
    void TestFindByIdWithSuccess() {
        final var id = "123456";
        final var name = "Mauro";
        final var email = "maurovidal@mail.com";
        final var password = "123";
        final var userResponse = new UserResponse(id, name, email, password);

        when(service.findById(anyString())).thenReturn(just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.get().uri("/users/" + id)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.name").isEqualTo(name)
                .jsonPath("$.email").isEqualTo(email)
                .jsonPath("$.password").isEqualTo(password);

    }

    @Test
    void findAll() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}