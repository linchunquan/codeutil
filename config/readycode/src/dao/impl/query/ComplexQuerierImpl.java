package dao.impl.query;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import model.User;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import dao.impl.BasicHibernateDao;
import dao.query.ComplexQuerier;

public class ComplexQuerierImpl extends BasicHibernateDao implements ComplexQuerier{

	private JdbcDaoSupport daoSupport = new JdbcDaoSupport(){};

	@Override
	public void save(Object entity) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().save(entity);
	}
	
	@Override
	public void update(Object entity) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().update(entity);
	}
	
	@Override
	public void saveAll(List entities) {
		// TODO Auto-generated method stub
		for(Object entity : entities){
			save(entity);
		}
	}
	
	@Override
	public void deleteAll(Collection entities) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().deleteAll(entities);
	}

	@Override
	public void deleteAll(Class clazz) {
		// TODO Auto-generated method stub
		deleteAll(this.getHibernateTemplate().find("from "+clazz.getName()));
	}
	
	@Override
	public void deleteAllThenSave(Class clazz, List entitiesToSave) {
		// TODO Auto-generated method stub
		deleteAll(clazz);
		saveAll(entitiesToSave);
	}
	
	public void executeSql(String sql){
		daoSupport.getJdbcTemplate().execute(sql);
	}
	
	public void executeSql(File sqlFile){
		//daoSupport.getJdbcTemplate().execute(sql);
		//SQLExec2 sqlExec = new SQLExec2(); 
		Statement smt = null;
		try {
			
			List<String> sqlList = loadSql(sqlFile);

			//daoSupport.getJdbcTemplate().batchUpdate(sqlList.toArray(new String[sqlList.size()]));
			
			
			smt = daoSupport.getDataSource().getConnection().createStatement();
			for (String sql : sqlList) {
				//System.out.println("execute sql:"+sql);
                smt.addBatch(sql);
            }
            smt.executeBatch();
			
            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				smt.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private List<String> loadSql(File sqlFile) throws Exception {
		List<String> sqlList = new ArrayList<String>();
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(sqlFile));
			String line = null;
			String sql="";
			while((line=br.readLine())!=null){
				line = line.trim();
				if(!line.isEmpty()&&!line.startsWith("--")){
					sql += line+" ";
					if(sql.endsWith("; ")){
						sqlList.add(sql);
						sql="";
					}
				}
			}
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
		return sqlList;
	}
	
	public SqlRowSet query(String sql){
		return daoSupport.getJdbcTemplate().queryForRowSet(sql);
	}
	
	public void setDataSource(DataSource dataSource){
		daoSupport.setDataSource(dataSource);
	}
	
	public List hqlQuery(String hql){
		return this.getHibernateTemplate().find(hql);
	}

	@Override
	public User getUserByName(String userName) {
		// TODO Auto-generated method stub
		List<User> users = this.getHibernateTemplate().find("from User user where user.userName like '"+userName+"'");
		if(users==null||users.size()==0){
			return null;
		}
		return users.get(0);
	}
}
