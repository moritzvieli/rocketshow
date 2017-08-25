package com.ascargon.rocketshow.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ascargon.rocketshow.Manager;

@Path("/system")
public class System {

	@Context
	ServletContext context;
    
	@Path("reload-setting")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response play() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.loadSettings();;
		return Response.status(200).build();
	}
	
}
