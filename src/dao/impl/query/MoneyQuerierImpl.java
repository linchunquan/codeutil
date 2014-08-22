package dao.impl.query;

import java.util.List;
import util.HqlUtil;
import util.datastruct.RecordCound;
import dao.impl.BasicHibernateDao;
import dao.query.MoneyQuerier;
import model.Money;

public class MoneyQuerierImpl extends BasicHibernateDao implements MoneyQuerier{

	@Override
	public List<Money> findAllMoneies(
		int         start, 
		int         limit,
		String[]    additionalQueryConditions,
		String      sort,
		String      dir,
		RecordCound recordCound
	){
		// TODO Auto-generated method stub
		String   targetObject        =  "money";
		String   queryHql            =  "from Money money {where [a]} {order by [s]}";
		String   countingHql         =  null;
		String[] queryArgs           =  null;
		String   additionalCondition =  HqlUtil.parseAdditionalCondition(targetObject, additionalQueryConditions);
		String   sorttingCondition   =  HqlUtil.parseSorttingCondition(targetObject, sort, dir);
		
		return this.findByPagging(queryHql, countingHql, start, limit, queryArgs, additionalCondition, sorttingCondition, recordCound);
	}
}
