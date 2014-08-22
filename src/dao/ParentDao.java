package dao;

import java.util.List;

import util.datastruct.RecordCound;





public interface ParentDao{

	public void setCompositeDao(CompositeDao compositeDao);

	/**
	 * 分页动态查询
	 * @param queryHql 主查询HQL语句
	 * @param countingHql 用于查询记录数的HQL语句，其算法逻辑应与主查询语句保持一致
	 * @param start 记录开始位置
	 * @param limit 最大记录数
	 * @param queryArgs 查询条件，通过正则表达式匹配赋值
	 * @param additionalCondition 附加查询条件，通过正则表达式匹配赋值
	 * @param sorttingCondition 排序条件
	 * @param recordCound 记录记录数目
	 * @return 返回分页动态查询结果
	 */
	List findByPagging(String queryHql, String countingHql, int start,
			int limit, String[] queryArgs, String additionalCondition,
			String sorttingCondition, RecordCound recordCound);
}
