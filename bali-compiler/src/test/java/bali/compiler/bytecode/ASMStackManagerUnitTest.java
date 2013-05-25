package bali.compiler.bytecode;

import bali.compiler.parser.tree.NumberLiteralValue;
import bali.compiler.parser.tree.Return;
import bali.compiler.parser.tree.Statement;
import bali.compiler.parser.tree.Type;
import bali.compiler.parser.tree.Variable;
import org.junit.Test;
import org.mockito.Mockito;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ASMStackManagerUnitTest implements Opcodes {

	private ASMStackManager manager = new ASMStackManager(new ASMConverter());
	private MethodVisitor v = Mockito.mock(MethodVisitor.class);

	@Test
	public void testExecuteReturnVoidStatement() {
		Statement statement = new Return(0, 0);

		manager.execute(statement, v);

		Mockito.verify(v).visitInsn(RETURN);
//		Assert.assertEquals("Max Depth", 0, manager.getMaxDepth());
	}

	@Test
	public void testExecuteReturnValueStatement() {

		NumberLiteralValue value = new NumberLiteralValue(0, 0);
		value.setSerialization("0");

		Return statement = new Return(0, 0);
		statement.setValue(value);

		manager.execute(statement, v);

		MethodVisitor verifier = Mockito.verify(v);
		verifier.visitTypeInsn(NEW, "bali/Number");
		verifier.visitInsn(DUP);
		verifier.visitInsn(ICONST_0);
		verifier.visitMethodInsn(INVOKESPECIAL, "bali/Number", "<init>", "(I)V");
		verifier.visitInsn(ARETURN);

//		Assert.assertEquals("Max Depth", 3, manager.getMaxDepth());
	}

//	@Test
//	public void testExecuteInvocationStatement(){
//
//		NumberLiteralValue value = new NumberLiteralValue(0, 0);
//		value.setSerialization("0");
//
//		Return statement = new Return(0,0);
//		statement.setValue(value);
//
//		manager.execute(statement, v);
//
//		MethodVisitor verifier = Mockito.verify(v);
//
//		verifier.visitTypeInsn(NEW, "bali/Number");
//		verifier.visitInsn(DUP);
//		verifier.visitInsn(ICONST_0);
//		verifier.visitMethodInsn(INVOKESPECIAL, "bali/Number", "<init>", "(I)V");
//		verifier.visitInsn(ARETURN);
//
//		Assert.assertEquals("Max Depth", 3, manager.getMaxDepth());
//	}

	@Test
	public void testDeclareVariableStatement() {

		Type type = new Type(0, 0);
		type.setQualifiedClassName("bali/Number");

		NumberLiteralValue value = new NumberLiteralValue(0, 0);
		value.setSerialization("0");

		Variable statement = new Variable(0, 0);
		statement.setName("aVariable");
		statement.setType(type);
		statement.setValue(value);

		manager.execute(statement, v);

		MethodVisitor verifier = Mockito.verify(v);
		verifier.visitTypeInsn(NEW, "bali/Number");
		verifier.visitInsn(DUP);
		verifier.visitInsn(ICONST_0);
		verifier.visitMethodInsn(INVOKESPECIAL, "bali/Number", "<init>", "(I)V");
		verifier.visitVarInsn(ASTORE, 1);

//		Assert.assertEquals("Max Depth", 3, manager.getMaxDepth());
	}

}
