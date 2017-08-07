package jalso.backend.daycationserver.service.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import jalso.backend.daycationserver.service.GooglePlacesService;
import jalso.backend.Config2;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

@Repository
public class GooglePlacesServiceImpl implements GooglePlacesService {
  private Config2 config = new Config2();

  private ArrayList<String> names = new ArrayList<String>();
  private ArrayList<String> vicinities = new ArrayList<String>();
  private ArrayList<String> photos = new ArrayList<String>();
  private ArrayList<ArrayList<String>> resLists = new ArrayList<ArrayList<String>>();
  private int[] maxPrices = {0, 1, 2, 3, 4};
  private int[] distance = {2500, 25000, 50000};
  private String[] eventsArr;
  private String[] foodsArr;
  private int maxPrice;
  private int radius;
  private String loc;

  public ArrayList<ArrayList<String>> googleEvents(String events, String foods, String transportation, int money, String location) {
    char walk = 'w';
    char bike = 'b';
    char bus = 'u';
    char car = 'c';
    
    loc = location;

    if (transportation.charAt(0) == walk) {  radius = distance[0]; }
    if (transportation.charAt(0) == bike) {  radius = distance[1]; }
    if (transportation.charAt(1) == bus || transportation.charAt(0) == car) { radius = distance[2]; }
    
    if (money <= 10) {  maxPrice = maxPrices[0];  }
    if (money > 10 && money <= 30) {  maxPrice = maxPrices[1];  }
    if (money > 30 && money <= 50) {  maxPrice = maxPrices[2];  }
    if (money > 50 && money <= 70) {  maxPrice = maxPrices[3];  }
    if (money > 70) { maxPrice = maxPrices[4];  }
  
    queryGoogle(events.split(","), "events");
    queryGoogle(foods.split(","), "foods");
    resLists.add(names);
    resLists.add(vicinities);
    resLists.add(photos);
    return resLists;
  }

  public void queryGoogle(String[] array, String arrayType) {
    String query;
    try {
      for (int i = 0; i < array.length; i++) {
      if (arrayType == "events") {
        query = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + loc + "&radius=" + radius + "&type=" + array[i] + "&key=" + config.getKey();
      } else {
        query = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + loc + "&radius=" + radius + "&keyword=" + array[i] + "&key=" + config.getKey();
      }

      HttpResponse<JsonNode> jsonResponse = Unirest.get(query)
        .asJson();

      JSONArray results = jsonResponse.getBody().getObject().getJSONArray("results");

      for (int j = 0; j < results.length(); j++) {
        String name;
        String vicinity;
        String photo;
        JSONObject jsonObject = results.getJSONObject(j);
        
        if (jsonObject.has("photos")) {
          JSONObject jsonArray = jsonObject.getJSONArray("photos").getJSONObject(0);
          photo = jsonArray.getString("photo_reference");
        } else {
          photo = "photo_reference not found";
        }

        name = jsonObject.getString("name");
        vicinity = jsonObject.getString("vicinity");
        names.add(name);
        vicinities.add(vicinity);
        photos.add(photo);
      }
    }
    } catch (UnirestException e) {
      e.printStackTrace();
    } catch (JSONException ex) {
      ex.printStackTrace();
    }
  }
}