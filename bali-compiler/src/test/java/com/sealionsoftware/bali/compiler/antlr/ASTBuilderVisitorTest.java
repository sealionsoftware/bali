package com.sealionsoftware.bali.compiler.antlr;

import bali.compiler.parser.BaliParser;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ASTBuilderVisitorTest {

    private ASTBuilderVisitor subject = new ASTBuilderVisitor();

    @Test
    public void testVisitScript() throws Exception {

        BaliParser.ScriptContext context = mock(BaliParser.ScriptContext.class);

        CodeBlockNode node = subject.visitScript(context);

        assertThat(node, notNullValue());
    }
}