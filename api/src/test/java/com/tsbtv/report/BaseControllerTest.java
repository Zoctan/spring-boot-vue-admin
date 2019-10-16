package com.zoctan.api;

import com.alibaba.fastjson.JSON;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.filter.AuthenticationFilter;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * MockMvc 测试控制器
 *
 * @author Zoctan
 * @date 2018/11/29
 */
// 测试时如果为 true，就不会修改数据库数据
@Rollback(value = false)
@Transactional
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
public abstract class BaseControllerTest {
  @Autowired protected WebApplicationContext context;
  @Autowired protected AuthenticationFilter authenticationFilter;

  protected MockMvc mockMvc;

  @Before
  public void setUp() {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(this.context)
            // 不添加则无法维持 SecurityContext
            .apply(springSecurity())
            .build();
  }

  private Result execute(
      final HttpMethod method, final String targetUrl, final Object args, final String token)
      throws Exception {
    final MockHttpServletRequestBuilder builders =
        MockMvcRequestBuilders.request(method, targetUrl)
            .contentType(MediaType.APPLICATION_JSON_UTF8);
    if (args != null) {
      builders.content(JSON.toJSONString(args));
    }
    if (StringUtils.isNotBlank(token)) {
      builders.header("Authorization", token);
    }
    return JSON.parseObject(
        this.mockMvc
            .perform(builders)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andReturn()
            .getResponse()
            .getContentAsString(),
        Result.class);
  }

  protected Result get(final String targetUrl, final Object args, final String token)
      throws Exception {
    return this.execute(HttpMethod.GET, targetUrl, args, token);
  }

  protected Result post(final String targetUrl, final Object args, final String token)
      throws Exception {
    return this.execute(HttpMethod.POST, targetUrl, args, token);
  }

  protected Result delete(final String targetUrl, final Object args, final String token)
      throws Exception {
    return this.execute(HttpMethod.DELETE, targetUrl, args, token);
  }

  protected Result patch(final String targetUrl, final Object args, final String token)
      throws Exception {
    return this.execute(HttpMethod.PATCH, targetUrl, args, token);
  }
}
