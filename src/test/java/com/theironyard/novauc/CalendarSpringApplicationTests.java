package com.theironyard.novauc;

import com.theironyard.novauc.entities.Event;
import com.theironyard.novauc.services.EventRepository;
import com.theironyard.novauc.services.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CalendarSpringApplicationTests {

    @Autowired
	UserRepository users;

	@Autowired
	EventRepository events;

	@Autowired
	WebApplicationContext wap;

	MockMvc mockMvc;

	@Before
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wap).build();
	} {}

    @Test
	public void testLogin() throws Exception {

		 long initialCount = users.count();

          mockMvc.perform(
          		MockMvcRequestBuilders.post("/login").param("name","TestUser" + initialCount+1 )
		  );

		  Assert.assertTrue(users.count() == initialCount + 1);

	}

	@Test
	public void testAddEvent() throws Exception {
		testLogin();

		mockMvc.perform(
				MockMvcRequestBuilders.post("/create-event")
						.param("description", "Test event")
						.param("dateTime", LocalDateTime.now().toString())
						.sessionAttr("userName", "TestUser")
		);


		ArrayList<Event> ourEntryEvent = (ArrayList<Event>) events.findAll();

	    boolean hasOurEntry = false;

		for (Event entry : ourEntryEvent) {
              if(entry.getDescription().equals("Test event")) {
			   hasOurEntry = true;
			}

		}

		Assert.assertTrue(hasOurEntry);
	}


}

