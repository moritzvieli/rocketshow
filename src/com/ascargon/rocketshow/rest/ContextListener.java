package com.ascargon.rocketshow.rest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ascargon.rocketshow.Manager;
import com.ascargon.rocketshow.midi.Startup;

public class ContextListener implements ServletContextListener {

	private Manager manager;
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		manager = new Manager();
		manager.init();
		
		servletContextEvent.getServletContext().setAttribute("manager", manager);
		
		
		
		
		// TODO
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
