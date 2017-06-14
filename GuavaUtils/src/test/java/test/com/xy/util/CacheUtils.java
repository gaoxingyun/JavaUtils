package test.com.xy.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Test;

/**
 * Created by xy on 2017/3/22.
 */
public class CacheUtils {
    @Test
    public void LoadCache() {
        LoadingCache<String, Object> cache = CacheBuilder.newBuilder().build(new CacheLoader<String, Object>() {
            @Override
            public Object load(String key) throws Exception {
                return null;
            }
        });
    }
}
