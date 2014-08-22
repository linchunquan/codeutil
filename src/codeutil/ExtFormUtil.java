package codeutil;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import model.annotation.ExtField;
import model.annotation.ExtWidge;
import model.annotation.NotMapping;
import model.annotation.RelatedType;

public class ExtFormUtil {
    
	//[--------------------------------------------------------------------------
	
    private static String extFormDir = "output/ext/form/";
    
    private static String widgeProxyTemplate = "config/template/WidgeProxyTemplate.js";
    
    private static String encode="UTF-8";
    
    public final static int defaultMarginLeft = -5;
    
    public final static int defaultLabelWidth = 100;
    
    public final static int defaultFieldWidth = 150;
    
    public final static int defaultColumns = 2;
    
    private static int fieldsetPadding = 22;
    
    private static int cellPadding = 10;
    
    private static int tabPanelPadding=3;
    
    private static int windowPanelPadding=15;
    
    private static int CHAR_LENGTH = 6;
    
    public static int getMarginLeft(String className) throws Exception{
    	Class clazz = Class.forName(className);
    	if(clazz.isAnnotationPresent(ExtWidge.class)){
    		return ((ExtWidge)clazz.getAnnotation(ExtWidge.class)).marginLeft();
    	}
    	return defaultMarginLeft;
    }
    
    public static int getLabelWidth(String className) throws Exception {
		
		
		int maxLength = -1;
		Field[] fields = Common.getFiledsIncludingParentClass(className);
		int fieldLength = fields.length;
		for (int i = 0; i < fieldLength; i++) {
			Field field = fields[i];
			if (field.isAnnotationPresent(ExtField.class)) {
				ExtField extField = ((ExtField) field.getAnnotation(ExtField.class));
				String labelName = extField.fieldName();
				if(!labelName.isEmpty()){
					int curLength = getLabelLength(labelName);
					if(curLength>0 && curLength>maxLength){
						maxLength = curLength;
					}
				}
			}
		}
		if(maxLength>0){
			return maxLength+cellPadding;
		}
		
		Class clazz = Class.forName(className);
		if (clazz.isAnnotationPresent(ExtWidge.class)) {
			return ((ExtWidge) clazz.getAnnotation(ExtWidge.class))
					.labelWidth();
		}
		
		return defaultLabelWidth;
	}
    
    public static int getFieldWidth(String className) throws Exception{
    	Class clazz = Class.forName(className);
    	if(clazz.isAnnotationPresent(ExtWidge.class)){
    		return ((ExtWidge)clazz.getAnnotation(ExtWidge.class)).fieldWidth();
    	}
    	return defaultFieldWidth;
    }
    
    public static int getColumns(String className) throws Exception{
    	Class clazz = Class.forName(className);
    	if(clazz.isAnnotationPresent(ExtWidge.class)){
    		return ((ExtWidge)clazz.getAnnotation(ExtWidge.class)).columns();
    	}
    	return defaultColumns;
    }
    
    public static int getFieldContainerWidth(String className) throws Exception{
    	//Field[] fields=Class.forName(className).getDeclaredFields();
    	Field[] fields=Common.getFiledsIncludingParentClass(className);
        int fieldLength = fields.length;
        int maxLength = 
        	getColumns(className)*(getLabelWidth(className)+getFieldWidth(className))
        			+getMarginLeft(className)
        			+cellPadding*(getColumns(className)-1);
        
        System.out.println("Initial widge width = " + maxLength + " for "+className);
        
        for(int i=0;i<fieldLength;i++){
        	Field field=fields[i];
        	Class type = field.getType();
        	String basicType = Common.basicTypeMap.get(type+"");
            if(basicType == null){             
                if(field.isAnnotationPresent(ExtField.class)&&field.isAnnotationPresent(RelatedType.class)){ 
                	if(!(type+"").contains("java.util.Set")){
                		if(!((ExtField)field.getAnnotation(ExtField.class)).shouldAddToTab()){
	                        String fieldClassName=(type+"").replace("class ", "");
	                        int widgeWidthForFieldClass = getFieldContainerWidth(fieldClassName)+fieldsetPadding*2+getLabelWidth(className)+getMarginLeft(className);
	                        if(widgeWidthForFieldClass>maxLength){
	                        	maxLength=widgeWidthForFieldClass;
	                        }
                		}
                    }
                }
            }
        }
        return maxLength;
    }
    
