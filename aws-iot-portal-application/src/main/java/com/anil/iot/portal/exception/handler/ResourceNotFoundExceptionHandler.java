package com.anil.iot.portal.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.amazonaws.services.iot.model.ResourceNotFoundException;
import com.anil.iot.portal.contoller.pojo.ExceptionMessage;

@ControllerAdvice
@ResponseBody
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotFoundExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ExceptionMessage executeHandler(ResourceNotFoundException resourceNotFoundException) {
		ExceptionMessage exceptionMessage = new ExceptionMessage();
		exceptionMessage.setErrorCode(resourceNotFoundException.getStatusCode());
		exceptionMessage.setErrorMessage(resourceNotFoundException.getErrorMessage());
		exceptionMessage.setErrorType(resourceNotFoundException.getErrorType().name());
		return exceptionMessage;
	}

}
