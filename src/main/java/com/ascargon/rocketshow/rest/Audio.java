package com.ascargon.rocketshow.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("/audio")
public class Audio {

	@Context
	ServletContext context;

//	@Path("out-lines")
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	public List<AudioLine> getOutDevices() throws Exception {
//		Manager manager = (Manager)context.getAttribute("manager");
//		return manager.getAudioUtil().getOutputAudioLines();
//	}
	
}
