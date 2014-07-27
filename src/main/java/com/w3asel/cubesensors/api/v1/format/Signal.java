package com.w3asel.cubesensors.api.v1.format;

/**
 * Converts the signal strength returned by the API into a rating based on the
 * levels the app uses.
 *
 * @author Joe
 */
public class Signal {
	private enum State {
		Full(60),
		High(70),
		Medium(80),
		Low(150);

		private final int maxValue;

		private State(final int maxValue) {
			this.maxValue = maxValue;
		}
	}

	public static String toRating(final int apiValue) {
		for (final State state : State.values()) {
			if (-apiValue < state.maxValue) {
				return state.name();
			}
		}
		return "Unknown";
	}
}