    public static int getFormPanelWidth(String className)throws Exception{
    	
    	//Field[] fields=Class.forName(className).getDeclaredFields();
    	Field[] fields=Common.getFiledsIncludingParentClass(className);
        int fieldLength = fields.length;
        int maxLength = getFieldContainerWidth(className);
        
        for(int i=0;i<fieldLength;i++){
        	Field field=fields[i];
        	Class type = field.getType();
        	String basicType = Common.basicTypeMap.get(type+"");
            if(basicType == null){             
                if(field.isAnnotationPresent(ExtField.class)&&field.isAnnotationPresent(RelatedType.class)){ 
                	if(!(type+"").contains("java.util.Set")){
                		if(((ExtField)field.getAnnotation(ExtField.class)).shouldAddToTab()){
	                        String fieldClassName=(type+"").replace("class ", "");
	                        int widgeWidthForFieldClass = getFieldContainerWidth(fieldClassName)+fieldsetPadding*2+2*tabPanelPadding;
	                        if(widgeWidthForFieldClass>maxLength){
	                        	maxLength=widgeWidthForFieldClass;
	                        }
                		}
                    }
                }
            }
        }
        return maxLength;
    }
    
    public static String getFieldWidge(Field field,String className,Map<String,String>defaultValuesForChild) throws Exception{
    	
    	String fieldWidge = "";
    	
    	String fieldName = field.getName();
        
    	boolean shouldNotMapping = field.isAnnotationPresent(NotMapping.class);
        
    	if(!shouldNotMapping){
    		
        	field.setAccessible(true);
            Class type = field.getType();
            String basicType = Common.basicTypeMap.get(type+"");
            
            if(field.isAnnotationPresent(ExtField.class)){
            	
            	ExtField extField = ((ExtField)field.getAnnotation(ExtField.class));
            
            	if(basicType!=null){
            		
            		String fieldType = "";
                    //if(fieldName.equals( Common.IDFieldName) || fieldName.equals(Common.ParentIDFieldName)){
                    //	fieldType = Common.HiddenType;
                    //}
                    //else{
                    	fieldType = extField.fieldType();
                    	if(fieldType.length() == 0){
                    		fieldType = basicType;
                    		if(!extField.optValues().trim().equals("null")){
                        		fieldType = Common.ComboType;
                        	}
                    		if(extField.useCheckBox()){
                    			fieldType = Common.CheckboxType;
                    		}
                    	}
                    //}
                    
                    String fieldLabel = extField.fieldName();
                    
                    boolean allowBlank = extField.allowBlank();

                    boolean shouldValidate = extField.shouldValidate();
                    
                    boolean shouldAddToTab = extField.shouldAddToTab();
                     
                    String validator = extField.validator();
                    
                    String regex = extField.regex();
                    
                    String regexText = extField.regexText();
                    
                    int colspan = extField.colspan();
                    
                    int width = extField.width();
                    
                    int height = extField.height();
                    
                    boolean editable = extField.editable();
                    
                    boolean readOnly = extField.readOnly();
                    
                    String optValues = extField.optValues();
                    
                    String defaultValue = defaultValuesForChild.containsKey(field.getName())?defaultValuesForChild.get(field.getName()) : extField.defaultValue();
                    //-------------------------------------------------------------------------
                    String template 
                    	= "			[fieldName]:new [ExtFieldType]({[defaultValue],fieldLabel:'[fieldLabel]',allowBlank:[allowBlank],validator:[validator],regex:[regex],regexText:[regexText],colspan:[colspan],height:[height],width:[width],shouldAddToTab:[shouldAddToTab],[readOnly],[gridcombo],[comboTemplate]})";
                    String comboTemplate
                    	= "editable:[editable] ,mode:'local' ,triggerAction:'all' ,store:new Ext.data.SimpleStore({fields : ['text', 'value'],data:[optValues]}) ,valueField:'value',displayField:'text',emptyText:'请选择'";
                    //if(!defaultValue.trim().equalsIgnoreCase("null")){
                    //	comboTemplate = comboTemplate+" ,defaultValue:"+defaultValue;
                    //}
                    if(!defaultValue.trim().equalsIgnoreCase("null")){
                    	template = template.replace("[defaultValue]", "defaultValue:"+defaultValue);
                    }
                    else{
                    	template = template.replace("[defaultValue],", "");
                    }
                    
                    template=template.replace("[fieldName]", fieldName);
                    template=template.replace("[ExtFieldType]", Common.getExtFieldType(fieldType));
                    template=template.replace("[fieldLabel]", fieldLabel);
                    template=template.replace("[allowBlank]", allowBlank+"");
                    
                    if(readOnly){
                    	template=template.replace("[readOnly]","readOnly:true");
                    }
                    else{
                    	template=template.replace("[readOnly],","");
                    }
                    
                    if(Common.GridComboBox.equals(fieldType)){
                    	String gridName     = extField.gridcombo_gridName();
                    	String valueField   = extField.gridcombo_valueField();
                    	String displayField = extField.gridcombo_displayField();
                    	String findCmd      = extField.gridcombo_findCmd();
                    	String selTitle     = extField.gridcombo_selTitle();
                    	int    selWidth     = extField.gridcombo_selWidth();
                    	int    selHeight    = extField.gridcombo_selHeight();
                    	template=template.replace("[gridcombo]",
                    			"gridName:'"+gridName
                    			+"',valueField:'"+valueField
                    			+"',displayField:'"+displayField
                    			+"',findCmd:'"+findCmd
                    			+"',selTitle:'"+selTitle
                    			+"',selWidth:"+selWidth
                    			+",selHeight:"+selHeight);
                    }
                    else{
                    	template=template.replace("[gridcombo],","");
                    }
                    
                    if(!shouldValidate){
                    	template=template.replace(",validator:[validator]","");
                    	template=template.replace(",regex:[regex]","");
                    	template=template.replace(",regexText:[regexText]","");
                    }
                    else{
                    	
                    	if(validator.trim().equalsIgnoreCase("null")&&regex.trim().equalsIgnoreCase("null")&&!fieldType.equalsIgnoreCase(Common.DateType)){
                    		
                    		String defaultValidator = "Validator.getBasicValidator(Validator.basicType.[TYPE],{min:[MIN],max:[MAX]})";
                    		
                    		if(basicType.equalsIgnoreCase(Common.IntType)){
                    			defaultValidator=defaultValidator.replace("[TYPE]", "INTEGER");
                    		}
                    		else if(basicType.equalsIgnoreCase(Common.DoubleType)){
                    			defaultValidator=defaultValidator.replace("[TYPE]", "DOUBLE");
                    		}
                    		else if(basicType.equalsIgnoreCase(Common.LongType)){
                    			defaultValidator=defaultValidator.replace("[TYPE]", "LONG");
                    		}
                    		else if(basicType.equalsIgnoreCase(Common.StringType)){
                    			defaultValidator=defaultValidator.replace("[TYPE]", "STRING");
                    		}
                    		
                    		double min=extField.min();
                    		if(min == Double.MIN_VALUE){
                    			defaultValidator=defaultValidator.replace("min:[MIN]", "");
                    		}
                    		else{
                    			defaultValidator=defaultValidator.replace("[MIN]",min+"");
                    		}
                    		
                    		double max=extField.max();
                    		if(max == Double.MAX_VALUE){
                    			defaultValidator=defaultValidator.replace("max:[MAX]", "");
                    		}
                    		else{
                    			defaultValidator=defaultValidator.replace("[MAX]",max+"");
                    		}
                    		
                    		//后续处理
                    		defaultValidator=defaultValidator.replace(",{,}", "");
                    		defaultValidator=defaultValidator.replace(",}", "}");
                    		defaultValidator=defaultValidator.replace("{,", "{");
                    		
                    		template=template.replace("[validator]",defaultValidator);
                    		template=template.replace(",regex:[regex],regexText:[regexText]","");
                    	}
                    	else{
                    		template=template.replace("[validator]",validator);
                    		template=template.replace("[regex]",regex);
                    		template=template.replace("[regexText]","'"+regexText+"'");
                    	}
                    }
                    
                    if(colspan == -1){
                    	template=template.replace(",colspan:[colspan]","");
                    }
                    else{
                    	template=template.replace("[colspan]",colspan+"");
                    }
                    
                    if(height == -1){
                    	template=template.replace(",height:[height]","");
                    }
                    else{
                    	template=template.replace("[height]",height+"");
                    }
                    
                    if(width == -1){
                    	if(!fieldType.equals(Common.TextAreaType)&&!fieldType.equals(Common.HtmlEditor)){
                    		//template=template.replace(",width:[width]","");
                    		template=template.replace("[width]",(ExtFormUtil.getFieldWidth(className))+"");
                    	}
                    	else{
                    		//template=template.replace("[width]",(getFieldContainerWidth(className)-ExtFormUtil.getLabelWidth(className)-ExtFormUtil.getMarginLeft(className))+"");
                    		//长度太长，对整体布局造成影响，更为与普通textfield同宽
                    		template=template.replace("[width]",(ExtFormUtil.getFieldWidth(className))+"");
                    	}
                    }
                    else{
                    	template=template.replace("[width]",width+"");
                    }
                    
                    if(shouldAddToTab){
                    	template=template.replace("[shouldAddToTab]",true+"");
                    }
                    else{
                    	template=template.replace(",shouldAddToTab:[shouldAddToTab]","");
                    }
                    
                    if(!fieldType.equals(Common.ComboType)){
                    	template=template.replace(",[comboTemplate]", "");
                    }
                    else{
                    	comboTemplate = comboTemplate.replace("[editable]",  editable+"");
                    	comboTemplate = comboTemplate.replace("[optValues]", optValues+"");
                    	template = template.replace("[comboTemplate]", comboTemplate);
                    }
                    
                    fieldWidge = template;
                
             	}
            	else{
            		if(!(type+"").contains("java.util.Set")){
	            		String fieldClassName=(type+"").replace("class ", "");
						String fieldLabel = extField.subFieldName();
					 	int    widgeWidth = ExtFormUtil.getFieldContainerWidth(fieldClassName);
					 
					 	String[] fullClassPathName = fieldClassName.split("\\.");
					 	String shortClassName = fullClassPathName[fullClassPathName.length-1];
					 	boolean shouldAddToTab = extField.shouldAddToTab();
					 
					 	fieldWidge = "			"+fieldName+":new "+shortClassName+"WidgeProxy({fieldLabel:'"+fieldLabel+"',widgeWidth:"+(widgeWidth+fieldsetPadding*2)+",shouldAddToTab:"+shouldAddToTab+"})";
            		}
            	}
            	
            	if(extField.useCheckBox()){
            		fieldWidge = fieldWidge.replace(",validator:Validator.getBasicValidator(Validator.basicType.INTEGER)", ",getValue:function(){return this.checked?1:0}");
            	}
            }
    	}
    	
    	return fieldWidge;
    }
    
