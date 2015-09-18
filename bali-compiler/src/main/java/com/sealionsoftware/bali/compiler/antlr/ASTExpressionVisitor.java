package com.sealionsoftware.bali.compiler.antlr;

import bali.compiler.parser.BaliBaseVisitor;
import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import org.antlr.v4.runtime.Token;

public class ASTExpressionVisitor extends BaliBaseVisitor<ExpressionNode> {

    public BooleanLiteralNode visitBooleanLiteral(BaliParser.BooleanLiteralContext ctx) {
        Token start = ctx.start;
        BooleanLiteralNode node = new BooleanLiteralNode(start.getLine(), start.getCharPositionInLine());
        node.setValue("true".equals(ctx.getText()));
        return node;
    }

}
