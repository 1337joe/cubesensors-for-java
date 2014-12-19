package com.w3asel.cubesensors.api.v1;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

/**
 * This class provides methods that map to each of the queries made available by the <a href="https://my.cubesensors.com/docs">CubeSensors API</a>.
 *
 * @author Joe
 */
public class CubeSensorsApiV1 {
	private static final Logger LOGGER = LoggerFactory.getLogger(CubeSensorsApiV1.class);

	private static final String RESOURCES_ROOT = "http://api.cubesensors.com/v1/";
	private static final String DEVICES_PATH = "devices/";
	private static final String MEDIA_TYPE_APPLICATION_JSON = "application/json";
	private static final String HTTP_HEADER_ACCEPT = "Accept";

	/** mapper for parsing json */
	private static final ObjectMapper MAPPER = new ObjectMapper();

	/** service to handle signing API queries */
	private static OAuthService service = new ServiceBuilder().provider(CubeSensorsAuthApi.class).apiKey(CubeSensorsProperties.getAppKey())
			.apiSecret(CubeSensorsProperties.getAppSecret()).signatureType(SignatureType.QueryString).build();

	/**
	 * Rebuilds the {@code service} object with debug turned on (defaulted to {@code System.out})
	 */
	public static void debug() {
		service = new ServiceBuilder().debug().provider(CubeSensorsAuthApi.class).apiKey(CubeSensorsProperties.getAppKey())
				.apiSecret(CubeSensorsProperties.getAppSecret()).signatureType(SignatureType.QueryString).build();
	}

	private final Token accessToken;

