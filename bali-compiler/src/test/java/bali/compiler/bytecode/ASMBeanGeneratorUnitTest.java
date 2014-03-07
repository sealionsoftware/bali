package bali.compiler.bytecode;

import bali.Number;
import bali.collection.Map;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.BeanNode;
import bali.compiler.parser.tree.PropertyNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.type.ParameterisedSite;
import bali.compiler.type.Site;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * User: Richard
 * Date: 08/05/13
 */
public class ASMBeanGeneratorUnitTest {

	private static Generator<BeanNode, GeneratedClass> generator = new ASMBeanGenerator();
	private BeanNode bean;

	@Before
	public void setUp() {
		bean = new BeanNode();
		bean.setQualifiedClassName("bali.test.ABean");
	}

	@Test
	public void testEmptyBean() throws Exception {

		Class loadedClass = build();
		Assert.assertEquals("Number of public fields", 0, loadedClass.getFields().length);
	}

	@Test
	public void testSimpleBean() throws Exception {

		SiteNode type = new SiteNode();
		type.setSite(new TestSite(Number.class));
		PropertyNode propertyNode = new PropertyNode();
		propertyNode.setName("aProperty");
		propertyNode.setType(type);

		bean.addProperty(propertyNode);

		Class loadedClass = build();
		Field[] publicFields = loadedClass.getFields();

		Assert.assertEquals("Number of public fields", 1, publicFields.length);

		Assert.assertEquals("Field Name", "aProperty", publicFields[0].getName());
		Assert.assertEquals("Field Class", Number.class, publicFields[0].getType());

	}

	@Test
	public void testExtendedBean() throws Exception {

		SiteNode type = new SiteNode();
		type.setSite(new TestSite(Number.class));
		PropertyNode propertyNode = new PropertyNode();
		propertyNode.setName("aProperty");
		propertyNode.setType(type);

		bean.addProperty(propertyNode);

		SiteNode superBean = new SiteNode();
		superBean.setSite(new TestSite(ASuperBean.class));
		bean.setSuperType(superBean);

		Class loadedClass = build();
		Field[] fields = loadedClass.getFields();

		Assert.assertEquals("Number of public fields", 2, fields.length);
	}

	@Test
	public void testBeanWithParametrizedProperty() throws Exception {

		SiteNode type = new SiteNode();
		type.setSite(new TestSite(Map.class, Arrays.<Site>asList(new TestSite(String.class), new TestSite(Number.class))));

		PropertyNode propertyNode = new PropertyNode();
		propertyNode.setName("aProperty");
		propertyNode.setType(type);

		bean.addProperty(propertyNode);

		Class loadedClass = build();
		Field[] fields = loadedClass.getFields();

		Assert.assertEquals("Number of public fields", 1, fields.length);
		Field field = fields[0];
		Type genericType = field.getGenericType();
		Assert.assertTrue("Field is a Parametrized Type", genericType instanceof ParameterizedType);
		ParameterizedType parameterizedType = (ParameterizedType) genericType;
		Type[] typeArguments = parameterizedType.getActualTypeArguments();
		Assert.assertEquals("Number of type arguments", 2, typeArguments.length);
		Assert.assertEquals("Type argument 0 is a String", String.class, typeArguments[0]);
		Assert.assertEquals("Type argument 1 is a String", Number.class, typeArguments[1]);

	}

	private Class build() throws Exception {
		GeneratedClass generated = generator.build(bean);
		return new ByteArrayClassLoader(generated.getCode()).findClass("bali.test.ABean");
	}

}
