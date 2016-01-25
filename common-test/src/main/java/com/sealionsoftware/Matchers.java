package com.sealionsoftware;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.Map;
import java.util.concurrent.Callable;

public class Matchers {

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

    public static Matcher<Callable> throwsException(Matcher<? extends Exception> exceptionMatcher) {
        return new TypeSafeDiagnosingMatcher<Callable>() {
            protected boolean matchesSafely(Callable runnable, Description description) {
                try {
                    runnable.call();
                } catch (Exception e) {
                    if (exceptionMatcher.matches(e)) return true;
                    exceptionMatcher.describeMismatch(e, description);
                    return false;
                }
                description.appendText("the exception was not thrown");
                return false;
            }

            public void describeTo(Description description) {
                description.appendText("throws " + exceptionMatcher);
            }
        };
    }


}
