package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.AssemblyEngine;
import com.sealionsoftware.bali.compiler.CompilationException;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;

public class MultithreadedAssemblyEngine implements AssemblyEngine {

    private final AssemblerSetFactory factory;

    public MultithreadedAssemblyEngine(AssemblerSetFactory factory) {
        this.factory = factory;
    }

    public void assemble(CodeBlockNode fragment) {
        for (ValidatingVisitor assembler : factory.assemblers()){
            fragment.accept(assembler);
            if (!assembler.getFailures().isEmpty()){
                throw new CompilationException(assembler.getFailures());
            }
        }
    }
}
