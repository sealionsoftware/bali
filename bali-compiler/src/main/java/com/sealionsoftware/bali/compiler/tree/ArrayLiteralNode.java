package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;

import java.util.List;

public class ArrayLiteralNode extends ExpressionNode {

    private List<ExpressionNode> items;
    private final MonitoredProperty<Site> site;

    public ArrayLiteralNode(Integer line, Integer character, CompilationThreadManager monitor) {
        super(line, character);
        this.site = new MonitoredProperty<>(this, "site", monitor);
    }

    public List<ExpressionNode> getItems() {
        return items;
    }

    public void setItems(List<ExpressionNode> items) {
        this.items = items;
        children.addAll(items);
    }

    public void setType(Type type){
        site.set(new Site(type, false));
    }

    public Site getSite() {
        return site.get();
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
