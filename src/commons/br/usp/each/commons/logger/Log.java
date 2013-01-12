package br.usp.each.commons.logger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log {
	
	private Logger logger;

	public Log(Class<?> clazz) {
		logger = Logger.getLogger(clazz);
	}
		
	public void warn(String message, Object... o) {
		logger.warn(String.format(message, o));
	}
	
	public void info(String message, Object... o) {
		logger.info(String.format(message, o));
	}
	
	public void debug(String message, Object... o) {
		logger.debug(String.format(message, o));
	}
	
	public void error(String message, Object... o) {
		logger.error(String.format(message, o));
	}
	
	public void fatal(String message, Object... o) {
		logger.fatal(String.format(message, o));
	}
	
	public void warn(String message, Throwable t, Object... o) {
		logger.warn(String.format(message, o), t);
	}
	
	public void info(String message, Throwable t, Object... o) {
		logger.info(String.format(message, o), t);
	}
	
	public void debug(String message, Throwable t, Object... o) {
		logger.debug(String.format(message, o), t);
	}
	
	public void error(String message, Throwable t, Object... o) {
		logger.error(String.format(message, o), t);
	}
	
	public void fatal(String message, Throwable t, Object... o) {
		logger.fatal(String.format(message, o), t);
	}
	
	public void setLevel(Level level) {
		logger.setLevel(level);
	}
}
