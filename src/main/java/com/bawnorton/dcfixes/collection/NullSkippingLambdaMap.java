package com.bawnorton.dcfixes.collection;

import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class NullSkippingLambdaMap<K, V> extends LambdaMap<K, V> {
    private final Set<K> nullComputedKeys = new HashSet<>();

    public NullSkippingLambdaMap(Function<K, V> elementMapper) {
        super(elementMapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        K castKey = (K) key;
        if (cache.containsKey(castKey)) {
            knownKeySet().add(castKey);
            return cache.get(castKey);
        }
        if (nullComputedKeys.contains(castKey)) {
            return null;
        }

        V value = elementMapper.apply(castKey);
        if (value == null) {
            nullComputedKeys.add(castKey);
            knownKeySet().remove(castKey);
            return null;
        }

        nullComputedKeys.remove(castKey);
        knownKeySet().add(castKey);
        cache.put(castKey, value);
        return value;
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        if (value == null) {
            knownKeySet().remove(key);
            V previousValue = cache.remove(key);
            nullComputedKeys.add(key);
            return previousValue;
        }

        knownKeySet().add(key);
        nullComputedKeys.remove(key);
        return cache.put(key, value);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public V remove(Object key) {
        knownKeySet().remove(key);
        nullComputedKeys.remove(key);
        return cache.remove(key);
    }

    @Override
    public void clear() {
        super.clear();
        nullComputedKeys.clear();
    }
}
