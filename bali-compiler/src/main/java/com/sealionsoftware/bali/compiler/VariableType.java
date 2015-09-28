package com.sealionsoftware.bali.compiler;

import java.util.Collections;
import java.util.List;

public class VariableType implements Type {

	private String name;
	private Type bound;

	public VariableType(String name, Type bound) {
		this.name = name;
		this.bound = bound;
	}

	public String getName() {
		return name;
	}

	public Boolean isAssignableTo(Type t) {
		if (t == this){
			return true;
		}
		if (t instanceof VariableType){
			VariableType vt = (VariableType) t;
			return name.equals(vt.getName());
		}
		if (bound == null){
			return true;
		}
		return bound.isAssignableTo(t);
	}

    public String getClassName() {
        return bound.getClassName();
    }

    public Type getSuperType() {
		if (bound == null){
			return null;
		}
		return bound.getSuperType();
	}

	public List<Parameter> getTypeArguments() {
		if (bound == null){
			return Collections.emptyList();
		}
		return bound.getTypeArguments();
	}

	public List<Type> getInterfaces() {
		if (bound == null){
			return Collections.emptyList();
		}
		return bound.getInterfaces();
	}


	public Class getTemplate() {
		if (bound == null){
			return null;
		}
		return bound.getTemplate();
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		if (bound != null){
			sb.append(bound).append(" ");
		}
		return sb.append(name).toString();
	}
}
