package dao.query;

import java.util.List;
import util.datastruct.RecordCound;
import dao.ParentDao;
import model.Money;

public interface MoneyQuerier extends ParentDao{

	public List<Money> findAllMoneies(
		int         start, 
		int         limit,
		String[]    additionalConditions,
		String      sort,
		String      dir,
		RecordCound recordCound
	);
}
