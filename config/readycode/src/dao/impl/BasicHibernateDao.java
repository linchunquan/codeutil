package dao.impl;

import java.sql.SQLException;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import controller.JSONHelper;
import util.StringUtil;
import util.HqlUtil;
import util.datastruct.RecordCound;
import dao.CompositeDao;
import dao.ParentDao;


public class BasicHibernateDao extends HibernateDaoSupport implements ParentDao{

	protected CompositeDao compositeDao;

	public void setCompositeDao(CompositeDao compositeDao) {
		this.compositeDao = compositeDao;
	}

	private int getRecordCount(final String countingHql){  
		int count = 0;
		Long l = (Long)getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session)throws HibernateException, SQLException {  
				Query q = session.createQuery(countingHql);  
				return q.uniqueResult();  
			}    
		});  
		count = l.intValue();  
		return count;  
	}
	
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
	@Override
	public List findByPagging(
			String queryHql,
			String countingHql,
			final int start,
			final int limit,
			String[] queryArgs,
			String   additionalCondition,
			String   sorttingCondition,
			RecordCound recordCound){
		
		countingHql = HqlUtil.assignAllArgusToHql(
				StringUtil.isEmptyString(countingHql)
	     		? HqlUtil.getCountHqlFromQueryHql(queryHql)
	    		: countingHql, queryArgs, additionalCondition, sorttingCondition
	    );
		
		queryHql    = HqlUtil.assignAllArgusToHql(queryHql, queryArgs, additionalCondition, sorttingCondition);
		
		
		recordCound.value=getRecordCount(countingHql);
		
		final String hql =  queryHql;
		
		List lst = this .getHibernateTemplate().executeFind( new  HibernateCallback(){
			public  Object doInHibernate(Session session)  throws  SQLException,
		           HibernateException {
				Query q = session.createQuery(hql);  
		   	 	q.setFirstResult(start);  
		   	 	q.setMaxResults(limit);  
		   	 	return q.list();  
		    }
		});
		
		return lst;
	}
	
	public List find(String hql){
		return this.getHibernateTemplate().find(hql);
	}
}
