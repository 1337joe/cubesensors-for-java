package com.w3asel.cubesensors.api.v1.format;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests on {@link Temperature}.
 *
 * @author Joe
 */
public class TestTemperature {
	/** test conversion to degrees C */
	@Test
	public void testToC() {
		String expected, actual;

		expected = "0.0";
		actual = Temperature.toC(0);
		Assert.assertEquals(expected, actual);

		expected = "100.0";
		actual = Temperature.toC(10000);
		Assert.assertEquals(expected, actual);

		expected = "22.5";
		actual = Temperature.toC(2249);
		Assert.assertEquals(expected, actual);

		expected = "22";
		actual = Temperature.toC(2249, 0);
		Assert.assertEquals(expected, actual);

		expected = "22.5";
		actual = Temperature.toC(2249, 1);
		Assert.assertEquals(expected, actual);

		expected = "22.49";
		actual = Temperature.toC(2249, 2);
		Assert.assertEquals(expected, actual);

		expected = "22.490";
		actual = Temperature.toC(2249, 3);
		Assert.assertEquals(expected, actual);
	}

	/** test conversion to degrees F */
	@Test
	public void testToF() {
		String expected, actual;

		expected = "32.0";
		actual = Temperature.toF(0);
		Assert.assertEquals(expected, actual);

		expected = "212.0";
		actual = Temperature.toF(10000);
		Assert.assertEquals(expected, actual);

		expected = "72.5";
		actual = Temperature.toF(2249);
		Assert.assertEquals(expected, actual);

		expected = "72";
		actual = Temperature.toF(2249, 0);
		Assert.assertEquals(expected, actual);

		expected = "72.5";
		actual = Temperature.toF(2249, 1);
		Assert.assertEquals(expected, actual);

		expected = "72.48";
		actual = Temperature.toF(2249, 2);
		Assert.assertEquals(expected, actual);

		expected = "72.482";
		actual = Temperature.toF(2249, 3);
		Assert.assertEquals(expected, actual);
	}
}
