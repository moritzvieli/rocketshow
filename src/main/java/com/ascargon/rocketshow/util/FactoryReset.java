package com.ascargon.rocketshow.util;

/**
 * Resets the whole application to its state where it was installed.
 *
 * @author Moritz A. Vieli
 */
public class FactoryReset {

	public static void reset() throws Exception {
		// Reset the interface
		ShellManager shellManager = new ShellManager(new String[] { "sudo", "/opt/rocketshow_reset.sh" });

		shellManager.getProcess().waitFor();
	}

}
