package com.w3asel.cubesensors.util;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.w3asel.cubesensors.CubeSensorsProperties;
import com.w3asel.cubesensors.api.v1.CubeSensorsApiV1;
import com.w3asel.cubesensors.api.v1.CubeSensorsException;
import com.w3asel.cubesensors.auth.CubeSensorsAuthApi;

/**
 * A utility class used to simplify the authorization using persistence for 
 * an authorized request token. 
 * If no persisted token found it will initiate the authorization, code provided using the console.
 * <p>
 * Usage with default providers: 
 * <pre><code>
 * Token accessToken = CubeSensorsUtils.instance().getAccessToken();
 * CubeSensorsApiV1 api = new CubeSensorsApiV1(accessToken);
 * api.getDevices();
 * </code></pre>
 * <p>Or assemble a CubeSensorsUtils setting a custom AuthPersistence or AuthProvider:
 * <pre>
 * <code>
 * CubeSensorsUtils u = new CubeSensorsUtils();
 * u.setAuthProvider(... custom auth provider ...)
 * u.setAuthPersistence(... custom persistence ...)
 * Token accessToken = u.getAccessToken();
 * CubeSensorsApiV1 api = new CubeSensorsApiV1(accessToken);
 * api.getDevices();
 * </code>
 * </pre>
 * 
 * @author Silviu Marcu
 *
 */
public class CubeSensorsUtils {

	private AuthPersistence authPersistence;
	private AuthProvider authProvider;

	public Token getAccessToken() {

		Token accessToken = authPersistence.getToken();

		if (isTokenValid(accessToken)) {
			return accessToken;
		}
		
		// authorize
		accessToken = authorize();

		// save
		authPersistence.saveToken(accessToken);

		return accessToken;
	}

	private Token authorize() {
		OAuthService service = new ServiceBuilder()
				.provider(CubeSensorsAuthApi.class)
				.apiKey(CubeSensorsProperties.getAppKey())
				.apiSecret(CubeSensorsProperties.getAppSecret())
				.signatureType(SignatureType.QueryString).build();

		Token requestToken = service.getRequestToken();
		String authorizationUrl = service.getAuthorizationUrl(requestToken);

		String authorization = authProvider.getAuthorization(authorizationUrl);

		Token accessToken = service.getAccessToken(requestToken, new Verifier(
				authorization));

		return accessToken;
	}

	private boolean isTokenValid(Token token) {
		// try a real call to verify the access
		CubeSensorsApiV1 api = new CubeSensorsApiV1(token);
		// throws CubeSensorsException if not authorized
		try {
			api.getDevices();
			return true;
		} catch (CubeSensorsException e) {
			if (e.getMessage().contains("401")) {
        		System.out.println("Unauthorized, reauthorizing");
        	} else {
        		e.printStackTrace();
        	}
			return false;
		}
	}

	public void setAuthPersistence(AuthPersistence authPersistence) {
		this.authPersistence = authPersistence;
	}

	public void setAuthProvider(AuthProvider authProvider) {
		this.authProvider = authProvider;
	}

	public static CubeSensorsUtils instance() {
		CubeSensorsUtils utils = new CubeSensorsUtils();
		utils.setAuthPersistence(new PrefAuthPersistence());
		utils.setAuthProvider(new ConsoleAuthProvider());
		return utils;
	}
}
