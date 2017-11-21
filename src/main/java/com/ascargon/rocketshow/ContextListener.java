package com.ascargon.rocketshow;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class ContextListener implements ServletContextListener {

	final static Logger logger = Logger.getLogger(ContextListener.class);
	
	private Manager manager;
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// Destroy the manager
		manager.close();
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		// Initialize the manager
		manager = new Manager();
		
		try {
			manager.load();
		} catch (IOException e) {
			logger.error("Could not load the manager", e);
		}
		
		servletContextEvent.getServletContext().setAttribute("manager", manager);
	}

}
