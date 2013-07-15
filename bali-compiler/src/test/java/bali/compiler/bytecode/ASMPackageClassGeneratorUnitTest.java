package bali.compiler.bytecode;

import bali.Boolean;
import com.sealionsoftware.bali.CharArrayString;
import com.sealionsoftware.bali.IdentityBoolean;
import com.sealionsoftware.bali.number.Byte;
import bali.Number;
import com.sealionsoftware.bali.collections.Array;
import bali.Value;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import static bali._.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 08/05/13
 */
public class ASMPackageClassGeneratorUnitTest {

	private static ASMPackageClassGenerator generator = new ASMPackageClassGenerator();

	private CompilationUnit unit;

	@Before
	public void setUp() {
		unit = new CompilationUnit(0, 0);
		unit.setName("bali.test");
	}

	@Test
	public void testNumberConstant() throws Exception {
		NumberLiteralExpression nlv = new NumberLiteralExpression(0, 0);
		nlv.setSerialization("1");
		nlv.getType().setDeclaration(new TestDeclaration(Number.class.getName()));
		testGenerateConstant(Number.class, nlv, new Byte((byte) 1));
	}

	@Test
	public void testStringConstant() throws Exception {
		StringLiteralExpression slv = new StringLiteralExpression(0, 0);
		slv.setSerialization("Hello World");
		slv.getType().setDeclaration(new TestDeclaration(CharArrayString.class.getName()));
		testGenerateConstant(CharArrayString.class, slv, new CharArrayString("Hello World".toCharArray()));
	}

	@Test
	public void testBooleanConstant() throws Exception {
		BooleanLiteralExpression blv = new BooleanLiteralExpression(0, 0);
		blv.setSerialization("true");
		blv.getType().setDeclaration(new TestDeclaration(IdentityBoolean.class.getName()));
		testGenerateConstant(IdentityBoolean.class, blv, IdentityBoolean.TRUE);
	}

	@Test
	public void testListConstant() throws Exception {

		NumberLiteralExpression one = new NumberLiteralExpression(0, 0);
		one.setSerialization("1");
		NumberLiteralExpression two = new NumberLiteralExpression(0, 0);
		two.setSerialization("2");
		NumberLiteralExpression three = new NumberLiteralExpression(0, 0);
		three.setSerialization("3");

		ListLiteralExpression llv = new ListLiteralExpression(0, 0);
		llv.addValue(one);
		llv.addValue(two);
		llv.addValue(three);

		llv.getType().setDeclaration(new TestDeclaration(Array.class.getName()));

		testGenerateConstant(Array.class, llv, new Array<>(new Object[]{
				NUMBER_FACTORY.forDecimalString("1".toCharArray()),
				NUMBER_FACTORY.forDecimalString("2".toCharArray()),
				NUMBER_FACTORY.forDecimalString("3".toCharArray())
		}));
	}

	@Test
	public void testNewObjectConstant() throws Exception {
		Type type = new Type(0, 0);
		type.setDeclaration(new TestDeclaration(Instantiatable.class.getName()));
		ConstructionExpression cv = new ConstructionExpression(0, 0);
		cv.setType(type);
		testGenerateConstant(Instantiatable.class, cv, new Instantiatable());
	}

	public <T extends Value<T>> void testGenerateConstant(java.lang.Class<T> clazz, Expression value, T expectation) throws Exception {

		Type type = new Type(0, 0);
		type.setDeclaration(new TestDeclaration(clazz.getName()));

		Constant constant = new Constant(0, 0);
		constant.setName("aConstant");
		constant.setValue(value);
		constant.setType(type);

		unit.addConstant(constant);
		GeneratedClass generated = generator.build(unit);
		java.lang.Class loadedClass = new ByteArrayClassLoader(generated.getCode()).loadClass(unit.getName() + "." + generated.getName());

		Assert.assertEquals("Number of fields", 1, loadedClass.getFields().length);
		Assert.assertEquals("Number of methods", 0, loadedClass.getDeclaredMethods().length);
		java.lang.reflect.Field constantField = loadedClass.getField("aConstant");
		Assert.assertEquals("Constant Type", clazz, constantField.getType());
		Assert.assertTrue("Constant Value", expectation.equalTo((T) constantField.get(null)) == IdentityBoolean.TRUE);
	}

	public static class Instantiatable implements Value<Instantiatable> {
		public Boolean equalTo(Instantiatable o) {
			return o instanceof Instantiatable ? IdentityBoolean.TRUE : IdentityBoolean.FALSE;
		}
	}

}
