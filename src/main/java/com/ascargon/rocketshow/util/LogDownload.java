package com.ascargon.rocketshow.util;

import java.io.File;

import org.apache.log4j.Logger;

import com.ascargon.rocketshow.Manager;

/**
 * Prepares the log directory and sends it to the download.
 *
 * @author Moritz A. Vieli
 */
public class LogDownload {

	final static Logger logger = Logger.getLogger(LogDownload.class);
	
	public final static String LOGS_FILE_NAME = "logs.zip";
	
	public static File getLogsFile() throws Exception {
		// Prepare the log directory for download
		ShellManager shellManager = new ShellManager(new String[] { "sudo", "zip", "-r", "-j", Manager.BASE_PATH + LOGS_FILE_NAME, Manager.BASE_PATH + "log/*" });
		
		shellManager.getProcess().waitFor();
		
		// Return the prepared zip
		return new File(Manager.BASE_PATH + LOGS_FILE_NAME);
	}

}
