package com.w3asel.cubesensors.util;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger LOGGER = LoggerFactory.getLogger(CubeSensorsUtils.class);

	private AuthPersistence authPersistence;
	private AuthProvider authProvider;

	/**
	 * Creates a new util object 
	 * @param authPersistence the {@link AuthPersistence} implementation to be used in the util object
	 * @param authProvider the {@link AuthProvider} implementation to be used in the util object
	 */
	public CubeSensorsUtils(AuthPersistence authPersistence, AuthProvider authProvider) {
		this.authPersistence = authPersistence;
		this.authProvider = authProvider;
	}
	
	/**
	 * Obtain an access token. If previously a token was created and persisted
	 * will be returned after validation (a real call is made to validate).
	 * <p>If no token previously persisted, the method will create a new token using
	 * the authentication provider (AuthProvider)
	 * @return the access token.
	 */
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
		if (token != null) {
			// try a real call to verify the access
			CubeSensorsApiV1 api = new CubeSensorsApiV1(token);
			// throws CubeSensorsException if not authorized
			try {
				api.getDevices();
				return true;
			} catch (CubeSensorsException e) {
				if (e.getMessage().contains("401")) {
					LOGGER.error("Unauthorized, reauthorizing", e);
	        	} else {
	        		LOGGER.error("cannot validate request token", e);
	        	}
			}
		}
		return false;
	}

	/**
	 * Inject an AuthPersistence implementation.
	 * @param authPersistence the implementation to be injected
	 */
	public void setAuthPersistence(AuthPersistence authPersistence) {
		this.authPersistence = authPersistence;
	}

	/**
	 * Inject an AuthProvider implementation
	 * @param authProvider the implementation object
	 */
	public void setAuthProvider(AuthProvider authProvider) {
		this.authProvider = authProvider;
	}

	/**
	 * Creates a new instance of {@link CubeSensorsUtils} using {@link PrefAuthPersistence}
	 * as {@link AuthPersistence} and {@link ConsoleAuthProvider} as {@link AuthProvider}
	 * @return the assembled utils object
	 */
	public static CubeSensorsUtils instance() {
		CubeSensorsUtils utils = new CubeSensorsUtils(new PrefAuthPersistence(), new ConsoleAuthProvider());
		return utils;
	}
}
