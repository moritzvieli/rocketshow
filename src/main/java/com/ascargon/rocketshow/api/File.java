package com.ascargon.rocketshow.api;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.ascargon.rocketshow.Manager;

@Path("/file")
public class File {

	@Context
	ServletContext context;

	@Path("list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<com.ascargon.rocketshow.song.file.File> getAll() throws Exception {
		Manager manager = (Manager) context.getAttribute("manager");
		return manager.getFileManager().getAllFiles();
	}
	
}
