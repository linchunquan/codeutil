package dao.impl.query;


import javax.sql.DataSource;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import dao.impl.BasicHibernateDao;
import dao.query.ComplexQuerier;

public class ComplexQuerierImpl extends BasicHibernateDao implements ComplexQuerier{

	private JdbcDaoSupport daoSupport = new JdbcDaoSupport(){};
	
	public SqlRowSet query(String sql){
		return daoSupport.getJdbcTemplate().queryForRowSet(sql);
	}
	
	public void setDataSource(DataSource dataSource){
		daoSupport.setDataSource(dataSource);
	}
}
