package jalso.backend.daycationserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jalso.backend.daycationserver.service.DaycationService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class DaycationServer {
  @Autowired DaycationService dbService;

  @RequestMapping(value = "/seed", method = RequestMethod.GET)
  public String seedData() {
    String[] users = {"rose", "trob", "david", "daniel", "ray", "james"};
    String[] passwords = {"rose123", "trob123", "david123", "daniel123", "ray123", "james123"};

    for (int i = 0; i < 6; i++) {
      dbService.signUp(users[i], passwords[i]);
    }

    return "Database is seeded!";
  }

  @RequestMapping(value = "/signup", method = RequestMethod.POST)
  public long signUp(@RequestParam("user") final String user, @RequestParam("pass") final String pass) {
    return dbService.signUp(user, pass);
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public List<Map<String,Object>> logIn(@RequestParam("user") final String user, @RequestParam("pass") final String pass) {
    return dbService.logIn(user, pass);
  }
}
