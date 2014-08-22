package codeutil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.List;

import model.annotation.DbView;
import model.annotation.ExtField;
import model.annotation.ExtWidge;
import model.annotation.NotMapping;
import model.annotation.RelatedType;

public class ExtGridUtil {
    
    private static String extGridDir = "output/ext/grid/";
    
    private static String template = "config/template/GridTemplate.js";
    
    private static String encode="UTF-8";

    private static final String dotTag=".";
    
    public static String getDataStatusColumn(String className) throws Exception{
    	
    	String[] fullClassPathName = className.split("\\.");
        String shortClassName = fullClassPathName[fullClassPathName.length-1];
    	
    	String text = "";
    	
    	Field[] fields=Common.getFiledsIncludingParentClass(className);
        int fieldLength = fields.length;
        
        for(int i=0;i<fieldLength;i++){
        	Field field=fields[i];
            String fieldName = field.getName();
            if(fieldName.equalsIgnoreCase("dataStatus")){
            	String updateClassName = shortClassName;
            	if(field.isAnnotationPresent(ExtField.class)) {    	
                	ExtField extField = (ExtField)field.getAnnotation(ExtField.class);
                	if(!extField.gridStatusColumn_className().isEmpty()){
                		updateClassName = extField.gridStatusColumn_className();
                	}
            	}
            	return "		this.dataStatusColumn = new DataStatusColumn(this,\""+updateClassName+"\");";
            }
        }
    	
    	return text;
    }
    
    public static String getExpanderColumn(String className) throws Exception{
    	
    	String[] fullClassPathName = className.split("\\.");
        String shortClassName = fullClassPathName[fullClassPathName.length-1];
    	
    	String text = "";
    	
    	Field[] fields=Common.getFiledsIncludingParentClass(className);
        int fieldLength = fields.length;
        
        for(int i=0;i<fieldLength;i++){
        	Field field=fields[i];
            String fieldName = field.getName();
            if(fieldName.equalsIgnoreCase("expandText")){
            	return "		this.plugins = this.expanderColumn = new Ext.ux.grid.RowExpander({tpl : new Ext.Template('<div style=\"padding-left:50px;color:red;\">{expandText}</div>')});this.expanderColumn.init(this);";
            }
        }
    	
    	return text;
    }
    
    public static String getOnRender(String className) throws Exception{
    	String text = "		this.on('render' , this.dynamicQuery, this);";
    	
    	Field[] fields=Common.getFiledsIncludingParentClass(className);
        int fieldLength = fields.length;
        
        for(int i=0;i<fieldLength;i++){
        	Field field=fields[i];
            String fieldName = field.getName();
            if(fieldName.equalsIgnoreCase("dataStatus")){
            	return "		this.on('render' , function(){this.dynamicQuery();this.getView().getRowClass=this.dataStatusColumn.getRowClass;this.getView().mainBody.on(\"mousedown\",this.dataStatusColumn.onMousedown);}, this);";
            }
        }
    	
    	return text;
    }
    
