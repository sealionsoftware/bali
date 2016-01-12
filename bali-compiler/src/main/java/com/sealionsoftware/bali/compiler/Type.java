package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.type.Class;

import java.util.List;

public interface Type {

    Boolean isAssignableTo(Type other);

    String getClassName();

    Class getTemplate();

    Type getSuperType();

    List<Type> getInterfaces();

    List<Parameter> getTypeArguments();

    List<Method> getMethods();

    List<Operator> getOperators();

    Method getMethod(String name);

    Operator getOperator(String name);
}
