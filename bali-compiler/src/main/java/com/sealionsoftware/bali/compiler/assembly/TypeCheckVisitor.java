package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;

public class TypeCheckVisitor extends ValidatingVisitor {

    public void visit(VariableNode node) {
        TypeNode type = node.getType();
        if (type == null){
            return;
        }
        Type variableType = type.getResolvedType();
        if (variableType != null && !node.getValue().getType().isAssignableTo(variableType)){
            failures.add(new CompileError(
                    ErrorCode.INVALID_TYPE,
                    node
            ));
        }

    }
}
