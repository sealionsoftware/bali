package com.sealionsoftware.bali.compiler.assembly;

import bali.Logic;
import com.sealionsoftware.bali.compiler.tree.IntegerLiteralNode;
import com.sealionsoftware.bali.compiler.tree.LogicLiteralNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.type.Class;
import com.sealionsoftware.bali.compiler.type.ClassBasedType;

import java.util.Map;

public class TypeAssigningVisitor extends ValidatingVisitor {

    private final Map<String, Class> library;

    public TypeAssigningVisitor(Map<String, Class> library) {
        this.library = library;
    }

    public void visit(LogicLiteralNode node) {
        node.setType(new ClassBasedType(library.get(Logic.class.getName())));
    }

    public void visit(TextLiteralNode node) {
        node.setType(new ClassBasedType(library.get(bali.Text.class.getName())));
    }

    public void visit(IntegerLiteralNode node) {
        node.setType(new ClassBasedType(library.get(bali.Integer.class.getName())));
    }

    public void visit(TypeNode node) {
        node.setResolvedType(new ClassBasedType(library.get("bali." + node.getName())));
        visitChildren(node);
    }

}
