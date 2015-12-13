package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.Control;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;

public class ReferenceMatchingVisitor extends ValidatingVisitor {

    private Deque<Scope> scopeStack = new ArrayDeque<>(asList(new Scope()));

    public void visit(CodeBlockNode codeBlock, Control agent) {
        pushAndWalk(agent, new Scope());
    }

    public void visit(VariableNode variable, Control control) {
        scopeStack.peek().add(createData(variable));
        control.visitChildren();
    }

    private void pushAndWalk(Control control, Scope scope) {
        scopeStack.push(scope);
        control.visitChildren();
        scopeStack.pop();
    }

    public void visit(ReferenceNode value, Control control) {

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
