package com.w3asel.cubesensors.api.v1;

import java.util.Map;

import com.w3asel.cubesensors.api.v1.json.JsonDevice;
import com.w3asel.cubesensors.api.v1.json.JsonDevice.ExtraMapping;

/**
 * Encapsulates all the data returned by the device and devices queries.
 *
 * @author Joe
 */
public class Device {
	/** the type of device, API currently states that 'cube' is the only supported value */
	public enum Type {
		/** cubesensor */
		cube,
	}

	/** the type of room as set in the cube configuration */
	public enum RoomType {
		/** Work (optimized for productivity) */
		work,
		/** Sleep (optimized for rest) */
		sleep,
		/** Live (optimized for comfort) */
		live,
	}

	/** the type of this device */
	public final Type type;
	/** the unique id of this device */
	public final String uid;
	/** the user-configured name of this device */
	public final String name;
	/** the user-configured room type of this device */
	public final RoomType roomType;

	/**
	 * Creates a fully-populated {@link Device}
	 *
	 * @param device
	 *            the API-returned device object
	 * @param extras
	 *            any extra values provided by the API
	 */
	public Device(final JsonDevice device, final Map<ExtraMapping, String> extras) {
		this.type = device.type;
		this.uid = device.uid;
		this.name = extras.get(ExtraMapping.name);
		this.roomType = extras.get(ExtraMapping.roomtype) == null ? null : RoomType.valueOf(extras.get(ExtraMapping.roomtype));
	}

	/** @return the type of this device */
	public Type getType() {
		return type;
	}

	/** @return the unique id of this device */
	public String getUid() {
		return uid;
	}

	/** @return the user-configured name of this device */
	public String getName() {
		return name;
	}

	/** @return the user-configured room type of this device */
	public RoomType getRoomType() {
		return roomType;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append("type:").append(type);
		sb.append(", ");
		sb.append("uid:").append(uid);
		if (name != null) {
			sb.append(", ");
			sb.append("name:").append(name);
		}
		if (roomType != null) {
			sb.append(", ");
			sb.append("roomType:").append(roomType);
		}
		sb.append(")");
		return sb.toString();
	}
}
