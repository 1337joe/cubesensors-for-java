package com.w3asel.cubesensors.api.v1.json;

import java.util.Map;

import com.w3asel.cubesensors.api.v1.Device.Type;

public class JsonDevice {
	public enum ExtraMapping {
		name,
		roomtype,
		state;
	}

	public Type type;
	public String uid;
	public Map<String, String> extra;
}
