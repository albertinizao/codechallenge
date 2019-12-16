package com.opipo.codechallenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid sortType")
public class InvalidRequestException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -5560074561034356165L;

}
