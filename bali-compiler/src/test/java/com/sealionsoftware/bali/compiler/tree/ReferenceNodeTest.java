package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.assembly.VariableData;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReferenceNodeTest {

	private CompilationThreadManager mockMonitor = mock(CompilationThreadManager.class);
	private ReferenceNode subject = new ReferenceNode(2, 3, mockMonitor);
	
	@Test
	public void testGetName() {
		String referenceName = "aVariable";
		subject.setName(referenceName);
		assertThat(subject.getName(), equalTo(referenceName));
	}

	@Test
	public void testGetVariableData() {
		VariableData variableData = new VariableData(
				"aVariable", mock(Site.class), UUID.randomUUID()
		);
		subject.setReferenceData(variableData);
		assertThat(subject.getReferenceData(), is(variableData));
	}

	@Test
	public void testGetLine() {
		assertThat(subject.getLine(), equalTo(2));
	}

	@Test
	public void testGetCharacter() {
		assertThat(subject.getCharacter(), equalTo(3));
	}

	@Test
	public void testGetChildren() {
		assertThat(subject.getChildren(), is(empty()));
	}

    @Test
    public void testGetType() {
        Site mockType = mock(Site.class);
        VariableData variableData = new VariableData(
                "aVariable", mockType, UUID.randomUUID()
        );
        subject.setReferenceData(variableData);
        assertThat(subject.getSite(), is(mockType));
    }

	@Test
	public void testAccept() {
		Visitor visitor = mock(Visitor.class);
		subject.accept(visitor);
		verify(visitor).visit(same(subject));
	}

	@Test
	public void testToString() {
		String referenceName = "aVariable";
		subject.setName(referenceName);
		assertThat(subject.toString(), equalTo(referenceName));
	}
}