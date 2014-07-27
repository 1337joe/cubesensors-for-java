package com.w3asel.cubesensors.auth;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.w3asel.cubesensors.CubeSensorsProperties;
import com.w3asel.cubesensors.CubeSensorsTestProperties;

/**
 * This isn't so much a test class as an example of how to use the Scribe-based
 * authorization API and a utility for generating an access token and verifying
 * that it works.
 *
 * <h1>Instructions:</h1>
 * <ul>
 * <li>First, make sure your API key/secret have been entered into
 * /src/main/resources/cubesensors.properties. If you haven't gotten these yet
 * email api@cubesensors.com and when they're granted they'll show up at
 * my.cubesensors.com</li>
 * <li>Only one test should be enabled at a time - enable tests by removing the
 * {@code @Ignore} annotation and disable them by putting it back.</li>
 * <li>Start with {@link TestAuth#testRequestToken()}. Run that and follow the
 * instructions to update src/test/resources/cubesensors.test.properties with
 * the appropriate values.</li>
 * <li>Once you have the request token and verifier in your properties file run
 * {@link TestAuth#testVerify()} and again follow the instructions on what
 * values to enter in the properties file.</li>
 * <li>When you've entered the access token values in the properties you should
 * be able to access the API. Test it by running
 * {@link TestAuth#testAccessToken()}.</li>
 * </ul>
 *
 * @author Joe
 */
public class TestAuth {
	private static OAuthService service;

	@BeforeClass
	public static void buildService() {
		service = new ServiceBuilder().debugStream(System.out)
				.provider(CubeSensorsAuthApi.class)
				.apiKey(CubeSensorsProperties.getAppKey())
				.apiSecret(CubeSensorsProperties.getAppSecret())
				.signatureType(SignatureType.QueryString).build();
	}

	@Ignore
	@Test
	public void testRequestToken() {
		final Token requestToken = service.getRequestToken();

		System.out.println();

		System.out
				.println("Copy these values into cubesensors.test.properties:");
		System.out.println("requestToken.token=" + requestToken.getToken());
		System.out.println("requestToken.secret=" + requestToken.getSecret());

		System.out.println();
		System.out.println("Navigate to: "
				+ service.getAuthorizationUrl(requestToken));
		System.out
				.println("Enter the string the cubesensors page gives you in cubesensors.test.properties after:");
		System.out.println("verifier=");
	}

	@Ignore
	@Test
	public void testVerify() {
		final Token requestToken = CubeSensorsTestProperties.getRequestToken();
		final Verifier verifier = CubeSensorsTestProperties
				.getRequestVerifier();

		final Token accessToken = service
				.getAccessToken(requestToken, verifier);

		System.out.println();

		System.out
				.println("Copy these values into cubesensors.test.properties:");
		System.out.println("accessToken.token=" + accessToken.getToken());
		System.out.println("accessToken.secret=" + accessToken.getSecret());
	}

	@Ignore
	@Test
	public void testAccessToken() {
		final Token accessToken = CubeSensorsTestProperties.getAccessToken();

		final OAuthRequest request = new OAuthRequest(Verb.GET,
				"http://api.cubesensors.com/v1/devices/");
		service.signRequest(accessToken, request);
		final Response response = request.send();

		System.out.println();

		System.out.println("Request succeeded");
		System.out.println(response.getBody());
	}
}
