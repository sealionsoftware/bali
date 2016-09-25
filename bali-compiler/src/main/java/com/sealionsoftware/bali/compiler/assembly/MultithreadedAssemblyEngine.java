package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.AssemblyEngine;
import com.sealionsoftware.bali.compiler.CompilationException;
import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.tree.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.synchronizedList;
import static java.util.stream.Collectors.toList;

public class MultithreadedAssemblyEngine implements AssemblyEngine {

    private final AssemblerSetFactory factory;
    private final CompilationThreadManager monitor;

    public MultithreadedAssemblyEngine(CompilationThreadManager monitor, AssemblerSetFactory factory) {
        this.monitor = monitor;
        this.factory = factory;
    }

    public void assemble(Node fragment) {
        assemble(fragment, emptyMap());
    }

    public void assemble(Node fragment, Map<String, Class> externalReferences) {

        final List<CompileError> validationFailures = synchronizedList(new ArrayList<>());

        monitor.run(factory.assemblers(externalReferences).stream().map((assembler) -> new AssemblyTask(
                assembler.getClass().getSimpleName(),
                () -> {
                    try {
                        fragment.accept(assembler);
                    } finally {
                        validationFailures.addAll(assembler.getFailures());
                    }
                }
        )).collect(toList()));

        if (!validationFailures.isEmpty()){
            throw new CompilationException(validationFailures);
        }

        Collection<BlockageDescription> blockages = monitor.getBlockages();
        if (!blockages.isEmpty()){
            throw new RuntimeException(blockages.toString());
        }
    }

}
