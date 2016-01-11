package com.sealionsoftware.bali.compiler;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;

import static bali.logic.Primitive.convert;
import static bali.number.Primitive.convert;
import static com.sealionsoftware.Matchers.containingError;
import static com.sealionsoftware.Matchers.isEmptyMap;
import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;

public class InvocationIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testInvokingValidMethod() {

        Map<String, Object> output = interpreter.run("var size = \"Hello World\".size()");
        assertThat(output, hasEntry("size", convert(11)));
    }

    @Test
    public void testChainedInvocations() {

        Map<String, Object> output = interpreter.run("var size = \"Hello World\".uppercase().size()");
        assertThat(output, hasEntry("size", convert(11)));
    }

    @Test
    public void testInvokingMethodWithParameter() {

         Map<String, Object> output = interpreter.run(
                "var Character char = \"W\".get(0) " +
                "var contains = \"Hello World\".contains(char) "
        );
        assertThat(output, hasEntry("contains", convert(true)));
    }

    @Test
    public void testInvokingMethodWithInvalidArgumentType() {

        Runnable invocation = () -> interpreter.run("var contains = \"Hello World\".contains(42)");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testInvokingValidMethodWithInvalidNumberOfArguments() {

        Runnable invocation = () -> interpreter.run("var contains = \"Hello World\".contains()");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_ARGUMENT_LIST))));
    }

    @Test @Ignore("Awaiting implementation of types with void methods")
    public void testInvokingValidVoidMethod() {

        Map<String, Object> output = interpreter.run("CONSOLE.aVoidMethod()");
        assertThat(output, isEmptyMap());
    }

    @Test
    public void testAssigningInvalidReturnType() {
        Runnable invocation = () -> interpreter.run("var Boolean size = \"Hello World\".size()");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testInvokingInvalidMethod() {
        Runnable invocation = () -> interpreter.run("\"Hello World\".notAMethod()");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.METHOD_NOT_FOUND))));
    }

}