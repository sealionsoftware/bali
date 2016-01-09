package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.IntegerLiteralNode;
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

    public void visit(BooleanLiteralNode node) {
        node.setType(new ClassBasedType(library.get(bali.Boolean.class.getName())));
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
