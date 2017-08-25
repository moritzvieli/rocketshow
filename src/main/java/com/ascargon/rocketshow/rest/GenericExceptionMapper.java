package com.ascargon.rocketshow.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class GenericExceptionMapper implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception e) {
		return Response.status(500).entity(e.getMessage()).type("text/plain").build();
	}

}