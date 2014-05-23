package zuna.metric.cohesion.c3;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import tml.Configuration;

public class TMLConfiguration extends Configuration{

	private static Logger logger = Logger.getLogger(Configuration.class);
	
	/** The actual properties */
	private static Properties properties = null;
	/** If TML will run in debug mode */
	private static boolean debugMode = false;
	/** Path to the execution context */
	private static String contextPath;
	/** Folder for TML to work */
	private static String tmlFolder;
	/** */
	public static String getTmlFolder() {
		if(tmlFolder==null)
			try {
				getTmlProperties();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		return tmlFolder;
	}

	public static String getReport() {
		if(properties == null)
			return "Configuration has not been initialized.";
		
		return properties.toString();
	}
	
	public void getReport(PrintStream out) {
		if(properties == null)
			out.append("Configuration has not been initialized.");
		
		properties.list(out);
	}
	
	public static String getContextPath() {
		return contextPath;
	}

	/**
	 * @return the debugMode
	 */
	public static boolean isDebugMode() {
		return debugMode;
	}


	/**
	 * @return the set the default properties for TML
	 * @throws IOException
	 */
	public static Properties getTmlProperties(boolean debugMode) throws IOException {
		return getTmlProperties(debugMode, null);
	}

	/**
	 * Set TML properties
	 * @param prop
	 */
	public static void setProperties(Properties prop) {
		properties = prop;
	}
	
}
