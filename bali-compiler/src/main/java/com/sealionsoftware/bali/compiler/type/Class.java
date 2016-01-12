package com.sealionsoftware.bali.compiler.type;

import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Operator;
import com.sealionsoftware.bali.compiler.Parameter;
import com.sealionsoftware.bali.compiler.Type;

import java.util.List;

public class Class {

    private final String className;

    private java.lang.Boolean initialised = false;
    private List<Parameter> typeParameters;
    private Type superType;
    private List<Type> interfaces;
    private List<Method> methods;
    private List<Operator> operators;


    Class(String className) {
        this.className = className;
    }

    void initialise(List<Parameter> typeParameters, Type superType, List<Type> interfaces, List<Method> methods, List<Operator> operators) {
        this.initialised = true;
        this.superType = superType;
        this.typeParameters = typeParameters;
        this.interfaces = interfaces;
        this.methods = methods;
        this.operators = operators;
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

    public List<Method> getMethods() {
        assertInitialised();
        return methods;
    }

    public List<Operator> getOperators() {
        assertInitialised();
        return operators;
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
