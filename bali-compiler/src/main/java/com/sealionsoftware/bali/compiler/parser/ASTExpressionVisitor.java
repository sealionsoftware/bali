package com.sealionsoftware.bali.compiler.parser;

import bali.compiler.parser.BaliBaseVisitor;
import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.IntegerLiteralNode;
import com.sealionsoftware.bali.compiler.tree.InvocationNode;
import com.sealionsoftware.bali.compiler.tree.OperationNode;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import org.antlr.v4.runtime.Token;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import static bali.number.Primitive.parse;
import static java.util.Arrays.asList;

public class ASTExpressionVisitor extends BaliBaseVisitor<ExpressionNode> {

    private CompilationThreadManager monitor;

    private Deque<ExpressionNode> targets = new ArrayDeque<>();

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
        String text = ctx.getText();
        node.setValue(text.substring(1, text.length() -1));
        return node;
    }

    public IntegerLiteralNode visitIntegerLiteral(BaliParser.IntegerLiteralContext ctx) {
        Token start = ctx.start;
        IntegerLiteralNode node = new IntegerLiteralNode(start.getLine(), start.getCharPositionInLine(), monitor);
        node.setValue(parse(ctx.getText()));
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
        BaliParser.OperatorContext operatorContext = ctx.operator();

        switch (expressionContexts.size()){
            case 1: if (invocationContext != null) {
                targets.push(expressionContexts.get(0).accept(this));
                ExpressionNode ret = invocationContext.accept(this);
                targets.pop();
                return ret;
            } break;
            case 2: if (operatorContext != null){
                Token start = ctx.start;
                OperationNode node = new OperationNode(start.getLine(), start.getCharPositionInLine(), monitor);
                node.setTarget(expressionContexts.get(0).accept(this));
                node.setOperatorName(ctx.operator().getText());
                node.setArguments(asList(expressionContexts.get(1).accept(this)));
                return node;
            } break;
        }

        return visitChildren(ctx);
    }

}
