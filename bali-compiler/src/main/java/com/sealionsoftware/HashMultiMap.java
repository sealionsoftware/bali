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

    @SuppressWarnings("unchecked")
    public Map.Entry<K, Collection<V>>[] toArray() {
        return delegate.entrySet().toArray(new Map.Entry[size()]);
    }

    public <T> T[] toArray(T[] a) {
        return delegate.entrySet().toArray(a);
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
        for (Map.Entry<? extends K, ? extends Collection<V>> entry : m.entrySet()){
            get(entry.getKey()).addAll(entry.getValue());
        }
    }

    public boolean remove(Object key) {
        return delegate.remove(key) == null;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    public boolean addAll(Collection<? extends Map.Entry<K, Collection<V>>> c) {
        boolean ret = false;
        for (Map.Entry<K, Collection<V>> entry : c) ret |= add(entry);
        return ret;
    }

    public boolean removeAll(Collection<?> c) {
        boolean ret = false;
        for (Object entry : c) ret |= remove(entry);
        return ret;
    }

    public boolean retainAll(Collection<?> c) {
        boolean ret = false;
        Iterator<K> keys = delegate.keySet().iterator();
        while (keys.hasNext()) if (!c.contains(keys.next())){
            keys.remove();
            ret = true;
        }
        return ret;
    }

    public void clear() {
        delegate.clear();
    }

    public Collection<V> values() {
        return flatten(delegate.values());
    }

    public String toString() {
        return delegate.toString();
    }

    public Integer numberOfValues() {
        return delegate.values().stream().mapToInt(Collection::size).sum();
    }
}
