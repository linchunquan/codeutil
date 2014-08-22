package dao.curd;

import java.util.Collection;
import dao.ParentDao;
import model.Money;

public interface MoneyDao extends ParentDao{

	Money getMoney(long id);

	Money saveMoney(Money money);

	void updateMoney(Money money);

	Money saveOrUpdateMoney(Money money);

	void deleteMoney(Money money);

	void deleteAllMoneies(Collection<Money> moneies);
}