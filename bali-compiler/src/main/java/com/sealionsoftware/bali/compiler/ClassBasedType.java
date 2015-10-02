package com.sealionsoftware.bali.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

public class ClassBasedType implements Type {

    private Class template;
    private List<Parameter> typeArguments;

    private Map<String, Type> typeArgumentMap;

    private boolean superTypeInitialised = false;
    private Type superType;

    private boolean interfacesInitialised = false;
    private List<Type> interfaces;

    public ClassBasedType(Class template) {
        this(template, emptyList());
    }

    public ClassBasedType(Class template, List<Parameter> typeArguments) {
        this.template = template;
        this.typeArguments = typeArguments;

        typeArgumentMap = new HashMap<>();
        for (Parameter argument : typeArguments){
            typeArgumentMap.put(argument.name, argument.type);
        }
    }

    public String getClassName() {
        return template.getClassName();
    }

    public Class getTemplate() {
        return template;
    }

    public Type getSuperType() {
        if (!superTypeInitialised){
            superTypeInitialised = true;
            superType = parameterise(template.getSuperType());
        }
        return superType;
    }

    public List<Type> getInterfaces() {
        if (!interfacesInitialised){
            interfacesInitialised = true;
            List<Type> interfaces = new ArrayList<>();
            for (Type rawInterface : template.getInterfaces()){
                interfaces.add(parameterise(rawInterface));
            }
            this.interfaces = interfaces;
        }
        return interfaces;
    }

    public List<Parameter> getTypeArguments() {
        return typeArguments;
    }

    public Boolean isAssignableTo(Type other) {

        if (other == null || other == this) {
            return true;
        }

        if (template.getClassName().equals(other.getClassName())) {
            for (Parameter argument : other.getTypeArguments()){
                Type typeArgument = typeArgumentMap.get(argument.name);
                if (!typeArgument.isAssignableTo(argument.type)) {
                    return false;
                }
            }
            return true;
        }

        Type superType = getSuperType();
        if (superType != null && superType.isAssignableTo(other)) {
            return true;
        }

        for (Type iface : getInterfaces()) if (iface.isAssignableTo(other)) {
            return true;
        }

        return false;
    }

    private Type parameterise(Type raw){

        if (raw == null){
            return null;
        }

        List<Parameter> arguments = raw.getTypeArguments();

        if (arguments.isEmpty()){
            return raw;
        }

        List<Parameter> newArguments = new ArrayList<>(arguments.size());
        for (Parameter argument : arguments){
            newArguments.add(new Parameter(argument.name, typeArgumentMap.get(argument.name)));
        }

        return new ClassBasedType(raw.getTemplate(), newArguments);
    }

    public String toString() {

        StringBuilder builder = new StringBuilder(template.getClassName());
        if (!typeArguments.isEmpty()){
            builder.append("<");
            Iterator<Parameter> i = typeArguments.iterator();
            builder.append(i.next().type);
            while (i.hasNext()){
                builder.append(", ").append(i.next().type);
            }
            builder.append(">");
        }
        return builder.toString();
    }
}
