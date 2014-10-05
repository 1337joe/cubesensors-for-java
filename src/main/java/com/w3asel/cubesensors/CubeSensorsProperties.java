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
public enum CubeSensorsProperties {
	INSTANCE;

	private static final String FILE = "cubesensors.properties";

	private final Logger LOGGER = LoggerFactory
			.getLogger(CubeSensorsProperties.class);

	private Properties properties;

	private CubeSensorsProperties() {
		properties = new Properties();

		final InputStream is = CubeSensorsProperties.class.getClassLoader()
				.getResourceAsStream(FILE);
		try {
			properties.load(is);
		} catch (final IOException e) {
			LOGGER.error("Error loading property file.", e);
		}
	}

	public static String getAppKey() {
		return INSTANCE.properties.getProperty("app.key");
	}

	public static String getAppSecret() {
		return INSTANCE.properties.getProperty("app.secret");
	}

	public static String getAppCallbackUrl() {
		return INSTANCE.properties.getProperty("app.callback.url", "oob");
	}
}
