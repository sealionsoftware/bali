package com.sealionsoftware.bali.compiler.type;

import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ClasspathClassFactory {

    private Map<String, Class> classLibrary;
    private ClassLoader loader;

    public ClasspathClassFactory(Map<String, Class> classLibrary, ClassLoader loader) {
        this.classLibrary = classLibrary;
        this.loader = loader;
    }

    public void addToLibrary(java.lang.Class clazz){
        get(clazz.getName());
    }

    Class get(String className){

        Class ret = classLibrary.get(className);
        if (ret != null) {
            return ret;
        }

        InputStream classFileStream = loader.getResourceAsStream(className.replace('.','/').concat(".class"));
        ClassReader reader;
        try {
            reader = new ClassReader(classFileStream);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read bytecode for class " + className, e);
        }

        ClassPathTypeBuilderVisitor visitor = new ClassPathTypeBuilderVisitor(this, className);
        ret = visitor.getClasspathClass();
        classLibrary.put(className, ret);
        reader.accept(visitor, ClassReader.SKIP_FRAMES);
        return ret;
    }

}
