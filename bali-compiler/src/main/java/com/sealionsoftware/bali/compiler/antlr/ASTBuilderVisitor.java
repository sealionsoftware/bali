package com.sealionsoftware.bali.compiler.antlr;

import bali.compiler.parser.BaliBaseVisitor;
import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.Node;

public class ASTBuilderVisitor extends BaliBaseVisitor<Node> {

    public CodeBlockNode visitScript(BaliParser.ScriptContext ctx) {
        CodeBlockNode node = new CodeBlockNode();
        visitChildren(ctx);
        return node;
    }

}
