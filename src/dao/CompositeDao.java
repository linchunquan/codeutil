package dao;

import dao.curd.*;
import dao.query.*;

public interface CompositeDao{

	LocationDao getLocationDao();

	MoneyDao getMoneyDao();

	PeopleClassDao getPeopleClassDao();

	LocationQuerier getLocationQuerier();

	MoneyQuerier getMoneyQuerier();

	PeopleClassQuerier getPeopleClassQuerier();

}