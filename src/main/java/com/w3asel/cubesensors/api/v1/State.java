package com.w3asel.cubesensors.api.v1;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.w3asel.cubesensors.api.v1.format.Pressure;
import com.w3asel.cubesensors.api.v1.format.Signal;
import com.w3asel.cubesensors.api.v1.format.Temperature;

/**
 * Contains all state variables as returned by the API. No conversions to
 * displayable formats are made when populating this object, but formatters are
 * available in com.w3asel.cubesensors.api.v1.format.
 *
 * @author Joe
 */
public class State {
	public final ZonedDateTime time;
	public final int temp;
	public final int pressure;
	public final int humidity;
	public final int voc;
	public final int light;
	public final int noise;
	// TODO noisedba when it's populated
	public final int battery;
	public final boolean shake;
	public final boolean cable;
	public final int vocResistance;
	public final int rssi;

	public State(final ZonedDateTime time, final int temp, final int pressure,
			final int humidity, final int voc, final int light,
			final int noise, final int battery, final boolean shake,
			final boolean cable, final int vocResistance, final int rssi) {
		this.time = time;
		this.temp = temp;
		this.pressure = pressure;
		this.humidity = humidity;
		this.voc = voc;
		this.light = light;
		this.noise = noise;
		this.battery = battery;
		this.shake = shake;
		this.cable = cable;
		this.vocResistance = vocResistance;
		this.rssi = rssi;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(time.withZoneSameInstant(ZoneId.systemDefault()))
				.append(": ");
		sb.append(Temperature.toF(temp)).append(" F");
		sb.append(", ");
		sb.append(Pressure.toInHg(pressure)).append(" in Hg");
		sb.append(", ");
		sb.append(humidity).append("%");
		sb.append(", ");
		sb.append(voc).append(" ppm");
		sb.append(", ");
		sb.append(light).append(" lux");
		sb.append(", ");
		sb.append(noise).append(" RMS");
		sb.append(", ");
		sb.append(battery / 100d).append(" %");
		if (shake) {
			sb.append(", shaken");
		}
		if (cable) {
			sb.append(", charging");
		}
		sb.append(", ");
		sb.append(Signal.toRating(rssi));
		sb.append(")");
		return sb.toString();
	}
}
