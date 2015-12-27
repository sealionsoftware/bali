package com.sealionsoftware.bali.compiler.antlr;

import bali.compiler.parser.BaliBaseVisitor;
import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import org.antlr.v4.runtime.Token;

public class ASTExpressionVisitor extends BaliBaseVisitor<ExpressionNode> {

    private CompilationThreadManager monitor;

    public ASTExpressionVisitor(CompilationThreadManager monitor) {
        this.monitor = monitor;
    }

    public BooleanLiteralNode visitBooleanLiteral(BaliParser.BooleanLiteralContext ctx) {
        Token start = ctx.start;
        BooleanLiteralNode node = new BooleanLiteralNode(start.getLine(), start.getCharPositionInLine(), monitor);
        node.setValue("true".equals(ctx.getText()));
        return node;
    }

    public TextLiteralNode visitTextLiteral(BaliParser.TextLiteralContext ctx) {
        Token start = ctx.start;
        TextLiteralNode node = new TextLiteralNode(start.getLine(), start.getCharPositionInLine(), monitor);
        node.setValue(ctx.getText());
        return node;
    }

    public ReferenceNode visitReference(BaliParser.ReferenceContext ctx) {
        Token start = ctx.start;
        ReferenceNode node = new ReferenceNode(start.getLine(), start.getCharPositionInLine(), monitor);
        node.setName(ctx.getText());
        return node;
    }

}
