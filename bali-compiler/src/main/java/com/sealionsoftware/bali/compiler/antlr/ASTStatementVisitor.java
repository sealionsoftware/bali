package com.sealionsoftware.bali.compiler.antlr;

import bali.compiler.parser.BaliBaseVisitor;
import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.StatementNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

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
        node.setType(buildType(ctx.type()));
        return node;
    }

    public ExpressionNode visitExpression(BaliParser.ExpressionContext ctx) {
        ASTExpressionVisitor expressionVisitor = new ASTExpressionVisitor();
        ExpressionNode node = expressionVisitor.visitExpression(ctx);
        container.addStatement(node);
        return node;
    }

    public TypeNode buildType(BaliParser.TypeContext ctx) {
        Token start = ctx.start;
        TypeNode node = new TypeNode(start.getLine(), start.getCharPositionInLine());
        node.setName(ctx.IDENTIFIER().getText());
        List<TypeNode> arguments = new ArrayList<>();
        BaliParser.TypeListContext argumentContexts = ctx.typeList();
        if (argumentContexts != null) for (BaliParser.TypeContext argumentContext : argumentContexts.type()) {
            arguments.add(buildType(argumentContext));
        }
        node.setArguments(arguments);
        return node;
    }


}
