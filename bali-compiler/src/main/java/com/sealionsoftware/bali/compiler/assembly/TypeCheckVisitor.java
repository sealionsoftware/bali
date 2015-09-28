package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.tree.VariableNode;

public class TypeCheckVisitor extends ValidatingVisitor {

    public void visit(VariableNode node) {
        Type variableType = node.getType().getResolvedType();
        if (variableType != null && !node.getValue().getType().isAssignableTo(variableType)){
            failures.add(new CompileError(
                    ErrorCode.INVALID_TYPE,
                    node
            ));
        }

    }
}
