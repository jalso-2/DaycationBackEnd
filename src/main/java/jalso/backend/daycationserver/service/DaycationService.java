package jalso.backend.daycationserver.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface DaycationService {
  long signUp(String username, String password);

  List<Map<String,Object>> logIn(String username, String password);

  List<Map<String, Object>> userPreferences(String userPref);

  int insertDestination(String destination);

  String removeDestination(String id);

  ArrayList<List<Map<String, Object>>> getUserLikes(String userId);

  String insertTrip(String trip);

  String removeTrip(String tripId);

  ArrayList<Object> getUserTrips(String userId);

  List<Map<String, Object>> currentTrip(String trip);
}
