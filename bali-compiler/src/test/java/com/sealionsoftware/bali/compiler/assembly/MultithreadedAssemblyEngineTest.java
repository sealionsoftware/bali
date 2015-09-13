package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.AssemblyEngine;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class MultithreadedAssemblyEngineTest {

    private AssemblyEngine subject = new MultithreadedAssemblyEngine();

    @Test
    public void testAssemble() throws Exception {

        CodeBlockNode node = mock(CodeBlockNode.class);
        subject.assemble(node);

    }
}