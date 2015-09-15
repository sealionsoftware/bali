package com.sealionsoftware.bali;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Map;

public class Matchers {

    public static Matcher<?> isNull() {
        return new BaseMatcher<Object>() {
            public boolean matches(Object o) {
                return o == null;
            }

            public void describeTo(Description description) {
                description.appendText("null");
            }
        };
    }

    public static Matcher<Map<?,?>> isEmpty() {
        return new BaseMatcher<Map<?,?>>() {
            public boolean matches(Object o) {
                return (o instanceof Map) && ((Map) o).isEmpty();
            }

            public void describeTo(Description description) {
                description.appendText("empty map");
            }
        };
    }
}
