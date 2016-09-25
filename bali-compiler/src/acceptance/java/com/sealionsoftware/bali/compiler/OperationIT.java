package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import static bali.number.Primitive.convert;
import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.bali.compiler.Matchers.containingError;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class OperationIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testValidOperator() {

        Object output = interpreter.evaluate("1 + 1");
        assertThat(output, equalTo(convert(2)));
    }

    @Test
    public void testChainedOperators() {

        Object output = interpreter.evaluate("1 + 2 + 3 + 4 / 2");
        assertThat(output, equalTo(convert(8)));
    }

    @Test
    public void testInvalidOperator() {
        Runnable invocation = () -> interpreter.evaluate("1 & 1");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.OPERATOR_NOT_FOUND))));
    }

    @Test
    public void testOperatorWithInvalidArgumentType() {
        Runnable invocation = () -> interpreter.evaluate("1 + true");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testInvalidReturnType() {
        Runnable invocation = () -> interpreter.run("var Logic ret = 1 + 1");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testOperatorOnOptionalVariable() {
        Runnable invocation = () -> interpreter.run(
                "var Text? maybeText " +
                "maybeText + \" append\" ");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.CANNOT_INVOKE_ON_OPTIONAL))));
    }

}
