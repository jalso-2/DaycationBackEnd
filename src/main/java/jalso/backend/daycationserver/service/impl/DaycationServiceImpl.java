package jalso.backend.daycationserver.service.impl;

import jalso.backend.daycationserver.service.DaycationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class DaycationServiceImpl extends JdbcDaoSupport  implements DaycationService {
  @Autowired 
	DataSource dataSource;
	
	@PostConstruct
	private void initialize(){
		setDataSource(dataSource);
	}
	
	@Override
	public long signUp(String user, String pass) {
    System.out.println("inside signUp function");
    try {
      String sql = "INSERT INTO users " +
        "(NAME, PASSWORD) VALUES (?, ?)" ;
      getJdbcTemplate().update(sql, new Object[]{
        user, pass
      });
      sql = "SELECT nextval('users_id_seq');";
      return getJdbcTemplate().queryForObject(sql, Integer.class) - 1;
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  @Override
	public List<Map<String,Object>> logIn(String user, String pass) {
    System.out.println("inside logIn function");
    try {
      String sql = "SELECT * FROM users WHERE name= ? AND password= ?;";
      return getJdbcTemplate().queryForList(sql, user, pass);
    } catch (Exception e) {
      List<Map<String,Object>> myList = new ArrayList();
      return myList;
    }

  }
}
