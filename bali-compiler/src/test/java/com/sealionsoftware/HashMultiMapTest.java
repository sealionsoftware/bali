package com.sealionsoftware;

import org.junit.Test;

import java.util.ArrayList;

import static com.sealionsoftware.Constant.list;
import static com.sealionsoftware.Constant.map;
import static com.sealionsoftware.Constant.put;
import static com.sealionsoftware.Matchers.containsOneEntry;
import static com.sealionsoftware.Matchers.containsOneValue;
import static com.sealionsoftware.Matchers.isEmpty;
import static com.sealionsoftware.Matchers.isEmptyMap;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HashMultiMapTest {

    private HashMultiMap<String, String> subject = new HashMultiMap<>(ArrayList::new);

    @Test
    public void testSize() throws Exception {
        subject.put("key", asList("value"));
        assertThat(subject.size(), equalTo(1));
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertThat(subject.isEmpty(), is(true));
        subject.put("key", asList("value"));
        assertThat(subject.isEmpty(), is(false));
    }

    @Test
    public void testGet() throws Exception {
        subject.put("key", asList("value"));
        assertThat(subject.get("key"), containsOneValue("value"));
    }

    @Test
    public void testGetEmptyList() throws Exception {
        assertThat(subject.get("key"), isEmpty());
    }

    @Test
    public void testContainsKey() throws Exception {
        subject.put("key", asList("value"));
        assertThat(subject.containsKey("key"), is(true));
    }


    @Test
    public void testPutOne() throws Exception {
        subject.putOne("key", "value");
        assertThat(subject, containsOneEntry("key", asList("value")));
    }

    @Test
    public void testPutOneIntoExistingValue() throws Exception {
        subject.putOne("key", "value");
        subject.putOne("key", "anotherValue");
        assertThat(subject, containsOneEntry("key", asList("value", "anotherValue")));
    }

    @Test
    public void testPutAll() throws Exception {
        subject.putAll(map(put("key", list("value"))));
        assertThat(subject.get("key"), containsOneValue("value"));
    }

    @Test
    public void testRemove() throws Exception {
        subject.put("key", asList("value"));
        subject.remove("key");
        assertThat(subject, isEmptyMap());
    }

    @Test
    public void testClear() throws Exception {
        subject.put("key", asList("value"));
        subject.clear();
        assertThat(subject, isEmptyMap());
    }

    @Test
    public void testContainsValue() throws Exception {
        subject.put("key", asList("value"));
        assertThat(subject.containsValue(asList("value")), is(true));
    }

    @Test
    public void testKeySet() throws Exception {
        subject.put("key", asList("value"));
        assertThat(subject.keySet(), containsOneValue("key"));

    }

    @Test
    public void testValues() throws Exception {
        subject.put("key", asList("value"));
        assertThat(subject.values(), containsOneValue(containsOneValue("value")));
    }

    @Test
    public void testEntrySet() throws Exception {
        subject.put("key", asList("value"));
        assertThat(subject.entrySet(), containsOneValue());
    }
}