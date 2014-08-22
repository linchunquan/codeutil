package dao.curd;

import java.util.Collection;
import dao.ParentDao;
import model.PeopleClass;

public interface PeopleClassDao extends ParentDao{

	PeopleClass getPeopleClass(long id);

	PeopleClass savePeopleClass(PeopleClass peopleClass);

	void updatePeopleClass(PeopleClass peopleClass);

	PeopleClass saveOrUpdatePeopleClass(PeopleClass peopleClass);

	void deletePeopleClass(PeopleClass peopleClass);

	void deleteAllPeopleClasses(Collection<PeopleClass> peopleClasses);
}