package com.w3asel.cubesensors.util;

import java.util.prefs.Preferences;

import org.scribe.model.Token;

/*
 * Provides an AuthPersistence using the java preferences mechanism 
 * @author Silviu Marcu
 */
public class PrefAuthPersistence implements AuthPersistence {

	public static final String ACCESS_TOKEN_TOKEN_PREF= "cubesensors.authorization.access.token";
	public static final String ACCESS_TOKEN_SECRET_PREF= "cubesensors.authorization.access.secret";
	
	/**
	 * Save the token using java preferences.
	 */
	@Override
	public void saveToken(Token token) {
		set(ACCESS_TOKEN_TOKEN_PREF, token.getToken());
		set(ACCESS_TOKEN_SECRET_PREF, token.getSecret());
	}

	/**
	 * Get the persisted token using java preferences.
	 */
	@Override
	public Token getToken() {
		String token = get(ACCESS_TOKEN_TOKEN_PREF);
		String secret = get(ACCESS_TOKEN_SECRET_PREF);
		return token != null ? new Token(token, secret) : null;
	}

	private String get(String key) {
		return Preferences.userRoot().get(key, null);
	}
	
	private void set(String key, String val) {
		Preferences.userRoot().put(key, val);
	}

}
