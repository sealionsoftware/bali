package com.sealionsoftware.bali.compiler;

import bali.Logic;
import bali.collection.Array;
import bali.number.Int;
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

        assertThat(output, hasEntry("aVariable", Logic.TRUE));
    }

    @Test
    public void testScriptContainingTypedVariable() {

        Map<String, Object> output = interpreter.run("var Logic aVariable = true");

        assertThat(output, hasEntry("aVariable", Logic.TRUE));
    }

    @Test
    public void testScriptContainingIncorrectlyTypedVariable() {

        Callable invocation = () -> interpreter.run("var Text aVariable = true");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testScriptContainingUnknownTypedVariable() {

        Callable invocation = () -> interpreter.run("var Foo aVariable = true");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.UNKNOWN_TYPE))));
    }

    @Test
    public void testScriptContainingGenericTypedVariable() {

        Map<String, Object> output = interpreter.run("var Group[Logic] aVariable = [true]");

        assertThat(output, hasEntry("aVariable", new Array<>(Logic.TRUE)));
    }

    @Test
    public void testScriptContainingIncorrectGenericTypedArgumets() {

        Callable invocation = () -> interpreter.run("var Group[Logic] aVariable = [1]");

        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testScriptContainingSomeIncorrectGenericTypedVariables() {

        Callable invocation = () -> interpreter.run("var Group[Logic] aVariable = [true, 1]");

        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testScriptContainingUnboundedGenericTypedVariable() {

        Map<String, Object> output = interpreter.run("var Group aVariable = [true, 1]");

        assertThat(output, hasEntry("aVariable", new Array<>(Logic.TRUE, new Int(1))));
    }


}
