package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.InvocationNode;

public class InvocationMethodResolver extends ValidatingVisitor {

    public void visit(InvocationNode node) {
        visitChildren(node);
        ExpressionNode target = node.getTarget();
        if (target == null){
            failures.add(new CompileError(
                    ErrorCode.METHOD_NOT_FOUND,
                    node
            ));
            return;
        }
        Type targetType = target.getType();
        Method method = targetType.getMethod(node.getMethodName());
        if (method == null){
            failures.add(new CompileError(
                    ErrorCode.METHOD_NOT_FOUND,
                    node
            ));
            return;
        }
        node.setResolvedMethod(method);
    }

}
