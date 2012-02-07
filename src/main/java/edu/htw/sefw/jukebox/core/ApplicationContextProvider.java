package edu.htw.sefw.jukebox.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextProvider implements ApplicationContextAware {
	private static ApplicationContext context = null;

	public static ApplicationContext getApplicationContext() {
		return context;
	}

	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		ApplicationContextProvider.context = ctx;
	}
}
