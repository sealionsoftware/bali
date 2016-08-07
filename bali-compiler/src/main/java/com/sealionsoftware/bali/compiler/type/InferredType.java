package com.sealionsoftware.bali.compiler.type;

import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Operator;
import com.sealionsoftware.bali.compiler.Parameter;
import com.sealionsoftware.bali.compiler.Type;

import java.util.List;

import static java.util.Collections.emptyList;

public class InferredType implements Type {
    
    private Type inferred;
    private final Type bound;

    public InferredType(Type bound) {
        this.bound = bound;
    }

    public Boolean isAssignableTo(Type other) {
        inferred = other;
        return other == this || bound == null || bound.isAssignableTo(other);
    }

    public String getClassName() {
        return (inferred != null) ? inferred.getClassName() : null;
    }

    public Class getTemplate() {
        return (inferred != null) ? inferred.getTemplate() : null;
    }

    public Type getSuperType() {
        return (inferred != null) ? inferred.getSuperType() : null;
    }

    public List<Type> getInterfaces() {
        return (inferred != null) ? inferred.getInterfaces() : emptyList();
    }

    public List<Parameter> getTypeArguments() {
        return (inferred != null) ? inferred.getTypeArguments() : emptyList();
    }

    public List<Method> getMethods() {
        return (inferred != null) ? inferred.getMethods() : emptyList();
    }

    public List<Operator> getOperators() {
        return (inferred != null) ? inferred.getOperators() : emptyList();
    }

    public List<Operator> getUnaryOperators() {
        return (inferred != null) ? inferred.getUnaryOperators() : emptyList();
    }

    public Method getMethod(String name) {
        return (inferred != null) ? inferred.getMethod(name) : null;
    }

    public Operator getOperator(String name) {
        return (inferred != null) ? inferred.getOperator(name) :null;
    }

    public Operator getUnaryOperator(String name) {
        return (inferred != null) ? inferred.getUnaryOperator(name) :null;
    }
    
}
