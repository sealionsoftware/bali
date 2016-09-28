package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.Site;

import java.util.UUID;

public class VariableData extends ReferenceData {

    public final UUID id;

    public VariableData(String name, Site type, UUID id) {
        super(name, type);
        this.id = id;
    }

}
