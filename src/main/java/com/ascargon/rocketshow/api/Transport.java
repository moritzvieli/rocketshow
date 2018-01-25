package com.ascargon.rocketshow.api;

import javax.servlet.ServletContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.song.Song;

@Path("/transport")
public class Transport {

	final static Logger logger = Logger.getLogger(Transport.class);
	
	@Context
	ServletContext context;
	
	@Path("load")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response load(@QueryParam("name") String songName) throws Exception {
		logger.info("Received API request for transport/load");
		
		Manager manager = (Manager) context.getAttribute("manager");
		
		Song currentSong = null;
		
		if(songName.length() == 0) {
			// Load the current song, if available
			manager.getPlayer().getCurrentSong();
		} else {
			// Stop a current song, if needed and don't play the idle video
			if(manager.getPlayer().getCurrentSong() != null) {
				manager.getPlayer().getCurrentSong().stop(false);
			}
			
			// Load the song with the given name
			currentSong = manager.getSongManager().loadSong(songName);

			manager.getPlayer().setCurrentSong(currentSong);
			currentSong.setName(songName);
			currentSong.getMidiMapping().setParent(manager.getSettings().getMidiMapping());
			currentSong.setManager(manager);
			
			currentSong.loadFiles();
		}
		
		if (currentSong != null) {
			currentSong.loadFiles();
		}
		
		return Response.status(200).build();
	}
	
	@Path("play")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response play() throws Exception {
		logger.info("Received API request for transport/play");
		
		Manager manager = (Manager) context.getAttribute("manager");
		if (manager.getCurrentSetList() != null) {
			manager.getPlayer().play();
		}
		return Response.status(200).build();
	}

	@Path("pause")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response pause() throws Exception {
		logger.info("Received API request for transport/pause");
		
		Manager manager = (Manager) context.getAttribute("manager");
		if (manager.getCurrentSetList() != null) {
			manager.getPlayer().pause();
		}
		return Response.status(200).build();
	}

	@Path("resume")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response resume() throws Exception {
		logger.info("Received API request for transport/resume");
		
		Manager manager = (Manager) context.getAttribute("manager");
		if (manager.getCurrentSetList() != null) {
			manager.getPlayer().resume();
		}
		return Response.status(200).build();
	}

	@Path("toggle-play")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response togglePlay() throws Exception {
		logger.info("Received API request for transport/toggle-play");
		
		Manager manager = (Manager) context.getAttribute("manager");
		if (manager.getCurrentSetList() != null) {
			manager.getPlayer().togglePlay();
		}
		return Response.status(200).build();
	}

	@Path("stop")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response stop() throws Exception {
		logger.info("Received API request for transport/stop");
		
		Manager manager = (Manager) context.getAttribute("manager");
		if (manager.getCurrentSetList() != null) {
			manager.getPlayer().stop(true);
		}
		return Response.status(200).build();
	}

	@Path("next-song")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response nextSong() throws Exception {
		logger.info("Received API request for transport/next-song");
		
		Manager manager = (Manager) context.getAttribute("manager");
		if (manager.getCurrentSetList() != null) {
			manager.getCurrentSetList().nextSong();
		}
		return Response.status(200).build();
	}

	@Path("previous-song")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response previousSong() throws Exception {
		logger.info("Received API request for transport/previous-song");
		
		Manager manager = (Manager) context.getAttribute("manager");
		if (manager.getCurrentSetList() != null) {
			manager.getCurrentSetList().previousSong();
		}
		return Response.status(200).build();
	}

	@Path("set-song-index")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response setSongIndex(@QueryParam("index") int index) throws Exception {
		logger.info("Received API request for transport/set-song-index");
		
		Manager manager = (Manager) context.getAttribute("manager");
		if (manager.getCurrentSetList() != null) {
			manager.getCurrentSetList().setSongIndex(index);
		}
		return Response.status(200).build();
	}

}
