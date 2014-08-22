package codeutil;

import java.util.List;

public class CommandControllerUtil {
    
    private static String outputRoot = "output/";
    private static String commandTypetemplate = "config/template/CommandTypeTemplate.java";
    private static String commandControllertemplate = "config/template/CommandHandleControllerTemplate.java";
    private static String commandControllerFunctemplate= "config/template/CommandHandleControllerTemplate.Func.java";
    
    public static void createCommandType()throws Exception{
    	
    	String fileDir  = outputRoot+"src/controller/";
    	String filePath = fileDir+"CommandType.java";
    	Common.mkdirs(fileDir);
    	
    	List<String>allClasses = Common.getAllClasses();
    	String template = Common.readFile(commandTypetemplate);
    	
    	int size = allClasses.size();
    	StringBuffer strBuffer = new StringBuffer();
    	StringBuffer strBuffer1 = new StringBuffer();
    	StringBuffer strBuffer2 = new StringBuffer();
    	int j = 0;
    	for(int i=0;i<size*4;i=i+4){
    		
    		String className = allClasses.get(j++);
    		String[] fullClassPathName = className.split("\\.");
    		
    		String shortClassName = fullClassPathName[fullClassPathName.length-1];
    		String lowerShortClassName =  Common.toLowerCaseOfTheFirstCharacter(shortClassName);
    		String pluralityClassName  =  Common.getPlurality(shortClassName);
    		String lowerPluralityClassName = Common.getPlurality(lowerShortClassName);
    		
    		strBuffer. append("	static public final String _findAll").append(pluralityClassName).append(" = \"findAll").append(pluralityClassName).append("\";\n");
    		strBuffer1.append("	static public final int findAll").append(pluralityClassName).append(" = ").append(i+1).append(";\n");
    		strBuffer2.append("		cmdMap.put(CommandType._findAll").append(pluralityClassName).append(" , ").append("CommandType.findAll").append(pluralityClassName).append(");\n");
    	
    		strBuffer. append("	static public final String _saveOrUpdate").append(shortClassName).append(" = \"saveOrUpdate").append(shortClassName).append("\";\n");
    		strBuffer1.append("	static public final int saveOrUpdate").append(shortClassName).append(" = ").append(i+2).append(";\n");
    		strBuffer2.append("		cmdMap.put(CommandType._saveOrUpdate").append(shortClassName).append(" , ").append("CommandType.saveOrUpdate").append(shortClassName).append(");\n");
    		
    		strBuffer. append("	static public final String _delete").append(shortClassName).append(" = \"delete").append(shortClassName).append("\";\n");
    		strBuffer1.append("	static public final int delete").append(shortClassName).append(" = ").append(i+3).append(";\n");
    		strBuffer2.append("		cmdMap.put(CommandType._delete").append(shortClassName).append(" , ").append("CommandType.delete").append(shortClassName).append(");\n");
    		
    		strBuffer. append("	static public final String _get").append(shortClassName).append(" = \"get").append(shortClassName).append("\";\n");
    		strBuffer1.append("	static public final int get").append(shortClassName).append(" = ").append(i+4).append(";\n");
    		strBuffer2.append("		cmdMap.put(CommandType._get").append(shortClassName).append(" , ").append("CommandType.get").append(shortClassName).append(");\n");
    	
    	}
    	Common.writeFile(filePath, template.replace("[CommandString]", strBuffer.toString()).replace("[CommandInt]", strBuffer1.toString()).replace("[CommandMap]", strBuffer2.toString()));
    }
    
    public static void createCommandController()throws Exception{
    	
    	String fileDir  = outputRoot+"src/controller/";
    	String filePath = fileDir+"CommandHandleController.java";
    	Common.mkdirs(fileDir);
    	
    	List<String>allClasses = Common.getAllClasses();
    	String template = Common.readFile(commandControllertemplate);
    	String templateFunc = Common.readFile(commandControllerFunctemplate);
    	
    	int size = allClasses.size();
    	StringBuffer strBuffer = new StringBuffer();
    	StringBuffer strBuffer1 = new StringBuffer();
    	
    	for(int i=0;i<size;i++){
    		
    		String className = allClasses.get(i);
    		String[] fullClassPathName = className.split("\\.");
    		
    		String shortClassName = fullClassPathName[fullClassPathName.length-1];
    		String lowerShortClassName =  Common.toLowerCaseOfTheFirstCharacter(shortClassName);
    		String pluralityClassName  =  Common.getPlurality(shortClassName);
    		String lowerPluralityClassName = Common.getPlurality(lowerShortClassName);
    		
    		strBuffer. append("		    	case CommandType.findAll").append(pluralityClassName).append(":findAll").append(pluralityClassName).append("(request,response);break;\n");
    		strBuffer. append("		    	case CommandType.saveOrUpdate").append(shortClassName).append(":saveOrUpdate").append(shortClassName).append("(request,response);break;\n");
    		strBuffer. append("		    	case CommandType.delete").append(shortClassName).append(":delete").append(shortClassName).append("(request,response);break;\n");
    		strBuffer. append("		    	case CommandType.get").append(shortClassName).append(":get").append(shortClassName).append("(request,response);break;\n");
    		
    		strBuffer1.append(Common.classTypeMapping(templateFunc, className)).append("\n");
    	}
    	Common.writeFile(filePath, template.replace("[CaseCommandType]", strBuffer.toString()).replace("[FindAllFunction]", strBuffer1.toString()));
    }
    
    public static void createAll() throws Exception{
    	createCommandType();
		createCommandController();
    }
    
	public static void main(String[]args) throws Exception, ClassNotFoundException{
		createAll();
	}
}
