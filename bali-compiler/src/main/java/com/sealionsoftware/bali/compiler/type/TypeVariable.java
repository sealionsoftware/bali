package com.sealionsoftware.bali.compiler.type;

import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Operator;
import com.sealionsoftware.bali.compiler.Parameter;
import com.sealionsoftware.bali.compiler.Type;

import java.util.List;

import static java.util.Collections.emptyList;

public class TypeVariable implements Type {

    private final String name;
    private final Type bound;

    public TypeVariable(String name, Type bound) {
        this.name = name;
        this.bound = bound;
    }

    public Boolean isAssignableTo(Type other) {
        if (other == this){
            return true;
        }
        if (other instanceof TypeVariable){
            TypeVariable variable = (TypeVariable) other;
            return name.equals(variable.getName());
        }
        return (bound != null) && bound.isAssignableTo(other);
    }

    public String getClassName() {
        return (bound != null) ? bound.getClassName() : null;
    }

    public Class getTemplate() {
        return (bound != null) ? bound.getTemplate() : null;
    }

    public Type getSuperType() {
        return (bound != null) ? bound.getSuperType() : null;
    }

    public List<Type> getInterfaces() {
        return (bound != null) ? bound.getInterfaces() : emptyList();
    }

    public List<Parameter> getTypeArguments() {
        return (bound != null) ? bound.getTypeArguments() : emptyList();
    }

    public List<Method> getMethods() {
        return (bound != null) ? bound.getMethods() : emptyList();
    }

    public List<Operator> getOperators() {
        return (bound != null) ? bound.getOperators() : emptyList();
    }

    public List<Operator> getUnaryOperators() {
        return (bound != null) ? bound.getUnaryOperators() : emptyList();
    }

    public Method getMethod(String name) {
        return (bound != null) ? bound.getMethod(name) : null;
    }

    public Operator getOperator(String name) {
        return (bound != null) ? bound.getOperator(name) :null;
    }

    public Operator getUnaryOperator(String name) {
        return (bound != null) ? bound.getUnaryOperator(name) :null;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
