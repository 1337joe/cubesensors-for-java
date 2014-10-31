package com.w3asel.cubesensors.api.v1.format;

import java.util.Formatter;

/**
 * Converts the pressure value returned by the API to a formatted string.
 *
 * @author Joe
 */
public class Pressure {
	private static final double MILLI_BAR_TO_IN_HG = 0.0295299830714d;

	/** to prevent instantiation of utility class */
	private Pressure() {
	}

	/**
	 * @param apiValue
	 *            the value returned by the by API
	 * @return the string representation of that value in milli bar
	 */
	public static String toMilliBar(final int apiValue) {
		// api returns mBar
		return String.valueOf(apiValue);
	}

	/**
	 * @param apiValue
	 *            the value returned by the by API
	 * @return the string representation of the value in inHg to one decimal place
	 */
	public static String toInHg(final int apiValue) {
		return toInHg(apiValue, 1);
	}

	/**
	 * @param apiValue
	 *            the value returned by the by API
	 * @param decimalPlaces
	 *            the number of decimal places to include
	 * @return the string representation of the value in inches mercury to the specified decimal places
	 */
	public static String toInHg(final int apiValue, final int decimalPlaces) {
		try (Formatter format = new Formatter()) {
			format.format("%." + decimalPlaces + "f", apiValue * MILLI_BAR_TO_IN_HG);
			return format.toString();
		}
	}
}
