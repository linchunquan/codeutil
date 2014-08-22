package dao.query;

import java.util.List;
import util.datastruct.RecordCound;
import dao.ParentDao;
import model.[Class];

public interface [Class]Querier extends ParentDao{

	public List<[Class]> findAll[Classes](
		int         start, 
		int         limit,
		String[]    additionalConditions,
		String      sort,
		String      dir,
		RecordCound recordCound
	);
	
	public List<[Class]> find[Classes]AssocTo(
		String assoIdName,
		long id
	);
}
