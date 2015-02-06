package com.w3asel.cubesensors.util;

import java.util.Scanner;

import com.w3asel.cubesensors.api.v1.CubeSensorsException;

/**
 * 
 * @author Silviu Marcu
 *
 */
public class ConsoleAuthProvider implements AuthProvider {

	@Override
	public String getAuthorization(String authorizationUrl)
			throws CubeSensorsException {
		
		System.out.println("authorizationUrl:"+authorizationUrl);
    	System.out.print("provide authorization code:");
    	
    	try ( Scanner in = new Scanner(System.in) ) {
    		String authorization = in.nextLine();
    		return authorization;
    	}
    }

}
