package com.sealionsoftware;

import com.sealionsoftware.Collections.Each;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

import static com.sealionsoftware.Collections.both;
import static com.sealionsoftware.Collections.flatten;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CollectionsTest {

    @Test
    public void constructorTest(){
        new Collections();
        new Collections.Each<>("one", "two");
    }

    @Test
    public void testBothWithIterables() throws Exception {

        Iterable<String> i = asList("1", "2");
        Iterable<String> j = asList("4", "5", "6");

        Iterator<Each<String, String>> subject = both(i, j).iterator();

        assertThat(subject.hasNext(), is(true));

        Each<String, String> next = subject.next();
        assertThat(next, notNullValue());
        assertThat(next.i, equalTo("1"));
        assertThat(next.j, equalTo("4"));

        assertThat(subject.hasNext(), is(true));

        next = subject.next();
        assertThat(next, notNullValue());
        assertThat(next.i, equalTo("2"));
        assertThat(next.j, equalTo("5"));

        assertThat(subject.hasNext(), is(false));
    }

    @Test
    public void testBothWithArrays() throws Exception {

        String[] i = {"1", "2"};
        String[] j = {"4", "5", "6"};

        Iterator<Each<String, String>> subject = both(i, j).iterator();

        assertThat(subject.hasNext(), is(true));

        Each<String, String> next = subject.next();
        assertThat(next, notNullValue());
        assertThat(next.i, equalTo("1"));
        assertThat(next.j, equalTo("4"));

        assertThat(subject.hasNext(), is(true));

        next = subject.next();
        assertThat(next, notNullValue());
        assertThat(next.i, equalTo("2"));
        assertThat(next.j, equalTo("5"));

        assertThat(subject.hasNext(), is(false));
    }

    @Test
    public void testFlatten(){
        Collection<String> flat = flatten(asList(asList("one", "two"), asList("three")));
        assertThat(flat, contains(equalTo("one"), equalTo("two"), equalTo("three")));
    }
}