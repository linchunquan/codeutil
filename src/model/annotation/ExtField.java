package model.annotation;

import java.lang.annotation.Documented;   
import java.lang.annotation.Inherited;   
import java.lang.annotation.Retention;   
import java.lang.annotation.Target;   
import java.lang.annotation.ElementType;   
import java.lang.annotation.RetentionPolicy; 

@Target(ElementType.FIELD)   
@Retention(RetentionPolicy.RUNTIME)   
@Documented  
@Inherited 
public @interface ExtField {
	
	String fieldName() default "";
	
	//int long double string date...
	String fieldType() default "";
	
	//定义Field名称，上级Field名称作为前缀与fieldName一起作为ui显示
    String subFieldName() default "";
    
    //[[text,value],[text,value],[text,value]...]
    String optValues() default "null";
    
    //验证函数的名称，实际定义需要在前端的js里，专门的文件MyValidators.js
    //注意：validator除了定义校验逻辑，还必须定义错误提示语，即返回字符串的值.
    String validator() default "null";
    
    String renderer() default "";
    
    //正则表达式验证，主要要采用双斜杠形式 例如/^\d+(\.\d+)?$/
    String regex()default "null";
    
    //当使用正则表达式时的错误提示语.
    //注意：当使用validator时，错误提示语是由所定义的函数返回的！
    String regexText()default "您输入的数据格式不正确！";
    
    boolean shouldValidate() default true;
    
    boolean shouldAddToTab() default false;

    int colspan()default -1;
    
    int width()default -1;
    
    int height()default -1;
    
    int colWidth()default 0;
    
    boolean editable() default true;
    
    boolean readOnly() default false;
    
    boolean allowBlank() default false;
    
    double max()default Double.MAX_VALUE;
    
    double min()default Double.MIN_VALUE;
    
    String dateFormat() default "Y-m-d H:i:s"; 
    
    String defaultValue() default "null";
    
    boolean useCheckBox() default false;
    
    /**一些常用的正则表达式
   Email    : /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
   Phone    : /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/,
   Mobile   : /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/,
   Url      : /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/,
   Currency : /^\d+(\.\d+)?$/,
   Number   : /^\d+$/,
   Zip      : /^[1-9]\d{5}$/,
   QQ       : /^[1-9]\d{4,8}$/,
   Integer : /^[-\+]?\d+$/,
   Double   : /^[-\+]?\d+(\.\d+)?$/,
   English : /^[A-Za-z]+$/,
   Chinese : /^[\u0391-\uFFE5]+$/,
   Username : /^[a-z]\w{3,}$/i,
   UnSafe   : /^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/,
     */
    
    /*************************************************************************/
    /****************************** Grid Combox ******************************/
    
    //boolean gridcombo_apply()    default false;
    String  gridcombo_gridName() default "";
	String  gridcombo_valueField()   default "";
	String  gridcombo_displayField() default "";
	String  gridcombo_findCmd()   default "";
	String  gridcombo_selTitle()  default "请选择";
	int     gridcombo_selWidth()  default 463;
	int     gridcombo_selHeight() default 300;
    
	/*************************************************************************/
    /*************************************************************************/
	
	/*************************************************************************/
    /****************************** Data Status ******************************/
    String  gridStatusColumn_className() default "";
	/*************************************************************************/
    /*************************************************************************/
}