    public static String getColumns(String subString, String className, String subHeader) throws Exception {
        
        StringBuffer columns = new StringBuffer();
        final String splitor = "SplitorTag";
        
        //Field[] fields=Class.forName(className).getDeclaredFields();
        Field[] fields=Common.getFiledsIncludingParentClass(className);
        int fieldLength = fields.length;
        
        for(int i=0;i<fieldLength;i++){
            Field field=fields[i];
            String fieldName = field.getName();
            if(subString.length()>0) {
                fieldName = subString+splitor+fieldName;
            }
            boolean shouldNotMapping = field.isAnnotationPresent(NotMapping.class);
            if(!shouldNotMapping){
                field.setAccessible(true);
                Class type = field.getType();
                String basicType = Common.basicTypeMap.get(type+"");
                if(basicType!=null){             
                    if(field.isAnnotationPresent(ExtField.class)) {    
                    	
                    	ExtField extField = (ExtField)field.getAnnotation(ExtField.class);
                    	
                        String header = extField.fieldName();
                        
                        String fieldType = extField.fieldType();
                        String render = extField.renderer();
                        if(fieldType.equals(Common.DateType)){
                        	render = "renderDate(\""+extField.dateFormat()+"\")";
                        }
                        else if(!extField.optValues().equalsIgnoreCase("null")){
                        	render = "renderOptionValue";
                        }
                        
                        if(!render.trim().isEmpty()){
                        	render = ", renderer: "+render;
                        }
                        
                        String widthString="";
                        if(extField.colWidth()>0){
                        	widthString=", width:"+extField.colWidth();
                        }
                        
                        if(!fieldName.contains(splitor)){
                        	if(fieldName.equalsIgnoreCase("dataStatus")){
                        		columns.append("            ,this.dataStatusColumn\n");                      
                        	}
                        	else{
                        		columns.append("            ,{dataIndex:\"").append(fieldName).append("\", header:\"").append(subHeader+header).append("\", sortable:true").append(render).append(widthString).append("}\n");                      
                        	}
                        }
                        else{
                        	String _fieldName = fieldName.replace(splitor, ".");
                        	//fieldName=fieldName.replace(splitor, "_");
                        	//改了这里
                        	fieldName=fieldName.replace(splitor,dotTag);
                        	
                        	//改了这里，不要render了
                        	//columns.append("            ,{dataIndex:\"").append(fieldName).append("\", header:\"").append(subHeader+header).append("\", sortable:true,renderer:function(value){try{ return "+_fieldName+";}catch(e){return '';} }").append("}\n");
                        	columns.append("            ,{dataIndex:\"").append(fieldName).append("\", header:\"").append(subHeader+header).append("\", sortable:true").append(render).append(widthString).append("}\n");
                        }
                    }
                    else if(field.getName().equalsIgnoreCase("expandText")){
                    	columns.append("            ,this.expanderColumn\n");   
                    }
                }
                else{
                    boolean hasRelatedType = field.isAnnotationPresent(RelatedType.class);
                    if(hasRelatedType){
                        if(!(type+"").contains("java.util.Set")){
                            String fieldClassName=(type+"").replace("class ", "");
                            String subFieldName = ((ExtField)field.getAnnotation(ExtField.class)).subFieldName();
                            //递归
                            if(subString.length()>0) {
                                columns.append(getColumns(subString+splitor+fieldName, fieldClassName,subHeader+subFieldName));
                            }
                            else {
                                columns.append(getColumns(fieldName, fieldClassName,subHeader+subFieldName));
                            }
                        }
                    }
                }            
            }
        }   
        return columns.toString();
    }
    
    public static String getQueryFields(String subString, String className, String subHeader) throws Exception {
        
        StringBuffer columns = new StringBuffer();
        
        //Field[] fields=Class.forName(className).getDeclaredFields();
        Field[] fields=Common.getFiledsIncludingParentClass(className);
        int fieldLength = fields.length;
        
        String splitor = ".";
        
        for(int i=0;i<fieldLength;i++){
            Field field=fields[i];
            String fieldName = field.getName();
            if(subString.length()>0) {
                fieldName = subString+splitor+fieldName;
            }
            boolean shouldNotMapping = field.isAnnotationPresent(NotMapping.class);
            if(!shouldNotMapping){
                field.setAccessible(true);
                Class type = field.getType();
                String basicType = Common.basicTypeMap.get(type+"");
                if(basicType!=null){             
                    if(field.isAnnotationPresent(ExtField.class)) {    
                        String header = ((ExtField)field.getAnnotation(ExtField.class)).fieldName();
                        columns.append(",[\"").append(subHeader+header).append("\", \"").append(fieldName).append("\"]");
                    }
                }
                else{
                    boolean hasRelatedType = field.isAnnotationPresent(RelatedType.class);
                    if(hasRelatedType){
                        if(!(type+"").contains("java.util.Set")){
                            String fieldClassName=(type+"").replace("class ", "");
                            String subFieldName = ((ExtField)field.getAnnotation(ExtField.class)).subFieldName();
                            //递归
                            if(subString.length()>0) {
                                columns.append(getQueryFields(subString+splitor+fieldName, fieldClassName,subHeader+subFieldName));
                            }
                            else {
                                columns.append(getQueryFields(fieldName, fieldClassName,subHeader+subFieldName));
                            }
                        }
                    }
                }            
            }
        }   
        return columns.toString();
    }
    
