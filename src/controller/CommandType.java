package controller;

import java.util.HashMap;
import java.util.Map;

public class CommandType {

	static public final Map<String,Integer> cmdMap = new HashMap<String,Integer>(); 

	static public final String _findAllLocations = "findAllLocations";
	static public final String _findAllMoneies = "findAllMoneies";
	static public final String _findAllPeopleClasses = "findAllPeopleClasses";

	static public final int findAllLocations = 1;
	static public final int findAllMoneies = 2;
	static public final int findAllPeopleClasses = 3;

	static {
	
		cmdMap.put(CommandType._findAllLocations , CommandType.findAllLocations);
		cmdMap.put(CommandType._findAllMoneies , CommandType.findAllMoneies);
		cmdMap.put(CommandType._findAllPeopleClasses , CommandType.findAllPeopleClasses);

 	}
}
