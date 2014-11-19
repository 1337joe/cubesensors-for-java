package com.w3asel.cubesensors.api.v1.json;

import java.util.Map;

import com.w3asel.cubesensors.api.v1.Device.Type;

/**
 * &#123;<br>
 * &nbsp;&nbsp;"type": "cube",<br>
 * &nbsp;&nbsp;"uid": "000D6F0003118A16",<br>
 * &nbsp;&nbsp;"extra": {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;"name": "Loft",<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;"roomtype": "work"<br>
 * &nbsp;&nbsp;}<br>
 * &#125;
 */
public class JsonDevice {
	/** expected keys for the {@code extra} map */
	public enum ExtraMapping {
		/** device name (from cube configuration) */
		name,
		/** room type (from cube configuration) */
		roomtype,
	}

	/** the type of device */
	public Type type;
	/** the unique identifier of the device */
	public String uid;
	/** extra data */
	public Map<String, String> extra;
}
