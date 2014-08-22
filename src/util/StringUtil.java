package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class StringUtil {
	
	public static boolean isEmptyString(String string){
		return string == null || string.length() == 0;
	}
	
	public static boolean isEmptyStringCollection(Collection<String>strings){
		return strings == null || strings.size() == 0;
	}
	
	public static boolean isEmptyStringCollection(String[]strings){
		return strings == null || strings.length == 0;
	}
	
	/**
	 * 剥去首尾字符
	 * @param string 待处理的字符串
	 * @param trim 除掉首尾字符后，是否继续去掉首尾空白
	 * @return
	 */
	public static String stripFirstAndLastChar(String string,boolean trim){
		if(trim){
			string=string.trim();
		}
		String str =  string.substring(1, string.length()-1);
		if(trim){
			return str.trim();
		}
		return str;
	}
	
	public static String replacePatternMatchStringToReplacement(String originalStr,String matchPattern,int patternPara,String replacement){

		Pattern p=null;
		
		if(patternPara>-1){
			p=Pattern.compile(matchPattern,patternPara);
		}
		
		else{
			p=Pattern.compile(matchPattern);
		}
		
		Matcher m=p.matcher(originalStr);
		StringBuffer buf=new StringBuffer();
		buf.toString();
		return m.replaceAll(replacement);
	}
	
	public static String stripPatternMatchString(String originalStr,String matchPattern,int patternPara){
		return replacePatternMatchStringToReplacement(originalStr,matchPattern,patternPara,"");
	}
	
	public static List<String> getPatternMatchStringArray(String originalStr,String matchPattern,int patternPara){

		Pattern p=null;
		
		if(patternPara>-1){
			p=Pattern.compile(matchPattern,patternPara);
		}
		
		else{
			p=Pattern.compile(matchPattern);
		}
		
		Matcher m=p.matcher(originalStr);
		List<String>strMatch = new ArrayList<String>();
		
		while(m.find()){
			strMatch.add(m.group());
		}
		
		return strMatch;
	}
	
	
	public static void test(){
		
		//String matchPattern="aaa|bc"; 
		//String originalStr="aaabc efg aaaaABC"; 
        
		
		//String matchPattern = ".+\\\\(.+)$"; 
        //String originalStr  = "c:\\dir1\\dir2\\name.txt"; 
		
		//String matchPattern = "\\{(.*?)[0-9]+(.*?)\\}"; 
        //String originalStr  = "fdsfs{like 10 }afds{ok 11 }cd{ 12 }"; 
       
		//String matchPattern = "[0-9]+?"; 
        //String originalStr  = "fdsfs{like 101 }afds{ok 13 }cd{ 121 }"; 
		
		//String matchPattern = "\\{(.*?)(\\[\\s*)[0-9]+(\\s*\\])(.*?)\\}"; 
        //String originalStr  = "fdsfs{likes[ 10] }afds{ok [11] }cd{ [12] }"; 
		
		
		String matchPattern = "\\{[^\\{\\}]*(\\[\\s*([0-9]+)\\s*\\])[^\\{\\}]*\\}"; 
	    String originalStr  = "fdsfs{likes[ 10] }afds{ok [11] }cd{ [g] }"; 
        
        System.out.println(getPatternMatchStringArray(originalStr,matchPattern,-1));
	}
	
	public static void main(String[] args) {
		
		test();
		
		String string = " a b c ";
		System.out.println(stripFirstAndLastChar(string,true));
		System.out.println(string);
	}
}
