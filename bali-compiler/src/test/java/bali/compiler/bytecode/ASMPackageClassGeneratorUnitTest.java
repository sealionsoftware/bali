package bali.compiler.bytecode;

import bali.Boolean;
import bali.CharArrayString;
import bali.IdentityBoolean;
import bali.Number;
import bali.String;
import bali.Value;
import bali.collection.Array;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.BooleanLiteralExpressionNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ConstantNode;
import bali.compiler.parser.tree.ConstructionExpressionNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.ListLiteralExpressionNode;
import bali.compiler.parser.tree.NumberLiteralExpressionNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.parser.tree.StringLiteralExpressionNode;
import bali.compiler.type.TypeLibrary;
import bali.number.Byte;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import static bali.number.NumberFactory.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 08/05/13
 */
public class ASMPackageClassGeneratorUnitTest {

	private static ASMPackageClassGenerator generator = new ASMPackageClassGenerator(new TypeLibrary());

	private CompilationUnitNode unit;

	@Before
	public void setUp() {
		unit = new CompilationUnitNode(0, 0);
		unit.setName("bali.test");
	}

	@Test
	public void testNumberConstant() throws Exception {
		NumberLiteralExpressionNode nlv = new NumberLiteralExpressionNode(0, 0);
		nlv.setSerialization("1");
		nlv.setType(new TestVanillaSite(Number.class));
		testGenerateConstant(Number.class, nlv, new Byte((byte) 1));
	}

	@Test
	public void testStringConstant() throws Exception {
		StringLiteralExpressionNode slv = new StringLiteralExpressionNode();
		slv.setSerialization("Hello World");
		slv.setType(new TestVanillaSite((String.class)));
		testGenerateConstant(String.class, slv, new CharArrayString("Hello World".toCharArray()));
	}

	@Test
	public void testBooleanConstant() throws Exception {
		BooleanLiteralExpressionNode blv = new BooleanLiteralExpressionNode();
		blv.setSerialization("true");
		blv.setType(new TestVanillaSite((Boolean.class)));
		testGenerateConstant(Boolean.class, blv, IdentityBoolean.TRUE);
	}

	@Test
	public void testListConstant() throws Exception {

		NumberLiteralExpressionNode one = new NumberLiteralExpressionNode(0, 0);
		one.setSerialization("1");
		NumberLiteralExpressionNode two = new NumberLiteralExpressionNode(0, 0);
		two.setSerialization("2");
		NumberLiteralExpressionNode three = new NumberLiteralExpressionNode(0, 0);
		three.setSerialization("3");

		ListLiteralExpressionNode llv = new ListLiteralExpressionNode(0, 0);
		llv.addValue(one);
		llv.addValue(two);
		llv.addValue(three);

		llv.setType(new TestVanillaSite((Array.class)));

		testGenerateConstant(Array.class, llv, new Array<>(new Object[]{
				NUMBER_FACTORY.forDecimalString("1".toCharArray()),
				NUMBER_FACTORY.forDecimalString("2".toCharArray()),
				NUMBER_FACTORY.forDecimalString("3".toCharArray())
		}));
	}

	@Test
	public void testNewObjectConstant() throws Exception {
		SiteNode type = new SiteNode(0, 0);
		type.setSite(new TestVanillaSite(Instantiatable.class));
		ConstructionExpressionNode cv = new ConstructionExpressionNode(0, 0);
		cv.setType(new TestVanillaSite(Instantiatable.class));
		testGenerateConstant(Instantiatable.class, cv, new Instantiatable());
	}

	public <T extends Value<T>> void testGenerateConstant(java.lang.Class<T> clazz, ExpressionNode value, T expectation) throws Exception {

		SiteNode type = new SiteNode();
		type.setSite(new TestVanillaSite(clazz));

		ConstantNode constant = new ConstantNode();
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
