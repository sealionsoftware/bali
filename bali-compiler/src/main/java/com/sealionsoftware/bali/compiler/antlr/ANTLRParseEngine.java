package com.sealionsoftware.bali.compiler.antlr;

import bali.compiler.parser.BaliLexer;
import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.ParseEngine;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.Node;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Lexer;

import java.io.IOException;
import java.io.StringReader;

public class ANTLRParseEngine implements ParseEngine {

    public CodeBlockNode parse(String fragment) {

        ANTLRInputStream input;
        try {
             input = new ANTLRInputStream(new StringReader(fragment));
        } catch (IOException e) {
            throw new RuntimeException("Could not parse fragment", e);
        }

        ASTBuilderVisitor astBuilder = new ASTBuilderVisitor();

        Lexer lexer = new BaliLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        BaliParser parser = new BaliParser(tokens);
        parser.setErrorHandler(new DefaultErrorStrategy());

        Node node = parser.script().accept(astBuilder);

        int errors = parser.getNumberOfSyntaxErrors();
        if (errors > 0) {
            throw new RuntimeException("Could not parse fragment [" + errors + " errors]");
        }

        if (node instanceof CodeBlockNode){
            return (CodeBlockNode) node;
        }
        return null;
    }
}
