package com.mauro.projects.webflux_course.service;

import com.mauro.projects.webflux_course.entity.User;
import com.mauro.projects.webflux_course.mapper.UserMapper;
import com.mauro.projects.webflux_course.model.request.UserRequest;
import com.mauro.projects.webflux_course.repository.UserRepository;
import com.mauro.projects.webflux_course.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public Mono<User> save(final UserRequest request) {
      return repository.save(mapper.toEntity(request));

    }

    public Mono<User> findById(String id) {
        return repository.findById(id).switchIfEmpty(Mono.error(new ObjectNotFoundException(
                format("Object not found. Id: %s, Type: %s", id, User.class.getSimpleName())
        )));

    }


    public Flux<User> findAll() {
        return repository.findAll();
    }


}
