package com.w3asel.cubesensors.api.v1.json;

import java.util.Map;

public class JsonDeviceResponse {
	public double duration;
	public boolean ok;
	public Map<String, String> resources;
	public JsonDevice device;
}
