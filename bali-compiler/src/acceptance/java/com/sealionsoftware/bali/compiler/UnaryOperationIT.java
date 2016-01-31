package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import java.util.concurrent.Callable;

import static bali.logic.Primitive.convert;
import static bali.number.Primitive.convert;
import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.bali.compiler.Matchers.containingError;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class UnaryOperationIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testValidOperator() {

        Object output = interpreter.evaluate("!true");
        assertThat(output, equalTo(convert(false)));
    }

    @Test
    public void testChainedOperators() {

        Object output = interpreter.evaluate("| - 1");
        assertThat(output, equalTo(convert(1)));
    }

    @Test
    public void testInvalidOperator() {
        Callable invocation = () -> interpreter.evaluate("& 1");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.OPERATOR_NOT_FOUND))));
    }

    @Test
    public void testInvalidReturnType() {
        Callable invocation = () -> interpreter.run("var Logic ret = ++1");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testUnaryAndBinaryOperators() {

        Object output = interpreter.evaluate("1 + - 1");
        assertThat(output, equalTo(convert(0)));
    }


}
