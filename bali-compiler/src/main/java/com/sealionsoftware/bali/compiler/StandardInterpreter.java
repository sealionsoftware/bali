package com.sealionsoftware.bali.compiler;

import bali.Writer;
import bali.command.Console;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.assembly.InterpreterAssemblySetFactory;
import com.sealionsoftware.bali.compiler.assembly.MultithreadedAssemblyEngine;
import com.sealionsoftware.bali.compiler.bytecode.ASMBytecodeEngine;
import com.sealionsoftware.bali.compiler.execution.ReflectiveExecutor;
import com.sealionsoftware.bali.compiler.parser.ANTLRParseEngine;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StandardInterpreter implements Interpreter {

    private ParseEngine parseEngine;
    private AssemblyEngine assemblyEngine;
    private BytecodeEngine bytecodeEngine;
    private Executor executor;

    public StandardInterpreter() {
        this(null, null, null, null);
    }

    public StandardInterpreter(ParseEngine parseEngine, AssemblyEngine assemblyEngine, BytecodeEngine bytecodeEngine, Executor executor) {
        CompilationThreadManager monitor = new CompilationThreadManager();

        this.parseEngine = parseEngine != null ? parseEngine : new ANTLRParseEngine(monitor);
        this.assemblyEngine = assemblyEngine != null ? assemblyEngine : new MultithreadedAssemblyEngine(
                monitor,
                new InterpreterAssemblySetFactory()
        );
        this.bytecodeEngine = bytecodeEngine != null ? bytecodeEngine : new ASMBytecodeEngine();
        this.executor = executor != null ? executor : new ReflectiveExecutor(new Console());
    }

    public void run(String fragment) {
        CodeBlockNode codeBlockNode = parseEngine.parseFragment(fragment);
        Map<String, java.lang.Class> externalScope = new HashMap<>();
        externalScope.put("console", Writer.class);
        assemblyEngine.assemble(codeBlockNode, externalScope);
        GeneratedPackage generatedPackage = bytecodeEngine.generate(codeBlockNode);
        executor.executeFragment(generatedPackage);
    }

    public Object evaluate(String expression) {
        ExpressionNode expressionNode = parseEngine.parseExpression(expression);
        assemblyEngine.assemble(expressionNode);
        GeneratedPackage generatedPackage = bytecodeEngine.generate(expressionNode);
        return executor.executeExpression(generatedPackage);
    }

    public static void main(String... args) {

        Interpreter interpreter = new StandardInterpreter();

        Scanner scanner = new Scanner(System.in);
        while (true){

            System.out.println("Enter code to run:");

            String input = scanner.nextLine();
            if ("exit".compareToIgnoreCase(input.trim()) == 0){
                break;
            }

            try {
                interpreter.run(input);

                System.out.println("Compilation successful");

            } catch (Exception e){
                System.out.println(e.toString());
            }

            System.out.println();
        }
    }
}
