package com.w3asel.cubesensors.api.v1;

public class CubeSensorsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CubeSensorsException() {
		super();
	}

	public CubeSensorsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CubeSensorsException(String message, Throwable cause) {
		super(message, cause);
	}

	public CubeSensorsException(String message) {
		super(message);
	}

	public CubeSensorsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}
