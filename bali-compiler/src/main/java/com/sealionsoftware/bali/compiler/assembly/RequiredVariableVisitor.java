package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;

public class RequiredVariableVisitor extends ValidatingVisitor {

    public void visit(VariableNode node) {
        TypeNode variableType = node.getType();
        ExpressionNode value = node.getValue();
        if (variableType != null && !variableType.getOptional() && value == null){
            failures.add(new CompileError(
                    ErrorCode.VALUE_REQUIRED,
                    node
            ));
        }
    }

}
