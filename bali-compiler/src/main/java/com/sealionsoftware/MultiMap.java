package com.sealionsoftware;

import java.util.Collection;
import java.util.Map;

public interface MultiMap<K, V> extends Map<K, Collection<V>> {

    void putOne(K key, V value);

}
