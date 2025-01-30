package com.mauro.projects.webflux_course.model.request;

import com.mauro.projects.webflux_course.validator.TrimString;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(

        @TrimString
        @NotBlank(message = "the name must not be null or empty")
        @Size(min = 3, max = 50, message = "must be between 3 and 50 characters")
        String name,

        @TrimString
        @Email(message = "invalid email")
        @NotBlank(message = "the email must not be null or empty")
        String email,

        @TrimString
        @NotBlank(message = "the password must not be null or empty")
        @Size(min = 3, max = 50, message = "must be between 3 and 50 characters")
        String password
) {

}
