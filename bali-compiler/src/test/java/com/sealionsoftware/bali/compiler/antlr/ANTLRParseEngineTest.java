package com.sealionsoftware.bali.compiler.antlr;

import com.sealionsoftware.bali.compiler.ParseEngine;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class ANTLRParseEngineTest {

    private ParseEngine subject = new ANTLRParseEngine();

    @Test
    public void testParse() throws Exception {

        CodeBlockNode ret = subject.parse("");

        assertThat(ret, notNullValue());
        assertThat(ret.getStatements().size(), equalTo(0));
    }
}