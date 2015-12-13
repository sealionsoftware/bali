package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.Control;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import org.junit.Test;

import static com.sealionsoftware.bali.compiler.Matchers.containsNoFailures;
import static com.sealionsoftware.bali.compiler.Matchers.containsOneFailure;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReferenceMatchingVisitorTest {

    private ReferenceMatchingVisitor subject = new ReferenceMatchingVisitor();

    @Test
    public void testVisitCodeBlockNode() throws Exception {

        CodeBlockNode node = new CodeBlockNode(2, 3);

        Control mockControl = mock(Control.class);
        subject.visit(node, mockControl);

        verify(mockControl).visitChildren();
        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitVariableNode() throws Exception {

        VariableNode node = new VariableNode(2, 3);

        Control mockControl = mock(Control.class);
        subject.visit(node, mockControl);

        verify(mockControl).visitChildren();
        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitResolvedReferenceNode() throws Exception {

        CompilationThreadManager mockMonitor = mock(CompilationThreadManager.class);
        TypeNode typeNode = mock(TypeNode.class);
        typeNode.setResolvedType(mock(Type.class));

        VariableNode variableNode = new VariableNode(2, 3);
        variableNode.setName("aVariable");
        variableNode.setType(typeNode);
        ReferenceNode node = new ReferenceNode(mockMonitor, 4, 5);
        node.setName("aVariable");

        Control mockControl = mock(Control.class);
        subject.visit(variableNode, mockControl);
        subject.visit(node, mockControl);

        assertThat(subject, containsNoFailures());
    }

    @Test
    public void testVisitUnresolvedReferenceNode() throws Exception {

        CompilationThreadManager mockMonitor = mock(CompilationThreadManager.class);
        ReferenceNode node = new ReferenceNode(mockMonitor, 2, 3);
        node.setName("aVariable");

        Control mockControl = mock(Control.class);
        subject.visit(node, mockControl);

        assertThat(subject, containsOneFailure(ErrorCode.CANNOT_RESOLVE_REFERENCE));
    }

}