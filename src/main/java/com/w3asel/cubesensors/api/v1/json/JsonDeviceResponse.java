package com.w3asel.cubesensors.api.v1.json;

/**
 * /v1/devices/000D6F0003118A16<br>
 * &#123;<br>
 * &nbsp;&nbsp;"ok": true,<br>
 * &nbsp;&nbsp;"device": {@link JsonDevice}<br>
 * &#125;
 */
public class JsonDeviceResponse {
	/** response status */
	public boolean ok;
	/** the requested device */
	public JsonDevice device;
}
