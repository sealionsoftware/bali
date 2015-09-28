package com.sealionsoftware.bali.compiler;

import java.util.List;

public interface Type {

    Boolean isAssignableTo(Type other);

    String getClassName();

    Class getTemplate();

    Type getSuperType();

    List<Type> getInterfaces();

    List<Parameter> getTypeArguments();
}
