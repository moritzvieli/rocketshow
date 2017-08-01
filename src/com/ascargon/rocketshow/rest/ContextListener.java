package com.ascargon.rocketshow.rest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ascargon.rocketshow.Manager;

public class ContextListener implements ServletContextListener {

	private Manager manager;
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		// Initialize the manager
		manager = new Manager();
		manager.load();
		
		servletContextEvent.getServletContext().setAttribute("manager", manager);
	}

}
