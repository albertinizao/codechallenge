package com.opipo.codechallenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Already exists")
public class AlreadyExistsException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 6281895797808623447L;

}
