package med.voll.api.infra.cache;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.Scheduler;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${spring.cache.caffeine.expiration:900}")
    private Integer defaultCacheExpiration;

    @Value("${spring.cache.caffeine.login.expiration:900}")
    private Integer loginCacheExpiration;

    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder().expireAfterWrite(defaultCacheExpiration, TimeUnit.SECONDS)
                .evictionListener((Object key, Object value,
                        RemovalCause cause) -> logger.info(String.format("Key %s was evicted (%s)%n", key, cause)))
                .removalListener((Object key, Object value,
                        RemovalCause cause) -> logger.info(String.format("Key %s was removed (%s)%n", key, cause)))
                .scheduler(Scheduler.systemScheduler());
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {

        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        cacheManager.setCaffeine(caffeine);

        var loginCache = Caffeine.newBuilder().expireAfterWrite(defaultCacheExpiration, TimeUnit.SECONDS).build();

        cacheManager.registerCustomCache("login", loginCache);

        return cacheManager;
    }
}
