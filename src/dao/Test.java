package dao;

import model.Location;
import model.PeopleClass;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import dao.curd.LocationDao;
import dao.curd.PeopleClassDao;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext ctx = new FileSystemXmlApplicationContext(new String[]{
				"config//dbConnection.xml",
				"config//dbHibernate.xml"
		});
		
		//LocationDao locationDao = (LocationDao)ctx.getBean("locationDao");
		//PeopleClassDao peopleClassDao = (PeopleClassDao)ctx.getBean("peopleClassDao");
		
		//Location location = new Location();
		//PeopleClass peopleClass = new PeopleClass();
		//peopleClass.getLocations().add(location);
		//location.setPeopleClass(peopleClass);

		//peopleClassDao.saveOrUpdatePeopleClass(peopleClass);

		
		//peopleClass = peopleClassDao.getPeopleClass(10);
		//peopleClass.getLocations().iterator().next().setLocationName("locationName");
		//peopleClassDao.saveOrUpdatePeopleClass(peopleClass);
		
		//peopleClass = peopleClassDao.getPeopleClass(11);
		//peopleClassDao.deletePeopleClass(peopleClass);
		JdbcDaoSupport jds = null;
		RowMapper rmp = null;
		
	}
}
