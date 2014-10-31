package com.w3asel.cubesensors.api.v1.format;

import java.util.Formatter;

/**
 * Converts the temperature value returned by the API to a formatted string.
 *
 * @author Joe
 */
public class Temperature {
	/** multiply by this to convert the raw value the API returns to a value with the correct decimal places */
	private static final double API_TO_DECIMAL_MULTIPLIER = 1 / 100d;
	private static final double C_TO_F_MULTIPLIER = 9d / 5d;
	private static final double F_OFFSET = 32;

	/** to prevent instantiation of utility class */
	private Temperature() {
	}

	/**
	 * The API returns the value to two decimal places, defaulting to one place here because that's what the app shows.
	 *
	 * @param apiValue
	 *            the value returned by the by API
	 * @return the string representation of that value in Celsius
	 */
	public static String toC(final int apiValue) {
		return toC(apiValue, 1);
	}

	/**
	 * @param apiValue
	 *            the value returned by the by API
	 * @param decimalPlaces
	 *            the number of decimal places to include
	 * @return the string representation of the value in Celsius to the specified decimal places
	 */
	public static String toC(final int apiValue, final int decimalPlaces) {
		try (Formatter format = new Formatter()) {
			format.format("%." + decimalPlaces + "f", apiValue * API_TO_DECIMAL_MULTIPLIER);
			return format.toString();
		}
	}

	/**
	 * @param apiValue
	 *            the value returned by the by API
	 *
	 * @return the string representation of that value in Fahrenheit
	 */
	public static String toF(final int apiValue) {
		return toF(apiValue, 1);
	}

	/**
	 * @param apiValue
	 *            the value returned by the by API
	 * @param decimalPlaces
	 *            the number of decimal places to include
	 * @return the string representation of the value in Fahrenheit to the specified decimal places
	 */
	public static String toF(final int apiValue, final int decimalPlaces) {
		final double fValue = apiValue * API_TO_DECIMAL_MULTIPLIER * C_TO_F_MULTIPLIER + F_OFFSET;
		try (Formatter format = new Formatter()) {
			format.format("%." + decimalPlaces + "f", fValue);
			return format.toString();
		}
	}
}
