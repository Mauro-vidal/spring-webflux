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



import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static reactor.core.publisher.Mono.empty;
import static reactor.core.publisher.Mono.just;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerImplTest {

    public static final String ID = "123456";
    public static final String NAME = "Mauro";
    public static final String EMAIL = "maurovidal@mail.com";
    public static final String PASSWORD = "123";

    public static final String NAME_INVALID = " Mauro";
    public static final String EMAIL_INVALID = " maurovidalmail.com";
    public static final String PASSWORD_INVALID = " 3";


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
        final var request = new UserRequest(NAME, EMAIL, PASSWORD);

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
    void TestSaveWithBadRequestNameInvalid() {
        final var request = new UserRequest(NAME_INVALID, EMAIL, PASSWORD);

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
        final var request = new UserRequest(NAME, EMAIL_INVALID, PASSWORD);

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
        final var request = new UserRequest(NAME, EMAIL, PASSWORD_INVALID);

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
        final var userResponse = new UserResponse(ID, NAME, EMAIL, PASSWORD);

        when(service.findById(anyString())).thenReturn(just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.get().uri("/users/" + ID)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(ID)
                .jsonPath("$.name").isEqualTo(NAME)
                .jsonPath("$.email").isEqualTo(EMAIL)
                .jsonPath("$.password").isEqualTo(PASSWORD);

    }

    @Test
    @DisplayName("Test find by id endpoint with object not found")
    void TestFindByIdWithObjectNotFound() {
        final var id = "12345";

        // Simula que o serviço retornará Mono.empty() quando o objeto não for encontrado
        when(service.findById(id)).thenReturn(empty());

        webTestClient.get().uri("/users/" + id)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()  // Verifica se o status HTTP 404 é retornado
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users/" + id)
                .jsonPath("$.status").isEqualTo(NOT_FOUND.value())
                .jsonPath("$.error").isEqualTo("Not Found")
                .jsonPath("$.message").isEqualTo(format("Object not found. Id: %s, Type: %s", id, User.class.getSimpleName()));
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