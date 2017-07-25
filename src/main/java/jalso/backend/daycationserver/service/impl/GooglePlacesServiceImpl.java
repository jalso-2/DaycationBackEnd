package jalso.backend.daycationserver.service.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import jalso.backend.daycationserver.service.GooglePlacesService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

@Repository
public class GooglePlacesServiceImpl implements GooglePlacesService {
  
  public String googleEvents(String events, String foods, String transportations) {
    if (events.contains(",")) {
      String[] eventsArr = events.split(",");
      for (String event : eventsArr) {
        System.out.println("event in eventsArr in if");
        System.out.println(event);
        System.out.println("event in eventsArr in if");
      }
    } else {
      String[] eventsArr = { events };
      for (String event : eventsArr) {
        System.out.println("event in eventsArr in if");
        System.out.println(event);
        System.out.println("event in eventsArr in if");
      }
    }
    if (foods.contains(",")) {
      String[] foodArr = foods.split(",");
      for (String food : foodArr) {
        System.out.println("food in foodArr in if");
        System.out.println(food);
        System.out.println("food in foodArr in if");
      }
    } else {
      String[] foodArr = { foods };
      for (String food : foodArr) {
        System.out.println("food in foodArr in if");
        System.out.println(food);
        System.out.println("food in foodArr in if");
      }
    }
    if (transportations.contains(",")) {
      String[] transportationArr = transportations.split(",");
      for (String transportation : transportationArr) {
        System.out.println("transportation in transportationArr in if");
        System.out.println(transportation);
        System.out.println("transportation in transportationArr in if");
      }
    } else {
      String[] transportationArr = { transportations };
      for (String transportation : transportationArr) {
        System.out.println("transportation in transportationArr in if");
        System.out.println(transportation);
        System.out.println("transportation in transportationArr in if");
      }
    }
 
    try {

      // HttpResponse<JsonNode> jsonResponse = Unirest.post("http://localhost:3000/v1/login")
      //   .header("accept", "application/json")
      //   .field("user", "jayfrank")
      //   .field("pass", "jayfrank123")
      //   .asJson();
      HttpResponse<JsonNode> jsonResponse = Unirest.get("https://maps.googleapis.com/maps/api/place/textsearch/json?query=new+york+city+point+of+interest&language=en&key=")
        .asJson();

      JSONArray results = jsonResponse.getBody().getObject().getJSONArray("results");
      // System.out.println(results);
      String[] addresses = new String[results.length()];

      for (int i = 0; i < results.length();i++) {
        JSONObject jsonObject = results.getJSONObject(i);

        addresses [i] = jsonObject.getString("name");
        System.out.println(addresses[i]);
      }
    } catch (UnirestException e) {
      e.printStackTrace();
    } catch (JSONException ex) {
      ex.printStackTrace();
    }
    return "Hello from the other siddeeeeee!!!!";
  }

}