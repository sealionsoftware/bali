package com.sealionsoftware.bali.compiler;

public class Site {

    public final Type type;
    public final Boolean isOptional;

    public Site(Type type) {
        this(type, null);
    }

    public Site(Type type, Boolean isOptional) {
        this.type = type;
        this.isOptional = Boolean.TRUE.equals(isOptional);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(type.toString());
        if (isOptional){
            sb.append("?");
        }
        return sb.toString();
    }

    public boolean isAssignableTo(Site other) {
        return (!isOptional || other.isOptional) && type.isAssignableTo(other.type);
    }
}
