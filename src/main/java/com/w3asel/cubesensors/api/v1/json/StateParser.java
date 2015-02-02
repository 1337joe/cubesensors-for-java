package com.w3asel.cubesensors.api.v1.json;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.w3asel.cubesensors.api.v1.State;

/**
 * Utility class to convert from the arrays returned by the API to {@link State} objects.
 *
 * @author Joe
 */
public class StateParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(StateParser.class);

	private enum ExpectedLabels {
		time,
		temp,
		pressure,
		humidity,
		voc,
		light,
		noise,
		noisedba,
		battery,
		shake,
		cable,
		voc_resistance,
		rssi
	}

	/**
	 * Produces a mapping of label to index for use in parsing state values to the appropriate slot in the state object.
	 */
	private static Map<ExpectedLabels, Integer> mapLabels(final List<String> labels) {
		final Map<ExpectedLabels, Integer> map = new EnumMap<>(ExpectedLabels.class);

		final List<ExpectedLabels> unusedLabels = new ArrayList<>(Arrays.asList(ExpectedLabels.values()));
		for (int index = 0; index < labels.size(); index++) {
			final String next = labels.get(index);
			ExpectedLabels labelValue;
			try {
				labelValue = ExpectedLabels.valueOf(next);
			} catch (final IllegalArgumentException e) {
				labelValue = null;
			}

			if (labelValue != null) {
				unusedLabels.remove(labelValue);
				if (map.containsKey(labelValue)) {
					LOGGER.warn("Duplicate state label: {} ({})", next, labels);
				}
				map.put(labelValue, index);
			} else {
				LOGGER.warn("Unexpected state label: {}", next);
			}
		}
		for (final ExpectedLabels label : unusedLabels) {
			LOGGER.warn("Unused label: {}", label);
		}

		return map;
	}

	/**
	 * Uses the provided label-index mapping to extract state values and create a new State object.
	 */
	private static State extractValues(final List<Object> values, final Map<ExpectedLabels, Integer> map) {
		final ZonedDateTime time = ZonedDateTime.parse((String) values.get(map.get(ExpectedLabels.time)));
		final int temp = safeInt((Integer) values.get(map.get(ExpectedLabels.temp)));
		final int pressure = safeInt((Integer) values.get(map.get(ExpectedLabels.pressure)));
		final int humidity = safeInt((Integer) values.get(map.get(ExpectedLabels.humidity)));
		final int voc = safeInt((Integer) values.get(map.get(ExpectedLabels.voc)));
		final int light = safeInt((Integer) values.get(map.get(ExpectedLabels.light)));
		final int noise = safeInt((Integer) values.get(map.get(ExpectedLabels.noise)));
		final int noisedba = safeInt((Integer) values.get(map.get(ExpectedLabels.noisedba)));
		final int battery = safeInt((Integer) values.get(map.get(ExpectedLabels.battery)));
		final boolean shake = safeBoolean((Boolean) values.get(map.get(ExpectedLabels.shake)));
		final boolean cable = safeBoolean((Boolean) values.get(map.get(ExpectedLabels.cable)));
		final int vocResistance = safeInt((Integer) values.get(map.get(ExpectedLabels.voc_resistance)));
		final int rssi = safeInt((Integer) values.get(map.get(ExpectedLabels.rssi)));
		return new State(time, temp, pressure, humidity, voc, light, noise, noisedba, battery, shake, cable, vocResistance, rssi);
	}

	private static int safeInt(Integer i) {
		return i == null ? 0 : i;
	}
	
	private static boolean safeBoolean(Boolean b) {
		return b == null ? false : b;
	}
	
	/**
	 * @param labels
	 *            the list of labels returned by the query
	 * @param values
	 *            the list of lists of values to map to those labels
	 * @return the decoded states
	 */
	public static List<State> parseState(final List<String> labels, final List<List<Object>> values) {
		final Map<ExpectedLabels, Integer> map = mapLabels(labels);

		final List<State> states = new ArrayList<>(values.size());

		for (final List<Object> state : values) {
			states.add(extractValues(state, map));
		}

		return states;
	}
}
