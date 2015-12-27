package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.Class;
import com.sealionsoftware.bali.compiler.ClassBasedType;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import org.junit.Test;

import java.util.Map;

import static com.sealionsoftware.Constant.map;
import static com.sealionsoftware.Constant.put;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TypeAssigningVisitorTest {

    private Class booleanMock = mock(Class.class);
    private Class textMock = mock(Class.class);
    private Map<String, Class> library = map(
            put("bali.Boolean", booleanMock),
            put("bali.Text", textMock)
    );
    private TypeAssigningVisitor subject = new TypeAssigningVisitor(library);

    @Test
    public void testVisitBooleanNode() throws Exception {
        BooleanLiteralNode node = mock(BooleanLiteralNode.class);
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
    public void testVisitTypeNode() throws Exception {
        TypeNode node = mock(TypeNode.class);
        when(node.getName()).thenReturn("Boolean");
        subject.visit(node);
        verify(node).setResolvedType(any(ClassBasedType.class));
    }
}