package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.Control;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;

public class TypeCheckVisitor extends ValidatingVisitor {

    public void visit(VariableNode node, Control control) {
        TypeNode variableType = node.getType();
        Type valueType = node.getValue().getType();
        if (variableType != null && (valueType == null || !valueType.isAssignableTo(variableType.getResolvedType()))){
            failures.add(new CompileError(
                    ErrorCode.INVALID_TYPE,
                    node
            ));
        }
        control.visitChildren();
    }

    public void visit(AssignmentNode node, Control control) {
        Type targetType = node.getTarget().getVariableData().type;
        Type valueType =  node.getValue().getType();
        if (targetType != null && (valueType == null || !valueType.isAssignableTo(targetType))){
            failures.add(new CompileError(
                    ErrorCode.INVALID_TYPE,
                    node
            ));
        }
        control.visitChildren();
    }
}
