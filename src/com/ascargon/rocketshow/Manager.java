package com.ascargon.rocketshow;

import java.io.File;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.ascargon.rocketshow.dmx.DmxSignalSender;
import com.ascargon.rocketshow.dmx.Midi2DmxConverter;
import com.ascargon.rocketshow.dmx.Midi2DmxMapping;
import com.ascargon.rocketshow.song.SetList;
import com.ascargon.rocketshow.song.Song;
import com.ascargon.rocketshow.song.file.MidiFile;
import com.ascargon.rocketshow.song.file.VideoFile;
import com.ascargon.rocketshow.video.VideoPlayer;

public class Manager {

	private DmxSignalSender dmxSignalSender;
	private Midi2DmxConverter midi2DmxConverter;
	
	private VideoPlayer videoPlayer;

	// Global settings
	private Midi2DmxMapping midi2DmxMapping;

	private SetList currentSetList;
	private Song currentSong;

	public void load() {
		dmxSignalSender = new DmxSignalSender();
		midi2DmxConverter = new Midi2DmxConverter(dmxSignalSender);

		midi2DmxMapping = new Midi2DmxMapping();
		midi2DmxMapping.setChannelOffset(0);
		
		HashMap<Integer, Integer> channelMap = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < 128; i++) {
			channelMap.put(0, 0);	
		}
		
		midi2DmxMapping.setChannelMap(channelMap);
		
		// TODO Load the setlist and the first song

		currentSetList = new SetList();

		currentSetList.setPath("/users/moritzvieli/Test Setlist.stl");

		currentSong = new Song();

		currentSong.setPath("/users/moritzvieli/Test Song.sng");

		MidiFile midiFile = new MidiFile();
		midiFile.setOffsetInMillis(1250);
		midiFile.setPath("/users/moritzvieli/test.mid");
		currentSong.getFileList().add(midiFile);
		
		VideoFile videoFile = new VideoFile();
		videoFile.setOffsetInMillis(0);
		videoFile.setPath("/users/moritzvieli/test.mpeg");
		currentSong.getFileList().add(videoFile);
		

		currentSetList.getSongList().add(currentSong);
		currentSetList.getSongList().add(currentSong);

		// Save song/setlist
//		try {
//
//			File file = new File("/users/moritzvieli/setlist.stl");
//			JAXBContext jaxbContext = JAXBContext.newInstance(SetList.class);
//			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//
//			// output pretty printed
//			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//			jaxbMarshaller.marshal(currentSetList, file);
//			jaxbMarshaller.marshal(currentSetList, System.out);
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}
//
//		try {
//
//			File file = new File("/users/moritzvieli/song.sng");
//			JAXBContext jaxbContext = JAXBContext.newInstance(Song.class);
//			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//
//			// output pretty printed
//			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//			jaxbMarshaller.marshal(currentSong, file);
//			jaxbMarshaller.marshal(currentSong, System.out);
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}


		// Load a setlist
		try {

			File file = new File("/users/moritzvieli/setlist.stl");
			JAXBContext jaxbContext = JAXBContext.newInstance(SetList.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			SetList s = (SetList) jaxbUnmarshaller.unmarshal(file);
			s.load();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}

	public void play() {
		currentSong.play();
	}

	public String test() {
		return "Hello there";
	}

	public Midi2DmxConverter getMidi2DmxConverter() {
		return midi2DmxConverter;
	}

	public void setMidi2DmxConverter(Midi2DmxConverter midi2DmxConverter) {
		this.midi2DmxConverter = midi2DmxConverter;
	}

	public VideoPlayer getVideoPlayer() {
		return videoPlayer;
	}

	public void setVideoPlayer(VideoPlayer videoPlayer) {
		this.videoPlayer = videoPlayer;
	}

	public Midi2DmxMapping getMidi2DmxMapping() {
		return midi2DmxMapping;
	}

	public void setMidi2DmxMapping(Midi2DmxMapping midi2DmxMapping) {
		this.midi2DmxMapping = midi2DmxMapping;
	}

}
