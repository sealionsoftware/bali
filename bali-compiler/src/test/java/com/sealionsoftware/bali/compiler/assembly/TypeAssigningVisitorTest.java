package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.tree.ArrayLiteralNode;
import com.sealionsoftware.bali.compiler.tree.IntegerLiteralNode;
import com.sealionsoftware.bali.compiler.tree.LogicLiteralNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.type.Class;
import com.sealionsoftware.bali.compiler.type.ClassBasedType;
import org.junit.Test;

import java.util.Map;

import static com.sealionsoftware.Constant.map;
import static com.sealionsoftware.Constant.put;
import static com.sealionsoftware.bali.compiler.Matchers.containsOneFailure;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TypeAssigningVisitorTest {

    private Class booleanMock = mock(Class.class);
    private Class textMock = mock(Class.class);
    private Class integerMock = mock(Class.class);
    private Map<String, Class> library = map(
            put("bali.Boolean", booleanMock),
            put("bali.Text", textMock),
            put("bali.Integer", integerMock)
    );
    private TypeAssigningVisitor subject = new TypeAssigningVisitor(library);

    @Test
    public void testVisitBooleanNode() throws Exception {
        LogicLiteralNode node = mock(LogicLiteralNode.class);
        subject.visit(node);
        verify(node).setType(any(ClassBasedType.class));
    }

    @Test
    public void testVisitTextNode() throws Exception {
        TextLiteralNode node = mock(TextLiteralNode.class);
        subject.visit(node);
        verify(node).setType(any(ClassBasedType.class));
    }

    @Test
    public void testVisitIntegerNode() throws Exception {
        IntegerLiteralNode node = mock(IntegerLiteralNode.class);
        subject.visit(node);
        verify(node).setType(any(ClassBasedType.class));
    }

    @Test
    public void testVisitArrayNode() throws Exception {
        ArrayLiteralNode node = mock(ArrayLiteralNode.class);
        subject.visit(node);
        verify(node).setType(any(ClassBasedType.class));
    }

    @Test
    public void testVisitTypeNode() throws Exception {
        TypeNode node = mock(TypeNode.class);
        when(node.getName()).thenReturn("Boolean");
        subject.visit(node);
        verify(node).setResolvedType(any(ClassBasedType.class));
    }

    @Test
    public void testVisitUnknownNode() throws Exception {
        TypeNode node = mock(TypeNode.class);
        when(node.getName()).thenReturn("Foo");
        subject.visit(node);
        assertThat(subject,  containsOneFailure(ErrorCode.UNKNOWN_TYPE));
    }
}