    public static String getWidgeMap(String className) throws Exception, ClassNotFoundException{
    	StringBuffer widgeMap = new StringBuffer();
    	//Field[] fields=Class.forName(className).getDeclaredFields();
    	Field[] fields=Common.getFiledsIncludingParentClass(className);
    	Map<String,String> defaultValuesForChild = Common.getDefaultValuesForChild(className);
    	int fieldLength = fields.length;
        for(int i=0;i<fieldLength;i++){
        	String fieldWidge = getFieldWidge(fields[i],className,defaultValuesForChild);
        	if(fieldWidge.length()>0){
        		widgeMap.append(fieldWidge).append(",").append("\n");
        	}
        }
        int lastIndex = widgeMap.toString().lastIndexOf(",\n");
        if(lastIndex>-1){
        	return widgeMap.toString().substring(0, lastIndex);
        }
        return widgeMap.toString();
    }
    
    private static String getGridcomboAssoc(String className) throws Exception, ClassNotFoundException{
    	Class clazz = Class.forName(className);
    	if(clazz.isAnnotationPresent(ExtWidge.class)){
    		ExtWidge extWidge = ((ExtWidge)clazz.getAnnotation(ExtWidge.class));
	    	if(    !extWidge.gridcombo_assocParent().isEmpty()
	    		&& !extWidge.gridcombo_assocChild().isEmpty()
	    		&& !extWidge.gridcombo_assocDepField().isEmpty()){
	    		StringBuffer strBuf = new StringBuffer("GridComboBoxUtil.assocTowComboBoxes(defaultCfg.widgeMap.")
	    		   .append(extWidge.gridcombo_assocParent())
	    		   .append(", defaultCfg.widgeMap.")
	    		   .append(extWidge.gridcombo_assocChild())
	    		   .append(", \"")
	    		   .append(extWidge.gridcombo_assocDepField())
	    		   .append("\");");
	    		
	    		return strBuf.toString();
    		}
    	}
    	return "";
    }
    
