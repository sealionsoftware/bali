package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import java.util.Collections;

import static com.sealionsoftware.Matchers.containsOneValue;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.junit.Assert.assertThat;

public class CompilationExceptionTest {

    private CompileError error = new CompileError(ErrorCode.UNKNOWN, null);
    private CompilationException subject = new CompilationException(Collections.singletonList(error));

    @Test
    public void testList() throws Exception {
        assertThat(subject.errorList, containsOneValue(withCode(ErrorCode.UNKNOWN)));
    }
}