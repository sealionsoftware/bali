package com.sealionsoftware.bali.compiler;

import bali.Boolean;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.Callable;

import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.bali.compiler.Matchers.containingError;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;

public class VariableDeclarationIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testScriptContainingVariable() {

        Map<String, Object> output = interpreter.run("var aVariable = true");

        assertThat(output, hasEntry("aVariable", Boolean.TRUE));
    }

    @Test
    public void testScriptContainingTypedVariable() {

        Map<String, Object> output = interpreter.run("var Boolean aVariable = true");

        assertThat(output, hasEntry("aVariable", Boolean.TRUE));
    }

    @Test
    public void testScriptContainingIncorrectlyTypedVariable() {

        Callable invocation = () -> interpreter.run("var Text aVariable = true");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

}
