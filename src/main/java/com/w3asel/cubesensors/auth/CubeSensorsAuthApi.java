package com.w3asel.cubesensors.auth;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * An extension of Scribe's {@link DefaultApi10a} for use with the CubeSensors API.
 *
 * @author Joe
 */
public class CubeSensorsAuthApi extends DefaultApi10a {
	private static final String ROOT = "http://api.cubesensors.com";
	private static final String AUTH = ROOT + "/auth";

	private static final String REQUEST_TOKEN_URL = AUTH + "/request_token";
	private static final String ACCESS_TOKEN_URL = AUTH + "/access_token";
	private static final String USER_AUTHORIZATION_URL = AUTH + "/authorize?oauth_token=%s";

	@Override
	public String getRequestTokenEndpoint() {
		return REQUEST_TOKEN_URL;
	}

	@Override
	public String getAccessTokenEndpoint() {
		return ACCESS_TOKEN_URL;
	}

	@Override
	public String getAuthorizationUrl(final Token requestToken) {
		return String.format(USER_AUTHORIZATION_URL, requestToken.getToken());
	}
}
