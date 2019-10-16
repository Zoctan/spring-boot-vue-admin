package com.zoctan.api.core.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.zoctan.api.core.cache.MyRedisCacheManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * Redis配置
 *
 * @author Zoctan
 * @date 2018/05/27
 */
@Configuration
public class RedisConfig {
  @Resource private RedisProperties redisProperties;

  @Bean
  @ConfigurationProperties(prefix = "spring.redis.jedis.pool")
  public JedisPoolConfig jedisPoolConfig() {
    return new JedisPoolConfig();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.redis")
  public RedisConnectionFactory redisConnectionFactory(
      @Qualifier(value = "jedisPoolConfig") final JedisPoolConfig jedisPoolConfig) {
    // 方法上的 @ConfigurationProperties 不生效
    // 未知 bug，暂时这样手动设置
    // fixme
    // 单机版 jedis
    final RedisStandaloneConfiguration redisStandaloneConfiguration =
        new RedisStandaloneConfiguration(
            this.redisProperties.getHost(), this.redisProperties.getPort());
    redisStandaloneConfiguration.setDatabase(this.redisProperties.getDatabase());
    redisStandaloneConfiguration.setPassword(this.redisProperties.getPassword());

    // 获得默认的连接池构造器
    final JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb =
        (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)
            JedisClientConfiguration.builder();
    // 指定 jedisPoolConifig 来修改默认的连接池构造器
    jpcb.poolConfig(jedisPoolConfig);
    // 连接超时
    jpcb.and().connectTimeout(Duration.ofSeconds(10));
    // 通过构造器来构造 jedis 客户端配置
    final JedisClientConfiguration jedisClientConfiguration = jpcb.build();
    // 单机配置 + 客户端配置 = jedis 连接工厂
    return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
  }

  /**
   * 配置 RedisTemplate，配置 key 和 value 的序列化类
   *
   * <p>key 序列化使用 StringRedisSerializer, 不配置的话，key 会出现乱码
   */
  @Bean
  public RedisTemplate redisTemplate(
      @Qualifier(value = "redisConnectionFactory") final RedisConnectionFactory factory) {
    final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

    // 设置 key 的序列化器为字符串 serializer
    final StringRedisSerializer stringSerializer = MyRedisCacheManager.STRING_SERIALIZER;

    redisTemplate.setKeySerializer(stringSerializer);
    redisTemplate.setHashKeySerializer(stringSerializer);

    // 设置 value 的序列化器为 fastjson serializer
    final GenericFastJsonRedisSerializer fastSerializer = MyRedisCacheManager.FASTJSON_SERIALIZER;

    redisTemplate.setValueSerializer(fastSerializer);
    redisTemplate.setHashValueSerializer(fastSerializer);

    // 如果 KeySerializer 或者 ValueSerializer 没有配置
    // 则对应的 KeySerializer、ValueSerializer 才使用 fastjson serializer
    redisTemplate.setDefaultSerializer(fastSerializer);

    redisTemplate.setConnectionFactory(factory);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }
}
