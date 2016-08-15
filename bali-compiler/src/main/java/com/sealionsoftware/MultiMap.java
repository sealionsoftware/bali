package com.sealionsoftware;
import java.util.Collection;
import java.util.Map;

public interface MultiMap<K, V> extends Collection<Map.Entry<K, Collection<V>>> {

    Collection<V> get(K key);

    void put(K key, V value);

    Collection<V> values();

    Integer numberOfValues();

}
