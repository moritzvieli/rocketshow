package com.ascargon.rocketshow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.ascargon.rocketshow.api.StateManager;
import com.ascargon.rocketshow.dmx.DmxSignalSender;
import com.ascargon.rocketshow.dmx.Midi2DmxConverter;
import com.ascargon.rocketshow.image.ImageDisplayer;
import com.ascargon.rocketshow.midi.Midi2ActionConverter;
import com.ascargon.rocketshow.midi.MidiDeviceConnectedListener;
import com.ascargon.rocketshow.midi.MidiInDeviceReceiver;
import com.ascargon.rocketshow.midi.MidiRouting;
import com.ascargon.rocketshow.midi.MidiUtil;
import com.ascargon.rocketshow.midi.MidiUtil.MidiDirection;
import com.ascargon.rocketshow.song.SetList;
import com.ascargon.rocketshow.song.Song;
import com.ascargon.rocketshow.song.SongManager;
import com.ascargon.rocketshow.song.file.FileManager;
import com.ascargon.rocketshow.util.ResetUsb;
import com.ascargon.rocketshow.util.ShellManager;

public class Manager {

	final static Logger logger = Logger.getLogger(Manager.class);

	public final static String BASE_PATH = "/opt/rocketshow/";

	// Manage the client states (web GUI)
	private StateManager stateManager;

	private Updater updater;

	private SongManager songManager;
	private FileManager fileManager;

	private ImageDisplayer imageDisplayer;
	private MidiInDeviceReceiver midiInDeviceReceiver;
	private Midi2ActionConverter midi2ActionConverter;

	private DmxSignalSender dmxSignalSender;
	private Midi2DmxConverter midi2DmxConverter;

	private MidiDevice midiOutDevice;
	private Timer connectMidiOutDeviceTimer;
	private List<MidiDeviceConnectedListener> midiOutDeviceConnectedListeners = new ArrayList<MidiDeviceConnectedListener>();

	private Session session;
	private Settings settings;

	private SetList currentSetList;

	private Song idleSong;

	private Player player;

	public void addMidiOutDeviceConnectedListener(MidiDeviceConnectedListener listener) {
		midiOutDeviceConnectedListeners.add(listener);

		// We already have a device connected -> fire the listener
		if (midiOutDevice != null) {
			listener.deviceConnected(midiOutDevice);
		}
	}

	public void removeMidiOutDeviceConnectedListener(MidiDeviceConnectedListener listener) {
		midiOutDeviceConnectedListeners.remove(listener);
	}

	public void reconnectMidiDevices() throws MidiUnavailableException {
		if (midiInDeviceReceiver != null) {
			midiInDeviceReceiver.connectMidiReceiver();
		}

		connectMidiSender();
	}

	/**
	 * Connect to the MIDI out device. Also call this method, if you change the
	 * settings or want to reconnect the device.
	 * 
	 * @throws MidiUnavailableException
	 */
	public void connectMidiSender() throws MidiUnavailableException {
		// Cancel an eventually existing timer
		if (connectMidiOutDeviceTimer != null) {
			connectMidiOutDeviceTimer.cancel();
			connectMidiOutDeviceTimer = null;
		}

		if (midiOutDevice != null && midiOutDevice.isOpen()) {
			// We already have an open sender -> close this one
			try {
				midiOutDevice.close();
			} catch (Exception e) {
			}
		}

		com.ascargon.rocketshow.midi.MidiDevice midiDevice = settings.getMidiOutDevice();

		logger.trace(
				"Try connecting to output MIDI device " + midiDevice.getId() + " \"" + midiDevice.getName() + "\"");

		midiOutDevice = MidiUtil.getHardwareMidiDevice(midiDevice, MidiDirection.OUT);

		if (midiOutDevice == null) {
			logger.trace("MIDI output device not found. Try again in 5 seconds.");

			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					try {
						connectMidiSender();
					} catch (Exception e) {
						logger.debug("Could not connect to MIDI output device", e);
					}
				}
			};

			connectMidiOutDeviceTimer = new Timer();
			connectMidiOutDeviceTimer.schedule(timerTask, 5000);

