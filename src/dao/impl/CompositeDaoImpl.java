package dao.impl;

import dao.impl.curd.*;
import dao.curd.*;
import dao.impl.query.*;
import dao.query.*;
import dao.CompositeDao;;

public class CompositeDaoImpl implements CompositeDao{

	private LocationDao locationDao;

	private MoneyDao moneyDao;

	private PeopleClassDao peopleClassDao;

	private LocationQuerier locationQuerier;

	private MoneyQuerier moneyQuerier;

	private PeopleClassQuerier peopleClassQuerier;

	public CompositeDaoImpl(
			LocationDao locationDao,
			MoneyDao moneyDao,
			PeopleClassDao peopleClassDao,
			LocationQuerier locationQuerier,
			MoneyQuerier moneyQuerier,
			PeopleClassQuerier peopleClassQuerier) {

 		this.locationDao = locationDao;
 		this.locationDao.setCompositeDao(this);

 		this.moneyDao = moneyDao;
 		this.moneyDao.setCompositeDao(this);

 		this.peopleClassDao = peopleClassDao;
 		this.peopleClassDao.setCompositeDao(this);

 		this.locationQuerier = locationQuerier;
 		this.locationQuerier.setCompositeDao(this);

 		this.moneyQuerier = moneyQuerier;
 		this.moneyQuerier.setCompositeDao(this);

 		this.peopleClassQuerier = peopleClassQuerier;
 		this.peopleClassQuerier.setCompositeDao(this);

	}

	public LocationDao getLocationDao() {
		return locationDao;
	}

	public void setLocationDao(LocationDao locationDao) {
		this.locationDao = locationDao;
	}

	public MoneyDao getMoneyDao() {
		return moneyDao;
	}

	public void setMoneyDao(MoneyDao moneyDao) {
		this.moneyDao = moneyDao;
	}

	public PeopleClassDao getPeopleClassDao() {
		return peopleClassDao;
	}

	public void setPeopleClassDao(PeopleClassDao peopleClassDao) {
		this.peopleClassDao = peopleClassDao;
	}

	public LocationQuerier getLocationQuerier() {
		return locationQuerier;
	}

	public void setLocationQuerier(LocationQuerier locationQuerier) {
		this.locationQuerier = locationQuerier;
	}

	public MoneyQuerier getMoneyQuerier() {
		return moneyQuerier;
	}

	public void setMoneyQuerier(MoneyQuerier moneyQuerier) {
		this.moneyQuerier = moneyQuerier;
	}

	public PeopleClassQuerier getPeopleClassQuerier() {
		return peopleClassQuerier;
	}

	public void setPeopleClassQuerier(PeopleClassQuerier peopleClassQuerier) {
		this.peopleClassQuerier = peopleClassQuerier;
	}
}