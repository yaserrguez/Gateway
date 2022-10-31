package bg.musala.test.yrm.restController;

import bg.musala.test.yrm.api.CacheApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.cache.Cache;
import javax.cache.CacheManager;

/**
 * Cache controller implement rest service to CacheApi.
 */
@RestController
@RequestMapping("/caches")
public class CacheController implements CacheApi
{
    @Autowired
    private CacheManager cacheManager;

    @Scheduled(fixedRate = 30000)
    @DeleteMapping()
    public ResponseEntity clearAllCaches()
    {
        cacheManager.getCacheNames().forEach(cacheName -> {
            clearCacheFromCacheName(cacheName);
        });
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{cacheName}")
    public ResponseEntity clearCache(@PathVariable("cacheName") String cacheName)
    {
        return clearCacheFromCacheName(cacheName) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    private Boolean clearCacheFromCacheName(final String cacheName)
    {
        final Cache cache = cacheManager.getCache(cacheName);
        if (cacheExists(cache))
        {
            cache.clear();
            return true;
        }
        return false;
    }

    private Boolean cacheExists(final Cache cache)
    {
        return cache != null;
    }
}
