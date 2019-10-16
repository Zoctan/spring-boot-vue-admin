package com.zoctan.api.core.cache;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Redis 容易出现缓存问题（超时、Redis 宕机等），当使用 spring cache 的注释 Cacheable、Cacheput 等处理缓存问题时， 我们无法使用 try catch
 * 处理出现的异常，所以最后导致结果是整个服务报错无法正常工作。 通过自定义 MyRedisCacheManager 并继承 RedisCacheManager 来处理异常可以解决这个问题
 *
 * <p>http://www.spring4all.com/article/937
 *
 * @author Zoctan
 * @date 2018/07/11
 */
@Slf4j
public class MyRedisCacheManager extends RedisCacheManager
    implements ApplicationContextAware, InitializingBean {
  /** key serializer */
  public static final StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();
  /**
   * value serializer
   *
   * <p>使用 FastJsonRedisSerializer 会报错：java.lang.ClassCastException FastJsonRedisSerializer<Object>
   * fastSerializer = new FastJsonRedisSerializer<>(Object.class);
   */
  public static final GenericFastJsonRedisSerializer FASTJSON_SERIALIZER =
      new GenericFastJsonRedisSerializer();
  /** key serializer pair */
  public static final RedisSerializationContext.SerializationPair<String> STRING_PAIR =
      RedisSerializationContext.SerializationPair.fromSerializer(STRING_SERIALIZER);
  /** value serializer pair */
  public static final RedisSerializationContext.SerializationPair<Object> FASTJSON_PAIR =
      RedisSerializationContext.SerializationPair.fromSerializer(FASTJSON_SERIALIZER);

  private final Map<String, RedisCacheConfiguration> initialCacheConfiguration =
      new LinkedHashMap<>();
  private ApplicationContext applicationContext;

  public MyRedisCacheManager(
      final RedisCacheWriter cacheWriter, final RedisCacheConfiguration defaultCacheConfiguration) {
    super(cacheWriter, defaultCacheConfiguration);
  }

  public MyRedisCacheManager(
      final RedisCacheWriter cacheWriter,
      final RedisCacheConfiguration defaultCacheConfiguration,
      final Map<String, RedisCacheConfiguration> initialCacheConfigurations) {
    super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations);
  }

  @Override
  public Cache getCache(@NonNull final String name) {
    final Cache cache = super.getCache(name);
    return new RedisCacheWrapper(cache);
  }

  @Override
  public void setApplicationContext(@NonNull final ApplicationContext applicationContext)
      throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void afterPropertiesSet() {
    final String[] beanNames = this.applicationContext.getBeanNamesForType(Object.class);
    for (final String beanName : beanNames) {
      final Class clazz = this.applicationContext.getType(beanName);
      this.add(clazz);
    }
    super.afterPropertiesSet();
  }

  @NonNull
  @Override
  protected Collection<RedisCache> loadCaches() {
    final List<RedisCache> caches = new LinkedList<>();
    for (final Map.Entry<String, RedisCacheConfiguration> entry :
        this.initialCacheConfiguration.entrySet()) {
      caches.add(super.createRedisCache(entry.getKey(), entry.getValue()));
    }
    return caches;
  }

  private void add(final Class clazz) {
    ReflectionUtils.doWithMethods(
        clazz,
        method -> {
          ReflectionUtils.makeAccessible(method);
          final CacheExpire cacheExpire = AnnotationUtils.findAnnotation(method, CacheExpire.class);
          if (cacheExpire == null) {
            return;
          }
          final Cacheable cacheable = AnnotationUtils.findAnnotation(method, Cacheable.class);
          if (cacheable != null) {
            this.add(cacheable.cacheNames(), cacheExpire);
            return;
          }
          final Caching caching = AnnotationUtils.findAnnotation(method, Caching.class);
          if (caching != null) {
            final Cacheable[] cs = caching.cacheable();
            if (cs.length > 0) {
              for (final Cacheable c : cs) {
                if (c != null) {
                  this.add(c.cacheNames(), cacheExpire);
                }
              }
            }
          } else {
            final CacheConfig cacheConfig =
                AnnotationUtils.findAnnotation(clazz, CacheConfig.class);
            if (cacheConfig != null) {
              this.add(cacheConfig.cacheNames(), cacheExpire);
            }
          }
        },
        method -> null != AnnotationUtils.findAnnotation(method, CacheExpire.class));
  }

  private void add(final String[] cacheNames, final CacheExpire cacheExpire) {
    for (final String cacheName : cacheNames) {
      if (cacheName == null || "".equals(cacheName.trim())) {
        continue;
      }
      final long expire = cacheExpire.expire();
      log.debug("cache name<{}> expire: {}", cacheName, expire);
      if (expire >= 0) {
        // 缓存配置
        final RedisCacheConfiguration config =
            RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(expire))
                .disableCachingNullValues()
                // .prefixKeysWith(cacheName)
                .serializeKeysWith(STRING_PAIR)
                .serializeValuesWith(FASTJSON_PAIR);
        this.initialCacheConfiguration.put(cacheName, config);
      } else {
        log.warn("{} use default expiration.", cacheName);
      }
    }
  }

  protected static class RedisCacheWrapper implements Cache {
    private final Cache cache;

    RedisCacheWrapper(final Cache cache) {
      this.cache = cache;
    }

    @Override
    public String getName() {
      log.debug("get name: {}", this.cache.getName());
      try {
        return this.cache.getName();
      } catch (final Exception e) {
        log.error("get name => {}", e.getMessage());
        return null;
      }
    }

    @Override
    public Object getNativeCache() {
      log.debug("native cache: {}", this.cache.getNativeCache());
      try {
        return this.cache.getNativeCache();
      } catch (final Exception e) {
        log.error("get native cache => {}", e.getMessage());
        return null;
      }
    }

    @Override
    public ValueWrapper get(@NonNull final Object o) {
      log.debug("get => o: {}", o);
      try {
        return this.cache.get(o);
      } catch (final Exception e) {
        log.error("get => o: {}, error: {}", o, e.getMessage());
        return null;
      }
    }

    @Override
    public <T> T get(@NonNull final Object o, final Class<T> aClass) {
      log.debug("get => o: {}, clazz: {}", o, aClass);
      try {
        return this.cache.get(o, aClass);
      } catch (final Exception e) {
        log.error("get => o: {}, clazz: {}, error: {}", o, aClass, e.getMessage());
        return null;
      }
    }

    @Override
    public <T> T get(@NonNull final Object o, @NonNull final Callable<T> callable) {
      log.debug("get => o: {}", o);
      try {
        return this.cache.get(o, callable);
      } catch (final Exception e) {
        log.error("get => o: {}, error: {}", o, e.getMessage());
        return null;
      }
    }

    @Override
    public void put(@NonNull final Object o, final Object o1) {
      log.debug("put => o: {}, o1: {}", o, o1);
      try {
        this.cache.put(o, o1);
      } catch (final Exception e) {
        log.error("put => o: {}, o1: {}, error: {}", o, o1, e.getMessage());
      }
    }

    @Override
    public ValueWrapper putIfAbsent(@NonNull final Object o, final Object o1) {
      log.debug("put if absent => o: {}, o1: {}", o, o1);
      try {
        return this.cache.putIfAbsent(o, o1);
      } catch (final Exception e) {
        log.error("put if absent => o: {}, o1: {}, error: {}", o, o1, e.getMessage());
        return null;
      }
    }

    @Override
    public void evict(@NonNull final Object o) {
      log.debug("evict => o: {}", o);
      try {
        this.cache.evict(o);
      } catch (final Exception e) {
        log.error("evict => o: {}, error: {}", o, e.getMessage());
      }
    }

    @Override
    public void clear() {
      log.debug("clear");
      try {
        this.cache.clear();
      } catch (final Exception e) {
        log.error("clear => error: {}", e.getMessage());
      }
    }
  }
}
