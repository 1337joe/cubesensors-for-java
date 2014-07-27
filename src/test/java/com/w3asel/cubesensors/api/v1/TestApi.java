package com.w3asel.cubesensors.api.v1;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.w3asel.cubesensors.CubeSensorsTestProperties;

/**
 * These tests demonstrate retrieving data from the API. Because the data on the
 * API can't be controlled the tests are assumed to pass if they return data
 * (meaning data was retrieved and parsed).
 *
 * Due to the volume of data it requests {@link TestApi#testGetSpan()} should be
 * used sparingly.
 *
 * @author Joe
 */
public class TestApi {
	private static CubeSensorsApiV1 api;

	private static List<Device> devices;

	@BeforeClass
	public static void initApi() {
		api = CubeSensorsApiV1.fromAccessToken(CubeSensorsTestProperties
				.getAccessToken());

		devices = api.getDevices();
	}

	@Test
	public void testGetDevices() {
		// device list was needed for all specific queries so it's fetched in
		// the init function
		Assert.assertNotNull(devices);
		Assert.assertFalse(devices.isEmpty());

		System.out.println(devices);
	}

	@Test
	public void testGetDevice() {
		for (final Device device : devices) {
			final Device specificDevice = api.getDevice(device.getUid());
			Assert.assertNotNull(specificDevice);

			System.out.println(specificDevice);
		}
	}

	@Test
	public void testGetCurrent() {
		for (final Device device : devices) {
			final State current = api.getCurrent(device.getUid());
			Assert.assertNotNull(device.getUid(), current);

			System.out.println(device + ": " + current);
		}
	}

	@Ignore
	@Test
	public void testGetSpan() {
		final List<State> span = api.getSpan(devices.get(0).getUid());
		Assert.assertNotNull(span);
		Assert.assertFalse(span.isEmpty());

		System.out.println(span);
	}
}
