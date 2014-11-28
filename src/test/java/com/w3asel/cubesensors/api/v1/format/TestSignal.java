package com.w3asel.cubesensors.api.v1.format;

import org.junit.Assert;
import org.junit.Test;

import com.w3asel.cubesensors.api.v1.format.Signal.State;

/**
 * Unit tests on {@link Signal}.
 *
 * @author Joe
 */
public class TestSignal {
	/** test corner cases of the signal strength labels */
	@Test
	public void testToRating() {
		String expected, actual;

		expected = State.FULL.getLabel();
		actual = Signal.toRating(-State.FULL.getMaxValue() + 1);
		Assert.assertEquals(expected, actual);

		expected = State.HIGH.getLabel();
		actual = Signal.toRating(-State.FULL.getMaxValue());
		Assert.assertEquals(expected, actual);

		expected = State.HIGH.getLabel();
		actual = Signal.toRating(-State.HIGH.getMaxValue() + 1);
		Assert.assertEquals(expected, actual);

		expected = State.MEDIUM.getLabel();
		actual = Signal.toRating(-State.HIGH.getMaxValue());
		Assert.assertEquals(expected, actual);

		expected = State.MEDIUM.getLabel();
		actual = Signal.toRating(-State.MEDIUM.getMaxValue() + 1);
		Assert.assertEquals(expected, actual);

		expected = State.LOW.getLabel();
		actual = Signal.toRating(-State.MEDIUM.getMaxValue());
		Assert.assertEquals(expected, actual);

		expected = State.LOW.getLabel();
		actual = Signal.toRating(-State.LOW.getMaxValue() + 1);
		Assert.assertEquals(expected, actual);

		expected = "Unknown";
		actual = Signal.toRating(-State.LOW.getMaxValue());
		Assert.assertEquals(expected, actual);
	}
}
