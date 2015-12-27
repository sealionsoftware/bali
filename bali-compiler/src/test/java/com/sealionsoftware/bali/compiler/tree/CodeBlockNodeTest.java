package com.sealionsoftware.bali.compiler.tree;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CodeBlockNodeTest {

    private CodeBlockNode subject = new CodeBlockNode(2, 3);

    @Test
    public void testGetStatements() throws Exception {

        StatementNode statementNode = mock(StatementNode.class);
        subject.addStatement(statementNode);
        List<StatementNode> statementNodes = subject.getStatements();
        assertThat(statementNodes, notNullValue());
        assertThat(statementNodes, hasSize(1));
        assertThat(statementNodes, hasItem(statementNode));
    }

    @Test
    public void testAccept() throws Exception {
        Visitor visitor = mock(Visitor.class);
        subject.accept(visitor);
        verify(visitor).visit(same(subject));
    }

    @Test
    public void testGetLine() throws Exception {
        assertThat(subject.getLine(), equalTo(2));
    }

    @Test
    public void testGetCharacter() throws Exception {
        assertThat(subject.getCharacter(), equalTo(3));
    }

    @Test
    public void testGetChildren() throws Exception {
        StatementNode statementNode = mock(StatementNode.class);
        subject.addStatement(statementNode);
        List<Node> statementNodes = subject.getChildren();
        assertThat(statementNodes, notNullValue());
        assertThat(statementNodes, hasSize(1));
        assertThat(statementNodes, hasItem(statementNode));
    }
}