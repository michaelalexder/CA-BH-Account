package com.bhca.account.exception;

import org.openapitools.client.model.ErrorView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorView handleServerException(Exception e) {
        return convert(HttpStatus.INTERNAL_SERVER_ERROR, e,
                "Unexpected error occurred. Sorry for inconvenience.");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorView handleBadRequestException(NoHandlerFoundException e) {
        return convert(HttpStatus.NOT_FOUND, e, "No such path exist.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorView handleBadRequestException(IllegalArgumentException e) {
        return convert(HttpStatus.BAD_REQUEST, e, "Bad request.");
    }

    private ErrorView convert(HttpStatus httpStatus, Exception e, String message) {
        logger.error(httpStatus.getReasonPhrase(), e);
        ErrorView errorView = new ErrorView();
        errorView.setMessage(message);
        errorView.setDetails(e.getMessage());
        return errorView;
    }
}
