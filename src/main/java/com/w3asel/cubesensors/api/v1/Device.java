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
	public enum Type {
		cube,
	}

	public enum RoomType {
		work,
		sleep,
		live,
	}

	public final Type type;
	public final String uid;
	public final String name;
	public final RoomType roomType;

	public Device(final JsonDevice device, final Map<ExtraMapping, String> extras) {
		this.type = device.type;
		this.uid = device.uid;
		this.name = extras.get(ExtraMapping.name);
		this.roomType = extras.get(ExtraMapping.roomtype) == null ? null : RoomType.valueOf(extras.get(ExtraMapping.roomtype));
	}

	public Type getType() {
		return type;
	}

	public String getUid() {
		return uid;
	}

	public String getName() {
		return name;
	}

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
