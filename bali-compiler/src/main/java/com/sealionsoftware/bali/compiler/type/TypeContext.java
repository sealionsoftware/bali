package com.sealionsoftware.bali.compiler.type;

class TypeContext {

    TypeContext(String name) {
        this.name = name;
    }

    String name;
    TypeSignatureVisitor typeVisitor;

}
