package com.ascargon.rocketshow.rest;

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
import com.ascargon.rocketshow.midi.MidiDevice;
import com.ascargon.rocketshow.midi.MidiRouting;
import com.ascargon.rocketshow.midi.MidiUtil;
import com.ascargon.rocketshow.midi.MidiUtil.MidiDirection;

@Path("/midi")
public class Midi {

	@Context
	ServletContext context;

	@Path("in-devices")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<MidiDevice> getInDevices() throws Exception {
		return MidiUtil.getMidiDevices(MidiDirection.IN);
	}

	@Path("out-devices")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<MidiDevice> getOutDevices() throws Exception {
		return MidiUtil.getMidiDevices(MidiDirection.OUT);
	}

	@Path("send-message")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response sendMessage(@QueryParam("command") int command, @QueryParam("channel") int channel,
			@QueryParam("note") int note, @QueryParam("velocity") int velocity) throws Exception {
		
		Manager manager = (Manager) context.getAttribute("manager");
		for(MidiRouting remoteMidiRouting : manager.getSettings().getRemoteMidiRoutingList()) {
			remoteMidiRouting.sendMidiMessage(command, channel, note, velocity);
		}
		return Response.status(200).build();
	}

}
