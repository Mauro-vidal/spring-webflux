package com.mauro.projects.webflux_course.service;

import com.mauro.projects.webflux_course.entity.User;
import com.mauro.projects.webflux_course.mapper.UserMapper;
import com.mauro.projects.webflux_course.model.request.UserRequest;
import com.mauro.projects.webflux_course.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    @Test
    void save() {
        UserRequest request = new UserRequest("valdir", "valdir@mail.com", "123");
        User entity = User.builder().build();

        Mockito.when(mapper.toEntity(ArgumentMatchers.any(UserRequest.class))).thenReturn(entity);
        Mockito.when(repository.save(ArgumentMatchers.any(User.class))).thenReturn(Mono.just(User.builder().build()));

        Mono<User> result = service.save(request);

        StepVerifier.create(result)
                .expectNextMatches(Objects::nonNull)
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));

    }
}