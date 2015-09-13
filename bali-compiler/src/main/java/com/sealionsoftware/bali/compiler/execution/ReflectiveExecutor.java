package com.sealionsoftware.bali.compiler.execution;

import com.sealionsoftware.bali.compiler.Executor;
import com.sealionsoftware.bali.compiler.Fragment;
import com.sealionsoftware.bali.compiler.GeneratedClass;
import com.sealionsoftware.bali.compiler.GeneratedPackage;
import com.sealionsoftware.bali.compiler.Interpreter;

import java.util.HashMap;
import java.util.Map;

public class ReflectiveExecutor implements Executor {

    public Map<String, Object> execute(GeneratedPackage generated) {

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

        ClassLoader classLoader = new ByteArrayClassLoader(Thread.currentThread().getContextClassLoader(), code);

        Object instance;
        try {
            Class<?> fragmentClass = classLoader.loadClass(Interpreter.FRAGMENT_CLASS_NAME);
            instance  = fragmentClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!(instance instanceof Fragment)){
            throw new RuntimeException("Loaded object is not a Fragment");
        }

        Fragment fragment = (Fragment) instance;

        return fragment.execute();
    }
}
