package com.w3asel.cubesensors.api.v1.format;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests on {@link Pressure}.
 *
 * @author Joe
 */
public class TestPressure {

	@Test
	public void testToMillibar() {
		String expected, actual;

		expected = "802";
		actual = Pressure.toMilliBar(802);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testToInHg() {
		String expected, actual;

		expected = "23.7";
		actual = Pressure.toInHg(802);
		Assert.assertEquals(expected, actual);

		expected = "23.7";
		actual = Pressure.toInHg(802, 1);
		Assert.assertEquals(expected, actual);

		expected = "23.683";
		actual = Pressure.toInHg(802, 3);
		Assert.assertEquals(expected, actual);
	}
}
