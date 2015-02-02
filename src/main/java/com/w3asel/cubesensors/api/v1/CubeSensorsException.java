package com.w3asel.cubesensors.api.v1;

/**
 * Runtime exception to be thrown by the CubeSensors API.
 *
 * @author Silviu Marcu
 */
public class CubeSensorsException extends RuntimeException {

	/** @see RuntimeException#RuntimeException() */
	public CubeSensorsException() {
		super();
	}

	/** @see RuntimeException#RuntimeException(String, Throwable, boolean, boolean) */
	public CubeSensorsException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/** @see RuntimeException#RuntimeException(String, Throwable) */
	public CubeSensorsException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/** @see RuntimeException#RuntimeException(String) */
	public CubeSensorsException(final String message) {
		super(message);
	}

	/** @see RuntimeException#RuntimeException(Throwable) */
	public CubeSensorsException(final Throwable cause) {
		super(cause);
	}
}
