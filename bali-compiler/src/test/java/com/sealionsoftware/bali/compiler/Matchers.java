package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.assembly.BlockageDescription;
import com.sealionsoftware.bali.compiler.assembly.ValidatingVisitor;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.Collection;

import static org.hamcrest.Matchers.empty;
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
                return listMatcher.matches(exception.errorList);
            }

            public void describeTo(Description description) {
                description.appendText("CompilationException containing error " + errorMatcher);
            }
        };
    }

}
