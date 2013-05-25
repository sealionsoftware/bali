package bali.compiler.bytecode;

import bali.Boolean;
import bali.List;
import bali.Number;
import bali.String;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.BooleanLiteralValue;
import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Constant;
import bali.compiler.parser.tree.ConstructionValue;
import bali.compiler.parser.tree.ListLiteralValue;
import bali.compiler.parser.tree.NumberLiteralValue;
import bali.compiler.parser.tree.StringLiteralValue;
import bali.compiler.parser.tree.Type;
import bali.compiler.parser.tree.Value;
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
	public void setUp(){
		unit = new CompilationUnit(0,0);
		unit.setName("bali.test");
	}

	@Test
	public void testNumberConstant() throws Exception {
		NumberLiteralValue nlv = new NumberLiteralValue(0,0);
		nlv.setSerialization("1");
		testGenerateConstant(Number.class, nlv, new Number(1));
	}

	@Test
	public void testStringConstant() throws Exception {
		StringLiteralValue slv = new StringLiteralValue(0,0);
		slv.setSerialization("Hello World");
		testGenerateConstant(String.class, slv, new String("Hello World".toCharArray()));
	}

	@Test
	public void testBooleanConstant() throws Exception {
		BooleanLiteralValue blv = new BooleanLiteralValue(0,0);
		blv.setSerialization("true");
		testGenerateConstant(Boolean.class, blv, Boolean.TRUE);
	}

	@Test
	public void testListConstant() throws Exception {

		NumberLiteralValue one = new NumberLiteralValue(0,0);
		one.setSerialization("1");
		NumberLiteralValue two = new NumberLiteralValue(0,0);
		two.setSerialization("2");
		NumberLiteralValue three = new NumberLiteralValue(0,0);
		three.setSerialization("3");

		ListLiteralValue llv = new ListLiteralValue(0,0);
		llv.addValue(one);
		llv.addValue(two);
		llv.addValue(three);

		testGenerateConstant(List.class, llv, new List<>(new Object[]{new Number(1), new Number(2), new Number(3)}));
	}

	@Test
	public void testNewObjectConstant() throws Exception {
		Type t = new Type(0,0);
		t.setQualifiedClassName(Instantiatable.class.getName());
		ConstructionValue cv = new ConstructionValue(0,0);
		cv.setType(t);
		testGenerateConstant(Instantiatable.class, cv, new Instantiatable());
	}

	public <T> void testGenerateConstant(java.lang.Class<T> clazz, Value value, T expectation) throws Exception {

		Type type = new Type(0,0);
		type.setQualifiedClassName(clazz.getName());

		Constant constant = new Constant(0,0);
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

		public boolean equals(Object o){
			return o instanceof Instantiatable;
		}
	}

}
