package com.sealionsoftware.bali.compiler;

import java.util.List;

public class Operator extends Method {

    private String symbol;

    public Operator(String name, Type returnType, List<Parameter> parameters, String symbol, Method templateMethod) {
        super(name, returnType, parameters, templateMethod);
        this.symbol = symbol;
    }

    public Operator(String name, Type returnType, List<Parameter> parameters, String symbol) {
        super(name, returnType, parameters);
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
