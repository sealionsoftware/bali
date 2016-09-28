package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.AssemblyEngine;
import com.sealionsoftware.bali.compiler.CompilationException;
import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import org.junit.Test;

import java.util.Collection;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MultithreadedAssemblyEngineTest {

    private CompilationThreadManager monitor = mock(CompilationThreadManager.class);
    private AssemblerSetFactory assemblerSetFactory = mock(AssemblerSetFactory.class);
    private AssemblyEngine subject = new MultithreadedAssemblyEngine(monitor, assemblerSetFactory);

    @Test
    public void testAssemble() throws Exception {

        ValidatingVisitor visitor = mock(ValidatingVisitor.class);
        when(assemblerSetFactory.assemblers(emptyMap())).thenReturn(asList(visitor));

        CodeBlockNode node = mock(CodeBlockNode.class);
        subject.assemble(node);

    }

    @Test(expected = RuntimeException.class)
    public void testAssembleWithUnexplainedBlockages() throws Exception {

        BlockageDescription blockage = mock(BlockageDescription.class);

        when(monitor.getBlockages()).thenReturn(asList(blockage));

        CodeBlockNode node = mock(CodeBlockNode.class);
        subject.assemble(node);
    }

    @Test(expected = CompilationException.class)
    public void testAssembleAndThrowCompileException() throws Exception {

        CompileError error = mock(CompileError.class);
        ValidatingVisitor visitor = mock(ValidatingVisitor.class);
        when(assemblerSetFactory.assemblers(emptyMap())).thenReturn(asList(visitor));
        when(visitor.getFailures()).thenReturn(asList(error));

        doAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            Collection<AssemblyTask> tasks = (Collection<AssemblyTask>) invocation.getArguments()[0];
            for (AssemblyTask task : tasks) {
                task.runnable.run();
            }
            return null;
        }).when(monitor).run(any());


        CodeBlockNode node = mock(CodeBlockNode.class);
        subject.assemble(node);

    }
}