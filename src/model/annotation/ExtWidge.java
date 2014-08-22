package model.annotation;

import java.lang.annotation.Documented;   
import java.lang.annotation.Inherited;   
import java.lang.annotation.Retention;   
import java.lang.annotation.Target;   
import java.lang.annotation.ElementType;   
import java.lang.annotation.RetentionPolicy; 

import codeutil.ExtFormUtil;
 
@Retention(RetentionPolicy.RUNTIME)   
@Documented  
@Inherited 
public @interface ExtWidge {
	
	int marginLeft() default ExtFormUtil.defaultMarginLeft;
	
	int labelWidth() default ExtFormUtil.defaultLabelWidth;
	
	int fieldWidth() default ExtFormUtil.defaultFieldWidth;
	
	int columns()    default ExtFormUtil.defaultColumns;
	
	String excludeFields() default "";
	
	//grid_gridViewforceFix和grid_autoExpandColumn不可同时设置
	boolean grid_viewforceFix() default false;
	String  grid_autoExpandColumn() default "false";
	
	String gridcombo_assocParent() default "";
	String gridcombo_assocChild() default "";
	String gridcombo_assocDepField() default "";
	
	//String pattern - fieldName1:value1,fieldName2:value2...
	String inherit_defaultValuesForChild() default "";
}
