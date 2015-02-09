package com.w3asel.cubesensors.util;

import java.util.Scanner;

import com.w3asel.cubesensors.api.v1.CubeSensorsException;

/**
 * Console implementation for AuthProvider. Using console to read the
 * authorization key.
 * 
 * @author Silviu Marcu
 */
public class ConsoleAuthProvider implements AuthProvider {

	/**
	 * Prints the authorizationUrl, the user will open the url and obtain an
	 * authorization key. When prompted, the user should provide the authorization
	 * key.
	 */
	@Override
	public String getAuthorization(String authorizationUrl)
			throws CubeSensorsException {

		System.out.println("authorizationUrl:" + authorizationUrl);
		System.out.print("provide authorization code:");

		try (Scanner in = new Scanner(System.in)) {
			String authorization = in.nextLine();
			return authorization;
		}
	}

}
