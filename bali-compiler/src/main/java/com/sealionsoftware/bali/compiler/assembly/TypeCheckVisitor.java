package com.sealionsoftware.bali.compiler.assembly;

import bali.Iterable;
import bali.Logic;
import com.sealionsoftware.Collections.Each;
import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Parameter;
import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.tree.ArrayLiteralNode;
import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalLoopNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.InvocationNode;
import com.sealionsoftware.bali.compiler.tree.IterationNode;
import com.sealionsoftware.bali.compiler.tree.OperationNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import com.sealionsoftware.bali.compiler.type.Class;
import com.sealionsoftware.bali.compiler.type.ClassBasedType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sealionsoftware.Collections.both;
import static java.util.Arrays.asList;

public class TypeCheckVisitor extends ValidatingVisitor {

    private final Map<String, Class> library;

    public TypeCheckVisitor(Map<String, Class> library) {
        this.library = library;
    }

    public void visit(VariableNode node) {
        TypeNode variableType = node.getType();
        ExpressionNode value = node.getValue();
        if (value != null){
            Site valueType = node.getValue().getSite();
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
        Site targetType = node.getTarget().getReferenceData().type;
        Site valueType =  node.getValue().getSite();
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
        Site targetType = new Site(new ClassBasedType(library.get(Logic.class.getName())), true);
        Site valueType =  node.getCondition().getSite();
        if (valueType == null || !valueType.isAssignableTo(targetType)){
            failures.add(new CompileError(
                    ErrorCode.INVALID_TYPE,
                    node
            ));
        }
        visitChildren(node);
    }

    public void visit(IterationNode node) {
        Site argumentType = node.getItemData().type;
        Site expectedType = new Site(new ClassBasedType(library.get(Iterable.class.getName()), asList(argumentType)), true);
        Site valueType =  node.getTarget().getSite();
        if (valueType == null || !valueType.isAssignableTo(expectedType)){
            failures.add(new CompileError(
                    ErrorCode.INVALID_TYPE,
                    node
            ));
        }
        node.getStatement().accept(this);
        node.getTarget().accept(this);
    }

    public void visit(InvocationNode node) {

        visitChildren(node);

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
            Site argumentSite = pair.j.getSite();
            Site parameterSite = pair.i.site;
            if (parameterSite != null && (argumentSite == null || !argumentSite.isAssignableTo(parameterSite))){
                failures.add(new CompileError(
                        ErrorCode.INVALID_TYPE,
                        pair.j
                ));
            }
        }
    }

    public void visit(OperationNode operationNode){
        visit((InvocationNode) operationNode);
    }

    public void visit(ArrayLiteralNode literalNode){
        List<Parameter> typeArguments = literalNode.getSite().type.getTypeArguments();
        if (typeArguments.size() == 1){
            Site argumentType = typeArguments.get(0).site;
            failures.addAll(literalNode.getItems().stream().filter(expressionNode -> !expressionNode.getSite().isAssignableTo(argumentType)).map(expressionNode -> new CompileError(
                    ErrorCode.INVALID_TYPE,
                    expressionNode
            )).collect(Collectors.toList()));
        }
        visitChildren(literalNode);
    }
}
