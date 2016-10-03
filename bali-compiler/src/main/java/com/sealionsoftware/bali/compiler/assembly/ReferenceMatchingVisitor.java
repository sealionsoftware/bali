package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Parameter;
import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalLoopNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.ExistenceCheckNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.IterationNode;
import com.sealionsoftware.bali.compiler.tree.Node;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.StatementNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;

public class ReferenceMatchingVisitor extends ValidatingVisitor {

    private final Deque<Scope> scopeStack;

    public ReferenceMatchingVisitor() {
        this(new Scope());
    }

    public ReferenceMatchingVisitor(Scope scope) {
        this.scopeStack = new ArrayDeque<>(asList(scope));
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
                pushAndWalk(conditionalNode.getConditional(), scope);
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
        VariableData itemDeclaration = new VariableData(
                iterationNode.getIdentifier(),
                listSite == null ? null : new Site(findIterativeTypeArgument(listSite.type)),
                UUID.randomUUID()
        );
        iterationNode.setItemData(itemDeclaration);
        Scope loopScope = new Scope();
        loopScope.add(itemDeclaration);
        pushAndWalk(iterationNode.getStatement(), loopScope);
    }

    private Type findIterativeTypeArgument(Type type) {

        String className = type.getClassName();
        if (className != null && className.equals(bali.Iterable.class.getName())){
            List<Parameter> typeArguments = type.getTypeArguments();
            for (Parameter argument : typeArguments) if (argument.name.equals("T")) {
                return argument.site.type;
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
