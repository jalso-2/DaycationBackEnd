package jalso.backend.daycationserver.service;

import java.util.List;
import java.util.Map;

public interface DaycationService {
  long signUp(String username, String password);

  List<Map<String,Object>> logIn(String username, String password);

  int insertDestination(String destination);

  String removeDestination(String id);

  String getUserLikes(String userId);
}
