package bali.compiler.bytecode;

import bali.Number;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.CodeBlock;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.Field;
import bali.compiler.parser.tree.Interface;
import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.Class;
import bali.compiler.parser.tree.NumberLiteralValue;
import bali.compiler.parser.tree.Return;
import bali.compiler.parser.tree.Type;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Modifier;

/**
 * TODO: tests for constructors
 *
* User: Richard
* Date: 08/05/13
*/
public class ASMClassGeneratorUnitTest {

	private static ASMClassGenerator generator = new ASMClassGenerator();

	private Class clazz;

	@Before
	public void setUp(){
		clazz = new Class();
		clazz.setQualifiedClassName("bali.test.AClass");
	}

	@Test
	public void testGenerateEmptyClass() throws Exception {

		java.lang.Class loadedClass = build();

		Assert.assertEquals("Number of fields", 0, loadedClass.getFields().length);
		Assert.assertEquals("Number of methods",0, loadedClass.getDeclaredMethods().length);
		Assert.assertEquals("Number of constructors", 1, loadedClass.getConstructors().length);
		Assert.assertEquals("Number of interfaces", 0, loadedClass.getInterfaces().length);

	}

	@Test
	public void testGenerateClassWithInternalField() throws Exception {

		Type number = new Type();
		number.setQualifiedClassName("bali.Number");

		NumberLiteralValue value = new NumberLiteralValue();
		value.setSerialization("1");

		Field declaration = new Field();
		declaration.setName("aField");
		declaration.setType(number);
		declaration.setValue(value);

		clazz.addField(declaration);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Field declaredField = loadedClass.getDeclaredField("aField");

		Assert.assertEquals("Number of fields", 1, loadedClass.getDeclaredFields().length);
		Assert.assertTrue("Is Private", Modifier.isPrivate(declaredField.getModifiers()));
		Assert.assertEquals("Type", Number.class, declaredField.getType());

		Object instance = loadedClass.newInstance();
		declaredField.setAccessible(true);

		Assert.assertEquals("Value", new Number(1), declaredField.get(instance));
	}

	@Test
	public void testGenerateClassWithVoidDeclaration() throws Exception {

		CodeBlock codeBlock = new CodeBlock();
		codeBlock.addStatement(new Return());

		Method declaration = new Method();
		declaration.setName("aMethod");
		declaration.setBody(codeBlock);

		clazz.addMethod(declaration);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Method declaredMethod = loadedClass.getDeclaredMethod("aMethod", new java.lang.Class[]{});

		Assert.assertEquals("Number of methods", 1, loadedClass.getDeclaredMethods().length);
		Assert.assertTrue("Is Private", Modifier.isPrivate(declaredMethod.getModifiers()));
		Assert.assertEquals("Return Type", void.class, declaredMethod.getReturnType());
	}

	@Test
	public void testGenerateClassWithNumberReturnDeclaration() throws Exception {

		Type type = new Type();
		type.setClassName("Number");
		type.setQualifiedClassName("bali.Number");

		NumberLiteralValue nlv = new NumberLiteralValue();
		nlv.setSerialization("1");

		Return ret = new Return();
		ret.setValue(nlv);

		CodeBlock codeBlock = new CodeBlock();
		codeBlock.addStatement(ret);

		Method declaration = new Method();
		declaration.setName("aMethod");
		declaration.setType(type);
		declaration.setBody(codeBlock);

		clazz.addMethod(declaration);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Method declaredMethod = loadedClass.getDeclaredMethod("aMethod", new java.lang.Class[]{});

		Assert.assertEquals("Number of methods", 1, loadedClass.getDeclaredMethods().length);
		Assert.assertEquals("Return Type", Number.class, declaredMethod.getReturnType());
	}

	@Test
	public void testGenerateClassWithNumberParamDeclaration() throws Exception {

		Type type = new Type();
		type.setClassName("Number");
		type.setQualifiedClassName("bali.Number");

		Declaration argument = new Declaration();
		argument.setType(type);
		argument.setName("anArgument");

		CodeBlock codeBlock = new CodeBlock();
		codeBlock.addStatement(new Return());

		Method declaration = new Method();
		declaration.setName("aMethod");
		declaration.addArgument(argument);
		declaration.setBody(codeBlock);

		clazz.addMethod(declaration);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Method declaredMethod = loadedClass.getDeclaredMethod("aMethod", new java.lang.Class[]{Number.class});

		Assert.assertEquals("Number of methods", 1, loadedClass.getDeclaredMethods().length);
		Assert.assertEquals("Return Type", void.class, declaredMethod.getReturnType());
	}

	@Test
	public void testGenerateImplementation() throws Exception{

		Type type = new Type();
		type.setClassName("Number");
		type.setQualifiedClassName("bali.Number");

		Declaration argument = new Declaration();
		argument.setType(type);
		argument.setName("anArgument");

		CodeBlock codeBlock = new CodeBlock();
		codeBlock.addStatement(new Return());

		Method declaration = new Method();
		declaration.setName("aMethod");
		declaration.addArgument(argument);
		declaration.setBody(codeBlock);
		declaration.setDeclared(true);

		Interface iface = new Interface();
		iface.setQualifiedClassName("bali.compiler.bytecode.ASuperInterface");
		iface.addMethodDeclaration(declaration);

		clazz.addMethod(declaration);
		clazz.addInterface(iface);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Method declaredMethod = loadedClass.getDeclaredMethod("aMethod", new java.lang.Class[]{Number.class});

		Assert.assertEquals("Number of interfaces", 1, loadedClass.getInterfaces().length);
		Assert.assertEquals("Is Method implementation public?", true, Modifier.isPublic(declaredMethod.getModifiers()));

	}

	private java.lang.Class build() throws Exception{
		GeneratedClass generated = generator.build(clazz);
		return new ByteArrayClassLoader(generated.getCode()).findClass("bali.test.AClass");
	}



}