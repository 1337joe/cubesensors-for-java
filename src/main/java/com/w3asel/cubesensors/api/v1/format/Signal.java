package com.w3asel.cubesensors.api.v1.format;

/**
 * Converts the signal strength returned by the API into a rating based on the levels the app uses.
 *
 * @author Joe
 */
public class Signal {
	/** to prevent instantiation of utility class */
	private Signal() {
	}

	/** values defined by the updateSignal() function in the webapp */
	enum State {
		FULL("Full", 60),
		HIGH("High", 70),
		MEDIUM("Medium", 80),
		LOW("Low", 150);

		private final String label;
		private final int maxValue;

		private State(final String label, final int maxValue) {
			this.label = label;
			this.maxValue = maxValue;
		}

		public String getLabel() {
			return label;
		}

		public int getMaxValue() {
			return maxValue;
		}
	}

	/**
	 * @param apiValue
	 *            the signal rssi value returned by the API
	 * @return the signal strength rating as defined by the webapp
	 */
	public static String toRating(final int apiValue) {
		for (final State state : State.values()) {
			if (-apiValue < state.getMaxValue()) {
				return state.getLabel();
			}
		}
		return "Unknown";
	}
}
