package com.mauro.projects.webflux_course.service;

import com.mauro.projects.webflux_course.entity.User;
import com.mauro.projects.webflux_course.mapper.UserMapper;
import com.mauro.projects.webflux_course.model.request.UserRequest;
import com.mauro.projects.webflux_course.repository.UserRepository;
import com.mauro.projects.webflux_course.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    @Test
    void testSave() {
        UserRequest request = new UserRequest("valdir", "valdir@mail.com", "123");
        User entity = User.builder().build();

        when(mapper.toEntity(ArgumentMatchers.any(UserRequest.class))).thenReturn(entity);
        when(repository.save(ArgumentMatchers.any(User.class))).thenReturn(Mono.just(User.builder().build()));

        Mono<User> result = service.save(request);

        StepVerifier.create(result)
                .expectNextMatches(Objects::nonNull)
                .expectComplete()
                .verify();

        verify(repository, times(1)).save(any(User.class));

    }

    @Test
    void testFindById() {
        when(repository.findById(anyString())).thenReturn(Mono.just(User.builder().build()));
        Mono<User> result = service.findById("123");
        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();
        Mockito.verify(repository, times(1)).findById(anyString());

    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(Flux.just(User.builder().build()));

        Flux<User> result = service.findAll();

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).findAll();

    }

    @Test
    void testUpdate() {
        UserRequest request = new UserRequest("Mauro", "mauro@mail.com", "123");
        User entity = User.builder().build();

        when(mapper.toEntity(any(UserRequest.class), any(User.class))).thenReturn(entity);
        when(repository.findById(anyString())).thenReturn(Mono.just(entity));
        when(repository.save(any(User.class))).thenReturn(Mono.just(entity));

        Mono<User> result = service.update("123", request);

        StepVerifier.create(result)
                .expectNextMatches(Objects::nonNull)
                .expectComplete()
                .verify();

        verify(repository, times(1)).save(any(User.class));

    }

    @Test
    void testDelete() {
        User entity = User.builder().build();
        when(repository.findAndRemove(anyString())).thenReturn(Mono.just(entity));

        Mono<User> result = service.delete("123");

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();

        verify(repository, times(1)).findAndRemove(anyString());


    }

    @Test
    void testHandleNotFound() {
        when(repository.findById(anyString())).thenReturn(Mono.empty());

        try {
            service.findById("123").block();
        } catch (Exception ex) {
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals(format("Object not found. Id: %s, Type: %s", "123", User.class.getSimpleName()), ex.getMessage());


        }
    }
}