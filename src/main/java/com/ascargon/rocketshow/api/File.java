package com.ascargon.rocketshow.api;

import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.ascargon.rocketshow.Manager;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

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

	@Path("upload")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public com.ascargon.rocketshow.song.file.File upload(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) throws Exception {

		Manager manager = (Manager) context.getAttribute("manager");
		return manager.getFileManager().saveFile(uploadedInputStream, fileDetail.getFileName());
	}

}
