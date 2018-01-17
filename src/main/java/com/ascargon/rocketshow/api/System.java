package com.ascargon.rocketshow.api;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.Settings;
import com.ascargon.rocketshow.VersionInfo;

@Path("/system")
public class System {

	@Context
	ServletContext context;
    
	@Path("reboot")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response reboot() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.reboot();
		return Response.status(200).build();
	}
	
	@Path("reload-setting")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response play() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.loadSettings();
		return Response.status(200).build();
	}
	
	@Path("reconnect-midi")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response reconnectMidi() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.reconnectMidiDevices();
		return Response.status(200).build();
	}
	
	@Path("current-version")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public VersionInfo version() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		return manager.getUpdater().getCurrentVersionInfo();
	}
	
	@Path("remote-version")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public VersionInfo remoteVersion() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		return manager.getUpdater().getRemoteVersionInfo();
	}
	
	@Path("update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response update() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.getUpdater().update();
		return Response.status(200).build();
	}

	@Path("state")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public com.ascargon.rocketshow.api.State getState() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		return manager.getStateManager().getCurrentState();
	}
	
	@Path("settings")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Settings getSettings() throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		return manager.getSettings();
	}
	
	@Path("settings")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveSettings(Settings settings) throws Exception {
		Manager manager = (Manager)context.getAttribute("manager");
		manager.setSettings(settings);
		manager.saveSettings();
		
		return Response.status(200).build();
	}
	
}
