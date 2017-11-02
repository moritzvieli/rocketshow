package com.ascargon.rocketshow.rest;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.audio.AudioLine;
import com.ascargon.rocketshow.audio.AudioUtil;

@Path("/audio")
public class Audio {

	@Context
	ServletContext context;

	@Path("out-lines")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<AudioLine> getOutDevices() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		return manager.getAudioUtil().getOutputAudioLines();
	}
	
}
