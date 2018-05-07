package com.xy.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * toMap工具类
 *
 */
public class ToMapUtils {

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                field.setAccessible(true);
                obj = field.get(this);
                if (obj != null) {
                    map.put(field.getName(), obj.toString());
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
