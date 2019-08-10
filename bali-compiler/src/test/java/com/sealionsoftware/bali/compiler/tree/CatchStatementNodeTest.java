package com.sealionsoftware.bali.compiler.tree;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CatchStatementNodeTest {

    private CatchStatementNode subject = new CatchStatementNode(2, 3);

    @Test
    public void testGetCoveredStatement() {
        StatementNode statementNode = mock(StatementNode.class);
        subject.setCoveredStatement(statementNode);
        assertThat(subject.getCoveredStatement(), Matchers.is(statementNode));
    }

    @Test
    public void testGetCatchStatement() {
        StatementNode statementNode = mock(StatementNode.class);
        subject.setCatchStatement(statementNode);
        assertThat(subject.getCatchStatement(), Matchers.is(statementNode));
    }

    @Test
    public void testGetCaughtName() {
        subject.setCaughtName("Exceptional");
        assertThat(subject.getCaughtName(), equalTo("Exceptional"));
    }

    @Test
    public void testGetCaughtType() {
        TypeNode typeNode = mock(TypeNode.class);
        subject.setCaughtType(typeNode);
        assertThat(subject.getCaughtType(), Matchers.is(typeNode));
    }

    @Test
    public void testGetLine(){
        assertThat(subject.getLine(), equalTo(2));
    }

    @Test
    public void testGetCharacter(){
        assertThat(subject.getCharacter(), equalTo(3));
    }

    @Test
    public void testGetChildren(){
        assertThat(subject.getChildren(), is(empty()));
    }

    @Test
    public void testAccept(){
        Visitor visitor = mock(Visitor.class);
        subject.accept(visitor);
        verify(visitor).visit(same(subject));
    }
}