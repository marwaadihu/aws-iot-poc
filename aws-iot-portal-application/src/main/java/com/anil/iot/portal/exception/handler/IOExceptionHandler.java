package com.anil.iot.portal.exception.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.anil.iot.portal.contoller.pojo.ExceptionMessage;

@ControllerAdvice
@ResponseBody
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class IOExceptionHandler {

	@ExceptionHandler(IOException.class)
	public ExceptionMessage executeHandler(IOException ioException) {
		ExceptionMessage exceptionMessage = new ExceptionMessage();
		exceptionMessage.setErrorCode(500);
		exceptionMessage.setErrorMessage(ioException.getMessage());
		return exceptionMessage;
	}

}
