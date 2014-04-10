package bali.compiler.bytecode;

import bali.Number;
import bali.compiler.GeneratedClass;
import bali.compiler.parser.tree.CodeBlockNode;
import bali.compiler.parser.tree.FieldNode;
import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.NumberLiteralExpressionNode;
import bali.compiler.parser.tree.ObjectNode;
import bali.compiler.parser.tree.ParameterNode;
import bali.compiler.parser.tree.ReturnStatementNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.type.Site;
import bali.logic.True;
import bali.number.Byte;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Modifier;

/**
 * TODO: tests for constructors
 * <p/>
 * User: Richard
 * Date: 08/05/13
 */
public class ASMClassGeneratorUnitTest {

	private static ASMClassGenerator generator = new ASMClassGenerator();

	private ObjectNode clazz;

	@Before
	public void setUp() {
		clazz = new ObjectNode();
		clazz.setSourceFile("bali.test.bali");
		clazz.setQualifiedClassName("bali.test.AClass");
	}

	@Test
	public void testGenerateEmptyClass() throws Exception {

		java.lang.Class loadedClass = build();

		Assert.assertEquals("Number of fields", 0, loadedClass.getFields().length);
		Assert.assertEquals("Number of methods", 0, loadedClass.getDeclaredMethods().length);
		Assert.assertEquals("Number of constructors", 1, loadedClass.getConstructors().length);
		Assert.assertEquals("Number of interfaces", 0, loadedClass.getInterfaces().length);

	}

	@Test
	public void testGenerateClassWithInternalField() throws Exception {

		SiteNode number = new SiteNode();
		number.setSite(new TestSite(Number.class));

		NumberLiteralExpressionNode value = new NumberLiteralExpressionNode();
		value.setSerialization("1");
		value.setType(new TestSite(Number.class));

		FieldNode declaration = new FieldNode();
		declaration.setName("aField");
		declaration.setType(number);
		declaration.setValue(value);

		clazz.addField(declaration);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Field declaredField = loadedClass.getDeclaredField("aField");

		Assert.assertEquals("Number of fields", 1, loadedClass.getDeclaredFields().length);
		Assert.assertTrue("Is Private", Modifier.isPrivate(declaredField.getModifiers()));
		Assert.assertEquals("Class", Number.class, declaredField.getType());

		Object instance = loadedClass.newInstance();
		declaredField.setAccessible(true);

		Assert.assertTrue("Value", new Byte((byte) 1).equalTo((Number) declaredField.get(instance)) == True.TRUE);
	}

	@Test
	public void testGenerateClassWithVoidDeclaration() throws Exception {

		CodeBlockNode codeBlock = new CodeBlockNode();
		codeBlock.addStatement(new ReturnStatementNode());

		MethodDeclarationNode declaration = new MethodDeclarationNode();
		declaration.setName("aMethod");
		declaration.setBody(codeBlock);

		clazz.addMethod(declaration);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Method declaredMethod = loadedClass.getDeclaredMethod("aMethod", new java.lang.Class[]{});

		Assert.assertEquals("Number of methods", 1, loadedClass.getDeclaredMethods().length);
		Assert.assertTrue("Is Private", Modifier.isPrivate(declaredMethod.getModifiers()));
		Assert.assertEquals("Return Class", void.class, declaredMethod.getReturnType());
	}

	@Test
	public void testGenerateClassWithNumberReturnDeclaration() throws Exception {

		SiteNode type = new SiteNode();
		type.setClassName("Number");
		type.setSite(new TestSite(Number.class));

		NumberLiteralExpressionNode nlv = new NumberLiteralExpressionNode();
		nlv.setSerialization("1");

		ReturnStatementNode ret = new ReturnStatementNode();
		ret.setValue(nlv);

		CodeBlockNode codeBlock = new CodeBlockNode();
		codeBlock.addStatement(ret);

		MethodDeclarationNode declaration = new MethodDeclarationNode();
		declaration.setName("aMethod");
		declaration.setType(type);
		declaration.setBody(codeBlock);

		clazz.addMethod(declaration);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Method declaredMethod = loadedClass.getDeclaredMethod("aMethod", new java.lang.Class[]{});

		Assert.assertEquals("Number of methods", 1, loadedClass.getDeclaredMethods().length);
		Assert.assertEquals("Return Class", Number.class, declaredMethod.getReturnType());
	}

	@Test
	public void testGenerateClassWithNumberParamDeclaration() throws Exception {

		SiteNode type = new SiteNode();
		type.setClassName("Number");
		type.setSite(new TestSite(Number.class));

		ParameterNode argument = new ParameterNode();
		argument.setType(type);
		argument.setName("anArgument");

		CodeBlockNode codeBlock = new CodeBlockNode();
		codeBlock.addStatement(new ReturnStatementNode());

		MethodDeclarationNode declaration = new MethodDeclarationNode();
		declaration.setName("aMethod");
		declaration.addParameter(argument);
		declaration.setBody(codeBlock);

		clazz.addMethod(declaration);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Method declaredMethod = loadedClass.getDeclaredMethod("aMethod", new java.lang.Class[]{Number.class});

		Assert.assertEquals("Number of methods", 1, loadedClass.getDeclaredMethods().length);
		Assert.assertEquals("Return Class", void.class, declaredMethod.getReturnType());
	}

	@Test
	public void testGenerateImplementation() throws Exception {

		SiteNode type = new SiteNode();
		type.setClassName("Number");
		type.setSite(new TestSite(Number.class));

		Site ifaceSite = new TestSite(ASuperInterface.class);

		ParameterNode argument = new ParameterNode();
		argument.setType(type);
		argument.setName("anArgument");

		NumberLiteralExpressionNode nlen = new NumberLiteralExpressionNode();
		nlen.setSerialization("1");
		nlen.setType(new TestSite(bali.Integer.class));
		ReturnStatementNode rsn = new ReturnStatementNode();
		rsn.setValue(nlen);
		CodeBlockNode codeBlock = new CodeBlockNode();
		codeBlock.addStatement(rsn);

		MethodDeclarationNode declaration = new MethodDeclarationNode();
		declaration.setName("aMethod");
		declaration.addParameter(argument);
		declaration.setBody(codeBlock);
		declaration.setDeclared(ifaceSite.getMethods().get(0));

		SiteNode ifaceType = new SiteNode();
		InterfaceNode iface = new InterfaceNode();
		iface.setQualifiedClassName("bali.compiler.bytecode.ASuperInterface");
		iface.addMethod(declaration);
		ifaceType.setSite(ifaceSite);

		clazz.addMethod(declaration);
		clazz.addImplementation(ifaceType);

		java.lang.Class loadedClass = build();
		java.lang.reflect.Method declaredMethod = loadedClass.getDeclaredMethod("aMethod", new java.lang.Class[]{bali.Integer.class});

		Assert.assertEquals("Number of interfaces", 1, loadedClass.getInterfaces().length);
		Assert.assertEquals("Is Method implementation public?", true, Modifier.isPublic(declaredMethod.getModifiers()));

	}

	private java.lang.Class build() throws Exception {
		GeneratedClass generated = generator.build(clazz);
		return new ByteArrayClassLoader(generated.getCode()).findClass("bali.test.AClass");
	}


}