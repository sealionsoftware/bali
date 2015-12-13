package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.Class;
import com.sealionsoftware.bali.compiler.ClassBasedType;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.Control;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;

import java.util.Map;

public class TypeAssigningVisitor extends ValidatingVisitor {

    private final Map<String, Class> library;

    public TypeAssigningVisitor(Map<String, Class> library) {
        this.library = library;
    }

    public void visit(BooleanLiteralNode node, Control control) {
        node.setType(new ClassBasedType(library.get(bali.Boolean.class.getName())));
    }

    public void visit(TextLiteralNode node, Control control) {
        node.setType(new ClassBasedType(library.get(bali.Text.class.getName())));
    }

    public void visit(TypeNode node, Control control) {
        node.setResolvedType(new ClassBasedType(library.get("bali." + node.getName())));
        control.visitChildren();
    }

}
