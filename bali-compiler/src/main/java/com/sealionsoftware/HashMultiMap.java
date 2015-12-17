package com.sealionsoftware;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.sealionsoftware.Collections.flatten;

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

    public boolean contains(Object o) {
        return delegate.containsKey(o);
    }

    public Iterator<Map.Entry<K, Collection<V>>> iterator() {
        return delegate.entrySet().iterator();
    }

    public Map.Entry<K, Collection<V>>[] toArray() {
        @SuppressWarnings("unchecked")
        Map.Entry<K, Collection<V>>[]  ret = delegate.entrySet().toArray(new Map.Entry[size()]);
        return ret;
    }

    public <T> T[] toArray(T[] a) {
        return a;
    }

    public boolean add(Map.Entry<K, Collection<V>> entry) {
        return get(entry.getKey()).addAll(entry.getValue());
    }

    public Collection<V> get(K key) {
        Collection<V> collection = delegate.get(key);
        if (collection == null){
            collection = constructor.produce();

        }
        return collection;
    }

    public void put(K key, V value) {

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

    public boolean remove(Object key) {
        return delegate.remove(key) == null;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    public boolean addAll(Collection<? extends Map.Entry<K, Collection<V>>> c) {
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public void clear() {
        delegate.clear();
    }

    public Collection<V> values() {
        return flatten(delegate.values());
    }
}
