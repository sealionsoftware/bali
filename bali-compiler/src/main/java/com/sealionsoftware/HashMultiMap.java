package com.sealionsoftware;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HashMultiMap<K, V> implements MultiMap<K, V> {

    private Producer<? extends Collection<V>> constructor;
    private HashMap<K, Collection<V>> delegate = new HashMap<>();

    public HashMultiMap(Producer<? extends Collection<V>> constructor){
        this.constructor = constructor;
    }

    public int size() {
        return delegate.size();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public Collection<V> get(Object key) {
        Collection<V> collection = delegate.get(key);
        return collection == null ? constructor.produce() : collection;
    }

    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    public Collection<V> put(K key, Collection<V> value) {
        return delegate.put(key, value);
    }

    public void putOne(K key, V value) {

        Collection<V> collection = delegate.get(key);
        if (collection == null){
            collection = constructor.produce();
            delegate.put(key, collection);
        }
        collection.add(value);
    }

    public void putAll(Map<? extends K, ? extends Collection<V>> m) {
        delegate.putAll(m);
    }

    public Collection<V> remove(Object key) {
        return delegate.remove(key);
    }

    public void clear() {
        delegate.clear();
    }

    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    public Set<K> keySet() {
        return delegate.keySet();
    }

    public Collection<Collection<V>> values() {
        return delegate.values();
    }

    public Set<Entry<K, Collection<V>>> entrySet() {
        return delegate.entrySet();
    }

}
