package com.sealionsoftware.bali.compiler.execution;

import bali.Writer;
import com.sealionsoftware.bali.compiler.Evaluation;
import com.sealionsoftware.bali.compiler.Executor;
import com.sealionsoftware.bali.compiler.Fragment;
import com.sealionsoftware.bali.compiler.GeneratedClass;
import com.sealionsoftware.bali.compiler.GeneratedPackage;
import com.sealionsoftware.bali.compiler.Interpreter;

import java.util.HashMap;
import java.util.Map;

public class ReflectiveExecutor implements Executor {

    private Writer console;

    public ReflectiveExecutor(Writer console) {
        this.console = console;
    }

    @SuppressWarnings("unchecked")
    public void executeFragment(GeneratedPackage generated) {
        execute(
                generated,
                Interpreter.FRAGMENT_CLASS_NAME,
                (clazz) -> {
                    ((Fragment) clazz.getConstructor(Writer.class).newInstance(console)).execute();
                    return null;
                });
    }

    public Object executeExpression(GeneratedPackage generated) {
        return execute(generated,
                Interpreter.EVALUATION_CLASS_NAME,
                (clazz) -> {
                    return ((Evaluation) clazz.newInstance()).evaluate();
                });
    }

    public <I extends Class, O> O execute(GeneratedPackage generated, String className, F<I, O> payload) {

        ClassLoader classLoader = buildClassLoader(generated);
        try {
            @SuppressWarnings("unchecked")
            I expressionClass = (I) classLoader.loadClass(className);
            return payload.apply(expressionClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private ClassLoader buildClassLoader(GeneratedPackage generated){
        String packageName = generated.getName();
        String qualifier = packageName != null && !packageName.isEmpty() ? packageName + "." : null;
        Map<String, byte[]> code = new HashMap<>();
        for (GeneratedClass clazz : generated.getClasses()){
            String name = clazz.getName();
            if (qualifier != null){
                name = qualifier + name;
            }
            code.put(name, clazz.getCode());
        }

        return new ByteArrayClassLoader(Thread.currentThread().getContextClassLoader(), code);
    }

    private interface F<I, O> {
        O apply(I i) throws Exception;
    }

}
