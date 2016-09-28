package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.Site;

public abstract class ReferenceData {

    public final String name;
    public final Site type;

    public ReferenceData(String name, Site type) {
        this.name = name;
        this.type = type;
    }

}
