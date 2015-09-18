package com.sealionsoftware.bali.compiler.antlr;

import bali.compiler.parser.BaliLexer;
import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.ParseEngine;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Lexer;

import java.io.StringReader;

public class ANTLRParseEngine implements ParseEngine {

    public CodeBlockNode parse(String fragment) {

        CodeBlockNode node;

        try {

            ANTLRInputStream input = new ANTLRInputStream(new StringReader(fragment));
            ASTStatementVisitor astBuilder = new ASTStatementVisitor();

            Lexer lexer = new BaliLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();
            BaliParser parser = new BaliParser(tokens);
            parser.setErrorHandler(new DefaultErrorStrategy());

            node = (CodeBlockNode) parser.script().accept(astBuilder);

            int errors = parser.getNumberOfSyntaxErrors();
            if (errors > 0) {
                throw new RuntimeException("Fragment contains [" + errors + "] syntax errors");
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not parse fragment", e);
        }

        return node;
    }
}
