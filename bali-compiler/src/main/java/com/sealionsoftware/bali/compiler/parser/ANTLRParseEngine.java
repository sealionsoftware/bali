package com.sealionsoftware.bali.compiler.parser;

import bali.compiler.parser.BaliLexer;
import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.ParseEngine;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.Node;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.StringReader;
import java.util.function.Function;

public class ANTLRParseEngine implements ParseEngine {

    private CompilationThreadManager monitor;

    public ANTLRParseEngine(CompilationThreadManager monitor) {
        this.monitor = monitor;
    }

    public CodeBlockNode parseFragment(String fragment) {
        return parse(
                fragment,
                "fragment",
                BaliParser::script,
                (context) -> {
                    ASTStatementVisitor astBuilder = new ASTStatementVisitor(monitor);
                    return (CodeBlockNode) context.accept(astBuilder);
                }
        );
    }

    public ExpressionNode parseExpression(String expression) {
        return parse(
                expression,
                "expression",
                BaliParser::expression,
                (context) -> {
                    ASTExpressionVisitor astBuilder = new ASTExpressionVisitor(monitor);
                    return context.accept(astBuilder);
                }
        );
    }

    private <N extends Node, C extends ParserRuleContext> N parse(String input, String name, Function<BaliParser, C> getContext, Function<C, N> doVisit) {

        try {

            BaliParser parser = constructParser(input);

            C context = getContext.apply(parser);
            int errors = parser.getNumberOfSyntaxErrors();
            if (errors > 0) {
                throw new RuntimeException(name.substring(0, 1).toUpperCase() + name.substring(1) + " contains [" + errors + "] syntax errors");
            }

            return doVisit.apply(context);

        } catch (Exception e) {
            throw new RuntimeException("Could not parse " + name, e);
        }
    }

    private BaliParser constructParser(String in) throws Exception {
        ANTLRInputStream input = new ANTLRInputStream(new StringReader(in));
        Lexer lexer = new BaliLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        return new BaliParser(tokens);
    }
}
