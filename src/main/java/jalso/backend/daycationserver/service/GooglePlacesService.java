package jalso.backend.daycationserver.service;

public interface GooglePlacesService {
  public String googleEvents(String events, String food, String transportation, int money, String location);
}