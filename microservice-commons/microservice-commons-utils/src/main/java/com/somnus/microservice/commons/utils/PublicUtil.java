package com.somnus.microservice.commons.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.utils
 * @title: PublicUtil
 * @description: The class Public util.
 * @date 2019/3/15 14:55
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PublicUtil {

    /**
     * 判断对象是否Empty(null或元素为0)
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param pObj 待检查对象
     *
     * @return boolean 返回的布尔值
     */
    public static boolean isEmpty(Object pObj) {
        if (pObj == null) {
            return true;
        }
        if (pObj == "") {
            return true;
        }
        if (pObj instanceof String) {
            return ((String) pObj) == null || ((String) pObj).length() == 0;
        } else if (pObj instanceof Collection) {
            return ((Collection<?>) pObj) == null || ((Collection<?>) pObj).isEmpty();
        } else if (pObj instanceof Map) {
            return ((Map<?,?>) pObj) == null || ((Map<?,?>) pObj).size() == 0;
        }
        return false;
    }

    /**
     * 判断对象是否为NotEmpty(!null或元素大于0)
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param pObj 待检查对象
     *
     * @return boolean 返回的布尔值
     */
    public static boolean isNotEmpty(Object pObj) {
        return !isEmpty(pObj);
    }

    @SneakyThrows
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass){
        if (map == null){
            return null;
        }
        Object obj = beanClass.newInstance();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            Method setter = property.getWriteMethod();
            if (setter != null) {
                setter.invoke(obj, map.get(property.getName()));
            }
        }

        return obj;
    }

    @SneakyThrows
    public static Map<String, String> objectToMap(Object obj){
        if(obj == null){
            return null;
        }
        Map<String, String> map = new HashMap<>();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            //默认PropertyDescriptor会有一个class对象，剔除之
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter!=null ? getter.invoke(obj) : null;
            map.put(key, String.valueOf(value));
        }

        return map;
    }

    public static String[] shortUrl(String url){
        String hex = DigestUtils.md5Hex(url);
        String[] chars = new String[]{
                "a","b","c","d","e","f","g","h", "i","j","k","l","m","n","o","p", "q","r","s","t","u","v","w","x", "y","z",
                "0","1","2","3","4","5", "6","7","8","9",
                "A","B","C","D", "E","F","G","H","I","J","K","L", "M","N","O","P","Q","R","S","T", "U","V","W","X","Y","Z"
        };
        String[] resUrl = new String[4];
        for (int i = 0; i < 4; i++) {
            //把加密字符按照8位一组16进制与0x3FFFFFFF进行位与运算
            int hexint = 0x3FFFFFFF & Integer.parseUnsignedInt(hex.substring(i * 8, (i+1) * 8), 16);
            String outChars = "";
            for (int j = 0; j < 6; j++) {
                //把得到的值与0x0000003D进行位与运算，取得字符数组chars索引
                int index = 0x0000003D & hexint;
                //把取得的字符相加
                outChars += chars[index];
                //每次循环按位右移5位
                hexint = hexint >> 5;
            }
            //把字符串存入对应索引的输出数组
            resUrl[i] = outChars;
        }
        return resUrl;
    }

}