package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalLoopNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.ExistenceCheckNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.Node;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.StatementNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;

public class ReferenceMatchingVisitor extends ValidatingVisitor {

    private Deque<Scope> scopeStack = new ArrayDeque<>(asList(new Scope()));

    public void visit(CodeBlockNode codeBlock) {
        pushAndWalk(codeBlock, new Scope());
    }

    public void visit(VariableNode variable) {
        scopeStack.peek().add(createData(variable));
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
                VariableData data = referenceNode.getVariableData();
                Site originalSite = data.type;
                Scope scope = new Scope();
                scope.add(new VariableData(data.name, new Site(originalSite != null ? originalSite.type : null, false), data.id));
                pushAndWalk(conditionalNode.getConditional(), scope);
                return;
            }
        }
        conditionalNode.getConditional().accept(this);
    }

    private void pushAndWalk(Node node, Scope scope) {
        scopeStack.push(scope);
        visitChildren(node);
        scopeStack.pop();
    }

    public void visit(ReferenceNode value) {

        String name = value.getName();
        Scope declarationScope = getScopeForReference(name);

        if (declarationScope == null) {
            failures.add(new CompileError(ErrorCode.CANNOT_RESOLVE_REFERENCE, value));
            return;
        }

        value.setVariableData(declarationScope.find(name));
    }

    private Scope getScopeForReference(String name) {

        for (Scope scope : scopeStack) if (scope.contains(name)) {
            return scope;
        }
        return null;
    }

    private static class Scope {

        private Map<String, VariableData> declarations = new HashMap<>();

        public Scope(){}

        public void add(VariableData vd) {
            declarations.put(vd.name, vd);
        }

        public VariableData find(String name) {
            return declarations.get(name);
        }

        public Boolean contains(String name) {
            return declarations.containsKey(name);
        }
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
