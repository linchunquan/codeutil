package dao.impl.curd;

import java.util.Collection;
import dao.impl.BasicHibernateDao;
import dao.curd.PeopleClassDao;
import model.PeopleClass;

public class PeopleClassDaoImpl  extends BasicHibernateDao implements PeopleClassDao{

	public PeopleClass getPeopleClass(long id) {
		return (PeopleClass)getHibernateTemplate().get(PeopleClass.class, id);
	}

	public PeopleClass savePeopleClass(PeopleClass peopleClass) {
		getHibernateTemplate().save(peopleClass);
		return peopleClass;
	}

	public void updatePeopleClass(PeopleClass peopleClass) {
		getHibernateTemplate().update(peopleClass);
	}

	public PeopleClass saveOrUpdatePeopleClass(PeopleClass peopleClass) {
		getHibernateTemplate().saveOrUpdate(peopleClass);
		return peopleClass;
	}

	public void deletePeopleClass(PeopleClass peopleClass) {
		getHibernateTemplate().delete(peopleClass);
	}

	public void deleteAllPeopleClasses(Collection<PeopleClass> peopleClasses) {
		getHibernateTemplate().deleteAll(peopleClasses);
	}
}