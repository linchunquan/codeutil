package dao.query;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import dao.ParentDao;

public interface ComplexQuerier extends ParentDao{

	SqlRowSet query(String sql);
}
