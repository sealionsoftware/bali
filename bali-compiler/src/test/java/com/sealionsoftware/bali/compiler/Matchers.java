package com.sealionsoftware.bali.compiler;

import bali.Iterator;
import bali.Logic;
import bali.RuntimeException;
import bali.Text;
import bali.collection.Array;
import com.sealionsoftware.bali.compiler.assembly.BlockageDescription;
import com.sealionsoftware.bali.compiler.assembly.ValidatingVisitor;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static bali.number.Primitive.convert;
import static bali.text.Primitive.convert;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class Matchers {

    public static Matcher<ValidatingVisitor> containsNoFailures(){
        return new TypeSafeDiagnosingMatcher<ValidatingVisitor>(){

            private Matcher<Collection<?>> failureMatcher = empty();

            protected boolean matchesSafely(ValidatingVisitor visitor, Description description) {
                return failureMatcher.matches(visitor.getFailures()) || description.appendText("had failures " + visitor.getFailures()) == null;
            }

            public void describeTo(Description description) {
                description.appendText("Validator with an empty failure list");
            }
        };
    }

    public static Matcher<ValidatingVisitor> containsOneFailure(ErrorCode error){
        return new TypeSafeDiagnosingMatcher<ValidatingVisitor>(){

            protected boolean matchesSafely(ValidatingVisitor visitor, Description description) {
                return hasItem(withCode(error)).matches(visitor.getFailures()) || description.appendText("had failures " + visitor.getFailures()) == null;
            }

            public void describeTo(Description description) {
                description.appendText("validator with one failure of code " + error);
            }
        };
    }

    public static Matcher<CompileError> withCode(ErrorCode code){
        return new TypeSafeDiagnosingMatcher<CompileError>(){

            protected boolean matchesSafely(CompileError error, Description description) {
                return error.code.equals(code);
            }

            public void describeTo(Description description) {
                description.appendText("compile error with code " + code.name());
            }
        };
    }

    public static Matcher<BlockageDescription> blockageOnProperty(String propertyName) {
        return new TypeSafeDiagnosingMatcher<BlockageDescription>() {
            protected boolean matchesSafely(BlockageDescription blockageDescription, Description description) {
                return blockageDescription.propertyName.equals(propertyName) || description.appendText("had name " + blockageDescription.propertyName) == null;
            }

            public void describeTo(Description description) {
                description.appendText("a property with name " + propertyName);
            }
        };
    }

    public static Matcher<CompilationException> containingError(Matcher<CompileError> errorMatcher) {
        return new TypeSafeDiagnosingMatcher<CompilationException>() {

            private final Matcher<? super Iterable<CompileError>> listMatcher = hasItem(errorMatcher);

            protected boolean matchesSafely(CompilationException exception, Description description) {
                return listMatcher.matches(exception.errorList) || description.appendText("error received was " + exception.errorList) == null;
            }

            public void describeTo(Description description) {
                description.appendText("CompilationException containing error " + errorMatcher);
            }
        };
    }

    public static Matcher<RuntimeException> withPayload(final Matcher<?> payloadMatcher) {
        return new TypeSafeDiagnosingMatcher<RuntimeException>() {

            protected boolean matchesSafely(RuntimeException e, Description description) {
                return payloadMatcher.matches(e.payload);
            }

            public void describeTo(Description description) {
                description.appendText("RuntimeException with payload " );
                payloadMatcher.describeTo(description);
            }
        };
    }

    @SafeVarargs
    public static <T> Matcher<Array<? super T>> hasValues(final T... values){
        List<Matcher<T>> matchers = Arrays.stream(values).map(org.hamcrest.Matchers::equalTo).collect(toList());
        @SuppressWarnings("unchecked")
        Matcher<T>[] matcherArray = matchers.toArray(new Matcher[matchers.size()]);
        return hasValues(matcherArray);
    }

    @SafeVarargs
    public static <T> Matcher<Array<? super T>> hasValues(final Matcher<T>... matchers){
        return new TypeSafeDiagnosingMatcher<Array<? super T>>() {
            protected boolean matchesSafely(Array<? super T> anArray, Description description) {

                int expectedSize = matchers.length;

                if (convert(anArray.size()) != expectedSize) {
                    description.appendText("The array was not of size " + expectedSize);
                    return false;
                }

                Iterator<? super T> iterator = anArray.iterator();
                for (Matcher<T> matcher : matchers) {
                    Object next = iterator.next();
                    if (!matcher.matches(next)) {
                        matcher.describeMismatch(next, description);
                        return false;
                    }

                }
                return true;
            }

            public void describeTo(Description description) {
                description.appendText(Logic.FALSE.toString());
            }
        };

    }

    public static Matcher<Site> isSiteOfType(Type type) {
        return new TypeSafeDiagnosingMatcher<Site>() {
            protected boolean matchesSafely(Site site, Description description) {
                Type siteType = site.type;
                return type == siteType || type.isAssignableTo(siteType) || description.appendValue(siteType).appendText(" is not assignable") == null;
            }

            public void describeTo(Description description) {
                description.appendText("is assignable to " + type);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static Matcher<TextBuffer> wrote(String... statements){
        return wrote((Matcher[]) Arrays.stream(statements).map((line) -> equalTo(convert(line))).toArray(Matcher[]::new));
    }

    @SafeVarargs
    public static Matcher<TextBuffer> wrote(Matcher<Text>... statementMatchers){
        return new TypeSafeDiagnosingMatcher<TextBuffer>(){

            protected boolean matchesSafely(TextBuffer target, Description description) {
                List<Text> written = target.getWrittenLines();

                if (statementMatchers.length != written.size()){
                    description.appendText("Number of written statements was " + written.size());
                    return false;
                }

                java.util.Iterator<Text> i = target.getWrittenLines().iterator();
                for (Matcher<Text> matcher : statementMatchers) if (!matcher.matches(i.next())) {
                    matcher.describeMismatch(target, description);
                    return false;
                }

                return true;
            }

            public void describeTo(Description description) {
                description.appendText(" TextBuffer wrote lines matching ");
                for (Matcher<Text> statementMatcher : statementMatchers){
                    statementMatcher.describeTo(description);
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static Matcher<TextBuffer> wroteNothing(){
        return new TypeSafeDiagnosingMatcher<TextBuffer>(){

            protected boolean matchesSafely(TextBuffer target, Description description) {
                List<Text> written = target.getWrittenLines();
                return written.isEmpty() || description.appendText(written.toString()).appendText(" was written") == null;
            }

            public void describeTo(Description description) {
                description.appendText(" TextBuffer wrote no lines");
            }
        };
    }

}
