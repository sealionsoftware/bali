package bali;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class Matchers {

    public static Matcher<Boolean> isTrue(){
        return new TypeSafeDiagnosingMatcher<Boolean>() {
            protected boolean matchesSafely(Boolean aBoolean, Description description) {
                return aBoolean == Boolean.TRUE || description.appendText(aBoolean.toString()) == null;
            }

            public void describeTo(Description description) {
                description.appendText(Boolean.TRUE.toString());
            }
        };

    }

    public static Matcher<Boolean> isFalse(){
        return new TypeSafeDiagnosingMatcher<Boolean>() {
            protected boolean matchesSafely(Boolean aBoolean, Description description) {
                return aBoolean == Boolean.FALSE || description.appendText(aBoolean.toString()) == null;
            }

            public void describeTo(Description description) {
                description.appendText(Boolean.FALSE.toString());
            }
        };

    }


}
