package com.ascargon.rocketshow.rest;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.ascargon.rocketshow.midi.MidiDevice;
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
	
}
