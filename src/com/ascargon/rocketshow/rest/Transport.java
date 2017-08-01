package com.ascargon.rocketshow.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ascargon.rocketshow.Manager;

@Path("/transport")
public class Transport {

	@Context
	ServletContext context;
    
	@Path("play")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response play() {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.play();
		return Response.status(200).build();
	}
	
	@Path("pause")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response pause() {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.pause();
		return Response.status(200).build();
	}

}
