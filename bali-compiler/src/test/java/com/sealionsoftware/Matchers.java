package com.sealionsoftware;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;

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

    public static <T> Matcher<Collection<T>> containsOneValue(T matcher) {
        return containsOneValue(equalTo(matcher));
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

    @SafeVarargs
    public static <T> Matcher<List<T>> containsSequentialValues(Matcher<T>... matchers) {
        return new TypeSafeDiagnosingMatcher<List<T>>() {

            public void describeTo(Description description) {
                description.appendText(" values matching: ");
                Iterator<Matcher<T>> i = asList(matchers).iterator();
                i.next().describeTo(description);
                while (i.hasNext()){
                    description.appendText(", ");
                    i.next().describeTo(description);
                }
            }

            protected boolean matchesSafely(List<T> ts, Description description) {
                if (ts.size() != matchers.length){
                    description.appendText("was an collection with " + ts.size() + " elements");
                    return false;
                }
                boolean matches = true;
                int i = 0;
                for (T item : ts) {
                    Matcher<T> matcher = matchers[i];
                    if (!matcher.matches(item)) {
                        if (!matches) description.appendText(", ");
                        description.appendText("item " + i + " did not match: ");
                        matcher.describeMismatch(item, description);
                        matches = false;

                    }
                    i++;
                }
                return matches;
            }
        };
    }

    @SafeVarargs
    public static <T> Matcher<Collection<T>> containsValuesMatching(Matcher<T>... matchers) {
        return new TypeSafeDiagnosingMatcher<Collection<T>>() {

            public void describeTo(Description description) {
                description.appendText(" values matching: ");
                Iterator<Matcher<T>> i = asList(matchers).iterator();
                i.next().describeTo(description);
                while (i.hasNext()){
                    description.appendText(", ");
                    i.next().describeTo(description);
                }
            }

            protected boolean matchesSafely(Collection<T> ts, Description description) {
                if (ts.size() != matchers.length){
                    description.appendText("was an collection with " + ts.size() + " elements");
                    return false;
                }

                boolean matches = true;
                for (T item : ts){
                    if (anyMatches(item)) break;
                    if (!matches) description.appendText(", ");
                    description.appendText(" item " + item + " did not match any matcher");
                    matches = false;
                }
                return matches;
            }

            private boolean anyMatches(T item){
                for (Matcher<T> matcher : matchers) if (matcher.matches(item)) return true;
                return false;
            }
        };
    }

    public static <K, V> Matcher<Map<K, V>> containsOneEntry(K key, V value) {
        return containsOneEntry(equalTo(key), equalTo(value));
    }

    public static <K, V> Matcher<Map<K, V>> containsOneEntry(Matcher<K> keyMatcher, Matcher<V> valueMatcher) {
        return new TypeSafeDiagnosingMatcher<Map<K, V>>() {

            public void describeTo(Description description) {
                description.appendText("one entry ");
                if (keyMatcher != null){
                    keyMatcher.describeTo(description);
                }
                description.appendText(" mapped to ");
                if (valueMatcher != null){
                    valueMatcher.describeTo(description);
                }
            }

            protected boolean matchesSafely(Map<K, V> map, Description description) {
                if (map.size() != 1){
                    description.appendText("was a map with " + map.size() + " entries");
                    return false;
                }
                Map.Entry<K, V> entry = map.entrySet().iterator().next();
                if (keyMatcher != null && !keyMatcher.matches(entry.getKey())){
                    keyMatcher.describeMismatch(map, description);
                    return false;
                }
                if (valueMatcher != null && !valueMatcher.matches(entry.getValue())){
                    valueMatcher.describeMismatch(map, description);
                    return false;
                }
                return true;
            }
        };
    }

    public static Matcher<? super Map.Entry<String,Collection<String>>[]> hasLength(int i) {
        return new TypeSafeDiagnosingMatcher<Map.Entry<String, Collection<String>>[]>() {
            protected boolean matchesSafely(Map.Entry<String, Collection<String>>[] entries, Description description) {
                return entries.length == i;
            }

            public void describeTo(Description description) {
                description.appendText("an array with length " + i);
            }
        };
    }

}
