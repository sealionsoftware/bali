package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.Callable;

import static bali.logic.Primitive.convert;
import static bali.number.Primitive.convert;
import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.bali.compiler.Matchers.containingError;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;

public class UnaryOperationIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testValidOperator() {

        Map<String, Object> output = interpreter.run("var ret = !true");
        assertThat(output, hasEntry("ret", convert(false)));
    }

    @Test
    public void testChainedOperators() {

        Map<String, Object> output = interpreter.run("var ret = | - 1");
        assertThat(output, hasEntry("ret", convert(1)));
    }

    @Test
    public void testInvalidOperator() {
        Callable invocation = () -> interpreter.run("var ret = & 1");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.OPERATOR_NOT_FOUND))));
    }

    @Test
    public void testInvalidReturnType() {
        Callable invocation = () -> interpreter.run("var Boolean ret = ++1");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testUnaryAndBinaryOperators() {

        Map<String, Object> output = interpreter.run("var ret = 1 + - 1");
        assertThat(output, hasEntry("ret", convert(0)));
    }


}
