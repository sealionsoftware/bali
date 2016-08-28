package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;

public class IntegerLiteralNode extends LiteralNode<Integer> {

    private final MonitoredProperty<Site> site;

    public IntegerLiteralNode(Integer line, Integer character, CompilationThreadManager monitor) {
        super(line, character);
        this.site =  new MonitoredProperty<>(this, "site", monitor);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void setSite(Site type) {
        this.site.set(type);
    }

    public Site getSite() {
        return site.get();
    }
}
