package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.bali.compiler.Matchers.containingError;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.hamcrest.MatcherAssert.assertThat;

public class AssignmentIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testAssigningUntypedVariable() {
        interpreter.run(
                "var aVariable = false " +
                "aVariable = true "
        );
    }

    @Test
    public void testAssigningTypedVariable() {
       interpreter.run(
                "var Logic aVariable = false " +
                "aVariable = true"
       );
    }

    @Test
    public void testScriptContainingIncorrectlyTypedVariable() {

        Runnable invocation = () -> interpreter.run(
                "var Text aVariable = \"Hello World\" " +
                "aVariable = true");

        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testAssigningOptionalVariable() {
        interpreter.run(
                "var Logic? aVariable " +
                "aVariable = true"
        );
    }

    @Test
    public void testAssigningOptionalValueToRequiredVariable() {

        Runnable invocation = () -> interpreter.run(
                "var Text? aVariable " +
                "var Text anotherVariable = \"Hello\" " +
                "anotherVariable = aVariable");

        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

}
