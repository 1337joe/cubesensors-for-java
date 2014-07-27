package com.w3asel.cubesensors.api.v1.json;

import java.util.List;

public class JsonDevicesResponse {
	public double duration;
	public boolean ok;
	public List<String> resources;
	public List<JsonDevice> devices;
}
