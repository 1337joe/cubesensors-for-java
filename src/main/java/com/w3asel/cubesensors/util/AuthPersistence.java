package com.w3asel.cubesensors.util;

import org.scribe.model.Token;

/**
 * Persistence for authentication tokens.
 * 
 * @author Silviu Marcu
 */
public interface AuthPersistence {

	/**
	 * saves a request token
	 * @param token the request token to be saved
	 */
	void saveToken(Token token);
	
	/**
	 * get the persisted token (null if not set)
	 * @return the persisted token (null if not set)
	 */
	Token getToken();
	
}
