package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;

import java.util.List;

public class InvocationNode extends ExpressionNode {

    private final MonitoredProperty<Method> resolvedMethod;
    private ExpressionNode target;
    private String methodName;
    private List<ExpressionNode> arguments;

    public InvocationNode(Integer line, Integer character, CompilationThreadManager monitor) {
        super(line, character);
        this.resolvedMethod = new MonitoredProperty<>(this, "resolvedMethod", monitor);
    }

    public Site getSite() {
        return resolvedMethod.get().getReturnType();
    }

    public Method getResolvedMethod() {
        return resolvedMethod.get();
    }

    public void setResolvedMethod(Method method) {
        resolvedMethod.set(method);
    }

    public ExpressionNode getTarget() {
        return target;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setTarget(ExpressionNode target) {
        this.target = target;
        children.add(target);
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<ExpressionNode> getArguments() {
        return arguments;
    }

    public void setArguments(List<ExpressionNode> arguments) {
        this.arguments = arguments;
        children.addAll(arguments);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
