package bali.compiler.bytecode;

import org.mockito.Mockito;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * TODO: This sucks
 * <p/>
 * User: Richard
 * Date: 14/05/13
 */
public class ASMStackManagerUnitTest implements Opcodes {

	private ASMStackManager manager = new ASMStackManager(new ASMConverter());
	private MethodVisitor v = Mockito.mock(MethodVisitor.class);

//	@Test
//	public void testExecuteReturnVoidStatement() {
//		Statement statement = new Return(0, 0);
//
//		manager.execute(statement, v);
//
//		Mockito.verify(v).visitInsn(RETURN);
////		Assert.assertEquals("Max Depth", 0, manager.getMaxDepth());
//	}
//
//	@Test
//	public void testExecuteReturnValueStatement() {
//
//		NumberLiteralExpression value = new NumberLiteralExpression(0, 0);
//		value.setSerialization("0");
//
//		Return statement = new Return(0, 0);
//		statement.setValue(value);
//
//		manager.execute(statement, v);
//
////		MethodVisitor verifier = Mockito.verify(v);
////		verifier.visitTypeInsn(NEW, "bali/Number");
////		verifier.visitInsn(DUP);
////		verifier.visitInsn(ICONST_0);
////		verifier.visitMethodInsn(INVOKESPECIAL, "bali/Number", "<init>", "(I)V");
////		verifier.visitInsn(ARETURN);
//
////		Assert.assertEquals("Max Depth", 3, manager.getMaxDepth());
//	}
//
////	@Test
////	public void testExecuteInvocationStatement(){
////
////		NumberLiteralValue value = new NumberLiteralValue(0, 0);
////		value.setSerialization("0");
////
////		Return statement = new Return(0,0);
////		statement.setValue(value);
////
////		manager.execute(statement, v);
////
////		MethodVisitor verifier = Mockito.verify(v);
////
////		verifier.visitTypeInsn(NEW, "bali/Number");
////		verifier.visitInsn(DUP);
////		verifier.visitInsn(ICONST_0);
////		verifier.visitMethodInsn(INVOKESPECIAL, "bali/Number", "<init>", "(I)V");
////		verifier.visitInsn(ARETURN);
////
////		Assert.assertEquals("Max Depth", 3, manager.getMaxDepth());
////	}
//
//	@Test
//	public void testDeclareVariableStatement() {
////
////		Type type = new Type(0, 0);
////		type.setQualifiedClassName("bali/Number");
////
////		NumberLiteralExpression value = new NumberLiteralExpression(0, 0);
////		value.setSerialization("0");
////
////		Reference ref = new Reference();
////		ref.setName("aVariable");
////
////		Variable statement = new Variable(0, 0);
////		statement.setReference(ref);
////		statement.setType(type);
////		statement.setValue(value);
////
////		manager.execute(statement, v);
//
////		MethodVisitor verifier = Mockito.verify(v);
////		verifier.visitTypeInsn(NEW, "bali/Number");
////		verifier.visitInsn(DUP);
////		verifier.visitInsn(ICONST_0);
////		verifier.visitMethodInsn(INVOKESPECIAL, "bali/Number", "<init>", "(I)V");
////		verifier.visitVarInsn(ASTORE, 1);
//
////		Assert.assertEquals("Max Depth", 3, manager.getMaxDepth());
//	}

}
