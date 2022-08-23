package com.somnus.microservice.autoconfigure.proxy.util;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author somnus
 * @title: Pair
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/13 16:29
 */
@RequiredArgsConstructor
public class Pair<K, V> implements Serializable, Cloneable, Map.Entry<K, V> {

    private final K k;
    private final V v;

    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    @Override
    public K getKey() {
        return k;
    }

    @Override
    public V getValue() {
        return v;
    }

    @Override
    public V setValue(V value) {
        throw new IllegalArgumentException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Pair<K, V> clone() {
        try {
            return (Pair<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
