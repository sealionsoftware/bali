package com.sealionsoftware.bali.compiler.assembly;

import java.util.HashMap;
import java.util.Map;

public class Scope {

    private Map<String, ReferenceData> declarations = new HashMap<>();

    public Scope(){}

    public void add(ReferenceData vd) {
        declarations.put(vd.name, vd);
    }

    public ReferenceData find(String name) {
        return declarations.get(name);
    }

    public Boolean contains(String name) {
        return declarations.containsKey(name);
    }
}
