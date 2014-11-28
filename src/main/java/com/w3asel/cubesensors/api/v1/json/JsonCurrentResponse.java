package com.w3asel.cubesensors.api.v1.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * /v1/devices/000D6F0003118A16/current<br>
 * {<br>
 * &nbsp;&nbsp;"ok": true,<br>
 * &nbsp;&nbsp;"results": [<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;["2014-07-31T00:53:00Z", 2249, 802, 46, 2460, 4, null, 47, 49, false, false, 30290, -62]<br>
 * &nbsp;&nbsp;],<br>
 * &nbsp;&nbsp;"field_list": ["time", "temp", "pressure", "humidity", "voc", "light", "noise", "noisedba", "battery", "shake", "cable", "voc_resistance",
 * "rssi"]<br>
 * }
 */
public class JsonCurrentResponse {
	/** response status */
	public boolean ok;
	/** the list of field values */
	public List<List<Object>> results;
	/** the list of field names in the same order as the result values */
	@JsonProperty("field_list")
	public List<String> fieldList;
}
