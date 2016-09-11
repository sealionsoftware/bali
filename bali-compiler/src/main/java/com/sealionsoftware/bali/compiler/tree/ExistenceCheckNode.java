package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;

public class ExistenceCheckNode extends ExpressionNode  {

    private ExpressionNode target;
    private final MonitoredProperty<Site> site;

    public ExistenceCheckNode(Integer line, Integer character, CompilationThreadManager monitor) {
        super(line, character);
        site = new MonitoredProperty<>(this, "site", monitor);
    }

    public void setType(Type type){
        site.set(new Site(type, false));
    }

    public Site getSite() {
        return site.get();
    }

    public ExpressionNode getTarget() {
        return target;
    }

    public void setTarget(ExpressionNode target) {
        children.add(target);
        this.target = target;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
