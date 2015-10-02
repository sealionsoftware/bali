package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.AssemblyEngine;
import com.sealionsoftware.bali.compiler.CompilationException;
import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MultithreadedAssemblyEngineTest {

    private AssemblerSetFactory assemblerSetFactory = mock(AssemblerSetFactory.class);
    private AssemblyEngine subject = new MultithreadedAssemblyEngine(assemblerSetFactory);

    @Test
    public void testAssemble() throws Exception {

        ValidatingVisitor visitor = mock(ValidatingVisitor.class);
        when(assemblerSetFactory.assemblers()).thenReturn(asList(visitor));

        CodeBlockNode node = mock(CodeBlockNode.class);
        subject.assemble(node);

    }

    @Test(expected = CompilationException.class)
    public void testAssembleAndThrow() throws Exception {

        CompileError error = mock(CompileError.class);
        ValidatingVisitor visitor = mock(ValidatingVisitor.class);
        when(assemblerSetFactory.assemblers()).thenReturn(asList(visitor));
        when(visitor.getFailures()).thenReturn(asList(error));

        CodeBlockNode node = mock(CodeBlockNode.class);

        subject.assemble(node);
    }
}