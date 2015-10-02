package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.antlr.ANTLRParseEngine;
import com.sealionsoftware.bali.compiler.asm.ASMBytecodeEngine;
import com.sealionsoftware.bali.compiler.assembly.InterpreterAssemblySetFactory;
import com.sealionsoftware.bali.compiler.assembly.MultithreadedAssemblyEngine;
import com.sealionsoftware.bali.compiler.execution.ReflectiveExecutor;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;

import java.util.Map;

public class StandardInterpreter implements Interpreter {

    private ParseEngine parseEngine;
    private AssemblyEngine assemblyEngine;
    private BytecodeEngine bytecodeEngine;
    private Executor executor;

    public StandardInterpreter() {
        this(
                new ANTLRParseEngine(),
                new MultithreadedAssemblyEngine(
                        new InterpreterAssemblySetFactory()
                ),
                new ASMBytecodeEngine(),
                new ReflectiveExecutor()
        );
    }

    public StandardInterpreter(ParseEngine parseEngine, AssemblyEngine assemblyEngine, BytecodeEngine bytecodeEngine, Executor executor) {
        this.parseEngine = parseEngine;
        this.assemblyEngine = assemblyEngine;
        this.bytecodeEngine = bytecodeEngine;
        this.executor = executor;
    }

    public Map<String, Object> run(String fragment) {

        CodeBlockNode codeBlockNode = parseEngine.parse(fragment);
        assemblyEngine.assemble(codeBlockNode);
        GeneratedPackage generatedPackage = bytecodeEngine.generate(codeBlockNode);
        return executor.execute(generatedPackage);
    }
}
