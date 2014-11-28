package com.w3asel.cubesensors.api.v1;

import java.time.ZonedDateTime;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.w3asel.cubesensors.CubeSensorsTestProperties;
import com.w3asel.cubesensors.auth.TestAuth;

/**
 * These tests demonstrate retrieving data from the API. Because the data on the API can't be controlled the tests are assumed to pass if they return data
 * (meaning data was retrieved and parsed).
 *
 * Note: you must have an access token set in cubesensors.test.properties for these tests to run, see {@link TestAuth}.
 *
 * @author Joe
 */
public class TestApi {
	private static CubeSensorsApiV1 api;

	private static List<Device> devices;

	/** initializes the api object using {@link CubeSensorsTestProperties} */
	@BeforeClass
	public static void initApi() {
		api = new CubeSensorsApiV1(CubeSensorsTestProperties.getAccessToken());

		devices = api.getDevices();
	}

	/** verifies devices were retrieved by {@link CubeSensorsApiV1#getDevices()} */
	@Test
	public void testGetDevices() {
		// device list was needed for all specific queries so it's fetched in the init function
		Assert.assertNotNull(devices);
		Assert.assertFalse(devices.isEmpty());

		System.out.println(devices);
	}

	/** verifies {@link CubeSensorsApiV1#getDevice(String)} returns data for all devices found */
	@Test
	public void testGetDevice() {
		for (final Device device : devices) {
			final Device specificDevice = api.getDevice(device.getUid());
			Assert.assertNotNull(specificDevice);

			System.out.println(specificDevice);
		}
	}

	/** verifies {@link CubeSensorsApiV1#getCurrent(String)} returns data for all devices found */
	@Test
	public void testGetCurrent() {
		for (final Device device : devices) {
			final State current = api.getCurrent(device.getUid());
			Assert.assertNotNull(device.getUid(), current);

			System.out.println(device + ": " + current);
		}
	}

	/** verifies {@link CubeSensorsApiV1#getSpan(String, ZonedDateTime, ZonedDateTime, Integer)} returns data for the first device with no arguments specified */
	@Test
	public void testGetSpanDefaults() {
		final List<State> span = api.getSpan(devices.get(0).getUid(), null, null, null);
		Assert.assertNotNull(span);
		Assert.assertFalse(span.isEmpty());

		// expecting 24 hours at 1 min resolution with some buffer for missed minutes
		Assert.assertTrue(1400 < span.size());

		System.out.println(span);
	}

	/** verifies {@link CubeSensorsApiV1#getSpan(String, ZonedDateTime, ZonedDateTime, Integer)} returns data for the first device with arguments specified */
	@Test
	public void testGetSpan() {
		final ZonedDateTime start = ZonedDateTime.now().minusHours(6);
		final ZonedDateTime end = ZonedDateTime.now();
		final Integer resolution = 30;

		final List<State> span = api.getSpan(devices.get(0).getUid(), start, end, resolution);
		Assert.assertNotNull(span);
		Assert.assertFalse(span.isEmpty());

		// expecting 6 hours at 30 min resolution
		Assert.assertEquals(12, span.size());

		System.out.println(span);
	}
}
