package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.tree.Node;
import org.junit.Test;

import java.util.Collections;

import static com.sealionsoftware.Matchers.containingError;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class CompilationExceptionTest {

    private CompileError error = new CompileError(ErrorCode.UNKNOWN, mock(Node.class));
    private CompilationException subject = new CompilationException(Collections.singletonList(error));

    @Test
    public void testList() throws Exception {
        assertThat(subject, containingError(withCode(ErrorCode.UNKNOWN)));
    }
}