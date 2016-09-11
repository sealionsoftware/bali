package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Operator;
import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.InvocationNode;
import com.sealionsoftware.bali.compiler.tree.OperationNode;

public class InvocationMethodResolver extends ValidatingVisitor {

    public void visit(InvocationNode node) {
        visitChildren(node);
        ErrorCode code = ErrorCode.METHOD_NOT_FOUND;
        Type targetType = getTargetType(node, code);
        if (targetType != null){
            setResolvedMethod(node, targetType.getMethod(node.getMethodName()), code);
        }
    }

    public void visit(OperationNode node) {
        visitChildren(node);
        ErrorCode code = ErrorCode.OPERATOR_NOT_FOUND;
        Type targetType = getTargetType(node, code);
        if (targetType != null){

            Operator resolvedMethod = node.getArguments().isEmpty() ?
                    targetType.getUnaryOperator(node.getOperatorName()):
                    targetType.getOperator(node.getOperatorName());

            setResolvedMethod(node, resolvedMethod, code);
        }
    }


    private Type getTargetType(InvocationNode node, ErrorCode errorCode) {
        ExpressionNode target = node.getTarget();
        if (target.getSite() == null){
            failures.add(new CompileError(
                    errorCode,
                    node
            ));
            return null;
        }
        Site targetSite = target.getSite();
        if (targetSite.isOptional){
            failures.add(new CompileError(
                    ErrorCode.CANNOT_INVOKE_ON_OPTIONAL,
                    node
            ));
        }
        return targetSite.type;
    }

    private void setResolvedMethod(InvocationNode node, Method method, ErrorCode errorCode) {
        if (method == null){
            failures.add(new CompileError(
                    errorCode,
                    node
            ));
            return;
        }
        node.setResolvedMethod(method);
    }
}
