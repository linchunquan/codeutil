package util;

import java.util.List;

public class HqlUtil {
	
	//private static final String queryArgumentPositionPattern =   "\\[\\s*[0-9]+\\s*\\]";
	//private static final String queryArgumentPattern = "\\{(.*?)"+queryArgumentPositionPattern+"(.*?)\\}";
	
	//private static final String additionalConditionPositionPattern =   "\\[\\s*a\\s*\\]";
	//private static final String additionalConditionPattern = "\\{(.*?)"+additionalConditionPositionPattern+"(.*?)\\}";
	
	//private static final String sorttingConditionPositionPattern =   "\\[\\s*s\\s*\\]";
	//private static final String sorttingConditionPattern = "\\{(.*?)"+sorttingConditionPositionPattern+"(.*?)\\}";
	
	private static final String queryArgumentPositionPattern =      "(\\[\\s*([0-9]+)\\s*\\])";
	private static final String queryArgumentPattern = "\\{[^\\{\\}]*(\\[\\s*([0-9]+)\\s*\\])[^\\{\\}]*\\}";
	
	private static final String additionalConditionPositionPattern =      "(\\[\\s*(a)\\s*\\])";
	private static final String additionalConditionPattern = "\\{[^\\{\\}]*(\\[\\s*(a)\\s*\\])[^\\{\\}]*\\}";
	
	private static final String sorttingConditionPositionPattern =        "(\\[\\s*(s)\\s*\\])";
	private static final String sorttingConditionPattern   = "\\{[^\\{\\}]*(\\[\\s*(s)\\s*\\])[^\\{\\}]*\\}";
	
	
	/**
	 * 去掉查询参数模式
	 * @param hql
	 * @return
	 */
	private static String stripQueryArgumentPattern(String hql){
		return StringUtil.stripPatternMatchString(hql, queryArgumentPattern, -1);
	}
	
    
	/**
	 * 去掉附加查询条件模式
	 * @param hql
	 * @return
	 */
	private static String stripAdditionalConditionPattern(String hql){
		return StringUtil.stripPatternMatchString(hql, additionalConditionPattern, -1);
	}
	
	
	/**
	 * 去掉排序条件模式
	 * @param hql
	 * @return
	 */
	private static String stripSorttingConditionPattern(String hql){
		return StringUtil.stripPatternMatchString(hql, sorttingConditionPattern, -1);
	}
	
	
	/**
	 * 根据查询参数对hql赋值
	 * @param hql
	 * @param queryArgs
	 * @return
	 */
	private static String assignQueryArgsToHql(String hql,String[]queryArgs){
		List<String>matchPatternStrings = StringUtil.getPatternMatchStringArray(hql, queryArgumentPattern, -1);
		int size = matchPatternStrings.size();
		for(int i=0;i<size;i++){
			String stringToAssign = matchPatternStrings.get(i);
			String positionString = StringUtil.getPatternMatchStringArray(stringToAssign, queryArgumentPositionPattern, -1).get(0);
			
			int arguIndex = Integer.valueOf(StringUtil.stripFirstAndLastChar(positionString, true));
			String argument = queryArgs[arguIndex];
			
			hql = hql.replace(stringToAssign, StringUtil.stripFirstAndLastChar(StringUtil.replacePatternMatchStringToReplacement(stringToAssign, queryArgumentPositionPattern, -1, argument),false));
		}
		return hql;
	}
	
	
	/**
	 * 根据附加查询条件对hql赋值
	 * @param hql
	 * @param additionalCondition
	 * @return
	 */
	private static String assignAdditionalConditionToHql(String hql,String additionalCondition){
		List<String>matchPatternStrings = StringUtil.getPatternMatchStringArray(hql, additionalConditionPattern, -1);
		
		String stringToAssign = matchPatternStrings.get(0);
		String positionString = StringUtil.getPatternMatchStringArray(stringToAssign, additionalConditionPositionPattern, -1).get(0);
		
		hql = hql.replace(stringToAssign, StringUtil.stripFirstAndLastChar(stringToAssign,false));
		hql = hql.replace(positionString, additionalCondition);
		
		return hql;
	}
	
	
	private static String assignSorttingConditionPatternToHql(String hql,String sorttingCondition){
		
		List<String>matchPatternStrings = StringUtil.getPatternMatchStringArray(hql, sorttingConditionPattern, -1);
		
		String stringToAssign = matchPatternStrings.get(0);
		String positionString = StringUtil.getPatternMatchStringArray(stringToAssign, sorttingConditionPositionPattern, -1).get(0);
		
		hql = hql.replace(stringToAssign, StringUtil.stripFirstAndLastChar(stringToAssign,false));
		hql = hql.replace(positionString, sorttingCondition);
		
		return hql;
	}
	
