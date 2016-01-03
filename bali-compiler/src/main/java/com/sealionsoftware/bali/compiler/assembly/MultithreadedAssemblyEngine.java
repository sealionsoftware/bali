package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.AssemblyEngine;
import com.sealionsoftware.bali.compiler.CompilationException;
import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.synchronizedList;

public class MultithreadedAssemblyEngine implements AssemblyEngine {

    private final AssemblerSetFactory factory;
    private final CompilationThreadManager monitor;

    public MultithreadedAssemblyEngine(CompilationThreadManager monitor, AssemblerSetFactory factory) {
        this.monitor = monitor;
        this.factory = factory;
    }

    public void assemble(CodeBlockNode fragment) {

        final List<CompileError> validationFailures = synchronizedList(new ArrayList<>());
        List<NamedRunnable> validationTasks = new ArrayList<>();

        for (final ValidatingVisitor assembler : factory.assemblers()){
            validationTasks.add(new NamedRunnable() {
                public String getName() {
                    return assembler.getClass().getSimpleName();
                }

                public void run() {
                    try {
                        fragment.accept(assembler);
                    } finally {
                        validationFailures.addAll(assembler.getFailures());
                        monitor.deregisterThread();
                    }
                }
            });
        }

        monitor.run(validationTasks);

        if (!validationFailures.isEmpty()){
            throw new CompilationException(validationFailures);
        }

        Collection<BlockageDescription> blockages = monitor.getBlockages();
        if (!blockages.isEmpty()){
            throw new RuntimeException(blockages.toString());
        }
    }
}
