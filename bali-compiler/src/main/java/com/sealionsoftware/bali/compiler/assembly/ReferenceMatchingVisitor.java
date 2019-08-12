package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Parameter;
import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.tree.*;
import com.sealionsoftware.bali.compiler.type.InferredType;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;

public class ReferenceMatchingVisitor extends ValidatingVisitor {

    private final Deque<Scope> scopeStack;

    public ReferenceMatchingVisitor() {
        this.scopeStack = new ArrayDeque<>(asList(new Scope()));
    }

    public ReferenceMatchingVisitor(Scope scope) {
        this.scopeStack = new ArrayDeque<>(asList(scope, new Scope()));
    }

    public void visit(CodeBlockNode codeBlock) {
        pushAndWalk(codeBlock, new Scope());
    }

    public void visit(VariableNode variable) {

        ReferenceData existing = getDeclaration(variable.getName());
        if (existing != null){
            failures.add(new CompileError(ErrorCode.NAME_ALREADY_USED, variable));
        } else {
            scopeStack.peek().add(createData(variable));
        }

        visitChildren(variable);
    }

    public void visit(ConditionalStatementNode conditionalStatementNode) {
        visit((ConditionalNode) conditionalStatementNode);
        StatementNode contraCondition = conditionalStatementNode.getContraConditional();
        if (contraCondition != null){
            contraCondition.accept(this);
        }
    }

    public void visit(TryStatementNode tryStatementNode) {
        ReferenceData existing = getDeclaration(tryStatementNode.getCaughtName());
        if (existing != null){
            failures.add(new CompileError(ErrorCode.NAME_ALREADY_USED, tryStatementNode));
            visitChildren(tryStatementNode);
            return;
        }

        tryStatementNode.getCoveredStatement().accept(this);

        VariableData itemDeclaration = new VariableData(
                tryStatementNode.getCaughtName(),
                tryStatementNode.getCaughtType().getResolvedType(),
                tryStatementNode.getId()
        );

        Scope catchScope = new Scope();
        catchScope.add(itemDeclaration);
        pushAndWalkChild(tryStatementNode.getCatchBlock(), catchScope);
    }

    public void visit(ConditionalLoopNode conditionalLoopNode) {
        visit((ConditionalNode) conditionalLoopNode);
    }

    private void visit(ConditionalNode conditionalNode) {

        conditionalNode.getCondition().accept(this);

        ExpressionNode condition = conditionalNode.getCondition();
        if (condition instanceof ExistenceCheckNode){
            ExistenceCheckNode existenceCheckNode = (ExistenceCheckNode) condition;
            ExpressionNode target = existenceCheckNode.getTarget();
            if (target instanceof ReferenceNode){
                ReferenceNode referenceNode = (ReferenceNode) target;
                Scope scope = new Scope();
                scope.add(buildPresentReference(referenceNode.getReferenceData()));
                pushAndWalkChild(conditionalNode.getConditional(), scope);
                return;
            }
        }
        conditionalNode.getConditional().accept(this);
    }

    public void visit(IterationNode iterationNode) {

        ReferenceData existing = getDeclaration(iterationNode.getIdentifier());
        if (existing != null){
            failures.add(new CompileError(ErrorCode.NAME_ALREADY_USED, iterationNode));
            visitChildren(iterationNode);
            return;
        }

        ExpressionNode target = iterationNode.getTarget();
        target.accept(this);

        Site listSite = target.getSite();
        Type itemType = listSite != null ? findIterativeTypeArgument(listSite.type) : null;
        VariableData itemDeclaration = new VariableData(
                iterationNode.getIdentifier(),
                itemType == null ? null : new Site(itemType),
                UUID.randomUUID()
        );
        iterationNode.setItemData(itemDeclaration);
        Scope loopScope = new Scope();
        loopScope.add(itemDeclaration);
        pushAndWalkChild(iterationNode.getStatement(), loopScope);
    }

    private Type findIterativeTypeArgument(Type type) {

        if (type instanceof InferredType){
            return new InferredType(null);
        }

        String className = type.getClassName();
        if (className != null && className.equals(bali.Iterable.class.getName())){
            List<Parameter> typeArguments = type.getTypeArguments();
            for (Parameter argument : typeArguments) if (argument.name.equals("T")) {
                Site argumentSite = argument.site;
                return argumentSite != null ? argumentSite.type : null;
            }
            return null;
        }
        for (Type iface : type.getInterfaces()){
            Type found = findIterativeTypeArgument(iface);
            if (found != null){
                return found;
            }
        }

        return null;
    }

    private ReferenceData buildPresentReference(ReferenceData data) {
        Site originalSite = data.type;
        if (data instanceof VariableData){
            return new VariableData(data.name, buildPresentSite(originalSite), ((VariableData) data).id);
        }
        if (data instanceof FieldData){
            return new FieldData(data.name, buildPresentSite(originalSite));
        }

        throw new RuntimeException("Invalid reference type");
    }

    private Site buildPresentSite(Site in){
        return new Site(in != null ? in.type : null);
    }

    private void pushAndWalkChild(Node node, Scope scope) {
        scopeStack.push(scope);
        node.accept(this);
        scopeStack.pop();
    }

    private void pushAndWalk(Node node, Scope scope) {
        scopeStack.push(scope);
        visitChildren(node);
        scopeStack.pop();
    }

    public void visit(ReferenceNode value) {

        String name = value.getName();
        ReferenceData declaration = getDeclaration(name);

        if (declaration == null) {
            failures.add(new CompileError(ErrorCode.CANNOT_RESOLVE_REFERENCE, value));
            return;
        }

        value.setReferenceData(declaration);
    }

    private ReferenceData getDeclaration(String name) {

        for (Scope scope : scopeStack) {
            ReferenceData reference = scope.find(name);
            if (reference != null) {
                return reference;
            }
        }
        return null;
    }

    private VariableData createData(VariableNode node){
        TypeNode type = node.getType();
        return new VariableData(
                node.getName(),
                type != null ?
                        type.getResolvedType() :
                        null,
                node.getId()
        );
    }

}