	/**
	 * 对hql语句赋值
	 * @param hql 待赋值的HQL语句
	 * @param queryArgs 查询条件，通过正则表达式匹配赋值
	 * @param additionalCondition 附加查询条件，通过正则表达式匹配赋值
	 * @param sorttingCondition 排序条件
	 * @return
	 */
	public static String assignAllArgusToHql(
			String hql,
			String[]queryArgs,
			String additionalCondition,
			String sorttingCondition
	){
		
		
		if(StringUtil.isEmptyStringCollection(queryArgs)){
			hql=stripQueryArgumentPattern(hql);
		}
		else{
			hql=assignQueryArgsToHql(hql,queryArgs);
		}
		
		if(StringUtil.isEmptyString(additionalCondition)){
			hql=stripAdditionalConditionPattern(hql);
		}
		else{
			hql=assignAdditionalConditionToHql(hql,additionalCondition);
		}
		
		if(StringUtil.isEmptyString(sorttingCondition)){
			
			hql=stripSorttingConditionPattern(hql);
		}
		else{
			hql=assignSorttingConditionPatternToHql(hql,sorttingCondition);
		}
		
		return hql;
	}
	
	/**
	 * 从主hql查询语句获得用于查询记录数的hql语句，算法简单，不具备普适性，谨慎使用
	 * @param hql
	 * @return
	 */
	public static String getCountHqlFromQueryHql(String hql){  
		return "select count(*) "+hql.substring(hql.indexOf("from"));  
	}
	
	public static String parseAdditionalCondition(String targetObject,String[]additionalQueryConditions){
		String additionalCondition = null;
		if(StringUtil.isEmptyString(targetObject)){
			targetObject = "";
		}
		else{
			targetObject = targetObject+".";
		}
		if(!StringUtil.isEmptyStringCollection(additionalQueryConditions)){
			StringBuffer strBuf = new StringBuffer();
			int len = additionalQueryConditions.length;
			for(int i=0;i<len;i++){
				strBuf.append(" ").append(targetObject).append(additionalQueryConditions[i]).append(" ");
				if(i<len-1){
					strBuf.append(" and ");
				}
			}
			additionalCondition = strBuf.toString();
		}
		return additionalCondition;
	}
	
	public static String parseSorttingCondition(String targetObject,String sort,String dir){
		String   sorttingCondition = null;
		if(StringUtil.isEmptyString(targetObject)){
			targetObject = "";
		}
		else{
			targetObject = targetObject+".";
		}
		if(!StringUtil.isEmptyString(sort)&&!StringUtil.isEmptyString(dir)){
			StringBuffer strBuf = new StringBuffer();
			strBuf.append(" ").append(targetObject).append(sort).append(" ").append(dir);
			sorttingCondition = strBuf.toString();
		}
		return sorttingCondition;
	}
	
	public static void main(String[] args){
		
		String[]queryArgs=new String[]{"Lin","Chun","Quan"};
		
		String additionalCondition = "Lin ChunQuan";
		
		String sorttingCondition = "Spring Lin";
		
		String string = "fdsfs { likes[ 0 ] }afds{ok [1 ] }cd{ [ 2] haha} fsfs { addition  [ a ] } dfdf { addition  av [ s ] }";
		//               fdsfs  likesLin afdsok Chun cd Quan haha fsfs  addition  Lin ChunQuan  dfdf  addition  av Spring Lin 
		
		System.out.println("assignQueryArgsToHql:"+assignQueryArgsToHql(string,queryArgs));
		System.out.println("assignAdditionalConditionToHql:"+assignAdditionalConditionToHql(string,additionalCondition));
		System.out.println("assignSorttingConditionPatternToHql:"+assignSorttingConditionPatternToHql(string,sorttingCondition));
		
		System.out.println("assignAllArgusToHql:"+assignAllArgusToHql(string,queryArgs,additionalCondition,sorttingCondition));
	}
}
