package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;

public class TextLiteralNode extends LiteralNode<String> {

    private final MonitoredProperty<Site> site;

    public TextLiteralNode(Integer line, Integer character, CompilationThreadManager monitor) {
        super(line, character);
        this.site = new MonitoredProperty<>(this, "site", monitor);
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

    public String toString(){
        return "\"" + getValue() + "\"";
    }
}
