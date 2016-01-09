package com.sealionsoftware;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static com.sealionsoftware.Constant.list;
import static com.sealionsoftware.Constant.map;
import static com.sealionsoftware.Constant.put;
import static com.sealionsoftware.Matchers.hasLength;
import static com.sealionsoftware.Matchers.isEmpty;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

public class HashMultiMapTest {

    private HashMultiMap<String, String> subject = new HashMultiMap<>(ArrayList::new);

    @Before
    public void setUp(){
        subject.put("key", "value");
    }

    @Test
    public void testSize() throws Exception {
        assertThat(subject.size(), equalTo(1));
    }

    @Test
    public void testIsEmptyAndClear() throws Exception {
        assertThat(subject.isEmpty(), is(false));
        subject.clear();
        assertThat(subject.isEmpty(), is(true));
    }

    @Test
    public void testGet() throws Exception {
        assertThat(subject.get("key"), hasItem("value"));
    }

    @Test
    public void testGetEmptyList() throws Exception {
        assertThat(subject.get("notAKey"), isEmpty());
    }

    @Test
    public void testContainsKey() throws Exception {
        assertThat(subject.contains("key"), is(true));
        assertThat(subject.contains("notAKey"), is(false));
    }

    @Test
    public void testPutOneIntoExistingValue() throws Exception {
        subject.put("key", "anotherValue");
        assertThat(subject, hasItem(new Entry<>("key", asList("value", "anotherValue"))));
    }

    @Test
    public void testPutAll() throws Exception {
        subject.putAll(map(put("key", list("anotherValue"))));
        assertThat(subject, hasItem(new Entry<>("key", asList("value", "anotherValue"))));
    }

    @Test
    public void testRemove() throws Exception {
        subject.remove("key");
        assertThat(subject, isEmpty());
    }

    @Test
    public void testToArray() throws Exception {
        Map.Entry<String, Collection<String>>[] array = subject.toArray();
        assertThat(array, notNullValue());
        assertThat(array.length, equalTo(1));
        assertThat(array[0].getKey(), equalTo("key"));
        assertThat(array[0].getValue(), hasItem("value"));
    }

    @Test
    public void testToArrayWithType() throws Exception {
        @SuppressWarnings("unchecked")
        Map.Entry<String, Collection<String>>[] array = subject.toArray(new Map.Entry[15]);
        assertThat(array, hasLength(15));
    }

    @Test
    public void testAdd() throws Exception {
        subject.add(new Entry<>("key", asList("anotherValue")));
        assertThat(subject, hasItem(new Entry<>("key", asList("value", "anotherValue"))));
    }

    @Test
    public void testDoesContainAll() throws Exception {
        assertThat(subject.containsAll(asList("key")), is(true));
    }

    @Test
    public void testDoesNotContainAll() throws Exception {
        assertThat(subject.containsAll(asList("key", "anotherKey")), is(false));
    }

    @Test
    public void testAddAll() throws Exception {
        subject.addAll(asList(new Entry<>("key", asList("anotherValue"))));
        assertThat(subject.containsAll(asList("key", "anotherKey")), is(false));
    }

    @Test
    public void testRemoveAll() throws Exception {
        subject.put("anotherKey", "anotherValue");
        subject.removeAll(asList("key"));
        assertThat(subject, hasItem(new Entry<>("anotherKey", asList("anotherValue"))));
    }

    @Test
    public void testRetainAll() throws Exception {
        subject.put("anotherKey", "anotherValue");
        subject.retainAll(asList("key"));
        assertThat(subject, hasItem(new Entry<>("key", asList("value"))));
    }

    @Test
    public void testToString() throws Exception {
        assertThat(subject, hasToString(containsString("key")));
        assertThat(subject, hasToString(containsString("value")));
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

        public String toString() {
            return "{" + key + "=" + value + '}';
        }
    }
}