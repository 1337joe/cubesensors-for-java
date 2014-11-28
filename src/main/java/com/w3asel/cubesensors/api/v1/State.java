package com.w3asel.cubesensors.api.v1;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.w3asel.cubesensors.api.v1.format.Pressure;
import com.w3asel.cubesensors.api.v1.format.Signal;
import com.w3asel.cubesensors.api.v1.format.Temperature;

/**
 * Contains all state variables as returned by the API. No conversions to displayable formats are made when populating this object, but formatters are available
 * in com.w3asel.cubesensors.api.v1.format.
 *
 * @author Joe
 */
public class State {
	private static final double TO_PERCENT = 1 / 100d;

	/** the time of this state report */
	public final ZonedDateTime time;
	/** current temperature (&deg;C * 100) */
	public final int temp;
	/** barometric pressure in mbar */
	public final int pressure;
	/** relative humidity in % */
	public final int humidity;
	/** amount of VOC gases in the air in ppm */
	public final int voc;
	/** light level measured in lux */
	public final int light;
	/** noise level measured in RMS */
	public final Integer noise;
	/** noise level measured in dBA */
	public final Integer noisedba;
	/** fullness of battery in % */
	public final int battery;
	/** indicates whether the Cube has recently been shaken or not */
	public final boolean shake;
	/** indicates whether the Cube is on cable and thus charging or not */
	public final boolean cable;
	/** raw resistance values from the VOC sensor */
	public final int vocResistance;
	/** wireless signal strength indicator (RSSI) */
	public final int rssi;

	/**
	 * Creates a fully-populated {@link State}
	 *
	 * @param time
	 *            the time of this state report
	 * @param temp
	 *            current temperature (&deg;C * 100)
	 * @param pressure
	 *            barometric pressure in mbar
	 * @param humidity
	 *            relative humidity in %
	 * @param voc
	 *            amount of VOC gases in the air in ppm
	 * @param light
	 *            light level measured in lux
	 * @param noise
	 *            noise level measured in RMS
	 * @param noisedba
	 *            noise level measured in dBA
	 * @param battery
	 *            fullness of battery in %
	 * @param shake
	 *            indicates whether the Cube has recently been shaken or not
	 * @param cable
	 *            indicates whether the Cube is on cable and thus charging or not
	 * @param vocResistance
	 *            raw resistance values from the VOC sensor
	 * @param rssi
	 *            wireless signal strength indicator (RSSI)
	 */
	public State(final ZonedDateTime time, final int temp, final int pressure, final int humidity, final int voc, final int light, final Integer noise,
			final Integer noisedba, final int battery, final boolean shake, final boolean cable, final int vocResistance, final int rssi) {
		this.time = time;
		this.temp = temp;
		this.pressure = pressure;
		this.humidity = humidity;
		this.voc = voc;
		this.light = light;
		this.noise = noise;
		this.noisedba = noisedba;
		this.battery = battery;
		this.shake = shake;
		this.cable = cable;
		this.vocResistance = vocResistance;
		this.rssi = rssi;
	}

	/** @return the time of this state report */
	public ZonedDateTime getTime() {
		return time;
	}

	/** @return current temperature (&deg;C * 100) */
	public int getTemp() {
		return temp;
	}

	/** @return barometric pressure in mbar */
	public int getPressure() {
		return pressure;
	}

	/** @return relative humidity in % */
	public int getHumidity() {
		return humidity;
	}

	/** @return amount of VOC gases in the air in ppm */
	public int getVoc() {
		return voc;
	}

	/** @return light level measured in lux */
	public int getLight() {
		return light;
	}

	/** @return noise level measured in RMS (may be null) */
	public Integer getNoise() {
		return noise;
	}

	/** @return noise level measured in dBA (may be null) */
	public Integer getNoisedba() {
		return noisedba;
	}

	/** @return fullness of battery in % */
	public int getBattery() {
		return battery;
	}

	/** @return indicates whether the Cube has recently been shaken or not */
	public boolean isShake() {
		return shake;
	}

	/** @return indicates whether the Cube is on cable and thus charging or not */
	public boolean isCable() {
		return cable;
	}

	/** @return raw resistance values from the VOC sensor */
	public int getVocResistance() {
		return vocResistance;
	}

	/** @return wireless signal strength indicator (RSSI) */
	public int getRssi() {
		return rssi;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(time.withZoneSameInstant(ZoneId.systemDefault())).append(": ");
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
		if (noise != null) {
			sb.append(noise).append(" RMS");
			sb.append(", ");
		}
		if (noisedba != null) {
			sb.append(noisedba).append(" dBA");
			sb.append(", ");
		}
		sb.append(battery * TO_PERCENT).append(" %");
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
