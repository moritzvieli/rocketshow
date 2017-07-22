package com.ascargon.showmachine.rest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ascargon.showmachine.midi.Startup;

public class ContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Startup s = new Startup();
		String[] args = new String[1];
		args[0] = "-l";
		try {
			s.main(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("XXXXXXXXX Starting up!");
	}

}
