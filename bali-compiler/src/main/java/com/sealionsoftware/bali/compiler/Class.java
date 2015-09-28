package com.sealionsoftware.bali.compiler;

import java.util.List;

public class Class {

    private final String className;

    private java.lang.Boolean initialised = false;
    private List<Parameter> typeParameters;
    private Type superType;
    private List<Type> interfaces;

    public Class(String className) {
        this.className = className;
    }

    void initialise(List<Parameter> typeParameters, Type superType, List<Type> interfaces) {
        this.initialised = true;
        this.superType = superType;
        this.typeParameters = typeParameters;
        this.interfaces = interfaces;
    }

    public List<Parameter> getTypeParameters(){
        assertInitialised();
        return typeParameters;
    }

    public Type getSuperType(){
        assertInitialised();
        return superType;
    }

    public List<Type> getInterfaces(){
        assertInitialised();
        return interfaces;
    }

    public String getClassName() {
        return className;
    }

    private void assertInitialised(){
        if (!initialised){
            throw new RuntimeException("Class has not been initialised");
        }
    }

    public String toString(){
        return className;
    }

}
