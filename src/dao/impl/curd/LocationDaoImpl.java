package dao.impl.curd;

import java.util.Collection;
import dao.impl.BasicHibernateDao;
import dao.curd.LocationDao;
import model.Location;

public class LocationDaoImpl  extends BasicHibernateDao implements LocationDao{

	public Location getLocation(long id) {
		return (Location)getHibernateTemplate().get(Location.class, id);
	}

	public Location saveLocation(Location location) {
		getHibernateTemplate().save(location);
		return location;
	}

	public void updateLocation(Location location) {
		getHibernateTemplate().update(location);
	}

	public Location saveOrUpdateLocation(Location location) {
		getHibernateTemplate().saveOrUpdate(location);
		return location;
	}

	public void deleteLocation(Location location) {
		getHibernateTemplate().delete(location);
	}

	public void deleteAllLocations(Collection<Location> locations) {
		getHibernateTemplate().deleteAll(locations);
	}
}