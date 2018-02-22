package com.zoctan.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Rollback
public abstract class BaseControllerTest {
    @Autowired
    WebApplicationContext context;
    @Value("${server.port}")
    int port;
    final String url = "http://localhost:" + this.port;
    MockMvc mockMvc;

    @Before
    public void setUp() throws JsonProcessingException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }
}
