package com.sealionsoftware.bali.compiler.parser;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Mock {

    public static TerminalNode mockTerminal(String value){
        TerminalNode ret = mock(TerminalNode.class);
        when(ret.getText()).thenReturn(value);
        return ret;
    }

    public static <T extends ParserRuleContext> T mockContext(Class<T> contextClass){
        T ret = mock(contextClass);
        ret.start = mock(Token.class);
        return ret;
    }

}
