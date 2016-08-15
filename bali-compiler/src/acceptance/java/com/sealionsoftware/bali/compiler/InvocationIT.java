package com.sealionsoftware.bali.compiler;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.Callable;

import static bali.logic.Primitive.convert;
import static bali.number.Primitive.convert;
import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.bali.compiler.Matchers.containingError;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class InvocationIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testInvokingValidMethod() {

        Object output = interpreter.evaluate("\"Hello World\".size()");
        assertThat(output, equalTo(convert(11)));
    }

    @Test
    public void testChainedInvocations() {

        Object output = interpreter.evaluate("\"Hello World\".uppercase().size()");
        assertThat(output, equalTo(convert(11)));
    }

    @Test
    public void testInvokingMethodWithParameter() {

        Object output = interpreter.evaluate("\"Hello World\".contains(\"W\"#1) ");
        assertThat(output, equalTo(convert(true)));
    }

    @Test
    public void testInvokingMethodWithInvalidArgumentType() {

        Callable invocation = () -> interpreter.evaluate("\"Hello World\".contains(42)");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testInvokingValidMethodWithInvalidNumberOfArguments() {

        Callable invocation = () -> interpreter.evaluate("\"Hello World\".contains()");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_ARGUMENT_LIST))));
    }

    @Test @Ignore("Awaiting implementation of types with void methods")
    public void testInvokingValidVoidMethod() {

        Object output = interpreter.evaluate("CONSOLE.aVoidMethod()");
        assertThat(output, nullValue());
    }

    @Test
    public void testAssigningInvalidReturnType() {
        Callable invocation = () -> interpreter.run("var Logic size = \"Hello World\".size()");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testInvokingInvalidMethod() {
        Callable invocation = () -> interpreter.evaluate("\"Hello World\".notAMethod()");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.METHOD_NOT_FOUND))));
    }

}
