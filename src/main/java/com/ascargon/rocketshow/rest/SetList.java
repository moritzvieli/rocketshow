package com.ascargon.rocketshow.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ascargon.rocketshow.Manager;

@Path("/setlist")
public class SetList {

	@Context
	ServletContext context;
    
	@Path("load")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response load(@QueryParam("name") String name) throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.loadSetlist(name);
		return Response.status(200).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(com.ascargon.rocketshow.song.SetList setList) throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.saveSetList(setList);
		manager.setCurrentSetList(setList);
		return Response.status(200).build();
	}
	
}