			return;
		}

		// We found a device
		if (connectMidiOutDeviceTimer != null) {
			connectMidiOutDeviceTimer.cancel();
			connectMidiOutDeviceTimer = null;
		}

		midiOutDevice.open();

		// Connect all listeners
		for (MidiDeviceConnectedListener listener : midiOutDeviceConnectedListeners) {
			listener.deviceConnected(midiOutDevice);
		}

		// We found the device and all listeners are connected
		logger.info("Successfully connected to output MIDI device " + midiOutDevice.getDeviceInfo().getName());
	}

	public void playIdleSong() throws Exception {
		logger.debug("Playing the idle song...");

		if (idleSong != null) {
			// The idle song is already initialized
			return;
		}

		if (settings.getIdleSong() == null || settings.getIdleSong().length() == 0) {
			return;
		}

		logger.info("Play idle song");

		idleSong = songManager.loadSong(settings.getIdleSong());
		idleSong.setManager(this);
		idleSong.setIdleSong(true);
		idleSong.play();
	}

	public void stopIdleSong() throws Exception {
		logger.debug("Stopping the idle song...");

		if (idleSong == null) {
			return;
		}

		logger.info("Stop idle song");

		idleSong.stop();
		idleSong = null;
	}

	public void load() throws IOException {
		logger.info("Initialize...");

		// Initialize the player
		player = new Player(this);

		// Initialize the client state
		stateManager = new StateManager();
		stateManager.load(this);

		// Initialize the updater
		updater = new Updater(this);

		// Initialize the songmanager
		songManager = new SongManager(this);

		// Initialize the filemanager
		fileManager = new FileManager();

		// Initialize the session
		session = new Session();

		// Initialize the settings
		settings = new Settings();

		// Initialize the MIDI action converter
		midi2ActionConverter = new Midi2ActionConverter(this);

		// Initialize the DMX sender
		dmxSignalSender = new DmxSignalSender(this);
		midi2DmxConverter = new Midi2DmxConverter(dmxSignalSender);

		// Initialize the image displayer
		try {
			imageDisplayer = new ImageDisplayer();
		} catch (IOException e) {
			logger.error("Could not initialize image displayer", e);
		}

		// Load the settings
		try {
			loadSettings();
		} catch (Exception e) {
			logger.error("Could not load the settings", e);
		}

		// Save the settings (in case none were already existant)
		try {
			saveSettings();
		} catch (JAXBException e) {
			logger.error("Could not save settings", e);
		}

		// Set the proper logging level (map from the log4j enum to our own
		// enum)
		switch (settings.getLoggingLevel()) {
		case INFO:
			logger.setLevel(Level.INFO);
			break;
		case WARN:
			logger.setLevel(Level.WARN);
			break;
		case ERROR:
			logger.setLevel(Level.ERROR);
			break;
		case DEBUG:
			logger.setLevel(Level.DEBUG);
			break;
		case TRACE:
			logger.setLevel(Level.TRACE);
			break;
		}

		// Initialize the required objects inside settings
		if (settings.getDeviceInMidiRoutingList() != null) {
			for (MidiRouting deviceInMidiRouting : settings.getDeviceInMidiRoutingList()) {
				try {
					deviceInMidiRouting.load(this);
				} catch (MidiUnavailableException e) {
					logger.error("Could not initialize the MIDI input device routing");
				}
			}
		}

		// Initialize the MIDI receiver
		midiInDeviceReceiver = new MidiInDeviceReceiver(this);
		try {
			midiInDeviceReceiver.load();
		} catch (MidiUnavailableException e) {
			logger.error("Could not initialize the MIDI receiver", e);
		}

		// Initialize the MIDI out device
		try {
			connectMidiSender();
		} catch (MidiUnavailableException e) {
			logger.error("Could not initialize the MIDI out device", e);
		}

		// Restore the session from the file
		try {
			restoreSession();
		} catch (Exception e) {
			logger.error("Could not restore session", e);
		}

		logger.info("Finished initializing");
	}

	public void saveSettings() throws JAXBException {
		File file = new File(BASE_PATH + "settings");
		JAXBContext jaxbContext = JAXBContext.newInstance(Settings.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(settings, file);

		settings.updateSystem();

		logger.info("Settings saved");
	}

	public void loadSettings() throws Exception {
		File file = new File(BASE_PATH + "settings");

		if (!file.exists() || file.isDirectory()) {
			return;
		}

		// Restore the session from the file
		JAXBContext jaxbContext = JAXBContext.newInstance(Settings.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		settings = (Settings) jaxbUnmarshaller.unmarshal(file);

		settings.updateSystem();

		// Reset the USB interface, if needed
		try {
			if (settings.isResetUsbAfterBoot()) {
				logger.info("Resetting all USB devices");
				ResetUsb.resetAllInterfaces();
			}
		} catch (Exception e) {
			logger.error("Could not reset the USB devices", e);
		}

		// Play the idle song, if set
		playIdleSong();

		logger.info("Settings loaded");
	}

	public void saveSession() {
		if (currentSetList == null) {
			session.setCurrentSetListName("");
		} else {
			session.setCurrentSetListName(currentSetList.getName());
		}

		try {
			File file = new File(BASE_PATH + "session");
			JAXBContext jaxbContext = JAXBContext.newInstance(Session.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(session, file);

			logger.info("Session saved");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private void restoreSession() throws Exception {
		File file = new File(BASE_PATH + "session");
		if (!file.exists() || file.isDirectory()) {
			// Create a default session
			saveSession();
			return;
		}

		// Restore the session from the file
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Session.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			session = (Session) jaxbUnmarshaller.unmarshal(file);

			if (session.getCurrentSetListName() != null && session.getCurrentSetListName().length() > 0) {
				// Load the last setlist
				loadSetList(session.getCurrentSetListName());
			} else {
				// Load the default setlist
				loadSetList("");
			}

			logger.info("Session restored");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void loadSetList(String name) throws Exception {
		if (currentSetList != null) {
			currentSetList.close();
			currentSetList = null;
		}

		if (name.length() > 0) {
			// Also load a new setlist, don't only unload the current one
			currentSetList = songManager.loadSetList(name);
			currentSetList.setManager(this);
			currentSetList.setName(name);
		}

		// Read the current song file
		if (currentSetList == null) {
			logger.debug("Try setting a default song...");
			
			// We have no setlist. Simply read the first song, if available
			List<Song> songs = songManager.getAllSongs();
			
			if(songs.size() > 0) {
				logger.debug("Set default song '" + songs.get(0).getName() + "'...");
				
				player.setCurrentSong(songs.get(0));
			}
		} else {
			// We got a setlist loaded
			try {
				currentSetList.readCurrentSong();
			} catch (Exception e) {
				logger.error("Could not read current song", e);
			}
		}
		
		stateManager.notifyClients();

		saveSession();
	}

	public void close() {
		logger.info("Close...");

		try {
			player.close();
		} catch (Exception e) {
			logger.error("Could not close the player", e);
		}

		if (connectMidiOutDeviceTimer != null) {
			connectMidiOutDeviceTimer.cancel();
		}

		if (midiInDeviceReceiver != null) {
			try {
				midiInDeviceReceiver.close();
			} catch (Exception e) {
				logger.error("Could not close MIDI in device receiver", e);
			}
		}

		if (midiOutDevice != null && midiOutDevice.isOpen()) {
			try {
				midiOutDevice.close();
			} catch (Exception e) {
				logger.error("Could not close MIDI out device", e);
			}
		}

		if (currentSetList != null) {
			try {
				currentSetList.close();
			} catch (Exception e) {
				logger.error("Could not close current set list", e);
			}
		}

		try {
			stopIdleSong();
		} catch (Exception e) {
			logger.error("Could not stop idle video", e);
		}

		logger.info("Finished closing");
	}

	public void reboot() throws Exception {
		for (RemoteDevice remoteDevice : settings.getRemoteDeviceList()) {
			if (remoteDevice.isSynchronize()) {
				remoteDevice.reboot();
			}
		}

		ShellManager shellManager = new ShellManager(new String[] { "sudo", "reboot" });
		shellManager.getProcess().waitFor();
	}

	public Midi2DmxConverter getMidi2DmxConverter() {
		return midi2DmxConverter;
	}

	public SetList getCurrentSetList() {
		return currentSetList;
	}

	public void setCurrentSetList(SetList currentSetList) {
		this.currentSetList = currentSetList;
		saveSession();
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public DmxSignalSender getDmxSignalSender() {
		return dmxSignalSender;
	}

	public void setDmxSignalSender(DmxSignalSender dmxSignalSender) {
		this.dmxSignalSender = dmxSignalSender;
	}

	public Midi2ActionConverter getMidi2ActionConverter() {
		return midi2ActionConverter;
	}

	public SongManager getSongManager() {
		return songManager;
	}

	public Updater getUpdater() {
		return updater;
	}

	public Session getSession() {
		return session;
	}

	public StateManager getStateManager() {
		return stateManager;
	}

	public FileManager getFileManager() {
		return fileManager;
	}

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public MidiInDeviceReceiver getMidiInDeviceReceiver() {
		return midiInDeviceReceiver;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
