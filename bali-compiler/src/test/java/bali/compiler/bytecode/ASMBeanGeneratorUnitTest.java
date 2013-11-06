package bali.compiler.bytecode;

import bali.Number;
import bali.annotation.MetaType;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.BeanNode;
import bali.compiler.parser.tree.PropertyNode;
import bali.compiler.parser.tree.SiteNode;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
	public void testGenerateEmptyBean() throws Exception {

		Class loadedClass = build();
		Assert.assertEquals("Number of setters", 0, getSetters(loadedClass).size());
		Assert.assertEquals("Number of getters", 0, getGetters(loadedClass).size());

		//TODO: Need an ASM method to get compile time annotations
//		Assert.assertEquals("Has Bean MetaType Annotation", 0, loadedClass.getAnnotation(MetaType.class));

	}

	@Test
	public void testGenerateSimpleBean() throws Exception {

		SiteNode type = new SiteNode();
		type.setSite(new TestVanillaSite(Number.class));
		PropertyNode propertyNode = new PropertyNode();
		propertyNode.setName("aProperty");
		propertyNode.setType(type);

		bean.addProperty(propertyNode);

		Class loadedClass = build();
		List<Method> setters = getSetters(loadedClass);
		List<Method> getters = getGetters(loadedClass);

		Assert.assertEquals("Number of setters", 1, setters.size());
		Assert.assertEquals("Number of getters", 1, getters.size());

		Assert.assertEquals("Setter Name", "setAProperty", setters.get(0).getName());
		Assert.assertEquals("Setter Type", Number.class, setters.get(0).getParameterTypes()[0]);

		Assert.assertEquals("Getter Name", "getAProperty", getters.get(0).getName());
		Assert.assertEquals("Getter Type", Number.class, getters.get(0).getReturnType());

	}

	@Test
	public void testGenerateExtendedBean() throws Exception {

		SiteNode type = new SiteNode();
		type.setSite(new TestVanillaSite(Number.class));
		PropertyNode propertyNode = new PropertyNode();
		propertyNode.setName("aProperty");
		propertyNode.setType(type);

		bean.addProperty(propertyNode);

		SiteNode superBean = new SiteNode();
		superBean.setSite(new TestVanillaSite(ASuperBean.class));
		bean.setSuperType(superBean);

		Class loadedClass = build();
		List<Method> setters = getSetters(loadedClass);
		List<Method> getters = getGetters(loadedClass);

		Assert.assertEquals("Number of setters", 2, setters.size());
		Assert.assertEquals("Number of getters", 2, getters.size());
	}

//
//	@Test
//	public void testGenerateInterfaceExtension() throws Exception {
//
//		SiteNode type = new SiteNode();
//		type.setSite(new TestVanillaSite(ASuperInterface.class));
//
//		iface.addImplementation(type);
//
//		Class loadedClass = build();
//
//		Assert.assertEquals("# Super interfaces", 1, loadedClass.getInterfaces().length);
//		Assert.assertEquals("Superinterfaces name", "bali.compiler.bytecode.ASuperInterface", loadedClass.getInterfaces()[0].getName());
//
//	}
//
//	@Test
//	public void testGenerateInterfaceWithVoidDeclaration() throws Exception {
//
//		MethodNode declaration = new MethodNode();
//		declaration.setName("aMethod");
//
//		iface.addMethod(declaration);
//
//		Class loadedClass = build();
//		java.lang.reflect.Method declaredMethod = loadedClass.getMethod("aMethod", new Class[]{});
//
//		Assert.assertEquals("Number of methods", 1, loadedClass.getMethods().length);
//		Assert.assertEquals("Return Type", void.class, declaredMethod.getReturnType());
//	}
//
//	@Test
//	public void testGenerateInterfaceWithNumberReturnDeclaration() throws Exception {
//
//		SiteNode type = new SiteNode();
//		type.setClassName("Number");
//		type.setSite(new TestVanillaSite(Number.class));
//
//		MethodNode declaration = new MethodNode();
//		declaration.setName("aMethod");
//		declaration.setType(type);
//
//		iface.addMethod(declaration);
//
//		Class loadedClass = build();
//		java.lang.reflect.Method declaredMethod = loadedClass.getMethod("aMethod", new Class[]{});
//
//		Assert.assertEquals("Number of methods", 1, loadedClass.getMethods().length);
//		Assert.assertEquals("Return Type", Number.class, declaredMethod.getReturnType());
//	}
//
//	@Test
//	public void testGenerateInterfaceWithNumberParamDeclaration() throws Exception {
//
//		SiteNode type = new SiteNode();
//		type.setClassName("Number");
//		type.setSite(new TestVanillaSite(Number.class));
//
//		ArgumentDeclarationNode argument = new ArgumentDeclarationNode();
//		argument.setType(type);
//		argument.setName("anArgument");
//
//		MethodNode declaration = new MethodNode();
//		declaration.setName("aMethod");
//		declaration.addArgument(argument);
//
//		iface.addMethod(declaration);
//
//		Class loadedClass = build();
//		java.lang.reflect.Method declaredMethod = loadedClass.getMethod("aMethod", new Class[]{Number.class});
//
//		Assert.assertEquals("Number of methods", 1, loadedClass.getMethods().length);
//		Assert.assertEquals("Return Type", void.class, declaredMethod.getReturnType());
//	}

	private List<Method> getSetters(Class clazz){
		List<Method> ret = new ArrayList<>();
		Method[] methods = clazz.getMethods();
		for (Method method : methods){
			String name = method.getName();
			if (name.startsWith("set")
					&& name.length() > 3
					&& method.getReturnType() == void.class
					&& method.getParameterTypes().length == 1){
				ret.add(method);
			}
		}
		return ret;
	}

	private List<Method> getGetters(Class clazz){
		List<Method> ret = new ArrayList<>();
		Method[] methods = clazz.getMethods();
		for (Method method : methods){
			String name = method.getName();
			if (name.startsWith("get")
					&& name.length() > 3
					&& method.getParameterTypes().length == 0
					&& method.getReturnType() != void.class
					&& !name.equals("getClass")){
				ret.add(method);
			}
		}
		return ret;
	}

	private Class build() throws Exception {
		GeneratedClass generated = generator.build(bean);
		return new ByteArrayClassLoader(generated.getCode()).findClass("bali.test.ABean");
	}


}