    public static void createWidgeProxyForClass(String templateString,String className) throws Exception {
    	
        String[] fullClassPathName = className.split("\\.");
        String shortClassName = fullClassPathName[fullClassPathName.length-1];

        String widgeProxy = Common.classTypeMapping(templateString, className) ;
        widgeProxy=widgeProxy.replace("[marginLeft]", ExtFormUtil.getMarginLeft(className)+"");
        widgeProxy=widgeProxy.replace("[labelWidth]", ExtFormUtil.getLabelWidth(className)+"");
        widgeProxy=widgeProxy.replace("[fieldWidth]", ExtFormUtil.getFieldWidth(className)+"");
        widgeProxy=widgeProxy.replace("[widgeWidth]", ExtFormUtil.getFieldContainerWidth(className)+"");
        widgeProxy=widgeProxy.replace("[columns]",    ExtFormUtil.getColumns   (className)+"");
        widgeProxy=widgeProxy.replace("[widgeMap]", getWidgeMap(className));
        widgeProxy=widgeProxy.replace("[GridcomboAssoc]", getGridcomboAssoc(className));
        widgeProxy=widgeProxy.replace("[windowWidth]", (getFormPanelWidth(className)+2*(windowPanelPadding))+"");
       
        FileOutputStream   out   =   new   FileOutputStream(extFormDir+shortClassName+"WidgeProxy.js");
        byte[]content = widgeProxy.getBytes(encode);
        out.write(content, 0, content.length);
    }
    
