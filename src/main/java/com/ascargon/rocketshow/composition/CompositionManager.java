package com.ascargon.rocketshow.composition;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.util.FileDurationGetter;

public class CompositionManager {

	final static Logger logger = Logger.getLogger(CompositionManager.class);

	public final static String COMPOSITIONS_PATH = "compositions/";
	public final static String SETS_PATH = "sets/";

	private Manager manager;

	public CompositionManager(Manager manager) {
		this.manager = manager;
	}

	public List<Composition> getAllCompositions() throws Exception {
		File folder = new File(Manager.BASE_PATH + COMPOSITIONS_PATH);
		File[] fileList = folder.listFiles();
		List<Composition> compositionList = new ArrayList<Composition>();

		if (fileList != null) {
			for (File file : fileList) {
				if (file.isFile()) {
					try {
						Composition composition = loadComposition(file.getName());
						compositionList.add(composition);
					} catch (Exception e) {
						logger.error("Could not load composition '" + file.getName() + "'", e);
					}
				}
			}
		}

		return compositionList;
	}

	public List<Set> getAllSets() throws Exception {
		File folder = new File(Manager.BASE_PATH + SETS_PATH);
		File[] fileList = folder.listFiles();
		List<Set> setList = new ArrayList<Set>();

		if (fileList != null) {
			for (File file : fileList) {
				if (file.isFile()) {
					Set set = new Set();
					set.setName(file.getName());

					setList.add(set);
				}
			}
		}

		return setList;
	}

	public Set loadSet(String name) throws Exception {
		Set set;

		logger.debug("Loading set '" + name + "'...");

		// Load a set
		JAXBContext jaxbContext = JAXBContext.newInstance(Set.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		set = (Set) jaxbUnmarshaller.unmarshal(new File(Manager.BASE_PATH + SETS_PATH + name));

		logger.info("Set '" + name + "' successfully loaded");

		return set;
	}

	public Composition loadComposition(String name) throws Exception {
		Composition composition;

		logger.debug("Loading composition " + name + "...");

		// Load a composition
		JAXBContext jaxbContext = JAXBContext.newInstance(Composition.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		composition = (Composition) jaxbUnmarshaller.unmarshal(new File(Manager.BASE_PATH + COMPOSITIONS_PATH + name));

		composition.setName(name);
		composition.getMidiMapping().setParent(manager.getSettings().getMidiMapping());
		composition.setManager(manager);

		logger.debug("Composition '" + name + "' successfully loaded");

		return composition;
	}

	public void saveSet(Set set, boolean checkCompositions) throws Exception {
		if (checkCompositions) {
			// Update all composition information
			Iterator<SetComposition> iterator = set.getSetCompositionList().iterator();

			while (iterator.hasNext()) {
				SetComposition setComposition = iterator.next();

				Composition composition = null;

				try {
					composition = loadComposition(setComposition.getName());
				} catch (Exception e) {
				}

				if (composition == null) {
					// The composition does not exist anymore (has been deleted)
					// --> delete it from the set
					iterator.remove();
				} else {
					// The composition still exists -> update some information
					setComposition.setDurationMillis(composition.getDurationMillis());
				}
			}
		}

		File file = new File(Manager.BASE_PATH + SETS_PATH + set.getName());
		JAXBContext jaxbContext = JAXBContext.newInstance(Set.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(set, file);

		logger.info("Set '" + set.getName() + "' saved");
	}

	public void saveSet(Set set) throws Exception {
		saveSet(set, true);
	}

	public void saveComposition(Composition composition) throws Exception {
		// Get the duration of each file
		ExecutorService executor = Executors.newFixedThreadPool(30);

		for (com.ascargon.rocketshow.composition.File file : composition.getFileList()) {
			Runnable fileDurationGetter = new FileDurationGetter(file);
			executor.execute(fileDurationGetter);
		}

		executor.shutdown();

		// Wait until all threads are finish
		while (!executor.isTerminated()) {
		}

		// Set the duration of the composition to the maximum duration of the
		// files
		long maxDuration = 0;

		for (com.ascargon.rocketshow.composition.File file : composition.getFileList()) {
			if (file.getDurationMillis() > maxDuration) {
				maxDuration = file.getDurationMillis();
			}
		}
		composition.setDurationMillis(maxDuration);

		// Save the composition in XML
		File file = new File(Manager.BASE_PATH + COMPOSITIONS_PATH + composition.getName());
		JAXBContext jaxbContext = JAXBContext.newInstance(Composition.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(composition, file);

		// TODO Only update the information of this composition in the sets,
		// don't check all other composition
		updateSet();

		logger.info("Composition '" + composition.getName() + "' saved");
	}

	public void deleteComposition(String name) throws Exception {
		// Delete the composition
		File file = new File(Manager.BASE_PATH + COMPOSITIONS_PATH + name);

		if (!file.exists()) {
			updateSet();
			return;
		} else {
			file.delete();
			updateSet();
		}

		// TODO What do we do, if this is the current composition?

		logger.info("Composition '" + name + "' deleted");
	}

	public void deleteSet(String name) throws Exception {
		// Delete the set
		File file = new File(Manager.BASE_PATH + SETS_PATH + name);

		if (file.exists()) {
			file.delete();
		}

		logger.info("Set '" + name + "' deleted");
	}

	private void updateSet() throws Exception {
		// Update all sets (remove deleted files, update playing times),
		// when a composition has been changed/deleted

		List<Set> sets = getAllSets();

		for (Set set : sets) {
			// Load the full set
			Set fullSet = loadSet(set.getName());
			saveSet(fullSet);
		}
	}

}
