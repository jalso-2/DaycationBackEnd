package jalso.backend.daycationserver.service.impl;

import jalso.backend.daycationserver.service.DaycationService;
import net.minidev.json.JSONArray;
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
    try {
      sql = "SELECT id,name,preferences,current_trip FROM users WHERE name= ? AND password= ?;";
      return getJdbcTemplate().queryForList(sql, user, pass);
    } catch (Exception e) {
      e.printStackTrace();
      List<Map<String,Object>> myList = new ArrayList<Map<String, Object>>();
      return myList;
    }
  }

  public List<Map<String, Object>> userPreferences(String userPref) {
    JSONParser parser = new JSONParser(1);
    JSONObject json = null;
    try {
      json = (JSONObject) parser.parse(userPref);
      JSONArray prefsArr = (JSONArray) json.get("prefs");
      int id = Integer.parseInt(json.get("userId").toString());

      sql = "UPDATE users SET preferences = CAST('" + prefsArr + "'as jsonb) WHERE id = ?;";
      getJdbcTemplate().update(sql, new Object[] { 
        id
      });
      
      sql = "SELECT id,name,preferences,current_trip FROM users WHERE id=?;";
      return getJdbcTemplate().queryForList(sql, id);
    } catch (Exception e) {
      e.printStackTrace();
      List<Map<String, Object>> myList = new ArrayList<Map<String, Object>>();
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
        json.get("userId"), destinationID
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

  public String removeDestination(String destId) {
    try {
      int id = Integer.parseInt(destId);
      
      sql = "DELETE FROM users_destinations WHERE destination_id=?; "
        + "DELETE FROM destinations WHERE id=?;";
      getJdbcTemplate().update(sql, new Object[] { 
        id, id
      });
      return "Destination was successfully deleted!";
    } catch (Exception e) {
      e.printStackTrace();
      return "Destination was not successfully deleted!";
    }
  }

  public ArrayList<List<Map<String, Object>>> getUserLikes(String userId) {
    try {
      ArrayList<Integer> likesInt = new ArrayList<Integer>();
      ArrayList<List<Map<String,Object>>> resList = new ArrayList<List<Map<String,Object>>>();
      int id = Integer.parseInt(userId);

      sql = "SELECT destination_id FROM users_destinations WHERE user_id = ?";

      List<Map<String,Object>> likesList = getJdbcTemplate().queryForList(sql, id);
      
      for (Object like : likesList) {
        int likeNum = Integer.parseInt(like.toString().replaceAll("[\\D]", ""));
        likesInt.add(likeNum);
      }

      for (int num : likesInt) {
        sql = "SELECT * FROM destinations WHERE id = ?";
        List<Map<String, Object>> temp = getJdbcTemplate().queryForList(sql, num);
        resList.add(temp);
      }

      return resList;
    } catch (Exception e) {
      e.printStackTrace();
      ArrayList<List<Map<String, Object>>> myList = new ArrayList<List<Map<String, Object>>>();
      return myList;    
    }
  }

  public String insertTrip(String trip) {
    JSONParser parser = new JSONParser(1);
    JSONObject json = null;
    try {
      json = (JSONObject) parser.parse(trip);
      JSONArray destinations = (JSONArray) json.get("destinations");
      ArrayList<Integer> destInt = new ArrayList<Integer>();
      
      for (Object dest : destinations) {
        int temp = this.insertDestination(dest.toString());
        destInt.add(temp);
      }

      sql = "INSERT into trips (NAME, DESCRIPTION) VALUES (?, CAST('" 
        + "{\"destinations\":" + destInt + "}' as jsonb));";
      
      getJdbcTemplate().update(sql, new Object[] { 
        json.get("name")
      });

      sql = "SELECT lastval();";
      int tripID = getJdbcTemplate().queryForObject(sql, Integer.class);

      sql = "INSERT INTO users_trips (USER_ID, TRIP_ID) VALUES (?, ?)";
      getJdbcTemplate().update(sql, new Object[] { 
        json.get("userId"), tripID 
      });

      return "Trip liked successfully";
    } catch (Exception e) {
      e.printStackTrace();
      return "Error liking a trip";
    }
  }

  public String removeTrip(String tripId) {
    try {
      int id = Integer.parseInt(tripId);

      sql = "Select description FROM trips WHERE id = ?";
      List<Map<String, Object>> destId = getJdbcTemplate().queryForList(sql, id);

      sql = "DELETE FROM users_trips WHERE trip_id=?; " 
        + "DELETE FROM trips WHERE id=?;";      
      getJdbcTemplate().update(sql, new Object[] { 
        id, id 
      });

      for (Map<String, Object> dest : destId) {
        String tempString = dest.get("description")
          .toString()
          .substring(18, dest.get("description")
          .toString().length() - 2);
        String[] tempArr = tempString.trim().split(",");
        for (String num : tempArr) {
          this.removeDestination(num.trim());
        }
      }
      return "Trip successfully deleted";
    } catch (Exception e) {
      e.printStackTrace();
      return "Error deleting trip";
    }
  }

  public ArrayList<Object> getUserTrips(String userId) {
    try {
      ArrayList<Object> holder = new ArrayList<Object>();
      int id = Integer.parseInt(userId);

      sql = "SELECT trip_id FROM users_trips WHERE user_id = ?";
      List<Map<String, Object>> tripList = getJdbcTemplate().queryForList(sql, id);

      for (Object tripId : tripList) {
        int tempTripId = Integer.parseInt(tripId.toString().replaceAll("[\\D]", ""));

        sql = "SELECT name,description FROM trips WHERE id = ?";
        List<Map<String, Object>> tempTrip = getJdbcTemplate().queryForList(sql, tempTripId);

        for (Map<String, Object> dest : tempTrip) {
          ArrayList<Object> tempHolder = new ArrayList<Object>();
          tempHolder.add(dest.get("name"));
          
          String tempString = dest.get("description")
            .toString()
            .substring(18, dest.get("description")
            .toString().length() - 2);
          
          String[] tempArr = tempString.trim().split(",");
          for (String num : tempArr) {
            int tempNum = Integer.parseInt(num.trim());
            
            sql = "SELECT * FROM destinations WHERE id = ?";
            List<Map<String, Object>> tempDest = getJdbcTemplate().queryForList(sql, tempNum);
            tempHolder.add(tempDest.get(0));
          }
          holder.add(tempHolder);
        }
      }
      return holder;
    } catch (Exception e) {
      e.printStackTrace();
      ArrayList<Object> tempHolder = new ArrayList<Object>();
      return tempHolder;
    }
  }

  public List<Map<String, Object>> currentTrip(String tripInfo) {
    JSONParser parser = new JSONParser(1);
    JSONObject json = null;
    try {
      json = (JSONObject) parser.parse(tripInfo);
      this.insertTrip(tripInfo);

      int id = Integer.parseInt(json.get("userId").toString());

      sql = "SELECT lastval();";
      int tripID = getJdbcTemplate().queryForObject(sql, Integer.class);
      
      sql = "UPDATE users SET current_trip = ? WHERE id = ?;";
      getJdbcTemplate().update(sql, new Object[] { 
        tripID, id
      });

      sql = "SELECT id,name,preferences,current_trip FROM users WHERE id= ?;";
      return getJdbcTemplate().queryForList(sql, id);
    } catch (Exception e) {
      e.printStackTrace();
      List<Map<String, Object>> myList = new ArrayList<Map<String, Object>>();
      return myList;
    }
  }
}