    public static void createAllWidgeProxies() throws Exception {
        
        File file = new File(extFormDir);
        file.mkdirs();

        FileInputStream in = new FileInputStream(widgeProxyTemplate);
        byte[] readBytes = new byte[in.available()];
        in.read(readBytes);
        String string4file = new String(readBytes,encode);
        
        in.close();
        
        List<String>allClasses = Common.getAllClasses();
        
        int size = allClasses.size();
        for(int i=0;i<size;i++) {
        	createWidgeProxyForClass(string4file,allClasses.get(i));
        }
    }
    //]--------------------------------------------------------------------------
    
    public static int getLabelLength(String labelName){
    	
    	char[]chars = labelName.toCharArray();
		int length = chars.length;
		int curLength = 0;
		for(int j=0;j<length;j++){
			if(chars[j] >= 0x80){
				curLength += 2;
			}
			else{
				curLength += 1;
			}
		}
		return (curLength+1)*CHAR_LENGTH;
    	/*
    	Font font = new Font("Arial", Font.PLAIN, 12);
    	FontMetrics metrics = new FontMetrics(font) {};
    	Rectangle2D bounds = metrics.getStringBounds(labelName+":", null);
    	int widthInPixels = (int) bounds.getWidth();
    	return widthInPixels;*/
    }
	/*public static void main(String[]args) throws Exception, ClassNotFoundException{
		createAllWidgeProxies();
	}*/
    public static void main(String [] args){
    	System.out.println(getLabelLength("你好吗"));
    }
}
