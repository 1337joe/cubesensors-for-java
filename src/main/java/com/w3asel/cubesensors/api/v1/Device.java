package com.w3asel.cubesensors.api.v1;

import java.util.EnumMap;

import com.w3asel.cubesensors.api.v1.json.JsonDevice;
import com.w3asel.cubesensors.api.v1.json.JsonDevice.ExtraMapping;

public class Device {
	public enum Type {
		cube,
	}

	public enum RoomType {
		work,
		sleep,
		live,
	}

	public enum State {
		active,
	}

	public final Type type;
	public final String uid;
	public final String name;
	public final RoomType roomType;
	public final State state;

	public Device(final JsonDevice device,
			final EnumMap<ExtraMapping, String> extras) {
		this.type = device.type;
		this.uid = device.uid;
		this.name = extras.get(ExtraMapping.name);
		this.roomType = extras.get(ExtraMapping.roomtype) == null ? null
				: RoomType.valueOf(extras.get(ExtraMapping.roomtype));
		this.state = extras.get(ExtraMapping.state) == null ? null : State
				.valueOf(extras.get(ExtraMapping.state));
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

	public State getState() {
		return state;
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
		if (state != null) {
			sb.append(", ");
			sb.append("state:").append(state);
		}
		sb.append(")");
		return sb.toString();
	}
}
