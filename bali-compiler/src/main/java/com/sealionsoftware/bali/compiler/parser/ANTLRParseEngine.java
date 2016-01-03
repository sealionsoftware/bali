package com.sealionsoftware.bali.compiler.parser;

import bali.compiler.parser.BaliLexer;
import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.ParseEngine;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;

import java.io.StringReader;

public class ANTLRParseEngine implements ParseEngine {

    private CompilationThreadManager monitor;

    public ANTLRParseEngine(CompilationThreadManager monitor) {
        this.monitor = monitor;
    }

    public CodeBlockNode parse(String fragment) {

        try {

            ANTLRInputStream input = new ANTLRInputStream(new StringReader(fragment));
            Lexer lexer = new BaliLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();
            BaliParser parser = new BaliParser(tokens);

            BaliParser.ScriptContext context = parser.script();
            int errors = parser.getNumberOfSyntaxErrors();
            if (errors > 0) {
                throw new RuntimeException("Fragment contains [" + errors + "] syntax errors");
            }

            ASTStatementVisitor astBuilder = new ASTStatementVisitor(monitor);
            return (CodeBlockNode) context.accept(astBuilder);


        } catch (Exception e) {
            throw new RuntimeException("Could not parse fragment", e);
        }
    }
}
