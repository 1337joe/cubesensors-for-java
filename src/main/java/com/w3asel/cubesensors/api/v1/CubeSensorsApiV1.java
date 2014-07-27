package com.w3asel.cubesensors.api.v1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.w3asel.cubesensors.CubeSensorsProperties;
import com.w3asel.cubesensors.api.v1.json.ErrorResponse;
import com.w3asel.cubesensors.api.v1.json.JsonCurrentResponse;
import com.w3asel.cubesensors.api.v1.json.JsonDevice;
import com.w3asel.cubesensors.api.v1.json.JsonDevice.ExtraMapping;
import com.w3asel.cubesensors.api.v1.json.JsonDeviceResponse;
import com.w3asel.cubesensors.api.v1.json.JsonDevicesResponse;
import com.w3asel.cubesensors.api.v1.json.JsonSpanResponse;
import com.w3asel.cubesensors.api.v1.json.StateParser;
import com.w3asel.cubesensors.auth.CubeSensorsAuthApi;

public class CubeSensorsApiV1 {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CubeSensorsApiV1.class);

	private static final String RESOURCES_ROOT = "http://api.cubesensors.com/v1/";

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static OAuthService service = new ServiceBuilder()
			.provider(CubeSensorsAuthApi.class)
			.apiKey(CubeSensorsProperties.getAppKey())
			.apiSecret(CubeSensorsProperties.getAppSecret())
			.signatureType(SignatureType.QueryString).build();

	public static void debug() {
		service = new ServiceBuilder().debug()
				.provider(CubeSensorsAuthApi.class)
				.apiKey(CubeSensorsProperties.getAppKey())
				.apiSecret(CubeSensorsProperties.getAppSecret())
				.signatureType(SignatureType.QueryString).build();
	}

	/**
	 * Creates an api instance from the provided access token.
	 */
	public static CubeSensorsApiV1 fromAccessToken(final Token accessToken) {
		return new CubeSensorsApiV1(accessToken);
	}

	private final Token accessToken;

	private CubeSensorsApiV1(final Token accessToken) {
		this.accessToken = accessToken;
	}

	private <T> T parseQuery(final String response, final Class<T> responseClass) {
		T queryResponse;
		try {
			/*
			 * Possible exceptions:
			 *
			 * IOException - if the underlying input source has problems during
			 * parsing
			 *
			 * JsonParseException - if parser has problems parsing content
			 *
			 * JsonMappingException - if the parser does not have any more
			 * content to map (note: Json "null" value is considered content;
			 * enf-of-stream not)
			 */
			queryResponse = MAPPER.readValue(response, responseClass);
		} catch (JsonParseException | JsonMappingException e) {
			try {
				final ErrorResponse error = MAPPER.readValue(response,
						ErrorResponse.class);

				LOGGER.error("Query returned an error: {}", error);
				return null;
			} catch (final IOException e1) {
				// fall-through to generic response
			}
			LOGGER.error("Error reading response", e);
			return null;
		} catch (final IOException e) {
			LOGGER.error("Error reading response", e);
			return null;
		}

		return queryResponse;
	}

	private Device extractDevice(final JsonDevice device) {
		final EnumMap<ExtraMapping, String> extras = new EnumMap<>(
				ExtraMapping.class);
		final Set<String> keys = new HashSet<>(device.extra.keySet());
		for (final ExtraMapping extra : ExtraMapping.values()) {
			extras.put(extra, device.extra.get(extra.name()));

			if (keys.contains(extra.name())) {
				keys.remove(extra.name());
			} else {
				LOGGER.debug("\"extra\" missing key \"{}\": {}", extra.name(),
						device.extra.toString());
			}
		}
		for (final String key : keys) {
			LOGGER.debug("Unexpected key in \"extra\": {}", key);
		}

		return new Device(device, extras);
	}

	public List<Device> getDevices() {
		final String queryUrl = RESOURCES_ROOT + "devices/";
		LOGGER.trace("Querying: {}", queryUrl);

		final OAuthRequest request = new OAuthRequest(Verb.GET, queryUrl);
		service.signRequest(accessToken, request);
		final Response response = request.send();
		LOGGER.trace("Response: {}", response.getBody());

		final JsonDevicesResponse queryResponse = parseQuery(
				response.getBody(), JsonDevicesResponse.class);
		if (queryResponse == null) {
			return null;
		}

		LOGGER.debug("Retrieved {} devices in {} seconds.",
				queryResponse.devices.size(), queryResponse.duration);

		final List<Device> devices = new ArrayList<Device>();
		for (final JsonDevice device : queryResponse.devices) {
			devices.add(extractDevice(device));
		}

		return devices;
	}

	public Device getDevice(final String uid) {
		final String queryUrl = RESOURCES_ROOT + "devices/" + uid;
		LOGGER.trace("Querying: {}", queryUrl);

		final OAuthRequest request = new OAuthRequest(Verb.GET, queryUrl);
		service.signRequest(accessToken, request);
		final Response response = request.send();
		LOGGER.trace("Response: {}", response.getBody());

		final JsonDeviceResponse queryResponse = parseQuery(response.getBody(),
				JsonDeviceResponse.class);
		if (queryResponse == null) {
			return null;
		}

		LOGGER.debug("Retrieved device {} in {} seconds.",
				queryResponse.device.uid, queryResponse.duration);

		return extractDevice(queryResponse.device);
	}

	public State getCurrent(final String uid) {
		final String queryUrl = RESOURCES_ROOT + "devices/" + uid + "/current";
		LOGGER.trace("Querying: {}", queryUrl);

		final OAuthRequest request = new OAuthRequest(Verb.GET, queryUrl);
		service.signRequest(accessToken, request);
		final Response response = request.send();
		LOGGER.trace("Response: {}", response.getBody());

		final JsonCurrentResponse queryResponse = parseQuery(
				response.getBody(), JsonCurrentResponse.class);
		if (queryResponse == null) {
			return null;
		}

		final List<State> states = StateParser.parseState(
				queryResponse.field_list, queryResponse.results);
		// can happen if the cube hasn't reported in recently enough
		if (states.isEmpty()) {
			return null;
		}
		return states.get(0);
	}

	public List<State> getSpan(final String uid) {
		final String queryUrl = RESOURCES_ROOT + "devices/" + uid + "/span";
		LOGGER.trace("Querying: {}", queryUrl);

		final OAuthRequest request = new OAuthRequest(Verb.GET, queryUrl);
		service.signRequest(accessToken, request);
		final Response response = request.send();
		LOGGER.trace("Response: {}", response.getBody());

		final JsonSpanResponse queryResponse = parseQuery(response.getBody(),
				JsonSpanResponse.class);
		if (queryResponse == null) {
			return null;
		}

		final List<State> states = StateParser.parseState(
				queryResponse.field_list, queryResponse.results);

		LOGGER.debug("Retrieved {} states", states.size());

		return states;
	}
}
