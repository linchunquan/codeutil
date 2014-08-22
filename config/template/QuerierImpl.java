package dao.impl.query;

import java.util.List;
import util.HqlUtil;
import util.datastruct.RecordCound;
import dao.impl.BasicHibernateDao;
import dao.query.[Class]Querier;
import model.[Class];

public class [Class]QuerierImpl extends BasicHibernateDao implements [Class]Querier{

	@Override
	public List<[Class]> findAll[Classes](
		int         start, 
		int         limit,
		String[]    additionalQueryConditions,
		String      sort,
		String      dir,
		RecordCound recordCound
	){
		// TODO Auto-generated method stub
		String   targetObject        =  "[class]";
		String   queryHql            =  "from [Class] [class] {where [a]} {order by [s]}";
		String   countingHql         =  null;
		String[] queryArgs           =  null;
		String   additionalCondition =  HqlUtil.parseAdditionalCondition(targetObject, additionalQueryConditions);
		String   sorttingCondition   =  HqlUtil.parseSorttingCondition(targetObject, sort, dir);
		
		return this.findByPagging(queryHql, countingHql, start, limit, queryArgs, additionalCondition, sorttingCondition, recordCound);
	}
	
	@Override
	public List<[Class]> find[Classes]AssocTo(
			String assoIdName,
			long id){
		return find("from [Class] where " + assoIdName + " = " + id);
	}
}
