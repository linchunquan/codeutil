package dao.impl.curd;

import java.util.Collection;
import dao.impl.BasicHibernateDao;
import dao.curd.MoneyDao;
import model.Money;

public class MoneyDaoImpl  extends BasicHibernateDao implements MoneyDao{

	public Money getMoney(long id) {
		return (Money)getHibernateTemplate().get(Money.class, id);
	}

	public Money saveMoney(Money money) {
		getHibernateTemplate().save(money);
		return money;
	}

	public void updateMoney(Money money) {
		getHibernateTemplate().update(money);
	}

	public Money saveOrUpdateMoney(Money money) {
		getHibernateTemplate().saveOrUpdate(money);
		return money;
	}

	public void deleteMoney(Money money) {
		getHibernateTemplate().delete(money);
	}

	public void deleteAllMoneies(Collection<Money> moneies) {
		getHibernateTemplate().deleteAll(moneies);
	}
}