package com.mauro.projects.webflux_course.model.response;

public record UserResponse(
        String id,
        String name,
        String email,
        String password
) {
}
