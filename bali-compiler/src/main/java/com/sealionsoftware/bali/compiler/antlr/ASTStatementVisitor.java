package com.sealionsoftware.bali.compiler.antlr;

import bali.compiler.parser.BaliBaseVisitor;
import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalLoopNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.StatementNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public class ASTStatementVisitor extends BaliBaseVisitor<StatementNode> {

    private CodeBlockNode container = new CodeBlockNode(0, 0);
    private CompilationThreadManager monitor;

    public ASTStatementVisitor(CompilationThreadManager monitor) {
        this.monitor = monitor;
    }

    public CodeBlockNode visitScript(BaliParser.ScriptContext ctx) {
        visitChildren(ctx);
        return container;
    }

    public CodeBlockNode visitCodeBlock(BaliParser.CodeBlockContext ctx) {
        CodeBlockNode parent = container;
        Token start = ctx.start;
        CodeBlockNode child = new CodeBlockNode(start.getLine(), start.getCharPositionInLine());
        try {
            container = child;
            visitChildren(ctx);
        } finally {
            container = parent;
        }
        return child;
    }

    public VariableNode visitVariableDeclaration(BaliParser.VariableDeclarationContext ctx) {
        Token start = ctx.start;
        VariableNode node = new VariableNode(start.getLine(), start.getCharPositionInLine());
        node.setName(ctx.IDENTIFIER().getText());
        container.addStatement(node);
        ASTExpressionVisitor expressionVisitor = new ASTExpressionVisitor(monitor);
        node.setValue(ctx.expression().accept(expressionVisitor));
        BaliParser.TypeContext typeContext = ctx.type();
        if (typeContext != null){
            node.setType(buildType(typeContext));
        }
        return node;
    }

    public AssignmentNode visitAssignment(BaliParser.AssignmentContext ctx) {

        Token start = ctx.start;
        AssignmentNode node = new AssignmentNode(start.getLine(), start.getCharPositionInLine());
        node.setTarget(visitAssignmentReference(ctx.reference()));
        container.addStatement(node);
        ASTExpressionVisitor expressionVisitor = new ASTExpressionVisitor(monitor);
        node.setValue(ctx.expression().accept(expressionVisitor));
        return node;
    }

    private ReferenceNode visitAssignmentReference(BaliParser.ReferenceContext ctx) {
        Token start = ctx.start;
        ReferenceNode node = new ReferenceNode(start.getLine(), start.getCharPositionInLine(), monitor);
        node.setName(ctx.IDENTIFIER().getText());
        return node;
    }

    public ExpressionNode visitExpression(BaliParser.ExpressionContext ctx) {
        ASTExpressionVisitor expressionVisitor = new ASTExpressionVisitor(monitor);
        ExpressionNode node = expressionVisitor.visitExpression(ctx);
        container.addStatement(node);
        return node;
    }

    public ConditionalStatementNode visitConditionalStatement(BaliParser.ConditionalStatementContext ctx){
        Token start = ctx.start;
        ASTExpressionVisitor expressionVisitor = new ASTExpressionVisitor(monitor);
        ConditionalStatementNode conditionalStatementNode = new ConditionalStatementNode(start.getLine(), start.getCharPositionInLine());

        conditionalStatementNode.setCondition(ctx.expression().accept(expressionVisitor));
        conditionalStatementNode.setConditional(ctx.controlExpression().accept(this));
        container.addStatement(conditionalStatementNode);

        return conditionalStatementNode;
    }

    public ConditionalLoopNode visitLoopStatement(BaliParser.LoopStatementContext ctx){
        Token start = ctx.start;
        ASTExpressionVisitor expressionVisitor = new ASTExpressionVisitor(monitor);
        ConditionalLoopNode loopNode = new ConditionalLoopNode(start.getLine(), start.getCharPositionInLine());

        loopNode.setCondition(ctx.expression().accept(expressionVisitor));
        loopNode.setConditional(ctx.controlExpression().accept(this));
        container.addStatement(loopNode);

        return loopNode;
    }

    private TypeNode buildType(BaliParser.TypeContext ctx) {
        Token start = ctx.start;
        TypeNode node = new TypeNode(monitor, start.getLine(), start.getCharPositionInLine());
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
