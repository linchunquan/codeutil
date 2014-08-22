package dao.query;

import java.util.List;
import util.datastruct.RecordCound;
import dao.ParentDao;
import model.Location;

public interface LocationQuerier extends ParentDao{

	public List<Location> findAllLocations(
		int         start, 
		int         limit,
		String[]    additionalConditions,
		String      sort,
		String      dir,
		RecordCound recordCound
	);
}
