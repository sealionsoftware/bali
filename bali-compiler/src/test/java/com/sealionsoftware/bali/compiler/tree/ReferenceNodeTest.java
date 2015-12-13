package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.assembly.VariableData;
import org.junit.Test;

import java.util.UUID;

import static com.sealionsoftware.Matchers.isEmpty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReferenceNodeTest {

	private CompilationThreadManager mockMonitor = mock(CompilationThreadManager.class);
	private ReferenceNode subject = new ReferenceNode(mockMonitor, 2, 3);
	
	@Test
	public void testGetName() throws Exception {
		String referenceName = "aVariable";
		subject.setName(referenceName);
		assertThat(subject.getName(), equalTo(referenceName));
	}

	@Test
	public void testGetVariableData() throws Exception {
		VariableData variableData = new VariableData(
				"aVariable", mock(Type.class), UUID.randomUUID()
		);
		subject.setVariableData(variableData);
		assertThat(subject.getVariableData(), is(variableData));
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
		assertThat(subject.getChildren(), isEmpty());
	}

	@Test
	public void testAccept() throws Exception {
		Visitor visitor = mock(Visitor.class);
		subject.accept(visitor);
		verify(visitor).visit(same(subject), isA(Control.class));
	}
}