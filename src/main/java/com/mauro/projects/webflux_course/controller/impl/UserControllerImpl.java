package com.mauro.projects.webflux_course.controller.impl;

import com.mauro.projects.webflux_course.controller.UserController;
import com.mauro.projects.webflux_course.mapper.UserMapper;
import com.mauro.projects.webflux_course.model.request.UserRequest;
import com.mauro.projects.webflux_course.model.response.UserResponse;
import com.mauro.projects.webflux_course.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService service;
    private final UserMapper mapper;

    @Override
    public ResponseEntity<Mono<Void>> save(final @Valid UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(request).then());
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> findById(String id) {
        return ResponseEntity.ok(service.findById(id).map(mapper::toResponse));
    }

    public ResponseEntity<Flux<UserResponse>> findAll() {
        return ResponseEntity.ok().body(service.findAll().map(mapper::toResponse));
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> update(String id, UserRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Mono<Void>> delete(String id) {
        return null;
    }
}
