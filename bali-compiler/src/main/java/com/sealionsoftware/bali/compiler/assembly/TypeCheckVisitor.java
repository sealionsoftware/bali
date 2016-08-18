package com.sealionsoftware.bali.compiler.assembly;

import bali.Logic;
import com.sealionsoftware.Collections.Each;
import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Parameter;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.tree.ArrayLiteralNode;
import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalLoopNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.InvocationNode;
import com.sealionsoftware.bali.compiler.tree.OperationNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import com.sealionsoftware.bali.compiler.type.Class;
import com.sealionsoftware.bali.compiler.type.ClassBasedType;

import java.util.List;
import java.util.Map;

import static com.sealionsoftware.Collections.both;

public class TypeCheckVisitor extends ValidatingVisitor {

    private final Map<String, Class> library;

    public TypeCheckVisitor(Map<String, Class> library) {
        this.library = library;
    }

    public void visit(VariableNode node) {
        TypeNode variableType = node.getType();
        ExpressionNode value = node.getValue();
        if (value != null){
            Type valueType = node.getValue().getType();
            if (variableType != null && (valueType == null || !valueType.isAssignableTo(variableType.getResolvedType()))){
                failures.add(new CompileError(
                        ErrorCode.INVALID_TYPE,
                        node
                ));
            }
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
        visitConditional(node);
    }

    public void visit(ConditionalLoopNode node) {
        visitConditional(node);
    }

    private void visitConditional(ConditionalNode node) {
        Type targetType = new ClassBasedType(library.get(Logic.class.getName()));
        Type valueType =  node.getCondition().getType();
        if (valueType == null || !valueType.isAssignableTo(targetType)){
            failures.add(new CompileError(
                    ErrorCode.INVALID_TYPE,
                    node
            ));
        }
        visitChildren(node);
    }

    public void visit(InvocationNode node) {
        Method resolvedMethod = node.getResolvedMethod();
        List<Parameter> parameterList = resolvedMethod.getParameters();
        List<ExpressionNode> argumentNodes = node.getArguments();

        if (parameterList.size() != argumentNodes.size()){
            failures.add(new CompileError(
                    ErrorCode.INVALID_ARGUMENT_LIST,
                    node
            ));
            return;
        }

        for (Each<Parameter, ExpressionNode> pair : both(parameterList, argumentNodes)){
            if (!pair.j.getType().isAssignableTo(pair.i.type)){
                failures.add(new CompileError(
                        ErrorCode.INVALID_TYPE,
                        pair.j
                ));
            }
        }

        visitChildren(node);
    }

    public void visit(OperationNode operationNode){
        visit((InvocationNode) operationNode);
    }

    public void visit(ArrayLiteralNode literalNode){
        List<Parameter> typeArguments = literalNode.getType().getTypeArguments();
        if (typeArguments.size() == 1){
            Type argumentType = typeArguments.get(0).type;
            for (ExpressionNode expressionNode : literalNode.getItems()){
                if (!expressionNode.getType().isAssignableTo(argumentType)){
                    failures.add(new CompileError(
                            ErrorCode.INVALID_TYPE,
                            expressionNode
                    ));
                }
            }
        }
        visitChildren(literalNode);
    }
}
