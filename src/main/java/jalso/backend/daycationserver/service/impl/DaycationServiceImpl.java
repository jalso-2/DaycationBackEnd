package jalso.backend.daycationserver.service.impl;

import jalso.backend.daycationserver.service.DaycationService;

import javax.sql.DataSource;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class DaycationServiceImpl extends JdbcDaoSupport  implements DaycationService {
  @Autowired 
	DataSource dataSource;
	
	@PostConstruct
	private void initialize(){
		setDataSource(dataSource);
	}
	
	@Override
	public void insert(String user, String pass) {
    System.out.println("inside insert function");
		String sql = "INSERT INTO users " +
				"(NAME, PASSWORD) VALUES (?, ?)" ;
		getJdbcTemplate().update(sql, new Object[]{
				user, pass
		});
	}
}
