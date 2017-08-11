package com.xy.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.util.Map;

/**
 * Created by xy on 2017/2/13.
 */
public class JacksonUtils {

    /**
     * json to bean.
     */
    public static <T> T jsonToBean(String str, Class<T> cls) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(str,cls);

    }

    /**
     * bean to json.
     */
    public static <T> String beanToJson(T bean) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(bean);
    }


    /**
     * xml to bean.
     */
    public static <T> T xmlToBean(String str, Class<T> cls) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return xmlMapper.readValue(str,cls);

    }

    /**
     * Bean to xml.
     */
    public static <T> String beanToXml(T bean) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.writeValueAsString(bean);
    }

    /**
     * map to Bean.
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> cls) throws IOException {
       ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.convertValue(map, cls);
    }

    /**
     * Bean to map.
     */
    public static <T> Map beanToMap(T bean) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(bean, Map.class);
    }

}
