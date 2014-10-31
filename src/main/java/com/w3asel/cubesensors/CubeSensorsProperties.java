package com.w3asel.cubesensors;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to simplify access to the data in cubesensors.properties.
 *
 * @author Joe
 */
public class CubeSensorsProperties {
	private static final Logger LOGGER = LoggerFactory.getLogger(CubeSensorsProperties.class);
	private static final String FILE = "cubesensors.properties";

	private static Properties properties = new Properties();
	static {
		final InputStream is = CubeSensorsProperties.class.getClassLoader().getResourceAsStream(FILE);
		try {
			properties.load(is);
		} catch (final IOException e) {
			LOGGER.error("Error loading property file.", e);
		}
	}

	/** to prevent instantiation of utility class */
	private CubeSensorsProperties() {
	}

	/** @return the oAuth app API key for your app */
	public static String getAppKey() {
		return properties.getProperty("app.key");
	}

	/** @return the oAuth app API secret for your app */
	public static String getAppSecret() {
		return properties.getProperty("app.secret");
	}

	/** @return the oAuth callback url to direct to after the user registers your app to have access to their data */
	public static String getAppCallbackUrl() {
		return properties.getProperty("app.callback.url", "oob");
	}
}
