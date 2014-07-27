package com.w3asel.cubesensors.api.v1.format;

import java.util.Formatter;

/**
 * Converts the pressure value returned by the API to a formatted string.
 *
 * @author Joe
 */
public class Pressure {
	private static final double MILLI_BAR_TO_IN_HG = 0.0295299830714d;

	public static String toMilliBar(final int apiValue) {
		// api returns mBar
		return String.valueOf(apiValue);
	}

	public static String toInHg(final int apiValue) {
		return toInHg(apiValue, 1);
	}

	public static String toInHg(final int apiValue, final int decimalPlaces) {
		try (Formatter format = new Formatter()) {
			format.format("%." + decimalPlaces + "f", apiValue
					* MILLI_BAR_TO_IN_HG);
			return format.toString();
		}
	}
}
