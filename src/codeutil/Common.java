package codeutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.annotation.ExtField;
import model.annotation.ExtWidge;
import model.annotation.RelatedType;

public class Common {
    
	final private static String encode="UTF-8";
	
	final public static String IDFieldName="id";
	final public static String ParentIDFieldName="parent_id";
	
	final public static String  IntType="int";
	final public static String LongType="long";
	final public static String DoubleType="double";
	final public static String StringType="string";
	
	final public static String DateType = "date";
	final public static String ComboType = "combo";
	final public static String CheckboxType = "checkbox";
	final public static String HiddenType = "hidden";
	final public static String TextAreaType = "textarea";
	final public static String HtmlEditor = "htmleditor";
	final public static String GridComboBox = "gridcombo";
 	
	public static Map<String,String>  basicTypeMap          = new HashMap<String,String> ();
	public static Map<String,String>  basicMySqlDBTypeMap   = new HashMap<String,String> ();
	public static Map<String,Integer> basicDBFieldLengthMap = new HashMap<String,Integer>();
	public static Map<String,String>  basicExtFieldTypeMap  = new HashMap<String,String> ();
    
    static{
        basicTypeMap.put("int", "int");
        basicTypeMap.put("class java.lang.Integer", "int");
        basicTypeMap.put("long", "long");
        basicTypeMap.put("class java.lang.Long", "long");
        basicTypeMap.put("double", "double");
        basicTypeMap.put("class java.lang.Double", "double");
        basicTypeMap.put("class java.lang.String", "string");
    }
    
    static{
    	basicExtFieldTypeMap.put("int",      "Ext.form.TextField");
    	basicExtFieldTypeMap.put("long",     "Ext.form.TextField");
    	basicExtFieldTypeMap.put("double",   "Ext.form.TextField");
    	basicExtFieldTypeMap.put("string",   "Ext.form.TextField");
    	basicExtFieldTypeMap.put(DateType,   "Ext.form.DateField");
    	basicExtFieldTypeMap.put(CheckboxType,"Ext.form.Checkbox");
    	basicExtFieldTypeMap.put(ComboType,  "Ext.form.ComboBox" );
    	basicExtFieldTypeMap.put(HiddenType, "Ext.form.Hidden");
    	basicExtFieldTypeMap.put(TextAreaType, "Ext.form.TextArea");
    	basicExtFieldTypeMap.put(HtmlEditor, "Ext.form.HtmlEditor");
    	basicExtFieldTypeMap.put(GridComboBox, "Ext.form.GridComboBox");
    }
    
    static{        
        basicMySqlDBTypeMap.put("int", "smallint");
        basicMySqlDBTypeMap.put("class java.lang.Integer", "smallint");
        basicMySqlDBTypeMap.put("long", "long");
        basicMySqlDBTypeMap.put("class java.lang.Long", "long");
        basicMySqlDBTypeMap.put("double", "double");
        basicMySqlDBTypeMap.put("class java.lang.Double", "double");
        basicMySqlDBTypeMap.put("class java.lang.String", "varchar");
    }
    
    static{
        basicDBFieldLengthMap.put("class java.lang.String", 255);
    }

    public static String getExtFieldType(String fieldType){
    	return basicExtFieldTypeMap.get(fieldType);
    }
    
    public static List<String>getAllClassesForShort(){
    	List<String>classes = getAllClasses();
    	int size = classes.size();
    	for(int i=0;i<size;i++){
    		String className = classes.get(i);
    		String[]_className = className.split("\\.");
    		classes.set(i, _className[_className.length-1]);
    	}
    	return classes;
    }
    
