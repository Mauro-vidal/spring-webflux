package com.mauro.projects.webflux_course.controller.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class StandardError implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:s")
    private LocalDateTime timestamp;
    private String path;
    private Integer status;
    private String error;
    private String message;
}
