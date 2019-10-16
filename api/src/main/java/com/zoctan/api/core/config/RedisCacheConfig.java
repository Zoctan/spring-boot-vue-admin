package com.zoctan.api.core.config;

import com.zoctan.api.core.cache.MyRedisCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import javax.annotation.Resource;

/**
 * Redis缓存配置
 *
 * @author Zoctan
 * @date 2018/07/11
 */
@Slf4j
@Configuration
@EnableCaching(proxyTargetClass = true)
@ConditionalOnProperty(name = "spring.redis.host")
@EnableConfigurationProperties(RedisProperties.class)
public class RedisCacheConfig extends CachingConfigurerSupport {
  @Resource private RedisConnectionFactory redisConnectionFactory;

  @Bean
  @Override
  public CacheManager cacheManager() {
    // 初始化一个 RedisCacheWriter
    final RedisCacheWriter redisCacheWriter =
        RedisCacheWriter.nonLockingRedisCacheWriter(this.redisConnectionFactory);
    final RedisCacheConfiguration defaultCacheConfig =
        RedisCacheConfiguration.defaultCacheConfig()
            // 不缓存 null 值
            // .disableCachingNullValues()
            // 使用注解时的序列化、反序列化对
            .serializeKeysWith(MyRedisCacheManager.STRING_PAIR)
            .serializeValuesWith(MyRedisCacheManager.FASTJSON_PAIR);
    // 初始化RedisCacheManager
    return new MyRedisCacheManager(redisCacheWriter, defaultCacheConfig);
  }

  /**
   * 如果 @Cacheable、@CachePut、@CacheEvict 等注解没有配置 key，则使用这个自定义 key 生成器
   *
   * <p>自定义缓存的 key 时，难以保证 key 的唯一性
   *
   * <p>此时最好指定方法名，比如：@Cacheable(value="", key="{#root.methodName, #id}")
   */
  @Bean
  @Override
  public KeyGenerator keyGenerator() {
    // 比如 User 类 list(Integer page, Integer size) 方法
    // 用户 A 请求：list(1, 2)
    // redis 缓存的 key：User.list#1,2
    return (target, method, params) -> {
      final String dot = ".";
      final StringBuilder sb = new StringBuilder(32);
      // 类名
      sb.append(target.getClass().getSimpleName());
      sb.append(dot);
      // 方法名
      sb.append(method.getName());
      // 如果存在参数
      if (0 < params.length) {
        sb.append("#");
        // 带上参数
        String comma = "";
        for (final Object param : params) {
          sb.append(comma);
          if (param == null) {
            sb.append("NULL");
          } else {
            sb.append(param.toString());
          }
          comma = ",";
        }
      }
      return sb.toString();
    };
  }

  /** 错误处理，主要是打印日志 */
  @Bean
  @Override
  public CacheErrorHandler errorHandler() {
    return new SimpleCacheErrorHandler() {
      @Override
      public void handleCacheGetError(
          final RuntimeException e, final Cache cache, final Object key) {
        log.error("==> cache: {}", cache);
        log.error("==>   key: {}", key);
        log.error("==> error: {}", e.getMessage());
        super.handleCacheGetError(e, cache, key);
      }

      @Override
      public void handleCachePutError(
          final RuntimeException e, final Cache cache, final Object key, final Object value) {
        log.error("==> cache: {}", cache);
        log.error("==>   key: {}", key);
        log.error("==> value: {}", value);
        log.error("==> error: {}", e.getMessage());
        super.handleCachePutError(e, cache, key, value);
      }

      @Override
      public void handleCacheEvictError(
          final RuntimeException e, final Cache cache, final Object key) {
        log.error("==> cache: {}", cache);
        log.error("==>   key: {}", key);
        log.error("==> error: {}", e.getMessage());
        super.handleCacheEvictError(e, cache, key);
      }

      @Override
      public void handleCacheClearError(final RuntimeException e, final Cache cache) {
        log.error("==> cache: {}", cache);
        log.error("==> error: {}", e.getMessage());
        super.handleCacheClearError(e, cache);
      }
    };
  }
}
