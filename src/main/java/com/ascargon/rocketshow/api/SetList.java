package com.ascargon.rocketshow.api;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
    
	@Path("list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<com.ascargon.rocketshow.song.SetList> getAll() throws Exception {
		Manager manager = (Manager) context.getAttribute("manager");
		return manager.getSongManager().getAllSetLists();
	}
	
	@Path("load")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response load(@QueryParam("name") String name) throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.loadSetList(name);
		return Response.status(200).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public com.ascargon.rocketshow.song.SetList get() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		return manager.getCurrentSetList();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(com.ascargon.rocketshow.song.SetList setList) throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.getSongManager().saveSetList(setList);
		manager.setCurrentSetList(setList);
		return Response.status(200).build();
	}
	
}
