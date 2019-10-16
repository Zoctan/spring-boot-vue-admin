package com.zoctan.api.core.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Spring MVC 配置
 *
 * @author Zoctan
 * @date 2018/05/27
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
  /** 使用阿里 FastJson 作为 JSON MessageConverter */
  @Override
  public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
    final FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
    final FastJsonConfig config = new FastJsonConfig();
    converter.setSupportedMediaTypes(
        new ArrayList<MediaType>() {
          private static final long serialVersionUID = 1924772982095119650L;

          {
            this.add(MediaType.APPLICATION_JSON_UTF8);
            this.add(MediaType.APPLICATION_OCTET_STREAM);
            this.add(MediaType.IMAGE_GIF);
            this.add(MediaType.IMAGE_JPEG);
            this.add(MediaType.IMAGE_PNG);
          }
        });
    config.setSerializerFeatures(
        // 保留空的字段
        // SerializerFeature.WriteMapNullValue,
        // Number null -> 0
        SerializerFeature.WriteNullNumberAsZero,
        // 美化输出
        SerializerFeature.PrettyFormat);
    converter.setFastJsonConfig(config);
    converter.setDefaultCharset(StandardCharsets.UTF_8);
    converters.add(converter);
  }

  /** 视图控制器 */
  @Override
  public void addViewControllers(final ViewControllerRegistry registry) {
    // solved swagger2
    registry.addRedirectViewController("/v2/api-docs", "/v2/api-docs?group=restful-api");
    registry.addRedirectViewController(
        "/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
    registry.addRedirectViewController(
        "/swagger-resources/configuration/security", "/swagger-resources/configuration/security");
    registry.addRedirectViewController("/swagger-resources", "/swagger-resources");
  }

  /** 资源控制器 */
  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    // solved swagger2
    registry
        .addResourceHandler("/swagger-ui.html**")
        .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
    registry
        .addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }
}
