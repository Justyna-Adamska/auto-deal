package com.example.autodeal;

import jakarta.persistence.EntityManager;
import org.h2.tools.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AutoDealApplicationTests {

	@Test
	void contextLoads() {
	}

//	@BeforeEach
//	void resetDatabase(@Autowired H2Util h2Util) { h2Util.resetDatabase(); }

//	@AfterEach
//	public void tearDown(@Autowired EntityManager entityManager, @Autowired Connection connection, @Autowired Server server) throws SQLException { entityManager.clear();
//		//clear entities from the session
//		connection.close();
//		server.stop();
//}


}
