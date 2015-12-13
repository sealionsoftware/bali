package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.Type;

import java.util.UUID;

public class VariableData {

    public final String name;
    public final Type type;
    public final UUID id;

    public VariableData(String name, Type type, UUID id) {
        this.name = name;
        this.type = type;
        this.id = id;
    }

}
