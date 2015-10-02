package com.sealionsoftware;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.Collection;
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

    public static Matcher<Map<?,?>> isEmptyMap() {
        return new BaseMatcher<Map<?,?>>() {
            public boolean matches(Object o) {
                return (o instanceof Map) && ((Map) o).isEmpty();
            }

            public void describeTo(Description description) {
                description.appendText("empty map");
            }
        };
    }

    public static Matcher<Collection<?>> isEmpty() {
        return new BaseMatcher<Collection<?>>() {
            public boolean matches(Object o) {
                return (o instanceof Collection) && ((Collection) o).isEmpty();
            }

            public void describeTo(Description description) {
                description.appendText("empty map");
            }
        };
    }

    public static <T> Matcher<Collection<T>> containsOneValue() {
        return containsOneValue(null);
    }

    public static <T> Matcher<Collection<T>> containsOneValue(Matcher<T> matcher) {
        return new TypeSafeDiagnosingMatcher<Collection<T>>() {

            public void describeTo(Description description) {
                description.appendText("one value ");
                if (matcher != null){
                    matcher.describeTo(description);
                }
            }

            protected boolean matchesSafely(Collection<T> ts, Description description) {
                if (ts.size() != 1){
                    description.appendText("was a collection with " + ts.size() + " elements");
                    return false;
                }
                if (matcher != null){
                    matcher.describeMismatch(ts, description);
                    return  matcher.matches(ts.iterator().next());
                }
                return true;
            }
        };
    }
}
