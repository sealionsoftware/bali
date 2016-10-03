package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.bali.compiler.Matchers.containingError;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.hamcrest.MatcherAssert.assertThat;

public class VariableDeclarationIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testScriptContainingVariable() {

        interpreter.run("var aVariable = true");

    }

    @Test
    public void testScriptContainingTypedVariable() {

        interpreter.run(
                "var Text aVariable = \"text\" " +
                "console << aVariable"
        );
    }

    @Test
    public void testScriptContainingIncorrectlyTypedVariable() {

        Runnable invocation = () -> interpreter.run("var Text aVariable = true");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testScriptContainingUnknownTypedVariable() {

        Runnable invocation = () -> interpreter.run("var Foo aVariable = true");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.UNKNOWN_TYPE))));
    }

    @Test
    public void testScriptContainingGenericTypedVariable() {

        interpreter.run("var Group[Logic] aVariable = [true]");

    }

    @Test
    public void testScriptContainingIncorrectGenericTypedArguments() {

        Runnable invocation = () -> interpreter.run("var Group[Logic] aVariable = [1]");

        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testScriptContainingSomeIncorrectGenericTypedVariables() {

        Runnable invocation = () -> interpreter.run("var Group[Logic] aVariable = [true, 1]");

        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testScriptContainingUnboundedGenericTypedVariable() {

        interpreter.run("var Group aVariable = [true, 1]");

    }

    @Test
    public void testDeclaringRequiredVariableWithoutValue() {

        Runnable invocation = () -> interpreter.run("var Text aVariable");

        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.VALUE_REQUIRED))));
    }

    @Test
    public void testDeclaringOptionalVariable() {

        interpreter.run("var Text? aVariable");

    }

    @Test
    public void testDeclaringTwoVariablesWithSameName() {

        Runnable invocation = () -> interpreter.run(
                "var aVariable = \"X\"" +
                "var aVariable = \"Y\""
        );

        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.NAME_ALREADY_USED))));

    }


}
