package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.assembly.InterpreterAssemblySetFactory;
import com.sealionsoftware.bali.compiler.assembly.MultithreadedAssemblyEngine;
import com.sealionsoftware.bali.compiler.bytecode.ASMBytecodeEngine;
import com.sealionsoftware.bali.compiler.execution.ReflectiveExecutor;
import com.sealionsoftware.bali.compiler.parser.ANTLRParseEngine;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;

import java.util.Map;
import java.util.Scanner;

public class StandardInterpreter implements Interpreter {

    private ParseEngine parseEngine;
    private AssemblyEngine assemblyEngine;
    private BytecodeEngine bytecodeEngine;
    private Executor executor;

    public StandardInterpreter() {
        CompilationThreadManager monitor = new CompilationThreadManager();
        this.parseEngine = new ANTLRParseEngine(monitor);
        this.assemblyEngine = new MultithreadedAssemblyEngine(
                monitor,
                new InterpreterAssemblySetFactory()
        );
        this.bytecodeEngine = new ASMBytecodeEngine();
        this.executor =  new ReflectiveExecutor();
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
//        for (GeneratedClass clazz : generatedPackage.getClasses()){
//            try (FileOutputStream fos = new FileOutputStream(new File(clazz.getName() + ".class"))) {
//                fos.write(clazz.getCode());
//            } catch (IOException ignore) {
//            }
//        }
        return executor.execute(generatedPackage);
    }

    public static void main(String... args) throws Exception {

        Interpreter interpreter = new StandardInterpreter();

        Scanner scanner = new Scanner(System.in);
        while (true){

            System.out.println("Enter code to run:");

            String input = scanner.nextLine();
            if ("exit".compareToIgnoreCase(input.trim()) == 0){
                break;
            }

            try {
                Map<String, Object> output = interpreter.run(input);

                System.out.println("Compilation successful");

                for (Map.Entry<String, Object> entry : output.entrySet() ){
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }

            } catch (Exception e){
                System.out.println(e.toString());
            }

            System.out.println();
        }
    }
}
