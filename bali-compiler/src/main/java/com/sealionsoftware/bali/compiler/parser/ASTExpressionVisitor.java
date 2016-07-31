package com.sealionsoftware.bali.compiler.parser;

import bali.compiler.parser.BaliBaseVisitor;
import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.tree.ArrayLiteralNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.IntegerLiteralNode;
import com.sealionsoftware.bali.compiler.tree.InvocationNode;
import com.sealionsoftware.bali.compiler.tree.LogicLiteralNode;
import com.sealionsoftware.bali.compiler.tree.OperationNode;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import static bali.number.Primitive.convert;
import static bali.number.Primitive.parse;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class ASTExpressionVisitor extends BaliBaseVisitor<ExpressionNode> {

    private CompilationThreadManager monitor;

    private Deque<ExpressionNode> targets = new ArrayDeque<>();

    public ASTExpressionVisitor(CompilationThreadManager monitor) {
        this.monitor = monitor;
    }

    public LogicLiteralNode visitLogicLiteral(BaliParser.LogicLiteralContext ctx) {
        Token start = ctx.start;
        LogicLiteralNode node = new LogicLiteralNode(start.getLine(), start.getCharPositionInLine(), monitor);
        node.setValue("true".equals(ctx.getText()));
        return node;
    }

    public TextLiteralNode visitTextLiteral(BaliParser.TextLiteralContext ctx) {
        Token start = ctx.start;
        TextLiteralNode node = new TextLiteralNode(start.getLine(), start.getCharPositionInLine(), monitor);
        String text = ctx.getText();
        node.setValue(text.substring(1, text.length() -1));
        return node;
    }

    public IntegerLiteralNode visitIntegerLiteral(BaliParser.IntegerLiteralContext ctx) {
        Token start = ctx.start;
        IntegerLiteralNode node = new IntegerLiteralNode(start.getLine(), start.getCharPositionInLine(), monitor);
        node.setValue(convert(parse(ctx.getText())));
        return node;
    }

    public ArrayLiteralNode visitArrayLiteral(BaliParser.ArrayLiteralContext ctx) {
        Token start = ctx.start;
        ArrayLiteralNode node = new ArrayLiteralNode(start.getLine(), start.getCharPositionInLine(), monitor);
        node.setItems(ctx.expression()
                .stream()
                .map((item) -> item.accept(this))
                .collect(Collectors.toList()));
        return node;
    }

    public ReferenceNode visitReference(BaliParser.ReferenceContext ctx) {
        Token start = ctx.start;
        ReferenceNode node = new ReferenceNode(start.getLine(), start.getCharPositionInLine(), monitor);
        node.setName(ctx.getText());
        return node;
    }

    public InvocationNode visitInvocation(BaliParser.InvocationContext ctx) {
        Token start = ctx.start;
        InvocationNode node = new InvocationNode(start.getLine(), start.getCharPositionInLine(), monitor);
        node.setMethodName(ctx.IDENTIFIER().getText());
        node.setTarget(targets.peek());
        node.setArguments(
                ctx.argument()
                        .stream()
                        .map((item) -> item.expression().accept(this))
                        .collect(Collectors.toList())
        );
        return node;
    }

    public ExpressionNode visitExpression(BaliParser.ExpressionContext ctx){

        List<BaliParser.ExpressionContext> expressionContexts = ctx.expression();
        BaliParser.InvocationContext invocationContext = ctx.invocation();
        TerminalNode operatorContext = ctx.OPERATOR();

        if (operatorContext != null) {
            Token start = ctx.start;
            OperationNode node = new OperationNode(start.getLine(), start.getCharPositionInLine(), monitor);
            node.setTarget(expressionContexts.get(0).accept(this));
            node.setOperatorName(ctx.OPERATOR().getText());
            node.setArguments(
                    expressionContexts.size() == 2 ?
                            asList(expressionContexts.get(1).accept(this)) :
                            emptyList()
            );
            return node;
        } else if (invocationContext != null && expressionContexts.size() == 1) {
            targets.push(expressionContexts.get(0).accept(this));
            ExpressionNode ret = invocationContext.accept(this);
            targets.pop();
            return ret;
        }

        return visitChildren(ctx);
    }

    protected ExpressionNode aggregateResult(ExpressionNode aggregate, ExpressionNode nextResult) {
        return nextResult != null ? nextResult : aggregate;
    }
}
