package com.sealionsoftware.bali.compiler.antlr;

import com.sealionsoftware.bali.compiler.ParseEngine;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ANTLRParseEngineTest {

    private CompilationThreadManager monitor = mock(CompilationThreadManager.class);
    private ParseEngine subject = new ANTLRParseEngine(monitor);

    @Test
    public void testParse() throws Exception {

        CodeBlockNode ret = subject.parse("");

        assertThat(ret, notNullValue());
        assertThat(ret.getStatements().size(), equalTo(0));
    }

    @Test(expected = RuntimeException.class)
    public void testParseInvalid() throws Exception {

        subject.parse("if try gobbledegook");
    }
}