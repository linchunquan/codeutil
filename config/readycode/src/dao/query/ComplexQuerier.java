package dao.query;

import java.io.File;
import java.util.Collection;
import java.util.List;

import model.User;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import dao.CompositeDao;
import dao.ParentDao;

public interface ComplexQuerier extends ParentDao{
	
	void save(Object entity);
	
	void update(Object entity);
	
	void saveAll(List entities);
	
	void deleteAll(Collection entities);
	
	void deleteAll(Class clazz);
	
	void deleteAllThenSave(Class clazz, List entitiesToSave);

	void executeSql(String sql);
	
	void executeSql(File sqlFile);
	
	SqlRowSet query(String sql);
	
	List hqlQuery(String hql);
	
	User getUserByName(String userName);
}
