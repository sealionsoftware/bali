package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.Class;
import com.sealionsoftware.bali.compiler.ClassBasedType;
import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;

import java.util.Map;

public class TypeCheckVisitor extends ValidatingVisitor {

    private final Map<String, Class> library;

    public TypeCheckVisitor(Map<String, Class> library) {
        this.library = library;
    }

    public void visit(VariableNode node) {
        TypeNode variableType = node.getType();
        Type valueType = node.getValue().getType();
        if (variableType != null && (valueType == null || !valueType.isAssignableTo(variableType.getResolvedType()))){
            failures.add(new CompileError(
                    ErrorCode.INVALID_TYPE,
                    node
            ));
        }
        visitChildren(node);
    }

    public void visit(AssignmentNode node) {
        Type targetType = node.getTarget().getVariableData().type;
        Type valueType =  node.getValue().getType();
        if (targetType != null && (valueType == null || !valueType.isAssignableTo(targetType))){
            failures.add(new CompileError(
                    ErrorCode.INVALID_TYPE,
                    node
            ));
        }
        visitChildren(node);
    }

    public void visit(ConditionalStatementNode node) {
        Type targetType = new ClassBasedType(library.get(bali.Boolean.class.getName()));
        Type valueType =  node.getCondition().getType();
        if (valueType == null || !valueType.isAssignableTo(targetType)){
            failures.add(new CompileError(
                    ErrorCode.INVALID_TYPE,
                    node
            ));
        }
        visitChildren(node);
    }
}