	public static List<String>getAllClasses(){
		String root="src/model/";
		List<String>allClassFiledPath = getAllFilePaths(root,",hbm,hbm2,annotation,");
		
		System.out.println("allClassFiledPath:"+allClassFiledPath);
		
		List<String>allClasses = new ArrayList<String>();

        for (int i = 0; i < allClassFiledPath.size(); i++) {
        	String classFiledPath = allClassFiledPath.get(i);
        	if(classFiledPath.contains("java")){
        		int index = classFiledPath.indexOf("model");
        		allClasses.add(classFiledPath.substring(index, classFiledPath.length()-5).replace("\\", "."));
        	}
        }
        System.out.println("allClasses:"+allClasses);
        Collections.sort(allClasses, new Comparator(){

			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				return ((String)o1).compareTo((String)o2);
			}});
        return allClasses;
	}
	
	public static List<String> getAllFilePaths(String root,String exceptDir) {
		// TODO Auto-generated method stub
		List<String>allFilePath = new ArrayList();
    	LinkedList list = new LinkedList();
    	//File dir = new File("D://workspace_utf8//agbts//src");
    	File dir = new File(root);
        File file[] = dir.listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isDirectory()){
            	if(!exceptDir.contains(","+file[i].getName()+",")){
            		System.out.println("file["+i+"]:"+file[i].getName());
            		list.add(file[i]);
            	}
            	else{
            		System.out.println(file[i].getName()+" Not Match:"+exceptDir);
            	}
            }
            else{
                //System.out.println("0---"+file[i].getAbsolutePath());
                allFilePath.add(file[i].getAbsolutePath());
            }
        }
        File tmp;
        while (!list.isEmpty()) {
            tmp = (File) list.removeFirst();
            if (tmp.isDirectory()) {
                file = tmp.listFiles();
                if (file == null)
                    continue;
                for (int i = 0; i < file.length; i++) {
                    if (file[i].isDirectory())
                        list.add(file[i]);
                    else{
                        //System.out.println("1---"+file[i].getAbsolutePath());
                        allFilePath.add(file[i].getAbsolutePath());
                    }
                }
            } else {
                //System.out.println("2---"+tmp.getAbsolutePath());
                allFilePath.add(tmp.getAbsolutePath());
            }
        }
        
        System.out.println("contain file size:"+allFilePath.size());

        return allFilePath;
	}
	
	public static Map<String,String>getForeignKeyMap(String className) throws Exception, Exception{
		
		Map<String,String>foreignKeyMap = new HashMap<String,String>();
		Map<String,Integer>sampeClassCount = new HashMap<String,Integer>();
		Field[] fields=Class.forName(className).getDeclaredFields();
		int fieldLength = fields.length;
		for(int i=0;i<fieldLength;i++){
			Field field = fields[i];
			boolean hasRelatedType = field.isAnnotationPresent(RelatedType.class);
			if(hasRelatedType){
				Class type = field.getType();
				String relatedType = ((RelatedType)field.getAnnotation(RelatedType.class)).type();
				String fieldName = field.getName();
				if(!(type+"").contains("java.util.Set")){
					if(((RelatedType)field.getAnnotation(RelatedType.class)).propertyRef().length()==0){
						//tables.append("	"+relatedType+"_ID integer,\n");
						//foreignKeyConstraint.append("ALTER TABLE "+shortClassName+" ADD FOREIGN KEY ("+relatedType+"_ID"+") REFERENCES "+relatedType+"(id);\n");
						if(!sampeClassCount.containsKey(relatedType)){
							foreignKeyMap.put(fieldName, relatedType+"_ID");
							sampeClassCount.put(relatedType, 1);
						}
						else{
							int sameClassCount = sampeClassCount.get(relatedType);
							foreignKeyMap.put(fieldName, relatedType+"_ID_"+sameClassCount);
							sampeClassCount.put(relatedType, sameClassCount+1);
						}
					}		
				}
				else{
					String[] fullClassPathName = className.split("\\.");
					String shortClassName = fullClassPathName[fullClassPathName.length-1];
					foreignKeyMap.put(fieldName, shortClassName+"_ID");
				}
			}
		}
		return foreignKeyMap;
	}
	
	public static String getPlurality(String shortClassName){
		
		if(shortClassName.endsWith("s")){
			shortClassName=shortClassName+"es";
		}
		else if(shortClassName.endsWith("y")){
			shortClassName=shortClassName.substring(0,shortClassName.length()-1)+"ies";
		}
		else{
			shortClassName=shortClassName+"s";
		}
		
		return shortClassName;
	}
	
	public static String toLowerCaseOfTheFirstCharacter(String str){
		return str.toLowerCase().charAt(0)+str.substring(1);
	}
	
	public static String classTypeMapping(String content,String className){
		String[] fullClassPathName = className.split("\\.");
		
		String shortClassName = fullClassPathName[fullClassPathName.length-1];
		String lowerShortClassName =  Common.toLowerCaseOfTheFirstCharacter(shortClassName);
		String pluralityClassName  =  Common.getPlurality(shortClassName);
		String lowerPluralityClassName = Common.getPlurality(lowerShortClassName);
		
		//System.out.println("shortClassName:"+shortClassName);
		//System.out.println("lowerShortClassName:"+lowerShortClassName);
		//System.out.println("pluralityClassName:"+pluralityClassName);
		//System.out.println("lowerPluralityClassName:"+lowerPluralityClassName);
		
		return content.replace("[Class]", shortClassName).replace("[class]", lowerShortClassName).replace("[Classes]", pluralityClassName).replace("[classes]", lowerPluralityClassName);
	}
	
	public static String readFile(String filePath) throws Exception{
		FileInputStream in = new FileInputStream(filePath);
        byte[] readBytes = new byte[in.available()];
        in.read(readBytes);
        return  new String(readBytes,encode);
	}
	
	public static void writeFile(String filePath,String fileContent) throws Exception{
		FileOutputStream   out   =   new   FileOutputStream(filePath);
        byte[]content = fileContent.getBytes(encode);
        out.write(content, 0, content.length);
	}
	
	static public void mkdirs(String dir){
		File file = new File(dir);
		file.mkdirs();
	}
	
	static public Map<String,String> getDefaultValuesForChild(String className) throws ClassNotFoundException{
		Map<String, String> valueMap = new HashMap<String,String>();
		Class clazz = Class.forName(className);
		if(clazz.isAnnotationPresent(ExtWidge.class)){
			ExtWidge extWidge= (ExtWidge)clazz.getAnnotation(ExtWidge.class);
			String defVal4Child = extWidge.inherit_defaultValuesForChild();
			if(!defVal4Child.isEmpty()){
				String[]keyAndValues = defVal4Child.split(",");
				for(String keyAndValue : keyAndValues){
					String[]strs = keyAndValue.trim().split(":");
					valueMap.put(strs[0].trim(), strs[1].trim());
				}
			}
		}
		return valueMap;
	}
	
	static public Field[] getFiledsIncludingParentClass(String className) throws SecurityException, ClassNotFoundException{
		
		Field[] fieldsOfThisClass = Class.forName(className).getDeclaredFields();
		
		Field[] fieldsOfParentClass = null;
		
		Set<String>fieldNames = new HashSet<String>();
		
		Set<String>excludeFieldNames = getExcludeFields(Class.forName(className));
		
		if(Class.forName(className).getSuperclass() != Object.class){
			
			fieldsOfParentClass = Class.forName(className).getSuperclass().getDeclaredFields();
			
			int length = fieldsOfThisClass.length;
			for(int i=0;i<length;i++){
				
				String fieldName = fieldsOfThisClass[i].getName();
				
				if(excludeFieldNames.contains(fieldName)){
					continue;
				}
				
				fieldNames.add(fieldName);
			}
		}
		
		if(fieldsOfParentClass==null){
			return fieldsOfThisClass;
		}
		
		List<Field> fields = new ArrayList<Field>();
		
		int length = fieldsOfParentClass.length;
		for(int i=0;i<length;i++){
			Field field = fieldsOfParentClass[i];
			String fieldName = field.getName();
			if(fieldNames.contains(fieldName)||excludeFieldNames.contains(fieldName)){
				continue;
			}
			fields.add(field);
		}
		
		fields.addAll(Arrays.asList(fieldsOfThisClass));
		
		int size = fields.size();
		return (Field[])fields.toArray(new Field[size]);
	}
	
	private static Set<String> getExcludeFields(Class clazz){
		
		Set<String> excludeFields = new HashSet<String>();
		
		if(clazz.isAnnotationPresent(ExtWidge.class)){
			
			ExtWidge extWidge= (ExtWidge)clazz.getAnnotation(ExtWidge.class);
			String excludeFieldString = extWidge.excludeFields();
			String[]_excludeFields = excludeFieldString.split(",");
			
			int length = _excludeFields.length;
			
			for(int i=0;i<length;i++){
				String excludeField = _excludeFields[i];
				if(!excludeField.isEmpty()){
					excludeFields.add(excludeField);
				}
			}
		}
		
		return excludeFields;
	}
}