	/**
	 * @param accessToken
	 *            the access token to be used when querying the API
	 */
	public CubeSensorsApiV1(final Token accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * Parses the results of a query and handles any errors.
	 */
	private <T> T parseQuery(final String response, final Class<T> responseClass) {
		T queryResponse;
		try {
			/*
			 * Possible exceptions:
			 *
			 * IOException - if the underlying input source has problems during parsing
			 *
			 * JsonParseException - if parser has problems parsing content
			 *
			 * JsonMappingException - if the parser does not have any more content to map (note: Json "null" value is considered content; enf-of-stream not)
			 */
			queryResponse = MAPPER.readValue(response, responseClass);
		} catch (JsonParseException | JsonMappingException e) {
			try {
				final ErrorResponse error = MAPPER.readValue(response, ErrorResponse.class);

				LOGGER.error("Query returned an error: {}", error);
				return null;
			} catch (final IOException e1) {
				LOGGER.error("Failed to read error response.", e1);
			}
			LOGGER.error("Error reading response.", e);
			return null;
		} catch (final IOException e) {
			LOGGER.error("Error reading response.", e);
			return null;
		}

		return queryResponse;
	}

	/**
	 * Converts the {@link JsonDevice} object into a {@link Device} and logs any missing/extra fields to debug.
	 */
	private Device extractDevice(final JsonDevice device) {
		final Map<ExtraMapping, String> extras = new EnumMap<>(ExtraMapping.class);
		final Set<String> keys = new HashSet<>(device.extra.keySet());
		for (final ExtraMapping extra : ExtraMapping.values()) {
			extras.put(extra, device.extra.get(extra.name()));

			if (keys.contains(extra.name())) {
				keys.remove(extra.name());
			} else {
				LOGGER.debug("\"extra\" missing key \"{}\": {}", extra.name(), device.extra.toString());
			}
		}
		for (final String key : keys) {
			LOGGER.debug("Unexpected key in \"extra\": {}", key);
		}

		return new Device(device, extras);
	}

	/** @return the list of accessible devices */
	public List<Device> getDevices() {
		final String queryUrl = RESOURCES_ROOT + DEVICES_PATH;
		LOGGER.trace("Querying: {}", queryUrl);

		final OAuthRequest request = new OAuthRequest(Verb.GET, queryUrl);
		request.getHeaders().put(HTTP_HEADER_ACCEPT, MEDIA_TYPE_APPLICATION_JSON);
		service.signRequest(accessToken, request);
		final Response response = request.send();
		LOGGER.trace("Response: {}", response.getBody());

		if (!response.isSuccessful()) {
			throw new CubeSensorsException(response.getBody());
		} 
		
		final JsonDevicesResponse queryResponse = parseQuery(response.getBody(), JsonDevicesResponse.class);
		if (queryResponse == null) {
			return new ArrayList<>();
		}

		LOGGER.debug("Retrieved {} devices.", queryResponse.devices.size());

		final List<Device> devices = new ArrayList<Device>();
		for (final JsonDevice device : queryResponse.devices) {
			devices.add(extractDevice(device));
		}

		return devices;
	}

	/**
	 * @param uid
	 *            the cube id to query for
	 * @return the description of a device
	 */
	public Device getDevice(final String uid) {
		final String queryUrl = RESOURCES_ROOT + DEVICES_PATH + uid;
		LOGGER.trace("Querying: {}", queryUrl);

		final OAuthRequest request = new OAuthRequest(Verb.GET, queryUrl);
		request.getHeaders().put(HTTP_HEADER_ACCEPT, MEDIA_TYPE_APPLICATION_JSON);
		service.signRequest(accessToken, request);
		final Response response = request.send();
		LOGGER.trace("Response: {}", response.getBody());

		if (!response.isSuccessful()) {
			throw new CubeSensorsException(response.getBody());
		} 
		
		final JsonDeviceResponse queryResponse = parseQuery(response.getBody(), JsonDeviceResponse.class);
		if (queryResponse == null) {
			return null;
		}

		LOGGER.debug("Retrieved device {}.", queryResponse.device.uid);

		return extractDevice(queryResponse.device);
	}

	/**
	 * @param uid
	 *            the cube id to query for
	 * @return the current state of the cube
	 */
	public State getCurrent(final String uid) {
		final String queryUrl = RESOURCES_ROOT + DEVICES_PATH + uid + "/current";
		LOGGER.trace("Querying: {}", queryUrl);

		final OAuthRequest request = new OAuthRequest(Verb.GET, queryUrl);
		request.getHeaders().put(HTTP_HEADER_ACCEPT, MEDIA_TYPE_APPLICATION_JSON);
		service.signRequest(accessToken, request);
		final Response response = request.send();
		LOGGER.trace("Response: {}", response.getBody());

		if (!response.isSuccessful()) {
			throw new CubeSensorsException(response.getBody());
		}
		
		final JsonCurrentResponse queryResponse = parseQuery(response.getBody(), JsonCurrentResponse.class);
		if (queryResponse == null) {
			return null;
		}

		final List<State> states = StateParser.parseState(queryResponse.fieldList, queryResponse.results);
		// can happen if the cube hasn't reported in recently enough
		if (states.isEmpty()) {
			return null;
		}
		return states.get(0);
	}

	/**
	 * Queries for a list of states over the time span specified. Leaving a field {@code null} will default to the API defaults.
	 *
	 * @param uid
	 *            the UID of the device to request data for
	 * @param start
	 *            the query start time
	 * @param end
	 *            the query end time
	 * @param resolution
	 *            the resolution in minutes
	 * @return a list of all states returned by the API
	 */
	public List<State> getSpan(final String uid, final ZonedDateTime start, final ZonedDateTime end, final Integer resolution) {
		final String queryUrl = RESOURCES_ROOT + DEVICES_PATH + uid + "/span";
		LOGGER.trace("Querying: {}", queryUrl);

		final OAuthRequest request = new OAuthRequest(Verb.GET, queryUrl);
		request.getHeaders().put(HTTP_HEADER_ACCEPT, MEDIA_TYPE_APPLICATION_JSON);

		if (start != null) {
			final ZonedDateTime startUtc = start.withZoneSameInstant(ZoneId.of("Z")).truncatedTo(ChronoUnit.SECONDS);
			request.addQuerystringParameter("start", startUtc.toString());
			LOGGER.trace("Adding querystring parameter {}={}", "start", startUtc);
		}
		if (end != null) {
			final ZonedDateTime endUtc = end.withZoneSameInstant(ZoneId.of("Z")).truncatedTo(ChronoUnit.SECONDS);
			request.addQuerystringParameter("end", endUtc.toString());
			LOGGER.trace("Adding querystring parameter {}={}", "end", endUtc);
		}
		if (resolution != null) {
			request.addQuerystringParameter("resolution", resolution.toString());
			LOGGER.trace("Adding querystring parameter {}={}", "resolution", resolution);
		}

		service.signRequest(accessToken, request);
		final Response response = request.send();
		LOGGER.trace("Response: {}", response.getBody());

		if (!response.isSuccessful()) {
			throw new CubeSensorsException(response.getBody());
		}
		
		final JsonSpanResponse queryResponse = parseQuery(response.getBody(), JsonSpanResponse.class);
		if (queryResponse == null) {
			return new ArrayList<>();
		}

		final List<State> states = StateParser.parseState(queryResponse.fieldList, queryResponse.results);

		LOGGER.debug("Retrieved {} states.", states.size());

		return states;
	}
}
