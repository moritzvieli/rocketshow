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
	public com.ascargon.rocketshow.song.SetList getCurrent() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		return manager.getCurrentSetList();
	}
	
	@Path("details")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public com.ascargon.rocketshow.song.SetList getByName(@QueryParam("name") String name) throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		return manager.getSongManager().loadSetList(name);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(com.ascargon.rocketshow.song.SetList setList) throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.getSongManager().saveSetList(setList);
		manager.loadSetList(setList.getName());
		return Response.status(200).build();
	}
	
	@Path("delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(@QueryParam("name") String name) throws Exception {
		Manager manager = (Manager) context.getAttribute("manager");
		manager.getSongManager().deleteSetList(name);
		
		// Load another setlist (if possible), if the current setlist has been deleted
		if(manager.getCurrentSetList() != null) {
			if(manager.getCurrentSetList().getName().equals(name)) {
				manager.loadFirstSetList();
			}
		}
		
		return Response.status(200).build();
	}
	
}
