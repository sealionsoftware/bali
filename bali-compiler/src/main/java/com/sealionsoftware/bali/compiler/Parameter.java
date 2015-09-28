package com.sealionsoftware.bali.compiler;

public class Parameter {

    public final String name;
    public final Type type;

    public Parameter(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String toString() {
        return type + " " + name;
    }
}
