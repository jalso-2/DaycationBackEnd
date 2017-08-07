package jalso.backend.daycationserver.service.impl;

import jalso.backend.daycationserver.service.DaycationService;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class DaycationServiceImpl extends JdbcDaoSupport  implements DaycationService {
  @Autowired 
	DataSource dataSource;
	String sql;
	@PostConstruct
	private void initialize(){
		setDataSource(dataSource);
	}
	
	public long signUp(String user, String pass) {
    System.out.println("inside signUp function");
    try {
      sql = "INSERT INTO users (NAME, PASSWORD) VALUES (?, ?)" ;
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

	public List<Map<String,Object>> logIn(String user, String pass) {
    System.out.println("inside logIn function");
     
    try {
      sql = "SELECT id,name,preferences FROM users WHERE name= ? AND password= ?;";
      return getJdbcTemplate().queryForList(sql, user, pass);
    } catch (Exception e) {
      e.printStackTrace();
      List<Map<String,Object>> myList = new ArrayList();
      return myList;
    }
  }

  public int insertDestination(String dest){ 
    JSONParser parser = new JSONParser(1);
    JSONObject json = null;

    try {
      json = (JSONObject) parser.parse(dest);
      sql = "INSERT INTO destinations (NAME, DESCRIPTION) VALUES (?, CAST('" 
        + json.get("description").toString().replaceAll("\"", "\\\"") + "'as jsonb))";
      getJdbcTemplate().update(sql, new Object[]{
        json.get("name")
      });

      sql = "SELECT nextval('destinations_id_seq');";
      int destinationID = getJdbcTemplate().queryForObject(sql, Integer.class) - 1;

      sql = "INSERT INTO users_destinations (USER_ID, DESTINATION_ID) VALUES (?, ?)";
      getJdbcTemplate().update(sql, new Object[] { 
        json.get("userid"), destinationID
      });

      return destinationID;
    } catch (ParseException e) {
      e.printStackTrace();
      return -1;
    } catch (Exception ex) {
      ex.printStackTrace();
      return -1;
    }
  }

  public void removeDestination(String destId) {
    try {
      int id = Integer.parseInt(destId);
      sql = "DELETE FROM users_destinations WHERE destination_id=?; "
        + "DELETE FROM destinations WHERE id=?;";
      getJdbcTemplate().update(sql, new Object[] { 
        id, id
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}