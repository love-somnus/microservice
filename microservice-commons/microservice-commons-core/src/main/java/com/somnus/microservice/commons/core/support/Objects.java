package com.somnus.microservice.commons.core.support;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.lang.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author kevin.liu
 * @title: Objects
 * @description: TODO
 * @date 2021/2/4 15:46
 */
@UtilityClass
public class Objects extends org.springframework.util.ObjectUtils {

    /**
     * 数组不为空
     * @param array
     * @return boolean
     */
    public static boolean isNotEmpty(@Nullable Object[] array){
        return !isEmpty(array);
    }

    /**
     * 对象不为空
     * @param obj
     * @return boolean
     */
    public static boolean isNotEmpty(@Nullable Object obj){
        return !isEmpty(obj);
    }

    /**
     * 对象转换
     * @param s
     * @param clazz
     * @param <S>
     * @param <D>
     * @return
     */
    public static <S, D> D convert(S s, Class<D> clazz){

        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return mapper.map(s, clazz);
    }

    /**
     * 对象转换
     * @param s
     * @param clazz
     * @param <S>
     * @param <D>
     * @return
     */
    public static <S, D> D convert(S s, Class<D> clazz, BiConsumer<S, D> consumer){

        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        D d = mapper.map(s, clazz);

        consumer.accept(s, d);

        return d;
    }

    /**
     * 对象转换DOMAIN -> VO
     * @param original
     * @param <DOMAIN>
     * @param <VO>
     * @return
     */
    public static <DOMAIN, VO> List<VO> convert(List<DOMAIN> original, Class<VO> clazz){

        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return original.parallelStream().map(entity -> mapper.map(entity, clazz)).collect(Collectors.toList());
    }

    /**
     * 分页对象转换DOMAIN -> VO
     * @param original
     * @param <DOMAIN>
     * @param <VO>
     * @return
     */
    public static <DOMAIN, VO> IPage<VO> convert(IPage<DOMAIN> original, Class<VO> clazz){

        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<VO> list = convert(original.getRecords(), clazz);

        IPage<VO> page = mapper.map(original, IPage.class);

        page.setRecords(list);

        return page;
    }

    /**
     * 分页对象转换DOMAIN -> VO
     * @param original
     * @param <DOMAIN>
     * @param <VO>
     * @return
     */
    public static <DOMAIN, VO> PageInfo<VO> convert(PageInfo<DOMAIN> original, Class<VO> clazz){

        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<VO> list = convert(original.getList(), clazz);

        PageInfo<VO> pageInfo = mapper.map(original, PageInfo.class);

        pageInfo.setList(list);

        return pageInfo;
    }

    /**
     * 对象转换DOMAIN -> VO
     * @param original
     * @param <DOMAIN>
     * @param <VO>
     * @return
     */
    public static <DOMAIN, VO> List<VO> convertList(List<DOMAIN> original, Class<VO> clazz, BiConsumer<DOMAIN, VO> consumer){

        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return original.parallelStream().map(entity -> convert(entity, clazz, consumer)).collect(Collectors.toList());
    }

    /**
     * 分页对象转换DOMAIN -> VO
     * @param original
     * @param <DOMAIN>
     * @param <VO>
     * @return
     */
    public static <DOMAIN, VO> IPage<VO> convertPage(IPage<DOMAIN> original, Class<VO> clazz, BiConsumer<DOMAIN, VO> consumer){

        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<VO> list = original.getRecords().parallelStream().map(entity -> convert(entity, clazz, consumer)).collect(Collectors.toList());

        IPage<VO> page = mapper.map(original, IPage.class);

        page.setRecords(list);

        return page;
    }

    /**
     * 分页对象转换DOMAIN -> VO
     * @param original
     * @param <DOMAIN>
     * @param <VO>
     * @return
     */
    public static <DOMAIN, VO> PageInfo<VO> convertPageInfo(PageInfo<DOMAIN> original, Class<VO> clazz, BiConsumer<DOMAIN, VO> consumer){

        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<VO> list = original.getList().parallelStream().map(entity -> convert(entity, clazz, consumer)).collect(Collectors.toList());

        PageInfo<VO> pageInfo = mapper.map(original, PageInfo.class);

        pageInfo.setList(list);

        return pageInfo;
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
