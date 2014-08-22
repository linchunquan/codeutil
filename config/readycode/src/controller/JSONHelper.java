package controller;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class JSONHelper {
	
	public static ObjectMapper mapper;
	public static JsonFactory f = new JsonFactory();
	
	static private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static private SimpleDateFormat sdf_simple=new SimpleDateFormat("yyyy-MM-dd");
	static private SimpleDateFormat sdfFormTime=new SimpleDateFormat("HH:mm:ss");
	static private SimpleDateFormat sdfFormShortTime=new SimpleDateFormat("HH:mm");
	
	public static final String successJsonTag="{success:true}";
	public static final String failJsonTag="{success:false}";
	
	static{
		mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public static String basicObjectToJson(Object object){
		StringWriter writer = new StringWriter();
		 try {
			mapper.writeValue(writer, object);
			//System.out.println("json:"+writer.toString());
			return writer.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{}";
		} 
	}
	public static Object basicJsonToObject(String json,Class className) throws IOException, JsonMappingException, IOException{
		return mapper.readValue(json, className);
	}
	public static Object basicJsonToObject(String json,TypeReference tf) throws JsonMappingException, IOException{
		return mapper.readValue(json, tf);
	}
}