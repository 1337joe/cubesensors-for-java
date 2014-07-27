package com.w3asel.cubesensors.api.v1.format;

import java.util.Formatter;

/**
 * Converts the temperature value returned by the API to a formatted string.
 *
 * @author Joe
 */
public class Temperature {
	private static final double C_TO_F_MULTIPLIER = 9d / 5d;

	/**
	 * The API returns the value to two decimal places, defaulting to one place
	 * here because that's what the app shows.
	 */
	public static String toC(final int apiValue) {
		return toC(apiValue, 1);
	}

	public static String toC(final int apiValue, final int decimalPlaces) {
		try (Formatter format = new Formatter()) {
			format.format("%." + decimalPlaces + "f", apiValue / 100d);
			return format.toString();
		}
	}

	public static String toF(final int apiValue) {
		return toF(apiValue, 1);
	}

	public static String toF(final int apiValue, final int decimalPlaces) {
		final double fValue = apiValue / 100d * C_TO_F_MULTIPLIER + 32;
		try (Formatter format = new Formatter()) {
			format.format("%." + decimalPlaces + "f", fValue);
			return format.toString();
		}
	}
}
