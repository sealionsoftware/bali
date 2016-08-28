package com.sealionsoftware.bali.compiler;

import java.util.List;

public class Operator extends Method {

    private String symbol;

    public Operator(String name, Site returnType, List<Parameter> parameters, String symbol, Method templateMethod) {
        super(name, returnType, parameters, templateMethod);
        this.symbol = symbol;
    }

    public Operator(String name, Site returnType, List<Parameter> parameters, String symbol) {
        this(name, returnType, parameters, symbol, null);
    }

    public String getSymbol() {
        return symbol;
    }
}
