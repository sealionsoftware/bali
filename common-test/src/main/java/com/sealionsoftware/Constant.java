package com.sealionsoftware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.addAll;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;

public class Constant {

    @SafeVarargs
    public static <T> List<T> list(T... arguments) {
        List<T> list = new ArrayList<>(arguments.length);
        addAll(list, arguments);
        return unmodifiableList(list);
    }

    @SafeVarargs
    public static <T> Set<T> set(T... arguments) {
        Set<T> set = new HashSet<>(arguments.length);
        addAll(set, arguments);
        return unmodifiableSet(set);
    }

    @SafeVarargs
    public static <K, V> Map<K, V> map(Mapping<K, V>... arguments) {
        Map<K, V> map = new HashMap<>(arguments.length);
        for (Mapping<K, V> put : arguments){
            map.put(put.key, put.value);
        }
        return unmodifiableMap(map);
    }

    public static <K, V> Mapping<K, V> put(K key, V value) {
        return new Mapping<>(key, value);
    }

    public static class Mapping<K, V> {
        public final K key;
        public final V value;

        public Mapping(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

}
