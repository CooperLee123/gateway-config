package com.aethercoder.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hepengfei on 2017/8/31.
 */
public class BeanUtils {
    public static void copyProperties(Object source, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target, "createTime");
    }


    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        String[] ignores = new String[ignoreProperties.length + 1];
        for(int i = 0; i < ignoreProperties.length; i++) {
            ignores[i] = ignoreProperties[i];
        }
        ignores[ignores.length - 1] = "createTime";
        org.springframework.beans.BeanUtils.copyProperties(source, target, ignores);
    }

    public static void copyPropertiesWithoutNull(Object source, Object target, String... ignoreProperties) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        List<String> emptyNames = new ArrayList<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        emptyNames.addAll(Arrays.asList(ignoreProperties));
        String[] nullProperties = new String[emptyNames.size()];
        emptyNames.toArray(nullProperties);
        copyProperties(source, target, nullProperties);
    }

    public static <T> T jsonToObject(String json, Class<T> cls) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            T o = mapper.readValue(json, cls);
            return o;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> jsonToList(String json, Class<T> cls)  {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, cls);
        try {
             List<T> list = (List<T>)mapper.readValue(json, javaType);
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String objectToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String result = mapper.writeValueAsString(object);
            return result;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
