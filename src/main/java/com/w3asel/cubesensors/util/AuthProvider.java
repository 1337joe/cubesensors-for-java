package com.w3asel.cubesensors.util;

import com.w3asel.cubesensors.api.v1.CubeSensorsException;

/**
 * Authentication provider, used to obtain an authorization token 
 * from a provided authorizationurl.
 * 
 * <p>Example of implementation could ask the user to open url 
 * and paste token after web page confirmation.
 * 
 * @author Silviu Marcu
 */
public interface AuthProvider {

	/**
	 * Get the authorization token using the authorizationUrl
	 * @param authorizationUrl the authorizationUrl used to get the authorization token
	 * @return the token
	 * @throws CubeSensorsException if cannot obtain authorization
	 */
	String getAuthorization(String authorizationUrl) throws CubeSensorsException;
	
}
