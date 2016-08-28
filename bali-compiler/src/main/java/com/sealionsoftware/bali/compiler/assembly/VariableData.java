package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.Site;

import java.util.UUID;

public class VariableData {

    public final String name;
    public final Site type;
    public final UUID id;

    public VariableData(String name, Site type, UUID id) {
        this.name = name;
        this.type = type;
        this.id = id;
    }

}
