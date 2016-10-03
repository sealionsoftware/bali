package com.sealionsoftware.bali.compiler;

public class Parameter {

    public final String name;
    public final Site site;

    public Parameter(String name, Site site) {
        this.name = name;
        this.site = site;
    }

    public String toString() {
        return site != null ? site + " " + name : name;
    }
}
