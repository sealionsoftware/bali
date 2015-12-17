package com.sealionsoftware;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import static com.sealionsoftware.Constant.list;
import static com.sealionsoftware.Constant.map;
import static com.sealionsoftware.Constant.put;
import static com.sealionsoftware.Matchers.containsOneValue;
import static com.sealionsoftware.Matchers.isEmpty;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HashMultiMapTest {

    private HashMultiMap<String, String> subject = new HashMultiMap<>(ArrayList::new);

    @Test
    public void testSize() throws Exception {
        subject.put("key", "value");
        assertThat(subject.size(), equalTo(1));
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertThat(subject.isEmpty(), is(true));
        subject.put("key", "value");
        assertThat(subject.isEmpty(), is(false));
    }

    @Test
    public void testGet() throws Exception {
        subject.put("key", "value");
        assertThat(subject.get("key"), containsOneValue("value"));
    }

    @Test
    public void testGetEmptyList() throws Exception {
        assertThat(subject.get("key"), isEmpty());
    }

    @Test
    public void testContainsKey() throws Exception {
        subject.put("key", "value");
        assertThat(subject.contains("key"), is(true));
    }

    @Test
    public void testPutOne() throws Exception {
        subject.put("key", "value");
        assertThat(subject, containsOneValue(new Entry<>("key", asList("value"))));
    }

    @Test
    public void testPutOneIntoExistingValue() throws Exception {
        subject.put("key", "value");
        subject.put("key", "anotherValue");
        assertThat(subject, containsOneValue(new Entry<>("key", asList("value", "anotherValue"))));
    }

    @Test
    public void testPutAll() throws Exception {
        subject.putAll(map(put("key", list("value"))));
        assertThat(subject.get("key"), containsOneValue("value"));
    }

    @Test
    public void testRemove() throws Exception {
        subject.put("key", "value");
        subject.remove("key");
        assertThat(subject, isEmpty());
    }

    @Test
    public void testClear() throws Exception {
        subject.put("key", "value");
        subject.clear();
        assertThat(subject, isEmpty());
    }

    private static class Entry<K, V> implements Map.Entry<K, V>{
        private final K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V ret = this.value;
            this.value = value;
            return ret;
        }
    }
}