    public static String getStoreFields(String subString, String className) throws Exception {
        
        StringBuffer columns = new StringBuffer();
        
        //Field[] fields=Class.forName(className).getDeclaredFields();
        Field[] fields=Common.getFiledsIncludingParentClass(className);
        int fieldLength = fields.length;
        
        //改了这里
        //String splitor = "_";
        if(!subString.isEmpty()){
        	columns.append(", \"").append(subString).append("\" ");
        }
        
        for(int i=0;i<fieldLength;i++){
            Field field=fields[i];
            String fieldName = field.getName();
            if(subString.length()>0) {
                //fieldName = subString+splitor+fieldName;
            	//改了这里
            	fieldName= subString+dotTag+fieldName;
            }
            boolean shouldNotMapping = field.isAnnotationPresent(NotMapping.class);
            if(!shouldNotMapping){
                field.setAccessible(true);
                Class type = field.getType();
                String basicType = Common.basicTypeMap.get(type+"");
                if(basicType!=null){
                    columns.append(", \"").append(fieldName).append("\" ");
                }
                else{
                    boolean hasRelatedType = field.isAnnotationPresent(RelatedType.class);
                    if(hasRelatedType){
                        String relatedType = ((RelatedType)field.getAnnotation(RelatedType.class)).type();
                        if(!(type+"").contains("java.util.Set")){
                            String fieldClassName=(type+"").replace("class ", "");
                            //递归
                            if(subString.length()>0) {
                            	//改了这里
                                //columns.append(getStoreFields(subString+splitor+fieldName, fieldClassName));
                            	columns.append(getStoreFields(subString+dotTag+fieldName, fieldClassName));
                            }
                            else {
                                columns.append(getStoreFields(fieldName, fieldClassName));
                            }
                        }
                    }
                }
            }
        }   
        return columns.toString();
    }
    
    //获得属性配置信息
    public static String getFieldConfig(String subString, String className) throws Exception {
        
        StringBuffer fieldConfig = new StringBuffer();
        
        //Field[] fields=Class.forName(className).getDeclaredFields();
        Field[] fields=Common.getFiledsIncludingParentClass(className);
        int fieldLength = fields.length;
        
        String splitor = ".";
        
        for(int i=0;i<fieldLength;i++){
            Field field=fields[i];
            String fieldName = field.getName();
            if(subString.length()>0) {
                fieldName = subString+splitor+fieldName;
            }
            boolean shouldNotMapping = field.isAnnotationPresent(NotMapping.class);
            if(!shouldNotMapping){
                field.setAccessible(true);
                Class type = field.getType();
                String basicType = Common.basicTypeMap.get(type+"");
                if(basicType!=null){
                    if(field.isAnnotationPresent(ExtField.class)) {
                        
                        ExtField extField = ((ExtField)field.getAnnotation(ExtField.class));
                        
                        String fieldType = extField.fieldType();
                        if(fieldType.length()==0){
                            fieldType=basicType;
                        }
                        String optValues = extField.optValues();
                        String validator = extField.validator();
                        String regex = extField.regex();
                        String regexText = extField.regexText();
                        boolean shouldValidate = extField.shouldValidate();
                        
                        fieldConfig.append(",\"").append(fieldName)
                            .append("\":{\"fieldType\":\"")
                            .append(fieldType)
                            .append("\",\"optValues\":")
                            .append(optValues)
                            .append(",\"shouldValidate\":")
                            .append(shouldValidate)
                            .append(",\"validator\":")
                            .append(validator)
                            .append(",\"regex\":")
                            .append(regex)
                            .append(",\"regexText\":\"")
                            .append(regexText).append("\"")
                            .append("}");
                    }
                }
                else{
                    boolean hasRelatedType = field.isAnnotationPresent(RelatedType.class);
                    if(hasRelatedType){
                        String relatedType = ((RelatedType)field.getAnnotation(RelatedType.class)).type();
                        if(!(type+"").contains("java.util.Set")){
                            String fieldClassName=(type+"").replace("class ", "");
                            //递归
                            if(subString.length()>0) {
                                fieldConfig.append(getFieldConfig(subString+splitor+fieldName, fieldClassName));
                            }
                            else {
                                fieldConfig.append(getFieldConfig(fieldName, fieldClassName));
                            }
                        }
                    }
                }
            }
        }   
        return fieldConfig.toString();
    }
   
