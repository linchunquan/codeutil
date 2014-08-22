package dao.query;

import java.util.List;
import util.datastruct.RecordCound;
import dao.ParentDao;
import model.PeopleClass;

public interface PeopleClassQuerier extends ParentDao{

	public List<PeopleClass> findAllPeopleClasses(
		int         start, 
		int         limit,
		String[]    additionalConditions,
		String      sort,
		String      dir,
		RecordCound recordCound
	);
}
