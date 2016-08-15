package com.sealionsoftware.bali.compiler.assembly;

import bali.Group;
import bali.Logic;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.tree.ArrayLiteralNode;
import com.sealionsoftware.bali.compiler.tree.IntegerLiteralNode;
import com.sealionsoftware.bali.compiler.tree.LogicLiteralNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.type.Class;
import com.sealionsoftware.bali.compiler.type.ClassBasedType;
import com.sealionsoftware.bali.compiler.type.InferredType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

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

    public void visit(ArrayLiteralNode node) {
        node.setType(new ClassBasedType(library.get(Group.class.getName()), asList(new InferredType(null))));
        visitChildren(node);
    }

    public void visit(TypeNode node) {
        visitChildren(node);
        List<Type> resolvedArgumentTypes = node.getArguments().stream().map(TypeNode::getResolvedType).collect(Collectors.toList());
        node.setResolvedType(new ClassBasedType(library.get("bali." + node.getName()), resolvedArgumentTypes));
    }

}
