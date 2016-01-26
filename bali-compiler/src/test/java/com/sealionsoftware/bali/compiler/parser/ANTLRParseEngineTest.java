package com.sealionsoftware.bali.compiler.parser;

import com.sealionsoftware.bali.compiler.ParseEngine;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ANTLRParseEngineTest {

    private CompilationThreadManager monitor = mock(CompilationThreadManager.class);
    private ParseEngine subject = new ANTLRParseEngine(monitor);

    @Test
    public void testParseFragment() throws Exception {

        CodeBlockNode ret = subject.parseFragment("var Boolean isTrue = true");

        assertThat(ret, notNullValue());
        assertThat(ret.getStatements().size(), equalTo(1));
    }

    @Test
    public void testParseEvaluation() throws Exception {

        ExpressionNode ret = subject.parseExpression("true");

        assertThat(ret, notNullValue());
    }

    @Test(expected = RuntimeException.class)
    public void testParseInvalid() throws Exception {

        subject.parseFragment("if try gobbledegook");
    }
}