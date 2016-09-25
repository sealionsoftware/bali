package com.sealionsoftware;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

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

    public static Matcher<Runnable> throwsException(Matcher<? extends Exception> exceptionMatcher) {
        return new TypeSafeDiagnosingMatcher<Runnable>() {
            protected boolean matchesSafely(Runnable runnable, Description description) {
                try {
                    runnable.run();
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

    public static Matcher<Exception> withMessage(String message) {
        return withMessage(equalTo(message));
    }

    public static Matcher<Exception> withMessage(Matcher<String> messageMatcher) {
        return new TypeSafeDiagnosingMatcher<Exception>() {
            protected boolean matchesSafely(Exception runnable, Description description) {
                description.appendText("the exception was not thrown");
                return messageMatcher.matches(runnable.getMessage());
            }

            public void describeTo(Description description) {
                description.appendText("with message ");
                messageMatcher.describeTo(description);
            }
        };
    }


}
