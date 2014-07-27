package com.w3asel.cubesensors.api.v1.json;

import java.util.List;
import java.util.Map;

public class JsonCurrentResponse {
	public String device_url;
	public Map<String, String> cube;
	public boolean ok;
	public double duration;
	public List<List<Object>> results;
	public List<String> field_list;
}
