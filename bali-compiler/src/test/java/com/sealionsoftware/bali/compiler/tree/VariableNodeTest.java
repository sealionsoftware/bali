package com.sealionsoftware.bali.compiler.tree;

import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class VariableNodeTest {

	private VariableNode subject = new VariableNode(2, 3);

	@Test
	public void testGetName(){
		subject.setName("aVariable");
		assertThat(subject.getName(), equalTo("aVariable"));
	}

	@Test
	public void testGetValue(){
		ExpressionNode value = mock(ExpressionNode.class);
		subject.setValue(value);
		assertThat(subject.getValue(), equalTo(value));
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

		TypeNode typeMock = mock(TypeNode.class);
		ExpressionNode expressionMock = mock(ExpressionNode.class);

		subject.setType(typeMock);
		subject.setValue(expressionMock);

		assertThat(subject.getChildren(), hasItems(typeMock, expressionMock));
	}

	@Test
	public void testAccept(){
		Visitor visitor = mock(Visitor.class);
		subject.accept(visitor);
		verify(visitor).visit(same(subject));
	}

	@Test
	public void testGetID(){
		assertThat(subject.getId(), instanceOf(UUID.class));
	}
}