    public static void createExtGridForClass(String templateString,String className) throws Exception {
    	
        String[] fullClassPathName = className.split("\\.");
        String shortClassName = fullClassPathName[fullClassPathName.length-1];
        
        //String extGrid = templateString.replace("[ClassName]", shortClassName);
        String extGrid = Common.classTypeMapping(templateString, className) ;
        
        extGrid = extGrid.replace("[InitColumns]", getColumns("",className, ""));
        extGrid = extGrid.replace("[InitDataStatusColumn]", getDataStatusColumn(className));
        extGrid = extGrid.replace("[InitExpanderColumn]", getExpanderColumn(className));
        
        extGrid = extGrid.replace("[InitOnRender]", getOnRender(className));
        
        String storeFields = getStoreFields("",className);
        storeFields=storeFields.replaceFirst(",", "");
        extGrid = extGrid.replace("[StoreFields]", storeFields);
        
        String queryFields = getQueryFields("",className,"");
        queryFields=queryFields.replaceFirst(",", "");
        extGrid = extGrid.replace("[QueryFields]", queryFields);
        
        String fieldConfig = getFieldConfig("",className);
        fieldConfig=fieldConfig.replaceFirst(",", "");
        extGrid = extGrid.replace("[FieldConfig]", fieldConfig);
        
        /*
        if(className.endsWith("_View")){
        	extGrid = extGrid.replace("_ViewForm", "Form");
        	extGrid = extGrid.replace("command.htm?cmd=delete"+shortClassName, "command.htm?cmd=delete"+shortClassName.replace("_View", ""));
        }*/
        Class clazz = Class.forName(className);
    	if(clazz.isAnnotationPresent(DbView.class)){
    		String dbViewSuf = ((DbView)clazz.getAnnotation(DbView.class)).dbViewSuf();
    		extGrid = extGrid.replace(dbViewSuf+"Form", "Form");
        	extGrid = extGrid.replace("command.htm?cmd=delete"+shortClassName, "command.htm?cmd=delete"+shortClassName.replace(dbViewSuf, ""));
    	}
    	
    	if(extGrid.contains(",{dataIndex:\"expandText\"")){
    		extGrid = extGrid.replace("this.getRowNumberer()", "this.getRowNumberer(),this.expanderColumn");
    		extGrid = extGrid.replace(",{dataIndex:\"expandText\"", "//,{dataIndex:\"expandText\"");
    	}
    	
    	//Set [forceFit] and [autoExpandColumn]
    	boolean forceFit = false;
    	String autoExpandColumn = "false";
    	if(clazz.isAnnotationPresent(ExtWidge.class)){
    		forceFit = ((ExtWidge)clazz.getAnnotation(ExtWidge.class)).grid_viewforceFix();
    		autoExpandColumn = ((ExtWidge)clazz.getAnnotation(ExtWidge.class)).grid_autoExpandColumn();
    	}
    	extGrid = extGrid.replace("[forceFit]",forceFit+"");
		extGrid = extGrid.replace("[autoExpandColumn]", autoExpandColumn);
        
        FileOutputStream   out   =   new   FileOutputStream(extGridDir+shortClassName+"Grid.js");
        byte[]content = extGrid.getBytes(encode);
        out.write(content, 0, content.length);
    }
    
    
    
    public static void createAllExtGrids() throws Exception {
        
        File file = new File(extGridDir);
        file.mkdirs();

        FileInputStream in = new FileInputStream(template);
        byte[] readBytes = new byte[in.available()];
        in.read(readBytes);
        String string4file = new String(readBytes,encode);
        
        //System.out.println("string4file:"+string4file);
        
        in.close();
        
        List<String>allClasses = Common.getAllClasses();
        
        int size = allClasses.size();
        for(int i=0;i<size;i++) {
            createExtGridForClass(string4file,allClasses.get(i));
        }
    }
    
	public static void main(String[]args) throws Exception, ClassNotFoundException{
	    createAllExtGrids();
	}
}
