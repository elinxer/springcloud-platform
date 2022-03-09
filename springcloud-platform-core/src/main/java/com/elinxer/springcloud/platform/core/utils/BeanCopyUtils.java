package com.elinxer.springcloud.platform.core.utils;


import org.springframework.beans.BeanUtils;

import java.util.*;

/**
 * Bean转化工具类
 */
public class BeanCopyUtils {

    public static <T> T sourceToTarget(Object source, Class<T> target) {
        if (source == null) {
            return null;
        }
        T targetObject = null;
        try {
            targetObject = target.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, targetObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetObject;
    }

    public static <T> List<T> sourceToTarget(Collection<?> sourceList, Class<T> target) {
        if (sourceList == null) {
            return null;
        }
        List<T> targetList = new ArrayList<>(sourceList.size());
        try {
            for (Object source : sourceList) {
                T targetObject = target.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(source, targetObject);
                targetList.add(targetObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetList;
    }


    public static <T> Set<T> sourceToTarget(Set<?> sourceList, Class<T> target) {
        if (sourceList == null) {
            return null;
        }
        Set<T> targetList = new HashSet<>(sourceList.size());
        try {
            for (Object source : sourceList) {
                T targetObject = target.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(source, targetObject);
                targetList.add(targetObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetList;
    }

}
