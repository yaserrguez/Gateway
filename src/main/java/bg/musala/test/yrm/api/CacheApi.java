package bg.musala.test.yrm.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * The interface Cache api.
 */
public interface CacheApi
{
    /**
     * It eliminates (to erase) all the * * cache * * of the services.
     *
     * @return the response entity
     */
    ResponseEntity clearAllCaches();

    /**
     * It eliminates (to erase) the * * cache * * with the specified name.
     *
     * @param cacheName name from the cahe to erase
     * @return the response entity
     */
    ResponseEntity clearCache(String cacheName);
}
