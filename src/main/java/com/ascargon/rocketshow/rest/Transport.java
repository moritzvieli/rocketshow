package com.ascargon.rocketshow.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
		manager.getCurrentSetList().play();
		return Response.status(200).build();
	}
	
	@Path("pause")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response pause() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.getCurrentSetList().pause();
		return Response.status(200).build();
	}
	
	@Path("resume")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response resume() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.getCurrentSetList().resume();
		return Response.status(200).build();
	}

	@Path("toggle-play")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response togglePlay() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.getCurrentSetList().togglePlay();
		return Response.status(200).build();
	}
	
	@Path("stop")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response stop() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.getCurrentSetList().stop();
		return Response.status(200).build();
	}

	@Path("next-song")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response nextSong() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.getCurrentSetList().nextSong();;
		return Response.status(200).build();
	}
	
	@Path("previous-song")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response previousSong() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.getCurrentSetList().previousSong();
		return Response.status(200).build();
	}
	
	@Path("set-song-index")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response setSongIndex(@QueryParam("index") int index) throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.getCurrentSetList().setCurrentSongIndex(index);;
		return Response.status(200).build();
	}
	
}
