package com.ascargon.rocketshow.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ascargon.rocketshow.Manager;

@Path("/song")
public class Song {

	@Context
	ServletContext context;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(com.ascargon.rocketshow.song.Song song) throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.saveSong(song);
		return Response.status(200).build();
	}
	
}
