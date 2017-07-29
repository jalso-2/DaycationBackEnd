package jalso.backend.daycationserver.service.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import jalso.backend.daycationserver.service.GooglePlacesService;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

@Repository
public class GooglePlacesServiceImpl implements GooglePlacesService {
  private int[] maxPrices = {0, 1, 2, 3, 4};
  private int[] distance = {2500, 25000, 50000};
  public ArrayList<String> names = new ArrayList<String>();
  public ArrayList<String> vicinities = new ArrayList<String>();
  public ArrayList<String> photos = new ArrayList<String>();
  public String[] eventsArr;
  public String[] foodsArr;
  public int maxPrice;
  public int radius;
  public String googleEvents(String events, String foods, String transportation, int money, String location) {
    char walk = 'w';
    char bike = 'b';
    char bus = 'u';
    char car = 'c';
    
    if (transportation.charAt(0) == walk) {  radius = distance[0]; }
    if (transportation.charAt(0) == bike) {  radius = distance[1]; }
    if (transportation.charAt(1) == bus || transportation.charAt(0) == car) { radius = distance[2]; }
    
    if (money <= 10) {  maxPrice = maxPrices[0];  }
    if (money > 10 && money <= 30) {  maxPrice = maxPrices[1];  }
    if (money > 30 && money <= 50) {  maxPrice = maxPrices[2];  }
    if (money > 50 && money <= 70) {  maxPrice = maxPrices[3];  }
    if (money > 70) { maxPrice = maxPrices[4];  }
  
    try {
      eventsArr = events.split(",");
      foodsArr = foods.split(",");
      System.out.println("transportation");
      System.out.println(transportation);
      System.out.println("radius");
      System.out.println(radius);
      System.out.println("distance[0]");
      System.out.println(distance[0]);
      for (int i = 0; i < eventsArr.length; i++) {
        
        System.out.println("fuck this shit");
        String query = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location + "&radius=" + radius + "&type=" + eventsArr[i] + "&key=";
        System.out.println(query);
        HttpResponse<JsonNode> jsonResponse = Unirest.get(query)
          .asJson();

        JSONArray results = jsonResponse.getBody().getObject().getJSONArray("results");
        System.out.println(results);
        for (int j = 0; j < results.length(); j++) {
          JSONObject jsonObject = results.getJSONObject(i);

          String name = jsonObject.getString("name");
          // String vicinity = jsonObject.getString("vicinity");
          // String photo = jsonObject.getString("photo_reference");
          names.add(name);
          // vicinities.add(vicinity);
          // photos.add(photo);

          System.out.println(name);
          // System.out.println(photo);
          // System.out.println(vicinity);
        }
      }

      // for (int i = 0; i < foodsArr.length; i++) {
      //   System.out.println("fuck this shit");
        
      //   HttpResponse<JsonNode> jsonResponse = Unirest.get("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location + "&" + radius + "&keyword=" + eventsArr[i] + "&key=")
      //     .asJson();

      //   JSONArray results = jsonResponse.getBody().getObject().getJSONArray("results");

      //   for (int j = 0; i < results.length(); j++) {
      //     JSONObject jsonObject = results.getJSONObject(i);
          
      //     String name = jsonObject.getString("name");
      //     String vicinity = jsonObject.getString("vicinity");
      //     String photo = jsonObject.getString("photo_reference");
      //     names.add(name);
      //     vicinities.add(vicinity);
      //     photos.add(photo);

      //     System.out.println(name);
      //     System.out.println(photo);
      //     System.out.println(vicinity);
      //   }
      // }

    } catch (UnirestException e) {
      e.printStackTrace();
    } catch (JSONException ex) {
      ex.printStackTrace();
    }

    return "Hello from the other siddeeeeee!!!!";
  }

}