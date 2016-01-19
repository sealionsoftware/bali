package com.sealionsoftware.bali.compiler.execution;

import java.util.Map;

public class ByteArrayClassLoader extends ClassLoader {

    public Map<String, byte[]> storedClassDefinitions;

    public ByteArrayClassLoader(ClassLoader parent, Map<String, byte[]> storedClassDefinitions) {
        super(parent);
        this.storedClassDefinitions = storedClassDefinitions;
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] code = storedClassDefinitions.get(name);
        if (code!= null){
            return defineClass(name, code, 0, code.length);
        }
        throw new ClassNotFoundException(name);
    }
}
