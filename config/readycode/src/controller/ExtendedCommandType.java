package controller;

import java.util.HashMap;
import java.util.Map;

public class ExtendedCommandType {

	static public final Map<String,Integer> cmdMap = new HashMap<String,Integer>();
	
	static public final String _login = "login";
	static public final String _logout="logout";
	static public final String _changePassword="changePassword";
	
	static public final int login = 1;
	static public final int logout = 2;
	static public final int changePassword = 3;
	
	static {
		cmdMap.put(ExtendedCommandType._login , ExtendedCommandType.login);
		cmdMap.put(ExtendedCommandType._logout, ExtendedCommandType.logout);
		cmdMap.put(ExtendedCommandType._changePassword, ExtendedCommandType.changePassword);
 	}
}
