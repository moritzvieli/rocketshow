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
	public Response play() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.getCurrentSong().play();
		return Response.status(200).build();
	}
	
	@Path("pause")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response pause() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.getCurrentSong().pause();
		return Response.status(200).build();
	}
	
	@Path("resume")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response resume() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.getCurrentSong().resume();
		return Response.status(200).build();
	}

	@Path("toggle-play")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response togglePlay() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.getCurrentSong().togglePlay();
		return Response.status(200).build();
	}
	
	@Path("stop")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response stop() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.getCurrentSong().stop();
		return Response.status(200).build();
	}
	
}
