package jalso.backend.daycationserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jalso.backend.daycationserver.service.DaycationService;

@RestController
@RequestMapping("/v1")
public class DaycationServer {
  @Autowired DaycationService dbService;
  
  @RequestMapping(value = "/signup/{user}/{pass}", method = RequestMethod.GET)
    public String addname(@PathVariable(value = "user") final String user, @PathVariable(value = "pass") final String pass) {
      dbService.insert(user, pass);
      return "You said: " + user + " " + pass;
    }

}
