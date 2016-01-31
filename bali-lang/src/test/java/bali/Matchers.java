package bali;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class Matchers {

    public static Matcher<Logic> isTrue(){
        return new TypeSafeDiagnosingMatcher<Logic>() {
            protected boolean matchesSafely(Logic aBoolean, Description description) {
                return aBoolean == Logic.TRUE || description.appendText(aBoolean.toString()) == null;
            }

            public void describeTo(Description description) {
                description.appendText(Logic.TRUE.toString());
            }
        };

    }

    public static Matcher<Logic> isFalse(){
        return new TypeSafeDiagnosingMatcher<Logic>() {
            protected boolean matchesSafely(Logic aBoolean, Description description) {
                return aBoolean == Logic.FALSE || description.appendText(aBoolean.toString()) == null;
            }

            public void describeTo(Description description) {
                description.appendText(Logic.FALSE.toString());
            }
        };

    }


}
