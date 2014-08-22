package dao.curd;

import java.util.Collection;
import dao.ParentDao;
import model.Location;

public interface LocationDao extends ParentDao{

	Location getLocation(long id);

	Location saveLocation(Location location);

	void updateLocation(Location location);

	Location saveOrUpdateLocation(Location location);

	void deleteLocation(Location location);

	void deleteAllLocations(Collection<Location> locations);
}