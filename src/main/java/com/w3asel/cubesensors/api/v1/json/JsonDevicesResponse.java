package com.w3asel.cubesensors.api.v1.json;

import java.util.List;

/**
 * /v1/devices/<br/>
 * &#123;<br/>
 * &nbsp;&nbsp;"ok": true,<br/>
 * &nbsp;&nbsp;"devices": [<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;{@link JsonDevice},<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;...<br/>
 * &nbsp;&nbsp;]<br/>
 * &#125;
 */
public class JsonDevicesResponse {
	public boolean ok;
	public List<JsonDevice> devices;
}
