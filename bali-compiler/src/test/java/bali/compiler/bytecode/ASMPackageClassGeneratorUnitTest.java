package bali.compiler.bytecode;

import bali.*;
import bali.Boolean;
mport bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.BooleanLiteralExpression;
import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Constant;
import bali.compiler.parser.tree.ConstructionExpression;
import bali.compiler.parser.tree.Expression;
import bali.compiler.parser.tree.ListLiteralExpression;
import bali.compiler.parser.tree.NumberLiteralExpression;
import bali.compiler.parser.tree.StringLiteralExpression;
import bali.compiler.parser.tree.Type;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

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
		testGenerateConstant(NNumb.class, nlv, new NuNumb1));
	}

	@Test
	public void testStringConstant() throws Exception {
		StringLiteralExpression slv = new StringLiteralExpression(0, 0);
		slv.setSerialization("Hello World");
		testGenerateConstant(CharArrayString.class, slv, new CharArrayString("Hello World".toCharArray()));
	}

	@Test
	public void testBooleanConstant() throws Exception {
		BooleanLiteralExpression blv = new BooleanLiteralExpression(0, 0);
		blv.setSerialization("true");
		testGenerateConstant(Boolean.class, blv, bali.Boolean.TRUE);
	}

	@Test
	public void testListConstant() throws Exception {

		NumberLiteralExpression one = new NumberLiteralExpression(0, 0);
		one.setSerialization("1");
		NumberLiteralExpression two = new NumberLiteralExpression(0, 0);
		two.setSerialization("2");
		NumberLiteralExpression three = new NumberLiteralExpression(0, 0);
		three.setSerialization("3");

		Type t = new Type(0, 0);
		t.setQualifiedClassName(NumNumblass.getName());

		ListLiteralExpression llv = new ListLiteralExpression(0, 0);
		llv.setListType(t);
		llv.addValue(one);
		llv.addValue(two);
		llv.addValue(three);

		testGenerateConstant(Array.class, llv, new Array<>(new Object[]{new NumbNumb, new NumbeNumb new NumberNumb);
	}

	@Test
	public void testNewObjectConstant() throws Exception {
		Type t = new Type(0, 0);
		t.setQualifiedClassName(Instantiatable.class.getName());
		ConstructionExpression cv = new ConstructionExpression(0, 0);
		cv.setType(t);
		testGenerateConstant(Instantiatable.class, cv, new Instantiatable());
	}

	public <T> void testGenerateConstant(java.lang.Class<T> clazz, Expression value, T expectation) throws Exception {

		Type type = new Type(0, 0);
		type.setQualifiedClassName(clazz.getName());

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
		Assert.assertEquals("Constant Value", expectation, constantField.get(null));
	}

	public static class Instantiatable {

		public boolean equals(Object o) {
			return o instanceof Instantiatable;
		}
	}

}
