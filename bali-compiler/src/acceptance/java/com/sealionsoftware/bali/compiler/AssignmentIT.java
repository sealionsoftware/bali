package com.sealionsoftware.bali.compiler;

import bali.Logic;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.Callable;

import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.bali.compiler.Matchers.containingError;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;

public class AssignmentIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testAssigningUntypedVariable() {

        Map<String, Object> output = interpreter.run("var aVariable = false aVariable = true");

        assertThat(output, notNullValue());
        assertThat(output, hasEntry("aVariable", Logic.TRUE));
    }

    @Test
    public void testAssigningTypedVariable() {

        Map<String, Object> output = interpreter.run("var Logic aVariable = false aVariable = true");

        assertThat(output, notNullValue());
        assertThat(output, hasEntry("aVariable", Logic.TRUE));
    }

    @Test
    public void testScriptContainingIncorrectlyTypedVariable() {

        Callable invocation = () -> interpreter.run("var Text aVariable = \"Hello World\" aVariable = true");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));

    }

}
