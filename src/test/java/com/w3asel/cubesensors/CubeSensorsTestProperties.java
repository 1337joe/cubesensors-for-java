package com.w3asel.cubesensors;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to simplify access to the test properties.
 *
 * @author Joe
 */
public enum CubeSensorsTestProperties {
	INSTANCE;

	private static final String FILE = "cubesensors.test.properties";

	private final Logger LOGGER = LoggerFactory
			.getLogger(CubeSensorsTestProperties.class);

	private Properties properties;

	private CubeSensorsTestProperties() {
		properties = new Properties();

		final InputStream is = CubeSensorsTestProperties.class.getClassLoader()
				.getResourceAsStream(FILE);
		try {
			properties.load(is);
		} catch (final IOException e) {
			LOGGER.error("Error loading property file.", e);
		}
	}

	public static Token getRequestToken() {
		final String tokenToken = INSTANCE.properties
				.getProperty("requestToken.token");
		final String tokenSecret = INSTANCE.properties
				.getProperty("requestToken.secret");
		return new Token(tokenToken, tokenSecret);
	}

	public static Verifier getRequestVerifier() {
		final String requestVerifier = INSTANCE.properties
				.getProperty("verifier");
		return new Verifier(requestVerifier);
	}

	public static Token getAccessToken() {
		final String tokenToken = INSTANCE.properties
				.getProperty("accessToken.token");
		final String tokenSecret = INSTANCE.properties
				.getProperty("accessToken.secret");
		return new Token(tokenToken, tokenSecret);
	}
}
