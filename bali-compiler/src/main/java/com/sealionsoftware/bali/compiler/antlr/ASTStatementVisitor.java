package com.sealionsoftware.bali.compiler.antlr;

import bali.compiler.parser.BaliBaseVisitor;
import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.StatementNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import org.antlr.v4.runtime.Token;

public class ASTStatementVisitor extends BaliBaseVisitor<StatementNode> {

    private CodeBlockNode container = new CodeBlockNode(0, 0);

    public CodeBlockNode visitScript(BaliParser.ScriptContext ctx) {
        visitChildren(ctx);
        return container;
    }

    public VariableNode visitVariableDeclaration(BaliParser.VariableDeclarationContext ctx) {
        Token start = ctx.start;
        VariableNode node = new VariableNode(start.getLine(), start.getCharPositionInLine());
        node.setName(ctx.IDENTIFIER().getText());
        container.addStatement(node);
        ASTExpressionVisitor expressionVisitor = new ASTExpressionVisitor();
        node.setValue(ctx.expression().accept(expressionVisitor));
        return node;
    }

    public ExpressionNode visitExpression(BaliParser.ExpressionContext ctx) {
        ASTExpressionVisitor expressionVisitor = new ASTExpressionVisitor();
        ExpressionNode node = expressionVisitor.visitExpression(ctx);
        container.addStatement(node);
        return node;
    }


}
