package com.w3asel.cubesensors.api.v1.json;

/**
 * /v1/devices/000D6F0003118A16/span<br/>
 * {<br/>
 * &nbsp;&nbsp;"ok": true,<br/>
 * &nbsp;&nbsp;"results": [<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;["2014-07-31T00:53:00Z", 2249, 802, 46, 2460, 4, 18,
 * 49, false, false, 30290, -62],<br/>
 * &nbsp;&nbsp;...<br/>
 * &nbsp;&nbsp;],<br/>
 * &nbsp;&nbsp;"field_list": ["time", "temp", "pressure", "humidity", "voc",
 * "light", "noise", "battery", "shake", "cable", "voc_resistance", "rssi"]<br/>
 * }
 *
 * Currently no differences in structure between the span and current query
 * responses.
 */
public class JsonSpanResponse extends JsonCurrentResponse {
}
