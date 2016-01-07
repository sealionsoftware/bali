package com.sealionsoftware.bali.compiler;

import java.util.Iterator;
import java.util.List;

public class Method {

    private Type returnType;
    private String name;
	private List<Parameter> parameters;

    private Method templateMethod;

    public Method(String name, Type returnType, List<Parameter> parameters, Method templateMethod) {
        this.returnType = returnType;
        this.name = name;
        this.parameters = parameters;
        this.templateMethod = templateMethod;
    }

    public Method(String name, Type returnType, List<Parameter> parameters) {
        this(name, returnType, parameters, null);
	}

    public Type getReturnType() {
        return returnType;
    }

    public String getName() {
        return name;
    }

    public List<Parameter> getParameters() {
		return parameters;
	}

    public Method getTemplateMethod() {
        return templateMethod;
    }

    public String toString(){
		StringBuilder sb = new StringBuilder(name);
		if (parameters.size() > 0){
			sb.append("(");
			Iterator<Parameter> i = parameters.iterator();
			sb.append(i.next());
			while (i.hasNext()){
				sb.append(", ").append(i.next());
			}
			sb.append(")");
		}
		return sb.toString();
	}
}
