package com.sealionsoftware.bali.compiler.antlr;

import bali.compiler.parser.BaliBaseVisitor;
import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.StatementNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import org.antlr.v4.runtime.Token;

public class ASTStatementVisitor extends BaliBaseVisitor<StatementNode> {

    private CodeBlockNode container;

    public CodeBlockNode visitScript(BaliParser.ScriptContext ctx) {
        CodeBlockNode node = new CodeBlockNode(0, 0);
        CodeBlockNode originalContainer = container;
        container = node;
        visitChildren(ctx);
        container = originalContainer;
        return node;
    }

    public BooleanLiteralNode visitBooleanLiteral(BaliParser.BooleanLiteralContext ctx) {
        Token start = ctx.start;
        BooleanLiteralNode node = new BooleanLiteralNode(start.getLine(), start.getCharPositionInLine());
        node.setValue("true".equals(ctx.getText()));
        if (container != null){
            container.addStatement(node);
        }
        visitChildren(ctx);
        return node;
    }

    public VariableNode visitVariableDeclaration(BaliParser.VariableDeclarationContext ctx) {
        Token start = ctx.start;
        VariableNode node = new VariableNode(start.getLine(), start.getCharPositionInLine());
        node.setName(ctx.IDENTIFIER().getText());
        if (container != null){
            container.addStatement(node);
        }
        ASTExpressionVisitor expressionVisitor = new ASTExpressionVisitor();
        node.setValue(ctx.expression().accept(expressionVisitor));
        return node;
    }


}
