package com.sealionsoftware;

import com.sealionsoftware.bali.compiler.CompilationException;
import com.sealionsoftware.bali.compiler.CompileError;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasItem;

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

    public static Matcher<Runnable> throwsException(Matcher<? extends Exception> exceptionMatcher) {
        return new TypeSafeDiagnosingMatcher<Runnable>() {
            protected boolean matchesSafely(Runnable runnable, Description description) {
                try {
                    runnable.run();
                } catch (Exception e) {
                    if (exceptionMatcher.matches(e)) return true;
                    exceptionMatcher.describeMismatch(e, description);
                    return false;
                } finally {
                    description.appendText("the exception was not thrown");
                }
                return false;
            }

            public void describeTo(Description description) {
                description.appendText("throws " + exceptionMatcher);
            }
        };
    }

    public static Matcher<CompilationException> containingError(Matcher<CompileError> errorMatcher) {
        return new TypeSafeDiagnosingMatcher<CompilationException>() {

            private final Matcher<? super Iterable<CompileError>> listMatcher = hasItem(errorMatcher);

            protected boolean matchesSafely(CompilationException exception, Description description) {
                return listMatcher.matches(exception.errorList);
            }

            public void describeTo(Description description) {
                description.appendText("CompilationException containing error " + errorMatcher);
            }
        };
    }

}
