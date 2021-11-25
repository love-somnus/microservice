package com.somnus.microservice.commons.base.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cglib.beans.BeanMap;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.utils
 * @title: PublicUtil
 * @description: The class Public util.
 * @date 2019/3/15 14:55
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PublicUtil extends org.springframework.beans.BeanUtils{

    /**
     * 实例化对象：传入类对类进行实例化对象
     *
     * @param clazz 类
     * @return 对象
     * @author Lius
     * @date 2018/10/26 13:49
     */
    public static <T> T newInstance(Class<T> clazz) {
        return (T) instantiateClass(clazz);
    }

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
            return ((String) pObj).length() == 0;
        } else if (pObj instanceof Collection) {
            return ((Collection<?>) pObj).isEmpty();
        } else if (pObj instanceof Map) {
            return ((Map<?, ?>) pObj).size() == 0;
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
    public static <T> T  mapToBean(Map<String, Object> beanMap, Class<T> valueType){
        T bean = newInstance(valueType);
        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(valueType);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String properName = propertyDescriptor.getName();
            // 过滤class属性
            if ("class".equals(properName)) {
                continue;
            }
            if (beanMap.containsKey(properName)) {
                Method writeMethod = propertyDescriptor.getWriteMethod();
                if (null == writeMethod) {
                    continue;
                }
                Object value = beanMap.get(properName);
                if (!writeMethod.isAccessible()) {
                    writeMethod.setAccessible(true);
                }
                try {
                    if(Objects.nonNull(value) && propertyDescriptor.getPropertyType().isAssignableFrom(Date.class)){
                        Date date = Date.from(LocalDateTime.parse(value.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.systemDefault()).toInstant());
                        writeMethod.invoke(bean, date);
                    } else if(propertyDescriptor.getPropertyType().isAssignableFrom(LocalDateTime.class)){
                        LocalDateTime date = LocalDateTime.parse(value.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        writeMethod.invoke(bean, date);
                    } else{
                        writeMethod.invoke(bean, value);
                    }
                } catch (Throwable throwable) {
                    throw new RuntimeException("Could not set property '" + properName + " ' to bean" + throwable);
                }
            }
        }
        return bean;
    }

    /**
     * Object 转换为map
     * @param src
     * @return
     */
    public static Map<?, ?> beanToMap(Object src){
        return BeanMap.create(src);
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