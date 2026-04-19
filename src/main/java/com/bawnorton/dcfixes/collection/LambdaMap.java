package com.bawnorton.dcfixes.collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public abstract class LambdaMap<K, V> implements Map<K, V> {
    protected final Function<K, V> elementMapper;
    protected final Map<K, V> cache;
    protected final Set<K> knownKeys;

    public LambdaMap(Function<K, V> elementMapper) {
        this(elementMapper, Collections.emptySet());
    }

    public LambdaMap(Function<K, V> elementMapper, Iterable<? extends K> knownKeys) {
        this.elementMapper = elementMapper;
        this.cache = new HashMap<>();
        this.knownKeys = new LinkedHashSet<>();
        for (K knownKey : knownKeys) {
            this.knownKeys.add(knownKey);
        }
    }

    protected Set<K> knownKeySet() {
        return knownKeys;
    }

    @Override
    public int size() {
        return knownKeySet().size();
    }

    @Override
    public boolean isEmpty() {
        return knownKeySet().isEmpty();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean containsKey(Object key) {
        return knownKeySet().contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for (K key : knownKeySet()) {
            if (Objects.equals(get(key), value)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        K castKey = (K) key;
        knownKeySet().add(castKey);
        if (cache.containsKey(castKey)) {
            return cache.get(castKey);
        }
        V value = elementMapper.apply(castKey);
        cache.put(castKey, value);
        return value;
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        knownKeySet().add(key);
        return cache.put(key, value);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public V remove(Object key) {
        knownKeySet().remove(key);
        return cache.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        cache.clear();
        knownKeySet().clear();
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        return Collections.unmodifiableSet(knownKeySet());
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return new AbstractCollection<>() {
            @Override
            public @NotNull Iterator<V> iterator() {
                Iterator<K> keyIterator = keySet().iterator();
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return keyIterator.hasNext();
                    }

                    @Override
                    public V next() {
                        return get(keyIterator.next());
                    }
                };
            }

            @Override
            public int size() {
                return LambdaMap.this.size();
            }

            @Override
            public boolean contains(Object o) {
                return LambdaMap.this.containsValue(o);
            }
        };
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return new AbstractSet<>() {
            @Override
            public @NotNull Iterator<Entry<K, V>> iterator() {
                Iterator<K> keyIterator = keySet().iterator();
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return keyIterator.hasNext();
                    }

                    @Override
                    public Entry<K, V> next() {
                        return new LazyEntry(keyIterator.next());
                    }
                };
            }

            @Override
            public int size() {
                return LambdaMap.this.size();
            }

            @Override
            public boolean contains(Object o) {
                if (!(o instanceof Entry<?, ?> otherEntry)) {
                    return false;
                }
                if (!LambdaMap.this.containsKey(otherEntry.getKey())) {
                    return false;
                }
                return Objects.equals(LambdaMap.this.get(otherEntry.getKey()), otherEntry.getValue());
            }
        };
    }

    private class LazyEntry implements Entry<K, V> {
        private final K key;

        private LazyEntry(K key) {
            this.key = key;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return LambdaMap.this.get(key);
        }

        @Override
        public V setValue(V value) {
            return LambdaMap.this.put(key, value);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Entry<?, ?> otherEntry)) {
                return false;
            }
            return Objects.equals(key, otherEntry.getKey()) && Objects.equals(getValue(), otherEntry.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
        }
    }
}
