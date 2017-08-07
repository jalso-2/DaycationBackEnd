package jalso.backend.daycationserver.service;

import java.util.ArrayList;

public interface GooglePlacesService {
  public ArrayList<ArrayList<String>> googleEvents(String events, String food, String transportation, int money, String location);
}